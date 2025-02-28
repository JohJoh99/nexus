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

		ArrayList<String> nexusDesktopClients = new ArrayList<String>();
		ArrayList<String> webClients = new ArrayList<String>();
		for(AbstractClientConnection con : cons) {
			switch (con.getClientType()) {
			case NEXUS_DESKTOP:
				nexusDesktopClients.add(con.getUsername().toUpperCase());
				break;
			case WEB_CLIENT:
				webClients.add(con.getUsername().toUpperCase());
				break;
			}
		}

		Logger.logArray("Current Nexus Desktop clients: %ARRAY%", nexusDesktopClients);
		Logger.logArray("Current Web clients: %ARRAY%", webClients);
	}

}
