package net.johjoh.nexus.desktop.panes.mainmenu;

import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import net.johjoh.nexus.desktop.util.ListUtil;

public class MainMenuPane extends BorderPane {
	
	private MainMenuColumn firstColumn;
	private MainMenuColumn secondColumn;
	private MainMenuColumn thirdColumn;
	
	private MessageFrame messageFrame;
	
	private boolean listsLoaded;
	
	public MessageFrame getMessageFrame() {
		return this.messageFrame;
	}
	
	public MainMenuPane() {
		//setPrefColumns(3);
		//setPadding(new Insets(40));
		//setHgap(10);
		//setVgap(10);
		
		firstColumn = new MainMenuColumn();
		secondColumn = new MainMenuColumn();
		thirdColumn = new MainMenuColumn();
		
		WeatherFrame weatherFrame = new WeatherFrame(48.4014, 9.9885, "Ulm");
		firstColumn.getChildren().add(weatherFrame);
		
		CalendarFrame calendarFrame = new CalendarFrame();
		firstColumn.getChildren().add(calendarFrame);
		
		messageFrame = new MessageFrame();
		secondColumn.getChildren().add(messageFrame);
		
		setLeft(firstColumn);
		setCenter(secondColumn);
		setRight(thirdColumn);
		
		
		
	    /*for (int i = 1; i <= 10; i++) {
	        MenuFrame label = new MenuFrame("Kachel " + i, "Dies sind alles tolle Kacheln");
	        //label.setMinHeight(50 + (i * 10));
	        //label.setMinWidth(100);
	        getChildren().add(label);
	    }*/
		
		listsLoaded = false;
	}
	
	public void loadLists() {
		if(ListUtil.getListIds().size() != 2)
			return;
		if(listsLoaded)
			return;
		
		listsLoaded = true;

		ListFrame todoListFrame = new ListFrame("TODO");
		thirdColumn.getChildren().add(todoListFrame);
		
		ListFrame shoppingListFrame = new ListFrame("Einkaufsliste");
		thirdColumn.getChildren().add(shoppingListFrame);
		
	}
	
	private class MainMenuColumn extends VBox {
		
		public MainMenuColumn() {
			getStyleClass().add("pane-1");
			
			setAlignment(Pos.TOP_CENTER);
		}
	}

}
