package ui;

import game.Game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.management.RuntimeErrorException;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;

import network.GameServer;

@SuppressWarnings("serial")
public class ServerFrame extends JFrame implements ActionListener {
	// The Size of the Frame
	private static final int FRAME_WIDTH = 600;
	private static final int FRAME_HEIGHT = 300;
	
	private JTextArea console;
	private JScrollPane consoleScrollPane;
	
	private GameServer gameServer;
	
	public ServerFrame() {
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		// Create Menu
		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = new JMenu("Server");
		
		menuBar.add(gameMenu);

		JMenuItem connect = new JMenuItem("Connect");
		JMenuItem disconnect = new JMenuItem("Disconnect");
		JMenuItem exit = new JMenuItem("Exit");

		gameMenu.add(connect);
		gameMenu.add(disconnect);
		gameMenu.add(new JSeparator());
		gameMenu.add(exit);
				
		connect.addActionListener(this);
		disconnect.addActionListener(this);
		exit.addActionListener(this);
		
		// Create Text Area for Console
		console = new JTextArea();
		console.setEditable(false);


		setJMenuBar(menuBar);
		
		JScrollPane consoleScrollPane = new JScrollPane(console);

		add(consoleScrollPane);
		setVisible(true);
		
		try {
			gameServer = new GameServer(this);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		JMenuItem menuItem = (JMenuItem) arg0.getSource();
		
		if(menuItem.getText().equals("Connect")) {
			// Load Game Map 1 for now (NO USER SELECTION)
			try {
				gameServer = new GameServer(this);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else if(menuItem.getText().equals("Disconnect")) {
			System.exit(0);
		} else if(menuItem.getText().equals("Exit")) {
			System.exit(0);
		}
	}
	
	public void writeToConsole(String message) {
		console.append(message + "\n");
	}
}
