package org.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MarcaPonto extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/demo/MarcaPonto.fxml")
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
