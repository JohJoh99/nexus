package net.johjoh.nexus.cloud.server.console.command.info;

import net.johjoh.nexus.cloud.server.console.command.Command;
import net.johjoh.nexus.cloud.server.logging.Logger;

public class HelpCommand extends Command {

	public HelpCommand() {
		super("help", "Cloud Server Help (Arguments: <Username>)");
	}

	@Override
	public void execute(String[] args) {
		Logger.log("Info:");
		Logger.log("/help");
		Logger.log("/info <Servername>");
		Logger.log("/list");
	}

}
