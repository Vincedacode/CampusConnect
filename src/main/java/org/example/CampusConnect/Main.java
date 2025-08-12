package org.example.CampusConnect;
import org.example.CampusConnect.Club.ClubRegistration;
import org.example.CampusConnect.Student.StudentRegistration;
import org.example.CampusConnect.Admin.AdminLogin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {

    public Main() {


        setTitle("CampusConnect");
        setSize(750, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(33, 150, 243));
        headerPanel.setPreferredSize(new Dimension(750, 100));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("CampusConnect", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subTitleLabel = new JLabel("University Event & Club Management System", SwingConstants.CENTER);
        subTitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subTitleLabel.setForeground(Color.WHITE);
        subTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(Box.createVerticalStrut(15));
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subTitleLabel);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 50));
        buttonPanel.setBackground(Color.WHITE);

        JButton studentBtn = new JButton("Student");
        JButton adminBtn = new JButton("Admin");
        JButton clubBtn = new JButton("Club");

        Dimension btnSize = new Dimension(120, 40);
        studentBtn.setPreferredSize(btnSize);
        adminBtn.setPreferredSize(btnSize);
        clubBtn.setPreferredSize(btnSize);


        buttonPanel.add(studentBtn);
        buttonPanel.add(adminBtn);
        buttonPanel.add(clubBtn);




        add(headerPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        studentBtn.addActionListener(e -> {
            new StudentRegistration().setVisible(true);
            dispose();
        });

        adminBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminLogin().setVisible(true);
                dispose();
            }
        });

        clubBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ClubRegistration().setVisible(true);
                dispose();
            }
        });


    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}
