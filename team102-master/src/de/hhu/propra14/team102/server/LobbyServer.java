package de.hhu.propra14.team102.server;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class to make a network game server.
 * 
 * @author anrio
 */
public class LobbyServer {	
	//private static final int PORT = 12345; 
	private PlayerGroup playerGroup;
	
	/**
	 * Constructor to make Server class.
	 * 
	 * Method make a network listener and accept connections.
	 * 
	 */
	public LobbyServer(int port) {
		playerGroup = new PlayerGroup();
		try {
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(port);
			Socket clientSocket;
			
			while (true) {
				System.out.println("Waiting for a client..."); 
				clientSocket = serverSocket.accept();
				new PlayerServerHandler(clientSocket, playerGroup).start(); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Method to run the class.
	 * 
	 * @param args		no any parameters needed.
	 */
	public static void main(String[] args) {
		int port = -1;
		if (args.length > 0) {
			try {
				port = new Integer(args[0]);
			} catch (Exception e) {
				System.out.println("Use:  LobbyServer port");
				System.exit(0);
			}
		} else {
			port = 12345;
		}
		new LobbyServer(port);
		
	}

}
