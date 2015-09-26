package main;

import gameObjects.Item;

import java.util.List;

public class Player {
	public static int HEALTH = 100;
	
	private String name;
	private int id;
	
	private int totalHealth;
	private int healthLeft;
	
	private List<Item> inventory;
	
	//private int attack;
	Location location;
}