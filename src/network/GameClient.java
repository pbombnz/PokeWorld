package network;

import game.Game;
import game.Player;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

import ui.GamePlayFrame;

import com.esotericsoftware.kryonet.*;

import network.Packets.*;

public class GameClient extends Listener {
	private Game game;
	private Client client;
	private GamePlayFrame clientFrame;
	
	
	public GameClient(GamePlayFrame clientFrame) throws IOException {
		this.clientFrame = clientFrame;
		
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
			
			if(game.getPlayers().size() == 1) {
				clientFrame.getClientPlayer().setLocation(game.getPlayers().get(0).getLocation());
				return;
			}
			for(Player connectedPlayer : game.getPlayers()) {
				if(connectedPlayer.getId() == clientFrame.getClientPlayer().getId()) {
					//System.out.println(connectedPlayer.getLocation().toString());
					clientFrame.setClientPlayer(connectedPlayer);
					clientFrame.repaint();
					return;
				}
			}
			// GET CLIENT PLAYER BY CONNECTION ID
			// SET CLIENT PLAYER
		}
		else if(object instanceof PlayerUpdate) {
			PlayerUpdate packet = ((PlayerUpdate) object);
			for(Player connectedPlayer: game.getPlayers()) {
				if(connectedPlayer.getId() == packet.player.getId()) {
					connectedPlayer.setLocation( packet.player.getLocation());
				}
			}
			//	GAME.GET(PLAYER) BY ID, UPDATE CLIENT GAME
		}
	} 
}

