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

	public LoginPane() {
		loginControllPane = new LoginControllPane();
		loginCenterPane = new LoginCenterPane();
		
		setTop(loginControllPane);
		setCenter(loginCenterPane);
		//setBottom(loginCenterPane);
	}
	
	public class LoginControllPane extends HBox {

		private Button closeButton;
		
		public LoginControllPane() {

			setAlignment(Pos.CENTER_RIGHT);
			closeButton = new Button("X");
			closeButton.setAlignment(Pos.CENTER_RIGHT);
			closeButton.setTooltip(new Tooltip("Beenden"));
			closeButton.setStyle("-fx-background-color: Red;");
			closeButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					closeButton.setStyle("-fx-background-color: MediumSeaGreen;");
				}
			});
			closeButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					System.exit(0);
				}
			});
			getChildren().add(closeButton);
			
		}
	}

	public class LoginCenterPane extends VBox {

		private Label loginLabel;
		private TextField usernameField;
		private PasswordField passwordField;
		private Button loginButton;
		private ServerSettingsLoginPane serverSettingsLoginPane;

		public LoginCenterPane() {
			setStyle("-fx-border-color: #b8860b; -fx-border-width: 1px; -fx-background-color: linear-gradient(from 0% 100% to 0% 0%, #000040, #000020);");
			setMaxSize(400, 640);
			setMinSize(400, 640);
			setPadding(new Insets(20, 20, 20, 20));
			setAlignment(Pos.CENTER);

			loginLabel = new Label("Anmelden");
			loginLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white; -fx-font-weight: bold;");
			getChildren().add(loginLabel);

			usernameField = new TextField();
			usernameField.setPromptText("Benutzername");
			getChildren().add(usernameField);

			passwordField = new PasswordField();
			passwordField.setPromptText("Passwort");
			getChildren().add(passwordField);

			loginButton = new Button("Anmelden");
			getChildren().add(loginButton);

			serverSettingsLoginPane = new ServerSettingsLoginPane();
			getChildren().add(serverSettingsLoginPane);

		}

		private class ServerSettingsLoginPane extends HBox {

			private Label serverSettingsLabel;
			private ImageView serverStatusImageView;
			private Image serverStatusImage;

			public ServerSettingsLoginPane() {
				serverSettingsLabel = new Label("Servereinstellungen");
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

}
