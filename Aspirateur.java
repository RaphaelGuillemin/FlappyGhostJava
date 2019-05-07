import javafx.scene.image.Image;

/**
 * Classe qui permet l'instantiation d'obstacles aspirateur
 */
class Aspirateur extends Obstacle {

    /**
     * Constructeur
     *
     * @param x   Position en x
     * @param y   Position en y
     * @param r   Rayon
     * @param num Identifiant d'image
     */
    Aspirateur(double x, double y, double r, int num) {
        super(x, y, r, num);
        this.rayon = Affichable.randomRange(60, 75);
        this.img = new Image("img/aspirator.png", this.rayon * 2, this.rayon * 2, false, false);
    }
}
