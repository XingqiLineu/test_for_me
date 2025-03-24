package edu.neu.csye6200;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Transaction {
    private String transactionId;
    private String senderAccountNumber;
    private String receiverAccountNumber;
    private double amount;
    private Date dateOfTransaction;
    private String type;

    public Transaction(String senderAccountNumber, String receiverAccountNumber, double amount,String type) {
        this.transactionId = UUID.randomUUID().toString();
        this.senderAccountNumber = senderAccountNumber;
        this.receiverAccountNumber = receiverAccountNumber;
        this.amount = amount;
        this.dateOfTransaction = new Date();
        this.type=type;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return transactionId + "," +
                senderAccountNumber + "," +
                receiverAccountNumber + "," +
                amount + "," +
                sdf.format(dateOfTransaction) + "," +
                type;
    }

    // Getters and Setters
}


