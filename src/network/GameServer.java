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
	private final int port = Network.DEFAULT_SERVER_PORT_TCP; // The TCP port the server is listening to
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
	
		// Create the server object and turn off debug unless its actually needed
		this.server = new Server(Network.DEFAULT_BUFFER_SIZE, Network.DEFAULT_BUFFER_SIZE);
		Log.set(Log.LEVEL_DEBUG);
	
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
		serverFrame.writeToConsole("[Server][Start] Attempting to bound server to TCP Port "+port+"...");
		try {
			this.server.bind(this.port, Network.DEFAULT_SERVER_PORT_UDP); 
		} catch(IOException e) {
			// Attempt to bound server to the TCP (and UDP Discovery port) failed
			serverFrame.writeToConsole("[Server][Start] Attempting to bound server to TCP Port "+port+"...FAILED!");
			this.server.stop();
			this.game = null;
		}
		serverFrame.writeToConsole("[Server][Start] Attempting to bound server to TCP Port "+port+"...SUCCESS!");
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
		//serverFrame.writeToConsole("Received connection from " + connection.getRemoteAddressTCP());
		//System.out.println(object);
		
		if (object instanceof ValidateNewPlayerUsername) {
			ValidateNewPlayerUsername packet = (ValidateNewPlayerUsername) object;
			ValidateNewPlayerUsername_Response packet_send = new ValidateNewPlayerUsername_Response();
			
			for(Player connectedPlayer : game.getPlayers()) {
				if(connectedPlayer.getName().equalsIgnoreCase(packet.name)){
					packet_send.valid = false;
					connection.sendTCP(packet_send);
					return;
				}
			}
			packet_send.valid = true;
			connection.sendTCP(packet_send);
		}
		else if (object instanceof ClientNewPlayer) {
			// Get the ClientNewPlayer Packet
			ClientNewPlayer packet = (ClientNewPlayer) object;
			handleNewPlayer(connection, packet);
		}	
		else if (object instanceof ClientQuit) {
			ClientQuit packet = (ClientQuit) object;
			String newName = game.getPlayerByID(packet.id).getName();
			game.getPlayers().remove(game.getPlayerByID(packet.id));
			server.sendToAllExceptTCP(packet.id, packet);
			
			ClientMessage joinMessagePacket = new ClientMessage();
			joinMessagePacket.playerName = newName;
			joinMessagePacket.message = "* Left Server *";
			server.sendToAllExceptTCP(connection.getID(), joinMessagePacket);
		}	
		
		else if(object instanceof PlayerUpdateLocationAndDirection) {
			serverFrame.writeToConsole("[Server][Recieved] Recieved UpdatePlayer Packet from Connection ID "+connection.getID()+".");
			serverFrame.writeToConsole("[Server][Sent] Sent Recieved UpdatePlayer Packet to all other clients.");
			PlayerUpdateLocationAndDirection packet = ((PlayerUpdateLocationAndDirection) object);
			
			Player player = game.getPlayerByID(packet.id);
			player.setLocation(packet.newLocation);
			player.setDirection(packet.newDirection);
			
			server.sendToAllExceptTCP(connection.getID(), packet);
		}
		
		else if(object instanceof ClientOnChoosePlayer) {
			ArrayList<Player> savedFilePlayers = new ArrayList<Player>();
			
			for(Player connectedPlayer: getGame().getPlayers()) {
				if(connectedPlayer.getId() == -1) {
					savedFilePlayers.add(connectedPlayer);
				}
			}
			
			ClientOnChoosePlayer_Response packet = new ClientOnChoosePlayer_Response();
			packet.savedFilePlayers = savedFilePlayers;
			connection.sendTCP(packet);
		}
		
		else if(object instanceof ClientUseExistingPlayer) {
			ClientUseExistingPlayer packet = ((ClientUseExistingPlayer) object);
			
			for(Player connectedPlayer: getGame().getPlayers()) {
				if(connectedPlayer.getId() == packet.oldId && connectedPlayer.getName().equals(packet.oldName)) {
					connectedPlayer.setId(packet.newId);
					connectedPlayer.setName(packet.newName);
				}
			}
			
			ClientMessage joinMessagePacket = new ClientMessage();
			joinMessagePacket.playerName = packet.newName;
			joinMessagePacket.message = "* Joined Server *";
			server.sendToAllExceptTCP(connection.getID(), joinMessagePacket);
			
			ClientNewGame newGame = new ClientNewGame();
			newGame.gameByteArray = game.toByteArray();
			
			// Send packet to client
			serverFrame.writeToConsole("[Server][Sent] Sent Game World to new client.");
			server.sendToAllTCP(newGame);
		}
		
		else if(object instanceof ClientMessage) {
			ClientMessage packet = (ClientMessage) object;
			serverFrame.writeToConsole("[Server][Client Message] Player Name: "+packet.playerName+" | Message: "+packet.message);
			server.sendToAllExceptTCP(connection.getID(), object);
		}
		
		else if(object instanceof PlayerUpdate) {
			PlayerUpdate packet = (PlayerUpdate) object;
			Player playerToUpdate = game.getPlayerByID(packet.id);
			serverFrame.writeToConsole("[Server][Player Update] Player : "+playerToUpdate.getName()+" | ID:"+playerToUpdate.getId());
			
			playerToUpdate.setAttack(packet.newAttack);
			playerToUpdate.setHealth(packet.newHealth);
			playerToUpdate.setPlayerLevel(packet.newPlayerLevel);
			
			server.sendToAllExceptTCP(connection.getID(), object);
		}
		
		else if(object instanceof PlayerPickUpItem) {
			PlayerPickUpItem packet = (PlayerPickUpItem) object;
			
			Player playerToUpdate = getGame().getPlayerByID(packet.id);
			playerToUpdate.getInventory().add(packet.item);
			
			BoardSquare sq = game.getRoomByName(packet.location.getRoom().getName()).getBoard().getSquareAt(packet.location.getY(), packet.location.getX());
			sq.setGameObjectOnSquare(null);
			
			server.sendToAllExceptTCP(packet.id, object);
		}
		
		else if(object instanceof PlayerDropItem) {
			PlayerDropItem packet = (PlayerDropItem) object;
			
			Player playerToUpdate = getGame().getPlayerByID(packet.id);
			playerToUpdate.getInventory().remove(packet.item);
			
			BoardSquare sq = game.getRoomByName(packet.location.getRoom().getName()).getBoard().getSquareAt(packet.location.getY(), packet.location.getX());
			sq.setGameObjectOnSquare(packet.item);
			
			server.sendToAllExceptTCP(packet.id, object);
		}
	}

	@Override
	public void idle (Connection connection) {
	}
	
	
	public void disconnect() {
		// Ask the user if the want to save (MAYBE FEATURE)
		
		// Tell all connected clients that the server is turning off
		server.sendToAllTCP(new ServerQuit());
		serverFrame.writeToConsole("[Server][Request] User requested to stop server. Disconnecting clients...");
		// Stop the server
		server.stop();
		
		// Safety measure just to make sure all server resources are let go
		try {
			server.dispose();
		} catch (IOException e) {
		}
		serverFrame.writeToConsole("[Server][Sent] Server Ended.");
	}
	
	// ================================================================
	// Handler Methods - Handles Incoming Packets in seperate Methods
	// ================================================================
	
	public void handleNewPlayer(Connection connection, ClientNewPlayer packet) {
		//Once all client validation is done, we need to create the location for the player
		
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
	 * @return The global copy of the game (Used when saving game to file)
	 */
	public Game getGame() {
		return game;
	}
	
	/**
	 * @return The global copy of the game (Used when loading game from file)
	 */
	public void setGame(Game game) {
		for(Player player : game.getPlayers()) {
			player.setId(-1);
		}
		this.game = game;
	}
}
