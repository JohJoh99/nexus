package net.johjoh.nexus.cloud.api.packet.data;

import java.util.UUID;

import net.johjoh.nexus.cloud.api.packet.Packet;
import net.johjoh.nexus.cloud.api.packet.PacketType;

public class PacketClientDataUpdate extends Packet {

	private static final String SESSIONUUID = "sessionUUID";
	private static final String UPDATE = "update";
	
	public PacketClientDataUpdate() {
		super(PacketType.CLIENT_DATA_UPDATE);
	}
	
	public PacketClientDataUpdate(UUID sessionUUID, String update) {
		super(PacketType.CLIENT_DATA_UPDATE);
		setSessionUUID(sessionUUID);
		setUpdate(update);
	}
	
	public void setSessionUUID(UUID sessionUUID) {
		setObject(SESSIONUUID, sessionUUID);
	}
	
	public void setUpdate(String update) {
		setObject(UPDATE, update);
	}
	
	public String getSessionUUID() {
		return getObjectAsString(SESSIONUUID);
	}
	
	public String getUpdate() {
		return getObjectAsString(UPDATE);
	}

	@Override
	public boolean isValid() {
		String sessionUUID = getSessionUUID();
		String update = getUpdate();
		
		return sessionUUID != null && update != null;
	}

}
