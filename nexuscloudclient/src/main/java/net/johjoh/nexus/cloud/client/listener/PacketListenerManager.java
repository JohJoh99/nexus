package net.johjoh.nexus.cloud.client.listener;

import java.util.HashSet;

import net.johjoh.nexus.cloud.api.packet.Packet;

public class PacketListenerManager {
	
	private static PacketListenerManager instance;
	
	public static PacketListenerManager getInstance() {
		if(instance == null) instance = new PacketListenerManager();
		return instance;
	}
	
	private HashSet<PacketListener> listeners = new HashSet<PacketListener>();
	
	private PacketListenerManager() {
		if(instance != null) throw new IllegalStateException("PacketListenerManager has already been created!");
		instance = this;
	}
	
	protected void addListener(PacketListener listener) {
		listeners.add(listener);
	}
	
	protected void removeListener(PacketListener listener) {
		listeners.remove(listener);
	}
	
	public void onPacketReceive(Packet packet) {
		for(PacketListener l : listeners) l.onPacketReceive(packet);
	}
	
	public void onPacketSend(Packet packet) {
		for(PacketListener l : listeners) l.onPacketSend(packet);
	}

}
