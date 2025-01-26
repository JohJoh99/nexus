package net.johjoh.nexus.cloud.api.packet.general;

import net.johjoh.nexus.cloud.api.packet.Packet;
import net.johjoh.nexus.cloud.api.packet.PacketType;

public class PacketClientGeneralHandshake extends Packet {

	private static final String USERNAME = "username";
	
	public PacketClientGeneralHandshake() {
		super(PacketType.CLIENT_GENERAL_HANDSHAKE);
	}
	
	public PacketClientGeneralHandshake(String username) {
		super(PacketType.CLIENT_GENERAL_HANDSHAKE);
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
		return getUsername() != null;
	}

}
