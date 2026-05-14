package Inputs;

import DrawingTools.CompositionItem;
import System.LeftPanel;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class ImageMouseInputs extends MouseAdapter{
    private LeftPanel leftPanel;
    private CompositionItem selectedItem; // Keep track of the currently selected item within this listener
    private Point lastMousePosition; // To store the mouse position during drag
    
    // --- Scaling related variables ---
    private boolean isScaling = false;
    private int initialItemWidth;
    private int initialItemHeight;
    private int initialMouseX;
    private int initialMouseY;
    private static final int MIN_SIZE = 10; // Minimum allowed width/height
    // ---------------------------------
    
    public ImageMouseInputs(LeftPanel leftPanel) {
        this.leftPanel = leftPanel;
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            
            isScaling = false; // Reset scaling state

            // Check if click is on a scale handle of the currently selected item
            if (leftPanel.getSelectedItem() != null) {
                Rectangle handleBounds = leftPanel.getSelectedItem().getScaleHandleBounds();
                if (handleBounds.contains(e.getPoint())) {
                    selectedItem = leftPanel.getSelectedItem(); // Ensure selectedItem is set
                    isScaling = true;
                    initialItemWidth = selectedItem.getWidth();
                    initialItemHeight = selectedItem.getHeight();
                    initialMouseX = e.getX();
                    initialMouseY = e.getY();
                    return; // Don't proceed with regular selection/drag if starting to scale
                }
            }
            
            selectedItem = null;
            // Get the list of composition items from the LeftPanel
            // Iterate through items in reverse order to select the topmost one
            for (int i = leftPanel.getCompositionItems().size() - 1; i >= 0; i--) {
                CompositionItem item = leftPanel.getCompositionItems().get(i);
                Rectangle itemBounds = item.getBounds();
                if (itemBounds.contains(e.getPoint())) {
                    selectedItem = item;
                    leftPanel.setSelectedItem(selectedItem); // Inform LeftPanel about the selection
                    lastMousePosition = e.getPoint(); // Store initial mouse position for dragging

                    // Bring the selected item to the front for easier manipulation
                    // This means changing its Z-order in the list, so it's drawn last (on top)
                    leftPanel.getCompositionItems().remove(item);
                    leftPanel.getCompositionItems().add(item);

                    leftPanel.repaint(); // Repaint to show the selection border and new z-order
                    break; // Found and selected an item, stop searching
                }
            }
            // If no item was clicked, ensure LeftPanel also deselects
            if (selectedItem == null) {
                leftPanel.setSelectedItem(null);
            }
        
        //If mouse2 selected image --> Menu
        }else if (SwingUtilities.isRightMouseButton(e)) {
           CompositionItem clickedItem = null;
            for (int i = leftPanel.getCompositionItems().size() - 1; i >= 0; i--) {
                CompositionItem item = leftPanel.getCompositionItems().get(i);
                Rectangle itemBounds = item.getBounds();
                if (itemBounds.contains(e.getPoint())) {
                    clickedItem = item;
                    break;
                } 
            }
            
            if (clickedItem != null) {
                // Select the clicked item if it's not already selected
                if (leftPanel.getSelectedItem() != clickedItem) {
                    leftPanel.setSelectedItem(clickedItem);
                }
                selectedItem = clickedItem; // Ensure local selectedItem is set for menu actions

                JPopupMenu popupMenu = new JPopupMenu();

                JMenuItem flipHItem = new JMenuItem("Flip Horizontal");
                flipHItem.addActionListener(actionEvent -> {
                    selectedItem.flipHorizontal();
                    leftPanel.repaint();
                });
                popupMenu.add(flipHItem);

                JMenuItem flipVItem = new JMenuItem("Flip Vertical");
                flipVItem.addActionListener(actionEvent -> {
                    selectedItem.flipVertical();
                    leftPanel.repaint();
                });
                popupMenu.add(flipVItem);

                popupMenu.addSeparator();

                JMenuItem rotateLeftItem = new JMenuItem("Rotate Left (90°)");
                rotateLeftItem.addActionListener(actionEvent -> {
                    selectedItem.rotate(-90);
                    leftPanel.repaint();
                });
                popupMenu.add(rotateLeftItem);

                JMenuItem rotateRightItem = new JMenuItem("Rotate Right (90°)");
                rotateRightItem.addActionListener(actionEvent -> {
                    selectedItem.rotate(90);
                    leftPanel.repaint();
                });
                popupMenu.add(rotateRightItem);

                popupMenu.addSeparator();
                
               JMenuItem deleteItem = new JMenuItem("Delete");
               deleteItem.addActionListener(actionEvent -> {
                    leftPanel.removeCompositionItem(selectedItem);
                    leftPanel.repaint();
                });
                popupMenu.add(deleteItem);
                popupMenu.addSeparator();
                
                popupMenu.show(e.getComponent(), e.getX(), e.getY());
            } else {
                // If right-clicked outside any item, deselect any previously selected item
                leftPanel.setSelectedItem(null);
                selectedItem = null;
            }  
        }  
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (selectedItem != null && SwingUtilities.isLeftMouseButton(e)) {
            if (isScaling) {
                // --- Scaling logic ---
                int currentDx = e.getX() - initialMouseX;
                int currentDy = e.getY() - initialMouseY;

                int newWidth = initialItemWidth + currentDx;
                int newHeight = initialItemHeight + currentDy;

                // Ensure new dimensions are not less than MIN_SIZE
                newWidth = Math.max(MIN_SIZE, newWidth);
                newHeight = Math.max(MIN_SIZE, newHeight);

                selectedItem.setWidth(newWidth);
                selectedItem.setHeight(newHeight);
                // ----------------------
            } else if (lastMousePosition != null) {
                // Regular dragging logic
                int dx = e.getX() - lastMousePosition.x;
                int dy = e.getY() - lastMousePosition.y;

                selectedItem.setX(selectedItem.getX() + dx);
                selectedItem.setY(selectedItem.getY() + dy);

                lastMousePosition = e.getPoint();
            }
            leftPanel.repaint(); 
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        lastMousePosition = null;
        isScaling = false;
    }
}
