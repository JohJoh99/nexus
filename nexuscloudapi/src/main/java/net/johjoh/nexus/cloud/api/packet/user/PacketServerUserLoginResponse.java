package net.johjoh.nexus.cloud.api.packet.user;

import net.johjoh.nexus.cloud.api.packet.Packet;
import net.johjoh.nexus.cloud.api.packet.PacketType;

public class PacketServerUserLoginResponse extends Packet {

	private static final String RESPONSE = "response";
	
	public PacketServerUserLoginResponse() {
		super(PacketType.SERVER_USER_LOGIN_RESPONSE);
	}
	
	public PacketServerUserLoginResponse(String response) {
		super(PacketType.SERVER_USER_LOGIN_RESPONSE);
		setObject(RESPONSE, response);
	}
	
	public void setResponse(String response) {
		setObject(RESPONSE, response);
	}
	
	public String getResponse() {
		return getObjectAsString(RESPONSE);
	}

	@Override
	public boolean isValid() {
		String response = getResponse();
		
		return response != null;
	}

}
