package network;

public interface GameClientListener {
	public void onGameUpdated();
	public void onServerRestart();
	public void onMessageRecieved(String playerName, String message);
}
