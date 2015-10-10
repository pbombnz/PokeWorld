package network;

import java.io.IOException;

import rooms.Board;
import rooms.Room;
import ui.ServerFrame;
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
		this.server = new Server(Network.DEAFAULT_BUFFER_SIZE, Network.DEAFAULT_BUFFER_SIZE);
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
		serverFrame.writeToConsole("[Client] Connected from " + connection.getRemoteAddressTCP() + " (Connection ID: "+connection.getID()+").");
	}

	@Override
	public void disconnected (Connection connection) {
	}

	@Override
	public void received (Connection connection, Object object) {
		serverFrame.writeToConsole("Received connection from " + connection.getRemoteAddressTCP());
		System.out.println(object);
		
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
		else if (object instanceof NewPlayer) {
			System.out.println("Got the new player!");
			// Get the NewPlayer Packet
			NewPlayer np = (NewPlayer) object;
			
			/*// Make sure the newly created player from the new client 
			// doesn't have a conflicting name with other players
			for(Player connectedPlayer: game.getPlayers()) {
				if(connectedPlayer.getName().equalsIgnoreCase(np.player.getName())) {
					// In the case that there is an name conflict, we tell the client to enter another name
					server.sendToTCP(connection.getID(), new NewPlayer_Error_NameAlreadyInUse());
					serverFrame.writeToConsole("[Server][Received] New player from new client has a name already in use. Requesting client to change name...");
					return;
				}
			}*/
			
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
					// Checks if any connected player is on the same location (x,y position & same room)
					// as the possible new position for the new player
					//System.out.println("y:" +y+" x:"+x);
					
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
					//System.out.println("FOUND: y:" +y+" x:"+x);
					break yLoop;	
				}
			}
			
			serverFrame.writeToConsole("[Server][Recieved] Calculated new location for new player at ("+newLocY+","+newLocX+") in start room.");
			
			// Finally we create the new location and assign it the new player
			Room startingRoom = game.getRooms().get(0);
			Location newLoc = new Location(startingRoom, newLocX, newLocY);
			np.player.setLocation(newLoc);
			game.getPlayers().add(np.player);

			// Send game world object to client so they can load the game, as well as the final
			// version of the client player (so their client player can actually be drawn)
			NewGame newGame = new NewGame();
			newGame.gameByteArray = game.toByteArray();
			
			// Send packet to client
			serverFrame.writeToConsole("[Server][Sent] Sent Game World to new client.");
			server.sendToAllTCP(newGame);
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
	}

	@Override
	public void idle (Connection connection) {
	}
	
	
	public void disconnect() {
		// Ask the user if the want to save (MAYBE FEATURE)
		
		// Send a packet to each client that the game is going to end.
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
}
