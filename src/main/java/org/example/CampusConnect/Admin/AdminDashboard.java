package org.example.CampusConnect.Admin;

import org.bson.Document;
import org.example.CampusConnect.DAO.*;
import org.example.CampusConnect.Main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class AdminDashboard extends JFrame {
    private String name;
    private Document adminData;
    private JTabbedPane tabbedPane;
    private JTable studentsTable, clubsTable, eventsTable, pendingTable;
    private DefaultTableModel studentsModel, clubsModel, eventsModel, pendingModel;
    private studentdao studentDao;

    private eventdao eventDao = new eventdao();

    private clubdao clubDao = new clubdao();

    public AdminDashboard(String name, Document adminData) {
        this.name = name;
        this.adminData = adminData;
        this.studentDao = new studentdao();

        setTitle("Admin Dashboard");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Students", createStudentsTab());
        tabbedPane.addTab("Clubs", createClubsTab());
        tabbedPane.addTab("Events", createEventsTab());
        tabbedPane.addTab("Pending Approvals", createPendingTab());
        tabbedPane.addTab("Logout", createLogoutTab());

        add(tabbedPane);
    }

    // ------------------ Students Tab -------------------
    private JPanel createStudentsTab() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"Name", "Email", "Department", "Joined Clubs" , "Registered Events"};
        studentsModel = new DefaultTableModel(columns, 0);
        studentsTable = new JTable(studentsModel);

        // 🔽 Fetch students from DB
        List<Document> allStudents = studentDao.getAllStudents();
        for (Document student : allStudents) {
            String fullName = student.getString("Fullname");
            String email = student.getString("Email");
            String department = student.getString("Department");
            List<String> joinedClubs = (List<String>) student.get("Joined_Clubs");
            List<String> registeredEvents = (List<String>) student.get("Registered_Events");

            studentsModel.addRow(new Object[]{
                    fullName,
                    email,
                    department,
                    joinedClubs != null ? String.join(", ", joinedClubs) : "-",
                    registeredEvents != null ? String.join(", ", registeredEvents) : "-"
            });
        }

        panel.add(new JScrollPane(studentsTable), BorderLayout.CENTER);
        return panel;
    }

    // ------------------ Clubs Tab -------------------
    private JPanel createClubsTab() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"Club Name", "Description", "Admin", "Approved"};
        clubsModel = new DefaultTableModel(columns, 0);
        clubsTable = new JTable(clubsModel);

        List<Document> approvedClubs = clubDao.getApprovedClubs();
        for(Document clubs: approvedClubs){
            String clubName = clubs.getString("Club_name");
            String description = clubs.getString("Description");
            String adminName = clubs.getString("Admin_name");
            String approved = "Yes";
          //  String membersCount = (String) clubs.get("Members");

            clubsModel.addRow(new Object[]{
                    clubName,
                    description,
                    adminName,
                    approved,
                //    membersCount
            });

        }


        panel.add(new JScrollPane(clubsTable), BorderLayout.CENTER);
        return panel;
    }

    // ------------------ Events Tab -------------------
    private JPanel createEventsTab() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"Event Title", "Date", "Club", "Approved"};
        eventsModel = new DefaultTableModel(columns, 0);
        eventsTable = new JTable(eventsModel);

        List<Document> approvedEvents = eventDao.getApprovedEvents();
        for (Document events : approvedEvents){
            String eventTitle = events.getString("Title");
           Date date = events.getDate("Date");
          //  String time = events.getString("Time");
            String clubName = events.getString("Club_Name");
            String approved = "Yes";

            eventsModel.addRow(new Object[]{
                    eventTitle,
                   date,
                   // time,
                    clubName,
                    approved
            });
        }



        panel.add(new JScrollPane(eventsTable), BorderLayout.CENTER);
        return panel;
    }

    // ------------------ Pending Approvals Tab -------------------
    private JPanel createPendingTab() {
        JPanel panel = new JPanel(new BorderLayout());



        String[] columns = {"Type", "Name", "Submitted By", "Approve"};
        pendingModel = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) { // Approved column
                    return Boolean.class; // Checkbox
                }
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Only checkbox is editable
            }
        };

        pendingTable = new JTable(pendingModel);


        List<Document> pendingEvents = eventDao.getPendingEvents();
        for (Document event : pendingEvents) {
            String title = event.getString("Title");
            String clubName = event.getString("Club_Name");

            pendingModel.addRow(new Object[]{
                    "Event",
                    title,
                    clubName,
                    false
            });
        }

        List<Document> pendingClubs = clubDao.getPendingClubs();
        for (Document club : pendingClubs) {
            String clubName = club.getString("Club_name");
            String adminName = club.getString("Admin_name");

            pendingModel.addRow(new Object[]{
                    "Club",
                    clubName,
                    adminName,
                    false
            });
        }

        pendingTable.getModel().addTableModelListener(e -> {
            int row = e.getFirstRow();
            int col = e.getColumn();

            if (col == 3) { // Checkbox column
                Boolean isApproved = (Boolean) pendingTable.getValueAt(row, col);
                if (isApproved != null && isApproved) {
                    String type = (String) pendingTable.getValueAt(row, 0);
                    String name = (String) pendingTable.getValueAt(row, 1);

                    if (type.equalsIgnoreCase("Event")) {
                        eventDao.approveEvent(name);
                    } else if (type.equalsIgnoreCase("Club")) {
                        clubDao.approveClub(name);
                    }

                    // Remove row from table
                    pendingModel.removeRow(row);

                    JOptionPane.showMessageDialog(this, type + " '" + name + "' approved!");
                }
            }
        });


        panel.add(new JScrollPane(pendingTable), BorderLayout.CENTER);
        return panel;

    }


    // ------------------ Logout Tab -------------------
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
