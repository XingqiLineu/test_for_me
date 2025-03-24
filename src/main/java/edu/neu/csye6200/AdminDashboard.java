package edu.neu.csye6200;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard {
    private JFrame frame;
    private JButton btnAddUser, btnRemoveUser, btnUpdateUser, btnLogOut;
    private Admin admin;

    public AdminDashboard(Admin admin) {
        this.admin = admin;
        frame = new JFrame("Admin Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        btnAddUser = new JButton("Add User");
        btnRemoveUser = new JButton("Remove User");
        btnUpdateUser = new JButton("Update User");
        btnLogOut = new JButton("Log Out");

        configureButton(btnAddUser, "Add User");
        configureButton(btnRemoveUser, "Remove User");
        configureButton(btnUpdateUser, "Update User");
        configureButton(btnLogOut, "Log Out");

        frame.pack();
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
            case "Add User":
                showUserDialog();
                break;
            case "Remove User":
                showRemoveUserDialog();
                break;
            case "Update User":
                showUpdateUserDialog();
                break;
            case "Log Out":
                frame.dispose();
                new Login();
                break;
            default:
                JOptionPane.showMessageDialog(frame, "Invalid action command: " + actionCommand, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showUserDialog() {
        JTextField txtFirstName = new JTextField();
        JTextField txtLastName = new JTextField();
        JTextField txtEmailId = new JTextField();
        JTextField txtPassword = new JTextField();
        JTextField txtAge = new JTextField();
        JTextField txtAccountNumber = new JTextField();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("First Name:"));
        panel.add(txtFirstName);
        panel.add(new JLabel("Last Name:"));
        panel.add(txtLastName);
        panel.add(new JLabel("Email ID:"));
        panel.add(txtEmailId);
        panel.add(new JLabel("Password:"));
        panel.add(txtPassword);
        panel.add(new JLabel("Age:"));
        panel.add(txtAge);
        panel.add(new JLabel("Account Number:"));
        panel.add(txtAccountNumber);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add User", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String firstName = txtFirstName.getText();
                String lastName = txtLastName.getText();
                String emailId = txtEmailId.getText();
                String password = txtPassword.getText();
                int age = Integer.parseInt(txtAge.getText());
                String accountNumber = txtAccountNumber.getText();

                Customer customer = new Customer(firstName, lastName, age, emailId, password, "Customer", accountNumber);
                admin.addUser(customer);
                JOptionPane.showMessageDialog(frame, "Operation successful: User added.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter valid numbers.");
            }
        }
    }

    private void showRemoveUserDialog() {
        String emailId = JOptionPane.showInputDialog("Enter Email ID to remove:");
        if (emailId != null && !emailId.isEmpty()) {
            admin.removeUser(emailId);
            JOptionPane.showMessageDialog(frame, "Operation successful: User removed.");
        }
    }

    private void showUpdateUserDialog() {
        String emailId = JOptionPane.showInputDialog("Enter Email ID of the user to update:");
        if (emailId != null && !emailId.isEmpty()) {
            Customer existingCustomer = admin.getCustomerByEmail(emailId);
            if (existingCustomer == null) {
                JOptionPane.showMessageDialog(frame, "No user found with the provided email ID.");
                return;
            }

            JTextField txtFirstName = new JTextField(existingCustomer.getFirstName());
            JTextField txtLastName = new JTextField(existingCustomer.getLastName());
            JTextField txtNewEmailId = new JTextField(existingCustomer.getEmailId());
            JTextField txtPassword = new JTextField(existingCustomer.getPassword());
            JTextField txtAge = new JTextField(String.valueOf(existingCustomer.getAge()));
            JTextField txtAccountNumber = new JTextField(existingCustomer.getAccountNumber());

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.add(new JLabel("New First Name:"));
            panel.add(txtFirstName);
            panel.add(new JLabel("New Last Name:"));
            panel.add(txtLastName);
            panel.add(new JLabel("New Email ID:"));
            panel.add(txtNewEmailId);
            panel.add(new JLabel("New Password:"));
            panel.add(txtPassword);
            panel.add(new JLabel("New Age:"));
            panel.add(txtAge);
            panel.add(new JLabel("New Account Number:"));
            panel.add(txtAccountNumber);

            int result = JOptionPane.showConfirmDialog(null, panel, "Update User", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    String firstName = txtFirstName.getText();
                    String lastName = txtLastName.getText();
                    String newEmailId = txtNewEmailId.getText();
                    String password = txtPassword.getText();
                    int age = Integer.parseInt(txtAge.getText());
                    String accountNumber = txtAccountNumber.getText();

                    Customer updatedCustomer = new Customer(firstName, lastName, age, newEmailId, password, "Customer", accountNumber);
                    admin.removeUser(emailId);
                    admin.addUser(updatedCustomer);
                    JOptionPane.showMessageDialog(frame, "Operation successful: User updated.");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid input. Please enter valid numbers.");
                }
            }
        }
    }

}

