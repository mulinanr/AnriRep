package de.hhu.propra14.team102.server;

import java.io.*;

/**
 * Class to control player data on server.
 * 
 * @author anrio
 *
 */
public class Player {
	private String clientAddress;
	private int clientPort;
	private String name;
	private PrintWriter output;
	private boolean isReady;
	private boolean isSpectator;
	private boolean isAdmin;
	private int team;

	/**
	 * Constructor create a player object on server.
	 * 
	 * Player object is created when player make a connection with server.
	 * The object hold information about player.
	 * 
	 * @param clientAddress		client IP address
	 * @param clientPort			client port
	 * @param name				client name
	 * @param output				PrinterWriter Object to send data to client
	 */
	public Player(String clientAddress, int clientPort, String name, PrintWriter output) {
		this.clientAddress = clientAddress;
		this.clientPort = clientPort;
		this.name = name;
		this.output = output;
		
		team = -1;
	}

	/**
	 * Method compare given IP and port with client's one.
	 * 
	 * @param ca		IP address to compare.
	 * @param p		port to compare.
	 * @return		true, if give IP and port match for the client.
	 */
	public boolean matches(String ca, int p) {
		if (clientAddress.equals(ca) && (clientPort == p))
			return true;
		return false;
	}

	/**
	 * Method send message to client.
	 * 
	 * @param message	message to send.
	 */
	public void sendMessage(String message) {
		output.println(message);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return clientAddress + " & " + clientPort + " & " + name + " & ";
	}

	/**
	 * Getter method to return player name.
	 * 
	 * @return	client's name to return.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter method to set player name.
	 * 
	 * @param name	value for name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter method return PrintWritter to communicate with server.
	 * 
	 * @return		PrintWritter to communicate with server.
	 */
	public PrintWriter getOutput() {
		return output;
	}

	/**
	 * Setter method to set PrintWritter to communicate with server.
	 * 
	 * @param output		PrintWritter to set.
	 */
	public void setOutput(PrintWriter output) {
		this.output = output;
	}

	/**
	 * Method to ask whether player is spectator.
	 * 
	 * @return		true if player is spectator.
	 */
	public boolean isSpectator() {
		return isSpectator;
	}

	/**
	 * Method to set player to be spectator.
	 * 
	 * @param isSpectator	true to set spectator.
	 */
	public void setSpectator(boolean isSpectator) {
		this.isSpectator = isSpectator;
	}

	/**
	 * Getter method to get client's IP address.
	 * 
	 * @return	client's IP address.
	 */
	public String getClientAddress() {
		return clientAddress;
	}

	/**
	 * Getter method to get client's port.
	 * 
	 * @return	client's port.
	 */
	public int getClientPort() {
		return clientPort;
	}

	/**
	 * Method to ask whether player is ready.
	 * 
	 * @return	true if player is ready.
	 */
	public boolean isReady() {
		return isReady;
	}

	/**
	 * Method to set player is ready.
	 * 
	 * @param isReady	true to set ready.
	 */
	public void setReady(boolean isReady) {
		this.isReady = isReady;
	}

	/**
	 * Method to ask whether player is admin.
	 * 
	 * @return	true if player is admin.
	 */
	public boolean isAdmin() {
		return isAdmin;
	}

	/**
	 * Method to set player as admin.
	 * 
	 * @param isAdmin	true to set admin.
	 */
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	/**
	 * Method to get player's team number.
	 * 
	 * @return	team's number.
	 */
	public int getTeam() {
		return team;
	}

	/**
	 * Method to set player's team number.
	 * 
	 * @param team	number to set.
	 */
	public void setTeam(int team) {
		this.team = team;
	}
	
	

}