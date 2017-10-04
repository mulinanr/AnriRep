/**
 * images from http://www.arifeldman.com/games/spritelib.html
 */

package de.hhu.propra14.team102;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;






import de.hhu.propra14.team102.levels.Level;
import de.hhu.propra14.team102.lobby.LobbyWatcher;

/**
 * Method to create and control Game Board.
 * 
 * @author anrio
 *
 */
public class WormsField extends JPanel implements ActionListener{

    private LobbyWatcher lWatcher;
    private Level gLevel;
    //private GameLauncher gLauncher;
    
    private ArrayList<TerrainObject> terrain;
    private ArrayList<Weapon> weapon;
    private ArrayList<Items> items;
    private ArrayList<Worm> team2;
    private ArrayList<Worm> team1;
    
	/**
	 * Variable to hold current team number.
	 */
    private int team = 0;
	/**
	 * Variable to hold current player index in team's 1 array.
	 */
    private int team1Index = 0;
	/**
	 * Variable to hold current player index in team's 2 array.
	 */
    private int team2Index = 3;
	/**
	 * Variable to hold the type of current weapon for teams.
	 */
    private int[] teamWeapon = new int[2];

	private Timer timer;
	private Timer gameTurn;
    private Worm worm;
    private Image[] images;

    private String[] teamName = new String[2];
    private int windVelocity = 0;
    
    private int turn = 1;
    private int timeLeft;
    private int maxTurnTime = 20;
    private int isFired = 2;
    private boolean netGame = false;
    private boolean isActive = false;
    //private String gameInfo;
    private String imagePath;
    private Color team1Color;
    private Color team2Color;
    private String congratulation = null;

    private Image backgroundImage;
    
    private long delta = 0;
    private long last = 0;
    private long fps = 0;
    

	/**
	 * Constructor to create network game.
	 * 
	 * @param levelNumber	level Number for terrain object.
	 * @param w				LobbyWatcher object to communicate with game server.
	 */
	public WormsField(Level gameLevel, LobbyWatcher w) {
		this.gLevel = gameLevel;
		this.lWatcher = w;

		netGame = true;
		this.lWatcher = w;

		addKeyListener(new KeyMonitor());
		addMouseListener(new MouseMonitor());

		weapon = new ArrayList<Weapon>();
		items = new ArrayList<Items>();

		setFocusable(true);
        setDoubleBuffered(true);
        
	    imagePath = GameConstants.imagePath;
	    team1Color = GameConstants.team1Color;
	    team2Color = GameConstants.team2Color;
        teamName[0] = GameConstants.player1Name;
        teamName[1] = GameConstants.player2Name;

        isActive = false;
        ImageIcon ii = new ImageIcon(this.getClass().getResource(imagePath + "background.png"));
        backgroundImage = ii.getImage();
               
        timeLeft = 30;
        
        loadImages();
        initTerrain(gLevel);    

        addWorms();
        
        last = System.nanoTime();
        timer = new Timer(30, this);
        
        gameTurn = new Timer(600, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
//				if (isActive) {
					if (timeLeft == 0) {
						endNetworkTurn();
					} else {
						timeLeft--;
					}
//				}
			}

		});
           
        timer.start();
        gameTurn.start();
}

    
	/**
	 * Constructor to start offline game with level Number for terrain object.
	 * 
	 * @param gameLevel		level Number for terrain object.
	 */
	public WormsField(Level gameLevel) {

		this.gLevel = gameLevel;
    	
		addKeyListener(new KeyMonitor());
		addMouseListener(new MouseMonitor());
		weapon = new ArrayList<Weapon>();
		items = new ArrayList<Items>();

	    imagePath = GameConstants.imagePath;
	    team1Color = GameConstants.team1Color;
	    team2Color = GameConstants.team2Color;
        teamName[0] = GameConstants.player1Name;
        teamName[1] = GameConstants.player2Name;

		isActive = true;
//		gD = new GameData();
		//gD.teamWeapon[1] = 1;

		setFocusable(true);
        setDoubleBuffered(true);
                
        ImageIcon ii = new ImageIcon(this.getClass().getResource(imagePath + "background.png"));
        backgroundImage = ii.getImage();
               
        timeLeft = 20;
        
        loadImages();
        initTerrain(gameLevel);    
        addWorms();
        
        last = System.nanoTime();
        timer = new Timer(30, this);
        
        gameTurn = new Timer(600, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (timeLeft == 0) {
					makeTurn();
				} else {
					timeLeft--;
				}
			}

		});
                
        timer.start();
        gameTurn.start();
    }

	/**
	 * Method to load images of terrain objects, to speed up game execution.
	 */
	private void loadImages() {
		ImageIcon ii;
		images = new Image[10];

		ii = new ImageIcon(this.getClass().getResource(imagePath + "wolke.png"));
		images[0] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(imagePath + "haus.png"));
		images[1] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(imagePath + "andere.png"));
		images[2] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(imagePath + "ground.png"));
		images[3] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(imagePath + "grass.png"));
		images[4] = ii.getImage();
//		ii = new ImageIcon(this.getClass().getResource("worms.png"));
//		images[5] = ii.getImage();
//		ii = new ImageIcon(this.getClass().getResource("explosion.jpeg"));
//		images[6] = ii.getImage();

	}

    
	/**
	 * Method to create and fill array of terrain objects, according the level data.
	 * 
	 * @param level		Level object with terrain objects' data.
	 */
	private void initTerrain(Level l) {

		terrain = new ArrayList<TerrainObject>();
		//terrain = gD.terrain;

		//Level l = new Level(level);

		for (int i = 0; i < l.getSize(); i++) {
			terrain.add(new TerrainObject(l.getX(i), l.getY(i), l.getType(i)));
		}

		// Ground
		System.out.println("Size ground: " + l.getGroundXIndex().size());
		for (int i = 0; i < l.getGroundXIndex().size(); i++) {
			terrain.add(new TerrainObject(l.getGroundXIndex().get(i), l.getGroundYIndex()
					.get(i), "ground.png"));
		}

		// Grass
		System.out.println("Size grass: " + l.getGrassXIndex().size());
		for (int i = 0; i < l.getGrassXIndex().size(); i++) {
			terrain.add(new TerrainObject(l.getGrassXIndex().get(i), l.getGrassYIndex()
					.get(i), "grass.png"));
		}
	}

    	
    	/**
    	 * Method to create and fill arrays of worms, according the level data.
    	 */
	private void addWorms() {

		team1 = new ArrayList<Worm>();
		team2 = new ArrayList<Worm>();

		for (int i = 0; i < 4; i++) {
			team1.add(new Worm(0, gLevel.getTeam1X(i), gLevel.getTeam1Y(i), terrain, items, team2));
		}

		for (int i = 0; i < 4; i++) {
			team2.add(new Worm(1, gLevel.getTeam2X(i), gLevel.getTeam2Y(i), terrain, items, team1));
		}

		worm = team1.get(0);
	}
    	
	/**
	 * Method to set user interface active.
	 * 
	 * Used in network game, to allow user interface in case of active turn.
	 */
	public void setActive() {
		isActive = true;
		System.out.println("Active is " + isActive);
	}	
   	
	/**
	 * @param team
	 */
	public void setTeam(int team) {
		this.team = team;
		
		if (team == 1) {
			team1Index = 3;
			team2Index = 0;
			worm = team2.get(0);
		}
	}
	
	/**
	 * Method to process off-line game turn.
	 */
	private void makeTurn() {

		timeLeft = maxTurnTime;
		isFired = 2;

		team = (team + 1) % 2;
        windVelocity = (int) (Math.pow(Math.random() - 0.5, 1) * 5.0);

		int teamMembersLeft = 4;
		if (team == 0) {
			team1Index = (team1Index + 1) % 4;
			while ((team1.get(team1Index).getWormHealt() == 0) && (teamMembersLeft != 0)) {
				team1Index = (team1Index + 1) % 4;
				teamMembersLeft--;
			}			
		} else {
			team2Index = (team2Index + 1) % 4;
			while ((team2.get(team2Index).getWormHealt() == 0) && (teamMembersLeft != 0)) {
				team2Index = (team2Index + 1) % 4;
				teamMembersLeft--;
			}			
		}

		if (teamMembersLeft == 0) {
			gameOver(team);
		}

		if (team == 1) {
			worm = team2.get(team2Index);
		} else {
			worm = team1.get(team1Index);
		}

		for (int i = 0; i < team1.size(); i++) {
			Worm w = (Worm) team1.get(i);
			w.updateGameState(terrain, items, team2, windVelocity);
		}

		for (int i = 0; i < team2.size(); i++) {
			Worm w = (Worm) team2.get(i);
			w.updateGameState(terrain, items, team1, windVelocity);
		}

		for (int i = 0; i < weapon.size(); i++) {
			Weapon w = (Weapon) weapon.get(i);
			w.updateGameState(terrain, team1, team2);
		}
	}

	/**
	 * Method to process network turn.
	 */
	private void endNetworkTurn() {
		
			team = (team + 1) % 2;

			int teamMembersLeft = 4;
			if (team == 0) {
				team1Index = (team1Index + 1) % 4;
				while ((team1.get(team1Index).getWormHealt() == 0)
						&& (teamMembersLeft != 0)) {
					team1Index = (team1Index + 1) % 4;
					teamMembersLeft--;
				}
			} else {
				team2Index = (team2Index + 1) % 4;
				while ((team2.get(team2Index).getWormHealt() == 0)
						&& (teamMembersLeft != 0)) {
					team2Index = (team2Index + 1) % 4;
					teamMembersLeft--;
				}
			}

			if (teamMembersLeft == 0) {
				lWatcher.sendGameOver(team);
				gameOver(team);
			}

		if (isActive) {
			GameData gameData = new GameData();

			gameData.setTeam(team);
			gameData.setTeam1Index(team1Index);
			gameData.setTeam2Index(team2Index);
			gameData.setWindVelocity((int) (Math.pow(Math.random() - 0.5, 1) * 5.0));

			for (int i = 0; i < 4; i++) {
				gameData.setxTeam1(i, team1.get(i).getX());
				gameData.setyTeam1(i, team1.get(i).getY());
				gameData.setTeam1Health(i, team1.get(i).getWormHealt());
				
				gameData.setxTeam2(i, team2.get(i).getX());
				gameData.setyTeam2(i, team2.get(i).getY());
				gameData.setTeam2Health(i, team2.get(i).getWormHealt());
			}

			lWatcher.sendTurn(gameData);
		}

		isActive = false;
		gameTurn.stop();
		//System.out.println("Network turn end");

	}


	/**
	 * Method to start network turn according to game data, sent by server.
	 */
	public void startNetworkTurn(GameData gData) {
		//System.out.println("Network turn start");

		timeLeft = maxTurnTime;
		isFired = 2;

		team = gData.getTeam();
		team1Index = gData.getTeam1Index();
		team2Index = gData.getTeam2Index();
		windVelocity = gData.getWindVelocity();

		for (int i = 0; i < 4; i++) {
			team1.get(i).setX(gData.getxTeam1(i));
			team1.get(i).setY(gData.getyTeam1(i));
			team1.get(i).setWormHealt(gData.getTeam1Health(i));

			team2.get(i).setX(gData.getxTeam2(i));
			team2.get(i).setY(gData.getyTeam2(i));
			team2.get(i).setWormHealt(gData.getTeam2Health(i));
		}

		for (int i = 0; i < team1.size(); i++) {
			Worm w = (Worm) team1.get(i);
			w.updateGameState(terrain, items, team2, windVelocity);
		}

		for (int i = 0; i < team2.size(); i++) {
			Worm w = (Worm) team2.get(i);
			w.updateGameState(terrain, items, team1, windVelocity);
		}
		
		if (team == 1) {
			worm = team2.get(team2Index);
		} else {
			worm = team1.get(team1Index);
		}
		
		gameTurn.start();
		//System.out.println("Network turn completed");

	}			


	/**
	 * Method to process game over event.
	 */
	public void gameOver(int team) {
		if (team == 0) {
			congratulation = "Winner is " + GameConstants.player2Name;
		} else {
			congratulation = "Winner is " + GameConstants.player1Name;
		}
		isActive = false;
		gameTurn.stop();
		
	}

    	/* (non-Javadoc)
    	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
    	 */
    	@Override
	public void paint(Graphics g) {
		super.paint(g);

		//long t1 = System.nanoTime();

		Graphics2D g2d = (Graphics2D) g;

		g2d.drawImage(backgroundImage, 0, 0, null);

		g2d.setColor(Color.red);
		g2d.drawString("FPS: " + Long.toString(fps) + "  " + isActive + "  " + team + " " + worm.getX(), 20, 10);

		g.fillRect(worm.getX()-2, worm.getY()-6, worm.getW()+4, worm.getH()+8);

		g.setColor(team1Color);
		for (int i = 0; i < team1.size(); i++) {
			Worm w = (Worm) team1.get(i);
			String health = "" + w.getWormHealt();
			g.drawString(health, team1.get(i).getX() + 5, team1.get(i).getY());
			g2d.drawImage(w.getImage(), w.getX(), w.getY(), this);
		}

		g.setColor(team2Color);		
		for (int i = 0; i < team2.size(); i++) {
			Worm w = (Worm) team2.get(i);
			String health = "" + w.getWormHealt();
			g.drawString(health, team2.get(i).getX() + 10, team2.get(i).getY());
			g2d.drawImage(w.getImage(), w.getX(), w.getY(), this);
		}

		for (int i = 0; i < terrain.size(); i++) {
			if (!terrain.get(i).isRemove())
				g2d.drawImage(images[terrain.get(i).getiT()], terrain.get(i).getX(), terrain.get(i).getY(), this);
		}

		for (int i = 0; i < weapon.size(); i++) {
			Weapon wP = (Weapon) weapon.get(i);
			if (wP.isVisible())
				g2d.drawImage(wP.getImage(), wP.getX(), wP.getY(), this);
				//g2d.drawRect(wP.getxCurrent(), wP.getyCurrent(), wP.getWidth(), wP.getHeight());
		}

		for (int i = 0; i < items.size(); i++) {
			Items it = (Items) items.get(i);
			if (it.isVisible())
				g2d.drawImage(it.getImage(), it.getX(), it.getY(), this);
		}

		g.setFont(new Font("standart", Font.BOLD, 20));
		g.setColor(Color.ORANGE);
		g.drawString("Health: ", 100, 25);
		g.drawString("Health: ", 520, 25);
		g.drawString(String.valueOf(timeLeft), 385, 25);

		g.drawString(String.valueOf(windVelocity), 385, 50);
		if (windVelocity < 0) {
			g.drawString("<<<- ", 315, 50);
		} else if (windVelocity > 0) {
			g.drawString(" ->>>", 400, 50);
		}

		g.setColor(team1Color);
		g.drawString(teamName[0], 0, 25);
		g.drawString(String.valueOf(team1.get(0).getWormHealt() + team1.get(1).getWormHealt() + team1.get(2).getWormHealt() + team1.get(3).getWormHealt()), 180, 25);
		if (team == 0) {
			if (teamWeapon[0] == 0) {
				g.drawString("Bullet", 230, 25);
			} else if (teamWeapon[0] == 1) {
				g.drawString("Bazooka", 230, 25);
				g.drawString(String.valueOf(worm.getWaffen1Left()), 330, 25);
			} else {
				g.drawString("Grenade", 230, 25);
				g.drawString(String.valueOf(worm.getWaffen2Left()), 330, 25);
			}
			if (!(worm.getInfo() == null) ) {
				g.drawString(worm.getInfo(), 0, 50);
			}
		}

		g.setColor(team2Color);
		g.drawString(teamName[1], 420, 25);
		g.drawString(String.valueOf(team2.get(0).getWormHealt() + team2.get(1).getWormHealt() + team2.get(2).getWormHealt() + team2.get(3).getWormHealt()), 600, 25);
		if (team == 1) {
			if (teamWeapon[1] == 0) {
				g.drawString("Bullet", 650, 25);
			} else if (teamWeapon[1] == 1) {
				g.drawString("Bazooka", 650, 25);
				g.drawString(String.valueOf(worm.getWaffen1Left()), 750, 25);
			} else {
				g.drawString("Grenade", 650, 25);
				g.drawString(String.valueOf(worm.getWaffen2Left()), 750, 25);
			}
			if (!(worm.getInfo() == null) ) {
				g.drawString(worm.getInfo(), 420, 50);
			}
		}

		if (congratulation != null) {
			g.setColor(Color.CYAN);
			g.setFont(new Font("standart", Font.BOLD, 50));
			g.drawString(congratulation, 50, 200);
		}

		Toolkit.getDefaultToolkit().sync();
		g.dispose();
		//System.out.println(System.nanoTime() - t1);
	}


	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		//System.out.println(delta);

		long t1 = System.nanoTime();
		//System.out.println(t1);

        delta = System.nanoTime() - last;
        last = System.nanoTime();
        fps = ((long) 1E9 ) / delta;
        
        addItems();
        updateMovingObjects();
        removeObjects();

		repaint();
		//System.out.println(System.nanoTime() - t1 +"  " +fps + "\n");
	}

	/**
	 * Method to add new Items on random basis.
	 */
	private void addItems() {
		if ((Math.random() > 0.996) && (items.size() < 2) && isActive) {			
			double random = Math.random();
			int type;
			int x;
			int y;
			
			if ((random - 0.75) > 0) {
				type = 0;
			} else if ((random - 0.5) > 0) {
				type = 1;
			} else if ((random - 0.25) > 0) {
				type = 2;
			} else {
				type = 3;
			}
			
			x = (int) (Math.random() * 780); 
					
			if (netGame) {
				lWatcher.sendMove("ITEM: " + type + " | " + x);
			} else {
				//addItem(type, x);
				items.add(new Items(terrain, type, x, 25));
			}

		}
	}

	/**
	 * Method to add Item in network game.
	 * 
	 * @param type		Type of Item.
	 * @param x			X position of Item.
	 */
	public void addItem(int type, int x) {
		items.add(new Items(terrain, type, x, 25));
		
	}


	/**
	 * Method to remove objects that is not visible.
	 */
	private void removeObjects() {

		for (int i = 0; i < weapon.size(); i++) {
			Weapon w = weapon.get(i);
			if (!w.isVisible()) {
				weapon.remove(i);
			}
		}

		for (int i = 0; i < items.size(); i++) {
			Items it = items.get(i);
			if (!it.isVisible()) {
				items.remove(i);
			}
		}

		for (int i = 0; i < terrain.size(); i++) {
			TerrainObject t = terrain.get(i);
			if (t.isRemove()) {
				terrain.remove(i);
			}
		}

	}

	/**
	 * Method to update position and visibility of all moving objects.
	 */
	private void updateMovingObjects() {

		//worm.update();
		for (int i = 0; i < team1.size(); i++) {
			Worm w = team1.get(i);
			w.update();
		}

		for (int i = 0; i < team2.size(); i++) {
			Worm w = team2.get(i);
			w.update();
		}

		for (int i = 0; i < weapon.size(); i++) {
			Weapon wp = weapon.get(i);
			wp.update();
		}

		for (int i = 0; i < items.size(); i++) {
			Items it = items.get(i);
			it.update();
		}


	}

	/**
	 * Method to process a fire in network game.
	 * 
	 * @param xTarget	X target position of fire.
	 * @param yTarget	Y target position of fire.
	 * @param type		Type of weapon.
	 * @param xWorm		X position of firing worm.
	 * @param yWorm		Y position of firing worm.
	 */
	public void fire(int xTarget, int yTarget, int type, int xWorm, int yWorm) {
		//System.out.println("Fire: " + x + " | " + y);
		if ((isFired > 0) && (worm.getWormHealt() > 0)) {
			//System.out.println("Fire:   x: " + e.getX() + " y: " + e.getY());
			if (team == 1) {
				weapon.add(new Weapon(xTarget, yTarget, team, type, xWorm, yWorm, worm, team1, team2, terrain, fps, windVelocity));
			} else {
				weapon.add(new Weapon(xTarget, yTarget, team, type, xWorm, yWorm, worm, team2, team1, terrain, fps, windVelocity));
			}
			if (type == 1) {
				worm.setWaffen1Left(worm.getWaffen1Left() - 1);
			} else if (type == 2) {
				worm.setWaffen2Left(worm.getWaffen2Left() - 1);
			}
		}

		isFired--;
		timeLeft = timeLeft - 10;
		if (timeLeft < 0) {
			timeLeft = 3;
		}
	}

	/**
	 * Method to process move left in network game.
	 * 
	 * @param x		X position of worm.
	 * @param y		Y position of worm.
	 */
	public void moveLeft(int x, int y) {
		//System.out.println("Move left");
		worm.setX(x);
		worm.setY(y);
		worm.moveLeft(fps);
	}

	/**
	 * Method to process move right in network game.
	 * 
	 * @param x		X position of worm.
	 * @param y		Y position of worm.
	 */
	public void moveRight(int x, int y) {
		//System.out.println("Move right");
		worm.setX(x);
		worm.setY(y);
		worm.moveRight(fps);
	}

	/**
	 * Method to process jump in network game.
	 * 
	 * @param x		X position of worm.
	 * @param y		Y position of worm.
	 */
	public void moveJump(int x, int y) {
		//System.out.println("Move jump");
		worm.setX(x);
		worm.setY(y);
		worm.jump(fps);
	}
	
	/**
	 * Method to process stop move in network game.
	 * 
	 * @param x		X position of worm.
	 * @param y		Y position of worm.
	 */
	public void stopMove(int x, int y) {
		//System.out.println("Stop Move");
		worm.setX(x);
		worm.setY(y);
		worm.stopMove();
	}


	/**
	 * Method to change team's weapon.
	 * 
	 * @param team	index of team to chande weapon.
	 */
	public void changeWeapon(int team) {
		//System.out.println("Change weapon: " + team);
		teamWeapon[team] = (teamWeapon[team] + 1) % 3;
		//System.out.println("Change weapon: " + team + "  " + teamWeapon[team]);
	}



    /**
     * Class to monitor keyboard related events.
     * 
     * @author anrio
     *
     */
    private class KeyMonitor extends KeyAdapter {

		/* (non-Javadoc)
		 * @see java.awt.event.KeyAdapter#keyPressed(java.awt.event.KeyEvent)
		 */
		public void keyPressed(KeyEvent e) {
			//System.out.println("Key Pressed: " + isActive);
			if (isActive && (worm.getWormHealt() > 0)) {
				int key = e.getKeyCode();

				if ((key == KeyEvent.VK_S) || (key == KeyEvent.VK_DOWN)) { 
					if (netGame) {
						lWatcher.sendMove("WEAPON: " + team);
					} else {
						changeWeapon(team);
					}
				} else if ((key == KeyEvent.VK_A) || (key == KeyEvent.VK_LEFT)) {
					if (netGame) {
						lWatcher.sendMove("MOVE_LEFT", worm.getX(), worm.getY());
					} else {
						worm.moveLeft(fps);
					}
				} else if ((key == KeyEvent.VK_D) || (key == KeyEvent.VK_RIGHT)) {
					if (netGame) {
						lWatcher.sendMove("MOVE_RIGHT", worm.getX(), worm.getY());
					} else {
						worm.moveRight(fps);
					}
				} else if ((key == KeyEvent.VK_W) || (key == KeyEvent.VK_UP)) {
					if (netGame) {
						lWatcher.sendMove("JUMP", worm.getX(), worm.getY());
					} else {
						worm.jump(fps);
					}
				}				
			}
		}
        
        /* (non-Javadoc)
         * @see java.awt.event.KeyAdapter#keyReleased(java.awt.event.KeyEvent)
         */
        public void keyReleased(KeyEvent e) {
			if (isActive && (worm.getWormHealt() > 0)) {
				if (netGame) {
					lWatcher.sendMove("STOP_MOVE", worm.getX(), worm.getY());
				} else {
					worm.stopMove();
				}
			}            
        }
    }
    
	/**
	 * Class to monitor mouse related events.
	 * 
	 * @author anrio
	 *
	 */
	private class MouseMonitor extends MouseAdapter {

		/* (non-Javadoc)
		 * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
		 */
		public void mousePressed(MouseEvent e) {
			if (isActive && (worm.getWormHealt() > 0)) {
				if (netGame) {
					lWatcher.sendFire(e.getX(), e.getY(), teamWeapon[team], worm.getX(), worm.getY());
				} else {
					fire(e.getX(), e.getY(), teamWeapon[team], worm.getX(), worm.getY());
				}
			}
		}

		public void mouseClicked(MouseEvent e) {

		}

		public void mouseEntered(MouseEvent e) {

		}

		public void mouseExited(MouseEvent e) {

		}

		public void mouseReleased(MouseEvent e) {

		}

	}

}