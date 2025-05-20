import javafx.scene.canvas.GraphicsContext;


public abstract class obstacles {
    double x,y,width,height;
    abstract void render(GraphicsContext gc);
}
