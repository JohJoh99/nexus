package net.johjoh.nexus.desktop.panes.mainmenu;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.databind.JsonNode;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.johjoh.nexus.desktop.util.WeatherUtil;

public class WeatherFrame extends MenuFrame {
	
	private ImageView weatherIcon;
	private Label temperatureLabel;
	private Label minMaxTempLabel;
	
	public WeatherFrame(double lat, double lon, String place) {
		super("Wetter - " + place, "a", 160);
		
		LocalDate today = LocalDate.now();
		if(!WeatherUtil.refreshWeather(lat, lon, today))
			return;
		JsonNode weather = WeatherUtil.getWeather();
		
		LocalTime now = LocalTime.now();
		JsonNode currentWeather = weather.get(now.getHour());
		
		String currentIcon = currentWeather.path("icon").asText();
		
        weatherIcon = new ImageView(WeatherUtil.getWeatherImage(currentIcon));
        weatherIcon.setFitHeight(64);
        weatherIcon.setFitWidth(64);
        
        temperatureLabel = new Label(currentWeather.path("temperature").asText() + "°C");
        temperatureLabel.setId("temperature-label");
        minMaxTempLabel = new Label("min: " + WeatherUtil.getMinTemp() + "° • max: " + WeatherUtil.getMaxTemp() + "°");
        minMaxTempLabel.setId("minmaxtemp-label");
        
        HBox hBox1 = new HBox();
        hBox1.setPadding(new Insets(10));
        hBox1.setSpacing(10);
        VBox vBox1 = new VBox();
        vBox1.setPadding(new Insets(0, 0, 10, 0));
        
        hBox1.getChildren().addAll(weatherIcon, vBox1);
        vBox1.getChildren().addAll(temperatureLabel, minMaxTempLabel);
        
        this.getChildren().add(hBox1);
		
	}

}
