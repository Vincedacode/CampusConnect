package org.example.CampusConnect.Admin;

import org.bson.Document;
import org.example.CampusConnect.DAO.*;
import org.example.CampusConnect.Main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
        setSize(1100, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main container with padding
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Header
        JLabel headerLabel = new JLabel("Welcome, " + name + " 👋", SwingConstants.LEFT);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerLabel.setForeground(new Color(33, 33, 33));
        headerLabel.setBorder(new EmptyBorder(0, 0, 10, 0));

        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Tabbed Pane Styling
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        tabbedPane.addTab("📚 Students", createStudentsTab());
        tabbedPane.addTab("🏛 Clubs", createClubsTab());
        tabbedPane.addTab("🎉 Events", createEventsTab());
        tabbedPane.addTab("✅ Pending Approvals", createPendingTab());
        tabbedPane.addTab("📊 Statistics", createStatisticsTab());
        tabbedPane.addTab("🚪 Logout", createLogoutTab());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JScrollPane styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(230, 230, 230));
        table.getTableHeader().setForeground(Color.BLACK);
        return new JScrollPane(table);
    }

    private JPanel createStudentsTab() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"Name", "Email", "Department", "Joined Clubs", "Registered Events"};
        studentsModel = new DefaultTableModel(columns, 0);
        studentsTable = new JTable(studentsModel);

        List<Document> allStudents = studentDao.getAllStudents();
        for (Document student : allStudents) {
            studentsModel.addRow(new Object[]{
                    student.getString("Fullname"),
                    student.getString("Email"),
                    student.getString("Department"),
                    listToString((List<String>) student.get("Joined_Clubs")),
                    listToString((List<String>) student.get("Registered_Events"))
            });
        }

        panel.add(styleTable(studentsTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createClubsTab() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"Club Name", "Description", "Admin", "Approved"};
        clubsModel = new DefaultTableModel(columns, 0);
        clubsTable = new JTable(clubsModel);

        List<Document> approvedClubs = clubDao.getApprovedClubs();
        for (Document clubs : approvedClubs) {
            clubsModel.addRow(new Object[]{
                    clubs.getString("Club_name"),
                    clubs.getString("Description"),
                    clubs.getString("Admin_name"),
                    "Yes"
            });
        }

        panel.add(styleTable(clubsTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createEventsTab() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"Event Title", "Date", "Time", "Club", "Approved"};
        eventsModel = new DefaultTableModel(columns, 0);
        eventsTable = new JTable(eventsModel);

        List<Document> approvedEvents = eventDao.getApprovedEvents();
        for (Document events : approvedEvents) {
            String eventTitle = events.getString("Title");

            // Handle Date
            Date date = events.getDate("Date");
            String formattedDate = (date != null)
                    ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(date)
                    : "N/A";

            // Handle Time safely (String or Date in DB)
            Object timeObj = events.get("Time");
            String time;
            if (timeObj instanceof Date) {
                time = new java.text.SimpleDateFormat("HH:mm").format((Date) timeObj);
            } else {
                time = String.valueOf(timeObj != null ? timeObj : "N/A");
            }

            String clubName = events.getString("Club_Name");
            String approved = "Yes";

            eventsModel.addRow(new Object[]{
                    eventTitle,
                    formattedDate,
                    time,
                    clubName,
                    approved
            });
        }

        panel.add(new JScrollPane(eventsTable), BorderLayout.CENTER);
        return panel;
    }



    private JPanel createPendingTab() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"Type", "Name", "Submitted By", "Approve"};
        pendingModel = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 3 ? Boolean.class : String.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        pendingTable = new JTable(pendingModel);

        List<Document> pendingEvents = eventDao.getPendingEvents();
        for (Document event : pendingEvents) {
            pendingModel.addRow(new Object[]{"Event", event.getString("Title"), event.getString("Club_Name"), false});
        }

        List<Document> pendingClubs = clubDao.getPendingClubs();
        for (Document club : pendingClubs) {
            pendingModel.addRow(new Object[]{"Club", club.getString("Club_name"), club.getString("Admin_name"), false});
        }

        pendingTable.getModel().addTableModelListener(e -> {
            int row = e.getFirstRow();
            int col = e.getColumn();

            if (col == 3 && Boolean.TRUE.equals(pendingTable.getValueAt(row, col))) {
                String type = (String) pendingTable.getValueAt(row, 0);
                String name = (String) pendingTable.getValueAt(row, 1);

                if (type.equalsIgnoreCase("Event")) {
                    eventDao.approveEvent(name);
                } else if (type.equalsIgnoreCase("Club")) {
                    clubDao.approveClub(name);
                }

                pendingModel.removeRow(row);
                JOptionPane.showMessageDialog(this, type + " '" + name + "' approved!");
            }
        });

        panel.add(styleTable(pendingTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStatisticsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        admindao adminDao = new admindao();

        long totalClubs = adminDao.getTotalClubs();
        long totalEvents = adminDao.getTotalEvents();
        JLabel summaryLabel = new JLabel("📌 Total Clubs: " + totalClubs + " | 🎯 Total Events: " + totalEvents);
        summaryLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        summaryLabel.setForeground(new Color(0, 102, 102));
        panel.add(summaryLabel, BorderLayout.NORTH);

        String[] clubColumns = {"Club Name", "Members"};
        DefaultTableModel clubStatsModel = new DefaultTableModel(clubColumns, 0);
        for (Document doc : adminDao.getMembersPerClub()) {
            clubStatsModel.addRow(new Object[]{doc.getString("Club_name"), doc.getInteger("memberCount")});
        }
        JTable clubStatsTable = new JTable(clubStatsModel);

        String[] eventColumns = {"Month", "Number of Events"};
        DefaultTableModel eventStatsModel = new DefaultTableModel(eventColumns, 0);
        String[] monthNames = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};

        for (Document doc : adminDao.getEventsPerMonth()) {
            int monthNumber = doc.getInteger("_id"); // 1-based month from DB
            String monthName = monthNames[monthNumber - 1];
            eventStatsModel.addRow(new Object[]{monthName, doc.getInteger("count")});
        }

        JTable eventStatsTable = new JTable(eventStatsModel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                styleTable(clubStatsTable), styleTable(eventStatsTable));
        splitPane.setDividerLocation(500);

        panel.add(splitPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createLogoutTab() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutBtn.setBackground(new Color(200, 0, 0));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setPreferredSize(new Dimension(120, 35));

        logoutBtn.addActionListener(e -> {
            new Main().setVisible(true);
            dispose();
        });

        panel.add(logoutBtn);
        return panel;
    }

    private String listToString(List<String> list) {
        return (list != null && !list.isEmpty()) ? String.join(", ", list) : "-";
    }
}
