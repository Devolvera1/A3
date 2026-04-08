package org.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usuario;
    @FXML
    private PasswordField senha;
    @FXML
    private Button Ok;
    @FXML
    private Button Cancelar;


    @FXML
    private void onOkButtonClick()  {
        String username = usuario.getText();
        String Senha = senha.getText();
        Database dbConnection = new Database();

        if (dbConnection.authenticateUser(username, Senha)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/principal.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Página Principal");
                stage.show();
                usuario.getScene().getWindow().hide();

            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erro", "Não foi possível abrir a página principal.");
            }

        } else {
            showAlert("Erro de Login", "Usuário ou senha incorretos.");
        }
        usuario.setOnAction(event -> onOkButtonClick());
        senha.setOnAction(event -> onOkButtonClick());

        usuario.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(newValue.toLowerCase())) {
                usuario.setText(newValue.toLowerCase());
            }
        });

        senha.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(newValue.toLowerCase())) {
                senha.setText(newValue.toLowerCase());
            }
        });
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void onCancelButtonClick() {
            System.exit(0);
        }
    }



