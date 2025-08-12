package org.example.CampusConnect.Student;
import org.example.CampusConnect.DAO.studentdao;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StudentRegistration extends JFrame {
    private JPanel inputPanel;
    private JTextField nameField, ageField, emailField;
    private JPasswordField passwordField, repeatPasswordField;
    private JButton registerBtn;
    private JComboBox<String> department;
    private JRadioButton maleRadio, femaleRadio;

    private ButtonGroup genderGroup;





    public StudentRegistration() throws HeadlessException {
        setTitle("Student Registration System");
        setSize(750, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);



        inputPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        inputPanel.setBackground(Color.lightGray);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        inputPanel.add(new JLabel("Name: "));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Age: "));
        ageField = new JTextField();
        inputPanel.add(ageField);

        inputPanel.add(new JLabel("Gender: "));

        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel.setBackground(Color.lightGray);

        maleRadio = new JRadioButton("Male");
        femaleRadio = new JRadioButton("Female");

        genderGroup = new ButtonGroup();

        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);

        genderPanel.add(maleRadio);
        genderPanel.add(femaleRadio);

        inputPanel.add(genderPanel);

        inputPanel.add(new JLabel("Email: "));
        emailField = new JTextField();
        inputPanel.add(emailField);

        inputPanel.add(new JLabel("Password: "));
        passwordField = new JPasswordField();
        inputPanel.add(passwordField);

        inputPanel.add(new JLabel("Repeat Password: "));
        repeatPasswordField = new JPasswordField();
        inputPanel.add(repeatPasswordField);


        inputPanel.add(new JLabel("Department: "));
        String[] departments = {"MMS", "Python", "Website Development", "CyberSecurity"};
        department = new JComboBox<>(departments);
        inputPanel.add(department);


        inputPanel.add(new JLabel());
        registerBtn = new JButton("Register");
        registerBtn.setBackground(new Color(0, 120, 215));
        registerBtn.setForeground(Color.white);
        inputPanel.add(registerBtn);

        JPanel loginPanel = new JPanel(new GridLayout(1,2,10,10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(0,50,10,50));


        JLabel loginLabel = new JLabel("Already have an account?");
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(Color.green);
        loginButton.setForeground(Color.white);

        loginPanel.add(loginLabel);
        loginPanel.add(loginButton);

        setLayout(new BorderLayout(10, 10));

        add(inputPanel, BorderLayout.NORTH);
        add(loginPanel, BorderLayout.CENTER);
        studentdao dbdao = new studentdao();


        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String age = ageField.getText().trim();
                String gender = maleRadio.isSelected() ? "Male"
                        : femaleRadio.isSelected() ? "Female" : "";
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword());
                String repeatPassword = new String(repeatPasswordField.getPassword());
                String departments = (String) department.getSelectedItem();
                String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";


                if (name.isEmpty() || age.isEmpty() || gender.isEmpty() || email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Please fill all required fields (Full Name, Age, Gender, Email, Password).",
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

                if(password.length() < 8 || password.length() > 16){
                    JOptionPane.showMessageDialog(
                            null,
                            "Password length should have a minimum of 8 characters or maximum of 16 characters!",
                            "Invalid Password",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                if(!password.equals(repeatPassword)){
                    JOptionPane.showMessageDialog(
                            null,
                            "Password does not match!",
                            "Password Mismatch",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                if(dbdao.checkStudentEmail(email)){
                    JOptionPane.showMessageDialog(
                            null,
                            "Email already exists!",
                            "Duplicate Email",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }


                int parsedAge;
                try {
                  parsedAge=  Integer.parseInt(age);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Age must be a valid number.",
                            "Input Error",
                            JOptionPane.ERROR_MESSAGE

                    );
                    return;
                }

                try {

                    dbdao.registerStudent(name,parsedAge,email,gender,password,departments);
                    JOptionPane.showMessageDialog(
                            null,
                            "Registration successful!"
                    );
                }catch (Exception ex){
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

            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StudentLogin().setVisible(true);
                dispose();
            }
        });

    }
    ;




}
