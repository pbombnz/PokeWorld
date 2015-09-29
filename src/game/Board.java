package game;

import game.objects.Key;


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
	
	public Board()
	{
		this.width = 10;
		this.height = 10;
		this.squares = new BoardSquare[height][width];
		
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				this.squares[i][j] = new BoardSquare(null);
				
				if(i == 4 && j == 4) {
					this.squares[i][j] = new BoardSquare(null);
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
}
