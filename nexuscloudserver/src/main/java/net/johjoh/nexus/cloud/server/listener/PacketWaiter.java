package net.johjoh.nexus.cloud.server.listener;

import net.johjoh.nexus.cloud.api.packet.PacketType;

public class PacketWaiter extends PacketListener {

	private final PacketType waitFor;
	private final String sender;
	private ClientPacket waitedFor = null;
	private int timeWaited = 0;
	
	public PacketWaiter(PacketType waitFor) {
		this.waitFor = waitFor;
		this.sender = null;
	}
	
	public PacketWaiter(PacketType waitFor, String sender) {
		this.waitFor = waitFor;
		this.sender = sender;
	}
	
	public ClientPacket wait(int timeout) {
		while(waitedFor == null) {
			try {
				Thread.sleep(250);
				timeWaited += 250;
			}
			catch (InterruptedException e) {}
			
			if(timeout != 0 && timeWaited >= timeout) break;
		}
		
		ClientPacket ret = waitedFor;
		waitedFor = null;
		timeWaited = 0;
		return ret;
	}
	
	@Override
	public void onPacketReceive(ClientPacket packet) {
		if(packet.getPacket().getPacketType() == waitFor) {
			if(sender != null && packet.getClientConnection().getName().equalsIgnoreCase(sender)) {
				return;
			}
			
			waitedFor = packet;
		}
	}

	@Override
	public void onPacketSend(ClientPacket packet) {}

}
