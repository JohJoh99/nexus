package net.johjoh.nexus.cloud.server.listener;

import net.johjoh.nexus.cloud.server.connection.AbstractClientConnection;

public abstract class PacketListener {
	
	public void register() {
		PacketListenerManager.getInstance().addListener(this);
	}
	
	public void unregister() {
		PacketListenerManager.getInstance().removeListener(this);
	}
	
	public abstract void onPacketReceive(ClientPacket packet);
	public abstract void onPacketSend(ClientPacket packet);
	public void onClientKeepAlive(AbstractClientConnection connection) {}
	public void onServerKeepAlive(AbstractClientConnection connection) {}
	public void onClientLogout(AbstractClientConnection connection, String reason) {}
	public void onServerLogout(AbstractClientConnection connection, String reason) {}
	public void onServerHandshake() {}
	public void onClientLogin(String username) {}
	public void onServerResponse(String username) {}

}
