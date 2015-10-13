package network;

/**
 * This Class allows me to update elements the UI or activate certain elements
 * without explicitly touching the UI or Game Logic.
 * 
 * @author Prashant Bhikhu
 *
 */
public interface GameClientListener {
	/**
	 * Handles when a new Game object is retrieved from the server or some other trigger will 
	 * cause a massive change to the game board. Used Mainly to redraw the canvas of the game.
	 */
	public void onGameUpdated();
	
	/**
	 * Handles when a player message is sent to the client. Sometimes used when the server sends a message
	 * to the client.
	 * 
	 * @param playerName The Player who sent the message
	 * @param message The message itself
	 */
	public void onMessageRecieved(String playerName, String message);
}
