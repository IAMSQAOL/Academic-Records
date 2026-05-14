package GUI;

import javax.swing.*;

import Users.User;
import database.DBConnection;

import java.awt.*;
import java.sql.ResultSet;
import GUI.participant.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParticipantDashboard extends JFrame {
    private JPanel topPanel;
    private JPanel contentPanel;
    private JPanel eventsPanel;
    private User currentUser;
    private JButton viewEventBtn;
    private JButton myRegisterdBtn;
    private JButton logoutButton;
    
    public ParticipantDashboard(User user) {
        this.currentUser = user;
        setTitle("Event Management System | Participant Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
        setLocationRelativeTo(null);

        // Top Panel with Title and Type Filter
        topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        JLabel title = new JLabel("Welcome, " + currentUser.getUsername());
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        topPanel.add(title, BorderLayout.WEST);

        viewEventBtn = new JButton("Events");
        styleButton(viewEventBtn);
        viewEventBtn.addActionListener(e -> viewEvents(currentUser));

        myRegisterdBtn = new JButton("My Registrations");
        styleButton(myRegisterdBtn);
        myRegisterdBtn.addActionListener(e -> {
            contentPanel.removeAll();
            contentPanel.add(new MyRegistrationsPanel(this,currentUser)); 
            contentPanel.revalidate();
            contentPanel.repaint();
        });

        logoutButton = new JButton("Logout");
        styleButton(logoutButton);
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to log out?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                dispose(); 
                new LoginFrame(); 
            }
        });

     
        contentPanel = new JPanel(new BorderLayout());
        
        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonContainer.setOpaque(false); 
        buttonContainer.add(viewEventBtn);
        buttonContainer.add(myRegisterdBtn);
        buttonContainer.add(logoutButton);
        topPanel.add(buttonContainer,BorderLayout.EAST);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        viewEvents(currentUser);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(103, 80, 164));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private JPanel createEventCard(String title, String type, String datetime, String venue,
                                   int registered, int capacity, double fee, String organizer,int event_id) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 228, 232), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel desc = new JLabel(datetime);
        JLabel loc = new JLabel(venue);
        JLabel stats = new JLabel(registered + "/" + capacity + " registered");
        JLabel feeLabel = new JLabel("Base Fee: " + (fee == 0.0 ? "Free" : "$" + fee));
        JLabel speaker = new JLabel(organizer);

        Font infoFont = new Font("Segoe UI", Font.PLAIN, 13);
        for (JLabel label : new JLabel[]{desc, loc, stats, feeLabel, speaker}) {
            label.setFont(infoFont);
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        JButton registerBtn = new JButton("Register");
        registerBtn.setBackground(new Color(103, 80, 164));
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        registerBtn.setFocusPainted(false);
        registerBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerBtn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        registerBtn.addActionListener(e -> {
            try {
                new RegisterEventDialog(this,event_id, title,datetime, venue,fee,currentUser);
            } catch (SQLException ex) {
                Logger.getLogger(ParticipantDashboard.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(desc);
        card.add(loc);
        card.add(stats);
        card.add(feeLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(speaker);
        card.add(Box.createVerticalGlue());
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(registerBtn);

        return card;
    }
    
    private void viewEvents(User user){
        contentPanel.removeAll();

        eventsPanel = new JPanel();
        eventsPanel.setLayout(new GridLayout(0, 2, 20, 20));
        eventsPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));
        eventsPanel.setBackground(new Color(245, 247, 250));

        ResultSet rs = DBConnection.getRS("SELECT * FROM Events WHERE Event_id NOT IN (SELECT Event_id FROM Registration WHERE userID = ?)",user.getUserId()); 
        try {
                while(rs.next()){
                        String event_title = rs.getString("Event_name");
                        String event_type = rs.getString("Event_type");
                        String event_date = rs.getString("Event_date");
                        String event_time = rs.getString("Event_time");
                        String event_datetime = event_date + " at " + event_time;
                        String event_venue = rs.getString("Venue");
                        int event_id = rs.getInt("Event_id");
                        ResultSet register_s = DBConnection.getRS("SELECT SUM(applicant_amount) as totalr FROM Registration WHERE Event_id = ?", event_id);
                        int registered = register_s.getInt("totalr");
                        int capacity = rs.getInt("Capacity");
                        double base_fee = rs.getDouble("Fee");
                        int organizerid = rs.getInt("Event_creator_id");
                        ResultSet og = DBConnection.getRS("SELECT name FROM Users u JOIN Events e ON u.userID = e.Event_creator_id WHERE e.Event_creator_id = ?",organizerid);
                        if(og.next()){
                            String organizer = og.getString("name");
                            eventsPanel.add(createEventCard(event_title, event_type, event_datetime,
                                event_venue, registered, capacity,base_fee,organizer,event_id));
                        }else{
                            eventsPanel.add(createEventCard(event_title, event_type, event_datetime,
                                event_venue, registered, capacity,base_fee,"Unknown",event_id));
                        }
                }
        
        JScrollPane scrollPane = new JScrollPane(eventsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            eventsPanel.add(new JLabel("Error loading events."));
        }
    }

}
