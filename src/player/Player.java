package player;

import gameObjects.Item;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import squares.Square;

/**
 *@author Wang Zhen
 */
public abstract class Player {
	public String name;
	public int attack ;
	public int health ;
	
	//for gui
	public Location location ;
	public String direction;//fl,fr,bl,br
	public ImageIcon touxiang;
	public ImageIcon faceleft;
	public ImageIcon faceright;
	public ImageIcon backleft;
	public ImageIcon backright ;
	
	public void goUp(){
		if (direction.equals("fl")) {
			direction="bl";
		} else if (direction.equals("fr")) {
			direction="br";
		} else if (direction.equals("bl")) {
			if(location.row==0){
				return;
			}else{
				location.row--;
			}
		} else if (direction.equals("br")) {
			if(location.row==0){
				return;
			}else{
				location.row--;
			}
		}
	}
	
	public void goDown(){
		if (direction.equals("fl")) {
			if(location.row>=8){
				return;
			}else{
				location.row++;
			}
		} else if (direction.equals("fr")) {
			if(location.row>=8){
				return;
			}else{
				location.row++;
			}
		} else if (direction.equals("bl")) {
			direction="fl";
		} else if (direction.equals("br")) {
			direction="fr";
		}
	}
	public void goLeft(){
		if (direction.equals("fl")) {
			if(location.col<=0){
				return;
			}else{
				location.col--;
			}
		} else if (direction.equals("fr")) {
			direction="fl";
		} else if (direction.equals("bl")) {
			if(location.col<=0){
				return;
			}else{
				location.col--;
			}
		} else if (direction.equals("br")) {
			direction="bl";
		}
	}
	
	public void goRight(){
		if (direction.equals("fl")) {
			direction="fr";
		} else if (direction.equals("fr")) {
			if(location.col>=10){
				return;
			}else{
				location.col++;
			}
		} else if (direction.equals("bl")) {
			direction="br";
		} else if (direction.equals("br")) {
			if(location.col>=10){
				return;
			}else{
				location.col++;
			}
		}
	}
}
