package net.johjoh.nexus.desktop.panes;

import javafx.scene.layout.TilePane;

public class MainMenuPane extends TilePane {
	
	public MainMenuPane() {
		setPrefColumns(3);
		setHgap(10);
		setVgap(10);
		
		
	    for (int i = 1; i <= 10; i++) {
	        MenuFrame label = new MenuFrame("Kachel " + i, "Dies sind alles tolle Kacheln");
	        label.setMinHeight(50 + (i * 10));
	        label.setMinWidth(100);
	        getChildren().add(label);
	    }
	}

}
