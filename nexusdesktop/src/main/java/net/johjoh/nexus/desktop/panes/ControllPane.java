package net.johjoh.nexus.desktop.panes;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import net.johjoh.nexus.desktop.NexusDesktop;

public class ControllPane extends BorderPane {
	
	private boolean calendar;
	private boolean lists;
	private boolean weather;
	private boolean family;
	
	private double xOffset;
	private double yOffset;
	
	public ControllPane() {
		calendar = false;
		lists = false;
		weather = false;
		family = false;
		
		setId("control-pane");
		setMinHeight(40);
		setMaxHeight(40);
		
		HBox topRightButtons = new HBox(10);
		topRightButtons.setId("top-right-buttons");
		topRightButtons.setAlignment(Pos.TOP_RIGHT);

		Button close = new Button("\u2715");
        close.setTooltip(new Tooltip("Beenden"));
        close.setPrefSize(35, 20);
        close.setId("control-close-button");
		close.setOnMouseReleased(new EventHandler<MouseEvent>() {
		    public void handle(MouseEvent event) {
		    	System.exit(0);
		    }
		});
		Button settings = new Button("\u26ED");
		settings.setPrefSize(35, 20);
		settings.setId("control-settings-button");
		settings.setOnMouseReleased(new EventHandler<MouseEvent>() {

	        public void handle(MouseEvent event) {
	        	NexusDesktop.getOverlayPane().setVisible(true);
	        	NexusDesktop.getOverlayPane().toFront();
	        	NexusDesktop.getSettingsPane().setLogin(false);
	        	NexusDesktop.getSettingsPane().setVisible(true);
	        	NexusDesktop.getSettingsPane().toFront();
	        }
	    });
		Button minimize = new Button("\u2013");
		minimize.setPrefSize(35, 20);
		minimize.setId("control-minimize-button");
		minimize.setOnMouseReleased(new EventHandler<MouseEvent>() {

	        public void handle(MouseEvent event) {
	            Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
	            stage.setIconified(true);
	        }
	    });

		topRightButtons.getChildren().addAll(minimize, settings, close);
		
		HBox topLeftButtons = new HBox(10);
		topLeftButtons.setId("top-left-buttons");
		topLeftButtons.setAlignment(Pos.CENTER_LEFT);
		
        Image icon = new Image(getClass().getResource("/logo.png").toExternalForm());
        ImageView imageView = new ImageView(icon);
        imageView.setFitHeight(38);
        imageView.setFitWidth(38);
        imageView.setOnMouseReleased(event -> {
        	NexusDesktop.getRootFrame().setCenter(NexusDesktop.getMainMenuPane());
        });
        
        Label calendarButton = new Label("KALENDER");
		calendarButton.getStyleClass().add("menu-list-label");
		
		Label listsButton = new Label("LISTEN");
		listsButton.getStyleClass().add("menu-list-label");
		
		Label weatherButton = new Label("WETTER");
		weatherButton.getStyleClass().add("menu-list-label");
		
		Label familySettingsButton = new Label("FAMILIENVERWALTUNG");
		familySettingsButton.getStyleClass().add("menu-list-label");
		
		calendarButton.setOnMouseReleased(event -> {
			if(calendar)
				return;
			
			if(lists) {
				lists = false;
				listsButton.setBorder(new Border(new BorderStroke(Color.WHITE,
						BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 0, 0))));
			}
			if(weather) {
				weather = false;
				weatherButton.setBorder(new Border(new BorderStroke(Color.WHITE,
						BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 0, 0))));
			}
			if(family) {
				family = false;
				familySettingsButton.setBorder(new Border(new BorderStroke(Color.WHITE,
						BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 0, 0))));
			}
				
			calendar = true;
			calendarButton.setBorder(new Border(new BorderStroke(Color.WHITE,
					BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 2, 0))));
			NexusDesktop.getRootFrame().setCenter(NexusDesktop.getCalendarPane());
		});
		calendarButton.setOnMouseEntered(event -> {
			if(!calendar)
				calendarButton.setBorder(new Border(new BorderStroke(Color.WHITE,
						BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 1, 0))));
		});
		calendarButton.setOnMouseExited(event -> {
			if(!calendar)
				calendarButton.setBorder(new Border(new BorderStroke(Color.WHITE,
						BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 0, 0))));
		});
		
		listsButton.setOnMouseReleased(event -> {
			if(lists)
				return;
			
			if(calendar) {
				calendar = false;
				calendarButton.setBorder(new Border(new BorderStroke(Color.WHITE,
						BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 0, 0))));
			}
			if(weather) {
				weather = false;
				weatherButton.setBorder(new Border(new BorderStroke(Color.WHITE,
						BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 0, 0))));
			}
			if(family) {
				family = false;
				familySettingsButton.setBorder(new Border(new BorderStroke(Color.WHITE,
						BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 0, 0))));
			}
				
			lists = true;
			listsButton.setBorder(new Border(new BorderStroke(Color.WHITE,
					BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 2, 0))));
			NexusDesktop.getRootFrame().setCenter(NexusDesktop.getListPane());
		});
		listsButton.setOnMouseEntered(event -> {
			if(!lists)
				listsButton.setBorder(new Border(new BorderStroke(Color.WHITE,
						BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 1, 0))));
		});
		listsButton.setOnMouseExited(event -> {
			if(!lists)
				listsButton.setBorder(new Border(new BorderStroke(Color.WHITE,
						BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 0, 0))));
		});
		
		weatherButton.setOnMouseReleased(event -> {
			if(weather)
				return;
			
			if(lists) {
				lists = false;
				listsButton.setBorder(new Border(new BorderStroke(Color.WHITE,
						BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 0, 0))));
			}
			if(calendar) {
				calendar = false;
				calendarButton.setBorder(new Border(new BorderStroke(Color.WHITE,
						BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 0, 0))));
			}
			if(family) {
				family = false;
				familySettingsButton.setBorder(new Border(new BorderStroke(Color.WHITE,
						BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 0, 0))));
			}
				
			weather = true;
			weatherButton.setBorder(new Border(new BorderStroke(Color.WHITE,
					BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 2, 0))));
			NexusDesktop.getRootFrame().setCenter(NexusDesktop.getWeatherPane());
		});
		weatherButton.setOnMouseEntered(event -> {
			if(!weather)
				weatherButton.setBorder(new Border(new BorderStroke(Color.WHITE,
						BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 1, 0))));
		});
		weatherButton.setOnMouseExited(event -> {
			if(!weather)
				weatherButton.setBorder(new Border(new BorderStroke(Color.WHITE,
						BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 0, 0))));
		});
		
		familySettingsButton.setOnMouseReleased(event -> {
			if(family)
				return;
			
			if(lists) {
				lists = false;
				listsButton.setBorder(new Border(new BorderStroke(Color.WHITE,
						BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 0, 0))));
			}
			if(weather) {
				weather = false;
				weatherButton.setBorder(new Border(new BorderStroke(Color.WHITE,
						BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 0, 0))));
			}
			if(calendar) {
				calendar = false;
				calendarButton.setBorder(new Border(new BorderStroke(Color.WHITE,
						BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 0, 0))));
			}
				
			family = true;
			familySettingsButton.setBorder(new Border(new BorderStroke(Color.WHITE,
					BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 2, 0))));
			NexusDesktop.getRootFrame().setCenter(NexusDesktop.getCalendarPane());
		});
		familySettingsButton.setOnMouseEntered(event -> {
			if(!family)
				familySettingsButton.setBorder(new Border(new BorderStroke(Color.WHITE,
						BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 1, 0))));
		});
		familySettingsButton.setOnMouseExited(event -> {
			if(!family)
				familySettingsButton.setBorder(new Border(new BorderStroke(Color.WHITE,
						BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 0, 0))));
		});
        
        topLeftButtons.getChildren().addAll(imageView, calendarButton, listsButton, weatherButton, familySettingsButton);

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
