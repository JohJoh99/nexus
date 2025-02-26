package net.johjoh.nexus.desktop.panes.mainmenu;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MenuFrame extends VBox {
	
    private Label titleLabel;
    private Text contentText;

    public MenuFrame(String title, String content, int heigth) {
    	getStyleClass().add("menu-frame");
    	
    	setMinSize(400, heigth);
    	setMaxSize(400, heigth);
    	
        titleLabel = new Label(title);
        titleLabel.getStyleClass().add("pane-1-title");
        contentText = new Text(content);
        contentText.getStyleClass().add("pane-1-text");

        // Füge die Elemente zur VBox hinzu
        this.getChildren().addAll(titleLabel, contentText);

    }

    // Methode zum Aktualisieren des Inhalts
    public void setTitle(String newTitle) {
    	titleLabel.setText(newTitle);
    }

}
