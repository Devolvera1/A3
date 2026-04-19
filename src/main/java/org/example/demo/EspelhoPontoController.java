package org.example.demo;

import javafx.collections.FXCollections;
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
import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


public class EspelhoPontoController implements Initializable {
    private final Database db = new Database();
    private Usuario usuarioLogado;


    @FXML
    private TableView<RegistroPonto> tabelaPonto;

    @FXML
    private Button Adicionar;
    @FXML
    private Button Editar;
    @FXML
    private Button Deletar;
    @FXML
    private TableColumn<RegistroPonto, Integer> colFuncID;
    @FXML
    private TableColumn<RegistroPonto, String> colNome;
    @FXML
    private TableColumn<RegistroPonto, String> colData;
    @FXML
    private TableColumn<RegistroPonto, String> colEntrada;
    @FXML
    private TableColumn<RegistroPonto, String> colSaida;
    @FXML
    private TableColumn<RegistroPonto, String> colEntrada2;
    @FXML
    private TableColumn<RegistroPonto, String> colSaida2;
    @FXML
    private TableColumn<RegistroPonto, String> colObs;
    @FXML
    private TableColumn<RegistroPonto, String> colStatus;

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
    }

    public void setUsuario(Usuario user) {
        this.usuarioLogado = user;
        if (user != null) {
            tabelaPonto.setItems(db.getPontosPorPerfil(user));

            boolean naoEhAdmin = !user.getFuncao().equalsIgnoreCase("ADMIN");

            Adicionar.setDisable(naoEhAdmin);
            Editar.setDisable(naoEhAdmin);
            Deletar.setDisable(naoEhAdmin);
        }
    }

    @FXML
    private void Adicionar (ActionEvent event) {
        System.out.println("AA");
    };
    @FXML
    private void Editar(ActionEvent event) {
        System.out.println("AA");
    };
    @FXML
    private void Deletar(ActionEvent event) {
        System.out.println("AA");
    };



    @FXML
    private void Reload(ActionEvent event) {
        if (this.usuarioLogado != null) {
            ObservableList<RegistroPonto> dadosAtualizados = db.getPontosPorPerfil(this.usuarioLogado);
            tabelaPonto.setItems(dadosAtualizados);
            tabelaPonto.refresh();
        }

    }
}