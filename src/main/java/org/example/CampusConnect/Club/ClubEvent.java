package org.example.CampusConnect.Club;
import org.example.CampusConnect.DAO.eventdao;
import org.example.CampusConnect.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.sql.Time;


public class ClubEvent extends JFrame {
    private JTextField titleField;
    private JSpinner dateSpinner;
    private JSpinner timeSpinner;
    private JButton createEventBtn;

    private   String clubName;
    public ClubEvent(String clubName) {
        this.clubName = clubName;
        eventdao dbdao = new eventdao();
        setTitle("Create Club Event");
        setSize(750, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        panel.add(new JLabel("Event Title:"));
        titleField = new JTextField();
        panel.add(titleField);

        // Date picker using JSpinner
        panel.add(new JLabel("Event Date:"));
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        panel.add(dateSpinner);

        // Time picker using JSpinner
        panel.add(new JLabel("Event Time:"));
        timeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "hh:mm a");
        timeSpinner.setEditor(timeEditor);
        panel.add(timeSpinner);

        // Button
        createEventBtn = new JButton("Create Event");
        createEventBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                Date date = (Date) dateSpinner.getValue(); // This includes date + time
                Date timeDate = (Date) timeSpinner.getValue();

                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                String timeString = timeFormat.format(timeDate);


                if(title.isEmpty() ){
                    JOptionPane.showMessageDialog(
                            null,
                            "Please fill all required fields ( Title ).",
                            "Input Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                if(dbdao.checkEventTitle(title)){
                    JOptionPane.showMessageDialog(
                            null,
                            "Event title already exists!",
                            "Duplicate event title",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                try {
                    dbdao.registerEvent(title,date,timeString,clubName);
                    JOptionPane.showMessageDialog(
                            null,
                            "Event registered successfully!"
                    );
                    new Main().setVisible(true);
                    dispose();
                }catch (Exception ex){
                    System.out.println(ex.getMessage());
                }


            }
        });
        panel.add(new JLabel()); // Empty label to fill space
        panel.add(createEventBtn);

        add(panel);
        setVisible(true);
    }






}
