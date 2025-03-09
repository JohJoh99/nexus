package net.johjoh.nexus.desktop.panes.lists;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.johjoh.nexus.desktop.NexusDesktop;
import net.johjoh.nexus.desktop.util.ListItem;
import net.johjoh.nexus.desktop.util.ListUtil;

public class ListPane extends BorderPane {
	
	private ListHeadPane listHeadPane;
	private ListRowPane listRowPane;
	private ListDetailsPane listDetailsPane;
	
	public ListRowPane getListRowPane() { return this.listRowPane; }
	
	public ListPane() {
		setId("list-pane");
	}
	
	public void reload() {
		
		listHeadPane = new ListHeadPane();
		listRowPane = new ListRowPane();
		listDetailsPane = new ListDetailsPane();
		
		ScrollPane listHeadScrollPane = new ScrollPane(listHeadPane);
		ScrollPane listRowScrollPane = new ScrollPane(listRowPane);
		
		setLeft(listHeadScrollPane);
		setCenter(listRowScrollPane);
		setRight(listDetailsPane);
		
		//listHeader = new HashMap<String, TextField>();
		//listHeaderLabels = new HashMap<String, Label>();
		//listRows = new ArrayList<Label>();
		
		/*for(String h : listItems.keySet()) {
			TextField textField = new TextField(h);
			Label label = new Label(h);
			label.getStyleClass().add("list-title-label");
			
			listHeader.put(h, textField);
			listHeaderLabels.put(h, label);
			
			listHeadPane.getChildren().add(label);
			
			//	StackPane mit Label über TextField
			
	        label.setOnMouseClicked(event -> {
	            if (event.getButton() == MouseButton.PRIMARY) {
	                if (event.getClickCount() == 1) {
	                    System.out.println("Einfacher Klick ausgeführt!");
	                } else if (event.getClickCount() == 2) {
	                    textField.setText(label.getText());
	                    StackPane root = (StackPane) label.getParent();
	                    root.getChildren().setAll(textField);
	                    textField.requestFocus();
	                }
	            }
	        });

	        textField.setOnAction(event -> {
	            label.setText(textField.getText());
	            StackPane root = (StackPane) textField.getParent();
	            root.getChildren().setAll(label);
	        });

	        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
	            if (!newVal) {
	                label.setText(textField.getText());
	                StackPane root = (StackPane) textField.getParent();
	                root.getChildren().setAll(label);
	            }
	        });
		}
		
		for(ListItem h : listItems.get("TODO")) {
			Label label = new Label(h.getTitle());
			label.getStyleClass().add("list-entry-label");
			listRows.add(label);
			
			listRowPane.getChildren().add(label);
		}*/
		
	}
	
	public void openDetails(int listId, String title) {
		listDetailsPane = new ListDetailsPane(listId, title);
		setRight(listDetailsPane);
	}
	
	private class ListHeadPane extends VBox {
		
		private HashMap<Integer, TextField> listHeader;
		private HashMap<Integer, ListTitleLabel> listHeaderLabels;
		
		public ListHeadPane() {
			setId("list-pane");

			listHeader = new HashMap<Integer, TextField>();
			listHeaderLabels = new HashMap<Integer, ListTitleLabel>();
			
			for(int i : ListUtil.getListIds()) {
				String title = ListUtil.getTitle(i);
				TextField textField = new TextField(title);
				textField.setVisible(false);
				ListTitleLabel label = new ListTitleLabel(title, i);
				label.getStyleClass().add("list-header-label");
				textField.getStyleClass().add("list-header-textfield");
				
				label.setOnMouseReleased(event -> {
					NexusDesktop.getListPane().getListRowPane().showListLines(label.getListId());
				});
				
				label.getStyleClass().add("list-title-label");
				
				listHeader.put(i, textField);
				listHeaderLabels.put(i, label);
				getChildren().addAll(listHeader.get(i), listHeaderLabels.get(i));
			}
		}
		
		private class ListTitleLabel extends Label {
			
			private int listId;
			
			public ListTitleLabel(String text, int listId) {
				super(text);
				this.listId = listId;
			}
			
			public int getListId() {
				return this.listId;
			}
		}
		
	}
	
	private class ListRowPane extends VBox {
		
		private ArrayList<ListLineBox> listLineBoxes;
		private HBox addBox;
		private TextField addTextField;
		private Button addButton;
		
		private int currentListId = -1;

		public ListRowPane() {
			setId("list-pane");
			
			listLineBoxes = new ArrayList<ListLineBox>();

			for(int i : ListUtil.getListIds()) {
				currentListId = i;
				for(ListItem li : ListUtil.getListItems(i)) {
					ListLineBox box = new ListLineBox(currentListId, li);
					listLineBoxes.add(box);
					
					getChildren().add(box);
				}
				
				break;
			}
			
			addBox = new HBox();
			
			addTextField = new TextField();
			addTextField.setPromptText("Titel");
			
			addButton = new Button("+");
			addButton.setOnMouseReleased(event -> {
				if(addTextField.getText().length() > 0 && !ListUtil.hasItem(currentListId, addTextField.getText())) {
					ListUtil.addListEntry(currentListId, addTextField.getText(), "");
					addTextField.clear();
					showListLines(currentListId);
				}
			});
			addBox.getChildren().addAll(addTextField, addButton);
			
			getChildren().add(addBox);
		}
		
		public void showListLines(int listId) {
			getChildren().clear();
			listLineBoxes.clear();
			
			currentListId = listId;

			for(ListItem li : ListUtil.getListItems(listId)) {
				ListLineBox box = new ListLineBox(currentListId, li);
				listLineBoxes.add(box);
				
				getChildren().add(box);
			}

			getChildren().add(addBox);
		}
		
		private class ListLineBox extends HBox {
			
			private Label titleLabel;
			private CheckBox doneCheckBox;
			private Button deleteButton;
			
			private int listId;
			private String title;
			
			public ListLineBox(int listId, ListItem li) {
				this.listId = listId;
				this.title = li.getTitle();
				
				titleLabel = new Label(li.getTitle());
				titleLabel.setOnMouseReleased(event -> {
					NexusDesktop.getListPane().openDetails(this.listId, this.title);
				});
				titleLabel.getStyleClass().add("list-line-label");
				doneCheckBox = new CheckBox();
				doneCheckBox.setSelected(li.getDone());
				deleteButton = new Button("-");
				
				getChildren().addAll(titleLabel, doneCheckBox, deleteButton);
			}
		}
		
	}
	
	private class ListDetailsPane extends VBox {
		
		private Label listLabel;
		private Label titleLabel;
		private Label detailsLabel;
		
		public ListDetailsPane() {
			
		}

		public ListDetailsPane(int listId, String listTitle) {
			setId("list-pane");
			
			ListItem li = ListUtil.getListItem(listId, listTitle);
			
			Label listCaption = new Label("Liste:");
			listCaption.getStyleClass().add("list-line-label");
			listLabel = new Label(ListUtil.getTitle(listId));
			listLabel.getStyleClass().add("list-line-label");
			Label itemCaption = new Label("Eintrag:");
			itemCaption.getStyleClass().add("list-line-label");
			titleLabel = new Label(li.getTitle());
			titleLabel.getStyleClass().add("list-line-label");
			detailsLabel = new Label(li.getDetails());
			detailsLabel.getStyleClass().add("list-line-label");
			
			getChildren().addAll(listCaption, listLabel, itemCaption, titleLabel, detailsLabel);
		}
		
	}

}
