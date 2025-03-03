package net.johjoh.nexus.desktop.panes.lists;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ListPane extends BorderPane {
	
	private ListHeadPane listHeadPane;
	private ListRowPane listRowPane;
	private ListDetailsPane listDetailsPane;
	
	private HashMap<String, TextField> listHeader;
	private HashMap<String, Label> listHeaderLabels;
	private ArrayList<Label> listRows;
	
	private HashMap<String, ArrayList<ListItem>> listItems;
	
	public ListPane() {
		setId("list-pane");
		
		listItems = new HashMap<String, ArrayList<ListItem>>();
		
		listItems.put("TODO", new ArrayList<ListItem>());
		listItems.put("Einkaufsliste", new ArrayList<ListItem>());
		
		listItems.get("TODO").add(new ListItem("TODO", "Apfelbaum pflanzen", "Rote Äpfel"));
		listItems.get("TODO").add(new ListItem("TODO", "Birnenbaum pflanzen", "Rote Birnenbaum"));
		listItems.get("Einkaufsliste").add(new ListItem("Einkaufsliste", "Roter Apfel", "17x"));
		listItems.get("Einkaufsliste").add(new ListItem("Einkaufsliste", "Roter Birnenbaum", "312x"));
		listItems.get("Einkaufsliste").add(new ListItem("Einkaufsliste", "Mehl", "10.000 Mehlkörner"));
		
		listHeadPane = new ListHeadPane();
		listRowPane = new ListRowPane();
		listDetailsPane = new ListDetailsPane();
		
		ScrollPane listHeadScrollPane = new ScrollPane(listHeadPane);
		ScrollPane listRowScrollPane = new ScrollPane(listRowPane);
		
		setLeft(listHeadScrollPane);
		setCenter(listRowScrollPane);
		setRight(listDetailsPane);
		
		listHeader = new HashMap<String, TextField>();
		listHeaderLabels = new HashMap<String, Label>();
		listRows = new ArrayList<Label>();
		
		for(String h : listItems.keySet()) {
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
		}
		
	}
	
	private class ListHeadPane extends VBox {
		
		public ListHeadPane() {
			
		}
		
	}
	
	private class ListRowPane extends VBox {

		public ListRowPane() {
			
		}
		
	}
	
	private class ListDetailsPane extends VBox {
		
		private Label listLabel;
		private Label titleLabel;
		private Label detailsLabel;

		public ListDetailsPane() {
			listLabel = new Label();
			titleLabel = new Label();
			detailsLabel = new Label();
		}
		
	}
	
	private class ListItem {
		
		private String list;
		private String title;
		private String details;
		
		public ListItem(String list, String title, String details) {
			this.list = list;
			this.title = title;
			this.details = details;
		}
		
		public String getList() {
			return this.list;
		}
		
		public String getTitle() {
			return this.title;
		}
		
		public String getDetails() {
			return this.details;
		}
		
	}

}
