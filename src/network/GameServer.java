package network;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ui.ServerFrame;
import game.Game;
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
	
	public GameServer(int port, ServerFrame serverFrame) throws IOException {
		if (port < 1 || port > 65535) {
			throw new NumberFormatException("Port out of range"); 
		}
		this.game = new Game();
		this.port = port;
		this.players = new HashMap<Connection, String>();
		this.serverFrame = serverFrame;	
		
		this.server = new Server(20480, 20480);
		
		server.start(); 
		server.bind(port, port); 
		SerialisationRegister.register(server);
		server.addListener(new Listener() {
			

			@Override
			public void connected(Connection connection) {
				super.connected(connection);
				serverFrame.writeToConsole("Received connection from " + connection.getRemoteAddressTCP());
			}

			@Override
			public void disconnected(Connection connection) {
				super.disconnected(connection);

				serverFrame.writeToConsole(players.get(connection) + " disconnected");
			}

			public void received(Connection connection, Object object) {
			}
		});			
	}
}
