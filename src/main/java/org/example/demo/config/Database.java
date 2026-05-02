package org.example.demo.config;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.demo.view.cadastro.Adicionar;
import org.example.demo.util.Cargo;
import org.example.demo.util.Departamento;
import org.example.demo.util.Usuario;

import java.sql.*;

public class Database {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/empresa";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public Usuario authenticateUser(String username, String password) {
        String query = "SELECT funcionario_id, username, funcao FROM usuarios WHERE username = ? AND senha = ?";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Usuario(
                        rs.getInt("funcionario_id"),
                        rs.getString("username"),
                        rs.getString("funcao")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean deletarFuncionario(int id) {
        String sql = "DELETE FROM funcionarios WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public ObservableList<Adicionar.Funcionario> getFuncionarios() {
        ObservableList<Adicionar.Funcionario> lista = FXCollections.observableArrayList();

        String sql = "SELECT f.id, f.nome, f.cpf, f.email, f.telefone, f.status, f.data_admissao, f.departamento_id, " +
                "c.nome AS nome_cargo, c.salarioBase, " +
                "d.nome AS nome_departamento " +
                "FROM funcionarios f " +
                "INNER JOIN cargos c ON f.cargo_id = c.ID " +
                "INNER JOIN departamentos d ON f.departamento_id = d.ID";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Adicionar.Funcionario f = new Adicionar.Funcionario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("email"),
                        rs.getString("telefone"),
                        rs.getString("nome_cargo"),
                        rs.getDouble("salarioBase"),
                        rs.getString("status"),
                        rs.getDate("data_admissao").toLocalDate(),
                        rs.getString("nome_departamento")
                );
                lista.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }


    public String registrarPonto(String username, String password) {
        String authQuery = "SELECT funcionario_id FROM usuarios WHERE username = ? AND senha = ?";
        String pontoQuery = "SELECT * FROM registroPonto WHERE funcionario_id = ? AND data_registro = CURDATE()";

        try (Connection conn = getConnection()) {
            int funcionarioId = -1;
            try (PreparedStatement pstmt = conn.prepareStatement(authQuery)) {
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    funcionarioId = rs.getInt("funcionario_id");
                } else {
                    return "Usuário ou senha inválidos!";
                }
            }

            try (PreparedStatement pstmt = conn.prepareStatement(pontoQuery,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
                pstmt.setInt(1, funcionarioId);
                ResultSet rs = pstmt.executeQuery();

                Time agora = new Time(System.currentTimeMillis());

                if (!rs.next()) {
                    String insert = "INSERT INTO registroPonto (funcionario_id, data_registro, entrada, status) VALUES (?, CURDATE(), ?, 'PENDENTE')";
                    try (PreparedStatement insStmt = conn.prepareStatement(insert)) {
                        insStmt.setInt(1, funcionarioId);
                        insStmt.setTime(2, agora);
                        insStmt.executeUpdate();
                        return "Entrada registrada às " + agora;
                    }
                } else {
                    String colunaUpdate = "";
                    if (rs.getTime("saida_almoco") == null) {
                        colunaUpdate = "saida_almoco";
                    } else if (rs.getTime("retorno_almoco") == null) {
                        colunaUpdate = "retorno_almoco";
                    } else if (rs.getTime("saida") == null) {
                        colunaUpdate = "saida";
                    } else {
                        return "Todos os pontos de hoje já foram registrados!";
                    }

                    String update = "UPDATE registroPonto SET " + colunaUpdate + " = ?, status = ? WHERE id = ?";
                    String novoStatus = colunaUpdate.equals("saida") ? "COMPLETO" : "PENDENTE";

                    try (PreparedStatement updStmt = conn.prepareStatement(update)) {
                        updStmt.setTime(1, agora);
                        updStmt.setString(2, novoStatus);
                        updStmt.setInt(3, rs.getInt("id"));
                        updStmt.executeUpdate();
                        return colunaUpdate.replace("_", " ") + " registrada às " + agora;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Erro no banco de dados.";
        }
    }

    public ObservableList<Departamento.RegistroPonto> getPontosPorPerfil(Usuario user) {
        ObservableList<Departamento.RegistroPonto> lista = FXCollections.observableArrayList();
        boolean isAdmin = user.getFuncao().equalsIgnoreCase("ADMIN");

        String sql = "SELECT p.*, f.nome FROM registroPonto p " +
                "INNER JOIN funcionarios f ON p.funcionario_id = f.id";

        if (!isAdmin) {
            sql += " WHERE p.funcionario_id = ?";
        }

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (!isAdmin) {
                pstmt.setInt(1, user.getId());
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                lista.add(new Departamento.RegistroPonto(
                        rs.getInt("id"),
                        rs.getInt("funcionario_id"),
                        rs.getString("nome"),
                        rs.getString("data_registro"),
                        rs.getString("entrada"),
                        rs.getString("saida_almoco"),
                        rs.getString("retorno_almoco"),
                        rs.getString("saida"),
                        rs.getString("Observacao"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean deletarPonto(int id) {
        String sql = "DELETE FROM registroPonto WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public ObservableList<Cargo> getCargos() {
        ObservableList<Cargo> lista = FXCollections.observableArrayList();
        String sql = "SELECT ID, nome FROM cargos WHERE ativo = TRUE";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Cargo(rs.getInt("ID"), rs.getString("nome")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public ObservableList<Departamento> getDepartamentos() {
        ObservableList<Departamento> lista = FXCollections.observableArrayList();
        String sql = "SELECT ID, nome FROM departamentos";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Departamento(rs.getInt("ID"), rs.getString("nome")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }






    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}