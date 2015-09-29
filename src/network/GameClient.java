package network;

import game.Game;

import java.io.IOException;

import com.esotericsoftware.kryonet.*;

import network.Packets.*;

public class GameClient {
	private Game game;
	private Client client;
	
	public GameClient() throws IOException {
	    client = new Client();
	    SerialisationRegister.register(client);

	    /*client.addListener(new Listener() {
			public void received (Connection connection, Object object) {
				System.out.println();
			}
		});*/
	    client.start();
	    try {
			client.connect(5000, "localhost", 7777);
		} catch (IOException e) {
			throw new IOException(e);
		}
	    //client.sendTCP("hi");
	}

	public Game getGame() {
		return game;
	}
}
