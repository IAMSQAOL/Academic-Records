package Inputs;

import DrawingTools.*;
import System.RightPanel;
import java.awt.event.*;

public class BasicMouseInputs extends MouseAdapter{
    private RightPanel drawingPanel;
    
    public BasicMouseInputs(RightPanel drawingPanel){
        this.drawingPanel = drawingPanel;
    }
    

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        RightPanel.BasicToolType currentTool = drawingPanel.getCurrentTool();
        Shape newShape = null;
        if (null != currentTool) switch (currentTool) {
            case CIRCLE:
                newShape = new Circle(x, y, 0, drawingPanel.getCurrentColor()); // Radius will be determined on drag
                break;
            case FREEHAND:
                newShape = new FreeHand(x, y, drawingPanel.getCurrentColor(), drawingPanel.getCurrentStrokeSize()); //
                break;
            case ERASER:
                newShape = new Eraser(x, y, drawingPanel.getBackground(), drawingPanel.getCurrentStrokeSize()); //
                break;
            case SQUARE:
                newShape = new Square(x, y, drawingPanel.getCurrentColor());
            default:
                break;
        }
        if (newShape != null) {
            drawingPanel.getShapes().add(newShape);
            drawingPanel.setCurrentDrawingShape(newShape); // Set the shape being currently drawn
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        drawingPanel.setCurrentDrawingShape(null);
        drawingPanel.repaint(); 
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        Shape currentShape = drawingPanel.getCurrentDrawingShape();
        RightPanel.BasicToolType currentTool = drawingPanel.getCurrentTool();

        if (currentShape != null) {
            if (currentTool == RightPanel.BasicToolType.FREEHAND && currentShape instanceof FreeHand) { 
                // Add points to the FreeHand path
                ((FreeHand) currentShape).addPoint(x, y); 
            } else if (currentTool == RightPanel.BasicToolType.ERASER && currentShape instanceof Eraser) {
                ((Eraser) currentShape).addPoint(x, y);
            } else if (currentTool == RightPanel.BasicToolType.CIRCLE && currentShape instanceof Circle) { 
                Circle circle = (Circle) currentShape;
                int startX = circle.x; 
                int startY = circle.y;
                int radius = (int) Math.sqrt(Math.pow(x - startX, 2) + Math.pow(y - startY, 2));
                circle.setRadius(radius); 
            } else if(currentTool == RightPanel.BasicToolType.SQUARE && currentShape instanceof Square){
                Square square = (Square) currentShape;
                int startX = square.x; 
                int startY = square.y; 
                int currentWidth = x - startX;
                int currentHeight = y - startY;
                 // Determine the side length of the square (take the absolute max of width/height)
                int side = Math.max(Math.abs(currentWidth), Math.abs(currentHeight));
                // Determine the top-left corner of the square
                int newX = startX;
                int newY = startY;
                if (currentWidth < 0) { // Dragging left
                    newX = startX - side;
                }
                if (currentHeight < 0) { // Dragging up
                    newY = startY - side;
                }
                // If dragging left and up, adjust x and y to account for negative side length
                // This ensures the square is drawn from its top-left corner
                if (currentWidth < 0 && currentHeight >= 0) { // Dragging left-down
                    newX = startX - side;
                    newY = startY;
                } else if (currentWidth >= 0 && currentHeight < 0) { // Dragging right-up
                    newX = startX;
                    newY = startY - side;
                } else if (currentWidth < 0 && currentHeight < 0) { // Dragging left-up
                    newX = startX - side;
                    newY = startY - side;
                }
                
                square.setX(newX);
                square.setY(newY);
                square.setWidth(side); 
                square.setHeight(side);
            }
            drawingPanel.repaint(); 
        }
    }

    

    //No use
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    
}
