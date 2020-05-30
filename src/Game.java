import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private boolean started;
    private boolean reset;

    // pour le scroll de la fenetre
    private double screenY = 0;
    double accelScreen = 2;
    public double speedScreen = 50;
    private double tampon;
    private boolean accelerate; // true si meduse sur plateforme accelerante

    private Meduse meduse;

    private List<Plateforme> plateformeList = new ArrayList<Plateforme>();
    private double yLastPLateforme = HighSeaTower.HEIGHT;

    private List<List<Bulle>> groupesBulles = new ArrayList<>();
    private double totalTime = 0;
    private boolean flag;

    private boolean debugOn = false;
    private String debugText;

    /**
     * Construit une nouvelle instance du jeu
     */
    public Game() {
        plateformeList.add(new Plateforme());
        for(int i = 1; i < 7; i++) { // 7 for good luck
            plateformeList.add(plateformeGenerator());
        }

        meduse = new Meduse(150, 430);
    }

    /**
     * Indique a la meduse qu'elle doit se deplacer a gauche. Commence la partie si elle n'est pas en cours
     */
    public void left() { started = true; meduse.left();  }

    /**
     * Indique a la meduse qu'elle doit se deplacer a droite. Commence la partie si elle n'est pas en cours
     */
    public void right() { started = true; meduse.right(); }

    /**
     * Indique a la meduse qu'elle n'as plus a se deplacer lateralement
     */
    public void stop() { meduse.stop(); }

    /**
     * Indique a la meduse qu'elle doit sauter. Commence la partie si elle n'est pas en cours
     */
    public void jump() { started = true; meduse.jump();}

    /**
     * Definit si l'on debug ou non. debugOn inialiser a false
     */
    public void debug() { debugOn = !debugOn; }

    /**
     * @return reset: true si l'on doit relancer une nouvelle partie, false sinon
     */
    public boolean restart() { return reset; }

    /**
     * Mise a jour de la vitesse et de la position des objets du jeux (bulles, plateformes, meduse, screen????????????????????????)
     *
     * @param deltaTime: temps ecoule depuis la derniere update
     */
    public void update(double deltaTime) {
        meduse.setOnPlatforme(false); // on doit reconfirmer que la meduse est sur une plateforme

        if(meduse.posY > HighSeaTower.HEIGHT - screenY - 1) { // fin de partie (meduse est sortie de l'ecran)
            reset = true;
        } else {
            if(!debugOn && started) { // scroll automatique lorsqu'une partie demarre et que l'on ne debug pas
                speedScreen += accelScreen * deltaTime;
                screenY += speedScreen * deltaTime;
            } else if(!started) { // sinon attend que la partie demare pour commencer l'animation
                deltaTime = 0;
                meduse.setOnPlatforme(true);
            }

            if((int) Math.abs(meduse.posY + meduse.height + screenY) <= 120 && meduse.speedY < 0){
                screenY -= meduse.speedY * deltaTime; // fait scroll l'ecran pour garder la meduse a 75%
            }

            // verifie si la plateforme la plus basse est encore utile, si non la retire et en ajoute une nouvelle
            if((plateformeList.get(0).getPosY() + screenY) > HighSeaTower.HEIGHT + 51) {
                plateformeList.remove(0);
                plateformeList.add(plateformeGenerator());
            }

            for(Plateforme plateforme : plateformeList) { // update l'emplacement des plateformes
                plateforme.update(deltaTime, screenY);
                meduse.testCollision(plateforme);
            }

            meduse.update(deltaTime, screenY); // update l'emplacement de la meduse

            // genere des groupes de bulles toutes les 3 secondes et les groupes inutiles
            if((int) totalTime % 3 == 0 && (int) totalTime > 0) {
                if(!flag) { // empeche que des bulles soient generees tant que (int) totalTime % 3 == 0
                    for(int i = 0; i < 3; i++) {
                        groupesBulles.add(bullesGenerator());
                        groupesBulles.remove(0);
                    }
                    flag = true;
                }
            } else if((int) totalTime % 3 == 1 && (int) totalTime > 0) {
                flag = false;
            } else if(started && totalTime == 0){ // genere le premier groupe de bulles
                for(int i = 0; i < 3; i++) {
                    groupesBulles.add(bullesGenerator());
                }
            }

            for(List<Bulle> listeBulle : groupesBulles) { // update l'emplacement des bulles
                for(Bulle bulle : listeBulle) {
                    bulle.update(deltaTime, screenY);
                }
            }

            totalTime += deltaTime;

            if(meduse.getAccelerate() && !accelerate) { // fait accelerer le scrolling de l'ecran
                accelerate = true;
                tampon = speedScreen;
                speedScreen *= 3;
            }else if(accelerate && !meduse.getOnPlateforme()){
                accelerate = false;
                meduse.setAccelerate(false);
                speedScreen = tampon;
            }

            if (debugOn) { // set le texte a afficher lors du debugging
                debugText = "Position = (" + (int) meduse.posX + ", " + (int)meduse.posY +" "+
                        + Math.round(-(meduse.posY + meduse.height - HighSeaTower.HEIGHT)) + ")\n"
                        + "v = (" + (int) meduse.speedX + ", " + (int) meduse.speedY + ")\n"
                        + "a = (" + (int) meduse.accelX + ", " + (int) meduse.accelY + ")\n"
                        + "Touche le sol : " + (meduse.getOnPlateforme() ? "oui" : "non");
            } else {
                debugText = "";
            }
        }
    }

    /**
     * Cree une nouvelle plateforme.
     * La position horizontale est definie aleatoirement dans les bornes de la fenetre.
     * La position verticale de la plateforme est situe a deltaY au dessus de celle qui la precede.
     * Le type de celle-ci est decide de facon aleatoire selon la probabilite de chaque plateforme:
     *      -  5%   pour une plateforme solide
     *      - 15%   pour une plateforme accelerante
     *      - 35%   pour une plateforme rebondissante
     *      - 45%   pour une plateforme simple
     *
     * @return plateforme: la nouvelle plateforme generee aleatoirement
     */
    public Plateforme plateformeGenerator() {
        Plateforme plateforme;
        double deltaY = -100; // espace entre les plateformes
        yLastPLateforme += deltaY;
        double randomX = HighSeaTower.WIDTH * Math.random();

        int type = (int)(100 * Math.random()) + 1; // definie le type de la plateforme
        /**
         * Empeche d'avoir une plateforme solide apres:
         *      - une autre plateforme solide
         *      - ou une plateforme rebondissante
         * Cela vient affecter la probabilite mais ces 2 cas nuisent a la jouabilite
         */
        if(type <= 5 && !(plateformeList.get(plateformeList.size() - 1) instanceof PlateformeSolide)
                && !(plateformeList.get(plateformeList.size() - 1) instanceof PlateformeRebondissante)) {
            plateforme = new PlateformeSolide(randomX, yLastPLateforme);
        } else if(type <= 15) {
            plateforme = new PlateformeAccelerante(randomX, yLastPLateforme);
        } else if(type <= 35) {
            plateforme = new PlateformeRebondissante(randomX, yLastPLateforme);
        } else {
            plateforme = new Plateforme(randomX, yLastPLateforme);
        }

        return plateforme;
    }

    /**
     * Cree un ArrayList contenant 5 Bulle possedant la meme position horizontale a 20 pixels pres.
     * La position verticale de ces 5 Bulle est initialise en dessous de la partie visible du canvas
     *
     * @return bulles: ArrayList de 5 Bulle
     */
    public List<Bulle> bullesGenerator() {
        double groupeX = (HighSeaTower.WIDTH + 1) * Math.random();
        double deltaX = 20;
        if((int)(2 * Math.random() + 1) == 2){
            deltaX *= -1;
        }

        List<Bulle> bulles = new ArrayList<Bulle>();
        for(int i = 0; i < 5; i++) {
            bulles.add(new Bulle(groupeX + deltaX, 500 - screenY));
        }

        return bulles;
    }

    /**
     * Dessine le fond, les bulles, les plateformes, la meduse et le score actuel sur le canvas
     * ainsi que le texte de debugging (si necessaire)
     *
     * @param context: permet de definir ce qu'il faut dessiner sur le canvas
     */
    public void draw(GraphicsContext context) {
        context.setFill(Color.NAVY);
        context.fillRect(0, 0, HighSeaTower.WIDTH, HighSeaTower.HEIGHT);

        for(List<Bulle> listeBulle : groupesBulles){
            for(Bulle bulle : listeBulle) {
                bulle.draw(context, screenY);
            }
        }

        for(Plateforme p : plateformeList){
            // la plateforme p est jaune lorsque l'on debug et que la meduse est posee dessus
            if(debugOn && meduse.intersects(p) && meduse.getOnPlateforme()){
                Color normalColor = p.getColor();
                p.setColor(Color.YELLOW);
                p.draw(context, screenY);
                p.setColor(normalColor);
            }else {
                p.draw(context, screenY);
            }
        }
        if(debugOn) { // affiche un carre rouge a l'arriere de la meduse, la representant, si l'on debug
            meduse.setColor(Color.rgb(255, 0, 0, 0.3));
        } else {
            meduse.setColor(Color.rgb(0,0,0,0));
        }
        meduse.draw(context, screenY);


        context.setFill(Color.WHITE);
        context.fillText(debugText, 10,10);

        context.setStroke(Color.WHITE);
        context.setTextAlign(TextAlignment.CENTER); // centre l'affichage du score
        context.strokeText(meduse.getPeak() + " m", HighSeaTower.WIDTH / 2, 40);
        context.setTextAlign(TextAlignment.LEFT); // reset alignement pour debugText
    }
}