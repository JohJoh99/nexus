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
	private boolean login;
	
	public SettingsPane() {
		getStyleClass().add("pane-1");
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
	
	public void setLogin(boolean flag) { this.login = flag; }
	
	public ConnectionSettings getConnectionSettings() { return this.connectionSettings; }
	public boolean getLogin() { return this.login; }
	
	private class SettingsHeader extends HBox {
		
		private Label title;
		
		private SettingsHeader() {
			addNodes();
		}
		
		private void addNodes() {
			title = new Label("Einstellungen");
			title.setId("settings-header-label");
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
		        	Settings.setServerSettings(NexusDesktop.getSettingsPane().getConnectionSettings().getServerIPField().getText(),
		        			Integer.valueOf(NexusDesktop.getSettingsPane().getConnectionSettings().getServerPortField().getText()),
		        			NexusDesktop.getSettingsPane().getConnectionSettings().getServerPasswordField().getText());
		        	
		            NexusDesktop.getSettingsPane().setVisible(false);
		            NexusDesktop.getSettingsPane().toBack();
		            if(!NexusDesktop.getSettingsPane().getLogin())
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
			clientTitel.getStyleClass().add("settings-list-label");
			getChildren().add(clientTitel);
			Label generalTitel = new Label("ALLGEMEIN");
			generalTitel.getStyleClass().add("settings-list-label");
			getChildren().add(generalTitel);
			Label spacer = new Label("");
			getChildren().add(spacer);
			Label serverTitel = new Label("SERVER");
			serverTitel.getStyleClass().add("settings-list-label");
			getChildren().add(serverTitel);
			Label connectionTitel = new Label("VERBINDUNGSEINSTELLUNGEN");
			connectionTitel.getStyleClass().add("settings-list-label");
			connectionTitel.setOnMouseReleased(new EventHandler<MouseEvent>() {

		        public void handle(MouseEvent event) {
		            settingsHeader.setTitle("SERVER / VERBINDUNGSEINSTELLUNGEN");
		            settingsHeader.getStyleClass().add("settings-list-label");
		            setCenter(connectionSettings);
		        }
		        
		    });
			getChildren().add(connectionTitel);
		}
		
	}
	
	private class ConnectionSettings extends VBox {
		
		private TextField serverIPField;
		private TextField serverPasswordField;
		private TextField serverPortField;
		
		private ConnectionSettings() {
			addNodes();
		}
		
		public TextField getServerIPField() { return this.serverIPField; }
		public TextField getServerPasswordField() { return this.serverPasswordField; }
		public TextField getServerPortField() { return this.serverPortField; }
		
		private void addNodes() {
			Label ipTitle = new Label("Server-IP:");
			getChildren().add(ipTitle);
			serverIPField = new TextField();
			serverIPField.setPromptText("Server-IP");
			serverIPField.setText(Settings.getServerIP());
			getChildren().add(serverIPField);
			
			Label passwordTitle = new Label("Server-Passwort:");
			getChildren().add(passwordTitle);
			serverPasswordField = new TextField();
			serverPasswordField.setPromptText("Passwort");
			serverPasswordField.setText(Settings.getServerPassword());
			getChildren().add(serverPasswordField);
			
			Label portTitle = new Label("Server-Port:");
			getChildren().add(portTitle);
			serverPortField = new TextField();
			serverPortField.setPromptText("Port");
			serverPortField.setText(String.valueOf(Settings.getServerPort()));
			getChildren().add(serverPortField);
		}
		
	}

}
