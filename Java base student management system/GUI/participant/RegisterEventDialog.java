package GUI.participant;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import Registrations.Registration;
import Users.User;
import database.DBConnection;
import java.sql.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterEventDialog extends JDialog {

    private double currentBaseFee;
    private JLabel subtotalValueLabel;
    private JLabel totalAmountLabel;
    private String discountType;
    private final java.util.Map<JCheckBox, Double> cateringMap = new java.util.HashMap<>();
    private final java.util.Map<JCheckBox, Double> transportMap = new java.util.HashMap<>();
    private final java.util.Map<JCheckBox, Integer> discountMap = new java.util.HashMap<>();

    private JCheckBox groupDiscountBox;
    private JCheckBox individualDiscountBox;
    private JTextField register_amount;
    private User currentUser;

    // Variables to store the currently selected catering and transport
    private String selectedCateringName = null;
    private String selectedTransportName = null;

    public RegisterEventDialog(JFrame parent, int eventId, String eventTitle, String dateTime, String venue, double baseFee, User user) throws SQLException {
        super(parent, "Register for Event", true);
        this.currentUser = user;
        this.currentBaseFee = baseFee;
        setSize(950, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Header
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(15, 25, 5, 25));

        JLabel title = new JLabel("Register for " + eventTitle);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        JLabel meta = new JLabel(dateTime + " • " + venue);
        meta.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        meta.setForeground(new Color(100, 100, 100));

        header.add(title);
        header.add(Box.createVerticalStrut(5));
        header.add(meta);

        // Main Split Pane for Left Form and Right Fee Panel
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setDividerLocation(0.65);
        mainSplitPane.setResizeWeight(0.65);
        mainSplitPane.setOneTouchExpandable(true);
        mainSplitPane.setBackground(Color.WHITE);

        // Left Form Panel
        JPanel form = new JPanel();
        form.setBackground(Color.WHITE);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(5, 25, 5, 15));

        form.add(sectionTitle("Personal Information"));
        JPanel personalInfoGrid = new JPanel(new GridLayout(2, 2, 15, 10));
        personalInfoGrid.setBackground(Color.WHITE);
        personalInfoGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        personalInfoGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField fullNameField = styledField("Full Name *");
        JTextField emailField = styledField("Email Address *");
        JTextField phoneField = styledField("Phone Number *");
        register_amount = styledField("Numbers of applicants *");
        register_amount.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                handleApplicantInput();
                updateFeeBreakdown();
            }

            public void removeUpdate(DocumentEvent e) {
                handleApplicantInput();
                updateFeeBreakdown();
            }

            public void changedUpdate(DocumentEvent e) {
                handleApplicantInput();
                updateFeeBreakdown();
            }
        });

        personalInfoGrid.add(fullNameField);
        personalInfoGrid.add(register_amount);
        personalInfoGrid.add(emailField);
        personalInfoGrid.add(phoneField);
        form.add(personalInfoGrid);

        form.add(Box.createVerticalStrut(15));
        form.add(sectionTitle("Additional Services"));
        JPanel addServicePanel = new JPanel();
        addServicePanel.setLayout(new BoxLayout(addServicePanel, BoxLayout.Y_AXIS));
        addServicePanel.setBackground(Color.WHITE);
        addServicePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        ResultSet cater = DBConnection.getRS("SELECT * FROM CateringServices WHERE Event_id = ?", eventId);
        try {
            while (cater.next()) {
                String cateringNameFromDB = cater.getString("Catering_type");
                double cateringPrice = cater.getDouble("Catering_fee");
                JCheckBox carterCheckBox = checkBoxItem(cateringNameFromDB, cateringPrice);
                carterCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                cateringMap.put(carterCheckBox, cateringPrice);
                carterCheckBox.setBackground(Color.WHITE);
                carterCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
                carterCheckBox.addActionListener(e -> {
                    // Deselect others and update selectedCateringName
                    for (JCheckBox other : cateringMap.keySet()) {
                        if (other != carterCheckBox) {
                            other.setSelected(false);
                        }
                    }
                    if (carterCheckBox.isSelected()) {
                        selectedCateringName = cateringNameFromDB;
                    } else {
                        selectedCateringName = null;
                    }
                    updateFeeBreakdown();
                });
                addServicePanel.add(carterCheckBox);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            addServicePanel.add(new JLabel("Error loading catering services."));
        }

        ResultSet transport = DBConnection.getRS("SELECT * FROM TransportServices WHERE Event_id = ?", eventId);
        try {
            while (transport.next()) {
                String transportNameFromDB = transport.getString("Transport_type");
                double transportPrice = transport.getDouble("Transport_fee");
                JCheckBox transportCheckBox = checkBoxItem(transportNameFromDB, transportPrice);
                transportCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                transportCheckBox.setBackground(Color.WHITE);
                transportCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
                transportMap.put(transportCheckBox, transportPrice);
                transportCheckBox.addActionListener(e -> {
                    // Deselect others and update selectedTransportName
                    for (JCheckBox other : transportMap.keySet()) {
                        if (other != transportCheckBox) {
                            other.setSelected(false);
                        }
                    }
                    if (transportCheckBox.isSelected()) {
                        selectedTransportName = transportNameFromDB;
                    } else {
                        selectedTransportName = null;
                    }
                    updateFeeBreakdown();
                });
                addServicePanel.add(transportCheckBox);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            addServicePanel.add(new JLabel("Error loading transport services."));
        }

        form.add(addServicePanel);

        form.add(Box.createVerticalStrut(15));
        form.add(sectionTitle("Registration Options"));
        JPanel discountPanel = new JPanel();
        discountPanel.setLayout(new BoxLayout(discountPanel, BoxLayout.Y_AXIS));
        discountPanel.setBackground(Color.WHITE);
        discountPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        ResultSet discounts = DBConnection.getRS("SELECT * FROM Discounts WHERE Event_id = ?", eventId);
        try {
            while (discounts.next()) {
                discountType = discounts.getString("discount_type");
                int discountValue = discounts.getInt("discount_amount");
                JCheckBox discountCheckBox = checkBoxItem(discountType, discountValue);
                discountCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                discountCheckBox.setBackground(Color.WHITE);
                discountCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
                discountMap.put(discountCheckBox, discountValue);
                discountCheckBox.addActionListener(e -> {
                    if (discountCheckBox.isSelected()) {
                        for (JCheckBox other : discountMap.keySet()) {
                            if (other != discountCheckBox) {
                                other.setSelected(false);
                                other.setEnabled(false);
                            }
                        }
                    } else {
                        handleApplicantInput();
                    }
                    updateFeeBreakdown();
                });
                discountPanel.add(discountCheckBox);
                if (discountType.equalsIgnoreCase("Group")) {
                    groupDiscountBox = discountCheckBox;
                } else if (discountType.equalsIgnoreCase("Individual")) {
                    individualDiscountBox = discountCheckBox;
                }
                discountPanel.add(discountCheckBox);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            discountPanel.add(new JLabel("Error loading discounts."));
        }

        form.add(discountPanel);

        //Right Fee Panel
        JPanel feePanel = new JPanel();
        feePanel.setBackground(Color.WHITE);
        feePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        feePanel.setLayout(new BoxLayout(feePanel, BoxLayout.Y_AXIS));

        // Fee Breakdown Title
        JLabel feeTitle = new JLabel("Fee Breakdown");
        feeTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        feeTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        feePanel.add(feeTitle);
        feePanel.add(Box.createVerticalStrut(10));

        // Base Registration Fee
        feePanel.add(feeLabel("Base Registration Fee:", "$" + String.format("%.2f", baseFee)));
        feePanel.add(Box.createVerticalStrut(5));

        // Subtotal
        JPanel subtotalPanel = feeLabel("Subtotal:", "$" + String.format("%.2f", baseFee));
        subtotalValueLabel = (JLabel) ((BorderLayout) subtotalPanel.getLayout()).getLayoutComponent(BorderLayout.EAST); // Get reference to right label
        feePanel.add(subtotalPanel);
        feePanel.add(Box.createVerticalStrut(10)); // Spacer before total

        // Total Amount
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBackground(Color.WHITE);
        JLabel totalTextLabel = new JLabel("Total Amount:");
        totalTextLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalAmountLabel = new JLabel("$" + String.format("%.2f", baseFee));
        totalAmountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalAmountLabel.setForeground(new Color(40, 85, 200));
        totalPanel.add(totalTextLabel, BorderLayout.WEST);
        totalPanel.add(totalAmountLabel, BorderLayout.EAST);
        totalPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        feePanel.add(totalPanel);
        feePanel.add(Box.createVerticalStrut(10)); // Spacer

        // Payment Note
        JTextArea note = new JTextArea("Once Register button pressed payment will proceed");
        note.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        note.setLineWrap(true);
        note.setWrapStyleWord(true);
        note.setEditable(false);
        note.setOpaque(false);
        note.setBackground(Color.WHITE);
        note.setMaximumSize(new Dimension(250, 60));
        note.setAlignmentX(Component.LEFT_ALIGNMENT);
        feePanel.add(note);
        feePanel.add(Box.createVerticalGlue());

        mainSplitPane.setLeftComponent(new JScrollPane(form));
        mainSplitPane.setRightComponent(feePanel);

        // Bottom Button
        JButton submit = new JButton("Complete Registration");
        submit.setFont(new Font("Segoe UI", Font.BOLD, 13));
        submit.setBackground(new Color(103, 80, 164));
        submit.setForeground(Color.WHITE);
        submit.setPreferredSize(new Dimension(180, 40));

        JPanel bottom = new JPanel();
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(BorderFactory.createEmptyBorder(5, 20, 15, 20));
        bottom.add(submit);
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String selectedDiscountType = null;
                for (Map.Entry<JCheckBox, Integer> entry : discountMap.entrySet()) {
                    if (entry.getKey().isSelected()) {
                        String fullText = entry.getKey().getText();
                        selectedDiscountType = fullText.split(" -")[0].trim();
                        break;
                    }
                }

                try {
                    int rid = Registration.generateUniqueRegistrationId();
                    Registration registration = new Registration(rid, eventId, user.getUserId(), fullNameField.getText().trim(), emailField.getText().trim(), phoneField.getText().trim(), Integer.parseInt(register_amount.getText().trim()));
                    // Pass the selected catering and transport names
                    registration.setCaterSelected(selectedCateringName);
                    registration.setTransportSelected(selectedTransportName);
                    registration.setDiscountType(selectedDiscountType);
                    registration.setBasefee(currentBaseFee);
                    registration.registerForEvent();
                    JOptionPane.showMessageDialog(RegisterEventDialog.this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    new BillSummaryDialog(parent, rid);
                } catch (Exception ex) {
                    Logger.getLogger(RegisterEventDialog.class.getName()).log(Level.SEVERE, "Unexpected error during registration for user: " + currentUser.getUserId(), ex);
                    JOptionPane.showMessageDialog(RegisterEventDialog.this, "An unexpected error occurred during registration: " + ex.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        updateFeeBreakdown();

        add(header, BorderLayout.NORTH);
        add(mainSplitPane, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
        setVisible(true);
    }

    private JLabel sectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        return label;
    }

    private JTextField styledField(String label) {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createTitledBorder(label));
        return tf;
    }

    private JCheckBox checkBoxItem(String label, int price) {
        JCheckBox cb = new JCheckBox(label + "   -" + price + "%");
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cb.setBackground(Color.WHITE);
        cb.setAlignmentX(Component.LEFT_ALIGNMENT);
        return cb;
    }

    private JCheckBox checkBoxItem(String label, double price) {
        JCheckBox cb = new JCheckBox(label + "   +$" + String.format("%.2f", price)); // Format price for display
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cb.setBackground(Color.WHITE);
        cb.setAlignmentX(Component.LEFT_ALIGNMENT);
        return cb;
    }

    private JPanel feeLabel(String left, String right) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel l = new JLabel(left);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JLabel r = new JLabel(right);
        r.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        p.add(l, BorderLayout.WEST);
        p.add(r, BorderLayout.EAST);
        return p;
    }

    private void updateFeeBreakdown() {
        double subtotal = currentBaseFee;
        try {
            int applicants = Integer.parseInt(register_amount.getText().trim());
            subtotal *= applicants;

            for (JCheckBox box : cateringMap.keySet()) {
                if (box.isSelected()) {
                    subtotal += applicants * cateringMap.get(box);
                }
            }

            for (JCheckBox box : transportMap.keySet()) {
                if (box.isSelected()) {
                    subtotal += applicants * transportMap.get(box);
                }
            }

            for (JCheckBox box : discountMap.keySet()) {
                if (box.isSelected()) {
                    int discount = discountMap.get(box);
                    subtotal -= (subtotal * discount / 100.0);
                }
            }
        } catch (NumberFormatException e) {
            // Only update the totalAmountLabel if the input is invalid
            // The subtotalValueLabel should still reflect the base fee or previous valid subtotal
            totalAmountLabel.setText("Enter a valid number");
            return; // Exit to prevent further calculation with invalid number
        }

        subtotalValueLabel.setText("$" + String.format("%.2f", subtotal));
        totalAmountLabel.setText("$" + String.format("%.2f", subtotal));
    }

    private void handleApplicantInput() {
        try {
            int num = Integer.parseInt(register_amount.getText().trim());

            if (groupDiscountBox != null) {
                groupDiscountBox.setEnabled(num > 5);
                if (num <= 5) groupDiscountBox.setSelected(false);
            }

            if (individualDiscountBox != null) {
                individualDiscountBox.setEnabled(num <= 1);
                if (num > 1) individualDiscountBox.setSelected(false);
            }

            // Re-enable other discounts if current selection is deselected
            if (groupDiscountBox != null && !groupDiscountBox.isSelected()) {
                if (individualDiscountBox != null) individualDiscountBox.setEnabled(num <= 1);
            }
            if (individualDiscountBox != null && !individualDiscountBox.isSelected()) {
                if (groupDiscountBox != null) groupDiscountBox.setEnabled(num > 5);
            }

            updateFeeBreakdown();
        } catch (NumberFormatException e) {
            // Invalid input: disable both
            if (groupDiscountBox != null) groupDiscountBox.setEnabled(false);
            if (individualDiscountBox != null) individualDiscountBox.setEnabled(false);
        }
    }
}