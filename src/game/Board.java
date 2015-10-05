package game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.nio.Buffer;
import java.util.Arrays;

import game.objects.GiantSword;
import game.objects.Goblin;
import game.objects.GoodPotion;
import game.objects.Key;
import game.objects.Tree;


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
				if(i == 9 && j == 9) {
					this.squares[i][j] = new BoardSquare(new Tree());
					System.out.println("TREE");
				}
				if(i == 3 && j == 4) {
					this.squares[i][j] = new BoardSquare(new Key());
				}
				if(i == 2 && j == 4) {
					this.squares[i][j] = new BoardSquare(new GoodPotion(50));
				}
				if(i == 0 && j == 0) {
					this.squares[i][j] = new BoardSquare(new Goblin());
				}

			}
		}

	}
	//	public Board(String filename) throws IOException{
	//		this.squares = new BoardSquare[height][width];
	//		BufferedReader in = new BufferedReader(new FileReader(filename));
	//
	//		String formatingLine = in.readLine();
	//		String[] formatArray = formatingLine.split(",");
	//		this.width = Integer.parseInt(formatArray[0]);
	//		this.height = Integer.parseInt(formatArray[1]);
	//		int y = 0;
	//		loop: while (true) {
	//			String line = in.readLine();
	//			if (line == null) {
	//				break loop;
	//			}
	//			String[] values = line.split(",");
	//
	//			for (int x = 0; x < values.length; x++) { switch (values[x]) {
	//				case "1":
	//				this.squares[y][x] = new BoardSquare(null);
	//				System.out.println("case 1");
	//			}
	//			y++;
	//	}
	//		}}

//	public Board(String filename) throws IOException{
//		//parses one line needs work
//		this.width = 10;
//		this.height = 10;
//		this.squares = new BoardSquare[height][width];
//		BufferedReader in = new BufferedReader(new FileReader(filename));
//		while(in.readLine() != null){
//		String line = in.readLine();
//		String[] values = line.split(",");
//		int y = 0;
//		for(int x = 0; x < values.length; x++){
//			switch(values[x]){
//			case "1":
//				this.squares[x][y] = new BoardSquare(null);
//
//			case "2":
//				this.squares[x][y] = new BoardSquare(new Key());
//
//			case "3":
//				this.squares[x][y] = new BoardSquare(new GiantSword());
//			}
//		}
//		}
//	}
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
		//	public static void main(String[] args) throws IOException {
		//		new Board("board.txt");
		//	}

	}
