import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Main extends JFrame {

    private DefaultTableModel roomTableModel;
    private JTable roomTable;
    private boolean loggedIn = false; // Track if the user is logged in or not
    private String loggedInUser = ""; // Track the username of the logged-in user
    private Map<String, String> userDatabase; // Simulated user database
    private JButton registerButton;
    private JButton loginButton;
    private JButton logoutButton;
    private JButton viewButton;
    private JButton reserveButton;
    private JButton viewReservationsButton;
    private JButton viewAllReservationsButton;
    private Map<String, List<String>> reservationDatabase;
    private JCheckBox showPasswordCheckbox; // Checkbox to show/hide password
    private JComboBox<String> buildingComboBox; // Combo box to select a building
    private JPanel buttonPanel;

    public Main() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        reservationDatabase = loadReservationData();
        setTitle("List of Rooms");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Initialize user database
        userDatabase = new HashMap<>();

        // Load user data from file
        loadUserData();
        loadReservationData();
        // Create a panel to hold the components
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Create a label for the title
        JLabel titleLabel = new JLabel("List of Rooms");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));

        reservationDatabase = new HashMap<>();
        viewReservationsButton = new JButton("View Receipt");
        viewReservationsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (loggedIn) {
                    List<String> reservations = reservationDatabase.get(loggedInUser);
                    if (reservations != null && !reservations.isEmpty()) {
                        StringBuilder receiptMessage = new StringBuilder();
                        receiptMessage.append("Receipt for ").append(loggedInUser).append(":\n");

                        for (String reservation : reservations) {
                            receiptMessage.append(displayRoomDetails(reservation));
                            receiptMessage.append("\n");
                        }

                        JOptionPane.showMessageDialog(Main.this, receiptMessage.toString(), "Receipt",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(Main.this,
                                "You have no reservations.", "Receipt", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(Main.this, "Please login to view the receipt.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        viewReservationsButton.setFont(new Font("Arial", Font.BOLD, 16));
        viewReservationsButton.setBackground(new Color(21, 71, 52));
        viewReservationsButton.setForeground(Color.BLACK);
        viewReservationsButton.setFocusPainted(false);

        viewAllReservationsButton = new JButton("View All Reservations");
        viewAllReservationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle the button click event here
                String allReservations = displayAllReservations();
                JOptionPane.showMessageDialog(Main.this, allReservations, "All Reservations",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        viewAllReservationsButton.setFont(new Font("Arial", Font.BOLD, 16));
        viewAllReservationsButton.setBackground(new Color(21, 71, 52));
        viewAllReservationsButton.setForeground(Color.BLACK);
        viewAllReservationsButton.setFocusPainted(false);

        // Create a combo box to select a building
        buildingComboBox = new JComboBox<>();
        buildingComboBox.addItem("Gokongwei");
        buildingComboBox.addItem("Henry Sy");
        buildingComboBox.addItem("Velasco");
        buildingComboBox.addItem("Andrew");
        buildingComboBox.setFont(new Font("Arial", Font.BOLD, 16));
        buildingComboBox.setBackground(Color.WHITE);
        buildingComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateUI();
            }
        });

        // Create a table model for the room list
        roomTableModel = new DefaultTableModel();
        roomTableModel.addColumn("Room");

        // Add room data to the table model
        addRoomData("Gokongwei", "Room 101");
        addRoomData("Gokongwei", "Room 102");
        addRoomData("Gokongwei", "Room 103");
        addRoomData("Gokongwei", "Room 104");
        addRoomData("Gokongwei", "Room 105");
        addRoomData("Gokongwei", "Room 106");
        addRoomData("Gokongwei", "Room 107");
        addRoomData("Gokongwei", "Room 108");
        addRoomData("Gokongwei", "Room 109");
        addRoomData("Gokongwei", "Room 110");
        addRoomData("Henry Sy", "Room 201");
        addRoomData("Henry Sy", "Room 202");
        addRoomData("Henry Sy", "Room 203");
        addRoomData("Henry Sy", "Room 204");
        addRoomData("Henry Sy", "Room 205");
        addRoomData("Henry Sy", "Room 206");
        addRoomData("Henry Sy", "Room 207");
        addRoomData("Henry Sy", "Room 208");
        addRoomData("Henry Sy", "Room 209");
        addRoomData("Henry Sy", "Room 210");
        addRoomData("Velasco", "Room 301");
        addRoomData("Velasco", "Room 302");
        addRoomData("Velasco", "Room 303");
        addRoomData("Velasco", "Room 304");
        addRoomData("Velasco", "Room 305");
        addRoomData("Velasco", "Room 306");
        addRoomData("Velasco", "Room 307");
        addRoomData("Velasco", "Room 308");
        addRoomData("Velasco", "Room 309");
        addRoomData("Velasco", "Room 310");
        addRoomData("Andrew", "Room 401");
        addRoomData("Andrew", "Room 402");
        addRoomData("Andrew", "Room 403");
        addRoomData("Andrew", "Room 404");
        addRoomData("Andrew", "Room 405");
        addRoomData("Andrew", "Room 406");
        addRoomData("Andrew", "Room 407");
        addRoomData("Andrew", "Room 408");
        addRoomData("Andrew", "Room 409");
        addRoomData("Andrew", "Room 410");

        // Create a JTable to display the available rooms
        roomTable = new JTable(roomTableModel);
        roomTable.setDefaultEditor(Object.class, null);
        roomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roomTable.setRowHeight(40);
        roomTable.setBackground(Color.WHITE);
        roomTable.setFont(new Font("Arial", Font.PLAIN, 16));
        roomTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        roomTable.getTableHeader().setForeground(new Color(0, 120, 215));

        // Set custom renderer for the table header
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(new Color(21, 71, 52)); // Set desired color for the column names
        headerRenderer.setForeground(Color.WHITE); // Set desired text color for the column names
        roomTable.getTableHeader().setDefaultRenderer(headerRenderer);

        // Create a scroll pane for the room table
        JScrollPane scrollPane = new JScrollPane(roomTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Create a button to register
        registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = JOptionPane.showInputDialog(Main.this, "Enter Username:");
                if (username != null && !username.trim().isEmpty()) {
                    String password = showPasswordInputDialog(Main.this, "Enter Password:");
                    if (password != null && !password.trim().isEmpty()) {
                        if (userDatabase.containsKey(username)) {
                            JOptionPane.showMessageDialog(Main.this, "Username already exists.", "Registration Error",
                                    JOptionPane.ERROR_MESSAGE);
                        } else {
                            userDatabase.put(username, password);
                            saveUserData(); // Save user data
                            JOptionPane.showMessageDialog(Main.this, "User Registration Successful!", "Registration",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(Main.this, "Invalid Password.", "Registration Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(Main.this, "Invalid Username.", "Registration Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.setBackground(new Color(21, 71, 52));
        registerButton.setForeground(Color.BLACK);
        registerButton.setFocusPainted(false);

        // Create a button to login
        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = JOptionPane.showInputDialog(Main.this, "Enter Username:");
                if (username != null && !username.trim().isEmpty()) {
                    String password = showPasswordInputDialog(Main.this, "Enter Password:");
                    if (password != null && !password.trim().isEmpty()) {
                        if (userDatabase.containsKey(username) && userDatabase.get(username).equals(password)) {
                            loggedIn = true;
                            loggedInUser = username;
                            JOptionPane.showMessageDialog(Main.this, "Login Successful!", "Login",
                                    JOptionPane.INFORMATION_MESSAGE);
                            updateUI();
                        } else {
                            JOptionPane.showMessageDialog(Main.this, "Invalid Username or Password.", "Login Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(Main.this, "Invalid Password.", "Login Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(Main.this, "Invalid Username.", "Login Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(new Color(21, 71, 52));
        loginButton.setForeground(Color.BLACK);
        loginButton.setFocusPainted(false);

        // Create a button to view room details
        viewButton = new JButton("View Details");
        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (loggedIn) {
                    int selectedRow = roomTable.getSelectedRow();
                    if (selectedRow != -1) {
                        String selectedRoom = (String) roomTable.getValueAt(selectedRow, 0);
                        String roomDetails = displayRoomDetails(selectedRoom);

                        // Show the room details in a dialog box
                        JOptionPane.showMessageDialog(Main.this, roomDetails, "Room Details",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        // No room selected
                        JOptionPane.showMessageDialog(Main.this, "Please select a room.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // User not logged in
                    JOptionPane.showMessageDialog(Main.this, "Please login to view room details.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        viewButton.setFont(new Font("Arial", Font.BOLD, 16));
        viewButton.setBackground(new Color(21, 71, 52));
        viewButton.setForeground(Color.BLACK);
        viewButton.setFocusPainted(false);

        // Create a button to reserve a room
        reserveButton = new JButton("Reserve");
        reserveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (loggedIn) {
                    int selectedRow = roomTable.getSelectedRow();
                    if (selectedRow != -1) {
                        String selectedRoom = (String) roomTable.getValueAt(selectedRow, 0);

                        // Show the room details in a confirmation dialog
                        int option = JOptionPane.showConfirmDialog(Main.this,
                                "Do you want to reserve the following room?\n\n" + selectedRoom,
                                "Reservation Pre-confirmation", JOptionPane.YES_NO_OPTION);

                        if (option == JOptionPane.YES_OPTION) {
                            // Perform the reservation process here
                            String referenceNumber = generateReferenceNumber();

                            // Add the reservation to the user's list of reservations
                            List<String> userReservations = reservationDatabase.getOrDefault(loggedInUser,
                                    new ArrayList<>());
                            userReservations.add(selectedRoom);
                            reservationDatabase.put(loggedInUser, userReservations);

                            JOptionPane.showMessageDialog(Main.this,
                                    "Reserving room: " + selectedRoom + "\nReference Number: " + referenceNumber,
                                    "Reservation Confirmation", JOptionPane.INFORMATION_MESSAGE);
                            roomTableModel.removeRow(selectedRow);

                            // Save the reservation data
                            saveReservationData();
                        }
                    } else {
                        // No room selected
                        JOptionPane.showMessageDialog(Main.this, "Please select a room to reserve.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // User not logged in
                    JOptionPane.showMessageDialog(Main.this, "Please login to reserve a room.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

            private String generateReferenceNumber() {
                // Generate a random 6-digit reference number
                int min = 100000;
                int max = 999999;
                int referenceNumber = min + (int) (Math.random() * (max - min + 1));
                return String.valueOf(referenceNumber);
            }
        });
        reserveButton.setFont(new Font("Arial", Font.BOLD, 16));
        reserveButton.setBackground(new Color(255, 90, 95));
        reserveButton.setForeground(Color.BLACK);
        reserveButton.setFocusPainted(false);

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                performLogout();
            }
        });

        logoutButton.setFont(new Font("Arial", Font.BOLD, 16));
        logoutButton.setBackground(new Color(21, 71, 52));
        logoutButton.setForeground(Color.BLACK);
        logoutButton.setFocusPainted(false);

        // Create a panel to hold the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(registerButton);
        buttonPanel.add(loginButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(reserveButton);
        buttonPanel.add(logoutButton);
        buttonPanel.add(viewReservationsButton);
        buttonPanel.add(viewAllReservationsButton);

        // Create a panel to hold the building selection and button panel
        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new BorderLayout());
        selectionPanel.setOpaque(false);
        selectionPanel.add(buildingComboBox, BorderLayout.NORTH);
        selectionPanel.add(buttonPanel, BorderLayout.CENTER);

        // Add the components to the panel
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(selectionPanel, BorderLayout.SOUTH);

        // Add the panel to the frame
        add(panel);

        // Set the frame visible
        setVisible(true);

        updateUI();
    }

    private void addRoomData(String string, String string2) {
    }

    private void performLogout() {
        loggedIn = false;
        loggedInUser = "";
        saveReservationData(); // Save reservation data before logging out
        updateUI();
        JOptionPane.showMessageDialog(this, "You have been logged out.", "Logout", JOptionPane.INFORMATION_MESSAGE);
        buttonPanel.add(viewReservationsButton);
    }

    private void updateUI() {
        // Enable/disable buttons based on login status and building selection
        String selectedBuilding = (String) buildingComboBox.getSelectedItem();
        boolean buildingSelected = (selectedBuilding != null);
        registerButton.setEnabled(!loggedIn && buildingSelected);
        loginButton.setEnabled(!loggedIn && buildingSelected);
        viewButton.setEnabled(loggedIn && buildingSelected);
        reserveButton.setEnabled(loggedIn && buildingSelected);
        viewReservationsButton.setEnabled(loggedIn);
        logoutButton.setEnabled(loggedIn); // Enable the logout button when logged in

        setTitle(loggedIn ? "DLSU Rooms-Manila - Welcome, " + loggedInUser : "DLSU Rooms-Manila");
        updateRoomTable(selectedBuilding, selectedBuilding, selectedBuilding);
        roomTable.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()) {
            @Override
            public boolean isCellEditable(EventObject anEvent) {
                return false;
            }
        });

        // Filter buildings in the table based on the selected building
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(roomTableModel);
        if (buildingSelected) {
            sorter.setRowFilter(RowFilter.regexFilter(selectedBuilding));
        }
        roomTable.setRowSorter(sorter);
    }

    private void addRoomData(String building, String room, String time, String day) {
        roomTableModel.addRow(new Object[] { building + " - " + room, time, day });
    }

    private void updateRoomTable(String building, String time, String day) {
        roomTableModel.setRowCount(0);

        // Add room data based on the selected building
        if (building != null) {
            switch (building) {
                case "Gokongwei":
                    addRoomData("Gokongwei", "Room 101", "9:00 am - 12:00 pm", "Monday");
                    addRoomData("Gokongwei", "Room 102", "7:00 am - 10:00 am", "Monday");
                    addRoomData("Gokongwei", "Room 103", "10:00 am - 1:00 pm", "Tuesday");
                    addRoomData("Gokongwei", "Room 104", "7:30 am - 10:30 am", "Tuesday");
                    addRoomData("Gokongwei", "Room 105", "8:00 am - 11:00 am", "Wednesday");
                    addRoomData("Gokongwei", "Room 106", "3:00 pm - 6:00 pm", "Wednesday");
                    addRoomData("Gokongwei", "Room 107", "1:00 pm - 4:00 pm", "Thursday");
                    addRoomData("Gokongwei", "Room 108", "1:30 pm - 4:30 pm", "Thursday");
                    addRoomData("Gokongwei", "Room 109", "4:30 pm - 7:30 pm", "Friday");
                    addRoomData("Gokongwei", "Room 110", "1:45 pm - 3:45 pm", "Saturday");
                    break;
                case "Henry Sy":
                    addRoomData("Henry Sy", "Room 201", "11:45 am - 2:45 pm", "Monday");
                    addRoomData("Henry Sy", "Room 202", "1:45 pm - 4:45 pm", "Monday");
                    addRoomData("Henry Sy", "Room 203", "2:00 pm - 5:00 pm", "Tuesday");
                    addRoomData("Henry Sy", "Room 204", "7:30 am - 10:30 am", "Wednesday");
                    addRoomData("Henry Sy", "Room 205", "11:00 am - 1:00 pm", "Thursday");
                    addRoomData("Henry Sy", "Room 206", "2:45 pm - 4:45 pm", "Friday");
                    addRoomData("Henry Sy", "Room 207", "7:45 am - 10:45 am", "Friday");
                    addRoomData("Henry Sy", "Room 208", "11:45 am - 1:45 pm", "Saturday");
                    addRoomData("Henry Sy", "Room 209", "9:30 am - 11:30 am", "Saturday");
                    addRoomData("Henry Sy", "Room 210", "12:30 pm - 3:30 pm", "Saturday");
                    break;
                case "Velasco":
                    addRoomData("Velasco", "Room 301", "11:30 am - 2:30 pm", "Monday");
                    addRoomData("Velasco", "Room 302", "1:30 pm - 4:30 pm", "Tuesday");
                    addRoomData("Velasco", "Room 303", "3:30 pm - 6:30 pm", "Wednesday");
                    addRoomData("Velasco", "Room 304", "10:30 am - 1:30 pm", "Wednesday");
                    addRoomData("Velasco", "Room 305", "9:30 am - 11:30 am", "Wednesday");
                    addRoomData("Velasco", "Room 306", "10:00 am - 1:00 pm", "Thursday");
                    addRoomData("Velasco", "Room 307", "12:00 pm - 3:00 pm", "Friday");
                    addRoomData("Velasco", "Room 308", "7:45 am - 10:45 am", "Friday");
                    addRoomData("Velasco", "Room 309", "7:30 am - 9:30 am", "Saturday");
                    addRoomData("Velasco", "Room 310", "12:30 pm - 3:30 pm", "Saturday");
                    break;
                case "Andrew":
                    addRoomData("Andrew", "Room 401", "8:00 am - 11:00 am", "Monday");
                    addRoomData("Andrew", "Room 402", "7:30 am - 8:30 am", "Wednesday");
                    addRoomData("Andrew", "Room 403", "9:00 am - 12:00 pm", "Wednesday");
                    addRoomData("Andrew", "Room 404", "4:00 pm - 6:00 pm", "Thursday");
                    addRoomData("Andrew", "Room 405", "12:00 pm - 2:00 pm", "Thursday");
                    addRoomData("Andrew", "Room 406", "10:00 am - 12:00 pm", "Friday");
                    addRoomData("Andrew", "Room 407", "12:00 pm - 3:00 pm", "Friday");
                    addRoomData("Andrew", "Room 408", "5:00 pm - 7:00 pm", "Saturday");
                    addRoomData("Andrew", "Room 409", "9:00 am - 12:00 pm", "Saturday");
                    addRoomData("Andrew", "Room 410", "11:00 am - 1:00 pm", "Saturday");
                    break;
                default:
                    break;
            }
        }
    }

    private String displayRoomDetails(String reservation) {
        String roomDetails = "";
        switch (reservation) {
            // GOKONGWEI DESCRIPTION
            case "Gokongwei - Room 101":
                roomDetails = "Gokongwei - Room 101\nCapacity: 40\nDay of Availability: Monday\nTime of Availability: 9:00 am - 12:00pm \n";
                break;
            case "Gokongwei - Room 102":
                roomDetails = "Gokongwei - Room 102\nCapacity: 30\nDay of Availability: Monday\nTime of Availability: 7:00 am - 10:00 am,\n";
                break;
            case "Gokongwei - Room 103":
                roomDetails = "Gokongwei - Room 103\nCapacity: 40\nDay of Availability: Tuesday\nTime of Availability: 10:00 am - 1:00 pm \n";
                break;
            case "Gokongwei - Room 104":
                roomDetails = "Gokongwei - Room 104\nCapacity: 20\nDay of Availability: Tuesday\nTime of Availability: 7:30 am - 10:30 pm\n";
                break;
            case "Gokongwei - Room 105":
                roomDetails = "Gokongwei - Room 105\nCapacity: 35\nDay of Availability: Wednesday\nTime of Availability: 8:00 am - 11:00 am\n";
                break;
            case "Gokongwei - Room 106":
                roomDetails = "Gokongwei - Room 106\nCapacity: 45\nDay of Availability: Wednesday\nTime of Availability: 3:00 - 6:00 pm\n";
                break;
            case "Gokongwei - Room 107":
                roomDetails = "Gokongwei - Room 107\nCapacity: 50\nDay of Availability: Thursday\nTime of Availability:  1:00 pm - 4:00 pm \n";
                break;
            case "Gokongwei - Room 108":
                roomDetails = "Gokongwei - Room 108\nCapacity: 30\nDay of Availability: Thursday\nTime of Availability:  1:30 pm - 4:30 pm\n";
                break;
            case "Gokongwei - Room 109":
                roomDetails = "Gokongwei - Room 109\nCapacity: 40\nDay of Availability: Friday\nTime of Availability: 4:30 pm - 7:30 pm\n";
                break;
            case "Gokongwei - Room 110":
                roomDetails = "Gokongwei - Room 110\nCapacity: 20\nDay of Availability: Saturday\nTime of Availability: 1:45 pm - 3:45 pm\n";
                break;
            // HENRY SY DESCRIPTION
            case "Henry Sy - Room 201":
                roomDetails = "Henry Sy - Room 201\nCapacity: 25\nDay of Availability: Monday\nTime of Availability: 11:45 am - 2:45 pm\n";
                break;
            case "Henry Sy - Room 202":
                roomDetails = "Henry Sy - Room 202\nCapacity: 35\nDay of Availability: Monday\nTime of Availability: 1:45 pm - 4:45 pm \n";
                break;
            case "Henry Sy - Room 203":
                roomDetails = "Henry Sy - Room 203\nCapacity: 40\nDay of Availability: Tuesday\nTime of Availability: 2:00 pm - 5:00 pm\n";
                break;
            case "Henry Sy - Room 204":
                roomDetails = "Henry Sy - Room 204\nCapacity: 15\nDay of Availability: Wednesday\nTime of Availability: 7:30 am - 10:30 am \n";
                break;
            case "Henry Sy - Room 205":
                roomDetails = "Henry Sy - Room 205\nCapacity: 20\nDay of Availability: Thursday\nTime of Availability: 11:00 am - 1:00 pm\n";
                break;
            case "Henry Sy - Room 206":
                roomDetails = "Henry Sy - Room 206\nCapacity: 35\nDay of Availability: Friday\nTime of Availability:  2:45 pm - 4:45 pm\n";
                break;
            case "Henry Sy - Room 207":
                roomDetails = "Henry Sy - Room 207\nCapacity: 20\nDay of Availability: Friday\nTime of Availability: 7:45 am - 10:45 am\n";
                break;
            case "Henry Sy - Room 208":
                roomDetails = "Henry Sy - Room 208\nCapacity: 25\nDay of Availability: Saturday\nTime of Availability: 11:45 am - 1:45 pm\n";
                break;
            case "Henry Sy - Room 209":
                roomDetails = "Henry Sy - Room 209\nCapacity: 45\nDay of Availability: Saturday\nTime of Availability: 9:30 am - 11:30 am\n";
                break;
            case "Henry Sy - Room 210":
                roomDetails = "Henry Sy - Room 210\nCapacity: 25\nDay of Availability: Saturday\nTime of Availability: 12:30 pm - 3:30 pm \n";
                break;
            // VELASCO DESCRIPTION
            case "Velasco - Room 301":
                roomDetails = "Velasco - Room 301\nCapacity: 25\nDay of Availability: Monday\nTime of Availability: 11:30 am - 2:30 pm\n";
                break;
            case "Velasco - Room 302":
                roomDetails = "Velasco - Room 302\nCapacity: 35\nDay of Availability: Tuesday\nTime of Availability: 1:30 pm - 4:30 pm\n";
                break;
            case "Velasco - Room 303":
                roomDetails = "Velasco - Room 303\nCapacity: 30\nDay of Availability: Wednesday\nTime of Availability:  3:30 pm  - 6:30 pm\n";
                break;
            case "Velasco - Room 304":
                roomDetails = "Velasco - Room 304\nCapacity: 40\nDay of Availability: Wednesday\nTime of Availability:  10:30 am - 1:30 pm\n";
                break;
            case "Velasco - Room 305":
                roomDetails = "Velasco - Room 305\nCapacity: 20\nDay of Availability: Wednesday\nTime of Availability: 9:30 am - 11:30am\n";
                break;
            case "Velasco - Room 306":
                roomDetails = "Velasco - Room 306\nCapacity: 30\nDay of Availability: Thursday\nTime of Availability: 10:00 am - 1:00 pm\n";
                break;
            case "Velasco - Room 307":
                roomDetails = "Velasco - Room 307\nCapacity: 25\nDay of Availability: Friday\nTime of Availability: 12:00 pm - 3:00 pm\n";
                break;
            case "Velasco - Room 308":
                roomDetails = "Velasco - Room 308\nCapacity: 45\nDay of Availability: Friday\nTime of Availability: 7:45 am - 10:45 am \n";
                break;
            case "Velasco - Room 309":
                roomDetails = "Velasco - Room 309\nCapacity: 30\nDay of Availability: Saturday\nTime of Availability: 7:30 am - 9:30 am\n";
                break;
            case "Velasco - Room 310":
                roomDetails = "Velasco - Room 310\nCapacity: 25\nDay of Availability: Saturday\nTime of Availability: 12:30 pm - 3:30 pm\n";
                break;

            // ANDREW DESCRIPTION
            case "Andrew - Room 401":
                roomDetails = "Andrew - Room 401\nCapacity: 30\nDay of Availability: Monday\nTime of Availability: 8:00 am - 11:00 \n";
                break;
            case "Andrew - Room 402":
                roomDetails = "Andrew - Room 402\nCapacity: 35\nDay of Availability: Wednesday\nTime of Availability: 7:30 am - 8:30 am\n";
                break;
            case "Andrew - Room 403":
                roomDetails = "Andrew - Room 403\nCapacity: 30\nDay of Availability: Wednesday\nTime of Availability: 9:00 am - 12:00 pm\n";
                break;
            case "Andrew - Room 404":
                roomDetails = "Andrew - Room 404\nCapacity: 20\nDay of Availability: Thursday\nTime of Availability:  4:00 pm - 6:00 pm\n";
                break;
            case "Andrew - Room 405":
                roomDetails = "Andrew - Room 405\nCapacity: 40\nDay of Availability: Thursday\nTime of Availability: 12:00 pm - 2:00 pm\n";
                break;
            case "Andrew - Room 406":
                roomDetails = "Andrew - Room 406\nCapacity: 20\nDay of Availability: Friday\nTime of Availability: 10:00 am - 12:00 pm\n";
                break;
            case "Andrew - Room 407":
                roomDetails = "Andrew - Room 407\nCapacity: 25\nDay of Availability: Friday\nTime of Availability: 12:00 pm - 3:00 pm\n";
                break;
            case "Andrew - Room 408":
                roomDetails = "Andrew - Room 408\nCapacity: 35\nDay of Availability: Saturday\nTime of Availability: 5:00 pm - 7:00 pm\n";
                break;
            case "Andrew - Room 409":
                roomDetails = "Andrew - Room 409\nCapacity: 20\nDay of Availability: Saturday\nTime of Availability: 9:00 am - 12:00 pm\n";
                break;
            case "Andrew - Room 410":
                roomDetails = "Andrew - Room 410\nCapacity: 30\nDay of Availability: Saturday\nTime of Availability: 11:00 am - 1:00 pm \n";
                break;
            // ADD THE FOLLOWING CASE TO DISPLAY RESERVATION DETAILS
            case "Reference Number - Room Building - Room Number - Time and Day":
                // Example: "Ref123 - Gokongwei - Room 101 - 9:00 am - 12:00 pm, Monday"
                roomDetails = "Reservation Details:\n" + reservation.replace(" - ", "\n");
                break;
        }
        return roomDetails;
    }

    private String displayAllReservations() {
        // Create checkboxes for the user to choose the organization options
        JCheckBox byDayCheckBox = new JCheckBox("By Day");
        JCheckBox byRoomCheckBox = new JCheckBox("By Room");
        JCheckBox byBuildingCheckBox = new JCheckBox("By Building");

        // Put the checkboxes in a panel
        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));
        checkBoxPanel.add(byDayCheckBox);
        checkBoxPanel.add(byRoomCheckBox);
        checkBoxPanel.add(byBuildingCheckBox);

        // Show the checkbox dialog to the user
        int option = JOptionPane.showConfirmDialog(Main.this, checkBoxPanel, "Choose organization options",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // Check the user's selection and display reservations accordingly
        if (option == JOptionPane.OK_OPTION) {
            boolean byDaySelected = byDayCheckBox.isSelected();
            boolean byRoomSelected = byRoomCheckBox.isSelected();
            boolean byBuildingSelected = byBuildingCheckBox.isSelected();

            try {
                FileInputStream fileIn = new FileInputStream("reservationdata.ser");
                ObjectInputStream in = new ObjectInputStream(fileIn);
                Object obj = in.readObject();
                in.close();
                fileIn.close();

                if (obj instanceof Map) {
                    Map<String, List<String>> reservationData = (Map<String, List<String>>) obj;
                    StringBuilder allReservationsBuilder = new StringBuilder("All Reservations:\n");

                    if (reservationData.isEmpty()) {
                        allReservationsBuilder.append("No reservations found.");
                    } else {
                        // Display reservations organized by day
                        if (byDaySelected) {
                            allReservationsBuilder.append("Reservations Organized by Day:\n");
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Map<String, StringBuilder> reservationsByDay = new HashMap<>();

                            for (Map.Entry<String, List<String>> entry : reservationData.entrySet()) {
                                List<String> userReservations = entry.getValue();

                                for (String reservation : userReservations) {
                                    String roomDetails = displayRoomDetails(reservation);

                                    // Parse the reservation date and group reservations by day
                                    Date reservationDate = parseReservationDate(reservation);
                                    String dayKey = dateFormat.format(reservationDate);

                                    reservationsByDay.putIfAbsent(dayKey, new StringBuilder());
                                    reservationsByDay.get(dayKey).append(roomDetails).append("\n");
                                }
                            }

                            // Append the reservations for each day to the output
                            for (Map.Entry<String, StringBuilder> entry : reservationsByDay.entrySet()) {
                                String day = entry.getKey();
                                StringBuilder reservationsForDay = entry.getValue();

                                allReservationsBuilder.append("Date: ").append(day).append("\n");
                                allReservationsBuilder.append(reservationsForDay).append("\n");
                            }
                        }

                        // Display reservations organized by room
                        if (byRoomSelected) {
                            allReservationsBuilder.append("Reservations Organized by Room:\n");
                            Map<String, StringBuilder> reservationsByRoom = new HashMap<>();

                            for (Map.Entry<String, List<String>> entry : reservationData.entrySet()) {
                                List<String> userReservations = entry.getValue();

                                for (String reservation : userReservations) {
                                    String roomDetails = displayRoomDetails(reservation);

                                    // Extract the room name from the reservation string
                                    String roomName = extractRoomName(reservation);

                                    reservationsByRoom.putIfAbsent(roomName, new StringBuilder());
                                    reservationsByRoom.get(roomName).append(roomDetails).append("\n");
                                }
                            }

                            // Append the reservations for each room to the output
                            for (Map.Entry<String, StringBuilder> entry : reservationsByRoom.entrySet()) {
                                String roomName = entry.getKey();
                                StringBuilder reservationsForRoom = entry.getValue();

                                allReservationsBuilder.append("Room: ").append(roomName).append("\n");
                                allReservationsBuilder.append(reservationsForRoom).append("\n");
                            }
                        }

                        // Display reservations organized by building
                        if (byBuildingSelected) {
                            allReservationsBuilder.append("Reservations Organized by Building:\n");
                            Map<String, StringBuilder> reservationsByBuilding = new HashMap<>();

                            for (Map.Entry<String, List<String>> entry : reservationData.entrySet()) {
                                List<String> userReservations = entry.getValue();

                                for (String reservation : userReservations) {
                                    String roomDetails = displayRoomDetails(reservation);

                                    // Extract the building name from the reservation string
                                    String buildingName = extractBuildingName(reservation);

                                    reservationsByBuilding.putIfAbsent(buildingName, new StringBuilder());
                                    reservationsByBuilding.get(buildingName).append(roomDetails).append("\n");
                                }
                            }

                            // Append the reservations for each building to the output
                            for (Map.Entry<String, StringBuilder> entry : reservationsByBuilding.entrySet()) {
                                String buildingName = entry.getKey();
                                StringBuilder reservationsForBuilding = entry.getValue();

                                allReservationsBuilder.append("Building: ").append(buildingName).append("\n");
                                allReservationsBuilder.append(reservationsForBuilding).append("\n");
                            }
                        }
                    }

                    JTextArea textArea = new JTextArea(allReservationsBuilder.toString());
                    textArea.setEditable(false);

                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new Dimension(800, 400));

                    JOptionPane.showMessageDialog(Main.this, scrollPane, "All Reservations", JOptionPane.PLAIN_MESSAGE);
                } else {
                    System.err.println("Error loading reservation data: Invalid data format.");
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading reservation data: " + e.getMessage());
            }
        }
        return loggedInUser;
    }

    private Date parseReservationDate(String reservation) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String[] parts = reservation.split(", ");
            for (String part : parts) {
                if (part.startsWith("Date: ")) {
                    String dateString = part.substring(6); // Extract the date string after "Date: "
                    return dateFormat.parse(dateString);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Date();
    }

    private String extractRoomName(String reservation) {
        int roomStartIndex = reservation.indexOf("Room: ") + 6;
        int roomEndIndex = reservation.indexOf(",", roomStartIndex);
        if (roomEndIndex == -1) {
            roomEndIndex = reservation.indexOf(" ", roomStartIndex);
        }

        return reservation.substring(roomStartIndex, roomEndIndex);
    }

    private String extractBuildingName(String reservation) {
        int buildingStartIndex = reservation.indexOf("Building: ") + 10;
        int buildingEndIndex = reservation.indexOf(",", buildingStartIndex);
        if (buildingEndIndex == -1) {
            buildingEndIndex = reservation.indexOf(" ", buildingStartIndex);
        }

        return reservation.substring(buildingStartIndex, buildingEndIndex);
    }

    private void saveUserData() {
        try {
            FileOutputStream fileOut = new FileOutputStream("userdata.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(userDatabase);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUserData() {
        try {
            FileInputStream fileIn = new FileInputStream("userdata.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            userDatabase = (Map<String, String>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int nextReferenceNumber = 1000;

    public void addReservation(String username, String building, String room, String timeAndDay) {
        if (!loggedIn) {
            // Add a check to ensure that the user is logged in before making a reservation
            JOptionPane.showMessageDialog(Main.this, "Please log in before making a reservation.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isRoomAvailable(building, room, timeAndDay, username)) {
            // Check if the room is available for the given time and day
            JOptionPane.showMessageDialog(Main.this, "Sorry, the selected room is already reserved.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String reference = "Ref" + nextReferenceNumber; // Generate the reference number
        nextReferenceNumber++; // Increment the reference number for the next reservation
        String reservationInfo = reference + " - " + building + " - " + room + " - " + timeAndDay; // Include reference
                                                                                                   // number in the
                                                                                                   // reservation info

        List<String> reservations = reservationDatabase.getOrDefault(username, new ArrayList<>());
        if (reservations.contains(reservationInfo)) {
            // Check if the user already has a reservation for this room at the selected
            // time and day
            JOptionPane.showMessageDialog(Main.this,
                    "You already have a reservation for this room at the selected time and day.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        reservations.add(reservationInfo);
        reservationDatabase.put(username, reservations);

        saveReservationData(); // Save the updated reservation data immediately after adding a reservation

        JOptionPane.showMessageDialog(Main.this, "Reservation successful.\n" + "Reference Number: " + reference,
                "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean isRoomAvailable(String building, String room, String timeAndDay, String username) {
        for (List<String> reservations : reservationDatabase.values()) {
            for (String reservation : reservations) {
                String[] reservationDetails = reservation.split(" - ");
                if (reservationDetails.length == 4) {
                    String reservedBuilding = reservationDetails[1].trim();
                    String reservedRoom = reservationDetails[2].trim();
                    String reservedTimeAndDay = reservationDetails[3].trim();

                    // Check if the room is reserved for the given time and day
                    if (reservedBuilding.equals(building) && reservedRoom.equals(room)
                            && reservedTimeAndDay.equals(timeAndDay)) {
                        return false; // Room is not available
                    }

                    // Check if the user already has a reservation for the same room and time
                    if (reservationDetails[0].startsWith("Ref") && reservationDetails[1].equals(building)
                            && reservationDetails[2].equals(room) && reservationDetails[3].equals(timeAndDay)
                            && username.equals(getUsernameFromReference(reservationDetails[0]))) {
                        return false; // Room is not available
                    }
                }
            }
        }
        return true; // Room is available
    }

    private String getUsernameFromReference(String reference) {
        // Extract the username from the reference number (RefXXX)
        return reference.substring(3);
    }

    public void saveReservationData() {
        try {
            File file = new File("reservationdata.ser");
            Map<String, List<String>> existingData = new HashMap<>();

            // If the file already exists, load its contents
            if (file.exists()) {
                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                Object obj = in.readObject();
                in.close();
                fileIn.close();
                if (obj instanceof Map) {
                    existingData = (Map<String, List<String>>) obj;
                } else {
                    System.err.println("Error loading reservation data: Invalid data format.");
                }
            }

            // Update existing data with new reservations from reservationDatabase
            for (Map.Entry<String, List<String>> entry : reservationDatabase.entrySet()) {
                String username = entry.getKey();
                List<String> reservations = entry.getValue();

                List<String> existingReservations = existingData.getOrDefault(username, new ArrayList<>());
                existingReservations.addAll(reservations);
                existingData.put(username, existingReservations);
            }

            FileOutputStream fileOut = new FileOutputStream("reservationdata.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(existingData); // Write the updated data to the file
            out.close();
            fileOut.close();
            System.out.println("Reservation data saved successfully.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Error saving reservation data: " + e.getMessage());
        }
    }

    private Map<String, List<String>> loadReservationData() {
        try {
            FileInputStream fileIn = new FileInputStream("reservationdata.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Object obj = in.readObject();
            in.close();
            fileIn.close();
            if (obj instanceof Map) {
                return (Map<String, List<String>>) obj;
            } else {
                System.err.println("Error loading reservation data: Invalid data format.");
                return new HashMap<>(); // Initialize with an empty map if data format is invalid
            }
        } catch (IOException | ClassNotFoundException e) {
            // If the file doesn't exist or cannot be read, return an empty map
            return new HashMap<>();
        }
    }

    private String showPasswordInputDialog(Component parentComponent, String message) {
        JPanel panel = new JPanel();
        JPasswordField passwordField = new JPasswordField(10);
        showPasswordCheckbox = new JCheckBox("Show Password");

        panel.add(new JLabel(message));
        panel.add(passwordField);
        panel.add(showPasswordCheckbox);

        showPasswordCheckbox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    passwordField.setEchoChar((char) 0);
                } else {
                    passwordField.setEchoChar('*');
                }
            }
        });

        int option = JOptionPane.showOptionDialog(parentComponent, panel, "Enter Password:",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

        if (option == JOptionPane.OK_OPTION) {
            return new String(passwordField.getPassword());
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main();
            }
        });
    }
}
