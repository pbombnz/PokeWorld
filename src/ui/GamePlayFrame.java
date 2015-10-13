package ui;

/**
 * @author Wang Zhen
 * This class include all methods,fileds of game gui and some game logic
 * 
 * This gui has two parts:1st person view gui on the right side and 3rd person 
 * view gui on the left side.
 *  
 * This gui has two keyboard control system:The 1st system is for using in 3rd person view gui.
 * (user can use WSAD to move. W-north S-south A-west D-east)
 * The 2nd system is for using on 1st person view gui.I create this control system for easily playing in 1st person view. 
 * (user can use Up,Down,Left,Right to move. Up-go,Down-turn around,Left-turn left,Right-turn right)
 * And user can also use "J" to jump in both 2 views.
 * 
 * The functions in this class:evolving animation,fighting animation,move,pick up(automaticlly pick up when player go to the sqaure with object),
 * drop,fight,change weather(rainy,sunny),change day or night,send message to other player,
 * monster wonder around,draw mini map,make player shake,refresh player's information.
 * 
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

import com.sun.org.apache.bcel.internal.generic.LCONST;

import rooms.Board;
import rooms.EmptyBoard;
import rooms.Room;
import network.GameClient;
import network.GameClientListener;
import game.BoardSquare;
import game.Game;
import game.Location;
import game.Player;
import game.Direction;
import game.avatar.Avatar;
import game.avatar.Evolution;
import game.objects.interactiveObjects.*;
import game.objects.GameObject;
import game.objects.scene.*;
import game.objects.monster.*;

@SuppressWarnings("serial")
public class GamePlayFrame extends JFrame implements KeyListener,
		ActionListener, WindowListener, GameClientListener {
	// The Emum has holds states for the JFrame so we know what to draw and when
	// for instance we draw
	private static enum FRAME_STATE {
		STANDBY, GAME_RUNNING
	};

	// The Size of the Frame
	private static final int FRAME_HEIGHT = 610;
	private static final int FULL_FRAME_WIDTH = 1400;
	// The size of the tiles when they are displayed
	private static final int TILE_WIDTH = 64;
	private static final int TILE_HEIGHT = 64;

	private FRAME_STATE frameState = FRAME_STATE.STANDBY;

	private GameClient gameClient = new GameClient();
	private Rotate rotate = new Rotate();

	public int jumpOffset = 0;
	//these fileds are for helping make player shake even if they do not move
	public int shakeOffsetZero = 0;//for changing the print postion on screen(some character picture is bigger, so need to be printed higher).the smaller the value is , the higher the character print
	public int shakeOffset = shakeOffsetZero;//the player will keep shake when they are standing in one place
	public int shakeTimer = 0;//using calculation number as timer. the shakeoffset will change when the timer reaches the timerLimit
	public final int SHAKE_TIMER_LIMIT = 200;// the shakeoffset will change when it reaches the timer limit

	//level up
	private boolean isLevelUpping = false;//stop key control and monster moving when the player is level uping
	private final int LVL2_PRINT_OFFSET = -5;//the lvl2 picture is bigger , so print it higher
	private final int LVL3_PRINT_OFFSET = -10;//the lvl3 picture is bigger , so print it higher

	//these fields are for printing information lables
	public boolean hasLoadedLabels = false;//only load label 1 time on panel. if hasLoadedLabels, wont load again
	public List<JLabel> infoLabels = new ArrayList<JLabel>();//add infomation labels here, delete them before print them again
	public JLabel headPictureLabel = null;
	public JLabel bgHeadViewLabel = null;
	public JLabel dieLabel = null;
	public JLabel attackLabel = null;
	public JPanel panel;
	public JLabel characterLabel;

	//fight
	private JDialog fightBox;//dialog when fight monster
	private boolean isFighting = false;//stop key control and monster moving when the player is level uping

	//add explored time
	private String startTime = null;
	public JLabel timeLabel = new JLabel();

	public List<JLabel> itemJLabels = new ArrayList<JLabel>();

	public boolean componentsAdded = false;//only add components 1 time on panel. if componentsAdded, wont load again

	//drop invertory
	public JButton dropButton = new JButton();
	private JDialog dropBox;//items choosing dialog when press drop button
	public JButton sendMessageButton = new JButton();
	public int jumpTimeCounter = 0;

	private boolean isJumping = false;//whether the player is jumping now

	//changing weather
	public JButton rainyButton = new JButton();
	public JButton sunnyButton = new JButton();
	public JButton dayButton = new JButton();
	public JButton nightButton = new JButton();
	public boolean isRainning = false;

	//monster wonder around
	private long lastMovedtime = 0;//the time that monster  moved in last wonder around unit 
	private boolean moved = false;
	private List<Monster> monstersChanged = new ArrayList<Monster>();//monster's location already be changed in one turn in wander around

	//choose day or night
	protected boolean isDay = true;
	private final int sightRange = 3;//the sight range in night

	private JTextArea textOutputArea;
	private JTextField inputMessageField;

	///================================================
	//the file below is for drawing 1st view
	//assume the is width of 1 square is 300 in 1st view
	private static final int FRAME_WIDTH = 800;//the width of the left backgroud picture
	public static final int CHARACTER_SIZE_IN_FIRST_VIIEW = 100;
	public static final int TREE_SCALE_FIRST_VIEW = 4;
	public static final int CHARACTER_BASED_Y_IN_FIRST_VIIEW = 450;
	public static final int UNIT_SECOND = 1;//every unit second,monster move around
	public int squareWidthView = 300;
	public int playerXView = (FULL_FRAME_WIDTH - FRAME_WIDTH) / 2;//push player in the mid of view window
	public int playerYView = FRAME_HEIGHT;
	public double speedOfScaleChange = 0.1;
	public double scaleY = 0.8;
	public int startX = 810;
	public int startY = 3;
	public int viewWidth = 550;
	public int viewHight = 550;
	//	public int horizonLine = 188;
	public int squareHeigh = 60;
	public int squareWidth = 200;
	public int midOfView = startX + viewWidth / 2;
	private int jumpOffsetFirstView = 0;
	private int turnOffset = 750;
	private int turnCounter = 0;
	private Direction firstViewDirection = Direction.BACK_LEFT;
	protected JLabel lvlupLabel_2;
	protected JLabel lvlupLabel_3;
	private final int TEXT_OUTPUT_ROWS = 5;
	private final int SEARCH_COLS = 15;
	private boolean UPDATE_ON_EVERY_CHARACTER = true;

	///==================================

	public GamePlayFrame() {
		gameClient.setGameClientListener(this);

		//initialises game frame
		setSize(FULL_FRAME_WIDTH, FRAME_HEIGHT);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		setResizable(false);

		//create panel
		panel = new GamePanel();
		setContentPane(panel);
		panel.setOpaque(false);
		panel.setLayout(null);

		// Create Menu
		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");

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

		frameState = GamePlayFrame.FRAME_STATE.STANDBY;
		//set start time
		startTime = getCurrentTime();
	}

	/**
	 * Add Components: drop ,rainy, sunny,day , night,send
	 * only add Components 1 time on panel.After labels added  change the hasLoadedLabels to true.
	 * if hasLoadedLabels is true, wont load button again
	 */
	public void addComponents() {
		//Add button
		dropButton.setText("Drop");
		dropButton.setToolTipText("Press this button to drop item");
		dropButton.setBounds(0, 300, 100, 40);
		panel.add(dropButton);
		dropButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				dropDialog();
			}
		});
		//add rainy and sunny button
		rainyButton.setText("Rainy");
		rainyButton
				.setToolTipText("Press this button to change the whether to rainny");
		rainyButton.setBounds(450, 10, 100, 30);
		panel.add(rainyButton);
		rainyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				isRainning = true;
				requestFocus();
			}
		});
		sunnyButton.setText("Sunny");
		sunnyButton
				.setToolTipText("Press this button to change the weather to sunny");
		sunnyButton.setBounds(450, 40, 100, 30);
		panel.add(sunnyButton);
		sunnyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				isRainning = false;
				requestFocus();
			}
		});
		//add day and night button
		dayButton.setText("Day");
		dayButton.setToolTipText("Press this button to change to day");
		dayButton.setBounds(350, 10, 100, 30);
		panel.add(dayButton);
		dayButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				isDay = true;
				requestFocus();
			}
		});
		nightButton.setText("Night");
		nightButton.setToolTipText("Press this button to change to night");
		nightButton.setBounds(350, 40, 100, 30);
		panel.add(nightButton);
		nightButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				isDay = false;
				requestFocus();
			}
		});

		//add textarea
		textOutputArea = new JTextArea(TEXT_OUTPUT_ROWS, 0);
		textOutputArea.setLineWrap(true);
		textOutputArea.setWrapStyleWord(true); // pretty line wrap.
		textOutputArea.setEditable(false);
		JScrollPane scroll = new JScrollPane(textOutputArea);
		scroll.setToolTipText("Output messages");
		// these two lines make the JScrollPane always scroll to the bottom when
		// text is appended to the JTextArea.
		DefaultCaret caret = (DefaultCaret) textOutputArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scroll.setBounds(0, 470, 350, 70);
		panel.add(scroll);
		textOutputArea.setText("");
		textOutputArea.append("Welcome to PokeWorld!");

		//add textField to input message
		inputMessageField = new JTextField(SEARCH_COLS);
		inputMessageField
				.setToolTipText("Input the message you want to send here");
		inputMessageField.setMaximumSize(new Dimension(0, 25));
		inputMessageField.setBounds(0, 440, 200, 20);
		inputMessageField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessage();
				requestFocus();
			}
		});
		panel.add(inputMessageField);

		//add send Message button
		sendMessageButton.setText("Send");
		sendMessageButton.setToolTipText("Press to send message");
		sendMessageButton.setBounds(210, 435, 80, 30);
		panel.add(sendMessageButton);
		sendMessageButton.addActionListener(this);
	}

	/**
	 * output Message To TextArea
	 */
	private void outputMessageToTextArea(String playerName, String message) {
		textOutputArea.append("\n<" + playerName + ">" + message);
	}

	/**
	 * send Message to client
	 */
	private void sendMessage() {
		String playerName = gameClient.getClientPlayer().getName();
		String message = inputMessageField.getText();
		outputMessageToTextArea(playerName, message);
		gameClient.sendMessage(playerName, message);
		inputMessageField.setText("");
	}

	/**
	 * drop the number index 
	 * @param index
	 */
	public void dropIventory(int index) {
		Player clientPlayer = gameClient.getClientPlayer();
		Location loc = clientPlayer.getLocation();
		loc.getRoom().board.getSquares()[loc.getY()][loc.getX()]
				.setGameObjectOnSquare(clientPlayer.getInventory().get(index));
		Item removedItem = clientPlayer.getInventory().remove(index);
		gameClient.sendDropItem(removedItem,
				new Location(loc.getRoom(), loc.getX(), loc.getY()),
				clientPlayer.getId());

	}

	/**
	 * get currrent time String
	 * @return
	 */
	public String getCurrentTime() {
		Date date = new Date(System.currentTimeMillis());
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = format.format(date);
		return time;
	}

	public void loadLabels() {
		// Get Client Player from Client Connection
		Player clientPlayer = gameClient.getClientPlayer();

		//load labels of player information
		//create background label
		JLabel bgHeadViewLabel = new JLabel(new ImageIcon("src/bgHeadView.png"));
		//load head picture
		JLabel headPictureLabel = null;

		System.out.println(clientPlayer);
		ImageIcon headPicture = clientPlayer.getAvatar()
				.getCurrentEvolution(clientPlayer.getPlayerLevel())
				.getDisplayPictureGIF();
		headPictureLabel = new JLabel(headPicture);
		headPictureLabel.setToolTipText("The head picture of character");

		int xPo = 10;
		int yPo = 10;
		int headPictureSize = 130;
		bgHeadViewLabel.setBounds(xPo, yPo, headPictureSize, headPictureSize);
		headPictureLabel.setBounds(xPo, yPo, headPictureSize, headPictureSize);
		headPictureLabel.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5,
				Color.BLACK));
		this.bgHeadViewLabel = bgHeadViewLabel;
		this.headPictureLabel = headPictureLabel;

		//load labels of die and attack and levelup
		JLabel dieLabel = null;
		JLabel attackLabel = null;
		JLabel levelUpLabel_2 = null;
		JLabel levelUpLabel_3 = null;

		//print head picture <- Bad Comment
		// Proper Comment: Sets all Labels with Sprites of the clients player.
		Evolution clientPlayerCurrentEvolution = clientPlayer.getAvatar()
				.getCurrentEvolution(clientPlayer.getPlayerLevel());

		dieLabel = new JLabel(clientPlayerCurrentEvolution.getDieGIF());
		attackLabel = new JLabel(clientPlayerCurrentEvolution.getAttackGIF());
		levelUpLabel_2 = new JLabel(
				clientPlayerCurrentEvolution.getEvolvingGIF());

		if (clientPlayer.getPlayerLevel() != Player.MAX_PLAYER_LEVEL) {
			Evolution clientPlayerNextEvolution = clientPlayer.getAvatar()
					.getNextEvolution(clientPlayer.getPlayerLevel());
			levelUpLabel_3 = new JLabel(
					clientPlayerNextEvolution.getEvolvingGIF());
		} else {
			levelUpLabel_3 = new JLabel();
		}

		int diexPo = 400;
		int dieyPo = 100;
		int dieOrAttackLabelSize = 300;

		dieLabel.setBounds(diexPo, dieyPo, dieOrAttackLabelSize * 2,
				dieOrAttackLabelSize);
		attackLabel.setBounds(diexPo, dieyPo, dieOrAttackLabelSize * 2,
				dieOrAttackLabelSize);
		levelUpLabel_2.setBounds(diexPo, dieyPo, dieOrAttackLabelSize * 2,
				dieOrAttackLabelSize);
		levelUpLabel_3.setBounds(diexPo, dieyPo, dieOrAttackLabelSize * 2,
				dieOrAttackLabelSize);

		this.dieLabel = dieLabel;
		this.attackLabel = attackLabel;
		this.lvlupLabel_2 = levelUpLabel_2;
		this.lvlupLabel_3 = levelUpLabel_3;

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

		//set time label; 
		//initialization
		panel.remove(timeLabel);
		//set format
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//get how long game rinning
		long runningTime = 0;
		try {
			runningTime = sdf.parse(getCurrentTime()).getTime()
					- sdf.parse(startTime).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		JLabel timeLabel = new JLabel();
		timeLabel.setText(getTimeText(runningTime));
		timeLabel.setLocation(10, 260);
		timeLabel.setSize(400, 20);
		timeLabel.setFont(new Font("Dialog", 1, 15));
		timeLabel.setHorizontalAlignment(JLabel.LEFT);
		this.timeLabel = timeLabel;
		panel.add(this.timeLabel);

		repaint();
	}

	private String getTimeText(long runningTime) {
		runningTime = runningTime / 1000;//trasfter ms to s
		long second = (runningTime % 60);
		runningTime -= second;
		runningTime = runningTime / 60;//trasfter s to m
		long minute = (runningTime % 60);
		runningTime -= minute;
		runningTime = runningTime / 60;//trasfter m to hour
		long hours = (runningTime % 60);
		if (second >= 10 && minute >= 10) {
			return ("Time: " + hours + ":" + minute + ":" + second);
		} else if (second < 10 && minute >= 10) {
			return ("Time: " + hours + ":" + minute + ":0" + second);
		} else if (second >= 10 && minute < 10) {
			return ("Time: " + hours + ":0" + minute + ":" + second);
		} else {
			return ("Time: " + hours + ":0" + minute + ":0" + second);
		}
	}

	private void changeShakeLimit() {
		if (shakeOffset == shakeOffsetZero) {
			shakeOffset = shakeOffsetZero + 5;
		} else {
			shakeOffset = shakeOffsetZero;
		}
	}

	//------------------------------------------------------------------------------------------------
	class GamePanel extends JPanel {
		protected void paintComponent(Graphics g) {
			super.paintComponent(g); // Clears panel
			// jumpTimeCounter run
			if (isJumping) {
				jumpTimeCounter++;
				if (jumpTimeCounter > 20) {
					if (jumpOffset == 0) {
						jumpOffset = -20;
						jumpOffsetFirstView = -50;
					} else {
						jumpOffset = 0;
						jumpOffsetFirstView = 0;
					}
				}
			}

			//draw welcome picture
			if (frameState == FRAME_STATE.STANDBY) {
				g.drawImage(new ImageIcon(
						"./sprites/backgrounds/welcome_bg.jpg").getImage(), 0,
						0, FULL_FRAME_WIDTH, FRAME_HEIGHT, null);
				return;
			}

			if (gameClient.getGame() == null && !gameClient.isConnected()) {
				super.paintComponent(g);
				frameState = FRAME_STATE.STANDBY;
				this.repaint();
				return;
			}

			////================================================================
			//1st view
			//draw frame
			g.setColor(Color.black);
			g.fillRect(startX - 10, 0, FULL_FRAME_WIDTH - startX, FRAME_HEIGHT);
			//			g.drawLine(startX - 10, startY, startX + viewWidth, startY
			//					+ viewHight);
			//print background
			if (turnOffset < 0) {
				turnOffset = 2250;
			}
			if (turnOffset > 2250) {
				turnOffset = 0;
			}

			if (isDay) {
				g.drawImage(new ImageIcon("src/firstview_bk.png").getImage(),
						startX + 2 - turnOffset, startY + 2
								- jumpOffsetFirstView - 60, null);
			} else {
				g.drawImage(
						new ImageIcon("src/darkfirstview_bk.png").getImage(),
						startX + 2 - turnOffset, startY + 2
								- jumpOffsetFirstView - 60, null);
			}
			int changeOffset = 50;
			if (turnCounter > 0) {
				//turn right
				turnCounter--;
				turnOffset += changeOffset;
			} else if (turnCounter < 0) {
				//turn left
				turnCounter++;
				turnOffset -= changeOffset;
			}

			Player clientPlayer = gameClient.getClientPlayer();

			if (!hasLoadedLabels) {
				//add all jlabel after picking character
				loadLabels();
				panel.add(headPictureLabel);
				panel.add(bgHeadViewLabel);
				hasLoadedLabels = true;
			}

			int numSquaresFace = 0;
			int numSquaresLeft = 0;
			int numSquaresRight = 0;
			Location playerLoc = clientPlayer.getLocation();
			int boardSize = 10;
			int offset = 1;//this offset is cuz the locaion is from 0 not 1
			if (clientPlayer.getDirection() == Direction.BACK_LEFT) {
				numSquaresFace = playerLoc.getY();
				numSquaresLeft = playerLoc.getX() + offset;
				numSquaresRight = boardSize - playerLoc.getX();
			} else if (clientPlayer.getDirection() == Direction.BACK_RIGHT) {
				numSquaresFace = boardSize - playerLoc.getX() - offset;
				numSquaresLeft = playerLoc.getY() + offset;
				numSquaresRight = boardSize - playerLoc.getY();
			} else if (clientPlayer.getDirection() == Direction.FACE_LEFT) {
				numSquaresFace = playerLoc.getX();
				numSquaresLeft = boardSize - playerLoc.getY();
				numSquaresRight = playerLoc.getY() + offset;
			} else if (clientPlayer.getDirection() == Direction.FACE_RIGHT) {
				numSquaresFace = boardSize - playerLoc.getY() - offset;
				numSquaresLeft = boardSize - playerLoc.getX();
				numSquaresRight = playerLoc.getX() + offset;
			}
			//			System.out.println(numSquaresFace);
			//			System.out.println(playerLoc.getX()+","+playerLoc.getY());
			//			System.out.println(numSquaresFace + " left:" + numSquaresLeft
			//					+ "right:" + numSquaresRight);

			double nowDrawLine = viewHight;//the height of line now draw(it is the bot of the frame at start)
			double previouDrawLine = viewHight;
			double previouX0 = midOfView - squareWidth / 2;//the line in the bot of the frame
			double previouY0 = viewHight;
			double previouX1 = midOfView + squareWidth / 2;//the line in the bot of the frame
			double previouY1 = viewHight;
			int checkLocationX = clientPlayer.getLocation().getX();
			int checkLocationY = clientPlayer.getLocation().getY();
			//#draw background Rectengel
			for (int i = 0; i < numSquaresFace + 1; i++) {
				//draw face square
				double nowWidthOfSquare = squareWidth * Math.pow(scaleY, i + 1);
				double nowStartX = midOfView - nowWidthOfSquare / 2;
				//add points for drawing Polygon
				int[] xPoint = new int[4];
				int[] yPoint = new int[4];
				//draw Polygon
				xPoint[0] = (int) previouX0;
				yPoint[0] = (int) previouY0 - jumpOffsetFirstView;
				xPoint[1] = (int) previouX1;
				yPoint[1] = (int) previouY1 - jumpOffsetFirstView;
				xPoint[2] = (int) (nowStartX + nowWidthOfSquare);
				yPoint[2] = (int) (nowDrawLine - squareHeigh
						* Math.pow(scaleY, i + 1) - jumpOffsetFirstView);
				xPoint[3] = (int) nowStartX;
				yPoint[3] = (int) (nowDrawLine - squareHeigh
						* Math.pow(scaleY, i + 1) - jumpOffsetFirstView);

				if (!isDay) {
					if (i > sightRange) {
						g.setColor(Color.gray.darker().darker());
					} else {
						g.setColor(Color.green.darker());
					}
				} else {
					g.setColor(Color.green.darker());
				}
				g.fillPolygon(xPoint, yPoint, 4);
				g.setColor(Color.BLACK);
				g.drawPolygon(xPoint, yPoint, 4);

				//draw left squares
				for (int j = 0; j < numSquaresLeft; j++) {
					int[] xPointLeft = new int[4];
					int[] yPointLeft = new int[4];
					//draw Polygon
					int previouWidthOfSquare = (int) (previouX1 - previouX0);
					xPointLeft[0] = (int) (previouX0 - j * previouWidthOfSquare);
					yPointLeft[0] = (int) (previouY0 - jumpOffsetFirstView);
					xPointLeft[1] = (int) (previouX1 - j * previouWidthOfSquare);
					yPointLeft[1] = (int) (previouY1 - jumpOffsetFirstView);
					xPointLeft[2] = (int) (nowStartX + nowWidthOfSquare - j
							* nowWidthOfSquare);
					yPointLeft[2] = (int) (nowDrawLine - squareHeigh
							* Math.pow(scaleY, i + 1) - jumpOffsetFirstView);
					xPointLeft[3] = (int) (nowStartX - j * nowWidthOfSquare);
					yPointLeft[3] = (int) (nowDrawLine - squareHeigh
							* Math.pow(scaleY, i + 1) - jumpOffsetFirstView);
					if (!isDay) {
						if (j > sightRange || i > sightRange) {
							g.setColor(Color.gray.darker().darker());
						} else {
							g.setColor(Color.green.darker());
						}
					} else {
						g.setColor(Color.green.darker());
					}
					g.fillPolygon(xPointLeft, yPointLeft, 4);
					g.setColor(Color.BLACK);
					g.drawPolygon(xPointLeft, yPointLeft, 4);
				}

				//draw right squares
				for (int j = 0; j < numSquaresRight; j++) {
					int[] xPointRight = new int[4];
					int[] yPointRight = new int[4];
					//draw Polygon
					int previouWidthOfSquare = (int) (previouX1 - previouX0);
					xPointRight[0] = (int) (previouX0 + j
							* previouWidthOfSquare);
					yPointRight[0] = (int) (previouY0 - jumpOffsetFirstView);
					xPointRight[1] = (int) (previouX1 + j
							* previouWidthOfSquare);
					yPointRight[1] = (int) (previouY1 - jumpOffsetFirstView);
					xPointRight[2] = (int) (nowStartX + nowWidthOfSquare + j
							* nowWidthOfSquare);
					yPointRight[2] = (int) (nowDrawLine - squareHeigh
							* Math.pow(scaleY, i + 1) - jumpOffsetFirstView);
					xPointRight[3] = (int) (nowStartX + j * nowWidthOfSquare);
					yPointRight[3] = (int) (nowDrawLine - squareHeigh
							* Math.pow(scaleY, i + 1) - jumpOffsetFirstView);
					if (!isDay) {
						if (j > sightRange || i > sightRange) {
							g.setColor(Color.gray.darker().darker());
						} else {
							g.setColor(Color.green.darker());
						}
					} else {
						g.setColor(Color.green.darker());
					}
					g.fillPolygon(xPointRight, yPointRight, 4);
					g.setColor(Color.BLACK);
					g.drawPolygon(xPointRight, yPointRight, 4);
				}

				//				g.drawImage(new ImageIcon("src/firstviewgrass.png").getImage(),xPoint[0],yPoint[0] , xPoint[1], yPoint[1], xPoint[2], yPoint[2], xPoint[3],yPoint[3],null);
				//				g.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer)
				//				System.out.println(xPoint[0]+","+yPoint[0]);

				//updata previou
				previouX0 = nowStartX;
				previouY0 = nowDrawLine - squareHeigh * Math.pow(scaleY, i + 1);
				previouX1 = nowStartX + nowWidthOfSquare;
				previouY1 = nowDrawLine - squareHeigh * Math.pow(scaleY, i + 1);
				previouDrawLine = nowDrawLine;
				nowDrawLine = nowDrawLine - squareHeigh
						* Math.pow(scaleY, i + 1);
			}

			//#create storages to store the positions and load information into storages 
			double nowDrawLinePrintObject = viewHight;//the height of line now draw(it is the bot of the frame at start)
			double previouDrawLinePrintObject = viewHight;
			double previouX0PrintObject = midOfView - squareWidth / 2;//the line in the bot of the frame
			double previouY0PrintObject = viewHight;
			double previouX1PrintObject = midOfView + squareWidth / 2;//the line in the bot of the frame
			double previouY1PrintObject = viewHight;
			int checkLocationXPrintObject = clientPlayer.getLocation().getX();
			int checkLocationYPrintObject = clientPlayer.getLocation().getY();
			List<PointArrayStorage> storages = new ArrayList<PointArrayStorage>();
			//these code is for road information into storages-----------------------------------
			for (int i = 0; i < numSquaresFace + 1; i++) {
				PointArrayStorage storage = new PointArrayStorage();
				//draw face square
				double nowWidthOfSquare = squareWidth * Math.pow(scaleY, i + 1);
				double nowStartX = midOfView - nowWidthOfSquare / 2;
				//add points for drawing Polygon
				int[] xPoint = new int[4];
				int[] yPoint = new int[4];
				//draw Polygon
				xPoint[0] = (int) previouX0PrintObject;
				yPoint[0] = (int) previouY0PrintObject - jumpOffsetFirstView;
				xPoint[1] = (int) previouX1PrintObject;
				yPoint[1] = (int) previouY1PrintObject - jumpOffsetFirstView;
				xPoint[2] = (int) (nowStartX + nowWidthOfSquare);
				yPoint[2] = (int) (nowDrawLinePrintObject - squareHeigh
						* Math.pow(scaleY, i + 1) - jumpOffsetFirstView);
				xPoint[3] = (int) nowStartX;
				yPoint[3] = (int) (nowDrawLinePrintObject - squareHeigh
						* Math.pow(scaleY, i + 1) - jumpOffsetFirstView);
				//SAVE INTO STORAGE
				storage.xPoint = xPoint;
				storage.yPoint = yPoint;
				//save left squares
				for (int j = 0; j < numSquaresLeft; j++) {
					int[] xPointLeft = new int[4];
					int[] yPointLeft = new int[4];
					int previouWidthOfSquare = (int) (previouX1PrintObject - previouX0PrintObject);
					xPointLeft[0] = (int) (previouX0PrintObject - j
							* previouWidthOfSquare);
					yPointLeft[0] = (int) (previouY0PrintObject - jumpOffsetFirstView);
					xPointLeft[1] = (int) (previouX1PrintObject - j
							* previouWidthOfSquare);
					yPointLeft[1] = (int) (previouY1PrintObject - jumpOffsetFirstView);
					xPointLeft[2] = (int) (nowStartX + nowWidthOfSquare - j
							* nowWidthOfSquare);
					yPointLeft[2] = (int) (nowDrawLinePrintObject - squareHeigh
							* Math.pow(scaleY, i + 1) - jumpOffsetFirstView);
					xPointLeft[3] = (int) (nowStartX - j * nowWidthOfSquare);
					yPointLeft[3] = (int) (nowDrawLinePrintObject - squareHeigh
							* Math.pow(scaleY, i + 1) - jumpOffsetFirstView);
					//save data
					PointArrayStorageLeft storageLeft = new PointArrayStorageLeft();
					storageLeft.xPoint = xPointLeft;
					storageLeft.yPoint = yPointLeft;
					storage.leftList.add(storageLeft);

				}

				//save right squares
				for (int j = 0; j < numSquaresRight; j++) {
					int[] xPointLeft = new int[4];
					int[] yPointLeft = new int[4];
					int previouWidthOfSquare = (int) (previouX1PrintObject - previouX0PrintObject);
					xPointLeft[0] = (int) (previouX0PrintObject + j
							* previouWidthOfSquare);
					yPointLeft[0] = (int) (previouY0PrintObject - jumpOffsetFirstView);
					xPointLeft[1] = (int) (previouX1PrintObject + j
							* previouWidthOfSquare);
					yPointLeft[1] = (int) (previouY1PrintObject - jumpOffsetFirstView);
					xPointLeft[2] = (int) (nowStartX + nowWidthOfSquare + j
							* nowWidthOfSquare);
					yPointLeft[2] = (int) (nowDrawLinePrintObject - squareHeigh
							* Math.pow(scaleY, i + 1) - jumpOffsetFirstView);
					xPointLeft[3] = (int) (nowStartX + j * nowWidthOfSquare);
					yPointLeft[3] = (int) (nowDrawLinePrintObject - squareHeigh
							* Math.pow(scaleY, i + 1) - jumpOffsetFirstView);
					//save data
					PointArrayStorageRight storageRight = new PointArrayStorageRight();
					storageRight.xPoint = xPointLeft;
					storageRight.yPoint = yPointLeft;
					storage.rightlList.add(storageRight);
				}

				//updata previou
				previouX0PrintObject = nowStartX;
				previouY0PrintObject = nowDrawLinePrintObject - squareHeigh
						* Math.pow(scaleY, i + 1);
				previouX1PrintObject = nowStartX + nowWidthOfSquare;
				previouY1PrintObject = nowDrawLinePrintObject - squareHeigh
						* Math.pow(scaleY, i + 1);
				previouDrawLinePrintObject = nowDrawLinePrintObject;
				nowDrawLinePrintObject = nowDrawLinePrintObject - squareHeigh
						* Math.pow(scaleY, i + 1);
				//add storage to stroages
				storages.add(storage);
			}
			//---------------------------------------------------------------------

			//#print object
			for (int i = numSquaresFace; i > -1; i--) {
				PointArrayStorage storage = storages.get(i);
				int[] xPoint = storage.xPoint;
				int[] yPoint = storage.yPoint;

				//draw left squares
				for (int j = numSquaresLeft - 1; j > -1; j--) {
					PointArrayStorageLeft storageLeft = storage.leftList.get(j);
					int[] xPointLeft = storageLeft.xPoint;
					int[] yPointLeft = storageLeft.yPoint;

					//print the object left this location====================================================
					Location nextLoc = nextSquareLocation(clientPlayer, i);

					Game ga = gameClient.getGame();
					Room r = clientPlayer.getLocation().getRoom();
					BoardSquare[][] bs = r.board.getSquares();

					int locX = nextLoc.getX();
					int locY = nextLoc.getY();
					if (clientPlayer.getDirection() == Direction.FACE_RIGHT) {
						locX = nextLoc.getX() + j;
					} else if (clientPlayer.getDirection() == Direction.FACE_LEFT) {
						locY = nextLoc.getY() + j;
					} else if (clientPlayer.getDirection() == Direction.BACK_LEFT) {
						locX = nextLoc.getX() - j;
					} else if (clientPlayer.getDirection() == Direction.BACK_RIGHT) {
						locY = nextLoc.getY() - j;
					}

					//diff direction has diff order to print ,this is for making sure the closer picture cover far
					//check whether next square is out of board
					if (locX != -1 && locX != 10 && locY != -1 && locY != 10) {
						if (bs[locY][locX].getGameObjectOnSquare() != null) {
							if (isDay) {
								if (bs[locY][locX].getGameObjectOnSquare() instanceof Tree) {
									int width = (xPoint[1] - xPoint[0])
											* TREE_SCALE_FIRST_VIEW;
									int height = width;
									int midPointX = xPointLeft[0]
											+ (xPointLeft[1] - xPointLeft[0])
											/ 2;
									int drawStartX = midPointX - width / 2;
									int drawStartY = yPointLeft[0] - (height);
									g.drawImage(bs[locY][locX]
											.getGameObjectOnSquare()
											.getSpriteImage().getImage(),
											drawStartX, drawStartY, width,
											height, null);
								} else {
									int height = (xPointLeft[2] - xPointLeft[3]);
									int width = height;
									int drawStartX = xPointLeft[3];
									int drawStartY = yPointLeft[0]
											- ((xPointLeft[2] - xPointLeft[3]));
									g.drawImage(bs[locY][locX]
											.getGameObjectOnSquare()
											.getSpriteImage().getImage(),
											drawStartX, drawStartY, width,
											height, null);
								}
							} else {
								if (isInSightRange(clientPlayer, locY, locX)) {
									if (bs[locY][locX].getGameObjectOnSquare() instanceof Tree) {
										int width = (xPoint[1] - xPoint[0])
												* TREE_SCALE_FIRST_VIEW;
										int height = width;
										int midPointX = xPointLeft[0]
												+ (xPointLeft[1] - xPointLeft[0])
												/ 2;
										int drawStartX = midPointX - width / 2;
										int drawStartY = yPointLeft[0]
												- (height);
										g.drawImage(bs[locY][locX]
												.getGameObjectOnSquare()
												.getSpriteImage().getImage(),
												drawStartX, drawStartY, width,
												height, null);
									} else {
										int height = (xPointLeft[2] - xPointLeft[3]);
										int width = height;
										int drawStartX = xPointLeft[3];
										int drawStartY = yPointLeft[0]
												- ((xPointLeft[2] - xPointLeft[3]));
										g.drawImage(bs[locY][locX]
												.getGameObjectOnSquare()
												.getSpriteImage().getImage(),
												drawStartX, drawStartY, width,
												height, null);
									}
								}
							}
						}
					}
				}

				//draw right squares
				for (int j = numSquaresRight - 1; j > -1; j--) {
					PointArrayStorageRight storageRight = storage.rightlList
							.get(j);
					int[] xPointRight = storageRight.xPoint;
					int[] yPointRight = storageRight.yPoint;
					//print the object left on this location====================================================
					Location nextLoc = nextSquareLocation(clientPlayer, i);

					Game ga = gameClient.getGame();
					Room r = clientPlayer.getLocation().getRoom();
					BoardSquare[][] bs = r.board.getSquares();

					int locX = nextLoc.getX();
					int locY = nextLoc.getY();
					if (clientPlayer.getDirection() == Direction.FACE_RIGHT) {
						locX = nextLoc.getX() - j;
					} else if (clientPlayer.getDirection() == Direction.FACE_LEFT) {
						locY = nextLoc.getY() - j;
					} else if (clientPlayer.getDirection() == Direction.BACK_LEFT) {
						locX = nextLoc.getX() + j;
					} else if (clientPlayer.getDirection() == Direction.BACK_RIGHT) {
						locY = nextLoc.getY() + j;
					}

					//diff direction has diff order to print ,this is for making sure the closer picture cover far
					//check whether next square is out of board
					if (locX != -1 && locX != 10 && locY != -1 && locY != 10) {
						if (bs[locY][locX].getGameObjectOnSquare() != null) {
							if (isDay) {
								if (bs[locY][locX].getGameObjectOnSquare() instanceof Tree) {
									int width = (xPoint[1] - xPoint[0])
											* TREE_SCALE_FIRST_VIEW;
									int height = width;
									int midPointX = xPointRight[0]
											+ (xPointRight[1] - xPointRight[0])
											/ 2;
									int drawStartX = midPointX - width / 2;
									int drawStartY = yPointRight[0] - (height);
									g.drawImage(bs[locY][locX]
											.getGameObjectOnSquare()
											.getSpriteImage().getImage(),
											drawStartX, drawStartY, width,
											height, null);
								} else {
									int height = (xPointRight[2] - xPointRight[3]);
									int width = height;
									int drawStartX = xPointRight[3];
									int drawStartY = yPointRight[0]
											- ((xPointRight[2] - xPointRight[3]));
									g.drawImage(bs[locY][locX]
											.getGameObjectOnSquare()
											.getSpriteImage().getImage(),
											drawStartX, drawStartY, width,
											height, null);
								}
							} else {
								if (isInSightRange(clientPlayer, locY, locX)) {
									if (bs[locY][locX].getGameObjectOnSquare() instanceof Tree) {
										int width = (xPoint[1] - xPoint[0])
												* TREE_SCALE_FIRST_VIEW;
										int height = width;
										int midPointX = xPointRight[0]
												+ (xPointRight[1] - xPointRight[0])
												/ 2;
										int drawStartX = midPointX - width / 2;
										int drawStartY = yPointRight[0]
												- (height);
										g.drawImage(bs[locY][locX]
												.getGameObjectOnSquare()
												.getSpriteImage().getImage(),
												drawStartX, drawStartY, width,
												height, null);
									} else {
										int height = (xPointRight[2] - xPointRight[3]);
										int width = height;
										int drawStartX = xPointRight[3];
										int drawStartY = yPointRight[0]
												- ((xPointRight[2] - xPointRight[3]));
										g.drawImage(bs[locY][locX]
												.getGameObjectOnSquare()
												.getSpriteImage().getImage(),
												drawStartX, drawStartY, width,
												height, null);
									}
								}
							}
						}
					}
				}

				//print the object on this location====================================================
				Location nextLoc = nextSquareLocation(clientPlayer, i);

				Game ga = gameClient.getGame();
				Room r = clientPlayer.getLocation().getRoom();
				BoardSquare[][] bs = r.board.getSquares();

				//check whether next square is out of board
				if (nextLoc.getX() != -1 && nextLoc.getX() != 10
						&& nextLoc.getY() != -1 && nextLoc.getY() != 10) {
					if (bs[nextLoc.getY()][nextLoc.getX()]
							.getGameObjectOnSquare() != null) {
						if (isDay) {
							if (bs[nextLoc.getY()][nextLoc.getX()]
									.getGameObjectOnSquare() instanceof Tree) {
								int width = (xPoint[1] - xPoint[0])
										* TREE_SCALE_FIRST_VIEW;
								int height = width;
								int midPointX = xPoint[0]
										+ (xPoint[1] - xPoint[0]) / 2;
								int drawStartX = midPointX - width / 2;
								int drawStartY = yPoint[0] - (height);
								g.drawImage(bs[nextLoc.getY()][nextLoc.getX()]
										.getGameObjectOnSquare()
										.getSpriteImage().getImage(),
										drawStartX, drawStartY, width, height,
										null);
							} else {
								int height = (xPoint[2] - xPoint[3]);
								int width = height;
								int drawStartX = xPoint[3];
								int drawStartY = yPoint[0]
										- ((xPoint[2] - xPoint[3]));
								g.drawImage(bs[nextLoc.getY()][nextLoc.getX()]
										.getGameObjectOnSquare()
										.getSpriteImage().getImage(),
										drawStartX, drawStartY, width, height,
										null);
								/**these are set size as (yPoint[0] - yPoint[3]), this print smaller picture---------------------------------------
								*/
								//							int height = yPoint[0] - yPoint[3];
								//							int width = height;
								//							int midPointX = xPoint[0] + (xPoint[1] - xPoint[0])
								//									/ 2;
								//							int drawStartX = midPointX - width / 2;
								//							int drawStartY = yPoint[3];
								//							g.drawImage(bs[nextLoc.getY()][nextLoc.getX()]
								//									.getGameObjectOnSquare().getSpriteImage()
								//								.getImage(), drawStartX,drawStartY,width,height, null);
								//----------------------------------------------------------------------------------
							}
						} else {
							if (isInSightRange(clientPlayer, nextLoc.getY(),
									nextLoc.getX())) {
								if (bs[nextLoc.getY()][nextLoc.getX()]
										.getGameObjectOnSquare() instanceof Tree) {
									int width = (xPoint[1] - xPoint[0])
											* TREE_SCALE_FIRST_VIEW;
									int height = width;
									int midPointX = xPoint[0]
											+ (xPoint[1] - xPoint[0]) / 2;
									int drawStartX = midPointX - width / 2;
									int drawStartY = yPoint[0] - (height);
									g.drawImage(
											bs[nextLoc.getY()][nextLoc.getX()]
													.getGameObjectOnSquare()
													.getSpriteImage()
													.getImage(), drawStartX,
											drawStartY, width, height, null);
								} else {
									int height = (xPoint[2] - xPoint[3]);
									int width = height;
									int drawStartX = xPoint[3];
									int drawStartY = yPoint[0]
											- ((xPoint[2] - xPoint[3]));
									g.drawImage(
											bs[nextLoc.getY()][nextLoc.getX()]
													.getGameObjectOnSquare()
													.getSpriteImage()
													.getImage(), drawStartX,
											drawStartY, width, height, null);
									/**these are set size as (yPoint[0] - yPoint[3]), this print smaller picture---------------------------------------
									*/
									//							int height = yPoint[0] - yPoint[3];
									//							int width = height;
									//							int midPointX = xPoint[0] + (xPoint[1] - xPoint[0])
									//									/ 2;
									//							int drawStartX = midPointX - width / 2;
									//							int drawStartY = yPoint[3];
									//							g.drawImage(bs[nextLoc.getY()][nextLoc.getX()]
									//									.getGameObjectOnSquare().getSpriteImage()
									//								.getImage(), drawStartX,drawStartY,width,height, null);
									//----------------------------------------------------------------------------------
								}
							}
						}
					}
				}
			}

			//print edge 
			g.setColor(Color.black);

			//print character
			Image characterImage = clientPlayer.getSpriteBasedOnDirection(
					firstViewDirection).getImage();
			g.drawImage(characterImage, midOfView,
					CHARACTER_BASED_Y_IN_FIRST_VIIEW + shakeOffset,
					CHARACTER_SIZE_IN_FIRST_VIIEW,
					CHARACTER_SIZE_IN_FIRST_VIIEW, null);

			g.fillRect(startX - 10, 0, 20, FRAME_HEIGHT);
			g.fillRect(FULL_FRAME_WIDTH - 20, 0, 20, FRAME_HEIGHT);
			///=============================================

			/// Draw background picture
			if (isDay) {
				g.drawImage(new ImageIcon("./sprites/backgrounds/game_bg.jpg")
						.getImage(), 0, 0, FRAME_WIDTH, FRAME_HEIGHT, null);
			} else {
				g.drawImage(new ImageIcon(
						"./sprites/backgrounds/darkgame_bg.jpg").getImage(), 0,
						0, FRAME_WIDTH, FRAME_HEIGHT, null);
			}

			///print compass
			g.drawImage(
					new ImageIcon("./sprites/other/compass.png").getImage(),
					600, 355, 200, 200, null);

			// Initial starting position of where the first square is going to be drawn
			int yPos = FRAME_HEIGHT / 2;
			int xPos = 110;

			//caculate time 1st for adding "wander around"
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long runningTime = 0;
			try {
				runningTime = sdf.parse(getCurrentTime()).getTime()
						- sdf.parse(startTime).getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}

			for (int cellY = 0; cellY < 10; cellY++) {
				for (int cellX = 9; cellX >= 0; cellX--) {
					int tileX = xPos + (cellX * TILE_WIDTH / 2);
					int tileY = yPos - (cellX * TILE_HEIGHT / 4);

					if (isDay) {
						g.drawImage(new ImageIcon("./sprites/tiles/grass.png")
								.getImage(), tileX, tileY, TILE_WIDTH,
								TILE_HEIGHT, null);
					} else {
						if (isInSightRange(clientPlayer, cellY, cellX)) {
							g.drawImage(new ImageIcon(
									"./sprites/tiles/grass.png").getImage(),
									tileX, tileY, TILE_WIDTH, TILE_HEIGHT, null);
						} else {
							g.drawImage(
									new ImageIcon(
											"./sprites/tiles/darkgrass.png")
											.getImage(), tileX, tileY,
									TILE_WIDTH, TILE_HEIGHT, null);
						}
					}

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
						if (connectedPlayer != clientPlayer) {
							Location otherPlayerLoc = connectedPlayer
									.getLocation();
							if (otherPlayerLoc
									.getRoom()
									.getName()
									.equals(clientPlayer.getLocation()
											.getRoom().getName())
									&& otherPlayerLoc.getX() == cellX
									&& otherPlayerLoc.getY() == cellY) {
								g.drawImage(
										connectedPlayer
												.getSpriteBasedOnDirection()
												.getImage(), tileX
												+ (TILE_WIDTH / 5), tileY
												- (TILE_HEIGHT / 3), null);
							}
						}
					}

					//draw player
					//shake the player when he dont move
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
					Room r = clientPlayer.getLocation().getRoom();//ga.getRooms().get(GameLauncher.ROOMINDEX);
					BoardSquare[][] bs = r.board.getSquares();
					if (bs[cellY][cellX].getGameObjectOnSquare() != null) {
						if (isDay) {
							if (bs[cellY][cellX].getGameObjectOnSquare() instanceof Tree) {
								g.drawImage(bs[cellY][cellX]
										.getGameObjectOnSquare()
										.getSpriteImage().getImage(),
										tileX - 60, tileY - 170, null);
							} else if (bs[cellY][cellX].getGameObjectOnSquare() instanceof MagicCircle) {
								g.drawImage(bs[cellY][cellX]
										.getGameObjectOnSquare()
										.getSpriteImage().getImage(), tileX,
										tileY, 20, 20, null);
							} else if (bs[cellY][cellX].getGameObjectOnSquare() instanceof Monster) {

								g.drawImage(bs[cellY][cellX]
										.getGameObjectOnSquare()
										.getSpriteImage().getImage(), tileX,
										tileY - (TILE_HEIGHT / 2), 50, 50, null);

								//add "wander around"----------------------------------
								//the monster will stop move when the player is fighting or level uping
								if (!isFighting && !isLevelUpping) {
									int passSecond = (int) (runningTime / 1000);
									//every unit second,monster move around
									if (runningTime != lastMovedtime) {
										if (passSecond % UNIT_SECOND == 0) {
											Monster monster = (Monster) bs[cellY][cellX]
													.getGameObjectOnSquare();
											if (!monstersChanged
													.contains(monster)) {
												if (!letMonsterMove(monster,
														cellY, cellX)) {
													turnMonsterAroundAndMove(
															monster, cellY,
															cellX);
												}
											}
											monstersChanged.add(monster);
											moved = true;
										}
									}
								}
								//update monstersChanged after print all monster in object list
								//-----------------------------------------------------
							} else {
								g.drawImage(bs[cellY][cellX]
										.getGameObjectOnSquare()
										.getSpriteImage().getImage(), tileX,
										tileY - (TILE_HEIGHT / 2), 50, 50, null);
							}
						} else {
							if (isInSightRange(clientPlayer, cellY, cellX)) {
								if (bs[cellY][cellX].getGameObjectOnSquare() instanceof Tree) {
									g.drawImage(bs[cellY][cellX]
											.getGameObjectOnSquare()
											.getSpriteImage().getImage(),
											tileX - 60, tileY - 170, null);
								} else if (bs[cellY][cellX]
										.getGameObjectOnSquare() instanceof MagicCircle) {
									g.drawImage(bs[cellY][cellX]
											.getGameObjectOnSquare()
											.getSpriteImage().getImage(),
											tileX, tileY, 20, 20, null);
								} else if (bs[cellY][cellX]
										.getGameObjectOnSquare() instanceof Monster) {

									g.drawImage(bs[cellY][cellX]
											.getGameObjectOnSquare()
											.getSpriteImage().getImage(),
											tileX, tileY - (TILE_HEIGHT / 2),
											50, 50, null);

									//add "wander around"----------------------------------
									//the monster will stop move when the player is fighting or level uping
									if (!isFighting && !isLevelUpping) {
										int passSecond = (int) (runningTime / 1000);
										//every unit second,monster move around
										int unitSecond = 2;
										if (runningTime != lastMovedtime) {
											if (passSecond % unitSecond == 0) {
												Monster monster = (Monster) bs[cellY][cellX]
														.getGameObjectOnSquare();
												if (!monstersChanged
														.contains(monster)) {
													if (!letMonsterMove(
															monster, cellY,
															cellX)) {
														turnMonsterAroundAndMove(
																monster, cellY,
																cellX);
													}
												}
												monstersChanged.add(monster);
												moved = true;
											}
										}
									}
									//update monstersChanged after print all monster in object list
									//-----------------------------------------------------
								} else {
									g.drawImage(bs[cellY][cellX]
											.getGameObjectOnSquare()
											.getSpriteImage().getImage(),
											tileX, tileY - (TILE_HEIGHT / 2),
											50, 50, null);
								}
							}
						}
					}
				}
				yPos += TILE_HEIGHT / 4;
				xPos += TILE_WIDTH / 2;
			}

			//update monstersChanged after print all monster in object list
			monstersChanged = new ArrayList<Monster>();

			if (moved) {
				lastMovedtime = runningTime;
			}
			//printInformation of player
			printInformation(clientPlayer);

			//print players'inventory
			printInventory(clientPlayer, g);

			//add buttons on frame
			if (componentsAdded == false) {
				addComponents();
				componentsAdded = true;
			}
			//draw small map
			int mapStartX = 600;
			int mapNowX = mapStartX;
			int mapStartY = 10;
			int mapSquareSize = 10;
			for (int cellY = 0; cellY < 10; cellY++) {
				for (int cellX = 0; cellX < 10; cellX++) {
					if (gameClient.getClientPlayer().getLocation().getX() == cellX
							&& gameClient.getClientPlayer().getLocation()
									.getY() == cellY) {
						g.setColor(Color.red);
						g.fillRect(mapNowX, mapStartY, mapSquareSize,
								mapSquareSize);
						g.setColor(Color.black);
						g.drawRect(mapNowX, mapStartY, mapSquareSize,
								mapSquareSize);
					} else {
						Room r = clientPlayer.getLocation().getRoom();//ga.getRooms().get(GameLauncher.ROOMINDEX);
						BoardSquare[][] bs = r.board.getSquares();
						if (bs[cellY][cellX].getGameObjectOnSquare() == null) {
							g.setColor(Color.GREEN.darker());
							g.fillRect(mapNowX, mapStartY, mapSquareSize,
									mapSquareSize);
							g.setColor(Color.black);
							g.drawRect(mapNowX, mapStartY, mapSquareSize,
									mapSquareSize);
						} else if (bs[cellY][cellX].getGameObjectOnSquare() instanceof Tree) {
							g.setColor(Color.black);
							g.fillRect(mapNowX, mapStartY, mapSquareSize,
									mapSquareSize);
							g.setColor(Color.black);
							g.drawRect(mapNowX, mapStartY, mapSquareSize,
									mapSquareSize);
						} else if (bs[cellY][cellX].getGameObjectOnSquare() instanceof Plant) {
							g.setColor(Color.PINK);
							g.fillRect(mapNowX, mapStartY, mapSquareSize,
									mapSquareSize);
							g.setColor(Color.black);
							g.drawRect(mapNowX, mapStartY, mapSquareSize,
									mapSquareSize);
						} else if (bs[cellY][cellX].getGameObjectOnSquare() instanceof Monster) {
							g.setColor(Color.blue);
							g.fillRect(mapNowX, mapStartY, mapSquareSize,
									mapSquareSize);
							g.setColor(Color.black);
							g.drawRect(mapNowX, mapStartY, mapSquareSize,
									mapSquareSize);
						} else if (bs[cellY][cellX].getGameObjectOnSquare() instanceof MagicCircle) {
							g.setColor(Color.CYAN);
							g.fillRect(mapNowX, mapStartY, mapSquareSize,
									mapSquareSize);
							g.setColor(Color.black);
							g.drawRect(mapNowX, mapStartY, mapSquareSize,
									mapSquareSize);
						} else if (bs[cellY][cellX].getGameObjectOnSquare() instanceof Key) {
							g.setColor(Color.ORANGE);
							g.fillRect(mapNowX, mapStartY, mapSquareSize,
									mapSquareSize);
							g.setColor(Color.black);
							g.drawRect(mapNowX, mapStartY, mapSquareSize,
									mapSquareSize);
						} else if (bs[cellY][cellX].getGameObjectOnSquare() instanceof RareCandy) {
							g.setColor(Color.blue);
							g.fillRect(mapNowX, mapStartY, mapSquareSize,
									mapSquareSize);
							g.setColor(Color.black);
							g.drawRect(mapNowX, mapStartY, mapSquareSize,
									mapSquareSize);
						} else {
							g.setColor(Color.LIGHT_GRAY);
							g.fillRect(mapNowX, mapStartY, mapSquareSize,
									mapSquareSize);
							g.setColor(Color.black);
							g.drawRect(mapNowX, mapStartY, mapSquareSize,
									mapSquareSize);
						}
					}

					mapNowX += mapSquareSize;
				}
				mapNowX = mapStartX;
				mapStartY += mapSquareSize;
			}
			//add raining weather
			if (isRainning) {
				int rainStartX = 200;
				int rainStartY = -50;
				int rainEndX = 700;
				int rainEndY = 500;
				int nextX = rainStartX;
				int nextY = rainStartY;
				int number = 0;
				while (number < 50) {
					nextX = (int) (Math.random() * 400 + rainStartX);
					nextY = (int) (Math.random() * 100 + rainStartY);
					int width = (int) (Math.random() * 100 + 20);//from 0 to 40
					int height = 3 * width;
					if (nextY + height > rainEndY) {
						height = rainEndY - nextY;
						width = height / 3;
					}
					g.setColor(Color.WHITE);
					g.drawLine(nextX, nextY, nextX + width, nextY + height);
					//					nextX = nextX+width;
					//					nextY = nextY+height;
					number++;
				}
			}
			//add rain for 1st view
			if (isRainning) {
				int rainStartX = 900;
				int rainStartY = -50;
				int rainEndY = 600;
				int nextX = rainStartX;
				int nextY = rainStartY;
				int number = 0;
				while (number < 50) {
					nextX = (int) (Math.random() * 500 + rainStartX);//get rain from rainStartX to rainStartX+500
					nextY = (int) (Math.random() * 100 + rainStartY);
					int width = (int) (Math.random() * 100 + 20);//from 0 to 40
					int height = 3 * width;
					if (nextY + height > rainEndY) {
						height = rainEndY - nextY;
						width = height / 3;
					}
					g.setColor(Color.WHITE);
					g.drawLine(nextX, nextY, nextX + width, nextY + height);
					number++;
				}
			}
		}
	}

	public boolean isInSightRange(Player player, int y, int x) {
		Location loc = player.getLocation();
		if (x < (loc.getX() + sightRange) && x > (loc.getX() - sightRange)
				&& y < (loc.getY() + sightRange)
				&& y > (loc.getY() - sightRange)) {
			return true;
		} else {
			return false;
		}
	}

	public void printInventory(Player player, Graphics g) {

		int itemSize = 40;
		int itemX = -itemSize;
		int itemY = 350;
		//print item bag 1st
		int bagX = -itemSize;
		int bagY = 350;
		for (int index = 1; index < 7; index++) {
			if (index == 4) {
				bagX = 0;
				bagY += itemSize;
			} else {
				bagX += itemSize;
			}
			g.drawRect(bagX, bagY, itemSize, itemSize);
		}
		//remove previou lables
		for (JLabel jl : itemJLabels) {
			panel.remove(jl);
		}
		//for each item in item bag
		int itemNumber = 0;
		for (Item item : player.getInventory()) {
			JLabel jlabel = new JLabel(player.getInventory().get(itemNumber)
					.getSpriteImage());
			//			jbutton.setIcon(player.getInventory().get(itemNumber)
			//					.getSpriteImage());
			if (itemNumber == 3) {
				itemX = 0;
				itemY += itemSize;
				itemNumber++;
			} else {
				itemX += itemSize;
				itemNumber++;
			}
			int xPo = itemX;
			int yPo = itemY;
			jlabel.setBounds(xPo, yPo, itemSize, itemSize);
			itemJLabels.add(jlabel);
			panel.add(jlabel);
			//			ActionListener al = new DropActionListener(player, item,itemJLables,jlabel);
			//			jlabel.addActionListener(al);
		}
		repaint();
	}

	public Location nextSquareLocation(Player player, int steps) {
		Direction dir = player.getDirection();
		if (dir == Direction.BACK_LEFT) {
			return new Location(player.getLocation().getRoom(), player
					.getLocation().getX(), player.getLocation().getY() - steps);
		} else if (dir == Direction.BACK_RIGHT) {
			return new Location(player.getLocation().getRoom(), player
					.getLocation().getX() + steps, player.getLocation().getY());
		} else if (dir == Direction.FACE_LEFT) {
			return new Location(player.getLocation().getRoom(), player
					.getLocation().getX() - steps, player.getLocation().getY());
		} else if (dir == Direction.FACE_RIGHT) {
			return new Location(player.getLocation().getRoom(), player
					.getLocation().getX(), player.getLocation().getY() + steps);
		}
		return null;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_J) {
			isJumping = true;
			//			jumpOffset = -20;
			//			jumpOffsetFirstView = -50;
		}
	}

	/**
	 * check whether the place can be move into
	 */
	private boolean canMove(int x, int y, Player player) {
		BoardSquare[][] sq = player.getLocation().getRoom().board.getSquares();
		if (x > 9 || x < 0 || y < 0 || y > 9) {
			return false;
		}
		if (sq[y][x].getGameObjectOnSquare() instanceof Tree
				|| sq[y][x].getGameObjectOnSquare() instanceof Fence) {
			return false;
		}
		for (Player otherPlayer : gameClient.getGame().getPlayers()) {
			Location otherLoc = otherPlayer.getLocation();
			if (otherLoc.getX() == x
					&& otherLoc.getY() == y
					&& otherLoc.getRoom().getName()
							.equals(player.getLocation().getRoom().getName())) {
				return false;
			}
		}
		//change here if add new kinds of Monster
		if (sq[y][x].getGameObjectOnSquare() instanceof Mewtwo
				|| sq[y][x].getGameObjectOnSquare() instanceof Rattata
				|| sq[y][x].getGameObjectOnSquare() instanceof Rhydon
				|| sq[y][x].getGameObjectOnSquare() instanceof Zubat) {
			fightDialog(new Location(player.getLocation().getRoom(), x, y));
			return false;
		}
		if (sq[y][x].getGameObjectOnSquare() instanceof MagicCircle) {
			MagicCircle mc = (MagicCircle) (sq[y][x].getGameObjectOnSquare());
			player.setLocation(new Location(player.getLocation().getRoom(), mc
					.getTeleportX(), mc.getTeleportY()));
			return false;
		}
		return true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!isLevelUpping && !isFighting) {
			Player clientPlayer = gameClient.getClientPlayer();
			// don't fire an event on backspace or delete
			Location loc = clientPlayer.getLocation();
			//for third view control
			GameObject gg = loc.getRoom().board.getSquares()[loc.getY()][loc
					.getX()].getGameObjectOnSquare();
			if (e.getKeyCode() == KeyEvent.VK_W) {
				//check whrther can move 
				if (canMove(loc.getX(), loc.getY() - 1, clientPlayer)) {
					//character will turn 1st if the character is not facing that side
					if (clientPlayer.getDirection() == Direction.BACK_LEFT) {
						loc.moveNorth();
					} else {
						clientPlayer.setDirection(Direction.BACK_LEFT);
						loc.moveNorth();
					}
				} else {
					clientPlayer.setDirection(Direction.BACK_LEFT);
				}
			} else if (e.getKeyCode() == KeyEvent.VK_S) {
				//check whrther can move 
				if (canMove(loc.getX(), loc.getY() + 1, clientPlayer)) {
					//character will turn 1st if the character is not facing that side
					if (clientPlayer.getDirection() == Direction.FACE_RIGHT) {
						loc.moveSouth();//oo
					} else {
						clientPlayer.setDirection(Direction.FACE_RIGHT);
						loc.moveSouth();//oo
					}
				} else {
					clientPlayer.setDirection(Direction.FACE_RIGHT);
				}
			} else if (e.getKeyCode() == KeyEvent.VK_A) {
				//check whrther can move 
				if (canMove(loc.getX() - 1, loc.getY(), clientPlayer)) {
					//character will turn 1st if the character is not facing that side
					if (clientPlayer.getDirection() == Direction.FACE_LEFT) {
						loc.moveWest();
					} else {
						clientPlayer.setDirection(Direction.FACE_LEFT);
						loc.moveWest();//oo
					}
				} else {
					clientPlayer.setDirection(Direction.FACE_LEFT);
				}
			} else if (e.getKeyCode() == KeyEvent.VK_D) {
				//check whrther can move 
				if (canMove(loc.getX() + 1, loc.getY(), clientPlayer)) {
					//character will turn 1st if the character is not facing that side
					if (clientPlayer.getDirection() == Direction.BACK_RIGHT) {
						loc.moveEast();
					} else {
						clientPlayer.setDirection(Direction.BACK_RIGHT);
						loc.moveEast();//oo
					}
				} else {
					clientPlayer.setDirection(Direction.BACK_RIGHT);
				}
			}
			//these are for 1st person view contrl
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				//character will turn 1st if the character is not facing that side
				if (clientPlayer.getDirection() == Direction.BACK_RIGHT) {
					//check whrther can move 
					if (canMove(loc.getX() + 1, loc.getY(), clientPlayer)) {
						loc.moveEast();
					}
				} else if (clientPlayer.getDirection() == Direction.BACK_LEFT) {
					//check whrther can move 
					if (canMove(loc.getX(), loc.getY() - 1, clientPlayer)) {
						loc.moveNorth();
					}
				} else if (clientPlayer.getDirection() == Direction.FACE_LEFT) {
					//check whrther can move 
					if (canMove(loc.getX() - 1, loc.getY(), clientPlayer)) {
						loc.moveWest();
					}
				} else if (clientPlayer.getDirection() == Direction.FACE_RIGHT) {
					if (canMove(loc.getX(), loc.getY() + 1, clientPlayer)) {
						loc.moveSouth();
					}
				}
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				//character will turn 1st if the character is not facing that side
				if (clientPlayer.getDirection() == Direction.BACK_LEFT) {
					clientPlayer.setDirection(Direction.FACE_RIGHT);
				} else if (clientPlayer.getDirection() == Direction.BACK_RIGHT) {
					clientPlayer.setDirection(Direction.FACE_LEFT);
				} else if (clientPlayer.getDirection() == Direction.FACE_LEFT) {
					clientPlayer.setDirection(Direction.BACK_RIGHT);
				} else if (clientPlayer.getDirection() == Direction.FACE_RIGHT) {
					clientPlayer.setDirection(Direction.BACK_LEFT);
				}
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				//character will turn 1st if the character is not facing that side
				if (clientPlayer.getDirection() == Direction.BACK_LEFT) {
					clientPlayer.setDirection(Direction.FACE_LEFT);
				} else if (clientPlayer.getDirection() == Direction.BACK_RIGHT) {
					clientPlayer.setDirection(Direction.BACK_LEFT);
				} else if (clientPlayer.getDirection() == Direction.FACE_LEFT) {
					clientPlayer.setDirection(Direction.FACE_RIGHT);
				} else if (clientPlayer.getDirection() == Direction.FACE_RIGHT) {
					clientPlayer.setDirection(Direction.BACK_RIGHT);
				}
				firstViewDirection = Direction.BACK_LEFT;
				turnCounter -= 15;
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				//character will turn 1st if the character is not facing that side
				if (clientPlayer.getDirection() == Direction.BACK_LEFT) {
					clientPlayer.setDirection(Direction.BACK_RIGHT);
				} else if (clientPlayer.getDirection() == Direction.BACK_RIGHT) {
					clientPlayer.setDirection(Direction.FACE_RIGHT);
				} else if (clientPlayer.getDirection() == Direction.FACE_LEFT) {
					clientPlayer.setDirection(Direction.BACK_LEFT);
				} else if (clientPlayer.getDirection() == Direction.FACE_RIGHT) {
					clientPlayer.setDirection(Direction.FACE_LEFT);
				}
				turnCounter += 15;
				firstViewDirection = Direction.BACK_RIGHT;
			}
			// these are for changing game newDirection
			else if (e.getKeyCode() == KeyEvent.VK_E) {
				//turn the GUI to the left side
				//change the board(change the location of objects)
				Board oldBoard = clientPlayer.getLocation().getRoom()
						.getBoard();
				Board newBoard = new EmptyBoard();

				for (int i = 0; i < oldBoard.getWidth(); i++) {
					for (int j = 0; j < oldBoard.getHeight(); j++) {
						int offset = 1;//because the start position is (0,0) not(1,1), so there is an offset
						newBoard.squares[i][j] = oldBoard.squares[oldBoard
								.getHeight() - (j + offset)][i];
					}
				}
				clientPlayer.getLocation().getRoom().board = newBoard;
				//gameClient.getGame().getRooms().get(GameLauncher.ROOMINDEX).board = newBoard;

				//change the locations of player 
				Location newloc = new Location();
				newloc.setRoom(clientPlayer.getLocation().getRoom());

				int offset = 1;//because the start position is (0,0) not(1,1), so there is a offset

				newloc.setX(oldBoard.getHeight()
						- (clientPlayer.getLocation().getY() + offset));

				newloc.setY(clientPlayer.getLocation().getX());
				clientPlayer.setLocation(newloc);
				//let the player image turn left 
				rotate.turnPlayerImageLeft(clientPlayer);
				//let monster turn
				Room r = clientPlayer.getLocation().getRoom();//ga.getRooms().get(GameLauncher.ROOMINDEX);
				BoardSquare[][] bs = r.board.getSquares();
				for (int cellY = 0; cellY < 10; cellY++) {
					for (int cellX = 9; cellX >= 0; cellX--) {
						if (bs[cellY][cellX].getGameObjectOnSquare() != null) {
							if (bs[cellY][cellX].getGameObjectOnSquare() instanceof Monster) {
								rotate.turnMonsterImageLeft((Monster) bs[cellY][cellX]
										.getGameObjectOnSquare());
							}
							if (bs[cellY][cellX].getGameObjectOnSquare() instanceof Plant) {
								rotate.turnPlantImageLeft((Plant) bs[cellY][cellX]
										.getGameObjectOnSquare());
							}
							if (bs[cellY][cellX].getGameObjectOnSquare() instanceof Fence) {
								rotate.turnFenceImageLeft((Fence) bs[cellY][cellX]
										.getGameObjectOnSquare());
							}
						}
					}
				}
			} else if (e.getKeyCode() == KeyEvent.VK_Q) {
				//turn the gui to right side
				//change the board(change the locations of object)
				Board newBoard = new EmptyBoard();
				Board oldBoard = clientPlayer.getLocation().getRoom()
						.getBoard();
				for (int i = 0; i < oldBoard.getWidth(); i++) {
					for (int j = 0; j < oldBoard.getHeight(); j++) {
						int offset = 1;//because the start position is (0,0) not(1,1), so there is a offset
						newBoard.squares[i][j] = oldBoard.squares[j][oldBoard
								.getWidth() - (i + offset)];
					}
				}
				clientPlayer.getLocation().getRoom().board = newBoard;
				//gameClient.getGame().getRooms().get(GameLauncher.ROOMINDEX).board = newBoard;

				//change the locations of player 
				Location newloc = new Location();
				newloc.setRoom(clientPlayer.getLocation().getRoom());

				int offset = 1;//because the start position is (0,0) not(1,1), so there is a offset

				newloc.setX(clientPlayer.getLocation().getY());

				newloc.setY(oldBoard.getWidth()
						- (clientPlayer.getLocation().getX() + offset));

				clientPlayer.setLocation(newloc);
				//let the player image turn left 
				rotate.turnPlayerImageRight(clientPlayer);
				//let monster turn
				Room r = clientPlayer.getLocation().getRoom();//ga.getRooms().get(GameLauncher.ROOMINDEX);
				BoardSquare[][] bs = r.board.getSquares();
				for (int cellY = 0; cellY < 10; cellY++) {
					for (int cellX = 9; cellX >= 0; cellX--) {
						if (bs[cellY][cellX].getGameObjectOnSquare() != null) {
							if (bs[cellY][cellX].getGameObjectOnSquare() instanceof Monster) {
								turnMonsterImageRight((Monster) bs[cellY][cellX]
										.getGameObjectOnSquare());
							}
							if (bs[cellY][cellX].getGameObjectOnSquare() instanceof Plant) {
								rotate.turnPlantImageRight((Plant) bs[cellY][cellX]
										.getGameObjectOnSquare());
							}
							if (bs[cellY][cellX].getGameObjectOnSquare() instanceof Fence) {
								rotate.turnFenceImageRight((Fence) bs[cellY][cellX]
										.getGameObjectOnSquare());
							}
						}
					}
				}

			}
			//allows player to jump on the spot
			else if (e.getKeyCode() == KeyEvent.VK_J) {
				isJumping = false;
				//			jumpOffset = 0;
				//			jumpOffsetFirstView = 0;
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
			GameObject ObjectOfLoc = loc.getRoom().board.getSquares()[loc
					.getY()][loc.getX()].getGameObjectOnSquare();

			//pick up
			//check whether the item bag is full
			if (clientPlayer.getInventory().size() < clientPlayer.getMaxItems()) {
				//If you find a key, adds it to the inventory and removes from the board
				if (ObjectOfLoc instanceof Key) {
					clientPlayer.addToInventory(((Key) ObjectOfLoc));
					loc.getRoom().board.getSquares()[loc.getY()][loc.getX()]
							.setGameObjectOnSquare(null);
					gameClient.sendPickupItem((Item) ObjectOfLoc, new Location(
							loc.getRoom(), loc.getX(), loc.getY()),
							clientPlayer.getId());
				}
				//If you find a goodPotion, increases your health and removes it from the board
				if (ObjectOfLoc instanceof GoodPotion) {
					clientPlayer.setHealth(clientPlayer.getHealth()
							+ ((GoodPotion) ObjectOfLoc).getHealthHealAmount());
					loc.getRoom().board.getSquares()[loc.getY()][loc.getX()]
							.setGameObjectOnSquare(null);
				}
			}
			//If you find a RareCandy, increases your level and removes it from the board
			if (ObjectOfLoc instanceof RareCandy) {
				//level up
				if (clientPlayer.getPlayerLevel() == 1
						|| clientPlayer.getPlayerLevel() == 2) {
					clientPlayer.setPlayerLevel(clientPlayer.getPlayerLevel()
							+ ((RareCandy) ObjectOfLoc).level());
					//=================================================
					//draw gif here
					if (clientPlayer.getPlayerLevel() == 2) {
						shakeOffsetZero = LVL2_PRINT_OFFSET;//let the character print higher. cuz lvl3 picture is bigger
						panel.add(lvlupLabel_2);
						isLevelUpping = true;//stop key control and monster moving when the player is level uping
						final Timer timer = new Timer();
						TimerTask tt = new TimerTask() {
							@Override
							public void run() {
								timer.cancel();
								//here is the methods run after timer here 
								///=======================================
								panel.remove(lvlupLabel_2);
								isLevelUpping = false;//restore key control and monster moving after the player level up
								//========================================
							}
						};
						timer.schedule(tt, 2500);
					} else if (clientPlayer.getPlayerLevel() == 3) {
						shakeOffsetZero = LVL3_PRINT_OFFSET;//let the character print higher. cuz lvl3 picture is bigger
						panel.add(lvlupLabel_3);
						final Timer timer = new Timer();
						isLevelUpping = true;//stop key control and monster moving when the player is level uping
						TimerTask tt = new TimerTask() {
							@Override
							public void run() {
								timer.cancel();
								//here is the methods run after timer here 
								///=======================================
								panel.remove(lvlupLabel_3);
								isLevelUpping = false;//restore key control and monster moving after the player level up
								//========================================
							}
						};
						timer.schedule(tt, 4300);
					}
					//================================================
					clientPlayer.setAttack(clientPlayer.getAttack()
							* clientPlayer.getPlayerLevel());
					loc.getRoom().board.getSquares()[loc.getY()][loc.getX()]
							.setGameObjectOnSquare(null);
				} else {
					JOptionPane.showMessageDialog(getContentPane(),
							"You are already max evolutionary level(level 3).",
							"Message", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			//If you find a Door, checks your inventory for a Key
			//If you have a Key, compares the Key ID and Door ID for a match
			//If they match, allows you to enter a different room
			if (ObjectOfLoc instanceof Door) {
				for (Item items : clientPlayer.getInventory()) {
					if (items instanceof Key) {
						if (((Door) ObjectOfLoc).id() == items.id()) {

							Door theDoor = (Door) ObjectOfLoc;
							Room newRoom = gameClient.getGame().getRooms()
									.get(theDoor.getNextRoom());
							clientPlayer.setLocation(new Location(newRoom,
									theDoor.getNextRoomX(), theDoor
											.getNextRoomY()));
							clientPlayer.setDirection(Direction.FACE_LEFT);

							//gameClient.sendPlayerMoveUpdateToServer();
							//Room nowRoom = clientPlayer.getLocation().getRoom();

							/*if (nowRoom.level == theDoor.linkFrom) {
								//for change room 
								//1. i change change board
								//2. i change the location of player
								//GameLauncher.ROOMINDEX = theDoor.linkTo - 1;
								clientPlayer.getLocation().setRoom();

							} else if (nowRoom.level == theDoor.linkFrom + 1) {
								System.out.println("this door goes to level 1");
								GameLauncher.ROOMINDEX = theDoor.linkTo - 2;
								clientPlayer.getLocation().setRoom(
										gameClient.getGame().getRooms()
										.get(GameLauncher.ROOMINDEX));

							}*/
						}
					}
				}
			}
			gameClient.sendPlayerUpdate();
			gameClient.sendPlayerMoveUpdateToServer();
			repaint();
		}
	}

	/**
	 * let the monster go through
	 * @param monster
	 */
	public boolean letMonsterMove(Monster monster, int cellY, int cellX) {
		BoardSquare[][] square = gameClient.getClientPlayer().getLocation()
				.getRoom().getBoard().getSquares();
		if (monster.getDirection() == Direction.FACE_LEFT) {
			if ((cellX - 1) >= 0) {
				//if add another kind of can't moved square, please also add it here(add)
				if (square[cellY][cellX - 1].getGameObjectOnSquare() == null
						&& !(gameClient.getClientPlayer().getLocation().getX() == cellX - 1 && gameClient
								.getClientPlayer().getLocation().getY() == (cellY))) {
					square[cellY][cellX - 1].setGameObjectOnSquare(monster);
					square[cellY][cellX].setGameObjectOnSquare(null);
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else if (monster.getDirection() == Direction.FACE_RIGHT) {
			if ((cellY + 1) < 10) {
				if (square[cellY + 1][cellX].getGameObjectOnSquare() == null
						&& !(gameClient.getClientPlayer().getLocation().getX() == cellX && gameClient
								.getClientPlayer().getLocation().getY() == (cellY + 1))) {
					square[cellY + 1][cellX].setGameObjectOnSquare(monster);
					square[cellY][cellX].setGameObjectOnSquare(null);
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else if (monster.getDirection() == Direction.BACK_RIGHT) {
			if ((cellX + 1) < 10) {
				//if add another kind of can't moved square, please also add it here(add)
				if ((square[cellY][cellX + 1].getGameObjectOnSquare() == null)
						&& !(gameClient.getClientPlayer().getLocation().getX() == cellX + 1 && gameClient
								.getClientPlayer().getLocation().getY() == (cellY))) {
					square[cellY + 1][cellX].setGameObjectOnSquare(monster);
					square[cellY][cellX].setGameObjectOnSquare(null);
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}

		} else if (monster.getDirection() == Direction.BACK_LEFT) {
			if ((cellY - 1) >= 0) {
				if (square[cellY - 1][cellX].getGameObjectOnSquare() == null
						&& !(gameClient.getClientPlayer().getLocation().getX() == cellX && gameClient
								.getClientPlayer().getLocation().getY() == (cellY - 1))) {
					square[cellY - 1][cellX].setGameObjectOnSquare(monster);
					square[cellY][cellX].setGameObjectOnSquare(null);
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * let the monster image turn around
	 * @param monster
	 */
	public void turnMonsterAroundAndMove(Monster monster, int cellY, int cellX) {
		BoardSquare[][] square = gameClient.getClientPlayer().getLocation()
				.getRoom().getBoard().getSquares();
		if (monster.getDirection() == Direction.FACE_LEFT) {
			monster.setDirection(Direction.BACK_RIGHT);
			if ((cellX + 1) < 10) {
				//if add another kind of can't moved square, please also add it here(add)
				if ((square[cellY][cellX + 1].getGameObjectOnSquare() == null)
						&& !(gameClient.getClientPlayer().getLocation().getX() == cellX + 1 && gameClient
								.getClientPlayer().getLocation().getY() == (cellY))) {
					square[cellY][cellX + 1].setGameObjectOnSquare(monster);
					square[cellY][cellX].setGameObjectOnSquare(null);
				}
			}
		} else if (monster.getDirection() == Direction.FACE_RIGHT) {
			monster.setDirection(Direction.BACK_LEFT);
			if ((cellY - 1) >= 0) {
				if (square[cellY - 1][cellX].getGameObjectOnSquare() == null
						&& !(gameClient.getClientPlayer().getLocation().getX() == cellX && gameClient
								.getClientPlayer().getLocation().getY() == (cellY - 1))) {
					square[cellY - 1][cellX].setGameObjectOnSquare(monster);
					square[cellY][cellX].setGameObjectOnSquare(null);
				}
			}
		} else if (monster.getDirection() == Direction.BACK_RIGHT) {
			monster.setDirection(Direction.FACE_LEFT);
			if ((cellX - 1) >= 0) {
				//if add another kind of can't moved square, please also add it here(add)
				if (square[cellY][cellX - 1].getGameObjectOnSquare() == null
						&& !(gameClient.getClientPlayer().getLocation().getX() == cellX - 1 && gameClient
								.getClientPlayer().getLocation().getY() == (cellY))) {
					square[cellY][cellX - 1].setGameObjectOnSquare(monster);
					square[cellY][cellX].setGameObjectOnSquare(null);
				}
			}
		} else if (monster.getDirection() == Direction.BACK_LEFT) {
			monster.setDirection(Direction.FACE_RIGHT);
			if ((cellY + 1) < 10) {
				if (square[cellY + 1][cellX].getGameObjectOnSquare() == null
						&& !(gameClient.getClientPlayer().getLocation().getX() == cellX && gameClient
								.getClientPlayer().getLocation().getY() == (cellY + 1))) {
					square[cellY + 1][cellX].setGameObjectOnSquare(monster);
					square[cellY][cellX].setGameObjectOnSquare(null);
				}
			}
		}
	}

	/**
	 * let the monster image turn right
	 * @param monster
	 */
	public void turnMonsterImageRight(Monster monster) {
		if (monster.getDirection() == Direction.FACE_LEFT) {
			monster.setDirection(Direction.FACE_RIGHT);
		} else if (monster.getDirection() == Direction.FACE_RIGHT) {
			monster.setDirection(Direction.BACK_RIGHT);
		} else if (monster.getDirection() == Direction.BACK_RIGHT) {
			monster.setDirection(Direction.BACK_LEFT);
		} else if (monster.getDirection() == Direction.BACK_LEFT) {
			monster.setDirection(Direction.FACE_LEFT);
		}
	}

	public void dropDialog() {
		Player clientPlayer = gameClient.getClientPlayer();
		dropBox = new JDialog();
		dropBox.setTitle("Which item do you want to drop?");
		dropBox.setBackground(Color.GRAY);
		dropBox.setLayout(null);
		dropBox.setBounds(100, 100, 400, 300);
		dropBox.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		//add cancel button
		JButton cancel = new JButton();
		cancel.setText("Cancel");
		cancel.setBounds(170, 200, 100, 40);
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dropBox.dispose();
			}
		});
		dropBox.add(cancel);
		//if there is 1 item in bag
		if (clientPlayer.getInventory().size() > 0) {
			JButton inventory1 = new JButton();
			inventory1.setIcon(clientPlayer.getInventory().get(0)
					.getSpriteImage());
			inventory1.setBounds(0, 0, 100, 100);
			dropBox.add(inventory1);
			inventory1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dropIventory(0);
					dropBox.dispose();
				}
			});
		}
		//if there are 2 items in bag
		if (clientPlayer.getInventory().size() > 1) {
			JButton inventory2 = new JButton();
			inventory2.setIcon(clientPlayer.getInventory().get(1)
					.getSpriteImage());
			inventory2.setBounds(150, 0, 100, 100);
			dropBox.add(inventory2);
			inventory2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dropIventory(1);
					dropBox.dispose();
				}
			});
		}
		//if there are 3 items in bag
		if (clientPlayer.getInventory().size() > 2) {
			JButton inventory3 = new JButton();
			inventory3.setIcon(clientPlayer.getInventory().get(2)
					.getSpriteImage());
			inventory3.setBounds(300, 0, 100, 100);
			dropBox.add(inventory3);
			inventory3.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dropIventory(2);
					dropBox.dispose();
				}
			});
		}
		//if there are 4 items in bag
		if (clientPlayer.getInventory().size() > 3) {
			JButton inventory4 = new JButton();
			inventory4.setIcon(clientPlayer.getInventory().get(3)
					.getSpriteImage());
			inventory4.setBounds(0, 100, 100, 100);
			dropBox.add(inventory4);
			inventory4.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dropIventory(3);
					dropBox.dispose();
				}
			});
		}
		//if there are 5 items in bag
		if (clientPlayer.getInventory().size() > 4) {
			JButton inventory5 = new JButton();
			inventory5.setIcon(clientPlayer.getInventory().get(4)
					.getSpriteImage());
			inventory5.setBounds(150, 100, 100, 100);
			dropBox.add(inventory5);
			inventory5.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dropIventory(4);
					dropBox.dispose();
				}
			});
		}
		//if there are 6 items in bag
		if (clientPlayer.getInventory().size() > 5) {
			JButton inventory6 = new JButton();
			inventory6.setIcon(clientPlayer.getInventory().get(5)
					.getSpriteImage());
			inventory6.setBounds(300, 100, 100, 100);
			dropBox.add(inventory6);
			inventory6.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dropIventory(5);
					dropBox.dispose();
				}
			});
		}

		dropBox.setVisible(true);
		requestFocus();
	}

	/**
	 *@author Sushant Balajee,Donald Tang,Wang Zhen
	 */
	public void fightDialog(final Location mosterLocation) {
		isFighting = true;
		Player clientPlayer = gameClient.getClientPlayer();
		final Location loc = clientPlayer.getLocation();

		GameObject ObjectOfLoc = loc.getRoom().board.getSquares()[mosterLocation
				.getY()][mosterLocation.getX()].getGameObjectOnSquare();

		JButton att = new JButton("Attack");
		JButton run = new JButton("Run Away");

		att.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fightBox.dispose();
				///update label
				loadLabels();
				//fight giflabel 
				panel.add(attackLabel);
				//add a timer 
				final Timer timer = new Timer();
				TimerTask tt = new TimerTask() {

					@Override
					public void run() {
						timer.cancel();
						//here is the methods run after timer here 
						///=======================================
						panel.remove(attackLabel);
						fight(mosterLocation);
						//========================================
					}
				};
				timer.schedule(tt, 2000);
			}
		});
		run.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fightBox.dispose();
				isFighting = false;
			}
		});

		att.setLocation(10, 90);
		att.setSize(att.getPreferredSize());
		run.setLocation(90, 90);
		run.setSize(run.getPreferredSize());

		JLabel type = new JLabel("Monster Type: "
				+ ((Monster) ObjectOfLoc).getName());
		JLabel attack = new JLabel("Monster Attack: "
				+ ((Monster) ObjectOfLoc).attack());
		JLabel health = new JLabel("Monster Health: "
				+ ((Monster) ObjectOfLoc).getHealth());
		JLabel message = new JLabel("Fighting one round?");

		type.setLocation(10, 10);
		attack.setLocation(10, 30);
		health.setLocation(10, 50);
		message.setLocation(10, 70);

		type.setSize(type.getPreferredSize());
		attack.setSize(type.getPreferredSize());
		health.setSize(type.getPreferredSize());
		message.setSize(type.getPreferredSize());

		fightBox = new JDialog();
		fightBox.setTitle("An enemy!");
		fightBox.setBackground(Color.GRAY);
		fightBox.setLayout(null);
		fightBox.setLocation(600, 300);
		fightBox.setSize(200, 170);

		fightBox.add(type);
		fightBox.add(attack);
		fightBox.add(health);
		fightBox.add(message);
		fightBox.add(att);
		fightBox.add(run);
		fightBox.setVisible(true);

	}

	/**
	 * @author Sushant Balajee,Donald Tang,Wang Zhen
	 */
	public void fight(Location mosterLocation) {
		Player clientPlayer = gameClient.getClientPlayer();
		//		Location loc = clientPlayer.getLocation();

		GameObject ObjectOfLoc = mosterLocation.getRoom().board.getSquares()[mosterLocation
				.getY()][mosterLocation.getX()].getGameObjectOnSquare();
		// COMPILER ERROR - HERE BELOW. Make sure monster still on object...
		int damage = ((Monster) ObjectOfLoc).attack();

		//Monster attacks first
		clientPlayer.setHealth(clientPlayer.getHealth() - damage);
		//Player attacks second
		((Monster) ObjectOfLoc).setHealth(((Monster) ObjectOfLoc).getHealth()
				- clientPlayer.getAttack());

		//		fightBox.dispose();

		//if the player dies, it will show a gif and a message dialog
		if (clientPlayer.isDead()) {
			panel.add(dieLabel);
			JOptionPane.showMessageDialog(null, " You Died ");
			System.exit(0);
		} else if (((Monster) ObjectOfLoc).isDead()) {
			JOptionPane.showMessageDialog(null, " You Won! \n" + " You lost "
					+ damage + " health \n" + " You gained " + damage
					+ " attack");

			//increases the player attack if they win
			clientPlayer.setAttack(clientPlayer.getAttack() + damage);

			//removes the monster from the board
			mosterLocation.getRoom().board.getSquares()[mosterLocation.getY()][mosterLocation
					.getX()].setGameObjectOnSquare(null);
		} else {
			JOptionPane.showMessageDialog(null,
					"Monster is still alive, but now he only has "
							+ ((Monster) ObjectOfLoc).getHealth() + "health");
		}
		gameClient.sendPlayerUpdate();
		isFighting = false;
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
				List<InetAddress> serverAddressList = gameClient
						.getServerList();
				InetAddress serverAddress = ServerSelectDialog.Chooser(this,
						serverAddressList);
				if (serverAddress == null) {
					return;
				}
				try {
					gameClient.connect(serverAddress);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(this,
							"Game Client was unable to initalise.\n"
									+ " Make sure that you have created and\n"
									+ " connected and the server and the\n"
									+ "ports are unblocked.", "ERROR",
							JOptionPane.ERROR_MESSAGE);

					frameState = FRAME_STATE.STANDBY;
					return;
				}

				// At this point in code, client is connected to server successfully.
				// Now we need to let the user enter a username (and validate that it
				// isn't already taken by another user) and pick a character
				String playerUsername = null;

				while (playerUsername == null) {
					playerUsername = JOptionPane.showInputDialog(this,
							"Input your Username?");

					if (playerUsername == null || playerUsername.length() < 1) {
						playerUsername = null;
						JOptionPane.showMessageDialog(this,
								"You need to enter a user name!", "ERROR",
								JOptionPane.ERROR_MESSAGE);
						continue;
					}

					if (!gameClient.isUsernameAlreadyTaken(playerUsername)) {
						JOptionPane
								.showMessageDialog(
										this,
										"Username Already in use. Please enter another one!",
										"ERROR", JOptionPane.ERROR_MESSAGE);
						playerUsername = null;
						continue;
					}
				}

				ArrayList<Player> savedFilePlayers = gameClient
						.sendOnClientCharacterSelect();
				System.out.println(savedFilePlayers);

				if (!savedFilePlayers.isEmpty()) {
					Player choosenClientPlayer = CharacterSelectDialog_LoadFile
							.Chooser(this, savedFilePlayers);
					gameClient.sendLoadedPlayerToSever(playerUsername,
							choosenClientPlayer);
				} else {

					Avatar playerAvatar = CharacterSelectDialog.Chooser(this);

					// Created a player for the client and send it the server to be passed to other clients
					gameClient.sendNewPlayerToServer(playerUsername,
							playerAvatar);
				}
				// redraw the board
				//frameState = FRAME_STATE.GAME_RUNNING;
				//add all jlabel after picking character
				//loadLabels();
				//panel.add(headPictureLabel);
				//panel.add(bgHeadViewLabel);
				this.repaint();
				addKeyListener(this);
			} else if (menuItem.getText().equals("Exit")) {
				System.exit(0);
			}
		} else if (source instanceof JButton) {
			if (inputMessageField.getText().length() > 0) {
				sendMessage();
			}
			this.requestFocus();
		}
	}

	@Override
	public void onGameUpdated() {
		frameState = FRAME_STATE.GAME_RUNNING;
		repaint();
	}

	@Override
	public void windowActivated(WindowEvent arg0) {

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		gameClient.disconnect();
		System.exit(0);
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
	}

	@Override
	public void onMessageRecieved(String playerName, String message) {
		outputMessageToTextArea(playerName, message);
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

/**
 * this way to print object in 1st view will print closer stuff 1st
 */
////#print objects
//			double nowDrawLinePrintObject = viewHight;//the height of line now draw(it is the bot of the frame at start)
//			double previouDrawLinePrintObject = viewHight;
//			double previouX0PrintObject = midOfView - squareWidth / 2;//the line in the bot of the frame
//			double previouY0PrintObject = viewHight;
//			double previouX1PrintObject = midOfView + squareWidth / 2;//the line in the bot of the frame
//			double previouY1PrintObject = viewHight;
//			int checkLocationXPrintObject = clientPlayer.getLocation().getX();
//			int checkLocationYPrintObject = clientPlayer.getLocation().getY();
//			for (int i = 0; i < numSquaresFace + 1; i++) {
//				//draw face square
//				double nowWidthOfSquare = squareWidth * Math.pow(scaleY, i + 1);
//				double nowStartX = midOfView - nowWidthOfSquare / 2;
//				//add points for drawing Polygon
//				int[] xPoint = new int[4];
//				int[] yPoint = new int[4];
//				//draw Polygon
//				xPoint[0] = (int) previouX0PrintObject;
//				yPoint[0] = (int) previouY0PrintObject - jumpOffsetFirstView;
//				xPoint[1] = (int) previouX1PrintObject;
//				yPoint[1] = (int) previouY1PrintObject - jumpOffsetFirstView;
//				xPoint[2] = (int) (nowStartX + nowWidthOfSquare);
//				yPoint[2] = (int) (nowDrawLinePrintObject - squareHeigh
//						* Math.pow(scaleY, i + 1) - jumpOffsetFirstView);
//				xPoint[3] = (int) nowStartX;
//				yPoint[3] = (int) (nowDrawLinePrintObject - squareHeigh
//						* Math.pow(scaleY, i + 1) - jumpOffsetFirstView);
//
//				//draw left squares
//				for (int j = 0; j < numSquaresLeft; j++) {
//					int[] xPointLeft = new int[4];
//					int[] yPointLeft = new int[4];
//					//draw Polygon
//					int previouWidthOfSquare = (int) (previouX1PrintObject - previouX0PrintObject);
//					xPointLeft[0] = (int) (previouX0PrintObject - j
//							* previouWidthOfSquare);
//					yPointLeft[0] = (int) (previouY0PrintObject - jumpOffsetFirstView);
//					xPointLeft[1] = (int) (previouX1PrintObject - j
//							* previouWidthOfSquare);
//					yPointLeft[1] = (int) (previouY1PrintObject - jumpOffsetFirstView);
//					xPointLeft[2] = (int) (nowStartX + nowWidthOfSquare - j
//							* nowWidthOfSquare);
//					yPointLeft[2] = (int) (nowDrawLinePrintObject - squareHeigh
//							* Math.pow(scaleY, i + 1) - jumpOffsetFirstView);
//					xPointLeft[3] = (int) (nowStartX - j * nowWidthOfSquare);
//					yPointLeft[3] = (int) (nowDrawLinePrintObject - squareHeigh
//							* Math.pow(scaleY, i + 1) - jumpOffsetFirstView);
//
//					//=====================================================================================
//					//print the object left on this location====================================================
//					Location nextLoc = nextSquareLocation(clientPlayer, i);
//
//					Game ga = gameClient.getGame();
//					Room r = clientPlayer.getLocation().getRoom();
//					BoardSquare[][] bs = r.board.getSquares();
//
//					int locX = nextLoc.getX();
//					int locY = nextLoc.getY();
//					if (clientPlayer.getDirection() == Direction.FACE_RIGHT) {
//						locX = nextLoc.getX() + j;
//					} else if (clientPlayer.getDirection() == Direction.FACE_LEFT) {
//						locY = nextLoc.getY() + j;
//					} else if (clientPlayer.getDirection() == Direction.BACK_LEFT) {
//						locX = nextLoc.getX() - j;
//					} else if (clientPlayer.getDirection() == Direction.BACK_RIGHT) {
//						locY = nextLoc.getY() - j;
//					}
//					
//					//diff direction has diff order to print ,this is for making sure the closer picture cover far
//					if (clientPlayer.getDirection() == Direction.FACE_RIGHT) {
//						//check whether next square is out of board
//						if (locX != -1 && locX != 10 && locY != -1
//								&& locY != 10) {
//							if (bs[locY][locX].getGameObjectOnSquare() != null) {
//
//								if (bs[locY][locX].getGameObjectOnSquare() instanceof Tree) {
//									int width = (xPoint[1] - xPoint[0])
//											* TREE_SCALE_FIRST_VIEW;
//									int height = width;
//									int midPointX = xPointLeft[0]
//											+ (xPointLeft[1] - xPointLeft[0])
//											/ 2;
//									int drawStartX = midPointX - width / 2;
//									int drawStartY = yPointLeft[0] - (height);
//									g.drawImage(bs[locY][locX]
//											.getGameObjectOnSquare()
//											.getSpriteImage().getImage(),
//											drawStartX, drawStartY, width,
//											height, null);
//								} else {
//									int height = (xPointLeft[2] - xPointLeft[3]);
//									int width = height;
//									int drawStartX = xPointLeft[3];
//									int drawStartY = yPointLeft[0]
//											- ((xPointLeft[2] - xPointLeft[3]));
//									g.drawImage(bs[locY][locX]
//											.getGameObjectOnSquare()
//											.getSpriteImage().getImage(),
//											drawStartX, drawStartY, width,
//											height, null);
//								}
//							}
//						}
//					}
//					//===============================================================================
//				}
//
//				//draw right squares
//				for (int j = 0; j < numSquaresRight; j++) {
//					int[] xPointLeft = new int[4];
//					int[] yPointLeft = new int[4];
//					//draw Polygon
//					int previouWidthOfSquare = (int) (previouX1PrintObject - previouX0PrintObject);
//					xPointLeft[0] = (int) (previouX0PrintObject + j
//							* previouWidthOfSquare);
//					yPointLeft[0] = (int) (previouY0PrintObject - jumpOffsetFirstView);
//					xPointLeft[1] = (int) (previouX1PrintObject + j
//							* previouWidthOfSquare);
//					yPointLeft[1] = (int) (previouY1PrintObject - jumpOffsetFirstView);
//					xPointLeft[2] = (int) (nowStartX + nowWidthOfSquare + j
//							* nowWidthOfSquare);
//					yPointLeft[2] = (int) (nowDrawLinePrintObject - squareHeigh
//							* Math.pow(scaleY, i + 1) - jumpOffsetFirstView);
//					xPointLeft[3] = (int) (nowStartX + j * nowWidthOfSquare);
//					yPointLeft[3] = (int) (nowDrawLinePrintObject - squareHeigh
//							* Math.pow(scaleY, i + 1) - jumpOffsetFirstView);
//
//				}
//
//				//				g.drawImage(new ImageIcon("src/firstviewgrass.png").getImage(),xPoint[0],yPoint[0] , xPoint[1], yPoint[1], xPoint[2], yPoint[2], xPoint[3],yPoint[3],null);
//				//				g.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer)
//				//				System.out.println(xPoint[0]+","+yPoint[0]);
//
//				//updata previou
//				previouX0PrintObject = nowStartX;
//				previouY0PrintObject = nowDrawLinePrintObject - squareHeigh
//						* Math.pow(scaleY, i + 1);
//				previouX1PrintObject = nowStartX + nowWidthOfSquare;
//				previouY1PrintObject = nowDrawLinePrintObject - squareHeigh
//						* Math.pow(scaleY, i + 1);
//				previouDrawLinePrintObject = nowDrawLinePrintObject;
//				nowDrawLinePrintObject = nowDrawLinePrintObject - squareHeigh
//						* Math.pow(scaleY, i + 1);
//
//				//=====================================================================================
//				//print the object on this location====================================================
//				Location nextLoc = nextSquareLocation(clientPlayer, i);
//
//				Game ga = gameClient.getGame();
//				Room r = clientPlayer.getLocation().getRoom();
//				BoardSquare[][] bs = r.board.getSquares();
//
//				//check whether next square is out of board
//				if (nextLoc.getX() != -1 && nextLoc.getX() != 10
//						&& nextLoc.getY() != -1 && nextLoc.getY() != 10) {
//					if (bs[nextLoc.getY()][nextLoc.getX()]
//							.getGameObjectOnSquare() != null) {
//
//						if (bs[nextLoc.getY()][nextLoc.getX()]
//								.getGameObjectOnSquare() instanceof Tree) {
//							int width = (xPoint[1] - xPoint[0])
//									* TREE_SCALE_FIRST_VIEW;
//							int height = width;
//							int midPointX = xPoint[0] + (xPoint[1] - xPoint[0])
//									/ 2;
//							int drawStartX = midPointX - width / 2;
//							int drawStartY = yPoint[0] - (height);
//							g.drawImage(bs[nextLoc.getY()][nextLoc.getX()]
//									.getGameObjectOnSquare().getSpriteImage()
//									.getImage(), drawStartX, drawStartY, width,
//									height, null);
//						} else {
//							int height = (xPoint[2] - xPoint[3]);
//							int width = height;
//							int drawStartX = xPoint[3];
//							int drawStartY = yPoint[0]
//									- ((xPoint[2] - xPoint[3]));
//							g.drawImage(bs[nextLoc.getY()][nextLoc.getX()]
//									.getGameObjectOnSquare().getSpriteImage()
//									.getImage(), drawStartX, drawStartY, width,
//									height, null);
//							//these are set size as (yPoint[0] - yPoint[3]), this print smaller picture---------------------------------------
//							//							int height = yPoint[0] - yPoint[3];
//							//							int width = height;
//							//							int midPointX = xPoint[0]+(xPoint[1]-xPoint[0])/2;
//							//							int drawStartX = midPointX-width/2;
//							//							int drawStartY = yPoint[3];
//							//							g.drawImage(bs[nextLoc.getY()][nextLoc.getX()]
//							//									.getGameObjectOnSquare().getSpriteImage()
//							//									.getImage(), drawStartX,drawStartY,width,height, null);
//							//----------------------------------------------------------------------------------
//						}
//					}
//				}
//				//===============================================================================
//			}
