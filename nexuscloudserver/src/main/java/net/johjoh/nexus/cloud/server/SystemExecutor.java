package net.johjoh.nexus.cloud.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SystemExecutor {
	
	private Exception lastException;
	private int exitCode;
	private String programResponse;
	private String lastLine;
	
	public void execute(String[] command) {
		try {

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();
            
			//Process process = Runtime.getRuntime().exec(command);
			exitCode = process.waitFor();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String line = "";
			while((line = reader.readLine()) != null) {
				builder.append(line + "\n");
				lastLine = line;
			}
			programResponse = builder.toString();
		}
		catch(IOException | InterruptedException e) {
			lastException = e;
			exitCode = -1;
		}
	}
	
	public int getExitCode() {
		return exitCode;
	}
	
	public Exception getException() {
		return lastException;
	}
	
	public String getProgramResponse() {
		return programResponse;
	}

	public String getLastLine() {
		return lastLine;
	}

}
