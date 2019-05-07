import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Classe parente des objets a afficher sur le canevas
 */
public class Affichable {
    protected double posX, posY, vX, vY, rayon, ini;
    protected final int augmentationVitesse = 15;
    protected static int vitesseInitX = 120;
    protected static int nbrDepassements = 0;
    protected Color color;
    protected Image img;
    protected boolean depasse;

    /**
     * Permet d'obtenir une valeur aleatoire dans une plage de valeurs specifiee
     * Source: https://stackoverflow.com/questions/7961788/math-random-explanation
     *
     * @param min La plus petite valeur de la plage aleatoire
     * @param max La plus grande valeur de la plage aleatoire
     * @return Une valeur aleatoire
     */
    static int randomRange(int min, int max) {
        int range = (max - min) + 1;
        return (int) (Math.random() * range) + min;
    }

    /**
     * Permet une mise a jour de la position en X
     *
     * @param dt
     */
    public void updatePosX(double dt) {
        posX -= dt * vX;
    }

    /**
     * Sert a reinitialiser le nombre de depassements
     */
    static void resetNbDepassements() {
        nbrDepassements = 0;
    }

    // Getters et setters

    /**
     * Getter pour la position en X
     *
     * @return la position en X
     */
    double getPosX() {
        return posX;
    }

    /**
     * Getter pour la position en Y
     *
     * @return la position en Y
     */
    double getPosY() {
        return posY;
    }

    /**
     * Getter pour le rayon
     *
     * @return le rayon
     */
    double getRayon() {
        return rayon;
    }

    public Color getColor() {
        return color;
    }

    public Image getImg() {
        return img;
    }

    /**
     * Setter pour la vitesse en Y
     *
     * @param vy la nouvelle vitesse verticale
     */
    void setVY(double vy) {
        this.vY = vy;
    }

    /**
     * Setter pour la couleur d'un objet
     *
     * @param color la nouvelle couleur
     */
    void setColor(Color color) {
        this.color = color;
    }
}
