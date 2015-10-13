package ui;

import game.Location;
import game.Player;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import rooms.Room1;


/**
 * this class is for loadfile
 * @contributer Prashant Bhikhu
 */
@SuppressWarnings("serial")
public class CharacterSelectDialog_LoadFile extends JDialog implements ActionListener {
	private JComboBox<String> loadedPlayersComboBox = new JComboBox<String>();
	private ArrayList<Player> loadedPlayers;
	private Player choosenPlayer = null;

	public CharacterSelectDialog_LoadFile(JFrame parentFrame, ArrayList<Player> loadedPlayers) {
		super(parentFrame, "Choose your Character (From Load File)?");
		this.loadedPlayers = loadedPlayers;
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		
		setLayout(new BorderLayout());

		setSize(600, 100);
		setResizable(false);

		String[] loadedPlayersComboBoxText = new String[loadedPlayers.size()];

		for(int i = 0; i < loadedPlayers.size(); i++) {
			Player player = loadedPlayers.get(i);
			String playerInfo = "Player in " + player.getLocation().getRoom().getName() +"@("+player.getLocation().getX()+","+player.getLocation().getY()+") - Player Level: " + player.getPlayerLevel() + " Health: " + player.getAttack() + " Attack: "+player.getAttack();
			loadedPlayersComboBox.addItem(playerInfo);
		}

		
		JButton okayBtn = new JButton("Okay");
		okayBtn.addActionListener(this);
		
		add(new JLabel("Choose an Existing Character that was loaded from a save file to play as?"), BorderLayout.PAGE_START);
		add(loadedPlayersComboBox, BorderLayout.CENTER);
		add(okayBtn, BorderLayout.LINE_END);

		setLocationRelativeTo(parentFrame);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		choosenPlayer = loadedPlayers.get(loadedPlayersComboBox.getSelectedIndex());
		this.dispose();
	}
	
	private Player getChoosenPlayer() {
		return choosenPlayer;
	}
	
	public static Player Chooser(JFrame parentFrame, ArrayList<Player> loadedPlayers) {
		CharacterSelectDialog_LoadFile dialog = new CharacterSelectDialog_LoadFile(parentFrame, loadedPlayers);
		return dialog.getChoosenPlayer();
	}
}
