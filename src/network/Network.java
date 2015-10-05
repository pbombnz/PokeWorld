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

/**
 * This class is used to register classes that will be
 * sent over the network with both the server and client.
 *
 * This must be done before any further communication occurs,
 * AND MUST BE DONE IN THE SAME ORDER ON BOTH CLIENT AND SERVER!
 *
 * @author Prashant Bhikhu
 */
public class Network {
	public static final int DEFAULT_SERVER_PORT_TCP = 8777; // Used for Game Server Communication
	public static final int DEFAULT_SERVER_PORT_UDP = 8778; // Used for Game Server Discovery
	
	public static void register(Object host) {
		Kryo kyro;
		if (host instanceof Server) {
			kyro = ((Server) host).getKryo();
		}
		else
		{
			kyro = ((Client) host).getKryo();
		}
		
		kyro.register(byte[].class);
		kyro.register(int.class);
		kyro.register(boolean.class);
		
		kyro.register(String.class);
		kyro.register(List.class);
		kyro.register(ArrayList.class);
		kyro.register(Map.class);
		kyro.register(HashMap.class);
		kyro.register(Set.class);
		kyro.register(HashSet.class);
		kyro.register(Image.class);
		kyro.register(ImageIcon.class);
		
		kyro.register(Packets.NewPlayer.class);
		kyro.register(Packets.NewGame.class);
		
		kyro.register(Game.class);
		kyro.register(Location.class);
		kyro.register(Player.class);
		kyro.register(game.Player.Direction.class);
		kyro.register(Avatar.class);
		kyro.register(Room.class);
		kyro.register(Board.class);
		kyro.register(BoardSquare.class);
		kyro.register(BoardSquare[][].class);
		
		kyro.register(GameObject.class);
		kyro.register(Item.class);
		kyro.register(Weapon.class);
		kyro.register(BadPotion.class);
		kyro.register(Chest.class);
		kyro.register(Dagger.class);
		kyro.register(Enemies.class);
		kyro.register(GiantSword.class);
		kyro.register(GoodPotion.class);
		kyro.register(Key.class);
		kyro.register(Machete.class);
		kyro.register(Weapon.class);		
		kyro.register(Weapon.class);
	}
}
