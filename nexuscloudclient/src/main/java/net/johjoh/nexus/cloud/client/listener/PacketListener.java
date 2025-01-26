package net.johjoh.nexus.cloud.client.listener;

import net.johjoh.nexus.cloud.api.packet.Packet;

public abstract class PacketListener {
	
	public void register() {
		PacketListenerManager.getInstance().addListener(this);
	}
	
	public void unregister() {
		PacketListenerManager.getInstance().removeListener(this);
	}
	
	public abstract void onPacketReceive(Packet packet);
	public abstract void onPacketSend(Packet packet);

}
