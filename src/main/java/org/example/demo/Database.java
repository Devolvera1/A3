package org.example.demo;

import java.sql.*;

public class Database {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/empresa";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public Usuario authenticateUser(String username, String password) {

        String query = "SELECT username, funcao FROM usuarios WHERE username = ? AND senha = ?";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Usuario(
                        rs.getString("username"),
                        rs.getString("funcao")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}