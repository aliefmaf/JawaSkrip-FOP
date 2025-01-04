-- MySQL Database Dump

-- Create Tables

CREATE TABLE `account` (
    `account_id` INT NOT NULL AUTO_INCREMENT,
    `acc_amount` DECIMAL(15,2) DEFAULT 0.00,
    `user_id` INT NOT NULL,
    PRIMARY KEY (`account_id`),
    UNIQUE (`user_id`)
);

CREATE TABLE `loan` (
    `loan_id` INT NOT NULL AUTO_INCREMENT,
    `user_id` INT,
    `principal_amount` DECIMAL(15,2) NOT NULL,
    `annual_interest_rate` DECIMAL(5,2) NOT NULL,
    `repayment_period` DECIMAL NOT NULL,
    `payment_per_period` DECIMAL(15,2) NOT NULL,
    `total_repayment` DECIMAL(15,2) NOT NULL,
    `start_date` DATE NOT NULL,
    `end_date` DATE NOT NULL,
    PRIMARY KEY (`loan_id`),
    FOREIGN KEY (`user_id`) REFERENCES `profile`(`user_id`) ON DELETE CASCADE
);

CREATE TABLE `loan_repayment` (
    `repayment_id` INT NOT NULL AUTO_INCREMENT,
    `loan_id` INT,
    `payment_amount` DECIMAL(15,2) NOT NULL,
    `remaining_amount` DECIMAL(15,2) NOT NULL,
    `payment_date` DATE NOT NULL,
    PRIMARY KEY (`repayment_id`),
    FOREIGN KEY (`loan_id`) REFERENCES `loan`(`loan_id`) ON DELETE CASCADE
);

CREATE TABLE `profile` (
    `user_id` INT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(255) NOT NULL,
    `password` TEXT NOT NULL,
    PRIMARY KEY (`user_id`),
    UNIQUE (`username`)
);

CREATE TABLE `savings` (
    `savings_id` INT NOT NULL AUTO_INCREMENT,
    `user_id` INT NOT NULL,
    `svg_status` BOOLEAN DEFAULT FALSE,
    `svg_percentage` DECIMAL(5,2) DEFAULT 0,
    `svg_amount` DECIMAL(10,2) DEFAULT 0.00,
    PRIMARY KEY (`savings_id`),
    UNIQUE (`user_id`),
    FOREIGN KEY (`user_id`) REFERENCES `profile`(`user_id`) ON DELETE CASCADE
);

CREATE TABLE `transaction` (
    `transaction_id` INT NOT NULL AUTO_INCREMENT,
    `account_id` INT,
    `transaction_type` VARCHAR(20) NOT NULL,
    `amount_transacted` DECIMAL(15,2) NOT NULL,
    `transaction_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `description` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`transaction_id`),
    FOREIGN KEY (`account_id`) REFERENCES `account`(`account_id`) ON DELETE CASCADE
);

-- Insert Data

INSERT INTO `account` (`account_id`, `acc_amount`, `user_id`) VALUES (3, 1845.00, 11);

INSERT INTO `profile` (`user_id`, `username`, `password`) VALUES (11, 'skibidi', '9af15b336e6a9619928537df30b2e6a2376569fcf9d7e773eccede65606529a0');

INSERT INTO `savings` (`savings_id`, `user_id`, `svg_status`, `svg_percentage`, `svg_amount`) VALUES (1, 11, TRUE, 5.00, 0.00);

INSERT INTO `transaction` (`transaction_id`, `account_id`, `transaction_type`, `amount_transacted`, `transaction_date`, `description`) 
VALUES (3, 3, 'Debit', 1000.00, '2025-01-03 17:50:14', 'for january expenses');

INSERT INTO `transaction` (`transaction_id`, `account_id`, `transaction_type`, `amount_transacted`, `transaction_date`, `description`) 
VALUES (4, 3, 'Credit', 100.00, '2025-01-03 17:51:04', 'for food');

INSERT INTO `transaction` (`transaction_id`, `account_id`, `transaction_type`, `amount_transacted`, `transaction_date`, `description`) 
VALUES (5, 3, 'Debit', 900.00, '2025-01-04 01:32:47', 'i have money');

-- Adjust the AUTO_INCREMENT Values
ALTER TABLE `account` AUTO_INCREMENT = 3;
ALTER TABLE `loan` AUTO_INCREMENT = 1;
ALTER TABLE `loan_repayment` AUTO_INCREMENT = 1;
ALTER TABLE `profile` AUTO_INCREMENT = 11;
ALTER TABLE `savings` AUTO_INCREMENT = 1;
ALTER TABLE `transaction` AUTO_INCREMENT = 5;
