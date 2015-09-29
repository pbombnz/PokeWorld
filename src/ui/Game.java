package ui;


import java.awt.Frame;

import javax.swing.JDialog;

import player.Player;
import ui.ChooseModeFrame;

/**
 *@author Wang Zhen
 * game
 */
public class Game {
	
	public static Player player;
	public static Boolean characterChoosed = false;
//	public static Board board;
	public static GameFrame gameFrame;

	public static void mainStep2() {
	}

	public static void mainStep3() {
		gameFrame = new GameFrame();
		gameFrame.printInformation(player);
		gameFrame.printCharacter(player);
		
	} 
	//	/**
	//	 * 
	//	 * Input a number from the keyboard. The number must be between the min and
	//	 * max parameters.
	//	 * this method from swen222 lab3 
	//	 * 
	//	 * @param min
	//	 * @param max
	//	 * @param scanner
	//	 * @return
	//	 */
	//	private static int inputNumber(int min, int max, Scanner scanner) {
	//		while (true) {
	//			String x = scanner.nextLine();
	//			try {
	//				int answer = Integer.parseInt(x);
	//				if (answer >= min && answer <= max) {
	//					return answer;
	//				}
	//			} catch (NumberFormatException e) {
	//			}
	//			System.out.println("Cluedo need 3-6 players!\n"
	//					+ "Please input again.");
	//		}
	//	}
}
