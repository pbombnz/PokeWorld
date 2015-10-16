package ui;

/**
 * @author Wang Zhen
 * 
 * These 2 classes(GameFrame and GamePanelFrame)  include all methods,fileds of game gui and some game logic
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
 * Control:
 * Q-rotate game left, E-rotate game right
 * W-north S-south A-west D-east J- jump
 * Up-go  ,Down-turn around ,Left-turn left,Right-turn right
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
public abstract class GameFrame extends JFrame implements KeyListener,
		ActionListener, WindowListener, GameClientListener {
	// The Emum has holds states for the JFrame so we know what to draw and when
	// for instance we draw
	protected static enum FRAME_STATE {
		STANDBY, GAME_RUNNING
	};

	// The Size of the Frame
	protected static final int FRAME_HEIGHT = 610;
	protected static final int FULL_FRAME_WIDTH = 1400;
	// The size of the tiles when they are displayed
	protected static final int TILE_WIDTH = 64;
	protected static final int TILE_HEIGHT = 64;

	protected FRAME_STATE frameState = FRAME_STATE.STANDBY;

	protected GameClient gameClient = new GameClient();
	private Rotate rotate = new Rotate();

	public int jumpOffset = 0;
	//these fileds are for helping make player shake even if they do not move
	public int shakeOffsetZero = 0;//for changing the print postion on screen(some character picture is bigger, so need to be printed higher).the smaller the value is , the higher the character print
	public int shakeOffset = shakeOffsetZero;//the player will keep shake when they are standing in one place
	public int shakeTimer = 0;//using calculation number as timer. the shakeoffset will change when the timer reaches the timerLimit
	public final int SHAKE_TIMER_LIMIT = 200;// the shakeoffset will change when it reaches the timer limit

	//level up
	protected boolean isLevelUpping = false;//stop key control and monster moving when the player is level uping
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
	protected boolean isFighting = false;//stop key control and monster moving when the player is level uping

	//add explored time
	protected String startTime = null;
	public JLabel timeLabel = new JLabel();

	public List<JLabel> itemJLabels = new ArrayList<JLabel>();

	public boolean componentsAdded = false;//only add components 1 time on panel. if componentsAdded, wont load again

	//drop invertory
	public JButton dropButton = new JButton();
	private JDialog dropBox;//items choosing dialog when press drop button
	public JButton sendMessageButton = new JButton();
	public int jumpTimeCounter = 0;

	protected boolean isJumping = false;//whether the player is jumping now

	//changing weather
	public JButton rainyButton = new JButton();
	public JButton sunnyButton = new JButton();
	public JButton dayButton = new JButton();
	public JButton nightButton = new JButton();
	public boolean isRainning = false;

	//monster wonder around
	protected long lastMovedtime = 0;//the time that monster  moved in last wonder around unit
	protected boolean moved = false;
	protected List<Monster> monstersChanged = new ArrayList<Monster>();//monster's location already be changed in one turn in wander around

	//choose day or night
	protected boolean isDay = true;
	protected final int sightRange = 3;//the sight range in night

	private JTextArea textOutputArea;
	private JTextArea controlTipArea;
	private JTextField inputMessageField;

	///================================================
	//the file below is for drawing 1st view
	//assume the is width of 1 square is 300 in 1st view
	protected static final int FRAME_WIDTH = 800;//the width of the left backgroud picture
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
	protected int jumpOffsetFirstView = 0;
	protected int turnOffset = 750;
	protected int turnCounter = 0;
	protected Direction firstViewDirection = Direction.BACK_LEFT;
	protected JLabel lvlupLabel_2;
	protected JLabel lvlupLabel_3;
	private final int TEXT_OUTPUT_ROWS = 5;
	private final int SEARCH_COLS = 15;

	///==================================

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
		//make the requestfocus
		textOutputArea.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				requestFocus();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				requestFocus();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				requestFocus();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				requestFocus();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				requestFocus();
			}
		});

		/**
		 * here is the control tips. we move this to "about". 
		 * if like is or think it is necesary,just uncomment 
		 */
		/*
		//add control tips
		controlTipArea = new JTextArea(TEXT_OUTPUT_ROWS, 0);
		controlTipArea.setLineWrap(true);
		controlTipArea.setWrapStyleWord(true); // pretty line wrap.
		controlTipArea.setEditable(false);
		JScrollPane controlTipScroll = new JScrollPane(controlTipArea);
		controlTipScroll.setToolTipText("Control tips");
		// these two lines make the JScrollPane always scroll to the bottom when
		// text is appended to the JTextArea.
		DefaultCaret controlTipCaret = (DefaultCaret) controlTipArea.getCaret();
		controlTipCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		controlTipScroll.setBounds(150, 0, 200, 80);
		panel.add(controlTipScroll);
		controlTipArea.setText("");
		controlTipArea
				.append("Control:\n Q-rotate game left, E-rotate game right J- jump\n W-north S-south A-west D-east\n Up-go  ,Down-turn around ,Left-turn left,Right-turn right");
		//make the requestfocus
		controlTipArea.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				requestFocus();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				requestFocus();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				requestFocus();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				requestFocus();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				requestFocus();
			}
		});
		*/

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

	/**
	 * load JLabels.
	 */
	public void loadLabels() {
		// Get Client Player from Client Connection
		Player clientPlayer = gameClient.getClientPlayer();

		//load labels of player information
		//create background label
		JLabel bgHeadViewLabel = new JLabel(new ImageIcon("src/bgHeadView.png"));
		//load head picture
		JLabel headPictureLabel = null;
		//		System.out.println(clientPlayer);
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
		Evolution clientPlayerCurrentEvolution = clientPlayer.getAvatar()
				.getCurrentEvolution(clientPlayer.getPlayerLevel());
		//create labels
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
		//set position
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

	/**
	 * print player's information on the panel
	 * it will print name,type,health,attack,level,control tips,exployed time
	 * @param player
	 */
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

	/**
	 * transfer long(runningTime) to a string
	 * @param runningTime
	 * @return
	 */
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

	/**
	 * when the shake timer get shaketimelimit, change the shakelimit,
	 * then the character will be print lower or higher
	 */
	protected void changeShakeLimit() {
		if (shakeOffset == shakeOffsetZero) {
			shakeOffset = shakeOffsetZero + 5;
		} else {
			shakeOffset = shakeOffsetZero;
		}
	}

	//------------------------------------------------------------------------------------------------

	/**
	 * check whether the location(x,y) is in the players sigh range.
	 * @param player
	 * @param y
	 * @param x
	 * @return
	 */
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

	/**
	 * print player's inventory in bag
	 * @param player
	 * @param g
	 */
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
		}
		repaint();
	}

	/**
	 * After player walk steps(int) step on currentlly face side,get the location
	 * @param player
	 * @param steps
	 * @return
	 */
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

	/**
	 * type the key
	 */
	@Override
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * press the key, start jump
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_J) {
			isJumping = true;
		}
	}

	/**
	 * check whether the place can be moved
	 */
	private boolean canMove(int x, int y, Player player) {
		BoardSquare[][] sq = player.getLocation().getRoom().board.getSquares();
		//if the location is out of board,cannot move
		if (x > 9 || x < 0 || y < 0 || y > 9) {
			return false;
		}
		//if the location is tree,cannot move
		if (sq[y][x].getGameObjectOnSquare() instanceof Tree
				|| sq[y][x].getGameObjectOnSquare() instanceof Fence) {
			return false;
		}
		//if a player is at that location, cannot move
		for (Player otherPlayer : gameClient.getGame().getPlayers()) {
			Location otherLoc = otherPlayer.getLocation();
			if (otherLoc.getX() == x
					&& otherLoc.getY() == y
					&& otherLoc.getRoom().getName()
							.equals(player.getLocation().getRoom().getName())) {
				return false;
			}
		}
		//if a monster is at that location, cannot move
		//(for teamwork ,change here if add new kinds of Monster)
		if (sq[y][x].getGameObjectOnSquare() instanceof Mewtwo
				|| sq[y][x].getGameObjectOnSquare() instanceof Rattata
				|| sq[y][x].getGameObjectOnSquare() instanceof Rhydon
				|| sq[y][x].getGameObjectOnSquare() instanceof Zubat) {
			fightDialog(new Location(player.getLocation().getRoom(), x, y));
			return false;
		}
		//if a MagicCircle is at that location, cannot move
		if (sq[y][x].getGameObjectOnSquare() instanceof MagicCircle) {
			MagicCircle mc = (MagicCircle) (sq[y][x].getGameObjectOnSquare());
			player.setLocation(new Location(player.getLocation().getRoom(), mc
					.getTeleportX(), mc.getTeleportY()));
			return false;
		}
		return true;
	}

	/**
	 * release key
	 * Q-rotate game left, E-rotate game right
	 * W-north S-south A-west D-east J- jump
	 * Up-go  ,Down-turn around ,Left-turn left,Right-turn right
	 *
	 * This gui has two keyboard control system:The 1st system is for using in 3rd person view gui.
	 * (user can use WSAD to move. W-north S-south A-west D-east)
	 * The 2nd system is for using on 1st person view gui.I create this control system for easily playing in 1st person view.
	 * (user can use Up,Down,Left,Right to move. Up-go,Down-turn around,Left-turn left,Right-turn right)
	 * And user can also use "J" to jump in both 2 views.
	 */
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

			//If you find a key, adds it to the inventory and removes from the board
			if (ObjectOfLoc instanceof Key) {
				//check whether the item bag is full
				if (clientPlayer.getInventory().size() < clientPlayer
						.getMaxItems()) {
					clientPlayer.addToInventory(((Key) ObjectOfLoc));
					loc.getRoom().board.getSquares()[loc.getY()][loc.getX()]
							.setGameObjectOnSquare(null);
					gameClient.sendPickupItem((Item) ObjectOfLoc, new Location(
							loc.getRoom(), loc.getX(), loc.getY()),
							clientPlayer.getId());
				} else {
					JOptionPane
							.showMessageDialog(
									getContentPane(),
									"Sorry, you cannot pick this item up.Your bag is full.",
									"No space in your bag!",
									JOptionPane.INFORMATION_MESSAGE);
				}
			}
			//If you find a goodPotion, increases your health and removes it from the board
			if (ObjectOfLoc instanceof GoodPotion) {
				//check whether the item bag is full
				clientPlayer.setHealth(clientPlayer.getHealth()
						+ ((GoodPotion) ObjectOfLoc).getHealthHealAmount());
				loc.getRoom().board.getSquares()[loc.getY()][loc.getX()]
						.setGameObjectOnSquare(null);

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
					square[cellY][cellX + 1].setGameObjectOnSquare(monster);
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

	/**
	 * draw dropDialog after pressing drop button
	 */
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
	 * draw dropDialog after pressing drop button
	 * @author Sushant Balajee,Donald Tang,Wang Zhen(add timer and animation)
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
		fightBox.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		fightBox.add(type);
		fightBox.add(attack);
		fightBox.add(health);
		fightBox.add(message);
		fightBox.add(att);
		fightBox.add(run);
		fightBox.setVisible(true);

	}

	/**
	 * fight with monster
	 * @author Sushant Balajee,Donald Tang,Wang Zhen
	 */
	public void fight(Location mosterLocation) {
		Player clientPlayer = gameClient.getClientPlayer();

		GameObject ObjectOfLoc = mosterLocation.getRoom().board.getSquares()[mosterLocation
				.getY()][mosterLocation.getX()].getGameObjectOnSquare();
		int damage = ((Monster) ObjectOfLoc).attack();

		//Monster attacks first
		clientPlayer.setHealth(clientPlayer.getHealth() - damage);
		//Player attacks second
		((Monster) ObjectOfLoc).setHealth(((Monster) ObjectOfLoc).getHealth()
				- clientPlayer.getAttack());

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
	 * link the gui to the network
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
				//				System.out.println(savedFilePlayers);

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
			} else if (menuItem.getText().equals("Instructions")) {
				JOptionPane
						.showMessageDialog(
								this,
								"WASD - Up Left Down Right\n"
										+ "Arrow Keys - Up Left Down Right (Allows Rotation)\n"
										+ "Q - Rotate Board Left\n"
										+ "E - Rotate Board Right",
								"Instructions", JOptionPane.INFORMATION_MESSAGE);
			}
		} else if (source instanceof JButton) {
			if (inputMessageField.getText().length() > 0) {
				sendMessage();
			}
			this.requestFocus();
		}
	}

	//unimplements method=============================================================
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
