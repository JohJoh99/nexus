package net.johjoh.nexus.cloud.server.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.UUID;

import org.hibernate.Session;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import net.johjoh.nexus.api.sql.HibernateUtil;
import net.johjoh.nexus.api.tables.NexusUser;
import net.johjoh.nexus.cloud.api.CloudSecurity.Encrypt;
import net.johjoh.nexus.cloud.api.client.ClientType;
import net.johjoh.nexus.cloud.api.packet.Packet;
import net.johjoh.nexus.cloud.api.packet.data.PacketClientDataRequest;
import net.johjoh.nexus.cloud.api.packet.data.PacketServerDataResponse;
import net.johjoh.nexus.cloud.api.packet.user.PacketClientRequestPasswordSalt;
import net.johjoh.nexus.cloud.api.packet.user.PacketClientUserLogin;
import net.johjoh.nexus.cloud.api.packet.user.PacketServerPasswordSaltResponse;
import net.johjoh.nexus.cloud.api.packet.user.PacketServerUserLoginResponse;

public class NexusDesktopConnection extends AbstractClientConnection {
	
	private UUID sessionId;
	
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
		else if(p instanceof PacketClientRequestPasswordSalt) {
			PacketClientRequestPasswordSalt pcrps = (PacketClientRequestPasswordSalt) p;
			String username = pcrps.getUsername();
			
			NexusUser user = getNexusUser(username);
			if(user == null || user.getPasswordSalt() == null || user.getPasswordSalt() == "") {
				PacketServerUserLoginResponse psulr = new PacketServerUserLoginResponse(false);
				sendPacket(psulr);
				return;
			}
			
			PacketServerPasswordSaltResponse pspsr = new PacketServerPasswordSaltResponse(user.getPasswordSalt());
			sendPacket(pspsr);
		}
		else if(p instanceof PacketClientUserLogin) {
			PacketClientUserLogin pcul = (PacketClientUserLogin) p ;
			String username = pcul.getUsername();
			String passwordSaltHash = pcul.getPasswordHash();

			PacketServerUserLoginResponse psulr = null;
			
			NexusUser user = getNexusUser(username);
			if(user == null || user.getPasswordHash() == null || user.getPasswordHash() == "") {
				psulr = new PacketServerUserLoginResponse(false);
				sendPacket(psulr);
				return;
			}
			
			if(passwordSaltHash.equals(user.getPasswordHash())) {
				sessionId = UUID.randomUUID();
				psulr = new PacketServerUserLoginResponse(true, sessionId);
			}
			else {
				psulr = new PacketServerUserLoginResponse(false);
			}
			sendPacket(psulr);
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
	
	private NexusUser getNexusUser(String username) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<NexusUser> criteria = builder.createQuery(NexusUser.class);
		Root<NexusUser> root = criteria.from(NexusUser.class);
		criteria.select(root).where(builder.equal(root.get("username"), username));
		NexusUser user = session.createQuery(criteria).uniqueResult();
		
		session.close();
		
		return user;
	}

}
