package rooms;

import game.BoardSquare;
import game.objects.Door;
import game.objects.GoodPotion;
import game.objects.Key;
import game.objects.RareCandy;
import game.objects.Rattata;
import game.objects.Tree;

import java.util.Arrays;
/**
 * @author Sushant Balajee
 * @author Prashant Bhikhu
 * @author Wang Zhen
 * this board is for store information of stage1
 */
public class Board1 extends Board{
	private static final long serialVersionUID = -3601832062619945416L;

	public Board1(int width, int height) {
		this.width = width;
		this.height = height;
		this.squares = new BoardSquare[height][width];

	}

	public Board1() {
		this.width = 10;
		this.height = 10;
		this.squares = new BoardSquare[height][width];
		

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				this.squares[i][j] = new BoardSquare(null);

				if (i == 5 && j == 0) {
					this.squares[i][j] = new BoardSquare(new RareCandy());
				}
				if (i == 9 && j == 9) {
					this.squares[i][j] = new BoardSquare(new Tree());
				}
				if (i == 4 && j == 6) {
					this.squares[i][j] = new BoardSquare(new Key(1));
				}
				if (i == 6 && j == 9) {
					this.squares[i][j] = new BoardSquare(new GoodPotion(50));
				}
				if (i == 0 && j == 0) {
					this.squares[i][j] = new BoardSquare(new Door(1,1,2
							));
				}
				if (i == 7 && j == 3) {
					this.squares[i][j] = new BoardSquare(new Rattata(10, 10));
				}
				if (i == 1 && j == 5) {
					this.squares[i][j] = new BoardSquare(new Rattata(15, 15));
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

	public BoardSquare getSquareAt(int y, int x) {
		if (y < 0 || y >= getHeight() || x < 0 || x >= getWidth()) {
			throw new NullPointerException("Cannot get Board Square at (" + y
					+ ", " + x + ") because the indices are out of range.");
		}
		return squares[y][x];
	}

}
