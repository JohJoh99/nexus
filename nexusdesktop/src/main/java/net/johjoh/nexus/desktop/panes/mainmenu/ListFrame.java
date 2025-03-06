package net.johjoh.nexus.desktop.panes.mainmenu;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ListFrame extends MenuFrame {
	
	private VBox listBox;
	private ScrollPane scrollPane;
	
	public ListFrame(String list) {
		super(list, "", 300);
		listBox = new VBox();
		scrollPane = new ScrollPane(listBox);
		
		getChildren().add(scrollPane);
	}
	
	private class ListFrameEntry extends HBox {
		
		private Label titleLabel;
		private CheckBox doneCheckBox;
		
		public ListFrameEntry(String title, boolean done) {
			titleLabel = new Label(title);
			doneCheckBox = new CheckBox();
			doneCheckBox.setSelected(done);
		}
	}

}
