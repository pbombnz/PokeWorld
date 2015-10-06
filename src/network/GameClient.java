package network;

import game.Game;
import game.Player;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

import com.esotericsoftware.kryonet.*;
import com.esotericsoftware.kryonet.Listener;

import network.Packets.*;

public class GameClient extends Listener {
	private Game game;
	private Client client;
	
	
	public GameClient() throws IOException {
	    client = new Client(20480, 20480);
	    Network.register(client);

	    client.addListener(this);
	    client.start();
	    try {
			client.connect(5000, "localhost", Network.DEFAULT_SERVER_PORT_TCP, Network.DEFAULT_SERVER_PORT_UDP);
		} catch (IOException e) {
			throw new IOException(e);
		}
	    //client.sendTCP("hi");
	}

	public Game getGame() {
		return game;
	}
	
	public int getClientID() {
		return client.getID();
	}
	
	//public void sendTCP(Object object) {
	//	client.sendTCP(object);
	//}
	
	public void joinServer(Player player) {
		NewPlayer packet = new NewPlayer();
		packet.player = player;
		packet.player.setId(client.getID());
		client.sendTCP(packet);
	}
	
	/**
	 * Returns the list of servers that the client can join
	 * 
	 * @return
	 */
	public List<InetAddress> getServerList() {
		return client.discoverHosts(Network.DEFAULT_SERVER_PORT_UDP, 5000);
	}
	
	@Override
	public void received (Connection connection, Object object) {
		//System.out.println();
		if(object instanceof NewGame) {
			byte[] gameBytes = ((NewGame) object).gameByteArray;
			game = Game.fromByteArray(gameBytes);
			// GET CLIENT PLAYER BY CONNECTION ID
			// SET CLIENT PLAYER
		}
		else if(object instanceof PlayerUpdate) {
			PlayerUpdate packet = ((PlayerUpdate) object);
			//if(packet.player == CLIENT PLAYER) {
			// 	IGNORE
			//} else {
			//	GAME.GET(PLAYER) BY ID, UPDATE CLIENT GAME
			//}
		}
	} 
}

