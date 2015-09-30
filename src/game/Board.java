package game;

import java.io.Serializable;
import java.util.Arrays;

import game.objects.GiantSword;
import game.objects.Key;


public class Board implements Serializable {
	private static final long serialVersionUID = -3601832062619945416L;
	
	private int width;
	private int height;
	
	private BoardSquare[][] squares;
	
	public Board(int width, int height)
	{
		this.width = width;
		this.height = height;
		this.squares = new BoardSquare[height][width];
		
	}
	
	public Board()
	{
		this.width = 10;
		this.height = 10;
		this.squares = new BoardSquare[height][width];
		
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				this.squares[i][j] = new BoardSquare(null);
				
				if(i == 4 && j == 4) {
					this.squares[i][j] = new BoardSquare(new GiantSword());
				}
				
				if(i == 3 && j == 4) {
					this.squares[i][j] = new BoardSquare(new Key());
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
		Board other = (Board) obj;
		if (height != other.height)
			return false;
		if (!Arrays.deepEquals(squares, other.squares))
			return false;
		if (width != other.width)
			return false;
		return true;
	}
	
	
}
