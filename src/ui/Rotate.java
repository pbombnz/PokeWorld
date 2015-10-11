package ui;

import game.Direction;
import game.Player;
import game.objects.monster.Monster;
import game.objects.scene.Fence;
import game.objects.scene.Plant;

public class Rotate {
	
	/**
	 * let the player image turn left
	 * @param player
	 */
	public void turnPlayerImageLeft(Player player) {
		if (player.getDirection() == Direction.FACE_LEFT) {
			player.setDirection(Direction.BACK_LEFT);
		} else if (player.getDirection() == Direction.BACK_LEFT) {
			player.setDirection(Direction.BACK_RIGHT);
		} else if (player.getDirection() == Direction.BACK_RIGHT) {
			player.setDirection(Direction.FACE_RIGHT);
		} else if (player.getDirection() == Direction.FACE_RIGHT) {
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
	 * let the monster image turn left
	 * @param monster
	 */
	public void turnMonsterImageLeft(Monster monster) {
		if (monster.getDirection() == Direction.FACE_LEFT) {
			monster.setDirection(Direction.BACK_LEFT);
		} else if (monster.getDirection() == Direction.BACK_LEFT) {
			monster.setDirection(Direction.BACK_RIGHT);
		} else if (monster.getDirection() == Direction.BACK_RIGHT) {
			monster.setDirection(Direction.FACE_RIGHT);
		} else if (monster.getDirection() == Direction.FACE_RIGHT) {
			monster.setDirection(Direction.FACE_LEFT);
		}
	}
	
	
	public void turnPlantImageLeft(Plant plant) {
		if (plant.getDirection() == Direction.FACE_LEFT) {
			plant.setDirection(Direction.BACK_LEFT);
		} else if (plant.getDirection() == Direction.BACK_LEFT) {
			plant.setDirection(Direction.BACK_RIGHT);
		} else if (plant.getDirection() == Direction.BACK_RIGHT) {
			plant.setDirection(Direction.FACE_RIGHT);
		} else if (plant.getDirection() == Direction.FACE_RIGHT) {
			plant.setDirection(Direction.FACE_LEFT);
		}
	}
	
	public void turnPlantImageRight(Plant plant) {
		if (plant.getDirection() == Direction.FACE_LEFT) {
			plant.setDirection(Direction.FACE_RIGHT);
		} else if (plant.getDirection() == Direction.FACE_RIGHT) {
			plant.setDirection(Direction.BACK_RIGHT);
		} else if (plant.getDirection() == Direction.BACK_RIGHT) {
			plant.setDirection(Direction.BACK_LEFT);
		} else if (plant.getDirection() == Direction.BACK_LEFT) {
			plant.setDirection(Direction.FACE_LEFT);
		}
	}
	
	public void turnFenceImageLeft(Fence fence) {
		if (fence.getDirection() == Direction.FACE_LEFT) {
			fence.setDirection(Direction.BACK_LEFT);
		} else if (fence.getDirection() == Direction.BACK_LEFT) {
			fence.setDirection(Direction.BACK_RIGHT);
		} else if (fence.getDirection() == Direction.BACK_RIGHT) {
			fence.setDirection(Direction.FACE_RIGHT);
		} else if (fence.getDirection() == Direction.FACE_RIGHT) {
			fence.setDirection(Direction.FACE_LEFT);
		}
	}
	
	public void turnFenceImageRight(Fence fence) {
		if (fence.getDirection() == Direction.FACE_LEFT) {
			fence.setDirection(Direction.FACE_RIGHT);
		} else if (fence.getDirection() == Direction.FACE_RIGHT) {
			fence.setDirection(Direction.BACK_RIGHT);
		} else if (fence.getDirection() == Direction.BACK_RIGHT) {
			fence.setDirection(Direction.BACK_LEFT);
		} else if (fence.getDirection() == Direction.BACK_LEFT) {
			fence.setDirection(Direction.FACE_LEFT);
		}
	}

	
}
