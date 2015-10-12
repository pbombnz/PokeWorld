package ui;

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


/**
 * 
 * @contributer Prashant Bhikhu
 */
@SuppressWarnings("serial")
public class CharcterSelectDialog2 extends JDialog implements ActionListener {
	private JComboBox<String> loadedPlayersComboBox = new JComboBox<String>();
	private ArrayList<Player> loadedPlayers;
	private Player choosenPlayer = null;

	public CharcterSelectDialog2(JFrame parentFrame, ArrayList<Player> loadedPlayers) {
		super(parentFrame, "Choose your Character (From Load File)?");
		this.loadedPlayers = loadedPlayers;
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		
		setLayout(new BorderLayout());

		setSize(700, 400);
		setResizable(false);

		String[] loadedPlayersComboBoxText = new String[loadedPlayers.size()];

		for(int i = 0; i < loadedPlayers.size(); i++) {
			Player player = loadedPlayers.get(i);
			loadedPlayersComboBoxText[i] = "Player in " + player.getLocation().getRoom().getName() +"@("+player.getLocation().getX()+","+player.getLocation().getY()+") - Player Level: " + player.getPlayerLevel() + " Health: " + player.getAttack() + " Attack: "+player.getAttack();
		}
		
		add(new JLabel("Choose an Existing Character that was loaded from a save file to play as?"), BorderLayout.PAGE_START);
		add(loadedPlayersComboBox, BorderLayout.CENTER);
		
		JButton okayBtn = new JButton("Okay");
		okayBtn.addActionListener(this);

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
		CharcterSelectDialog2 dialog = new CharcterSelectDialog2(parentFrame, loadedPlayers);
		return dialog.getChoosenPlayer();
	}
}
