package GUI.organizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import Events.*;
import Events.Event;
import Events.Discounts.Discount;
import Events.Discounts.GroupDiscount;
import Events.Discounts.IndividualDiscount;

import Events.Services.AdditionalService;
import Events.Services.CateringService;
import Events.Services.TransportService;
import Users.User;

public class CreateEventPanel extends JPanel {
    private List<String> discountOptions = new ArrayList<>();
    private List<String> cateringServices = new ArrayList<>();
    private List<String> transportServices = new ArrayList<>();

    // JPanels to display the added items, now allowing for individual deletion
    private JPanel discountsDisplayPanel;
    private JPanel cateringDisplayPanel;
    private JPanel transportDisplayPanel;

    // Scroll panes for display panels if content overflows
    private JScrollPane cateringScrollPane;
    private JScrollPane transportScrollPane;
    private JScrollPane discountsScrollPane;

    public CreateEventPanel(User user) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        JLabel title = new JLabel("Create New Event");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.add(title, BorderLayout.WEST);

        // Main content area - JSplitPane for left (details) and right (services)
        JSplitPane mainContentSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainContentSplitPane.setDividerLocation(0.5); //
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

        JTextField eventName = field("Event Name *");
        JComboBox<String> eventType = comboBox("Event Type *", new String[]{"Seminar", "Workshop", "Cultural Event", "Sports Event"});
        JTextField date = field("Date * (dd/mm/yyyy)");
        JTextField time = field("Time * (hh:mm)");
        JTextField venue = field("Venue *");
        JTextField organizer = field("Organizer *"); 
        organizer.setText(user.getUsername());
        organizer.setEditable(false); 
        JTextField capacity = field("Capacity *");
        JTextField fee = field("Base Registration Fee ($)");
        JTextArea descriptionArea = new JTextArea(4, 20); 
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        descriptionScrollPane.setBorder(BorderFactory.createTitledBorder("Description *"));
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));


        int rowDetails = 0;
        gbcDetails.gridx = 0; gbcDetails.gridy = rowDetails; gbcDetails.gridwidth = 2; 
        eventDetailsPanel.add(eventName, gbcDetails);

        rowDetails++;
        gbcDetails.gridx = 0; gbcDetails.gridy = rowDetails;
        eventDetailsPanel.add(eventType, gbcDetails);

        rowDetails++;
        gbcDetails.gridx = 0; gbcDetails.gridy = rowDetails; gbcDetails.gridwidth = 1; 
        eventDetailsPanel.add(date, gbcDetails);
        gbcDetails.gridx = 1;
        eventDetailsPanel.add(time, gbcDetails);

        rowDetails++;
        gbcDetails.gridx = 0; gbcDetails.gridy = rowDetails;
        eventDetailsPanel.add(venue, gbcDetails);
        gbcDetails.gridx = 1;
        eventDetailsPanel.add(organizer, gbcDetails);

        rowDetails++;
        gbcDetails.gridx = 0; gbcDetails.gridy = rowDetails;
        eventDetailsPanel.add(capacity, gbcDetails);
        gbcDetails.gridx = 1;
        eventDetailsPanel.add(fee, gbcDetails);

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

        JButton create = new JButton("Create Event");
        create.setBackground(new Color(103, 80, 164));
        create.setForeground(Color.WHITE);
        create.setFont(new Font("Segoe UI", Font.BOLD, 13));
        create.setPreferredSize(new Dimension(130, 35));
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Add event's services (can reconstruct this part in the future)
                int eventId = Event.getNewestId();
                List<AdditionalService> cateringServiceObjects = new ArrayList<>();
                for (String item : cateringServices) {
                    String[] parts = item.split(" - \\$");
                    if (parts.length == 2) {
                        String name = parts[0].trim();
                        double price = Double.parseDouble(parts[1].trim());
                        cateringServiceObjects.add(new CateringService(eventId, name, price));
                        cateringServiceObjects.get(cateringServiceObjects.size() - 1).insertService();
                    }
                }
                List<AdditionalService> transportServiceObjects = new ArrayList<>();
                for (String item : transportServices) {
                    String[] parts = item.split(" - \\$");
                    if (parts.length == 2) {
                        String name = parts[0].trim();
                        double price = Double.parseDouble(parts[1].trim());
                        transportServiceObjects.add(new TransportService(eventId, name, price));
                        transportServiceObjects.get(transportServiceObjects.size() - 1).insertService();
                    }
                }

                List<Discount> DiscountObjects = new ArrayList<>();
                for (String item : discountOptions) {
                    String[] parts = item.split(" - ");
                    if (parts.length == 2) {
                        String type = parts[0].trim();
                        int percent = Integer.parseInt(parts[1].replace("%", "").trim());

                        Discount discount = null;

                        switch (type) {
                            case "Group":
                                discount = new GroupDiscount(eventId, percent);
                                break;
                            case "Individual":
                                discount = new IndividualDiscount(eventId, percent);
                                break;
                        }

                        if (discount != null) {
                            discount.insertDiscount(); 
                            //DiscountObjects.add(discount); 
                        }
                    }
                }
                // Create Event logic (Can reconstruct this part in the future)
                Event newEvent = null;
                switch (eventType.getSelectedItem().toString()) {
                    case "Seminar":
                        newEvent = new Seminar(eventName.getText(), date.getText(), time.getText(),
                        venue.getText(), user.getUserId(), Integer.parseInt(capacity.getText()),
                        Double.parseDouble(fee.getText()), descriptionArea.getText());
                        newEvent.createEvent();
                        break;
                    case "Workshop":
                        newEvent = new Workshop(eventName.getText(), date.getText(), time.getText(),
                        venue.getText(), user.getUserId(), Integer.parseInt(capacity.getText()),
                        Double.parseDouble(fee.getText()), descriptionArea.getText());
                        newEvent.createEvent();
                        break;
                    case "Cultural Event":
                        newEvent = new Cultural(eventName.getText(), date.getText(), time.getText(),
                        venue.getText(), user.getUserId(), Integer.parseInt(capacity.getText()),
                        Double.parseDouble(fee.getText()), descriptionArea.getText());
                        newEvent.createEvent();
                        break;
                    case "Sports Event":
                        newEvent = new Sport(eventName.getText(), date.getText(), time.getText(),
                        venue.getText(), user.getUserId(), Integer.parseInt(capacity.getText()),
                        Double.parseDouble(fee.getText()), descriptionArea.getText());
                        newEvent.createEvent();
                        break;
                    default:
                        System.out.println("Unknown Event Type");
                }
                JOptionPane.showMessageDialog(CreateEventPanel.this, eventName.getText() + " " + eventType.getSelectedItem() + " Created", "Success", JOptionPane.INFORMATION_MESSAGE);

            }
        });

        footer.add(Box.createHorizontalStrut(10));
        footer.add(create);

        add(header, BorderLayout.NORTH);
        add(mainContentSplitPane, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);

        // Initial update of display panels (they will be empty)
        updateDisplayPanel(cateringDisplayPanel, cateringServices);
        updateDisplayPanel(transportDisplayPanel, transportServices);
        updateDisplayPanel(discountsDisplayPanel, discountOptions);

        setVisible(true);
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
            String percentage = percentageField.getText().trim();
            if (selectedType != null && !selectedType.isEmpty() && !percentage.isEmpty()) {
                try {
                    double percentValue = Double.parseDouble(percentage);
                    if (percentValue >= 0 && percentValue <= 100) {
                        boolean typeAlreadyAdded = false;
                    for (String existingEntry : targetList) {
                        String existingType = existingEntry.split(" - ")[0].trim();
                        if (existingType.equalsIgnoreCase(selectedType)) { 
                            break;
                        }
                    }

                    if (typeAlreadyAdded) {
                        JOptionPane.showMessageDialog(this,
                                "A discount for '" + selectedType + "' has already been added. " +
                                "Please remove the existing one if you wish to change its percentage.",
                                "Duplicate Discount Type", JOptionPane.WARNING_MESSAGE);
                    } else {
                        // If type is not a duplicate, add the new entry
                        String entry = selectedType + " - " + percentage + "%";
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

                // Label for the item
                JLabel label = new JLabel(item);
                label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                gbcItem.gridx = 0;
                gbcItem.gridy = i;
                panel.add(label, gbcItem);

                // "X" button for deletion
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
                gbcItem.weightx = 1.0; // Reset weightx
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