package game;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;


import player.Player;
import squares.Space;
import squares.Square;

/**
 *@author Wang Zhen
 *
 *for storing game information , printinSg game board and printing the move range 
 */
public class Board {
	public Square[][] squares = new Square[6][24];
//	public List<Player> players = new ArrayList<Player>();
//	public Card theMurderer = null;
//	public Card theWeapon = null;
//	public Card theMurderRoom = null;
//	public List<Card> roomCards = new ArrayList<Card>();
//	public List<Card> characterCards = new ArrayList<Card>();
//	public List<Card> weaponCards = new ArrayList<Card>();
//	public List<Card> otherCards = new ArrayList<Card>();
//	public int playerNumber = -1;
//	public List<Position> positionCanMove = new ArrayList<Position>(); 
//
//	public Board() {
//		roomCards.add(new RoomCardBallRoom());
//		roomCards.add(new RoomCardBilliardRoom());
//		roomCards.add(new RoomCardDiningRoom());
//		roomCards.add(new RoomCardHall());
//		roomCards.add(new RoomCardKitchen());
//		roomCards.add(new RoomCardLibrary());
//		roomCards.add(new RoomCardLounge());
//		roomCards.add(new RoomCardStudy());
//		roomCards.add(new RoomCardConservatory());
//		characterCards.add(new CharacterCardColonelMustard());
//		characterCards.add(new CharacterCardMissScarlett());
//		characterCards.add(new CharacterCardMrsPeacock());
//		characterCards.add(new CharacterCardMrsWhite());
//		characterCards.add(new CharacterCardProfessorPlum());
//		characterCards.add(new CharacterCardTheReverendGreen());
//		weaponCards.add(new WeaptonCardCandlestick());
//		weaponCards.add(new WeaptonCardDagger());
//		weaponCards.add(new WeaptonCardLeadPipe());
//		weaponCards.add(new WeaptonCardRevolver());
//		weaponCards.add(new WeaptonCardRope());
//		weaponCards.add(new WeaptonCardSpanner());
//
//		for (Card c : roomCards) {
//			otherCards.add(c);
//		}
//		for (Card c : characterCards) {
//			otherCards.add(c);
//		}
//		for (Card c : weaponCards) {
//			otherCards.add(c);
//		}
//
//		for (int i = 0; i < 25; i++) {
//			for (int j = 0; j < 24; j++) {
//				squares[i][j] = new Space();
//			}
//		}
//		//kitchen
//		for (int i = 1; i < 7; i++) {
//			for (int j = 0; j < 6; j++) {
//				squares[i][j] = new WallKitchenWall();
//			}
//		}
//		for (int i = 2; i < 6; i++) {
//			for (int j = 1; j < 5; j++) {
//				squares[i][j] = new RoomKitchen();
//			}
//		}
//		squares[6][4] = new RoomKitchenDoor();
//		squares[5][1] = new WallKitchenWall();
//		//BallRoom
//		for (int i = 2; i < 8; i++) {
//			for (int j = 8; j < 16; j++) {
//				squares[i][j] = new WallBallRoomWall();
//			}
//		}
//		for (int i = 0; i < 7; i++) {
//			for (int j = 9; j < 15; j++) {
//				squares[i][j] = new RoomBallRoom();
//			}
//		}
//		squares[2][11] = new RoomBallRoom();
//		squares[2][12] = new RoomBallRoom();
//		squares[0][9] = new RoomBallRoom();
//		squares[1][9] = new RoomBallRoom();
//		squares[0][14] = new RoomBallRoom();
//		squares[1][14] = new RoomBallRoom();
//		squares[0][11] = new WallBallRoomWall();
//		squares[0][12] = new WallBallRoomWall();
//		squares[2][9] = new WallBallRoomWall();
//		squares[2][10] = new WallBallRoomWall();
//		squares[2][13] = new WallBallRoomWall();
//		squares[2][14] = new WallBallRoomWall();
//		squares[0][10] = new WallBallRoomWall();
//		squares[1][10] = new WallBallRoomWall();
//		squares[0][13] = new WallBallRoomWall();
//		squares[1][13] = new WallBallRoomWall();
//
//		squares[5][8] = new RoomBallRoomDoor();
//		squares[7][10] = new RoomBallRoomDoor();
//		squares[5][15] = new RoomBallRoomDoor();
//		squares[7][13] = new RoomBallRoomDoor();
//		//conservatory
//		for (int i = 1; i < 6; i++) {
//			for (int j = 18; j < 24; j++) {
//				squares[i][j] = new WallConservatoryWall();
//			}
//		}
//		for (int i = 2; i < 5; i++) {
//			for (int j = 19; j < 24; j++) {
//				squares[i][j] = new RoomConservatory();
//			}
//		}
//		squares[4][23] = new WallConservatoryWall();
//		squares[5][18] = new Space();
//		squares[4][18] = new RoomConservatoryDoor();
//		//Dining room
//		for (int i = 9; i < 16; i++) {
//			for (int j = 0; j < 8; j++) {
//				squares[i][j] = new WallDiningRoomWall();
//			}
//		}
//		for (int i = 10; i < 15; i++) {
//			for (int j = 1; j < 7; j++) {
//				squares[i][j] = new RoomDiningRoom();
//			}
//		}
//		squares[10][4] = new WallDiningRoomWall();
//		squares[10][5] = new WallDiningRoomWall();
//		squares[10][6] = new WallDiningRoomWall();
//		squares[9][5] = new Space();
//		squares[9][6] = new Space();
//		squares[9][7] = new Space();
//		squares[12][7] = new RoomDiningRoomDoor();
//		squares[15][6] = new RoomDiningRoomDoor();
//		//Lounge
//		for (int i = 19; i < 25; i++) {
//			for (int j = 0; j < 7; j++) {
//				squares[i][j] = new WallLoungeWall();
//			}
//		}
//		for (int i = 20; i < 24; i++) {
//			for (int j = 1; j < 6; j++) {
//				squares[i][j] = new RoomLounge();
//			}
//		}
//		squares[23][5] = new WallLoungeWall();
//		squares[19][6] = new RoomLoungeDoor();
//		////////////////////////////////////////////////
//		//Hall
//		for (int i = 18; i < 25; i++) {
//			for (int j = 9; j < 15; j++) {
//				squares[i][j] = new WallHallWall();
//			}
//		}
//		for (int i = 19; i < 24; i++) {
//			for (int j = 10; j < 14; j++) {
//				squares[i][j] = new RoomHall();
//			}
//		}
//		squares[18][11] = new RoomHallDoor();
//		squares[18][12] = new RoomHallDoor();
//		squares[20][14] = new RoomHallDoor();
//		//study
//		for (int i = 21; i < 25; i++) {
//			for (int j = 17; j < 24; j++) {
//				squares[i][j] = new WallStudyWall();
//			}
//		}
//		for (int i = 22; i < 24; i++) {
//			for (int j = 18; j < 24; j++) {
//				squares[i][j] = new RoomStudy();
//			}
//		}
//		squares[21][17] = new RoomHallDoor();
//		//				squares[18][12] = new RoomHallDoor();
//
//		//library
//		for (int i = 14; i < 19; i++) {
//			for (int j = 17; j < 24; j++) {
//				squares[i][j] = new WallLibraryWall();
//			}
//		}
//		for (int i = 15; i < 18; i++) {
//			for (int j = 18; j < 24; j++) {
//				squares[i][j] = new RoomLibrary();
//			}
//		}
//		squares[15][23] = new WallLibraryWall();
//		squares[17][23] = new WallLibraryWall();
//		squares[17][18] = new WallLibraryWall();
//		squares[15][18] = new WallLibraryWall();
//		squares[18][17] = new Space();
//		squares[14][17] = new Space();
//		squares[16][17] = new RoomLibraryDoor();
//		squares[14][20] = new RoomLibraryDoor();
//		squares[14][21] = new RoomLibraryDoor();
//
//		//billiard
//		for (int i = 8; i < 13; i++) {
//			for (int j = 18; j < 24; j++) {
//				squares[i][j] = new WallBilliardRoomWall();
//			}
//		}
//		for (int i = 9; i < 12; i++) {
//			for (int j = 19; j < 24; j++) {
//				squares[i][j] = new RoomBilliardRoom();
//			}
//		}
//		squares[12][23] = new RoomBilliardRoomDoor();
//		squares[9][18] = new RoomBilliardRoomDoor();
//
//		//swimmingpool
//		for (int i = 10; i < 16; i++) {
//			for (int j = 10; j < 15; j++) {
//				squares[i][j] = new Swimmingpool();
//			}
//		}
//		for (int i = 11; i < 15; i++) {
//			for (int j = 11; j < 14; j++) {
//				squares[i][j] = new Swimmingpool();
//			}
//		}
//		//null place 
//		for (int i = 0; i < 9; i++) {
//			squares[0][i] = new Nullplace();
//		}
//		squares[1][6] = new Nullplace();
//		for (int i = 15; i < 24; i++) {
//			squares[0][i] = new Nullplace();
//		}
//		squares[12][22] = new RoomBilliardRoomDoor();
//		squares[12][23] = new WallBilliardRoomWall();
//		squares[14][21] = new WallLibraryWall();
//		squares[18][23] = new Nullplace();
//		squares[14][23] = new Nullplace();
//		squares[13][23] = new Nullplace();
//		squares[1][17] = new Nullplace();
//		squares[5][23] = new Nullplace();
//		squares[7][23] = new Nullplace();
//		squares[6][0] = new Nullplace();
//		squares[8][0] = new Nullplace();
//		squares[16][0] = new Nullplace();
//		squares[18][0] = new Nullplace();
//		squares[20][23] = new Nullplace();
//		squares[21][23] = new Nullplace();
//		squares[24][6] = new Nullplace();
//		squares[24][17] = new Nullplace();
//		squares[24][15] = new Nullplace();
//		squares[24][8] = new Nullplace();
//	}
//
//	/**change square on board i, j to sq
//	 * 
//	 * @param i
//	 * @param j
//	 * @param sq
//	 */
//	public void put(int i, int j, Square sq) {
//		squares[i][j] = sq;
//	}
//	/**
//	 * print one square
//	 * @param i
//	 * @param j
//	 * @return
//	 */
//	public String getPrint(int i, int j) {
//		return squares[i][j].getPrint();
//	}
//
//	/**
//	 * print the whole board
//	 */
//	public void print() {
//		//clone a board and add character position on the cloneboard and print all squares of cloneboard
//		Board boardClone = new Board();
//		for (int i = 0; i < 25; i++) {
//			for (int j = 0; j < 24; j++) {
//				boardClone.squares[i][j] = this.squares[i][j];
//			}
//		}
//		for (Player p : this.players) {
//			boardClone.squares[p.x][p.y] = p.correspondSquare();
//		}
//
//		System.out
//				.println("=====================================================");
//		System.out.print("  ");
//		//<10
//		for (int i = 0; i < 11; i++) {
//			System.out.print(" " + i);
//		}
//		//>=10
//		for (int i = 11; i < 25; i++) {
//			System.out.print(i);
//		}
//		System.out.println("");
//		for (int i = 0; i < 25; i++) {
//			if (i < 10) {
//				System.out.print(" " + i);
//			} else {
//				System.out.print(i);
//			}
//			for (int j = 0; j < 24; j++) {
//				System.out.print(boardClone.squares[i][j].getPrint());
//			}
//			System.out.println("|");
//		}
//		System.out
//				.println("======================================================");
//		System.out
//		.println("'m'=ColonelMustard,'s'=MissScarlett,'p'=MrsPeacock,'w'=MrsWhite,'u'=ProfessorPlum,'g'=TheReverendGreen\n");
//	}
//	
//	/**print board with move range(place can be moved is present by "o"),
//	 * (x,y) is the position in of the start position(x is row number and y is colunm number in my position system), step is the steps player can move
//	 * @param x
//	 * @param y
//	 * @param step
//	 */
//	public List<Position> getMoveRange(int x, int y, int step) {
//		Board boardClone = new Board();
//		for (int i = 0; i < 25; i++) {
//			for (int j = 0; j < 24; j++) {
//				boardClone.squares[i][j] = this.squares[i][j];
//			}
//		}
//		boardClone.squares[x][y] = new MoveRangeSign();
//		while (step > 0) {
//			spread(boardClone);
//			step--;
//		}
//		//set player
//		for (Player p : this.players) {
//			boardClone.squares[p.x][p.y] = p.correspondSquare();
//		}
////		boardClone.print();
//		//store positions
//		positionCanMove = new ArrayList<Position>();
//		for (int i = 0; i < 25; i++) {
//			for (int j = 0; j < 24; j++) {
//				if (boardClone.squares[i][j] instanceof MoveRangeSign
//						|| boardClone.squares[i][j] instanceof MoveRangeSignDoor) {
//					positionCanMove.add(new Position(i, j));
//				}
//			}
//		}
//		return positionCanMove;
//	}
//
//	/**
//	 * for each square that signed by can be move , check whether the squared
//	 * around it can be moved, if can move there, sign that square as can be moved.
//	 * if that squae
//	 * @param board
//	 */
//	public void spread(Board board) {
//		//find signed positions
//		List<Position> signedPosition = new ArrayList<Position>();
//		for (int i = 0; i < 25; i++) {
//			for (int j = 0; j < 24; j++) {
//				if (board.squares[i][j] instanceof MoveRangeSign
//						|| board.squares[i][j] instanceof MoveRangeSignDoor) {
//					signedPosition.add(new Position(i, j));
//				}
//			}
//		}
//		//move one step for each signed step
//		for (Position po : signedPosition) {
//			goOneStep(po.x, po.y, board);
//		}
//	}
//
//	/**
//	 * for one square at (x,y) on the board, check whether the squared
//	 * around it can be moved, if can move there, sign that square as can be moved.
//	 * if that squae
//	 * @param x
//	 * @param y
//	 * @param board
//	 */
//	public void goOneStep(int x, int y, Board board) {
//		Square sq = board.squares[x][y];
//		if (x + 1 <= 24) {
//			if (canBeMoved(x + 1, y, board, sq)) {
//				Square sqTo = board.squares[x + 1][y];
//				if (sqTo instanceof RoomBallRoomDoor
//						|| sqTo instanceof RoomBilliardRoomDoor
//						|| sqTo instanceof RoomConservatoryDoor
//						|| sqTo instanceof RoomDiningRoomDoor
//						|| sqTo instanceof RoomHallDoor
//						|| sqTo instanceof RoomKitchenDoor
//						|| sqTo instanceof RoomLoungeDoor
//						|| sqTo instanceof RoomStudyDoor
//						|| sqTo instanceof RoomLibraryDoor) {
//					board.squares[x + 1][y] = new MoveRangeSignDoor();
//				} else {
//					board.squares[x + 1][y] = new MoveRangeSign();
//				}
//			}
//		}
//		if (x - 1 >= 0) {
//			if (canBeMoved(x - 1, y, board, sq)) {
//				Square sqTo = board.squares[x - 1][y];
//				if (sqTo instanceof RoomBallRoomDoor
//						|| sqTo instanceof RoomBilliardRoomDoor
//						|| sqTo instanceof RoomConservatoryDoor
//						|| sqTo instanceof RoomDiningRoomDoor
//						|| sqTo instanceof RoomHallDoor
//						|| sqTo instanceof RoomKitchenDoor
//						|| sqTo instanceof RoomLoungeDoor
//						|| sqTo instanceof RoomStudyDoor
//						|| sqTo instanceof RoomLibraryDoor) {
//					board.squares[x - 1][y] = new MoveRangeSignDoor();
//				} else {
//					board.squares[x - 1][y] = new MoveRangeSign();
//				}
//			}
//		}
//		if (y + 1 <= 23) {
//			if (canBeMoved(x, y + 1, board, sq)) {
//				Square sqTo = board.squares[x][y + 1];
//				if (sqTo instanceof RoomBallRoomDoor
//						|| sqTo instanceof RoomBilliardRoomDoor
//						|| sqTo instanceof RoomConservatoryDoor
//						|| sqTo instanceof RoomDiningRoomDoor
//						|| sqTo instanceof RoomHallDoor
//						|| sqTo instanceof RoomKitchenDoor
//						|| sqTo instanceof RoomLoungeDoor
//						|| sqTo instanceof RoomStudyDoor
//						|| sqTo instanceof RoomLibraryDoor) {
//					board.squares[x][y + 1] = new MoveRangeSignDoor();
//				} else {
//					board.squares[x][y + 1] = new MoveRangeSign();
//				}
//			}
//		}
//		if (y - 1 >= 0) {
//			if (canBeMoved(x, y - 1, board, sq)) {
//				Square sqTo = board.squares[x][y - 1];
//				if (sqTo instanceof RoomBallRoomDoor
//						|| sqTo instanceof RoomBilliardRoomDoor
//						|| sqTo instanceof RoomConservatoryDoor
//						|| sqTo instanceof RoomDiningRoomDoor
//						|| sqTo instanceof RoomHallDoor
//						|| sqTo instanceof RoomKitchenDoor
//						|| sqTo instanceof RoomLoungeDoor
//						|| sqTo instanceof RoomStudyDoor
//						|| sqTo instanceof RoomLibraryDoor) {
//					board.squares[x][y - 1] = new MoveRangeSignDoor();
//				} else {
//					board.squares[x][y - 1] = new MoveRangeSign();
//				}
//			}
//		}
//	}
//
//	/**
//	 * as a square at x,y on the board, check whether it can be moved and return the boolean.
//	 * sqMoveFrom is the square that spread the move sign to this square.
//	 * @param x
//	 * @param y
//	 * @param board
//	 * @param sqMoveFrom
//	 * @return
//	 */
//	public boolean canBeMoved(int x, int y, Board board, Square sqMoveFrom) {
//		if (x > 24 || x < 0 || y > 23 || y < 0) {
//			return false;
//		}
//		Square sq = board.squares[x][y];
//		if (sq instanceof Nullplace
//				|| sq instanceof CharacterCardTheReverendGreen
//				|| sq instanceof CharacterCardColonelMustard
//				|| sq instanceof CharacterCardMissScarlett
//				|| sq instanceof CharacterCardMrsPeacock
//				|| sq instanceof CharacterCardMrsWhite
//				|| sq instanceof CharacterCardProfessorPlum
//				|| sq instanceof MoveRangeSign) {
//			return false;
//		}
//		if (sqMoveFrom instanceof RoomBallRoomDoor
//				|| sqMoveFrom instanceof RoomBilliardRoomDoor
//				|| sqMoveFrom instanceof RoomConservatoryDoor
//				|| sqMoveFrom instanceof RoomDiningRoomDoor
//				|| sqMoveFrom instanceof RoomHallDoor
//				|| sqMoveFrom instanceof RoomKitchenDoor
//				|| sqMoveFrom instanceof RoomLoungeDoor
//				|| sqMoveFrom instanceof RoomStudyDoor
//				|| sqMoveFrom instanceof RoomLibraryDoor
//				|| sqMoveFrom instanceof MoveRangeSignDoor) {
//			return true;
//		}
//
//		if (sq instanceof WallHallWall || sq instanceof WallBallRoomWall
//				|| sq instanceof WallBilliardRoomWall
//				|| sq instanceof WallConservatoryWall
//				|| sq instanceof WallLibraryWall
//				|| sq instanceof WallDiningRoomWall
//				|| sq instanceof WallKitchenWall
//				|| sq instanceof WallLoungeWall || sq instanceof WallStudyWall
//				|| sq instanceof Nullplace || sq instanceof MoveRangeSign
//				|| sq instanceof MoveRangeSignDoor) {
//			return false;
//		}
//		return true;
//	}
//	
//	public static void main(String[] args) {
//		Board n = new Board();
//		n.print();
//	}
}
