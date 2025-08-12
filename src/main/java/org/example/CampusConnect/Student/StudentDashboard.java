package org.example.CampusConnect.Student;

import org.bson.Document;
import org.example.CampusConnect.DAO.*;
import org.example.CampusConnect.Main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.List;
public class StudentDashboard extends JFrame {
    private String fullName;
    private Document studentData;

    private DefaultTableModel studentsModel, clubsModel, eventsModel, pendingModel;

    private JTable studentsTable, clubsTable, eventsTable, pendingTable;

    private JTabbedPane tabbedPane;
    private eventdao eventDao = new eventdao();

    private clubdao clubDao = new clubdao();

    private studentdao studentDao = new studentdao();


    public  StudentDashboard(String fullName, Document studentData) {
        this.fullName = fullName;
        this.studentData = studentData;

        setTitle("Welcome, " + fullName);
        setSize(750, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();
        tabbedPane.add("Available Clubs", createClubsTab());
        tabbedPane.add("Available Events",createEventsTab());
        tabbedPane.add("Logout",createLogoutTab());
        add(tabbedPane);


    }

    private JPanel createClubsTab() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"Club Name", "Description", "Admin", "Join"};
        clubsModel = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) { // Join column
                    return Boolean.class; // Checkbox
                }
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Only checkbox is editable
            }
        };

        clubsTable = new JTable(clubsModel);

        // Get all approved clubs (you already have this DAO method)
        List<Document> approvedClubs = clubDao.getApprovedClubs();
        for (Document club : approvedClubs) {
            String clubName = club.getString("Club_name");
            String description = club.getString("Description");
            String adminName = club.getString("Admin_name");

            clubsModel.addRow(new Object[]{
                    clubName,
                    description,
                    adminName,
                    false // default unchecked
            });
        }

        // Listen for checkbox changes
        clubsTable.getModel().addTableModelListener(e -> {
            int row = e.getFirstRow();
            int col = e.getColumn();

            if (col == 3) { // Join checkbox column
                Boolean isJoining = (Boolean) clubsTable.getValueAt(row, col);
                if (isJoining != null && isJoining) {
                    String clubName = (String) clubsTable.getValueAt(row, 0);

                    // Update the database (student joins the club)
                    clubDao.addStudentToClub(clubName, fullName);
                    studentDao.addClubToStudent(fullName,clubName);


                    // Remove row from the table
                    clubsModel.removeRow(row);

                    JOptionPane.showMessageDialog(this, "You have joined '" + clubName + "'!");
                }
            }
        });

        panel.add(new JScrollPane(clubsTable), BorderLayout.CENTER);
        return panel;
    }


    private JPanel createEventsTab() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"Event Title", "Date", "Club", "Join"};
        eventsModel = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) { // Join column
                    return Boolean.class; // Checkbox
                }
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Only checkbox is editable
            }
        };

         eventsTable = new JTable(eventsModel);

        // Get all approved clubs (you already have this DAO method)
        List<Document> approvedEvents = eventDao.getApprovedEvents();
        for (Document event : approvedEvents) {
            String title = event.getString("Title");
            Date date = event.getDate("Date");
            String clubName = event.getString("Club_Name");

            eventsModel.addRow(new Object[]{
                    title,
                    date,
                    clubName,
                    false // default unchecked
            });
        }

        // Listen for checkbox changes
        eventsTable.getModel().addTableModelListener(e -> {
            int row = e.getFirstRow();
            int col = e.getColumn();

            if (col == 3) { // Join checkbox column
                Boolean isJoining = (Boolean) eventsTable.getValueAt(row, col);
                if (isJoining != null && isJoining) {
                    String eventTitle = (String) eventsTable.getValueAt(row, 0);

                    // Update the database (student joins the club)
                    eventDao.addStudentToAttendees(eventTitle,fullName);
                    studentDao.addEventToStudent(fullName,eventTitle);


                    // Remove row from the table
                    eventsModel.removeRow(row);

                    JOptionPane.showMessageDialog(this, "You have registered for '" + eventTitle + "'!");
                }
            }
        });



        panel.add(new JScrollPane(eventsTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLogoutTab() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            new Main().setVisible(true);
            dispose();
        });
        panel.add(logoutBtn);
        return panel;
    }


}