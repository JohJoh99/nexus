package net.johjoh.nexus.cloud.api;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class CloudSecurity {
	
	private static PublicKey publicKey;
	private static PrivateKey privateKey;
	private static BigInteger publicModulus;
	private static BigInteger publicExponent;
	private static Decrypt decrypt;
	
	public static void initialize() throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, ClassNotFoundException, ClassCastException, IOException {
		KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
		gen.initialize(2048);
		KeyPair pair = gen.generateKeyPair();
		publicKey = pair.getPublic();
		privateKey = pair.getPrivate();
		KeyFactory fact = KeyFactory.getInstance("RSA");
		RSAPublicKeySpec spec = fact.getKeySpec(publicKey, RSAPublicKeySpec.class);
		publicModulus = spec.getModulus();
		publicExponent = spec.getPublicExponent();
		decrypt = new Decrypt(privateKey);
	}

	private CloudSecurity() {}

	public static PublicKey getPublicKey() {
		return publicKey;
	}
	
	public static BigInteger getPublicModulus() {
		return publicModulus;
	}
	
	public static BigInteger getPublicExponent() {
		return publicExponent;
	}
	
	public static Decrypt getDecrypt() {
		return decrypt;
	}
	
	public static PublicKey getPublicKey(BigInteger modulus, BigInteger exponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
		RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
	    KeyFactory fact = KeyFactory.getInstance("RSA");
	    PublicKey pubKey = fact.generatePublic(keySpec);
	    return pubKey;
	}
	
	public static class Encrypt {
		
		private Cipher cipher;
		
		public Encrypt(PublicKey publicKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		}
		
		public byte[] encrypt(byte[] data) throws IllegalBlockSizeException, BadPaddingException {
			synchronized(cipher) {
				cipher.update(data);
				return cipher.doFinal();
			}
		}
		
	}
	
	public static class Decrypt {
		
		private Cipher cipher;
		
		public Decrypt(PrivateKey privateKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
		}
		
		public byte[] decrypt(byte[] data) throws IllegalBlockSizeException, BadPaddingException {
			synchronized(cipher) {
				cipher.update(data);
				return cipher.doFinal();
			}
		}
		
	}

}
