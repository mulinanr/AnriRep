package de.hhu.propra14.team102.server;

import java.net.*;
import java.io.*;

/**
 * Class to control communication with client from server side.
 * 
 * @author anrio
 *
 */
public class PlayerServerHandler extends Thread {
	private Socket clientSocket;
	private String clientAddress;
	private int clientPort;

	private PlayerGroup pGroup;

	/**
	 * Constructor
	 * 
	 * @param s				Client socket
	 * @param playerGroup	PlayerGroup object to handle client.
	 */
	public PlayerServerHandler(Socket s, PlayerGroup playerGroup) {
		this.pGroup = playerGroup;
		clientSocket = s;
		clientAddress = clientSocket.getInetAddress().getHostAddress();
		clientPort = clientSocket.getPort();
		System.out.println("Client connection from (" + clientAddress + ", "
				+ clientPort + ")");
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			PrintWriter output = new PrintWriter(
					clientSocket.getOutputStream(), true);

			pGroup.addPerson(clientAddress, clientPort, "unknown", output);

			processClient(input, output);

			pGroup.delPerson(clientAddress, clientPort);
			clientSocket.close();
			System.out.println("Client (" + clientAddress + ", " + clientPort
					+ ") connection closed\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to handle communication with client.
	 * 
	 * @param in
	 * @param out
	 */
	private void processClient(BufferedReader in, PrintWriter out) {
		String line;
		boolean done = false;
		try {
			while (!done) {
				if ((line = in.readLine()) == null)
					done = true;
				else {
					System.out.println("Client (" + clientAddress + ", "
							+ clientPort + "): " + line);
					if (line.trim().equals("bye"))
						done = true;
					else if (line.trim().equals("//bye")) {
						done = true;
					}
					else
						doRequest(line, out);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to process client's request.
	 * 
	 * @param line
	 * @param out
	 */
	private void doRequest(String line, PrintWriter out) {
		//System.out.println("doRequest : " + line);

		if (line.trim().toLowerCase().startsWith("//who")) {
			//System.out.println("Processing 'who'");
			out.println(pGroup.who());	
		} else if (line.trim().toLowerCase().startsWith("//players")) {
			//System.out.println("Processing 'players'");
			out.println(pGroup.players());			
		} else if (line.trim().toLowerCase().startsWith("//name")) {
			//System.out.println("Processing 'name'");
			out.println(pGroup.setPlayerName(clientAddress, clientPort, line));			
		} else if (line.trim().toLowerCase().startsWith("//teams")) {
			//System.out.println("Processing 'teams'");
			out.println(pGroup.teams());			
		} else if (line.trim().toLowerCase().startsWith("//ready")) {
			//System.out.println("Processing 'ready'");
			out.println(pGroup.setReady(clientAddress, clientPort));			
		} else if (line.trim().toLowerCase().startsWith("//spectator")) {
			//System.out.println("Processing 'spectator'");
			out.println(pGroup.addSpectator(clientAddress, clientPort));			
		} else if (line.trim().toLowerCase().startsWith("//create team")) {
			//System.out.println("Processing 'create team'");
			out.println(pGroup.createTeam(clientAddress, clientPort));			
		} else if (line.trim().toLowerCase().startsWith("//add to team")) {
			//System.out.println("Processing 'add to team'");
			out.println(pGroup.addToTeam(clientAddress, clientPort, line));	
			
		} else if (line.trim().toLowerCase().startsWith("//start")) {
			//System.out.println("Processing 'start' " + clientPort);
			pGroup.startGame(clientAddress, clientPort, line);			
		} else if (line.trim().toLowerCase().startsWith("//game_over")) {
			//System.out.println("Processing 'game over'");
			pGroup.gameOver(clientAddress, clientPort, line);			
			
		} else if (line.trim().toLowerCase().startsWith("//chat")) {
			//System.out.println("Processing 'chat'");
			pGroup.broadcast(clientAddress, clientPort, line.substring(8));
			
		} else if (line.trim().toLowerCase().startsWith("//weapon")) {
			//System.out.println("Processing '//weapon' " + line);
			pGroup.sendGameCommand(clientAddress, clientPort, line);
		} else if (line.trim().toLowerCase().startsWith("//move_left")) {
			//System.out.println("Processing '//move_left'");
			pGroup.sendGameCommand(clientAddress, clientPort, line);
		} else if (line.trim().toLowerCase().startsWith("//move_right")) {
			//System.out.println("Processing '//move_right'");
			pGroup.sendGameCommand(clientAddress, clientPort, line);
		} else if (line.trim().toLowerCase().startsWith("//stop_move")) {
			//System.out.println("Processing '//stop_move'");
			pGroup.sendGameCommand(clientAddress, clientPort, line);
		} else if (line.trim().toLowerCase().startsWith("//jump")) {
			//System.out.println("Processing '//jump'");
			pGroup.sendGameCommand(clientAddress, clientPort, line);
		} else if (line.trim().toLowerCase().startsWith("//fire")) {
			//System.out.println("Processing '//fire' " + line);
			pGroup.sendGameCommand(clientAddress, clientPort, line);
		} else if (line.trim().toLowerCase().startsWith("//item")) {
			//System.out.println("Processing '//item' " + line);
			pGroup.sendGameCommand(clientAddress, clientPort, line);
		} else if (line.trim().toLowerCase().startsWith("//game_over")) {
			//System.out.println("Processing '//game_over' " + line);
			pGroup.gameOver(line, clientPort, line);
			pGroup.sendGameCommand(clientAddress, clientPort, line);
		} else if (line.trim().toLowerCase().startsWith("//game_turn")) {
			System.out.println("Processing '//game_turn' " + line);
			pGroup.turnPlayers(clientAddress, clientPort, line);
		} else {
			System.out.println("Processing 'unknown'");
			//pGroup.turnPlayers(clientAddress, clientPort, line);
		}
	}		

}