package JUnit;

import org.junit.Test;

import game.Board;
import game.objects.GameObject;

public class Tests {

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testBoard(){
		Board board = new Board();
			GameObject o = (GameObject) board.squares[11][11];	
	}
}
