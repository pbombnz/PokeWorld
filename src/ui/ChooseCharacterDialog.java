package ui;

import game.avatar.Avatar;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 
 * @author Wang Zhen 
 * @contributer Prashant Bhikhu
 */
@SuppressWarnings("serial")
public class ChooseCharacterDialog extends JDialog implements ActionListener {
	JPanel buttonsPanel = new JPanel();
	Avatar choosenAvatar = null;

	public ChooseCharacterDialog(JFrame parentFrame) {
		super(parentFrame, "Choose your Character?");
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		
		setLayout(new BorderLayout());

		setSize(700, 400);
		setResizable(false);
		
		buttonsPanel.setLayout(new FlowLayout());

		try {
			for (Avatar avatar : Avatar.getAllAvatars()) {
				JButton avatarButton = new JButton();
				avatarButton.setText(avatar.getName());
				avatarButton.setName(avatar.getName());
				avatarButton.setIcon(avatar.getNormal());
				avatarButton.addActionListener(this);
				
				buttonsPanel.add(avatarButton);
			}
		} catch (IOException e) {
			new RuntimeException(e);
		}
		
		add(new JLabel("What Character would you like to pick?"), BorderLayout.PAGE_START);
		add(buttonsPanel, BorderLayout.CENTER);

		setLocationRelativeTo(parentFrame);
		setVisible(true);
	}

	public void paintComponents(Graphics g) {
		super.paintComponents(g);
	}

	
	@Override
	public void actionPerformed(ActionEvent event) {
		
		JButton button = (JButton) event.getSource();
		
		try {
			for (Avatar avatar : Avatar.getAllAvatars()) {
				if(avatar.getName().equals(button.getName())) {
					choosenAvatar = avatar;
					this.dispose();
					return;
				}
			}
		} catch (IOException e) {
			new RuntimeException(e);
		}
	}
	
	public static Avatar Chooser(JFrame parentFrame) {
		ChooseCharacterDialog dialog = new ChooseCharacterDialog(parentFrame);
		return dialog.getChoosenAvatar();
	}
	
	public Avatar getChoosenAvatar() {
		return choosenAvatar;
	}
}
