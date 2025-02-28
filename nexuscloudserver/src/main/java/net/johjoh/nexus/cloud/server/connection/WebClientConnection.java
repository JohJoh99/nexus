package net.johjoh.nexus.cloud.server.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import net.johjoh.cloud.packet.general.PacketServerGeneralStart;
import net.johjoh.nexus.cloud.api.CloudSecurity.Encrypt;
import net.johjoh.nexus.cloud.api.client.ClientType;
import net.johjoh.nexus.cloud.api.packet.Packet;
import net.johjoh.nexus.cloud.api.packet.general.PacketClientGeneralLogin;
import net.johjoh.nexus.cloud.server.CloudServer;
import net.johjoh.nexus.cloud.server.ServerHandler;

public class WebClientConnection extends AbstractClientConnection {
	
	public WebClientConnection(String username, ClientType clientType, Encrypt encrypt, Socket clientSocket, DataOutputStream dataOutput, DataInputStream dataInput) {
		super(username, clientType, encrypt, clientSocket, dataOutput, dataInput);
	}

	@Override
	public void onPacketReceive(Packet p) {
		if(p instanceof PacketClientGeneralLogin) {
			CloudServer.getInstance().broadcast(p, ClientType.HUB);
		}
		else if(p instanceof PacketServerGeneralStart) {
			String sn = ((PacketServerGeneralStart) p).getServerName();
			
			ServerHandler.getInstance().checkServerStart(sn);
		}
	}

	@Override
	public String[] getInfo() {
		return new String[] {"No extra information available"};
	}

}
