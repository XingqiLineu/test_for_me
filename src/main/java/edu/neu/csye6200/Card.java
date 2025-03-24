package edu.neu.csye6200;

import java.util.Date;

public class Card {
    private String cardNumber;
    private double creditLimit;
    private boolean isEnabled;
    private double outstandingBalance;
    private Date pendingDate;

    public Card(String cardNumber, double creditLimit, boolean isEnabled) {
        this.cardNumber = cardNumber;
        this.creditLimit = creditLimit;
        this.isEnabled = isEnabled;
        this.outstandingBalance = 0; // Initially zero
        // pendingDate will be calculated based on card usage
    }

    // Getters and Setters
}


