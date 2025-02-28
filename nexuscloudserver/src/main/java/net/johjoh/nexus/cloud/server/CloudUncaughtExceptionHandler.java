package net.johjoh.nexus.cloud.server;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Properties;

import net.johjoh.nexus.cloud.server.logging.Logger;

public class CloudUncaughtExceptionHandler implements UncaughtExceptionHandler {

	private boolean restartOnError = true;
	
	public void setRestartOnError(boolean flag) {
		restartOnError = flag;
	}
	
	@Override
	public void uncaughtException(Thread thread, Throwable t) {
		Logger.log("Thread '" + thread.getName() + "' occurred an unexpected Exception", t, thread);
		try {
			CloudServer server = CloudServer.getInstance();
			if(server != null) {
				server.stopServer();
			}
		}
		catch(IOException e) {
			Logger.log("Failed to Stop Server correctly", e, Thread.currentThread());
		}
		if(restartOnError) {
			Properties props = ServerMain.loadProperties();
			
			restartOnError = Boolean.parseBoolean(props.getProperty("restart-on-error"));
			
			if(restartOnError) {
				Logger.log("Server will restart if an uncaught Exception is thrown");
			}
			else {
				Logger.log("Server will stop if an uncaught Exception is thrown");
			}
			
			CloudServer.setInstance(new CloudServer(Integer.parseInt(props.getProperty("server-port")), props.getProperty("server-password")));
			CloudServer.getInstance().start();
		}
		else {
			System.exit(1);
		}
	}

}
