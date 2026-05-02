package org.example.demo.view.ponto;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.example.demo.config.Database;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MarcaPontoController {

    @FXML private TextField txtUsuario;
    @FXML private TextField txtSenha;
    @FXML private Text lblData;
    @FXML private Text lblRelogio;

    private Database db = new Database();

    @FXML
    public void initialize() {
        lblData.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        lblRelogio.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    @FXML
    private void handleRegistrar() {
        String user = txtUsuario.getText();
        String pass = txtSenha.getText();

        if (user.isEmpty() || pass.isEmpty()) {
            showAlert("Atenção", "Preencha todos os campos!");
            return;
        }

        String resultado = db.registrarPonto(user, pass);
        showAlert("Registro de Ponto", resultado);

        txtUsuario.clear();
        txtSenha.clear();
    }

    private void showAlert(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}