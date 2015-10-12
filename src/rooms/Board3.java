package rooms;

import java.io.Serializable;
import java.util.Arrays;

import game.BoardSquare;
import game.objects.interactiveObjects.*;
import game.objects.scene.*;
import game.objects.monster.*;
/**
 * @author Sushant Balajee
 * @author Prashant Bhikhu
 */

public class Board3 extends Board{
	private static final long serialVersionUID = -3601832062619945416L;


	public Board3(int width, int height)
	{
		this.width = width;
		this.height = height;
		this.squares = new BoardSquare[height][width];

	}

	public Board3()
	{ 
		this.width = 10;
		this.height = 10;
		this.squares = new BoardSquare[height][width];

		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				this.squares[i][j] = new BoardSquare(null);

				if(i == 9 && j == 9 || i == 1 && j == 0  || i == 1 && j == 9) {
					this.squares[i][j] = new BoardSquare(new Tree());
				}
				if(i == 0 && j == 9) {
					this.squares[i][j] = new BoardSquare(new Key(3));
				}
				if(i == 0 && j == 0 || i == 4 && j == 5) {
					this.squares[i][j] = new BoardSquare(new Key(100));
				}
				if(i == 7 && j == 4) {
					this.squares[i][j] = new BoardSquare(new GoodPotion(100));
				}
				if(i == 9 && j == 0) {
					this.squares[i][j] = new BoardSquare(new Door(3,3));
				}
				if (i == 8 && j == 8) {
					this.squares[i][j] = new BoardSquare(new Door(2,3));
				}
				if(i == 2 && j == 5) {
					this.squares[i][j] = new BoardSquare(new Rhydon(50,50));
				}
				if(i == 6 && j == 0) {
					this.squares[i][j] = new BoardSquare(new Rhydon(60,60));
				}
				if(i == 4 && j == 9) {
					this.squares[i][j] = new BoardSquare(new Rhydon(70,70));
				}
			}
		}
	}

	public BoardSquare[][] getSquares() {
		return squares;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + Arrays.hashCode(squares);
		result = prime * result + width;
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
		Board3 other = (Board3) obj;
		if (height != other.height)
			return false;
		if (!Arrays.deepEquals(squares, other.squares))
			return false;
		if (width != other.width)
			return false;
		return true;
	}


	public BoardSquare getSquareAt(int y, int x) {
		if(y < 0 || y >= getHeight() || x < 0 || x >= getWidth()) {
			throw new NullPointerException("Cannot get Board Square at ("+y+", "+x+") because the indices are out of range.");
		}
		return squares[y][x];
	}

}
