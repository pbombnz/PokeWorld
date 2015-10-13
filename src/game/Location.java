package game;

import java.io.Serializable;

import rooms.Room;
import rooms.Room1;
/**
 * @author Sushant Balajee, Donald Tang
 * 
 * A simple class that holds the room, x and y to represent the position.
 * It also has methods to move but does not have logic for it.
 *
 */

public class Location implements Serializable {
	private static final long serialVersionUID = -5585594220565441892L;
	
	private Room room;
	private int x;
	private int y;
	
	public Location() {}
	
	
	public Location(Room room, int x, int y) {
		super();
		this.room = room;
		this.x = x;
		this.y = y;
	}

	/**
	 * @return Room, the room
	 */
	public Room getRoom() {
		return room;
	}

	/**
	 * @param room, sets the room
	 */
	public void setRoom(Room room) {
		this.room = room;
	}

	/**
	 * @param x, sets the x coord
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * @param y, sets the y coord
	 */
	public void setY(int y) {
		this.y=y;
	}

	/**
	 * @return int, the x coord
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * @return int, the y coord
	 */
	public int getY() {
		return y;
	}

	/**
	 * moves North
	 */
	public void moveNorth() {
		this.y = this.y - 1;
	}
	/**
	 * moves East
	 */
	public void moveEast() {
		this.x = this.x + 1;
	}
	/**
	 * moves South
	 */
	public void moveSouth() {
		this.y = this.y + 1;
	}
	/**
	 * moves West
	 */
	public void moveWest() {
		this.x = this.x - 1;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((room == null) ? 0 : room.hashCode());
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (room == null) {
			if (other.room != null)
				return false;
		} else if (!room.equals(other.room))
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Location [room=" + room.getName() + ", x=" + x + ", y=" + y + "]";
	}


}
