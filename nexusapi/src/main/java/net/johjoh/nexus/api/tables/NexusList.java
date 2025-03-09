package net.johjoh.nexus.api.tables;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "list")
public class NexusList {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

    @Column(name = "title", nullable = false, length = 32)
    private String title;

    @Column(name = "owner_id", nullable = false)
    private int ownerId;
    
    public NexusList() {
    	
    }
    
    public NexusList(String title, int ownerId) {
    	this.title = title;
    	this.ownerId = ownerId;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public int getOwnerId() { return ownerId; }
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }
	
	public enum ListType {
		
			SHOPPING_LIST,
			TODO_LIST,
			OTHER;
		
	}

}
