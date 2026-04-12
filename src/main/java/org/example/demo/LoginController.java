package org.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    public void initialize() {

        usuario.setOnAction(this::onOkButtonClick);
        senha.setOnAction(this::onOkButtonClick);

        usuario.textProperty().addListener((obs, oldV, newV) ->
                usuario.setText(newV.toLowerCase()));

        senha.textProperty().addListener((obs, oldV, newV) ->
                senha.setText(newV.toLowerCase()));
    }

    @FXML
    private void onOkButtonClick(ActionEvent event) {

        String username = usuario.getText();
        String Senha = senha.getText();

        Database dbConnection = new Database();

        if (dbConnection.authenticateUser(username, Senha)) {

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/principal.fxml"));
                Parent root = loader.load();

                PrincipalController controller = loader.getController();
                controller.setUsuario(username);

                Stage stage = (Stage) ((Node) event.getSource())
                        .getScene().getWindow();

                stage.setScene(new Scene(root));
                stage.centerOnScreen();
                stage.setTitle("Página Principal");

            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erro", "Não foi possível abrir a página principal.");
            }

        } else {
            showAlert("Erro de Login", "Usuário ou senha incorretos.");
        }
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