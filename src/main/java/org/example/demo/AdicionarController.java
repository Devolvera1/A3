package org.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class AdicionarController {

    @FXML private TextField Id, Nome, Cpf, Email, Telefone, Salario;
    @FXML private ComboBox<String> Status;
    @FXML private ComboBox<Cargo> Cargo;
    @FXML private ComboBox<Departamento> Departamento;

    private Database db = new Database();

    @FXML
    public void initialize() {
        Cargo.setItems(db.getCargos());
        Departamento.setItems(db.getDepartamentos());

        Status.getItems().addAll("ATIVO", "INATIVO", "EM_EXPERIENCIA");
        Status.setValue("ATIVO");
        Id.setDisable(true);
        Salario.setDisable(true);
    }

    @FXML
    private void Ok(ActionEvent event) {
        if (Cargo.getValue() == null || Departamento.getValue() == null) {
            exibirAlerta("Erro", "Selecione um Cargo e um Departamento!");
            return;
        }
        String sql = "INSERT INTO funcionarios (nome, cpf, email, telefone, status, data_admissao, cargo_id, departamento_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, Nome.getText());
            pstmt.setString(2, Cpf.getText());
            pstmt.setString(3, Email.getText());
            pstmt.setString(4, Telefone.getText());
            pstmt.setString(5, Status.getValue().toUpperCase());
            pstmt.setDate(6, java.sql.Date.valueOf(LocalDate.now()));
            pstmt.setInt(7, Cargo.getValue().getId());
            pstmt.setInt(8, Departamento.getValue().getId());

            pstmt.executeUpdate();

            exibirAlerta("Sucesso", "Funcionário cadastrado com sucesso!");
            Fechar(event);

        } catch (SQLException e) {
            e.printStackTrace();
            exibirAlerta("Erro", "Erro SQL: " + e.getMessage());
        }
    }

    private void exibirAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
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