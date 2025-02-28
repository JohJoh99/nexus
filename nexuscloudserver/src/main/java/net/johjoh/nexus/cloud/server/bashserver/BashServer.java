package net.johjoh.nexus.cloud.server.bashserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import net.johjoh.nexus.cloud.server.logging.Level;
import net.johjoh.nexus.cloud.server.logging.Logger;

public class BashServer extends Thread {

	private static BashServer instance;
	
	public static BashServer getInstance() {
		return instance;
	}
	
	public static void setInstance(BashServer instance) {
		BashServer.instance = instance;
	}
	
	private ServerSocket socket;
	private int port;
	private String password;
	
    public BashServer(int port, String password) {
    	this.port = port;
    	this.password = password;
    }
    
    @Override
    public void run() {
    	Thread.currentThread().setName("BashServer");
    	Logger.log("Starting BashServer on Port " + port);
    	
        try {
        	socket = new ServerSocket(port);
        	Logger.log("BashServer started!");
        }
        catch(IOException e)  {
            Logger.log(Level.FATAL, "Failed to create ServerSocket: " + e.getClass().getName() + ": " + e.getMessage());
            return;
        }
        
        while(socket.isBound() && !socket.isClosed()) {
            Socket connection;
            try {
            	connection = socket.accept();
            }
            catch(IOException e) {
            	if(!e.getMessage().equalsIgnoreCase("socket closed")) {
					Logger.log(Level.WARNING, "Couldn't accept Socket (" + e.getClass().getName() + ": " + e.getMessage() + ")");
				}
            	continue;
            }
            
            try {
                InputStreamReader inputStream = new InputStreamReader(connection.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                BufferedReader input = new BufferedReader(inputStream);

                String password = input.readLine();
              	if(!this.password.equals(password)) {
              		outputStream.writeBytes("invalid-password");
                    outputStream.flush();
                	outputStream.close();	
                	continue;
              	}
                
                String command = input.readLine();
                Logger.log("Command received: " + command);
                if(command == null) {
                	connection.close();
                	continue;
                }
                
                String response = handleCommand(command);
                Logger.log("Response: " + response.replace("\n", "\\n"));
                
                outputStream.writeBytes(response);
                outputStream.flush();
            	outputStream.close();	
            }
            catch(IOException e) {
            	Logger.log(Level.FATAL, "Something went wrong while handling the Connection (" + e.getClass().getName() + ": " + e.getMessage() + ")");
            }
        }
    }
    
    public void close() throws IOException {
    	Logger.log("Stopping BashServer...");
        socket.close();
    }
    
    private String handleCommand(String command) {
    	command = command.toLowerCase();
    	
    	/*if(lcommand.startsWith("execute")) {
    		String[] split = command.split(" ");
    		if(split.length > 1) {
    			String execute = "";
    			for(int i = 1; i < split.length; i++) {
    				execute += split[i] + " ";
    			}
    			execute = execute.trim();
    			
				return "execution-error/" + e.getClass().getName() + ":" + e.getMessage() + "\n";
    			
    			return "success\n" + exitCode + "\n" + programResponse + "\n";
    		}
    		else {
    			return "too-few-arguments\n";
    		}
    	}*/
    	
    	return "unknown-command\n";
    }

}
