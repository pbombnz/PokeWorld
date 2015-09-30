package game;

import java.io.Serializable;

public class Room implements Serializable {
	private static final long serialVersionUID = -6734434217984592426L;
	
	public String roomName;
	public Board board;
	
	public Room() {
		this.roomName = "Testing Room";
		this.board = new Board();
	}
}
