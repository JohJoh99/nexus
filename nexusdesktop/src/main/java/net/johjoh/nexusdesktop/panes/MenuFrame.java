package net.johjoh.nexusdesktop.panes;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MenuFrame extends VBox {
	
    private Label titleLabel;
    private Text contentText;

    public MenuFrame(String title, String content) {
        titleLabel = new Label(title);
        contentText = new Text(content);

        // Füge die Elemente zur VBox hinzu
        this.getChildren().addAll(titleLabel, contentText);

        // Setze Stil für die Kachel
        this.setStyle("-fx-background-color: #3c3f41; -fx-padding: 10; -fx-border-color: #2b2b2b; -fx-border-width: 2;");
        titleLabel.setStyle("-fx-text-fill: #ffffff;");
        contentText.setStyle("-fx-fill: #cccccc;");
    }

    // Methode zum Aktualisieren des Inhalts
    public void updateContent(String newContent) {
        contentText.setText(newContent);
    }

}
