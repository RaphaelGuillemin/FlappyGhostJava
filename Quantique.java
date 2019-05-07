/**
 * Classe qui permet la creation d'obstacles au deplacement simple
 */
public class Quantique extends Obstacle {

    /**
     * Constructeur
     *
     * @param x   Position en x
     * @param y   Position en Y
     * @param r   Rayon
     * @param num Identifiant d'image
     */
    Quantique(double x, double y, double r, int num) {
        super(x, y, r, num);
    }

    /**
     * Permet de donner une trajectoire pseudoaleatoire a l'objet instantie
     *
     * @param dt DeltaTime, l'ecart de temps depuis la derniere mise a jour
     */
    @Override
    public void updatePosX(double dt) {
        this.posX -= dt * this.vX;
        if (Math.round(Controller.getTemps()) % 20 == 0) {
            int randomY = randomRange(-30, 30);
            if (!(this.posY + randomY < 0) && !(this.posY + randomY > Controller.getHeight())) {
                this.posY += randomY;
            }
            int randomX = randomRange(-30, 30);
            if (this.posX + randomX + this.rayon < Fantome.fantome.getPosX() - Fantome.fantome.rayon || randomX < 0) {
                posX += randomX;
            }
        }
    }
}
