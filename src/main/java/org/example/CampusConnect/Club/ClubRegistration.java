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

    public ClubRegistration()
    {
        clubdao dbdao = new clubdao();
        setTitle("Campus Connect Page");
        setSize(750, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("CampusConnect Club Registration");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 200, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBackground(Color.LIGHT_GRAY); // Background color for the form
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Margin (top, left, bottom, right)

        inputPanel.add(new JLabel("Club Name"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Description"));
        description = new JTextField();
        inputPanel.add(description);

        inputPanel.add(new JLabel("Admin Name"));
        adminName = new JTextField();
        inputPanel.add(adminName);

        createButton = new JButton("Create club");
        createButton.setBackground(Color.BLUE);
        createButton.setForeground(Color.white);

        inputPanel.add(new JLabel());
        inputPanel.add(createButton);

        add(inputPanel, BorderLayout.CENTER);

        createButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String describe = description.getText().trim();
                String admin_name = adminName.getText().trim();

                if (name.isEmpty() || describe.isEmpty() || admin_name.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Please fill all required fields (Full Name, Description, Admin-name).",
                            "Input Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                if(dbdao.checkClubName(name)){
                    JOptionPane.showMessageDialog(
                            null,
                            "Club name already exists!",
                            "Duplicate Club Name",
                            JOptionPane.ERROR_MESSAGE
                    );

                }
                try {
                    dbdao.insertClub(name,describe,admin_name);
                    JOptionPane.showMessageDialog(
                            null,
                            "Registration successful!"
                    );
                    new ClubEvent(name).setVisible(true);
                    dispose();
                }catch (Exception ex){
                    System.out.println(ex.getMessage());
                }

                nameField.setText("");
                description.setText("");
                adminName.setText("");
            }


        });
    }



}

