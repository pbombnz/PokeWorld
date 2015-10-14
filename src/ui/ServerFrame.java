package ui;

import game.Game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
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
 * This class is the 'view' of the server. It will show various information of the server
 * such as incoming and outgoing packets, who's connected etc. It also allows us to manage
 * the server (via server on-off toggle) and allows us to load or save the game.
 * 
 * @author Prashant Bhikhu
 */
@SuppressWarnings("serial")
public class ServerFrame extends JFrame implements ActionListener, WindowListener {
	// The Size of the Frame
	private static final int FRAME_WIDTH = 600;
	private static final int FRAME_HEIGHT = 300;
	
	// The Menu and Menu Items attached to the Server Frame 
	private JMenuBar menuBar = new JMenuBar();
	
	JMenu gameMenu = new JMenu("Server");
	JMenuItem connect = new JMenuItem("Connect");
	JMenuItem disconnect = new JMenuItem("Disconnect");
	JMenuItem exit = new JMenuItem("Exit");
	
	JMenu SaveLoadMenu = new JMenu("Save");
	JMenuItem saveGame = new JMenuItem("Save Game World");
	
	JMenu clearServerLogMenu = new JMenu("Clear Server Log...");
	JMenuItem clearServerLog = new JMenuItem("Clear");
	
	private JTextArea console = new JTextArea();
	private JScrollPane consoleScrollPane;
	
	private GameServer gameServer;
	
	// For XML Saving (although never used)
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

		SaveLoadMenu.add(saveGame);
		
		menuBar.add(clearServerLogMenu);
		
		clearServerLogMenu.add(clearServerLog);
				
		// Add Listeners to all Menu Items
		connect.addActionListener(this);
		disconnect.addActionListener(this);
		exit.addActionListener(this);
		saveGame.addActionListener(this);
		clearServerLog.addActionListener(this);
		
		// Sets the Console properties so that it cannot be edited (only for display), and auto scrolls
		console.setEditable(false);
		((DefaultCaret) console.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		// Wrap the console in a scroll pane to enable scrolling
		consoleScrollPane = new JScrollPane(console);

		// Add the menu and console scroll pane to the frame
		setJMenuBar(menuBar);
		add(consoleScrollPane);
		// Finally show frame
		setVisible(true);
		
		// Finally start the server on frame load
		connect();
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		/* The Frame only listens for JMenuItems, hence why we can make the assumption
		 * that every source is going to be a MenuItem. No additional pre-condition if 
		 * statement required. 
		 */
		JMenuItem menuItem = (JMenuItem) arg0.getSource();

		// Determine what menu item was pressed
		if(menuItem == connect) {
			connect();
		} else if(menuItem == disconnect) {
			// reset the menu item enabled variables based on the change in server state.
			connect.setEnabled(true);
			disconnect.setEnabled(false);
			saveGame.setEnabled(false);
			// Disconnects the server
			gameServer.disconnect();
			gameServer = null;
		} else if(menuItem == exit) {
			// Closes window and safely disconnects server
			windowClosing(null);
		} else if (menuItem == saveGame) {
			// Save the current state of the game
			GameToJson.saveGame(this, gameServer.getGame());
			/*sl = new SaverLoader();
			try {
				String path = sl.savePath();
				sl.saveGame(path);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}*/
		} else if (menuItem == clearServerLog) {
			// Clear the server log (if its gets too spammy)
			console.setText("");
		}
	}
	
	/**
	 * Attempts to start a server connection
	 */
	public void connect() {
		try {
			gameServer = new GameServer(this);
			// reset the menu item enabled variables based on the change in server state.
			connect.setEnabled(false);
			disconnect.setEnabled(true);
			saveGame.setEnabled(true);
		} catch (IOException e) {
			// Cannot start the server (likely problem is that there is a server already running on the port)
			
			// Show an error to the user so they know the server is having issues
			JOptionPane.showInternalMessageDialog(this, "Cannot connect", "Server Error", JOptionPane.ERROR_MESSAGE);
			// reset the menu item enabled variables based on the change in server state.
			connect.setEnabled(true);
			disconnect.setEnabled(false);
			saveGame.setEnabled(false);
			return;
		}	
		
		// Checks if User wants to load Game from file
		if (JOptionPane.showConfirmDialog(this, "Would you like to load a previously saved game from a JSON file?", "Load Game",
		        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			// Checks if User wants to load Game from file
			Game loadedGame = JsonToGame.loadGame(this);
			//String path = sl.loadPath();
			// if game loaded is null, then the user canceled the load operation, do do anything otherwise
			// if the game loaded is valid, set it as the global server game.
			if(loadedGame == null) {
				return;
			} else {
				gameServer.setGame(loadedGame);
			}
		} 
	}
	
	/**
	 * Writes (or appends) a message to the in-frame console. (NOT the Java Console)
	 * @param message the message that is going to be displayed in console
	 */
	public void writeToConsole(String message) {
		console.append(message + "\n");
	}


	@Override
	public void windowClosing(WindowEvent e) {
		if(gameServer != null) {
			gameServer.disconnect();
		}
		this.dispose();
	}

	// UNUSED METHODS

	@Override
	public void windowActivated(WindowEvent e) {	
	}


	@Override
	public void windowClosed(WindowEvent e) {
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
