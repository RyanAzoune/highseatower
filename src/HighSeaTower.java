/**
 * Auteurs: Killian Gervais (20131841) & Ryan Azoune (20162578)
 *
 * Projet:  High Sea Tower
 *          Un jeu de type "platformer" ou le joueur incarne une meduse et cherche a se rendre le plus haut possible
 */

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class HighSeaTower extends Application {
    public static final int WIDTH = 350, HEIGHT = 480;

    public static void main (String[] args) {
        HighSeaTower.launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Pane root = new Pane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);

        GraphicsContext context = canvas.getGraphicsContext2D();

        Controleur controleur = new Controleur();

        scene.setOnKeyPressed((value) -> {
            switch (value.getCode()) {
                case SPACE:
                case UP:
                    controleur.jump();
                    break;
                case LEFT:
                    controleur.left();
                    break;
                case RIGHT:
                    controleur.right();
                    break;
                case ESCAPE:
                    controleur.escape();
                    break;
                case T:
                    controleur.debug();
                    break;
            }
        });

        scene.setOnKeyReleased((value) -> {
            if(value.getCode() == KeyCode.LEFT || value.getCode() == KeyCode.RIGHT) {
                    controleur.stop();
            }
        });

        AnimationTimer timer = new AnimationTimer() {
            private long lastTime = 0;
            private final double MAXDT = 0.02;

            @Override
            public void handle(long now) {
                if(lastTime == 0) {
                    lastTime = now;
                    return;
                }

                double deltaTime = (now - lastTime) * 1e-9;

                if(deltaTime > MAXDT) {
                    deltaTime = MAXDT;
                }

                context.clearRect(0, 0, WIDTH, HEIGHT);
                controleur.update(deltaTime);
                controleur.draw(context);

                lastTime = now;
            }
        };
        timer.start();

        stage.setScene(scene);
        stage.setTitle("High Sea Tower");
        stage.getIcons().add(new Image("/jellyfish1.png"));
        stage.setResizable(false);
        stage.show();
    }
}