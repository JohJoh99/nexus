package net.johjoh.nexus.desktop.panes.login;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.johjoh.nexus.desktop.NexusDesktop;
import net.johjoh.nexus.desktop.util.LoginUtil;

public class LoginPane extends BorderPane {

	private LoginControllPane loginControllPane;
	private LoginCenterPane loginCenterPane;
	private ServerSettingsLoginPane serverSettingsLoginPane;

	public LoginPane() {
		getStyleClass().add("pane-1");

		setMaxSize(400, 640);
		setMinSize(400, 640);

		loginControllPane = new LoginControllPane();
		loginCenterPane = new LoginCenterPane();
		serverSettingsLoginPane = new ServerSettingsLoginPane();

		setTop(loginControllPane);
		setCenter(loginCenterPane);
		setBottom(serverSettingsLoginPane);
	}
	
	public LoginCenterPane getLoginCenterPane() {
		return this.loginCenterPane;
	}
	
	public ServerSettingsLoginPane getServerSettingsLoginPane() {
		return this.serverSettingsLoginPane;
	}

	public class LoginControllPane extends HBox {

		private Button closeButton;

		public LoginControllPane() {

			setAlignment(Pos.CENTER_RIGHT);

			closeButton = new Button("\u2715");
			closeButton.getStyleClass().add("button-1");
			closeButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					System.exit(0);
				}
			});
			getChildren().add(closeButton);

			HBox.setMargin(closeButton, new Insets(10));

		}
	}

	public class LoginCenterPane extends VBox {

		private Label loginLabel;
		private TextField usernameField;
		private PasswordField passwordField;
		private Label loginFailedLabel;
		private Button loginButton;

		public LoginCenterPane() {
			setId("login-center-pane");
			
			setAlignment(Pos.CENTER);

			loginLabel = new Label("Anmelden");
			loginLabel.setId("login-label");
			getChildren().add(loginLabel);

			usernameField = new TextField();
			usernameField.getStyleClass().add("login-text-field");
			usernameField.setPromptText("Benutzername");
			getChildren().add(usernameField);

			passwordField = new PasswordField();
			passwordField.getStyleClass().add("login-text-field");
			passwordField.setPromptText("Passwort");
			getChildren().add(passwordField);
			
			loginFailedLabel = new Label("Falscher Nutzername oder Passwort");
			loginFailedLabel.setId("login-failed-label");
			loginFailedLabel.setVisible(false);
			getChildren().add(loginFailedLabel);

			loginButton = new Button("\u2794");
			loginButton.setId("login-button");
			loginButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
		            NexusDesktop.getLoginPane().setVisible(false);
		            NexusDesktop.getOverlayPane().setVisible(false);
					//LoginUtil.sendLoginRequest(usernameField.getText(), passwordField.getText());
				}
			});
			getChildren().add(loginButton);

		}
		
		public void loginFailed() {
			loginFailedLabel.setVisible(true);
		}

	}

	public class ServerSettingsLoginPane extends HBox {

		private Label registerLabel;
		private Label seperatorLabel;
		private Label serverSettingsLabel;
		private ImageView serverStatusImageView;
		private Image serverStatusImage;

		public ServerSettingsLoginPane() {
			setAlignment(Pos.CENTER);
			
			registerLabel = new Label("Registrieren");
			registerLabel.getStyleClass().add("login-label-button");
			registerLabel.setOnMouseReleased(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
		        	NexusDesktop.getRegisterPane().setVisible(true);
		        	NexusDesktop.getRegisterPane().toFront();
				}
			});
			getChildren().add(registerLabel);
			
			seperatorLabel = new Label("\u2022");
			seperatorLabel.setId("login-seperator");
			getChildren().add(seperatorLabel);
			
			serverSettingsLabel = new Label("Servereinstellungen");
			serverSettingsLabel.getStyleClass().add("login-label-button");
			serverSettingsLabel.setOnMouseReleased(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
		        	NexusDesktop.getSettingsPane().setLogin(true);
		        	NexusDesktop.getSettingsPane().setVisible(true);
		        	NexusDesktop.getSettingsPane().toFront();
				}
			});
			getChildren().add(serverSettingsLabel);

			serverStatusImage = new Image(getClass().getResource("/connecting.png").toExternalForm());
			serverStatusImageView = new ImageView(serverStatusImage);
			serverStatusImageView.setFitHeight(16);
			serverStatusImageView.setFitWidth(16);
			Tooltip tooltip = new Tooltip("Verbindet...");
			Tooltip.install(serverStatusImageView, tooltip);
			getChildren().add(serverStatusImageView);
		}

		public void setConnecting() {
			serverStatusImage = new Image(getClass().getResource("/connecting.png").toExternalForm());
			Tooltip tooltip = new Tooltip("Verbindet...");
			Tooltip.install(serverStatusImageView, tooltip);
		}

		public void setConnected() {
			serverStatusImage = new Image(getClass().getResource("/connected.png").toExternalForm());
			Tooltip tooltip = new Tooltip("Verbunden");
			Tooltip.install(serverStatusImageView, tooltip);
		}

		public void setDisconnected() {
			serverStatusImage = new Image(getClass().getResource("/disconnected.png").toExternalForm());
			Tooltip tooltip = new Tooltip("Verbindung getrennt");
			Tooltip.install(serverStatusImageView, tooltip);
		}
	}

}
