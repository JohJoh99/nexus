package net.johjoh.nexus.desktop.panes;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

public class MainMenuPane extends BorderPane {
	
	private MainMenuColumn firstColumn;
	private MainMenuColumn secondColumn;
	private MainMenuColumn thirdColumn;
	
	
	public MainMenuPane() {
		//setPrefColumns(3);
		setPadding(new Insets(40));
		//setHgap(10);
		//setVgap(10);
		
		firstColumn = new MainMenuColumn();
		secondColumn = new MainMenuColumn();
		thirdColumn = new MainMenuColumn();
		
		MenuFrame a = new MenuFrame("Kachel " + 1, "Dies sind alles tolle Kacheln");
		firstColumn.getChildren().add(a);
		
		MenuFrame b = new MenuFrame("Kachel " + 2, "Dies sind alles tolle Kacheln");
		secondColumn.getChildren().add(b);
		
		MenuFrame c = new MenuFrame("Kachel " + 3, "Dies sind alles tolle Kacheln");
		secondColumn.getChildren().add(c);
		
		MenuFrame d = new MenuFrame("Kachel " + 4, "Dies sind alles tolle Kacheln");
		thirdColumn.getChildren().add(d);
		
		setLeft(firstColumn);
		setCenter(secondColumn);
		setRight(thirdColumn);
		
		
		
	    /*for (int i = 1; i <= 10; i++) {
	        MenuFrame label = new MenuFrame("Kachel " + i, "Dies sind alles tolle Kacheln");
	        //label.setMinHeight(50 + (i * 10));
	        //label.setMinWidth(100);
	        getChildren().add(label);
	    }*/
	}
	
	private class MainMenuColumn extends VBox {
		
		public MainMenuColumn() {
			getStyleClass().add("pane-1");
		}
	}

}
