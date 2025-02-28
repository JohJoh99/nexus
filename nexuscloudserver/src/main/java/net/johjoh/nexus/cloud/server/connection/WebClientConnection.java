package net.johjoh.nexus.cloud.server.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import net.johjoh.nexus.cloud.api.CloudSecurity.Encrypt;
import net.johjoh.nexus.cloud.api.client.ClientType;
import net.johjoh.nexus.cloud.api.packet.Packet;

public class WebClientConnection extends AbstractClientConnection {
	
	public WebClientConnection(String username, ClientType clientType, Encrypt encrypt, Socket clientSocket, DataOutputStream dataOutput, DataInputStream dataInput) {
		super(username, clientType, encrypt, clientSocket, dataOutput, dataInput);
	}

	@Override
	public void onPacketReceive(Packet p) {
		
	}

	@Override
	public String[] getInfo() {
		return new String[] {"No extra information available"};
	}

}
