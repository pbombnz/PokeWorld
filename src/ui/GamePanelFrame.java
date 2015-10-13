package ui;

import game.BoardSquare;
import game.Direction;
import game.Game;
import game.Location;
import game.Player;
import game.objects.interactiveObjects.Key;
import game.objects.interactiveObjects.RareCandy;
import game.objects.monster.Monster;
import game.objects.scene.MagicCircle;
import game.objects.scene.Plant;
import game.objects.scene.Tree;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import rooms.Room;

/**
 *@author Wang Zhen
 */
public class GamePanelFrame extends GameFrame{
	

	public GamePanelFrame() {
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

		frameState = GameFrame.FRAME_STATE.STANDBY;
		//set start time
		startTime = getCurrentTime();
	}


	/**
	 * the game panel on game frame
	 */
	class GamePanel extends JPanel {
		/**
		 * this method will run every time game refresh
		 */
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
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
			//these are 1st person view
			//draw frame
			g.setColor(Color.black);
			g.fillRect(startX - 10, 0, FULL_FRAME_WIDTH - startX, FRAME_HEIGHT);
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
			//changeoffset is for turning backgroud picture when turn character
			//change the offset when it will get edge
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

			//load labels
			Player clientPlayer = gameClient.getClientPlayer();
			if (!hasLoadedLabels) {
				//add all jlabel after picking character
				loadLabels();
				panel.add(headPictureLabel);
				panel.add(bgHeadViewLabel);
				hasLoadedLabels = true;
			}

			//caculating how many squares on the player's left side,right side and face side
			int numSquaresFace = 0;//the number of squares on the player's face side
			int numSquaresLeft = 0;//the number of squares on the player's left side
			int numSquaresRight = 0;////the number of squares on the player's right side 
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

			//set the size and based data of 1st person view 
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

				//draw ground Polygons on face side
				double nowWidthOfSquare = squareWidth * Math.pow(scaleY, i + 1);
				double nowStartX = midOfView - nowWidthOfSquare / 2;
				//add points for drawing Polygon
				int[] xPoint = new int[4];
				int[] yPoint = new int[4];
				//calcutating the 4 potions for a Polygon
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
				//draw ground Polygons on face side
				if (!isDay) {
					if (i > sightRange) {
						//draw darker Polygons in night and out of range
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

				//draw ground Polygons on left side
				for (int j = 0; j < numSquaresLeft; j++) {
					int[] xPointLeft = new int[4];
					int[] yPointLeft = new int[4];
					//calcutating the 4 potions for a Polygon
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
					//draw ground Polygons on left side
					if (!isDay) {
						if (j > sightRange || i > sightRange) {
							//draw darker Polygons in night and out of range
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

				//draw ground Polygons on right side
				for (int j = 0; j < numSquaresRight; j++) {
					int[] xPointRight = new int[4];
					int[] yPointRight = new int[4];
					//calcutating the 4 potions for a Polygon
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
					//draw ground Polygons on right side
					if (!isDay) {
						if (j > sightRange || i > sightRange) {
							//draw darker Polygons in night and out of range
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

				//updata values
				previouX0 = nowStartX;
				previouY0 = nowDrawLine - squareHeigh * Math.pow(scaleY, i + 1);
				previouX1 = nowStartX + nowWidthOfSquare;
				previouY1 = nowDrawLine - squareHeigh * Math.pow(scaleY, i + 1);
				previouDrawLine = nowDrawLine;
				nowDrawLine = nowDrawLine - squareHeigh
						* Math.pow(scaleY, i + 1);
			}

			//#create storages to store the positions and load information into storages 
			//save positions and read and print object is because the object need to be printed reverse
			double nowDrawLinePrintObject = viewHight;//the height of line now draw(it is the bot of the frame at start)
			double previouDrawLinePrintObject = viewHight;
			double previouX0PrintObject = midOfView - squareWidth / 2;//the line in the bot of the frame
			double previouY0PrintObject = viewHight;
			double previouX1PrintObject = midOfView + squareWidth / 2;//the line in the bot of the frame
			double previouY1PrintObject = viewHight;
			int checkLocationXPrintObject = clientPlayer.getLocation().getX();
			int checkLocationYPrintObject = clientPlayer.getLocation().getY();
			List<PointArrayStorage> storages = new ArrayList<PointArrayStorage>();
			//caculating all positions and save into storages
			for (int i = 0; i < numSquaresFace + 1; i++) {
				//create storage
				PointArrayStorage storage = new PointArrayStorage();

				//get positions of the face side square and save into storage 
				double nowWidthOfSquare = squareWidth * Math.pow(scaleY, i + 1);
				double nowStartX = midOfView - nowWidthOfSquare / 2;
				int[] xPoint = new int[4];
				int[] yPoint = new int[4];
				//calcutating the 4 potions for a Polygon
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

				//get positions of the left side square and save into storage 
				for (int j = 0; j < numSquaresLeft; j++) {
					int[] xPointLeft = new int[4];
					int[] yPointLeft = new int[4];
					int previouWidthOfSquare = (int) (previouX1PrintObject - previouX0PrintObject);
					//calcutating the 4 potions for a Polygon
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
					//SAVE INTO STORAGE
					PointArrayStorageLeft storageLeft = new PointArrayStorageLeft();
					storageLeft.xPoint = xPointLeft;
					storageLeft.yPoint = yPointLeft;
					storage.leftList.add(storageLeft);

				}

				//get positions of the right side square and save into storage 
				for (int j = 0; j < numSquaresRight; j++) {
					int[] xPointLeft = new int[4];
					int[] yPointLeft = new int[4];
					int previouWidthOfSquare = (int) (previouX1PrintObject - previouX0PrintObject);
					//calcutating the 4 potions for a Polygon
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
					//SAVE INTO STORAGE
					PointArrayStorageRight storageRight = new PointArrayStorageRight();
					storageRight.xPoint = xPointLeft;
					storageRight.yPoint = yPointLeft;
					storage.rightlList.add(storageRight);
				}

				//updata values
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

			//#load the positions from storages and print objects
			//save positions and read and print object is because the object need to be printed reverse
			for (int i = numSquaresFace; i > -1; i--) {
				PointArrayStorage storage = storages.get(i);
				int[] xPoint = storage.xPoint;
				int[] yPoint = storage.yPoint;

				//draw left side objects
				for (int j = numSquaresLeft - 1; j > -1; j--) {
					PointArrayStorageLeft storageLeft = storage.leftList.get(j);
					int[] xPointLeft = storageLeft.xPoint;
					int[] yPointLeft = storageLeft.yPoint;

					//print the object left this location
					Location nextLoc = nextSquareLocation(clientPlayer, i);

					Game ga = gameClient.getGame();
					Room r = clientPlayer.getLocation().getRoom();
					BoardSquare[][] bs = r.board.getSquares();

					//caculate position depend on player face side
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
									// tree
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
									//not tree
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
								//is night
								if (isInSightRange(clientPlayer, locY, locX)) {
									//only print objects in the sight range in night
									if (bs[locY][locX].getGameObjectOnSquare() instanceof Tree) {
										//tree
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
										//not tree
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

				//draw right side objects
				for (int j = numSquaresRight - 1; j > -1; j--) {
					PointArrayStorageRight storageRight = storage.rightlList
							.get(j);
					int[] xPointRight = storageRight.xPoint;
					int[] yPointRight = storageRight.yPoint;
					//print the object left on this location
					Location nextLoc = nextSquareLocation(clientPlayer, i);

					Game ga = gameClient.getGame();
					Room r = clientPlayer.getLocation().getRoom();
					BoardSquare[][] bs = r.board.getSquares();

					//caculate position depend on player face side
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
									//tree
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
									//not tree
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
								//night
								if (isInSightRange(clientPlayer, locY, locX)) {
									//only print objects in the sight range in night
									if (bs[locY][locX].getGameObjectOnSquare() instanceof Tree) {
										//tree
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
										//not tree
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

				//print the object on face side
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
								//tree
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
								//not tree
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
								//int height = yPoint[0] - yPoint[3];
								//int width = height;
								//int midPointX = xPoint[0] + (xPoint[1] - xPoint[0])/ 2;
								//int drawStartX = midPointX - width / 2;
								//int drawStartY = yPoint[3];
								//g.drawImage(bs[nextLoc.getY()][nextLoc.getX()]
								//	.getGameObjectOnSquare().getSpriteImage()
								//  .getImage(), drawStartX,drawStartY,width,height, null);
								//----------------------------------------------------------------------------------
							}
						} else {
							//night
							if (isInSightRange(clientPlayer, nextLoc.getY(),
									nextLoc.getX())) {
								//only print objects in the sight range in night
								if (bs[nextLoc.getY()][nextLoc.getX()]
										.getGameObjectOnSquare() instanceof Tree) {
									//tree
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
									//not tree
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
									//int height = yPoint[0] - yPoint[3];
									//int width = height;
									//int midPointX = xPoint[0] + (xPoint[1] - xPoint[0])/ 2;
									//int drawStartX = midPointX - width / 2;
									//int drawStartY = yPoint[3];
									//g.drawImage(bs[nextLoc.getY()][nextLoc.getX()]
									//	.getGameObjectOnSquare().getSpriteImage()
									//  .getImage(), drawStartX,drawStartY,width,height, null);
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
					640, 400, 200, 200, null);

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

			//print ground square(grass)
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
							//print darkgrass out of the sight range in night
							g.drawImage(
									new ImageIcon(
											"./sprites/tiles/darkgrass.png")
											.getImage(), tileX, tileY,
									TILE_WIDTH, TILE_HEIGHT, null);
						}
					}

					//print player
					Location clientPlayerLoc = clientPlayer.getLocation();
					if (clientPlayerLoc.getX() == cellX
							&& clientPlayerLoc.getY() == cellY) {
						//let player shake even if he dont move
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

					//print other players
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
						//draw player as red 
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
							//draw space as green 
							g.setColor(Color.GREEN.darker());
							g.fillRect(mapNowX, mapStartY, mapSquareSize,
									mapSquareSize);
							g.setColor(Color.black);
							g.drawRect(mapNowX, mapStartY, mapSquareSize,
									mapSquareSize);
						} else if (bs[cellY][cellX].getGameObjectOnSquare() instanceof Tree) {
							//draw tree as black 
							g.setColor(Color.black);
							g.fillRect(mapNowX, mapStartY, mapSquareSize,
									mapSquareSize);
							g.setColor(Color.black);
							g.drawRect(mapNowX, mapStartY, mapSquareSize,
									mapSquareSize);
						} else if (bs[cellY][cellX].getGameObjectOnSquare() instanceof Plant) {
							//draw plant as pink 
							g.setColor(Color.PINK);
							g.fillRect(mapNowX, mapStartY, mapSquareSize,
									mapSquareSize);
							g.setColor(Color.black);
							g.drawRect(mapNowX, mapStartY, mapSquareSize,
									mapSquareSize);
						} else if (bs[cellY][cellX].getGameObjectOnSquare() instanceof Monster) {
							//draw monster as CYAN 
							g.setColor(Color.CYAN);
							g.fillRect(mapNowX, mapStartY, mapSquareSize,
									mapSquareSize);
							g.setColor(Color.black);
							g.drawRect(mapNowX, mapStartY, mapSquareSize,
									mapSquareSize);
						} else if (bs[cellY][cellX].getGameObjectOnSquare() instanceof MagicCircle) {
							//draw migiccircle as cyan 
							g.setColor(Color.CYAN);
							g.fillRect(mapNowX, mapStartY, mapSquareSize,
									mapSquareSize);
							g.setColor(Color.black);
							g.drawRect(mapNowX, mapStartY, mapSquareSize,
									mapSquareSize);
						} else if (bs[cellY][cellX].getGameObjectOnSquare() instanceof Key) {
							//draw key as orange
							g.setColor(Color.ORANGE);
							g.fillRect(mapNowX, mapStartY, mapSquareSize,
									mapSquareSize);
							g.setColor(Color.black);
							g.drawRect(mapNowX, mapStartY, mapSquareSize,
									mapSquareSize);
						} else if (bs[cellY][cellX].getGameObjectOnSquare() instanceof RareCandy) {
							//draw candy as blue 
							g.setColor(Color.blue);
							g.fillRect(mapNowX, mapStartY, mapSquareSize,
									mapSquareSize);
							g.setColor(Color.black);
							g.drawRect(mapNowX, mapStartY, mapSquareSize,
									mapSquareSize);
						} else {
							//draw others as LIGHT_GRAY 
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
			//draw white lines as rain
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
}
