import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Bulle extends Entity {

    public Bulle(double x, double y) {
        this.posX = x;
        this.posY = y;
        this.width = 10 + (int) (31 * Math.random());
        this.speedY = - (int) (350 + 101 * Math.random());
        this.color = Color.rgb(0, 0, 255, 0.4);
    }


    /**
     * @see Entity
     */
    @Override
    public void draw(GraphicsContext context, double yCanvas) {
        context.setFill(color);
        context.fillOval(posX, posY + yCanvas, width, width);
    }
}