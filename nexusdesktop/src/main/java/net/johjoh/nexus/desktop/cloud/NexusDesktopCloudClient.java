package net.johjoh.nexus.desktop.cloud;

import net.johjoh.nexus.cloud.api.client.ClientType;
import net.johjoh.nexus.cloud.api.packet.Packet;
import net.johjoh.nexus.cloud.api.packet.data.PacketServerDataResponse;
import net.johjoh.nexus.cloud.api.packet.user.PacketServerUserLoginResponse;
import net.johjoh.nexus.cloud.client.CloudClient;

public class NexusDesktopCloudClient extends CloudClient {
	
	public NexusDesktopCloudClient(String username, String password, String hostname, int port) {
		super(username, ClientType.NEXUS_DESKTOP, password, hostname, port);
	}

	@Override
	public void onPacketReceive(Packet p) {
		if(p instanceof PacketServerDataResponse) {
			
		}
		else if(p instanceof PacketServerUserLoginResponse) {
			
		}
	}

}
