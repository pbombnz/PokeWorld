package network;

import game.Game;
import game.Player;
import game.avatar.Avatar;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import ui.GamePlayFrame;

import com.esotericsoftware.kryonet.*;

import network.Packets.NewGame;
import network.Packets.ValidateNewPlayerUsername_Response;
import network.Packets.*;

/**
 * This class handles the Client Connection of the game and passes interactions 
 * to the server. The Client will have a local copy of the Game object and constantly
 * be updated from the server from other client interactions and update the local
 * copy of the Game object.
 * 
 * @author Prashant Bhikhu
 *
 */
public class GameClient extends Listener {
	private Game game; // The local copy of the game (reducing Large network packet transfers)
	private Client client; // The actual client connection used to connect to the server
	
	// Used to get responses from the server when the client is awaiting for server feedback
	private Stack<Object> recievedServerReponses = new Stack<Object>();

	/**
	 * Create a new GameServer object
	 * 
	 * @param serverFrame The frame in which console messages are written in to
	 * @throws IOException Thrown when Client cannot connect to a server
	 */
	public GameClient() {
		
	    client = new Client(20480, 20480);
	    Network.register(client);
	    
	    client.addListener(this);
	    client.start();
	}
	
	public void connect(InetAddress host) throws IOException {
	    try {
			client.connect(5000, host, Network.DEFAULT_SERVER_PORT_TCP, Network.DEFAULT_SERVER_PORT_UDP);
		} catch (IOException e) {
			throw new IOException(e);
		}
	}

	public Game getGame() {
		return game;
	}
	
	//public void sendTCP(Object object) {
	//	client.sendTCP(object);
	//}
	
	public boolean isNewPlayerUsernameValid(String name) {
		ValidateNewPlayerUsername packet = new ValidateNewPlayerUsername();
		packet.name = name;
		client.sendTCP(packet);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
		
		Object object = getRecievedServerReponses().pop();
			
		if(object instanceof ValidateNewPlayerUsername_Response) {
			ValidateNewPlayerUsername_Response packet_recv = (ValidateNewPlayerUsername_Response) object;
			return packet_recv.valid;
		}  else {
			 throw new RuntimeException("FIND THE GOD DAMN PACKET");
		}
	}
	
	
	public void createPlayer(String playerUsername, Avatar playerAvatar) {
		// Create the Player with the variables supplied and the connection ID as well
		Player player = new Player(client.getID(), playerUsername, playerAvatar);
		
		NewPlayer packet = new NewPlayer();
		packet.player = player;
		client.sendTCP(packet);
	}
	
	
	
	/**
	 * Returns the list of servers that the client can join. May not be needed
	 * 
	 * @return
	 */
	public List<InetAddress> getServerList() {
		return client.discoverHosts(Network.DEFAULT_SERVER_PORT_UDP, 2000);
	}
	
	@Override
	public void received (Connection connection, Object object) {
		//System.out.println();
		if(object instanceof NewGame) {
			handleNewGamePacket((NewGame) object);
		}
		else if(object instanceof PlayerMove) {
			PlayerMove packet = ((PlayerMove) object);
			for(Player connectedPlayer: game.getPlayers()) {
				if(connectedPlayer.getId() == packet.id) {
					connectedPlayer.setLocation( packet.newLocation);
				}
			}
			//	GAME.GET(PLAYER) BY ID, UPDATE CLIENT GAME
		}
		
		else if(object instanceof ValidateNewPlayerUsername_Response) {
			recievedServerReponses.add(object);
		}
	}

	public void handleNewGamePacket(NewGame packet) {
		byte[] gameBytes = packet.gameByteArray;
		game = Game.fromByteArray(gameBytes);
		
		List<Avatar> avatars = null;
		try {
			avatars = Avatar.getAllAvatars();
		} catch (IOException e) {
		}
		
		for(Player connectedPlayer: game.getPlayers()) {
			for(Avatar avatar: avatars) {
				if(avatar.getAvatarName().equals(connectedPlayer.getAvatar().getAvatarName())) {
					connectedPlayer.setAvatar(avatar);
				}
			}
		}
	}
	
	public Player getClientPlayer() {
		for(Player connectedPlayer: game.getPlayers()) {
			if(connectedPlayer.getId() == client.getID()) {
				return connectedPlayer;
			}
		}
		return null;
	}
	
	public Stack<Object> getRecievedServerReponses() {
		return recievedServerReponses;
	}
}

