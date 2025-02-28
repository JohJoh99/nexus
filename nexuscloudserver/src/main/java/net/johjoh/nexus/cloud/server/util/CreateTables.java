package net.johjoh.nexus.cloud.server.util;

import static net.johjoh.sqltools.SQLTools.*;

import java.sql.Connection;
import java.sql.SQLException;

import net.johjoh.sqltools.TableCreator;

public class CreateTables {
	
	//	MyISAM: Nur ein Nutzer kann schreiben, Gelesen werden von mehreren Nutzern -> Viele kleine Datenpakete von mehreren Anwendern unschlagbar
	//	InnoDB: Beliebig viele können schreiben, SELECT InnoDB deutlich Nase vorne, INSERT/UPDATE MyISAM etwas schneller, wird aber von InnoDB durch das Transaktionsprotokoll mehr als ausgeglichen
	//	MyISAM kein autobackup
	//	InnoDB Nachfolger von MyISAM
	//	Große Projekte -> InnoDB
	//	Kleine Projekte -> MyISAM
	
	public static void createAllTables(Connection con) throws SQLException {
		createUsersTable(con);
		createMapTable(con);
		createMotdTable(con);
		createLangTable(con);
		createSignTable(con);
		createFriendsTable(con);
		createMaintananceTable(con);
		createDefendersInventoryTable(con);
		createBuildMapsTable(con);
		createSkyPvPHomesTable(con);
		createPluginsTable(con);
		createRootServersTable(con);
		createServersTable(con);
		createGameProfileStorageTable(con);
		createRunningJarsTable(con);
		createModpacksTable(con);
		createAdventureMapsTable(con);
		createUserServersTable(con);
	}
	
	public static void createUsersTable(Connection con) throws SQLException {
		TableCreator tc = new TableCreator(TABLE_USERS).ifNotExists().primary(COLUMN_ID).unique(COLUMN_PLAYER_UUID).engine("InnoDB").defaultCharset("utf8");
		tc.column(COLUMN_ID, "int(11)", true, "AUTO_INCREMENT");
		tc.column(COLUMN_PLAYER_UUID, "char(36)", true, null);
		tc.column(COLUMN_PLAYER_NAME, "varchar(16)", true, null);
		tc.column(COLUMN_COINS, "int(11)", true, "0");
		tc.column(COLUMN_LANGUAGE, "int(11)", true, "0");
		tc.column(COLUMN_RANK, "int(11)", true, "0");
		tc.column(COLUMN_PLAYER_XP, "int(11)", true, "0");
		tc.column(COLUMN_ONLINE, "tinyint(1)", true, "0");
		tc.column(COLUMN_CURRENT_SERVER, "varchar(255)", false, null);
		tc.column(COLUMN_FIRST_LOGIN, "datetime", true, "1000-01-01 00:00:00");
		tc.column(COLUMN_LAST_LOGIN, "datetime", true, "1000-01-01 00:00:00");
		tc.column(COLUMN_LAST_LOGOUT, "datetime", true, "1000-01-01 00:00:00");
		tc.column(COLUMN_ACHIEVEMENTS, "text", false, null);
		tc.column(COLUMN_AUTONICK, "tinyint(1)", true, "0");
		tc.column(COLUMN_SILENT_HUB, "tinyint(1)", true, "0");
		tc.column(COLUMN_SHOW_ONLY_YT_AND_TEAM, "tinyint(1)", true, "0");
		tc.column(COLUMN_ALLOW_PARTY_INVITE, "tinyint(1)", true, "1");
		tc.column(COLUMN_SHOW_STATS, "tinyint(1)", true, "0");
		tc.column(COLUMN_PRIVATE_MESSAGES, "tinyint(1)", true, "1");
		tc.column(COLUMN_TEAM_MESSAGES, "tinyint(1)", true, "1");
		tc.column(COLUMN_LOBBY_REAL_TIME, "tinyint(1)", true, "1");
		
		con.prepareStatement(tc.getStatement()).execute();
	}
	
	public static void createMapTable(Connection con) throws SQLException {
		TableCreator tc = new TableCreator(TABLE_MAPS).ifNotExists().primary(COLUMN_ID).unique(COLUMN_NAME).engine("InnoDB").defaultCharset("utf8");
		tc.column(COLUMN_ID, "int(11)", true, "AUTO_INCREMENT");
		tc.column(COLUMN_GAMEMODE, "varchar(255)", true, null);
		tc.column(COLUMN_MAP_TYPE, "enum('GAME_LOBBY','GAME_MAP','SERVER_LOBBY','DEATHMATCH')", true, null);
		tc.column(COLUMN_NAME, "varchar(255)", true, null);
		tc.column(COLUMN_MINECRAFT_VERSION, "varchar(16)", true, null);
		tc.column(COLUMN_AUTHOR, "varchar(255)", true, null);
		tc.column(COLUMN_DESCRIPTION, "varchar(255)", true, null);
		tc.column(COLUMN_LOCATION, "varchar(255)", true, null);
		tc.column(COLUMN_EXTRA_TAGS, "text", true, null);
		tc.column(COLUMN_TIMES_PLAYED, "int(11)", true, "0");
		
		con.prepareStatement(tc.getStatement()).execute();
	}

	public static void createMotdTable(Connection con) throws SQLException {
		TableCreator tc = new TableCreator(TABLE_MOTD).ifNotExists().primary(COLUMN_ID).unique(COLUMN_NAME).engine("InnoDB").defaultCharset("utf8");
		tc.column(COLUMN_ID, "int(11)", true, "AUTO_INCREMENT");
		tc.column(COLUMN_NAME, "varchar(255)", true, null);
		tc.column(COLUMN_VALUE, "varchar(255)", true, null);
		
		con.prepareStatement(tc.getStatement()).execute();
	}

	public static void createLangTable(Connection con) throws SQLException {
		TableCreator tc = new TableCreator(TABLE_LANG).ifNotExists().primary(COLUMN_ID).unique(COLUMN_KEY).engine("InnoDB").defaultCharset("utf8");
		tc.column(COLUMN_ID, "int(11)", true, "AUTO_INCREMENT");
		tc.column(COLUMN_KEY, "varchar(255)", true, null);
		tc.column(COLUMN_GERMAN, "text", true, null);
		tc.column(COLUMN_ENGLISH, "text", true, null);
		
		con.prepareStatement(tc.getStatement()).execute();
	}
	
	public static void createSignTable(Connection con) throws SQLException {
		TableCreator tc = new TableCreator(TABLE_SIGNS_LOBBY).ifNotExists().primary(COLUMN_ID).engine("InnoDB").defaultCharset("utf8");
		tc.column(COLUMN_ID, "int(11)", true, "AUTO_INCREMENT");
		tc.column(COLUMN_SERVER_NAME, "varchar(16)", true, null);
		tc.column(COLUMN_X, "int(11)", true, null);
		tc.column(COLUMN_Y, "int(11)", true, null);
		tc.column(COLUMN_Z, "int(11)", true, null);
		tc.column(COLUMN_BLOCK_FACE, "varchar(255)", true, null);
		
		con.prepareStatement(tc.getStatement()).execute();
	}
	
	public static void createFriendsTable(Connection con) throws SQLException {
		TableCreator tc = new TableCreator(TABLE_FRIENDS).ifNotExists().primary(COLUMN_ID).unique(COLUMN_PLAYER_UUID).engine("InnoDB").defaultCharset("utf8");
		tc.column(COLUMN_ID, "int(11)", true, "AUTO_INCREMENT");
		tc.column(COLUMN_PLAYER_UUID, "char(36)", true, null);
		tc.column(COLUMN_FRIEND_REQUESTS, "tinyint(1)", true, "1");
		tc.column(COLUMN_FRIEND_JOIN_MESSAGES, "tinyint(1)", true, "1");
		tc.column(COLUMN_FRIEND_JUMP, "tinyint(1)", true, "1");
		tc.column(COLUMN_FRIENDS, "varchar(2047)", false, null);
		tc.column(COLUMN_SENT_REQUESTS, "varchar(2047)", false, null);
		tc.column(COLUMN_GOTTEN_REQUESTS, "varchar(2047)", false, null);
		
		con.prepareStatement(tc.getStatement()).execute();
	}
	
	public static void createMaintananceTable(Connection con) throws SQLException {
		TableCreator tc = new TableCreator(TABLE_MAINTENANCE).ifNotExists().primary(COLUMN_SERVER_NAME).engine("InnoDB").defaultCharset("utf8");
		tc.column(COLUMN_SERVER_NAME, "varchar(16)", true, null);
		tc.column(COLUMN_LOCAL_MAINTENANCE, "tinyint(1)", true, "0");
		tc.column(COLUMN_GLOBAL_MAINTENANCE, "tinyint(1)", true, "0");
		
		con.prepareStatement(tc.getStatement()).execute();
	}
	
	public static void createDefendersInventoryTable(Connection con) throws SQLException {
		TableCreator tc = new TableCreator(TABLE_DEFENDERS_INVENTORYS).ifNotExists().primary(COLUMN_ID).unique(COLUMN_PLAYER_UUID).engine("InnoDB").defaultCharset("utf8");
		tc.column(COLUMN_ID, "int(11)", true, "AUTO_INCREMENT");
		tc.column(COLUMN_PLAYER_UUID, "char(36)", true, null);
		tc.column(COLUMN_MAIN_INVENTORY, "text", false, null);
		tc.column(COLUMN_STORAGE_INVENTORY, "text", false, null);
		tc.column(COLUMN_STORAGE_INVENTORY_SIZE, "int(11)", true, String.valueOf(9));
		
		con.prepareStatement(tc.getStatement()).execute();
	}
	
	public static void createBuildMapsTable(Connection con) throws SQLException {
		TableCreator tc = new TableCreator(TABLE_BUILD_MAPS).ifNotExists().primary(COLUMN_ID).unique(COLUMN_MAP_NAME).engine("InnoDB").defaultCharset("utf8");
		tc.column(COLUMN_ID, "int(11)", true, "AUTO_INCREMENT");
		tc.column(COLUMN_MAP_NAME, "char(36)", true, null);
		tc.column(COLUMN_MINECRAFT_VERSION, "varchar(16)", true, null);
		tc.column(COLUMN_MAP_SPAWN_X, "double", true, "0.0");
		tc.column(COLUMN_MAP_SPAWN_Y, "double", true, "64.0");
		tc.column(COLUMN_MAP_SPAWN_Z, "double", true, "0.0");
		tc.column(COLUMN_MAP_SPAWN_YAW, "float", true, "0.0");
		tc.column(COLUMN_MAP_SPAWN_PITCH, "float", true, "0.0");
		tc.column(COLUMN_MAP_LOADED, "tinyint(1)", true, "1");
		tc.column(COLUMN_MAP_GENERATOR, "int(11)", true, "0");
		tc.column(COLUMN_MAP_GAMEMODE, "varchar(255)", false, null);
		tc.column(COLUMN_MAP_TYPE, "enum('GAME_LOBBY','GAME_MAP','SERVER_LOBBY','DEATHMATCH')", false, null);
		tc.column(COLUMN_MAP_AUTHOR, "varchar(255)", false, null);
		tc.column(COLUMN_MAP_DESCRIPTION, "varchar(255)", false, null);
		tc.column(COLUMN_MAP_EXTRA_TAGS, "text", false, null);

		tc.column(COLUMN_EXP_X1, "double", false, "0.0");
		tc.column(COLUMN_EXP_Y1, "double", false, "0.0");
		tc.column(COLUMN_EXP_Z1, "double", false, "0.0");
		tc.column(COLUMN_EXP_X2, "double", false, "0.0");
		tc.column(COLUMN_EXP_Y2, "double", false, "0.0");
		tc.column(COLUMN_EXP_Z2, "double", false, "0.0");
		
		con.prepareStatement(tc.getStatement()).execute();
	}
	
	public static void createSkyPvPHomesTable(Connection con) throws SQLException {
		TableCreator tc = new TableCreator(TABLE_SKYPVP_HOMES).ifNotExists().primary(COLUMN_ID).unique(COLUMN_PLAYER_UUID).engine("InnoDB").defaultCharset("utf8");
		tc.column(COLUMN_ID, "int(11)", true, "AUTO_INCREMENT");
		tc.column(COLUMN_PLAYER_UUID, "char(36)", true, null);
		tc.column(COLUMN_NAME, "char(36)", true, "world");
		tc.column(COLUMN_X, "double", true, "0.0");
		tc.column(COLUMN_Y, "double", true, "64.0");
		tc.column(COLUMN_Z, "double", true, "0.0");
		
		con.prepareStatement(tc.getStatement()).execute();
	}
	
	public static void createPluginsTable(Connection con) throws SQLException {
		TableCreator tc = new TableCreator(TABLE_PLUGINS).ifNotExists().primary(COLUMN_ID).engine("MyISAM").defaultCharset("utf8");
		tc.column(COLUMN_ID, "int(11)", true, "AUTO_INCREMENT");
		tc.column(COLUMN_NAME, "varchar(36)", true, null);
		tc.column(COLUMN_VERSION, "varchar(36)", true, "1.0.0");
		tc.column(COLUMN_FILE_NAME, "varchar(36)", true, null);
		tc.column(COLUMN_MINECRAFT_VERSION, "varchar(36)", true, null);
		tc.column(COLUMN_SOFTWARE_TYPE, "tinyint(1)", true, null);
		tc.column(COLUMN_IS_REALM_PLUGIN, "tinyint(1)", true, "0");
		
		con.prepareStatement(tc.getStatement()).execute();
	}
	
	public static void createRootServersTable(Connection con) throws SQLException {
		TableCreator tc = new TableCreator(TABLE_ROOT_SERVERS).ifNotExists().primary(COLUMN_ID).unique(COLUMN_NAME).engine("MyISAM").defaultCharset("utf8");
		tc.column(COLUMN_ID, "int(11)", true, "AUTO_INCREMENT");
		tc.column(COLUMN_NAME, "varchar(36)", true, null);
//		tc.column(COLUMN_ROOT_SERVER_TYPE, "varchar(15)", true, null);
		tc.column(COLUMN_ROOT_SERVER_IP, "varchar(15)", true, null);
//		tc.column(COLUMN_ROOT_SERVER_PORT, "char(5)", true, null);
		tc.column(COLUMN_ROOT_SERVER_PATH, "varchar(255)", true, "/home/heidy");
		tc.column(COLUMN_IS_MAIN, "tinyint(1)", true, "0");
		tc.column(COLUMN_CPU_CORES, "int(11)", true, null);
		tc.column(COLUMN_RAM, "int(11)", true, null);
		
		con.prepareStatement(tc.getStatement()).execute();
	}
	
	public static void createGameProfileStorageTable(Connection con) throws SQLException {
		TableCreator tc = new TableCreator(TABLE_GAME_PROFILE_STORAGE).ifNotExists().primary(COLUMN_ID).engine("InnoDB").defaultCharset("utf8");
		tc.column(COLUMN_ID, "varchar(10)", true, null);
		tc.column(COLUMN_PLAYER_UUID, "char(36)", true, null);
		tc.column(COLUMN_PLAYER_NAME, "varchar(16)", true, null);
		tc.column(COLUMN_TEXTURE, "text", true, null);
		tc.column(COLUMN_SIGNATURE, "text", false, null);
		tc.column(COLUMN_REFRESH_TIME, "datetime", true, "0000-00-00 00:00:00");
		
		con.prepareStatement(tc.getStatement()).execute();
	}
	
	public static void createRunningJarsTable(Connection con) throws SQLException {
		TableCreator tc = new TableCreator(TABLE_RUNNING_JARS).ifNotExists().primary(COLUMN_ID).engine("MyISAM").defaultCharset("utf8");
		tc.column(COLUMN_ID, "int(11)", true, "AUTO_INCREMENT");
		tc.column(COLUMN_NAME, "varchar(36)", true, null);
		tc.column(COLUMN_VERSION, "varchar(36)", true, "1.0.0");
		tc.column(COLUMN_FILE_NAME, "varchar(36)", true, null);
		tc.column(COLUMN_MINECRAFT_VERSION, "varchar(36)", true, null);
		tc.column(COLUMN_SOFTWARE_TYPE, "tinyint(1)", true, null);
		
		con.prepareStatement(tc.getStatement()).execute();
	}
	
	public static void createModpacksTable(Connection con) throws SQLException {
		TableCreator tc = new TableCreator(TABLE_MODPACKS).ifNotExists().primary(COLUMN_ID).engine("MyISAM").defaultCharset("utf8");
		tc.column(COLUMN_ID, "int(11)", true, "AUTO_INCREMENT");
		tc.column(COLUMN_NAME, "varchar(36)", true, null);
		tc.column(COLUMN_DESCRIPTION, "text", false, null);
		tc.column(COLUMN_MINECRAFT_VERSION, "varchar(36)", true, null);
		tc.column(COLUMN_FILE_NAME, "varchar(36)", true, null);
		tc.column(COLUMN_VERSION, "varchar(36)", true, null);
		tc.column(COLUMN_PROPERTIES, "varchar(256)", false, null);
		tc.column(COLUMN_MIN_RAM, "smallint", false, "1024");
		
		con.prepareStatement(tc.getStatement()).execute();
	}
	
	public static void createAdventureMapsTable(Connection con) throws SQLException {
		TableCreator tc = new TableCreator(TABLE_ADVENTURE_MAPS).ifNotExists().primary(COLUMN_ID).engine("MyISAM").defaultCharset("utf8");
		tc.column(COLUMN_ID, "int(11)", true, "AUTO_INCREMENT");
		tc.column(COLUMN_NAME, "varchar(36)", true, null);
		tc.column(COLUMN_DESCRIPTION, "text", false, null);
		tc.column(COLUMN_MINECRAFT_VERSION, "varchar(36)", true, null);
		tc.column(COLUMN_FILE_NAME, "varchar(36)", true, null);
		tc.column(COLUMN_VERSION, "varchar(36)", true, null);
		tc.column(COLUMN_PROPERTIES, "varchar(256)", false, null);
		
		con.prepareStatement(tc.getStatement()).execute();
	}
	
	public static void createServersTable(Connection con) throws SQLException {
		TableCreator tc = new TableCreator(TABLE_SERVERS).ifNotExists().primary(COLUMN_NAME).unique(COLUMN_NAME).engine("MyISAM").defaultCharset("utf8");
		tc.column(COLUMN_NAME, "varchar(36)", true, null);
		tc.column(COLUMN_SERVER_TYPE, "varchar(36)", true, null);
		tc.column(COLUMN_ROOT_SERVER, "int(11)", true, null);
		tc.column(COLUMN_PORT, "int(11)", true, null);
		tc.column(COLUMN_MINECRAFT_VERSION, "varchar(16)", true, "");
		tc.column(COLUMN_SOFTWARE_TYPE, "tinyint(1)", true, null);
		tc.column(COLUMN_AUTOSTART, "tinyint(1)", true, "0");
		tc.column(COLUMN_PLUGINS, "text", true, null);
		tc.column(COLUMN_AUTO_SHUTDOWN_TIME, "int", true, "-1");
		tc.column(COLUMN_RAM, "smallint", true, "1024");
		tc.column(COLUMN_CPU_CORES, "tinyint", true, "-1");
		tc.column(COLUMN_SLOTS, "smallint", true, "20");
		tc.column(COLUMN_NOVA_ADDONS, "text", false, null);
		tc.column(COLUMN_MODPACK, "int(11)", false, null);
		tc.column(COLUMN_MODS, "text", false, null);
		tc.column(COLUMN_MINIGAME, "int(11)", false, null);
		
		con.prepareStatement(tc.getStatement()).execute();
	}
	
	public static void createUserServersTable(Connection con) throws SQLException {
		TableCreator tc = new TableCreator(TABLE_USERSERVERS).ifNotExists().primary(COLUMN_NAME).unique(COLUMN_NAME).engine("MyISAM").defaultCharset("utf8");
		tc.column(COLUMN_SERVER_NAME, "varchar(36)", true, null);
		tc.column(COLUMN_OWNER, "int(11)", true, null);
		tc.column(COLUMN_REALM_ID, "smallint", true, null);
		tc.column(COLUMN_NAME, "varchar(36)", true, null);
		tc.column(COLUMN_ICON, "varchar(36)", true, "COBBLESTONE");
		tc.column(COLUMN_MOTD, "varchar(128)", true, "A Minecraft Server");
		tc.column(COLUMN_OPS, "text", false, null);
		tc.column(COLUMN_MODERATORS, "text", false, null);
		tc.column(COLUMN_WHITELIST, "text", false, null);
		tc.column(COLUMN_REALMPLUGINS, "text", true, null);


		con.prepareStatement(tc.getStatement()).execute();
	}

}
