package net.johjoh.nexus.desktop;

import java.util.Properties;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import net.johjoh.nexus.desktop.cloud.NexusDesktopCloudClient;
import net.johjoh.nexus.desktop.panes.ControllPane;
import net.johjoh.nexus.desktop.panes.LoginPane;
import net.johjoh.nexus.desktop.panes.RootFrame;
import net.johjoh.nexus.desktop.panes.SettingsPane;
import net.johjoh.nexus.desktop.panes.calendar.CalendarPane;
import net.johjoh.nexus.desktop.panes.lists.ListPane;
import net.johjoh.nexus.desktop.panes.mainmenu.MainMenuPane;
import net.johjoh.nexus.desktop.panes.weather.WeatherPane;
import net.johjoh.nexus.desktop.util.SSLUtil;
import net.johjoh.nexus.desktop.util.Settings;

public class NexusDesktop extends Application {
	
	private static Application instance;
	private static Stage primaryStageInstance;
	private static NexusDesktopCloudClient cloudClient;
	
	private static MainMenuPane mainMenuPane;
	private static CalendarPane calendarPane;
	private static SettingsPane settingsPane;
	private static ControllPane controllPane;
	private static Pane overlayPane;
	private static RootFrame rootFrame;
	private static LoginPane loginPane;
	private static WeatherPane weatherPane;
	private static ListPane listPane;

	public static void main(String[] args) {
		
		SSLUtil.disableSSLVerification();
		
		Properties props = Settings.loadProperties();
		Settings.loadSettings(props);
		
		cloudClient = new NexusDesktopCloudClient("NEXUSDESKTOP-1337", Settings.getServerIP(), Settings.getServerPassword(), Settings.getServerPort());
		new Thread(cloudClient).start();;
		
		createPanes();
		launch(args);
	}
	
	private static void createPanes() {
		mainMenuPane = new MainMenuPane();
		calendarPane = new CalendarPane();
		settingsPane = new SettingsPane();
		controllPane = new ControllPane();
		overlayPane = new Pane();
		rootFrame = new RootFrame();
		loginPane = new LoginPane();
		weatherPane = new WeatherPane();
		listPane = new ListPane();
	}
	
	public static Application getInstance() { return instance; }
	public static Stage getPrimaryStageInstance() { return primaryStageInstance; }
	
	public static MainMenuPane getMainMenuPane() { return mainMenuPane; }
	public static CalendarPane getCalendarPane() { return calendarPane; }
	public static SettingsPane getSettingsPane() { return settingsPane; }
	public static ControllPane getControllPane() { return controllPane; }
	public static Pane getOverlayPane() { return overlayPane; }
	public static RootFrame getRootFrame() { return rootFrame; }
	public static LoginPane getLoginPane() { return loginPane; }
	public static WeatherPane getWeatherPane() { return weatherPane; }
	public static ListPane getListPane() { return listPane; }

	@SuppressWarnings("static-access")
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		instance = this;
		primaryStageInstance = primaryStage;
		
        //Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        //Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        
        StackPane mainPane = new StackPane();
        getRootFrame().setPrefSize(1280, 720);
        getSettingsPane().setPrefSize(600, 580);
        mainPane.setAlignment(getSettingsPane(), Pos.CENTER);
        getOverlayPane().setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        getOverlayPane().setVisible(false); 
		mainPane.getChildren().addAll(getSettingsPane(), getOverlayPane(), getRootFrame(), getLoginPane());
		
		getSettingsPane().setVisible(false);
		
		getOverlayPane().toFront();
		getRootFrame().toFront();
		

    	getOverlayPane().setVisible(true);
    	getOverlayPane().toFront();
    	getLoginPane().setVisible(true);
    	getLoginPane().toFront();
		
		
		Scene scene = new Scene(mainPane);

		scene.getStylesheets().add(getClass().getResource("/darkmode.css").toExternalForm());
		scene.getStylesheets().add(getClass().getResource("/calendar_dark_mode.css").toExternalForm());
		
		primaryStage.setTitle("Nexus");
		primaryStage.setScene(scene);
		primaryStage.setWidth(1280);
		primaryStage.setHeight(720);
		primaryStage.centerOnScreen();
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.show();
		
		

        primaryStage.setOnCloseRequest((WindowEvent event) -> {
    		if(cloudClient != null) {
    			cloudClient.setAutoReconnect(false);
    			cloudClient.close(true, "Nexus Desktop is being closed!");
    		}
        });
	}
	
	//CalendarView c = new CalendarView();
	//CalendarSource cs = new CalendarSource("Google");
	
	/*c.getCalendars().add(myCalendar);
    GoogleCalendarView googleCalendarView = new GoogleCalendarView();
    googleCalendarView.setClientSecretsFile("path/to/your/client_secret.json");*/
	
}
