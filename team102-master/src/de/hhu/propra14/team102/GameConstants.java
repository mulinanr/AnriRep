package de.hhu.propra14.team102;

import java.awt.Color;

/**
 * Class to hold constants and configuration values used in game.
 * 
 * Static variables is accessible from all classes without instantiate. So, class constructor is not needed.
 * 
 * @author anrio
 *
 */
public class GameConstants {

	// Konstanten

	/**
	 * Variable to hold game board width.
	 */
	public static final int boardWidth = 800;
	
	/**
	 * Variable to hold game board heigth.
	 */
	public static final int boardHeight = 600;

	/**
	 * Variable to hold first player's or team's name, defined via option panel.
	 */
	public static String player1Name = "Player 1";
	
	/**
	 * Variable to hold second player's or team's name, defined via option panel.
	 */
	public static String player2Name = "Player 2";
	
	/**
	 * Variable to hold level number, defined via option panel.
	 */
	public static int gameLevel = 1;

	/**
	 * Variable to hold first team's colors, defined via option panel.
	 */
	public static Color team1Color = Color.GREEN;
	
	/**
	 * Variable to hold second team's colors, defined via option panel.
	 */
	public static Color team2Color = Color.BLUE;
	
	/**
	 * Variable to hold first team's theme, defined via option panel.
	 * 
	 * Used to hold theme image and sound files.
	 */
	public static String team1Theme = "/theme1/";
	
	/**
	 * Variable to hold second team's theme, defined via option panel.
	 * 
	 * Used to hold theme image and sound files.
	 */
	public static String team2Theme = "/theme1/";


	/**
	 * Variable to define path for weapons' and items' images.
	 */
	public static String imagePath = "/images/";
	
	/**
	 * Variable to define server's host.
	 */
	public static String host = "localhost";
	
	/**
	 * Variable to define server's port.
	 */
	public static int port = 12345;

}