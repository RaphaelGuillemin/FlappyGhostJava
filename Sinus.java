/**
 * Classe qui permet la creation d'obstacles au deplacement sinusoidal
 */
public class Sinus extends Obstacle {

    /**
     * Constructeur
     *
     * @param x   Position en x
     * @param y   Position en y
     * @param r   Rayon
     * @param num Identifiant d'image
     */
    Sinus(double x, double y, double r, int num) {
        super(x, y, r, num);
    }

    /**
     * Permet de donner une trajectoire sinusoidale a l'objet instantie
     *
     * @param dt DeltaTime, l'ecart de temps depuis la derniere mise a jour
     */
    @Override
    public void updatePosX(double dt) {
        this.vX = vitesseInitX;
        this.posX -= dt * this.vX;
        this.posY = (50 * Math.sin(this.posX / 25)) + this.ini;
    }
}
