package uoc.tfg.raulberme.pruchase.entity;

public enum RolUserType {
	SUPERADMIN((byte) 0), ADMIN((byte) 1), REGISTERED_USER((byte) 2);

	private byte id;

	RolUserType(byte id) {
		this.id = id;
	}

	public byte id() {
		return id;
	}
}
