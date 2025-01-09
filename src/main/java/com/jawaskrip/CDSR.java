package com.jawaskrip;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class CDSR {

    static double balance=getBalanceFromUsername(login.username);
    static double savingBalance = getSavingsBalanceFromUserID(getUserIDFromUsername(login.username));
    static double loanBalance = getLoanBalanceFromLoanID(jawaSkripFinance.getLoanIDFromUserID(getUserIDFromUsername(login.username)));

    static boolean svngs = getSvgStatusFromUserID(getUserIDFromUsername(login.username));

    static int save = getSvgPercentageFromUserID(getUserIDFromUsername(login.username));
    
    
    public static double getBalanceFromUsername(String username) {
        String query = "SELECT a.acc_amount FROM profile p JOIN account a ON p.user_id = a.user_id WHERE p.username = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getDouble("acc_amount"); // Retrieve account_id
            } else {
                System.out.println("No account found for username: " + username);
                return 0;  // Invalid account if no match is found
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving account ID: " + e.getMessage());
            return -1;
        }
    }

    public static double getSavingsBalanceFromUserID(int userID) {
        String query = "SELECT svg_amount FROM savings WHERE user_id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getDouble("svg_amount"); 
            } else {
                return 0;  // Invalid account if no match is found
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving savings balance: " + e.getMessage());
            return -1;
        }
    }

    public static double getLoanBalanceFromLoanID(int loanID) {
        String query = "SELECT remaining_amount FROM loan_repayment WHERE loan_id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, loanID);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getDouble("remaining_amount"); 
            } else {
                return 0;  // Invalid account if no match is found
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving loan balance: " + e.getMessage());
            return -1;
        }
    }

    public static int getAccountIdFromUsername(String username) {
        String query = "SELECT a.account_id FROM profile p JOIN account a ON p.user_id = a.user_id WHERE p.username = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt("account_id"); // Retrieve account_id
            } else {
                System.out.println("No account found for username: " + username);
                return -1;  // Invalid account if no match is found
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving account ID: " + e.getMessage());
            return -1;
        }

    }

    public static int getUserIDFromUsername(String username) {
        String query = "SELECT user_id FROM profile WHERE username = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt("user_id"); // Retrieve user_id
            } else {
                System.out.println("No user found for username: " + username);
                return -1;  // Invalid user if no match is found
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving user ID: " + e.getMessage());
            return -1;
        }
    }


    public static int getSavingsIDFromUserID(int userID) {
        String query = "SELECT savings_id FROM savings WHERE user_id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt("savings_id"); // Retrieve savings_id
            } else {
                System.out.println("No savings_id found for user: " + userID);
                return -1;  // Invalid user if no match is found
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving savings_id: " + e.getMessage());
            return -1;
        }
    }

    public static boolean getSvgStatusFromUserID(int UserID) {
        String query = "SELECT svg_status FROM savings WHERE user_id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, UserID);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getBoolean("svg_status"); // Retrieve svg_status
            } else {
                System.out.println("No user found for username svgstat: " + UserID);
                return false;  // Invalid user if no match is found
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving user ID: " + e.getMessage());
            return false;
        }
    }

    public static int getSvgPercentageFromUserID(int UserID) {
        String query = "SELECT svg_percentage FROM savings WHERE user_id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, UserID);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt("svg_percentage"); // Retrieve svg_percentage
            } else {
                System.out.println("No user found for username svgperc: " + UserID);
                return -1;  // Invalid user if no match is found
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving user ID: " + e.getMessage());
            return -1;
        }
    }


    public static boolean compareDates(int loanID) {
        String query = "SELECT end_date FROM loan WHERE loan_id = ?";

        try (Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, loanID);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                // Retrieve the date from the database
                Date endDate = resultSet.getDate("end_date"); // Or use getTimestamp if it's a timestamp

                // Convert to LocalDate or LocalDateTime
                LocalDate dbDate = endDate.toLocalDate();

                // Get current date
                LocalDate currentDate = LocalDate.now();

                // Compare dates
                if (dbDate.isAfter(currentDate)) {
                    return true;
                } else {
                    System.out.println("You have an outstanding loan. Please repay it first.");
                    return false;
                }
            } else {
                return true;
            }
        } catch (SQLException e) {
            return true;
        }
    }
    

    public static void credit(){
        boolean truth = true;
        String desc = "";

        System.out.println("==Credit==");
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter amount: ");
        double cre = scan.nextDouble();
        scan.nextLine();

        while(truth == true){
            System.out.print("Description : ");
            desc = scan.nextLine();

            if( desc.length() > 100){
                System.out.print("\nDescription is too long. Please write it again. \n");
            }
            else{
                truth=false;
            }
        }
       
        if(cre>= 0 && cre<= balance){
            balance-=cre;
            System.out.println("Credit successfully recorded !! \n");

            String updateSQL = "UPDATE account SET acc_amount = ? WHERE account_id = ?";
            String updateSQL2 = "INSERT INTO transaction (account_id, amount_transacted, transaction_type, transaction_date, description, transaction_date_only) VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection connection = DatabaseUtil.getConnection();  // Automatically closes the connection
                PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
                PreparedStatement preparedStatement2 = connection.prepareStatement(updateSQL2)) {
    
                // Set the parameters for the SQL query
                preparedStatement.setDouble(1, balance); // Set the new balance
                preparedStatement.setInt(2, getAccountIdFromUsername(login.username));     // Set the account ID
    
                // Execute the update query
                int rowsAffected = preparedStatement.executeUpdate(); // Returns the number of rows affected
    
                preparedStatement2.setInt(1, getAccountIdFromUsername(login.username));
                preparedStatement2.setDouble(2, cre);
                preparedStatement2.setString(3, "Credit");
                preparedStatement2.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
                preparedStatement2.setString(5, desc);
                preparedStatement2.setDate(6, Date.valueOf(LocalDate.now()));


                int rowsAffected2 = preparedStatement2.executeUpdate(); // Returns the number of rows affected

                if (rowsAffected > 0 && rowsAffected2 > 0) {
                    System.out.println("Account balance updated successfully.");
                } else {
                    System.out.println("Failed to update account balance.");
                }
            } catch (SQLException e) {
                System.out.println("Error updating account balance: " + e.getMessage());
            }

        }
        else{
            System.out.println("Transaction failed \n");
        }

    }
            
    public static void debit() {
        double extra=0;
        boolean truth = true;
        String desc = "";
        System.out.println("==Debit==");
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter amount: ");
        double deb = scan.nextDouble();
        scan.nextLine();

        if(svngs==true){
            extra = (save/100.0)*deb;
            
            String updateSQL = "UPDATE savings SET svg_amount = svg_amount + ? WHERE user_id = ?";
            String insertSQL = "INSERT INTO savings_transaction (savings_id, amount_saved, transaction_date, transaction_date_only) VALUES (?, ?, ?, ?)";
            try (Connection connection = DatabaseUtil.getConnection();  // Automatically closes the connection
                PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
                PreparedStatement preparedStatement2 = connection.prepareStatement(insertSQL)) {
    
                // Set the parameters for the SQL query
                preparedStatement.setDouble(1, extra); // Set the new balance
                preparedStatement.setInt(2, getUserIDFromUsername(login.username));     // Set the account ID
                // Execute the update query
                int rowsAffected = preparedStatement.executeUpdate(); // Returns the number of rows affected
    
                preparedStatement2.setInt(1, getSavingsIDFromUserID(getUserIDFromUsername(login.username)));
                preparedStatement2.setDouble(2, extra);
                preparedStatement2.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
                preparedStatement2.setDate(4, Date.valueOf(LocalDate.now()));

                int rowsAffected2 = preparedStatement2.executeUpdate(); // Returns the number of rows affected

                if (rowsAffected > 0 && rowsAffected2 > 0) {
                    //System.out.println("Savings updated successfully.");
                } else {
                    System.out.println("Failed to update savings.");
                }
            } catch (SQLException e) {
                System.out.println("Error updating savings: " + e.getMessage());
            }
        }

        while(truth == true){
            System.out.print("Description : ");
            desc = scan.nextLine();

            if( desc.length() > 100){
                System.out.print("\nDescription is too long. Please write it again. \n");
            }
            else{
                truth=false;
            }
        }
        

        if(deb>= 0 && deb<= 50000){
            balance+=deb;
            System.out.println("Debit successfully recorded !! \n");


            String updateSQL = "UPDATE account SET acc_amount = ? WHERE account_id = ?";
            String updateSQL2 = "INSERT INTO transaction (account_id, amount_transacted, transaction_type, transaction_date, description, transaction_date_only) VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection connection = DatabaseUtil.getConnection();  // Automatically closes the connection
                 PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
                 PreparedStatement preparedStatement2 = connection.prepareStatement(updateSQL2)) {
    
                // Set the parameters for the SQL query
                preparedStatement.setDouble(1, balance); // Set the new balance
                preparedStatement.setInt(2, getAccountIdFromUsername(login.username));     // Set the account ID
                // Execute the update query
                int rowsAffected = preparedStatement.executeUpdate(); // Returns the number of rows affected

                preparedStatement2.setInt(1, getAccountIdFromUsername(login.username));
                preparedStatement2.setDouble(2, deb);
                preparedStatement2.setString(3, "Debit");
                preparedStatement2.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
                preparedStatement2.setString(5, desc);
                preparedStatement2.setDate(6, Date.valueOf(LocalDate.now()));

                int rowsAffected2 = preparedStatement2.executeUpdate(); // Returns the number of rows affected
    
                if (rowsAffected > 0 && rowsAffected2 > 0) {
                    System.out.println("Account balance updated successfully.");
                } else {
                    System.out.println("Failed to update account balance.");
                }
    
            } catch (SQLException e) {
                System.out.println("Error updating account balance: " + e.getMessage());
            }

        }
        else{
            System.out.println("Transaction failed \n");
        }
    }

    public static void Savings(){
        String resp;
        Scanner scan = new Scanner(System.in);
        while(svngs==false){
            System.out.println("==Savings==\n");
            System.out.print("Are you sure you want to activate it? (Y/N) : ");
            resp=scan.nextLine();

            if(resp.trim().equalsIgnoreCase("Y")){
                System.out.print("Please enter the percentage you wish to deduct from the next debit: ");
                save = scan.nextInt();

                String updateSQL = "UPDATE savings SET svg_status = TRUE, svg_percentage = ? WHERE user_id = ?";
                try (Connection connection = DatabaseUtil.getConnection();  // Automatically closes the connection
                    PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
    
                    // Set the parameters for the SQL query
                    preparedStatement.setInt(1, save); 
                    preparedStatement.setInt(2, getUserIDFromUsername(login.username)); 

                    // Execute the update query
                    int rowsAffected = preparedStatement.executeUpdate(); // Returns the number of rows affected
    
                    if (rowsAffected > 0) {
                        System.out.println("Savings activated successfully.");
                    } else {
                        System.out.println("Failed to activate savings.");
                    }
                } catch (SQLException e) {
                    System.out.println("Error activating savings: " + e.getMessage());
                }

                svngs=true;
                continue;
            }
            else{
                return;
            }
        }
    }


    public static void history(){
        Scanner scan = new Scanner(System.in);
        boolean repeat=true;
        int choice=0;

        String query = "SELECT amount_transacted, transaction_type, transaction_date, description FROM transaction WHERE account_id = ?";
        while(repeat){
            try (Connection connection = DatabaseUtil.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                // Set the parameter for the SQL query
                preparedStatement.setInt(1, getAccountIdFromUsername(login.username));
                
                // Execute the query
                ResultSet resultSet = preparedStatement.executeQuery();

                // Display header
                System.out.println("=====================================");
                System.out.println("  Transactions for " + login.username);
                System.out.println("=====================================");
                System.out.printf("| %-4s | %-10s | %-8s | %-25s | %-21s |%n", 
                                "ID", "Amount", "Type", "Description", "Date");
                System.out.println("---------------------------------------------------------------");

                // Check if there are any results
                boolean hasTransactions = false;

                // Iterate through results and display them
                int id = 1;
                while (resultSet.next()) {
                    hasTransactions = true;
                    int amount = resultSet.getInt("amount_transacted");
                    String type = resultSet.getString("transaction_type");
                    String description = resultSet.getString("description");
                    Timestamp date = resultSet.getTimestamp("transaction_date");

                    // Print transaction details in a tabular format
                    System.out.printf("| %-4s | %-10s | %-8s | %-25s | %-21s |%n",
                                    id, amount, type, description, date);
                    id++;
                }

                // If no transactions were found
                if (!hasTransactions) {
                    System.out.println("| No transactions found for this user.                       |");
                    repeat=false;
                }

                // Footer
                System.out.println("=====================================");

                System.out.println("Filtering option\n1.Date range\n2.Amount range\n3.Transaction type\n==================\nSorting option\n4.Sort by amount\n5.Sort by date\n0.Exit");
                choice = scan.nextInt();
                scan.nextLine();
                
                switch(choice){
                    case 1:
                    System.out.print("Enter start date (yyyy-mm-dd): ");
                    String startDate = scan.nextLine();
                    System.out.print("Enter end date (yyyy-mm-dd): ");
                    String endDate = scan.nextLine();
                    query = "SELECT amount_transacted, transaction_type, transaction_date, description FROM transaction WHERE account_id = ? AND transaction_date_only BETWEEN '" + startDate +"' AND '" + endDate +"' ORDER BY transaction_date";
                    break;
                    case 2:
                    System.out.print("Enter start amount: ");
                    int startAmount = scan.nextInt();
                    System.out.print("Enter end amount: ");
                    int endAmount = scan.nextInt();
                    query = "SELECT amount_transacted, transaction_type, transaction_date, description FROM transaction WHERE account_id = ? AND amount_transacted BETWEEN " + startAmount +" AND " + endAmount +" ORDER BY amount_transacted";
                    break;
                    case 3:
                    System.out.print("Enter transaction type (Credit/Debit): ");
                    String transType = scan.nextLine();
                    query = "SELECT amount_transacted, transaction_type, transaction_date, description FROM transaction WHERE account_id = ? AND transaction_type = '"+ transType +"' ORDER BY transaction_date";
                    break;
                    case 4:
                    query = "SELECT amount_transacted, transaction_type, transaction_date, description FROM transaction WHERE account_id = ? ORDER BY amount_transacted";
                    break;
                    case 5:
                    query = "SELECT amount_transacted, transaction_type, transaction_date, description FROM transaction WHERE account_id = ? ORDER BY transaction_date";
                    break;
                    default:
                    repeat=false;
                    
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}