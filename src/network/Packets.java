package network;

import java.util.ArrayList;

import game.Location;
import game.Player;
import game.Direction;
import game.objects.interactiveObjects.Item;

/**
* These classes represent the different types of packets that can
* be sent across the network. Each packet represents a type of
* data being transferred from Clients to and from the Server.
*
* @author Prashant Bhikhu
*
*/
public class Packets {
	public static class ValidateNewPlayerUsername {
		String name;
	}
	public static class ValidateNewPlayerUsername_Response {
		boolean valid;
	}
	
	public static class ServerQuit {
	}	
		
	public static class ClientOnChoosePlayer {
	}
	
	public static class ClientOnChoosePlayer_Response {
		ArrayList<Player> savedFilePlayers;
	}
	/**
	 * USED: Client -> Server
	 *
	 * A packet containing the new player with the avatar and set name is
	 * sent to the server to5 be added the global game object within the server.
	 * 
	 * NOTE: Issues will occur when reading the Avatar object of the Player.
	 * This is because for some reason, when passing an ImageIcon object
	 * over the network, it adds the prefix "file:" which causes all ImageIcon
	 * Images to be unuseable. 
	 * 
	 */
	public static class ClientNewPlayer {
		Player player;
	}


	/**
	 * USED: Server -> Client
	 * 
	 * A packet that sends over the Game object from the server to
	 * a client when the client has already initialized a connection
	 * and chosen the player name and avatar.
	 *
	 */
	public static class ClientNewGame {
		byte[] gameByteArray;
	}
	
	
	/**
	 * USED: Client -> Server, then Server -> All Clients 
	 * 
	 * A packet used when there is any change in variables for a player
	 * (including move).
	 *
	 */
	public static class PlayerUpdateLocationAndDirection {
		int id;
		Location newLocation;
		Direction newDirection;
	}

	public static class PlayerUpdateAttack {
		int id;
		int newAttack;
	}

	public static class PlayerUpdateHealth {
		int id;
		int newAttack;
	}
	
	public static class PlayerPickUpItem {
		int id;
		Item item;
		Location location;
	}
	
	public static class PlayerDropItem {
		int id;
		Item item;
		Location location;
	}
	
	public static class PlayerUpdatePlayerLevel {
		int id;
		int newPlayerLevel;
	}	
	
	/**
	 * USED: Client -> Server, then Server -> All Clients 
	 * 
	 * A packet used when there is  a player sends a message for other players to view.
	 *
	 */
	public static class ClientMessage {
		String playerName;
		String message;
	}	
	
	/**
	 * USED: Client -> Server, then Server -> All Clients 
	 * 
	 * A packet that sends both ways, initially from client to server,
	 * then the server passes it to the all clients. Used when there is
	 * any change in variables for a player (including move).
	 *
	 */
	public static class ClientQuit {
		int id;
	}
}