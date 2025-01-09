package com.jawaskrip;

import java.util.Map;
import java.util.Scanner;
import java.sql.*;
import java.util.HashMap;
import java.time.*;
import java.time.temporal.ChronoUnit;

public class jawaSkripFinance {

    public static int getLoanIDFromUserID(int UserID) {
        String query = "SELECT loan_id FROM loan WHERE user_id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, UserID);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt("loan_id"); // Retrieve loan_id if user has a loan
            } else {
                System.out.println("No loan found for user_id1 : " + UserID);
                return -1;  // Invalid user if no match is found
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving user ID: " + e.getMessage());
            return -1;
        }
    }

    public static double getTotalRepaymentFromLoanID(int LoanID) {
        String query = "SELECT total_repayment FROM loan WHERE loan_id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, LoanID);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt("total_repayment"); // Retrieve total_repayment if user has a loan
            } else {
                System.out.println("No loan found for user_id2: " + LoanID);
                return -1;  // Invalid user if no match is found
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving user ID: " + e.getMessage());
            return -1;
        }
    }

    public static double getTotalAmountPaidFromLoanID(int LoanID) {
        String query = "SELECT total_amount_paid FROM loan WHERE loan_id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, LoanID);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt("total_amount_paid"); // Retrieve total_repayment if user has a loan
            } else {
                System.out.println("No loan found for repayment_id3: " + LoanID);
                return -1;  // Invalid user if no match is found
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving user ID: " + e.getMessage());
            return -1;
        }
    }

    public static double calculateTotalRepayment(double principalAmount, double annualInterestRate, double repaymentPeriod) {
        // Convert annual interest rate from percentage to decimal
        double monthlyInterestRate = (annualInterestRate / 100) / 12;

        // Convert repayment period to months
        double totalMonths = repaymentPeriod * 12;

        // Calculate monthly payment using the formula for an amortizing loan
        double monthlyPayment = 
            (principalAmount * monthlyInterestRate) / 
            (1 - Math.pow(1 + monthlyInterestRate, -totalMonths));

        // Calculate total repayment amount
        double totalRepayment = monthlyPayment * totalMonths;
        return totalRepayment;
    }

    public static Map<String, Double> calculateDepositInterest(double deposit, double depositPeriod) {
        // Bank interest rates based on the table
        Map<String, Double> bankInterestRates = new HashMap<>();
        bankInterestRates.put("RHB", 2.6);
        bankInterestRates.put("Maybank", 2.5);
        bankInterestRates.put("Hong Leong", 2.3);
        bankInterestRates.put("Alliance", 2.85);
        bankInterestRates.put("AmBank", 2.55);
        bankInterestRates.put("Standard Chartered", 2.65);

        // Map to store total interest earned by each bank
        Map<String, Double> totalInterestMap = new HashMap<>();

        for (Map.Entry<String, Double> entry : bankInterestRates.entrySet()) {
            String bankName = entry.getKey();
            double interestRate = entry.getValue();
            // Calculate total interest for the deposit period
            double totalInterest = (deposit * interestRate * depositPeriod) / 100; // Convert % to decimal
            totalInterestMap.put(bankName, totalInterest);
        }
        return totalInterestMap;
    }

    public static void credLoan() {
        Scanner input = new Scanner(System.in);
        int Cl = 0, schedule_pay = 0;
        double principal_amount, interest_rate, repayment_period;
        double total_repayment = 0, payment_per_period = 0;
        System.out.println("1.Apply\n2.Repay");
        Cl = input.nextInt();

        switch (Cl) {
            case 1:
                //check if user has an existing loan
                if (getLoanIDFromUserID(CDSR.getUserIDFromUsername(login.username)) != -1) {
                    System.out.println("Error: User already has an existing loan.");
                    break;
                }

                System.out.println("Enter principal amount"); //loan: principal_amount 
                principal_amount = input.nextDouble();
                System.out.println("Enter interest rate"); //loan: interest_rate
                interest_rate = input.nextDouble();
                System.out.println("Enter repayment period (in years)");
                repayment_period = input.nextDouble();
                total_repayment = calculateTotalRepayment(principal_amount, interest_rate, repayment_period);
                System.out.println("The total repayment value is RM" + total_repayment);

                System.out.println("Do you want to schedule payment monthly (1) or quarterly (2)?");
                schedule_pay = input.nextInt();
                switch (schedule_pay) {
                    case 1:
                        payment_per_period = total_repayment / (repayment_period * 12);
                        System.out.println("You need to pay RM" + payment_per_period + " per month");

                        String updateSQL = "INSERT INTO loan (user_id, principal_amount, annual_interest_rate, repayment_period, payment_per_period, total_repayment, start_date, end_date, pay_interval) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        try (Connection connection = DatabaseUtil.getConnection();
                        PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {

                        // Set parameters for the query
                        preparedStatement.setInt(1, CDSR.getUserIDFromUsername(login.username));
                        preparedStatement.setDouble(2, principal_amount);
                        preparedStatement.setDouble(3, interest_rate);
                        preparedStatement.setDouble(4, repayment_period);
                        preparedStatement.setDouble(5, payment_per_period);
                        preparedStatement.setDouble(6, total_repayment);
                        preparedStatement.setDate(7, Date.valueOf(LocalDate.now()));
                        preparedStatement.setDate(8, Date.valueOf(LocalDate.now().plusMonths((long) (repayment_period * 12))));
                        preparedStatement.setString(9, "Monthly");
                        // Execute the query
                        int rowsAffected = preparedStatement.executeUpdate();

                        if (rowsAffected > 0) {
                            System.out.println("Loan application successful.");
                        } else {
                            System.out.println("Loan application failed.");
                        }

                        } catch (SQLException e) {
                        e.printStackTrace();
                        }

                        break;
                    case 2:
                        payment_per_period = total_repayment / (repayment_period * 4);
                        System.out.println("You need to pay RM" + payment_per_period + " per quarter");

                        String updateSQL2 = "INSERT TO loan (user_id, principal_amount, annual_interest_rate, repayment_period, payment_per_period, total_repayment, start_date, end_date, pay_interval) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        try (Connection connection = DatabaseUtil.getConnection();
                        PreparedStatement preparedStatement = connection.prepareStatement(updateSQL2)) {

                        // Set parameters for the query
                        preparedStatement.setInt(1, CDSR.getUserIDFromUsername(login.username));
                        preparedStatement.setDouble(2, principal_amount);
                        preparedStatement.setDouble(3, interest_rate);
                        preparedStatement.setDouble(4, repayment_period);
                        preparedStatement.setDouble(5, payment_per_period);
                        preparedStatement.setDouble(6, total_repayment);
                        preparedStatement.setDate(7, Date.valueOf(LocalDate.now()));
                        preparedStatement.setDate(8, Date.valueOf(LocalDate.now().plusMonths((long) (repayment_period * 12))));
                        preparedStatement.setString(9, "Quarterly");

                        // Execute the query
                        int rowsAffected = preparedStatement.executeUpdate();

                        if (rowsAffected > 0) {
                            System.out.println("Loan application successful.");
                        } else {
                            System.out.println("Loan application failed.");
                        }

                        } catch (SQLException e) {
                        e.printStackTrace();
                        }

                        break;
                    default:
                        System.out.println("Error: Invalid choice.");
                }
                break;

            case 2:
                String selectQuery = "SELECT payment_per_period FROM loan WHERE user_id = ?";    
                try (Connection connection = DatabaseUtil.getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {

                    // Set parameters for the query
                    preparedStatement.setInt(1, CDSR.getUserIDFromUsername(login.username));

                    // Execute the query
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        payment_per_period = resultSet.getDouble("payment_per_period");
                    } else {
                        System.out.println("Error: Loan not found.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                System.out.printf("Please pay the following amount: RM%.2f%n", payment_per_period);
                System.out.println("Enter amount you want to pay:");

                double payment = input.nextDouble();

                String insertSQL3 = "INSERT INTO loan_repayment (loan_id, payment_amount, payment_date, total_amount_paid) VALUES (?, ?, ?, ?)";
                String updateSQL4 = "UPDATE loan SET remaining_amount = ?, total_amount_paid = total_amount_paid + ? WHERE loan_id = ?";

                try (Connection connection = DatabaseUtil.getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement(insertSQL3);
                    PreparedStatement preparedStatement2 = connection.prepareStatement(updateSQL4)) {

                    // Set parameters for the query
                    preparedStatement.setInt(1, getLoanIDFromUserID(CDSR.getUserIDFromUsername(login.username)));
                    preparedStatement.setDouble(2, payment);
                    preparedStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
                    preparedStatement.setDouble(4, getTotalAmountPaidFromLoanID(getLoanIDFromUserID(CDSR.getUserIDFromUsername(login.username))));
                    int rowsAffected = preparedStatement.executeUpdate();

                    preparedStatement2.setDouble(1, getTotalRepaymentFromLoanID(getLoanIDFromUserID(CDSR.getUserIDFromUsername(login.username))) - getTotalAmountPaidFromLoanID(getLoanIDFromUserID(CDSR.getUserIDFromUsername(login.username))));
                    preparedStatement2.setDouble(2, payment);
                    preparedStatement2.setDouble(3, getLoanIDFromUserID(CDSR.getUserIDFromUsername(login.username)));
                    int rowsAffected2 = preparedStatement2.executeUpdate();

                    if (rowsAffected > 0 && rowsAffected2 > 0) {
                        System.out.println("Repayment successful.");
                        // Check if loan is fully repaid
                        if (getTotalAmountPaidFromLoanID(getLoanIDFromUserID(CDSR.getUserIDFromUsername(login.username))) >= getTotalRepaymentFromLoanID(getLoanIDFromUserID(CDSR.getUserIDFromUsername(login.username)))) {
                            String updateSQL6 = "UPDATE loan SET loan_status = 'Paid' WHERE loan_id = ?";
                            try (PreparedStatement preparedStatement4 = connection.prepareStatement(updateSQL6)) {
                                preparedStatement4.setInt(1, getLoanIDFromUserID(CDSR.getUserIDFromUsername(login.username)));
                                int rowsAffected4 = preparedStatement4.executeUpdate();
                                if (rowsAffected4 > 0) {
                                    System.out.println("Loan fully repaid.");
                                } else {
                                    System.out.println("Error updating loan status.");
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }

                    } else {
                        System.out.println("Repayment failed.");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }


                break;
            default:
                System.out.println("Error: Invalid choice.");
        }
    }

    public static void depositInterestPredictor() {
        Scanner input = new Scanner(System.in);
        double deposit_amount, deposit_period;
        System.out.println("Enter deposit amount:");
        deposit_amount = input.nextDouble();
        System.out.println("Enter deposit period (in years):");
        deposit_period = input.nextDouble();

        Map<String, Double> totalInterest = calculateDepositInterest(deposit_amount, deposit_period);

        System.out.println("Deposit Interest Prediction:");
        for (Map.Entry<String, Double> entry : totalInterest.entrySet()) {
            System.out.printf("%s: RM %.2f%n", entry.getKey(), entry.getValue());
        }
    }



}
