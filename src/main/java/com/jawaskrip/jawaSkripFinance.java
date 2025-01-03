package com.jawaskrip;
/*
import java.util.Map;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

public class jawaSkripFinance{
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

    public static Map<String, Double> predictMonthlyInterest(double deposit) {
        // Bank interest rates based on the table
        Map<String, Double> bankInterestRates = new HashMap<>();
        bankInterestRates.put("RHB", 2.6);
        bankInterestRates.put("Maybank", 2.5);
        bankInterestRates.put("Hong Leong", 2.3);
        bankInterestRates.put("Alliance", 2.85);
        bankInterestRates.put("AmBank", 2.55);
        bankInterestRates.put("Standard Chartered", 2.65);

        // Map to store monthly interest earned by each bank
        Map<String, Double> monthlyInterestMap = new HashMap<>();

        for (Map.Entry<String, Double> entry : bankInterestRates.entrySet()) {
            String bankName = entry.getKey();
            double interestRate = entry.getValue();
            // Calculate monthly interest
            double monthlyInterest = (deposit * interestRate) / 12 / 100; // Convert % to decimal
            monthlyInterestMap.put(bankName, monthlyInterest);
        }

        return monthlyInterestMap;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int x = 0, Cl = 0, schedule_pay = 0, exit = 1;
        double principal_amount, interest_rate, repayment_period, total_repayment=0, payment_per_period=0;
        do{
            System.out.println("Click 1 for credit loan, 2 for Deposit Interest Predictor");
            x = input.nextInt();
            switch(x){
            
                case 1:
                    System.out.println("Click 1 to apply, 2 to repay");
                    Cl = input.nextInt();

                    switch(Cl){
                        case 1:
                            System.out.println("Enter principal amount");
                            principal_amount = input.nextDouble();
                            System.out.println("Enter interest rate");
                            interest_rate = input.nextDouble();
                            System.out.println("Enter repayment period");
                            repayment_period = input.nextDouble();
                            total_repayment = calculateTotalRepayment(principal_amount, interest_rate, repayment_period);
                            System.out.println("The total repayment value is RM" + total_repayment);

                            System.out.println("Do you want to schedule payment monthly (1) or quarternerly (2) ?");
                            schedule_pay = input.nextInt();

                            switch(schedule_pay){
                                case 1:
                                    payment_per_period = total_repayment / (repayment_period * 12);
                                    System.out.println("You need to pay RM"+ payment_per_period + " per month ");
                                    break;
                                case 2: 
                                    payment_per_period = total_repayment / (repayment_period * 4);
                                    System.out.println("You need to pay RM"+ payment_per_period + " per quarter ");
                                    break;
                                default:
                                    System.out.println("Error");
                            }
                            break;

                        case 2 :
                            System.out.printf("Please pay the following amount: RM%.2f", payment_per_period);
                            break;
                        default : 
                            System.out.println("Error");
                    break;
                }

                case 2:
                    Map<String, Double> monthlyInterest = predictMonthlyInterest(total_repayment);

                    System.out.println("Monthly Interest Prediction:");
                    for (Map.Entry<String, Double> entry : monthlyInterest.entrySet()) {
                    System.out.printf("%s: RM %.2f%n", entry.getKey(), entry.getValue());
                    }
                    break;
                default:
                    System.out.println("Error");
            }
            System.out.println("Press 0 to exit system, press other keys to continue");
            exit = input.nextInt();
        }while(exit!=0);
    input.close();
}
}
*/



import java.util.Map;
import java.util.Scanner;
import java.sql.*;
import java.util.HashMap;
import java.time.*;

public class jawaSkripFinance {

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
        System.out.println("1. Apply\n2. Repay");
        Cl = input.nextInt();

        switch (Cl) {
            case 1:
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

                        String updateSQL = "INSERT TO loan (user_id, principal_amount, annual_interest_rate, repayment_period, payment_per_period, total_repayment, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
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

                        String updateSQL2 = "INSERT TO loan (user_id, principal_amount, annual_interest_rate, repayment_period, payment_per_period, total_repayment, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
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
                System.out.printf("Please pay the following amount: RM%.2f%n", payment_per_period);

                

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



/*
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int x = 0, Cl = 0, schedule_pay = 0, exit = 1;
        double principal_amount, interest_rate, repayment_period, deposit_period, deposit_amount;
        double total_repayment = 0, payment_per_period = 0;

        do {
            System.out.println("1. Credit loan\n2. Deposit Interest Predictor");
            x = input.nextInt();
            switch (x) {
                case 1:
                    System.out.println("1. Apply\n2. Repay");
                    Cl = input.nextInt();

                    switch (Cl) {
                        case 1:
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
                                    break;
                                case 2:
                                    payment_per_period = total_repayment / (repayment_period * 4);
                                    System.out.println("You need to pay RM" + payment_per_period + " per quarter");
                                    break;
                                default:
                                    System.out.println("Error: Invalid choice.");
                            }
                            break;

                        case 2:
                            System.out.printf("Please pay the following amount: RM%.2f%n", payment_per_period);
                            break;
                        default:
                            System.out.println("Error: Invalid choice.");
                    }
                    break;

                case 2:
                    System.out.println("Enter deposit amount:");
                    deposit_amount = input.nextDouble();
                    System.out.println("Enter deposit period (in years):");
                    deposit_period = input.nextDouble();

                    Map<String, Double> totalInterest = calculateDepositInterest(deposit_amount, deposit_period);

                    System.out.println("Deposit Interest Prediction:");
                    for (Map.Entry<String, Double> entry : totalInterest.entrySet()) {
                        System.out.printf("%s: RM %.2f%n", entry.getKey(), entry.getValue());
                    }
                    break;

                default:
                    System.out.println("Error: Invalid choice.");
            }
            System.out.println("Press 0 to exit system, press any other key to continue");
            exit = input.nextInt();
        } while (exit != 0);

        input.close();
    }
*/
}
