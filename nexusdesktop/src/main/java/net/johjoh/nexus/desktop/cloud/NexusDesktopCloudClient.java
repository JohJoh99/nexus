package net.johjoh.nexus.desktop.cloud;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.johjoh.nexus.cloud.api.client.ClientType;
import net.johjoh.nexus.cloud.api.packet.Packet;
import net.johjoh.nexus.cloud.api.packet.data.PacketServerDataResponse;
import net.johjoh.nexus.cloud.api.packet.user.PacketServerUserLoginResponse;
import net.johjoh.nexus.cloud.client.CloudClient;
import net.johjoh.nexus.desktop.util.CalendarUtil;

public class NexusDesktopCloudClient extends CloudClient {
	
	public NexusDesktopCloudClient(String username, String password, String hostname, int port) {
		super(username, ClientType.NEXUS_DESKTOP, password, hostname, port);
	}

	@Override
	public void onPacketReceive(Packet p) {
		if(p instanceof PacketServerDataResponse) {
			PacketServerDataResponse psdr = (PacketServerDataResponse) p;
			String response = psdr.getResponse();
			
			JsonNode jsonNode = null;

	        ObjectMapper objectMapper = new ObjectMapper();
	        try {
	            jsonNode = objectMapper.readTree(response);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return;
	        }
	        
	        String table = jsonNode.get("table").asText();
            if(table.equalsIgnoreCase("calendar")) {
            	CalendarUtil.loadCalendarFromNode(jsonNode);
            }
	        
	        
	        
		}
		else if(p instanceof PacketServerUserLoginResponse) {
			PacketServerUserLoginResponse psulr = (PacketServerUserLoginResponse) p;
			
		}
	}
	
	@Override
	public void onConnect() {
		
	}

}
