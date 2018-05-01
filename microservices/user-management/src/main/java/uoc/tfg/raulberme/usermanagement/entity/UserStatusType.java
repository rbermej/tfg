package uoc.tfg.raulberme.usermanagement.entity;

public enum UserStatusType {
	ACTIVATED((byte) 1), BLOQUED((byte) 2), DEACTIVATED((byte) 3);

	private byte id;

	UserStatusType(byte id) {
		this.id = id;
	}

	public byte id() {
		return id;
	}
}
