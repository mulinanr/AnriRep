package de.hhu.propra14.team102;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import de.hhu.propra14.team102.levels.Level;
//import de.hhu.propra14.team102.lobby.LobbyClient;
import de.hhu.propra14.team102.lobby.LobbyWatcher;

/**
 * Class to start Worm Game.
 * 
 * @author anrio
 *
 */
public class WormsGame extends JFrame{

	private WormsField wField;
	private Level gameLevel;
	private GameLauncher gLauncher;

	/**
	 * Constructor to start off-line game.
	 * 
	 * @param gameLevel		Level object, generated on random base or predefined.
	 */
	public WormsGame(Level gameLevel, GameLauncher g) {
		
		this.gLauncher = g;

		wField = new WormsField(gameLevel);
		//wField.setGameLauncer(g);
		add(wField);

		// Subject for configuration in future
        setSize(800, 600);
        setTitle("Worms");
        
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (gLauncher != null) {
					gLauncher.setVisible(true);
				}

			}
		});
        
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);        
	}

	/**
	 * Constructor to start network game.
	 * 
	 * @param level		level number to use in network game. decided by server.
	 * @param w			LobbyWatcher Object to communicate with Game server.
	 */
	public WormsGame(Level gameLevel, LobbyWatcher w, String title) {

		wField = new WormsField(gameLevel, w);

		add(wField);

		// Subject for configuration in future
        setSize(800, 600);
        setTitle("Worms - " + title);
        
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        
	}

	/**
	 * Main method to start Game.
	 * 
	 * @param args	level number to use.
	 */
	public static void main(String[] args) {
		//gameLevel = new Level(1);
		new WormsGame(new Level(1), null);
	}

	public WormsField getWormsField() {
		return wField;
	}
	
}