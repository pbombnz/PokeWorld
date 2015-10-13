package network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.util.*;

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
	public static final int DEFAULT_BUFFER_SIZE = 16384; // Sets the buffer size of both clients and server.
	public static final int DEFAULT_DISCOVERY_TIMEOUT = 2000; // Sets the timeout for GameServer discovery (in milliseconds).
	/**
	 * 
	 * @param host The socket object (which can either be a Kyro Client or Server)
	 */
	public static void register(Object host) {
		// Cast the host (regardless if client or server) to a Kyro object to allow for class object registration
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
		kyro.register(network.Packets.ValidateNewPlayerUsername.class);
		kyro.register(network.Packets.ValidateNewPlayerUsername_Response.class);
		
		kyro.register(network.Packets.PlayerUpdateLocationAndDirection.class);
		kyro.register(network.Packets.PlayerUpdate.class);
		kyro.register(network.Packets.PlayerPickUpItem.class);
		kyro.register(network.Packets.PlayerDropItem.class);
		
		kyro.register(network.Packets.ClientUseExistingPlayer.class);
		kyro.register(network.Packets.ClientOnChoosePlayer.class);
		kyro.register(network.Packets.ClientOnChoosePlayer_Response.class);
		kyro.register(network.Packets.ClientNewPlayer.class);
		kyro.register(network.Packets.ClientNewGame.class);
		kyro.register(network.Packets.ClientMessage.class);
		kyro.register(network.Packets.ClientQuit.class);
		
		kyro.register(network.Packets.ServerQuit.class);
		
		
		// Game Related Objects Registration
		kyro.register(game.Game.class);
		kyro.register(game.Location.class);
		kyro.register(game.Player.class);
		kyro.register(game.Direction.class);
		kyro.register(game.avatar.Avatar.class);
		kyro.register(game.avatar.Evolution.class);
		
		kyro.register(rooms.Room.class);
		kyro.register(rooms.Room1.class);
		kyro.register(rooms.Room2.class);
		kyro.register(rooms.Room3.class);
		kyro.register(rooms.Room4.class);
		
		kyro.register(rooms.EmptyBoard.class);
		kyro.register(rooms.Board.class);
		kyro.register(rooms.Board1.class);
		kyro.register(rooms.Board2.class);
		kyro.register(rooms.Board3.class);
		kyro.register(rooms.Board4.class);
		
		
		kyro.register(game.BoardSquare.class);
		kyro.register(game.BoardSquare[].class);
		kyro.register(game.BoardSquare[][].class);
		

		// Game Objects Registration
		kyro.register(game.objects.GameObject.class);
		
		kyro.register(game.objects.interactiveObjects.Item.class);
		kyro.register(game.objects.interactiveObjects.BadPotion.class);
		kyro.register(game.objects.interactiveObjects.GoodPotion.class);
		kyro.register(game.objects.interactiveObjects.Key.class);
		kyro.register(game.objects.interactiveObjects.Door.class);
		kyro.register(game.objects.interactiveObjects.RareCandy.class);
		
		kyro.register(game.objects.monster.Mewtwo.class);
		kyro.register(game.objects.monster.Monster.class);
		kyro.register(game.objects.monster.Rattata.class);
		kyro.register(game.objects.monster.Rhydon.class);
		kyro.register(game.objects.monster.Zubat.class);
		
		kyro.register(game.objects.scene.Plant.class);
		kyro.register(game.objects.scene.Fence.class);
		kyro.register(game.objects.scene.Tree.class);
	}
}
