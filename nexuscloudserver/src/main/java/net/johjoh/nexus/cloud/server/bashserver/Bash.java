package net.johjoh.nexus.cloud.server.bashserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import net.johjoh.nexus.cloud.server.logging.Level;
import net.johjoh.nexus.cloud.server.logging.Logger;

public class Bash {

	public static BashResult bash(String command) {
		if(System.getProperty("os.name").toLowerCase().contains("windows")) {
			Logger.log(Level.WARNING, "Bashing will not work on windows!");
			return new BashResult(command, null);
		}
		command = command.trim();
			
		int exitCode;
		String programResponse;
		
		try {
			Process process = Runtime.getRuntime().exec(command);
			exitCode = process.waitFor();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String line = "";
			while((line = reader.readLine()) != null) {
				builder.append(line + "\n");
			}
			programResponse = builder.toString();
		}
		catch(IOException | InterruptedException e) {
			return new BashResult(command, e);
		}
		
		return new BashResult(command, exitCode, programResponse);
	}
		
	public static class BashResult {
		
		private String command;
		private Exception exception;
		private int exitCode;
		private String programResponse;
		
		public BashResult(String command, Exception exception) {
			this.command = command;
			this.exception = exception;
		}
		
		public BashResult(String command, int exitCode, String programResponse) {
			this.command = command;
			this.exitCode = exitCode;
			this.programResponse = programResponse;
		}
		
		public String getCommand() {
			return command;
		}
		
		public Exception getException() {
			return exception;
		}
		
		public int getExitCode() {
			return exitCode;
		}
		
		public String getProgramResponse() {
			return programResponse;
		}
		
	}

}
