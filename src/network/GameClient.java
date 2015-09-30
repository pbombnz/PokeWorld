package network;

import game.Game;
import game.Player;

import java.io.IOException;

import com.esotericsoftware.kryonet.*;

import network.Packets.*;

public class GameClient {
	private Game game;
	private Client client;
	
	public GameClient() throws IOException {
	    client = new Client(20480, 20480);
	    Network.register(client);

	    client.addListener(new Listener() {
			public void received (Connection connection, Object object) {
				//System.out.println();
				if(object instanceof byte[]) {
					byte[] gameBytes = (byte[]) object;
					System.out.println(Game.fromByteArray(gameBytes).toString());
					game = Game.fromByteArray(gameBytes);
					System.out.println("game is received from server to client");
				}
			} 
		});
	    
	    client.start();
	    try {
			client.connect(5000, "localhost", Network.PORT);
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
		NewPlayer np = new NewPlayer();
		np.player = player;
		client.sendTCP(np);
	}
}
