import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Meduse extends Entity {
    private Image[] rightFrames, leftFrames;
    private Image image;
    private double frameRate = 8; // 8 FPS
    private double tempsTotal = 0; // pour animation de la meduse

    private boolean onPlatforme, goLeft, goRight, accelerate;
    private int peak; // hauteur maximale atteinte

    /**
     * Construit une meduse
     *
     * @param posX: position initiale de la meduse sur l'axe horizontal
     * @param posY: position initiale de la meduse sur l'axe vertical
     */
    public Meduse(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
        this.width = 50;
        this.height = 50;
        this.accelY = 1200;

        rightFrames = new Image[]{
                new Image("/jellyfish1.png"),
                new Image("/jellyfish2.png"),
                new Image("/jellyfish3.png"),
                new Image("/jellyfish4.png"),
                new Image("/jellyfish5.png"),
                new Image("/jellyfish6.png"),
        };
        leftFrames = new Image[]{
                new Image("/jellyfish1g.png"),
                new Image("/jellyfish2g.png"),
                new Image("/jellyfish3g.png"),
                new Image("/jellyfish4g.png"),
                new Image("/jellyfish5g.png"),
                new Image("/jellyfish6g.png")
        };
        image = rightFrames[0];
    }

    /**
     * Mise a jour de la position et de la vitesse de la meduse en fonction du temps, du scroll de l'ecran
     * et de l'action qu'elle est entrain d'effectuer
     *
     * @param deltaTime: temps ecoule depuis la derniere update
     * @param screenY: position de l'ecran a deltaTime
     */
    @Override
    public void update(double deltaTime, double screenY) {
        if (goLeft) {
            accelX = -1200;
        } else if (goRight) {
            accelX = 1200;
        } else {
            accelX = 0;
            if(speedX != 0) { // afin que la meduse puisse s'arreter
                speedX *= .99;
            }
        }
        super.update(deltaTime, screenY);

        peak = (int) Math.max(HighSeaTower.HEIGHT - (posY + height), peak);

        tempsTotal += deltaTime;
        int frame = (int)(tempsTotal * frameRate);

        if(speedX >= 0) {
            image = rightFrames[frame % rightFrames.length];
        } else {
            image = leftFrames[frame % leftFrames.length];
        }
    }

    /**
     * Definie l'interaction de la meduse avec other, une plateforme, en fonction du type de cette derniere
     * Il a interaction seulement si:
     *      - il y a une intersection entre la meduse et la plateforme
     *      - l'intersection est entre la plateforme et le bas de la meduse uniquement
     *      - la vitesse de la meduse est vers le bas
     *      - si other est une plateforme solide on ajoute les deux cas suivants:
     *          # l'intersection est entre la plateforme et le haut de la meduse
     *          # la vitesse de la meduse est vers le haut
     *
     * @param other: une plateforme quelconque
     */
    public void testCollision(Plateforme other) {
        // verifie si other est une plateforme accelerante
        if(other instanceof PlateformeAccelerante && intersects(other)
                && Math.abs(this.posY + this.height - other.posY) < 10 && this.speedY > 0) {
            this.accelerate = true;
        }
        // verifie si other est une plateforme rebondissante
        if(other instanceof PlateformeRebondissante && intersects(other)
                && Math.abs(this.posY + this.height - other.posY) < 10 && this.speedY >= 0) {
            this.onPlatforme = true;
            this.speedY = Math.min(speedY * (-1.5), -100);
        }
         // verifie si other est une plateforme solide
        if(other instanceof PlateformeSolide && intersects(other)
                && Math.abs(this.posY - (other.posY + other.height)) < 10 && this.speedY < 0) {
            pushOutDown((PlateformeSolide) other);
            this.speedY *= -1;
        }
        // verifie si other est une plateforme simple
        if(intersects(other) && Math.abs(this.posY + this.height - other.posY) < 10 && this.speedY > 0) {
            pushOutUp(other);
            this.speedY = 0;
            this.onPlatforme = true;
        }
    }

    /**
     * Retourne s'il y a une intersection entre la meduse et la plateforme other ou non
     *
     * @param other: une plateforme quelconque
     * @return true si une des Entity est a gauche de l'autre ou l'une est au dessus de l'autre, sinon false
     */
    public boolean intersects(Plateforme other) {
        return !(posX + width < other.posX
                || other.posX + other.width < this.posX
                || posY + height < other.posY
                || other.posY + other.height < this.posY);
    }

    /**
     * Afin que la meduse reste sur le dessus de la plateforme other malgres l'effet de l'acceleration verticale sa
     * position verticale est poussee vers le haut de la plateforme
     *
     * @param other: une plateforme
     */
    public void pushOutUp(Plateforme other) {
        double deltaY = this.posY + this.height - other.posY;
        this.posY -= deltaY;
    }

    /**
     * Afin que la meduse ne puisse pas traverser la plateforme solide sa position verticale est poussee vers le dessous
     * de la plateforme solide other
     *
     * @param other: une plateforme solide
     */
    public void pushOutDown(PlateformeSolide other) {
        double deltaY = this.posY - (other.posY + other.height);
        this.posY += deltaY;
    }

    /**
     * Change la valeur de onPlateforme
     *
     * @param onPlatforme: true si la meduse est sur une plateforme, false sinon
     */
    public void setOnPlatforme(boolean onPlatforme) { this.onPlatforme = onPlatforme; }

    /**
     * @return si la meduse se trouve sur une plateforme, peut importe le type de celle-ci
     */
    public boolean getOnPlateforme() { return this.onPlatforme; }

    /**
     * @return si la meduse se trouve sur une plateforme accelerante
     */
    public boolean getAccelerate() { return this.accelerate; }

    /**
     * Change la valeur variable accelerate
     *
     * @param accelerate: true si la meduse est sur une plateforme accelerante, false sinon
     */
    public void setAccelerate(boolean accelerate) { this.accelerate = accelerate; }

    /**
     * @return la hauteur maximale atteinte par la meduse
     */
    public int getPeak() { return this.peak; }

    /**
     * Indique que la meduse se deplace vers la gauche
     */
    public void left() { this.goLeft = true; }

    /**
     * Indique que la meduse se deplace vers la droite
     */
    public void right() { this.goRight = true; }

    /**
     * Reset les variables goLeft et goRight de la meduse a false
     */
    public void stop() { this.goLeft = false; this.goRight =false; }

    /**
     * Fait sauter la meduse en lui donner une vitesse vers le haut du canvas
     */
    public void jump() {
        if(onPlatforme) {
            speedY = -600;
        }
    }

    /**
     * @see Entity
     */
    @Override
    public void draw(GraphicsContext context, double screenY) {
        context.drawImage(image, posX, posY + screenY, width, width);
        context.setFill(this.color);
        context.fillRect(posX, posY + screenY, width, width);
    }
}
