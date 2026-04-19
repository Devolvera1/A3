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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


public class EspelhoPontoController implements Initializable {
    private final Database db = new Database();
    private Usuario usuarioLogado;


    @FXML
    private TableView<RegistroPonto> tabelaPonto;
    @FXML
    private TableColumn<RegistroPonto, Integer> colFuncID;
    @FXML
    private TableColumn<RegistroPonto, String> colNome;
    @FXML
    private TableColumn<RegistroPonto, String> colData;
    @FXML
    private TableColumn<RegistroPonto, String> colStatus;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colFuncID.setCellValueFactory(c -> c.getValue().funcionarioIDProperty().asObject());
        colNome.setCellValueFactory(c -> c.getValue().nomeFuncionarioProperty());
        colData.setCellValueFactory(c -> c.getValue().dataProperty());
        colStatus.setCellValueFactory(c -> c.getValue().statusProperty());
    }

    public void setUsuario(Usuario user) {
        this.usuarioLogado = user;
        if (user != null) {
            System.out.println("Logado como: " + user.getUsername() + " | ID: " + user.getId());
            tabelaPonto.setItems(db.getPontosPorPerfil(user));
        }
    }



    private void Reload (ActionEvent event) {
        System.out.println("Reload");
    }
}