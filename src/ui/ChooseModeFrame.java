package ui;


import game.Main;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


/**
 *@author Wang Zhen
 */
public class ChooseModeFrame extends JFrame {
	public ChooseModeFrame() {
		this.setLocation(300, 150);
		this.setSize(650, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JPanel panel = new ImagePanel();
		setContentPane(panel);

		panel.setOpaque(false);
		panel.setLayout(null);

		JPanel jp = (JPanel) getContentPane();
		jp.setOpaque(false);

		JButton function1JButton = new JButton();
		function1JButton.setText("Singer Player");
		function1JButton.setBounds(250, 300, 200, 50);
		panel.add(function1JButton);
		function1JButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				Game.mainStep2();
			}
		});

		//menu
		//MenuBar
		JMenuBar menBar = new JMenuBar();
		JMenu game = new JMenu("Game");
		game.setMnemonic(KeyEvent.VK_G);
		//menus
		menBar.add(game);
		//menuitems
		JMenuItem exit = new JMenuItem("Exit");
		exit.setMnemonic(KeyEvent.VK_E);
		game.add(exit);
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});

		JMenuItem restart = new JMenuItem("Restart");
		restart.setMnemonic(KeyEvent.VK_R);
		game.add(restart);
		restart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Main.main(null);
				dispose();
			}
		});

		this.setJMenuBar(menBar);

		setVisible(true);
	}

	class ImagePanel extends JPanel {
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			ImageIcon icon = new ImageIcon("src/welcomepicture.png");
			g.drawImage(icon.getImage(), 0, 0, null);

		}
	}

}
