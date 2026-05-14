package DrawingTools;

import java.awt.*;
import java.io.Serializable;

public class Circle extends Shape implements Serializable{
    private int radius;
    
    public Circle(int x, int y, Color color) {
        super(x, y, color);
        this.radius = 0;
    }
    
    public Circle(int x, int y,int radius,Color color) {
        super(x, y, color);
        this.radius = radius;
    }
    

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.drawOval(x-radius, y-radius, 2*radius, 2*radius);
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
    
}
