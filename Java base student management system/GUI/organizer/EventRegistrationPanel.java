package GUI.organizer;

import Users.User;
import database.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventRegistrationPanel extends JPanel {
    List<Integer> event_id =  new ArrayList<>();
    
    public EventRegistrationPanel(User user) {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));
        
        // Top Section
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        JLabel titleLabel = new JLabel("View Registrations");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JPanel dropdownWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        dropdownWrapper.setBackground(Color.WHITE);
        ResultSet ev = DBConnection.getRS("SELECT * FROM Events WHERE Event_creator_id = ?", user.getUserId());
        List<String> eventList = new ArrayList<>();
        try {
            while (ev.next()) {
                int eventId = ev.getInt("Event_id");
                event_id.add(eventId);
                String eventName = ev.getString("Event_name");
                eventList.add(eventId + " - " + eventName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        String[] columns = {"Registration ID","Name", "Role","Email","Phone", "Catering", "Transport", "Fee Paid"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);

        JComboBox<String> eventSelector = new JComboBox<>(eventList.toArray(new String[0]));
        
        eventSelector.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        eventSelector.setPreferredSize(new Dimension(280, 35));
        eventSelector.addActionListener(e -> {
            int selectedIndex = eventSelector.getSelectedIndex();
            if (selectedIndex >= 0) {
                int selectedEventId = event_id.get(selectedIndex);
                updateTableForEvent(selectedEventId, tableModel);
            }
        });
        dropdownWrapper.add(eventSelector);
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(dropdownWrapper, BorderLayout.EAST);
        

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 30, 30, 30));
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void updateTableForEvent(int eventId, DefaultTableModel model) {
        model.setRowCount(0); 

        ResultSet rs = DBConnection.getRS("SELECT * FROM Registration r JOIN Events e ON r.Event_id = e.Event_id WHERE r.Event_id = ?", eventId);
        try {
            while (rs.next()) {
                String regId = rs.getString("Registration_id");
                String user_id = rs.getString("userID");

                ResultSet ne = DBConnection.getRS("SELECT * FROM Users u JOIN Registration r ON u.userID = r.userID WHERE u.userID = ?", user_id);
                    if(ne.next()){
                              String name = rs.getString("FullName"); 
                              String role = ne.getString("Role"); 
                              String email = rs.getString("Email");
                              String Phone = rs.getString("Phone");
                              String catering = rs.getString("cater_selected"); 
                              String transport = rs.getString("transport_selected"); 
                              double feePaid = rs.getDouble("final_fee");
                    model.addRow(new Object[]{
                        regId, name, role, email,Phone,catering, transport, "RM " + String.format("%.2f", feePaid)
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}

