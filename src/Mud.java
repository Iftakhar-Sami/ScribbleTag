import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Mud extends Barrier {
    
    String imgpath;
    Image img;

    public Mud(double x, double y, double width, double height, String imgpath) {
        super(x,y,width,height);
        this.imgpath = imgpath;
        this.img = new Image(imgpath);
        
    }

    @Override
    public void render(GraphicsContext gc){
        gc.drawImage(img,x,y,width,height);
    }
}
