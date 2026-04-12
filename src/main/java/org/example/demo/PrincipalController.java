package org.example.demo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PrincipalController {

    @FXML
    private Label labelDataHora;

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss");

    @FXML
    public void initialize() {
        iniciarRelogio();
    }

    private void iniciarRelogio() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    LocalDateTime agora = LocalDateTime.now();
                    labelDataHora.setText(agora.format(formatter));
                })
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML
    private Label labelUsuario;

    public void setUsuario(String usuario) {
        labelUsuario.setText(usuario);
    }
    @FXML
    private void fecharJanela(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();
        stage.close();
    }

    @FXML
    private void minimizarJanela(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void Sair() {
        System.exit(0);
    }

    @FXML
    private void abrirCadastro() {
        System.out.println("Cadastro");
    }

    @FXML
    private void AbrirRelatorio() {
        System.out.println("Relatório");
    }

    @FXML
    private void AbrirDepartamento() {
        System.out.println("Departamento");
    }

    @FXML
    private void AbrirConfiguracoes() {
        System.out.println("Configurações");
    }

    @FXML
    private void AbrirSobre() {
        System.out.println("Sobre");
    }
}