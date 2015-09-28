package gui;

import game.Position;

/**
 *@author Wang Zhen
 *
 *this class is for storing position
 */
public class UIPosition {
	public int x ;
	public int y ;
	public UIPosition(int x, int y) {
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
