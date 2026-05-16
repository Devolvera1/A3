package org.example.demo.view.principal;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.example.demo.view.DemostrativoPagamento.DemostrativoPagamentoController;
import org.example.demo.view.Relatorio.Relatorio;
import org.example.demo.view.Relatorio.RelatorioController;
import org.example.demo.view.espelho.EspelhoPontoController;
import org.example.demo.util.Usuario;

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
    @FXML
    private Button Relatorio;

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
            Relatorio.setDisable(true);
        } else {
            Cadastro.setDisable(false);
            Relatorio.setDisable(false);
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
        Stage Stage = new Stage();
        Stage.setTitle("Cadastro");
        Stage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/demo/Img/logo.png")));
        Stage.setScene(new Scene(root));
        Stage.show();
    }

    @FXML
    private void EspelhoPonto(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/EspelhoPonto.fxml"));
            Parent root = loader.load();
            EspelhoPontoController controller = loader.getController();
            controller.setUsuario(this.usuario);
            Stage stage = new Stage();
            stage.setTitle("Espelho de ponto");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/demo/Img/logo.png")));
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void RegistroPonto(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/MarcaPonto.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Registro de ponto");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/demo/Img/logo.png")));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void DemostrativoPagamento(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/DemostrativoPagamento.fxml"));
            Parent root = loader.load();
            DemostrativoPagamentoController controller = loader.getController();
            controller.setUsuario(this.usuario);
            Stage stage = new Stage();
            stage.setTitle("Demostrativo De Pagamentos");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/demo/Img/logo.png")));
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Relatorio(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/Relatorio.fxml"));
            Parent root = loader.load();
            RelatorioController controller = loader.getController();
            controller.setUsuario(this.usuario);
            Stage stage = new Stage();
            stage.setTitle("Relatório");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/demo/Img/logo.png")));
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void AbrirConfiguracoes(ActionEvent event) {
        exibirAlerta("Aviso", "Funcionalidade em desenvolvimento.", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void AbrirSobre(ActionEvent event) {
        String conteudo = "INTEGRANTES:\n"
                + "• Breno Ramos dos Santos (RA: 823159781)\n"
                + "• Diego Gomes de Souza (RA: 8222242618)\n"
                + "• Guilherme dos Santos Coelho (RA: 823120490)\n"
                + "• Guilherme Feitosa Santana (RA: 823111787)\n"
                + "• Gustavo Henrique de Amorim (RA: 824156924)\n"
                + "• Kaue Luiz Frambach (RA: 823148763)\n"
                + "• Victor Henrique da Silva Perillo Brasil (RA: 823130525)\n\n"
                + "https://github.com/Devolvera1/A3\n\n"
                + "© 2026 Todos os direitos reservados.";

        exibirAlerta("Sobre o Projeto", conteudo, Alert.AlertType.INFORMATION);
    }

    @FXML
    private void Sair(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/demo/Login.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/demo/Img/logo.png")));
            stage.setScene(new Scene(root));
            stage.show();
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exibirAlerta(String t, String m, Alert.AlertType tipo) {
        Alert a = new Alert(tipo); a.setTitle(t); a.setContentText(m); a.showAndWait();
    }


}