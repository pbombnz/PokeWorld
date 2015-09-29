package network;

import game.Player;

/**
* These classes represent the different types of packets that can
* be sent across the network. Each packet represents a type of
* data being transferred from Client <--> Server.
*
* @author Prashant Bhikhu
*
*/
public class Packets {

	public static class NewPlayer {
		Player name;
	}

	public static class NewGame {
		Game game;
	}
	
}