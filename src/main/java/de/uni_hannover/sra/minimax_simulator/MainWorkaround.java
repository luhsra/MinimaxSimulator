package de.uni_hannover.sra.minimax_simulator;

/**
 * Dirty workaround to get JavaFX 11 applications running in a single JAR.
 * This is necessary because the are nor MultiModuleExecutableJARs at the moment.
 *
 * @link https://stackoverflow.com/questions/52569724/javafx-11-create-a-jar-file-with-gradle
 * @link http://openjdk.java.net/projects/jigsaw/spec/issues/#MultiModuleExecutableJARs
 * @author Philipp Rohde
 */
public class MainWorkaround {

    /**
     * Gets the application up and running.
     *
     * @param args
     *          command line arguments
     */
    public static void main(String args[]) {
        Main.main(args);
    }
}
