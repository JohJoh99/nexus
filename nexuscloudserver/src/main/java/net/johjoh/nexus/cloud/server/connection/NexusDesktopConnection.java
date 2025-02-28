package net.johjoh.nexus.cloud.server.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Collection;

import net.johjoh.nexus.cloud.api.CloudSecurity.Encrypt;
import net.johjoh.nexus.cloud.api.client.ClientType;
import net.johjoh.nexus.cloud.api.packet.Packet;
import net.johjoh.nexus.cloud.api.packet.general.PacketClientGeneralBroadcast;
import net.johjoh.nexus.cloud.server.CloudServer;
import net.johjoh.nexus.cloud.server.ServerHandler;

public class NexusDesktopConnection extends AbstractClientConnection {
	
	private int playerCount;
	private int maxPlayers;
	
	public NexusDesktopConnection(String username, ClientType clientType, Encrypt encrypt, Socket clientSocket, DataOutputStream dataOutput, DataInputStream dataInput) {
		super(username, clientType, encrypt, clientSocket, dataOutput, dataInput);
	}

	@Override
	public void onPacketReceive(Packet p) {
		if(p instanceof PacketRedirectGeneralPlayerUpdate) {
			CloudServer.getInstance().broadcast(p, this);
		}
		else if(p instanceof PacketClientGameMaxPlayers) {
			PacketClientGameMaxPlayers pcgmp = (PacketClientGameMaxPlayers) p;
			maxPlayers = pcgmp.getMaxPlayers();
			CloudServer.getInstance().broadcast(new PacketServerHubMaxPlayers(getUsername(), maxPlayers), ClientType.HUB);
		}
		else if(p instanceof PacketClientGamePlayerCount) {
			PacketClientGamePlayerCount pcgpc = (PacketClientGamePlayerCount) p;
			playerCount = pcgpc.getPlayerCount();
			CloudServer.getInstance().broadcast(new PacketServerHubPlayerCount(getUsername(), playerCount, 0), ClientType.HUB);
		}
		else if(p instanceof PacketClientGeneralBroadcast) {
			PacketClientGeneralBroadcast pcgb = (PacketClientGeneralBroadcast) p;
			CloudServer.getInstance().broadcast(new PacketServerBungeeBroadcast(pcgb.getSenderName(), this.getUsername(), pcgb.getMessage(), pcgb.getMessageType()), ClientType.BUNGEE_CORD);
		}
		else if(p instanceof PacketRedirectLobbyMove) {
			CloudServer.getInstance().broadcast(p, ClientType.HUB);
		}
	}

	@Override
	public String[] getInfo() {
		return new String[] {"No extra information available"};
	}
	
	@Override
	public void onConnect() {
		Collection<AbstractClientConnection> cons = CloudServer.getInstance().getConnections();
		
		for(AbstractClientConnection con : cons) {
			if(!con.isClosed() && con.getClientType() == ClientType.GAME_SERVER) {
				GameConnection gs = (GameConnection) con;
				sendPacket(new PacketServerHubMaxPlayers(gs.getUsername(), gs.getMaxPlayers()));
				sendPacket(new PacketServerHubPlayerCount(gs.getUsername(), gs.getPlayerCount(), gs.getSpectatorCount()));
				sendPacket(new PacketServerHubServerState(gs.getUsername(), gs.getServerState()));
				if(gs.getExtraInformation() != null) {
					sendPacket(new PacketServerHubExtraInformation(gs.getUsername(), gs.getExtraInformation()));
				}
			}
		}
		
		CloudServer.getInstance().broadcast(new PacketServerBungeeHubOnline(getUsername(), true), ClientType.BUNGEE_CORD);
	}
	
	@Override
	public void afterDisconnect() {
		CloudServer.getInstance().broadcast(new PacketServerBungeeHubOnline(getUsername(), false), ClientType.BUNGEE_CORD);
		ServerHandler.getInstance().checkGameServerStart(getUsername());
	}

}
