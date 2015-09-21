package main;
import java.awt.HeadlessException;

import javax.swing.JFrame;


@SuppressWarnings("serial")
public class GameGUI extends JFrame {
	private final int WIDTH = 800;
	private final int HIEGHT = 600;
	
	public GameGUI() throws HeadlessException {
		super();
		super.setSize(WIDTH, HIEGHT);
		
		//super.pack();
		super.setVisible(true);
	} 
	
	public static void main(String[] args) {
		new GameGUI();
	}
}
