package net.johjoh.nexus.cloud.api.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import net.johjoh.nexus.cloud.api.CloudSecurity;
import net.johjoh.nexus.cloud.api.CloudSecurity.Decrypt;
import net.johjoh.nexus.cloud.api.CloudSecurity.Encrypt;
import net.johjoh.nexus.cloud.api.StringArrayReader;

/**
 * The packet class used for handling the information sent and received by the
 * CloudServer and CloudClient
 * 
 * @author JohJoh
 * @version 1.0
 */
public abstract class Packet {

	public static final int READ_TIMEOUT = 1000;

	private final PacketType packetType;
	private final String name;
	private HashMap<String, Object> list = new HashMap<String, Object>();

	/**
	 * Creates a new packet
	 * 
	 * @param packetType The type of the packet needed for sending and receiving
	 * @param objects    Objects which should be added to the HashMap. This should
	 *                   be given as "key", "value", "key", ...
	 */
	protected Packet(PacketType packetType, Object... objects) {
		this.packetType = packetType;
		this.name = packetType.getName();
		for (int i = 0; i != objects.length; i++) {
			list.put(objects[i].toString(), objects[i + 1]);
			i++;
		}
	}

	/**
	 * Returns the type of this packet
	 * 
	 * @return The type of this packet
	 */
	public PacketType getPacketType() {
		return packetType;
	}

	/**
	 * Returns the packet name
	 * 
	 * @return The packet name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sends the packet over a given DataOutputStream This method should ONLY be
	 * called by the client or server client thread
	 * 
	 * @param dataOutput A DataOutputStream
	 * @throws IOException if an IOException occurs
	 * @deprecated Old method. use {@code sendEncrypted()} or
	 *             {@code sendUnencrypted()} instead
	 */
	@Deprecated
	public void _send(DataOutputStream dataOutput) throws IOException {
		if (!isValid())
			throw new IllegalStateException("Packet is not valid");
		dataOutput.writeInt(packetType.getId());
		dataOutput.writeInt(list.size());
		for (String key : list.keySet()) {
			dataOutput.writeUTF(key);
			Object obj = list.get(key);
			if (obj instanceof String) {
				dataOutput.write(0);
				dataOutput.writeUTF(obj.toString());
			} else if (obj instanceof Integer) {
				dataOutput.write(1);
				dataOutput.writeInt(((Integer) obj).intValue());
			} else if (obj instanceof Double) {
				dataOutput.write(2);
				dataOutput.writeDouble(((Double) obj).doubleValue());
			} else if (obj instanceof Float) {
				dataOutput.write(3);
				dataOutput.writeFloat(((Float) obj).floatValue());
			} else if (obj instanceof Long) {
				dataOutput.write(4);
				dataOutput.writeLong(((Long) obj).longValue());
			} else if (obj instanceof Boolean) {
				dataOutput.write(5);
				dataOutput.writeBoolean(((Boolean) obj).booleanValue());
			} else if (obj instanceof String[]) {
				dataOutput.write(6);
				String[] array = (String[]) obj;
				dataOutput.writeInt(array.length);
				for (String s : array) {
					dataOutput.writeUTF(s);
				}
			} else if (obj instanceof UUID) {
				dataOutput.write(7);
				dataOutput.writeUTF(obj.toString());
			} else if (obj instanceof UUID[]) {
				dataOutput.write(8);
				UUID[] array = (UUID[]) obj;
				dataOutput.writeInt(array.length);
				for (UUID u : array) {
					dataOutput.writeUTF(u.toString());
				}
			}
		}
	}

	protected void setObject(String key, Object obj) {
		list.put(key, obj);
	}

	protected Object getObject(String key) {
		return list.get(key);
	}

	protected Integer getObjectAsInteger(String key) {
		Object integer = getObject(key);
		if (integer instanceof Integer)
			return (Integer) integer;
		else
			return null;
	}

	protected Float getObjectAsFloat(String key) {
		Object floot = getObject(key);
		if (floot instanceof Float)
			return (Float) floot;
		else
			return null;
	}

	protected Double getObjectAsDouble(String key) {
		Object dooble = getObject(key);
		if (dooble instanceof Double)
			return (Double) dooble;
		else
			return null;
	}

	protected String getObjectAsString(String key) {
		Object string = getObject(key);
		if (string instanceof String)
			return (String) string;
		else
			return null;
	}

	protected Boolean getObjectAsBoolean(String key) {
		Object bool = getObject(key);
		if (bool instanceof Boolean)
			return (Boolean) bool;
		else
			return null;
	}

	protected String[] getObjectAsStringArray(String key) {
		Object stringArray = getObject(key);
		if (stringArray instanceof String[])
			return (String[]) stringArray;
		else
			return null;
	}

	protected UUID getObjectAsUUID(String key) {
		Object uuid = getObject(key);
		if (uuid instanceof UUID)
			return (UUID) uuid;
		else
			return null;
	}

	protected UUID[] getObjectAsUUIDArray(String key) {
		Object uuidArray = getObject(key);
		if (uuidArray instanceof UUID[])
			return (UUID[]) uuidArray;
		else
			return null;
	}

	protected BigInteger getObjectAsBigInteger(String key) {
		Object bigInteger = getObject(key);
		if (bigInteger instanceof BigInteger)
			return (BigInteger) bigInteger;
		else
			return null;
	}

	protected void removeObject(String key) {
		list.remove(key);
	}

	@Deprecated
	public static Packet _read(DataInputStream dis) throws IOException {
		int count = 0;
		while (dis.available() == 0) {
			count += 50;
			if (count >= READ_TIMEOUT)
				throw new IOException("It took to long for the opposite connection to send a required packet");
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
		}

		int id = dis.readInt();
		PacketType pt = PacketType.fromId(id);
		if (pt == null)
			throw new IOException("Packet is not recognized");
		Packet p = pt.getNewInstance();
		int size = dis.readInt();
		for (int i = 0; i != size; i++) {
			String key = dis.readUTF();
			Object obj = null;
			int chromaKey = dis.read();
			if (chromaKey == 0) {
				obj = dis.readUTF();
			} else if (chromaKey == 1) {
				obj = dis.readInt();
			} else if (chromaKey == 2) {
				obj = dis.readDouble();
			} else if (chromaKey == 3) {
				obj = dis.readFloat();
			} else if (chromaKey == 4) {
				obj = dis.readLong();
			} else if (chromaKey == 5) {
				obj = dis.readBoolean();
			} else if (chromaKey == 6) {
				int arraylen = dis.readInt();
				obj = new String[arraylen];
				for (int ii = 0; ii < arraylen; ii++) {
					((String[]) obj)[ii] = dis.readUTF();
				}
			} else if (chromaKey == 7) {
				obj = UUID.fromString(dis.readUTF());
			} else if (chromaKey == 8) {
				int arraylen = dis.readInt();
				obj = new UUID[arraylen];
				for (int ii = 0; ii < arraylen; ii++) {
					((UUID[]) obj)[ii] = UUID.fromString(dis.readUTF());
				}
			}

			if (obj != null) {
				p.setObject(key, obj);
			}
		}
		if (!p.isValid())
			throw new IOException("Received Packet is not valid!");
		return p;
	}

	public void sendEncrypted(DataOutputStream dos, Encrypt encrypt)
			throws IOException, IllegalBlockSizeException, BadPaddingException, PacketException {
		double encryptionLength = 245;

		String written = writePacket();
		byte[] data = written.getBytes();
		byte[][] splitData = new byte[(int) Math.ceil((double) data.length / encryptionLength)][];
		for (int i = 0; i < data.length; i++) {
			int mainIndex = (int) Math.floor((double) i / encryptionLength);
			int subIndex = i % (int) encryptionLength;
			if (splitData[mainIndex] == null) {
				if (mainIndex == (splitData.length - 1)) {
					splitData[mainIndex] = new byte[data.length - ((splitData.length - 1) * (int) encryptionLength)];
				} else {
					splitData[mainIndex] = new byte[(int) encryptionLength];
				}
			}

			splitData[mainIndex][subIndex] = data[i];
		}

		dos.writeInt(0);
		dos.writeInt(splitData.length);
		for (byte[] d : splitData) {
			byte[] encrypted = encrypt.encrypt(d);
			dos.writeInt(encrypted.length);
			dos.write(encrypted);
		}
		dos.flush();
	}

	public void sendUnencrypted(DataOutputStream dos) throws IOException, PacketException {
		String written = writePacket();
		byte[] data = written.getBytes();
		dos.writeInt(1);
		dos.writeInt(data.length);
		dos.write(data);
		dos.flush();
	}

	public static Packet read(DataInputStream dis)
			throws IOException, IllegalBlockSizeException, BadPaddingException, PacketException {
		int count = 0;
		while (dis.available() == 0) {
			count += 50;
			if (count >= READ_TIMEOUT)
				throw new IOException("It took to long for the opposite connection to send a required packet");
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
		}

		int encryptionStatus = dis.readInt();
		if (encryptionStatus == 0) {
			ArrayList<Byte> data = new ArrayList<Byte>();
			Decrypt decrypt = CloudSecurity.getDecrypt();

			int splitLen = dis.readInt();
			for (int i = 0; i < splitLen; i++) {
				int arrayLen = dis.readInt();
				byte[] encrypted = new byte[arrayLen];
				dis.readFully(encrypted);
				byte[] decrypted = decrypt.decrypt(encrypted);
				for (byte b : decrypted) {
					data.add(b);
				}
			}

			Byte[] bytes = data.toArray(new Byte[data.size()]);
			byte[] prim = new byte[bytes.length];
			for (int i = 0; i < bytes.length; i++) {
				prim[i] = bytes[i];
			}

			String read = new String(prim);
			Packet packet = readPacket(read);
			return packet;
		} else {
			int arrayLen = dis.readInt();
			byte[] data = new byte[arrayLen];
			dis.readFully(data);

			String str = new String(data);
			Packet packet = readPacket(str);
			return packet;
		}
	}

	public String writePacket() throws IOException, PacketException {
		StringBuilder builder = new StringBuilder();

		try {
			if (!isValid())
				throw new IllegalStateException("Packet is not valid");
			builder.append(packetType.getId() + "\n");
			builder.append(list.size() + "\n");
			for (String key : list.keySet()) {
				builder.append(key + "\n");
				Object obj = list.get(key);
				if (obj instanceof String) {
					builder.append("0\n");
					builder.append(obj.toString() + "\n");
				} else if (obj instanceof Integer) {
					builder.append("1\n");
					builder.append(((Integer) obj).intValue() + "\n");
				} else if (obj instanceof Double) {
					builder.append("2\n");
					builder.append(((Double) obj).doubleValue() + "\n");
				} else if (obj instanceof Float) {
					builder.append("3\n");
					builder.append(((Float) obj).floatValue() + "\n");
				} else if (obj instanceof Long) {
					builder.append("4\n");
					builder.append(((Long) obj).longValue() + "\n");
				} else if (obj instanceof Boolean) {
					builder.append("5\n");
					builder.append(((Boolean) obj).booleanValue() + "\n");
				} else if (obj instanceof String[]) {
					builder.append("6\n");
					String[] array = (String[]) obj;
					builder.append(array.length + "\n");
					for (String s : array) {
						builder.append(s + "\n");
					}
				} else if (obj instanceof UUID) {
					builder.append("7\n");
					builder.append(obj.toString() + "\n");
				} else if (obj instanceof UUID[]) {
					builder.append("8\n");
					UUID[] array = (UUID[]) obj;
					builder.append(array.length + "\n");
					for (UUID u : array) {
						builder.append((u != null ? u.toString() : "null") + "\n");
					}
				} else if (obj instanceof BigInteger) {
					builder.append("9\n");
					builder.append(((BigInteger) obj).toString() + "\n");
				}
			}

			return builder.toString().trim();
		} catch (Exception e) {
			if (e instanceof IOException) {
				throw (IOException) e;
			} else {
				throw new PacketException("Failed to write packet: " + e.getClass().getName() + ": " + e.getMessage());
			}
		}
	}

	public static Packet readPacket(String decrypted) throws IOException, PacketException {
		try {
			String lines[] = decrypted.split("\\r?\\n");
			StringArrayReader reader = new StringArrayReader(lines);

			int id = Integer.parseInt(reader.readNext());
			PacketType pt = PacketType.fromId(id);
			if (pt == null)
				throw new IOException("Packet is not recognized");
			Packet p = pt.getNewInstance();
			int size = Integer.parseInt(reader.readNext());
			for (int i = 0; i != size; i++) {
				String key = reader.readNext();
				Object obj = null;
				int chromaKey = Integer.parseInt(reader.readNext());
				if (chromaKey == 0) {
					obj = reader.readNext();
				} else if (chromaKey == 1) {
					obj = Integer.parseInt(reader.readNext());
				} else if (chromaKey == 2) {
					obj = Double.parseDouble(reader.readNext());
				} else if (chromaKey == 3) {
					obj = Float.parseFloat(reader.readNext());
				} else if (chromaKey == 4) {
					obj = Long.parseLong(reader.readNext());
				} else if (chromaKey == 5) {
					obj = Boolean.parseBoolean(reader.readNext());
				} else if (chromaKey == 6) {
					int arraylen = Integer.parseInt(reader.readNext());
					obj = new String[arraylen];
					for (int ii = 0; ii < arraylen; ii++) {
						((String[]) obj)[ii] = reader.readNext();
					}
				} else if (chromaKey == 7) {
					obj = UUID.fromString(reader.readNext());
				} else if (chromaKey == 8) {
					int arraylen = Integer.parseInt(reader.readNext());
					obj = new UUID[arraylen];
					for (int ii = 0; ii < arraylen; ii++) {
						String uuidRead = reader.readNext();
						if (!uuidRead.equals("null")) {
							((UUID[]) obj)[ii] = UUID.fromString(uuidRead);
						}
					}
				} else if (chromaKey == 9) {
					obj = new BigInteger(reader.readNext());
				}

				if (obj != null) {
					p.setObject(key, obj);
				}
			}
			if (!p.isValid())
				throw new IOException("Received Packet is not valid!");
			return p;
		} catch (Exception e) {
			if (e instanceof IOException) {
				throw (IOException) e;
			} else {
				throw new PacketException("Failed to read packet: " + e.getClass().getName() + ": " + e.getMessage());
			}
		}
	}

	/**
	 * Returns whether the packet is valid and can be sent/received or not
	 * 
	 * @return {@code true} if the packet is valid, {@code false} if otherwise
	 */
	public abstract boolean isValid();

}
