package rooms;

import java.io.Serializable;

/**
 *@author Wang Zhen
 *this class is for ceate a room and store information
 */
public abstract class Room implements Serializable{
	public String roomName;
	public Board board;
	public String getName(){
		return roomName;
	}

	@Override
	public abstract int hashCode();

	@Override
	public abstract boolean equals(Object obj);
	
	public abstract Board getBoard();
}
