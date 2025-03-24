package edu.neu.csye6200;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVFileUtility {

    private static final String USERS_FILE = "users.csv";
    private static final String ACCOUNTS_FILE = "accounts.csv";
    private static final String TRANSACTIONS_FILE = "transactions.csv";

    public static void addCustomer(Customer customer) throws IOException {
        try (FileWriter fw = new FileWriter(USERS_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(customerToCSV(customer));
        }
    }

    public static void removeCustomer(String emailId) throws IOException {
        List<Customer> customers = getAllCustomers();
        customers.removeIf(c -> c.getEmailId().equals(emailId));
        writeCustomers(customers);
    }

    public static Customer findCustomer(String emailId) throws IOException {
        List<Customer> customers = getAllCustomers();
        for (Customer customer : customers) {
            if (customer.getEmailId().equals(emailId)) {
                return customer; // 找到匹配的客户并返回
            }
        }
        return null; // 如果没有找到匹配的客户，返回 null
    }

    private static String customerToCSV(Customer customer) {
        // Assuming cards information is not required in CSV
        return customer.toString();
    }

    private static List<Customer> getAllCustomers() throws IOException {
        List<Customer> customers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (lineNumber <= 2) continue; // 跳过前两行

                String[] data = line.split(",");
                Customer customer = new Customer(data[0], data[1], Integer.parseInt(data[2]), data[3], data[4], data[5], data[6]);
                customers.add(customer);
            }
        }
        return customers;
    }


    private static void writeCustomers(List<Customer> customers) throws IOException {
        List<String> lines = new ArrayList<>();

        // 读取并保存前两行
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            int lineNumber = 0;
            while (lineNumber < 2) {
                String line = reader.readLine();
                if (line == null) break;
                lines.add(line);
                lineNumber++;
            }
        }

        // 添加更新后的客户信息
        for (Customer customer : customers) {
            lines.add(customerToCSV(customer));
        }

        // 写入整个文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, false))) {
            for (String l : lines) {
                writer.write(l);
                writer.newLine();
            }
        }
    }

    public static double getBalanceByAccountNumber(String accountNumber) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
            String line;
            br.readLine(); // 跳过标题行
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(accountNumber)) {
                    return Double.parseDouble(data[1]); // 假设余额在第二列
                }
            }
        } catch (NumberFormatException e) {
            throw new IOException("Error parsing balance for account number: " + accountNumber, e);
        }
        return 0.0; // 如果账户不存在，返回0或抛出异常
    }


    public static void updateAccountBalance(String accountNumber, double newBalance) throws IOException {
        File accountsFile = new File(ACCOUNTS_FILE);
        File tempFile = new File(accountsFile.getAbsolutePath() + ".tmp");

        try (BufferedReader br = new BufferedReader(new FileReader(accountsFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(accountNumber)) {
                    // Update the balance of the matching account
                    line = accountNumber + "," + newBalance;
                }
                bw.write(line);
                bw.newLine();
            }
        }

        // Directly delete the original file and rename the temp file without checking.
        accountsFile.delete();
        tempFile.renameTo(accountsFile);
    }


    // You may also need a method to load the entire account object, not just the balance
    public static Account getAccountByNumber(String accountNumber) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(accountNumber)) {
                    // Assuming the balance is in the second column and it's a double
                    double balance = Double.parseDouble(data[1]);
                    // Return the found Account
                    return new Account(accountNumber, balance);
                }
            }
        } catch (NumberFormatException e) {
            throw new IOException("Error parsing balance for account: " + accountNumber, e);
        }
        // If account is not found, return null or throw an exception based on your error handling strategy
        return null;
    }

    public static void addAccount(Account account) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ACCOUNTS_FILE, true))) {
            String accountData = account.getAccountNumber() + "," + account.getBalance();
            bw.write(accountData);
            bw.newLine();
        }
    }


    public static void removeAccount(String accountNumber) throws IOException {
        File accountsFile = new File(ACCOUNTS_FILE);
        File tempFile = new File(accountsFile.getAbsolutePath() + ".tmp");

        try (BufferedReader br = new BufferedReader(new FileReader(accountsFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (!data[0].equals(accountNumber)) {
                    bw.write(line);
                    bw.newLine();
                }
            }
        }

        // Directly delete the original file and rename the temp file without checking.
        accountsFile.delete();
        tempFile.renameTo(accountsFile);
    }

    public static void addTransaction(Transaction transaction) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(TRANSACTIONS_FILE, true))) {
            bw.write(transaction.toString());
            bw.newLine();
        }
    }

    public static List<Transaction> getTransactionsForAccount(String accountNumber) throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(TRANSACTIONS_FILE))) {
            br.readLine(); // 跳过标题行
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String type = data[5]; // 假设第六个字段是交易类型

                // 如果是存款或取款，保留所有相关记录
                if (type.equals("Deposit") && data[2].equals(accountNumber)) {
                    Transaction transaction = new Transaction(data[1], data[2], Double.parseDouble(data[3]), type);
                    transactions.add(transaction);
                } else if (type.equals("Withdraw") && data[1].equals(accountNumber)) {
                    Transaction transaction = new Transaction(data[1], data[2], Double.parseDouble(data[3]), type);
                    transactions.add(transaction);
                }
                // 如果是转账，根据是转出还是转入来决定是否添加记录
                else if (type.equals("Transfer Out") && data[1].equals(accountNumber)) {
                    Transaction transaction = new Transaction(data[1], data[2], Double.parseDouble(data[3]), type);
                    transactions.add(transaction);
                } else if (type.equals("Transfer In") && data[2].equals(accountNumber)) {
                    Transaction transaction = new Transaction(data[1], data[2], Double.parseDouble(data[3]), type);
                    transactions.add(transaction);
                }
            }
        }
        return transactions;
    }

}

