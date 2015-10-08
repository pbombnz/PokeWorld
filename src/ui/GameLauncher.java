package ui;

import java.awt.Frame;

/**
 *@author Wang Zhen
 */
public class GameLauncher {
	public static int ROOMINDEX = 0;
	Frame gameFrame;
	public GameLauncher() {
		gameFrame = new GamePlayFrame();
	}
}
