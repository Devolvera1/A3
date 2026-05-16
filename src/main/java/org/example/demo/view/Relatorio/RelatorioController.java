package org.example.demo.view.Relatorio;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import org.example.demo.config.Database;
import org.example.demo.util.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RelatorioController {

    @FXML private Label funcionario;
    @FXML private Label Salario;
    @FXML private Label Despesas;
    @FXML private Label Registro;

    @FXML private PieChart TBdepartamento;
    @FXML private BarChart<String, Number> TBDespesas;

    private Usuario usuario;
    public void initialize() {
        carregarCardsKPI();
        carregarGraficoDepartamentos();
        carregarGraficoDespesas();
    }


    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    private void carregarCardsKPI() {
        try (Connection conn = Database.getConnection()) {
            String sqlFunc = "SELECT COUNT(*) FROM funcionarios WHERE status = 'ATIVO'";
            try (PreparedStatement stmt = conn.prepareStatement(sqlFunc); ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    funcionario.setText(String.valueOf(rs.getInt(1)));
                }
            }

            String sqlSalario = "SELECT SUM(salario) FROM historicoSalarios " +
                    "WHERE (dataFim IS NULL OR (YEAR(dataFim) = YEAR(CURDATE()) AND MONTH(dataFim) = MONTH(CURDATE()))) " +
                    "AND dataInicio <= LAST_DAY(CURDATE())";
            try (PreparedStatement stmt = conn.prepareStatement(sqlSalario); ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double totalSalario = rs.getDouble(1);
                    Salario.setText(String.format("R$ %.2f", totalSalario));
                }
            }

            String sqlDespesas = "SELECT SUM(valor) FROM despesas " +
                    "WHERE YEAR(dia) = YEAR(CURDATE()) AND MONTH(dia) = MONTH(CURDATE())";
            try (PreparedStatement stmt = conn.prepareStatement(sqlDespesas); ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double totalDespesas = rs.getDouble(1);
                    Despesas.setText(String.format("R$ %.2f", totalDespesas));
                }
            }

            String sqlPontos = "SELECT COUNT(*) FROM registroPonto " +
                    "WHERE status IN ('INCONSISTENTE', 'FALTA') " +
                    "AND YEAR(data_registro) = YEAR(CURDATE()) AND MONTH(data_registro) = MONTH(CURDATE())";
            try (PreparedStatement stmt = conn.prepareStatement(sqlPontos); ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Registro.setText(String.valueOf(rs.getInt(1)));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao carregar KPIs mensais: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Alimenta o PieChart com a quantidade de funcionários por departamento
     * considerando apenas os que trabalharam/estão ativos neste mês
     */
    private void carregarGraficoDepartamentos() {
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();

        String sql = "SELECT d.nome, COUNT(f.id) AS total " +
                "FROM funcionarios f " +
                "INNER JOIN departamentos d ON f.departamento_id = d.ID " +
                "WHERE f.status = 'ATIVO' " +
                "GROUP BY d.nome";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                pieData.add(new PieChart.Data(rs.getString("nome"), rs.getInt("total")));
            }

            TBdepartamento.setData(pieData);

        } catch (SQLException e) {
            System.err.println("Erro ao carregar gráfico de departamentos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Alimenta o BarChart com a soma das despesas agrupadas por tipo APENAS DESTE MÊS
     */
    private void carregarGraficoDespesas() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Custos do Mês Atual");

        String sql = "SELECT tipo, SUM(valor) AS total FROM despesas " +
                "WHERE YEAR(dia) = YEAR(CURDATE()) AND MONTH(dia) = MONTH(CURDATE()) " +
                "GROUP BY tipo ORDER BY total DESC";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                series.getData().add(new XYChart.Data<>(rs.getString("tipo"), rs.getDouble("total")));
            }

            TBDespesas.getData().clear();
            TBDespesas.getData().add(series);

        } catch (SQLException e) {
            System.err.println("Erro ao carregar gráfico de despesas mensais: " + e.getMessage());
            e.printStackTrace();
        }
    }
}