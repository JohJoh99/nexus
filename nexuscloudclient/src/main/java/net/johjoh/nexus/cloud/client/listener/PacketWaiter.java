package net.johjoh.nexus.cloud.client.listener;

import net.johjoh.nexus.cloud.api.packet.Packet;
import net.johjoh.nexus.cloud.api.packet.PacketType;

public class PacketWaiter extends PacketListener {

	private final PacketType waitFor;
	private Packet waitedFor = null;
	private int timeWaited = 0;
	
	public PacketWaiter(PacketType waitFor) {
		this.waitFor = waitFor;
	}
	
	public Packet wait(int timeout) {
		while(waitedFor == null) {
			try {
				Thread.sleep(250);
				timeWaited += 250;
			}
			catch (InterruptedException e) {}
			
			if(timeout != 0 && timeWaited >= timeout) break;
		}
		
		Packet ret = waitedFor;
		waitedFor = null;
		timeWaited = 0;
		return ret;
	}
	
	@Override
	public void onPacketReceive(Packet packet) {
		if(packet.getPacketType() == waitFor) {
			waitedFor = packet;
		}
	}

	@Override
	public void onPacketSend(Packet packet) {}

}
