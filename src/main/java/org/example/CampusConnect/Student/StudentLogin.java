package org.example.CampusConnect.Student;

import org.bson.Document;
import org.example.CampusConnect.DAO.studentdao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentLogin extends JFrame {

    private JPanel mainPanel;
    private JTextField emailField;
    private JPasswordField passwordField;
    JButton loginButton;

    public StudentLogin() {
        setTitle("Student Login");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ===== Main Panel Setup =====
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // ===== Title =====
        JLabel titleLabel = new JLabel("Student Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(new Color(0, 102, 204));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // ===== Form Panel =====
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Email
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        emailField = new JTextField(18);
        formPanel.add(emailField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(18);
        formPanel.add(passwordField, gbc);

        // Login Button
        gbc.gridx = 1;
        gbc.gridy = 2;
        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 120, 215));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(120, 30));
        formPanel.add(loginButton, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // ===== Register Panel =====
        JPanel regPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        regPanel.setBackground(Color.WHITE);

        JLabel regLabel = new JLabel("Don't have an account?");
        JButton regButton = new JButton("Register");
        regButton.setBackground(new Color(46, 204, 113));
        regButton.setForeground(Color.WHITE);
        regButton.setFocusPainted(false);

        regPanel.add(regLabel);
        regPanel.add(regButton);

        mainPanel.add(regPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);

        // ===== DAO Instance =====
        studentdao dbdao = new studentdao();

        // ===== Event Listeners =====
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText().trim().toLowerCase();
                String password = new String(passwordField.getPassword()).trim();
                String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

                if (email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Please fill all required fields (Email, Password).",
                            "Input Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                if (!email.matches(emailRegex)) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Please enter a valid email address.",
                            "Invalid Email",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                if (password.length() < 8 || password.length() > 16) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Password must be between 8 and 16 characters!",
                            "Invalid Password",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                try {
                    Document studentDoc = dbdao.loginStudent(email, password);
                    if (studentDoc != null) {
                        String fullName = studentDoc.getString("Fullname");
                        JOptionPane.showMessageDialog(null, "Login Successful!");
                        new StudentDashboard(fullName, studentDoc).setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(
                                null,
                                "Account not found!",
                                "Invalid login details",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

        regButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StudentRegistration().setVisible(true);
                dispose();
            }
        });
    }
}
