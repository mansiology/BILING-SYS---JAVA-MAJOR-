import gui.MainApp;
import javax.swing.SwingUtilities;

/**
 * Main.java — Entry point for the Electricity Billing Management System.
 *
 * Uses SwingUtilities.invokeLater() to ensure all GUI creation happens
 * on the Swing Event Dispatch Thread (EDT), which is the correct and
 * thread-safe way to start a Java Swing application.
 *
 * To compile and run:
 *   Mac/Linux:
 *     javac -d out model/*.java exception/*.java datastructure/*.java service/*.java gui/*.java Main.java
 *     java -cp out Main
 *
 *   Windows:
 *     javac -d out model\*.java exception\*.java datastructure\*.java service\*.java gui\*.java Main.java
 *     java -cp out Main
 */
public class Main {
    public static void main(String[] args) {
        // Launch the application on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new MainApp();
        });
    }
}
