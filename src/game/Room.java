package game;

import java.io.File;

public class Room {
	public String roomName;
	public Board board;
	
	public Room(){
		this.roomName = "Testing Room";
		this.board = new Board();
	}
	
	//public Room importRoom(File fileName){
	//	return new Room();
	//}
}
