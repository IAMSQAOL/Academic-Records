package System;

import DrawingTools.Shape;
import Inputs.BasicMouseInputs;

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList; 
import java.util.List;

public class RightPanel extends JPanel{
    
    private BasicMouseInputs mouseInputs;
    public enum BasicToolType{CIRCLE,SQUARE,FREEHAND,ERASER,NONE}
    private BasicToolType currentTool = BasicToolType.NONE;
    private List<Shape> shapes = new ArrayList<>();
    public Color currentColor = Color.BLACK;
    private int currentStrokeSize = 5;
    private Shape currentDrawingShape = null;
    
    
    public RightPanel(){
        this.setBackground(Color.white); // This is the color the eraser will "draw" with
        mouseInputs = new Inputs.BasicMouseInputs(this);
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }
    
    
    //Tool select
    public void setCurrentTool(BasicToolType tool) {
        this.currentTool = tool;
        System.out.println("Current tool set to: " + tool);
    }
    public void clearCanvas() {
        shapes.clear(); // Remove all drawn shapes
        repaint(); 
    }
    public void setCurrentColor(Color color) {
        this.currentColor = color;
    }
    public void setCurrentStrokeSize(int size) {
        this.currentStrokeSize = size;
    }
    //Tool call
    public BasicToolType getCurrentTool() {
        return currentTool;
    }
    public Color getCurrentColor() {
        return currentColor;
    }
    public int getCurrentStrokeSize() {
        return currentStrokeSize;
    }
    public List<Shape> getShapes() {
        return shapes;
    }
    public void setCurrentDrawingShape(Shape shape) {
        this.currentDrawingShape = shape;
    }
    public Shape getCurrentDrawingShape() {
        return currentDrawingShape;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        for (Shape shape : shapes) { //
            shape.draw(g);
        }
    }
}