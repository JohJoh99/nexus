package net.johjoh.nexus.cloud.api.packet;

public abstract class RedirectPacket extends Packet {

	public RedirectPacket(PacketType type) {
		super(type);
	}
	
	public RedirectPacket(PacketType type, String sender) {
		super(type);
		setObject("sender", sender);
	}
	
	public void setSender(String sender) {
		setObject("sender", sender);
	}
	
	public String getSender() {
		Object sender = getObject("sender");
		if(sender != null) return (String) sender;
		else return null;
	}

	@Override
	public boolean isValid() {
		Object sender = getObject("sender");
		
		boolean a = sender != null && sender instanceof String;
		return a && isValid2();
	}
	
	public abstract boolean isValid2();

}
