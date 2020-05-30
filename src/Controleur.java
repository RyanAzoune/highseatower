import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;

import static javafx.application.Platform.exit;

public class Controleur {
    Game game;
    private boolean restart;

    public Controleur() {
        game = new Game();
    }

    void draw(GraphicsContext context) { game.draw(context); }

    void update(double deltaTime) {
        restart();
        if (restart) {
            game = new Game();
            restart = false;
        } else {
            game.update(deltaTime);
        }
    }

    public void left() { game.left();}

    public void right(){ game.right();}

    public void stop(){ game.stop(); }

    public void jump() { game.jump(); }

    public void escape() { Platform.exit(); }

    public void debug() { game.debug(); }

    public void restart() { this.restart = game.restart(); }
}
