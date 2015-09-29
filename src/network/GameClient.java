package network;

import game.Game;

import java.io.IOException;
import java.util.Map;

import ui.GameFrame;
import ui.ServerFrame;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener;

import network.Packets.*;

public class GameClient {
	private Game game;
	private GameFrame gameFrame;
	private Client client;
	
	public GameClient() {
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
			System.out.println(e);
		}
	    client.sendTCP("hi"); System.out.println(client.getID());
	}
}
