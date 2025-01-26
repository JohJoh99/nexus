package net.johjoh.nexus.desktop.panes;

import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import net.johjoh.nexus.desktop.NexusDesktop;

public class RootFrame extends BorderPane {
	
	public RootFrame() {
		//root.getStylesheets().add("https://raw.githubusercontent.com/antoniopelusi/JavaFX-Dark-Theme/main/style.css");
		setTop(NexusDesktop.getControllPane());
		setCenter(NexusDesktop.getMainMenuPane());
		setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
	}

}
