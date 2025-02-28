package net.johjoh.nexus.cloud.server.listener;

import net.johjoh.nexus.cloud.api.packet.Packet;
import net.johjoh.nexus.cloud.server.connection.AbstractClientConnection;

public class ClientPacket {
	
	private final Packet packet;
	private final AbstractClientConnection connection;
	
	public ClientPacket(Packet packet, AbstractClientConnection connection) {
		this.packet = packet;
		this.connection = connection;
	}
	
	public Packet getPacket() {
		return packet;
	}
	
	public AbstractClientConnection getClientConnection() {
		return connection;
	}

}
