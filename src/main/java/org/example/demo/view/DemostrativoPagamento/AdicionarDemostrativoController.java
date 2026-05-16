package org.example.demo.view.DemostrativoPagamento;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.demo.config.Database;
import org.example.demo.view.cadastro.Adicionar;

import java.sql.*;

public class AdicionarDemostrativoController {
    @FXML private TextField ID, Salario, DataInicio, DataFim;
    @FXML private ComboBox<String> Motivo;
    @FXML private ComboBox<Adicionar.Funcionario> Funcionarios;

    @FXML
    public void initialize() {
        carregarFuncionarios();
        Motivo.getItems().addAll("Promoção", "Ajuste Anual", "Mérito", "Enquadramento", "Outros");
        ID.setDisable(true);

        aplicarMascaraData(DataInicio);
        aplicarMascaraData(DataFim);
    }

    private void carregarFuncionarios() {
        String sql = "SELECT id, nome FROM funcionarios ORDER BY nome ASC";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Funcionarios.getItems().add(new Adicionar.Funcionario(rs.getInt("id"), rs.getString("nome")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    private void Ok(ActionEvent event) {
        Adicionar.Funcionario func = Funcionarios.getValue();
        if (func == null || DataInicio.getText().length() < 10) {
            exibirAlerta("Erro", "Preencha o funcionário e a data de início!");
            return;
        }

        String sql = "INSERT INTO historIcoSalarios (funcionario_id, salario, dataInicio , dataFim, motivo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, func.getId());
            pstmt.setDouble(2, Double.parseDouble(Salario.getText().replace(",", ".")));
            pstmt.setString(3, converterData(DataInicio.getText()));
            pstmt.setString(4, converterData(DataFim.getText()));
            pstmt.setString(5, Motivo.getValue());
            pstmt.executeUpdate();
            Fechar(event);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private String converterData(String d) {
        if (d == null || d.length() < 10) return null;
        String[] p = d.split("/");
        return p[2] + "-" + p[1] + "-" + p[0];
    }

    private void aplicarMascaraData(TextField tf) {
        tf.textProperty().addListener((obs, velho, novo) -> {
            if (novo == null || novo.length() < velho.length()) return;
            String texto = novo.replaceAll("[^\\d]", "");
            if (texto.length() > 8) texto = texto.substring(0, 8);
            if (texto.length() >= 5) {
                tf.setText(texto.substring(0, 2) + "/" + texto.substring(2, 4) + "/" + texto.substring(4));
            } else if (texto.length() >= 3) {
                tf.setText(texto.substring(0, 2) + "/" + texto.substring(2));
            }
            Platform.runLater(tf::end);
        });
    }

    @FXML private void Cancelar(ActionEvent event) { Fechar(event); }
    @FXML private void Fechar(ActionEvent event) {
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }
    private void exibirAlerta(String t, String m) {
        Alert a = new Alert(Alert.AlertType.ERROR); a.setTitle(t); a.setContentText(m); a.showAndWait();
    }
}