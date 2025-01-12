package com.jawaskrip;

import java.sql.*;
import java.util.Scanner;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class login {
    public static String username;

    // Method to register user
    public static void registerUser(Scanner scanner) {
        System.out.print("Enter a username: ");
        username = scanner.nextLine();

        String email; 
        while(true){
        System.out.print("Enter an email: ");
         email = scanner.nextLine();

         if (email.endsWith("@gmail.com")){
            break; 
        }else{
            System.out.println("Invalid email address!");
        }
        }

        String password; 
       while(true){
        System.out.print("Enter a password(password must have special characters and not more than 12 characters): ");
         password = scanner.nextLine();
        
        System.out.print("Enter password again: ");
        String password2 = scanner.nextLine();
        System.out.println("");

        if (isPasswordValid(password,password2)) {
            break;
        } else {
            System.out.println("Invalid password. Please try again.");
        }
       }

        // Hash password for security
        String hashedPassword = hashPassword(password);

        String insertQuery = "INSERT INTO profile (username, email,password) VALUES (?, ?, ?)";
        String insertAccountQuery = "INSERT INTO account (user_id) VALUES (?)";
        String insertSavingsQuery = "INSERT INTO savings (user_id) VALUES (?)";


        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery,  Statement.RETURN_GENERATED_KEYS);
             PreparedStatement accountStatement = connection.prepareStatement(insertAccountQuery);
             PreparedStatement savingsStatement = connection.prepareStatement(insertSavingsQuery)) {


            // Set parameters for the query
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, hashedPassword);
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                int userId = generatedKeys.getInt(1);
                // Step 3: Insert into account table using the retrieved user_id
                accountStatement.setInt(1, userId);
                accountStatement.executeUpdate();

                savingsStatement.setInt(1, userId);
                savingsStatement.executeUpdate();

            System.out.println("Registration successful!");
            } else {
                System.out.println("Registration failed.");
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                System.out.println("Error: Username already exists.");
            } else {
                e.printStackTrace();
            }
        }
    }

    // Method to log in a user
    public static void loginUser(Scanner scanner) {
        boolean success = false;
        while (!success) {
            System.out.print("Enter your username (Enter ! to escape): ");
            username = scanner.nextLine();

            if (username.equals("!")){
                break; 
            }
            else {
                System.out.print("Enter your password: ");
                String password = scanner.nextLine();

                // Hash the entered password to compare with the stored hash
                String hashedPassword = hashPassword(password);

                String selectQuery = "SELECT * FROM profile WHERE username = ? AND password = ?";

                try (Connection connection = DatabaseUtil.getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {

                    // Set parameters for the query
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, hashedPassword);

                    // Execute the query
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        System.out.println("Login successful! Welcome, " + username + "!\n");
                        success = true; // Login successful, exit loop
                    } else {
                        System.out.println("Invalid username or password. Please try again.");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

     // Method to validate the password
     private static boolean isPasswordValid(String password, String password2) {
        // Check length and presence of at least one special character
        String specialCharacterPattern = ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*";
        return password.length() <= 12 && password.matches(specialCharacterPattern) && password.equals(password2);
    }
    /* 
    // Hash the password using SHA-256 
    public static String hashPassword(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = messageDigest.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
*/
    // Hash the password using BCrypt
    public static String hashPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
}