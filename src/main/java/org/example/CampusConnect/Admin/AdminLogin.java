package org.example.CampusConnect.Admin;

import org.bson.Document;
import org.example.CampusConnect.DAO.admindao;
import org.example.CampusConnect.Main;

import javax.swing.*;
import java.awt.*;

public class AdminLogin extends JFrame {
    private JPanel inputPanel;
    private JTextField emailField;
    private JPasswordField passwordField;
    JButton loginButton;
    JButton backButton;

    public AdminLogin() {
        setTitle("Admin Login - CampusConnect");
        setSize(750, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JLabel titleLabel = new JLabel("Admin Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        titleLabel.setForeground(new Color(45, 52, 54));
        add(titleLabel, BorderLayout.NORTH);

        // Main input panel
        inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(30, 30, 30, 30),
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Email Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        inputPanel.add(emailLabel, gbc);

        // Email Field
        gbc.gridx = 1;
        emailField = new JTextField(20);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(emailField, gbc);

        // Password Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        inputPanel.add(passwordLabel, gbc);

        // Password Field
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(passwordField, gbc);

        // Buttons panel (Login + Back side by side)
        gbc.gridx = 1;
        gbc.gridy = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setBackground(new Color(0, 120, 215));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        backButton = new JButton("Back");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        backButton.setBackground(new Color(158, 158, 158));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        buttonPanel.add(backButton);
        buttonPanel.add(loginButton);

        inputPanel.add(buttonPanel, gbc);

        // Center wrapper
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(new Color(245, 245, 245));
        centerWrapper.add(inputPanel);

        add(centerWrapper, BorderLayout.CENTER);

        admindao dbdao = new admindao();

        // Login button action
        loginButton.addActionListener(e -> {
            String email = emailField.getText().trim().toLowerCase();
            String password = new String(passwordField.getPassword()).trim();
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Please fill all required fields ( Email, Password).",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!email.matches(emailRegex)) {
                JOptionPane.showMessageDialog(null,
                        "Please enter a valid email address.",
                        "Invalid Email", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (password.length() < 8 || password.length() > 16) {
                JOptionPane.showMessageDialog(null,
                        "Password length should have a minimum of 8 characters or maximum of 16 characters!",
                        "Invalid Password", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Document adminDoc = dbdao.loginAdmin(email, password);
                if (adminDoc != null) {
                    String name = adminDoc.getString("name");
                    JOptionPane.showMessageDialog(null, "Login Successful!");
                    new AdminDashboard(name, adminDoc).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Account not found!",
                            "Invalid login details", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        });

        // Back button action
        backButton.addActionListener(e -> {
            new Main().setVisible(true);
            dispose();
        });
    }
}
