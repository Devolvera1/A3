package org.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;

public class CadastroController {

    @FXML
    private void Fechar (ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void Minimizar (ActionEvent event) {
        ((Stage)((Node)event.getSource()).getScene().getWindow()).setIconified(true);

    }
}
