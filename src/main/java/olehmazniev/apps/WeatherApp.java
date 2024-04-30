package olehmazniev.apps;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        
        JSONObject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");
        
        String urlString = "https://api.open-meteo.com/v1/forecast?" +
                "latitude=" + latitude + "&longitude=" + longitude +
                "&hourly=temperature_2m,relative_humidity_2m,"+
                "weather_code,wind_speed_10m&timezone=auto";
        
        try {
            HttpURLConnection conn = fetchApiResponse(urlString);
            if (conn.getResponseCode() != 200){
                System.err.println("Error: Could not connect to API");
                return null;
            }
            
            StringBuilder resultJson = new StringBuilder();
            Scanner scanner = new Scanner(conn.getInputStream());
            
            while(scanner.hasNext()){
                resultJson.append(scanner.nextLine());
            }
            
            conn.disconnect();
            scanner.close();
            
            JSONParser parser = new JSONParser();
            JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));
            JSONObject hourly = (JSONObject) resultJsonObj.get("hourly");
                
            JSONArray time = (JSONArray) hourly.get("time");
            int index = findIndexOfCurrentTime(time);
            
            JSONArray temperatureDate = (JSONArray) hourly.get("temperature_2m");
            double temperature = (double) temperatureDate.get(index);
            
            JSONArray weatherCode = (JSONArray) hourly.get("weather_code");
            String weatherCondition = convertWeatherCode((long) weatherCode.get(index));
            
            JSONArray relativeHumidity = (JSONArray) hourly.get("relative_humidity_2m");
            long humidity = (long) relativeHumidity.get(index);
            
            JSONArray windSpeedData = (JSONArray) hourly.get("wind_speed_10m");
            double windSpeed = (double) windSpeedData.get(index);
            
            JSONObject weatherData = new JSONObject();
            weatherData.put("temperature", temperature);
            weatherData.put("weather_condition", weatherCondition);
            weatherData.put("humidity", humidity);
            weatherData.put("windSpeed", windSpeed);
            
            return weatherData;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
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

    private static int findIndexOfCurrentTime(JSONArray timelist) {
        String currentTime = getCurrentTime();
        
        for (int i = 0; i < timelist.size(); i++) {
            
            String time = (String) timelist.get(i);
            if(time.equalsIgnoreCase(currentTime)) {
                return i;
            }
        }
        
        return 0;
    }
    
    private static String getCurrentTime(){
        LocalDateTime currentDateTime = LocalDateTime.now();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");
        
        return currentDateTime.format(formatter);
    }

    private static String convertWeatherCode(long weatherCode) {
       String weatherCondition = "";
       
       if (weatherCode == 0L) {
           weatherCondition = "Clear";
       } else if (weatherCode >= 0L && weatherCode <= 3L) {
           weatherCondition = "Cloudy";
       } else if ((weatherCode >= 51L && weatherCode <= 67L) || 
               (weatherCode >= 80L && weatherCode <= 99L)) {
           weatherCondition = "Rain";
       } else if (weatherCode >= 71L && weatherCode <= 77L) {
           weatherCondition = "Snow";
       }
       
       return weatherCondition;
    }
}
