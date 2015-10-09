package network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;

import game.*;
import game.avatar.Avatar;
import game.objects.*;

import java.awt.Image;
import java.util.*;

import javax.swing.ImageIcon;

import rooms.Board;
import rooms.Room1;

import game.objects.interactiveObjects.*;

/**
 * This class is used to register classes for the server
 * and client so they serialise these objects then send
 * them over the network, where they can be deserialised.
 *
 * This must be done on both the client and server, before any 
 * network communication occurs. It is very important that the 
 * exact same classes are registered on both the client and server,
 * and that they are registered in the exact same order. 
 * 
 * Because of this, typically the code that registers classes is placed 
 * in a method on a class available to both the client and server
 *
 * @author Prashant Bhikhu
 */
public class Network {
	public static final int DEFAULT_SERVER_PORT_TCP = 8777; // Used for Game Server Communication
	public static final int DEFAULT_SERVER_PORT_UDP = 8778; // Used for Game Server Discovery
	
	/**
	 * 
	 * @param host The socket object (which can either be a Kyro Client or Server)
	 */
	public static void register(Object host) {
		// Cast the object to a Kyro object to allow for class object registration
		Kryo kyro;
		if (host instanceof Server) {
			kyro = ((Server) host).getKryo();
		}
		else
		{
			kyro = ((Client) host).getKryo();
		}
		
		// Register all classes that are possibly going to be used when sending and recieving objects
		
		// Primitive Object Registration
		kyro.register(byte[].class);
		kyro.register(int.class);
		kyro.register(boolean.class);
		
		// General Object Registration
		kyro.register(String.class);
		kyro.register(List.class);
		kyro.register(ArrayList.class);
		kyro.register(Map.class);
		kyro.register(HashMap.class);
		kyro.register(Set.class);
		kyro.register(HashSet.class);
		kyro.register(Image.class);
		kyro.register(ImageIcon.class);
		
		// Network Packet Registration
		kyro.register(Packets.ValidateNewPlayerUsername.class);
		kyro.register(Packets.ValidateNewPlayerUsername_Response.class);
		kyro.register(Packets.NewPlayer.class);
		kyro.register(Packets.PlayerMove.class);
		kyro.register(Packets.PlayerQuit.class);
		kyro.register(Packets.NewGame.class);
		kyro.register(Packets.PlayerMessage.class);
		
		// Game Related Objects Registration
		kyro.register(Game.class);
		kyro.register(Location.class);
		kyro.register(Player.class);
		kyro.register(game.Player.Direction.class);
		kyro.register(Avatar.class);
		kyro.register(Room1.class);
		kyro.register(Board.class);
		kyro.register(BoardSquare.class);
		kyro.register(BoardSquare[][].class);
		
		// Game Objects Registration
		kyro.register(GameObject.class);
		kyro.register(Item.class);
		kyro.register(BadPotion.class);
		kyro.register(GoodPotion.class);
		kyro.register(Key.class);
	}
}
