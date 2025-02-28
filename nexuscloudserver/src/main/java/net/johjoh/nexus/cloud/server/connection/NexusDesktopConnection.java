package net.johjoh.nexus.cloud.server.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.johjoh.nexus.cloud.api.CloudSecurity.Encrypt;
import net.johjoh.nexus.cloud.api.client.ClientType;
import net.johjoh.nexus.cloud.api.packet.Packet;
import net.johjoh.nexus.cloud.api.packet.data.PacketClientDataRequest;
import net.johjoh.nexus.cloud.api.packet.data.PacketServerDataResponse;
import net.johjoh.nexus.cloud.server.CloudServer;

public class NexusDesktopConnection extends AbstractClientConnection {
	
	public NexusDesktopConnection(String username, ClientType clientType, Encrypt encrypt, Socket clientSocket, DataOutputStream dataOutput, DataInputStream dataInput) {
		super(username, clientType, encrypt, clientSocket, dataOutput, dataInput);
	}

	@Override
	public void onPacketReceive(Packet p) {
		if(p instanceof PacketClientDataRequest) {
			PacketClientDataRequest pcdr = (PacketClientDataRequest) p;
			String sessionUUID = pcdr.getSessionUUID();
			String request = pcdr.getRequest();
			
			JsonNode jsonNode = null;

	        ObjectMapper objectMapper = new ObjectMapper();
	        try {
	            jsonNode = objectMapper.readTree(request);
	        } catch (Exception e) {
	            e.printStackTrace();
	            //	TODO: Send Error Packet?
	            return;
	        }
	        
	        String table = jsonNode.get("table").asText();
	        if(table.equalsIgnoreCase("calendar")) {
	        	String response = null;
	        	
	        	//	TODO: SQL-Abfrage
	        	
	        	PacketServerDataResponse psdr = new PacketServerDataResponse(UUID.fromString(sessionUUID), response);
	        	sendPacket(psdr);
	        }
			
		}
	}

	@Override
	public String[] getInfo() {
		return new String[] {"No extra information available"};
	}
	
	@Override
	public void onConnect() {
		
	}
	
	@Override
	public void afterDisconnect() {
		
	}

}
