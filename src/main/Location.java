package main;

public class Location {
	private Room room;
	private int x;
	private int y;
	
	public Location(Room room, int x, int y) {
		super();
		this.room = room;
		this.x = x;
		this.y = y;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public void moveNorth() {
		this.y = this.y - 1;
	}

	public void moveEast() {
		this.x = this.x + 1;
	}
	
	public void moveSouth() {
		this.y = this.y - 1;
	}
	
	public void moveWest() {
		this.x = this.x - 1;
	}
}
