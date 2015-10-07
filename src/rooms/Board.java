package rooms;

import java.io.Serializable;
import java.util.Arrays;

import game.BoardSquare;
import game.objects.Door;
import game.objects.Rattata;
import game.objects.GoodPotion;
import game.objects.Key;
import game.objects.RareCandy;
import game.objects.Tree;
/**
 *@author Wang Zhen
 */

public abstract class Board implements Serializable {
	protected static final long serialVersionUID = -3601832062619945416L;
	protected int width;
	protected int height;

	public BoardSquare[][] squares;

	public abstract BoardSquare[][] getSquares();

	public abstract int getWidth() ;

	public abstract int getHeight() ;

	@Override
	public abstract int hashCode() ;

	@Override
	public abstract boolean equals(Object obj) ;


	public abstract BoardSquare getSquareAt(int y, int x) ;

}
