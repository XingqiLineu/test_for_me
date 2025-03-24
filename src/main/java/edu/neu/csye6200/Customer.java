package edu.neu.csye6200;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Customer extends Person {
    private String accountNumber; // Direct reference to an Account object
    private String emailId;
    private String password;
    private List<Card> cards;
    private String role;

    public Customer(String firstName, String lastName, int age, String emailId, String password, String role, String accountNumber) {
        super(firstName, lastName, age);
        this.accountNumber = accountNumber;
        this.emailId = emailId;
        this.password = password;
        this.cards = new ArrayList<>();
        this.role = role;
    }

    public double getBalance() throws IOException {
        // Delegate the call to CSVFileUtility
        return CSVFileUtility.getBalanceByAccountNumber(this.accountNumber);
    }

    public void depositToAccount(double amount) throws IOException {
        Account account = CSVFileUtility.getAccountByNumber(this.accountNumber);
        if (account != null) {
            account.deposit(amount); // Deposit the amount and record the transaction
        }
    }

    public boolean withdrawFromAccount(double amount) throws IOException {
        Account account = CSVFileUtility.getAccountByNumber(this.accountNumber);
        if (account != null) {
            return account.withdraw(amount); // 执行取款操作并返回操作结果
        } else {
            // 如果找不到账户，返回 false
            return false;
        }
    }

    public boolean transferMoney(String recipientAccountNumber, double amount) throws IOException {
        Account senderAccount = CSVFileUtility.getAccountByNumber(this.accountNumber);
        Account recipientAccount = CSVFileUtility.getAccountByNumber(recipientAccountNumber);

        if (senderAccount != null && recipientAccount != null) {
            return senderAccount.transfer(recipientAccount, amount);
        } else {
            return false;
        }
    }

    public String getAccountTransactionHistory() throws IOException {
        Account account = CSVFileUtility.getAccountByNumber(this.accountNumber);
        if (account != null) {
            return account.getTransactionHistoryString();
        } else {
            return "Account not found.";
        }
    }

    // Getters and Setters

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getPassword() {
        return password;
    }

    public List<Card> getCards() {
        return cards;
    }

    @Override
    public String toString() {
        return super.toString() + "," + emailId + "," + password + "," + role + "," + accountNumber;
    }
}


