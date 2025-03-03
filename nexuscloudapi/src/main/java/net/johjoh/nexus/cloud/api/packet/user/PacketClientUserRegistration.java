package net.johjoh.nexus.cloud.api.packet.user;

import net.johjoh.nexus.cloud.api.packet.Packet;
import net.johjoh.nexus.cloud.api.packet.PacketType;

public class PacketClientUserRegistration extends Packet {
	
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	
	public PacketClientUserRegistration() {
		super(PacketType.CLIENT_USER_REGISTRATION);
	}
	
	public PacketClientUserRegistration(String username, String password) {
		super(PacketType.CLIENT_USER_REGISTRATION);
		setObject(USERNAME, username);
		setObject(PASSWORD, password);
	}
	
	public void setUsername(String username) {
		setObject(USERNAME, username);
	}
	
	public void setPassword(String password) {
		setObject(PASSWORD, password);
	}
	
	public String getUsername() {
		return getObjectAsString(USERNAME);
	}
	
	public String getPassword() {
		return getObjectAsString(PASSWORD);
	}

	@Override
	public boolean isValid() {
		String username = getUsername();
		String password = getPassword();
		
		return username != null && password != null;
	}

}
