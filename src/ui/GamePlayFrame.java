package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
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
import javax.swing.JSeparator;

import rooms.Board;
import rooms.Board1;
import rooms.Board2;
import rooms.Board3;
import rooms.EmptyBoard;
import rooms.Room;
import rooms.Room1;
import network.GameClient;
import game.BoardSquare;
import game.Game;
import game.Location;
import game.Player;
import game.Player.Direction;
import game.avatar.Avatar;
import game.objects.Door;
import game.objects.GameObject;
import game.objects.GoodPotion;
import game.objects.Item;
import game.objects.Key;
import game.objects.Monster;
import game.objects.RareCandy;
import game.objects.Tree;

/**
 * @author Wang Zhen
 */
@SuppressWarnings("serial")
public class GamePlayFrame extends JFrame implements KeyListener,
		ActionListener {
	// The Emum has holds states for the JFrame so we know what to draw and when
	// for instance we draw
	private static enum FRAME_STATE {
		CREATED_FRAME, DO_NOTHING, GAME_CONNECTING, GAME_START, GAME_NORMAL, GAME_ENDED_EXPECTED, GAME_ENDED_UNEXPECTED
	};

	// The Size of the Frame
	private static final int FRAME_WIDTH = 1000;
	private static final int FRAME_HEIGHT = 600;
	private static final int FULL_FRAME_WIDTH = 1350;
	// The size of the tiles when they are displayed
	private static final int TILE_WIDTH = 64;
	private static final int TILE_HEIGHT = 64;

	private FRAME_STATE frameState = FRAME_STATE.CREATED_FRAME;

	private GameClient gameClient;
	private Player clientPlayer;

	public int jumpOffset = 0;
	public int shakeOffset = 0;//the player will keep shake when they are standing in one place
	public int shakeTimer = 0;//using calculation number as timer. the shakeoffset will change when the timer reaches the timerLimit
	public final int SHAKE_TIMER_LIMIT = 40;// the shakeoffset will change when it reaches the timer limit

	public List<JLabel> infoLabels = new ArrayList<JLabel>();

	public JLabel headPictureLabel = null;
	public JLabel bgHeadViewLabel = null;
	public JLabel dieLabel = null;
	public JLabel attackLabel = null;
	public JPanel panel;
	public JLabel characterLabel;
	private JDialog fightBox;

	public GamePlayFrame() {

		//initialises game frame
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

		createGame.addActionListener(this);
		joinGame.addActionListener(this);
		exit.addActionListener(this);

		setJMenuBar(menuBar);
		setVisible(true);

		frameState = GamePlayFrame.FRAME_STATE.CREATED_FRAME;

	}

	public void loadLabels() {
		//load labels of player information
		//create background label
		JLabel bgHeadViewLabel = new JLabel(new ImageIcon("src/bgHeadView.png"));
		//load head picture
		JLabel headPictureLabel = null;
		if (clientPlayer.getAvatar().getAvatarName().equals("Bulbasaur")) {
			headPictureLabel = new JLabel(new ImageIcon("src/Bulbasaur.gif"));
		} else if (clientPlayer.getAvatar().getAvatarName()
				.equals("Charmander")) {
			headPictureLabel = new JLabel(new ImageIcon("src/Charmander.gif"));
		} else if (clientPlayer.getAvatar().getAvatarName().equals("Squirtle")) {
			headPictureLabel = new JLabel(new ImageIcon("src/Squirtle.gif"));
		}
		int xPo = 10;
		int yPo = 10;
		int touxiangSize = 130;
		bgHeadViewLabel.setBounds(xPo, yPo, touxiangSize, touxiangSize);
		headPictureLabel.setBounds(xPo, yPo, touxiangSize, touxiangSize);
		headPictureLabel.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5,
				Color.BLACK));
		this.bgHeadViewLabel = bgHeadViewLabel;
		this.headPictureLabel = headPictureLabel;

		//load labels of die and attack
		JLabel dieLabel = null;
		JLabel attackLabel = null;

		//print head picture
		if (clientPlayer.getAvatar().getAvatarName().equals("Bulbasaur")) {
			dieLabel = new JLabel(new ImageIcon("src/Bulbasaur_die.gif"));
			attackLabel = new JLabel(new ImageIcon("src/Bulbasaur_attack.gif"));
		} else if (clientPlayer.getAvatar().getAvatarName()
				.equals("Charmander")) {
			dieLabel = new JLabel(new ImageIcon("src/Charmander_die.gif"));
			attackLabel = new JLabel(new ImageIcon("src/Charmander_attack.gif"));
		} else if (clientPlayer.getAvatar().getAvatarName().equals("Squirtle")) {
			dieLabel = new JLabel(new ImageIcon("src/Squirtle_die.gif"));
			attackLabel = new JLabel(new ImageIcon("src/Squirtle_attack.gif"));
		}

		int diexPo = 250;
		int dieyPo = 0;
		int dieOrAttackLabelSize = 300;

		dieLabel.setBounds(diexPo, dieyPo, dieOrAttackLabelSize * 2,
				dieOrAttackLabelSize);
		attackLabel.setBounds(diexPo, dieyPo, dieOrAttackLabelSize * 2,
				dieOrAttackLabelSize);

		this.dieLabel = dieLabel;
		this.attackLabel = attackLabel;
	}

	public void printInformation(Player player) {

		//clear all info labels first
		for (JLabel jl : infoLabels) {
			panel.remove(jl);
		}
		//print player name
		JLabel playerNameTextLabel = new JLabel();
		playerNameTextLabel.setText("Player Name: " + player.getName());
		playerNameTextLabel.setLocation(10, 140);
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
		//print character health
		JLabel health = new JLabel();
		health.setText("Health: " + player.getHealth());
		health.setLocation(10, 180);
		health.setSize(400, 20);
		health.setFont(new Font("SanSerif", Font.PLAIN, 15));
		health.setHorizontalAlignment(JLabel.LEFT);
		panel.add(health);
		//print character attack
		JLabel attack = new JLabel();
		attack.setText("Attack: " + player.getAttack() + "\n");
		attack.setLocation(10, 200);
		attack.setSize(400, 20);
		attack.setFont(new Font("SanSerif", Font.PLAIN, 15));
		attack.setHorizontalAlignment(JLabel.LEFT);
		panel.add(attack);
		//print character level
		JLabel level = new JLabel();
		level.setText("Level: " + player.getPlayerLevel() + "\n");
		level.setLocation(10, 220);
		level.setSize(400, 20);
		level.setFont(new Font("SanSerif", Font.PLAIN, 15));
		level.setHorizontalAlignment(JLabel.LEFT);
		panel.add(level);

		//store labels into a list and remove them first everytime you refresh
		infoLabels.add(playerNameTextLabel);
		infoLabels.add(textJLabel);
		infoLabels.add(health);
		infoLabels.add(attack);
		infoLabels.add(level);

		repaint();
	}

	private void changeShakeLimit() {
		if (shakeOffset == 0) {
			shakeOffset = 5;
		} else {
			shakeOffset = 0;
		}
	}

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

					//print player
					if (clientPlayerLoc.getX() == cellX
							&& clientPlayerLoc.getY() == cellY) {
						if (shakeTimer >= SHAKE_TIMER_LIMIT) {
							shakeTimer = 0;
							changeShakeLimit();
							g.drawImage(clientPlayer
									.getSpriteBasedOnDirection().getImage(),
									tileX + (TILE_WIDTH / 5), tileY
											- (TILE_HEIGHT / 3) + jumpOffset
											+ shakeOffset, null);
						} else {
							shakeTimer++;
							g.drawImage(clientPlayer
									.getSpriteBasedOnDirection().getImage(),
									tileX + (TILE_WIDTH / 5), tileY
											- (TILE_HEIGHT / 3) + jumpOffset
											+ shakeOffset, null);
						}
					}

					for (Player connectedPlayer : gameClient.getGame()
							.getPlayers()) {
						/*if(connectedPlayer.getSpriteBasedOnDirection().getImageLoadStatus() == MediaTracker.ABORTED) {
							System.out.println("PlayerID: "+connectedPlayer.getId()+"  Load Status: ABORTED");
						} else if(connectedPlayer.getSpriteBasedOnDirection().getImageLoadStatus() == MediaTracker.COMPLETE) {
							System.out.println("PlayerID: "+connectedPlayer.getId()+"  Load Status: COMPLETED");
						}
						else if(connectedPlayer.getSpriteBasedOnDirection().getImageLoadStatus() == MediaTracker.ERRORED)
						{
							System.out.println("PlayerID: "+connectedPlayer.getId()+"  Load Status: ERRORED");
						}
						else if(connectedPlayer.getSpriteBasedOnDirection().getImageLoadStatus() == MediaTracker.LOADING)
						{
							System.out.println("PlayerID: "+connectedPlayer.getId()+"  Load Status: LOADING");
						}else {
							System.out.println("PlayerID: "+connectedPlayer.getId()+"  Load Status: "+connectedPlayer.getSpriteBasedOnDirection().getImageLoadStatus());							
						}*/

						//						System.out.println("PlayerID: "
						//								+ connectedPlayer.getId()
						//								+ "  Image: "
						//								+ connectedPlayer.getSpriteBasedOnDirection()
						//										.getImage());

						if (connectedPlayer != clientPlayer) {
							if (connectedPlayer.getLocation().getX() == cellX
									&& connectedPlayer.getLocation().getY() == cellY) {
								g.drawImage(
										connectedPlayer
												.getSpriteBasedOnDirection()
												.getImage(), tileX
												+ (TILE_WIDTH / 5), tileY
												- (TILE_HEIGHT / 3), null);
							}
						}
					}
					//System.out.println(clientPlayer
					//		.getSpriteBasedOnDirection().getImage());

					//Location clientPlayerLoc = clientPlayer.getLocation();	
					//print player
					//					System.out.println("PlayerID: "
					//							+ clientPlayer.getId()
					//							+ "  Image: "
					//							+ clientPlayer.getSpriteBasedOnDirection()
					//									.getImage());

					if (clientPlayerLoc.getX() == cellX
							&& clientPlayerLoc.getY() == cellY) {
						if (shakeTimer >= SHAKE_TIMER_LIMIT) {
							shakeTimer = 0;
							changeShakeLimit();
							g.drawImage(clientPlayer
									.getSpriteBasedOnDirection().getImage(),
									tileX + (TILE_WIDTH / 5), tileY
											- (TILE_HEIGHT / 3) + jumpOffset
											+ shakeOffset, null);
						} else {
							shakeTimer++;
							g.drawImage(clientPlayer
									.getSpriteBasedOnDirection().getImage(),
									tileX + (TILE_WIDTH / 5), tileY
											- (TILE_HEIGHT / 3) + jumpOffset
											+ shakeOffset, null);
						}
					}

					//print object of game
					Game ga = gameClient.getGame();
					Room r = ga.rooms.get(GameLauncher.ROOMINDEX);
					BoardSquare[][] bs = r.board.getSquares();

					if (bs[cellY][cellX].getGameObjectOnSquare() != null) {
						if (bs[cellY][cellX].getGameObjectOnSquare() instanceof Tree) {
							g.drawImage(bs[cellY][cellX]
									.getGameObjectOnSquare().getSpriteImage()
									.getImage(), tileX - 60, tileY - 170, null);
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

			//print compass
			g.drawImage(
					new ImageIcon("./sprites/other/compass.png").getImage(),
					800, 355, 200, 200, null);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// don't fire an event on backspace or delete
		Location loc = clientPlayer.getLocation();
		if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
			//character will turn 1st if the character is not facing that side
			if (clientPlayer.getDirection() == Direction.BACK_LEFT) {
				loc.moveNorth();
			} else {
				clientPlayer.setDirection(Player.Direction.BACK_LEFT);
				loc.moveNorth();
			}
		} else if (e.getKeyCode() == KeyEvent.VK_S
				|| e.getKeyCode() == KeyEvent.VK_DOWN) {
			//character will turn 1st if the character is not facing that side
			if (clientPlayer.getDirection() == Direction.FACE_RIGHT) {
				loc.moveSouth();//oo
			} else {
				clientPlayer.setDirection(Player.Direction.FACE_RIGHT);
				loc.moveSouth();//oo
			}
		} else if (e.getKeyCode() == KeyEvent.VK_A
				|| e.getKeyCode() == KeyEvent.VK_LEFT) {
			//character will turn 1st if the character is not facing that side
			if (clientPlayer.getDirection() == Direction.FACE_LEFT) {
				loc.moveWest();
			} else {
				clientPlayer.setDirection(Player.Direction.FACE_LEFT);
				loc.moveWest();//oo
			}
		} else if (e.getKeyCode() == KeyEvent.VK_D
				|| e.getKeyCode() == KeyEvent.VK_RIGHT) {
			//character will turn 1st if the character is not facing that side
			if (clientPlayer.getDirection() == Direction.BACK_RIGHT) {
				loc.moveEast();
			} else {
				clientPlayer.setDirection(Player.Direction.BACK_RIGHT);
				loc.moveEast();//oo
			}
		} else if (e.getKeyCode() == KeyEvent.VK_E) {
			//turn the GUI to the left side
			//change the board(change the location of objects)
			Board oldBoard = gameClient.getGame().rooms
					.get(GameLauncher.ROOMINDEX).board;
			Board newBoard = new EmptyBoard();

			for (int i = 0; i < oldBoard.getWidth(); i++) {
				for (int j = 0; j < oldBoard.getHeight(); j++) {
					int offset = 1;//because the start position is (0,0) not(1,1), so there is an offset
					newBoard.squares[i][j] = oldBoard.squares[gameClient
							.getGame().rooms.get(GameLauncher.ROOMINDEX).board
							.getHeight()
							- (j + offset)][i];
				}
			}
			gameClient.getGame().rooms.get(GameLauncher.ROOMINDEX).board = newBoard;

			//change the locations of player 
			Location newloc = new Location();
			newloc.setRoom(clientPlayer.getLocation().getRoom());

			int offset = 1;//because the start position is (0,0) not(1,1), so there is a offset

			newloc.setX(gameClient.getGame().rooms.get(GameLauncher.ROOMINDEX).board
					.getHeight() - (clientPlayer.getLocation().getY() + offset));

			newloc.setY(clientPlayer.getLocation().getX());
			clientPlayer.setLocation(newloc);
			//let the player image turn left 
			turnPlayerImageLeft(clientPlayer);
		} else if (e.getKeyCode() == KeyEvent.VK_Q) {
			//turn the gui to right side
			//change the board(change the locations of object)
			Board newBoard = new EmptyBoard();
			Board oldBoard = gameClient.getGame().rooms
					.get(GameLauncher.ROOMINDEX).board;
			for (int i = 0; i < oldBoard.getWidth(); i++) {
				for (int j = 0; j < oldBoard.getHeight(); j++) {
					int offset = 1;//because the start position is (0,0) not(1,1), so there is a offset
					newBoard.squares[i][j] = oldBoard.squares[j][gameClient
							.getGame().rooms.get(GameLauncher.ROOMINDEX).board
							.getWidth()
							- (i + offset)];
				}
			}
			gameClient.getGame().rooms.get(GameLauncher.ROOMINDEX).board = newBoard;

			//change the locations of player 
			Location newloc = new Location();

			newloc.setRoom(clientPlayer.getLocation().getRoom());

			int offset = 1;//because the start position is (0,0) not(1,1), so there is a offset

			newloc.setX(clientPlayer.getLocation().getY());

			newloc.setY(gameClient.getGame().rooms.get(GameLauncher.ROOMINDEX).board
					.getWidth() - (clientPlayer.getLocation().getX() + offset));

			clientPlayer.setLocation(newloc);
			//let the player image turn left 
			turnPlayerImageRight(clientPlayer);

		}
		//allows player to jump on the spot
		else if (e.getKeyCode() == KeyEvent.VK_J) {
			jumpOffset += 20;
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

		//If you find a key, adds it to the inventory and removes from the board
		if (go instanceof Key) {
			System.out.println(loc.getRoom());
			clientPlayer.addToInventory(((Key) go));
			loc.getRoom().board.getSquares()[loc.getY()][loc.getX()]
					.setGameObjectOnSquare(null);
		}
		//If you find a goodPotion, increases your health and removes it from the board
		if (go instanceof GoodPotion) {
			//			System.out.println("is this working");
			clientPlayer.setHealth(clientPlayer.getHealth()
					+ ((GoodPotion) go).getHealthHealAmount());
			loc.getRoom().board.getSquares()[loc.getY()][loc.getX()]
					.setGameObjectOnSquare(null);
		}
		//If you encounter a monster, fight prompt appears
		if (go instanceof Monster) {
			fightDialog();
		}
		//If you find a RareCandy, increases your level and removes it from the board
		if (go instanceof RareCandy) {
			clientPlayer.setPlayerLevel(clientPlayer.getPlayerLevel()
					+ ((RareCandy) go).level());

			clientPlayer.setAttack(clientPlayer.getAttack()
					* clientPlayer.getPlayerLevel());
			loc.getRoom().board.getSquares()[loc.getY()][loc.getX()]
					.setGameObjectOnSquare(null);
		}
		//If you find a Door, checks your inventory for a Key
		//If you have a Key, compares the Key ID and Door ID for a match
		//If they match, allows you to enter a different room
		if (go instanceof Door) {
			for (Item items : clientPlayer.getInventory()) {
				if (items instanceof Key) {
					if (((Door) go).id() == items.id()) {

						Door theDoor = (Door) go;
						Room nowRoom = gameClient.getGame().rooms
								.get(GameLauncher.ROOMINDEX);

						if (nowRoom.level == theDoor.linkFrom) {
							//for change room 
							//1. i change change board
							//2. i change the location of player
							GameLauncher.ROOMINDEX = theDoor.linkTo - 1;
							clientPlayer.getLocation().setRoom(
									gameClient.getGame().rooms
											.get(GameLauncher.ROOMINDEX));

						} else if (nowRoom.level == theDoor.linkFrom + 1) {
							System.out.println("this door goes to level 1");
							GameLauncher.ROOMINDEX = theDoor.linkTo - 2;
							clientPlayer.getLocation().setRoom(
									gameClient.getGame().rooms
											.get(GameLauncher.ROOMINDEX));

						}
					}
				}
			}
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

	/**
	 *@author Sushant Balajee
	 *@author Donald Tang
	 *@contributer Wang Zhen(add Timer)
	 */
	public void fightDialog() {

		final Location loc = clientPlayer.getLocation();

		GameObject go = loc.getRoom().board.getSquares()[loc.getY()][loc.getX()]
				.getGameObjectOnSquare();

		JButton att = new JButton("Attack");
		JButton run = new JButton("Run Away");

		att.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//fight giflabel 
				panel.add(attackLabel);
				//add a timer 
				final Timer timer = new Timer();
				TimerTask tt = new TimerTask() {

					@Override
					public void run() {
						timer.cancel();
						//here is the methods run after timer here 
						//////////////////////////////////
						panel.remove(attackLabel);
						fight();
						//////////////////////////////////
					}
				};
				timer.schedule(tt, 2000);
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
		fightBox.setLocation(600, 400);
		fightBox.setSize(200, 150);

		fightBox.add(type);
		fightBox.add(attack);
		fightBox.add(health);
		fightBox.add(att);
		fightBox.add(run);
		fightBox.setVisible(true);

	}

	/**
	 * @author Sushant Balajee
	 * @author Donald Tang
	 */
	private void fight() {

		final Location loc = clientPlayer.getLocation();

		GameObject go = loc.getRoom().board.getSquares()[loc.getY()][loc.getX()]
				.getGameObjectOnSquare();

		final int damage = ((Monster) go).attack();

		//Monster attacks first
		clientPlayer.setHealth(clientPlayer.getHealth() - damage);
		//Player attacks second
		((Monster) go).setHealth(((Monster) go).getHealth()
				- clientPlayer.getAttack());

		fightBox.dispose();

		//if the player dies, it will show a gif and a message dialog
		if (clientPlayer.isDead()) {
			panel.add(dieLabel);
			JOptionPane.showMessageDialog(null, " You Died ");
			System.exit(0);
		} else if (((Monster) go).isDead()) {
			JOptionPane.showMessageDialog(null, " You Won! \n" + " You lost "
					+ damage + " health \n" + " You gained " + damage
					+ " attack");

			//increases the player attack if they win
			clientPlayer.setAttack(clientPlayer.getAttack() + damage);

			//removes the monster from the board
			loc.getRoom().board.getSquares()[loc.getY()][loc.getX()]
					.setGameObjectOnSquare(null);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_J) {
			jumpOffset -= 20;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * @author Prashant Bhikhu
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object source = arg0.getSource();
		if (source instanceof JMenuItem) {
			JMenuItem menuItem = (JMenuItem) source;

			if (menuItem.getText().equals("Create Game (As Server)")) {
				new ServerFrame();
			} else if (menuItem.getText().equals("Join Game (As Client)")) {
				try {
					gameClient = new GameClient(this);
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
						JOptionPane.showMessageDialog(this,
								"You need to enter a user name that"
										+ " is at least 3 characters long.",
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
				loadLabels();
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
			}
		}
	}

	public void setClientPlayer(Player player) {
		this.clientPlayer = player;
	}

	public Player getClientPlayer() {
		return clientPlayer;
	}
}

//=========================================================================
/**
 * these is another way to print the character-create a jlabel for character and add it to the panel
 * */
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
