import javafx.scene.paint.Color;

public class PlateformeSolide extends Plateforme {

    /**
     *  Construit une plateforme solide.
     *  Une autre entite ne peut pas la traverser depuis le bas mais peut se poser dessus.
     *
     * @param posX: position de la plateforme sur l'axe horizontal
     * @param posY: position de la plateforme sur l'axe vertical
     */
    public PlateformeSolide(double posX, double posY) {
        super(posX, posY);
        this.color = Color.rgb(184, 15, 36);
    }
}
