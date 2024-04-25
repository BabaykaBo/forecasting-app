package olehmazniev.apps;

import javax.swing.SwingUtilities;

/**
 * @author Oleh Mazniev
 * @version 1.0
 *
 */
public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new WeatherAppGui().setVisible(true);
            }

        });
    }
}
