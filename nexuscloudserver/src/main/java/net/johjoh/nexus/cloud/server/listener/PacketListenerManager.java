package net.johjoh.nexus.cloud.server.listener;

import java.util.HashSet;

import net.johjoh.nexus.cloud.api.packet.Packet;
import net.johjoh.nexus.cloud.server.connection.AbstractClientConnection;

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
	
	public void unregisterAll() {
		listeners.clear();
	}
	
	public void onPacketReceive(Packet packet, AbstractClientConnection connection) {
		for(PacketListener l : listeners) l.onPacketReceive(new ClientPacket(packet, connection));
	}
	
	public void onPacketSend(Packet packet, AbstractClientConnection connection) {
		for(PacketListener l : listeners) l.onPacketSend(new ClientPacket(packet, connection));
	}
	
	public void onClientKeepAlive(AbstractClientConnection connection) {
		for(PacketListener l : listeners) l.onClientKeepAlive(connection);
	}
	
	public void onServerKeepAlive(AbstractClientConnection connection) {
		for(PacketListener l : listeners) l.onClientKeepAlive(connection);
	}
	
	public void onClientLogout(AbstractClientConnection connection, String reason) {
		for(PacketListener l : listeners) l.onClientLogout(connection, reason);
	}
	
	public void onServerLogout(AbstractClientConnection connection, String reason) {
		for(PacketListener l : listeners) l.onServerLogout(connection, reason);
	}
	
	public void onServerHandshake() {
		for(PacketListener l : listeners) l.onServerHandshake();
	}
	
	public void onClientLogin(String username) {
		for(PacketListener l : listeners) l.onClientLogin(username);
	}
	
	public void onServerResponse(String username) {
		for(PacketListener l : listeners) l.onServerResponse(username);
	}

}
