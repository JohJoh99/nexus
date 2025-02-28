package net.johjoh.nexus.cloud.server.console.command;

public abstract class Command {
	
	private String name;
	private String description;
	
	public Command(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public abstract void execute(String[] args);

}
