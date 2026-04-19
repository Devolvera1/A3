package org.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class Database {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/empresa";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public Usuario authenticateUser(String username, String password) {
        // ADICIONEI 'funcionario_id' NA QUERY ABAIXO:
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


    public ObservableList<Funcionario> getFuncionarios() {
        ObservableList<Funcionario> lista = FXCollections.observableArrayList();
        String query = "SELECT * FROM funcionarios";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                java.sql.Date sqlDate = rs.getDate("data_admissao");
                java.time.LocalDate dataLocal = (sqlDate != null) ? sqlDate.toLocalDate() : null;
                lista.add(new Funcionario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("email"),
                        rs.getString("telefone"),
                        rs.getString("cargo"),
                        rs.getDouble("salario"),
                        rs.getString("status"),
                        dataLocal
                ));
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

    public ObservableList<RegistroPonto> getPontosPorPerfil(Usuario user) {
        ObservableList<RegistroPonto> lista = FXCollections.observableArrayList();
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
                lista.add(new RegistroPonto(
                        rs.getInt("funcionario_id"),
                        rs.getString("nome"),
                        rs.getString("data_registro"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }





    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}