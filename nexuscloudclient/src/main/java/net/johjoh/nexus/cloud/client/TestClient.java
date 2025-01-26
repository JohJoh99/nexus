package net.johjoh.nexus.cloud.client;

import net.johjoh.nexus.cloud.api.client.ClientType;
import net.johjoh.nexus.cloud.api.packet.Packet;

public class TestClient extends CloudClient {

	public TestClient() {
		super("LOBBY01", ClientType.HUB, "password_safe");
	}

	@Override
	public void onPacketReceive(Packet p) {
		
	}
	
	public static void main(String[] args) {
		TestClient tc = new TestClient();
		tc.startAsThread();
	}

}
