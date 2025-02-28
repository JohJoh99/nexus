package net.johjoh.nexus.cloud.server.console.command.info;

import net.johjoh.nexus.cloud.server.CloudServer;
import net.johjoh.nexus.cloud.server.connection.AbstractClientConnection;
import net.johjoh.nexus.cloud.server.console.command.Command;
import net.johjoh.nexus.cloud.server.logging.Logger;

public class InfoCommand extends Command {

	public InfoCommand() {
		super("info", "Shows Information about a Connection (Arguments: <Username>)");
	}

	@Override
	public void execute(String[] args) {
		if(args.length == 1) {
			AbstractClientConnection c = CloudServer.getInstance().getConnection(args[0]);
			if(c != null) {
				Logger.log("Client Address: " + c.getInetAddress().getHostAddress());
				Logger.log("Client Type: " + c.getClientType().name());
				for(String s : c.getInfo()) {
					Logger.log(s);
				}
			}
			else {
				Logger.log("No Connection with this Username found");
			}
		}
		else {
			Logger.log("Too few Arguments (Username of Connection is missing)");
		}
	}

}
