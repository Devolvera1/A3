module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    exports org.example.demo.config;
    opens org.example.demo.config to javafx.fxml;
    exports org.example.demo.view.espelho;
    opens org.example.demo.view.espelho to javafx.fxml;
    exports org.example.demo.util;
    opens org.example.demo.util to javafx.fxml;
    exports org.example.demo.view.cadastro;
    opens org.example.demo.view.cadastro to javafx.fxml;
    exports org.example.demo.view.ponto;
    opens org.example.demo.view.ponto to javafx.fxml;
    exports org.example.demo.view.login;
    opens org.example.demo.view.login to javafx.fxml;
    exports org.example.demo.view.principal;
    opens org.example.demo.view.principal to javafx.fxml;
}