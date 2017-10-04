package de.hhu.propra14.team102;

//import com.sun.tools.javadoc.Start;
import de.hhu.propra14.team102.lobby.LobbyClient;
import de.hhu.propra14.team102.levels.Level;
import de.hhu.propra14.team102.levels.LevelGenerator;
import de.hhu.propra14.team102.server.LobbyServer;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Class provide the user interface to start game and choose game options.
 * 
 * It is possible to start offline game, network game, degine game options and exit game.
 * Options include game level, players' names, teams' colors and themes.
 * 
 * Created by anrio on 11/06/14.
 */
public class GameLauncher extends JFrame implements ItemListener, ActionListener {
    //JFrame f = new JFrame("Worms");
    private JButton Start = new JButton("Start Game");
    private JButton StartOn = new JButton("Start Online Game");
    private JButton Opt = new JButton("Options");
    private JButton Exit = new JButton("Exit");
    private JButton Save = new JButton("Save");

    private JComboBox cb;

    private JRadioButton team1Theme1;
    private JRadioButton team1Theme2;
    private JRadioButton team2Theme1;
    private JRadioButton team2Theme2;
    
    private JRadioButton team1Color1;
    private JRadioButton team1Color2;
    private JRadioButton team1Color3;
    
    private JRadioButton team2Color1;
    private JRadioButton team2Color2;
    private JRadioButton team2Color3;
    
    private JTextField team1Name;
    private JTextField team2Name;
    private int level = 1;
    private String PlayerName = "null";
    
    private JTextField host;
    private JTextField port;

    private CardLayout layout = new CardLayout();
    private JPanel main = new JPanel(layout);
    private JPanel p = new JPanel();
    private JPanel opt = new JPanel();
    
    
    /**
     * Class contructor, any parameters aren't needed.
     */
    public GameLauncher(){
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Panel();
        Action();
        setVisible(true);
    }

    /**
     * Main method to start Game Launcher.
     * 
     * Main method invoke default constructor of the class, without any parameters.
     * 
     * @param args	Any arguments are not needed.
     */
    public static void main(String[] args) {
        new GameLauncher();
    }

    /**
     * Class to make the launcher panel, any parameters aren't needed.
     * 
     * Launcher panel allow to start offline game, start network game,
     * initiate option panel and exit game.
     */
    public void Panel(){


        p.setLayout(null);
        Start.setBounds(350, 100, 150, 40);
        StartOn.setBounds(350, 200, 150, 40);
        Opt.setBounds(350, 300, 150, 40);
        Exit.setBounds(350, 400, 150, 40);
        p.add(Start);
        p.add(StartOn);
        p.add(Opt);
        p.add(Exit);
        main.setLayout(layout);
        optpanel();
        main.add(p);
        main.add(opt);
        getContentPane().add(main);
    }

	/**
	 * Class to make the options panel, any parameters aren't needed.
	 * 
	 * Option panel allow to define players' names, game level, 
	 * teams' themes and colors.
	 */
	public void optpanel() {
		
//		JComboBox cb;
		JLabel headerLabel;
		JLabel levelNumberLabel;
		JLabel team1NameLabel;
		JLabel team2NameLabel;
		JLabel themesLabel;
		JLabel team1Theme1ImageLabel;
		JLabel team1Theme2ImageLabel;
		JLabel team2Theme1ImageLabel;
		JLabel team2Theme2ImageLabel;
	    JLabel hostLabel;
	    JLabel portLabel;

		ButtonGroup teams1Themes;
		ButtonGroup teams1Color;

		ButtonGroup teams2Themes;
		ButtonGroup teams2Color;
		
		opt.setLayout(null);

		headerLabel = new JLabel("Game Options");
		headerLabel.setFont(new Font("Tahoma", Font.BOLD, 25));
		headerLabel.setBounds(300, 10, 200, 30);

		team1NameLabel = new JLabel("Player 1 Name");
		team1NameLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		team1NameLabel.setBounds(100, 70, 180, 20);

		team1Name = new JTextField("Madu 1", 20);
		team1Name.setFont(new Font("Tahoma", Font.PLAIN, 15));
		team1Name.setBounds(100, 100, 180, 30);

		team2NameLabel = new JLabel("Player 2 Name");
		team2NameLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		team2NameLabel.setBounds(500, 70, 180, 20);

		team2Name = new JTextField("Uss 2", 20);
		team2Name.setFont(new Font("Tahoma", Font.PLAIN, 15));
		team2Name.setBounds(500, 100, 180, 30);

		levelNumberLabel = new JLabel("Level Number");
		levelNumberLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		levelNumberLabel.setBounds(300, 170, 180, 20);

		String comboBoxItems[] = { "Generated Level", "Level 1", "Level 2",
				"Level 3" };
		cb = new JComboBox<String>(comboBoxItems);
		cb.setSelectedIndex(1);
		cb.setFont(new Font("Tahoma", Font.PLAIN, 20));
		cb.setBounds(300, 200, 200, 50);
		cb.setEditable(false);
		cb.addItemListener(this);

		themesLabel = new JLabel("Teams' Themes and Colors");
		themesLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		themesLabel.setBounds(280, 280, 180, 20);

		ImageIcon iiTheme1 = new ImageIcon(this.getClass().getResource(
				"worms1.png"));
		team1Theme1ImageLabel = new JLabel(iiTheme1);
		team1Theme1ImageLabel.setBounds(30, 300, 100, 50);		
		team2Theme1ImageLabel = new JLabel(iiTheme1);
		team2Theme1ImageLabel.setBounds(430, 300, 100, 50);

		ImageIcon iiTheme2 = new ImageIcon(this.getClass().getResource(
				"worms2.png"));
		team1Theme2ImageLabel = new JLabel(iiTheme2);
		team1Theme2ImageLabel.setBounds(30, 360, 100, 50);
		team2Theme2ImageLabel = new JLabel(iiTheme2);
		team2Theme2ImageLabel.setBounds(430, 360, 100, 50);


		team1Theme1 = new JRadioButton("Theme 1", false);
		team1Theme1.setBounds(100, 300, 100, 50);
		team1Theme2 = new JRadioButton("Theme 2", true);
		team1Theme2.setBounds(100, 360, 100, 50);

		team2Theme1 = new JRadioButton("Theme 1", true);
		team2Theme1.setBounds(500, 300, 100, 50);
		team2Theme2 = new JRadioButton("Theme 2", false);
		team2Theme2.setBounds(500, 360, 100, 50);

		teams1Themes = new ButtonGroup();
		teams1Themes.add(team1Theme1);
		teams1Themes.add(team1Theme2);

		teams2Themes = new ButtonGroup();
		teams2Themes.add(team2Theme1);
		teams2Themes.add(team2Theme2);

		team1Color1 = new JRadioButton("Green", true);
		team1Color1.setForeground(Color.GREEN);
		team1Color1.setBounds(200, 300, 100, 50);

		team1Color2 = new JRadioButton("Blue", false);
		team1Color2.setForeground(Color.BLUE);
		team1Color2.setBounds(200, 330, 100, 50);

		team1Color3 = new JRadioButton("Black", false);
		team1Color3.setForeground(Color.BLACK);
		team1Color3.setBounds(200, 360, 100, 50);

		teams1Color = new ButtonGroup();
		teams1Color.add(team1Color1);
		teams1Color.add(team1Color2);
		teams1Color.add(team1Color3);

		team2Color1 = new JRadioButton("Green", false);
		team2Color1.setForeground(Color.GREEN);
		team2Color1.setBounds(600, 300, 100, 50);

		team2Color2 = new JRadioButton("Blue", true);
		team2Color2.setForeground(Color.BLUE);
		team2Color2.setBounds(600, 330, 100, 50);

		team2Color3 = new JRadioButton("Black", false);
		team2Color3.setForeground(Color.BLACK);
		team2Color3.setBounds(600, 360, 100, 50);

		teams2Color = new ButtonGroup();
		teams2Color.add(team2Color1);
		teams2Color.add(team2Color2);
		teams2Color.add(team2Color3);
		
		hostLabel = new JLabel("Server Host");
		hostLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		hostLabel.setBounds(100, 450, 180, 20);
		
		host = new JTextField("127.0.0.1", 20);
		host.setFont(new Font("Tahoma", Font.PLAIN, 15));
		host.setBounds(100, 470, 180, 30);

		portLabel = new JLabel("Server Port");
		portLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		portLabel.setBounds(100, 510, 180, 20);
		
		port = new JTextField("12345", 20);
		port.setFont(new Font("Tahoma", Font.PLAIN, 15));
		port.setBounds(100, 530, 180, 30);

		
		Save.setBounds(350, 500, 150, 40);
		Save.addActionListener(this);

		opt.add(headerLabel);
		opt.add(team1NameLabel);
		opt.add(team2NameLabel);

		opt.add(team1Name);
		opt.add(team2Name);

		opt.add(levelNumberLabel);
		opt.add(cb);

		opt.add(themesLabel);
		opt.add(team1Theme1ImageLabel);
		opt.add(team1Theme2ImageLabel);
		opt.add(team2Theme1ImageLabel);
		opt.add(team2Theme2ImageLabel);

		opt.add(team1Theme1);
		opt.add(team1Theme2);
		opt.add(team2Theme1);
		opt.add(team2Theme2);

		opt.add(team1Color1);
		opt.add(team1Color2);
		opt.add(team1Color3);
		opt.add(team2Color1);
		opt.add(team2Color2);
		opt.add(team2Color3);
		
		opt.add(hostLabel);
		opt.add(host);
		
		opt.add(portLabel);
		opt.add(port);

		opt.add(Save);

	}
	
	/**
	 * Method to start game.
	 */
	private void startGame() {
		Level l;

		if (level == 0) {
			l = (new LevelGenerator()).makeLevel();
			setVisible(false);
		} else {
			l = new Level(level);
			setVisible(false);
		}
		new WormsGame(l, this);

	}


    /**
     * Method to process button press events.
     * 
     * Process button pressed events of launcher panel and option panel.
     */
	public void Action() {
		Start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startGame();

//				Level l;
//
//				if (level == 0) {
//					l = (new LevelGenerator()).makeLevel();
//					//setVisible(false);
//				} else {
//					l = new Level(level);
//
//					// setVisible(false);
//				}
//				new WormsGame(l);

			}
		});

		StartOn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameConstants.team1Theme = "/theme1/";
				GameConstants.team2Theme = "/theme1/";
				new LobbyClient(PlayerName);
				setVisible(false);
			}
		});

		Opt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				layout.next(main);
			}
		});

		Exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				System.exit(0);
			}
		});

		Save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				layout.previous(main);
			}
		});
	}

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {
		if (e.getSource() == Save) {
			PlayerName = team1Name.getText();
			GameConstants.player1Name = team1Name.getText();
			GameConstants.player2Name = team2Name.getText();

			level = cb.getSelectedIndex();
			GameConstants.gameLevel = cb.getSelectedIndex();
			//System.out.println(PlayerName + " " + level);

			if (team1Theme1.isSelected()) {
				GameConstants.team1Theme = "/theme1/";
			} else {
				GameConstants.team1Theme = "/theme2/";
			}

			if (team1Color1.isSelected()) {
				GameConstants.team1Color = Color.GREEN;
			} else if (team1Color2.isSelected()) {
				GameConstants.team1Color = Color.BLUE;
			} else {
				GameConstants.team1Color = Color.BLACK;
			}

			if (team2Theme1.isSelected()) {
				GameConstants.team2Theme = "/theme1/";
			} else {
				GameConstants.team2Theme = "/theme2/";
			}

			if (team2Color1.isSelected()) {
				GameConstants.team2Color = Color.GREEN;
			} else if (team2Color2.isSelected()) {
				GameConstants.team2Color = Color.BLUE;
			} else {
				GameConstants.team2Color = Color.BLACK;
			}
			
			GameConstants.host = host.getText();
			GameConstants.port = new Integer(port.getText());



		}
    }




    /* (non-Javadoc)
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        //
    }
}