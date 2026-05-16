package org.example.demo.view.DemostrativoPagamento;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AdicionarDemostrativo extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/demo/AdicionarDemostrativo.fxml")
        );

        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/demo/Img/logo.png")));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
