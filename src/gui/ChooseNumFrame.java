package gui;

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

import ui.GameFrame.ImagePanel;

/**
 *@author Wang Zhen
 */
public class ChooseNumFrame extends JFrame {
	public ChooseNumFrame() {
		this.setLocation(300, 150);
		this.setSize(650, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JPanel panel = new ImagePanel();
		setContentPane(panel);

		panel.setOpaque(false);
		panel.setLayout(null);

		JPanel jp = (JPanel) getContentPane();
		jp.setOpaque(false);// she zhi kong jian tou ming
		//Text
//		JLabel textJLabel = new JLabel();
//		textJLabel.setText("How many players?");
//		textJLabel.setLocation(500, 50);
//		textJLabel.setSize(200, 50);
//		textJLabel.setFont(new Font("SanSerif", Font.PLAIN, 21));
//		textJLabel.setHorizontalAlignment(JLabel.CENTER);
//		panel.add(textJLabel);
		// Function buttons will display the amount of players needed
		JButton function1JButton = new JButton();
		function1JButton.setText("START");
		function1JButton.setBounds(250, 300, 100, 50);
		panel.add(function1JButton);
		function1JButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				Game.playerNumberLimited = 3;
				Game.mainStep2();
			}
		});

//		JButton function2JButton = new JButton();
//		function2JButton.setText("4");
//		function2JButton.setBounds(550, 200, 50, 25);
//		panel.add(function2JButton);
//		function2JButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent ev) {
//				Canvas.playerNumberLimited = 4;
//				Canvas.mainStep2();
//			}
//		});
//
//		JButton function3JButton = new JButton();
//		function3JButton.setText("5");
//		function3JButton.setBounds(550, 300, 50, 25);
//		panel.add(function3JButton);
//		function3JButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent ev) {
//				Canvas.playerNumberLimited = 5;
//				Canvas.mainStep2();
//			}
//		});
//
//		JButton function4JButton = new JButton();
//		function4JButton.setText("6");
//		function4JButton.setBounds(550, 400, 50, 25);
//		panel.add(function4JButton);
//		function4JButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent ev) {
//				Canvas.playerNumberLimited = 6;
//				Canvas.mainStep2();
//			}
//		});

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
