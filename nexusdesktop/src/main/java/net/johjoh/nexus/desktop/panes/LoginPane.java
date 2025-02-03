package net.johjoh.nexus.desktop.panes;

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

public class LoginPane extends BorderPane {

	private LoginControllPane loginControllPane;
	private LoginCenterPane loginCenterPane;
	private ServerSettingsLoginPane serverSettingsLoginPane;

	public LoginPane() {
		setId("login-pane");

		setMaxSize(400, 640);
		setMinSize(400, 640);

		loginControllPane = new LoginControllPane();
		loginCenterPane = new LoginCenterPane();
		serverSettingsLoginPane = new ServerSettingsLoginPane();

		setTop(loginControllPane);
		setCenter(loginCenterPane);
		setBottom(serverSettingsLoginPane);
	}

	public class LoginControllPane extends HBox {

		private Button closeButton;

		public LoginControllPane() {

			setAlignment(Pos.CENTER_RIGHT);

			closeButton = new Button("\u2715");
			closeButton.setId("login-close-button");
			closeButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					System.exit(0);
				}
			});
			getChildren().add(closeButton);

			HBox.setMargin(closeButton, new Insets(10));

		}
	}

	private class LoginCenterPane extends VBox {

		private Label loginLabel;
		private TextField usernameField;
		private PasswordField passwordField;
		private Button loginButton;

		public LoginCenterPane() {
			setId("login-center-pane");
			
			setAlignment(Pos.CENTER);

			loginLabel = new Label("Anmelden");
			loginLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white; -fx-font-weight: bold;");
			getChildren().add(loginLabel);

			usernameField = new TextField();
			usernameField.getStyleClass().add("login-text-field");
			usernameField.setPromptText("Benutzername");
			getChildren().add(usernameField);

			passwordField = new PasswordField();
			passwordField.getStyleClass().add("login-text-field");
			passwordField.setPromptText("Passwort");
			getChildren().add(passwordField);

			loginButton = new Button("\u2794");
			loginButton.setId("login-button");
			getChildren().add(loginButton);

		}

	}

	private class ServerSettingsLoginPane extends HBox {

		private Label serverSettingsLabel;
		private ImageView serverStatusImageView;
		private Image serverStatusImage;

		public ServerSettingsLoginPane() {
			setAlignment(Pos.CENTER);
			
			serverSettingsLabel = new Label("Servereinstellungen");
			serverSettingsLabel.setId("server-settings-label");
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
