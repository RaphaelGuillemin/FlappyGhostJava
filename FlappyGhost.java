/*
 * @Author Raphael Guillemin
 * p1202392
 * @Author Pierre Antoine Vaillancourt
 * p0954712
 */

/**
 * Permet de resoudre un probleme d'affichage sur des ecrans de plus de 60 hz
 *

/**
 * Classe principale du programme
 * Note: "multithreaded false" permet de resoudre un bogue d'affichage sur des ecrans
 * de plus de 60 hz
>>>>>>> 512ece8bf84084b9cb274ae310558a4f8c94e4b5
 * Source: Piotr Miller
 * https://stackoverflow.com/questions/45812036/javafx-60-fps-frame-rate-cap-doesnt-work/45827990#45827990
 */
public class FlappyGhost {

    public static void main(String[] args) {
        //must be set before launching the main class
        System.setProperty("quantum.multithreaded", "false");
        Controller.commencer(args);
    }
}