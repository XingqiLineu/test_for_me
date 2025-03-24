package edu.neu.csye6200;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Login {
    private JFrame frame;
    private JTextField txtEmailId;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public Login() {
        frame = new JFrame("Banking System Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(null);

        JLabel lblEmailId = new JLabel("Email ID:");
        lblEmailId.setBounds(20, 20, 100, 20);
        frame.add(lblEmailId);

        txtEmailId = new JTextField();
        txtEmailId.setBounds(130, 20, 150, 20);
        frame.add(txtEmailId);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(20, 50, 100, 20);
        frame.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(130, 50, 150, 20);
        frame.add(txtPassword);

        btnLogin = new JButton("Login");
        btnLogin.setBounds(100, 100, 100, 20);
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (authenticate(txtEmailId.getText(), new String(txtPassword.getPassword()))) {
                        JOptionPane.showMessageDialog(frame, "Login Successful");
                        // Open the respective admin/customer dashboard
                    } else {
                        JOptionPane.showMessageDialog(frame, "Invalid Email ID or Password");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error reading file");
                }
            }
        });
        frame.add(btnLogin);

        frame.setVisible(true);
    }

    private boolean authenticate(String emailId, String password) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                // Assuming CSV format: firstName,lastName,age,emailId,password,role
                if (userData[3].equals(emailId) && userData[4].equals(password)) {
                    // Login successful
                    openDashboard(userData); // userData[2] contains the role
                    return true;
                }
            }
        }
        return false;
    }

    private void openDashboard(String[] userData) {
        String accountNumber = userData[6];
        String role = userData[5]; // 根据CSV格式调整索引
        String emailId = userData[3];
        String password = userData[4];
        String firstName = userData[0];
        String lastName = userData[1];
        int age = Integer.parseInt(userData[2]);
        if ("Admin".equals(role)) {
            Admin admin = new Admin(firstName, lastName, age, emailId, password);
            new AdminDashboard(admin);
            frame.dispose(); // 关闭登录窗口
        } else if ("Customer".equals(role)) {
            Customer customer = new Customer(firstName, lastName, age, emailId, password, role, accountNumber);
            new CustomerDashboard(customer);
            frame.dispose(); // 关闭登录窗口
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}

