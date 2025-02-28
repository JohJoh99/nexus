package net.johjoh.nexus.cloud.server.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import net.johjoh.nexus.cloud.api.CloudSecurity;
import net.johjoh.nexus.cloud.api.CloudSecurity.Encrypt;
import net.johjoh.nexus.cloud.api.client.ClientType;
import net.johjoh.nexus.cloud.api.packet.Packet;
import net.johjoh.nexus.cloud.api.packet.PacketException;
import net.johjoh.nexus.cloud.api.packet.general.PacketClientGeneralHandshake;
import net.johjoh.nexus.cloud.api.packet.general.PacketClientGeneralLogin;
import net.johjoh.nexus.cloud.api.packet.general.PacketClientGeneralPublicKey;
import net.johjoh.nexus.cloud.api.packet.general.PacketServerGeneralHandshake;
import net.johjoh.nexus.cloud.api.packet.general.PacketServerGeneralLoginResponse;
import net.johjoh.nexus.cloud.api.packet.general.PacketServerGeneralPublicKey;
import net.johjoh.nexus.cloud.server.CloudServer;
import net.johjoh.nexus.cloud.server.listener.PacketListenerManager;
import net.johjoh.nexus.cloud.server.logging.Level;
import net.johjoh.nexus.cloud.server.logging.Logger;

public class LoginHandler extends Thread {
	
	private Socket clientSocket;
	protected boolean done = false;
	
	public LoginHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	
	@Override
	public void run() {
		Thread.currentThread().setName("LoginHandler-" + Thread.currentThread().threadId());
		DataInputStream dataInput = null;
		DataOutputStream dataOutput = null;
		try {
			dataInput = new DataInputStream(clientSocket.getInputStream());
			dataOutput = new DataOutputStream(clientSocket.getOutputStream());
			//new PacketServerGeneralHandshake()._send(dataOutput);
			//PacketListenerManager.getInstance().onServerHandshake();
			int count = 0;
			while(dataInput.available() == 0) {
				count += 50;
				if(count >= Packet.READ_TIMEOUT) {
					return;
				}
				try {
					Thread.sleep(50);
				}
				catch (InterruptedException e) {}
			}
			
			Packet clientHandshake = Packet.read(dataInput);
			if(clientHandshake instanceof PacketClientGeneralHandshake) {
				String preUsername = ((PacketClientGeneralHandshake) clientHandshake).getUsername();
				Logger.log(preUsername + " is logging in");
				new PacketServerGeneralPublicKey(CloudSecurity.getPublicModulus(), CloudSecurity.getPublicExponent()).sendUnencrypted(dataOutput);
				Packet clientKey = Packet.read(dataInput);
				if(clientKey instanceof PacketClientGeneralPublicKey) {
					PacketClientGeneralPublicKey pcgpk = (PacketClientGeneralPublicKey) clientKey;
					PublicKey key;
					Encrypt encrypt;
					try {
						key = CloudSecurity.getPublicKey(pcgpk.getModulus(), pcgpk.getExponent());
						encrypt = new Encrypt(key);
					}
					catch(NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | NoSuchPaddingException e) {
						Logger.log("Client from " + clientSocket.getInetAddress().getHostAddress() + " didn't send correct key");
						dataInput.close();
						dataOutput.close();
						clientSocket.close();
						return;
					}
					
					new PacketServerGeneralHandshake().sendEncrypted(dataOutput, encrypt);
					
					Packet clientLogin = Packet.read(dataInput);
					if(clientLogin instanceof PacketClientGeneralLogin) {
						PacketClientGeneralLogin pcl = (PacketClientGeneralLogin) clientLogin;
						PacketListenerManager.getInstance().onClientLogin(pcl.getUsername());
						if(pcl.getPassword().equalsIgnoreCase(CloudServer.getInstance().getPassword())) {
							Logger.log(pcl.getUsername().toUpperCase() + " successfully logged in!");
							String username = pcl.getUsername();
							ClientType clientType = pcl.getClientType();
						
							if(clientType != null) {
								AbstractClientConnection c = null;
								if(clientType == ClientType.NEXUS_DESKTOP) {
									c = new NexusDesktopConnection(username, clientType, encrypt, clientSocket, dataOutput, dataInput);
								}
								else if(clientType == ClientType.WEB_CLIENT) {
									c = new WebClientConnection(username, clientType, encrypt, clientSocket, dataOutput, dataInput);
								}
								
								boolean loginSuccess = CloudServer.getInstance().addConnection(c);
								if(loginSuccess) {
									c.start();
								}
								else {
									Logger.log(Level.ERROR, "Failed to add connection (Username already exists)");
									new PacketServerGeneralLoginResponse("Username already exists").sendEncrypted(dataOutput, encrypt);
									PacketListenerManager.getInstance().onServerResponse(pcl.getUsername());
									dataInput.close();
									dataOutput.close();
									clientSocket.close();
								}
							}
							else {
								Logger.log(Level.WARNING, username.toUpperCase() + " has invalid client type");
								new PacketServerGeneralLoginResponse("Invalid Client Type").sendEncrypted(dataOutput, encrypt);
								PacketListenerManager.getInstance().onServerResponse(pcl.getUsername());
								dataInput.close();
								dataOutput.close();
								clientSocket.close();
							}
						}
						else {
							Logger.log("Client from " + clientSocket.getInetAddress().getHostAddress() + " entered wrong password");
							new PacketServerGeneralLoginResponse("Invalid Password").sendEncrypted(dataOutput, encrypt);
							PacketListenerManager.getInstance().onServerResponse(pcl.getUsername());
							dataInput.close();
							dataOutput.close();
							clientSocket.close();
						}	
					}
					else {
						Logger.log("Client from " + clientSocket.getInetAddress().getHostAddress() + " didn't send login packet");
						dataInput.close();
						dataOutput.close();
						clientSocket.close();
					}
				}
				else {
					Logger.log("Client from " + clientSocket.getInetAddress().getHostAddress() + " didn't send client key packet");
					dataInput.close();
					dataOutput.close();
					clientSocket.close();
				}
			}
			else {
				Logger.log("Client from " + clientSocket.getInetAddress().getHostAddress() + " didn't send handshake packet");
				dataInput.close();
				dataOutput.close();
				clientSocket.close();
			}
		}
		catch(IOException | IllegalBlockSizeException | BadPaddingException | PacketException e) {
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
		}
		done = true;
	}

}
