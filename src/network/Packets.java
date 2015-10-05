package network;

import game.Game;
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
	/**
	 * USED: Client -> Server
	 *
	 * A packet containing the new player with the avatar and set name is
	 * sent to the server to be added the global game object within the server.
	 */
	public static class NewPlayer {
		Player player;
	}
	
	/** 
	 * USED: Server -> Client
	 * 
	 * Sends an empty packet to the client to understand that the name is already
	 * in use (or invalid is some other way that conflicts within the server).
	 */
	public static class NewPlayer_Error_NameAlreadyInUse {
	}

	/**
	 * USED: Server -> Client
	 * 
	 * A packet that sends over the game object from the server to
	 * a client when the client has already initalized a connection
	 * and chosen the player name and avatar.
	 *
	 */
	public static class NewGame {
		Game game;
	}
	
}