package org.example.demo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class Voltar {

    public void trocarTela(Node node, String fxmlPath) {
        try {
            URL resource = getClass().getResource(fxmlPath);
            if (resource == null) {
                throw new IOException("Arquivo FXML não encontrado: " + fxmlPath);
            }

            Parent root = FXMLLoader.load(resource);
            Stage stage = (Stage) node.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}