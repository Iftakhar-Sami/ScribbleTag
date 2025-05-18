
import java.util.Set;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Player {
    double centerX, centerY; 
    double radius;
    Image image;
    String[] movementKeys;

    public Player(double centerX, double centerY, String imagePath,String[]movementKeys) {
        this.image = new Image(imagePath);
        this.movementKeys = movementKeys;
        this.radius = this.image.getWidth() / 4.0;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public void render(GraphicsContext gc) {
        
        gc.drawImage(image, centerX - radius, centerY - radius, radius * 2, radius * 2);
    }

    public boolean intersects(Player p2) {
        double dx = this.centerX - p2.centerX;
        double dy = this.centerY - p2.centerY;
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < this.radius + p2.radius;
    }

    public void update(double dt, Set<String> input) {
            
    double dx = 0, dy = 0;
    double speed = 250; 

    if (input.contains(movementKeys[0])) dy -= 1;
    if (input.contains(movementKeys[1])) dy += 1;
    if (input.contains(movementKeys[2])) dx -= 1;
    if (input.contains(movementKeys[3])) dx += 1;

    
    if (dx != 0 && dy != 0) {
        
        dx /= Math.sqrt(2);
        dy /= Math.sqrt(2);
        
    }

    
    centerX += dx * speed * dt;
    centerY += dy * speed * dt;


    }
}