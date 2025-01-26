package net.johjoh.nexus.cloud.api.packet.general;

import net.johjoh.nexus.cloud.api.packet.Packet;
import net.johjoh.nexus.cloud.api.packet.PacketType;

public class PacketServerGeneralHandshake extends Packet {

	public PacketServerGeneralHandshake() {
		super(PacketType.SERVER_GENERAL_HANDSHAKE);
	}

	@Override
	public boolean isValid() {
		return true;
	}

}
