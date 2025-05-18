
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
    double boostTimeLeft;
    boolean speedBoostActive = false;
    Color color;
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
        if (hasTorch == true)
            gc.drawImage(himage, centerX + radius * 2 - 192, centerY - radius - 68, 320, 180);


        double progressBarWidth = 30;
        double progressBarHeight = 7;
        double progressBarX = centerX - progressBarWidth / 2;
        double progressBarY = centerY - radius - progressBarHeight+5;

        double progress = 1 - (CooldownTimeLeft / boostCooldown);
        gc.setStroke(Color.valueOf("#4a4a4a")); 
        gc.setLineWidth(2); 
        gc.strokeRoundRect(progressBarX, progressBarY, progressBarWidth, progressBarHeight, 7,7);

        // Draw blue fill
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

        centerX += dx * speed * dt;
        centerY += dy * speed * dt;

        if(hasTorch && torchHoldTime<=60){
            torchHoldTime+=dt;
        }

    }

    private static class TrailPoint {
        double x, y;

        TrailPoint(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}