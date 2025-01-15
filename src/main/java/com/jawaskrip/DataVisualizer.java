package com.jawaskrip;
import javafx.scene.chart.XYChart;
import java.util.Map;
import java.util.TreeMap;
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

            Map<String, Double> dateToAmountMap = new TreeMap<>();
            while (resultSet.next()) {
            String date = resultSet.getString("transaction_date_only");
            double amount = resultSet.getDouble("amount_transacted");

            dateToAmountMap.put(date, dateToAmountMap.getOrDefault(date, 0.0) + amount);
            }

            for (Map.Entry<String, Double> entry : dateToAmountMap.entrySet()) {
                spendingData.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

        } catch (SQLException e) {
            System.err.println("Database error while fetching spending trends: " + e.getMessage());
        }
        return spendingData;
    }

    public XYChart.Series<String, Number> getSavingsGrowth() {
        XYChart.Series<String, Number> savingsData = new XYChart.Series<>();
        savingsData.setName("Savings Growth");
        String query = "SELECT amount_saved, transaction_date_only FROM savings_transaction WHERE savings_id = ? ORDER BY transaction_date_only ASC";
        try (Connection connection = DatabaseUtil.getConnection(); 
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, CDSR.getSavingsIDFromUserID(CDSR.getUserIDFromUsername(login.username)));

            ResultSet resultSet = preparedStatement.executeQuery();

            Map<String, Double> dateToAmountMap = new TreeMap<>();
            while (resultSet.next()) {
            String date = resultSet.getString("transaction_date_only");
            double amount = resultSet.getDouble("amount_saved");

            dateToAmountMap.put(date, dateToAmountMap.getOrDefault(date, 0.0) + amount);
            }

            for (Map.Entry<String, Double> entry : dateToAmountMap.entrySet()) {
                savingsData.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

        } catch (SQLException e) {
            System.err.println("Database error while fetching savings growth: " + e.getMessage());
        }
        return savingsData;
    }

    public XYChart.Series<String, Number> getLoanRepayments() {
        XYChart.Series<String, Number> loanData = new XYChart.Series<>();
        loanData.setName("Loan Repayments");
        String query = "SELECT total_amount_paid FROM loan WHERE loan_id = ?";
        
        try (Connection connection = DatabaseUtil.getConnection(); 
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, jawaSkripFinance.getLoanIDFromUserID(CDSR.getUserIDFromUsername(login.username)));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                int i=1;
                while (resultSet.next()) {
                    double totalRepayment = resultSet.getDouble("total_amount_paid");
                    loanData.getData().add(new XYChart.Data<>(String.valueOf(i), totalRepayment));
                    i++;
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error while fetching loan repayments: " + e.getMessage());
        }
        return loanData;
    }
}