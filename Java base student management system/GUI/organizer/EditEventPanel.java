package GUI.organizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import Events.Event;
import Events.Discounts.GroupDiscount; 
import Events.Discounts.IndividualDiscount;
import Events.Discounts.Discount; 

import Events.Services.CateringService;
import Events.Services.TransportService;
import Users.User;
import database.DBConnection; 

public class EditEventPanel extends JPanel {
    private List<String> discountOptions = new ArrayList<>();
    private List<String> cateringServices = new ArrayList<>();
    private List<String> transportServices = new ArrayList<>();

    private JPanel discountsDisplayPanel;
    private JPanel cateringDisplayPanel;
    private JPanel transportDisplayPanel;

    private JScrollPane cateringScrollPane;
    private JScrollPane transportScrollPane;
    private JScrollPane discountsScrollPane;

    private JTextField eventNameField;
    private JComboBox<String> eventTypeComboBox;
    private JTextField dateField;
    private JTextField timeField;
    private JTextField venueField;
    private JTextField organizerField; 
    private JTextField capacityField;
    private JTextField feeField;
    private JTextArea descriptionArea;

    private Event eventToEdit; 

    public EditEventPanel(User user, Event event) {
        this.eventToEdit = event; 
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        JLabel title = new JLabel("Edit Event: " + event.getName()); 
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.add(title, BorderLayout.WEST);

        // Main content area - JSplitPane for left (details) and right (services)
        JSplitPane mainContentSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainContentSplitPane.setDividerLocation(0.5);
        mainContentSplitPane.setResizeWeight(0.5);
        mainContentSplitPane.setOneTouchExpandable(true);

        // Left Panel: Event Details
        JPanel eventDetailsPanel = new JPanel(new GridBagLayout());
        eventDetailsPanel.setBackground(Color.WHITE);
        eventDetailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
        GridBagConstraints gbcDetails = new GridBagConstraints();
        gbcDetails.insets = new Insets(8, 8, 8, 8);
        gbcDetails.fill = GridBagConstraints.HORIZONTAL;
        gbcDetails.weightx = 1.0;

        // Initialize fields and pre-fill with event data
        eventNameField = field("Event Name *");
        eventNameField.setText(event.getName());

        eventTypeComboBox = comboBox("Event Type *", new String[]{"Seminar", "Workshop", "Cultural Event", "Sports Event"});
        eventTypeComboBox.setSelectedItem(event.getType()); 

        dateField = field("Date * (dd-mm-yyyy)"); 
        dateField.setText(event.getDate());

        timeField = field("Time * (hh:mm)");
        timeField.setText(event.getTime());

        venueField = field("Venue *");
        venueField.setText(event.getVenue());

        organizerField = field("Organizer *");
        organizerField.setText(user.getUsername()); 
        organizerField.setEditable(false); 

        capacityField = field("Capacity *");
        capacityField.setText(String.valueOf(event.getCapacity())); 

        feeField = field("Base Registration Fee ($)");
        feeField.setText(String.format("%.2f", event.getFee())); 

        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setText(event.getDescription()); // Pre-fill description
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        descriptionScrollPane.setBorder(BorderFactory.createTitledBorder("Description *"));
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));


        int rowDetails = 0;
        gbcDetails.gridx = 0; gbcDetails.gridy = rowDetails; gbcDetails.gridwidth = 2;
        eventDetailsPanel.add(eventNameField, gbcDetails);

        rowDetails++;
        gbcDetails.gridx = 0; gbcDetails.gridy = rowDetails;
        eventDetailsPanel.add(eventTypeComboBox, gbcDetails);

        rowDetails++;
        gbcDetails.gridx = 0; gbcDetails.gridy = rowDetails; gbcDetails.gridwidth = 1;
        eventDetailsPanel.add(dateField, gbcDetails);
        gbcDetails.gridx = 1;
        eventDetailsPanel.add(timeField, gbcDetails);

        rowDetails++;
        gbcDetails.gridx = 0; gbcDetails.gridy = rowDetails;
        eventDetailsPanel.add(venueField, gbcDetails);
        gbcDetails.gridx = 1;
        eventDetailsPanel.add(organizerField, gbcDetails);

        rowDetails++;
        gbcDetails.gridx = 0; gbcDetails.gridy = rowDetails;
        eventDetailsPanel.add(capacityField, gbcDetails);
        gbcDetails.gridx = 1;
        eventDetailsPanel.add(feeField, gbcDetails);

        rowDetails++;
        gbcDetails.gridx = 0; gbcDetails.gridy = rowDetails; gbcDetails.gridwidth = 2;
        gbcDetails.weighty = 0.5;
        gbcDetails.fill = GridBagConstraints.BOTH;
        eventDetailsPanel.add(descriptionScrollPane, gbcDetails);
        gbcDetails.weighty = 0.0;
        gbcDetails.fill = GridBagConstraints.HORIZONTAL;
        gbcDetails.gridwidth = 1;


        // Right Panel: Services and Options
        JPanel servicesOptionsPanel = new JPanel(new GridBagLayout());
        servicesOptionsPanel.setBackground(Color.WHITE);
        servicesOptionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 20));
        GridBagConstraints gbcServices = new GridBagConstraints();
        gbcServices.insets = new Insets(8, 8, 8, 8);
        gbcServices.fill = GridBagConstraints.HORIZONTAL;
        gbcServices.weightx = 1.0;

        // --- Catering Service Section ---
        int rowServices = 0;
        gbcServices.gridx = 0; gbcServices.gridy = rowServices;
        servicesOptionsPanel.add(new JLabel(""), gbcServices);

        rowServices++;
        gbcServices.gridx = 0; gbcServices.gridy = rowServices;
        JButton addCateringServiceBtn = new JButton("+ Add Catering Service");
        addCateringServiceBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        addCateringServiceBtn.addActionListener(e -> {
            openServicePriceInputDialog("Catering Service", cateringServices, "Catering Item Name:", "Price ($):");
            updateDisplayPanel(cateringDisplayPanel, cateringServices);
        });
        servicesOptionsPanel.add(addCateringServiceBtn, gbcServices);

        cateringDisplayPanel = createDisplayPanel("Catering Services:");
        cateringScrollPane = new JScrollPane(cateringDisplayPanel);
        cateringScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        cateringScrollPane.setBorder(BorderFactory.createTitledBorder("Catering Services (Added)"));
        gbcServices.gridx = 0; gbcServices.gridy = ++rowServices;
        gbcServices.weighty = 0.33;
        gbcServices.fill = GridBagConstraints.BOTH;
        servicesOptionsPanel.add(cateringScrollPane, gbcServices);
        gbcServices.weighty = 0.0;
        gbcServices.fill = GridBagConstraints.HORIZONTAL;

        // --- Transport Service Section ---
        rowServices++;
        gbcServices.gridx = 0; gbcServices.gridy = rowServices;
        JButton addTransportServiceBtn = new JButton("+ Add Transport Service");
        addTransportServiceBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        addTransportServiceBtn.addActionListener(e -> {
            openServicePriceInputDialog("Transport Service", transportServices, "Transport Service Name:", "Price ($):");
            updateDisplayPanel(transportDisplayPanel, transportServices);
        });
        servicesOptionsPanel.add(addTransportServiceBtn, gbcServices);

        transportDisplayPanel = createDisplayPanel("Transport Services:");
        transportScrollPane = new JScrollPane(transportDisplayPanel);
        transportScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        transportScrollPane.setBorder(BorderFactory.createTitledBorder("Transport Services (Added)"));
        gbcServices.gridx = 0; gbcServices.gridy = ++rowServices;
        gbcServices.weighty = 0.33;
        gbcServices.fill = GridBagConstraints.BOTH;
        servicesOptionsPanel.add(transportScrollPane, gbcServices);
        gbcServices.weighty = 0.0;
        gbcServices.fill = GridBagConstraints.HORIZONTAL;

        // --- Discount Section ---
        rowServices++;
        gbcServices.gridx = 0; gbcServices.gridy = rowServices;
        JButton addDiscountBtn = new JButton("+ Add Discount");
        addDiscountBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        addDiscountBtn.addActionListener(e -> {
            openDiscountSelectionInputDialog("Add Discount", discountOptions);
            updateDisplayPanel(discountsDisplayPanel, discountOptions);
        });
        servicesOptionsPanel.add(addDiscountBtn, gbcServices);

        discountsDisplayPanel = createDisplayPanel("Discounts:");
        discountsScrollPane = new JScrollPane(discountsDisplayPanel);
        discountsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        discountsScrollPane.setBorder(BorderFactory.createTitledBorder("Discounts (Added)"));
        gbcServices.gridx = 0; gbcServices.gridy = ++rowServices;
        gbcServices.weighty = 0.34;
        gbcServices.fill = GridBagConstraints.BOTH;
        servicesOptionsPanel.add(discountsScrollPane, gbcServices);


        mainContentSplitPane.setLeftComponent(eventDetailsPanel);
        mainContentSplitPane.setRightComponent(servicesOptionsPanel);

        // Footer
        JPanel footer = new JPanel();
        footer.setBackground(Color.WHITE);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));

        JButton updateButton = new JButton("Update Event"); // Changed from Create Event
        updateButton.setBackground(new Color(103, 80, 164));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        updateButton.setPreferredSize(new Dimension(130, 35));
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!validateInputs()) {
                    return; 
                }


                eventToEdit.setName(eventNameField.getText());
                eventToEdit.setType((String) eventTypeComboBox.getSelectedItem());
                eventToEdit.setDate(dateField.getText());
                eventToEdit.setTime(timeField.getText());
                eventToEdit.setVenue(venueField.getText());

                eventToEdit.setCapacity(Integer.parseInt(capacityField.getText()));
                eventToEdit.setFee(Double.parseDouble(feeField.getText()));
                eventToEdit.setDescription(descriptionArea.getText());

                eventToEdit.updateEvent();


                DBConnection.execute("DELETE FROM CateringServices WHERE Event_id = ?", eventToEdit.getId());
                DBConnection.execute("DELETE FROM TransportServices WHERE Event_id = ?", eventToEdit.getId());
                DBConnection.execute("DELETE FROM Discounts WHERE Event_id = ?", eventToEdit.getId());


                // Insert current catering services
                for (String item : cateringServices) {
                    String[] parts = item.split(" - \\$");
                    if (parts.length == 2) {
                        String name = parts[0].trim();
                        double price = Double.parseDouble(parts[1].trim());
                        CateringService cs = new CateringService(eventToEdit.getId(), name, price);
                        cs.insertService(); 
                    }
                }
                // Insert current transport services
                for (String item : transportServices) {
                    String[] parts = item.split(" - \\$");
                    if (parts.length == 2) {
                        String name = parts[0].trim();
                        double price = Double.parseDouble(parts[1].trim());
                        TransportService ts = new TransportService(eventToEdit.getId(), name, price);
                        ts.insertService(); 
                    }
                }
                // Insert current discounts
                for (String item : discountOptions) {
                    String[] parts = item.split(" - ");
                    if (parts.length == 2) {
                        String type = parts[0].trim();
                        int percent = Integer.parseInt(parts[1].replace("%", "").trim());

                        Discount discount = null; // Base class
                        switch (type) {
                            case "Group":
                                discount = new GroupDiscount(eventToEdit.getId(), percent);
                                break;
                            case "Individual":
                                discount = new IndividualDiscount(eventToEdit.getId(), percent);
                                break;
                            // Add cases for "Student", "Staff" if you have specific classes for them
                            case "Student": // Assuming these are also handled by IndividualDiscount or a new class
                            case "Staff":
                                discount = new IndividualDiscount(eventToEdit.getId(), percent); // Or a specific Staff/Student class
                                break;
                        }

                        if (discount != null) {
                            discount.insertDiscount(); // Assuming insertDiscount handles the SQL INSERT
                        }
                    }
                }

                JOptionPane.showMessageDialog(EditEventPanel.this, eventToEdit.getName() + " Updated Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            }
        });

        footer.add(updateButton); // Only one button for simplicity, you can add cancel if needed

        add(header, BorderLayout.NORTH);
        add(mainContentSplitPane, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);

        // Load and display existing services/discounts
        loadExistingServicesAndDiscounts();

        // Initial update of display panels
        updateDisplayPanel(cateringDisplayPanel, cateringServices);
        updateDisplayPanel(transportDisplayPanel, transportServices);
        updateDisplayPanel(discountsDisplayPanel, discountOptions);
    }


    private void loadExistingServicesAndDiscounts() {

        try {
           
            // Load Catering Services
            ResultSet rs = DBConnection.getRS("SELECT Catering_type, Catering_fee FROM CateringServices WHERE Event_id = ?");
            while (rs.next()) {
                cateringServices.add(rs.getString("Catering_type") + " - $" + rs.getDouble("Catering_fee"));
            }
            
            // Load Transport Services
            ResultSet ts = DBConnection.getRS("SELECT Transport_type, Transport_fee FROM TransportServices WHERE Event_id = ?");
            while (ts.next()) {
                transportServices.add(ts.getString("Transport_type") + " - $" + ts.getDouble("Transport_fee"));
            }



            // Load Discounts
            ResultSet ds = DBConnection.getRS("SELECT discount_type, discount_amount FROM Discounts WHERE Event_id = ?");
            while (ds.next()) {
                discountOptions.add(ds.getString("discount_type") + " - " + ds.getInt("discount_amount") + "%");
            }


        } catch (SQLException ex) {
            Logger.getLogger(EditEventPanel.class.getName()).log(Level.SEVERE, "Error loading existing event services/discounts", ex);
            JOptionPane.showMessageDialog(this, "Error loading existing services/discounts: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } 
    }


    private boolean validateInputs() {
        if (eventNameField.getText().trim().isEmpty() ||
            dateField.getText().trim().isEmpty() ||
            timeField.getText().trim().isEmpty() ||
            venueField.getText().trim().isEmpty() ||
            capacityField.getText().trim().isEmpty() ||
            feeField.getText().trim().isEmpty() ||
            descriptionArea.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields marked with * are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            Integer.parseInt(capacityField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Capacity must be a valid number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            Double.parseDouble(feeField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Base Registration Fee must be a valid number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }


    private boolean isNameDuplicate(List<String> list, String newName) {
        for (String entry : list) {
            String existingName = entry.split(" - ")[0].trim();
            if (existingName.equalsIgnoreCase(newName.trim())) {
                return true;
            }
        }
        return false;
    }

    private void openServicePriceInputDialog(String title, List<String> targetList, String nameLabel, String priceLabel) {
        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 5));
        panel.add(new JLabel(nameLabel));
        panel.add(nameField);
        panel.add(new JLabel(priceLabel));
        panel.add(priceField);

        int result = JOptionPane.showConfirmDialog(this, panel, title,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String price = priceField.getText().trim();
            if (!name.isEmpty() && !price.isEmpty()) {
                try {
                    Double.parseDouble(price);
                    String newEntry = name + " - $" + price;
                    if (isNameDuplicate(targetList, name)) {
                    JOptionPane.showMessageDialog(this,
                            "An item with the name '" + name + "' already exists. Please use a different name or edit the existing item.",
                            "Duplicate Name", JOptionPane.WARNING_MESSAGE);
                    } else {
                        targetList.add(newEntry);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Price must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Both fields cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void openDiscountSelectionInputDialog(String title, List<String> targetList) {
        String[] discountTypes = {"Individual", "Group"}; 
        JComboBox<String> typeChooser = new JComboBox<>(discountTypes);
        JTextField percentageField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 5));
        panel.add(new JLabel("Discount Type:"));
        panel.add(typeChooser);
        panel.add(new JLabel("Percentage (%):"));
        panel.add(percentageField);

        int result = JOptionPane.showConfirmDialog(this, panel, title,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String selectedType = (String) typeChooser.getSelectedItem();
            String percentageText = percentageField.getText().trim();
            if (selectedType != null && !selectedType.isEmpty() && !percentageText.isEmpty()) {
                try {
                    double percentValue = Double.parseDouble(percentageText);
                    if (percentValue >= 0 && percentValue <= 100) {
                        boolean typeAlreadyAdded = false;
                        for (String existingEntry : targetList) {
                            String existingType = existingEntry.split(" - ")[0].trim();
                            if (existingType.equalsIgnoreCase(selectedType)) {
                                typeAlreadyAdded = true;
                                break;
                            }
                        }

                        if (typeAlreadyAdded) {
                            JOptionPane.showMessageDialog(this,
                                    "A discount for '" + selectedType + "' has already been added. " +
                                    "Please remove the existing one if you wish to change its percentage.",
                                    "Duplicate Discount Type", JOptionPane.WARNING_MESSAGE);
                        } else {
                            String entry = selectedType + " - " + percentageText + "%";
                            targetList.add(entry);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Percentage must be between 0 and 100.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Percentage must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Discount type and percentage cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateDisplayPanel(JPanel panel, List<String> entries) {
        panel.removeAll();

        if (entries.isEmpty()) {
            JLabel placeholder = new JLabel("No items added yet.");
            placeholder.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            placeholder.setForeground(Color.GRAY);
            panel.add(placeholder);
        } else {
            panel.setLayout(new GridBagLayout());
            GridBagConstraints gbcItem = new GridBagConstraints();
            gbcItem.insets = new Insets(2, 5, 2, 5);
            gbcItem.fill = GridBagConstraints.HORIZONTAL;
            gbcItem.weightx = 1.0;

            for (int i = 0; i < entries.size(); i++) {
                String item = entries.get(i);

                JLabel label = new JLabel(item);
                label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                gbcItem.gridx = 0;
                gbcItem.gridy = i;
                panel.add(label, gbcItem);

                JButton deleteBtn = new JButton("X");
                deleteBtn.setFont(new Font("Segoe UI", Font.BOLD, 10));
                deleteBtn.setForeground(Color.RED);
                deleteBtn.setBackground(Color.WHITE);
                deleteBtn.setBorder(BorderFactory.createLineBorder(Color.RED));
                deleteBtn.setFocusPainted(false);
                deleteBtn.setPreferredSize(new Dimension(25, 20));
                deleteBtn.setMinimumSize(new Dimension(25, 20));
                deleteBtn.setMaximumSize(new Dimension(25, 20));

                final String itemToRemove = item;
                deleteBtn.addActionListener(e -> {
                    entries.remove(itemToRemove);
                    updateDisplayPanel(panel, entries);
                });

                gbcItem.gridx = 1;
                gbcItem.gridy = i;
                gbcItem.weightx = 0.0;
                gbcItem.fill = GridBagConstraints.NONE;
                panel.add(deleteBtn, gbcItem);
                gbcItem.weightx = 1.0;
                gbcItem.fill = GridBagConstraints.HORIZONTAL;
            }
        }
        panel.revalidate();
        panel.repaint();
    }

    private JTextField field(String title) {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createTitledBorder(title));
        return tf;
    }

    private JComboBox<String> comboBox(String title, String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cb.setBorder(BorderFactory.createTitledBorder(title));
        return cb;
    }

    private JPanel createDisplayPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        return panel;
    }
}