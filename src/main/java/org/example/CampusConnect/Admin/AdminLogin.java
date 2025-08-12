package org.example.CampusConnect.Admin;
import org.bson.Document;
import org.example.CampusConnect.DAO.admindao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class AdminLogin extends JFrame {
    private JPanel inputPanel;
    private JTextField emailField;
    private JPasswordField passwordField;
    JButton loginButton;

    public AdminLogin() {

        setTitle("Admin Login System");
        setSize(750, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.lightGray);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;


        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Email: "), gbc);


        gbc.gridx = 1;
        emailField = new JTextField(20);
        inputPanel.add(emailField, gbc);


        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Password: "), gbc);


        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        inputPanel.add(passwordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 120, 215));
        loginButton.setForeground(Color.white);
        inputPanel.add(loginButton, gbc);




        setLayout(new BorderLayout(10, 10));
        add(inputPanel, BorderLayout.CENTER);


        admindao dbdao = new admindao();

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText().trim().toLowerCase();
                String password = new String(passwordField.getPassword()).trim();
                String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

                if (email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Please fill all required fields ( Email, Password).",
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
                            "Password length should have a minimum of 8 characters or maximum of 16 characters!",
                            "Invalid Password",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                try {
                    Document adminDoc = dbdao.loginAdmin(email,password);
                    if (adminDoc != null) {
                        String name = adminDoc.getString("name");
                        JOptionPane.showMessageDialog(
                                null,
                                "Login Successful!"
                        );

                        new AdminDashboard(name,adminDoc).setVisible(true);
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
    }}
