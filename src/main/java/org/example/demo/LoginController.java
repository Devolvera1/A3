package org.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usuario;

    @FXML
    private PasswordField senha;

    @FXML
    private void onOkButtonClick(ActionEvent event) {

        String username = usuario.getText();
        String password = senha.getText();

        Database db = new Database();
        Usuario user = db.authenticateUser(username, password);

        if (user != null) {

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/principal.fxml"));
                Parent root = loader.load();

                PrincipalController controller = loader.getController();
                controller.setUsuario(user);

                Stage stage = (Stage) ((Node) event.getSource())
                        .getScene().getWindow();
                stage.centerOnScreen();


                stage.setScene(new Scene(root));

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Login inválido!");
            alert.show();
        }
    }

    @FXML
    private void onCancelButtonClick(ActionEvent event) {
        System.exit(0);
    }
}