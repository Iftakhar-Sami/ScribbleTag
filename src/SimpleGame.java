import java.util.HashSet;
import java.util.Set;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SimpleGame extends Application {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;
    private final Set<String> input = new HashSet<>();

    private GraphicsContext gc;
    private Player player;
    private Player player2;
    private Torch torch;
    private double collisioncooldown=0;
    
    private boolean wascolliding=false;
    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();

        Pane root = new Pane(canvas);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Scribble Tag");
        stage.getIcons().add(new Image("file:src/resources/green_character.png"));
        stage.show();

        scene.setOnKeyPressed(e -> input.add(e.getCode().toString()));
        scene.setOnKeyReleased(e -> input.remove(e.getCode().toString()));

        player = new Player(100, 250, "file:src/resources/green_character.png", "file:src/resources/greenht.png",
                new String[] { "W", "S", "A", "D" });
        player2 = new Player(900, 250, "file:src/resources/purple_character.png", "file:src/resources/puurpleht.png",
                new String[] { "UP", "DOWN", "LEFT", "RIGHT" });

        torch = new Torch(475, 275, "file:src/resources/weapon_staff.png");

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
        player2.update(dt, input);
        if (torch.Hastorch == false) {
            if (player.intersectsTorch(torch)) {
                torch.Hastorch = true;
                player.sethasTorch(true);

            } else if (player2.intersectsTorch(torch)) {
                torch.Hastorch = true;
                player2.sethasTorch(true);
            }
        }
        else{

            boolean iscolliding=player.intersects(player2);
    
            if(collisioncooldown>0) collisioncooldown-=dt;
           
            if(iscolliding && !wascolliding){
                
                    player.sethasTorch(!player.HasTorch());
                    player2.sethasTorch(!player2.HasTorch());
                    collisioncooldown=1.5;
                    
            }
            else if(iscolliding && wascolliding){
    
                if ( collisioncooldown<=0) {
                    player.sethasTorch(!player.HasTorch());
                    player2.sethasTorch(!player2.HasTorch());
                    collisioncooldown=1.5;
                }
            }
            wascolliding=iscolliding;
        }

    }

    private void render() {
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        player.render(gc);
        player2.render(gc);
        torch.render(gc);

    }

    public static void main(String[] args) {
        launch();
    }
}
