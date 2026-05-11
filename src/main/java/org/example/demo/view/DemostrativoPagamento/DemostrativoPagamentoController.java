package org.example.demo.view.DemostrativoPagamento;

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
import javafx.collections.transformation.FilteredList;
import org.example.demo.util.Departamento;
import org.example.demo.util.Usuario;
import org.example.demo.config.Database;

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

    @FXML private TableView<Departamento.HistoricoSalario> tabelaPonto;
    @FXML private TextField Research;
    @FXML private DatePicker inicio;
    @FXML private DatePicker fim;
    @FXML private ComboBox<String> status;
    @FXML private Button Editar;

    @FXML private TableColumn<Departamento.HistoricoSalario, Integer> Id;
    @FXML private TableColumn<Departamento.HistoricoSalario, String> nome;
    @FXML private TableColumn<Departamento.HistoricoSalario, Double> salario;
    @FXML private TableColumn<Departamento.HistoricoSalario, String> Datainicio;
    @FXML private TableColumn<Departamento.HistoricoSalario, String> Datafim;
    @FXML private TableColumn<Departamento.HistoricoSalario, String> Observação;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Id.setCellValueFactory(c -> c.getValue().idProperty().asObject());
        nome.setCellValueFactory(c -> c.getValue().nomeFuncionarioProperty());
        salario.setCellValueFactory(c -> c.getValue().salarioProperty().asObject());
        Datainicio.setCellValueFactory(c -> c.getValue().dataInicioProperty());
        Datafim.setCellValueFactory(c -> c.getValue().dataFimProperty());
        Observação.setCellValueFactory(c -> c.getValue().motivoProperty());

        LocalDate hoje = LocalDate.now();
        inicio.setValue(hoje.withDayOfMonth(1));
        fim.setValue(hoje.withDayOfMonth(hoje.lengthOfMonth()));

        Research.textProperty().addListener((obs, old, novo) -> aplicarFiltro());
        inicio.valueProperty().addListener((obs, old, novo) -> aplicarFiltro());
        fim.valueProperty().addListener((obs, old, novo) -> aplicarFiltro());
    }

    public void setUsuario(Usuario user) {
        this.usuarioLogado = user;
        if (user != null) {
            ObservableList<Departamento.HistoricoSalario> lista = db.listarHistoricoSalarios(user);
            dadosFiltrados = new FilteredList<>(lista, p -> true);
            tabelaPonto.setItems(dadosFiltrados);
            aplicarFiltro();

            boolean naoAdmin = !user.getFuncao().equalsIgnoreCase("ADMIN");
            Editar.setDisable(naoAdmin);
        }
    }

    private void aplicarFiltro() {
        if (dadosFiltrados == null) return;

        dadosFiltrados.setPredicate(registro -> {
            String textoBusca = Research.getText();
            boolean bateComTexto = true;
            if (textoBusca != null && !textoBusca.trim().isEmpty()) {
                String buscaMinuscula = textoBusca.toLowerCase();
                bateComTexto = String.valueOf(registro.getFuncionarioId()).contains(buscaMinuscula)
                        || registro.getNomeFuncionario().toLowerCase().contains(buscaMinuscula);
            }

            LocalDate dataDe = inicio.getValue();
            LocalDate dataAte = fim.getValue();
            boolean dataBate = true;

            if (dataDe != null && dataAte != null) {
                try {
                    LocalDate dataReg = LocalDate.parse(registro.getDataInicio(), formatter);
                    dataBate = !dataReg.isBefore(dataDe) && !dataReg.isAfter(dataAte);
                } catch (Exception e) {
                    dataBate = true;
                }
            }

            return bateComTexto && dataBate;
        });
    }


    @FXML
    private void Reload(ActionEvent event) {
        if (this.usuarioLogado != null) {
            setUsuario(this.usuarioLogado);
        }
    }

    private void abrirJanela(String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.showAndWait();
            Reload(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exibirAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}