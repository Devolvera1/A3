package org.example.demo.view.DemostrativoPagamento;

import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.demo.config.Database;
import org.example.demo.util.Departamento;
import org.example.demo.util.Usuario;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DemostrativoPagamentoController implements Initializable {
    private final Database db = new Database();
    private Usuario usuarioLogado;
    private FilteredList<Departamento.HistoricoSalario> dadosFiltrados;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML private TableView<Departamento.HistoricoSalario> TabelaSalario;
    @FXML private TableColumn<Departamento.HistoricoSalario, Integer> ID;
    @FXML private TableColumn<Departamento.HistoricoSalario, String> Nome;
    @FXML private TableColumn<Departamento.HistoricoSalario, String> Salario;
    @FXML private TableColumn<Departamento.HistoricoSalario, String> DataInicio;
    @FXML private TableColumn<Departamento.HistoricoSalario, String> DataFim;
    @FXML private TableColumn<Departamento.HistoricoSalario, String> Motivo;

    @FXML private Button Adicionar, Editar, Deletar;
    @FXML private TextField Research;
    @FXML private DatePicker inicio;
    @FXML private DatePicker fim;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ID.setCellValueFactory(c -> c.getValue().idProperty().asObject());
        Nome.setCellValueFactory(c -> c.getValue().nomeFuncionarioProperty());
        Salario.setCellValueFactory(c -> c.getValue().salarioProperty().asString());
        DataInicio.setCellValueFactory(c -> c.getValue().dataInicioProperty());
        DataFim.setCellValueFactory(c -> c.getValue().dataFimProperty());
        Motivo.setCellValueFactory(c -> c.getValue().motivoProperty());
        Research.textProperty().addListener((obs, antigo, novo) -> aplicarFiltro());
        inicio.valueProperty().addListener((obs, antigo, novo) -> aplicarFiltro());
        fim.valueProperty().addListener((obs, antigo, novo) -> aplicarFiltro());

        TabelaSalario.setRowFactory(tv -> {
            TableRow<Departamento.HistoricoSalario> row = new TableRow<>();
            LocalDate hoje = LocalDate.now();
            inicio.setValue(hoje.withDayOfMonth(1));
            fim.setValue(hoje.withDayOfMonth(hoje.lengthOfMonth()));
            Research.textProperty().addListener((obs, antigo, novo) -> aplicarFiltro());
            inicio.valueProperty().addListener((obs, antigo, novo) -> aplicarFiltro());
            fim.valueProperty().addListener((obs, antigo, novo) -> aplicarFiltro());
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Editar(new ActionEvent());
                }
            });
            return row;
        });
    }

    public void setUsuario(Usuario user) {
        this.usuarioLogado = user;
        if (user != null) {
            dadosFiltrados = new FilteredList<>(db.getHistoricoSalarial(), p -> true);
            TabelaSalario.setItems(dadosFiltrados);
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
            String busca = Research.getText();
            boolean textoBate = true;
            if (busca != null && !busca.isEmpty()) {
                String lower = busca.toLowerCase();
                textoBate = registro.getNomeFuncionario().toLowerCase().contains(lower) ||
                        String.valueOf(registro.getId()).contains(lower);
            }

            LocalDate dInicio = inicio.getValue();
            LocalDate dFim = fim.getValue();
            boolean dataBate = true;

            if (dInicio != null && dFim != null) {
                try {
                    LocalDate dataReg = LocalDate.parse(registro.getDataInicio(), formatter);
                    dataBate = !dataReg.isBefore(dInicio) && !dataReg.isAfter(dFim);
                } catch (Exception e) {
                    dataBate = false;
                }
            }

            return textoBate && dataBate;
        });
    }

    @FXML
    private void Adicionar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/AdicionarDemostrativo.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/demo/Img/logo.png")));
            stage.setScene(new Scene(root));
            stage.showAndWait();
            Reload(null);
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    private void Editar(ActionEvent event) {
        Departamento.HistoricoSalario selecionado = TabelaSalario.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            exibirAlerta("Atenção", "Selecione um registro para editar!", Alert.AlertType.WARNING);
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/EditarDemostrativo.fxml"));
            Parent root = loader.load();
            EditarDemostrativoController controller = loader.getController();
            controller.setDados(selecionado);
            Stage stage = new Stage();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/demo/Img/logo.png")));
            stage.setScene(new Scene(root));
            stage.showAndWait();
            Reload(null);
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    private void Deletar(ActionEvent event) {
        Departamento.HistoricoSalario selecionado = TabelaSalario.getSelectionModel().getSelectedItem();
        if (selecionado == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deseja excluir este histórico?", ButtonType.YES, ButtonType.NO);
        if (alert.showAndWait().get() == ButtonType.YES) {
            if (db.deletarHistoricoSalario(selecionado.getId())) {
                Reload(null);
            }
        }
    }

    @FXML
    private void Reload(ActionEvent event) {
        if (usuarioLogado != null) {
            dadosFiltrados = new FilteredList<>(db.getHistoricoSalarial(), p -> true);
            TabelaSalario.setItems(dadosFiltrados);
            aplicarFiltro();
        }
    }

    private void exibirAlerta(String t, String m, Alert.AlertType tipo) {
        Alert a = new Alert(tipo); a.setTitle(t); a.setContentText(m); a.showAndWait();
    }
}