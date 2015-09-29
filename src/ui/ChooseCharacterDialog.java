package ui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;


import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import player.Location;
import player.MasterYi;
import ui.ChooseModeFrame.ImagePanel;

/**
 * @author Wang Zhen
 * @contributer Prashant Bhikhu
 * 
 */
@SuppressWarnings("serial")
public class ChooseCharacterDialog extends JDialog implements ActionListener {
	JPanel questionTextPanel = new JPanel();
	JPanel buttonsPanel = new JPanel();
	
	public ChooseCharacterDialog(JFrame parentFrame) {
		super(parentFrame, "Choose your Character?");
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		
		setLayout(new BorderLayout());

		
		//setLocation(300, 150);
		setSize(500, 200);
		
		buttonsPanel.setLayout(new FlowLayout());

		JButton masterYiButton = new JButton();
		masterYiButton.setName("Master Yi");
		masterYiButton.setIcon(new ImageIcon("src/warrior.png"));
		//masterYiButton.setBounds(50, 200, 150, 150);
		buttonsPanel.add(masterYiButton);//panel.add(warrior);
		masterYiButton.addActionListener(this);
		
		JButton wizJButton = new JButton();
		//wizJButton.setIcon(null);
		//panel.add(wizJButton);
		buttonsPanel.add(wizJButton);
		//wizJButton.addActionListener(this);
		
		JButton halfdJButton = new JButton();
		//halfdJButton.setIcon(icon3);
		buttonsPanel.add(halfdJButton);
		//halfdJButton.addActionListener(this);
		
		questionTextPanel.add(new JLabel("What Character would you like to pick?"));
		
		add(questionTextPanel, BorderLayout.PAGE_START);
		add(buttonsPanel, BorderLayout.CENTER);

		setVisible(true);
	}

	public void paintComponents(Graphics g) {
		super.paintComponents(g);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		JButton button = (JButton) event.getSource();
		if(button.getName().equals("Master Yi")) {

		}
		
	}
}
