package network;

/**
* These classes represent the different types of packets that can
* be sent across the network. Each packet represents a type of
* data being transferred from Client <--> Server.
*
* @author Josh Brake, 300274198, brakejosh
*
*/
public class Packets {

	/**
	 * A chat message, containing the name of the player and
	 * their message. Sent Client -> Server
	 */
	public static class ChatMessage {
		String name, message;
	}

	/**
	 * A move, containing the name of the player and the key code
	 * of the key they pressed. Sent Client -> Server
	 */
	public static class Move {
		String name;
		int key;
	}

	/**
	 * A new player joining the game, containing their user name.
	 * Sent Client -> Server
	 */
	public static class NewPlayer {
		String name;
	}

	/**
	 * A new game update, containing the changes from other users.
	 * Is serialized by GameWorld, sent, and constructed as a new GameWorld
	 * from the data structures inside. Sent Server -> Client
	 */
	public static class NewGame {
		byte[] game;
	}
}