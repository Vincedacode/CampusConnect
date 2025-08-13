package org.example.CampusConnect;

import org.example.CampusConnect.Club.ClubRegistration;
import org.example.CampusConnect.Student.StudentRegistration;
import org.example.CampusConnect.Admin.AdminLogin;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    public Main() {
        setTitle("CampusConnect");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

        // ----- HEADER -----
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(25, 118, 210));
        headerPanel.setPreferredSize(new Dimension(800, 120));

        JLabel titleLabel = new JLabel("CampusConnect", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);

        JLabel subTitleLabel = new JLabel("University Event & Club Management System", SwingConstants.CENTER);
        subTitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subTitleLabel.setForeground(Color.WHITE);

        JPanel headerTextPanel = new JPanel(new GridLayout(2, 1));
        headerTextPanel.setOpaque(false);
        headerTextPanel.add(titleLabel);
        headerTextPanel.add(subTitleLabel);

        headerPanel.add(headerTextPanel, BorderLayout.CENTER);

        // ----- MAIN CONTENT -----
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);

        // Buttons with consistent styling
        JButton studentBtn = createStyledButton("Student");
        JButton adminBtn = createStyledButton("Admin");
        JButton clubBtn = createStyledButton("Club");

        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(studentBtn, gbc);

        gbc.gridx = 1;
        contentPanel.add(adminBtn, gbc);

        gbc.gridx = 2;
        contentPanel.add(clubBtn, gbc);

        // ----- FOOTER -----
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(240, 240, 240));
        footerPanel.setPreferredSize(new Dimension(800, 40));

        JLabel footerLabel = new JLabel("© 2025 CampusConnect - All Rights Reserved");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerLabel.setForeground(new Color(100, 100, 100));

        footerPanel.add(footerLabel);

        // Add panels to frame
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        // Actions
        studentBtn.addActionListener(e -> {
            new StudentRegistration().setVisible(true);
            dispose();
        });

        adminBtn.addActionListener(e -> {
            new AdminLogin().setVisible(true);
            dispose();
        });

        clubBtn.addActionListener(e -> {
            new ClubRegistration().setVisible(true);
            dispose();
        });
    }

    // Helper method for styled buttons
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 50));
        button.setBackground(new Color(33, 150, 243));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(25, 118, 210));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(33, 150, 243));
            }
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}
