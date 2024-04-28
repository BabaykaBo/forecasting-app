package olehmazniev.apps;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Oleh Mazniev
 */
public class WeatherApp {

    public static JSONObject getWeatherData(String locationName) {
        JSONArray locationData = getLocationData(locationName);

        return null;
    }

    private static JSONArray getLocationData(String locationName) {
        locationName = locationName.replace(" ", "+");


        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" + locationName + "&count=10&language=en&format=json";

        try {
            HttpURLConnection conn = fetchApiResponse(urlString);
       
            

            if (conn != null && 200 != conn.getResponseCode()) {

                System.err.println("Error: Could not connect to API!");
                return null;
            } else {
                StringBuilder resultJson = new StringBuilder();
                try (Scanner scanner = new Scanner(conn.getInputStream())) {

                    while (scanner.hasNext()) {
                        resultJson.append(scanner.nextLine());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                conn.disconnect();

                JSONParser parser = new JSONParser();
                JSONObject resultsJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

                return (JSONArray) resultsJsonObj.get("results");
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static HttpURLConnection fetchApiResponse(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.connect();

            return conn;

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return null;
    }
}
