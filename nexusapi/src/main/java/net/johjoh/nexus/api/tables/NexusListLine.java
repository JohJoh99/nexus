package net.johjoh.nexus.api.tables;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "list_line")
public class NexusListLine {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

    @Column(name = "list_id", nullable = false)
	private int listId;

    @Column(name = "title", nullable = false, length = 32)
    private String title;

    @Column(name = "done")
    private boolean done;

    @Column(name = "details", nullable = true)
    private String details;
    
    public NexusListLine() {
    	
    }
    
    public NexusListLine(int listId, String title, boolean done, String details) {
    	this.listId = listId;
    	this.title = title;
    	this.done = done;
    	this.details = details;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public int getListId() { return listId; }
    public void setListId(int listId) { this.listId = listId; }
    
    public boolean getDone() { return done; }
    public void setDone(boolean done) { this.done = done; }
    
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

}
