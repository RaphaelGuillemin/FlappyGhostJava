import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Classe pour creer et gerer le fantome
 */
public class Fantome extends Affichable {
    private double aY = 500;
    private boolean isDead = false;
    static Fantome fantome;

    /**
     * Constructeur
     *
     * @param posX Position en x
     * @param posY Position en y
     */
    Fantome(double posX, double posY) {
        super.posX = posX;
        super.posY = posY;
        super.rayon = 30;
        vX = vitesseInitX;
        this.color = Color.rgb(0, 0, 0);
        this.img = new Image("img/ghost.png", this.rayon * 2, this.rayon * 2, false, false);
    }

    /**
     * Permet de mettre a jour la vitesse du fantome
     * a son positionnement
     *
     * @param deltaTime La difference de temps avec la derniere mise a jour
     */
    @Override
    public void updatePosX(double deltaTime) {
        vY += deltaTime * aY;
        vY = Math.min(300, vY);
        posY += deltaTime * vY;
        if (posY + this.rayon > Controller.getHeight() - 40) {
            vY = -300;
            Controller.ajouterAspirateur();
        }
        if (posY - this.rayon < 0) {
            vY = -vY;
            Controller.ajouterNuage();
        }
        posY = Math.min(posY, Controller.getHeight() - 40 - this.rayon);
        posY = Math.max(posY, this.rayon);
    }

    /**
     * Getter - Indique si le fantome est "mort" ou non
     *
     * @return true si le fantome est entre en collision avec un obstacle
     */
    boolean getIsDead() {
        return isDead;
    }

    /**
     * Setter pour mettre le fantome comme "mort"
     *
     * @param isDead
     */
    void setIsDead(boolean isDead) {
        this.isDead = isDead;
    }

    /**
     * Setter pour l'acceleration verticale du fantome
     *
     * @param ay Acceleration en y
     */
    void setAY(double ay) {
        this.aY = ay;
    }

    /**
     * Getter pour l'acceleration verticale du fantome
     *
     * @return Acceleration en Y
     */
    double getAY() {
        return this.aY;
    }


}
