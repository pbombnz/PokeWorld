package ui;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
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
import ui.ChooseModeFrame.ImagePanel;

/**
 *@author Wang Zhen
 */
public class ChooseCharaFrame extends JFrame {
	JRadioButton colonelMustard, missScarlett, mrsPeacock, mrsWhite,
			professorPlum, theReverendGreen;
	ButtonGroup charGroup;
	JTextField name;

	public ChooseCharaFrame() {
		this.setLocation(300, 150);
		this.setSize(650, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JPanel panel = new ImagePanel();
		setContentPane(panel);

		panel.setOpaque(false);
		panel.setLayout(null);

		JPanel jp = (JPanel) getContentPane();
		jp.setOpaque(false);// she zhi kong jian tou ming

//		JLabel textJLabel = new JLabel();
//		textJLabel
//				.setText("Please input your name and choose the character that you want to be.");
//		textJLabel.setLocation(550, 50);
//		textJLabel.setSize(400, 50);
//		textJLabel.setFont(new Font("SanSerif", Font.PLAIN, 12));
//		textJLabel.setHorizontalAlignment(JLabel.CENTER);
//		panel.add(textJLabel);

//		//RadioButtons
//		Box cBox = Box.createVerticalBox();
//		colonelMustard = new JRadioButton("ColonelMustard");
//		missScarlett = new JRadioButton("MissScarlett");
//		mrsPeacock = new JRadioButton("MrsPeacock");
//		mrsWhite = new JRadioButton("MrsWhite");
//		professorPlum = new JRadioButton("ProfessorPlum");
//		theReverendGreen = new JRadioButton("TheReverendGreen");
//
//		charGroup = new ButtonGroup();
//		//Allows only 1 button to be pressed at a time 
//		charGroup.add(colonelMustard);
//		charGroup.add(missScarlett);
//		charGroup.add(mrsPeacock);
//		charGroup.add(mrsWhite);
//		charGroup.add(professorPlum);
//		charGroup.add(theReverendGreen);
//
//		if (contai(Game.characterCanChoose, Game.YELLOW)) {
//			cBox.add(colonelMustard);
//		}
//		if (contai(Game.characterCanChoose, Game.RED)) {
//			cBox.add(missScarlett);
//		}
//		if (contai(Game.characterCanChoose, Game.BLUE)) {
//			cBox.add(mrsPeacock);
//		}
//		if (contai(Game.characterCanChoose, Game.WHITE)) {
//			cBox.add(mrsWhite);
//		}
//		if (contai(Game.characterCanChoose, Game.PRUPLU)) {
//			cBox.add(professorPlum);
//		}
//		if (contai(Game.characterCanChoose, Game.GREEN)) {
//			cBox.add(theReverendGreen);
//		}
//		cBox.setBorder(BorderFactory.createTitledBorder("Character"));
//		cBox.setBounds(550, 200, 150, 170);
//		panel.add(cBox);
//
//		//text
//		name = new JTextField(15);
//		name.setMaximumSize(new Dimension(0, 25));
//		name.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//
//			}
//		});
//		name.setBounds(550, 130, 100, 20);
//		panel.add(name);
//		// this forces an action event to fire on every key press, so the
//		// user doesn't need to hit enter for results.

		JButton warrior = new JButton();
		Icon icon=new ImageIcon("src/warrior.png");
	    warrior.setIcon(icon);
		warrior.setBounds(50, 200, 150, 150);
		panel.add(warrior);
		warrior.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				Game.player = new MasterYi(new Location(0, 0));
				Game.characterChoosed = true;
				Game.mainStep3();
			}
		});
		
		JButton wizJButton = new JButton();
		Icon icon2=new ImageIcon("");
		wizJButton.setIcon(icon2);
		wizJButton.setBounds(200, 200, 150, 150);
		panel.add(wizJButton);
		wizJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				Game.mainStep2();
			}
		});
		
		JButton halfdJButton = new JButton();
		Icon icon3=new ImageIcon("");
		halfdJButton.setIcon(icon3);
		halfdJButton.setBounds(350, 200, 150, 150);
		panel.add(halfdJButton);
		halfdJButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				Game.mainStep2();
			}
		});
//		next.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent ev) {
//				Game.returnedName = name.getName();
//				if (colonelMustard.isSelected()) {
//					Game.returnedCharType = "colonelMustard";
//				} else if (mrsPeacock.isSelected()) {
//					Game.returnedCharType = "mrsPeacock";
//				} else if (missScarlett.isSelected()) {
//					Game.returnedCharType = "missScarlett";
//				} else if (mrsWhite.isSelected()) {
//					Game.returnedCharType = "mrsWhite";
//				} else if (theReverendGreen.isSelected()) {
//					Game.returnedCharType = "theReverendGreen";
//				} else if (professorPlum.isSelected()) {
//					Game.returnedCharType = "professorPlum";
//				} else {
//					Game.returnedCharType = null;
//				}
//				Game.mainStep3();
//			}
//		});
//		

//		//menu
//		//MenuBar
//		JMenuBar menBar = new JMenuBar();
//		JMenu game = new JMenu("Game");
//		game.setMnemonic(KeyEvent.VK_G);
//		//menus
//		menBar.add(game);
//		//menuitems
//		JMenuItem exit = new JMenuItem("Exit");
//		exit.setMnemonic(KeyEvent.VK_E); 
//		game.add(exit);
//		exit.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				System.exit(0);
//			}
//		});
//
//		JMenuItem restart = new JMenuItem("Restart");
//		restart.setMnemonic(KeyEvent.VK_R);
//		game.add(restart);
//		restart.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				Main.main(null);
//				dispose();
//			}
//		});
//
//		this.setJMenuBar(menBar);

		setVisible(true);
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
			ImageIcon icon = new ImageIcon("src/welcomepicture.png");
			g.drawImage(icon.getImage(), 0, 0, null);

		}
	}
}
