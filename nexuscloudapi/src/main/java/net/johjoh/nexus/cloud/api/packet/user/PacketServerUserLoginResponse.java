package net.johjoh.nexus.cloud.api.packet.user;

import java.util.UUID;

import net.johjoh.nexus.cloud.api.packet.Packet;
import net.johjoh.nexus.cloud.api.packet.PacketType;

public class PacketServerUserLoginResponse extends Packet {

	private static final String SUCCESSFUL = "successful";
	private static final String SESSION_ID = "sessionId";
	
	public PacketServerUserLoginResponse() {
		super(PacketType.SERVER_USER_LOGIN_RESPONSE);
	}
	
	public PacketServerUserLoginResponse(boolean successful, UUID sessionId) {
		super(PacketType.SERVER_USER_LOGIN_RESPONSE);
		setObject(SUCCESSFUL, successful);
		setObject(SESSION_ID, sessionId);
	}
	
	public PacketServerUserLoginResponse(boolean successful) {
		super(PacketType.SERVER_USER_LOGIN_RESPONSE);
		setObject(SUCCESSFUL, successful);
		setObject(SESSION_ID, new UUID(0L, 0L));
	}
	
	public void setSuccessful(boolean successful) {
		setObject(SUCCESSFUL, successful);
	}
	
	public void setSessionId(UUID sessionId) {
		setObject(SESSION_ID, sessionId);
	}
	
	public boolean getSuccessful() {
		return getObjectAsBoolean(SUCCESSFUL);
	}
	
	public UUID getSessionId() {
		return getObjectAsUUID(SESSION_ID);
	}

	@Override
	public boolean isValid() {
		Boolean successful = getSuccessful();
		UUID sessionId = getSessionId();
		
		return successful != null && sessionId != null;
	}

}
