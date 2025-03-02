package net.johjoh.nexus.cloud.api.packet.user;

import net.johjoh.nexus.cloud.api.packet.Packet;
import net.johjoh.nexus.cloud.api.packet.PacketType;

public class PacketClientUserLogin extends Packet {
	
	private static final String USERNAME = "username";
	private static final String PASSWORD_HASH = "passwordHash";
	
	public PacketClientUserLogin() {
		super(PacketType.CLIENT_USER_LOGIN);
	}
	
	public PacketClientUserLogin(String username, String passwordHash) {
		super(PacketType.CLIENT_USER_LOGIN);
		setObject(USERNAME, username);
		setObject(PASSWORD_HASH, passwordHash);
	}
	
	public void setUsername(String username) {
		setObject(USERNAME, username);
	}
	
	public void setPasswordHash(String passwordHash) {
		setObject(PASSWORD_HASH, passwordHash);
	}
	
	public String getUsername() {
		return getObjectAsString(USERNAME);
	}
	
	public String getPasswordHash() {
		return getObjectAsString(PASSWORD_HASH);
	}

	@Override
	public boolean isValid() {
		String username = getUsername();
		String passwordHash = getPasswordHash();
		
		return username != null && passwordHash != null;
	}

}
