package net.johjoh.nexus.cloud.api.client;

public enum ClientType {

	NEXUS_DESKTOP(0),
	WEB_CLIENT(1);

	private int id;

	/**
	 * Enumeration constructor
	 * 
	 * @param id The internal id for the client type
	 */
	ClientType(int id) {
		this.id = id;
	}

	/**
	 * Returns the internal id of this client type
	 * 
	 * @return The internal id of this client type
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Converts an internal id to a client type
	 * 
	 * @param id The internal id representing a client type
	 * @return The client type of the given id or {@code null} if the client type is
	 *         unknown
	 */
	public static ClientType fromId(Integer id) {
		if (id == null)
			return null;
		for (ClientType c : values()) {
			if (c.getId() == id.intValue())
				return c;
		}
		return null;
	}

}
