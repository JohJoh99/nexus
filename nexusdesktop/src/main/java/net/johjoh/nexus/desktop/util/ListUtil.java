package net.johjoh.nexus.desktop.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javafx.application.Platform;
import net.johjoh.nexus.cloud.api.packet.data.PacketClientDataUpdate;
import net.johjoh.nexus.desktop.NexusDesktop;

public class ListUtil {
	
	private static HashMap<Integer, List<ListItem>> listItems = new HashMap<Integer, List<ListItem>>();
	private static HashMap<Integer, String> listTitles = new HashMap<Integer, String>();
	
	public static List<ListItem> getListItems(int listId) {
		return listItems.get(listId);
	}
	
	public static int getListIdFromTitle(String title) {
		for(int i : listTitles.keySet()) {
			if(listTitles.get(i).equalsIgnoreCase(title)) {
				return i;
			}
		}
		return -1;
	}
	
	public static String getTitle(int listId) {
		return listTitles.get(listId);
	}
	
	public static Set<Integer> getListIds() {
		return listTitles.keySet();
	}
	
	public static boolean hasItem(int listId, String title) {
		for(ListItem li : listItems.get(listId)) {
			if(li.getTitle().equalsIgnoreCase(title)) {
				return true;
			}
		}
		return false;
	}
	
	public static ListItem getListItem(int listId, String itemTitle) {
		for(ListItem li : listItems.get(listId)) {
			if(li.getTitle().equalsIgnoreCase(itemTitle)) {
				return li;
			}
		}
		return null;
	}
	
	public static void addListEntry(int listId, String title, String details) {

    	ObjectNode rootNode = null;
    	
        try {
            // Erstelle einen ObjectMapper
            ObjectMapper mapper = new ObjectMapper();

            // Erstelle das root ObjectNode
            rootNode = mapper.createObjectNode();
            rootNode.put("table", "list");
            rootNode.put("type", "line");

            // Erstelle das content ObjectNode
            ObjectNode contentNode = mapper.createObjectNode();
            contentNode.put("listId", listId);
            contentNode.put("title", title);
            contentNode.put("details", details);

            // Füge das content ObjectNode zum rootNode hinzu
            rootNode.set("content", contentNode);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        
    	PacketClientDataUpdate pcdu = new PacketClientDataUpdate(NexusDesktop.getCloudClient().getSessionId(), rootNode.toString());
    	NexusDesktop.getCloudClient().sendPacket(pcdu);
    	
    	listItems.get(listId).add(new ListItem(listId, title, false, details));
	}
	
	public static void loadListFromNode(JsonNode jsonNode) {
		
    	JsonNode head = jsonNode.get("headcontent");
    	JsonNode lines = jsonNode.get("linecontent");
    	
    	int headId = head.get("id").asInt();
    	String listTitle = head.get("title").asText();
    	int ownerId = head.get("ownerId").asInt();
    	
    	listItems.put(headId, new ArrayList<ListItem>());
    	listTitles.put(headId, listTitle);
		
        if (lines.isArray()) {
            for (JsonNode node : lines) {
                int id = node.get("id").asInt();
                String name = node.get("name").asText();
                boolean done = node.get("done").asBoolean();
                String details = node.get("details").asText();
                listItems.get(headId).add(new ListItem(id, name, done, details));
            }
        }
    	
    	if(ownerId == NexusDesktop.USERID) {
    		
    	}
    	else {
    		
    	}
    	
    	NexusDesktop.getListPane().reload();
		Platform.runLater(() -> {
	    	NexusDesktop.getMainMenuPane().loadLists();
		});
	}

}
