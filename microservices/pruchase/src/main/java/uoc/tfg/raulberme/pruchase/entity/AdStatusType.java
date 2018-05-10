package uoc.tfg.raulberme.pruchase.entity;

public enum AdStatusType {
	ACTIVATED((byte) 0), BLOQUED((byte) 1), DEACTIVATED((byte) 2), SOLD((byte) 3);

	private byte id;

	AdStatusType(byte id) {
		this.id = id;
	}

	public byte id() {
		return id;
	}
}
