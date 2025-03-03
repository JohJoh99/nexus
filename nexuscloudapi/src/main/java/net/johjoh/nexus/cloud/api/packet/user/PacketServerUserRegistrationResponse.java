package net.johjoh.nexus.cloud.api.packet.user;

import net.johjoh.nexus.cloud.api.packet.Packet;
import net.johjoh.nexus.cloud.api.packet.PacketType;

public class PacketServerUserRegistrationResponse extends Packet {

	private static final String SUCCESSFUL = "successful";
	
	public PacketServerUserRegistrationResponse() {
		super(PacketType.SERVER_USER_REGISTRATION_RESPONSE);
	}
	
	public PacketServerUserRegistrationResponse(boolean successful) {
		super(PacketType.SERVER_USER_REGISTRATION_RESPONSE);
		setObject(SUCCESSFUL, successful);
	}
	
	public void setSuccessful(boolean successful) {
		setObject(SUCCESSFUL, successful);
	}
	
	public boolean getSuccessful() {
		return getObjectAsBoolean(SUCCESSFUL);
	}

	@Override
	public boolean isValid() {
		Boolean successful = getSuccessful();
		
		return successful != null;
	}

}
