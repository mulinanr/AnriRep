package de.hhu.propra14.team102;

//import java.util.ArrayList;

/**
 * Class to hold arrays of worms' teams and current game turn data.
 * 
 * Class only hold the data. No any method needed.
 * All the variables are public to be accessed from other classes.
 * 
 * @author anrio
 *
 */
public class GameData {

	private int team;
	private int team1Index;
	private int team2Index;
	private int windVelocity;
	
	private double[] xTeam1;
	private double[] yTeam1;
	private int[] team1Health;
	
	private double[] xTeam2;
	private double[] yTeam2;
	private int[] team2Health;

	/**
	 * Constructor to instantiate teams arrays and game turn variables. 
	 * 
	 * The first team is always team 1.
	 * The first player index of team 1 is 0;
	 * The first player index of team 2 is 3,
	 * during the first turn it will be changed to 0.
	 * 
	 */
	public GameData() {
		xTeam1 = new double[4];
		yTeam1 = new double[4];
		team1Health = new int[4];
		
		xTeam2 = new double[4];
		yTeam2 = new double[4];
		team2Health = new int[4];
	}

	/**
	 * Getter method to get current Team number.
	 * 
	 * @return	get current Team number.
	 */
	public int getTeam() {
		return team;
	}

	/**
	 * Setter method to set current Team number.
	 * 
	 * @param team		current Team number.
	 */
	public void setTeam(int team) {
		this.team = team;
	}

	/**
	 * Getter method to get Team 1 current player number.
	 * 
	 * @return		Team 1 current player number.
	 */
	public int getTeam1Index() {
		return team1Index;
	}

	/**
	 * Setter method to set Team 1 current player number.
	 * 
	 * @param team1Index		Team 1 current player number.
	 */
	public void setTeam1Index(int team1Index) {
		this.team1Index = team1Index;
	}

	/**
	 * Getter method to get Team 2 current player number.
	 * 
	 * @return		Team 2 current player number.
	 */
	public int getTeam2Index() {
		return team2Index;
	}

	/**
	 * Setter method to set Team 2 current player number.
	 * 
	 * @param team2Index		Team 2 current player number.
	 */
	public void setTeam2Index(int team2Index) {
		this.team2Index = team2Index;
	}

	/**
	 * Getter method to get wind velocity.
	 * 
	 * @return		wind velocity.
	 */
	public int getWindVelocity() {
		return windVelocity;
	}

	/**
	 * Setter method to set wind velocity.
	 * 
	 * @param windVelocity
	 */
	public void setWindVelocity(int windVelocity) {
		this.windVelocity = windVelocity;
	}

	/**
	 * Getter method to get the X position Team 1 player
	 * 
	 * @param i		player Index
	 * @return		X position Team 1 player
	 */
	public double getxTeam1(int i) {
		return xTeam1[i];
	}

	/**
	 * Setter method to set the X position Team 1 player
	 * 
	 * @param i			player Index
	 * @param xTeam1		X position Team 1 player
	 */
	public void setxTeam1(int i, int xTeam1) {
		this.xTeam1[i] = xTeam1;
	}

	/**
	 * Getter method to get the Y position Team 1 player
	 * 
	 * @param i			player Index
	 * @return			Y position Team 1 player
	 */
	public double getyTeam1(int i) {
		return yTeam1[i];
	}

	/**
	 * Setter method to set the Y position Team 1 player
	 * 
	 * @param i			player Index
	 * @param yTeam1		Y position Team 1 player
	 */
	public void setyTeam1(int i, int yTeam1) {
		this.yTeam1[i] = yTeam1;
	}

	/**
	 * Getter method to get the X position Team 2 player
	 * 
	 * @param i			player Index
	 * @return			X position Team 2 player
	 */
	public double getxTeam2(int i) {
		return xTeam2[i];
	}

	/**
	 * Setter method to set the X position Team 1 player
	 * 
	 * @param i			player Index
	 * @param xTeam2		X position Team 2 player
	 */
	public void setxTeam2(int i, int xTeam2) {
		this.xTeam2[i] = xTeam2;
	}

	/**
	 * Getter method to get the Y position Team 2 player
	 * 
	 * @param i			player Index
	 * @return			Y position Team 2 player
	 */
	public double getyTeam2(int i) {
		return yTeam2[i];
	}

	/**
	 * Setter method to set the Y position Team 1 player
	 * 
	 * @param i			player Index
	 * @param yTeam2		Y position Team 2 player
	 */
	public void setyTeam2(int i, int yTeam2) {
		this.yTeam2[i] = yTeam2;
	}

	/**
	 * Getter method to get the Health of Team 1 player
	 * 
	 * @param i			player Index
	 * @return			Health of Team 1 player
	 */
	public int getTeam1Health(int i) {
		return team1Health[i];
	}

	/**
	 * Setter method to set the Health of Team 1 player
	 * 
	 * @param i			player Index
	 * @param health		Health of Team 1 player
	 */
	public void setTeam1Health(int i, int health) {
		this.team1Health[i] = health;
	}

	/**
	 * Getter method to get the Health of Team 2 player
	 * 
	 * @param i			player Index
	 * @return			Health of Team 2 player
	 */
	public int getTeam2Health(int i) {
		return team2Health[i];
	}

	/**
	 * Setter method to set the Health of Team 2 player
	 * 
	 * @param i			player Index
	 * @param health		Health of Team 2 player
	 */
	public void setTeam2Health(int i, int health) {
		this.team2Health[i] = health;
	}


}