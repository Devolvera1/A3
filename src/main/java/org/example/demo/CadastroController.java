package org.example.demo;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class CadastroController implements Initializable {

    @FXML private TableView<Funcionario> tabelaFuncionarios;
    @FXML private TableColumn<Funcionario, Integer> ID;
    @FXML private TableColumn<Funcionario, String> Nome, CPF, Email, Telefone, Cargo, Status;
    @FXML private TableColumn<Funcionario, Double> Salario;
    @FXML private TableColumn<Funcionario, LocalDate> Data_admissao;
    @FXML private TextField Research;
    @FXML private DatePicker De;
    @FXML private DatePicker Ate;

    private final Database db = new Database();

    private FilteredList<Funcionario> dadosFiltrados;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ID.setCellValueFactory(cellData -> cellData.getValue().id().asObject());
        Nome.setCellValueFactory(cellData -> cellData.getValue().nome());
        CPF.setCellValueFactory(cellData -> cellData.getValue().CPF());
        Email.setCellValueFactory(cellData -> cellData.getValue().email());
        Telefone.setCellValueFactory(cellData -> cellData.getValue().telefone());
        Cargo.setCellValueFactory(cellData -> cellData.getValue().cargo());
        Salario.setCellValueFactory(cellData -> cellData.getValue().salario().asObject());
        Status.setCellValueFactory(cellData -> cellData.getValue().status());
        Data_admissao.setCellValueFactory(cellData -> cellData.getValue().data_admissao());

        DateTimeFormatter formatadorBR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Data_admissao.setCellFactory(coluna -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate data, boolean vazio) {
                super.updateItem(data, vazio);
                if (vazio || data == null) {
                    setText(null);
                } else {
                    setText(formatadorBR.format(data));
                }
            }
        });

        ObservableList<Funcionario> listaOriginal = db.getFuncionarios();
        dadosFiltrados = new FilteredList<>(listaOriginal, p -> true);

        tabelaFuncionarios.getSortOrder().add(ID);
        ID.setSortType(TableColumn.SortType.ASCENDING);
        Research.textProperty().addListener((observable, oldValue, newValue) -> aplicarFiltros());
        De.valueProperty().addListener((observable, oldValue, newValue) -> aplicarFiltros());
        Ate.valueProperty().addListener((observable, oldValue, newValue) -> aplicarFiltros());

        SortedList<Funcionario> dadosOrdenados = new SortedList<>(dadosFiltrados);
        dadosOrdenados.comparatorProperty().bind(tabelaFuncionarios.comparatorProperty());

        tabelaFuncionarios.setItems(dadosOrdenados);
    }

    private void aplicarFiltros() {
        dadosFiltrados.setPredicate(func -> {
            String textoBusca = Research.getText();
            boolean bateComTexto = true;

            if (textoBusca != null && !textoBusca.trim().isEmpty()) {
                String buscaMinuscula = textoBusca.toLowerCase();

                bateComTexto = String.valueOf(func.id().get()).contains(buscaMinuscula)
                        || func.nome().get().toLowerCase().contains(buscaMinuscula)
                        || func.CPF().get().toLowerCase().contains(buscaMinuscula)
                        || func.email().get().toLowerCase().contains(buscaMinuscula)
                        || func.telefone().get().toLowerCase().contains(buscaMinuscula)
                        || func.cargo().get().toLowerCase().contains(buscaMinuscula)
                        || func.status().get().toLowerCase().contains(buscaMinuscula);
            }

            LocalDate dataFunc = func.data_admissao().get();
            LocalDate dataDe = De.getValue();
            LocalDate dataAte = Ate.getValue();
            boolean bateComData = true;

            if (dataFunc != null) {
                if (dataDe != null && dataFunc.isBefore(dataDe)) {
                    bateComData = false;
                }
                if (dataAte != null && dataFunc.isAfter(dataAte)) {
                    bateComData = false;
                }
            } else if (dataDe != null || dataAte != null) {
                bateComData = false;
            }

            return bateComTexto && bateComData;
        });
    }

    @FXML
    private void Adicionar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/Adicionar.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Adicionar Novo Registro");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/demo/Img/logo.png")));
            stage.setScene(new Scene(root));
            stage.showAndWait();
            Reload(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Editar(ActionEvent event) {
        Funcionario selecionado = tabelaFuncionarios.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            exibirAlerta("Atenção", "Por favor, clique em um funcionário na tabela para poder editá-lo!", Alert.AlertType.WARNING);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/Editar.fxml"));
            Parent root = loader.load();

            EditarController controller = loader.getController();
            controller.setFuncionario(selecionado);

            Stage stage = new Stage();
            stage.setTitle("Editar Registro");
            try {
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/demo/Img/logo.png")));
            } catch (Exception e) {
                System.out.println("Ícone não encontrado.");
            }
            stage.setScene(new Scene(root));
            stage.showAndWait();
            Reload(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Deletar(ActionEvent event) {
        Funcionario selecionado = tabelaFuncionarios.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            exibirAlerta("Atenção", "Por favor, clique em um funcionário na tabela para poder deletá-lo!", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION, "Tem certeza que deseja deletar este funcionário?", ButtonType.YES, ButtonType.NO);

        confirmacao.setHeaderText(null);
        confirmacao.setTitle("");

        javafx.stage.Stage dialogStage = (javafx.stage.Stage) confirmacao.getDialogPane().getScene().getWindow();
        dialogStage.initStyle(javafx.stage.StageStyle.UNDECORATED);

        confirmacao.getDialogPane().setStyle("-fx-border-color: #333333; -fx-border-width: 1px;");

        confirmacao.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.YES) {
                if (db.deletarFuncionario(selecionado.id().get())) {
                    Reload(null);
                    exibirAlerta("Sucesso!", "Usuário deletado com sucesso!", Alert.AlertType.INFORMATION);
                } else {
                    exibirAlerta("Erro", "Não foi possível remover o usuário do banco.", Alert.AlertType.ERROR);
                }
            }
        });
    }


    @FXML
    private void Reload(ActionEvent event) {
        ObservableList<Funcionario> novaLista = db.getFuncionarios();
        dadosFiltrados = new FilteredList<>(novaLista, p -> true);
        Research.clear();
        De.setValue(null);
        Ate.setValue(null);

        SortedList<Funcionario> dadosOrdenados = new SortedList<>(dadosFiltrados);
        dadosOrdenados.comparatorProperty().bind(tabelaFuncionarios.comparatorProperty());

        tabelaFuncionarios.setItems(dadosOrdenados);
    }

    private void exibirAlerta(String titulo, String msg, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
