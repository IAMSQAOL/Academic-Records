package Toolbars;

import DrawingTools.CompositionItem;
import System.LeftPanel;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.FileFilter;

public class ImageToolbar {
    private static JSlider rotationSlider;
    private static JLabel rotationValueLabel;
    
    private static String customLibraryDirectoryPath = null;
    
    public static JToolBar CreateImageToolBar(LeftPanel leftPanel, JFrame parentFrame){
        JToolBar imageToolbar = new JToolBar("Image Composition Tools");
        imageToolbar.setFloatable(false);

        // --- Insert Animal Button ---
        JButton insertAnimalButton = new JButton("Insert Animal");
        insertAnimalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                BufferedImage animalImage = loadImageFromResources("resources/images/default_animal.png");
                if (animalImage != null) {
                    
                    CompositionItem item = new CompositionItem(animalImage,
                            leftPanel.getWidth() / 2 - animalImage.getWidth() / 2,
                            leftPanel.getHeight() / 2 - animalImage.getHeight() / 2);
                    leftPanel.addCompositionItem(item);
                } else {
                    JOptionPane.showMessageDialog(parentFrame, "Default animal image not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        imageToolbar.add(insertAnimalButton);
        
        // --- Insert Flower Button ---
        JButton insertFlowerButton = new JButton("Insert Flower");
        insertFlowerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Placeholder for selecting a flower image from resources
                BufferedImage flowerImage = loadImageFromResources("resources/images/default_flower.png");
                if (flowerImage != null) {
                    CompositionItem item = new CompositionItem(flowerImage,
                            leftPanel.getWidth() / 2 - flowerImage.getWidth() / 2,
                            leftPanel.getHeight() / 2 - flowerImage.getHeight() / 2);
                    leftPanel.addCompositionItem(item);
                } else {
                    JOptionPane.showMessageDialog(parentFrame, "Default flower image not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        imageToolbar.add(insertFlowerButton);
        
        
        JButton setLibraryDirectoryButton = new JButton("Set Library Directory");
        setLibraryDirectoryButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Image Library Directory");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // Only allow directory selection

            int userSelection = fileChooser.showOpenDialog(parentFrame);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File selectedDirectory = fileChooser.getSelectedFile();
                customLibraryDirectoryPath = selectedDirectory.getAbsolutePath();
                JOptionPane.showMessageDialog(parentFrame, "Image Library Directory set to:\n" + customLibraryDirectoryPath, "Directory Set", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        imageToolbar.add(setLibraryDirectoryButton);
        
        JButton imageLibraryButton = new JButton("Image Library");
        imageLibraryButton.addActionListener(e -> {
            showImageLibraryDialog(parentFrame, leftPanel);
        });
        imageToolbar.add(imageLibraryButton);
        

        // --- Rotate Whole Canvas (90) ---
        JButton rotateCanvasRightButton = new JButton("Rotate Whole Left Canvas");
        rotateCanvasRightButton.addActionListener(e -> {
            leftPanel.rotateAllItems(90); // Rotate whole canvas 90 degrees clockwise
            updateRotationSlider(leftPanel.getSelectedItem());
        });
        imageToolbar.add(rotateCanvasRightButton);
        
        JButton mergeCanvasButton = new JButton("Merge Whole Left Canvas");
        mergeCanvasButton.setToolTipText("Merge all current items on the Left Canvas into a single image.");
        mergeCanvasButton.addActionListener(e -> {
            leftPanel.mergeAllItemsAsImage(parentFrame); // Call the new merge method in LeftPanel
        });
        imageToolbar.add(mergeCanvasButton);
        
        // --- Rotation Slider ---
        rotationValueLabel = new JLabel("Rotation: 0°");
        rotationSlider = new JSlider(JSlider.HORIZONTAL, 0, 360, 0); // 0 to 359 degrees
        rotationSlider.setMajorTickSpacing(90);
        rotationSlider.setMinorTickSpacing(10);
        rotationSlider.setPaintTicks(true);
        rotationSlider.setPaintLabels(true);
        rotationSlider.setToolTipText("Adjust rotation angle (0-359 degrees)");

        rotationSlider.addChangeListener(new ChangeListener() { @Override
            public void stateChanged(ChangeEvent e) {
                CompositionItem selected = leftPanel.getSelectedItem();
                if (selected != null) {
                    double newAngle = rotationSlider.getValue();
                    selected.setRotationAngleDegrees(newAngle);
                    rotationValueLabel.setText(String.format("Rotation: %.0f°", newAngle));
                    leftPanel.repaint();
                } else {
                    // If no item is selected, reset slider to 0 and disable
                    rotationValueLabel.setText("Rotation: 0°");
                    rotationSlider.setValue(0);
                }
                rotationSlider.setEnabled(selected != null); // Enable/disable based on selection
            }
        });
        rotationSlider.setEnabled(false);
        JPanel rotationPanel = new JPanel();
        rotationPanel.setLayout(new BoxLayout(rotationPanel, BoxLayout.Y_AXIS));
        rotationPanel.add(rotationValueLabel);
        rotationPanel.add(rotationSlider);
        imageToolbar.add(rotationPanel);

        // Add a MouseListener to the leftPanel to update the slider when selection changes
        leftPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                SwingUtilities.invokeLater(() -> { 
                    CompositionItem selected = leftPanel.getSelectedItem();
                    updateRotationSlider(selected);
                });
            }
        });
        return imageToolbar;
    }
    
   
    public static BufferedImage loadImageFromResources(String path) {
        try {
            java.net.URL imgURL = ImageToolbar.class.getClassLoader().getResource(path);
            if (imgURL != null) {
                return ImageIO.read(imgURL);
            } else {
                System.err.println("Couldn't find resource: " + path);
                System.out.println("Error loading image from resource: " + path);
                return null;
            }
        } catch (IOException e) {
            System.err.println("Error loading image from resource: " + e.getMessage());
            System.out.println("Error loading image from resource: " + e.getMessage());
            return null;
        }
    }
    
    
    public static BufferedImage loadImageFromFile(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                return ImageIO.read(file);
            } else {
                System.err.println("Couldn't find file: " + path);
                return null;
            }
        } catch (IOException e) {
            System.err.println("Error loading image from file: " + e.getMessage());
            return null;
        }
    }
    
    private static void updateRotationSlider(CompositionItem selectedItem) {
        if (selectedItem != null) {
            double currentAngle = selectedItem.getRotationAngleDegrees();
            // Round to nearest integer for slider display
            int sliderValue = (int) Math.round(currentAngle);
            if (sliderValue == 360) sliderValue = 0; // Normalize 360 to 0 for slider range
            rotationSlider.setValue(sliderValue);
            rotationValueLabel.setText(String.format("Rotation: %d°", sliderValue));
            rotationSlider.setEnabled(true);
        } else {
            rotationSlider.setValue(0);
            rotationValueLabel.setText("Rotation: 0°");
            rotationSlider.setEnabled(false);
        }
    }
    
    // --- Image Library Dialog ---
    private static void showImageLibraryDialog(JFrame parentFrame, LeftPanel leftPanel) {
        JDialog libraryDialog = new JDialog(parentFrame, "Image Library", true);
        libraryDialog.setLayout(new BorderLayout());
        libraryDialog.setSize(400, 300);
        libraryDialog.setLocationRelativeTo(parentFrame);

        JPanel imageGridPanel = new JPanel(new GridLayout(0, 3, 5, 5)); // 3 columns, auto rows
        JScrollPane scrollPane = new JScrollPane(imageGridPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Faster scrolling
        libraryDialog.add(scrollPane, BorderLayout.CENTER);

        // Add default images
        addThumbnailToLibrary(imageGridPanel, "resources/images/default_animal.png", true, leftPanel, libraryDialog, parentFrame);
        addThumbnailToLibrary(imageGridPanel, "resources/images/default_flower.png", true, leftPanel, libraryDialog, parentFrame);

        // Load custom images from the set directory
        if (customLibraryDirectoryPath != null) {
            File libraryDir = new File(customLibraryDirectoryPath);
            if (libraryDir.exists() && libraryDir.isDirectory()) {
                File[] files = libraryDir.listFiles(new FileFilter() {
                    // Filter for common image file extensions
                    @Override
                    public boolean accept(File pathname) {
                        String name = pathname.getName().toLowerCase();
                        return pathname.isFile() &&
                                (name.endsWith(".png") || name.endsWith(".jpg") ||
                                 name.endsWith(".jpeg") || name.endsWith(".gif") ||
                                 name.endsWith(".bmp"));
                    }

                   
                });

                if (files != null) {
                    for (File file : files) {
                         addThumbnailToLibrary(imageGridPanel, file.getAbsolutePath(), false, leftPanel, libraryDialog, parentFrame);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(libraryDialog, "Selected library directory does not exist or is not a valid directory.", "Error", JOptionPane.ERROR_MESSAGE);
                customLibraryDirectoryPath = null; // Reset if invalid
            }
        } else {
            JOptionPane.showMessageDialog(libraryDialog, "No image library directory has been set.\nPlease use 'Set Library Dir.' button to select one.", "No Directory Set", JOptionPane.INFORMATION_MESSAGE);
        }


        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> libraryDialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        libraryDialog.add(buttonPanel, BorderLayout.SOUTH);

        libraryDialog.setVisible(true);
    }
    
    private static void addThumbnailToLibrary(JPanel panel, String imagePath, boolean isResource, LeftPanel leftPanel, JDialog libraryDialog, JFrame parentFrame) { 
        try {
            BufferedImage originalImage;
            if (isResource) {
                originalImage = loadImageFromResources(imagePath);
            } else {
                originalImage = loadImageFromFile(imagePath);
            }

            if (originalImage != null) {
                // Scale image to a thumbnail size
                int thumbnailSize = 80;
                Image scaledImage = originalImage.getScaledInstance(thumbnailSize, thumbnailSize, Image.SCALE_SMOOTH);
                ImageIcon thumbnailIcon = new ImageIcon(scaledImage);

                JButton thumbnailButton = new JButton(thumbnailIcon);
                thumbnailButton.setToolTipText(imagePath);
                thumbnailButton.setPreferredSize(new Dimension(thumbnailSize + 10, thumbnailSize + 10)); // Add some padding

                BufferedImage finalImage = originalImage; // To be used in lambda
                thumbnailButton.addActionListener(e -> {
                    // --- Show dimension dialog ---
                    ImageDimensionDialog dimensionDialog = new ImageDimensionDialog(parentFrame, finalImage);
                    dimensionDialog.setVisible(true);

                    if (dimensionDialog.isConfirmed()) {
                        int newWidth = dimensionDialog.getChosenWidth();
                        int newHeight = dimensionDialog.getChosenHeight();
                        CompositionItem item = new CompositionItem(finalImage,
                                leftPanel.getWidth() / 2 - newWidth / 2, // Center based on new width
                                leftPanel.getHeight() / 2 - newHeight / 2, // Center based on new height
                                newWidth, newHeight); // Use new constructor
                        leftPanel.addCompositionItem(item);
                        libraryDialog.dispose(); // Close library dialog after selection
                    }
                    // If not confirmed, dialog remains open, image not added
                });
                panel.add(thumbnailButton);
            }
        } catch (Exception e) {
            System.err.println("Error loading thumbnail for " + imagePath + ": " + e.getMessage());
        }
    }
}
