package net.johjoh.nexus.cloud.api.packet.general;

import net.johjoh.nexus.cloud.api.client.ClientType;
import net.johjoh.nexus.cloud.api.packet.Packet;
import net.johjoh.nexus.cloud.api.packet.PacketType;

public class PacketClientGeneralLogin extends Packet {
	
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	private static final String CLIENT_TYPE = "clientType";
	
	public PacketClientGeneralLogin() {
		super(PacketType.CLIENT_GENERAL_LOGIN);
	}
	
	public PacketClientGeneralLogin(String username, String password, ClientType clientType) {
		super(PacketType.CLIENT_GENERAL_LOGIN);
		setObject(USERNAME, username);
		setObject(PASSWORD, password);
		setObject(CLIENT_TYPE, clientType.getId());
	}
	
	public void setUsername(String username) {
		setObject(USERNAME, username);
	}
	
	public void setPassword(String password) {
		setObject(PASSWORD, password);
	}
	
	public void setClientType(ClientType clientType) {
		setObject(CLIENT_TYPE, clientType.getId());
	}
	
	public String getUsername() {
		return getObjectAsString(USERNAME);
	}
	
	public String getPassword() {
		return getObjectAsString(PASSWORD);
	}
	
	public ClientType getClientType() {
		return ClientType.fromId(getObjectAsInteger(CLIENT_TYPE));
	}

	@Override
	public boolean isValid() {
		String username = getUsername();
		String password = getPassword();
		ClientType clientType = getClientType();
		
		return username != null && password != null && clientType != null;
	}

}
