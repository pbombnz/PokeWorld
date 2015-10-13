package ui;

import game.Game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import storage.GameToJson;
import storage.GameToXML;
import storage.JsonToGame;
import storage.SaverLoader;
import storage.XMLToGame;
import network.GameServer;
/**
 * create this frame for showing information of server
 */
@SuppressWarnings("serial")
public class ServerFrame extends JFrame implements ActionListener, WindowListener {
	// The Size of the Frame
	private static final int FRAME_WIDTH = 600;
	private static final int FRAME_HEIGHT = 300;
	
	
	
	private JMenuBar menuBar = new JMenuBar();
	
	JMenu gameMenu = new JMenu("Server");
	JMenuItem connect = new JMenuItem("Connect");
	JMenuItem disconnect = new JMenuItem("Disconnect");
	JMenuItem exit = new JMenuItem("Exit");
	
	JMenu SaveLoadMenu = new JMenu("Save");
	JMenuItem savePlayer = new JMenuItem("Save Game World");
	
	JMenu clearServerLogMenu = new JMenu("Clear Server Log...");
	JMenuItem clearServerLog = new JMenuItem("Clear");
	
	private JTextArea console = new JTextArea();
	private JScrollPane consoleScrollPane;
	
	private GameServer gameServer;
	private GameToXML saver;
	private XMLToGame loader;
	private SaverLoader sl;
	
	public ServerFrame() {
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		setResizable(false);

		// Create Menu
		menuBar.add(gameMenu);

		gameMenu.add(connect);
		gameMenu.add(disconnect);
		gameMenu.add(new JSeparator());
		gameMenu.add(exit);
		
		menuBar.add(SaveLoadMenu);

		SaveLoadMenu.add(savePlayer);
		
		menuBar.add(clearServerLogMenu);
		
		clearServerLogMenu.add(clearServerLog);
				
		connect.addActionListener(this);
		disconnect.addActionListener(this);
		exit.addActionListener(this);
		savePlayer.addActionListener(this);
		clearServerLog.addActionListener(this);
		
		// Sets the Console properties so that it cannot be editted (only for display), and auto scrolls
		console.setEditable(false);
		((DefaultCaret) console.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		// Wrap the console in a scrollpane to enable scrolling
		consoleScrollPane = new JScrollPane(console);

		// Add the menu and console scrollpane to the frame
		setJMenuBar(menuBar);
		add(consoleScrollPane);
		// Finally show frame
		setVisible(true);
		
		connect();
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		JMenuItem menuItem = (JMenuItem) arg0.getSource();
		
		if(menuItem == connect) {
			connect();
		} else if(menuItem == disconnect) {
			connect.setEnabled(true);
			disconnect.setEnabled(false);
			savePlayer.setEnabled(false);
			gameServer.disconnect();
			gameServer = null;
		} else if(menuItem == exit) {
			windowClosing(null);
		} else if (menuItem == savePlayer) {
			GameToJson.saveGame(this, gameServer.getGame());
			/*sl = new SaverLoader();
			try {
				String path = sl.savePath();
				sl.saveGame(path);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}*/
		} else if (menuItem == clearServerLog) {
			console.setText("");
		}
	}
	
	public void connect() {
		try {
			gameServer = new GameServer(this);
			connect.setEnabled(false);
			disconnect.setEnabled(true);
			savePlayer.setEnabled(true);
		} catch (IOException e) {
			JOptionPane.showInternalMessageDialog(this, "Cannot connect", "Server Error", JOptionPane.ERROR_MESSAGE);
			connect.setEnabled(true);
			disconnect.setEnabled(false);
			savePlayer.setEnabled(false);
			return;
		}	
		
		// Checks if User wants to load Game from file
		if (JOptionPane.showConfirmDialog(this, "Would you like to load a previously saved game from a JSON file?", "Load Game",
		        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			// Checks if User wants to load Game from file
			Game loadedGame = JsonToGame.loadGame(this);
			String path = sl.loadPath();
			// if the load is null, then the user canceled the load operation, do do anything.
			if(loadedGame == null) {
				return;
			} else {
				gameServer.setGame(loadedGame);
			}
		} 
	}
	
	/**
	 * write message ToConsole
	 * @param message
	 */
	public void writeToConsole(String message) {
		console.append(message + "\n");
	}


	@Override
	public void windowActivated(WindowEvent e) {	
	}


	@Override
	public void windowClosed(WindowEvent e) {
	}


	@Override
	public void windowClosing(WindowEvent e) {
		if(gameServer != null) {
			gameServer.disconnect();
		}
		this.dispose();
	}


	@Override
	public void windowDeactivated(WindowEvent e) {	
	}


	@Override
	public void windowDeiconified(WindowEvent e) {
	}


	@Override
	public void windowIconified(WindowEvent e) {
	}


	@Override
	public void windowOpened(WindowEvent e) {
	}
}
