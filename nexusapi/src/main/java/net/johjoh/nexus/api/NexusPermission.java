package net.johjoh.nexus.api;

public enum NexusPermission {
	
	NONE(0),
	READ(1),
	WRITE(2);
	
	private final int id;
	
	NexusPermission(int id) {
		this.id = id;
	}

    public int getId() {
        return id;
    }

}
