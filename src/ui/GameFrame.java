package ui;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.RepaintManager;

import Storage.GameToJson;
import Storage.InvalidSaveException;
import Storage.JsonToGame;
import network.GameClient;
import game.BoardSquare;
import game.Game;
import game.Location;
import game.Player;
import game.Room;
import game.avatar.Avatar;
import game.objects.GameObject;
import game.objects.Item;
import game.objects.Tree;

/**
 * @author Wang Zhen
 * @contributer Prashant Bhikhu
 */
@SuppressWarnings("serial")
public class GameFrame extends JFrame implements KeyListener, ActionListener {
	// The Emum has holds states for the JFrame so we know what to draw and when
	// for instance we draw
	private static enum FRAME_STATE {
		CREATED_FRAME, DO_NOTHING, GAME_CONNECTING, GAME_START, GAME_NORMAL, GAME_ENDED_EXPECTED, GAME_ENDED_UNEXPECTED
	};

	// The Size of the Frame
	private static final int FRAME_WIDTH = 1000;
	private static final int FRAME_HEIGHT = 600;

	// The size of the tiles when they are displayed
	private static final int TILE_WIDTH = 64;
	private static final int TILE_HEIGHT = 64;

	private FRAME_STATE frameState = FRAME_STATE.CREATED_FRAME;
	private GameClient gameClient;

	private Player clientPlayer;

	public JPanel panel;
	public JLabel touxiangLabel;
	public JLabel characterLabel;
	private int labelSize = 50;
	public JScrollPane itemX;
	public List<JLabel> infoLabels = new ArrayList<JLabel>();

	public GameFrame() {

		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		panel = new GamePanel();
		setContentPane(panel);

		panel.setOpaque(false);
		panel.setLayout(null);

		// Create Menu
		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");
		//gameMenu.setMnemonic(KeyEvent.VK_G);

		menuBar.add(gameMenu);

		JMenuItem createGame = new JMenuItem("Create Game (As Server)");
		JMenuItem joinGame = new JMenuItem("Join Game (As Client)");
		JMenuItem exit = new JMenuItem("Exit");

		gameMenu.add(createGame);
		gameMenu.add(joinGame);
		gameMenu.add(new JSeparator());
		gameMenu.add(exit);

		JMenu SaveLoadMenu = new JMenu("Save / Load");

		menuBar.add(SaveLoadMenu);

		JMenuItem savePlayer = new JMenuItem("Save Player Info");
		JMenuItem loadPlayer = new JMenuItem("Load Player Info");

		SaveLoadMenu.add(savePlayer);
		SaveLoadMenu.add(loadPlayer);

		createGame.addActionListener(this);
		joinGame.addActionListener(this);
		exit.addActionListener(this);

		savePlayer.addActionListener(this);
		loadPlayer.addActionListener(this);

		setJMenuBar(menuBar);
		setVisible(true);

		frameState = GameFrame.FRAME_STATE.CREATED_FRAME;

	}

	/*public ImageIcon getCharacterImage(Player player) {
		if (player.getDirection() == Player.Direction.FACE_LEFT) {
			return player.getAvatar().getFaceleft();
		} else if (player.getDirection() == Player.Direction.FACE_RIGHT) {
			return player.getAvatar().getFaceright();
		} else if (player.getDirection() == Player.Direction.BACK_LEFT) {
			return player.getAvatar().getBackleft();
		} else if (player.getDirection() == Player.Direction.BACK_RIGHT) {
			return player.getAvatar().getBackright();
		}
		return null;
	}*/

	public void printInformation(Player player) {
		//initialize
		if (touxiangLabel != null) {
			panel.remove(touxiangLabel);
		}
		//clear all info labels 1st
		for (JLabel jl : infoLabels) {
			panel.remove(jl);
		}
		//print normal image
		JLabel label = new JLabel(player.getAvatar().getName());
		int xPo = 10;
		int yPo = 10;
		int touxiangSize = 200;
		label.setBounds(xPo, yPo, touxiangSize, touxiangSize);
		touxiangLabel = label;
		panel.add(touxiangLabel);
		//print name
		JLabel textJLabel = new JLabel();
		textJLabel.setText("Name: " + player.getAvatar().getName());
		textJLabel.setLocation(10, 10);
		textJLabel.setSize(400, 20);
		textJLabel.setFont(new Font("Dialog", 1, 15));
		textJLabel.setHorizontalAlignment(JLabel.LEFT);
		panel.add(textJLabel);

		JLabel health = new JLabel();
		health.setText("Health: " + player.getHealth());
		health.setLocation(10, 30);
		health.setSize(400, 20);
		health.setFont(new Font("SanSerif", Font.PLAIN, 15));
		health.setHorizontalAlignment(JLabel.LEFT);
		panel.add(health);

		JLabel attack = new JLabel();
		attack.setText("Attack: " + player.getAttack() + "\n");
		attack.setLocation(10, 50);
		attack.setSize(400, 20);
		attack.setFont(new Font("SanSerif", Font.PLAIN, 15));
		attack.setHorizontalAlignment(JLabel.LEFT);
		panel.add(attack);

		
		itemX = new JScrollPane();
		itemX.setSize(150, 390);
		itemX.setLocation(20, 150);
		panel.add(itemX);
		//store labels into list and can remove them 1st when everytime refresh
		infoLabels.add(health);
		infoLabels.add(textJLabel);
		infoLabels.add(attack);
		
		repaint();
	}

	/*public void printCharacter(Player player) {
		//initialize
		if (characterLabel != null) {
			panel.remove(characterLabel);
		}

		//print
		/*JLabel chaL = null;
		if (player.getDirection().equals("fl")) {
			chaL = new JLabel(player.faceleft);
		} else if (player.getDirection().equals("fr")) {
			chaL = new JLabel(player.faceright);
		} else if (player.getDirection().equals("bl")) {
			chaL = new JLabel(player.backleft);
		} else if (player.getDirection().equals("br")) {
			chaL = new JLabel(player.backright);
		}
		int xPo = trasferX(player.location.col, player.location.row);
		int yPo = trasferY(player.location.col, player.location.row);
		int charaSize = 40;
		chaL.setBounds(xPo, yPo, charaSize, charaSize);
		characterLabel = chaL;
		panel.add(characterLabel);

		repaint();
	}*/

	/*public int trasferX(int col, int row) {
		int offset = 10;
		int edgeLong = 30;
		int base = 470;
		return (int) ((offset + edgeLong) * (col) - offset * row+ base);
	}

	public int trasferY(int col, int row) {
		int offset = 10;
		int edgeLong = 30;
		int base = 50;
		return (int) ((offset + edgeLong) * (row) + base);
	}*/

	class GamePanel extends JPanel {
		protected void paintComponent(Graphics g) {
			super.paintComponent(g); // Clears panel

			//System.out.println(true);

			if (frameState == FRAME_STATE.CREATED_FRAME/*gameClient == null || gameClient.getGame() == null*/) {
				g.drawImage(new ImageIcon(
						"./sprites/backgrounds/welcome_bg.jpg").getImage(), 0,
						0, FRAME_WIDTH, FRAME_HEIGHT, null);
				return;
			}

			// Draws background
			g.drawImage(new ImageIcon("./sprites/backgrounds/game_bg.jpg")
					.getImage(), 0, 0, FRAME_WIDTH, FRAME_HEIGHT, null);

			printInformation(clientPlayer);

			// Initial starting position of where the first square is going to be drawn
			int yPos = FRAME_HEIGHT / 2;
			int xPos = FRAME_WIDTH / 5;

			for (int cellY = 0; cellY < 10; cellY++) {
				for (int cellX = 9; cellX >= 0; cellX--) {
					int tileX = xPos + (cellX * TILE_WIDTH / 2);
					int tileY = yPos - (cellX * TILE_HEIGHT / 4);

					g.drawImage(new ImageIcon("./sprites/tiles/grass.png")
							.getImage(), tileX, tileY, TILE_WIDTH, TILE_HEIGHT,
							null);
					//g.drawString(cellY+", "+cellX, screenX+20, screenY+20); // Shows the Array dimension associated with the array
					//System.out.println(gameClient.getGame());
					//System.out.println(gameClient.getGame().players);
					//System.out.println(gameClient.getGame().players.size());
					//System.out.println(gameClient.getGame().players.get(clientPlayer));
					Location clientPlayerLoc = gameClient.getGame().players
							.get(clientPlayer);
					//System.out.println(clientPlayerLoc);

					if (clientPlayerLoc.getX() == cellX
							&& clientPlayerLoc.getY() == cellY) {
						//System.out.println(clientPlayer.getSpriteBasedOnDirection() != null);
						g.drawImage(clientPlayer.getSpriteBasedOnDirection()
								.getImage(), tileX + (TILE_WIDTH / 5), tileY
								- (TILE_HEIGHT / 3), null);
						//continue;
					}

					Game ga = gameClient.getGame();
					Room r = ga.rooms.get(0);
					BoardSquare[][] bs = r.board.getSquares();
					if (bs[cellY][cellX].getGameObjectOnSquare() != null) {
						if (bs[cellY][cellX].getGameObjectOnSquare() instanceof Tree) {
							g.drawImage(bs[cellY][cellX]
									.getGameObjectOnSquare().getSpriteImage()
									.getImage(), tileX, tileY - 200, null);
							//tree bounding box faulty check x and y
						} else {
							g.drawImage(bs[cellY][cellX]
									.getGameObjectOnSquare().getSpriteImage()
									.getImage(), tileX, tileY
									- (TILE_HEIGHT / 2), 50, 50, null);
						}
					}

					printInformation(clientPlayer);

					for (int i = 0; i < clientPlayer.getInventory().size(); i++) {
						g.drawImage(clientPlayer.getInventory().get(i)
								.getSpriteImage().getImage(), 40,
								400 + (i * 50), 40, 40, null);
					}
				}
				yPos += TILE_HEIGHT / 4;
				xPos += TILE_WIDTH / 2;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// don't fire an event on backspace or delete
		//fl faceleft fr faceright bl backleft br backright.
		Location loc = gameClient.getGame().players.get(clientPlayer);
		if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
			loc.moveNorth();
			clientPlayer.setDirection(Player.Direction.BACK_LEFT);
			//printCharacter(clientPlayer);
		} else if (e.getKeyCode() == KeyEvent.VK_S
				|| e.getKeyCode() == KeyEvent.VK_DOWN) {
			loc.moveSouth();
			clientPlayer.setDirection(Player.Direction.FACE_RIGHT);
			//printCharacter(clientPlayer);
		} else if (e.getKeyCode() == KeyEvent.VK_A
				|| e.getKeyCode() == KeyEvent.VK_LEFT) {
			loc.moveWest();
			clientPlayer.setDirection(Player.Direction.FACE_LEFT);
			//printCharacter(clientPlayer);
		} else if (e.getKeyCode() == KeyEvent.VK_D
				|| e.getKeyCode() == KeyEvent.VK_RIGHT) {
			loc.moveEast();
			clientPlayer.setDirection(Player.Direction.BACK_RIGHT);
			//printCharacter(clientPlayer);
		}

		if (loc.getY() < 0) {
			loc.moveSouth();
		} else if (loc.getY() == loc.getRoom().board.getHeight()) {
			loc.moveNorth();
		}

		if (loc.getX() < 0) {
			loc.moveEast();
		} else if (loc.getX() == loc.getRoom().board.getWidth()) {
			loc.moveWest();
		}

		//check to take items
		GameObject go = loc.getRoom().board.getSquares()[loc.getY()][loc.getX()]
				.getGameObjectOnSquare();
		if (go != null && go instanceof Item) {
			clientPlayer.addToInventory(((Item) go));
			loc.getRoom().board.getSquares()[loc.getY()][loc.getX()]
					.setGameObjectOnSquare(null);
		}

		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object source = arg0.getSource();
		if (source instanceof JMenuItem) {
			JMenuItem menuItem = (JMenuItem) source;

			if (menuItem.getText().equals("Create Game (As Server)")) {
				new ServerFrame();
				//this.dispose();
			} else if (menuItem.getText().equals("Join Game (As Client)")) {
				try {
					gameClient = new GameClient();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(this,
							"Game Client was unable to initalise.\n"
									+ " Make sure that you have created and\n"
									+ " connected and the server and the\n"
									+ "ports are unblocked.", "ERROR",
							JOptionPane.ERROR_MESSAGE);

					frameState = FRAME_STATE.CREATED_FRAME;
					return;
				}

				// At this point in code, client is connected to server successfully.

				// Now we need to let the user enter a username and pick a character
				String clientUsername = null;
				while (clientUsername == null) {
					clientUsername = JOptionPane.showInputDialog(this,
							"Input your Username?");
					if (clientUsername != null && clientUsername.length() < 3) {
						clientUsername = null;
					}

					if (clientUsername == null) {
						JOptionPane
								.showMessageDialog(
										this,
										"You need to enter a user name that is at least 3 characters long.",
										"ERROR", JOptionPane.ERROR_MESSAGE);
					}
				}

				Avatar clientAvatar = ChooseCharacterDialog.Chooser(this);

				// Created a player for the client
				clientPlayer = new Player(clientUsername);
				clientPlayer.setAvatar(clientAvatar);

				gameClient.joinServer(clientPlayer);

				// redraw the board
				frameState = FRAME_STATE.GAME_START;

				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				this.repaint();
				addKeyListener(this);
			} else if (menuItem.getText().equals("Exit")) {
				System.exit(0);
			} else if (menuItem.getText().equals("Save Player Info")) {
				if (frameState == FRAME_STATE.CREATED_FRAME) {
					JOptionPane.showMessageDialog(this,
							"Need to load game first", "ERROR",
							JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					try {
						clientPlayer.setLocation(gameClient.getGame().players
								.get(clientPlayer));
						gameClient.getGame().players.put(clientPlayer,
								clientPlayer.getLocation());
						GameToJson.savePlayer(clientPlayer);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvalidSaveException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else if (menuItem.getText().equals("Load Player Info")) {
				if (frameState == FRAME_STATE.CREATED_FRAME) {
					JOptionPane.showMessageDialog(this,
							"Need to load game first", "ERROR",
							JOptionPane.ERROR_MESSAGE);
					return;
				} else {
					Player newClientPlayer = JsonToGame.loadPlayer();
					if (newClientPlayer != null) {
						clientPlayer = newClientPlayer;
						clientPlayer.getLocation().setRoom(
								gameClient.getGame().rooms.get(0));
						//hard coded - removes key if player has it, places player in right spot
						if (clientPlayer.getInventory().contains(
								gameClient.getGame().rooms.get(0).board
										.getSquares()[3][4]))
							gameClient.getGame().rooms.get(0).board
									.getSquares()[3][4]
									.setGameObjectOnSquare(null);
						gameClient.getGame().players.clear();
						gameClient.getGame().players.put(clientPlayer,
								clientPlayer.getLocation());
						repaint();
					}
				}
			}
			JMenuItem savePlayer = new JMenuItem("Save Player Info");
			JMenuItem loadPlayer = new JMenuItem("Load Player Info");
		}

	}
}
