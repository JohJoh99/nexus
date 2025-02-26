package net.johjoh.nexus.desktop.panes.weather;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.johjoh.nexus.desktop.util.WeatherUtil;

public class WeatherPane extends VBox {
	
	private String place = "Ulm";
	private String day = "Heute";
	private double lat = 48.4014;
	private double lon = 9.9885;
	
	double minTemp = 999;
	double maxTemp = -999;
	double minWind = 9999;
	double maxWind = -9999;
	double maxPrecip = -9999;
	double rainProbability = 0.0;
	int windDirection = 0;
	double currentWindSpeed = 0.0; 
	
	private Label dateLabel;
	private Label weatherTodayLabel;
	private Label currentTempLabel;
	private WHBox precipitationBox;
	private Label currentWindLabel;
	
	public WeatherPane() {
		setId("weather-pane");
		
		addNodes();
	}
	
	private void changeDay(int daysFromToday) {
		LocalDate today = LocalDate.now().plusDays(daysFromToday);
		WeatherUtil.refreshWeather(lat, lon, today);
		JsonNode weather = WeatherUtil.getWeather();
		if(weather == null)
			return;
		
		LocalTime now = LocalTime.now();
		JsonNode currentWeather = weather.get(now.getHour());
		
		windDirection = currentWeather.path("wind_direction").asInt();
		currentWindSpeed = currentWeather.path("wind_speed").asDouble();
		
		minTemp = 999;
		maxTemp = -999;
		minWind = 9999;
		maxWind = -9999;
		maxPrecip = -9999;
		
		ArrayList<Double> rainProbabilityByHour = new ArrayList<Double>();
		
		for(int i = 0; i < 25; i++) {
			JsonNode w = weather.get(i);
			if(w.path("temperature").asDouble() < minTemp)
				minTemp = w.path("temperature").asDouble();
			if(w.path("temperature").asDouble() > maxTemp)
				maxTemp = w.path("temperature").asDouble();
			if(w.path("wind_speed").asDouble() < minWind)
				minWind = w.path("wind_speed").asDouble();
			if(w.path("wind_speed").asDouble() > maxWind)
				maxWind = w.path("wind_speed").asDouble();
			if(w.path("precipitation").asDouble() > maxPrecip)
				maxPrecip = w.path("precipitation").asDouble();
			
			rainProbabilityByHour.add((w.path("precipitation") == null || w.path("precipitation") == null ? 0 : w.path("precipitation").asDouble()));
		}
		double cumulativeProbability = 1.0;
		for (double probability : rainProbabilityByHour) {
            cumulativeProbability *= (1 - probability / 100.0);
        }
        rainProbability = Math.round((1 - cumulativeProbability) * 100);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		dateLabel.setText(formatter.format(today));
		
		weatherTodayLabel.setText(day + ": " + maxTemp + "°/" + minTemp + "°");
		currentTempLabel.setText(currentWeather.path("temperature").asDouble() + "°");
		precipitationBox.setValue(rainProbability + "% \u00B7 bis " + maxPrecip + "mm");
		currentWindLabel.setText("Wind: " + (daysFromToday == 0 ? getDirectionFromDegree(windDirection) : "-") + " bis " + (daysFromToday == 0 ? currentWindSpeed : "-") + " km/h");
		
	}
	
	private void addNodes() {
		LocalDate today = LocalDate.now();
		WeatherUtil.refreshWeather(lat, lon, today);
		JsonNode weather = WeatherUtil.getWeather();
		if(weather == null)
			return;
		
		LocalTime now = LocalTime.now();
		JsonNode currentWeather = weather.get(now.getHour());
		
		windDirection = currentWeather.path("wind_direction").asInt();
		currentWindSpeed = currentWeather.path("wind_speed").asDouble();
		
		ArrayList<Double> rainProbabilityByHour = new ArrayList<Double>();
		
		for(int i = 0; i < 25; i++) {
			JsonNode w = weather.get(i);
			if(w.path("temperature").asDouble() < minTemp)
				minTemp = w.path("temperature").asDouble();
			if(w.path("temperature").asDouble() > maxTemp)
				maxTemp = w.path("temperature").asDouble();
			if(w.path("wind_speed").asDouble() < minWind)
				minWind = w.path("wind_speed").asDouble();
			if(w.path("wind_speed").asDouble() > maxWind)
				maxWind = w.path("wind_speed").asDouble();
			if(w.path("precipitation").asDouble() > maxPrecip)
				maxPrecip = w.path("precipitation").asDouble();
			
			rainProbabilityByHour.add((w.path("precipitation") == null || w.path("precipitation") == null ? 0 : w.path("precipitation").asDouble()));
		}
		
		double cumulativeProbability = 1.0;
		for (double probability : rainProbabilityByHour) {
            cumulativeProbability *= (1 - probability / 100.0);
        }
        rainProbability = Math.round((1 - cumulativeProbability) * 100);
		
		Label placeLabel = new Label(place);
		placeLabel.setId("place-label");

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		dateLabel = new Label(formatter.format(today));
		
		HBox daySelection = new HBox();
		daySelection.setId("day-selection");
		Button todayButton = new Button("Heute");
		todayButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				changeDay(0);
			}
		});
		todayButton.getStyleClass().add("date-button");
		Button tomorrowButton = new Button("Morgen");
		tomorrowButton.getStyleClass().add("date-button");
		tomorrowButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				changeDay(1);
			}
		});
		Button datomorrowButton = new Button("Übermorgen");
		datomorrowButton.getStyleClass().add("date-button");
		datomorrowButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				changeDay(2);
			}
		});
		daySelection.getChildren().addAll(todayButton, tomorrowButton, datomorrowButton);
		
		HBox hBox1 = new HBox();
		
		//	links
		VBox vBox1 = new VBox();
		
		HBox hBox2 = new HBox();
		
		VBox vBox2 = new VBox();
		weatherTodayLabel = new Label(day + ": " + maxTemp + "°/" + minTemp + "°");
		
		HBox hBox3 = new HBox();
		currentTempLabel = new Label(currentWeather.path("temperature").asDouble() + "°");
		ImageView currentWeatherImage = new ImageView();
		
		VBox vBox3 = new VBox();
		Label currentWeatherDescriptionLabel = new Label(currentWeather.path("condition").asText());
		currentWindLabel = new Label("Wind: " + getDirectionFromDegree(windDirection) + " bis " + currentWindSpeed + " km/h");
		Label currentPrecipitationLabel = new Label("Niederschlag %1% \u00B7 bis %2mm"); 
		
		//	rechts
		VBox vBox4 = new VBox();
		
		HBox hBox4 = new HBox();
		Label dayLabel = new Label(day);
		ImageView todayWeatherImage = new ImageView();
		Label todayTempLabel = new Label(maxTemp + "°C " + minTemp + "°C");
		hBox4.getChildren().addAll(dayLabel, todayWeatherImage, todayTempLabel);
		
		precipitationBox = new WHBox("Precipitation", "Niederschlag", rainProbability + "% \u00B7 bis " + maxPrecip + "mm");
		WHBox windBox = new WHBox("Wind", "Wind", minWind + " bis " + maxWind + " km/h");
		WHBox sunriseBox = new WHBox("Sunrise", "Sonnenaufgang", "%1 Uhr");
		WHBox sunsetBox = new WHBox("Sunset", "Sonnenuntergang", "%1 Uhr");
		WHBox sunhoursBox = new WHBox("Sunhours", "Sonnenstunden", "%1h");
		WHBox humidityBox = new WHBox("Humidity", "Luftfeuchtigkeit", "%1%");
		WHBox airpressureBox = new WHBox("Airpressure", "Luftdruck", "%1 mbar");
		
		hBox1.getChildren().addAll(vBox1, vBox4);
		vBox1.getChildren().addAll(hBox2);
		hBox2.getChildren().addAll(vBox2, vBox3);
		vBox2.getChildren().addAll(weatherTodayLabel, hBox3);
		hBox3.getChildren().addAll(currentTempLabel, currentWeatherImage);
		vBox3.getChildren().addAll(currentWeatherDescriptionLabel, currentWindLabel, currentPrecipitationLabel);
		vBox4.getChildren().addAll(hBox4, precipitationBox, windBox, sunriseBox, sunsetBox, sunhoursBox, humidityBox, airpressureBox);
		
		getChildren().addAll(placeLabel, dateLabel, daySelection, hBox1);
		
		//Label l = new Label();
		//l.setText(getWeather(getWeatherRequestString(48.4014, 9.9885, new Date())));
		//setCenter(l);
	}
	
	private class WHBox extends HBox {
		
		private Label valueLabel;
		
		public WHBox(String icon, String title, String value) {
	        Image image = new Image(getClass().getResource("/weather/" + icon + ".png").toExternalForm());
	        ImageView imageView = new ImageView(image);
	        imageView.setFitHeight(14);
	        imageView.setFitWidth(14);
	        
	        Label titleLabel = new Label(title);
	        
	        valueLabel = new Label(value);
			
	        getChildren().addAll(imageView, titleLabel, valueLabel);
		}
		
		public void setValue(String value) {
			valueLabel.setText(value);
		}
	}
	
	private String getDirectionFromDegree(int degrees) {
        if (degrees >= 337.5 || degrees < 22.5) {
            return "N";
        } else if (degrees >= 22.5 && degrees < 67.5) {
            return "NO";
        } else if (degrees >= 67.5 && degrees < 112.5) {
            return "O";
        } else if (degrees >= 112.5 && degrees < 157.5) {
            return "SO";
        } else if (degrees >= 157.5 && degrees < 202.5) {
            return "S";
        } else if (degrees >= 202.5 && degrees < 247.5) {
            return "SW";
        } else if (degrees >= 247.5 && degrees < 292.5) {
            return "W";
        } else if (degrees >= 292.5 && degrees < 337.5) {
            return "NW";
        } else {
            return "Ungültig";
        }
	}

}
