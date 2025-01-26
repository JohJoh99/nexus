package net.johjoh.nexus.desktop.panes;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.johjoh.nexus.desktop.NexusDesktop;
import net.johjoh.nexus.desktop.util.Settings;

public class SettingsPane extends BorderPane {
	
	private SettingsHeader settingsHeader;
	private SettingsCategories settingsCategories;
	private SettingsFooter settingsFooter;
	private ConnectionSettings connectionSettings;
	
	public SettingsPane() {
		setStyle("-fx-background-color: white;");
		setMaxSize(600, 580);
		setMinSize(600, 580);
		
		settingsHeader = new SettingsHeader();
		settingsCategories = new SettingsCategories();
		settingsFooter = new SettingsFooter();
		connectionSettings = new ConnectionSettings();
		
		setTop(settingsHeader);
		setLeft(settingsCategories);
		setBottom(settingsFooter);
	}
	
	private class SettingsHeader extends HBox {
		
		private Label title;
		
		private SettingsHeader() {
			addNodes();
		}
		
		private void addNodes() {
			title = new Label("Einstellungen");
			getChildren().add(title);
		}
		
		public void setTitle(String newTitle) {
			title.setText(newTitle);
		}
		
	}
	
	private class SettingsFooter extends HBox {
		
		private SettingsFooter() {
			setAlignment(Pos.CENTER);
			addNodes();
		}
		
		private void addNodes() {
			Button done = new Button("Fertig");
			done.setOnMouseReleased(new EventHandler<MouseEvent>() {

		        public void handle(MouseEvent event) {
		            NexusDesktop.getSettingsPane().setVisible(false);
		            NexusDesktop.getSettingsPane().toBack();
		            NexusDesktop.getOverlayPane().setVisible(false);
		        }
		    });
			getChildren().add(done);
		}
		
	}
	
	private class SettingsCategories extends VBox {
		
		private SettingsCategories() {
			addNodes();
		}
		
		private void addNodes() {
			Label clientTitel = new Label("CLIENT");
			getChildren().add(clientTitel);
			Label generalTitel = new Label("ALLGEMEIN");
			getChildren().add(generalTitel);
			Label spacer = new Label("");
			getChildren().add(spacer);
			Label serverTitel = new Label("SERVER");
			getChildren().add(serverTitel);
			Label connectionTitel = new Label("VERBINDUNGSEINSTELLUNGEN");
			connectionTitel.setOnMouseReleased(new EventHandler<MouseEvent>() {

		        public void handle(MouseEvent event) {
		            settingsHeader.setTitle("SERVER / VERBINDUNGSEINSTELLUNGEN");
		            setCenter(connectionSettings);
		        }
		        
		    });
			getChildren().add(connectionTitel);
		}
		
	}
	
	private class ConnectionSettings extends VBox {
		
		private ConnectionSettings() {
			addNodes();
		}
		
		private void addNodes() {
			Label ipTitle = new Label("Server-IP:");
			getChildren().add(ipTitle);
			TextField ipField = new TextField();
			ipField.setPromptText("Server-IP");
			ipField.setText(Settings.getServerIP());
			getChildren().add(ipField);
			
			Label passwordTitle = new Label("Server-Passwort:");
			getChildren().add(passwordTitle);
			TextField passwordField = new TextField();
			passwordField.setPromptText("Passwort");
			passwordField.setText(Settings.getServerPassword());
			getChildren().add(passwordField);
			
			Label portTitle = new Label("Server-Port:");
			getChildren().add(portTitle);
			TextField portField = new TextField();
			portField.setPromptText("Port");
			portField.setText(String.valueOf(Settings.getServerPort()));
			getChildren().add(portField);
		}
		
	}

}
