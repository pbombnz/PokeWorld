package JUnit;

import org.junit.Test;

import game.objects.GameObject;
import rooms.Board1;

public class Tests {

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testBoard(){
		Board1 board = new Board1();
			GameObject o = (GameObject) board.squares[11][11];	
	}
}
