import java.util.HashSet;
import java.util.Set;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SimpleGame extends Application {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;
    private final Set<String> input = new HashSet<>();
    
    
    private GraphicsContext gc;
    private Player player;
    
    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        
        Pane root = new Pane(canvas);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Simple Game");
        stage.show();

        scene.setOnKeyPressed(e -> input.add(e.getCode().toString()));
        scene.setOnKeyReleased(e -> input.remove(e.getCode().toString()));

        
        player = new Player(100,250,"file:src/resources/green_character.png",new String[]{"W","S","A","D"});
        
        new AnimationTimer() {
            long lastTime = 0;
            
            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }
                
                double deltaTime = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;
                
                update(deltaTime, input);
                render();
            }
        }.start();
    }
    
    
    
    private void update(double dt, Set<String> input) {
        player.update(dt, input);
    }
    
    private void render() {
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        player.render(gc);
        
    }
    
   
    public static void main(String[] args) {
        launch();
    }
}
