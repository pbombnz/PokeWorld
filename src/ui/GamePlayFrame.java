package ui;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import player.Location;
import player.MasterYi;
import player.Player;
import ui.ChooseModeFrame.ImagePanel;

/**
 *@author Wang Zhen
 */
public class GamePlayFrame extends JFrame {
	public JPanel panel;
	public JLabel touxiangLabel;
	public JLabel characterLabel;
	private int labelSize = 50;
	

	public GamePlayFrame() {
		this.setLocation(100, 100);
		this.setSize(1000, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		panel = new ImagePanel();
		setContentPane(panel);

		panel.setOpaque(false);
		panel.setLayout(null);

		JPanel jp = (JPanel) getContentPane();
		jp.setOpaque(false);// she zhi kong jian tou ming

		//add keylistener
		this.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				// don't fire an event on backspace or delete
				//fl faceleft fr faceright bl backleft br backright.
				if (e.getKeyCode() == KeyEvent.VK_W
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
				}
			}
		});

		setVisible(true);
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

	public boolean contai(List<String> li, String s) {
		for (String str : li) {
			if (str.equals(s)) {
				return true;
			}
		}
		return false;
	}

	class ImagePanel extends JPanel {
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			ImageIcon icon = new ImageIcon("src/playBG.png");
			g.drawImage(icon.getImage(), 0, 0, null);

		}
	}
}
