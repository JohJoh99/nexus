package net.johjoh.nexus.cloud.client.logging;

public enum Level {

	WARNING("WARN"),
	ERROR("ERRO"),
	FATAL("FATA"),
	INFO("INFO");

	private String name;

	Level(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

}
