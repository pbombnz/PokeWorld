package game;

import game.objects.GameObject;

import java.awt.Image;
import java.io.Serializable;

import javax.swing.ImageIcon;

/**
 * This class is how we represent a square on the board. It stores a gameObject
 * which is encapsulated so it has getters and setters.
 *
 */
public class BoardSquare implements Serializable {

	private static final long serialVersionUID = -5741277974211422063L;

	private static final ImageIcon terrainTileSprite = new ImageIcon("./sprites/tiles/grass.png");

	private GameObject gameObjectOnSquare;

	public BoardSquare() {
	}

	public BoardSquare(GameObject gameObjectOnSquare) {
		this.gameObjectOnSquare = gameObjectOnSquare;
	}

	/**
	 * @return Image， the image of the tile
	 */
	public static Image getTileImage() {
		return terrainTileSprite.getImage();
	}

	/**
	 * @return GameObject, the GameObject on the square
	 */
	public GameObject getGameObjectOnSquare() {
		return gameObjectOnSquare;
	}

	/**
	 * @param gameObjectOnSquare, sets GameObject on the square
	 */
	public void setGameObjectOnSquare(GameObject gameObjectOnSquare) {
		this.gameObjectOnSquare = gameObjectOnSquare;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gameObjectOnSquare == null) ? 0 : gameObjectOnSquare.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BoardSquare other = (BoardSquare) obj;
		if (gameObjectOnSquare == null) {
			if (other.gameObjectOnSquare != null)
				return false;
		} else if (!gameObjectOnSquare.equals(other.gameObjectOnSquare))
			return false;
		return true;
	}

}
