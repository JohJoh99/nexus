package net.johjoh.nexus.cloud.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.crypto.NoSuchPaddingException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import net.johjoh.nexus.api.sql.HibernateUtil;
import net.johjoh.nexus.api.tables.NexusUser;
import net.johjoh.nexus.cloud.api.CloudSecurity;
import net.johjoh.nexus.cloud.server.bashserver.BashServer;
import net.johjoh.nexus.cloud.server.console.Console;
import net.johjoh.nexus.cloud.server.logging.Level;
import net.johjoh.nexus.cloud.server.logging.Logger;
import net.johjoh.nexus.cloud.server.logging.ManagedFileOutputStream;
import net.johjoh.nexus.cloud.server.logging.TeePrintStream;

public class ServerMain {
	
	private static Properties cloudProperties;
	
	public static Properties getProperties() {
		return cloudProperties;
	}
	
	public static void main(String[] args) {
		
		//	Load Hibernate
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.openSession();
		
        /*try {
            session.beginTransaction();

            NexusUser user = new NexusUser("JohJoh", "joshfrank4@outlook.com", "j9cdtrnj", "salt");
            session.persist(user);

            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
            sessionFactory.close();
        }*/
		
		//  Load MySQL Config
		//MySQLConnectionUtils.reloadCfgFromProperties();
		
		//  Load/Register jdbc
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			Logger.log(Level.FATAL, "Failed to load JDBC Driver");
			Logger.logMinimal(e);
			System.exit(1);
		}
		
		//	Manages Lock File
		File lockFile = new File(".lock");
		if(lockFile.exists()) {
			Logger.log(Level.FATAL, "Server is already started!");
			System.exit(1);
		}
		else {
			try {
				lockFile.createNewFile();
				lockFile.deleteOnExit();
				//TODO add Write lock
			}
			catch (IOException e) {
				Logger.log(Level.FATAL, "Failed to create .lock");
				Logger.logMinimal(e);
				System.exit(1);
			}
		}
		
		//  Create SQL Tables
		/*Connection con = null;
		try {
			con = MySQLConnectionUtils.getNewConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			CreateTables.createAllTables(con);
			
			//	Load Plugins, Root Servers, Running Jars
			TableStorage.init();
			TableStorage.loadData(con);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		MySQLConnectionUtils.closeConnection(con);*/
		
		//	Creates Source Folders
		File serverParent = new File(lockFile.getAbsolutePath()).getParentFile().getParentFile();
		File runningJars = new File(serverParent.getAbsolutePath() + "/sources/running_jars");
		File plugins = new File(serverParent.getAbsolutePath() + "/sources/plugins");
		File maps = new File(serverParent.getAbsolutePath() + "/sources/maps");
		File modpacks = new File(serverParent.getAbsolutePath() + "/sources/modpacks");
		File adventureMaps = new File(serverParent.getAbsolutePath() + "/sources/adventure_maps");
		if(!runningJars.exists())
			runningJars.mkdirs();
		if(!plugins.exists())
			plugins.mkdirs();
		if(!maps.exists())
			maps.mkdirs();
		if(!modpacks.exists())
			modpacks.mkdirs();
		if(!adventureMaps.exists())
			adventureMaps.mkdirs();
		
		//	ShutdownThread
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {
				if(BashServer.getInstance() != null) {
					BashServer server = BashServer.getInstance();
					try {
						server.close();
					}
					catch(IOException e) {
						Logger.log("Failed to stop BashServer correctly", e, Thread.currentThread());
					}
				}
				
				if(CloudServer.getInstance() != null) {
					CloudServer server = CloudServer.getInstance();
					try {
						server.stopServer();
					}
					catch(IOException e) {
						Logger.log("Failed to stop server correctly", e, Thread.currentThread());
					}
				}
			}
			
		}, "ShutdownThread"));
		
		packLog();
		try {
			File latestLog = new File("latest.log");
			if(!latestLog.exists()) {
				latestLog.createNewFile();
			}
			FileOutputStream file = new FileOutputStream(latestLog, true);
			ManagedFileOutputStream stream = new ManagedFileOutputStream(file);
			TeePrintStream teeOut = new TeePrintStream(stream, System.out);
		    TeePrintStream teeErr = new TeePrintStream(stream, System.err);
		    System.setOut(teeOut);
		    System.setErr(teeErr);
		    new Console(teeOut, teeErr).start();
		}
		catch(IOException e) {
			Logger.log(Level.FATAL, "Failed to Register File Logger");
			Logger.logMinimal(e);
			System.exit(1);
			return;
		}
		
		Properties props = loadProperties();
		cloudProperties = props;

		CloudUncaughtExceptionHandler cueh = new CloudUncaughtExceptionHandler();
		boolean restartOnError = Boolean.parseBoolean(props.getProperty("restart-on-error"));
		cueh.setRestartOnError(restartOnError);
		Thread.setDefaultUncaughtExceptionHandler(cueh);
		if(restartOnError) {
			Logger.log("Server will restart if an uncaught Exception is thrown");
		}
		else {
			Logger.log("Server will stop if an uncaught Exception is thrown");
		}
		
		Logger.log("Generating RSA key pair...");
		try {
			CloudSecurity.initialize();
		}
		catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | ClassNotFoundException | ClassCastException | IOException e) {
			Logger.log(Level.FATAL, "Failed to generate key pair, cannot proceed: " + e.getClass() .getName() + ": " + e.getMessage());
			System.exit(1);
			return;
		}
		
		
		
		CloudServer.setInstance(new CloudServer(Integer.parseInt(props.getProperty("server-port")), props.getProperty("server-password")));
		CloudServer.getInstance().start();
		
		//MySQLConnectionUtils.closeConnection(con);
		
		BashServer.setInstance(new BashServer(Integer.parseInt(props.getProperty("bash-server-port")), props.getProperty("bash-server-password")));
		BashServer.getInstance().start();
		
	}
	
	private static String packLog() {
		File latestLog = new File("latest.log");
		if(latestLog.exists()) {
	    	try {
				String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				
				File logDir = new File("logs/");
				if(!logDir.exists()) {
					logDir.mkdir();
				}
				
				int cursor = 1;
				File packFile = new File("logs/", time + "-" + cursor + ".zip");
				while(packFile.exists()) {
					cursor++;
					packFile = new File("logs/", time + "-" + cursor + ".zip");
				}
				packFile.createNewFile();
				
				byte[] buffer = new byte[1024];
	    		FileOutputStream fos = new FileOutputStream(packFile);
	    		ZipOutputStream zos = new ZipOutputStream(fos);
	    		ZipEntry ze = new ZipEntry("latest.log");
	    		zos.putNextEntry(ze);
	    		FileInputStream in = new FileInputStream(latestLog);
	 
	    		int len;
	    		while ((len = in.read(buffer)) > 0) {
	    			zos.write(buffer, 0, len);
	    		}
	 
	    		in.close();
	    		zos.closeEntry();
	    		zos.close();
	    		
	    		latestLog.delete();
	    	}
	    	catch(IOException ex) {
	    		Logger.log("Failed to pack latest.log", ex, Thread.currentThread());
	    	}
		}
		return null;
	}
	
	public static Properties loadProperties() {
		Properties defaultProperties = new Properties();
		defaultProperties.setProperty("server-port", "4242");
		defaultProperties.setProperty("server-password", "password_safe");
		defaultProperties.setProperty("restart-on-error", "true");
		defaultProperties.setProperty("bash-server-port", "1674");
		defaultProperties.setProperty("bash-server-password", "password_super_safe");
		
		File propertyFile = new File("cloud.properties");
		if(!propertyFile.exists()) {
			try {
				propertyFile.createNewFile();
			}
			catch (IOException e) {
				Logger.log(Level.FATAL, "Failed to Create cloud.properties");
				Logger.logMinimal(e);
				System.exit(1);
				return defaultProperties;
			}
			try {
				defaultProperties.store(new FileOutputStream(propertyFile), "CloudServer Properties File");
			}
			catch (IOException e) {
				Logger.log(Level.FATAL, "Failed to Save cloud.properties");
				Logger.logMinimal(e);
				System.exit(1);
				return defaultProperties;
			}
		}
		
		Properties cloudProperties = new Properties();
		try {
			cloudProperties.load(new FileInputStream(propertyFile));
		}
		catch (IOException e) {
			Logger.log(Level.FATAL, "Failed to Load cloud.properties");
			Logger.logMinimal(e);
			System.exit(1);
			return defaultProperties;
		}
		
		if(!isValid(cloudProperties)) {
			Logger.log(Level.ERROR, "Loaded cloud.properties are not valid... Using defaults");
			cloudProperties = defaultProperties;
			try {
				defaultProperties.store(new FileOutputStream(propertyFile), "CloudServer Properties File");
			}
			catch (IOException e) {}
		}
		return cloudProperties;
	}
	
	private static boolean isValid(Properties cloudServerProperties) {
		String port = cloudServerProperties.getProperty("server-port");
		String password = cloudServerProperties.getProperty("server-password");
		String restartOnError = cloudServerProperties.getProperty("restart-on-error");
		String bashServerPort = cloudServerProperties.getProperty("bash-server-port");
		String bashServerPassword = cloudServerProperties.getProperty("bash-server-password");
		
		boolean a = port != null && isInteger(port);
		boolean b = password != null;
		boolean c = restartOnError != null && isBoolean(restartOnError);
		boolean d = bashServerPort != null && isInteger(bashServerPort);
		boolean e = bashServerPassword != null;
		
		return a && b && c && d && e;
	}
	
	private static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;
		}
		catch(NumberFormatException e) {
			return false;
		}
	}
	
	private static boolean isBoolean(String s) {
		if(s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false")) return true;
		return false;
	}

}
