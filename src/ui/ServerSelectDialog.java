package ui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * This class Generates the Dialog box that appears when the user has select
 * the LAN server they would like to connect to.
 * 
 * @author Prashant Bhikhu
 */
@SuppressWarnings("serial")
public class ServerSelectDialog extends JDialog {
	private List<InetAddress> serverAddresses; // A list of server addresses used construct the combobox
	private InetAddress selectedAddress; // the selected server address
	
	JComboBox<String> serversComboBox; // The combobox which displays all possible server connections
	JButton okayButton; // The button used to select the server that the server wants to connect to

	/**
	 * A Private method used by he static public method (which is really all the GUI needs).
	 * This constructs the JDialog box for listing the possible server we can connect to.
	 * 
	 * @param parentFrame the JFrame at which the JDialog is going appear centered on. 
	 * @param serverAddresses The List of possible servers to connect to.
	 */
	private ServerSelectDialog(JFrame parentFrame, List<InetAddress> serverAddresses) {
		// Create the JDialog itself
		super(parentFrame, "Choose to a Server");
		
		// Make the argument of the server list into field for later use
		this.serverAddresses = serverAddresses;
		
		
		// When the server list is empty, it indicates there are no LAN servers, 
		// so produce a error message to the user and don't bother showing the dialog at all.
		if(serverAddresses.isEmpty()) {
			JOptionPane.showMessageDialog(this, "No LAN Servers found!", "ERROR", JOptionPane.ERROR_MESSAGE);
			
			// Show an input dialog that allows the user to input the IP manually because it could be LAN discovery problems or multiplayer on web
			String manualIPAddress = JOptionPane.showInputDialog(this, "Enter a Server Address:", "ERROR", JOptionPane.ERROR_MESSAGE);
			// If the input box is empty, the close the the overall dialog box
			if(manualIPAddress != null) {
				// Parse the input as an IP address
				try {
					
					selectedAddress = InetAddress.getByName(manualIPAddress);
				} catch (UnknownHostException e1) {
					// Failure to parse.
					JOptionPane.showMessageDialog(this, "No server at "+manualIPAddress+"!", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
			// Regardless of the outcome above, we dont need to load the main dialog box as there are no servers
			dispose();
			return;
		}
		
		// Setting JDialog Properties (Functional)
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		setLayout(new BorderLayout(0,10));

		// Because the converting a List<InetAddress> has difficulty using .toArray(),
		// I had to manually 'convert' it to an String array so it can be added  to combobox
		String[] stringAddress = new String[serverAddresses.size()];
		for (int i = 0; i < serverAddresses.size(); i++) {
			stringAddress[i] = serverAddresses.get(i).toString();
		}
		
		// Construct Combobox based on the array generated above
		serversComboBox = new JComboBox<String>(stringAddress);
		
		// Construct the okay button
		okayButton = new JButton("Connect");
		okayButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// If the okay button is clicked, it indicates the user has selected the server they want to connect to
				
				// Get the selected server
				selectedAddress = getInetAddressByIndex(serversComboBox.getSelectedIndex());
				// Close the dialog box as its no longer required
				dispose();		
			}
		});
		// User can hit the Enter Key to instead of clicking okay
		getRootPane().setDefaultButton(okayButton); 
		
		add(new JLabel("Select a Server:"), BorderLayout.PAGE_START);	
		add(serversComboBox, BorderLayout.CENTER);
		add(okayButton, BorderLayout.PAGE_END);
		
		// Set Physical Properties (Physical)
		setSize(200, 125);
		setResizable(false);
		setLocationRelativeTo(parentFrame);
		setVisible(true);
	}
	
	/**
	 * @return the Selected InetAddress that the user picked
	 */
	private InetAddress getSelected() {
		return selectedAddress;
	}
	
	/**
	 * @param i The selected index from the combobox which will be used to retrieve the InetAddress
	 * @return The InetServer address at index i is valid, if i is not valid, then we return null
	 */
	private InetAddress getInetAddressByIndex(int i) {
		// Due to this being a private method and constructed dynamically on compile time,
		// Verification isn't needed as its assumed it will never request a index out of range.
		
		// Return the server address at index i in the server list, otherwise return null if the list is empty
		return (!serverAddresses.isEmpty() ? serverAddresses.get(i) : null); 
	}	
	
	/**
	 * Generates a JDialogBox for display and choosing a server that we can connect to.
	 * 
	 * @param parentFrame the JFrame at which the JDialog is going appear centered on. 
	 * @param serverAddresses The List of possible servers to connect to.
	 * @return Returns the selected server unless no server was selected (or dialog box was canceled) null is returned
	 */
	public static InetAddress Chooser(JFrame parentFrame, List<InetAddress> addresses) {
		ServerSelectDialog dialog = new ServerSelectDialog(parentFrame, addresses);
		return dialog.getSelected();
	}
}
