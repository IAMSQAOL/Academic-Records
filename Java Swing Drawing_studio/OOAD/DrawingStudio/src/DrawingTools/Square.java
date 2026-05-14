package DrawingTools;

import java.awt.*;
import java.io.Serializable;


public class Square extends Shape implements Serializable{
    private int width,height;
    
    public Square(int x, int y, Color color) {
        super(x, y, color);
        this.width = 0; 
        this.height = 0;
    }
    
    public Square(int x,int y,int width,int height,Color color){
        super(x, y, color);
        this.width = width;
        this.height = height;
    }
    
    //Setter
    public void setWidth(int width) {
        this.width = width;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    
    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.drawRect(x, y, width, height);
    }
    
}
