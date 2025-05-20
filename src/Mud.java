import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Mud  {
    double x,y,width,height;
    String mudimgpath;
    Image mudimg;
    public Mud(double x, double y, double width, double height, String mudimgpath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.mudimgpath = mudimgpath;
        this.mudimg = new Image(mudimgpath);
        
    }
    public void render(GraphicsContext gc){
        gc.drawImage(mudimg,x,y,width,height);
    }
}
