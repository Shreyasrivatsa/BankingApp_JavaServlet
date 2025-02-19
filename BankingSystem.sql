CREATE DATABASE BankingSystem;
USE BankingSystem;

CREATE TABLE Accounts (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);
-- Alter the Accounts table to add a balance column
ALTER TABLE Accounts ADD COLUMN balance DOUBLE DEFAULT 0.0;
select * from Accounts;
