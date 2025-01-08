package com.jawaskrip;
import javafx.scene.chart.XYChart;
import java.sql.*;

public class DataVisualizer {

    public XYChart.Series<String, Number> getSpendingTrends() {
        XYChart.Series<String, Number> spendingData = new XYChart.Series<>();
        spendingData.setName("Spending Trends");

        String query = "SELECT amount_transacted, transaction_date_only FROM transaction WHERE account_id = ? AND transaction_type = 'Credit' ORDER BY transaction_date_only";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, CDSR.getAccountIdFromUsername(login.username));

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String category = resultSet.getString("transaction_date_only");
                double totalAmount = resultSet.getDouble("amount_transacted");
                

                spendingData.getData().add(new XYChart.Data<>(category, totalAmount));
            }

        } catch (SQLException e) {
            System.err.println("Database error while fetching spending trends: " + e.getMessage());
        }
        return spendingData;
    }



    public XYChart.Series<String, Number> getSavingsGrowth(int userId) {
        XYChart.Series<String, Number> savingsData = new XYChart.Series<>();
        savingsData.setName("Savings Growth for User: " + userId);
        String query = "SELECT DATE_FORMAT(date, '%Y-%m') AS month, SUM(amount) AS total_savings " +
                       "FROM transactions WHERE user_id = ? AND transaction_type = 'savings' GROUP BY month";
        try (Connection connection = DatabaseUtil.getConnection(); 
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String month = resultSet.getString("month");
                    double totalSavings = resultSet.getDouble("total_savings");
                    savingsData.getData().add(new XYChart.Data<>(month, totalSavings));
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error while fetching savings growth: " + e.getMessage());
        }
        return savingsData;
    }

    public XYChart.Series<String, Number> getLoanRepayments(int userId) {
        XYChart.Series<String, Number> loanData = new XYChart.Series<>();
        loanData.setName("Loan Repayments for User: " + userId);
        String query = "SELECT DATE_FORMAT(date, '%Y-%m') AS month, SUM(amount) AS total_repayment " +
                       "FROM transactions WHERE user_id = ? AND transaction_type = 'loan' GROUP BY month";
        try (Connection connection = DatabaseUtil.getConnection(); 
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String month = resultSet.getString("month");
                    double totalRepayment = resultSet.getDouble("total_repayment");
                    loanData.getData().add(new XYChart.Data<>(month, totalRepayment));
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error while fetching loan repayments: " + e.getMessage());
        }
        return loanData;
    }
}