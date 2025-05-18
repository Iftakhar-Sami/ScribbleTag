
import java.util.Set;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Player {
    private int HEIGHT = 600;
    private int WIDTH = 1000;
    double centerX, centerY; 
    double radius;

    Image image,himage;
    String[] movementKeys;
    private boolean hasTorch=false;

    public Player(double centerX, double centerY, String imagePath,String handimagepath,String[]movementKeys) {
        this.image = new Image(imagePath);
        this.movementKeys = movementKeys;
        this.radius = 30;
        this.centerX = centerX;
        this.centerY = centerY;
        this.himage = new Image(handimagepath);
    
    }

    public void render(GraphicsContext gc) {
        
        gc.drawImage(image, centerX - radius, centerY - radius, radius * 2, radius * 2);
        if(hasTorch==true) gc.drawImage(himage, centerX +radius*2 -192, centerY - radius-68, 320, 180);
    }
    public void sethasTorch(boolean hasTorch){
        this.hasTorch=hasTorch;
    }
    public boolean HasTorch(){
        return this.hasTorch;
    }

    public boolean intersects(Player p2) {
        double dx = this.centerX - p2.centerX;
        double dy = this.centerY - p2.centerY;
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < this.radius + p2.radius;
    }
    public boolean intersectsTorch(Torch torch) {
        double dx = this.centerX - torch.x-25;//25 to incorprate torches size that is 50 px in this case
        double dy = this.centerY - torch.y-25;
        return dx<this.radius && dx>-this.radius && dy<this.radius && dy>-this.radius;
    }

    public void update(double dt, Set<String> input) {
            
    double dx = 0, dy = 0;
    double speed = 250; 

    if (input.contains(movementKeys[0]) && centerY > radius-10) dy -= 1;
    if (input.contains(movementKeys[1])&& centerY < HEIGHT - radius+10) dy += 1;
    if (input.contains(movementKeys[2]) && centerX > radius-9) dx -= 1;
    if (input.contains(movementKeys[3])&& centerX < WIDTH - radius+9) dx += 1;

    
    if (dx != 0 && dy != 0) {
        
        dx /= Math.sqrt(2);
        dy /= Math.sqrt(2);
        
    }

    
    centerX += dx * speed * dt;
    centerY += dy * speed * dt;


    }
}