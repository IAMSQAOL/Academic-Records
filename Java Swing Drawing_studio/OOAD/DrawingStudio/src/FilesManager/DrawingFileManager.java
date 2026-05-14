package FilesManager;

import DrawingTools.Shape;
import System.RightPanel;
import System.LeftPanel; 
import java.awt.Color;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DrawingFileManager {
    private RightPanel drawingPanel;
    private File defaultSaveLoadDirectory;

    public DrawingFileManager(RightPanel drawingPanel, String defaultPath){
        this.drawingPanel = drawingPanel;
        if (defaultPath != null && !defaultPath.isEmpty()) {
            File path = new File(defaultPath);
            if (path.exists() && path.isDirectory()) {
                this.defaultSaveLoadDirectory = path;
            } else {
                System.err.println("Default directory does not exist or is not a directory: " + defaultPath);
            }
        }
    }

    public void saveDrawing(){
        JFileChooser fileChooser = new JFileChooser();
        if (defaultSaveLoadDirectory != null) {
            fileChooser.setCurrentDirectory(defaultSaveLoadDirectory);
        }
        fileChooser.setDialogTitle("Save Drawing");
        int userSelection = fileChooser.showSaveDialog(drawingPanel);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().toLowerCase().endsWith(".ser")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".ser");
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileToSave))) {
                oos.writeObject(drawingPanel.getShapes());
                JOptionPane.showMessageDialog(drawingPanel, "Drawing saved successfully to " + fileToSave.getName(), "Save Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(drawingPanel, "Error saving drawing: " + ex.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void loadDrawing(){
        JFileChooser fileChooser = new JFileChooser();
        if (defaultSaveLoadDirectory != null) {
            fileChooser.setCurrentDirectory(defaultSaveLoadDirectory);
        }
        fileChooser.setDialogTitle("Load Drawing");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Drawing Files (*.ser)", "ser"));
        int userSelection = fileChooser.showOpenDialog(drawingPanel);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileToLoad))) {
                List<Shape> loadedShapes = (List<Shape>) ois.readObject();
                drawingPanel.getShapes().clear();
                drawingPanel.getShapes().addAll(loadedShapes);
                drawingPanel.repaint();
                JOptionPane.showMessageDialog(drawingPanel, "Drawing loaded successfully from " + fileToLoad.getName(), "Load Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(drawingPanel, "Error loading drawing: " + ex.getMessage(), "Load Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void exportDrawingAsImage() {
        BufferedImage image = new BufferedImage(drawingPanel.getWidth(), drawingPanel.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        drawingPanel.paint(g2d);
        g2d.dispose();

        JFileChooser fileChooser = new JFileChooser();
        if (defaultSaveLoadDirectory != null) {
            fileChooser.setCurrentDirectory(defaultSaveLoadDirectory);
        }
        fileChooser.setDialogTitle("Export Drawing as Image");
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG Image (*.png)", "png"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JPEG Image (*.jpeg)", "jpeg", "jpg"));
        fileChooser.setAcceptAllFileFilterUsed(false);

        int userSelection = fileChooser.showSaveDialog(drawingPanel);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String format = "png";
            String extension = ".png";

            if (fileChooser.getFileFilter() instanceof FileNameExtensionFilter) {
                FileNameExtensionFilter filter = (FileNameExtensionFilter) fileChooser.getFileFilter();
                if (filter.getExtensions().length > 0) {
                    format = filter.getExtensions()[0];
                    extension = "." + format;
                }
            } else {
                String fileName = fileToSave.getName();
                int dotIndex = fileName.lastIndexOf('.');
                if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
                    extension = fileName.substring(dotIndex);
                    format = extension.substring(1);
                }
            }

            if (!fileToSave.getName().toLowerCase().endsWith(extension)) {
                fileToSave = new File(fileToSave.getAbsolutePath() + extension);
            }

            try {
                ImageIO.write(image, format, fileToSave);
                JOptionPane.showMessageDialog(drawingPanel, "Image exported successfully to " + fileToSave.getName(), "Export Success", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(drawingPanel, "Error exporting image: " + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void exportLeftCanvasAsImage(LeftPanel leftPanel, JFrame parentFrame) {
        // Create a BufferedImage to draw the LeftPanel's content onto
        int panelWidth = leftPanel.getWidth();
        int panelHeight = leftPanel.getHeight();

        if (panelWidth <= 0 || panelHeight <= 0) {
             JOptionPane.showMessageDialog(parentFrame, "Left Canvas dimensions are invalid. Cannot export.", "Export Error", JOptionPane.ERROR_MESSAGE);
             return;
        }

        BufferedImage imageToExport = new BufferedImage(panelWidth, panelHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = imageToExport.createGraphics();

        // Clear with transparent background
        g2d.setBackground(new Color(0,0,0,0));
        g2d.clearRect(0, 0, panelWidth, panelHeight);
        // Draw all composition items from the LeftPanel onto the buffered image
        leftPanel.paint(g2d); 

        g2d.dispose();

        JFileChooser fileChooser = new JFileChooser();
        if (defaultSaveLoadDirectory != null) {
            fileChooser.setCurrentDirectory(defaultSaveLoadDirectory);
        }
        fileChooser.setDialogTitle("Export Left Canvas as Image");
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG Image (*.png)", "png"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JPEG Image (*.jpg, *.jpeg)", "jpg", "jpeg"));
        fileChooser.setAcceptAllFileFilterUsed(false); 
        int userSelection = fileChooser.showSaveDialog(parentFrame); 
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String format = "png";
            String extension = ".png"; 
            if (fileChooser.getFileFilter() instanceof FileNameExtensionFilter) {
                FileNameExtensionFilter filter = (FileNameExtensionFilter) fileChooser.getFileFilter();
                if (filter.getExtensions().length > 0) {
                    format = filter.getExtensions()[0];
                    extension = "." + format;
                }
            } else {
                String fileName = fileToSave.getName();
                int dotIndex = fileName.lastIndexOf('.');
                if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
                    extension = fileName.substring(dotIndex);
                    format = extension.substring(1);
                }
            }
            if (!fileToSave.getName().toLowerCase().endsWith(extension)) {
                fileToSave = new File(fileToSave.getAbsolutePath() + extension);
            }
            try {
                ImageIO.write(imageToExport, format, fileToSave);
                JOptionPane.showMessageDialog(parentFrame, "Left Canvas exported successfully to " + fileToSave.getName(), "Export Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(parentFrame, "Error exporting image: " + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
