package edu.neu.csye6200;

import java.io.IOException;
import java.util.List;

public class Account {
    private String accountNumber;
    private double balance;
    private List<Transaction> transactionHistory;

    public Account(String accountNumber, double balance) throws IOException {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.transactionHistory = CSVFileUtility.getTransactionsForAccount(accountNumber); // 从 CSV 文件加载交易历史
    }

    private void recordTransaction(String senderAccountNumber, String receiverAccountNumber, double amount, String type) throws IOException {
        Transaction transaction = new Transaction(senderAccountNumber, receiverAccountNumber, amount, type);
        transactionHistory.add(transaction);
        CSVFileUtility.addTransaction(transaction);
    }

    public void deposit(double amount) throws IOException {
        this.balance += amount;
        recordTransaction("BANK", this.accountNumber, amount, "Deposit");
        CSVFileUtility.updateAccountBalance(this.accountNumber, this.balance); // Update the balance in CSV
    }

    public boolean withdraw(double amount) throws IOException {
        if (amount > 0 && this.balance >= amount) {
            this.balance -= amount;
            recordTransaction(this.accountNumber, "BANK", amount, "Withdraw");
            CSVFileUtility.updateAccountBalance(this.accountNumber, this.balance); // 更新 CSV 文件中的余额
            return true;
        }
        return false;
    }

    public boolean transfer(Account receiverAccount, double amount) throws IOException {
        if (amount > 0 && this.balance >= amount) {
            this.balance -= amount;
            receiverAccount.balance += amount;
            recordTransaction(this.accountNumber, receiverAccount.accountNumber, amount, "Transfer Out");
            receiverAccount.recordTransaction(this.accountNumber, receiverAccount.accountNumber, amount, "Transfer In");
            CSVFileUtility.updateAccountBalance(this.accountNumber, this.balance);
            CSVFileUtility.updateAccountBalance(receiverAccount.accountNumber, receiverAccount.balance);
            return true;
        }
        return false;
    }

    public String getTransactionHistoryString() {
        StringBuilder historyBuilder = new StringBuilder();
        if (transactionHistory.isEmpty()) {
            historyBuilder.append("No transactions found for account ").append(accountNumber);
        } else {
            historyBuilder.append("Transaction history for account ").append(accountNumber).append(":\n");
            for (Transaction transaction : transactionHistory) {
                historyBuilder.append(transaction.toString()).append("\n");
            }
        }
        return historyBuilder.toString();
    }

    // ... other getters and setters ...

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }
}

