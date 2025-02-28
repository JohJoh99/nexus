package net.johjoh.nexus.cloud.server.logging;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Logger {
	
	public static boolean log(String msg) {
		return log(Level.INFO, msg);
	}
	
	public static boolean log(Level level, String msg) {
		if(level.equals(Level.INFO)) {
			return log(System.out, level, msg);
		}
		else {
			return log(System.err, level, msg);
		}
	}
	
	public static boolean log(PrintStream ps, Level l, String msg, String threadName) {
		String out = "[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] ";
		ps.println(out + "[" + l.getName() + "/" + threadName + "]: " + msg);
		return true;
	}
	
	public static boolean log(PrintStream ps, Level l, String msg) {
		return log(ps, l, msg, Thread.currentThread().getName());
	}
	
	public static boolean log(Throwable t, Thread executingThread) {
		return log("Thread '" + executingThread.getName() + "' occurred an Exception", t, executingThread);
	}
	
	public static boolean log(String msg, Throwable t, Thread executingThread) {
		log(Level.FATAL, msg + ": " + t.getClass().getName() + ": " + t.getMessage());
		for(StackTraceElement ste : t.getStackTrace()) {
			log(Level.FATAL, "	at " + ste.toString());
		}
		return true;
	}
	
	public static boolean logMinimal(Throwable t) {
		Logger.log(Level.ERROR, t.getClass().getName() + ": " + t.getMessage());
		return true;
	}
	
	public static boolean logMinimal(String message, Throwable t) {
		Logger.log(Level.ERROR, message + "(" + t.getClass().getName() + ": " + t.getMessage() + ")");
		return true;
	}
	
	public static boolean logArray(String msg, ArrayList<String> list) {
		if(list.size() == 0) return true;
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
		return true;
	}

}
