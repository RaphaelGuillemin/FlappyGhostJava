import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static javafx.geometry.Pos.CENTER;

import javafx.util.Duration;

/**
 * Classe qui gere l'affichage du jeu
 */

public class Vue extends Application {

    private static final int width = 640, height = 440;
    private static Canvas canvas;
    private static GraphicsContext context;
    private static Scene scene;
    private static CheckBox debug;
    private static Text score;
    private static Button buttonPause;
    private static double duration;
    private static TranslateTransition translateTransition;
    private static TranslateTransition translateTransition2;
    private static ParallelTransition parallelTransition;
    private static StackPane bckgrndEtCanvas;

    /**
     * Cree la fenetre du jeu
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        if (primaryStage == null) {
            throw new Exception();
        }
        creerFenetre(primaryStage);
        Controller.timer(primaryStage);
    }

    /**
     * Permet de creer la fenetre d'affichage
     *
     * @param primaryStage
     */
    private static void creerFenetre(Stage primaryStage) {
        // Creation de la fenetre
        VBox root = new VBox();
        primaryStage.setResizable(false);
        scene = new Scene(root, width, height);
        // Titre de la fenetre
        primaryStage.setTitle("Flappy Ghost");
        // Image dans barre des taches et icone d'application
        primaryStage.getIcons().add(new Image("img/ghost.png"));
        canvas = new Canvas(width, height - 40);
        root.getChildren().add(canvas);

        // Image de fond - Utilise parallelTransition pour donner l'impression que l'arriere-plan
        // defile a l'infini
        Image image = new Image("img/bg.png");
        ImageView bckgrndImage1 = new ImageView(image);
        ImageView bckgrndImage2 = new ImageView(image);
        duration = width / Controller.getVitesseX();

        translateTransition = new TranslateTransition(Duration.seconds(duration), bckgrndImage1);
        translateTransition.setFromX(0);
        translateTransition.setToX(-1 * width);
        translateTransition.setInterpolator(Interpolator.LINEAR);

        translateTransition2 = new TranslateTransition(Duration.seconds(duration), bckgrndImage2);
        translateTransition2.setFromX(0);
        translateTransition2.setToX(-1 * width);
        translateTransition2.setInterpolator(Interpolator.LINEAR);

        parallelTransition = new ParallelTransition(translateTransition, translateTransition2);
        parallelTransition.setCycleCount(Animation.INDEFINITE);
        parallelTransition.play();

        // On met l'arriere-plan defilant dans une HBox
        HBox bckgrnd = new HBox();
        bckgrnd.setMaxWidth(600);
        bckgrnd.getChildren().add(bckgrndImage1);
        bckgrnd.getChildren().add(bckgrndImage2);

        // Stackpane pour combiner l'arriere-plan et le canevas
        bckgrndEtCanvas = new StackPane();
        bckgrndEtCanvas.getChildren().add(bckgrnd);
        bckgrndEtCanvas.getChildren().add(canvas);
        StackPane.setAlignment(canvas, Pos.CENTER_LEFT);
        root.getChildren().add(bckgrndEtCanvas);

        // Separation entre le canevas et la zone commandes
        context = canvas.getGraphicsContext2D();
        Separator separator = new Separator();
        root.getChildren().add(separator);

        // Zone commandes
        HBox commandes = new HBox();
        commandes.setAlignment(CENTER);
        buttonPause = new Button("Pause");
        debug = new CheckBox("Debug");
        Separator separateur = new Separator();
        separateur.setMinHeight(40);
        separateur.setOrientation(Orientation.VERTICAL);
        Separator separateur1 = new Separator();
        separateur1.setMinHeight(40);
        separateur1.setOrientation(Orientation.VERTICAL);
        score = new Text("Score : 0");

        commandes.getChildren().add(buttonPause);
        commandes.getChildren().add(separateur);
        commandes.getChildren().add(debug);
        commandes.getChildren().add(separateur1);
        commandes.getChildren().add(4, score);
        root.getChildren().add(commandes);
    }

    /**
     * Permet de dessiner des images sur le canevas
     *
     * @param posX  Position en x
     * @param posY  Position en y
     * @param rayon Rayon de l'image
     * @param color Couleur de l'image
     * @param img   L'image a afficher
     */
    static void draw(double posX, double posY, double rayon, Color color, Image img) {
        if (Controller.isDebug()) {
            context.setFill(color);
            context.fillOval(
                    posX - rayon,
                    posY - rayon,
                    2 * rayon,
                    2 * rayon);
        } else {
            context.drawImage(img, posX - rayon, posY - rayon);
        }
    }

    // Getters et setters
    static void setTextButton(String str) {
        score.setText(str);
    }

    static void setCanvasScale(int scale) {
        bckgrndEtCanvas.setScaleY(scale);
    }

    static double getCanvasScale() {
        return bckgrndEtCanvas.getScaleY();
    }

    static Canvas getCanvas() {
        return canvas;
    }

    static Scene getScene() {
        return scene;
    }

    static CheckBox getDebug() {
        return debug;
    }

    static double getDuration() {
        return duration;
    }

    static TranslateTransition getTranslateTransition() {
        return translateTransition;
    }

    static TranslateTransition getTranslateTransition2() {
        return translateTransition2;
    }

    static GraphicsContext getContext() {
        return context;
    }

    static Button getButtonPause() {
        return buttonPause;
    }

    static ParallelTransition getParallelTransition() {
        return parallelTransition;
    }

    static int getWidth() {
        return width;
    }

    static int getHeight() {
        return height;
    }

    static void setDuration(double duration) {
        Vue.duration = duration;
    }
}
