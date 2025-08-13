package org.example.CampusConnect.Club;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import org.example.CampusConnect.DAO.clubdao;

public class ClubRegistration extends JFrame {
    private JTextField nameField;
    private JTextField description;
    private JTextField adminName;
    private JButton createButton;

    public ClubRegistration() {
        clubdao dbdao = new clubdao();
        setTitle("Campus Connect - Club Registration");
        setSize(750, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Title
        JLabel titleLabel = new JLabel("CampusConnect Club Registration", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLabel.setForeground(new Color(40, 40, 40));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Input Form Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Club Name
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Club Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(250, 30));
        inputPanel.add(nameField, gbc);

        // Description
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        description = new JTextField();
        inputPanel.add(description, gbc);

        // Admin Name
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Admin Name:"), gbc);
        gbc.gridx = 1;
        adminName = new JTextField();
        inputPanel.add(adminName, gbc);

        // Create Button
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        createButton = new JButton("Create Club");
        createButton.setBackground(new Color(33, 150, 243));
        createButton.setForeground(Color.WHITE);
        createButton.setFocusPainted(false);
        createButton.setPreferredSize(new Dimension(150, 35));
        createButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        // Hover effect
        createButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                createButton.setBackground(new Color(30, 136, 229));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                createButton.setBackground(new Color(33, 150, 243));
            }
        });

        inputPanel.add(createButton, gbc);
        add(inputPanel, BorderLayout.CENTER);

        // Button Action
        createButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String describe = description.getText().trim();
            String admin_name = adminName.getText().trim();

            if (name.isEmpty() || describe.isEmpty() || admin_name.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please fill all required fields (Full Name, Description, Admin-name).",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (dbdao.checkClubName(name)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Club name already exists!",
                        "Duplicate Club Name",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            try {
                dbdao.insertClub(name, describe, admin_name);
                JOptionPane.showMessageDialog(
                        this,
                        "Registration successful!"
                );
                new ClubEvent(name).setVisible(true);
                dispose();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

            nameField.setText("");
            description.setText("");
            adminName.setText("");
        });
    }
}
