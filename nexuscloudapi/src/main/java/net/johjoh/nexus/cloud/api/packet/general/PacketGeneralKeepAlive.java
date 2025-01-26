package net.johjoh.nexus.cloud.api.packet.general;

import net.johjoh.nexus.cloud.api.packet.Packet;
import net.johjoh.nexus.cloud.api.packet.PacketType;

public class PacketGeneralKeepAlive extends Packet {

	public PacketGeneralKeepAlive() {
		super(PacketType.GENERAL_KEEP_ALIVE);
	}

	@Override
	public boolean isValid() {
		return true;
	}

}
