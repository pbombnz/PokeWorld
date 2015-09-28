package gui;

import game.Board;
import game.Position;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

import Cards.Card;
import Cards.WeaptonCardCandlestick;
import Cards.WeaptonCardDagger;
import Cards.WeaptonCardLeadPipe;
import Cards.WeaptonCardRevolver;
import Cards.WeaptonCardRope;
import Cards.WeaptonCardSpanner;
import player.ColonelMustard;
import player.MissScarlett;
import player.MrsPeacock;
import player.MrsWhite;
import player.Player;
import player.ProfessorPlum;
import player.TheReverendGreen;
import squares.RoomBallRoom;
import squares.RoomBilliardRoom;
import squares.RoomConservatory;
import squares.RoomDiningRoom;
import squares.RoomHall;
import squares.RoomKitchen;
import squares.RoomLibrary;
import squares.RoomLounge;
import squares.RoomStudy;
import squares.Space;
import squares.Square;
import squares.Swimmingpool;
import ui.ChooseNumFrame;
import ui.GameFrame;

/**
 *@author Wang Zhen
 *Canvas class,for running the game
 */
public class Game {

	public static Board board;
	public static int playerIndex = -1;
	public static Frame chooseNumFrame;
	public static Frame chooseCharaFrame;
	public static Frame gameFrame;
	public static boolean finished = false;
	public static boolean canDice = true;
	public static Player playerThisTurn;
	public static int step;
	public static int playerNumberLimited;
	public static String returnedName;
	public static String returnedCharType;
	public static final String YELLOW = "colonelMustard";
	public static final String BLUE = "mrsPeacock";
	public static final String WHITE = "mrsWhite";
	public static final String PRUPLU = "professorPlum";
	public static final String GREEN = "theReverendGreen";
	public static final String RED = "missScarlett";
	public static List<String> characterCanChoose = new ArrayList<String>();
	public static String sugmurderInput;
	public static Square sugroomInput;
	public static Card sugweaponInput;
	public static boolean canSug = true;
	public static boolean canAcc = true;//only 1 change in 1 turn

	public static void play() {
		characterCanChoose.add(BLUE);
		characterCanChoose.add(GREEN);
		characterCanChoose.add(RED);
		characterCanChoose.add(WHITE);
		characterCanChoose.add(PRUPLU);
		characterCanChoose.add(YELLOW);
		chooseNumFrame = new ChooseNumFrame();
	}

	public static void mainStep2() {
		chooseNumFrame.dispose();
		board = new Board();
		if (board.players.size() < playerNumberLimited) {
			chooseCharaFrame = new ChooseCharaFrame();
		} else {
			mainStep3();
		}
	}

	public static void mainStep3() {
		chooseCharaFrame.dispose();
		//add character
		if (returnedCharType != null) {
			if (returnedCharType.equals(BLUE)) {
				Player newplayer = new MrsPeacock();
				board.players.add(newplayer);
				if (returnedName != null) {
					newplayer.setName(returnedName);
				}
				characterCanChoose.remove(BLUE);
			} else if (returnedCharType.equals(RED)) {
				Player newplayer = new MissScarlett();
				board.players.add(newplayer);
				if (returnedName != null) {
					newplayer.setName(returnedName);
				}
				characterCanChoose.remove(RED);
			} else if (returnedCharType.equals(YELLOW)) {
				Player newplayer = new ColonelMustard();
				board.players.add(newplayer);
				if (returnedName != null) {
					newplayer.setName(returnedName);
				}
				characterCanChoose.remove(YELLOW);
			} else if (returnedCharType.equals(GREEN)) {
				Player newplayer = new TheReverendGreen();
				board.players.add(newplayer);
				if (returnedName != null) {
					newplayer.setName(returnedName);
				}
				characterCanChoose.remove(GREEN);
			} else if (returnedCharType.equals(WHITE)) {
				Player newplayer = new MrsWhite();
				board.players.add(newplayer);
				if (returnedName != null) {
					newplayer.setName(returnedName);
				}
				characterCanChoose.remove(WHITE);
			} else if (returnedCharType.equals(PRUPLU)) {
				Player newplayer = new ProfessorPlum();
				board.players.add(newplayer);
				if (returnedName != null) {
					newplayer.setName(returnedName);
				}
				characterCanChoose.remove(PRUPLU);
			}
		}
		if (board.players.size() < playerNumberLimited) {
			chooseCharaFrame = new ChooseCharaFrame();
		} else {
			mainStep4();
		}
	}

	public static void mainStep4() {
		getWinCards(board);
		giveCardsToPlayer(board);

		//close Frame chooseNumFrame and open gameFrame
		playerIndex = findNextPlayer(playerIndex, board);
		//		System.out.println(playerNumber+" "+board.players.size());
		playerThisTurn = board.players.get(playerIndex);

		gameFrame = new GameFrame();

		run(board);
		//		((GameFrame) gameFrame).printCharacter(board);
	}

	/**
	 * send cards(without 3 cards that are stored as victory conditions) to each player 
	 * @param board
	 */
	public static void giveCardsToPlayer(Board board) {
		int playerNumber = -1;
		while (board.otherCards.size() > 0) {
			playerNumber = findNextPlayer(playerNumber, board);
			Player playerGetCard = board.players.get(playerNumber);
			Card card = board.otherCards
					.get((int) (Math.random() * board.otherCards.size()));
			playerGetCard.cards.add(card);
			//remove from others
			board.otherCards.remove(card);
		}
	}

	/**
	 * choose 1 murderer 1 weapon and 1 room as victory conditions and store them
	 * @param board
	 */
	public static void getWinCards(Board board) {
		int characterNum = (int) (Math.random() * 6);
		int roomNum = (int) (Math.random() * 9);
		int weaponNum = (int) (Math.random() * 6);
		board.theMurderer = board.characterCards.get(characterNum);
		board.theMurderRoom = board.roomCards.get(roomNum);
		board.theWeapon = board.weaponCards.get(weaponNum);
		board.otherCards.remove(board.theMurderer);
		board.otherCards.remove(board.theMurderRoom);
		board.otherCards.remove(board.theWeapon);
	}

	private static void run(Board board) {
		((GameFrame) gameFrame).printCharacter(board);

	}

	public static void endTurn() {
		//initialize
		step = 0;
		canDice = true;
		playerIndex = findNextPlayer(playerIndex, board);
		playerThisTurn = board.players.get(playerIndex);
		((GameFrame) gameFrame).cleanMoveRange();
		((GameFrame) gameFrame).printInfor();
		((GameFrame) gameFrame).printCard();
		canSug = true;
		sugmurderInput = null;
		sugweaponInput = null;
		sugroomInput = null;
	}

	public static void dice() {
		if (canDice) {
			int mice1 = ((int) (Math.random() * 6) + 1);
			int mice2 = ((int) (Math.random() * 6) + 1);
			int miceResult = mice1 + mice2;
			((GameFrame) gameFrame).dicing(mice1, mice2);
			step = miceResult;
			((GameFrame) gameFrame).printMoveRange(board, board.getMoveRange(
					playerThisTurn.x, playerThisTurn.y, miceResult));
			canDice = false;
		} else {
			//notice
			JOptionPane.showMessageDialog(gameFrame,
					"You already dice one time.You cannot dice again.",
					"Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	public static void suggestion() {
		if (canSug) {
			Square playerSquare = board.squares[playerThisTurn.x][playerThisTurn.y];
			if (playerSquare instanceof Space) {
				JOptionPane
						.showMessageDialog(
								gameFrame,
								"You are not in any room, so you can not make announcement or accusation.",
								"Error", JOptionPane.ERROR_MESSAGE);
			} else {
				((GameFrame) gameFrame).sAskMurder();
			}
		} else {
			JOptionPane.showMessageDialog(gameFrame,
					"You can only suggestion 1 time in 1 turn.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void suggestion1() {
		((GameFrame) gameFrame).sAskWeapon();
	}

	public static void suggestion2() {

		String roomInput = board.squares[playerThisTurn.x][playerThisTurn.y]
				.getType();
		System.out.println(sugmurderInput);
		if (sugmurderInput.equals(board.theMurderer.name())
				&& roomInput.equals(board.theMurderRoom.name())
				&& sugweaponInput.name().equals(board.theWeapon.name())) {
			JOptionPane.showMessageDialog(gameFrame, "You win!!!!!!!",
					"You win", JOptionPane.ERROR_MESSAGE);
			gameFrame.dispose();
		} else {
			JOptionPane.showMessageDialog(gameFrame,
					"Your announcement is wrong!", "Sorry",
					JOptionPane.ERROR_MESSAGE);
		}
		canSug = false;
	}

	public static void accusation() {
		if (canAcc) {
			Square playerSquare = board.squares[playerThisTurn.x][playerThisTurn.y];
			if (playerSquare instanceof Swimmingpool) {

				((GameFrame) gameFrame).aAskMurder();

			} else {
				JOptionPane
						.showMessageDialog(
								gameFrame,
								"You cannot accusation because you are not in swimming pool",
								"Error", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(gameFrame,
					"You can only accusation 1 time in 1 turn.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void accusation0() {
		((GameFrame) gameFrame).aAskWeapon();
	}

	public static void accusation1() {
		((GameFrame) gameFrame).aAskRoom();
	}

	public static void accusation2() {

		if (sugmurderInput.equals(board.theMurderer.name())
				&& sugroomInput.getType().equals(board.theMurderRoom.name())
				&& sugweaponInput.name().equals(board.theWeapon.name())) {

			JOptionPane.showMessageDialog(gameFrame, "You win!!!!!!!",
					"You win", JOptionPane.ERROR_MESSAGE);
			gameFrame.dispose();

			finished = true;
		} else {
			JOptionPane.showMessageDialog(gameFrame,
					"Your announcement is wrong!Sorry,you are out", "Sorry",
					JOptionPane.ERROR_MESSAGE);
			board.players.remove(playerThisTurn);
		}
	}

	/**
	 * scan the string that user input .If the murderer name is valid, return the name
	 * @param scanner
	 * @return
	 */
	public static String inputMurderer(Scanner scanner) {
		//input murderer
		System.out
				.println("Please input the murderer.\n"
						+ "Miss (S)carlett,Colonel (M)ustard,Mrs. (W)hite,The Reverend (G)reen,Mrs. (P)eacock or Professor Pl(u)m?");
		System.out.println("S/M/W/G/P/u");
		String murdererStr = scanner.nextLine();
		String murdererName = null;
		if (murdererStr.equalsIgnoreCase("s")) {
			murdererName = "MissScarlett";
		} else if (murdererStr.equalsIgnoreCase("m")) {
			murdererName = "ColonelMustard";
		} else if (murdererStr.equalsIgnoreCase("w")) {
			murdererName = "MrsWhite";
		} else if (murdererStr.equalsIgnoreCase("g")) {
			murdererName = "TheReverendGreen";
		} else if (murdererStr.equalsIgnoreCase("p")) {
			murdererName = "MrsPeacock";
		} else if (murdererStr.equalsIgnoreCase("u")) {
			murdererName = "ProfessorPlum";
		} else {
			System.out.println("Invalid input.Please input again.");
			murdererStr = scanner.nextLine();
			while (!(murdererStr.equalsIgnoreCase("s")
					|| murdererStr.equalsIgnoreCase("m")
					|| murdererStr.equalsIgnoreCase("w")
					|| murdererStr.equalsIgnoreCase("g")
					|| murdererStr.equalsIgnoreCase("p") || murdererStr
						.equalsIgnoreCase("u"))) {
				System.out.println("Invalid input.Please input again.");
				murdererStr = scanner.nextLine();
			}
			if (murdererStr.equalsIgnoreCase("s")) {
				murdererName = "MissScarlett";
			} else if (murdererStr.equalsIgnoreCase("m")) {
				murdererName = "ColonelMustard";
			} else if (murdererStr.equalsIgnoreCase("w")) {
				murdererName = "MrsWhite";
			} else if (murdererStr.equalsIgnoreCase("g")) {
				murdererName = "TheReverendGreen";
			} else if (murdererStr.equalsIgnoreCase("p")) {
				murdererName = "MrsPeacock";
			} else if (murdererStr.equalsIgnoreCase("u")) {
				murdererName = "ProfessorPlum";
			}
		}
		return murdererName;
	}

	/**
	 * scan the string that user input .If the room name is valid, return the name
	 * @param scanner
	 * @return
	 */
	public static Square inputRoom(String roomStr) {
		Square roomSquare = null;
		if (roomStr.equalsIgnoreCase("b")) {
			roomSquare = new RoomBallRoom();
		} else if (roomStr.equalsIgnoreCase("i")) {
			roomSquare = new RoomBilliardRoom();
		} else if (roomStr.equalsIgnoreCase("c")) {
			roomSquare = new RoomConservatory();
		} else if (roomStr.equalsIgnoreCase("d")) {
			roomSquare = new RoomDiningRoom();
		} else if (roomStr.equalsIgnoreCase("h")) {
			roomSquare = new RoomHall();
		} else if (roomStr.equalsIgnoreCase("k")) {
			roomSquare = new RoomKitchen();
		} else if (roomStr.equalsIgnoreCase("l")) {
			roomSquare = new RoomLibrary();
		} else if (roomStr.equalsIgnoreCase("o")) {
			roomSquare = new RoomLounge();
		} else if (roomStr.equalsIgnoreCase("s")) {
			roomSquare = new RoomStudy();
		}
		return roomSquare;
	}

	/**
	 * scan the string that user input .If the weapon name is valid, return the name
	 * @param scanner
	 * @return
	 */
	public static Card inputWeapon(String weaponStr) {
		Card weaponCard = null;
		if (weaponStr.equalsIgnoreCase("c")) {
			weaponCard = new WeaptonCardCandlestick();
		} else if (weaponStr.equalsIgnoreCase("d")) {
			weaponCard = new WeaptonCardDagger();
		} else if (weaponStr.equalsIgnoreCase("l")) {
			weaponCard = new WeaptonCardLeadPipe();
		} else if (weaponStr.equalsIgnoreCase("r")) {
			weaponCard = new WeaptonCardRevolver();
		} else if (weaponStr.equalsIgnoreCase("o")) {
			weaponCard = new WeaptonCardRope();
		} else if (weaponStr.equalsIgnoreCase("s")) {
			weaponCard = new WeaptonCardSpanner();
		}
		return weaponCard;
	}

	/**
	 * check whether position x,y is in the range of can be moved
	 * @param x
	 * @param y
	 * @param board
	 * @return
	 */
	public static boolean hasPosition(int x, int y, Board board) {
		for (Position p : board.positionCanMove) {
			if (p.equal(x, y)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * find the player who should play in next turn
	 * @param num
	 * @param board
	 * @return
	 */
	public static int findNextPlayer(int num, Board board) {
		if (num >= board.players.size() - 1 || num < 0) {
			return 0;
		} else {
			return (num + 1);
		}
	}

	/**
	 * scan the user input , if it is valid number, build this game of input number players.
	 * @param scanner
	 * @param board
	 */
	public static void buildPlayer(int playerNumber, Board board) {
		//		int playerNumber = inputNumber(3, 6, scanner);
		for (int i = 0; i < playerNumber; i++) {
			Player player = randomPlayer();
			while (hasPlayer(player, board)) {
				player = randomPlayer();
			}
			board.players.add(player);
		}
	}

	/**
	 * check whether the charactor is a player in this game 
	 * @param player
	 * @param board
	 * @return
	 */
	public static boolean hasPlayer(Player player, Board board) {
		for (Player p : board.players) {
			if (p.name().equals(player.name())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * find a random player from 6 players.
	 * @return
	 */
	public static Player randomPlayer() {
		int num = ((int) (Math.random() * 6));
		if (num == 0) {
			return new ColonelMustard();
		} else if (num == 1) {
			return new MissScarlett();
		} else if (num == 2) {
			return new MrsWhite();
		} else if (num == 3) {
			return new MrsPeacock();
		} else if (num == 4) {
			return new ProfessorPlum();
		} else {
			return new TheReverendGreen();
		}
	}

	/**
	 * 
	 * Input a number from the keyboard. The number must be between the min and
	 * max parameters.
	 * this method from swen222 lab3 
	 * 
	 * @param min
	 * @param max
	 * @param scanner
	 * @return
	 */
	private static int inputNumber(int min, int max, Scanner scanner) {
		while (true) {
			String x = scanner.nextLine();
			try {
				int answer = Integer.parseInt(x);
				if (answer >= min && answer <= max) {
					return answer;
				}
			} catch (NumberFormatException e) {
			}
			System.out.println("Cluedo need 3-6 players!\n"
					+ "Please input again.");
		}
	}

}
