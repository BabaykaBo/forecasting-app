package olehmazniev.apps;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import org.json.simple.JSONObject;

/**
 * The {@code WeatherAppGui} class creates the main user interface for the
 * Weather App. This class extends {@link JFrame} and sets up the layout and
 * visual elements that the user interacts with to view weather information.
 *
 * Elements in the GUI include input fields for search, buttons for executing
 * searches, and labels to display weather conditions, temperature, humidity,
 * and wind speed. Images are also used to visually represent weather data.
 *
 * @author Oleh Mazniev
 * @version 1.0
 * @email zaaqq135[at]gmail[dot]com
 */
public class WeatherAppGui extends JFrame {
    
    private static final String DEFAULT_FONT = "Dialog";
    private JSONObject weatherData;
    /**
     * Constructs the main window of the Weather App GUI. Sets the title,
     * default close operation, size, layout, and initializes the GUI components
     * by calling {@link #addGuiComponents()}.
     */
    public WeatherAppGui() {
        super("Weather App");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450, 650);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        addGuiComponents();
    }

    /**
     * Adds GUI components to the frame. This method sets up the visual elements
     * such as the search field, search button, weather images, and text fields
     * for displaying temperature, weather conditions, humidity, and wind speed.
     */
    private void addGuiComponents() {
        // Adding search text field
        JTextField searchTextField = new JTextField();
        searchTextField.setBounds(15, 15, 351, 45);
        searchTextField.setFont(new Font(DEFAULT_FONT, Font.PLAIN, 24));
        add(searchTextField);

        // Adding weather condition image
        JLabel weatherConditionImage = new JLabel(loadImage("src/assets/rain.png"));
        weatherConditionImage.setBounds(0, 125, 450, 217);
        add(weatherConditionImage);

        // Adding temperature display
        JLabel temperatureText = new JLabel("10 C");
        temperatureText.setBounds(0, 350, 450, 54);
        temperatureText.setFont(new Font(DEFAULT_FONT, Font.BOLD, 48));
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperatureText);

        // Adding weather condition description
        JLabel weatherConditionDesc = new JLabel("Cloudy");
        weatherConditionDesc.setBounds(0, 405, 450, 36);
        weatherConditionDesc.setFont(new Font(DEFAULT_FONT, Font.PLAIN, 32));
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherConditionDesc);

        // Adding humidity display
        JLabel humidityImage = new JLabel(loadImage("src/assets/humidity.png"));
        humidityImage.setBounds(15, 500, 74, 66);
        add(humidityImage);

        // Adding humidity text
        JLabel humidityText = new JLabel("<html><b>Humidity</b> 100%</html>");
        humidityText.setBounds(90, 500, 85, 55);
        humidityText.setFont(new Font(DEFAULT_FONT, Font.PLAIN, 16));
        add(humidityText);

        // Adding wind speed display
        JLabel windspeedImage = new JLabel(loadImage("src/assets/windspeed.png"));
        windspeedImage.setBounds(220, 500, 74, 66);
        add(windspeedImage);

        // Adding wind speed text
        JLabel windspeedText = new JLabel("<html><b>Windspeed</b> 15km/h</html>");
        windspeedText.setBounds(310, 500, 85, 55);
        windspeedText.setFont(new Font(DEFAULT_FONT, Font.PLAIN, 16));
        add(windspeedText);
        
        // Adding search button with image
        JButton searchButton = new JButton(loadImage("src/assets/search.png"));
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375, 13, 47, 45);
        
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInput = searchTextField.getText();
                
                if (userInput.replaceAll("\\s", "").length() <= 0) {
                    return;
                }
                
                weatherData = WeatherApp.getWeatherData(userInput);
                
                String weatherCondition = (String) weatherData.get("weather_condition");
                
                
                String imageName = switch(weatherCondition){
                    case "Clear" -> "clear.png";
                    case "Cloudy" -> "cloudy.png";
                    case "Rain" -> "rain.png";
                    case "Snow" -> "snow.png";
                    default -> "question.png";
                };
                
                weatherConditionImage.setIcon(loadImage("src/assets/" + imageName));
                
                double temperature = (double) weatherData.get("temperature");
                temperatureText.setText(temperature + " C");
                
                weatherConditionDesc.setText(weatherCondition);
                
                long humidity = (long) weatherData.get("humidity");
                humidityText.setText("<html><b>Humidity</b> " + humidity + "%</html>");
                
                double windSpeed = (double) weatherData.get("windSpeed");
                windspeedText.setText("<html><b>Windspeed</b> " + windSpeed + "km/h</html>");
                
                
            }
            
        });
        
        add(searchButton);
    }

    /**
     * Loads an image icon from a specified path.
     *
     * @param resourcePath The relative path to the image file.
     * @return An {@link Icon} object containing the image; {@code null} if the
     * image could not be loaded.
     */
    private Icon loadImage(String resourcePath) {
        try {
            BufferedImage image = ImageIO.read(new File(resourcePath));
            return new ImageIcon(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.err.println("Could not find resource!");
        return null;
    }
}
