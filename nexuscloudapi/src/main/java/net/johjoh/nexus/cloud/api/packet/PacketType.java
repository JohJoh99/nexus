package net.johjoh.nexus.cloud.api.packet;

import static net.johjoh.nexus.cloud.api.packet.PacketType.PacketBound.*;
import static net.johjoh.nexus.cloud.api.packet.PacketType.PacketCategory.*;

import java.lang.reflect.InvocationTargetException;

import net.johjoh.nexus.cloud.api.packet.general.PacketClientGeneralBroadcast;
import net.johjoh.nexus.cloud.api.packet.general.PacketClientGeneralHandshake;
import net.johjoh.nexus.cloud.api.packet.general.PacketClientGeneralLogin;
import net.johjoh.nexus.cloud.api.packet.general.PacketClientGeneralPublicKey;
import net.johjoh.nexus.cloud.api.packet.general.PacketGeneralKeepAlive;
import net.johjoh.nexus.cloud.api.packet.general.PacketGeneralLogout;
import net.johjoh.nexus.cloud.api.packet.general.PacketServerGeneralHandshake;
import net.johjoh.nexus.cloud.api.packet.general.PacketServerGeneralLoginResponse;
import net.johjoh.nexus.cloud.api.packet.general.PacketServerGeneralPublicKey;

/**
 * Enumeration containing all packets
 * 
 * @author JohJoh
 * @version 1.0
 */
public enum PacketType {
	
	/**
	 * Checks if Client and Server are still up
	 *  -> AbstractClientConnection run()
	 *  -> CloudClient run()
	 */
	GENERAL_KEEP_ALIVE(0, PacketGeneralKeepAlive.class, GENERAL, null),
	/**
	 * Closes Connection to Client
	 * CloudClient close() -> AbstractClientConnection run()
	 */
	GENERAL_LOGOUT(1, PacketGeneralLogout.class, GENERAL, null),
	
	/**
	 * Check for safe Connection
	 * CloudClient connect() -> LoginHandler run()
	 */
	CLIENT_GENERAL_HANDSHAKE(2, PacketClientGeneralHandshake.class, GENERAL, CLIENT),
	/**
	 * CloudSecurity Decrypt Key
	 * LoginHandler run() -> CloudClient connect()
	 */
	SERVER_GENERAL_PUBLIC_KEY(3, PacketServerGeneralPublicKey.class, GENERAL, SERVER),
	/**
	 * CloudSecurity Decrypt Key
	 * CloudClient connect() -> LoginHandler run()
	 */
	CLIENT_GENERAL_PUBLIC_KEY(4, PacketClientGeneralPublicKey.class, GENERAL, CLIENT),
	/**
	 * Check for safe Connection
	 * LoginHandler run() -> CloudClient connect()
	 */
	SERVER_GENERAL_HANDSHAKE(5, PacketServerGeneralHandshake.class, GENERAL, SERVER),
	/**
	 * Login Info for Cloud Server from Client
	 * CloudClient connect() -> LoginHandler run()
	 */
	CLIENT_GENERAL_LOGIN(6, PacketClientGeneralLogin.class, GENERAL, CLIENT),
	/**
	 * Login Info Confirmation from Cloud Server for Client
	 * LoginHandler run() -> CloudClient connect()
	 */
	SERVER_GENERAL_LOGIN_RESPONSE(7, PacketServerGeneralLoginResponse.class, GENERAL, SERVER),
	/**
	 * Client Packet to broadcast (Client to CloudServer)
	 *  -> BungeeCloudClient onPacketReceive()
	 */
	CLIENT_GENERAL_BROADCAST(30, PacketClientGeneralBroadcast.class, GENERAL, CLIENT),
	
	;
	
	private int id;
	private Class<? extends Packet> packetClass;
	private PacketCategory category;
	private PacketBound bound;
	
	private PacketType(int id, Class<? extends Packet> packetClass, PacketCategory category, PacketBound bound) {
		this.id = id;
		this.packetClass = packetClass;
		this.category = category;
		this.bound = bound;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name();
	}
	
	public Class<? extends Packet> getPacketClass() {
		return packetClass;
	}
	
	public PacketCategory getCategory() {
		return category;
	}
	
	public PacketBound getBound() {
		return bound;
	}
	
	public Packet getNewInstance() {
		try {
			return packetClass.getConstructor().newInstance();
		}
		catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {}
		return null;
	}
	
	@Override
	public String toString() {
		return name();
	}
	
	public static PacketType fromString(String name) {
		for(PacketType pt : values()) {
			if(pt.getName().equals(name)) {
				return pt;
			}
		}
		return null;
	}
	
	public static PacketType fromId(int id) {
		for(PacketType pt : values()) {
			if(pt.getId() == id) {
				return pt;
			}
		}
		return null;
	}
	
	public static enum PacketCategory {
		
		GENERAL, GAME_SERVER, HUB_SERVER, BUNGEE_CORD, WEB_CLIENT;

	}
	
	public static enum PacketBound {
		
		CLIENT, SERVER, REDIRECT;
		
	}

}
