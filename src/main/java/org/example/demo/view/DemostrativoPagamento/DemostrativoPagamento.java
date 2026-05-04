package org.example.demo.view.DemostrativoPagamento;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DemostrativoPagamento extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/demo/DemostrativoPagamento.fxml")
        );
        Scene scene = new Scene(loader.load());

        stage.setTitle("Demostrativo De pagamento");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
