package org.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.transformation.FilteredList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EspelhoPontoController implements Initializable {
    private final Database db = new Database();
    private Usuario usuarioLogado;
    private FilteredList<RegistroPonto> dadosFiltrados;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML private TableView<RegistroPonto> tabelaPonto;
    @FXML private Button Adicionar, Editar, Deletar;
    @FXML private DatePicker fim;
    @FXML private DatePicker inicio;
    @FXML private ComboBox status;
    @FXML private TextField Research;

    @FXML private TableColumn<RegistroPonto, Integer> colFuncID;
    @FXML private TableColumn<RegistroPonto, String> colNome, colData, colEntrada, colSaida, colEntrada2, colSaida2, colObs, colStatus;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colFuncID.setCellValueFactory(c -> c.getValue().funcionarioIDProperty().asObject());
        colNome.setCellValueFactory(c -> c.getValue().nomeFuncionarioProperty());
        colData.setCellValueFactory(c -> c.getValue().dataProperty());
        colEntrada.setCellValueFactory(c -> c.getValue().entradaProperty());
        colSaida.setCellValueFactory(c -> c.getValue().saidaProperty());
        colEntrada2.setCellValueFactory(c -> c.getValue().entrada2Property());
        colSaida2.setCellValueFactory(c -> c.getValue().saida2Property());
        colObs.setCellValueFactory(c -> c.getValue().observacaoProperty());
        colStatus.setCellValueFactory(c -> c.getValue().statusProperty());

        LocalDate hoje = LocalDate.now();
        inicio.setValue(hoje.withDayOfMonth(1));
        fim.setValue(hoje.withDayOfMonth(hoje.lengthOfMonth()));

        inicio.valueProperty().addListener((obs, antigo, novo) -> aplicarFiltro());
        fim.valueProperty().addListener((obs, antigo, novo) -> aplicarFiltro());

        status.getItems().addAll("TODOS", "COMPLETO", "FALTA", "INCONSISTENTE");
        status.setValue("TODOS");
        status.valueProperty().addListener((obs, antigo, novo) -> aplicarFiltro());
        Research.textProperty().addListener((observable, oldValue, newValue) -> aplicarFiltro());
        tabelaPonto.setRowFactory(tv -> {
            TableRow<RegistroPonto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Deletar(new ActionEvent());
                }
            });
            return row;
        });
    }

    public void setUsuario(Usuario user) {
        this.usuarioLogado = user;
        if (user != null) {
            dadosFiltrados = new FilteredList<>(db.getPontosPorPerfil(user), p -> true);
            tabelaPonto.setItems(dadosFiltrados);
            aplicarFiltro();

            boolean naoEhAdmin = !user.getFuncao().equalsIgnoreCase("ADMIN");
            Adicionar.setDisable(naoEhAdmin);
            Editar.setDisable(naoEhAdmin);
            Deletar.setDisable(naoEhAdmin);
        }
    }

    private void aplicarFiltro() {
        if (dadosFiltrados == null) return;

        dadosFiltrados.setPredicate(registro -> {
            String textoBusca = Research.getText();
            boolean bateComTexto = true;
            if (textoBusca != null && !textoBusca.trim().isEmpty()) {
                String buscaMinuscula = textoBusca.toLowerCase();

                bateComTexto = String.valueOf(registro.funcionarioIDProperty().get()).contains(buscaMinuscula)
                        || registro.nomeFuncionarioProperty().get().toLowerCase().contains(buscaMinuscula);
            }
            LocalDate dataInicio = inicio.getValue();
            LocalDate dataFim = fim.getValue();
            boolean dataBate = true;

            if (dataInicio != null && dataFim != null) {
                try {
                    LocalDate dataReg = LocalDate.parse(registro.getData(), formatter);
                    dataBate = !dataReg.isBefore(dataInicio) && !dataReg.isAfter(dataFim);
                } catch (Exception e) {
                    dataBate = true;
                }
            }

            String statusSelecionado = (String) status.getValue();
            boolean statusBate = true;

            if (statusSelecionado != null && !statusSelecionado.equals("TODOS")) {
                statusBate = registro.getStatus().equalsIgnoreCase(statusSelecionado);
            }
            return bateComTexto && dataBate && statusBate;
        });
    }


    @FXML
    private void Adicionar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/AdicionarEspelho.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/demo/Img/logo.png")));
            stage.setScene(new Scene(root));
            stage.showAndWait();
            Reload(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Editar(ActionEvent event){
        RegistroPonto selecionado = tabelaPonto.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            exibirAlerta("Atenção", "Por favor, clique em um funcionário na tabela para poder editá-lo!", Alert.AlertType.WARNING);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/EditarEspelho.fxml"));
            Parent root = loader.load();
            EditarEspelhoController controller = loader.getController();
            controller.setDados(selecionado);
            Stage stage = new Stage();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/demo/Img/logo.png")));
            stage.setScene(new Scene(root));
            stage.showAndWait();
            Reload(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Deletar(ActionEvent event) {
        RegistroPonto selecionado = tabelaPonto.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            exibirAlerta("Atenção", "Selecione um registro na tabela para excluir!", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Deseja realmente deletar o ponto do dia " + selecionado.getData() + "?");

        confirmacao.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.OK) {
                if (db.deletarPonto(selecionado.getId())) {
                    tabelaPonto.getItems().remove(selecionado);
                    exibirAlerta("Sucesso!", "Registro removido!", Alert.AlertType.INFORMATION);
                } else {
                    exibirAlerta("Erro", "Erro ao excluir do banco de dados.", Alert.AlertType.ERROR);
                }
            }
        });
    }

    private void exibirAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    private void Reload(ActionEvent event) {
        if (this.usuarioLogado != null) {
            ObservableList<RegistroPonto> dadosDoBanco = db.getPontosPorPerfil(this.usuarioLogado);
            dadosFiltrados = new FilteredList<>(dadosDoBanco, p -> true);
            tabelaPonto.setItems(dadosFiltrados);
            aplicarFiltro();
        }

    }
}
