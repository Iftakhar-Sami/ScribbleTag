

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Player {
    private int HEIGHT = 600;
    private int WIDTH = 1000;
    double centerX, centerY;
    double radius;
    double baseSpeed = 250, boostspeed = 400;
    Image image, himage;
    String[] movementKeys;
    double speed = baseSpeed;
    private boolean hasTorch = false;

    double boostCooldown = 7;
    double CooldownTimeLeft = 0;
    double boostDuration = 2;
    double slowDownDuration = 1.2;
    double slowDownTimeLeft = 0;
    double speedMultiplier=1;
    double slipSpeed=0.45;
    double boostTimeLeft;
    boolean speedBoostActive = false,intersectsMud=false;
    

    Color color;

    double currentX, currentY, prevX, prevY;

    private Queue<TrailPoint> trail = new LinkedList<>();
    
    double torchHoldTime=0;

    private static final int TRAIL_LENGTH = 3;

    public Player(double centerX, double centerY,Color color, String imagePath, String handimagepath, String[] movementKeys) {
        this.image = new Image(imagePath);
        this.movementKeys = movementKeys;
        this.radius = 30;
        this.centerX = centerX;
        this.centerY = centerY;
        this.color=color;
        this.himage = new Image(handimagepath);
        currentX = centerX;
        currentY = centerY;

    }

    public void render(GraphicsContext gc,double X,double Y) {
        double opacity = 0.05;
        for (TrailPoint p : trail) {
            gc.setGlobalAlpha(opacity);
            gc.drawImage(image, p.x - radius, p.y - radius, radius * 2, radius * 2);

            opacity += 0.05;
        }

        gc.setGlobalAlpha(1.0);
        gc.drawImage(image, centerX - radius, centerY - radius, radius * 2, radius * 2);
        if (hasTorch == true){
            Image torchimg =  new Image("file:src/resources/weapon_staff.png");
            gc.drawImage(torchimg,centerX+radius-26,centerY-34,50,50);
            gc.drawImage(himage,centerX+radius-26,centerY-26,50,50);

        }


        double progressBarWidth = 30;
        double progressBarHeight = 7;
        double progressBarX = centerX - progressBarWidth / 2;
        double progressBarY = centerY - radius - progressBarHeight+5;

        double progress = 1 - (CooldownTimeLeft / boostCooldown);
        gc.setStroke(Color.valueOf("#4a4a4a")); 
        gc.setLineWidth(2); 
        gc.strokeRoundRect(progressBarX, progressBarY, progressBarWidth, progressBarHeight, 7,7);

        
        gc.setFill(color); 
        gc.fillRoundRect(progressBarX + 2, progressBarY + 2.15, progressBarWidth * progress - 4, progressBarHeight - 4,3,3);

        gc.setFill(color);
        Font font=Font.loadFont("file:src/resources/SuperFoods-2OxXo.ttf",40);
        gc.setFont(font);
        gc.fillText(String.format("%.1f",torchHoldTime),X,Y);
    }

    public void sethasTorch(boolean hasTorch) {
        this.hasTorch = hasTorch;
    }

    public boolean HasTorch() {
        return this.hasTorch;
    }

    public boolean intersects(Player p2) {
        double dx = this.centerX - p2.centerX;
        double dy = this.centerY - p2.centerY;
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < this.radius + p2.radius - 10;
    }

    public boolean intersectsTorch(Torch torch) {
        double dx = this.centerX - torch.x - 25;// 25 to incorprate torches size that is 50 px in this case
        double dy = this.centerY - torch.y - 25;
        return dx < this.radius && dx > -this.radius && dy < this.radius && dy > -this.radius;
    }

    public void update(double dt, Set<String> input) {
        prevX=currentX;
        prevY=currentY;
        currentX=centerX;
        currentY=centerY;

        double dx = 0, dy = 0;

        if (input.contains(movementKeys[0]) && centerY > radius - 10)
            dy -= 1;
        if (input.contains(movementKeys[1]) && centerY < HEIGHT - radius + 10)
            dy += 1;
        if (input.contains(movementKeys[2]) && centerX > radius - 9)
            dx -= 1;
        if (input.contains(movementKeys[3]) && centerX < WIDTH - radius + 9)
            dx += 1;
        if (input.contains(movementKeys[4]) && CooldownTimeLeft <= 0) {
            speedBoostActive = true;
            CooldownTimeLeft = boostCooldown;
            boostTimeLeft = boostDuration;
            speed = boostspeed;
        }

        if (CooldownTimeLeft > 0)
            CooldownTimeLeft -= dt;
        if (speedBoostActive) {
            trail.add(new TrailPoint(centerX, centerY));
            if (trail.size() > TRAIL_LENGTH)
                trail.poll();

            boostTimeLeft -= dt;
            if (boostTimeLeft <= 0) {
                speed = baseSpeed;
                speedBoostActive = false;
            }
        } else
            trail.clear();

        

        if (dx != 0 && dy != 0) {

            dx /= Math.sqrt(2);
            dy /= Math.sqrt(2);
        }
        
        //mud effect
        if(intersectsMud){
            speedMultiplier = slipSpeed;
            slowDownTimeLeft = slowDownDuration;

        }
        if(slowDownTimeLeft>0){
            slowDownTimeLeft-=dt;
        }
        else 
            speedMultiplier = 1;

        centerX += dx * speed * dt*speedMultiplier;
        centerY += dy * speed * dt*speedMultiplier;

        if(hasTorch){
            torchHoldTime+=dt;
        }

    }
    public boolean intersectWithBarrier(Barrier barrier){
        return centerX+radius-10>barrier.x &&
               centerX-radius+10<barrier.x+barrier.width &&
               centerY+radius-10>barrier.y &&
               centerY-radius+10<barrier.y+barrier.height;
    }
    public boolean intersectWithMud(Mud mud){
        return centerX+radius-20>mud.x &&
               centerX-radius+20<mud.x+mud.width &&
               centerY+radius-20>mud.y &&
               centerY-radius+20<mud.y+mud.height;
    }

    private static class TrailPoint {
        double x, y;

        TrailPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

}