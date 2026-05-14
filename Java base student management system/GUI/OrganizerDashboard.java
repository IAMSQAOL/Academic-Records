package GUI;

import GUI.organizer.CreateEventPanel;
import GUI.organizer.EditEventPanel; 
import GUI.organizer.EventRegistrationPanel;
import javax.swing.*;

import Users.User;
import database.DBConnection;
import java.sql.*;
import java.awt.*;
import java.awt.event.ActionListener;


import Events.Event; 
import Events.Seminar;
import Events.Workshop;
import Events.Cultural;
import static Events.Event.deleteEvent;
import Events.Sport;


public class OrganizerDashboard extends JFrame {
    private JPanel contentPanel;
    private JPanel eventsPanel;
    private JPanel topPanel;
    private JButton returnButton;
    private JButton createEventBtn;
    private JButton ViewRegistrationBtn;
    private User currentUser;
    private JButton logoutButton;

    public OrganizerDashboard(User user) {
        this.currentUser = user;
        setTitle("Event Management System | Organizer Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
        setLocationRelativeTo(null);

        contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

        topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        JLabel title = new JLabel("Event Management System: Welcome, " + user.getUsername());
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        topPanel.add(title, BorderLayout.WEST);

        ViewRegistrationBtn = new JButton("View Registration");
        styleButton(ViewRegistrationBtn);
        ViewRegistrationBtn.setVisible(true);
        ViewRegistrationBtn.addActionListener(e -> viewRegistrationsPanel(user));

        returnButton = new JButton("My Events");
        styleButton(returnButton);
        returnButton.setVisible(true);
        returnButton.addActionListener(e -> showEventsPanel(user));

        createEventBtn = new JButton("+ Create Event");
        styleButton(createEventBtn);
        createEventBtn.addActionListener(e -> showCreateEventPanel(currentUser));

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

        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonContainer.setOpaque(false);
        buttonContainer.add(ViewRegistrationBtn);
        buttonContainer.add(returnButton);
        buttonContainer.add(createEventBtn);
        buttonContainer.add(logoutButton);
        topPanel.add(buttonContainer, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        eventsPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        eventsPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));
        eventsPanel.setBackground(new Color(245, 247, 250));

        showEventsPanel(currentUser);

        setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(103, 80, 164));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private JPanel createEventCard(int eventId, String title, String type, String datetime, String venue,
                                   int registered, int capacity, String organizer, double fee) { 
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout()); // Using BorderLayout for card
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 228, 232), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Top content panel for labels
        JPanel topContentPanel = new JPanel();
        topContentPanel.setLayout(new BoxLayout(topContentPanel, BoxLayout.Y_AXIS));
        topContentPanel.setOpaque(false);
        topContentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel typeLabel = new JLabel(type);
        typeLabel.setOpaque(true);
        typeLabel.setBackground(new Color(230, 230, 250));
        typeLabel.setForeground(new Color(100, 50, 200));
        typeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        typeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        typeLabel.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
        typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel desc = new JLabel("Event date time: " + datetime);
        JLabel loc = new JLabel("Venue: " + venue);
        JLabel stats = new JLabel(registered + "/" + capacity + " registered");
        JLabel feeLabel = new JLabel("Fee: " + (fee == 0.0 ? "Free" : "$" + String.format("%.2f", fee))); // Display fee

        Font infoFont = new Font("Segoe UI", Font.PLAIN, 13);
        for (JLabel label : new JLabel[]{desc, loc, stats, feeLabel}) {
            label.setFont(infoFont);
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        JLabel organizerLabel = new JLabel("by " + organizer); 
        organizerLabel.setFont(infoFont);
        organizerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);


        topContentPanel.add(typeLabel);
        topContentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        topContentPanel.add(titleLabel);
        topContentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        topContentPanel.add(desc);
        topContentPanel.add(loc);
        topContentPanel.add(stats);
        topContentPanel.add(feeLabel); 
        topContentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        topContentPanel.add(organizerLabel); 
        topContentPanel.add(Box.createVerticalGlue()); 

        card.add(topContentPanel, BorderLayout.CENTER); 

        // Edit Button at the bottom right
        JButton editBtn = new JButton("Edit Event");
        editBtn.setBackground(new Color(103, 80, 164));
        editBtn.setForeground(Color.WHITE);
        editBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        editBtn.setFocusPainted(false);
        editBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        editBtn.setPreferredSize(new Dimension(130, 35)); 

        editBtn.addActionListener(e -> {
            // Fetch the full Event object using eventId
            Event eventToEdit = getEventById(eventId);
            if (eventToEdit != null) {
                showEditEventPanel(currentUser, eventToEdit);
            } else {
                JOptionPane.showMessageDialog(this, "Could not load event details for editing.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton cancelBtn = new JButton("Cancel Event");
        cancelBtn.setBackground(new Color(220, 53, 69)); 
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelBtn.setFocusPainted(false);
        cancelBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        cancelBtn.setPreferredSize(new Dimension(150, 35));
        
        cancelBtn.addActionListener(e -> cancelEvent(eventId));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0)); 
        buttonPanel.setOpaque(false);
        buttonPanel.add(cancelBtn);
        buttonPanel.add(editBtn);

        card.add(buttonPanel, BorderLayout.SOUTH); 

        return card;
    }


    private Event getEventById(int eventId) {
        ResultSet rs = null;
        try {
            // Assuming Event class has a constructor that takes all fields
            rs = DBConnection.getRS("SELECT * FROM Events WHERE Event_id = ?", eventId);
            if (rs.next()) {
                String name = rs.getString("Event_name");
                String type = rs.getString("Event_type");
                String date = rs.getString("Event_date");
                String time = rs.getString("Event_time");
                String venue = rs.getString("Venue");
                String organizer_id = rs.getString("Event_creator_id");
                int capacity = rs.getInt("Capacity");
                double fee = rs.getDouble("Fee");
                String description = rs.getString("Description");

                // Instantiate the correct Event subclass based on type
                switch (type) {
                    case "Seminar":
                        return new Seminar(eventId, name, date, time, venue, organizer_id, capacity, fee, description);
                    case "Workshop":
                        return new Workshop(eventId, name, date, time, venue, organizer_id, capacity, fee, description);
                    case "Cultural Event":
                        return new Cultural(eventId, name, date, time, venue, organizer_id, capacity, fee, description);
                    case "Sports Event":
                        return new Sport(eventId, name, date, time, venue, organizer_id, capacity, fee, description);
                    default:
                        return new Event(eventId, name, date, time, venue, organizer_id, capacity, fee, description) {
               
                        };
                }
            }
        } catch (SQLException e) {
            return null;
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { /* log */ }
        }
        return null; 
    }

     private void cancelEvent(int eventId) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel this event? This action cannot be undone.",
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Event.deleteEvent(eventId);
                showEventsPanel(currentUser);
            }catch(Exception e){
                   JOptionPane.showMessageDialog(this, "Failed to cancel event. Event not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
     }
    
    private void showEditEventPanel(User user, Event eventToEdit) {
        contentPanel.removeAll();
       
        createEventBtn.setVisible(false);
        ViewRegistrationBtn.setVisible(false);
        returnButton.setVisible(true); 
        for (ActionListener al : returnButton.getActionListeners()) {
            returnButton.removeActionListener(al); 
        }
        returnButton.addActionListener(e -> showEventsPanel(user)); 

        EditEventPanel editPanel = new EditEventPanel(user, eventToEdit);

        contentPanel.add(editPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }


    private void showCreateEventPanel(User user) {
        contentPanel.removeAll();

        createEventBtn.setVisible(false);
        ViewRegistrationBtn.setVisible(false); 
        returnButton.setVisible(true);
        for (ActionListener al : returnButton.getActionListeners()) {
            returnButton.removeActionListener(al);
        }
        returnButton.addActionListener(e -> showEventsPanel(user));

        CreateEventPanel createPanel = new CreateEventPanel(user);

        contentPanel.add(createPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void viewRegistrationsPanel(User user) {
        contentPanel.removeAll();
        // Hide main dashboard buttons, show My Events button
        createEventBtn.setVisible(false);
        ViewRegistrationBtn.setVisible(false);
        returnButton.setVisible(true);
        for (ActionListener al : returnButton.getActionListeners()) {
            returnButton.removeActionListener(al);
        }
        returnButton.addActionListener(e -> showEventsPanel(user));

        EventRegistrationPanel viewPanel = new EventRegistrationPanel(user);
        contentPanel.add(viewPanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }


    private void showEventsPanel(User user) {
        contentPanel.removeAll();
        eventsPanel.removeAll();
        eventsPanel.revalidate();
        eventsPanel.repaint();

        // Restore button visibility for this view
        createEventBtn.setVisible(true);
        ViewRegistrationBtn.setVisible(true);
        returnButton.setVisible(false); 
        for (ActionListener al : returnButton.getActionListeners()) {
            returnButton.removeActionListener(al);
        }

        try (ResultSet rs = DBConnection.getRS("SELECT * FROM Events WHERE Event_creator_id = ?", user.getUserId())) {
            boolean eventsFound = false;
            while (rs.next()) {
                eventsFound = true;
                String event_title = rs.getString("Event_name");
                String event_type = rs.getString("Event_type");
                String event_date = rs.getString("Event_date");
                String event_time = rs.getString("Event_time");
                String event_datetime = event_date + " at " + event_time;
                String event_venue = rs.getString("Venue");
                int event_id = rs.getInt("Event_id");
                int capacity = rs.getInt("Capacity");
                double fee = rs.getDouble("Fee"); // Get fee from ResultSet
                int registered = 0;

                try (ResultSet register_s = DBConnection.getRS("SELECT SUM(applicant_amount) AS total_participant FROM Registration WHERE Event_id = ?", event_id)) {
                    if (register_s.next()) {
                        registered = register_s.getInt("total_participant");
                    }
                } catch (SQLException e) {
                    System.err.println("Error fetching registration count for event " + event_id + ": " + e.getMessage());
                }

                // Pass eventId and fee to createEventCard
                eventsPanel.add(createEventCard(event_id, event_title, event_type, event_datetime,
                        event_venue, registered, capacity, user.getUsername(), fee));
            }

            if (!eventsFound) {
                JLabel noEventsLabel = new JLabel("No events created yet. Click '+ Create Event' to get started!");
                noEventsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
                noEventsLabel.setHorizontalAlignment(SwingConstants.CENTER);
                eventsPanel.setLayout(new BorderLayout());
                eventsPanel.add(noEventsLabel, BorderLayout.CENTER);
            } else {
                 eventsPanel.setLayout(new GridLayout(0, 3, 20, 20));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            eventsPanel.add(new JLabel("Error loading events from database."));
        }

        JScrollPane scrollPane = new JScrollPane(eventsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

}