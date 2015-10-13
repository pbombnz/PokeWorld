package ui;

/**
 *@author Wang Zhen
 *This class stores other way to design the gui. 
 *If want to change the print way to use Jlabel to print character, we can use methods in this class
 */
public class OtherDesignForGameFrame {
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
	 * this way to print object in 1st view will print far stuff before close stuff
	 */
	////#print objects
	//				double nowDrawLinePrintObject = viewHight;//the height of line now draw(it is the bot of the frame at start)
	//				double previouDrawLinePrintObject = viewHight;
	//				double previouX0PrintObject = midOfView - squareWidth / 2;//the line in the bot of the frame
	//				double previouY0PrintObject = viewHight;
	//				double previouX1PrintObject = midOfView + squareWidth / 2;//the line in the bot of the frame
	//				double previouY1PrintObject = viewHight;
	//				int checkLocationXPrintObject = clientPlayer.getLocation().getX();
	//				int checkLocationYPrintObject = clientPlayer.getLocation().getY();
	//				for (int i = 0; i < numSquaresFace + 1; i++) {
	//					//draw face square
	//					double nowWidthOfSquare = squareWidth * Math.pow(scaleY, i + 1);
	//					double nowStartX = midOfView - nowWidthOfSquare / 2;
	//					//add points for drawing Polygon
	//					int[] xPoint = new int[4];
	//					int[] yPoint = new int[4];
	//					//draw Polygon
	//					xPoint[0] = (int) previouX0PrintObject;
	//					yPoint[0] = (int) previouY0PrintObject - jumpOffsetFirstView;
	//					xPoint[1] = (int) previouX1PrintObject;
	//					yPoint[1] = (int) previouY1PrintObject - jumpOffsetFirstView;
	//					xPoint[2] = (int) (nowStartX + nowWidthOfSquare);
	//					yPoint[2] = (int) (nowDrawLinePrintObject - squareHeigh
	//							* Math.pow(scaleY, i + 1) - jumpOffsetFirstView);
	//					xPoint[3] = (int) nowStartX;
	//					yPoint[3] = (int) (nowDrawLinePrintObject - squareHeigh
	//							* Math.pow(scaleY, i + 1) - jumpOffsetFirstView);
	//
	//					//draw left squares
	//					for (int j = 0; j < numSquaresLeft; j++) {
	//						int[] xPointLeft = new int[4];
	//						int[] yPointLeft = new int[4];
	//						//draw Polygon
	//						int previouWidthOfSquare = (int) (previouX1PrintObject - previouX0PrintObject);
	//						xPointLeft[0] = (int) (previouX0PrintObject - j
	//								* previouWidthOfSquare);
	//						yPointLeft[0] = (int) (previouY0PrintObject - jumpOffsetFirstView);
	//						xPointLeft[1] = (int) (previouX1PrintObject - j
	//								* previouWidthOfSquare);
	//						yPointLeft[1] = (int) (previouY1PrintObject - jumpOffsetFirstView);
	//						xPointLeft[2] = (int) (nowStartX + nowWidthOfSquare - j
	//								* nowWidthOfSquare);
	//						yPointLeft[2] = (int) (nowDrawLinePrintObject - squareHeigh
	//								* Math.pow(scaleY, i + 1) - jumpOffsetFirstView);
	//						xPointLeft[3] = (int) (nowStartX - j * nowWidthOfSquare);
	//						yPointLeft[3] = (int) (nowDrawLinePrintObject - squareHeigh
	//								* Math.pow(scaleY, i + 1) - jumpOffsetFirstView);
	//
	//						//=====================================================================================
	//						//print the object left on this location====================================================
	//						Location nextLoc = nextSquareLocation(clientPlayer, i);
	//
	//						Game ga = gameClient.getGame();
	//						Room r = clientPlayer.getLocation().getRoom();
	//						BoardSquare[][] bs = r.board.getSquares();
	//
	//						int locX = nextLoc.getX();
	//						int locY = nextLoc.getY();
	//						if (clientPlayer.getDirection() == Direction.FACE_RIGHT) {
	//							locX = nextLoc.getX() + j;
	//						} else if (clientPlayer.getDirection() == Direction.FACE_LEFT) {
	//							locY = nextLoc.getY() + j;
	//						} else if (clientPlayer.getDirection() == Direction.BACK_LEFT) {
	//							locX = nextLoc.getX() - j;
	//						} else if (clientPlayer.getDirection() == Direction.BACK_RIGHT) {
	//							locY = nextLoc.getY() - j;
	//						}
	//						
	//						//diff direction has diff order to print ,this is for making sure the closer picture cover far
	//						if (clientPlayer.getDirection() == Direction.FACE_RIGHT) {
	//							//check whether next square is out of board
	//							if (locX != -1 && locX != 10 && locY != -1
	//									&& locY != 10) {
	//								if (bs[locY][locX].getGameObjectOnSquare() != null) {
	//
	//									if (bs[locY][locX].getGameObjectOnSquare() instanceof Tree) {
	//										int width = (xPoint[1] - xPoint[0])
	//												* TREE_SCALE_FIRST_VIEW;
	//										int height = width;
	//										int midPointX = xPointLeft[0]
	//												+ (xPointLeft[1] - xPointLeft[0])
	//												/ 2;
	//										int drawStartX = midPointX - width / 2;
	//										int drawStartY = yPointLeft[0] - (height);
	//										g.drawImage(bs[locY][locX]
	//												.getGameObjectOnSquare()
	//												.getSpriteImage().getImage(),
	//												drawStartX, drawStartY, width,
	//												height, null);
	//									} else {
	//										int height = (xPointLeft[2] - xPointLeft[3]);
	//										int width = height;
	//										int drawStartX = xPointLeft[3];
	//										int drawStartY = yPointLeft[0]
	//												- ((xPointLeft[2] - xPointLeft[3]));
	//										g.drawImage(bs[locY][locX]
	//												.getGameObjectOnSquare()
	//												.getSpriteImage().getImage(),
	//												drawStartX, drawStartY, width,
	//												height, null);
	//									}
	//								}
	//							}
	//						}
	//						//===============================================================================
	//					}
	//
	//					//draw right squares
	//					for (int j = 0; j < numSquaresRight; j++) {
	//						int[] xPointLeft = new int[4];
	//						int[] yPointLeft = new int[4];
	//						//draw Polygon
	//						int previouWidthOfSquare = (int) (previouX1PrintObject - previouX0PrintObject);
	//						xPointLeft[0] = (int) (previouX0PrintObject + j
	//								* previouWidthOfSquare);
	//						yPointLeft[0] = (int) (previouY0PrintObject - jumpOffsetFirstView);
	//						xPointLeft[1] = (int) (previouX1PrintObject + j
	//								* previouWidthOfSquare);
	//						yPointLeft[1] = (int) (previouY1PrintObject - jumpOffsetFirstView);
	//						xPointLeft[2] = (int) (nowStartX + nowWidthOfSquare + j
	//								* nowWidthOfSquare);
	//						yPointLeft[2] = (int) (nowDrawLinePrintObject - squareHeigh
	//								* Math.pow(scaleY, i + 1) - jumpOffsetFirstView);
	//						xPointLeft[3] = (int) (nowStartX + j * nowWidthOfSquare);
	//						yPointLeft[3] = (int) (nowDrawLinePrintObject - squareHeigh
	//								* Math.pow(scaleY, i + 1) - jumpOffsetFirstView);
	//
	//					}
	//
	//					//				g.drawImage(new ImageIcon("src/firstviewgrass.png").getImage(),xPoint[0],yPoint[0] , xPoint[1], yPoint[1], xPoint[2], yPoint[2], xPoint[3],yPoint[3],null);
	//					//				g.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer)
	//					//				System.out.println(xPoint[0]+","+yPoint[0]);
	//
	//					//updata previou
	//					previouX0PrintObject = nowStartX;
	//					previouY0PrintObject = nowDrawLinePrintObject - squareHeigh
	//							* Math.pow(scaleY, i + 1);
	//					previouX1PrintObject = nowStartX + nowWidthOfSquare;
	//					previouY1PrintObject = nowDrawLinePrintObject - squareHeigh
	//							* Math.pow(scaleY, i + 1);
	//					previouDrawLinePrintObject = nowDrawLinePrintObject;
	//					nowDrawLinePrintObject = nowDrawLinePrintObject - squareHeigh
	//							* Math.pow(scaleY, i + 1);
	//
	//					//=====================================================================================
	//					//print the object on this location====================================================
	//					Location nextLoc = nextSquareLocation(clientPlayer, i);
	//
	//					Game ga = gameClient.getGame();
	//					Room r = clientPlayer.getLocation().getRoom();
	//					BoardSquare[][] bs = r.board.getSquares();
	//
	//					//check whether next square is out of board
	//					if (nextLoc.getX() != -1 && nextLoc.getX() != 10
	//							&& nextLoc.getY() != -1 && nextLoc.getY() != 10) {
	//						if (bs[nextLoc.getY()][nextLoc.getX()]
	//								.getGameObjectOnSquare() != null) {
	//
	//							if (bs[nextLoc.getY()][nextLoc.getX()]
	//									.getGameObjectOnSquare() instanceof Tree) {
	//								int width = (xPoint[1] - xPoint[0])
	//										* TREE_SCALE_FIRST_VIEW;
	//								int height = width;
	//								int midPointX = xPoint[0] + (xPoint[1] - xPoint[0])
	//										/ 2;
	//								int drawStartX = midPointX - width / 2;
	//								int drawStartY = yPoint[0] - (height);
	//								g.drawImage(bs[nextLoc.getY()][nextLoc.getX()]
	//										.getGameObjectOnSquare().getSpriteImage()
	//										.getImage(), drawStartX, drawStartY, width,
	//										height, null);
	//							} else {
	//								int height = (xPoint[2] - xPoint[3]);
	//								int width = height;
	//								int drawStartX = xPoint[3];
	//								int drawStartY = yPoint[0]
	//										- ((xPoint[2] - xPoint[3]));
	//								g.drawImage(bs[nextLoc.getY()][nextLoc.getX()]
	//										.getGameObjectOnSquare().getSpriteImage()
	//										.getImage(), drawStartX, drawStartY, width,
	//										height, null);
	//								//these are set size as (yPoint[0] - yPoint[3]), this print smaller picture---------------------------------------
	//								//							int height = yPoint[0] - yPoint[3];
	//								//							int width = height;
	//								//							int midPointX = xPoint[0]+(xPoint[1]-xPoint[0])/2;
	//								//							int drawStartX = midPointX-width/2;
	//								//							int drawStartY = yPoint[3];
	//								//							g.drawImage(bs[nextLoc.getY()][nextLoc.getX()]
	//								//									.getGameObjectOnSquare().getSpriteImage()
	//								//									.getImage(), drawStartX,drawStartY,width,height, null);
	//								//----------------------------------------------------------------------------------
	//							}
	//						}
	//					}
	//					//===============================================================================
	//				}
}
