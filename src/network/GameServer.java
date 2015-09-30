package network;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ui.ServerFrame;
import game.Game;
import game.objects.Key;
import network.Packets.*;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class GameServer {
	private int port;
	private Game game;
	private ServerFrame serverFrame;
	private Server server;
	private Map<Connection, String> players;	
	
	public GameServer(ServerFrame serverFrame) throws IOException {
		//if (port < 1 || port > 65535) {
		//	throw new NumberFormatException("Port out of range"); 
		//}
		this.game = Game.createTestMap();
		this.port = Network.PORT;
		this.players = new HashMap<Connection, String>();
		this.serverFrame = serverFrame;	
		
		this.server = new Server(20480, 20480);
		
		Network.register(server);
		server.addListener(new Listener() {
			

			@Override
			public void connected(Connection connection) {
				super.connected(connection);
				serverFrame.writeToConsole("[Client] Connected from " + connection.getRemoteAddressTCP() + ".");
				serverFrame.writeToConsole("[Client] Sending Game to the Client.");
				//connection.sendTCP(game.toByteArray());
				
				//Game g = Game.fromByteArray(game.toByteArray());
				//System.out.println(g.rooms.size());
				//System.out.println("the key is here: " + (g.rooms.get(0).board.getSquares()[3][4].getGameObjectOnSquare() instanceof Key));
			}

			@Override
			public void disconnected(Connection connection) {
				super.disconnected(connection);
				serverFrame.writeToConsole(players.get(connection) + " disconnected");
			}

			public void received(Connection connection, Object object) {
				serverFrame.writeToConsole("Received connection from " + connection.getRemoteAddressTCP());
				if(object instanceof String) {
					serverFrame.writeToConsole("Received connection from " + (String) object);
				} //else {
				//	serverFrame.writeToConsole("WTF " + object.toString());
				//}

			}
		});	
		server.bind(port); 
		server.start(); 
	}
}
