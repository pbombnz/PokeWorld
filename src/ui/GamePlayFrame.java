package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Polygon;
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

import com.sun.image.codec.jpeg.TruncatedFileException;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;

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
import game.objects.monster.*;
import game.objects.RareCandy;
import game.objects.Tree;

@SuppressWarnings("serial")
public class GamePlayFrame extends JFrame implements KeyListener, ActionListener {
	// The Emum has holds states for the JFrame so we know what to draw and when
	// for instance we draw
	private static enum FRAME_STATE {
		CREATED_FRAME, DO_NOTHING, GAME_CONNECTING, GAME_START, GAME_NORMAL, GAME_ENDED_EXPECTED, GAME_ENDED_UNEXPECTED
	};

	// The Size of the Frame
	//
	private static final int FRAME_HEIGHT = 610;
	private static final int FULL_FRAME_WIDTH = 1400;
	private static final int WELCOME_FRAME_WIDTH = 1000;
	// The size of the tiles when they are displayed
	private static final int TILE_WIDTH = 64;
	private static final int TILE_HEIGHT = 64;

	private FRAME_STATE frameState = FRAME_STATE.CREATED_FRAME;

	private GameClient gameClient;
	private Player clientPlayer;

	public int jumpOffset = 0;
	public int shakeOffset = 0;//the player will keep shake when they are standing in one place
	public int shakeTimer = 0;//using calculation number as timer. the shakeoffset will change when the timer reaches the timerLimit
	public final int SHAKE_TIMER_LIMIT = 200;// the shakeoffset will change when it reaches the timer limit

	public List<JLabel> infoLabels = new ArrayList<JLabel>();

	public JLabel headPictureLabel = null;
	public JLabel bgHeadViewLabel = null;
	public JLabel dieLabel = null;
	public JLabel attackLabel = null;
	public JPanel panel;
	public JLabel characterLabel;
	private JDialog fightBox;
	///================================================
	//the file below is for drawing 1st view
	//assume the is width of 1 square is 300 in 1st view
	private static final int FRAME_WIDTH = 800;//the width of the left backgroud picture
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

	///==================================

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

		//load labels of die and attack and levelup
		JLabel dieLabel = null;
		JLabel attackLabel = null;
		JLabel levelUpLabel_2 = null;
		JLabel levelUpLabel_3 = null;

		//print head picture
		if (clientPlayer.getPlayerLevel() == 1) {
			if (clientPlayer.getAvatar().getAvatarName().equals("Bulbasaur")) {
				dieLabel = new JLabel(new ImageIcon("src/Bulbasaur_die.gif"));
				attackLabel = new JLabel(new ImageIcon(
						"src/Bulbasaur_attack.gif"));
				levelUpLabel_2 = new JLabel(new ImageIcon(
						"src/Charmander_upto_level2.gif"));
				levelUpLabel_3 = new JLabel(new ImageIcon(
						"src/Charmander_upto_level3.gif"));
			} else if (clientPlayer.getAvatar().getAvatarName()
					.equals("Charmander")) {
				dieLabel = new JLabel(new ImageIcon("src/Charmander_die.gif"));
				attackLabel = new JLabel(new ImageIcon(
						"src/Charmander_attack.gif"));
				levelUpLabel_2 = new JLabel(new ImageIcon(
						"src/Charmander_upto_level2.gif"));
				levelUpLabel_3 = new JLabel(new ImageIcon(
						"src/Charmander_upto_level3.gif"));
			} else if (clientPlayer.getAvatar().getAvatarName()
					.equals("Squirtle")) {
				dieLabel = new JLabel(new ImageIcon("src/Squirtle_die.gif"));
				attackLabel = new JLabel(new ImageIcon(
						"src/Squirtle_attack.gif"));
				levelUpLabel_2 = new JLabel(new ImageIcon(
						"src/Charmander_upto_level2.gif"));
				levelUpLabel_3 = new JLabel(new ImageIcon(
						"src/Charmander_upto_level3.gif"));
			}
		} else if (clientPlayer.getPlayerLevel() == 2) {
			if (clientPlayer.getAvatar().getAvatarName().equals("Bulbasaur")) {
				dieLabel = new JLabel(new ImageIcon("src/Bulbasaur_die.gif"));
				attackLabel = new JLabel(new ImageIcon(
						"src/Bulbasaur_attack.gif"));
				levelUpLabel_2 = new JLabel(new ImageIcon(
						"src/Charmander_upto_level2.gif"));
				levelUpLabel_3 = new JLabel(new ImageIcon(
						"src/Charmander_upto_level3.gif"));
			} else if (clientPlayer.getAvatar().getAvatarName()
					.equals("Charmander")) {
				dieLabel = new JLabel(new ImageIcon("src/Charmander_die.gif"));
				attackLabel = new JLabel(new ImageIcon(
						"src/Charmander_attack.gif"));
				levelUpLabel_2 = new JLabel(new ImageIcon(
						"src/Charmander_upto_level2.gif"));
				levelUpLabel_3 = new JLabel(new ImageIcon(
						"src/Charmander_upto_level3.gif"));
			} else if (clientPlayer.getAvatar().getAvatarName()
					.equals("Squirtle")) {
				dieLabel = new JLabel(new ImageIcon("src/Squirtle_die.gif"));
				attackLabel = new JLabel(new ImageIcon(
						"src/Squirtle_attack.gif"));
				levelUpLabel_2 = new JLabel(new ImageIcon(
						"src/Charmander_upto_level2.gif"));
				levelUpLabel_3 = new JLabel(new ImageIcon(
						"src/Charmander_upto_level3.gif"));
			}
		} else {
			if (clientPlayer.getAvatar().getAvatarName().equals("Bulbasaur")) {
				dieLabel = new JLabel(new ImageIcon("src/Bulbasaur_die.gif"));
				attackLabel = new JLabel(new ImageIcon(
						"src/Bulbasaur_attack.gif"));
				levelUpLabel_2 = new JLabel(new ImageIcon(
						"src/Charmander_upto_level2.gif"));
				levelUpLabel_3 = new JLabel(new ImageIcon(
						"src/Charmander_upto_level3.gif"));
			} else if (clientPlayer.getAvatar().getAvatarName()
					.equals("Charmander")) {
				dieLabel = new JLabel(new ImageIcon("src/Charmander_die.gif"));
				attackLabel = new JLabel(new ImageIcon(
						"src/Charmander_attack_lvl3.gif"));
				levelUpLabel_2 = new JLabel(new ImageIcon(
						"src/Charmander_upto_level2.gif"));
				levelUpLabel_3 = new JLabel(new ImageIcon(
						"src/Charmander_upto_level3.gif"));
			} else if (clientPlayer.getAvatar().getAvatarName()
					.equals("Squirtle")) {
				dieLabel = new JLabel(new ImageIcon("src/Squirtle_die.gif"));
				attackLabel = new JLabel(new ImageIcon(
						"src/Squirtle_attack.gif"));
				levelUpLabel_2 = new JLabel(new ImageIcon(
						"src/Charmander_upto_level2.gif"));
				levelUpLabel_3 = new JLabel(new ImageIcon(
						"src/Charmander_upto_level3.gif"));
			}
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

			//draw welcome picture
			if (frameState == FRAME_STATE.CREATED_FRAME/*gameClient == null || gameClient.getGame() == null*/) {
				g.drawImage(new ImageIcon(
						"./sprites/backgrounds/welcome_bg.jpg").getImage(), 0,
						0, WELCOME_FRAME_WIDTH, FRAME_HEIGHT, null);
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

			g.drawImage(new ImageIcon("src/firstview_bk.png").getImage(),
					startX + 2 - turnOffset, startY + 2 - jumpOffsetFirstView
					- 60, null);
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

			int numSquaresFace = 0;
			int numSquaresLeft = 0;
			int numSquaresRight = 0;
			Location playerLoc = clientPlayer.getLocation();
			int boardSize = 10;
			int offset = 1;//this offset is cuz the locaion is from 0 not 1
			if (clientPlayer.getDirection() == Direction.BACK_LEFT) {
				numSquaresFace = playerLoc.getY();
				numSquaresLeft = playerLoc.getX();
				numSquaresRight = boardSize - playerLoc.getX() - offset;
			} else if (clientPlayer.getDirection() == Direction.BACK_RIGHT) {
				numSquaresFace = boardSize - playerLoc.getX() - offset;
				numSquaresLeft = playerLoc.getY();
				numSquaresRight = boardSize - playerLoc.getY() - offset;
			} else if (clientPlayer.getDirection() == Direction.FACE_LEFT) {
				numSquaresFace = playerLoc.getX();
				numSquaresLeft = boardSize - playerLoc.getY() - offset;
				numSquaresRight = playerLoc.getY();
				;
			} else if (clientPlayer.getDirection() == Direction.FACE_RIGHT) {
				numSquaresFace = boardSize - playerLoc.getY() - offset;
				numSquaresLeft = boardSize - playerLoc.getX() - offset;
				numSquaresRight = playerLoc.getX();
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
				g.setColor(Color.green.darker());
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
					g.setColor(Color.green.darker());
					g.fillPolygon(xPointLeft, yPointLeft, 4);
					g.setColor(Color.BLACK);
					g.drawPolygon(xPointLeft, yPointLeft, 4);
				}

				//draw right squares
				for (int j = 0; j < numSquaresRight; j++) {
					int[] xPointLeft = new int[4];
					int[] yPointLeft = new int[4];
					//draw Polygon
					int previouWidthOfSquare = (int) (previouX1 - previouX0);
					xPointLeft[0] = (int) (previouX0 + j * previouWidthOfSquare);
					yPointLeft[0] = (int) (previouY0 - jumpOffsetFirstView);
					xPointLeft[1] = (int) (previouX1 + j * previouWidthOfSquare);
					yPointLeft[1] = (int) (previouY1 - jumpOffsetFirstView);
					xPointLeft[2] = (int) (nowStartX + nowWidthOfSquare + j
							* nowWidthOfSquare);
					yPointLeft[2] = (int) (nowDrawLine - squareHeigh
							* Math.pow(scaleY, i + 1) - jumpOffsetFirstView);
					xPointLeft[3] = (int) (nowStartX + j * nowWidthOfSquare);
					yPointLeft[3] = (int) (nowDrawLine - squareHeigh
							* Math.pow(scaleY, i + 1) - jumpOffsetFirstView);
					g.setColor(Color.green.darker());
					g.fillPolygon(xPointLeft, yPointLeft, 4);
					g.setColor(Color.BLACK);
					g.drawPolygon(xPointLeft, yPointLeft, 4);
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
				//print the object on this location
				Location nextLoc = nextSquareLocation(clientPlayer, i);

				Game ga = gameClient.getGame();
				Room r = ga.getRooms().get(GameLauncher.ROOMINDEX);
				BoardSquare[][] bs = r.board.getSquares();

				//check whether next square is out of board
				if (nextLoc.getX() != -1 && nextLoc.getX() != 10
						&& nextLoc.getY() != -1 && nextLoc.getY() != 10) {
					if (bs[nextLoc.getX()][nextLoc.getY()]
							.getGameObjectOnSquare() != null) {
						if (bs[nextLoc.getX()][nextLoc.getY()]
								.getGameObjectOnSquare() instanceof Tree) {
							//							System.out.println(nextLoc.getX() + ","
							//									+ nextLoc.getY());
							//							System.out.println(previouX0 + "," + previouY0);
							g.drawImage(bs[nextLoc.getX()][nextLoc.getY()]
									.getGameObjectOnSquare().getSpriteImage()
									.getImage(), (int) previouX0 - 20,
									(int) previouY0 - 150, null);
						} else {
							g.drawImage(bs[nextLoc.getX()][nextLoc.getY()]
									.getGameObjectOnSquare().getSpriteImage()
									.getImage(), (int) previouX0,
									(int) previouY1 - 20, 50, 50, null);
						}
					}
				}
			}

			//print edge 
			g.setColor(Color.black);

			//print character
			Image characterImage = clientPlayer.getSpriteBasedOnDirection(
					firstViewDirection).getImage();
			g.drawImage(characterImage, midOfView, 500 + shakeOffset, null);

			g.fillRect(startX - 10, 0, 20, FRAME_HEIGHT);
			g.fillRect(FULL_FRAME_WIDTH - 20, 0, 20, FRAME_HEIGHT);
			///=============================================

			/// Draw background picture
			g.drawImage(new ImageIcon("./sprites/backgrounds/game_bg.jpg")
			.getImage(), 0, 0, FRAME_WIDTH, FRAME_HEIGHT, null);

			///print compass
			g.drawImage(
					new ImageIcon("./sprites/other/compass.png").getImage(),
					600, 355, 200, 200, null);

			// Initial starting position of where the first square is going to be drawn
			int yPos = FRAME_HEIGHT / 2;
			int xPos = 110;

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
					Room r = ga.getRooms().get(GameLauncher.ROOMINDEX);
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

		}
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
			jumpOffset = -20;
			jumpOffsetFirstView = -50;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// don't fire an event on backspace or delete
		Location loc = clientPlayer.getLocation();
		//for third view control
		GameObject gg = loc.getRoom().board.getSquares()[loc.getY()][loc.getX()]
				.getGameObjectOnSquare();
		if (e.getKeyCode() == KeyEvent.VK_W) {
			//character will turn 1st if the character is not facing that side
			if (clientPlayer.getDirection() == Direction.BACK_LEFT) {
				loc.moveNorth();
			} else {
				clientPlayer.setDirection(Player.Direction.BACK_LEFT);
				loc.moveNorth();
			}
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			//character will turn 1st if the character is not facing that side
			if (clientPlayer.getDirection() == Direction.FACE_RIGHT) {
				loc.moveSouth();//oo
			} else {
				clientPlayer.setDirection(Player.Direction.FACE_RIGHT);
				loc.moveSouth();//oo
			}
		} else if (e.getKeyCode() == KeyEvent.VK_A) {
			//character will turn 1st if the character is not facing that side
			if (clientPlayer.getDirection() == Direction.FACE_LEFT) {
				loc.moveWest();
			} else {
				clientPlayer.setDirection(Player.Direction.FACE_LEFT);
				loc.moveWest();//oo
			}
		} else if (e.getKeyCode() == KeyEvent.VK_D) {
			//character will turn 1st if the character is not facing that side
			if (clientPlayer.getDirection() == Direction.BACK_RIGHT) {
				loc.moveEast();
			} else {
				clientPlayer.setDirection(Player.Direction.BACK_RIGHT);
				loc.moveEast();//oo
			}
		}
		//these are for 1st person view contrl
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			//character will turn 1st if the character is not facing that side
			if (clientPlayer.getDirection() == Direction.BACK_RIGHT) {
				loc.moveEast();
			} else if (clientPlayer.getDirection() == Direction.BACK_LEFT) {
				loc.moveNorth();
			} else if (clientPlayer.getDirection() == Direction.FACE_LEFT) {
				loc.moveWest();
			} else if (clientPlayer.getDirection() == Direction.FACE_RIGHT) {
				loc.moveSouth();
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
		// these are for changing game direction
		else if (e.getKeyCode() == KeyEvent.VK_E) {
			//turn the GUI to the left side
			//change the board(change the location of objects)
			Board oldBoard = gameClient.getGame().getRooms()
					.get(GameLauncher.ROOMINDEX).board;
			Board newBoard = new EmptyBoard();

			for (int i = 0; i < oldBoard.getWidth(); i++) {
				for (int j = 0; j < oldBoard.getHeight(); j++) {
					int offset = 1;//because the start position is (0,0) not(1,1), so there is an offset
					newBoard.squares[i][j] = oldBoard.squares[gameClient
					                                          .getGame().getRooms().get(GameLauncher.ROOMINDEX).board
					                                          .getHeight()
					                                          - (j + offset)][i];
				}
			}
			gameClient.getGame().getRooms().get(GameLauncher.ROOMINDEX).board = newBoard;

			//change the locations of player 
			Location newloc = new Location();
			newloc.setRoom(clientPlayer.getLocation().getRoom());

			int offset = 1;//because the start position is (0,0) not(1,1), so there is a offset

			newloc.setX(gameClient.getGame().getRooms().get(GameLauncher.ROOMINDEX).board
					.getHeight() - (clientPlayer.getLocation().getY() + offset));

			newloc.setY(clientPlayer.getLocation().getX());
			clientPlayer.setLocation(newloc);
			//let the player image turn left 
			turnPlayerImageLeft(clientPlayer);
		} else if (e.getKeyCode() == KeyEvent.VK_Q) {
			//turn the gui to right side
			//change the board(change the locations of object)
			Board newBoard = new EmptyBoard();
			Board oldBoard = gameClient.getGame().getRooms()
					.get(GameLauncher.ROOMINDEX).board;
			for (int i = 0; i < oldBoard.getWidth(); i++) {
				for (int j = 0; j < oldBoard.getHeight(); j++) {
					int offset = 1;//because the start position is (0,0) not(1,1), so there is a offset
					newBoard.squares[i][j] = oldBoard.squares[j][gameClient
					                                             .getGame().getRooms().get(GameLauncher.ROOMINDEX).board
					                                             .getWidth()
					                                             - (i + offset)];
				}
			}
			gameClient.getGame().getRooms().get(GameLauncher.ROOMINDEX).board = newBoard;

			//change the locations of player 
			Location newloc = new Location();
			newloc.setRoom(clientPlayer.getLocation().getRoom());

			int offset = 1;//because the start position is (0,0) not(1,1), so there is a offset

			newloc.setX(clientPlayer.getLocation().getY());

			newloc.setY(gameClient.getGame().getRooms().get(GameLauncher.ROOMINDEX).board
					.getWidth() - (clientPlayer.getLocation().getX() + offset));

			clientPlayer.setLocation(newloc);
			//let the player image turn left 
			turnPlayerImageRight(clientPlayer);

		}
		//allows player to jump on the spot
		else if (e.getKeyCode() == KeyEvent.VK_J) {
			jumpOffset = 0;
			jumpOffsetFirstView = 0;
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
			//=================================================
			//draw gif here
			if (clientPlayer.getPlayerLevel() == 2) {
				panel.add(lvlupLabel_2);
				final Timer timer = new Timer();
				TimerTask tt = new TimerTask() {
					@Override
					public void run() {
						timer.cancel();
						//here is the methods run after timer here 
						///=======================================
						panel.remove(lvlupLabel_2);
						//========================================
					}
				};
				timer.schedule(tt, 2500);
			} else if (clientPlayer.getPlayerLevel() == 3) {
				panel.add(lvlupLabel_3);
				final Timer timer = new Timer();
				TimerTask tt = new TimerTask() {
					@Override
					public void run() {
						timer.cancel();
						//here is the methods run after timer here 
						///=======================================
						panel.remove(lvlupLabel_3);
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
		}
		//If you find a Door, checks your inventory for a Key
		//If you have a Key, compares the Key ID and Door ID for a match
		//If they match, allows you to enter a different room
		if (go instanceof Door) {
			for (Item items : clientPlayer.getInventory()) {
				if (items instanceof Key) {
					if (((Door) go).id() == items.id()) {

						Door theDoor = (Door) go;
						Room nowRoom = gameClient.getGame().getRooms()
								.get(GameLauncher.ROOMINDEX);

						if (nowRoom.level == theDoor.linkFrom) {
							//for change room 
							//1. i change change board
							//2. i change the location of player
							GameLauncher.ROOMINDEX = theDoor.linkTo - 1;
							clientPlayer.getLocation().setRoom(
									gameClient.getGame().getRooms()
									.get(GameLauncher.ROOMINDEX));

						} else if (nowRoom.level == theDoor.linkFrom + 1) {
							System.out.println("this door goes to level 1");
							GameLauncher.ROOMINDEX = theDoor.linkTo - 2;
							clientPlayer.getLocation().setRoom(
									gameClient.getGame().getRooms()
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
	 *@contributer Wang Zhen(add Timer and gif)
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
						fight();
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
	public void fight() {

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
