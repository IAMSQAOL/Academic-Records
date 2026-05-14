package Toolbars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;


public class ImageDimensionDialog extends JDialog{
    private JTextField widthField;
    private JTextField heightField;
    private int chosenWidth = -1;
    private int chosenHeight = -1;
    private boolean confirmed = false;

    public ImageDimensionDialog(JFrame parent, BufferedImage image) {
        super(parent, "Set Image Dimensions",true); // Modal dialog
        setLayout(new BorderLayout(10, 10));
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        inputPanel.add(new JLabel("Width:"));
        widthField = new JTextField(String.valueOf(image.getWidth()));
        inputPanel.add(widthField);

        inputPanel.add(new JLabel("Height:"));
        heightField = new JTextField(String.valueOf(image.getHeight()));
        inputPanel.add(heightField);

        add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int w = Integer.parseInt(widthField.getText());
                    int h = Integer.parseInt(heightField.getText());

                    if (w > 0 && h > 0) {
                        chosenWidth = w;
                        chosenHeight = h;
                        confirmed = true;
                        dispose(); // Close the dialog
                    } else {
                        JOptionPane.showMessageDialog(ImageDimensionDialog.this,
                                "Width and Height must be positive integers.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(ImageDimensionDialog.this,
                            "Please enter valid integer numbers for width and height.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        buttonPanel.add(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            confirmed = false;
            dispose(); // Close the dialog
        });
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent); // Center relative to the parent frame
    }

    public int getChosenWidth() {
        return chosenWidth;
    }

    public int getChosenHeight() {
        return chosenHeight;
    }

    public boolean isConfirmed() {
        return confirmed;
    }
    
}
