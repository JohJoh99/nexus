package net.johjoh.nexus.desktop.util;

public class ListItem {
	
	private int listId;
	private String title;
	private boolean done;
	private String details;
	
	public ListItem(int listId, String title, boolean done, String details) {
		this.listId = listId;
		this.title = title;
		this.done = done;
		this.details = details;
	}
	
	public int getList() {
		return this.listId;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String newTitle) {
		this.title = newTitle;
	}
	
	public boolean getDone() {
		return this.done;
	}
	
	public void setDone(boolean done) {
		this.done = done;
	}
	
	public String getDetails() {
		return this.details;
	}
	
	public String setDetails(String details) {
		return this.details;
	}

}
