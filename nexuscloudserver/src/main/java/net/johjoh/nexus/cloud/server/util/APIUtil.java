package net.johjoh.nexus.cloud.server.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class APIUtil {
	
	public static <T> List<T> merge(Collection<T> merge0, Collection<T> merge1) {
		List<T> list = new ArrayList<T>();
		list.addAll(merge0);
		list.addAll(merge1);
		return list;
	}
	
	public static <T> List<T> copy(Collection<T> copyOf) {
		List<T> list = new ArrayList<T>();
		list.addAll(copyOf);
		return list;
	}
	
	public static boolean classExists(String name) {
		try {
			Class.forName(name);
			return true;
		}
		catch(ClassNotFoundException e) {
			return false;
		}
	}
	
	public static ArrayList<Integer> parseIntList(String str) {
		if(str == null) return new ArrayList<Integer>();
		String[] intArray = str.split(",");
		ArrayList<Integer> intList = new ArrayList<Integer>();
		for(String s : intArray) {
			if(isInteger(s)) {
				intList.add(Integer.parseInt(s));
			}
			else {
				return null;
			}
		}
		return intList;
	}
	
	public static ArrayList<String> parseStringList(String str) {
		if(str == null) return new ArrayList<String>();
		String[] strArray = str.split(",");
		ArrayList<String> strList = new ArrayList<String>();
		for(String s : strArray) {
			strList.add(s);
		}
		return strList;
	}
	
	public static String toString(List<?> list) {
		String str = "";
		for(Object s : list) {
			str += s.toString() + ",";
		}
		if(str.length() > 0) str = str.substring(0, str.length() - 1);
		return str;
	}
	
	public static boolean isInteger(String str) {
		try {
			Integer.parseInt(str);
			return true;
		}
		catch(NumberFormatException e) {
			return false;
		}
	}
	
	public static boolean isUUID32(String playerArgument) {
		boolean matches = playerArgument.length() == 32 && playerArgument.matches("^[a-zA-Z0-9]*$");
		if(matches) {
			try {
				toUUID36(playerArgument);
				return true;
			}
			catch(IllegalArgumentException e) {}
		}
		return false;
	}
	
	public static boolean isUUID36(String playerArgument) {
		boolean matches = playerArgument.length() == 36 && playerArgument.matches("^[a-zA-Z0-9-]*$");
		if(matches) {
			try {
				UUID.fromString(playerArgument);
				return true;
			}
			catch(IllegalArgumentException e) {}
		}
		return false;
	}
	
	public static boolean isName(String playerArgument) {
		return playerArgument.length() > 0 && playerArgument.length() <= 16 && playerArgument.matches("^[a-zA-Z0-9_]*$");
	}
	
	public static UUID toUUID36(String uuid32) {
		String[] array = new String[5];
		array[0] = uuid32.substring(0, 8);
		array[1] = uuid32.substring(8, 12);
		array[2] = uuid32.substring(12, 16);
		array[3] = uuid32.substring(16, 20);
		array[4] = uuid32.substring(20, 32);
		String ruuid = array[0] + "-" + array[1] + "-" + array[2] + "-" + array[3] + "-" + array[4];
		return UUID.fromString(ruuid);
	}

	public static String toUUID32(UUID uuid36) {
		return uuid36.toString().replace("-", "");
	}

}
