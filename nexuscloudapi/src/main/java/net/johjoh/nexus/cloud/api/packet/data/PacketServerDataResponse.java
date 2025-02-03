package net.johjoh.nexus.cloud.api.packet.data;

import java.util.UUID;

import net.johjoh.nexus.cloud.api.packet.Packet;
import net.johjoh.nexus.cloud.api.packet.PacketType;

public class PacketServerDataResponse extends Packet {

	private static final String SESSIONUUID = "sessionUUID";
	private static final String RESPONSE = "response";
	
	public PacketServerDataResponse() {
		super(PacketType.SERVER_DATA_RESPONSE);
	}
	
	public PacketServerDataResponse(UUID sessionUUID, String response) {
		super(PacketType.SERVER_DATA_RESPONSE);
		setSessionUUID(sessionUUID);
		setResponse(response);
	}
	
	public void setSessionUUID(UUID sessionUUID) {
		setObject(SESSIONUUID, sessionUUID);
	}
	
	public void setResponse(String response) {
		setObject(RESPONSE, response);
	}
	
	public String getSessionUUID() {
		return getObjectAsString(SESSIONUUID);
	}
	
	public String getResponse() {
		return getObjectAsString(RESPONSE);
	}

	@Override
	public boolean isValid() {
		String sessionUUID = getSessionUUID();
		String request = getResponse();
		
		return sessionUUID != null && request != null;
	}

}
