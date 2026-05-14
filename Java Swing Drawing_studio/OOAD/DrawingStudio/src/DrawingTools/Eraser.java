package DrawingTools;

import java.awt.*;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class Eraser extends Shape implements Serializable{
    private List<Point> points; 
    private int strokeSize;     
    
    public Eraser(int x, int y, Color color) {
        super(x, y, color);
    }
    
    public Eraser(int x, int y, Color color, int strokeSize) {
        super(x, y, color); 
        this.points = new ArrayList<>();
        this.points.add(new Point(x,y));
        this.strokeSize = strokeSize;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(strokeSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)); 

        for (int i = 0; i < points.size() - 1; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get(i + 1);
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }

    public void addPoint(int x, int y) {
         this.points.add(new Point(x, y));
    }
    
}
