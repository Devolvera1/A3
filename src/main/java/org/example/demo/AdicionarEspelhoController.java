package org.example.demo;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdicionarEspelhoController {

    @FXML private TextField ID, Data, Entrada, Saida, Saida2, Entrada2;
    @FXML private ComboBox<String> Obs;
    @FXML private ComboBox<Funcionario> Funcionarios;



    @FXML
    public void initialize() {
        Funcionarios();
        Obs.getItems().addAll("Esqueceu de marcar", "Marcação em Duplicidade", "Problemas Técnicos","Atestados Médicos", "Afastamentos Diversos", "Serviços Externos", "Compensação de Banco de Horas","Troca de Turno","Troca de Escala", "Compensação de Banco de Horas");
        Obs.setValue("Esqueceu de marcar");
        ID.setDisable(true);
        ID.setPromptText("Gerado automaticamente");

        aplicarMascaraData(Data);
        aplicarMascaraHora(Entrada);
        aplicarMascaraHora(Saida);
        aplicarMascaraHora(Entrada2);
        aplicarMascaraHora(Saida2);

    }

    private void Funcionarios() {
        String sql = "SELECT id, nome FROM funcionarios ORDER BY nome ASC";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Funcionarios.getItems().add(new Funcionario(
                        rs.getInt("id"),
                        rs.getString("nome")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void Ok(ActionEvent event) {
        Funcionario selecionado = Funcionarios.getSelectionModel().getSelectedItem();
        String dataDigitada = Data.getText();

        if (selecionado == null || dataDigitada.length() < 10) {
            exibirAlerta("Erro", "Selecione um funcionário e insira uma data válida (dd/mm/aaaa)!");
            return;
        }

        String dataParaBanco;
        try {
            String[] partes = dataDigitada.split("/");
            dataParaBanco = partes[2] + "-" + partes[1] + "-" + partes[0];
        } catch (Exception e) {
            exibirAlerta("Erro", "Formato de data inválido!");
            return;
        }

        String sql = "INSERT INTO registroPonto (funcionario_id, data_registro, entrada, saida_almoco, retorno_almoco, saida, status, observacao) " +
                "VALUES (?, ?, ?, ?, ?, ?, 'COMPLETO', ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, selecionado.getId());
            pstmt.setString(2, dataParaBanco);
            pstmt.setString(3, Entrada.getText().isEmpty() ? null : Entrada.getText() + ":00");
            pstmt.setString(4, Saida.getText().isEmpty() ? null : Saida.getText() + ":00");
            pstmt.setString(5, Entrada2.getText().isEmpty() ? null : Entrada2.getText() + ":00");
            pstmt.setString(6, Saida2.getText().isEmpty() ? null : Saida2.getText() + ":00");
            pstmt.setString(7, Obs.getValue());

            pstmt.executeUpdate();
            exibirAlerta("Sucesso", "Ponto cadastrado com sucesso!", Alert.AlertType.INFORMATION);
            Fechar(event);

        } catch (SQLException e) {
            e.printStackTrace();
            exibirAlerta("Erro", "Erro ao salvar no banco: " + e.getMessage());
        } catch (NumberFormatException e) {
            exibirAlerta("Erro", "O ID deve ser um número inteiro!");
        }
    }

    private void exibirAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void aplicarMascaraHora(TextField tf) {
        tf.textProperty().addListener((obs, velho, novo) -> {
            String texto = novo.replaceAll("[^\\d]", "");

            if (texto.length() > 4) texto = texto.substring(0, 4);

            if (texto.length() >= 3) {
                tf.setText(texto.substring(0, 2) + ":" + texto.substring(2));
            } else {
                tf.setText(texto);
            }
            Platform.runLater(tf::end);
        });
    }

    private void aplicarMascaraData(TextField tf) {
        tf.textProperty().addListener((obs, velho, novo) -> {
            String texto = novo.replaceAll("[^\\d]", "");

            if (texto.length() > 8) texto = texto.substring(0, 8);

            if (texto.length() >= 5) {
                tf.setText(texto.substring(0, 2) + "/" + texto.substring(2, 4) + "/" + texto.substring(4));
            } else if (texto.length() >= 3) {
                tf.setText(texto.substring(0, 2) + "/" + texto.substring(2));
            } else {
                tf.setText(texto);
            }
            Platform.runLater(tf::end);
        });
    }

    private void exibirAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }



    @FXML
    private void Fechar(ActionEvent event) {
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    private void Cancelar(ActionEvent event) {
        Fechar(event);
    }
}