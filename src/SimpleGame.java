import java.util.HashSet;
import java.util.Set;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SimpleGame extends Application {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;
    private final Set<String> input = new HashSet<>();

    private GraphicsContext gc,menuGc;
    private Player player;
    private Player player2;
    private Torch torch;
    private boolean gameOver = false;
    private double collisioncooldown = 0;
    private double radius = 10;

    private boolean wascolliding = false;

    @Override
    public void start(Stage stage) {
        Pane menuPane = new Pane();
        menuPane.setPrefSize(WIDTH, HEIGHT);
        Canvas menucanvas= new Canvas(WIDTH, HEIGHT);
        menuGc = menucanvas.getGraphicsContext2D();

        Font font = Font.loadFont("file:src/resources/SuperFoods-2OxXo.ttf", 60);
        Text menuTitle = new Text("Scribble Tag");
        menuTitle.setFont(font);;
        menuTitle.setLayoutX(WIDTH / 2 - 170);
        menuTitle.setLayoutY(HEIGHT / 2 - 100);
        menuTitle.setFill(Color.valueOf("#4a4a4a"));
        
        menuGc.setFill(Color.valueOf("#37d88c"));
        menuGc.fillOval(-300,HEIGHT/2+80,1000,1000);
        menuGc.setFill(Color.valueOf("#9178ff"));
        menuGc.fillOval(300,-830,1000,1000);

        font = Font.loadFont("file:src/resources/SuperFoods-2OxXo.ttf", 40);

        Text startTxt = new Text("START");
        startTxt.setLayoutX(WIDTH / 2 - 50);
        startTxt.setLayoutY(HEIGHT / 2 + 40);
        startTxt.setFill(Color.GRAY);
        startTxt.setFont(font);

        Text exitTxt = new Text("EXIT");
        exitTxt.setLayoutX(WIDTH / 2 - 40);
        exitTxt.setLayoutY(HEIGHT / 2 + 90);
        exitTxt.setFill(Color.GRAY);
        exitTxt.setFont(font);

        

        menuPane.getChildren().addAll(menucanvas,menuTitle, startTxt, exitTxt);
        Scene menuScene = new Scene(menuPane);

        stage.setScene(menuScene);
        stage.setTitle("Scribble Tag");
        stage.getIcons().add(new Image("file:src/resources/green_character.png"));
        stage.show();

        startTxt.setOnMouseEntered(event -> startTxt.setFill(Color.valueOf("#9178ff")));
        startTxt.setOnMouseExited(event -> startTxt.setFill(Color.GRAY));
        exitTxt.setOnMouseEntered(event -> exitTxt.setFill(Color.valueOf("#4a4a4a")));
        exitTxt.setOnMouseExited(event -> exitTxt.setFill(Color.GRAY));
        exitTxt.setOnMouseClicked(event -> stage.close());
        startTxt.setOnMouseClicked(event -> {

            Canvas canvas = new Canvas(WIDTH, HEIGHT);
            gc = canvas.getGraphicsContext2D();

            Pane root = new Pane(canvas);
            Scene scene = new Scene(root);
            stage.setScene(scene);
           

            scene.setOnKeyPressed(e -> input.add(e.getCode().toString()));
            scene.setOnKeyReleased(e -> input.remove(e.getCode().toString()));

            player = new Player(100, 250, Color.valueOf("#37d88c"), "file:src/resources/green_character.png",
                    "file:src/resources/greenht.png",
                    new String[] { "W", "S", "A", "D", "SPACE" });
            player2 = new Player(900, 250, Color.valueOf("#9178ff"), "file:src/resources/purple_character.png",
                    "file:src/resources/puurpleht.png",
                    new String[] { "UP", "DOWN", "LEFT", "RIGHT", "PERIOD" });

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
                    if (gameOver)
                        gameOver(deltaTime, input);
                    else {
                        update(deltaTime, input);
                        render();
                    }
                }
            }.start();
        });
    }

    private void gameOver(double dt, Set<String> input) {
        double spd = 1500;

        if (player.HasTorch()) {
            gc.setFill(player.color);
            if (radius <= 1200) {

                radius += spd * dt;
                gc.fillOval(35 - radius, HEIGHT - 50 - radius, radius * 2, radius * 2);
            } else {
                gc.setFill(player.color);
                gc.fillRect(0, 0, WIDTH, HEIGHT);
                Font font = Font.loadFont("file:src/resources/SuperFoods-2OxXo.ttf", 60);
                gc.setFont(font);
                gc.setFill(Color.WHITE);
                gc.fillText("Player 1 Wins!", WIDTH / 2 - 200, HEIGHT / 2);
                font = Font.loadFont("file:src/resources/SuperFoods-2OxXo.ttf", 24);
                gc.setFont(font);
                gc.setFill(Color.WHITE);
                gc.fillText("Press R to restart", WIDTH / 2 - 110, HEIGHT - 50);

            }

        } else {
            gc.setFill(player2.color);
            if (radius <= 1200) {

                radius += spd * dt;
                gc.fillOval(WIDTH - 65 - radius, 70 - radius, radius * 2, radius * 2);
            } else {
                gc.setFill(player2.color);
                gc.fillRect(0, 0, WIDTH, HEIGHT);
                Font font = Font.loadFont("file:src/resources/SuperFoods-2OxXo.ttf", 60);
                gc.setFont(font);
                gc.setFill(Color.WHITE);
                gc.fillText("Player 2 Wins!", WIDTH / 2 - 200, HEIGHT / 2);
                font = Font.loadFont("file:src/resources/SuperFoods-2OxXo.ttf", 24);
                gc.setFont(font);
                gc.setFill(Color.WHITE);
                gc.fillText("Press R to restart", WIDTH / 2 - 110, HEIGHT - 50);

            }
        }
        if (input.contains("R")) {
            restart();
        }
    }

    private void restart() {
        gameOver = false;
        collisioncooldown = 0;
        radius = 10;
        player = new Player(100, 250, Color.valueOf("#37d88c"), "file:src/resources/green_character.png",
                "file:src/resources/greenht.png",
                new String[] { "W", "S", "A", "D", "SPACE" });
        player2 = new Player(900, 250, Color.valueOf("#9178ff"), "file:src/resources/purple_character.png",
                "file:src/resources/puurpleht.png",
                new String[] { "UP", "DOWN", "LEFT", "RIGHT", "PERIOD" });
        torch = new Torch(475, 275, "file:src/resources/weapon_staff.png");

    }

    private void update(double dt, Set<String> input) {
        player.update(dt, input);
        player2.update(dt, input);
        if(input.contains("ESCAPE"))
            System.exit(0);

        if (torch.Hastorch == false) {
            if (player.intersectsTorch(torch)) {
                torch.Hastorch = true;
                player.sethasTorch(true);

            } else if (player2.intersectsTorch(torch)) {
                torch.Hastorch = true;
                player2.sethasTorch(true);
            }
        } else {

            boolean iscolliding = player.intersects(player2);

            if (collisioncooldown > 0)
                collisioncooldown -= dt;

            if (iscolliding && !wascolliding) {

                player.sethasTorch(!player.HasTorch());
                player2.sethasTorch(!player2.HasTorch());
                collisioncooldown = 1.5;

            } else if (iscolliding && wascolliding) {

                if (collisioncooldown <= 0) {
                    player.sethasTorch(!player.HasTorch());
                    player2.sethasTorch(!player2.HasTorch());
                    collisioncooldown = 1.5;
                }
            }
            wascolliding = iscolliding;
        }
        if (player.torchHoldTime >= 5 || player2.torchHoldTime >= 5) {
            gameOver = true;
        }

    }

    private void render() {
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        player.render(gc, 25, HEIGHT - 30);
        player2.render(gc, WIDTH - 80, 50);
        torch.render(gc);

    }

    

    public static void main(String[] args) {
        launch();
    }
}
