package edu.neu.csye6200;

import java.io.IOException;

public class Admin extends Person {
    private String emailID;
    private String password;

    public Admin(String firstName, String lastName, int age, String emailID, String password) {
        super(firstName, lastName, age);
        this.emailID = emailID;
        this.password = password;
    }

    public void addUser(Customer customer) {
        try {
            CSVFileUtility.addCustomer(customer);
            Account newAccount = new Account(customer.getAccountNumber(), 0.0); // 初始余额设置为0
            CSVFileUtility.addAccount(newAccount);
        } catch (IOException e) {
            e.printStackTrace();
            // 处理异常或通知用户
        }
    }

    public void removeUser(String emailId) {
        try {
            Customer customer = CSVFileUtility.findCustomer(emailId);
            if (customer != null) {
                CSVFileUtility.removeCustomer(emailId);
                CSVFileUtility.removeAccount(customer.getAccountNumber());
            }
        } catch (IOException e) {
            e.printStackTrace();
            // 处理异常或通知用户
        }
    }

    public Customer getCustomerByEmail(String emailId) {
        try {
            return CSVFileUtility.findCustomer(emailId);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception or notify user
        }
        return null;
    }

    // Getters and Setters
}


