package org.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.*;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditarController {

    @FXML private TextField Id, Nome, Cpf, Email, Telefone, Salario;
    @FXML private ComboBox<String> Status;
    @FXML private ComboBox<Cargo> Cargo;
    @FXML private ComboBox<Departamento> Departamento;

    @FXML
    public void initialize() {
        Status.getItems().addAll("ATIVO", "INATIVO", "EM_EXPERIENCIA", "AFASTADO");
        Id.setDisable(true);
        Salario.setDisable(true);

        carregarCargos();
        carregarDepartamentos();
    }

    public void setFuncionario(Funcionario f) {
        Id.setText(String.valueOf(f.id().get()));
        Nome.setText(f.nome().get());
        Cpf.setText(f.CPF().get());
        Email.setText(f.email().get());
        Telefone.setText(f.telefone().get());
        Status.setValue(f.status().get().toUpperCase());

        for (Cargo c : Cargo.getItems()) {
            if (c.getNome().equals(f.cargo().get())) {
                Cargo.setValue(c);
                break;
            }
        }
    }

    private void carregarCargos() {
        String sql = "SELECT id, nome FROM cargos WHERE ativo = TRUE";
        try (Connection conn = Database.getConnection();
             ResultSet rs = conn.createStatement().executeQuery(sql)) {
            while (rs.next()) {
                Cargo.getItems().add(new Cargo(rs.getInt("id"), rs.getString("nome")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void carregarDepartamentos() {
        String sql = "SELECT id, nome FROM departamentos";
        try (Connection conn = Database.getConnection();
             ResultSet rs = conn.createStatement().executeQuery(sql)) {
            while (rs.next()) {
                Departamento.getItems().add(new Departamento(rs.getInt("id"), rs.getString("nome")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    private void Ok(ActionEvent event) {
        if (Cargo.getValue() == null || Departamento.getValue() == null) {
            exibirAlerta("Erro", "Selecione um Cargo e um Departamento!");
            return;
        }

        String sql = "UPDATE funcionarios SET nome=?, cpf=?, email=?, telefone=?, status=?, cargo_id=?, departamento_id=? WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, Nome.getText());
            pstmt.setString(2, Cpf.getText());
            pstmt.setString(3, Email.getText());
            pstmt.setString(4, Telefone.getText());
            pstmt.setString(5, Status.getValue());
            pstmt.setInt(6, Cargo.getValue().getId());
            pstmt.setInt(7, Departamento.getValue().getId());
            pstmt.setInt(8, Integer.parseInt(Id.getText()));

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                exibirAlerta("Sucesso", "Funcionário atualizado!");
                Fechar(event);
            } else {
                exibirAlerta("Aviso", "Nenhum registro encontrado com o ID: " + Id.getText());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            exibirAlerta("Erro", "Erro ao salvar: " + e.getMessage());
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
