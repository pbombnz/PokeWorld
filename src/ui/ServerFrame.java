package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import network.GameServer;

@SuppressWarnings("serial")
public class ServerFrame extends JFrame implements ActionListener {
	// The Size of the Frame
	private static final int FRAME_WIDTH = 600;
	private static final int FRAME_HEIGHT = 300;
	
	private JMenuBar menuBar = new JMenuBar();
	JMenu gameMenu = new JMenu("Server");
	JMenuItem connect = new JMenuItem("Connect");
	JMenuItem disconnect = new JMenuItem("Disconnect");
	JMenuItem exit = new JMenuItem("Exit");
	
	private JTextArea console = new JTextArea();
	private JScrollPane consoleScrollPane;
	
	private GameServer gameServer;
	
	public ServerFrame() {
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		// Create Menu
		menuBar.add(gameMenu);

		gameMenu.add(connect);
		gameMenu.add(disconnect);
		gameMenu.add(new JSeparator());
		gameMenu.add(exit);
				
		connect.addActionListener(this);
		disconnect.addActionListener(this);
		exit.addActionListener(this);
		
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
		
		try {
			gameServer = new GameServer(this);
			connect.setEnabled(false);
			disconnect.setEnabled(true);
		} catch (IOException e) {
			JOptionPane.showInternalMessageDialog(this, "Cannot connect", "Server Error", JOptionPane.ERROR_MESSAGE);
		}
		
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		JMenuItem menuItem = (JMenuItem) arg0.getSource();
		
		if(menuItem.getText().equals("Connect")) {
			// Load Game Map 1 for now (NO USER SELECTION)
			try {
				gameServer = new GameServer(this);
				connect.setEnabled(false);
				disconnect.setEnabled(true);
			} catch (IOException e) {
				JOptionPane.showInternalMessageDialog(this, e.getMessage(), "Server Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if(menuItem.getText().equals("Disconnect")) {
			connect.setEnabled(true);
			disconnect.setEnabled(false);
			gameServer.disconnect();
		} else if(menuItem.getText().equals("Exit")) {
			gameServer.disconnect();
			System.exit(0);
		}
	}
	
	public void writeToConsole(String message) {
		console.append(message + "\n");
	}
}
