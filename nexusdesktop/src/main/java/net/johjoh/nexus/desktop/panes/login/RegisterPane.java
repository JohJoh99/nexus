package net.johjoh.nexus.desktop.panes.login;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.johjoh.nexus.desktop.NexusDesktop;

public class RegisterPane extends BorderPane {
	
	public RegisterPane() {
		getStyleClass().add("pane-1");

		setMaxSize(400, 640);
		setMinSize(400, 640);
		
		setVisible(false);
		
		RegisterControllPane registerControlPane = new RegisterControllPane();
		RegisterCenterPane registerCenterPane = new RegisterCenterPane();
		setTop(registerControlPane);
		setCenter(registerCenterPane);
	}

	public class RegisterControllPane extends HBox {

		private Button closeButton;

		public RegisterControllPane() {

			setAlignment(Pos.CENTER_RIGHT);

			closeButton = new Button("\u2715");
			closeButton.getStyleClass().add("button-1");
			closeButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					NexusDesktop.getRegisterPane().setVisible(false);
				}
			});
			getChildren().add(closeButton);

			HBox.setMargin(closeButton, new Insets(10));

		}
	}
	
	public class RegisterCenterPane extends VBox {
		
		private Label registerLabel;
		private TextField usernameField;
		private PasswordField passwordField;
		private PasswordField repeatPasswordField;
		private Button registerButton;
		
		public RegisterCenterPane() {
			setId("login-center-pane");
			
			setAlignment(Pos.CENTER);

			registerLabel = new Label("Registrieren");
			registerLabel.setId("login-label");
			getChildren().add(registerLabel);

			usernameField = new TextField();
			usernameField.getStyleClass().add("login-text-field");
			usernameField.setPromptText("Benutzername*");
			getChildren().add(usernameField);

			passwordField = new PasswordField();
			passwordField.getStyleClass().add("login-text-field");
			passwordField.setPromptText("Passwort*");
			getChildren().add(passwordField);

			repeatPasswordField = new PasswordField();
			repeatPasswordField.getStyleClass().add("login-text-field");
			repeatPasswordField.setPromptText("Passwort wiederholen*");
			getChildren().add(repeatPasswordField);

			registerButton = new Button("\u2794");
			registerButton.setId("login-button");
			registerButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					
				}
			});
			getChildren().add(registerButton);
		}
		
	}

}
