package Toolbars;

import System.RightPanel;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BasicTools {
     public static JToolBar CreateBasicCanvasToolbar(RightPanel drawingPanel, JFrame parentFrame, JSplitPane splitPane){
        JPanel basicToolsPanel = new JPanel();
        
        JToolBar BasicToolbar = new JToolBar("Drawing Tools");
        BasicToolbar.setFloatable(false);
           
        
        //Circle Button
        ImageIcon circleButtonIcon = createImageIcon("/resources/icons/circle.png");
        JButton circleButton = new JButton("Circle", circleButtonIcon);
        circleButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.setCurrentTool(RightPanel.BasicToolType.CIRCLE);
            }
        });
        basicToolsPanel.add(circleButton);
        
        ImageIcon squareButtonIcon = createImageIcon("/resources/icons/square.png");
        JButton squareButton = new JButton("Square",squareButtonIcon);
        squareButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.setCurrentTool(RightPanel.BasicToolType.SQUARE);
            }
        });
        basicToolsPanel.add(squareButton);
        
        ImageIcon colorButtonIcon = createImageIcon("/resources/icons/color_picker.png");
        JButton colorButton = new JButton("Color",colorButtonIcon);
        colorButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(null,"Choose Drawing Color", drawingPanel.currentColor);
                drawingPanel.setCurrentColor(newColor);
            }
        });

        basicToolsPanel.add(colorButton);
        
        ImageIcon FreeHandButtonIcon = createImageIcon("/resources/icons/freehand.png");
        JButton FreehandButton = new JButton("Freehand",FreeHandButtonIcon);
        FreehandButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.setCurrentTool(RightPanel.BasicToolType.FREEHAND);
            }
        });
        
        basicToolsPanel.add(FreehandButton);
        
        ImageIcon eraserButtonIcon = createImageIcon("/resources/icons/eraser.png");
        JButton eraserButton = new JButton("Eraser",eraserButtonIcon);
        eraserButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.setCurrentTool(RightPanel.BasicToolType.ERASER);
            }
        });
        
        basicToolsPanel.add(eraserButton);
        
        JButton clearButton = new JButton("Clear");
        clearButton.setToolTipText("Clear the right canvas");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingPanel.clearCanvas(); 
            }
        });
        basicToolsPanel.add(clearButton);
        
        
        // --- Toggle Left Canvas Button ---
        JButton toggleLeftCanvasButton = new JButton("Toggle Left Canvas");
        toggleLeftCanvasButton.setToolTipText("Show/Hide the Left Panel");
        
        toggleLeftCanvasButton.addActionListener(new ActionListener() {
            private int lastOpenLocation = -1; // Stores the divider location when the left panel was last open

            @Override
            public void actionPerformed(ActionEvent e) {
                if (splitPane.getDividerLocation() > 10) { // If LeftPanel is currently visible (more than 10px wide)
                    lastOpenLocation = splitPane.getDividerLocation(); // Store current position
                    splitPane.setDividerLocation(0); // Collapse LeftPanel
                    splitPane.setResizeWeight(0.0); // Make it non-resizable when collapsed
                } else { // If LeftPanel is collapsed
                    if (lastOpenLocation != -1) {
                        splitPane.setDividerLocation(lastOpenLocation); // Restore to last open position
                    } else {
                        // If no previous location, default to a sensible split
                        splitPane.setDividerLocation(splitPane.getPreferredSize().width / 3); 
                    }
                    splitPane.setResizeWeight(0.5); // Restore resize weight
                }
            }
        });
        basicToolsPanel.add(toggleLeftCanvasButton);
        // ----------------------------------------
        
        JLabel strokeSizeLabel = new JLabel("Stroke Size:");
        basicToolsPanel.add(strokeSizeLabel);

        JSlider strokeSlider = new JSlider(JSlider.HORIZONTAL, 1, 50, drawingPanel.getCurrentStrokeSize()); // Min 1, Max 50
        strokeSlider.setMajorTickSpacing(10);
        strokeSlider.setMinorTickSpacing(1);
        strokeSlider.setPaintTicks(true);
        strokeSlider.setPaintLabels(true);
        strokeSlider.setToolTipText("Adjust drawing stroke size");

        JLabel currentValueLabel = new JLabel(String.valueOf(drawingPanel.getCurrentStrokeSize()), SwingConstants.CENTER);
        currentValueLabel.setPreferredSize(new Dimension(30, 20)); // Give it a fixed size
        
        strokeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int newSize = strokeSlider.getValue();
                drawingPanel.setCurrentStrokeSize(newSize);
                currentValueLabel.setText(String.valueOf(newSize));
            }
        });
        
        basicToolsPanel.add(strokeSlider);
        basicToolsPanel.add(currentValueLabel);
        
        BasicToolbar.add(basicToolsPanel);
        return BasicToolbar;
    }
    
    private static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = BasicTools.class.getResource(path);
        if (imgURL != null) {
            ImageIcon buttonIcon = new ImageIcon(imgURL);
            Image setButtonIcon = (buttonIcon.getImage()).getScaledInstance(28, 28, Image.SCALE_SMOOTH);
            return new ImageIcon(setButtonIcon);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
