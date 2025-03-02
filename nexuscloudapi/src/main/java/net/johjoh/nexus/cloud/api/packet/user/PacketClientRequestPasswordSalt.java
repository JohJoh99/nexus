package net.johjoh.nexus.cloud.api.packet.user;

import net.johjoh.nexus.cloud.api.packet.Packet;
import net.johjoh.nexus.cloud.api.packet.PacketType;

public class PacketClientRequestPasswordSalt extends Packet {
	
	private static final String USERNAME = "username";
	
	public PacketClientRequestPasswordSalt() {
		super(PacketType.CLIENT_REQUEST_PASSWORD_SALT);
	}
	
	public PacketClientRequestPasswordSalt(String username) {
		super(PacketType.CLIENT_REQUEST_PASSWORD_SALT);
		setObject(USERNAME, username);
	}
	
	public void setUsername(String username) {
		setObject(USERNAME, username);
	}
	
	public String getUsername() {
		return getObjectAsString(USERNAME);
	}

	@Override
	public boolean isValid() {
		String username = getUsername();
		
		return username != null;
	}

}
