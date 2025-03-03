package net.johjoh.nexus.cloud.api.packet.data;

import java.util.UUID;

import net.johjoh.nexus.cloud.api.packet.Packet;
import net.johjoh.nexus.cloud.api.packet.PacketType;

public class PacketClientDataRequest extends Packet {

	private static final String SESSIONUUID = "sessionUUID";
	private static final String REQUEST = "request";
	
	public PacketClientDataRequest() {
		super(PacketType.CLIENT_DATA_REQUEST);
	}
	
	public PacketClientDataRequest(UUID sessionUUID, String request) {
		super(PacketType.CLIENT_DATA_REQUEST);
		setSessionUUID(sessionUUID);
		setRequest(request);
	}
	
	public void setSessionUUID(UUID sessionUUID) {
		setObject(SESSIONUUID, sessionUUID);
	}
	
	public void setRequest(String request) {
		setObject(REQUEST, request);
	}
	
	public UUID getSessionUUID() {
		return getObjectAsUUID(SESSIONUUID);
	}
	
	public String getRequest() {
		return getObjectAsString(REQUEST);
	}

	@Override
	public boolean isValid() {
		UUID sessionUUID = getSessionUUID();
		String request = getRequest();
		
		return sessionUUID != null && request != null;
	}

}
