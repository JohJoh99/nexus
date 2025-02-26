package net.johjoh.nexus.desktop.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.scene.image.Image;

public class WeatherUtil {
	
	//	https://api.brightsky.dev/weather?lat=48.4014&lon=9.9885&date=2025-02-08
	private static String weatherRequest = "https://api.brightsky.dev/weather?lat=%1&lon=%2&date=%3";
	private static JsonNode weather;
	
	private static double minTemp;
	private static double maxTemp;
	
	public static JsonNode getWeather() {
		return weather;
	}
	
	public static double getMinTemp() {
		return minTemp;
	}
	
	public static double getMaxTemp() {
		return maxTemp;
	}
	
	public static boolean refreshWeather(double lat, double lon, LocalDate date) {
		weather = getWeather(getWeatherRequestString(lat, lon, date));
		if(weather == null)
			return false;
		
		for(int i = 0; i < 25; i++) {
			JsonNode w = weather.get(i);
			if(w.path("temperature").asDouble() < minTemp)
				minTemp = w.path("temperature").asDouble();
			if(w.path("temperature").asDouble() > maxTemp)
				maxTemp = w.path("temperature").asDouble();
		}
		
		return true;
	}
	
	private static JsonNode getWeather(String request) {
	    JsonNode weather = null;
	    try {
	        URL url = new URL(request);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");

	        // Überprüfe den HTTP-Statuscode
	        int responseCode = conn.getResponseCode();
	        if (responseCode != HttpURLConnection.HTTP_OK) {
	            // Handle error
	            return null;
	        }

	        InputStream is = conn.getInputStream();
	        ObjectMapper mapper = new ObjectMapper();
	        JsonNode root = mapper.readTree(is);

	        // Überprüfe, ob das "weather"-Feld vorhanden ist
	        if (root.has("weather")) {
	            weather = root.path("weather");
	        }

	        // Schließe den InputStream
	        is.close();
	        conn.disconnect();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	    return weather;
	}
	
	private static String getWeatherRequestString(double lat, double lon, LocalDate date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return weatherRequest.replace("%1", String.valueOf(lat)).replace("%2", String.valueOf(lon)).replace("%3", formatter.format(date));
	}
	
	public static Image getWeatherImage(String description) {
		Image image = null;
		try {
			image = new Image(WeatherUtil.class.getResource("/weathericons/" + description + ".png").toExternalForm());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return image;
	}

}
