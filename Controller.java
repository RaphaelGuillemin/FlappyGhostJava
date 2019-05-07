import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.util.ArrayList;

/**
 * Classe qui sert de lien entre la classe vue et les classes de type modele
 */
class Controller {
  static void commencer(String[] args){
    Application.launch(Vue.class, args);
  }
    private static double temps = 0;
    private static boolean pause = false;
    private static int score = 0;
    private static boolean debug = false;
    private static String string;
    private static int highScore;
    private static final int width = Vue.getWidth(), height = Vue.getHeight();
    private static long lastTime = 0;
    private static double deltaTime;

    /**
     * Fonction de gestion de temps
     *
     * @param primaryStage L'affichage principal
     */
    static void timer(Stage primaryStage) {
        AnimationTimer timer = new AnimationTimer() {
            /**
             * Implementation d'un AnimationTimer qui ajuste l'affichage a une certaine frequence
             * @param now
             */
            @Override
            public void handle(long now) {
            // Gestion du temps
            if (lastTime == 0 || pause || primaryStage.isIconified()) {
                if (lastTime == 0) {
                    initialise();
                }
                lastTime = now;
                return;
            }
            //
            deltaTime = (now - lastTime) * 1e-9;
            // Correction de 2/3 car il manque environ 40 frames, repartis sur 60 (BASE SUR 60FPS)
            double correction = (double) 2 / 3;
            temps += 1 + correction;

            // Efface le canevas
            Vue.getContext().clearRect(0, 0, width, height - 40);

            // Met a jour les objets dans le canevas (obstacles, fantome)
            tour(deltaTime);

            // Change le score sur la barre de commandes
            Vue.setTextButton("Score : " + score + "  Highscore : " + highScore);
            lastTime = now;
            }
        };

        // On commence l'animation de la zone d'affichage
        timer.start();

        // Quand on clique sur pause, pause le jeu
        Vue.getButtonPause().setOnAction((event) -> boutonPause());

        // Quand on coche debug, debug mode
        Vue.getDebug().setOnAction((event) -> boutonDebug());

        Vue.getScene().setOnKeyPressed(Controller::toucheAppuyee);

        Platform.runLater(() -> Vue.getCanvas().requestFocus());

        /* Lorsqu’on clique ailleurs sur la scène,
        le focus retourne sur le canvas */
        Vue.getScene().setOnMouseClicked((event) -> Vue.getCanvas().requestFocus());

        // Pour afficher le tout
        primaryStage.setScene(Vue.getScene());
        primaryStage.show();
    }

    /**
     * Gere les effets du bouton pause
     */
    private static void boutonPause() {
        if (!pause) {
            pause = true;
            Vue.getButtonPause().setText("Resume");
            Vue.getParallelTransition().pause();
        } else {
            pause = false;
            Vue.getButtonPause().setText("Pause");
            Vue.getParallelTransition().play();
        }
    }

    /**
     * Gere les effets de la case a cocher "debug"
     */
    private static void boutonDebug() {
        debug = !debug;
        Vue.getContext().clearRect(0, 0, Vue.getWidth(), Vue.getHeight());
        for (int i = 0; i < Obstacle.obstacles.size(); i++) {
            Obstacle obstacle = Obstacle.obstacles.get(i);
            Vue.draw(obstacle.getPosX(), obstacle.getPosY(), obstacle.getRayon(), obstacle.getColor(), obstacle.getImg());
        }
        Fantome fantome = Fantome.fantome;
        Vue.draw(fantome.posX, fantome.posY, fantome.rayon, fantome.color, fantome.img);
    }

    /**
     * Effectue certaines actions en fonction des touches appuyees par le joueur
     *
     * @param value Touche appuyee
     */
    private static void toucheAppuyee(KeyEvent value) {
        string += value.getText();
        if (value.getCode() == KeyCode.SPACE) {
            Fantome.fantome.setVY(-300);
        } else if (value.getCode() == KeyCode.ESCAPE) {
            Platform.exit();
        } else if (string.contains("twado")) {
            string = "";
            if (Vue.getCanvasScale() == 1) {
                Vue.setCanvasScale(-1);

            } else {
                Vue.setCanvasScale(1);
            }
        } else if (value.getCode() == KeyCode.E && Affichable.vitesseInitX < 3000) {
            //FACULTATIF accelere le jeu
            Affichable.vitesseInitX += 15;
            Obstacle.accelerer();
        }
    }

    /**
     * Initialise le jeu en placant le fantome et en vidant la liste d'obstacles
     */
    private static void initialise() {
        Fantome.fantome = new Fantome(width / 2, (height - 40) / 2);
        Fantome fantome = Fantome.fantome;
        Vue.draw(fantome.posX, fantome.posY, fantome.rayon, fantome.color, fantome.img);
        Obstacle.obstacle = null;
        Obstacle.obstacles = new ArrayList<>();
        Affichable.resetNbDepassements();
        Affichable.vitesseInitX = 120;
        score = 0;
        temps = 0;
    }

    /**
     * Deroulement d'un "tour" du jeu, c-a-d une image (a raison de 60 images/seconde)
     *
     * @param deltaTime Le temps depuis la derniere mise a jour
     */
    private static void tour(double deltaTime) {
        // generation des obstacles
        if (temps == 1 + (double) 2 / 3 || Math.round(temps) % 300 == 0) {
            Obstacle obstacle = Obstacle.nouvelObstacle();
            Vue.draw(obstacle.posX, obstacle.posY, obstacle.rayon, obstacle.color, obstacle.img);
        }
        Fantome fantome = Fantome.fantome;

        // deplacement du Fantome
        fantome.updatePosX(deltaTime);

        // gestion des obstacles
        for (int i = 0; i < Obstacle.obstacles.size(); i++) {
            Obstacle obstacle = Obstacle.obstacles.get(i);
            // supprime l'obstacle si hors de l'ecran a gauche
            if (obstacle.getPosX() < Fantome.fantome.getPosX() - 320 - obstacle.getRayon()) {
                enlever(i);
            } else {
                // deplace l'obstacle
                obstacle.updatePosX(deltaTime);
                // verifie si collision ou depassement
                for (int j = 0; j < Obstacle.obstacles.size(); j++) {
                    Obstacle.obstacles.get(j).collision(fantome);
                    Obstacle.obstacles.get(j).depassement(fantome);
                }

                // Affichage de l'obstacle
                Vue.draw(obstacle.posX, obstacle.posY, obstacle.rayon, obstacle.color, obstacle.img);
            }
        }

        // Gestion du score le plus eleve
        if (highScore < score) {
            highScore = score;
        }

        // Vitesse d'arriere-plan
        Vue.getParallelTransition().setRate(getVitesseX() / 120);

        // Affichage du fantome
        Vue.draw(fantome.posX, fantome.posY, fantome.rayon, fantome.color, fantome.img);

        // Partie perdue, on recommence
        if (fantome.getIsDead()) {
            initialise();
        }
    }

    /**
     * Permet de retirer les obstacles de la liste d'obstacles
     *
     * @param i L'index de l'obstacle a enlever
     */
    private static void enlever(int i) {
        Platform.runLater(() -> {
            if (Obstacle.obstacles.size() >= i + 1) {
                Obstacle.obstacles.remove(i);
            }
        });
    }

    /**
     * Permet d'obtenir la vitesse horizontale des obstacles
     *
     * @return Vitesse en x
     */
    static double getVitesseX() {
        return Affichable.vitesseInitX;
    }

    /**
     * Permet de generer des aspirateurs
     */
    static void ajouterAspirateur() {
        if (!contientAspirateur()) {
            Obstacle.obstacle = new Aspirateur(Fantome.fantome.posX + width / 2 + 50, height - 40, 1, 1);
            Obstacle.obstacles.add(Obstacle.obstacle);
        }
    }

    /**
     * Permet de generer des nuages
     */
    static void ajouterNuage() {
        if (!contientNuage()) {
            Obstacle.obstacle = new Nuage(Fantome.fantome.posX + width / 2 + 50, 45, 1, 1);
            Obstacle.obstacles.add(Obstacle.obstacle);
        }
    }

    /**
     * Permet de verifier si le niveau contient un obstacle de type "aspirateur"
     *
     * @return true si le niveau contient un tel obstacle
     */
    private static boolean contientAspirateur() {
        for (int i = 0; i < Obstacle.obstacles.size(); i++) {
            if (Obstacle.obstacles.get(i) instanceof Aspirateur) {
                return true;
            }
        }
        return false;
    }

    /**
     * Permet de verifier si le niveau contient un obstacle de type "nuage"
     *
     * @return true si le niveau contient un tel obstacle
     */
    private static boolean contientNuage() {
        for (int i = 0; i < Obstacle.obstacles.size(); i++) {
            if (Obstacle.obstacles.get(i) instanceof Nuage) {
                return true;
            }
        }
        return false;
    }

    // Getters et setters

    public static double getTemps() {
        return temps;
    }

    public static int getScore() {
        return score;
    }

    public static void setScore(int score) {
        Controller.score = score;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }
}
