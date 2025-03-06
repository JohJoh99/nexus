package net.johjoh.nexus.desktop.util;

import java.util.HashMap;
import java.util.List;

public class ListUtil {
	
	private static HashMap<Integer, List<ListItem>> listItems = new HashMap<Integer, List<ListItem>>();
	
	public static List<ListItem> getListItems(int listId) {
		return listItems.get(listId);
	}

}
