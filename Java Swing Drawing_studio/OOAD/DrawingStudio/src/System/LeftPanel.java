package System;

import DrawingTools.CompositionItem;
import Inputs.ImageMouseInputs;
import Toolbars.ImageDimensionDialog;
import Toolbars.ImageToolbar;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.ArrayList;
import java.util.List;

//Drag and Drop
import java.io.File;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.dnd.*;


public class LeftPanel extends JPanel implements DropTargetListener{
    
    private List<CompositionItem> compositionItems;
    private CompositionItem selectedItem;
    private JFrame parentFrame;
    
    public LeftPanel(){
        this.parentFrame = parentFrame;
        this.setBackground(Color.LIGHT_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        this.compositionItems = new ArrayList<>();
        this.setLayout(null);
        
        //MouseInputs
        ImageMouseInputs imageMouseListener = new ImageMouseInputs(this);
        addMouseListener(imageMouseListener);
        addMouseMotionListener(imageMouseListener);
    }
    
    public LeftPanel(JFrame parentFrame){
        this();
    }
    
    
    public void addCompositionItem(CompositionItem item) {
        this.compositionItems.add(item);
        repaint();
    }
    
    public void removeCompositionItem(CompositionItem item) {
        this.compositionItems.remove(item);
        if (selectedItem == item) {
            selectedItem = null;
        }
        repaint();
    }
    
    public List<CompositionItem> getCompositionItems() {
        return compositionItems;
    }
    
    public void setCompositionItems(List<CompositionItem> items) {
        this.compositionItems = items;
        repaint();
    }
    
    public CompositionItem getSelectedItem() {
        return selectedItem;
    }
    
    public void setSelectedItem(CompositionItem item) {
         if (this.selectedItem != item) {
            this.selectedItem = item;
            repaint();
        }
    }
    
    public void mergeAllItemsAsImage(JFrame parentFrame) {
        if (compositionItems.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "No items to merge on the Left Canvas.", "Merge Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        // Calculate bounding box that contains all items
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        if (panelWidth <= 0 || panelHeight <= 0) {
            JOptionPane.showMessageDialog(parentFrame, "Left Canvas dimensions are invalid. Cannot merge.", "Merge Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Iterate to find overall bounds, considering transformations
        for (CompositionItem item : compositionItems) {
            Rectangle itemBounds = item.getBounds(); 
            minX = Math.min(minX, itemBounds.getX());
            minY = Math.min(minY, itemBounds.getY());
            maxX = Math.max(maxX, itemBounds.getX() + itemBounds.getWidth());
            maxY = Math.max(maxY, itemBounds.getY() + itemBounds.getHeight());
        }

        int mergedImageWidth = (int) Math.round(maxX - minX);
        int mergedImageHeight = (int) Math.round(maxY - minY);

        if (mergedImageWidth <= 0 || mergedImageHeight <= 0) {
            JOptionPane.showMessageDialog(parentFrame, "Calculated merge area is invalid. No items or items have no size.", "Merge Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create a BufferedImage to draw all items onto
        BufferedImage mergedImage = new BufferedImage(mergedImageWidth, mergedImageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = mergedImage.createGraphics();

        // Make background transparent
        g2d.setBackground(new Color(0,0,0,0));
        g2d.clearRect(0, 0, mergedImageWidth, mergedImageHeight);

        // Translate the Graphics2D context so that items are drawn relative to the merged image's (0,0)
        // i.e., the top-left of the overall bounding box becomes (0,0) in the merged image
        g2d.translate(-minX, -minY);
        for (CompositionItem item : compositionItems) {
            item.draw(g2d);
        }
        g2d.dispose();
        CompositionItem mergedItem = new CompositionItem(mergedImage, (int) minX, (int) minY);
        // Clear all existing items and add the new merged item
        compositionItems.clear(); 
        addCompositionItem(mergedItem); 
        setSelectedItem(mergedItem); 
        repaint();

        JOptionPane.showMessageDialog(parentFrame, "All items merged into a single image on the Left Canvas.", "Merge Complete", JOptionPane.INFORMATION_MESSAGE);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        for (CompositionItem item : compositionItems) {
            item.draw(g2d);
            // Optional: Draw selection border if item is selected
            if (item == selectedItem) {
                g2d.setColor(Color.BLUE);
                g2d.setStroke(new BasicStroke(2));
                Rectangle bounds = item.getBounds();
                g2d.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
                
                 //----- scale logic -----
                g2d.setColor(Color.RED); // Use a distinct color for the handle
                Rectangle handleBounds = item.getScaleHandleBounds();
                g2d.fillRect(handleBounds.x, handleBounds.y, handleBounds.width, handleBounds.height);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(handleBounds.x, handleBounds.y, handleBounds.width, handleBounds.height);
                // -----------------------
            }
        }
    }
     public void rotateAllItems(double degrees) {
        
        for (CompositionItem item : compositionItems) {
            item.rotate(90);
        }
        repaint(); 
    }
     
   
    public JFrame getParentFrame() { 
        return parentFrame;
    }
     
     // --- DropTargetListener Implementations ---
    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        // Check if the dragged data contains a file list (e.g., from Explorer)
        if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            dtde.acceptDrag(DnDConstants.ACTION_COPY); // Accept the drag as a copy operation
            this.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3)); // Visual feedback
        } else {
            dtde.rejectDrag(); // Reject if not a file list
        }
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        // Similar check as dragEnter, or just accept if already accepted in dragEnter
        if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            dtde.acceptDrag(DnDConstants.ACTION_COPY);
        } else {
            dtde.rejectDrag();
        }
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
        // Not typically needed for simple drag and drop, but must be implemented
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
        this.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2)); // Revert visual feedback
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        this.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2)); // Revert visual feedback

        try {
            // Check if the drop is acceptable (contains file list)
            if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                dtde.acceptDrop(DnDConstants.ACTION_COPY);

                // Get the transferable data
                 java.awt.datatransfer.Transferable transferable = dtde.getTransferable();

                // Get the list of files. Cast is necessary as getContents returns Object.
                @SuppressWarnings("unchecked") // Suppress unchecked cast warning
                List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);

                // Process each dropped file
                for (File file : files) {
                    if (file.isFile()) {
                        String filePath = file.getAbsolutePath();
                        BufferedImage droppedImage = ImageToolbar.loadImageFromFile(filePath); // Use loadImageFromFile from ImageToolbar

                        if (droppedImage != null) {
                            // --- Show dimension dialog ---
                            ImageDimensionDialog dimensionDialog = new ImageDimensionDialog(parentFrame, droppedImage);
                            dimensionDialog.setVisible(true);

                            if (dimensionDialog.isConfirmed()) {
                                int newWidth = dimensionDialog.getChosenWidth();
                                int newHeight = dimensionDialog.getChosenHeight();

                                // Calculate initial position for the dropped image (centered or near drop point)
                                int x = (int) dtde.getLocation().getX() - newWidth / 2;
                                int y = (int) dtde.getLocation().getY() - newHeight / 2;

                                CompositionItem item = new CompositionItem(droppedImage, x, y, newWidth, newHeight); // Use new constructor
                                addCompositionItem(item);
                            }
                            // If not confirmed, do nothing (image is not added)
                        } else {
                            System.err.println("Failed to load image from dropped file: " + filePath);
                        }
                    }
                }
                dtde.dropComplete(true); // Indicate successful drop
            } else {
                dtde.rejectDrop(); // Reject if data flavor is not supported
            }
        } catch (UnsupportedFlavorException | IOException ex) {
            dtde.rejectDrop(); // Reject on error
            JOptionPane.showMessageDialog(this, "Error processing dropped file: " + ex.getMessage(), "Drop Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    // ------------------------------------------
}
