import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Plateforme extends Entity{

    /**
     * Construit une plateforme simple.
     * Une autre entite peut:
     *      - la traverser si elle provient d'en dessous de la plateforme
     *      - se poser dessus
     *
     * @param posX: position de la plateforme sur l'axe horizontal
     * @param posY: position de la plateforme sur l'axe vertical
     */
    // Plateforme simple
    public Plateforme(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
        this.width = 80 + (int)(96 * Math.random());
        this.height = 10;
        this.color = Color.rgb(230, 134, 58);
    }

    /**
     * Construit la plateforme de depart du jeu "High Sea Tower"
     */
    public Plateforme() {
        this.posX = 0;
        this. posY = HighSeaTower.HEIGHT;
        this.width = HighSeaTower.WIDTH;
        this.height = 10;
    }

    /**
     * @see Entity
     */
    @Override
    public void draw(GraphicsContext context, double screenY) {
        context.setFill(color);
        context.fillRect(posX, posY + screenY, width, height);
    }
}
