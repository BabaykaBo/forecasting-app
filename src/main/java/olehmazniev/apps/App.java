package olehmazniev.apps;

import javax.swing.SwingUtilities;

/**
 * The {@code App} class serves as the main entry point for the WeatherApp. This
 * class contains the main method which initializes and displays the WeatherApp
 * GUI by invoking {@link WeatherAppGui} on the Event Dispatch Thread (EDT) to
 * ensure thread safety in Swing components.
 *
 * @author Oleh Mazniev
 * @version 1.0
 */
public class App {

    /**
     * Launches the WeatherApp GUI. This is the entry point of the application.
     * The {@code main} method schedules the application's GUI for creation and
     * visibility on the Swing Event Dispatch Thread, ensuring that all UI
     * updates are safely executed on the EDT.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new WeatherAppGui().setVisible(true);
        });
    }
}
