package main;

public class GameTask {

	// Object ID's
	public static final int EMPTY = 0;
	public static final int GRASS = 1;
	public static final int TREE = 2;
	public static final int ROCK = 3;
	public static final int BUSH = 4;

	public static final int CHEST = 10;
	public static final int KEY = 20;
	public static final int DOOR = 30;
	public static final int BADGUY = 40;
	public static final int BOSS = 50;

	public static final int PLAYER = 99;

	private Direction dir = Direction.UP;

	public enum Direction{ UP(0), RIGHT(1), DOWN(2), LEFT(3);

	private int value;

	private Direction(int value){
		this.value = value;
	}

	public int getValue(){
		return this.value;
	}

	public static Direction leftDir(Direction dir){
		Direction newDir = UP;
		switch(dir){
		case UP:
			return LEFT;
		case LEFT:
			return DOWN;
		case DOWN:
			return RIGHT;
		case RIGHT:
			return UP;
		}
		return newDir;
	}

	public static Direction rightDir(Direction dir){
		Direction newDir = UP;
		switch(dir){
		case UP:
			return RIGHT;
		case RIGHT:
			return DOWN;
		case DOWN:
			return LEFT;
		case LEFT:
			return UP;
		}
		return newDir;
	}
	}

	public int moveUp(int playerID){
		return move(playerID, Direction.UP);
	}
	public int moveDown(int playerID){
		return move(playerID, Direction.DOWN);
	}
	public int moveLeft(int playerID){
		return move(playerID, Direction.LEFT);
	}
	public int moveRight(int playerID){
		return move(playerID, Direction.RIGHT);
	}


	private int move(int playerID, Direction direction) {
		// TODO Auto-generated method stub
	
	}

}


