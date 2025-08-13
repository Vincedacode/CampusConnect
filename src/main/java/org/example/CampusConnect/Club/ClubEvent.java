package org.example.CampusConnect.Club;

import org.example.CampusConnect.DAO.eventdao;
import org.example.CampusConnect.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.text.SimpleDateFormat;

public class ClubEvent extends JFrame {
    private JTextField titleField;
    private JSpinner dateSpinner;
    private JSpinner timeSpinner;
    private JButton createEventBtn;

    private String clubName;

    public ClubEvent(String clubName) {
        this.clubName = clubName;
        eventdao dbdao = new eventdao();

        setTitle("Create Club Event");
        setSize(750, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Title
        JLabel titleLabel = new JLabel("Create Event for " + clubName, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        titleLabel.setForeground(new Color(40, 40, 40));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Event Title
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Event Title:"), gbc);
        gbc.gridx = 1;
        titleField = new JTextField();
        titleField.setPreferredSize(new Dimension(250, 30));
        formPanel.add(titleField, gbc);

        // Event Date
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Event Date:"), gbc);
        gbc.gridx = 1;
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        formPanel.add(dateSpinner, gbc);

        // Event Time
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Event Time:"), gbc);
        gbc.gridx = 1;
        timeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "hh:mm a");
        timeSpinner.setEditor(timeEditor);
        formPanel.add(timeSpinner, gbc);

        // Create Event Button
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        createEventBtn = new JButton("Create Event");
        createEventBtn.setBackground(new Color(33, 150, 243));
        createEventBtn.setForeground(Color.WHITE);
        createEventBtn.setFocusPainted(false);
        createEventBtn.setPreferredSize(new Dimension(150, 35));
        createEventBtn.setFont(new Font("SansSerif", Font.BOLD, 14));

        // Hover effect
        createEventBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                createEventBtn.setBackground(new Color(30, 136, 229));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                createEventBtn.setBackground(new Color(33, 150, 243));
            }
        });

        // Button Action
        createEventBtn.addActionListener((ActionEvent e) -> {
            String title = titleField.getText().trim();
            Date date = (Date) dateSpinner.getValue();
            Date timeDate = (Date) timeSpinner.getValue();

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            String timeString = timeFormat.format(timeDate);

            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please fill all required fields (Title).",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (dbdao.checkEventTitle(title)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Event title already exists!",
                        "Duplicate Event Title",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            try {
                dbdao.registerEvent(title, date, timeString, clubName);
                JOptionPane.showMessageDialog(
                        this,
                        "Event registered successfully!"
                );
                new Main().setVisible(true);
                dispose();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        });

        formPanel.add(createEventBtn, gbc);
        add(formPanel, BorderLayout.CENTER);
        setVisible(true);
    }
}
