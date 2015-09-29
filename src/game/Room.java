package game;

import java.io.Serializable;

public class Room implements Serializable {
	public String roomName;
	public Board board;
	
	public Room(){
		this.roomName = "Testing Room";
		this.board = new Board();
	}
}
