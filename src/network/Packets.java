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
	
	/**
	 * USED: Client -> Server
	 * 
	 * This packet is used when the clients needs to verify if the user name 
	 * they choose is unique 
	 * 
	 */
	public static class ValidateNewPlayerUsername {
		String name;
	}
	
	/**
	 * USED: Server -> Client
	 * 
	 * This packet is used when the clients needs to verify if the user name 
	 * they choose is unique, this packet confirms whether it is or not to the
	 * client who is awaiting for a responses
	 * 
	 */
	public static class ValidateNewPlayerUsername_Response {
		boolean valid;
	}	
		
	/**
	 * USED: Client -> Server 
	 *
	 * This packet is used when the client tells the server that it is ready
	 * to choose a player
	 */
	public static class ClientOnChoosePlayer {
	}

	/**
	 * USED: Server -> Client 
	 *
	 * This packet is used when the server responds to a specific client's packet
	 * (ClientOnChoosePlayer) and sends over any saved players from a file. This is
	 * so the client knows to pick a savedFilePlayer if there is any or whether to 
	 * create a completely new player.
	 */
	public static class ClientOnChoosePlayer_Response {
		ArrayList<Player> savedFilePlayers;
	}
	
	/**
	 * USED: Client -> Server 
	 *
	 * This packet is used when the client picks a client player from the saved
	 * file. It gets handled by the server differently as it replaced the old
	 * ID and name with the new name and IDs so the client can use it
	 */
	public static class ClientUseExistingPlayer {
		int oldId;
		int newId;
		String oldName;
		String newName;
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
	 * Images to be unusable. 
	 * 
	 * UPDATE NOTE: I don't think its doing the above anymore
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
	 * USED: Client -> Server, then Server -> All Client
	 *
	 * This packet is used when the local player is updated. This sends
	 * a message to the server and passes through to other clients to
	 * update other player's info.
	 */
	public static class PlayerUpdate {
		int id;
		int newPlayerLevel;
		int newHealth;
		int newAttack;
	}
	
	/**
	 * USED: Client -> Server, then Server -> All Clients 
	 * 
	 * A packet used when there is any change in location or direction for a player 
	 */
	public static class PlayerUpdateLocationAndDirection {
		int id;
		Location newLocation;
		Direction newDirection;
	}

	/**
	 * USED: Client -> Server, then Server -> All Clients 
	 * 
	 * A packet used when a player picks up a particular item, the location, item and 
	 * the player that picked it up (the player ID) need to be sent to everyone so
	 * all client's game, see the object disappear, and within the game object, added
	 * to the player's inventory.
	 */
	public static class PlayerPickUpItem {
		int id;
		Item item;
		Location location;
	}
	
	/**
	 * USED: Client -> Server, then Server -> All Clients 
	 * 
	 * A packet used when a player dropped a particular item, the location dropped on, item and 
	 * the player that dropped it (the player ID) need to be sent to everyone so
	 * all client's game, see the object appear on the map and within the gae object, removed
	 * from the player's inventory.
	 */
	public static class PlayerDropItem {
		int id;
		Item item;
		Location location;
	}
	
	
	/**
	 * USED: Client -> Server, then Server -> All Clients 
	 * 
	 * A packet used when there is a player sends a message for other players to view.
	 */
	public static class ClientMessage {
		String playerName;
		String message;
	}	
	
	/**
	 * USED: Client -> Server, then Server -> All Clients 
	 * 
	 * Used when a player leaves the server. This notifies other client games to remove
	 * the player from the game board (and essentially the whole game).
	 */
	public static class ClientQuit {
		int id;
	}
	
	/**
	 * USED: Server -> Client
	 * 
	 * A packet that tells clients that the server has ended, hence end of gameplay.
	 */
	public static class ServerQuit {
	}
}