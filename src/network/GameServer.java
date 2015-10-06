package network;

import java.io.IOException;

import ui.ServerFrame;
import game.Board;
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
 * of incoming and outcoming packets. The Server will have a global master 
 * copy of the game world and pass the whole game or specific objects of the game
 * (such as players) to all clients when the game world needs to be updated 
 * globally.
 * 
 * @author Prashant Bhikhu
 *
 */
public class GameServer extends Listener {
	private int port; // The TCP port the server is listening to
	private Game game; // The global game world object that is given to all clients
	private ServerFrame serverFrame; // The frame in which console messages are written in to
	private Server server; // The actual server handling incoming/outcoming packets
	
	/**
	 * Create a new GameServer object
	 * 
	 * @param serverFrame The frame in which console messages are written in to
	 * @throws IOException Thrown when Server cannot be bound to any port
	 */
	public GameServer(final ServerFrame serverFrame) throws IOException {
		this.game = Game.createTestMap(); // Creates the game object
		this.port = Network.DEFAULT_SERVER_PORT_TCP; // Assign the default port used for the server
		this.serverFrame = serverFrame;	
	
		// Create the server object and turn off debug unless its actually needed
		this.server = new Server(20480, 20480);
		Log.set(Log.LEVEL_NONE);
	
		// Write messages to notify the user what is happened so far
		serverFrame.writeToConsole("[Server][Start] Intialized.");
		serverFrame.writeToConsole("[Server][Start] Created Server Socket.");
		serverFrame.writeToConsole("[Server][Start] Created Game Object.");
		serverFrame.writeToConsole("[Server][Start] Registered Server for Packet Serialisation.");
		
		// Register the server so we can pass serialized objects in packets
		Network.register(server);
		// Set the listener to this class so we can handle incoming packets
		server.addListener(this);
		
		// Attempt to bound server to the TCP (and UDP Discovery port)
		while(true) {
			serverFrame.writeToConsole("[Server][Start] Attempting to bound server to TCP Port "+port+"...");
			try {
				server.bind(this.port, Network.DEFAULT_SERVER_PORT_UDP); 
				break;
			} catch(IOException e) {
				// Attempt to bound server to the TCP (and UDP Discovery port) failed
				serverFrame.writeToConsole("[Server][Start] Attempting to bound server to TCP Port "+port+"...FAILED!");
				// on failure, try the next incremented port to bound to
				this.port++;
				// Make sure the new port is within the standard port range, otherwise if 
				// exceeded 65535, then reset to lowest legal port number, 1
				if(this.port > 65535) {
					this.port = 1;
				} else if (this.port == Network.DEFAULT_SERVER_PORT_TCP) {
					// in the case we loop through all TCP port but didnt find a port to bound, then we produce an error
					throw new IOException("All Ports for the server are blocked or the Discovery Port is unusable on this device.");
				}
			}
			serverFrame.writeToConsole("[Server][Start] Attempting to bound server to TCP Port "+port+"...SUCCESS!");
		}
		// Start the server after a port is bound
		server.start(); 
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
		
		if (object instanceof NewPlayer) {
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
			Location newLoc = new Location(game.rooms.get(0), newLocX, newLocY);
			np.player.setLocation(newLoc);
			game.getPlayers().add(np.player);

			// Send game world object to client so they can load the game, as well as the final
			// version of the client player (so their client player can actually be drawn)
			NewGame newGame = new NewGame();
			newGame.gameByteArray = game.toByteArray();
			
			// Send packet to client
			serverFrame.writeToConsole("[Server][Sent] Sent Game World to new client.");
			server.sendToTCP(connection.getID(), newGame);
		}		
		else if(object instanceof PlayerUpdate) {
			serverFrame.writeToConsole("[Server][Recieved] Recieved UpdatePlayer Packet from Connection ID "+connection.getID()+".");
			serverFrame.writeToConsole("[Server][Sent] Sent Recieved UpdatePlayer Packet to all other clients.");
			PlayerUpdate packet = ((PlayerUpdate) object);
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
