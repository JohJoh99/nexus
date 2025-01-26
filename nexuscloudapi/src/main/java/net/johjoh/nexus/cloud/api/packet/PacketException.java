package net.johjoh.nexus.cloud.api.packet;

public class PacketException extends Exception {
	
	private static final long serialVersionUID = 6142611005352179160L;
	
	private final String message;
	
	public PacketException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return message;
	}

}
