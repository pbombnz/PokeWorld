package network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;

import game.Game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is used to register classes that will be
 * sent over the network with both the server and client.
 *
 * This must be done before any further communication occurs,
 * AND MUST BE DONE IN THE SAME ORDER ON BOTH CLIENT AND SERVER!
 *
 * @author Prashant Bhikhu
 */
public class SerialisationRegister {
	public static void register(Object host) {
		if (host instanceof Server) {
			Kryo kyro = ((Server) host).getKryo();
			
			kyro.register(byte[].class);
			kyro.register(int.class);
			kyro.register(String.class);
			
			kyro.register(Game.class);
			kyro.register(Room.class);
			kyro.register(Board.class);
			
		} else if (host instanceof Client) {
			Kryo kyro = ((Client) host).getKryo();
			
			kyro.register(Game.class);
			kyro.register(byte[].class);
		}
	}
}
