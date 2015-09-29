package ui;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;




import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import network.GameClient;

import com.esotericsoftware.kryonet.*;

import player.Player;

/**
 * @author Wang Zhen
 * @contributer Prashant Bhikhu
 */
@SuppressWarnings("serial")
public class GameFrame extends JFrame implements KeyListener, ActionListener {
	private static enum FRAME_STATE { CREATED_FRAME, DO_NOTHING, 
									  GAME_CONNECTING, GAME_START, GAME_NORMAL, 
									  GAME_ENDED_EXPECTED, GAME_ENDED_UNEXPECTED}; 
	
	// The Size of the Frame
	private static final int FRAME_WIDTH = 1000;
	private static final int FRAME_HEIGHT = 600;
	
	// The size of the tiles when they are displayed
	private static final int TILE_WIDTH = 64;
	private static final int TILE_HEIGHT = 64;
	
	private FRAME_STATE frameState = FRAME_STATE.CREATED_FRAME;
	private GameClient gameClient;
	
	public JPanel panel;
	public JLabel touxiangLabel;
	public JLabel characterLabel;
	private int labelSize = 50;
	

	public GameFrame() {
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		panel = new GamePanel();
		setContentPane(panel);

		panel.setOpaque(false);
		panel.setLayout(null);


		// Create Menu
		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");
		gameMenu.setMnemonic(KeyEvent.VK_G);
		
		menuBar.add(gameMenu);

		JMenuItem createGame = new JMenuItem("Create Game (As Server)");
		JMenuItem joinGame = new JMenuItem("Join Game (As Client)");
		JMenuItem exit = new JMenuItem("Exit");

		gameMenu.add(createGame);
		gameMenu.add(joinGame);
		gameMenu.add(new JSeparator());
		gameMenu.add(exit);
				
		
		createGame.addActionListener(this);
		joinGame.addActionListener(this);
		exit.addActionListener(this);
		

		setJMenuBar(menuBar);
		setVisible(true);
		
		frameState = GameFrame.FRAME_STATE.CREATED_FRAME;
	}

	public ImageIcon getCharacterImage(Player player) {
		//initialize
		if (player.direction.equals("fl")) {
			return player.faceleft;
		} else if (player.direction.equals("fr")) {
			return player.faceright;
		} else if (player.direction.equals("bl")) {
			return player.backleft;
		} else if (player.direction.equals("br")) {
			return player.backright;
		}
		return null;
	}

	public void printInformation(Player player) {
		//initialize
		if (touxiangLabel != null) {
			panel.remove(touxiangLabel);
		}
		//print touxiang
		JLabel label = new JLabel(player.touxiang);
		int xPo = 10;
		int yPo = 10;
		int touxiangSize = 200;
		label.setBounds(xPo, yPo, touxiangSize, touxiangSize);
		touxiangLabel = label;
		panel.add(touxiangLabel);
		//print name
		JLabel textJLabel = new JLabel();
		textJLabel.setText("Name: " + player.name);
		textJLabel.setLocation(40, 210);
		textJLabel.setSize(400, 20);
		textJLabel.setFont(new Font("Dialog", 1, 15));
		textJLabel.setHorizontalAlignment(JLabel.LEFT);
		panel.add(textJLabel);

		JLabel health = new JLabel();
		health.setText("Health: " + player.health);
		health.setLocation(40, 230);
		health.setSize(400, 20);
		health.setFont(new Font("SanSerif", Font.PLAIN, 15));
		health.setHorizontalAlignment(JLabel.LEFT);
		panel.add(health);

		JLabel attack = new JLabel();
		attack.setText("Attack: " + player.attack + "\n");
		attack.setLocation(40, 250);
		attack.setSize(400, 20);
		attack.setFont(new Font("SanSerif", Font.PLAIN, 15));
		attack.setHorizontalAlignment(JLabel.LEFT);
		panel.add(attack);

		repaint();
	}

	public void printCharacter(Player player) {
		//initialize
		if (characterLabel != null) {
			panel.remove(characterLabel);
		}

		//print
		JLabel chaL = null;
		if (player.direction.equals("fl")) {
			chaL = new JLabel(player.faceleft);
		} else if (player.direction.equals("fr")) {
			chaL = new JLabel(player.faceright);
		} else if (player.direction.equals("bl")) {
			chaL = new JLabel(player.backleft);
		} else if (player.direction.equals("br")) {
			chaL = new JLabel(player.backright);
		}
		int xPo = trasferX(player.location.col, player.location.row);
		int yPo = trasferY(player.location.col, player.location.row);
		int charaSize = 40;
		chaL.setBounds(xPo, yPo, charaSize, charaSize);
		characterLabel = chaL;
		panel.add(characterLabel);

		repaint();
	}

	public int trasferX(int col, int row) {
		int offset = 10;
		int edgeLong = 30;
		int base = 470;
		return (int) ((offset + edgeLong) * (col) - offset * row+ base);
	}

	public int trasferY(int col, int row) {
		int offset = 10;
		int edgeLong = 30;
		int base = 50;
		return (int) ((offset + edgeLong) * (row) + base);
	}

	
	class GamePanel extends JPanel {
		protected void paintComponent(Graphics g) {
			super.paintComponent(g); // Clears panel

			if(frameState == FRAME_STATE.CREATED_FRAME) {
				g.drawImage(new ImageIcon("./sprites/backgrounds/welcome_bg.jpg").getImage(), 0, 0, FRAME_WIDTH, FRAME_HEIGHT, null );				
				return;
			}
			
			// Draws background
			g.drawImage(new ImageIcon("./sprites/backgrounds/game_bg.jpg").getImage(), 0, 0, FRAME_WIDTH, FRAME_HEIGHT, null );
			
			
			// Initial starting position of where the first square is going to be drawn
			int yPos = FRAME_HEIGHT/2; 
			int xPos = FRAME_WIDTH/5;
					
			for (int cellY = 0; cellY < 10; cellY++){
			    for (int cellX = 9; cellX >= 0; cellX--) {
		            int tileX = xPos + (cellX * TILE_WIDTH/2);
		            int tileY = yPos - (cellX * TILE_HEIGHT/4);
		            
					g.drawImage(new ImageIcon("./sprites/tiles/grass.png").getImage(), tileX, tileY, TILE_WIDTH, TILE_HEIGHT, null );
					//g.drawString(cellY+", "+cellX, screenX+20, screenY+20); // Shows the Array dimension associated with the array
			    }
			    yPos += TILE_HEIGHT/4;
			    xPos += TILE_WIDTH/2;
			}
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		// don't fire an event on backspace or delete
		//fl faceleft fr faceright bl backleft br backright.
		/*if (e.getKeyCode() == KeyEvent.VK_W
				|| e.getKeyCode() == KeyEvent.VK_UP) {
			Game.player.goUp();
			printCharacter(Game.player);
		} else if (e.getKeyCode() == KeyEvent.VK_S
				|| e.getKeyCode() == KeyEvent.VK_DOWN) {
			Game.player.goDown();
			printCharacter(Game.player);
		} else if (e.getKeyCode() == KeyEvent.VK_A
				|| e.getKeyCode() == KeyEvent.VK_LEFT) {
			Game.player.goLeft();
			printCharacter(Game.player);
		} else if (e.getKeyCode() == KeyEvent.VK_D
				|| e.getKeyCode() == KeyEvent.VK_RIGHT) {
			Game.player.goRight();
			printCharacter(Game.player);
		}*/
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {	
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object source = arg0.getSource();
		if(source instanceof JMenuItem) {
			JMenuItem menuItem = (JMenuItem) source;
			
			if(menuItem.getText().equals("Create Game (As Server)")) {
				new ServerFrame();
				//this.dispose();
			} else if (menuItem.getText().equals("Join Game (As Client)")) {
				gameClient = new GameClient();
			}
			else if (menuItem.getText().equals("Exit")) {
				System.exit(0);
			}
		}
		
	}	
}
