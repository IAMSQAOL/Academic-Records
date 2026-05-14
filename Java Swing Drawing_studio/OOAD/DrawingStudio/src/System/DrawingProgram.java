package System;

import FilesManager.DrawingFileManager;
import Toolbars.BasicTools;
import Toolbars.ImageToolbar;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class DrawingProgram {
    public static void main(String[] args){
        JFrame frame = new JFrame("Drawing Studio 2.0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Panel 
        RightPanel RightCanvas = new RightPanel(); 
        RightCanvas.setBackground(Color.white); 
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5); 
        splitPane.setOneTouchExpandable(true);
        
        LeftPanel leftCanvas = new LeftPanel(frame); 
        
        splitPane.setLeftComponent(leftCanvas);
        splitPane.setRightComponent(RightCanvas);
        
        //MenuBar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem loadItem = new JMenuItem("Load Drawing from this pc");
        JMenuItem exportImageItem = new JMenuItem("Export Drawing as Image");
        JMenuItem exportLeftCanvasItem = new JMenuItem("Export Left Canvas as Image");
        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.add(exportImageItem);
        fileMenu.add(exportLeftCanvasItem);
        fileMenu.addSeparator(); 
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);
        
        Image icon = getWindowIcon("resources/icons/icon.png");
        if (icon != null) {
            frame.setIconImage(icon);
        } else {
            System.err.println("Warning: Window icon not found or could not be loaded.");
        }
        frame.getContentPane().setBackground(Color.LIGHT_GRAY);
        
        JPanel toolbarsPanel = new JPanel();
        toolbarsPanel.setLayout(new BoxLayout(toolbarsPanel, BoxLayout.Y_AXIS));
        JToolBar BasicToolBar = BasicTools.CreateBasicCanvasToolbar(RightCanvas,frame, splitPane);
        toolbarsPanel.add(BasicToolBar);
        JToolBar imageToolbar = ImageToolbar.CreateImageToolBar(leftCanvas, frame);
        toolbarsPanel.add(imageToolbar);
        
        
        frame.getContentPane().add(toolbarsPanel,BorderLayout.NORTH);
        frame.getContentPane().add(splitPane, BorderLayout.CENTER);
        
        frame.pack();
        frame.setSize(1280,720);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        //MenuBar function
        String preferredPath = "C:\\";
        DrawingFileManager fileManager = new DrawingFileManager(RightCanvas,preferredPath);
        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileManager.saveDrawing(); // Call save logic from fileManager
            }
        });
        loadItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileManager.loadDrawing(); // Call load logic from fileManager
            }
        });
        
        exportImageItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileManager.exportDrawingAsImage(); // Call export image logic from fileManager
            }
        });
        exportLeftCanvasItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileManager.exportLeftCanvasAsImage(leftCanvas, frame); // Call new export method
            }
        });
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
    
    private static Image getWindowIcon(String path) {
        java.net.URL imgURL = DrawingProgram.class.getResource("/" + path); // Note the leading "/" for root of classpath
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            return icon.getImage(); // Get the Image object from the ImageIcon
        } else {
            return null;
        }
    }
}
