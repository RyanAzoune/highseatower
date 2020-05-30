import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Entity {
    protected double posX, posY;
    protected double width, height;
    protected double speedX, speedY;
    protected double accelX, accelY;
    protected Color color;

    /**
     * Mise a jour de la position et de la vitesse de l'entite en fonction du temps et du scroll de l'ecran
     *
     * @param deltaTime: temps ecoule depuis la derniere update
     * @param screenY: position de l'ecran a deltaTime
     */
    public void update(double deltaTime, double screenY) {
        speedX += accelX * deltaTime;
        speedY += accelY * deltaTime;
        posX += speedX * deltaTime;
        posY += speedY * deltaTime ;

        // rebond de l'entite si elle atteint les bordures laterales de la fenetre de jeu avec une vitesse horizontale
        if(posX + width > HighSeaTower.WIDTH || posX < 0) {
            speedX *= -.85; // rend le jeu plus realiste et la meduse plus maniable
        }
        // gardes l'entite dans les bornes de l'ecran
        posX = Math.min(posX, HighSeaTower.WIDTH - width);
        posX = Math.max(posX, 0);
    }

    /**
     * @return la position sur l'axe verticale de l'entite
     */
    public double getPosY() { return this.posY; }

    /**
     * Change la couleur de l'entite
     *
     * @param color: la nouvelle couleur de l'entite
     */
    public void setColor(Color color) { this.color = color; }

    /**
     * @return la couleur de l'entite
     */
    public Color getColor() { return this.color; }

    /**
     * Dessine l'entite en fonction de sa postion sur le canvas
     *
     * @param context: permet de definir ce qu'il faut dessiner sur le canvas
     * @param screenY: position de la fenetre par rapport au canvas
     */
    public abstract void draw(GraphicsContext context, double screenY);

    public void setPosX(double posX) {this.posX = posX;}

    public double getPosX() { return this.posX; }

    public void setPosY(double posY) {this.posY = posY;}

    public void setSpeedX(double speedX) {this.speedX = speedX;}

    public double getSpeedX() { return this.speedX; }

    public void setSpeedY(double speedY) {this.speedY = speedY;}

    public double getSpeedY() { return this.speedY; }

    public void setAccelX(double accelX) { this.accelX = accelX; }

    public double getAccelX() { return this.accelX; }

    public void setAccelY(double accelY) { this.accelY = accelY; }

    public double getAccelY() { return this.accelY; }

    public double getWidth(){ return this.width;}

    public double getHeight() { return this.height; }
}
