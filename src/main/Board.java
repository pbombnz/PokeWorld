package main;

public class Board {
	private int width;
	private int height;
	
	private BoardSquare[][] squares;
	
	public Board(int width, int height)
	{
		this.width = width;
		this.height = height;
		this.squares = new BoardSquare[height][width];
	}
}