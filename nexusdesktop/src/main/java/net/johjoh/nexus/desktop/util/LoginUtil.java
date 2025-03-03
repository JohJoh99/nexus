package net.johjoh.nexus.desktop.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import net.johjoh.nexus.cloud.api.packet.user.PacketClientRequestPasswordSalt;
import net.johjoh.nexus.cloud.api.packet.user.PacketClientUserRegistration;
import net.johjoh.nexus.desktop.NexusDesktop;

public class LoginUtil {

	private static String username;
	private static String password;
	
	public static String getUsername() { return username; }
	public static String getPassword() { return password; }
	
	public static void sendRegistrationRequest(String usernameVar, String passwordVar) {
		PacketClientUserRegistration pcur = new PacketClientUserRegistration(usernameVar, passwordVar);
		NexusDesktop.getCloudClient().sendPacket(pcur);
	}
	
	public static void sendLoginRequest(String usernameVar, String passwordVar) {
		username = usernameVar;
		password = passwordVar;
		PacketClientRequestPasswordSalt pcrps = new PacketClientRequestPasswordSalt(usernameVar);
		NexusDesktop.getCloudClient().sendPacket(pcrps);
	}
	
	public static String hashPassword(String passwordVar, String salt) {
        try {
            String combined = passwordVar + salt;
            
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(combined.getBytes(StandardCharsets.UTF_8));
            
            return Base64.getEncoder().encodeToString(hashedBytes);
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Fehler beim Hashen des Passworts", e);
        }
    }

}
