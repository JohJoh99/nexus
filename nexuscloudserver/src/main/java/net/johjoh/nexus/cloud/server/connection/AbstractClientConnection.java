package net.johjoh.nexus.cloud.server.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import net.johjoh.nexus.cloud.api.CloudSecurity.Encrypt;
import net.johjoh.nexus.cloud.api.client.ClientType;
import net.johjoh.nexus.cloud.api.packet.Packet;
import net.johjoh.nexus.cloud.api.packet.PacketException;
import net.johjoh.nexus.cloud.api.packet.PacketType;
import net.johjoh.nexus.cloud.api.packet.general.PacketGeneralKeepAlive;
import net.johjoh.nexus.cloud.api.packet.general.PacketGeneralLogout;
import net.johjoh.nexus.cloud.api.packet.general.PacketServerGeneralLoginResponse;
import net.johjoh.nexus.cloud.server.CloudServer;
import net.johjoh.nexus.cloud.server.listener.PacketListenerManager;
import net.johjoh.nexus.cloud.server.logging.Level;
import net.johjoh.nexus.cloud.server.logging.Logger;

public abstract class AbstractClientConnection extends Thread {
	
	private static final int tps = 30;
	
	private String username;
	private ClientType clientType;
	private Socket clientSocket;
	private DataOutputStream dataOutput;
	private DataInputStream dataInput;
	private Encrypt encrypt;
	private boolean closed;
	private long lastReceivedKeepAlive;
	private ConcurrentLinkedQueue<Packet> packetQueue = new ConcurrentLinkedQueue<Packet>();
	
	public AbstractClientConnection(String username, ClientType clientType, Encrypt encrypt, Socket clientSocket, DataOutputStream dataOutput, DataInputStream dataInput) {
		this.username = username;
		this.clientType = clientType;
		this.encrypt = encrypt;
		this.clientSocket = clientSocket;
		this.dataOutput = dataOutput;
		this.dataInput = dataInput;
	}
	
	@Override
	public void run() {
		Thread.currentThread().setName("ClientConnection-" + Thread.currentThread().threadId());
		try {
			new PacketServerGeneralLoginResponse("OK").sendEncrypted(dataOutput, encrypt);
		}
		catch (IllegalBlockSizeException | BadPaddingException | IOException | PacketException e) {
			if(e.getMessage() != null) {
				if(e.getMessage().equalsIgnoreCase("socket closed")) {
					Logger.log(Level.WARNING + "The socket from " + clientSocket.getInetAddress().getHostAddress() + " was closed during the login routine");
				}
				else if(e.getMessage().equalsIgnoreCase("connection reset")) {
					Logger.log(Level.WARNING + "The socket from " + clientSocket.getInetAddress().getHostAddress() + " reset the connection during the login routine");
				}
				else {
					Logger.log(e, Thread.currentThread());
				}
			}
			else {
				Logger.log(e, Thread.currentThread());
			}
			
			if(dataInput != null) {
				try {
					dataInput.close();
				}
				catch (IOException e1) {}
			}
			if(dataOutput != null) {
				try {
					dataOutput.close();
				}
				catch (IOException e1) {}
			}
			if(clientSocket != null) {
				try {
					clientSocket.close();
				} catch (IOException e1) {}
			}
			
			return;
		}
		PacketListenerManager.getInstance().onServerResponse(getUsername());
		onConnect();
		lastReceivedKeepAlive = System.currentTimeMillis();
		while(true) {
			long b = System.currentTimeMillis();
			if(clientSocket.isConnected() && !clientSocket.isClosed() && !clientSocket.isOutputShutdown() && !clientSocket.isInputShutdown() && !closed) {
				try {
					if(dataInput.available() != 0) {
						try {
							Packet packet = Packet.read(dataInput);
							if(packet.getPacketType() == PacketType.GENERAL_KEEP_ALIVE) {
								lastReceivedKeepAlive = System.currentTimeMillis();
								PacketListenerManager.getInstance().onServerKeepAlive(this);
							}
							else if(packet.getPacketType() == PacketType.GENERAL_LOGOUT) {
								PacketGeneralLogout pgl = (PacketGeneralLogout) packet;
								String reason = pgl.getMessage() != null ? pgl.getMessage() : "logged out";
								close(reason, false);
								PacketListenerManager.getInstance().onClientLogout(this, reason);
								break;
							}
							else {
								onPacketReceive(packet);
								PacketListenerManager.getInstance().onPacketReceive(packet, this);
							}
						}
						catch(IOException | IllegalBlockSizeException | BadPaddingException | PacketException e) {
							Logger.log("Failed to read Packet", e, Thread.currentThread());
						}
					}
					if(System.currentTimeMillis() - lastReceivedKeepAlive > 1000) {
						if(closed) break;
						try {
							new PacketGeneralKeepAlive().sendUnencrypted(dataOutput);
							PacketListenerManager.getInstance().onServerKeepAlive(this);
						}
						catch(SocketException | PacketException e) {
							Logger.log(Level.ERROR, getUsername().toUpperCase() + " lost Connection");
							Logger.logMinimal(e);
							break;
						}
					}
					if(!packetQueue.isEmpty()) {
						Packet packet = null;
						while((packet = packetQueue.poll()) != null) {
							try {
								packet.sendEncrypted(dataOutput, encrypt);
								if(packet.getPacketType() == PacketType.GENERAL_LOGOUT) {
									closed = true;
									PacketListenerManager.getInstance().onServerLogout(AbstractClientConnection.this, ((PacketGeneralLogout) packet).getMessage());
									try {
										dataOutput.close();
										dataInput.close();
									}
									catch(Throwable t) {
										Logger.logMinimal(t);
									}
									try {
										clientSocket.close();
									}
									catch(Throwable t) {
										Logger.logMinimal(t);
									}
									Logger.log("Closed connection to " + clientSocket.getInetAddress().getHostAddress() + "[" + getUsername() + "]: " + ((PacketGeneralLogout) packet).getMessage());
									CloudServer.getInstance().removeConnection(AbstractClientConnection.this);
								}
								else {
									PacketListenerManager.getInstance().onPacketSend(packet, this);
								}
							}
							catch(IOException | IllegalBlockSizeException | BadPaddingException | PacketException e) {
								Logger.log("Failed to send Packet", e, Thread.currentThread());
							}
						}
					}
					if(System.currentTimeMillis() - lastReceivedKeepAlive > 15000) {
						Logger.log("Client from " + clientSocket.getInetAddress().getHostAddress() + " timed out");
						break;
					}
				}
				catch(IOException e) {
					Logger.log(e, Thread.currentThread());
					break;
				}
			}
			else {
				break;
			}
			
			try {
				Thread.sleep(Math.min((1000/tps), Math.max((1000/tps) - (System.currentTimeMillis() - b), 0)));
			}
			catch(InterruptedException e) {}
		}
		afterDisconnect();
		closed = true;
		if(CloudServer.getInstance() != null) CloudServer.getInstance().removeConnection(this);
		interrupt();
	}
	
	public void sendPacket(Packet p) {
		packetQueue.add(p);
	}
	
	public String getUsername() {
		return username;
	}
	
	public Encrypt getEncrypt() {
		return encrypt;
	}
	
	public ClientType getClientType() {
		return clientType;
	}
	
	public InetAddress getInetAddress() {
		return clientSocket.getInetAddress();
	}
	
	public boolean isClosed() {
		return closed;
	}

	public void close(String reason, boolean sendLogOutPacket) {
		new Thread() {
			
			@Override
			public void run() {
				Thread.currentThread().setName("DisconnectThread-" + threadId());
				if(sendLogOutPacket) {
					sendPacket(new PacketGeneralLogout(reason));
				}
				else {
					try {
						Thread.sleep(100);
					}
					catch (InterruptedException e) {}
					closed = true;
					try {
						dataOutput.close();
						dataInput.close();
					}
					catch(Throwable t) {
						Logger.logMinimal(t);
					}
					try {
						clientSocket.close();
					}
					catch(Throwable t) {
						Logger.logMinimal(t);
					}
					Logger.log("Closed connection to " + clientSocket.getInetAddress().getHostAddress() + "[" + getUsername() + "]: " + reason);
					CloudServer.getInstance().removeConnection(AbstractClientConnection.this);
				}
			}
			
		}.start();
	}
	
	public void afterDisconnect() {
		
	}
	
	public void onConnect() {
		
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((clientType == null) ? 0 : clientType.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof AbstractClientConnection)) {
			return false;
		}
		AbstractClientConnection other = (AbstractClientConnection) obj;
		if (clientType != other.clientType) {
			return false;
		}
		if (username == null) {
			if (other.username != null) {
				return false;
			}
		} else if (!username.equals(other.username)) {
			return false;
		}
		return true;
	}

	public abstract void onPacketReceive(Packet p);
	public abstract String[] getInfo();

}
