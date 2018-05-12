package uoc.tfg.raulberme.pruchase.entity;

public enum AdStatusType {
	ACTIVATED((byte) 0), SOLD((byte) 1);

	private byte id;

	AdStatusType(byte id) {
		this.id = id;
	}

	public byte id() {
		return id;
	}
}
