import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Torch {
    double height;
    double width;
    double x;
    double y;
    Image image;
    boolean Hastorch=false;
    public Torch(double x, double y, String imagePath) {
        this.x = x;
        this.y = y;
        image = new Image(imagePath);
        this.height = 50.0;
        this.width = 50.0;
    }
    public void render(GraphicsContext gc) {
        if(Hastorch==false)
            gc.drawImage(image, x, y, width, height);
    }
    public void clear(GraphicsContext gc) {
        gc.clearRect(x, y, this.width, this.height);
    }

}
