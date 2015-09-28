package player;

import game.Room;

public class Location {
	public int col;
	public int row;
	public Room room;
	
	public Location( int x, int y) {
		super();
		this.col = x;
		this.row = y;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	
}
