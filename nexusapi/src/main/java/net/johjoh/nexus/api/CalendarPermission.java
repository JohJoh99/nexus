package net.johjoh.nexus.api;

public enum CalendarPermission {
	
	NONE(0),
	READ(1),
	WRITE(2);
	
	private final int id;
	
	CalendarPermission(int id) {
		this.id = id;
	}

    public int getId() {
        return id;
    }

}
