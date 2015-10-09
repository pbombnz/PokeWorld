package rooms;

import java.util.Arrays;

import game.BoardSquare;
import game.objects.interactiveObjects.*;
import game.objects.monster.*;
import game.objects.scene.*;

/**
 *@author Wang Zhen
 *this Board is for clone. it is used when the whole panel change newDirection
 */
public class EmptyBoard extends Board{

	public EmptyBoard() {
		this.width = 10;
		this.height = 10;
		this.squares = new BoardSquare[height][width];
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
		Board other = (Board) obj;
		if (height != other.height)
			return false;
		if (!Arrays.deepEquals(squares, other.squares))
			return false;
		if (width != other.width)
			return false;
		return true;
	}

	public BoardSquare getSquareAt(int y, int x) {
		if (y < 0 || y >= getHeight() || x < 0 || x >= getWidth()) {
			throw new NullPointerException("Cannot get Board Square at (" + y
					+ ", " + x + ") because the indices are out of range.");
		}
		return squares[y][x];
	}
}
