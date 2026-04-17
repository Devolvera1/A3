package org.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
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
                Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                loginStage.close();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/principal.fxml"));
                Parent root = loader.load();

                PrincipalController controller = loader.getController();
                controller.setUsuario(user);

                Stage stage = new Stage();
                stage.setTitle("Sistema de Gerenciamento CoreRH 1.0");
                stage.setScene(new Scene(root));
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/demo/Img/logo.png")));
                stage.setMaximized(true);

                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onCancelButtonClick(ActionEvent event) {
        System.exit(0);
    }
}