package network;

import java.io.IOException;

import ui.ServerFrame;
import game.Game;
import game.Location;
import network.Packets.*;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

public class GameServer extends Listener {
	private int port;
	private Game game;
	private ServerFrame serverFrame;
	private Server server;	
	
	public GameServer(final ServerFrame serverFrame) throws IOException {
		//if (port < 1 || port > 65535) {
		//	throw new NumberFormatException("Port out of range"); 
		//}
		
		this.game = Game.createTestMap();
		this.port = Network.DEFAULT_SERVER_PORT_TCP;
		this.serverFrame = serverFrame;	
	
		this.server = new Server(20480, 20480);
		Log.set(Log.LEVEL_NONE);
	
		serverFrame.writeToConsole("[Server][Start] Intialized.");
		serverFrame.writeToConsole("[Server][Start] Created Server Socket.");
		serverFrame.writeToConsole("[Server][Start] Created Game Object.");
		serverFrame.writeToConsole("[Server][Start] Registered Server for Packet Serialisation.");
		
		Network.register(server);
		server.addListener(this);
		
		while(true) {
			serverFrame.writeToConsole("[Server][Start] Attempting to bound server to TCP Port "+port+"...");
			try {
				server.bind(this.port, Network.DEFAULT_SERVER_PORT_UDP); 
				break;
			} catch(IOException e) {
				serverFrame.writeToConsole("[Server][Start] Attempting to bound server to TCP Port "+port+"...FAILED!");
				this.port++;
				if(this.port > 65535) {
					this.port = 1;
				} else if (this.port == port) {
					throw new IOException("All Ports for the server are blocked or the Discovery Port is unusable on this device.");
				}
			}
			serverFrame.writeToConsole("[Server][Start] Attempting to bound server to TCP Port "+port+"...SUCCESS!");
		}
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
		
		/*if(object instanceof String) {
			serverFrame.writeToConsole("Received connection from " + (String) object);
		}  
		else*/ 
		if (object instanceof NewPlayer) {
			NewPlayer np = (NewPlayer) object;
			Location loc = new Location(game.rooms.get(0), 9, 0);
			np.player.setLocation(loc);
			game.players2.add(np.player);
			
			//System.out.println(game);
			//System.out.println(game.players2);
			//System.out.println(game.players2.size());
			
			//System.out.println(Game.fromByteArray(game.toByteArray()).toString());
			
			//send game back to client
			NewGame newGame = new NewGame();
			newGame.gameByteArray = game.toByteArray();
			
			
			connection.sendTCP(newGame);
		}		
		
	}

	@Override
	public void idle (Connection connection) {
	}
}
