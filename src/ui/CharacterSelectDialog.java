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
 * This class creates a Dialog which allows clients to pick a player.
 * 
 * @author Prashant Bhikhu
 */
@SuppressWarnings("serial")
public class CharacterSelectDialog extends JDialog implements ActionListener {
	JPanel buttonsPanel = new JPanel();
	Avatar choosenAvatar = null;

	/**
	 * Creates the JDialog box for the character select
	 * 
	 * @param parentFrame The frame used for the JDialog to be centered
	 */
	private CharacterSelectDialog(JFrame parentFrame) {
		super(parentFrame, "Choose your Character?"); // Creates JDialog Box

		// Sets Functional Properties
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		// Sets Physical Properties
		setLayout(new BorderLayout());
		setSize(700, 200);
		setResizable(false);

		// Adds all Avatars loaded into a Button and add the button into a JPanel for all the buttons
		buttonsPanel.setLayout(new FlowLayout());
		try {
			for (Avatar avatar : Avatar.getAllAvatars()) {			
				JButton avatarButton = new JButton();
				avatarButton.setText(avatar.getName());
				avatarButton.setName(avatar.getName());
				avatarButton.setIcon(avatar.getDisplayPic());
				avatarButton.setToolTipText("Name:" + avatar.getName() + "\n");
				avatarButton.addActionListener(this);

				buttonsPanel.add(avatarButton);
			}
		} catch (IOException e) {
			new RuntimeException(e); // If Avatar folder isn't structured correctly an error is produced
		}
		// Add a Text Label so the Client knows what to do, to the main Dialog box
		add(new JLabel("What Character would you like to pick?"),
				BorderLayout.PAGE_START);
		// Add the button's panel to the main dialog box
		add(buttonsPanel, BorderLayout.CENTER);
		// make vision and centered
		setLocationRelativeTo(parentFrame);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {

		JButton button = (JButton) event.getSource();

		try {
			for (Avatar avatar : Avatar.getAllAvatars()) {
				if (avatar.getName().equals(button.getName())) {
					choosenAvatar = avatar;
					this.dispose();
					return;
				}
			}
		} catch (IOException e) {
			new RuntimeException(e);
		}
	}

	/**
	 * Creates the JDialog box for the character selection and returns the selected Avatar
	 * 
	 * @param parentFrame The frame used for the JDialog to be centered
	 * @return The selected Avatar, otherwise if dialog box was closed then return null
	 */
	public static Avatar Chooser(JFrame parentFrame) {
		CharacterSelectDialog dialog = new CharacterSelectDialog(parentFrame);
		return dialog.getChoosenAvatar();
	}

	
	/**
	 * Returns the Avatar chosen from the Character selections
	 * @return
	 */
	private Avatar getChoosenAvatar() {
		return choosenAvatar;
	}
}
