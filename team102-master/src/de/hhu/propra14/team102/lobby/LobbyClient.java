
package de.hhu.propra14.team102.lobby;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import de.hhu.propra14.team102.GameConstants;

/**
 * Class to make networks lobby
 * 
 * idea http://www.java2s.com/Code/Java/Swing-JFC/TextAreaExample.htm
 * 
 * @author anrio
 */
public class LobbyClient  extends JFrame implements ActionListener {

	private static final long serialVersionUID = 2888413569745370132L;

	//private static final int PORT = 12345;
	//private static final String HOST = "localhost";
	
	private String host;
	private int port;
	private String name;

	private Socket socket;
	private PrintWriter output;

	private JTextArea jtaPlayerList;
	private JTextArea jtaTeam1List;
	private JTextArea jtaTeam2List;
	private JTextArea jtaChatArea;

	private JTextField jTextField;
	private JButton jbStart;
	private JButton jbReady;
	private JButton jbTeam1;
	private JButton jbTeam2;
	private JButton jbSpectator;

	/**
	 * Constructor, make Lobby Object. Player name is provided as parameter.
	 * 
	 * @param name		Player name to display.
	 */
	public LobbyClient(String name) {
		super("Netzwerk Lobby - " + name);
		this.name = name;
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.host = GameConstants.host;
		this.port = GameConstants.port;
		
		initializeGUI();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				closeLink();
			}
		});

		setSize(500, 500);
		setVisible(true);

		makeContact();
	}

	/**
	 * Method to initialize Graphic User Interface.
	 */
	private void initializeGUI() {
		Container c = getContentPane();
		c.setLayout(new BorderLayout(0, 30));

		JLabel jlPlayerList = new JLabel("Players:               ");
		//jlPlayerList.setBounds(300, 10, 200, 30);
		jtaPlayerList = new JTextArea(15, 10);
		//jtaPlayerList.setPreferredSize(new Dimension(170, 30));
		jtaPlayerList.setEditable(false);


		JLabel jlTeam1List = new JLabel("Team 1:");
		//jlTeam1List.setPreferredSize(new Dimension(200, 30));
		jtaTeam1List = new JTextArea(15, 10);
		//jlTeam1List.setPreferredSize(new Dimension(200, 30));
		jtaTeam1List.setEditable(false);

		JLabel jlTeam2List = new JLabel("              Team 2: ");
		//jlTeam2List.setPreferredSize(new Dimension(170, 30));
		jtaTeam2List = new JTextArea(15, 10);
		//jtaTeam2List.setPreferredSize(new Dimension(170, 30));
		jtaTeam2List.setEditable(false);

		JPanel pLabels = new JPanel(new BorderLayout());
		pLabels.add(jlPlayerList, "West");
		pLabels.add(jlTeam1List, "Center");
		pLabels.add(jlTeam2List, "East");

		JPanel pLists = new JPanel();
		pLists.add(jtaPlayerList, "West");
		pLists.add(jtaTeam1List, "Center");
		pLists.add(jtaTeam2List, "East");

		JPanel pUpperArea = new JPanel();
		pUpperArea.add(pLabels);
		pUpperArea.add(pLists);
		c.add(pUpperArea, "North");

		JPanel pChatArea = new JPanel();
		//pChatArea.setPreferredSize(new Dimension(200, 30));
		JLabel jlChatArea = new JLabel("Chat:");
		jtaChatArea = new JTextArea(15, 31);
		jtaChatArea.setEditable(false);
		JScrollPane jsp = new JScrollPane(jtaChatArea);
		//c.add(jlChatArea, "North");
		c.add(jsp, "Center");
		//c.add(pChatArea, "Center");

		JLabel jlMsg = new JLabel("Message: ");
		jTextField = new JTextField(15);
		jTextField.addActionListener(this);

		jbStart = new JButton("Start");
		jbStart.addActionListener(this);

		jbReady = new JButton("Ready");
		jbReady.addActionListener(this);

		jbTeam1 = new JButton("Team 1");
		jbTeam1.addActionListener(this);

		jbTeam2 = new JButton("Team 2");
		jbTeam2.addActionListener(this);

		jbSpectator = new JButton("Spectator");
		jbSpectator.addActionListener(this);



		JPanel pMessages = new JPanel(new FlowLayout());
		pMessages.add(jlMsg);
		pMessages.add(jTextField);

		JPanel pButtons = new JPanel(new GridLayout(5, 1 ));

		pButtons.add(jbReady);
		pButtons.add(jbStart);
		pButtons.add(jbTeam1);
		pButtons.add(jbTeam2);
		pButtons.add(jbSpectator);

		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.add(pMessages);
		c.add(pButtons, "East");

		c.add(p, "South");

	}

	/**
	 * Method to close network connection with Game server.
	 */
	private void closeLink() {
		try {
			output.println("//bye");
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		//System.exit(0);
	}

	/**
	 * Method to make network connection with Game server.
	 */
	private void makeContact() {
		try {
			//socket = new Socket(HOST, PORT);
			socket = new Socket(host, port);
			BufferedReader input = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			output = new PrintWriter(socket.getOutputStream(), true);
			output.println("//NAME: " + name);

			new LobbyWatcher(this, input).start();
		} catch (Exception e) {
			System.out.println("Connection error");
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jbStart) {
			performButtonStartFunction();
		} else if (e.getSource() == jbReady) {
			performButtonReadyFunction();
		} else if (e.getSource() == jbTeam1) {
			performButtonTeam1Function();
		} else if (e.getSource() == jbTeam2) {
			performButtonTeam2Function();
		} else if (e.getSource() == jbSpectator) {
			performButtonSpectatorFunction();
		} else if (e.getSource() == jTextField)
			sendMessage();
	}

	/**
	 * Method to process Button 1 pressed event.
	 */
	private void performButtonStartFunction() {
		//System.out.println("Button1 pressed");
		output.println("//start");
	}

	/**
	 * Method to process Button 2 pressed event.
	 */
	private void performButtonReadyFunction() {
		//System.out.println("Button2 pressed");
		output.println("//ready");
	}

	/**
	 * Method to process Button 3 pressed event.
	 */
	private void performButtonTeam1Function() {
		//System.out.println("Button3 pressed");
		output.println("//add to team 1");
	}

	/**
	 * Method to process Button 3 pressed event.
	 */
	private void performButtonTeam2Function() {
		//System.out.println("Button3 pressed");
		output.println("//add to team 2");
	}


	/**
	 * Method to process Button 3 pressed event.
	 */
	private void performButtonSpectatorFunction() {
		//System.out.println("Button3 pressed");
		output.println("//spectator");
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * Method to send text or chat message to game server.
	 */
	private void sendMessage() {
		String message = jTextField.getText().trim();

		if (message.equals("")) {
			JOptionPane.showMessageDialog(null, "No message entered",
					"Send Message Error", JOptionPane.ERROR_MESSAGE);
		} else if (message.startsWith("//")) {
			output.println(message);
		}else {
			output.println("//CHAT: " + message);
		}

		jTextField.setText("");
	}

	/**
	 * Method to send data to game server.
	 * 
	 * @param data
	 */
	public void sendData(String data) {
		output.println(data);
	}

	/**
	 * Method to show received message to user.
	 * 
	 * @param message
	 */
	public void showMessage(final String message)

	{
		Runnable updateMsgsText = new Runnable() {
			public void run() {
				jtaChatArea.append(message);
				jtaChatArea.setCaretPosition(jtaChatArea.getText().length());
			}
		};
		SwingUtilities.invokeLater(updateMsgsText);
	}

	/**
	 * Method to show received PlayerList to user.
	 * 
	 * @param message
	 */
	public void showPlayers(final String message) {
		Runnable updateMsgsText = new Runnable() {
			public void run() {
				if (message.length() > 0) {
					jtaPlayerList.append(message);
				} else {
					jtaPlayerList.setText(message);
				}
				
				jtaPlayerList.setCaretPosition(jtaPlayerList.getText().length());
			}
		};
		SwingUtilities.invokeLater(updateMsgsText);
	}
	

	/**
	 * Method to show received Team 1 List to user.
	 * 
	 * @param message
	 */
	public void showTeam1(final String message)

	{
		Runnable updateMsgsText = new Runnable() {
			public void run() {
				if (message.length() > 0) {
					jtaTeam1List.append(message);
				} else {
					jtaTeam1List.setText(message);
				}
				jtaTeam1List.setCaretPosition(jtaTeam1List.getText().length());
			}
		};
		SwingUtilities.invokeLater(updateMsgsText);
	}

	/**
	 * Method to show received Team 1 List to user.
	 * 
	 * @param message
	 */
	public void showTeam2(final String message)

	{
		Runnable updateMsgsText = new Runnable() {
			public void run() {
				if (message.length() > 0) {
					jtaTeam2List.append(message);
				} else {
					jtaTeam2List.setText(message);
				}
				jtaTeam2List.setCaretPosition(jtaTeam2List.getText().length());
			}
		};
		SwingUtilities.invokeLater(updateMsgsText);
	}


	/**
	 * Method to directly run LobbyClient.
	 * 
	 * Used for test purposes.
	 * 
	 * @param args		no any parameters needed.
	 */
	public static void main(String args[]) {
		new LobbyClient("Player 1");
	}



}