package net.johjoh.nexus.desktop.panes;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import net.johjoh.nexus.desktop.NexusDesktop;

public class ControllPane extends BorderPane {
	
	private double xOffset;
	private double yOffset;
	
	public ControllPane() {
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
		settings.setOnMouseReleased(new EventHandler<MouseEvent>() {

	        public void handle(MouseEvent event) {
	        	NexusDesktop.getOverlayPane().setVisible(true);
	        	NexusDesktop.getOverlayPane().toFront();
	        	NexusDesktop.getSettingsPane().setLogin(false);
	        	NexusDesktop.getSettingsPane().setVisible(true);
	        	NexusDesktop.getSettingsPane().toFront();
	        }
	    });
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
        	NexusDesktop.getRootFrame().setCenter(NexusDesktop.getMainMenuPane());
        });
        
		Button calendarButton = new Button("Kalender");
		calendarButton.setOnMouseReleased(event -> {
			NexusDesktop.getRootFrame().setCenter(NexusDesktop.getCalendarPane());
		});
		Button listsButton = new Button("Listen");
		Button weatherButton = new Button("Wetter");
		Button familySettingsButton = new Button("Familienverwaltung");
        
        topLeftButtons.getChildren().addAll(imageView, calendarButton, listsButton, weatherButton, familySettingsButton);

		setStyle("-fx-background-color: #111114;");
		setRight(topRightButtons);
        setLeft(topLeftButtons);

		setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

		setOnMouseDragged(event -> {
            NexusDesktop.getPrimaryStageInstance().setX(event.getScreenX() - xOffset);
            NexusDesktop.getPrimaryStageInstance().setY(event.getScreenY() - yOffset);
        });
		
		
	}

}
