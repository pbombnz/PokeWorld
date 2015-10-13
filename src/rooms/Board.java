package rooms;

import java.io.Serializable;
import java.util.Arrays;

import game.BoardSquare;
import game.objects.interactiveObjects.*;
import game.objects.monster.*;
import game.objects.scene.*;
/**
 *@author Wang Zhen
 *this board is for store information
 */

/**
 * @author Donald
 *
 */
public abstract class Board implements Serializable {
	protected static final long serialVersionUID = -3601832062619945416L;
	
	protected int width;
	protected int height;
	public BoardSquare[][] squares;

	/** no-args Constructor **/
	public Board() {
		super();
	}
	
	/**
	 * @return an array of BoardSquards
	 */
	public abstract BoardSquare[][] getSquares();

	/**
	 * @return width of board
	 */
	public abstract int getWidth() ;

	/**
	 * @return height of board
	 */
	public abstract int getHeight() ;

	@Override
	public abstract int hashCode() ;

	@Override
	public abstract boolean equals(Object obj) ;


	/**
	 * @param y coord
	 * @param x coord
	 * @return BoardSquare at coords
	 */
	public abstract BoardSquare getSquareAt(int y, int x) ;

}
