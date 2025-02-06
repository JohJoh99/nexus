package net.johjoh.nexus.desktop.panes;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import net.johjoh.nexus.desktop.NexusDesktop;
import net.johjoh.nexus.desktop.util.Settings;

public class SettingsPane extends BorderPane {
	
	private SettingsHeaderContainer settingsHeaderContainer;
	private SettingsCategories settingsCategories;
	private SettingsFooter settingsFooter;
	private ConnectionSettings connectionSettings;
	private boolean login;
	
	public SettingsPane() {
		getStyleClass().add("pane-1");
		setMaxSize(600, 580);
		setMinSize(600, 580);
		
		settingsHeaderContainer = new SettingsHeaderContainer();
		settingsCategories = new SettingsCategories();
		settingsFooter = new SettingsFooter();
		connectionSettings = new ConnectionSettings();
		
		setTop(settingsHeaderContainer);
		setLeft(settingsCategories);
		setBottom(settingsFooter);
	}
	
	public void setLogin(boolean flag) {
		this.login = flag;
		
		if(this.login) {
			openConnectionSettingsFront();
		}
	}
	
	public void openConnectionSettingsFront() {
		settingsHeaderContainer.getSettingsHeader().setCategory("SERVER");
    	settingsHeaderContainer.getSettingsHeader().setCategorySpacer(true);
    	settingsHeaderContainer.getSettingsHeader().setSubCategory("VERBINDUNGSEINSTELLUNGEN");
        settingsHeaderContainer.getStyleClass().add("settings-list-label");
        setCenter(connectionSettings);
	}
	
	public ConnectionSettings getConnectionSettings() { return this.connectionSettings; }
	public boolean getLogin() { return this.login; }
	
	private class SettingsHeaderContainer extends VBox {
		
		private SettingsHeader settingsHeader;
		
		public SettingsHeaderContainer() {
			setAlignment(Pos.TOP_CENTER);
			
			settingsHeader = new SettingsHeader();
			getChildren().add(settingsHeader);
			
			SettingsHeaderUnderline shul = new SettingsHeaderUnderline();
			getChildren().add(shul);
		}
		
		public SettingsHeader getSettingsHeader() {
			return this.settingsHeader;
		}
	}
	
	private class SettingsHeaderUnderline extends Pane {
		
		public SettingsHeaderUnderline() {
			setId("settings-header-underline");
			
			setMaxSize(560, 10);
			setMinSize(560, 10);
		}
	}
	
	private class SettingsHeader extends HBox {
		
		private Label category;
		private Label categorySpacer;
		private Label subCategory;
		
		private SettingsHeader() {
			addNodes();
		}
		
		private void addNodes() {
			setId("settings-header");
			
			category = new Label();
			category.setId("settings-header-category");
			category.getStyleClass().add("settings-header-label");
			getChildren().add(category);
			
			categorySpacer = new Label();
			categorySpacer.setId("settings-header-category-spacer");
			categorySpacer.getStyleClass().add("settings-header-label");
			getChildren().add(categorySpacer);
			
			subCategory = new Label();
			subCategory.setId("settings-header-sub-category");
			subCategory.getStyleClass().add("settings-header-label");
			getChildren().add(subCategory);
			
		}
		
		public void setCategory(String category) {
			this.category.setText(category);
		}
		
		public void setCategorySpacer(boolean visible) {
			if(visible)
				this.categorySpacer.setText(" \\ ");
			else
				this.categorySpacer.setText("");
		}
		
		public void setSubCategory(String subCategory) {
			this.subCategory.setText(subCategory);
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
		        	openConnectionSettingsFront();
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
