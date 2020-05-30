import javafx.scene.paint.Color;

public class PlateformeRebondissante extends Plateforme {

    /**
     * Construit une plateforme rebondissante.
     * Une autre entite peut la traverser depuis le bas, mais au lieu de se poser dessus l'entite va rebondir
     *
     * @param posX: position de la plateforme sur l'axe horizontal
     * @param posY: position de la plateforme sur l'axe vertical
     */
    public PlateformeRebondissante (double posX, double posY) {
        super(posX, posY);
        this.color = Color.LIGHTGREEN;
    }
}
