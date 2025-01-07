package com.jawaskrip;


import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        System.out.println("== Ledger System == ");

        do {
            System.out.print("Login or Register \n1. Login \n2. Register \n3. Exit \n->");
            choice = sc.nextInt();
            sc.nextLine();  // Consume newline character
            // Login page
            if (choice == 1) {
                login.loginUser(sc);
            }
            // Register page
            else if (choice == 2) {
                login.registerUser(sc);
            }
            else if (choice == 3){
                System.exit(0);
                break; 
            }
        } while (choice != 1);




        boolean truth = true;
        while(truth== true){
            System.out.printf("Account balance: %.2f\n", CDSR.balance);

            System.out.println("==Transactions==");
            System.out.println("1.Credit \n2.Debit\n3.Savings\n4.Credit Loan\n5.Deposit Interest Predictor\n6.Spending Trends\n7.Saving growth\n8.Loan Repayment\n9.Exit");

            choice = sc.nextInt();

            switch(choice){
                case 1:
                if (!CDSR.compareDates(jawaSkripFinance.getLoanIDFromUserID(CDSR.getUserIDFromUsername(login.username)))) break;
                CDSR.credit();
                break;
                case 2:
                if (!CDSR.compareDates(jawaSkripFinance.getLoanIDFromUserID(CDSR.getUserIDFromUsername(login.username)))) break;
                CDSR.debit();
                break;
                case 3:
                CDSR.Savings();
                break;
                case 4:
                jawaSkripFinance.credLoan();
                break;
                case 5:
                jawaSkripFinance.depositInterestPredictor();
                break;
                case 6:
                //jawaSkripFinance.spendingTrends();
                break;
                case 7:
                //jawaSkripFinance.savingGrowth();
                break;
                case 8:
                //jawaSkripFinance.loanRepayment();
                break;
                case 9:
                truth=false;
                break;
                default:
                System.out.println("Invalid input \n");
                break;
            }

        }
        sc.close();  // Close the scanner object
    }

}


