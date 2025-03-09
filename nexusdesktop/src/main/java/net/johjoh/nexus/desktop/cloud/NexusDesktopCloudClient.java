package net.johjoh.nexus.desktop.cloud;

import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;
import net.johjoh.nexus.cloud.api.client.ClientType;
import net.johjoh.nexus.cloud.api.packet.Packet;
import net.johjoh.nexus.cloud.api.packet.data.PacketClientDataRequest;
import net.johjoh.nexus.cloud.api.packet.data.PacketServerDataResponse;
import net.johjoh.nexus.cloud.api.packet.user.PacketClientUserLogin;
import net.johjoh.nexus.cloud.api.packet.user.PacketServerPasswordSaltResponse;
import net.johjoh.nexus.cloud.api.packet.user.PacketServerUserLoginResponse;
import net.johjoh.nexus.cloud.api.packet.user.PacketServerUserRegistrationResponse;
import net.johjoh.nexus.cloud.client.CloudClient;
import net.johjoh.nexus.desktop.NexusDesktop;
import net.johjoh.nexus.desktop.util.CalendarUtil;
import net.johjoh.nexus.desktop.util.ListUtil;
import net.johjoh.nexus.desktop.util.LoginUtil;

public class NexusDesktopCloudClient extends CloudClient {
	
	private UUID sessionId;
	
	public NexusDesktopCloudClient(String username, String password, String hostname, int port) {
		super(username, ClientType.NEXUS_DESKTOP, password, hostname, port);
	}

	@Override
	public void onPacketReceive(Packet p) {
		//switch(p) {
			//case PacketServerDataResponse psdr -> {
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
            else if(table.equalsIgnoreCase("list")) {
            	
            	ListUtil.loadListFromNode(jsonNode);
            }
	        
	        
		}
		else if(p instanceof PacketServerUserLoginResponse) {
			PacketServerUserLoginResponse psulr = (PacketServerUserLoginResponse) p;
			if(!psulr.getSuccessful() || psulr.getSessionId() == null) {
				NexusDesktop.getLoginPane().getLoginCenterPane().loginFailed();
				return;
			}
			
			sessionId = psulr.getSessionId();

			Platform.runLater(() -> {
	            NexusDesktop.getLoginPane().setVisible(false);
	            NexusDesktop.getOverlayPane().setVisible(false);
	            NexusDesktop.getMainMenuPane().getMessageFrame().setTitle("Willkommen " + LoginUtil.getUsername());
			});
            
            String calendarRequest = "{\"table\":\"calendar\",\"request_type\":\"init\"}";
            PacketClientDataRequest pcdr = new PacketClientDataRequest(sessionId, calendarRequest);
            sendPacket(pcdr);
            
            String listRequest = "{\"table\":\"list\",\"request_type\":\"init\"}";
            pcdr = new PacketClientDataRequest(sessionId, listRequest);
            sendPacket(pcdr);
			
		}
		else if(p instanceof PacketServerPasswordSaltResponse) {
			PacketServerPasswordSaltResponse pcrps = (PacketServerPasswordSaltResponse) p;
			String salt = pcrps.getSalt();
			
			String passwordSaltHash = LoginUtil.hashPassword(LoginUtil.getPassword(), salt);
			
			PacketClientUserLogin pcul = new PacketClientUserLogin(LoginUtil.getUsername(), passwordSaltHash);
			sendPacket(pcul);
		}
		else if(p instanceof PacketServerUserRegistrationResponse) {
			PacketServerUserRegistrationResponse psurr = (PacketServerUserRegistrationResponse) p;
			if(psurr.getSuccessful()) {
				NexusDesktop.getRegisterPane().setVisible(false);
				
				NexusDesktop.showAlert(AlertType.INFORMATION, "Registrierung", "Erfolgreich! Du kannst dich jetzt anmelden.");
				
				/*Alert alert = new Alert(AlertType.INFORMATION);
		        alert.setTitle("Registrierung");
		        alert.setHeaderText(null);
		        alert.setContentText("Erfolgreich! Du kannst dich jetzt anmelden.");

		        alert.showAndWait();*/
			}
			else {
				NexusDesktop.showAlert(AlertType.ERROR, "Registrierung", "Nutzer existiert bereits oder konnte nicht erstellt werden!");
				/*Alert alert = new Alert(AlertType.ERROR);
		        alert.setTitle("Registrierung");
		        alert.setHeaderText(null);
		        alert.setContentText("Nutzer existiert bereits oder konnte nicht erstellt werden!");

		        alert.showAndWait();*/
			}
		}
	}
	
	@Override
	public void onConnect() {
		Platform.runLater(() -> {
			if(NexusDesktop.getLoginPane() != null)
				NexusDesktop.getLoginPane().getServerSettingsLoginPane().setConnected();
		});
	}
	
	public UUID getSessionId() {
		return this.sessionId;
	}

}
