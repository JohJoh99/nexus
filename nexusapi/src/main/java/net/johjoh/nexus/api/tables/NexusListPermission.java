package net.johjoh.nexus.api.tables;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "list_permission")
public class NexusListPermission {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

    @Column(name = "list_id")
	private int listId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "permission_id")
    private int permissionId;
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getListId() { return listId; }
    public void setListId(int listId) { this.listId = listId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public int getPermissionId() { return permissionId; }
    public void setPermissionId(int permissionId) { this.permissionId = permissionId; }

}
