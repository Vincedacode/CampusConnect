package org.example.CampusConnect.Student;

import org.example.CampusConnect.DAO.studentdao;
import org.example.CampusConnect.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentRegistration extends JFrame {
    private JTextField nameField, ageField, emailField;
    private JPasswordField passwordField, repeatPasswordField;
    private JButton registerBtn;
    private JComboBox<String> department;
    private JRadioButton maleRadio, femaleRadio;
    private ButtonGroup genderGroup;

    public StudentRegistration() throws HeadlessException {
        setTitle("CampusConnect - Student Registration");
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ===== HEADER =====
        JLabel headerLabel = new JLabel("Student Registration", JLabel.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerLabel.setForeground(new Color(0, 102, 204));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        // ===== MAIN FORM PANEL =====
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        formPanel.add(nameField, gbc);

        // Age
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        ageField = new JTextField();
        formPanel.add(ageField, gbc);

        // Gender
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        genderPanel.setBackground(Color.WHITE);
        maleRadio = new JRadioButton("Male");
        femaleRadio = new JRadioButton("Female");
        genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);
        genderPanel.add(maleRadio);
        genderPanel.add(femaleRadio);
        formPanel.add(genderPanel, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField();
        formPanel.add(emailField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField();
        formPanel.add(passwordField, gbc);

        // Repeat Password
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Repeat Password:"), gbc);
        gbc.gridx = 1;
        repeatPasswordField = new JPasswordField();
        formPanel.add(repeatPasswordField, gbc);

        // Department
        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(new JLabel("Department:"), gbc);
        gbc.gridx = 1;
        String[] departments = {"MMS", "Python", "Website Development", "CyberSecurity"};
        department = new JComboBox<>(departments);
        formPanel.add(department, gbc);

        // ===== REGISTER BUTTON =====
        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        registerBtn = new JButton("Register");
        registerBtn.setBackground(new Color(0, 120, 215));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerBtn.setFocusPainted(false);
        registerBtn.setPreferredSize(new Dimension(120, 35));
        formPanel.add(registerBtn, gbc);


        // ===== FOOTER LOGIN PANEL =====
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        footerPanel.setBackground(Color.WHITE);

        JButton backButton = new JButton("Back");
        backButton.setBackground(new Color(128, 128, 128));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);

        JLabel loginLabel = new JLabel("Already have an account?");
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(34, 139, 34));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);

        footerPanel.add(backButton); // Add Back button first
        footerPanel.add(loginLabel);
        footerPanel.add(loginButton);



        // ===== ADD TO FRAME =====
        setLayout(new BorderLayout());
        add(headerLabel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        // ===== DAO INSTANCE =====
        studentdao dbdao = new studentdao();

        // ===== ACTION LISTENERS =====
        registerBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String age = ageField.getText().trim();
            String gender = maleRadio.isSelected() ? "Male"
                    : femaleRadio.isSelected() ? "Female" : "";
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String repeatPassword = new String(repeatPasswordField.getPassword());
            String departmentsSelected = (String) department.getSelectedItem();
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

            if (name.isEmpty() || age.isEmpty() || gender.isEmpty() || email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!email.matches(emailRegex)) {
                JOptionPane.showMessageDialog(null, "Invalid email address.", "Invalid Email", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (password.length() < 8 || password.length() > 16) {
                JOptionPane.showMessageDialog(null, "Password length must be 8-16 characters.", "Invalid Password", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!password.equals(repeatPassword)) {
                JOptionPane.showMessageDialog(null, "Passwords do not match.", "Password Mismatch", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (dbdao.checkStudentEmail(email)) {
                JOptionPane.showMessageDialog(null, "Email already exists.", "Duplicate Email", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int parsedAge;
            try {
                parsedAge = Integer.parseInt(age);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Age must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                dbdao.registerStudent(name, parsedAge, email, gender, password, departmentsSelected);
                JOptionPane.showMessageDialog(null, "Registration successful!");
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

            nameField.setText("");
            ageField.setText("");
            genderGroup.clearSelection();
            emailField.setText("");
            department.setSelectedIndex(0);
            passwordField.setText("");

            new StudentLogin().setVisible(true);
            dispose();
        });

        loginButton.addActionListener(e -> {
            new StudentLogin().setVisible(true);
            dispose();
        });

        backButton.addActionListener(e ->{
            new Main().setVisible(true);
            dispose();
        });
    }
}
