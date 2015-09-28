package game;
/**
 *@author Wang Zhen
 *
 *storing x,y of square 
 *(x is row number and y is colunm number in my position system)
 */
public class Position {
	public int x ;
	public int y ;
	public Position(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	public boolean equal(Position p2){
		return (x==p2.x&&y==p2.y);
	}
	public boolean equal(int x2,int y2){
		return (x==x2&&y==y2);
	}
}
