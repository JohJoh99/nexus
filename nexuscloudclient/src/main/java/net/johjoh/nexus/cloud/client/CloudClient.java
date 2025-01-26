package net.johjoh.nexus.cloud.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import net.johjoh.nexus.cloud.api.CloudSecurity;
import net.johjoh.nexus.cloud.api.CloudSecurity.Encrypt;
import net.johjoh.nexus.cloud.api.client.ClientType;
import net.johjoh.nexus.cloud.api.packet.Packet;
import net.johjoh.nexus.cloud.api.packet.PacketException;
import net.johjoh.nexus.cloud.api.packet.PacketType;
import net.johjoh.nexus.cloud.api.packet.general.PacketClientGeneralHandshake;
import net.johjoh.nexus.cloud.api.packet.general.PacketClientGeneralLogin;
import net.johjoh.nexus.cloud.api.packet.general.PacketClientGeneralPublicKey;
import net.johjoh.nexus.cloud.api.packet.general.PacketGeneralKeepAlive;
import net.johjoh.nexus.cloud.api.packet.general.PacketGeneralLogout;
import net.johjoh.nexus.cloud.api.packet.general.PacketServerGeneralHandshake;
import net.johjoh.nexus.cloud.api.packet.general.PacketServerGeneralLoginResponse;
import net.johjoh.nexus.cloud.api.packet.general.PacketServerGeneralPublicKey;
import net.johjoh.nexus.cloud.client.listener.PacketListenerManager;
import net.johjoh.nexus.cloud.client.logging.Level;
import net.johjoh.nexus.cloud.client.logging.Logger;

public abstract class CloudClient implements Runnable {
	
	private final int timeout = 10000;
	private final int reconnectDelay = 5000;
	private final int tps = 30;
	private final ConcurrentLinkedQueue<Packet> packetQueue = new ConcurrentLinkedQueue<Packet>();
	private final ClientType clientType;
	private final String username;
	private final String host;
	private final int port;
	private final String password;
	
	private Socket serverSocket;
	private DataInputStream dataInput;
	private DataOutputStream dataOutput;
	private Thread thread; //Only initialized if startAsThread() is executed
	private Encrypt encrypt;
	private long lastReceivedKeepAlive;
	private boolean autoReconnect = true;
	private boolean autoReconnect2 = true;
	private boolean hasConnection = false;
	
	public CloudClient(String username, ClientType clientType, String password) {
		this.clientType = clientType;
		this.username = username;
		this.password = password;
		this.host = "localhost";
		this.port = 4242;
	}
	
	public CloudClient(String username, ClientType clientType, String password, String hostname, int port) {
		this.clientType = clientType;
		this.username = username;
		this.password = password;
		this.host = hostname;
		this.port = port;
	}
	
	public void sendPacket(Packet p) {
		packetQueue.add(p);
	}
	
	public void setAutoReconnect(boolean flag) {
		this.autoReconnect = flag;
	}
	
	@Override
	public void run() {
		Thread.currentThread().setName("CloudClient");
		if(!autoReconnect) {
			autoReconnect2 = false;
			autoReconnect = true;
		}
		
		Logger.log(Level.INFO, "Generating RSA key pair...", false);
		try {
			CloudSecurity.initialize();
		}
		catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | ClassNotFoundException | ClassCastException | IOException e) {
			Logger.log(Level.FATAL, "Failed to generate key pair, cannot proceed: " + e.getClass() .getName() + ": " + e.getMessage());
			return;
		}
		
		while(autoReconnect) {
			autoReconnect = autoReconnect2;
			Logger.log(Level.INFO, "Connecting to CloudServer...", false);
			try {
				connect();
			}
			catch(IOException | IllegalBlockSizeException | BadPaddingException | PacketException e) {
				Logger.log(Level.FATAL, "Client occurred Exception! " + e.getClass().getName() + ": " + e.getMessage(), false);
				close(false, null);
			}
			if(autoReconnect) {
				try {
					Thread.sleep(reconnectDelay);
				}
				catch (InterruptedException e) {}
			}
				
		}
	}
	
	public void connect() throws IOException, PacketException, IllegalBlockSizeException, BadPaddingException {
		if(hasConnection) return;
		
		serverSocket = new Socket(host, port);
		dataInput = new DataInputStream(serverSocket.getInputStream());
		dataOutput = new DataOutputStream(serverSocket.getOutputStream());
		
		hasConnection = true;
		
		new PacketClientGeneralHandshake(getUsername()).sendUnencrypted(dataOutput);
		
		Packet serverKey = Packet.read(dataInput);
		if(serverKey instanceof PacketServerGeneralPublicKey) {
			PacketServerGeneralPublicKey psgpk = (PacketServerGeneralPublicKey) serverKey;
			try {
				PublicKey key = CloudSecurity.getPublicKey(psgpk.getModulus(), psgpk.getExponent());
				encrypt = new Encrypt(key);
			}
			catch(NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | NoSuchPaddingException e) {
				Logger.log(Level.FATAL, "Failed to read public key from server: " + e.getClass().getName() + ": " + e.getMessage());
				close(false, null);
				return;
			}
			
			new PacketClientGeneralPublicKey(CloudSecurity.getPublicModulus(), CloudSecurity.getPublicExponent()).sendEncrypted(dataOutput, encrypt);
			
			Packet serverHandshake = Packet.read(dataInput);
			if(serverHandshake instanceof PacketServerGeneralHandshake) {
				new PacketClientGeneralLogin(username, password, clientType).sendEncrypted(dataOutput, encrypt);
				Packet serverResponse = Packet.read(dataInput);
				if(serverResponse instanceof PacketServerGeneralLoginResponse) {
					PacketServerGeneralLoginResponse slr = (PacketServerGeneralLoginResponse) serverResponse;
					if(slr.getResponse().equals("OK")) {
						Logger.log(Level.INFO, "Logged in!", false);
						connectionLoop();
						Logger.log(Level.INFO, "Disconnected.", false);
						close(false, null);
					}
					else {
						Logger.log(Level.FATAL, "Kicked: " + slr.getResponse(), false);
						close(false, null);
					}
				}
				else {
					Logger.log(Level.FATAL, "Server didn't answer with right Packet!", false);
					close(false, null);
				}
			}
			else {
				Logger.log(Level.FATAL, "Server didn't send Handshake", false);
				close(false, null);
			}
		}
	}
	
	private void connectionLoop() throws IOException {
		onConnect();
		lastReceivedKeepAlive = System.currentTimeMillis();
		while(hasConnection && !serverSocket.isClosed() && serverSocket.isConnected()) {
			long b = System.currentTimeMillis();
			if(dataInput.available() != 0) {
				try {
					Packet packet = Packet.read(dataInput);
					if(packet.getPacketType() == PacketType.GENERAL_KEEP_ALIVE) {
						lastReceivedKeepAlive = System.currentTimeMillis();
						new PacketGeneralKeepAlive().sendUnencrypted(dataOutput);
					}
					else if(packet.getPacketType() == PacketType.GENERAL_LOGOUT) {
						close(false, null);
						Logger.log(Level.INFO, "CloudServer is shutting down!", false);
						break;
					}
					else {
						onPacketReceive(packet);
						PacketListenerManager.getInstance().onPacketReceive(packet);
					}
				}
				catch(IOException | IllegalBlockSizeException | BadPaddingException | PacketException t) {
					Logger.log("Failed to Read Packet", t, false);
					close(false, null);
				}
			}
			if(!packetQueue.isEmpty()) {
				Packet packet = null;
				while((packet = packetQueue.poll()) != null) {
					try {
						packet.sendEncrypted(dataOutput, encrypt);
						PacketListenerManager.getInstance().onPacketSend(packet);
						if(packet.getPacketType() == PacketType.GENERAL_LOGOUT) {
							hasConnection = false;
							try {
								dataOutput.close();
								dataInput.close();
								serverSocket.close();
							}
							catch(Throwable t) {}
							break;
						}
					}
					catch(IOException | IllegalBlockSizeException | BadPaddingException | PacketException e) {
						Logger.log("Failed to send Packet", e, false);
					}
				}
			}
			if(lastReceivedKeepAlive + timeout < System.currentTimeMillis()) {
				Logger.log(Level.ERROR, "Connection Timeout", false);
				close(false, null);
			}
			try {
				Thread.sleep(Math.min((1000/tps), Math.max((1000/tps) - (System.currentTimeMillis() - b), 0)));
			}
			catch(InterruptedException e) {}
		}
		hasConnection = false;
	}
	
	public void close(boolean logOut, String reason) {
		if(!hasConnection) return;
		if(logOut) {
			sendPacket(new PacketGeneralLogout(reason));
		}
		else {
			try {
				Thread.sleep(100);
			}
			catch (InterruptedException e) {}
			hasConnection = false;
			try {
				dataOutput.close();
				dataInput.close();
				serverSocket.close();
			}
			catch(Throwable t) {}
		}
	}
	
	public abstract void onPacketReceive(Packet p);
	
	public void startAsThread() {
		thread = new Thread(this);
		thread.start();
	}
	
	public Thread getThread() {
		return thread;
	}
	
	public boolean isConnected() {
		return hasConnection;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void onConnect() {}

}
