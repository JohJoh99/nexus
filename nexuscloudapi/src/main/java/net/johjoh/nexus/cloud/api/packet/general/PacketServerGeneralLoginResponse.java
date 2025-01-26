package net.johjoh.nexus.cloud.api.packet.general;

import net.johjoh.nexus.cloud.api.packet.Packet;
import net.johjoh.nexus.cloud.api.packet.PacketType;

public class PacketServerGeneralLoginResponse extends Packet {

	private static final String RESPONSE = "response";
	
	public PacketServerGeneralLoginResponse() {
		super(PacketType.SERVER_GENERAL_LOGIN_RESPONSE);
	}
	
	public PacketServerGeneralLoginResponse(String response) {
		super(PacketType.SERVER_GENERAL_LOGIN_RESPONSE);
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
