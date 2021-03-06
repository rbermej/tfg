package uoc.tfg.raulberme.usermanagement.entity;

public enum UserStatusType {
	ACTIVATED((byte) 0), BLOQUED((byte) 1), DEACTIVATED((byte) 2);

	private byte id;

	UserStatusType(byte id) {
		this.id = id;
	}

	public byte id() {
		return id;
	}
}
