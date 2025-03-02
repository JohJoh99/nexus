package net.johjoh.nexus.cloud.api.packet.user;

import net.johjoh.nexus.cloud.api.packet.Packet;
import net.johjoh.nexus.cloud.api.packet.PacketType;

public class PacketServerPasswordSaltResponse extends Packet {

	private static final String SALT = "salt";
	
	public PacketServerPasswordSaltResponse() {
		super(PacketType.SERVER_PASSWORD_SALT_RESPONSE);
	}
	
	public PacketServerPasswordSaltResponse(String salt) {
		super(PacketType.SERVER_PASSWORD_SALT_RESPONSE);
		setObject(SALT, salt);
	}
	
	public void setSalt(String salt) {
		setObject(SALT, salt);
	}
	
	public String getSalt() {
		return getObjectAsString(SALT);
	}

	@Override
	public boolean isValid() {
		String salt = getSalt();
		
		return salt != null;
	}

}
