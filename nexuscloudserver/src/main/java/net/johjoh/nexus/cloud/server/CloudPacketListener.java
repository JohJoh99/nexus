package net.johjoh.nexus.cloud.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import net.johjoh.nexus.cloud.server.listener.ClientPacket;
import net.johjoh.nexus.cloud.server.listener.PacketListener;
import net.johjoh.nexus.cloud.server.logging.Level;
import net.johjoh.nexus.cloud.server.logging.Logger;
import net.johjoh.nexus.cloud.server.logging.ManagedFileOutputStream;

public class CloudPacketListener extends PacketListener {

	private static boolean PACKET_VERBOSE = true;
	private PrintStream printStream;
	
	public CloudPacketListener() throws IOException {
		register();
		File latestLog = new File("packet.log");
		if(!latestLog.exists()) {
			latestLog.createNewFile();
		}
		FileOutputStream file = new FileOutputStream(latestLog, true);
		ManagedFileOutputStream stream = new ManagedFileOutputStream(file);
		printStream = new PrintStream(stream);
	}
	
	@Override
	public void onPacketReceive(ClientPacket packet) {
		if(PACKET_VERBOSE) {
			Logger.log(System.out, Level.INFO, packet.getClientConnection().getUsername() + " -> " + packet.getPacket().getName(), "PaLog");
		}
		
		Logger.log(printStream, Level.INFO, packet.getClientConnection().getUsername() + " -> " + packet.getPacket().getName(), "PaLog");
	}

	@Override
	public void onPacketSend(ClientPacket packet) {
		if(PACKET_VERBOSE) {
			Logger.log(System.out, Level.INFO, packet.getPacket().getName() + " -> " + packet.getClientConnection().getUsername(), "PaLog");
		}
		
		Logger.log(printStream, Level.INFO, packet.getPacket().getName() + " -> " + packet.getClientConnection().getUsername(), "PaLog");
	}

}
