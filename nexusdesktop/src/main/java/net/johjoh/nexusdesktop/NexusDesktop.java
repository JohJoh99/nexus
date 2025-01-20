package net.johjoh.nexusdesktop;

import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.CalendarView;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.johjoh.nexusdesktop.panes.CalendarPane;
import net.johjoh.nexusdesktop.panes.MainMenuPane;

public class NexusDesktop extends Application {
	
	private double xOffset;
	private double yOffset;
	
	private static MainMenuPane mainMenuPane;
	private static CalendarPane calendarPane;
	

	public static void main(String[] args) {
		createPanes();
		launch(args);
	}
	
	private static void createPanes() {
		mainMenuPane = new MainMenuPane();
		calendarPane = new CalendarPane();
	}
	
	public static MainMenuPane getMainMenuPane() {
		return mainMenuPane;
	}
	
	public static CalendarPane getCalendarPane() {
		return calendarPane;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
		
		//	Base window
		BorderPane root = new BorderPane();
		
		//	Tab list
		HBox topRightButtons = new HBox(10);
		topRightButtons.setPadding(new Insets(10));
		topRightButtons.setAlignment(Pos.CENTER_RIGHT);
		topRightButtons.setStyle("-fx-background-color: #111114;");

		Button close = new Button("X");
        close.setTooltip(new Tooltip("Beenden"));
    	close.setStyle("-fx-background-color: Red;");
		close.setOnMouseEntered(new EventHandler<MouseEvent>() {
		    public void handle(MouseEvent event) {
		    	close.setStyle("-fx-background-color: MediumSeaGreen;");
		    }
		});
		close.setOnMouseReleased(new EventHandler<MouseEvent>() {
		    public void handle(MouseEvent event) {
		    	System.exit(0);
		    }
		});
		Button settings = new Button("Q");
		Button minimize = new Button("_");
		minimize.setOnMouseReleased(new EventHandler<MouseEvent>() {

	        public void handle(MouseEvent event) {
	            Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
	            stage.setIconified(true);
	        }
	    });

		topRightButtons.getChildren().addAll(minimize, settings, close);
		
		HBox topLeftButtons = new HBox(10);
		
        Image icon = new Image(getClass().getResource("/logo.png").toExternalForm());
        ImageView imageView = new ImageView(icon);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        imageView.setOnMouseReleased(event -> {
        	root.setCenter(getMainMenuPane());
        });
        
		Button calendarButton = new Button("Kalender");
		calendarButton.setOnMouseReleased(event -> {
			root.setCenter(getCalendarPane());
		});
		Button listsButton = new Button("Listen");
		Button weatherButton = new Button("Wetter");
		Button familySettingsButton = new Button("Familienverwaltung");
        
        topLeftButtons.getChildren().addAll(imageView, calendarButton, listsButton, weatherButton, familySettingsButton);

		BorderPane topPane = new BorderPane();
		topPane.setStyle("-fx-background-color: #111114;");
		topPane.setRight(topRightButtons);
        topPane.setLeft(topLeftButtons);

        // Add mouse pressed and dragged event handlers
		topPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

		topPane.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });
		
		//	Filling base window
		//root.getStylesheets().add("https://raw.githubusercontent.com/antoniopelusi/JavaFX-Dark-Theme/main/style.css");
		root.setTop(topPane);
		root.setCenter(getMainMenuPane());
		//root.setCenter(calendarView);
		root.setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

		CalendarView c = new CalendarView();
		CalendarSource cs = new CalendarSource("Google");
		
		c.getCalendars().add(myCalendar);
        GoogleCalendarView googleCalendarView = new GoogleCalendarView();
        googleCalendarView.setClientSecretsFile("path/to/your/client_secret.json");
		
		//	Creating scene
		Scene scene = new Scene(root);
		primaryStage.setTitle("Nexus");
		primaryStage.setScene(scene);
		primaryStage.setWidth(1280);
		primaryStage.setHeight(720);
		primaryStage.centerOnScreen();
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.show();
	}

	/*
	 * import javafx.application.Application; import javafx.geometry.Insets; import
	 * javafx.geometry.Pos; import javafx.scene.Scene; import
	 * javafx.scene.control.Button; import javafx.scene.control.Tab; import
	 * javafx.scene.control.TabPane; import javafx.scene.layout.BorderPane; import
	 * javafx.scene.layout.HBox; import javafx.scene.layout.StackPane; import
	 * javafx.stage.Stage;
	 * 
	 * public class NexusDesktop extends Application {
	 * 
	 * public static void main(String[] args) { launch(args); }
	 * 
	 * @Override public void start(Stage primaryStage) {
	 * 
	 * BorderPane root = new BorderPane();
	 * 
	 * // Szene erstellen Scene scene = new Scene(root, 800, 600);
	 * primaryStage.setTitle("Nexus"); primaryStage.setScene(scene);
	 * primaryStage.show(); }
	 */

	/*
	 * // Rechts oben: Drei Buttons HBox topRightButtons = new HBox(10);
	 * topRightButtons.setPadding(new Insets(10));
	 * topRightButtons.setAlignment(Pos.CENTER_RIGHT);
	 * 
	 * Button button1 = new Button("Button 1"); Button button2 = new
	 * Button("Button 2"); Button button3 = new Button("Button 3");
	 * 
	 * topRightButtons.getChildren().addAll(button1, button2, button3);
	 * 
	 * // Obere Leiste mit Buttons BorderPane topPane = new BorderPane();
	 * topPane.setRight(topRightButtons);
	 * 
	 * // Linke Tabs TabPane leftTabs = new TabPane(); leftTabs.setPrefWidth(150);
	 * leftTabs.getTabs().addAll(new Tab("Left Tab 1", new StackPane(new
	 * Button("Content 1"))), new Tab("Left Tab 2", new StackPane(new
	 * Button("Content 2"))));
	 * 
	 * // Rechte Tabs TabPane rightTabs = new TabPane();
	 * rightTabs.setPrefWidth(150); rightTabs.getTabs().addAll(new
	 * Tab("Right Tab 1", new StackPane(new Button("Content A"))), new
	 * Tab("Right Tab 2", new StackPane(new Button("Content B"))));
	 * 
	 * // Zentraler Bereich TabPane centerTabs = new TabPane();
	 * centerTabs.getTabs().addAll(new Tab("Main Tab 1", new StackPane(new
	 * Button("Main Content 1"))), new Tab("Main Tab 2", new StackPane(new
	 * Button("Main Content 2"))));
	 * 
	 * // Layout zusammenstellen root.setTop(topPane); root.setLeft(leftTabs);
	 * root.setRight(rightTabs); root.setCenter(centerTabs);
	 * 
	 * // Szene erstellen Scene scene = new Scene(root, 800, 600);
	 * primaryStage.setTitle("JavaFX Layout mit Buttons");
	 * primaryStage.setScene(scene); primaryStage.show(); }
	 */
}
