package org.example.demo;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PrincipalController {

    @FXML
    private Label labelDataHora;

    @FXML
    private Label labelUsuario;

    @FXML
    private Button Cadastro;

    private Usuario usuario;

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");

    @FXML
    public void initialize() {
        iniciarRelogio();
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        labelUsuario.setText(usuario.getUsername());
        configurarPermissoes();
    }

    private void configurarPermissoes() {

        if (!usuario.getFuncao().equalsIgnoreCase("ADMIN")) {
            Cadastro.setDisable(true);
        } else {
            Cadastro.setDisable(false);
        }
    }

    private void iniciarRelogio() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e ->
                        labelDataHora.setText(LocalDateTime.now().format(formatter)))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML
    private void abrirCadastro(ActionEvent event) throws IOException {

        if (!usuario.getFuncao().equalsIgnoreCase("ADMIN")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Acesso negado!");
            alert.show();
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/Cadastro.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        stage.setScene(new Scene(root));
    }

    @FXML
    private void AbrirRelatorio(ActionEvent event) {
        System.out.println("Relatório");
    }

    @FXML
    private void AbrirDepartamento(ActionEvent event) {
        System.out.println("Departamento");
    }

    @FXML
    private void AbrirConfiguracoes(ActionEvent event) {
        System.out.println("Configurações");
    }

    @FXML
    private void AbrirSobre(ActionEvent event) {
        System.out.println("Sobre");
    }

    @FXML
    private void fecharJanela(ActionEvent event) {
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    private void minimizarJanela(ActionEvent event) {
        ((Stage)((Node)event.getSource()).getScene().getWindow()).setIconified(true);
    }

    @FXML
    private void Sair(ActionEvent event) {
        System.exit(0);
    }
}