package net.johjoh.nexus.cloud.api.packet.general;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import net.johjoh.nexus.cloud.api.CloudSecurity;
import net.johjoh.nexus.cloud.api.packet.Packet;
import net.johjoh.nexus.cloud.api.packet.PacketType;

public class PacketServerGeneralPublicKey extends Packet {

	private static final String MODULUS = "modulus";
	private static final String EXPONENT = "exponent";
	
	public PacketServerGeneralPublicKey() {
		super(PacketType.SERVER_GENERAL_PUBLIC_KEY);
	}
	
	public PacketServerGeneralPublicKey(BigInteger modulus, BigInteger exponent) {
		super(PacketType.SERVER_GENERAL_PUBLIC_KEY);
		setObject(MODULUS, modulus);
		setObject(EXPONENT, exponent);
	}
	
	public void setModulus(BigInteger modulus) {
		setObject(MODULUS, modulus);
	}
	
	public void setExponent(BigInteger exponent) {
		setObject(EXPONENT, exponent);
	}
	
	public BigInteger getModulus() {
		return getObjectAsBigInteger(MODULUS);
	}
	
	public BigInteger getExponent() {
		return getObjectAsBigInteger(EXPONENT);
	}
	
	public PublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
		return CloudSecurity.getPublicKey(getModulus(), getExponent());
	}

	@Override
	public boolean isValid() {
		BigInteger modulus = getModulus();
		BigInteger exponent = getExponent();
		
		return modulus != null && exponent != null;
	}

}
