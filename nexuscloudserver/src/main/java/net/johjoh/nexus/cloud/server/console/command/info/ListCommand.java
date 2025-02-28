package net.johjoh.nexus.cloud.server.console.command.info;

import java.util.ArrayList;
import java.util.Collection;

import net.johjoh.nexus.cloud.server.CloudServer;
import net.johjoh.nexus.cloud.server.connection.AbstractClientConnection;
import net.johjoh.nexus.cloud.server.console.command.Command;
import net.johjoh.nexus.cloud.server.logging.Logger;

public class ListCommand extends Command {

	public ListCommand() {
		super("list", "Lists all Connections or Files (Arguments: <plugins/roots/jars>)");
	}

	@Override
	public void execute(String[] args) {
		Collection<AbstractClientConnection> cons = CloudServer.getInstance().getConnections();
		Logger.log("Currently there are " + cons.size() + " Connections connected");

		ArrayList<String> subServer = new ArrayList<String>();
		ArrayList<String> bungeeCords = new ArrayList<String>();
		ArrayList<String> hubServers = new ArrayList<String>();
		ArrayList<String> gameServers = new ArrayList<String>();
		ArrayList<String> customServers = new ArrayList<String>();
		ArrayList<String> userServers = new ArrayList<String>();
		ArrayList<String> moddedServers = new ArrayList<>();
		ArrayList<String> webClients = new ArrayList<String>();
		for(AbstractClientConnection con : cons) {
			switch (con.getClientType()) {
			case SUB_SERVER:
				subServer.add(con.getUsername().toUpperCase());
				break;
			case BUNGEE_CORD:
				bungeeCords.add(con.getUsername().toUpperCase());
				break;
			case HUB:
				hubServers.add(con.getUsername().toUpperCase());
				break;
			case GAME_SERVER:
				gameServers.add(con.getUsername().toUpperCase());
				break;
			case CUSTOM_SERVER:
				customServers.add(con.getUsername().toUpperCase());
				break;
			case USER_SERVER:
				userServers.add(con.getUsername().toUpperCase());
				break;
			case MODDED_SERVER:
				moddedServers.add(con.getUsername().toUpperCase());
				break;
			case WEB_CLIENT:
				webClients.add(con.getUsername().toUpperCase());
				break;
			}
		}

		Logger.logArray("Current Sub servers: %ARRAY%", subServer);
		Logger.logArray("Current BungeeCords: %ARRAY%", bungeeCords);
		Logger.logArray("Current Hub servers: %ARRAY%", hubServers);
		Logger.logArray("Current Game servers: %ARRAY%", gameServers);
		Logger.logArray("Current Custom servers: %ARRAY%", customServers);
		Logger.logArray("Current User servers: %ARRAY%", userServers);
		Logger.logArray("Current Modded servers: %ARRAY%", moddedServers);
		Logger.logArray("Current Web clients: %ARRAY%", webClients);
	}

}
