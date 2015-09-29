package game;

import game.objects.GameObject;

import java.awt.Image;

import javax.swing.ImageIcon;

public class BoardSquare {
	private static final ImageIcon terrainTileSprite = new ImageIcon("./sprites/tiles/grass.png");
	
	private GameObject gameObjectOnSquare;
	
	public BoardSquare(GameObject gameObjectOnSquare) {
		this.gameObjectOnSquare = gameObjectOnSquare;
	}
	
	public static Image getTileImage() {
		return terrainTileSprite.getImage();
	}

	public GameObject getGameObjectOnSquare() {
		return gameObjectOnSquare;
	}

	public void setGameObjectOnSquare(GameObject gameObjectOnSquare) {
		this.gameObjectOnSquare = gameObjectOnSquare;
	}
}