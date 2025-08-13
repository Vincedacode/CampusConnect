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

    private DefaultTableModel clubsModel, eventsModel;
    private JTable clubsTable, eventsTable;

    private JTabbedPane tabbedPane;
    private eventdao eventDao = new eventdao();
    private clubdao clubDao = new clubdao();
    private studentdao studentDao = new studentdao();

    public StudentDashboard(String fullName, Document studentData) {
        this.fullName = fullName;
        this.studentData = studentData;

        // Window Setup
        setTitle("Welcome, " + fullName);
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Tabbed Pane
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        tabbedPane.add("📚 Available Clubs", createClubsTab());
        tabbedPane.add("🎉 Available Events", createEventsTab());
        tabbedPane.add("🚪 Logout", createLogoutTab());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createClubsTab() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Available Clubs to Join");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(title, BorderLayout.NORTH);

        String[] columns = {"Club Name", "Description", "Admin", "Join"};
        clubsModel = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return (columnIndex == 3) ? Boolean.class : String.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        clubsTable = new JTable(clubsModel);
        clubsTable.setRowHeight(25);
        clubsTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        clubsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Load Data
        List<String> joinedClubs = studentDao.getJoinedClubs(fullName);
        List<Document> approvedClubs = clubDao.getApprovedClubs();
        for (Document club : approvedClubs) {
            String clubName = club.getString("Club_name");
            if (!joinedClubs.contains(clubName)) {
                clubsModel.addRow(new Object[]{
                        clubName,
                        club.getString("Description"),
                        club.getString("Admin_name"),
                        false
                });
            }
        }

        // Checkbox action
        clubsTable.getModel().addTableModelListener(e -> {
            int row = e.getFirstRow();
            int col = e.getColumn();
            if (col == 3) {
                Boolean isJoining = (Boolean) clubsTable.getValueAt(row, col);
                if (isJoining != null && isJoining) {
                    String clubName = (String) clubsTable.getValueAt(row, 0);
                    clubDao.addStudentToClub(clubName, fullName);
                    studentDao.addClubToStudent(fullName, clubName);
                    clubsModel.removeRow(row);
                    JOptionPane.showMessageDialog(this, "You have joined '" + clubName + "'!");
                }
            }
        });

        panel.add(new JScrollPane(clubsTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createEventsTab() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Upcoming Events");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panel.add(title, BorderLayout.NORTH);

        String[] columns = {"Event Title", "Date", "Club", "Join"};
        eventsModel = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return (columnIndex == 3) ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        eventsTable = new JTable(eventsModel);
        eventsTable.setRowHeight(25);
        eventsTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        eventsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        List<Document> approvedEvents = eventDao.getApprovedEvents();
        List<String> joinedEvents = studentDao.getJoinedEvents(fullName);

        for (Document event : approvedEvents) {
            String titleText = event.getString("Title");

            // Format Date safely
            Date dateObj = event.getDate("Date");
            String formattedDate = (dateObj != null)
                    ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(dateObj)
                    : "N/A";



            if (!joinedEvents.contains(titleText)) {
                eventsModel.addRow(new Object[]{
                        titleText,
                        formattedDate,
                        event.getString("Club_Name"),
                        false
                });
            }
        }

        eventsTable.getModel().addTableModelListener(e -> {
            int row = e.getFirstRow();
            int col = e.getColumn();
            if (col == 3) {
                Boolean isJoining = (Boolean) eventsTable.getValueAt(row, col);
                if (isJoining != null && isJoining) {
                    String eventTitle = (String) eventsTable.getValueAt(row, 0);
                    eventDao.addStudentToAttendees(eventTitle, fullName);
                    studentDao.addEventToStudent(fullName, eventTitle);
                    eventsModel.removeRow(row);
                    JOptionPane.showMessageDialog(this, "You have registered for '" + eventTitle + "'!");
                }
            }
        });

        panel.add(new JScrollPane(eventsTable), BorderLayout.CENTER);
        return panel;
    }


    private JPanel createLogoutTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logoutBtn.setBackground(new Color(220, 53, 69));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setPreferredSize(new Dimension(150, 40));

        logoutBtn.addActionListener(e -> {
            new Main().setVisible(true);
            dispose();
        });

        panel.add(logoutBtn);
        return panel;
    }
}
