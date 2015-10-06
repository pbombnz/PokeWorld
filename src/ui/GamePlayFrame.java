package ui;

import java.awt.Color;
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
import javax.swing.JButton;
import javax.swing.JDialog;
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
import game.Board;
import game.BoardSquare;
import game.Game;
import game.Location;
import game.Player;
import game.Player.Direction;
import game.Room;
import game.avatar.Avatar;
import game.objects.GameObject;
import game.objects.Goblin;
import game.objects.GoodPotion;
import game.objects.Item;
import game.objects.Key;
import game.objects.Monster;
import game.objects.RareCandy;
import game.objects.Tree;
import game.objects.Weapon;

/**
 * @author Wang Zhen
 */
@SuppressWarnings("serial")
public class GamePlayFrame extends JFrame implements KeyListener, ActionListener {
	// The Emum has holds states for the JFrame so we know what to draw and when
	// for instance we draw
	private static enum FRAME_STATE {
		CREATED_FRAME, DO_NOTHING, GAME_CONNECTING, GAME_START, GAME_NORMAL, GAME_ENDED_EXPECTED, GAME_ENDED_UNEXPECTED
	};

	// The Size of the Frame
	private static final int FRAME_WIDTH = 1000;
	private static final int FRAME_HEIGHT = 600;
	private static final int FULL_FRAME_WIDTH = 1350;
	private static final int POP_UP_WIDTH = 790;
	private static final int POP_UP_HEIGHT = 75;
	// The size of the tiles when they are displayed
	private static final int TILE_WIDTH = 64;
	private static final int TILE_HEIGHT = 64;

	private FRAME_STATE frameState = FRAME_STATE.CREATED_FRAME;
	private GameClient gameClient;

	private Player clientPlayer;

	public JPanel panel;
	public JLabel characterLabel;
	private int labelSize = 50;
	private JDialog fightBox;
	private int roomIndex = 0;
	public int printPlayerOffset = 0;

	public List<JLabel> infoLabels = new ArrayList<JLabel>();
	public JLabel headPictureLabel = null;
	public JLabel bgHeadViewLabel = null;

	public GamePlayFrame() {

		setSize(FULL_FRAME_WIDTH, FRAME_HEIGHT);
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

		frameState = GamePlayFrame.FRAME_STATE.CREATED_FRAME;

	}

	public void createHeadViewLabels() {
		//print head picture
		JLabel bgHeadViewLabel = new JLabel(new ImageIcon("src/bgHeadView.png"));
		JLabel headPictureLabel = new JLabel(new ImageIcon("src/newtu.gif"));
		int xPo = 10;
		int yPo = 0;
		int touxiangSize = 130;
		bgHeadViewLabel.setBounds(xPo, yPo, touxiangSize, touxiangSize);
		headPictureLabel.setBounds(xPo, yPo, touxiangSize, touxiangSize);
		this.bgHeadViewLabel = bgHeadViewLabel;
		this.headPictureLabel = headPictureLabel;
	}

	public void printInformation(Player player) {
		//initialize
		//clear all info labels 1st
		for (JLabel jl : infoLabels) {
			panel.remove(jl);
		}
		//print player name
		JLabel playerNameTextLabel = new JLabel();
		playerNameTextLabel.setText("Player Name: "+player.getName());
		playerNameTextLabel.setLocation(10, 130);
		playerNameTextLabel.setSize(400, 20);
		playerNameTextLabel.setFont(new Font("Dialog", 1, 20));
		playerNameTextLabel.setHorizontalAlignment(JLabel.LEFT);
		panel.add(playerNameTextLabel);
		//print character type
		JLabel textJLabel = new JLabel();
		textJLabel.setText("Character Name: " + player.getAvatar().getName());
		textJLabel.setLocation(10, 160);
		textJLabel.setSize(400, 20);
		textJLabel.setFont(new Font("Dialog", 1, 15));
		textJLabel.setHorizontalAlignment(JLabel.LEFT);
		panel.add(textJLabel);

		JLabel health = new JLabel();
		health.setText("Health: " + player.getHealth());
		health.setLocation(10, 180);
		health.setSize(400, 20);
		health.setFont(new Font("SanSerif", Font.PLAIN, 15));
		health.setHorizontalAlignment(JLabel.LEFT);
		panel.add(health);

		JLabel attack = new JLabel();
		attack.setText("Attack: " + player.getAttack() + "\n");
		attack.setLocation(10, 200);
		attack.setSize(400, 20);
		attack.setFont(new Font("SanSerif", Font.PLAIN, 15));
		attack.setHorizontalAlignment(JLabel.LEFT);
		panel.add(attack);

		JLabel level = new JLabel();
		level.setText("Level: " + player.getPlayerLevel() + "\n");
		level.setLocation(10, 220);
		level.setSize(400, 20);
		level.setFont(new Font("SanSerif", Font.PLAIN, 15));
		level.setHorizontalAlignment(JLabel.LEFT);
		panel.add(level);

		//store labels into list and can remove them 1st when everytime refresh
		infoLabels.add(playerNameTextLabel);
		infoLabels.add(textJLabel);
		infoLabels.add(health);
		infoLabels.add(attack);
		infoLabels.add(level);

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

			if (frameState == FRAME_STATE.CREATED_FRAME/*gameClient == null || gameClient.getGame() == null*/) {
				g.drawImage(new ImageIcon(
						"./sprites/backgrounds/welcome_bg.jpg").getImage(), 0,
						0, FRAME_WIDTH, FRAME_HEIGHT, null);
				return;
			}

			// Draw background picture
			g.drawImage(new ImageIcon("./sprites/backgrounds/game_bg.jpg")
					.getImage(), 0, 0, FRAME_WIDTH, FRAME_HEIGHT, null);

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

					Location clientPlayerLoc = clientPlayer.getLocation();

					// HARDED CODED. Change later. Gets clientPlayer location
					if (clientPlayerLoc == null) {
						clientPlayerLoc = gameClient.getGame().players2.get(0)
								.getLocation();
						clientPlayer.setLocation(clientPlayerLoc);
					}

					//print player
					if (clientPlayerLoc.getX() == cellX
							&& clientPlayerLoc.getY() == cellY) {
						g.drawImage(clientPlayer.getSpriteBasedOnDirection()
								.getImage(), tileX + (TILE_WIDTH / 5), tileY
								- (TILE_HEIGHT / 3) + printPlayerOffset, null);
					}

					//print object of game
					Game ga = gameClient.getGame();
					Room r = ga.rooms.get(0);
					BoardSquare[][] bs = r.board.getSquares();
					if (bs[cellY][cellX].getGameObjectOnSquare() != null) {
						if (bs[cellY][cellX].getGameObjectOnSquare() instanceof Tree) {
							g.drawImage(bs[cellY][cellX]
									.getGameObjectOnSquare().getSpriteImage()
									.getImage(), tileX - 60, tileY - 170, null);
							//tree bounding box faulty check x and y
						} else {
							g.drawImage(bs[cellY][cellX]
									.getGameObjectOnSquare().getSpriteImage()
									.getImage(), tileX, tileY
									- (TILE_HEIGHT / 2), 50, 50, null);
						}
					}

				}
				yPos += TILE_HEIGHT / 4;
				xPos += TILE_WIDTH / 2;
			}

			//printInformation of player
			printInformation(clientPlayer);

			//print players'inventory
			for (int i = 0; i < clientPlayer.getInventory().size(); i++) {
				g.drawImage(clientPlayer.getInventory().get(i).getSpriteImage()
						.getImage(), 40, 400 + (i * 50), 40, 40, null);
			}

			//printcompass
			g.drawImage(
					new ImageIcon("./sprites/other/compass.png").getImage(),
					800, 355, 200, 200, null);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// don't fire an event on backspace or delete
		//fl faceleft fr faceright bl backleft br backright.
		Location loc = clientPlayer.getLocation();
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
		} else if (e.getKeyCode() == KeyEvent.VK_E) {
			//turn the gui to left side
			//change the board(change the locations of object)
			Board newBoard = new Board();
			Board oldBoard = gameClient.getGame().rooms.get(roomIndex).board;
			for (int i = 0; i < oldBoard.getWidth(); i++) {
				for (int j = 0; j < oldBoard.getHeight(); j++) {
					int offset = 1;//because the start position is (0,0) not(1,1), so there is a offset
					newBoard.squares[i][j] = oldBoard.squares[gameClient
							.getGame().rooms.get(roomIndex).board.getHeight()
							- (j + offset)][i];
				}
			}
			gameClient.getGame().rooms.get(roomIndex).board = newBoard;

			//change the locations of player 
			Location newloc = new Location();
			newloc.setRoom(clientPlayer.getLocation().getRoom());
			int offset = 1;//because the start position is (0,0) not(1,1), so there is a offset
			newloc.setX(gameClient.getGame().rooms.get(roomIndex).board
					.getHeight() - (clientPlayer.getLocation().getY() + offset));
			newloc.setY(clientPlayer.getLocation().getX());
			clientPlayer.setLocation(newloc);
			//let the player image turn left 
			turnPlayerImageLeft(clientPlayer);

		} else if (e.getKeyCode() == KeyEvent.VK_Q) {
			//turn the gui to right side
			//change the board(change the locations of object)
			Board newBoard = new Board();
			Board oldBoard = gameClient.getGame().rooms.get(roomIndex).board;
			for (int i = 0; i < oldBoard.getWidth(); i++) {
				for (int j = 0; j < oldBoard.getHeight(); j++) {
					int offset = 1;//because the start position is (0,0) not(1,1), so there is a offset
					newBoard.squares[i][j] = oldBoard.squares[j][gameClient
							.getGame().rooms.get(roomIndex).board.getWidth()
							- (i + offset)];
				}
			}
			gameClient.getGame().rooms.get(roomIndex).board = newBoard;

			//change the locations of player 
			Location newloc = new Location();
			newloc.setRoom(clientPlayer.getLocation().getRoom());
			int offset = 1;//because the start position is (0,0) not(1,1), so there is a offset
			newloc.setX(clientPlayer.getLocation().getY());
			newloc.setY(gameClient.getGame().rooms.get(roomIndex).board
					.getWidth() - (clientPlayer.getLocation().getX() + offset));
			clientPlayer.setLocation(newloc);
			//let the player image turn left 
			turnPlayerImageRight(clientPlayer);

		}
		//jump
		else if (e.getKeyCode() == KeyEvent.VK_J) {
			printPlayerOffset += 20;

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

		if (go instanceof Key) {
			clientPlayer.addToInventory(((Key) go));
			loc.getRoom().board.getSquares()[loc.getY()][loc.getX()]
					.setGameObjectOnSquare(null);
		}
		if (go instanceof GoodPotion) {
			System.out.println("is this working");
			clientPlayer.setHealth(clientPlayer.getHealth()
					+ ((GoodPotion) go).getHealthHealAmount());
			loc.getRoom().board.getSquares()[loc.getY()][loc.getX()]
					.setGameObjectOnSquare(null);
		}

		if (go instanceof Monster) {
			fightDialog();

		}
		if (go instanceof RareCandy) {
			clientPlayer.setPlayerLevel(clientPlayer.getPlayerLevel()
					+ ((RareCandy) go).level());
			clientPlayer.setAttack(clientPlayer.getAttack()
					* clientPlayer.getPlayerLevel());
			loc.getRoom().board.getSquares()[loc.getY()][loc.getX()]
					.setGameObjectOnSquare(null);
		}
		repaint();
	}

	/**
	 * let the player image turn left
	 * @param player
	 */
	public void turnPlayerImageLeft(Player player) {
		if (player.getDirection().equals(Direction.FACE_LEFT)) {
			player.setDirection(Direction.BACK_LEFT);
		} else if (player.getDirection().equals(Direction.BACK_LEFT)) {
			player.setDirection(Direction.BACK_RIGHT);
		} else if (player.getDirection().equals(Direction.BACK_RIGHT)) {
			player.setDirection(Direction.FACE_RIGHT);
		} else if (player.getDirection().equals(Direction.FACE_RIGHT)) {
			player.setDirection(Direction.FACE_LEFT);
		}
	}

	/**
	 * let the player image turn right
	 * @param player
	 */
	public void turnPlayerImageRight(Player player) {
		if (player.getDirection() == Direction.FACE_LEFT) {
			player.setDirection(Direction.FACE_RIGHT);
		} else if (player.getDirection() == Direction.FACE_RIGHT) {
			player.setDirection(Direction.BACK_RIGHT);
		} else if (player.getDirection() == Direction.BACK_RIGHT) {
			player.setDirection(Direction.BACK_LEFT);
		} else if (player.getDirection() == Direction.BACK_LEFT) {
			player.setDirection(Direction.FACE_LEFT);
		}
	}

	public void fightDialog() {

		final Location loc = clientPlayer.getLocation();

		GameObject go = loc.getRoom().board.getSquares()[loc.getY()][loc.getX()]
				.getGameObjectOnSquare();

		JButton att = new JButton("Attack");
		JButton run = new JButton("Run Away");

		att.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fight();
			}
		});
		run.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fightBox.dispose();
			}
		});

		att.setLocation(10, 70);
		att.setSize(att.getPreferredSize());
		run.setLocation(90, 70);
		run.setSize(run.getPreferredSize());

		JLabel type = new JLabel("Monster Type: " + ((Monster) go).getName());
		JLabel attack = new JLabel("Monster Attack: " + ((Monster) go).attack());
		JLabel health = new JLabel("Monster Health: "
				+ ((Monster) go).getHealth());

		type.setLocation(10, 10);
		attack.setLocation(10, 30);
		health.setLocation(10, 50);

		type.setSize(type.getPreferredSize());
		attack.setSize(type.getPreferredSize());
		health.setSize(type.getPreferredSize());

		fightBox = new JDialog();
		fightBox.setTitle("An enemy!");
		fightBox.setBackground(Color.GRAY);
		fightBox.setLayout(null);
		fightBox.setSize(200, 120);
		fightBox.setLocation(POP_UP_WIDTH, POP_UP_HEIGHT);
		fightBox.add(type);
		fightBox.add(attack);
		fightBox.add(health);
		fightBox.add(att);
		fightBox.add(run);
		fightBox.setVisible(true);

	}

	private void fight() {

		final Location loc = clientPlayer.getLocation();

		GameObject go = loc.getRoom().board.getSquares()[loc.getY()][loc.getX()]
				.getGameObjectOnSquare();

		final int damage = ((Monster) go).attack();

		clientPlayer.setHealth(clientPlayer.getHealth() - damage);
		((Monster) go).setHealth(((Monster) go).getHealth()
				- clientPlayer.getAttack());

		fightBox.dispose();

		if (clientPlayer.isDead()) {
			JOptionPane.showMessageDialog(null, " You Died ");
			//System.exit(0);
		}

		else if (((Monster) go).isDead()) {

			JOptionPane.showMessageDialog(null, " You Won! \n" + " You lost "
					+ damage + " health \n" + " You gained " + damage
					+ " attack");

			clientPlayer.setAttack(clientPlayer.getAttack() + damage);

			loc.getRoom().board.getSquares()[loc.getY()][loc.getX()]
					.setGameObjectOnSquare(null);

		}

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_J) {
			printPlayerOffset -= 20;
		}
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
				//add all jlabel after picking character
				createHeadViewLabels();
				panel.add(headPictureLabel);
				panel.add(bgHeadViewLabel);

				try {
					Thread.sleep(1000);
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
						GameToJson.savePlayer(clientPlayer);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InvalidSaveException e) {
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
						gameClient.getGame().players2.clear();
						gameClient.getGame().players2.add(clientPlayer);
						repaint();
					}
				}
			}
			JMenuItem savePlayer = new JMenuItem("Save Player Info");
			JMenuItem loadPlayer = new JMenuItem("Load Player Info");
		}

	}
}
