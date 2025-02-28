package net.johjoh.nexus.cloud.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.johjoh.nexus.cloud.api.client.ClientType;
import net.johjoh.nexus.cloud.api.packet.Packet;
import net.johjoh.nexus.cloud.server.connection.AbstractClientConnection;
import net.johjoh.nexus.cloud.server.connection.LoginHandler;
import net.johjoh.nexus.cloud.server.listener.PacketListenerManager;
import net.johjoh.nexus.cloud.server.logging.Level;
import net.johjoh.nexus.cloud.server.logging.Logger;

public class CloudServer extends Thread {
	
	public static final String SERVER_VERSION = "v1.0.0";
	public static final String SERVER_BUILD = "build 1";
	public static final String LATEST_BUNGEE = "1.19";
	public static final String LATEST_SPIGOT = "1.19.3";
	
	private static CloudServer instance;
	
	public static CloudServer getInstance() {
		return instance;
	}
	
	public static void setInstance(CloudServer instance) {
		CloudServer.instance = instance;
	}
	
	private String host;
	private int port;
	private String password;
	private ServerSocket serverSocket;
	private ConcurrentHashMap<String, AbstractClientConnection> connections = new ConcurrentHashMap<String, AbstractClientConnection>();
	private boolean canAcceptConnections = true;
	
	public CloudServer(int port, String password) {
		this.port = port;
		this.password = password;
	}
	
	@Override
	public void run() {
		Thread.currentThread().setName("ServerThread");
		
		Logger.log("CloudServer " + SERVER_VERSION + " (" + SERVER_BUILD + ") by JohJoh");
		
		try {
			host = InetAddress.getLocalHost().getHostAddress();
		}
		catch(UnknownHostException e) {
			host = "0.0.0.0";
		}
		Logger.log("Starting Server on " + host + ":" + port);
		try {
			serverSocket = new ServerSocket(port);
			Logger.log("Server started!");
		}
		catch(IOException e) {
			Logger.log(Level.FATAL, "Could not start server!");
			Logger.log(Level.FATAL, e.getClass().getName() + ": " + e.getMessage());
			System.exit(1);
			return;
		}
		
		Logger.log("Initializing PacketListener...");
		try {
			new CloudPacketListener();
		}
		catch(IOException e) {
			Logger.logMinimal("Failed to initialize PacketListener", e);
		}
		
		Logger.log("Listening for Connections...");
		while(serverSocket.isBound() && !serverSocket.isClosed()) {
			try {
				if(!canAcceptConnections) continue;
				
				//boolean b = true;
				//if(b) throw new NullPointerException("It Works! (not)");
				
				Socket clientSocket = serverSocket.accept();
				Logger.log("New connection: " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());
				new LoginHandler(clientSocket).start();
			}
			catch(IOException e) {
				if(!e.getMessage().equalsIgnoreCase("socket closed")) {
					Logger.log(Level.WARNING, "Couldn't accept Socket (" + e.getClass().getName() + ": " + e.getMessage() + ")");
				}
			}
		}
	}
	
	public void stopServer() throws IOException {
		canAcceptConnections = false;
		Logger.log("Disconnecting connections...");
		disconnectAll("Server Shutdown");
		try {
			Thread.sleep(3000);
		}
		catch (InterruptedException e) {}
		serverSocket.close();
		Logger.log("Server closed!");
		PacketListenerManager.getInstance().unregisterAll();
	}
	
	public String getHost() {
		return host;
	}
	
	public int getPort() {
		return port;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void disconnectAll(String reason) {
		for(AbstractClientConnection c : connections.values()) {
			c.close(reason, true);
			connections.remove(c.getName().toUpperCase());
		}
	}
	
	public AbstractClientConnection getConnection(String username) {
		return connections.get(username.toUpperCase());
	}
	
	public Collection<AbstractClientConnection> getConnections() {
		return copy(connections.values());
	}
	
	public Set<String> getUsernames() {
		return connections.keySet();
	}
	
	public void broadcast(Packet packet) {
		broadcast(packet, getConnections());
	}
	
	public void broadcast(Packet packet, AbstractClientConnection except) {
		Collection<AbstractClientConnection> connections = getConnections();
		for(AbstractClientConnection c : connections) {
			if(!c.equals(except)) c.sendPacket(packet);
		}
	}
	
	public void broadcast(Packet packet, ClientType clientType) {
		HashSet<AbstractClientConnection> selected = new HashSet<AbstractClientConnection>();
		Collection<AbstractClientConnection> connections = getConnections();
		for(AbstractClientConnection c : connections) {
			if(c.getClientType() == clientType) {
				selected.add(c);
			}
		}
		broadcast(packet, selected);
	}
	
	public void broadcast(Packet packet, ClientType clientType, AbstractClientConnection except) {
		HashSet<AbstractClientConnection> selected = new HashSet<AbstractClientConnection>();
		Collection<AbstractClientConnection> connections = getConnections();
		for(AbstractClientConnection c : connections) {
			if(c.getClientType() == clientType) {
				if(!c.equals(except)) selected.add(c);
			}
		}
		broadcast(packet, selected);
	}
	
	public void broadcast(Packet packet, Collection<AbstractClientConnection> cons) {
		for(AbstractClientConnection c : cons) {
			c.sendPacket(packet);
		}
	}
	
	public void removeConnection(AbstractClientConnection c) {
		connections.remove(c.getUsername());
	}
	
	public boolean addConnection(AbstractClientConnection c) {
		if(!connections.containsKey(c.getUsername().toUpperCase())) {
			connections.put(c.getUsername().toUpperCase(), c);
			return true;
		}
		return false;
	}
	
	public static <T> List<T> copy(Collection<T> copyOf) {
		List<T> list = new ArrayList<T>();
		list.addAll(copyOf);
		return list;
	}

}
