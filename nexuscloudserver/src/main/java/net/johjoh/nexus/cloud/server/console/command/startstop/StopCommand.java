package net.johjoh.nexus.cloud.server.console.command.startstop;

import net.johjoh.nexus.cloud.server.console.command.Command;
import net.johjoh.nexus.cloud.server.logging.Logger;

public class StopCommand extends Command {

	public StopCommand() {
		super("stop", "Stops the Server and closes the VM");
	}

	@Override
	public void execute(String[] args) {
		Logger.log("Stopping Server...");
		System.exit(0);
	}

}
