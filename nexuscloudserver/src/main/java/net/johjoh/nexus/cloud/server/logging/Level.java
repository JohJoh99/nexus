package net.johjoh.nexus.cloud.server.logging;

public enum Level {
	
	WARNING("WARNING"), ERROR("ERROR"), FATAL("FATAL"), INFO("INFO");
	
	private String name;
	
	private Level(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

}
