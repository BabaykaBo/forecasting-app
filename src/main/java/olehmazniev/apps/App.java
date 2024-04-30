package olehmazniev.apps;

import javax.swing.SwingUtilities;

/**
 * The {@code App} class serves as the main entry point for the WeatherApp. This
 * class contains the main method which initializes and displays the WeatherApp.
 *
 * @author Oleh Mazniev
 * @version 1.0
 * @email zaaqq135[at]gmail[dot]com
 */
public class App {

    /**
     * Launches the WeatherApp GUI. This is the entry point of the application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
         SwingUtilities.invokeLater(() -> new WeatherAppGui().setVisible(true));
    }
}
