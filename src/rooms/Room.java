package rooms;

import java.io.Serializable;

/**
 *@author Wang Zhen
 *this class is for ceate a room and store information
 */
public abstract class Room implements Serializable{

	private static final long serialVersionUID = 5558707554450196186L;
	public String roomName;
	public Board board;
	public int level;
	
	public Room() {
		super();
	}
	
	public String getName(){
		return roomName;
	}

	@Override
	public abstract int hashCode();

	@Override
	public abstract boolean equals(Object obj);
	
	public abstract Board getBoard();
}
