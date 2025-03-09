package net.johjoh.nexus.cloud.server.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import net.johjoh.nexus.api.sql.HibernateUtil;
import net.johjoh.nexus.api.tables.NexusCalendar;
import net.johjoh.nexus.api.tables.NexusCalendarLine;
import net.johjoh.nexus.api.tables.NexusCalendarPermission;
import net.johjoh.nexus.api.tables.NexusList;
import net.johjoh.nexus.api.tables.NexusListLine;
import net.johjoh.nexus.api.tables.NexusUser;
import net.johjoh.nexus.cloud.api.CloudSecurity.Encrypt;
import net.johjoh.nexus.cloud.api.client.ClientType;
import net.johjoh.nexus.cloud.api.packet.Packet;
import net.johjoh.nexus.cloud.api.packet.data.PacketClientDataRequest;
import net.johjoh.nexus.cloud.api.packet.data.PacketClientDataUpdate;
import net.johjoh.nexus.cloud.api.packet.data.PacketServerDataResponse;
import net.johjoh.nexus.cloud.api.packet.user.PacketClientRequestPasswordSalt;
import net.johjoh.nexus.cloud.api.packet.user.PacketClientUserLogin;
import net.johjoh.nexus.cloud.api.packet.user.PacketClientUserRegistration;
import net.johjoh.nexus.cloud.api.packet.user.PacketServerPasswordSaltResponse;
import net.johjoh.nexus.cloud.api.packet.user.PacketServerUserLoginResponse;
import net.johjoh.nexus.cloud.api.packet.user.PacketServerUserRegistrationResponse;

public class NexusDesktopConnection extends AbstractClientConnection {
	
	public static HashMap<UUID, Integer> users = new HashMap<UUID, Integer>();
	
	private UUID sessionId;
	
	public NexusDesktopConnection(String username, ClientType clientType, Encrypt encrypt, Socket clientSocket, DataOutputStream dataOutput, DataInputStream dataInput) {
		super(username, clientType, encrypt, clientSocket, dataOutput, dataInput);
	}

	@Override
	public void onPacketReceive(Packet p) {
		if(p instanceof PacketClientDataRequest) {
			PacketClientDataRequest pcdr = (PacketClientDataRequest) p;
			UUID sessionUUID = pcdr.getSessionUUID();
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
	        	
	        	List<JsonNode> calendars = getUserCalendarsNode(users.get(sessionUUID));
	        	for(JsonNode response : calendars) {
	        		PacketServerDataResponse psdr = new PacketServerDataResponse(sessionUUID, response.toString());
	        		sendPacket(psdr);
	        	}
	        }
	        else if(table.equalsIgnoreCase("list")) {
	        	
	        	List<JsonNode> lists = getUserListsNode(users.get(sessionUUID));
	        	for(JsonNode response : lists) {
	        		PacketServerDataResponse psdr = new PacketServerDataResponse(sessionUUID, response.toString());
	        		sendPacket(psdr);
	        	}
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
				users.put(sessionId, user.getId());
				psulr = new PacketServerUserLoginResponse(true, sessionId);
			}
			else {
				psulr = new PacketServerUserLoginResponse(false);
			}
			sendPacket(psulr);
		}
		else if(p instanceof PacketClientUserRegistration) {
			PacketClientUserRegistration pcur = (PacketClientUserRegistration) p ;
			if(getNexusUser(pcur.getUsername()) != null) {
				PacketServerUserRegistrationResponse psurr = new PacketServerUserRegistrationResponse(false);
				sendPacket(psurr);
				return;
			}
			
			if(createNexusUser(pcur.getUsername(), pcur.getPassword())) {
				PacketServerUserRegistrationResponse psurr = new PacketServerUserRegistrationResponse(true);
				sendPacket(psurr);
			}
		}
		else if(p instanceof PacketClientDataUpdate) {
			PacketClientDataUpdate pcdu = (PacketClientDataUpdate) p;
			String update = pcdu.getUpdate();
			
			JsonNode jsonNode = null;

	        ObjectMapper objectMapper = new ObjectMapper();
	        try {
	            jsonNode = objectMapper.readTree(update);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return;
	        }
	        
	        String table = jsonNode.get("table").asText();
            String type = jsonNode.get("type").asText();
            if(table.equalsIgnoreCase("list") && type.equalsIgnoreCase("line")) {

                JsonNode contentNode = jsonNode.get("content");
                int listId = contentNode.get("listId").asInt();
                String title = contentNode.get("title").asText();
                String details = contentNode.get("details").asText();


        		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        		Session session = sessionFactory.openSession();
        		
                try {
                    session.beginTransaction();

                    NexusListLine line = new NexusListLine(listId, title, false, details);
                    session.persist(line);

                    session.getTransaction().commit();
                } catch (Exception e) {
                    if (session.getTransaction() != null) {
                        session.getTransaction().rollback();
                    }
                    e.printStackTrace();
                } finally {
                    session.close();
                }
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
		users.remove(sessionId);
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
	
	private boolean createNexusUser(String username, String password) {
		boolean success = false;

		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		
        try {
            session.beginTransaction();
            
            String salt = generateSalt(16);

            NexusUser user = new NexusUser(username, hashPassword(password, salt), salt);
            session.persist(user);
            
            NexusCalendar calendar = new NexusCalendar("Mein Kalender", user.getId());
            session.persist(calendar);
            
            NexusList list = new NexusList("TODO", user.getId());
            session.persist(list);
            list = new NexusList("Einkaufsliste", user.getId());
            session.persist(list);

            session.getTransaction().commit();
            
            success = true;
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            success = false;
        } finally {
            session.close();
        }
        
        return success;
	}
	
	private List<JsonNode> getUserCalendarsNode(int userId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		
		//	TODO: Add other calendar
		/*CriteriaQuery<NexusCalendarPermission> criteria = builder.createQuery(NexusCalendarPermission.class);
		Root<NexusCalendarPermission> root = criteria.from(NexusCalendarPermission.class);
		criteria.select(root).where(builder.equal(root.get("ownerId"), userId));
		List<NexusCalendarPermission> calendars = session.createQuery(criteria).getResultList();*/
		
		CriteriaQuery<NexusCalendar> criteria = builder.createQuery(NexusCalendar.class);
		Root<NexusCalendar> root = criteria.from(NexusCalendar.class);
		criteria.select(root).where(builder.equal(root.get("ownerId"), userId));
		List<NexusCalendar> calendars = session.createQuery(criteria).getResultList();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
		List<JsonNode> calendarNodes = new ArrayList<JsonNode>();
		
		for(NexusCalendar calendar : calendars) {
			ObjectMapper mapper = new ObjectMapper();
			ObjectNode rootNode = mapper.createObjectNode();
			rootNode.put("table", "calendar");

            ObjectNode headContentNode = mapper.createObjectNode();
            headContentNode.put("id", calendar.getId());
            headContentNode.put("title", calendar.getName());
            headContentNode.put("ownerId", calendar.getOwnerId());
            rootNode.set("headcontent", headContentNode);
            
            ArrayNode lineContentArray = mapper.createArrayNode();
            
    		CriteriaQuery<NexusCalendarLine> criteria2 = builder.createQuery(NexusCalendarLine.class);
    		Root<NexusCalendarLine> root2 = criteria2.from(NexusCalendarLine.class);
    		criteria2.select(root2).where(builder.equal(root2.get("calendarId"), calendar.getId()));
    		List<NexusCalendarLine> calendarLines = session.createQuery(criteria2).getResultList();
            
            for(NexusCalendarLine line : calendarLines) {
                ObjectNode lineContent2 = mapper.createObjectNode();
                lineContent2.put("id", line.getId());
                lineContent2.put("name", line.getTitle());
                lineContent2.put("place", line.getPlace());
                lineContent2.put("all_day", line.getAllDay());
                lineContent2.put("start_datetime", line.getStartDateTime().format(formatter));
                lineContent2.put("end_datetime", line.getEndDateTime().format(formatter));
                lineContentArray.add(lineContent2);
            }
            
            rootNode.set("linecontent", lineContentArray);
            
            calendarNodes.add(rootNode);
		}
		
		session.close();
		
		return calendarNodes;
	}
	
	private List<JsonNode> getUserListsNode(int userId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		
		//	TODO: Add other calendar
		/*CriteriaQuery<NexusCalendarPermission> criteria = builder.createQuery(NexusCalendarPermission.class);
		Root<NexusCalendarPermission> root = criteria.from(NexusCalendarPermission.class);
		criteria.select(root).where(builder.equal(root.get("owner_id"), userId));
		List<NexusCalendarPermission> calendars = session.createQuery(criteria).getResultList();*/
		
		CriteriaQuery<NexusList> criteria = builder.createQuery(NexusList.class);
		Root<NexusList> root = criteria.from(NexusList.class);
		criteria.select(root).where(builder.equal(root.get("ownerId"), userId));
		List<NexusList> lists = session.createQuery(criteria).getResultList();
		
		List<JsonNode> calendarNodes = new ArrayList<JsonNode>();
		
		for(NexusList list : lists) {
			ObjectMapper mapper = new ObjectMapper();
			ObjectNode rootNode = mapper.createObjectNode();
			rootNode.put("table", "list");

            ObjectNode headContentNode = mapper.createObjectNode();
            headContentNode.put("id", list.getId());
            headContentNode.put("title", list.getTitle());
            headContentNode.put("ownerId", list.getOwnerId());
            rootNode.set("headcontent", headContentNode);
            
            ArrayNode lineContentArray = mapper.createArrayNode();
            
    		CriteriaQuery<NexusListLine> criteria2 = builder.createQuery(NexusListLine.class);
    		Root<NexusListLine> root2 = criteria2.from(NexusListLine.class);
    		criteria2.select(root2).where(builder.equal(root2.get("listId"), list.getId()));
    		List<NexusListLine> listLines = session.createQuery(criteria2).getResultList();
            
            for(NexusListLine line : listLines) {
                ObjectNode lineContent2 = mapper.createObjectNode();
                lineContent2.put("id", line.getId());
                lineContent2.put("name", line.getTitle());
                lineContent2.put("done", line.getDone());
                lineContent2.put("details", line.getDetails());
                lineContentArray.add(lineContent2);
            }
            
            rootNode.set("linecontent", lineContentArray);
            
            calendarNodes.add(rootNode);
		}
		
		session.close();
		
		return calendarNodes;
	}
	
	private void updateCalendarEntry(JsonNode rootNode) {
		try {
			String table = rootNode.path("table").asText();
	        String type = rootNode.path("type").asText();
	        JsonNode contentNode = rootNode.path("content");
	
	        // Lese die Felder aus dem contentNode
	        int id = contentNode.path("id").asInt();
	        String name = contentNode.path("name").asText();
	        String place = contentNode.path("place").asText();
	        boolean allDay = contentNode.path("all_day").asBoolean();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
	        LocalDateTime startDateTime = LocalDateTime.parse(contentNode.path("start_datetime").asText(), formatter);
	        LocalDateTime endDateTime = LocalDateTime.parse(contentNode.path("end_datetime").asText(), formatter);
	        
	        Session session = HibernateUtil.getSessionFactory().openSession();
	        NexusCalendarLine line = new NexusCalendarLine();
	        //	TODO: Eintrag richtif idendifizieren
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
    private String generateSalt(int length) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[length];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
	
	private String hashPassword(String passwordVar, String salt) {
        try {
            String combined = passwordVar + salt;
            
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(combined.getBytes(StandardCharsets.UTF_8));
            
            return Base64.getEncoder().encodeToString(hashedBytes);
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Fehler beim Hashen des Passworts", e);
        }
    }

}
