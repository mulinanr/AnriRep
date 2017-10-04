package de.hhu.propra14.team102.server;

import java.io.*;
import java.util.*;

import com.google.gson.Gson;

/**
 * Class control server activities in response to client requests.
 * 
 * @author anrio
 *
 */
public class PlayerGroup {
	private ArrayList<Player> players;
	private ArrayList<Player> team1;
	private ArrayList<Player> team2;
	private ArrayList<Player> spectators;

	private String currentPlayerAddress;
	private int currentPlayerPort;

	private int nextTeam;
	private int nextTeam1Player;
	private int nextTeam2Player;

	private Gson gson;

	/**
	 * Constructor, no any parameters needed.
	 */
	public PlayerGroup() {
		players = new ArrayList<Player>();
		team1 = new ArrayList<Player>();
		team2 = new ArrayList<Player>();
		spectators = new ArrayList<Player>();

		gson = new Gson();
	}

	/**
	 * Method to add new player to player's list.
	 * 
	 * @param clientAddress		client IP address.
	 * @param clientPort			client port.
	 * @param name				client name.
	 * @param output				PrintWriter to communicate with client.
	 */
	synchronized public void addPerson(String clientAddress, int clientPort,
			String name, PrintWriter output) {
		players.add(new Player(clientAddress, clientPort, name, output));
		//broadcast(clientAddress, clientPort, "Welcome to Server");
		//broadcastInfo();
	}

	/**
	 * Method to delete player from player's list.
	 * 
	 * @param clientAddress	client IP address.
	 * @param clientPort		client port.
	 */
	synchronized public void delPerson(String clientAddress, int clientPort) {
		Player c;
		for (int i = 0; i < players.size(); i++) {
			c = (Player) players.get(i);
			if (c.matches(clientAddress, clientPort)) {
				players.remove(i);
				broadcast(clientAddress, clientPort, "has disappeared");
				break;
			}
		}
	}

	/**
	 * Method to send message to all players.
	 */
	private void broadcastInfo() {

		List<String> playerList = new ArrayList<String>();
		List<String> team1List = new ArrayList<String>();
		List<String> team2List = new ArrayList<String>();

		for (int i = 0; i < players.size(); i++) {
			Player p = (Player) players.get(i);

			if (p.getName().equals("unknown")) {
				playerList.add(p.getClientAddress() + " : " + p.getClientPort());
			} else {
				playerList.add(p.getName());
			}			
		}

		for (int i = 0; i < team1.size(); i++) {
			Player p = (Player) team1.get(i);
			if (p.getName() != null) {
				team1List.add(p.getName());
			} else {
				team1List.add(p.getClientAddress() + " : " + p.getClientPort());
			}			
		}

		for (int i = 0; i < team2.size(); i++) {
			Player p = (Player) team2.get(i);
			if (p.getName() != null) {
				team2List.add(p.getName());
			} else {
				team2List.add(p.getClientAddress() + " : " + p.getClientPort());
			}			
		}

		String jsonPlayerList = gson.toJson(playerList);
		String jsonTeam1List = gson.toJson(team1List);
		String jsonTeam2List = gson.toJson(team2List);

		// saata sonum igale mangijale
		for (int i = 0; i < players.size(); i++) {
			Player p = (Player) players.get(i);
			p.sendMessage("JSON_PLAYERS: " + jsonPlayerList);
			System.out.println(("JSON_PLAYERS: " + jsonPlayerList).substring(13));
			if (team1.size() > 0) {
				p.sendMessage("JSON_TEAM1: " + jsonTeam1List);
				System.out.println(("JSON_TEAM1: " + jsonTeam1List).substring(11));
			} 
			if (team2.size() > 0) {
				p.sendMessage("JSON_TEAM2: " + jsonTeam2List);
				System.out.println(("JSON_TEAM2: " + jsonTeam2List).substring(11));
			} 
		}		
	}


	/**
	 * Method to send chat message to all players.
	 * 
	 * @param address	sender IP address.
	 * @param port		sender port.
	 * @param message	message to sent.
	 */
	synchronized public void broadcast(String address, int port, String message) {

		String playerName = "Client (" + address + ", " + port + "):";

		// Leida mangija nimi aadressi ja pordi jargi
		for (int i = 0; i < players.size(); i++) {
			Player p = (Player) players.get(i);
			if (p.getClientAddress().equals(address)
					&& p.getClientPort() == port) {
				if (!(p.getName().isEmpty()) || !(p.getName().equals("unknown"))) {
					playerName = p.getName();
					break;
				} 
			}
		}

		// saata sonum igale mangijale
		for (int i = 0; i < players.size(); i++) {
			Player p = (Player) players.get(i);
			p.sendMessage(playerName + "  " + message);
		}
	}

	/**
	 * Method to list IP and port of players in player's list.
	 * 
	 * @return		List of players as String.
	 */
	synchronized public String who() {
		Player c;
		String whoList = "WHO$$ ";
		for (int i = 0; i < players.size(); i++) {
			c = (Player) players.get(i);
			whoList += c.toString();
		}
		return whoList;
	}

	/**
	 * Method to list names of players in player's list.
	 * 
	 * @return		List of players as String.
	 */
	synchronized public String players() {
		Player p;
		String playersList = "PLAYERS: \n";

		for (int i = 0; i < players.size(); i++) {
			p = (Player) players.get(i);
			if (p.getName().equals("unknown")) {
				playersList += p.getClientAddress() + " : " + p.getClientPort()
						+ "\n";
			} else {
				playersList += p.getName() + "\n";
			}				
		}

		return playersList;
	}

	/**
	 * Method to set name for player.
	 * 
	 * @param address	player IP address.
	 * @param port		player port.
	 * @param line		String with player name.
	 * @return			String with information about operation success.
	 */
	synchronized public String setPlayerName(String address, int port,
			String line) {
		Player p;

		for (int i = 0; i < players.size(); i++) {
			p = (Player) players.get(i);
			if (p.getClientAddress().equals(address)
					&& p.getClientPort() == port) {
				p.setName(line.substring(7));
				broadcastInfo();
				return "Name " + line.substring(7) + " is set for client "
						+ address + " : " + port + "\n";
			}
		}
		return "No client found\n";
	}

	/**
	 * Method to return teams lists.
	 * 
	 * @return
	 */
	synchronized public String teams() {

		Player p, t;
		String teamList = "TEAMS: \n";

		if (team1.size() > 0) {
			teamList += "Team 1: \n";
			for (int i = 0; i < team1.size(); i++) {
				p = (Player) team1.get(i);
				if (p.getName().equals("unknown")) {
					teamList += "\t" + p.getClientAddress() + " : "
							+ p.getClientPort() + "\n";
				} else
					teamList += "\t" + p.getName() + "\n";
			}
		} else
			teamList += "Team 1 is empty\n";

		if (team2.size() > 0) {
			teamList += "Team 2: \n";
			for (int i = 0; i < team2.size(); i++) {
				t = (Player) team2.get(i);
				if (t.getName().equals("unknown")) {
					teamList += "\t" + t.getClientAddress() + " : "
							+ t.getClientPort() + "\n";
				} else
					teamList += "\t" + t.getName() + "\n";
			}
		} else
			teamList += "Team 2 is empty\n";

		return teamList;

	}

	/**
	 * Method to add player as spectator.
	 * 
	 * @param address
	 * @param port
	 * @return
	 */
	synchronized public String addSpectator(String address, int port) {

		Player p;

		for (int i = 0; i < players.size(); i++) {
			p = (Player) players.get(i);
			if (p.getClientAddress().equals(address)
					&& p.getClientPort() == port) {
				if (!p.isSpectator() && (p.getTeam() == -1)) {
					p.setSpectator(true);
					spectators.add(p);
					return "Player " + p.getName()
							+ " is added to spectator's list\n";
				} else {
					return "Player " + p.getName()
							+ " is already in spectator's list or a team's member.\n";
				}
			}
		}
		return "No client found\n";

	}

	/**
	 * Method to set player ready.
	 * 
	 * @param address
	 * @param port
	 * @return
	 */
	synchronized public String setReady(String address, int port) {
		Player p;

		for (int i = 0; i < players.size(); i++) {
			p = (Player) players.get(i);
			if (p.getClientAddress().equals(address)
					&& p.getClientPort() == port) {
				p.setReady(true);
				return "Player " + p.getName() + " is ready\n";
			}
		}
		return "No client found\n";

	}

	/**
	 * Method to create a new team.
	 * 
	 * @param address
	 * @param port
	 * @return
	 */
	synchronized public String createTeam(String address, int port) {
		Player p;

		if ((team1.size() > 0) && (team2.size() > 0))
			return "No more teams possible\n";
		else {
			for (int i = 0; i < players.size(); i++) {
				p = (Player) players.get(i);
				if (p.getClientAddress().equals(address)
						&& p.getClientPort() == port) {
					if (!p.isReady()) {
						return "Player not ready jet";
					}
					if (p.getTeam() == -1) {
						if (team1.size() == 0) {
							team1.add(p);
							p.setTeam(0);
							return "Player " + p.getName()
									+ " is added to team 1\n";
						} else {
							team2.add(p);
							p.setTeam(1);
							return "Player " + p.getName()
									+ " is added to team 2\n";
						}

					} else {
						return "Player is already added to team";
					}

				}
			}
		}
		return "No client found\n";
	}

	/**
	 * Method to add a player to team.
	 * 
	 * @param address
	 * @param port
	 * @param line
	 * @return
	 */
	synchronized public String addToTeam(String address, int port, String line) {
		Player p = null;
		boolean found = false;

		for (int i = 0; i < players.size(); i++) {
			p = (Player) players.get(i);
			if (p.getClientAddress().equals(address)
					&& p.getClientPort() == port) {
				found = true;
				break;
			}
		}

		if (!p.isReady()) {
			return "Player not ready jet";
		}

		if (found == false) {
			return "No client found\n";
		}
		
		if (p.getTeam() != -1) {
			return "Player is already added to team";
		}

		if ((line.substring(7).contains("team1"))
				|| (line.substring(7).contains("team 1"))) {
			if (team1.size() <= 4) {
				team1.add(p);
				p.setTeam(0);
				broadcastInfo();
				return "Player " + p.getName() + " is added to team 1\n";
			} else
				return "No more players possible\n";
		}

		else if ((line.substring(7).contains("team2"))
				|| (line.substring(7).contains("team 2"))) {
			if (team2.size() <= 4) {
				team2.add(p);
				p.setTeam(1);
				broadcastInfo();
				return "Player " + p.getName() + " is added to team 2\n";
			} else {
				return "No more players possible\n";
			}
		} else {
			return "No free teams found\n";
		}

	}

	/**
	 * Method to start network game.
	 * 
	 * @param address
	 * @param port
	 * @param line
	 */
	synchronized public void startGame(String address, int port, String line) {
		Player p = null;

		nextTeam = 0;
		nextTeam1Player = 0;
		nextTeam2Player = 0;

		int team = -1;

		// leida jooksev mangija
		for (int i = 0; i < players.size(); i++) {
			Player t = (Player) players.get(i);
			if (t.getClientAddress().equals(address)
					&& t.getClientPort() == port) {
				p = t;
				break;
			}
		}

		// kontrillida, et meeskonnad ei ole tuhjad
		if ((team1.size() == 0) || (team2.size() == 0)) {
			p.sendMessage("Teams are empty");
			return;
		}
		
		team = p.getTeam();
		if (team < 0) {
			p.sendMessage("Player isn't a team member.");
			return;
		}


		// jatta jooksva mangija andmed meelde
		p.setAdmin(true);
		nextTeam = (team + 1) % 2;
		currentPlayerAddress = p.getClientAddress();
		currentPlayerPort = p.getClientPort();
		
		System.out.println("Start1: " + team + " nextTeam: " + nextTeam + " nextTeam1: " + nextTeam1Player + " nextTeam2: " + nextTeam2Player);

		
		// kaivitada mang 1 meeskonna mangijatele
		for (int i = 0; i < team1.size(); i++) {
			Player t = (Player) team1.get(i);
			t.sendMessage("START " + team);
		}

		// kaivitada mang 2 meeskonna mangijatele
		for (int i = 0; i < team2.size(); i++) {
			Player t = (Player) team2.get(i);
			t.sendMessage("START " + team);
		}

		// kaivitada mang pealtvaatajatele		
		for (int i = 0; i < spectators.size(); i++) {
			Player t = (Player) spectators.get(i);
			t.sendMessage("START " + team);
		}

		// leida jargmise mangija andmed, jatta meelde
		if (team == 0) {
			for (int i = 0; i < team1.size(); i++) {
				Player t = (Player) team1.get(i);
				if (t.getClientAddress().equals(address)
						&& t.getClientPort() == port) {
					nextTeam1Player = (nextTeam1Player + 1) % team1.size();
				}
			}
		} else if (team == 1) {
			for (int i = 0; i < team2.size(); i++) {
				Player t = (Player) team2.get(i);
				if (t.getClientAddress().equals(address)
						&& t.getClientPort() == port) {
					nextTeam2Player = (nextTeam2Player + 1) % team1.size();
				}
			}
		}
		System.out.println("Start2 - nextTeam: " + nextTeam + " nextTeam1: " + nextTeam1Player + " nextTeam2: " + nextTeam2Player);

		// lubada jooksvale mangijale teha kaik
		p.sendMessage("//SET_ACTIVE");
		System.out.println("Start3:  Active player " + p.getName() + " " + currentPlayerAddress + " " + currentPlayerPort);
	}

	/**
	 * Method to make a network game turn.
	 * 
	 * @param address
	 * @param port
	 * @param line
	 */
	public void turnPlayers(String address, int port, String line) {

		Player p = null;

		// veenduda, et sonum tuli oigest mangijast
		for (int i = 0; i < players.size(); i++) {
			Player t = (Player) players.get(i);
			if (t.getClientAddress().equals(address)
					&& t.getClientPort() == port) {
				p = t;
				break;
			}
		}
		if (!(p.getClientAddress().equals(currentPlayerAddress))
				|| !(p.getClientPort() == currentPlayerPort)) {
			System.out.println("Player not allowed to make turn: " + p.getName() + " " + p.getClientAddress() + " " + p.getClientPort() + " " + currentPlayerAddress + " " + currentPlayerPort);
			return;
		}


		// saata sonum 1 meeskonnast mangijatele
		for (int i = 0; i < team1.size(); i++) {
			Player t = (Player) team1.get(i);
//			if (!(t.getClientAddress().equals(currentPlayerAddress))
//					|| !(t.getClientPort() == currentPlayerPort)) {
				t.sendMessage(line);
//			}
		}

		// saata sonum 2 meeskonnast mangijatele
		for (int i = 0; i < team2.size(); i++) {
			Player t = (Player) team2.get(i);
//			if (!(t.getClientAddress().equals(currentPlayerAddress))
//					|| !(t.getClientPort() == currentPlayerPort)) {
				t.sendMessage(line);
//			}
		}

		// saata sonum pealtvaatajatele
		for (int i = 0; i < spectators.size(); i++) {
			Player t = (Player) spectators.get(i);
			t.sendMessage(line);
		}

		// leida jargmine meeskond ja jargmine mangija
		if (nextTeam == 0) {
			p = (Player) team1.get(nextTeam1Player);
			nextTeam1Player = (nextTeam1Player + 1) % team1.size();
		} else {
			p = (Player) team2.get(nextTeam2Player);
			nextTeam2Player = (nextTeam2Player + 1) % team2.size();
		}
		nextTeam = (nextTeam + 1) % 2;
		
		System.out.println("Turn1: " + " nextTeam: " + nextTeam + " nextTeam1: " + nextTeam1Player + " nextTeam2: " + nextTeam2Player);


		// saata sonum jargmisele mangijale
		p.sendMessage("//SET_ACTIVE");

		// uuendada jooksva mangija andmed, nende jargi hiljem kontrollime
		currentPlayerAddress = p.getClientAddress();
		currentPlayerPort = p.getClientPort();
		
		System.out.println("Turn2:  Active player " + p.getName() + " " + currentPlayerAddress + " " + currentPlayerPort);

	}

	/**
	 * Method to process game over
	 * 
	 * @param clientAddress
	 * @param clientPort
	 * @param line
	 */
	public void gameOver(String clientAddress, int clientPort, String line) {
		
		// clear team 1
		for (int i = 0; i < team1.size(); i++) {
			team1.get(i).setTeam(-1);
		}
		team1.clear();
		
		// clear team 2
		for (int i = 0; i < team2.size(); i++) {
			team2.get(i).setTeam(-1);
		}
		team2.clear();
		
		// clear spectators
		for (int i = 0; i < spectators.size(); i++) {
			spectators.get(i).setSpectator(false);
		}
		spectators.clear();
		
		// clear ready flag
		for (int i = 0; i < players.size(); i++) {
			players.get(i).setReady(false); 
		}
		
		broadcastInfo();

	}

	/**
	 * Method to send game info to all players.
	 * 
	 * @param clientAddress
	 * @param clientPort
	 * @param line
	 */
	public void sendGameCommand(String clientAddress, int clientPort, String line) {
		// saata kask 1 meeskonna mangijatele
		for (int i = 0; i < team1.size(); i++) {
			Player t = (Player) team1.get(i);
			t.sendMessage(line);
		}

		// saata kask 2 meeskonna mangijatele
		for (int i = 0; i < team2.size(); i++) {
			Player t = (Player) team2.get(i);
			t.sendMessage(line);
		}

		// saata kask pealtvaatajatele		
		for (int i = 0; i < spectators.size(); i++) {
			Player t = (Player) spectators.get(i);
			t.sendMessage(line);
		}

	}
}