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

public class EditarEspelhoController {

    @FXML private TextField ID, Data, Entrada, Saida, Entrada2, Saida2;
    @FXML private ComboBox<String> Obs;
    @FXML private ComboBox<Funcionario> Funcionarios;

    @FXML
    public void initialize() {
        carregarFuncionarios();
        Obs.getItems().addAll("Esqueceu de marcar", "Marcação em Duplicidade", "Problemas Técnicos",
                "Atestados Médicos", "Afastamentos Diversos", "Serviços Externos",
                "Compensação de Banco de Horas", "Troca de Turno", "Troca de Escala");

        ID.setDisable(true);

        aplicarMascaraData(Data);
        aplicarMascaraHora(Entrada);
        aplicarMascaraHora(Saida);
        aplicarMascaraHora(Entrada2);
        aplicarMascaraHora(Saida2);
    }

    private void carregarFuncionarios() {
        String sql = "SELECT id, nome FROM funcionarios ORDER BY nome ASC";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Funcionarios.getItems().add(new Funcionario(rs.getInt("id"), rs.getString("nome")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void setDados(RegistroPonto registro) {
        ID.setText(String.valueOf(registro.getId()));

        if (registro.getData() != null) {
            String[] partes = registro.getData().toString().split("-");
            Data.setText(partes[2] + "/" + partes[1] + "/" + partes[0]);
        }

        Entrada.setText(limparHora(registro.getEntrada()));
        Saida.setText(limparHora(registro.getSaidaAlmoco()));
        Entrada2.setText(limparHora(registro.getRetornoAlmoco()));
        Saida2.setText(limparHora(registro.getSaida()));
        Obs.setValue(registro.getObservacao());

        for (Funcionario f : Funcionarios.getItems()) {
            if (f.getId() == registro.getFuncionarioId()) {
                Funcionarios.setValue(f);
                break;
            }
        }
    }

    private String limparHora(String h) {
        if (h == null || h.isEmpty()) return "";
        return h.length() > 5 ? h.substring(0, 5) : h;
    }

    @FXML
    private void Ok(ActionEvent event) {
        Funcionario func = Funcionarios.getSelectionModel().getSelectedItem();
        String dataDigitada = Data.getText();

        if (func == null || dataDigitada.length() < 10) {
            exibirAlerta("Erro", "Selecione o funcionário e a data corretamente!");
            return;
        }

        String dataBanco;
        try {
            String[] p = dataDigitada.split("/");
            dataBanco = p[2] + "-" + p[1] + "-" + p[0];
        } catch (Exception e) {
            exibirAlerta("Erro", "Data inválida!");
            return;
        }

        String sql = "UPDATE registroPonto SET funcionario_id=?, data_registro=?, entrada=?, " +
                "saida_almoco=?, retorno_almoco=?, saida=?, status='Esqueceu de marcar', observacao=? " +
                "WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, func.getId());
            pstmt.setString(2, dataBanco);
            pstmt.setString(3, formatarHora(Entrada.getText()));
            pstmt.setString(4, formatarHora(Saida.getText()));
            pstmt.setString(5, formatarHora(Entrada2.getText()));
            pstmt.setString(6, formatarHora(Saida2.getText()));
            pstmt.setString(7, Obs.getValue());
            pstmt.setInt(8, Integer.parseInt(ID.getText()));

            pstmt.executeUpdate();
            exibirAlerta("Sucesso", "Registro atualizado!", Alert.AlertType.INFORMATION);
            Fechar(event);
        } catch (SQLException e) {
            exibirAlerta("Erro", "Erro ao atualizar: " + e.getMessage());
        }
    }

    private String formatarHora(String t) {
        if (t == null || t.trim().isEmpty()) return null;
        return t.length() == 5 ? t + ":00" : t;
    }

    private void aplicarMascaraHora(TextField tf) {
        tf.textProperty().addListener((obs, velho, novo) -> {
            if (novo == null || novo.length() < velho.length()) return;
            String texto = novo.replaceAll("[^\\d]", "");
            if (texto.length() > 4) texto = texto.substring(0, 4);

            if (texto.length() >= 3) {
                String formatado = texto.substring(0, 2) + ":" + texto.substring(2);
                if (!novo.equals(formatado)) {
                    tf.setText(formatado);
                    Platform.runLater(tf::end);
                }
            }
        });
    }

    private void aplicarMascaraData(TextField tf) {
        tf.textProperty().addListener((obs, velho, novo) -> {
            if (novo == null) return;
            String texto = novo.replaceAll("[^\\d]", "");

            if (texto.length() > 8) texto = texto.substring(0, 8);

            String formatado = texto;
            if (texto.length() >= 5) {
                formatado = texto.substring(0, 2) + "/" + texto.substring(2, 4) + "/" + texto.substring(4);
            } else if (texto.length() >= 3) {
                formatado = texto.substring(0, 2) + "/" + texto.substring(2);
            }

            if (!novo.equals(formatado)) {
                tf.setText(formatado);
                int pos = formatado.length();
                Platform.runLater(() -> tf.positionCaret(pos));
            }
        });
    }


    private void exibirAlerta(String t, String m) { exibirAlerta(t, m, Alert.AlertType.ERROR); }

    private void exibirAlerta(String t, String m, Alert.AlertType tipo) {
        Alert a = new Alert(tipo);
        a.setTitle(t); a.setHeaderText(null); a.setContentText(m);
        a.showAndWait();
    }

    @FXML private void Cancelar(ActionEvent event) { Fechar(event); }

    @FXML private void Fechar(ActionEvent event) {
        ((Stage)((Node)event.getSource()).getScene().getWindow()).close();
    }
}
