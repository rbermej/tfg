package uoc.tfg.raulberme.communication.entity;

public enum UserRolOnAd {
	SELLER((byte) 0), BUYER((byte) 1);

	private byte id;

	UserRolOnAd(byte id) {
		this.id = id;
	}

	public byte id() {
		return id;
	}
}
