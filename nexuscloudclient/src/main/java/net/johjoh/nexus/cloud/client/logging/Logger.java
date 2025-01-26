package net.johjoh.nexus.cloud.client.logging;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Logger {
	
	public static void log(String msg) {
		log(Level.INFO, msg);
	}
	
	public static void log(Level l, String msg) {
		if(l.equals(Level.INFO)) {
			log(System.out, l, msg, true);
		}
		else {
			log(System.err, l, msg, true);
		}
	}
	
	public static void log(Level l, String msg, boolean showTime) {
		if(l.equals(Level.INFO)) {
			log(System.out, l, msg, showTime);
		}
		else {
			log(System.err, l, msg, showTime);
		}
	}
	
	public static void log(PrintStream ps, Level l, String msg, boolean showTime) {
		String out = "";
		if(showTime) out += "[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] ";
		ps.println(out + "[" + l.getName() + "/" + Thread.currentThread().getName() + "]: " + msg);
	}
	
	public static boolean log(Throwable t) {
		return log("Thread '" + Thread.currentThread().getName() + "' occurred an Exception:", t);
	}
	
	public static boolean log(Throwable t, boolean showTime) {
		return log("Thread '" + Thread.currentThread().getName() + "' occurred an Exception:", t, showTime);
	}
	
	public static boolean log(String msg, Throwable t) {
		log(Level.FATAL, msg + " " + t.getClass().getName() + ": " + t.getMessage());
		for(StackTraceElement ste : t.getStackTrace()) {
			log(Level.FATAL, "	at " + ste.toString());
		}
		return true;
	}
	
	public static boolean log(String msg, Throwable t, boolean showTime) {
		log(Level.FATAL, msg + " " + t.getClass().getName() + ": " + t.getMessage(), showTime);
		for(StackTraceElement ste : t.getStackTrace()) {
			log(Level.FATAL, "	at " + ste.toString(), showTime);
		}
		return true;
	}
	
	public static boolean logMinimal(Throwable t) {
		Logger.log(Level.ERROR, t.getClass().getName() + ": " + t.getMessage());
		return true;
	}
	
	public static void logArray(String msg, ArrayList<String> list) {
		if(msg.contains("%ARRAY%")) {
			String asList = "";
			for(String s : list) {
				asList += s + ", ";
			}
			if(asList.length() > 0) {
				asList = asList.substring(0, asList.length() - 2);
			}
			msg = msg.replace("%ARRAY%", asList);
		}
		Logger.log(msg);
	}

}
