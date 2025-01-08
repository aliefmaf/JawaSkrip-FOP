/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML.java to edit this template
 */
package com.jawaskrip;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class LedgerSystem {

    private static int userId; // Static variable to store user ID

    public static void setUserId(int id) {
        userId = CDSR.getUserIDFromUsername(login.username); // Set the user ID from `App`
    }

    public static void spendingTrends() {
        Platform.runLater(() -> displayGraph("Spending Trends", createSpendingTrendsSeries()));
    }

    public static void savingGrowth() {
        Platform.runLater(() -> displayGraph("Saving Growth", createSavingsGrowthSeries()));
    }

    public static void loanRepayment() {
        Platform.runLater(() -> displayGraph("Loan Repayment", createLoanRepaymentsSeries()));
    }

    private static void displayGraph(String title, XYChart.Series<String, Number> series) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        barChart.getData().add(series);
        barChart.setTitle(title);

        Stage stage = new Stage();
        stage.setScene(new Scene(barChart, 800, 600));
        stage.setTitle(title);
        stage.show();
    }

    private static XYChart.Series<String, Number> createSpendingTrendsSeries() {
        DataVisualizer dataVisualizer = new DataVisualizer();
        return dataVisualizer.getSpendingTrends();
    }

    private static XYChart.Series<String, Number> createSavingsGrowthSeries() {
        DataVisualizer dataVisualizer = new DataVisualizer();
        return dataVisualizer.getSavingsGrowth();
    }

    private static XYChart.Series<String, Number> createLoanRepaymentsSeries() {
        DataVisualizer dataVisualizer = new DataVisualizer();
        return dataVisualizer.getLoanRepayments();
    }
}