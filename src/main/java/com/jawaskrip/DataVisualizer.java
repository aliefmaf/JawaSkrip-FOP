package com.jawaskrip;
import javafx.scene.chart.XYChart;
import java.util.Map;
import java.util.TreeMap;
import java.sql.*;
import java.text.SimpleDateFormat;

public class DataVisualizer {

    public XYChart.Series<String, Number> getSpendingTrends() {
        XYChart.Series<String, Number> spendingData = new XYChart.Series<>();
        spendingData.setName("Spending Trends");

        String query = "SELECT amount_transacted, transaction_date_only FROM transaction WHERE account_id = ? AND transaction_type = 'Credit' ORDER BY transaction_date_only ASC";
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
        String query = "SELECT payment_amount, payment_date FROM loan_repayment WHERE loan_id = ? ORDER BY payment_date ASC";
        
        try (Connection connection = DatabaseUtil.getConnection(); 
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, jawaSkripFinance.getLoanIDFromUserID(CDSR.getUserIDFromUsername(login.username)));
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                double cumulativeSum = 0;  // To keep track of the cumulative sum
                
                // Using TreeMap to store the cumulative sum for each date
                TreeMap<Date, Double> cumulativePayments = new TreeMap<>();
                
                while (resultSet.next()) {
                    double totalRepayment = resultSet.getDouble("payment_amount");  // Get the repayment amount from the database
                    Date transactionDate = resultSet.getDate("payment_date");  // Get the date from the database
                    
                    cumulativeSum += totalRepayment;  // Update cumulative sum
                    cumulativePayments.put(transactionDate, cumulativeSum);  // Store the cumulative sum with the date as the key
                }
                
                // Add the data to the chart in the same order as fetched (since it's sorted in descending order)
                for (Map.Entry<Date, Double> entry : cumulativePayments.entrySet()) {
                    // Format the date to a string if needed, or use the Date object directly as the X-axis value
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  // Custom date format
                    String formattedDate = sdf.format(entry.getKey());
                    
                    loanData.getData().add(new XYChart.Data<>(formattedDate, entry.getValue()));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Database error while fetching loan repayments: " + e.getMessage());
        }
        
        return loanData;
    }

}