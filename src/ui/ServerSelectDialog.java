package ui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * 
 * @contributer Prashant Bhikhu
 */
@SuppressWarnings("serial")
public class ServerSelectDialog extends JDialog implements ActionListener {
	private List<InetAddress> serverAddresses;
	private InetAddress selectedAddress;
	
	JComboBox<String> serversComboBox;
	JButton okayButton;

	public ServerSelectDialog(JFrame parentFrame, List<InetAddress> serverAddresses) {
		super(parentFrame, "Choose to a Server");
		
		if(serverAddresses.size() == 0) {
			JOptionPane.showMessageDialog(this, "No LAN Servers found!", "ERROR", JOptionPane.ERROR_MESSAGE);
			dispose();
			return;
		}
		
		this.serverAddresses = serverAddresses;
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		setLayout(new BorderLayout(0,10));

		String[] stringAddress = new String[serverAddresses.size()];
		for (int i = 0; i < serverAddresses.size(); i++) {
			stringAddress[i] = serverAddresses.get(i).toString();
		}
		
		serversComboBox = new JComboBox<String>(stringAddress);
		okayButton = new JButton("Connect");
		okayButton.addActionListener(this);
		
		add(new JLabel("Select a Server:"), BorderLayout.PAGE_START);	
		add(serversComboBox, BorderLayout.CENTER);
		add(okayButton, BorderLayout.PAGE_END);
		
		setSize(200, 125);
		setResizable(false);
		setLocationRelativeTo(parentFrame);
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		selectedAddress = getInetAddressByIndex(serversComboBox.getSelectedIndex());
		dispose();
	}
	
	public static InetAddress Chooser(JFrame parentFrame, List<InetAddress> addresses) {
		ServerSelectDialog dialog = new ServerSelectDialog(parentFrame, addresses);
		return dialog.getSelected();
	}
	
	public InetAddress getSelected() {
		return selectedAddress;
	}
	
	private InetAddress getInetAddressByIndex(int i) {
		if (serverAddresses.size() > 0) {
			return serverAddresses.get(i);
		}
		return null;
	}	
	
	public static void main(String[] args) throws UnknownHostException {
		List<InetAddress> d = new ArrayList<InetAddress>();
		//d.add(InetAddress.getByName("localhost"));
		//d.add(InetAddress.getByName("google.com"));
		//d.add(InetAddress.getByName("google.com"));
		System.out.println(Chooser(null, d));
	}
}
