package gui;

import game.Board;
import game.Main;
import game.Position;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import Cards.Card;
import player.ColonelMustard;
import player.MissScarlett;
import player.MrsPeacock;
import player.MrsWhite;
import player.Player;
import player.ProfessorPlum;
import player.TheReverendGreen;

/**
 *@author Wang Zhen
 */
public class GameFrame extends JFrame implements WindowListener {
	//for ask murder in suggestion
	public JRadioButton colonelMustardSU, missScarlettSU, mrsPeacockSU,
			mrsWhiteSU, professorPlumSU, theReverendGreenSU;
	public ButtonGroup charGroup;

	public JRadioButton CandlestickS, DaggerS, LeadPipeS, RevolverS, RopeS,
			SpannerS;
	public ButtonGroup weaponGroup;
	public JRadioButton ssBallRoom, ssBilliardRoomS, ssConservatoryS, ssHallS,
			ssKitchenS, ssLiabaryS, ssLoungeS, ssStudyS;
	public ButtonGroup roomGroup;
	//PANEL
	public JPanel panel;
	
	public ImageIcon red = makeImageIcon("21.png");
	public ImageIcon yellow = makeImageIcon("22.png");
	public ImageIcon white = makeImageIcon("23.png");
	public ImageIcon green = makeImageIcon("24.png");
	public ImageIcon blue = makeImageIcon("25.png");
	public ImageIcon purple = makeImageIcon("26.png");

	public ImageIcon redp = makeImageIcon("16.jpg");
	public ImageIcon yellowp = makeImageIcon("11.jpg");
	public ImageIcon whitep = makeImageIcon("14.jpg");
	public ImageIcon greenp = makeImageIcon("13.jpg");
	public ImageIcon bluep = makeImageIcon("15.jpg");
	public ImageIcon purplep = makeImageIcon("12.jpg");
	//Character card
	public ImageIcon redC = makeImageIcon("16.jpg");
	public ImageIcon yellowC = makeImageIcon("11.jpg");
	public ImageIcon whiteC = makeImageIcon("14.jpg");
	public ImageIcon greenC = makeImageIcon("13.jpg");
	public ImageIcon blueC = makeImageIcon("15.jpg");
	public ImageIcon purpleC = makeImageIcon("12.jpg");
	//WEAPON card
	public String Candlestick = "Candlestick";
	public String Dagger = "Dagger";
	public String LeadPipe = "LeadPipe";
	public String Revolver = "Revolver";
	public String Rope = "Rope";
	public String Spanner = "Spanner";

	public ImageIcon CandlestickP = makeImageIcon("weapon.jpg");
	public ImageIcon DaggerP = makeImageIcon("weapon1.jpg");
	public ImageIcon LeadPipeP = makeImageIcon("weapon3.jpg");
	public ImageIcon RevolverP = makeImageIcon("weapon6.jpg");
	public ImageIcon RopeP = makeImageIcon("weapon4.jpg");
	public ImageIcon SpannerP = makeImageIcon("weapon5.jpg");
	//WEAPON image in room
	public ImageIcon iiCandlestickp = makeImageIcon("weapons.jpg");
	public ImageIcon iiDaggerP = makeImageIcon("weapons1.jpg");
	public ImageIcon iiLeadPipeP = makeImageIcon("weapons3.jpg");
	public ImageIcon iiRevolverP = makeImageIcon("weapons6.jpg");
	public ImageIcon iiRopeP = makeImageIcon("weapons4.jpg");
	public ImageIcon iiSpannerP = makeImageIcon("weapons5.jpg");
	public JLabel rrcand = new JLabel(iiCandlestickp);
	public JLabel rrrope = new JLabel(iiRopeP);
	public JLabel rrpipe = new JLabel(iiLeadPipeP);
	public JLabel rrRepo = new JLabel(iiRevolverP);
	public JLabel rrdagg = new JLabel(iiDaggerP);
	public JLabel rrspan = new JLabel(iiSpannerP);
	public JLabel rrl = new JLabel();
	public JLabel rro = new JLabel();
	public JLabel rrs = new JLabel();
	
	//room card
	public String BallRoom = "BallRoom";
	public String BilliardRoom = "BilliardRoom";
	public String Conservatory = "Conservatory";
	public String DiningRoom = "DiningRoom";
	public String Hall = "Hall";
	public String Kitchen = "Kitchen";
	public String Liabary = "Liabary";
	public String Lounge = "Lounge";
	public String Study = "Study";

	public ImageIcon BallRoomP = makeImageIcon("35.jpg");
	public ImageIcon BilliardRoomP = makeImageIcon("37.jpg");
	public ImageIcon ConservatoryP = makeImageIcon("36.jpg");
	public ImageIcon DiningRoomP = makeImageIcon("33.jpg");
	public ImageIcon HallP = makeImageIcon("31.jpg");
	public ImageIcon KitchenP = makeImageIcon("34.jpg");
	public ImageIcon LiabaryP = makeImageIcon("38.jpg");
	public ImageIcon LoungeP = makeImageIcon("32.jpg");
	public ImageIcon StudyP = makeImageIcon("39.jpg");

	//LABELs
	public JLabel redl = new JLabel(red);
	public JLabel yellowl = new JLabel(yellow);
	public JLabel whitel = new JLabel(white);
	public JLabel greenl = new JLabel(green);
	public JLabel bluel = new JLabel(blue);
	public JLabel purplel = new JLabel(purple);
	public int labelSize = 20;

	public ImageIcon dice1 = makeImageIcon("dice1.jpg");
	public ImageIcon dice2 = makeImageIcon("dice2.jpg");
	public ImageIcon dice3 = makeImageIcon("dice3.jpg");
	public ImageIcon dice4 = makeImageIcon("dice4.jpg");
	public ImageIcon dice5 = makeImageIcon("dice5.jpg");
	public ImageIcon dice6 = makeImageIcon("dice6.jpg");

	public JLabel dicel1 = new JLabel(dice2);
	public JLabel dicel2 = new JLabel(dice2);
	public List<JLabel> movedL = new ArrayList<JLabel>();
	public boolean waitMoving = false;
	public List<JLabel> charactersLabels = new ArrayList<JLabel>();
	public JLabel text;
	public List<JLabel> cardLabels = new ArrayList<JLabel>();
	public JDialog j;
	public JDialog jsugaskweapon;
	public JDialog jaskmurder;
	public JDialog jaskroom;
	public JDialog jaskweapon;
	
	public GameFrame() {
		this.setLocation(200, 0);
		this.setSize(1000, 800);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		panel = new ImagePanel();
		setContentPane(panel);

		panel.setOpaque(false);
		panel.setLayout(null);

		JPanel jp = (JPanel) getContentPane();
		jp.setOpaque(false);// she zhi kong jian tou ming

		//DICE
		//creates the dice button
		JButton dice = new JButton();
		dice.setMnemonic(KeyEvent.VK_D);
		dice.setText("Dice");
		dice.setBounds(550, 300, 100, 30);
		panel.add(dice);
		dice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				Game.dice();
			}
		});
		//Puts the dice to panel on specific place
		dicel1.setBounds(50, 550, 80, 80);
		dicel1.setBorder(BorderFactory.createLineBorder(Color.black));
		panel.add(dicel1);

		dicel2.setBounds(150, 550, 80, 80);
		dicel2.setBorder(BorderFactory.createLineBorder(Color.black));
		panel.add(dicel2);

		panel.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				onClick(e);
			}
		});

		//Suggestion button
		JButton suggestion = new JButton();
		suggestion.setText("Suggestion");
		suggestion.setMnemonic(KeyEvent.VK_S);
		suggestion.setBounds(550, 350, 100, 30);
		panel.add(suggestion);
		suggestion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				Game.suggestion();
				;
			}
		});
		//Accuse button
		JButton accusation = new JButton();
		accusation.setText("Accusation");
		accusation.setMnemonic(KeyEvent.VK_A);
		accusation.setBounds(550, 400, 100, 30);
		panel.add(accusation);
		accusation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				Game.accusation();
			}
		});
		//Endturn button
		JButton endTurn = new JButton();
		endTurn.setText("End Turn");
		endTurn.setMnemonic(KeyEvent.VK_T);
		endTurn.setBounds(550, 450, 100, 30);
		panel.add(endTurn);
		endTurn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				Game.endTurn();
			}
		});

		//show cards

		//window
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(this);

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

		//set text
		text = new JLabel(getImageFromName(Game.playerThisTurn.name()));
		text.setBounds(500, 0, 200, 300);
		panel.add(text);
		//show cards
		int basex = 300;
		int basey = 490;
		int with = 110;
		int hight = 200;
		for (Card c : Game.playerThisTurn.cards) {
			JLabel jl = getJlaFromCardName(c.name());
			jl.setBounds(basex, basey, with, hight);
			basex = basex + with;
			panel.add(jl);
			cardLabels.add(jl);
		}
		//weapon to room
		rrcand.setBounds(200,40,40,40);
		rrspan.setBounds(40,40,40,40);
		rrpipe.setBounds(400,40,40,40);
		rrRepo.setBounds(40,200,40,40);
		rro.setBounds(40,400,40,40);
		rrdagg.setBounds(200,400,40,40);
		rrrope.setBounds(400,250,40,40);
		rrl.setBounds(400,400,40,40);
		rrs.setBounds(400,500,40,40);

		panel.add(rrcand);
		panel.add(rrpipe);
		panel.add(rrRepo);
		panel.add(rrdagg);
		panel.add(rrrope);
		panel.add(rrspan);
		panel.add(rrl);
		panel.add(rro);
		panel.add(rro);
		setVisible(true);
	}

	/**
	 * Asks who the murderer is when the suggest button is pressed
	 * User can pick who the murderer is using the radio buttons and the button
	 */
	public void sAskMurder() {
		j = new JDialog();
		j.setVisible(true);
		j.setBounds(300, 200, 400, 400);
		//RadioButtons 
		Box cBox = Box.createVerticalBox();

		colonelMustardSU = new JRadioButton("ColonelMustard");
		missScarlettSU = new JRadioButton("MissScarlett");
		mrsPeacockSU = new JRadioButton("MrsPeacock");
		mrsWhiteSU = new JRadioButton("MrsWhite");
		professorPlumSU = new JRadioButton("ProfessorPlum");
		theReverendGreenSU = new JRadioButton("TheReverendGreen");

		charGroup = new ButtonGroup();
		//Allows only 1 button to be pressed at a time 
		charGroup.add(colonelMustardSU);
		charGroup.add(missScarlettSU);
		charGroup.add(mrsPeacockSU);
		charGroup.add(mrsWhiteSU);
		charGroup.add(professorPlumSU);
		charGroup.add(theReverendGreenSU);

		cBox.add(colonelMustardSU);
		cBox.add(missScarlettSU);
		cBox.add(mrsPeacockSU);
		cBox.add(mrsWhiteSU);
		cBox.add(professorPlumSU);
		cBox.add(theReverendGreenSU);
		cBox.setBorder(BorderFactory.createTitledBorder("Character"));
		cBox.setBounds(350, 250, 150, 170);
		JLabel textJLabel = new JLabel();
		textJLabel.setText("Who is murder?");
		textJLabel.setLocation(0, 0);
		textJLabel.setSize(400, 50);
		textJLabel.setFont(new Font("SanSerif", Font.PLAIN, 12));
		textJLabel.setHorizontalAlignment(JLabel.CENTER);
		//Creates the select button and updates the suggested murderer to the one
		// selected in the radio button
		JButton next = new JButton();
		next.setText("select");
		next.setBounds(200, 300, 100, 25);
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (colonelMustardSU.isSelected()) {
					Game.sugmurderInput = "colonelMustard";
				} else if (mrsPeacockSU.isSelected()) {
					Game.sugmurderInput = "mrsPeacock";
				} else if (missScarlettSU.isSelected()) {
					Game.sugmurderInput = "missScarlett";
				} else if (mrsWhiteSU.isSelected()) {
					Game.sugmurderInput = "mrsWhite";
				} else if (theReverendGreenSU.isSelected()) {
					Game.sugmurderInput = "theReverendGreen";
				} else if (professorPlumSU.isSelected()) {
					Game.sugmurderInput = "professorPlum";
				} else {
					Game.sugmurderInput = null;
				}
				Game.suggestion1();
				j.dispose();
			}
		});
		//add to the JDialog
		j.add(textJLabel);
		j.add(next);
		j.add(cBox);
		//
	}
	
	/**
	 * Works similar to sAskMurder except this one is used when the user accuses
	 */
	public void aAskMurder() {
		j = new JDialog();
		j.setVisible(true);
		j.setBounds(300, 200, 400, 400);
		//RadioButtons 
		Box cBox = Box.createVerticalBox();

		colonelMustardSU = new JRadioButton("ColonelMustard");
		missScarlettSU = new JRadioButton("MissScarlett");
		mrsPeacockSU = new JRadioButton("MrsPeacock");
		mrsWhiteSU = new JRadioButton("MrsWhite");
		professorPlumSU = new JRadioButton("ProfessorPlum");
		theReverendGreenSU = new JRadioButton("TheReverendGreen");

		charGroup = new ButtonGroup();
		//Allows only 1 button to be pressed at a time 
		charGroup.add(colonelMustardSU);
		charGroup.add(missScarlettSU);
		charGroup.add(mrsPeacockSU);
		charGroup.add(mrsWhiteSU);
		charGroup.add(professorPlumSU);
		charGroup.add(theReverendGreenSU);

		cBox.add(colonelMustardSU);
		cBox.add(missScarlettSU);
		cBox.add(mrsPeacockSU);
		cBox.add(mrsWhiteSU);
		cBox.add(professorPlumSU);
		cBox.add(theReverendGreenSU);
		cBox.setBorder(BorderFactory.createTitledBorder("Character"));
		cBox.setBounds(350, 250, 150, 170);
		JLabel textJLabel = new JLabel();
		textJLabel.setText("Who is murder?");
		textJLabel.setLocation(0, 0);
		textJLabel.setSize(400, 50);
		textJLabel.setFont(new Font("SanSerif", Font.PLAIN, 12));
		textJLabel.setHorizontalAlignment(JLabel.CENTER);
		//button
		JButton next = new JButton();
		next.setText("select");
		next.setBounds(200, 300, 100, 25);
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (colonelMustardSU.isSelected()) {
					Game.sugmurderInput = "colonelMustard";
				} else if (mrsPeacockSU.isSelected()) {
					Game.sugmurderInput = "mrsPeacock";
				} else if (missScarlettSU.isSelected()) {
					Game.sugmurderInput = "missScarlett";
				} else if (mrsWhiteSU.isSelected()) {
					Game.sugmurderInput = "mrsWhite";
				} else if (theReverendGreenSU.isSelected()) {
					Game.sugmurderInput = "theReverendGreen";
				} else if (professorPlumSU.isSelected()) {
					Game.sugmurderInput = "professorPlum";
				} else {
					Game.sugmurderInput = null;
				}
				Game.accusation0();
				j.dispose();
			}
		});
		//add
		j.add(textJLabel);
		j.add(next);
		j.add(cBox);
	}
	
	/**
	 *  Displays a JDialog which lets the user select a weapon during an accusation.
	 */
	public void sAskWeapon() {
		jaskweapon = new JDialog();
		jaskweapon.setVisible(true);
		jaskweapon.setBounds(300, 200, 400, 400);
		//RadioButtons 
		Box cBox = Box.createVerticalBox();

		CandlestickS = new JRadioButton("CandlestickS");
		DaggerS = new JRadioButton("DaggerS");
		LeadPipeS = new JRadioButton("LeadPipeS");
		RevolverS = new JRadioButton("RevolverS");
		RopeS = new JRadioButton("RopeS");
		SpannerS = new JRadioButton("SpannerS");

		weaponGroup = new ButtonGroup();
		//Allows only 1 button to be pressed at a time 
		weaponGroup.add(CandlestickS);
		weaponGroup.add(DaggerS);
		weaponGroup.add(LeadPipeS);
		weaponGroup.add(RevolverS);
		weaponGroup.add(RopeS);
		weaponGroup.add(SpannerS);

		cBox.add(CandlestickS);
		cBox.add(DaggerS);
		cBox.add(LeadPipeS);
		cBox.add(RevolverS);
		cBox.add(RopeS);
		cBox.add(SpannerS);

		cBox.setBorder(BorderFactory.createTitledBorder("Weapon"));
		cBox.setBounds(350, 250, 150, 170);
		JLabel textJLabel = new JLabel();
		textJLabel.setText("Which weapon?");
		textJLabel.setLocation(0, 0);
		textJLabel.setSize(400, 50);
		textJLabel.setFont(new Font("SanSerif", Font.PLAIN, 12));
		textJLabel.setHorizontalAlignment(JLabel.CENTER);
		//Creates the select button and updates the suggested weapon input in the canvas
		JButton next = new JButton();
		next.setText("select");
		next.setBounds(200, 300, 100, 25);
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (CandlestickS.isSelected()) {
					Game.sugweaponInput = Game.inputWeapon("c");
					rrcand.setBounds(Game.playerThisTurn.y*20,Game.playerThisTurn.x*20,40,40);
				} else if (DaggerS.isSelected()) {
					Game.sugweaponInput = Game.inputWeapon("d");
					rrdagg.setBounds(Game.playerThisTurn.y*20,Game.playerThisTurn.x*20,40,40);
				} else if (LeadPipeS.isSelected()) {
					Game.sugweaponInput = Game.inputWeapon("l");
					rrpipe.setBounds(Game.playerThisTurn.y*20,Game.playerThisTurn.x*20,40,40);
				} else if (RevolverS.isSelected()) {
					Game.sugweaponInput = Game.inputWeapon("r");
					rrRepo.setBounds(Game.playerThisTurn.y*20,Game.playerThisTurn.x*20,40,40);
				} else if (RopeS.isSelected()) {
					Game.sugweaponInput = Game.inputWeapon("o");
					rrrope.setBounds(Game.playerThisTurn.y*20,Game.playerThisTurn.x*20,40,40);
				} else if (SpannerS.isSelected()) {
					Game.sugweaponInput = Game.inputWeapon("s");
					rrspan.setBounds(Game.playerThisTurn.y*20,Game.playerThisTurn.x*20,40,40);
				}
				else {
					Game.sugmurderInput = null;
				}
				Game.suggestion2();
				jaskweapon.dispose();
			}
		});
		//add
		jaskweapon.add(textJLabel);
		jaskweapon.add(next);
		jaskweapon.add(cBox);
	}
	
	/**
	 * Similar to sAskWeapon() but this is used during an accusation
	 */
	public void aAskWeapon() {
		jaskweapon = new JDialog();
		jaskweapon.setVisible(true);
		jaskweapon.setBounds(300, 200, 400, 400);
		//RadioButtons 
		Box cBox = Box.createVerticalBox();

		CandlestickS = new JRadioButton("CandlestickS");
		DaggerS = new JRadioButton("DaggerS");
		LeadPipeS = new JRadioButton("LeadPipeS");
		RevolverS = new JRadioButton("RevolverS");
		RopeS = new JRadioButton("RopeS");
		SpannerS = new JRadioButton("SpannerS");

		weaponGroup = new ButtonGroup();
		//Allows only 1 button to be pressed at a time 
		weaponGroup.add(CandlestickS);
		weaponGroup.add(DaggerS);
		weaponGroup.add(LeadPipeS);
		weaponGroup.add(RevolverS);
		weaponGroup.add(RopeS);
		weaponGroup.add(SpannerS);

		cBox.add(CandlestickS);
		cBox.add(DaggerS);
		cBox.add(LeadPipeS);
		cBox.add(RevolverS);
		cBox.add(RopeS);
		cBox.add(SpannerS);

		cBox.setBorder(BorderFactory.createTitledBorder("Weapon"));
		cBox.setBounds(350, 250, 150, 170);
		JLabel textJLabel = new JLabel();
		textJLabel.setText("Which weapon?");
		textJLabel.setLocation(0, 0);
		textJLabel.setSize(400, 50);
		textJLabel.setFont(new Font("SanSerif", Font.PLAIN, 12));
		textJLabel.setHorizontalAlignment(JLabel.CENTER);
		//button
		JButton next = new JButton();
		next.setText("select");
		next.setBounds(200, 300, 100, 25);
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (CandlestickS.isSelected()) {
					Game.sugweaponInput = Game.inputWeapon("c");
				} else if (DaggerS.isSelected()) {
					Game.sugweaponInput = Game.inputWeapon("d");
				} else if (LeadPipeS.isSelected()) {
					Game.sugweaponInput = Game.inputWeapon("l");
				} else if (RevolverS.isSelected()) {
					Game.sugweaponInput = Game.inputWeapon("r");
				} else if (RopeS.isSelected()) {
					Game.sugweaponInput = Game.inputWeapon("o");
				} else if (SpannerS.isSelected()) {
					Game.sugweaponInput = Game.inputWeapon("s");
				}
				else {
					Game.sugmurderInput = null;
				}
				Game.accusation1();
				jaskweapon.dispose();
			}
		});
		//add
		jaskweapon.add(textJLabel);
		jaskweapon.add(next);
		jaskweapon.add(cBox);
	}
	
	/**
	 *  Displays a JDialog which lets the user select a room during an accusation.
	 */
	public void aAskRoom() {
		jaskroom = new JDialog();
		jaskroom.setVisible(true);
		jaskroom.setBounds(300, 200, 400, 400);
		//RadioButtons 
		Box cBox = Box.createVerticalBox();

		ssBallRoom = new JRadioButton(BallRoom);
		ssBilliardRoomS = new JRadioButton(BilliardRoom);
		ssConservatoryS = new JRadioButton(Conservatory);
		ssHallS = new JRadioButton(Hall);
		ssKitchenS = new JRadioButton(Kitchen);
		ssLiabaryS= new JRadioButton(Liabary);
		ssLoungeS= new JRadioButton(Lounge);
		ssStudyS= new JRadioButton(Study);
		
		
		roomGroup = new ButtonGroup();
		//Allows only 1 button to be pressed at a time 
		roomGroup.add(ssBallRoom);
		roomGroup.add(ssBilliardRoomS);
		roomGroup.add(ssConservatoryS);
		roomGroup.add(ssHallS);
		roomGroup.add(ssKitchenS);
		roomGroup.add(ssLiabaryS);
		roomGroup.add(ssLoungeS);
		roomGroup.add(ssStudyS);

		cBox.add(ssBallRoom);
		cBox.add(ssBilliardRoomS);
		cBox.add(ssConservatoryS);
		cBox.add(ssHallS);
		cBox.add(ssKitchenS);
		cBox.add(ssLiabaryS);
		cBox.add(ssLoungeS);
		cBox.add(ssStudyS);

		cBox.setBorder(BorderFactory.createTitledBorder("Room"));
		cBox.setBounds(350, 250, 150, 170);
		JLabel textJLabel = new JLabel();
		textJLabel.setText("Which room?");
		textJLabel.setLocation(0, 0);
		textJLabel.setSize(400, 50);
		textJLabel.setFont(new Font("SanSerif", Font.PLAIN, 12));
		textJLabel.setHorizontalAlignment(JLabel.CENTER);
		//Creates the select button and updates sugRoomInput in the canvas
		JButton next = new JButton();
		next.setText("select");
		next.setBounds(200, 300, 100, 25);
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				if (ssBallRoom.isSelected()) {
					Game.sugroomInput = Game.inputRoom("b");
				} else if (ssBilliardRoomS.isSelected()) {
					Game.sugroomInput =  Game.inputRoom("i");
				} else if (ssConservatoryS.isSelected()) {
					Game.sugroomInput =  Game.inputRoom("c");
				} else if (ssHallS.isSelected()) {
					Game.sugroomInput =  Game.inputRoom("h");
				} else if (ssKitchenS.isSelected()) {
					Game.sugroomInput =  Game.inputRoom("k");
				} else if (ssLiabaryS.isSelected()) {
					Game.sugroomInput =  Game.inputRoom("l");
				}else if (ssLoungeS.isSelected()) {
					Game.sugroomInput =  Game.inputRoom("o");
				}else if (ssStudyS.isSelected()) {
					Game.sugroomInput =  Game.inputRoom("s");
				}
				else {
					Game.sugroomInput = null;
				}
				Game.accusation2();
				jaskroom.dispose();
			}
		});
		//add
		jaskroom.add(textJLabel);
		jaskroom.add(next);
		jaskroom.add(cBox);
	}

	/**
	 * Prints the cards the current player has.
	 */
	public void printCard() {
		//cleans the cards displayed from previous player
		for (JLabel jl : cardLabels) {
			panel.remove(jl);
		}
		cardLabels = new ArrayList<JLabel>();
		//updates the hand
		int basex = 300;
		int basey = 490;
		int with = 110;
		int hight = 200;
		for (Card c : Game.playerThisTurn.cards) {
			JLabel jl = getJlaFromCardName(c.name());
			jl.setBounds(basex, basey, with, hight);
			basex = basex + with;
			panel.add(jl);
			cardLabels.add(jl);
		}
		validate();
		repaint();
	}

	private JLabel getJlaFromCardName(String name) {
		if (name.equals(BallRoom)) {
			return new JLabel(BallRoomP);
		} else if (name.equals(BilliardRoom)) {
			return new JLabel(BilliardRoomP);
		} else if (name.equals(Conservatory)) {
			return new JLabel(CandlestickP);
		} else if (name.equals(DiningRoom)) {
			return new JLabel(DaggerP);
		} else if (name.equals(Hall)) {
			return new JLabel(HallP);
		} else if (name.equals(Kitchen)) {
			return new JLabel(KitchenP);
		} else if (name.equals(Liabary)) {
			return new JLabel(LiabaryP);
		} else if (name.equals(Lounge)) {
			return new JLabel(LoungeP);
		} else if (name.equals(Study)) {
			return new JLabel(StudyP);
			//weapon
		} else if (name.equals(Candlestick)) {
			return new JLabel(CandlestickP);
		} else if (name.equals(Dagger)) {
			return new JLabel(DaggerP);
		} else if (name.equals(LeadPipe)) {
			return new JLabel(LeadPipeP);
		} else if (name.equals(Revolver)) {
			return new JLabel(RevolverP);
		} else if (name.equals(Rope)) {
			return new JLabel(RopeP);
		} else if (name.equals(Spanner)) {
			return new JLabel(SpannerP);
			//character
		} else if (name.equalsIgnoreCase(Game.YELLOW)) {
			return new JLabel(yellowC);
		} else if (name.equalsIgnoreCase(Game.GREEN)) {
			return new JLabel(greenC);
		} else if (name.equalsIgnoreCase(Game.WHITE)) {
			return new JLabel(whiteC);
		} else if (name.equalsIgnoreCase(Game.BLUE)) {
			return new JLabel(blueC);
		} else if (name.equalsIgnoreCase(Game.PRUPLU)) {
			return new JLabel(purpleC);
		} else if (name.equalsIgnoreCase(Game.RED)) {
			return new JLabel(redC);
		}
		return null;
	}
	
	/**
	 * prints the players character
	 */
	public void printInfor() {
		text.setIcon(getImageFromName(Game.playerThisTurn.name()));
		repaint();
	}

	public ImageIcon getImageFromName(String s) {
		if (s.equalsIgnoreCase(Game.RED)) {
			return redp;
		} else if (s.equalsIgnoreCase(Game.WHITE)) {
			return whitep;
		} else if (s.equalsIgnoreCase(Game.GREEN)) {
			return greenp;
		} else if (s.equalsIgnoreCase(Game.PRUPLU)) {
			return purplep;
		} else if (s.equalsIgnoreCase(Game.YELLOW)) {
			return yellowp;
		} else if (s.equalsIgnoreCase(Game.BLUE)) {
			return bluep;
		}
		return null;
	}

	/**
	 * Checks whether the X button is pressed and shows a confirmation dialog box.
	 */
	public void windowClosing(WindowEvent e) {
		// Ask the user to confirm they wanted to do this
		int r = JOptionPane.showConfirmDialog(this, new JLabel("Exit Cluedo?"),
				"Confirm Exit", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (r == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}
	
	/**
	 *  Redraws the player's character icon to a valid position clicked.
	 * @param e
	 */
	protected void onClick(MouseEvent e) {
		if (waitMoving) {
			Point pointClicked = new Point(e.getX(), e.getY());
			Position po = getPositionFromPoint(pointClicked);
			for (Position p : Game.board.positionCanMove) {
				if (po.equal(p)) {
					Game.playerThisTurn.x = po.x;
					Game.playerThisTurn.y = po.y;
					cleanMoveRange();
					printCharacter(Game.board);
				}
			}
		}
	}

	public Position getPositionFromPoint(Point pointClicked) {
		int y = (int) (pointClicked.getX() / 20);
		int x = (int) (pointClicked.getY() / 20);
		return new Position(x, y);
	}

	public void dicing(int a, int b) {
		setDice(dicel1, a);
		setDice(dicel2, b);
		waitMoving = true;
		repaint();
	}

	public void setDice(JLabel la, int a) {
		if (a == 1) {
			la.setIcon(dice1);
		} else if (a == 2) {
			la.setIcon(dice2);
		} else if (a == 3) {
			la.setIcon(dice3);
		} else if (a == 4) {
			la.setIcon(dice4);
		} else if (a == 5) {
			la.setIcon(dice5);
		} else if (a == 6) {
			la.setIcon(dice6);
		}
	}

	/**
	 * Prints the characters on the board
	 * @param board
	 */
	public void printCharacter(Board board) {
		//initialize
		for (JLabel jl : charactersLabels) {
			panel.remove(jl);
		}
		charactersLabels = new ArrayList<JLabel>();
		//print
		for (Player player : board.players) {
			JLabel label = getLabelFromPlayer(player);
			int xPo = (player.y) * labelSize;
			int yPo = (player.x) * labelSize;
			label.setBounds(xPo, yPo, labelSize, labelSize);
			panel.add(label);
			charactersLabels.add(label);
		}
		repaint();
	}

	/**
	 *  prints the possible move range of a character on the board
	 * @param board
	 * @param la
	 */
	public void printMoveRange(Board board, List<Position> la) {
		cleanMoveRange();
		for (Position p : la) {
			ImageIcon mark = makeImageIcon("mark.png");
			JLabel label = new JLabel(mark);
			int xPo = (p.y) * labelSize;
			int yPo = (p.x) * labelSize;
			label.setBounds(xPo, yPo, labelSize, labelSize);
			movedL.add(label);
			panel.add(label);
		}
		repaint();
	}

	public void cleanMoveRange() {
		for (JLabel moj : movedL) {
			panel.remove(moj);
		}
		movedL = new ArrayList<JLabel>();
		repaint();
	}

	/**
	 * returns the player's current character.
	 * @param player
	 * @return
	 */
	public JLabel getLabelFromPlayer(Player player) {
		if (player instanceof ColonelMustard) {
			return yellowl;
		} else if (player instanceof MissScarlett) {
			return redl;
		} else if (player instanceof MrsPeacock) {
			return bluel;
		} else if (player instanceof MrsWhite) {
			return whitel;
		} else if (player instanceof ProfessorPlum) {
			return purplel;
		} else if (player instanceof TheReverendGreen) {
			return greenl;
		}
		return null;
	}

	class ImagePanel extends JPanel {
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			ImageIcon icon = new ImageIcon("src/background.jpg");
			g.drawImage(icon.getImage(), 0, 0, null);
		}
	}

	private static ImageIcon makeImageIcon(String filename) {
		// using the URL means the image loads when stored
		// in a jar or expanded into individual files.
		java.net.URL imageURL = GameFrame.class.getResource(filename);
		ImageIcon icon = null;
		if (imageURL != null) {
			icon = new ImageIcon(imageURL);
		}
		return icon;
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

}
