import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Barrier extends obstacles {
    
    public Barrier(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    @Override
    public void render(GraphicsContext gc){
        gc.setFill(Color.GRAY);
        gc.fillRoundRect(x,y,width,height,Math.min(width,height),Math.min(width,height));
    }
}
