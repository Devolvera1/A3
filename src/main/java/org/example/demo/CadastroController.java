package org.example.demo;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class CadastroController implements Initializable {

    @FXML private TableView<Funcionario> tabelaFuncionarios;


    @FXML private TableColumn<Funcionario, Integer> ID;
    @FXML private TableColumn<Funcionario, String> Nome;
    @FXML private TableColumn<Funcionario, String> CPF;
    @FXML private TableColumn<Funcionario, String> Email;
    @FXML private TableColumn<Funcionario, String> Telefone;
    @FXML private TableColumn<Funcionario, String> Cargo;
    @FXML private TableColumn<Funcionario, Double> Salario;
    @FXML private TableColumn<Funcionario, String> Status;

    private Database db = new Database();

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


        tabelaFuncionarios.setItems(db.getFuncionarios());
    }

    @FXML
    private void Fechar(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void Minimizar(ActionEvent event) {
        ((Stage)((Node)event.getSource()).getScene().getWindow()).setIconified(true);
    }
    @FXML
    private void Adicionar(ActionEvent event) {

    }
}