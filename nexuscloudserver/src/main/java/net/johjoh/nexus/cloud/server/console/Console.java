package net.johjoh.nexus.cloud.server.console;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import net.johjoh.nexus.cloud.server.CloudServer;
import net.johjoh.nexus.cloud.server.console.command.Command;
import net.johjoh.nexus.cloud.server.console.command.info.HelpCommand;
import net.johjoh.nexus.cloud.server.console.command.info.InfoCommand;
import net.johjoh.nexus.cloud.server.console.command.info.ListCommand;
import net.johjoh.nexus.cloud.server.console.command.startstop.StopCommand;
import net.johjoh.nexus.cloud.server.logging.Level;
import net.johjoh.nexus.cloud.server.logging.Logger;
import net.johjoh.nexus.cloud.server.logging.TeePrintStream;

public class Console extends Thread {
	
	private static Console instance;
	
	public static Console getInstance() {
		return instance;
	}
	
	private HashMap<String, Command> commands = new HashMap<String, Command>();
	private boolean running = true;
	private final TeePrintStream teeOut;
	private final TeePrintStream teeErr;

	public Console(TeePrintStream teeOut, TeePrintStream teeErr) {
		instance = this;
		this.teeOut = teeOut;
		this.teeErr = teeErr;
	}
	
	@Override
	public void run() {
		Thread.currentThread().setName("ConsoleThread");
		commands.put("help", new HelpCommand());
		commands.put("info", new InfoCommand());
		commands.put("list", new ListCommand());
		
		commands.put("stop", new StopCommand());
		try {
			Scanner scanner = new Scanner(System.in);
			while(running) {
				String input = scanner.nextLine();
				Logger.log(teeOut.getFilePrintStream(), Level.INFO, "Console Input: " + input);
				try {
					if(CloudServer.getInstance() == null) {
						Logger.log(Level.ERROR, "CloudServer Instance is null");
						continue;
					}
					
					String[] splitInput = input.split(" ");
					String command = splitInput[0];
					ArrayList<String> listArgs = new ArrayList<String>();
					for(int i = 1; i < splitInput.length; i++) {
						listArgs.add(splitInput[i]);
					}
					String[] args = listArgs.toArray(new String[listArgs.size()]);
					
					if(command.equalsIgnoreCase("help") || command.equalsIgnoreCase("?")) {
						Logger.log("Available Commands:");
						for(Command cmd : commands.values()) {
							Logger.log(cmd.getName() + ": " + cmd.getDescription());
						}
					}
					else {
						Command cmd = commands.get(command);
						if(cmd != null) {
							cmd.execute(args);
						}
						else {
							Logger.log("Command not found");
						}
					}
				}
				catch(Throwable t) {
					Logger.log(t, Thread.currentThread());
				}
			}
			scanner.close();
		}
		catch(Exception e) {
			Logger.log(e, Thread.currentThread());
			System.exit(1);
		}
	}
	
	public TeePrintStream getOutputStream() {
		return teeOut;
	}
	
	public TeePrintStream getErrorStream() {
		return teeErr;
	}

}
