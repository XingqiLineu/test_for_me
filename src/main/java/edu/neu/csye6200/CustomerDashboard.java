package edu.neu.csye6200;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class CustomerDashboard {
    private JFrame frame;
    private JButton btnQuery, btnTransfer, btnWithdraw, btnDeposit, btnBill, btnLogOut;
    private Customer customer;

    public CustomerDashboard(Customer customer) {
        this.customer = customer;
        frame = new JFrame("Customer Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        btnQuery = new JButton("Query");
        btnTransfer = new JButton("Transfer");
        btnWithdraw = new JButton("Withdraw");
        btnDeposit = new JButton("Deposit");
        btnBill = new JButton("Bill");
        btnLogOut = new JButton("Log Out");

        // 配置按钮和添加到框架
        configureButton(btnQuery, "Query");
        configureButton(btnTransfer, "Transfer");
        configureButton(btnWithdraw, "Withdraw");
        configureButton(btnDeposit, "Deposit");
        configureButton(btnBill, "Bill");
        configureButton(btnLogOut, "Log Out");

        frame.pack(); // 自动调整大小以适应组件
        frame.setVisible(true);
    }

    private void configureButton(JButton button, String actionCommand) {
        button.setActionCommand(actionCommand);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getMinimumSize().height));
        button.addActionListener(e -> performAction(e.getActionCommand()));
        frame.add(button);
        frame.add(Box.createVerticalStrut(5)); // 添加间隔
    }

    private void performAction(String actionCommand) {
        switch (actionCommand) {
            case "Query":
                try {
                    double balance = customer.getBalance();
                    JOptionPane.showMessageDialog(frame, "Your balance is: " + balance);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(frame, "Unable to retrieve balance.");
                    e.printStackTrace();
                }
                break;
            case "Transfer":
                showTransferDialog();
                break;
            case "Withdraw":
                String amountString = JOptionPane.showInputDialog(frame, "Enter amount to withdraw:");
                if (amountString != null && !amountString.isEmpty()) {
                    try {
                        double amount = Double.parseDouble(amountString);
                        boolean success = customer.withdrawFromAccount(amount);
                        if (success) {
                            JOptionPane.showMessageDialog(frame, "Withdrawal successful. New balance: " + customer.getBalance());
                        } else {
                            JOptionPane.showMessageDialog(frame, "Withdrawal failed. Check balance or enter a valid amount.");
                        }
                    } catch (NumberFormatException | IOException e) {
                        JOptionPane.showMessageDialog(frame, "Invalid input or error processing withdrawal.");
                        e.printStackTrace();
                    }
                }
                break;
            case "Deposit":
                String depositAmountString = JOptionPane.showInputDialog("Enter amount to deposit:");
                try {
                    double amount = Double.parseDouble(depositAmountString);
                    customer.depositToAccount(amount);
                    JOptionPane.showMessageDialog(frame, "Deposit successful. New balance: " + customer.getBalance());
                } catch (NumberFormatException | IOException e) {
                    JOptionPane.showMessageDialog(frame, "Invalid input or error processing deposit.");
                    e.printStackTrace();
                }
                break;
            case "Bill":
                showBillDialog();
                break;
            case "Log Out":
                frame.dispose(); // 关闭当前窗口
                new Login(); // 打开登录窗口
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + actionCommand);
        }
    }

    private void showTransferDialog() {
        JTextField txtRecipientAccountNumber = new JTextField();
        JTextField txtTransferAmount = new JTextField();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Recipient Account Number:"));
        panel.add(txtRecipientAccountNumber);
        panel.add(new JLabel("Amount to Transfer:"));
        panel.add(txtTransferAmount);

        int result = JOptionPane.showConfirmDialog(null, panel, "Transfer Money", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String recipientAccountNumber = txtRecipientAccountNumber.getText();
            String amountString = txtTransferAmount.getText();
            try {
                double amount = Double.parseDouble(amountString);
                if (customer.transferMoney(recipientAccountNumber, amount)) {
                    JOptionPane.showMessageDialog(frame, "Transfer successful.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Transfer failed. Check balance and recipient account.");
                }
            } catch (NumberFormatException | IOException e) {
                JOptionPane.showMessageDialog(frame, "Invalid input or error processing transfer.");
                e.printStackTrace();
            }
        }
    }

    private void showBillDialog() {
        try {
            String transactionHistory = customer.getAccountTransactionHistory();
            JTextArea textArea = new JTextArea(10, 30);
            textArea.setText(transactionHistory);
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            JOptionPane.showMessageDialog(null, scrollPane, "Account Transaction History", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Unable to retrieve transaction history.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
