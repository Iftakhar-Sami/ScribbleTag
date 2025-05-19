import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Barrier {
    double x, y,width,height;
    public Barrier(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public void draw(GraphicsContext gc,Color color){
        gc.setFill(color);
        gc.fillRoundRect(x,y,width,height,Math.min(width,height),Math.min(width,height));
    }
}
