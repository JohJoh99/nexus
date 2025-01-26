package net.johjoh.nexus.cloud.api.packet.general;

import net.johjoh.nexus.cloud.api.packet.Packet;
import net.johjoh.nexus.cloud.api.packet.PacketType;

public class PacketClientGeneralBroadcast extends Packet {

	private static final String SENDER_NAME = "senderName";
	private static final String MESSAGE = "message";
	private static final String MESSAGE_TYPE = "message_type"; //0 for Broadcast; 1 for Teamchat
	
	public PacketClientGeneralBroadcast() {
		super(PacketType.CLIENT_GENERAL_BROADCAST);
	}
	
	public PacketClientGeneralBroadcast(String senderName, String message, int messageType) {
		super(PacketType.CLIENT_GENERAL_BROADCAST);
		setSenderName(senderName);
		setMessage(message);
		setMessageType(messageType);
	}
	
	public void setSenderName(String senderName) {
		setObject(SENDER_NAME, senderName);
	}
	
	public void setMessage(String message) {
		setObject(MESSAGE, message);
	}
	
	public void setMessageType(int messageType) {
		setObject(MESSAGE_TYPE, messageType);
	}
	
	public String getSenderName() {
		return getObjectAsString(SENDER_NAME);
	}
	
	public String getMessage() {
		return getObjectAsString(MESSAGE);
	}
	
	public Integer getMessageType() {
		return getObjectAsInteger(MESSAGE_TYPE);
	}

	@Override
	public boolean isValid() {
		String senderName = getSenderName();
		String message = getMessage();
		Integer messageType = getMessageType();
		
		return senderName != null && message != null && messageType != null && (messageType == 0 || messageType == 1);
	}

}
