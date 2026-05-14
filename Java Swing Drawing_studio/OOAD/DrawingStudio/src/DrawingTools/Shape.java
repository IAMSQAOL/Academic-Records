package DrawingTools;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

public abstract class Shape implements Serializable{
    public int x;
    public int y;
    protected Color color;
    
    public Shape(int x,int y,Color color){
        this.x = x;
        this.y = y;
        this.color = color;
    }
    
    public abstract void draw(Graphics g);
}
