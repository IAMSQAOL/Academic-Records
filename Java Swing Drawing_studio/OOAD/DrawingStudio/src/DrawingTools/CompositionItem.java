package DrawingTools;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.io.Serializable;
import javax.imageio.ImageIO;


public class CompositionItem implements Serializable{
    private transient BufferedImage image;
    private byte[] imageBytes;
    private int x, y; // Top-left corner position
    private int width, height; // Current dimensions
    private double rotationAngle; // in radians
    private boolean flippedHorizontal;
    private boolean flippedVertical;
    double scaleX,scaleY;
    private int originalImageWidth;
    private int originalImageHeight;
    private static final int SCALE_HANDLE_SIZE = 10;
    
    //load image
    public CompositionItem(BufferedImage image, int x, int y) {
        this.image = image;
        this.originalImageWidth = image.getWidth();
        this.originalImageHeight = image.getHeight();
        
        this.x = x;
        this.y = y;
        this.width = originalImageWidth;
        this.height = originalImageHeight;
        
        this.rotationAngle = 0;
        this.flippedHorizontal = false;
        this.flippedVertical = false;
        writeImageToBytes();
    }
    
    public CompositionItem(BufferedImage image, int x, int y, int initialWidth, int initialHeight) {
        this.image = image;
        this.originalImageWidth = image.getWidth();
        this.originalImageHeight = image.getHeight();

        this.x = x;
        this.y = y;
        this.width = initialWidth; // Set initial width
        this.height = initialHeight; // Set initial height

        this.rotationAngle = 0;
        this.flippedHorizontal = false;
        this.flippedVertical = false;
        writeImageToBytes();
    }
    
    // Drawing method for CompositionItem
    public void draw(Graphics2D g2d) {
        if (image == null && imageBytes != null) {
            readImageFromBytes(); // Deserialize image if it's transient
        }
        if (image == null) return;

        AffineTransform originalTransform = g2d.getTransform();
        AffineTransform at = new AffineTransform();
        
        //Calculate current scale factors based on current width/height vs original image dimensions
        scaleX = (double) width / originalImageWidth;
        scaleY = (double) height / originalImageHeight;
        
        // 1. Translate to center of image for rotation/flip
        at.translate(x + width / 2.0, y + height / 2.0);

        // 2. Apply flips
        if (flippedHorizontal) {
            at.scale(-1.0, 1.0);
        }
        if (flippedVertical) {
            at.scale(1.0, -1.0);
        }

        // 3. Apply rotation
        at.rotate(rotationAngle);
        // 4. Apply scaling
        at.scale(scaleX, scaleY);
        // 5. Translate back from center to top-left of image for drawing
        at.translate(-width / 2.0, -height / 2.0);

        g2d.drawImage(image, at, null);
        g2d.setTransform(originalTransform); // Restore original transform
        
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }

    public double getRotationAngle() { return rotationAngle; }
    public void setRotationAngle(double angle) { this.rotationAngle = angle; }

    public boolean isFlippedHorizontal() { return flippedHorizontal; }
    public void setFlippedHorizontal(boolean flippedHorizontal) { this.flippedHorizontal = flippedHorizontal; }

    public boolean isFlippedVertical() { return flippedVertical; }
    public void setFlippedVertical(boolean flippedVertical) { this.flippedVertical = flippedVertical; }

     // --- Image Manipulation ---
    public void flipHorizontal() {
        this.flippedHorizontal = !this.flippedHorizontal;
    }

    public void flipVertical() {
        this.flippedVertical = !this.flippedVertical;
    }

    public void rotate(double degrees) {
        this.rotationAngle += Math.toRadians(degrees);
        // Normalize angle to be within -PI to PI or 0 to 2PI if preferred
        this.rotationAngle = this.rotationAngle % (2 * Math.PI);
        if (this.rotationAngle < 0) this.rotationAngle += 2 * Math.PI;
    }
    
    public void setRotationAngleDegrees(double degrees) {
        this.rotationAngle = Math.toRadians(degrees);
        // Normalize angle to be within 0 to 2PI
        this.rotationAngle = this.rotationAngle % (2 * Math.PI);
        if (this.rotationAngle < 0) {
            this.rotationAngle += (2 * Math.PI);
        }
    }
    public double getRotationAngleDegrees() {
        // Ensure the returned angle is between 0 and 360 degrees for consistent slider display
        double degrees = Math.toDegrees(rotationAngle);
        // Normalize to be between 0 and 360
        degrees = degrees % 360;
        if (degrees < 0) {
            degrees += 360;
        }
        return degrees;
    }

    public boolean contains(int mx, int my) {
        // Create a GeneralPath from the transformed bounding box
        Rectangle bounds = getBounds();
        return bounds.contains(mx, my);
    }
    
    
    // --- Serialization handling for BufferedImage ---
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject(); // Serialize default fields
        writeImageToBytes();      // Ensure imageBytes is up-to-date
        out.writeObject(imageBytes); // Serialize image data
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject(); // Deserialize default fields
        imageBytes = (byte[]) in.readObject(); // Deserialize image data  
        readImageFromBytes();     // Reconstruct BufferedImage
    }

    private void writeImageToBytes() {
        if (image != null) {
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                ImageIO.write(image, "png", bos); // Using PNG format for serialization
                imageBytes = bos.toByteArray();
            } catch (IOException e) {
                System.err.println("Error converting BufferedImage to bytes: " + e.getMessage());
                imageBytes = null;
            }
        }
    }

    private void readImageFromBytes() {
        if (imageBytes != null) {
            try (ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes)) {
                image = ImageIO.read(bis);
            } catch (IOException e) {
                System.err.println("Error converting bytes to BufferedImage: " + e.getMessage());
                image = null;
            }
        }
    }

    // Method to get bounds for mouse interaction (selection/dragging)
    public Rectangle getBounds() {
      // Create an AffineTransform for the current item's drawing state
        AffineTransform boundsTransform = new AffineTransform();

        // Start from the item's (x, y) and its current scaled dimensions
        boundsTransform.translate(x, y);

        // Translate to the center of the *current* width/height for rotation/flipping pivot
        boundsTransform.translate(width / 2.0, height / 2.0);

        // Apply transformations (flips, rotation)
        if (flippedHorizontal) {
            boundsTransform.scale(-1.0, 1.0);
        }
        if (flippedVertical) {
            boundsTransform.scale(1.0, -1.0);
        }
        boundsTransform.rotate(rotationAngle);
        boundsTransform.scale(scaleX, scaleY);
        // Translate back from center
        boundsTransform.translate(-width / 2.0, -height / 2.0);

        // The points are now the corners of the *current* scaled image (width x height)
        Point2D p1 = new Point2D.Double(0, 0);
        Point2D p2 = new Point2D.Double(originalImageWidth, 0);
        Point2D p3 = new Point2D.Double(0, originalImageHeight);
        Point2D p4 = new Point2D.Double(originalImageWidth, originalImageHeight);

        // Transform these points to find their screen coordinates
        boundsTransform.transform(p1, p1);
        boundsTransform.transform(p2, p2);
        boundsTransform.transform(p3, p3);
        boundsTransform.transform(p4, p4);

        // Find min/max X and Y coordinates among the transformed corners
        double minX = Math.min(Math.min(p1.getX(), p2.getX()), Math.min(p3.getX(), p4.getX()));
        double maxX = Math.max(Math.max(p1.getX(), p2.getX()), Math.max(p3.getX(), p4.getX()));
        double minY = Math.min(Math.min(p1.getY(), p2.getY()), Math.min(p3.getY(), p4.getY()));
        double maxY = Math.max(Math.max(p1.getY(), p2.getY()), Math.max(p3.getY(), p4.getY()));

        // Return the new bounding rectangle
        return new Rectangle((int) minX, (int) minY, (int) (maxX - minX), (int) (maxY - minY));
    }
    
     public Rectangle getScaleHandleBounds() {
        Rectangle currentBounds = getBounds();
        // Position the handle at the bottom-right corner of the item
        return new Rectangle(currentBounds.x + currentBounds.width - SCALE_HANDLE_SIZE / 2,
                             currentBounds.y + currentBounds.height - SCALE_HANDLE_SIZE / 2,
                             SCALE_HANDLE_SIZE,
                             SCALE_HANDLE_SIZE);
    }
    public static int getScaleHandleSize() {
        return SCALE_HANDLE_SIZE;
    }
    
}
