package net.johjoh.nexus.desktop.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import net.johjoh.nexus.cloud.client.logging.Level;
import net.johjoh.nexus.cloud.client.logging.Logger;

public class Settings {
	
	private static String serverIP;
	private static int serverPort;
	private static String serverPassword;
	
	public static String getServerIP() {
		return serverIP;
	}
	
	public static void setServerSettings(String newServerIP, int newPort, String newPassword) {
		serverIP = newServerIP;
		serverPort = newPort;
		serverPassword = newPassword;
		
		File propertyFile = new File("nexus.properties");
		Properties nexusProperties = new Properties();
		
		try {
			nexusProperties.load(new FileInputStream(propertyFile));
		}
		catch (IOException e) {
			Logger.log(Level.FATAL, "Failed to Load nexus.properties");
			Logger.logMinimal(e);
		}
		nexusProperties.setProperty("server-ip", newServerIP);
		nexusProperties.setProperty("server-port", String.valueOf(newPort));
		nexusProperties.setProperty("server-password", newPassword);
		
        try (FileOutputStream output = new FileOutputStream("nexus.properties")) {
        	nexusProperties.store(output, "Nexus-Konfiguration");
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public static int getServerPort() {
		return serverPort;
	}
	
	public static String getServerPassword() {
		return serverPassword;
	}
	
	public static void loadSettings(Properties properties) {
		serverIP = properties.getProperty("server-ip");
		serverPort = Integer.parseInt(properties.getProperty("server-port"));
		serverPassword = properties.getProperty("server-password");
	}
	
	public static Properties loadProperties() {
		Properties defaultProperties = new Properties();
		defaultProperties.setProperty("server-ip", "127.0.0.1");
		defaultProperties.setProperty("server-port", "4242");
		defaultProperties.setProperty("server-password", "password_safe");
		
		File propertyFile = new File("nexus.properties");
		if(!propertyFile.exists()) {
			try {
				propertyFile.createNewFile();
			}
			catch (IOException e) {
				Logger.log(Level.FATAL, "Failed to Create nexus.properties");
				Logger.logMinimal(e);
				System.exit(1);
				return defaultProperties;
			}
			try {
				defaultProperties.store(new FileOutputStream(propertyFile), "CloudServer Properties File");
			}
			catch (IOException e) {
				Logger.log(Level.FATAL, "Failed to Save nexus.properties");
				Logger.logMinimal(e);
				System.exit(1);
				return defaultProperties;
			}
		}
		
		Properties nexusProperties = new Properties();
		try {
			nexusProperties.load(new FileInputStream(propertyFile));
		}
		catch (IOException e) {
			Logger.log(Level.FATAL, "Failed to Load nexus.properties");
			Logger.logMinimal(e);
			System.exit(1);
			return defaultProperties;
		}
		
		if(!isValid(nexusProperties)) {
			Logger.log(Level.ERROR, "Loaded nexus.properties are not valid... Using defaults");
			nexusProperties = defaultProperties;
			try {
				defaultProperties.store(new FileOutputStream(propertyFile), "CloudServer Properties File");
			}
			catch (IOException e) {}
		}
		return nexusProperties;
	}
	
	private static boolean isValid(Properties nexusProperties) {
		String ip = nexusProperties.getProperty("server-ip");
		String port = nexusProperties.getProperty("server-port");
		String password = nexusProperties.getProperty("server-password");
		
		boolean a = ip != null;
		boolean b = port != null && isInteger(port);
		boolean c = password != null;
		
		return a && b && c;
	}
	
	private static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;
		}
		catch(NumberFormatException e) {
			return false;
		}
	}
	
	@SuppressWarnings("unused")
	private static boolean isBoolean(String s) {
		if(s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false")) return true;
		return false;
	}

}
