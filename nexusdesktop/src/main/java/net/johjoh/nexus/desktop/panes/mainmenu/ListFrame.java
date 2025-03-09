package net.johjoh.nexus.desktop.panes.mainmenu;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import net.johjoh.nexus.desktop.util.ListItem;
import net.johjoh.nexus.desktop.util.ListUtil;

public class ListFrame extends MenuFrame {
	
	private VBox listBox;
	private ScrollPane scrollPane;
	
	public ListFrame(String list) {
		
		super(list, "", 300);
		listBox = new VBox();
		listBox.setFillWidth(true);
		scrollPane = new ScrollPane(listBox);
		listBox.getStyleClass().add("pane-1");
		scrollPane.getStyleClass().add("pane-1");
		
		int listId = ListUtil.getListIdFromTitle(list);
		
		for(ListItem li : ListUtil.getListItems(listId)) {
			ListFrameEntry lfe = new ListFrameEntry(li.getTitle(), li.getDone());
			listBox.getChildren().add(lfe);
		}
		
		getChildren().add(scrollPane);
	}
	
	private class ListFrameEntry extends HBox {
		
		private Label titleLabel;
		private CheckBox doneCheckBox;
		
		public ListFrameEntry(String title, boolean done) {
			setId("list-frame-entry");
			setFillWidth(true);
			setMaxWidth(Double.MAX_VALUE);
			
			titleLabel = new Label(title);
			titleLabel.setId("list-frame-title");
			titleLabel.setMaxWidth(Double.MAX_VALUE);
			doneCheckBox = new CheckBox();
			doneCheckBox.setSelected(done);
			doneCheckBox.setAlignment(Pos.CENTER_RIGHT);
			HBox.setHgrow(titleLabel, Priority.ALWAYS);
			getChildren().addAll(titleLabel, doneCheckBox);
		}
	}

}
