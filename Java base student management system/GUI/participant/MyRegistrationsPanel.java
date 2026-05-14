package GUI.participant;
import Registrations.Registration;
import Users.User;
import database.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import Events.Event;

public class MyRegistrationsPanel extends JPanel {
    private JFrame parentFrame;
    
    public MyRegistrationsPanel(JFrame parentFrame,User user) {
         this.parentFrame = parentFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        JLabel title = new JLabel("My Registered Events");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        topBar.add(title, BorderLayout.WEST);

        JPanel eventList = new JPanel();
        eventList.setLayout(new GridLayout(0, 2, 20, 20));
        eventList.setBackground(new Color(245, 247, 250));
        eventList.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));

          ResultSet rs = Registration.getuserRegisterEvent(user.getUserId());
            try {
                while(rs.next()){
                        int eid = rs.getInt("Event_id");
                        String ecn = Event.getcreatorName(eid);
                        String e_title = rs.getString("Event_name");
                        String e_t = rs.getString("Event_type");
                        String e_d = rs.getString("Event_date");
                        String e_ti = rs.getString("Event_time");
                        String e_v = rs.getString("Venue");
                        String e_de = rs.getString("Description");
                        String dt = e_d + " at " + e_ti;
                        int rid = Registration.getRegisteredID(eid, user.getUserId());
                        eventList.add(eventCard(e_title,e_t,dt,e_v,ecn,e_de,rid));
                        
                }
            } catch (SQLException ex) {
                Logger.getLogger(MyRegistrationsPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        JScrollPane scrollPane = new JScrollPane(eventList);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(topBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel eventCard(String title, String type, String datetime, String venue,String event_creator,String description,int registration_id) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 228, 232), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        
        JPanel topContentPanel = new JPanel();
        topContentPanel.setLayout(new BoxLayout(topContentPanel, BoxLayout.Y_AXIS));
        topContentPanel.setOpaque(false); // Make it transparent
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

        JLabel desc = new JLabel(datetime);
        JLabel loc = new JLabel(venue);
        JLabel ec = new JLabel(event_creator);
        JLabel dec = new JLabel(description);

        Font infoFont = new Font("Segoe UI", Font.PLAIN, 13);
        for (JLabel label : new JLabel[]{desc, loc, ec, dec}) {
            label.setFont(infoFont);
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        topContentPanel.add(typeLabel);
        topContentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        topContentPanel.add(titleLabel);
        topContentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        topContentPanel.add(desc);
        topContentPanel.add(loc);
        topContentPanel.add(ec);
        topContentPanel.add(dec);

        card.add(topContentPanel, BorderLayout.CENTER);

        JButton billBtn = new JButton("View Bill Summary");
        billBtn.setBackground(new Color(103,80,164));
        billBtn.setForeground(Color.WHITE);
        billBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        billBtn.setFocusPainted(true);
        billBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)); // FlowLayout with no gaps 
        buttonPanel.setOpaque(false); 
        
        buttonPanel.add(billBtn);
        card.add(buttonPanel, BorderLayout.SOUTH);
        
        billBtn.addActionListener(e -> new BillSummaryDialog(parentFrame, registration_id));
        return card;
    }

}