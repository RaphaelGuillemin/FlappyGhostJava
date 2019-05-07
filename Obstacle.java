import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Classe abstraite qui permet de creer des obstacles
 */
abstract class Obstacle extends Affichable {
    static ArrayList<Obstacle> obstacles;
    static Obstacle obstacle;

    /**
     * Constructeur
     *
     * @param posX  Position en X
     * @param posY  Position en Y
     * @param rayon Rayon
     * @param num   Identifiant d'image
     */
    Obstacle(double posX, double posY, double rayon, int num) {
        this.posX = posX;
        this.posY = posY;
        this.vX = vitesseInitX;
        this.vY = 0;
        this.rayon = rayon;
        this.ini = posY;
        this.depasse = false;
        this.img = new Image("img/" + num + ".png", this.rayon * 2, this.rayon * 2, false, false);
    }

    /**
     * Permet de creer un nouvel obstacle et de l'ajouter a la liste d'obstacles
     *
     * @return Un objet obstacle
     */
    static Obstacle nouvelObstacle() {
        int random = Affichable.randomRange(0, 2);
        double x = Fantome.fantome.getPosX() + Controller.getWidth() / 2 + 50;
        double y = Affichable.randomRange(30, Controller.getHeight() - 70);
        double taille = Affichable.randomRange(10, 45);
        int num = Affichable.randomRange(0, 26);
        switch (random) {
            case 1:
                Obstacle.obstacle = new Sinus(x, y, taille, num);
                break;
            case 2:
                Obstacle.obstacle = new Quantique(x, y, taille, num);
                break;
            default:
                Obstacle.obstacle = new Simple(x, y, taille, num);
                break;
        }
        Obstacle.obstacles.add(Obstacle.obstacle);
        return obstacle;
    }

    /**
     * Augmente la vitesse en x des obstacles dans la liste d'obstacles
     */
    static void accelerer() {
        for (int i = 0; i < Obstacle.obstacles.size(); i++) {
            Obstacle obstacle = Obstacle.obstacles.get(i);
            if (obstacle.vX != vitesseInitX && obstacle.vX < 3000) {
                obstacle.vX = vitesseInitX;
            }
        }
    }

    /**
     * Gere les collisions entre le fantome et les obstacles
     *
     * @param autre Le fantome en collision avec l'obstacle
     */
    void collision(Fantome autre) {
        double dx = this.posX - autre.posX;
        double dy = this.posY - autre.posY;
        double d2 = dx * dx + dy * dy;

        // Une collision est detectee
        if (d2 < (this.rayon + autre.rayon) * (this.rayon + autre.rayon)) {
            this.setColor(Color.rgb(255, 0, 0));
            if (!Controller.isDebug()) {
                autre.setIsDead(true);
            }
            // Pas de collision
        } else {
            this.setColor(Color.rgb(255, 255, 0));
        }
    }

    /**
     * Gere le depassement des obstacles
     *
     * @param autre Le fantome qui depasse l'obstacle
     */
    void depassement(Fantome autre) {

        if (autre.posX - autre.rayon > this.posX + this.rayon && !this.depasse) {
            nbrDepassements++;
            Controller.setScore(Controller.getScore() + 5);
            this.depasse = true;
            if (nbrDepassements % 2 == 0 && Affichable.vitesseInitX < 3000) {
                autre.setAY(autre.getAY() + augmentationVitesse);
                Affichable.vitesseInitX += augmentationVitesse;
                accelerer();
            }
        }
    }

    // Getters et setters

    public static ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

    public static void setObstacles(ArrayList<Obstacle> obstacles) {
        Obstacle.obstacles = obstacles;
    }

    public static Obstacle getObstacle() {
        return obstacle;
    }

    public static void setObstacle(Obstacle obstacle) {
        Obstacle.obstacle = obstacle;
    }
}
