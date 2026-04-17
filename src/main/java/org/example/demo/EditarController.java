package org.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditarController {

    @FXML private TextField Id, Nome, Cpf, Email, Telefone, Cargo, Salario;
    @FXML private ComboBox<String> Status;

    @FXML
    public void initialize() {
        Status.getItems().addAll("Ativo", "Inativo", "Em Experiência", "Afastada");
        Id.setDisable(true);
    }

    public void setFuncionario(Funcionario f) {
        Id.setText(String.valueOf(f.id().get()));
        Nome.setText(f.nome().get());
        Cpf.setText(f.CPF().get());
        Email.setText(f.email().get());
        Telefone.setText(f.telefone().get());
        Cargo.setText(f.cargo().get());
        Salario.setText(String.valueOf(f.salario().get()));
        Status.setValue(f.status().get());
    }

    @FXML
    private void Ok(ActionEvent event) {
        if (Nome.getText().isEmpty() || Cpf.getText().isEmpty() || Salario.getText().isEmpty()) {
            exibirAlerta("Erro", "Preencha os campos obrigatórios!");
            return;
        }

        String sql = "UPDATE funcionarios SET nome=?, cpf=?, email=?, telefone=?, cargo=?, salario=?, status=? WHERE id=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, Nome.getText());
            pstmt.setString(2, Cpf.getText());
            pstmt.setString(3, Email.getText());
            pstmt.setString(4, Telefone.getText());
            pstmt.setString(5, Cargo.getText());

            double salario = Double.parseDouble(Salario.getText().replace(",", "."));
            pstmt.setDouble(6, salario);
            pstmt.setString(7, Status.getValue());

            pstmt.setInt(8, Integer.parseInt(Id.getText()));

            pstmt.executeUpdate();

            exibirAlerta("Sucesso", "Funcionário atualizado com sucesso!");
            Fechar(event);

        } catch (SQLException e) {
            e.printStackTrace();
            exibirAlerta("Erro", "Erro ao salvar no banco!");
        } catch (NumberFormatException e) {
            exibirAlerta("Erro", "Salário inválido!");
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
