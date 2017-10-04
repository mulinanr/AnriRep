package de.hhu.propra14.team102.lobby;

import java.io.BufferedReader;
//import java.util.StringTokenizer;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import de.hhu.propra14.team102.GameData;
import de.hhu.propra14.team102.WormsField;
import de.hhu.propra14.team102.WormsGame;
import de.hhu.propra14.team102.levels.Level;

/**
 * Class to watch network messages from game server.
 * 
 * Watch for a message, analyze it and make an action.
 * 
 * @author anrio
 *
 */
public class LobbyWatcher extends Thread {
	private LobbyClient client;
	private BufferedReader input;
	private Gson gson;

	private WormsField wField;

	/**
	 * Constructor, create a watcher object.
	 * Two parameters is needed:
	 * LobbyClient to communicate with Game Board and
	 * BufferedReader to communicate with network Game Server.
	 * 
	 * @param c		LobbyClient to communicate with Game Board
	 * @param i		BufferedReader to communicate with network Game Server
	 */
	public LobbyWatcher(LobbyClient c, BufferedReader i) {
		client = c;
		input = i;

		gson = new Gson();
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		String line;
		try {
			while ((line = input.readLine()) != null) {
				//System.out.println(line);
				if (line.startsWith("START")) {					// server command to create network game field
					startGame(line.substring(6));
				} else if (line.startsWith("TEAMS:")) {			// server command to display teams
					client.showTeam1(line + "\n");
				} else if (line.startsWith("PLAYERS:")) {		// server command to display players
					client.showMessage(line.substring(7) + "\n");
				} else if (line.startsWith("JSON_PLAYERS: ")) {		// server command to display players in dedicated windows
					Type type = new TypeToken<List<String>>(){}.getType();
					List<String> playerList = gson.fromJson(line.substring(13), type);
					client.showPlayers("");
					client.showTeam1("");
					client.showTeam2("");
					for (int i = 0; i < playerList.size(); i++) {
						client.showPlayers(playerList.get(i) + "\n");
					}					
				} else if (line.startsWith("JSON_TEAM1: ")) {		// server command to display team 1 in dedicated windows
					Type type = new TypeToken<List<String>>(){}.getType();
					List<String> team1List = gson.fromJson(line.substring(11), type);
					client.showTeam1("");
					for (int i = 0; i < team1List.size(); i++) {
						client.showTeam1(team1List.get(i) + "\n");
					}					
				} else if (line.startsWith("JSON_TEAM2: ")) {		// server command to display team 2 in dedicated windows
					Type type = new TypeToken<List<String>>(){}.getType();
					List<String> team2List = gson.fromJson(line.substring(11), type);
					client.showTeam2("");
					for (int i = 0; i < team2List.size(); i++) {
						client.showTeam2(team2List.get(i) + "\n");
					}					
				} else if (line.startsWith("//SET_ACTIVE")) {		// server command to set current player active
					wField.setActive();
				} else if (line.startsWith("//WEAPON")) {			// server command to change weapon
					wField.changeWeapon(new Integer(line.substring(10)));
				} else if (line.startsWith("//MOVE_LEFT")) {		// server command to move left
					Type type = new TypeToken<List<Integer>>(){}.getType();
					List<Integer> move = gson.fromJson(line.substring(11), type);
					wField.moveLeft(move.get(0), move.get(1));
				} else if (line.startsWith("//MOVE_RIGHT")) {		// server command to move right
					Type type = new TypeToken<List<Integer>>(){}.getType();
					List<Integer> move = gson.fromJson(line.substring(12), type);
					wField.moveRight(move.get(0), move.get(1));
				} else if (line.startsWith("//STOP_MOVE")) {		// server command to move right
					Type type = new TypeToken<List<Integer>>(){}.getType();
					List<Integer> move = gson.fromJson(line.substring(11), type);
					wField.stopMove(move.get(0), move.get(1));
				} else if (line.startsWith("//JUMP")) {				// server command to jump
					Type type = new TypeToken<List<Integer>>(){}.getType();
					List<Integer> move = gson.fromJson(line.substring(6), type);
					wField.moveJump(move.get(0), move.get(1));
				} else if (line.startsWith("//ITEM")) {				// server command to create item					
					int type = new Integer(line.substring(8, 9));
					int x = new Integer(line.substring(12));
					wField.addItem(type, x);
				} else if (line.startsWith("//FIRE")) {				// server command to fire
					Type type = new TypeToken<List<Integer>>(){}.getType();
					List<Integer> fire = gson.fromJson(line.substring(7), type);
					wField.fire(fire.get(0), fire.get(1), fire.get(2), fire.get(3), fire.get(4));
				} else if (line.startsWith("//GAME_TURN")) {		// server command to make turn
					GameData gameData = gson.fromJson(line.substring(12), GameData.class);
					wField.startNetworkTurn(gameData);
				} else if (line.startsWith("//GAME_OVER")) {		// server command to game over
					int team = new Integer(line.substring(12));
					wField.gameOver(team);
				} else {
					client.showMessage(line + "\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Link to Server Lost!",
					"Connection Closed", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	/**
	 * Method to set player active.
	 * 
	 * Needed to make network turn and set certain player to active.
	 */
	private void setActive() {
		client.showMessage("Player set active\n");
		wField.setActive();
	}

	/**
	 * Method to start network game.
	 */
	public void startGame(String team) {
		client.showMessage("Game started\n");
		WormsGame wGame = new WormsGame(new Level(1), this, client.getName());
		wField = wGame.getWormsField();
		wField.setTeam(new Integer(team));
	}

	/**
	 * Method to send network game data to server.
	 * 
	 * Send data about worm move and fire to server at the end of game turn.
	 * 
	 * @param state		Network Game data.
	 */
	public void sendMove(String kindOfMove, int x, int y) {
		List<Integer> move = new ArrayList<Integer>();
		move.add(x);
		move.add(y);
		
		String movePosition = gson.toJson(move);
		client.sendData("//" + kindOfMove + movePosition);
	}

	/**
	 * Method to make network fire.
	 * 
	 * @param xTarget		X Position of target
	 * @param yTarget		Y Position of target
	 * @param type			Type of weapon
	 * @param xWorm			X Position of firing worm
	 * @param yWorm			Y Position of firing worm
	 */
	public void sendFire(int xTarget, int yTarget, int type, int xWorm, int yWorm) {
		List<Integer> fire = new ArrayList<Integer>();
		fire.add(xTarget);
		fire.add(yTarget);
		fire.add(type);
		fire.add(xWorm);
		fire.add(yWorm);

		String firePosition = gson.toJson(fire);
		client.sendData("//FIRE: " + firePosition);

	}

	/**
	 * Method to process network game turn.
	 * 
	 * @param gameData		JSON string of turn data
	 */
	public void sendTurn(GameData gameData) {
		String gameTurn = gson.toJson(gameData);
		client.sendData("//GAME_TURN: " + gameTurn);
	}

	/**
	 * Method to change weapon in network game.
	 * 
	 * @param newWeapon		new Weapon
	 */
	public void sendWeaponChange(String newWeapon) {
		client.sendData("//" + newWeapon);
		
	}

	/**
	 * Method to send game data in network game.
	 * 
	 * @param newItem	JSON object of game data
	 */
	public void sendMove(String newItem) {
		client.sendData("//" + newItem);
	}

	/**
	 * Method to process game over event in network game.
	 * 
	 * @param team		Winner team
	 */
	public void sendGameOver(int team) {
		client.sendData("//GAME_OVER: " + team);
		
	}



}