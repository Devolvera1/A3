package org.example.demo;
import java.sql.*;

public class Database {

    private static final String URL =
            "jdbc:mysql://127.0.0.1:3306/empresa";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public boolean authenticateUser(String username, String password) {
        String query = "SELECT * FROM usuarios WHERE username = ? AND senha = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
