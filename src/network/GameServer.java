package network;

import java.io.IOException;
import java.util.ArrayList;

import rooms.Board;
import rooms.Room;
import ui.ServerFrame;
import game.BoardSquare;
import game.Game;
import game.Location;
import game.Player;
import network.Packets.*;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log; 

/**
 * This class handles the Server Connection of the game and the interactions 
 * of incoming and outgoing packets. The Server will have a global master 
 * copy of the game world and pass the whole game or specific objects of the game
 * (such as players) to all clients when the game world needs to be updated 
 * globally.
 * 
 * @author Prashant Bhikhu
 *
 */
public class GameServer extends Listener {
	private static final boolean DEBUG = false;
	private Game game; // The global game world object that is given to all clients
	private ServerFrame serverFrame; // The frame in which console messages are written in to
	private Server server; // The actual server handling incoming/outgoing packets
	
	/**
	 * Create a new GameServer object
	 * 
	 * @param serverFrame The frame in which console messages are written in to
	 * @throws IOException Thrown when Server cannot be bound to any port
	 */
	public GameServer(ServerFrame serverFrame) throws IOException {
		this.game = Game.createTestMap(); // Creates the game object
		this.serverFrame = serverFrame;	
	
		// Create the server object 
		this.server = new Server(Network.DEFAULT_BUFFER_SIZE, Network.DEFAULT_BUFFER_SIZE);
		
		// Determine whether to write debug messages in Java console or not
		if(DEBUG) {
			Log.set(Log.LEVEL_DEBUG); 
		} else {
			Log.set(Log.LEVEL_NONE); 
		}
	
		// Write messages to notify the user what is happened so far
		serverFrame.writeToConsole("[Server][Start] Intialized.");
		serverFrame.writeToConsole("[Server][Start] Created Server Socket.");
		serverFrame.writeToConsole("[Server][Start] Created Game Object.");
		serverFrame.writeToConsole("[Server][Start] Registered Server for Packet Serialisation.");
		
		// Register the server so we can pass serialized objects in packets
		Network.register(this.server);
		// Set the listener to this class so we can handle incoming packets
		this.server.addListener(this);
		
		// Attempt to bound server to the TCP (and UDP Discovery port)
		serverFrame.writeToConsole("[Server][Start] Attempting to bound server to TCP Port "+Network.DEFAULT_SERVER_PORT_TCP+"...");
		try {
			this.server.bind(Network.DEFAULT_SERVER_PORT_TCP, Network.DEFAULT_SERVER_PORT_UDP); 
		} catch(IOException e) {
			// Attempt to bound server to the TCP (and UDP Discovery port) failed
			serverFrame.writeToConsole("[Server][Start] Attempting to bound server to TCP Port "+Network.DEFAULT_SERVER_PORT_TCP+"...FAILED!");
			this.server.stop();
			this.game = null;
		}
		serverFrame.writeToConsole("[Server][Start] Attempting to bound server to TCP Port "+Network.DEFAULT_SERVER_PORT_TCP+"...SUCCESS!");
		// Start the server after a port is bound
		this.server.start(); 
	}
	
	@Override
	public void connected (Connection connection) {
		serverFrame.writeToConsole("[Server][Client] Connected from " + connection.getRemoteAddressTCP() + " (Connection ID: "+connection.getID()+").");
	}

	@Override
	public void disconnected (Connection connection) {
		serverFrame.writeToConsole("[Server][Client] Disconnected from " + connection.getRemoteAddressTCP() + " (Connection ID: "+connection.getID()+").");
	}

	@Override
	public void received (Connection connection, Object object) {
		// Handles the incoming packets
		
		if (object instanceof ValidateNewPlayerUsername) {
			validatePlayerName(connection, object);
		} 
		else if (object instanceof ClientNewPlayer) {
			handleNewPlayer(connection, object);
		} 
		else if (object instanceof ClientQuit) {
			handleClientQuit(connection, object);
		}	
		else if(object instanceof PlayerUpdateLocationAndDirection) {
			handlePlayerUpdateLocation(connection, object);
		}
		else if(object instanceof ClientOnChoosePlayer) {
			handleClientOnChoosePlayer(connection);
		}
		else if(object instanceof ClientUseExistingPlayer) {
			handleClientUseExistingPlayer(connection, object);
		}
		else if(object instanceof ClientMessage) {
			handleClientMessage(connection, object);
		}
		else if(object instanceof PlayerUpdate) {
			handlePlayerUpdate(connection, object);
		}
		else if(object instanceof PlayerPickUpItem) {
			handlePickupItem(object);
		}
		else if(object instanceof PlayerDropItem) {
			handlePlayerDropItem(object);
		}
	}

	/**
	 * @param object
	 */
	private void handlePlayerDropItem(Object object) {
		PlayerDropItem packet = (PlayerDropItem) object;
		
		Player playerToUpdate = getGame().getPlayerByID(packet.id);
		playerToUpdate.getInventory().remove(packet.item);
		
		BoardSquare sq = game.getRoomByName(packet.location.getRoom().getName()).getBoard().getSquareAt(packet.location.getY(), packet.location.getX());
		sq.setGameObjectOnSquare(packet.item);
		
		server.sendToAllExceptTCP(packet.id, object);
	}

	/**
	 * @param object
	 */
	private void handlePickupItem(Object object) {
		PlayerPickUpItem packet = (PlayerPickUpItem) object;
		
		Player playerToUpdate = getGame().getPlayerByID(packet.id);
		playerToUpdate.getInventory().add(packet.item);
		
		BoardSquare sq = game.getRoomByName(packet.location.getRoom().getName()).getBoard().getSquareAt(packet.location.getY(), packet.location.getX());
		sq.setGameObjectOnSquare(null);
		
		server.sendToAllExceptTCP(packet.id, object);
	}

	@Override
	public void idle (Connection connection) {
	}
	
	/**
	 * Safely disconnects the server so clients know. Can be
	 */
	public void disconnect() {
		// Tell all connected clients that the server is turning off
		server.sendToAllTCP(new ServerQuit());
		serverFrame.writeToConsole("[Server][Request] User requested to stop server. Disconnecting clients...");
		// Stop and close the server safely
		server.stop();
		server.close();
		// Write a confirmation message
		serverFrame.writeToConsole("[Server][Sent] Server Ended.");
	}

	/**
	 * @return The global copy of the game (Used when saving game to file)
	 */
	public Game getGame() {
		return game;
	}
	
	/**
	 * @return The global copy of the game (Used when loading game from file)
	 */
	public void setGame(Game game) {
		// Because the player in the loaded game are not associated to any client, we set
		// their id to '-1'  so the server knows these need to be assigned to clients.
		for(Player player : game.getPlayers()) {
			player.setId(-1);
		}
		this.game = game; // Sets the loaded game as the global game now
	}	
	
	/* 
	 * ================================================================
	 * Handler Methods - Handles Incoming Packets in separate Methods
	 * ================================================================
	 */
	
	/**
	 * Checks if a player's new chosen name is unique (ie. not other currently connected players have it)
	 * 
	 * @param connection The Client's connection
	 * @param object The packet incoming
	 */
	private void validatePlayerName(Connection connection, Object object) {
		ValidateNewPlayerUsername packet = (ValidateNewPlayerUsername) object;
		ValidateNewPlayerUsername_Response packetSend = new ValidateNewPlayerUsername_Response();
		
		for(Player connectedPlayer : game.getPlayers()) {
			if(connectedPlayer.getName().equalsIgnoreCase(packet.name)){
				packetSend.valid = false;
				connection.sendTCP(packetSend);
				return;
			}
		}
		packetSend.valid = true;
		connection.sendTCP(packetSend);
	}	
	
	/**
	 * Controls how a server handles a packet that indicates that a new playe wants to join the game
	 * 
	 * @param connection The Client's connection
	 * @param object The packet incoming
	 */
	private void handleNewPlayer(Connection connection, Object object) {
		// Gets the packet from the incoming object
		ClientNewPlayer packet = (ClientNewPlayer) object;
		
		//Once all client name validation is done, we need to create the location for the player
		
		// From top-right to top-left and going all the way, we find a location to put the player on the board
		// which involves checking if their are any conflicting game objects in the new position
		int newLocY = 0;
		int newLocX = 0;
		Board boardOfFirstRoom = game.getRooms().get(0).getBoard();
		
		yLoop:
		for(int y = 0; y < boardOfFirstRoom.getHeight(); y++) {
			xLoop:
			for(int x = boardOfFirstRoom.getWidth()-1; x >= 0; x--) {
				// Checks if any other connected player is on the same location 
				// as the possible new position for the new player
				
				for(Player player: game.getPlayers()) {
					Location playerLocation = player.getLocation();
					
					// Compares the location. if a player is found at the current location
					if(playerLocation.getRoom().equals(game.getRooms().get(0)) &&
					   playerLocation.getX() == x && 
					   playerLocation.getY() == y) {
						//System.out.println(playerLocation.toString());
						continue xLoop;
					}
				}
				
				// When any object (another player or game object) is occupying the location or the space, we keep looking for a new location
				if(boardOfFirstRoom.getSquareAt(y, x).getGameObjectOnSquare() != null) {
					continue xLoop;
				}
				
				//Reaching here, indicates the square is available for the player to be placed
				newLocY = y;
				newLocX = x;
				break yLoop;	
			}
		}
		
		serverFrame.writeToConsole("[Server][Recieved] Calculated new location for new player (ID:"+connection.getID()+") at ("+newLocY+","+newLocX+") in Start Room.");
		
		// Finally we create the new location and assign it the new player
		Room startingRoom = game.getRooms().get(0);
		Location newLoc = new Location(startingRoom, newLocX, newLocY);
		packet.player.setLocation(newLoc);
		game.getPlayers().add(packet.player);

		// Sends a packet (in form of a player message) to all clients saying a new user has joined
		ClientMessage joinMessagePacket = new ClientMessage();
		joinMessagePacket.playerName = packet.player.getName();
		joinMessagePacket.message = "* Joined Server *";
		server.sendToAllExceptTCP(connection.getID(), joinMessagePacket);
		
		// Send game world object to client so they can load the game, as well as the final
		// version of the client player (so their client player can actually be drawn)
		ClientNewGame newGame = new ClientNewGame();
		newGame.gameByteArray = game.toByteArray();
		
		// Send packet to client
		serverFrame.writeToConsole("[Server][Sent] Sent Game World to new client.");
		server.sendToAllTCP(newGame);
	}
	
	/**
	 * Handles a PlayerUpdate packet when received
	 * 
	 * @param connection The Client's connection
	 * @param object The packet incoming
	 */
	private void handlePlayerUpdate(Connection connection, Object object) {
		// Reads the packet and identifies the player to update
		PlayerUpdate packet = (PlayerUpdate) object;
		Player playerToUpdate = game.getPlayerByID(packet.id);
		serverFrame.writeToConsole("[Server][Player Update] Player : "+playerToUpdate.getName()+" | ID:"+playerToUpdate.getId());
		
		// update the player properties of the packet on the global game
		playerToUpdate.setAttack(packet.newAttack);
		playerToUpdate.setHealth(packet.newHealth);
		playerToUpdate.setPlayerLevel(packet.newPlayerLevel);
		
		// Passes the packet to all other clients so they can update their local game
		server.sendToAllExceptTCP(connection.getID(), object);
	}

	/**
	 * Handles client messages
	 * 
	 * @param connection The Client's connection
	 * @param object The packet incoming
	 */
	private void handleClientMessage(Connection connection, Object object) {
		// Reads the incoming message packet
		ClientMessage packet = (ClientMessage) object;
		// Writes a message on the server console
		serverFrame.writeToConsole("[Server][Client Message] Player Name: "+packet.playerName+" | Message: "+packet.message);
		// Passes the packet to all other clients so they can update their local game
		server.sendToAllExceptTCP(connection.getID(), object);
	}

	/**
	 * Handle when the client use existing player from the loaded game from a file.
	 * 
	 * @param connection The Client's connection
	 * @param object The packet incoming
	 */
	private void handleClientUseExistingPlayer(Connection connection, Object object) {
		// Reads the incoming handleClientUseExistingPlayer packet
		ClientUseExistingPlayer packet = ((ClientUseExistingPlayer) object);
		
		// Set the saved player's ID and name for the new client so they use getClientPlayer() locally
		for(Player connectedPlayer: getGame().getPlayers()) {
			if(connectedPlayer.getId() == packet.oldId && connectedPlayer.getName().equals(packet.oldName)) {
				connectedPlayer.setId(packet.newId);
				connectedPlayer.setName(packet.newName);
			}
		}
		
		// Sends a join message to all clients so they know
		ClientMessage joinMessagePacket = new ClientMessage();
		joinMessagePacket.playerName = packet.newName;
		joinMessagePacket.message = "* Joined Server *";
		server.sendToAllExceptTCP(connection.getID(), joinMessagePacket);
		
		// Send a copy of the game to the client
		ClientNewGame newGame = new ClientNewGame();
		newGame.gameByteArray = game.toByteArray();
		
		// Send packet to client
		serverFrame.writeToConsole("[Server][Sent] Sent Game World to new client.");
		server.sendToAllTCP(newGame);
	}

	/**
	 * Handles and chooses what do when a client said they are about to pick a character
	 * 
	 * @param connection The Client's connection
	 */
	private void handleClientOnChoosePlayer(Connection connection) {
		// Add all non-playing saved characters into a list so we can past it in a packet
		ArrayList<Player> savedFilePlayers = new ArrayList<Player>();
		for(Player connectedPlayer: getGame().getPlayers()) {
			if(connectedPlayer.getId() == -1) {
				savedFilePlayers.add(connectedPlayer);
			}
		}
		// Send a new response packet with the saved players so the clients knows which players to pick out of
		ClientOnChoosePlayer_Response packet = new ClientOnChoosePlayer_Response();
		packet.savedFilePlayers = savedFilePlayers;
		connection.sendTCP(packet);
	}	
	
	/**
	 * Handles packets that are about a specific player's location or direction moving
	 * 
	 * @param connection the client connection
	 * @param object the packet incoming
	 */
	private void handlePlayerUpdateLocation(Connection connection, Object object) {
		// Retrieve incoming object as packet
		PlayerUpdateLocationAndDirection packet = ((PlayerUpdateLocationAndDirection) object);
		// Set the player's new location on the global game
		Player player = game.getPlayerByID(packet.id);
		player.setLocation(packet.newLocation);
		player.setDirection(packet.newDirection);
		// Passes the retrieved packet to clients so they can do the update the location on their screen
		server.sendToAllExceptTCP(connection.getID(), packet);
	}
	
	/**
	 * Handles the way a client quits (Assuming they quit nicely)
	 * 
	 * @param connection The Client's connection
	 * @param object The packet incoming
	 */
	private void handleClientQuit(Connection connection, Object object) {
		// Casts the incoming object as a packet
		ClientQuit packet = (ClientQuit) object;
		
		// Deletes leaving player from global game
		String playerName = game.getPlayerByID(packet.id).getName();
		game.getPlayers().remove(game.getPlayerByID(packet.id));
		
		// Passes the message through the server to clients so they can delete the player off their game
		server.sendToAllExceptTCP(packet.id, packet);
		
		// Sends a quit message to the clients so that clients are aware of the player quitting
		ClientMessage packetSend = new ClientMessage();
		packetSend.playerName = playerName;
		packetSend.message = "* Left Server *";
		server.sendToAllExceptTCP(connection.getID(), packetSend);
	}
}
