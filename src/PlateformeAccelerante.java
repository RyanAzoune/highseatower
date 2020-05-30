import javafx.scene.paint.Color;

public class PlateformeAccelerante extends Plateforme {

    /**
     * Construit une plateforme accelerante.
     * Tant qu'une entite se trouve sur ce type de plateforme la vitesse de scroll de l'ecran est multiplie par 3.
     * Possede aussi les memes d'interactions avec une autre entite qu'une plateforme simple.
     *
     * @param posX: position de la plateforme sur l'axe horizontal
     * @param posY: position de la plateforme sur l'axe vertical
     */
    public PlateformeAccelerante(double posX, double posY) {
        super(posX, posY);
        this.color = Color.rgb(230, 221, 58);
    }
}
