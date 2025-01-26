package net.johjoh.nexus.cloud.api.packet.general;

import net.johjoh.nexus.cloud.api.packet.Packet;
import net.johjoh.nexus.cloud.api.packet.PacketType;

public class PacketGeneralLogout extends Packet {

	private static final String MESSAGE = "message";
	
	public PacketGeneralLogout() {
		super(PacketType.GENERAL_LOGOUT);
	}
	
	public PacketGeneralLogout(String message) {
		super(PacketType.GENERAL_LOGOUT);
		setObject(MESSAGE, message);
	}
	
	public void setMessage(String message) {
		setObject(MESSAGE, message);
	}
	
	public String getMessage() {
		return getObjectAsString(MESSAGE);
	}

	@Override
	public boolean isValid() {
		return true;
	}

}
