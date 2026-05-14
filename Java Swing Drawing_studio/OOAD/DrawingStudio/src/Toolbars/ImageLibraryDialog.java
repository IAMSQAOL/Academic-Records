package Toolbars;

import DrawingTools.CompositionItem;
import System.LeftPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List; 


public class ImageLibraryDialog extends JDialog{
    private LeftPanel leftPanel;
    private List<String> imagePaths;
    private static final int THUMBNAIL_SIZE = 100;
    
    public ImageLibraryDialog(JFrame parent, LeftPanel leftPanel, List<String> imagePaths) {
        super(parent, "Image Library", true); // Modal dialog
        this.leftPanel = leftPanel;
        this.imagePaths = imagePaths; // Assign the passed list
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel imagePanel = new JPanel(new GridLayout(0, 3, 10, 10)); // 3 columns, auto rows
        imagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Iterate through the provided imagePaths list
        for (String path : this.imagePaths) {
            addImageToPanel(path, imagePanel);
        }

        JScrollPane scrollPane = new JScrollPane(imagePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(parent); // Center the dialog on the parent frame
    }

    private void addImageToPanel(String path, JPanel panel) {
        try {
            BufferedImage originalImage;
            // Determine if the path is a resource path or an absolute file path
            if (path.startsWith("resources/")) {
                originalImage = ImageToolbar.loadImageFromResources(path);
            } else {
                originalImage = ImageToolbar.loadImageFromFile(path); // Use the new helper method
            }

            if (originalImage == null) {
                System.err.println("Failed to load image for library: " + path);
                return;
            }

            // Scale image to thumbnail size
            Image scaledImage = originalImage.getScaledInstance(THUMBNAIL_SIZE, THUMBNAIL_SIZE, Image.SCALE_SMOOTH);
            ImageIcon thumbnailIcon = new ImageIcon(scaledImage);

            // Extract a display name (e.g., from path, or keep it generic)
            String displayName = new File(path).getName(); // Use file name as display name
            JLabel imageLabel = new JLabel(displayName, thumbnailIcon, SwingConstants.CENTER);
            imageLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
            imageLabel.setHorizontalTextPosition(SwingConstants.CENTER);
            imageLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));

            imageLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        BufferedImage selectedImage;
                        if (path.startsWith("resources/")) {
                            selectedImage = ImageToolbar.loadImageFromResources(path);
                        } else {
                            selectedImage = ImageToolbar.loadImageFromFile(path);
                        }

                        if (selectedImage != null) {
                            CompositionItem item = new CompositionItem(selectedImage,
                                    leftPanel.getWidth() / 2 - selectedImage.getWidth() / 2,
                                    leftPanel.getHeight() / 2 - selectedImage.getHeight() / 2);
                            leftPanel.addCompositionItem(item);
                            dispose(); // Close the dialog after selection
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(ImageLibraryDialog.this, "Error inserting image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
                }
            });
            panel.add(imageLabel);

        } catch (Exception e) {
            System.err.println("Error adding image to library: " + path + " - " + e.getMessage());
            e.printStackTrace();
        }
    }
}