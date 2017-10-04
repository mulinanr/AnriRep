package de.hhu.propra14.team102;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.ImageIcon;

/**
 * Class to control fired Weapon objects on the Game Boards.
 * 
 * Weapon Object is created at the time of fire.
 * May be three type of Weapon objects - bullet, bazooka and grenade.
 * Weapon object move by parabolic trajectory and may be affected by wind.
 * 
 * @author anrio
 *
 */
public class Weapon {

	private ArrayList<TerrainObject> terrain;
	private ArrayList<Worm> others;
	private ArrayList<Worm> ours;

	private int team;

	private int xTarget;
	private int yTarget;

	private double xCurrent;
	private double yCurrent;

	private double xVelocity;
	private double yVelocity;
	private double windVelocity;
	private long fps;

	private int width;
	private int height;

	private int explosionDuration = 5;
	private boolean directionRight; 
	private double gravitySpeed = 0.5;

	private boolean isVisible;

	private String imagePath;
	private Image[] images;
	private Image[] explosions;
	private Image current;
	private int imageIndex = 0;
	private int theme;

	private Rectangle hitRestangle;
	private Rectangle checkRestangle;

	private int type; // 0 - bullet
						// 1 - bazooka
						// 2 - grenade

	/**
	 * Constructor, create a new weapon object. 
	 * Weapon object is created by mouse click.
	 * Weapon belong to certain team.
	 * The type of weapon is defined by team's weapon chosen.
	 * Weapon move with certain velocity in given direction.
	 * Weapon could collide with Worm object or with Terrain object
	 * The velocity of weapon depend of FPS.
	 * 
	 * @param x		X position of mouse click.
	 * @param y		Y position of mouse click.
	 * @param team	Team, which member worm fire the weapon.
	 * @param type	Type of weapon created.
	 * @param worm	Worm object, whom the weapon belong to.
	 * @param o		Array of other team Worm object to check collision.
	 * @param t		Array of Terrain object to check collision.
	 * @param fps	FPS to adjust weapon moving velocity depend of performance of computer.
	 */
	public Weapon(int xTarget, int yTarget, int team, int type, int xWorm, int yWorm, Worm worm, ArrayList<Worm> o, ArrayList<Worm> w, ArrayList<TerrainObject> t, long fps, int wVelocity) {
		terrain = t;
		this.others = o;
		this.ours = w;

		this.xTarget = xTarget;
		this.yTarget = yTarget;

//		this.xCurrent = worm.getX() + worm.getW() / 2;
//		this.yCurrent = worm.getY();

		// center position of the current worm
		this.xCurrent = xWorm + worm.getW() / 2;
		this.yCurrent = yWorm + worm.getH() / 2;

		
		this.type = type;
		this.team = team;

		this.fps = fps;
		this.windVelocity = wVelocity;

		if (team == 0) {
			if (GameConstants.team1Theme.equals("/theme1/")) {
				theme = 0;
			} else {
				theme = 1;
			}
		} else {
			if (GameConstants.team2Theme.equals("/theme1/")) {
				theme = 0;
			} else {
				theme = 1;
			}
		}
		
		double angle = Math.atan((yTarget - yCurrent) / Math.abs((xTarget - xCurrent)));
		double distance = xTarget - xCurrent;
		if (distance == 0) {
			distance = 0.0001;
		}
		if (distance <= 0) {
			directionRight = false;
		} else {
			directionRight = true;
		}
		
		double weaponVelocity = getXVelocity(type);
		if (distance > 300) {
			weaponVelocity = weaponVelocity * Math.abs(distance / 300.0);
		}
		weaponVelocity = weaponVelocity * (30 / fps);

		xVelocity = weaponVelocity * Math.cos(angle);
		xVelocity = xVelocity + windVelocity;
		yVelocity = weaponVelocity * Math.sin(angle);
//		yVelocity = (yTarget - yCurrent) * (xVelocity / distance);
//		yVelocity = yVelocity * (30 / fps);
		
		if (!directionRight) {
			xVelocity = -xVelocity;
		}
		xVelocity = xVelocity + windVelocity;



		
		imagePath = GameConstants.imagePath;
		current = loadImages(type);

		width = current.getWidth(null);
		height = current.getHeight(null);

		System.out.println("new Weapon: " + worm.getX() + " | " + xCurrent + " : " + current.getWidth(null) + "  " + worm.getW());
		if (!directionRight) {
			xCurrent = xCurrent - current.getWidth(null) - worm.getW()/2 - 2;
			distance = xCurrent - xTarget;
		} else {
			xCurrent = xCurrent + worm.getW()/2 + 2;
			distance = xTarget - xCurrent;
		}

		hitRestangle = new Rectangle((int) xCurrent, (int) yCurrent, current.getWidth(null), current.getHeight(null));
				
		System.out.println("new Weapon: " + type + "  xV: " + xVelocity + "  yV: " + yVelocity + " angle: " + Math.toDegrees(angle));

		isVisible = true;

	}

	/**
	 * Method to load images of weapon, based on weapon type.
	 * 
	 * Load all the images needed for animation effect.
	 * 
	 * @param type
	 * @return current image to display.
	 */
	private Image loadImages(int type) {
		images = new Image[3];
		explosions = new Image[3];

		ImageIcon ii;

		if (type == 0) {
			if (directionRight) {
				ii = new ImageIcon(this.getClass().getResource(imagePath + "bulletRight1.png"));
				images[0] = ii.getImage();
				ii = new ImageIcon(this.getClass().getResource(imagePath + "bulletRight2.png"));
				images[1] = ii.getImage();
				ii = new ImageIcon(this.getClass().getResource(imagePath + "bulletRight3.png"));
				images[2] = ii.getImage();
			} else {
				ii = new ImageIcon(this.getClass().getResource(imagePath + "bulletLeft1.png"));
				images[0] = ii.getImage();
				ii = new ImageIcon(this.getClass().getResource(imagePath + "bulletLeft2.png"));
				images[1] = ii.getImage();
				ii = new ImageIcon(this.getClass().getResource(imagePath + "bulletLeft3.png"));
				images[2] = ii.getImage();
			}
			getSound(1);

		} else if (type == 1) {
			if (directionRight) {
				ii = new ImageIcon(this.getClass().getResource(imagePath + "bazookaRight1.png"));
				images[0] = ii.getImage();
				ii = new ImageIcon(this.getClass().getResource(imagePath + "bazookaRight2.png"));
				images[1] = ii.getImage();
				ii = new ImageIcon(this.getClass().getResource(imagePath + "bazookaRight3.png"));
				images[2] = ii.getImage();
			} else {
				ii = new ImageIcon(this.getClass().getResource(imagePath + "bazookaLeft1.png"));
				images[0] = ii.getImage();
				ii = new ImageIcon(this.getClass().getResource(imagePath + "bazookaLeft2.png"));
				images[1] = ii.getImage();
				ii = new ImageIcon(this.getClass().getResource(imagePath + "bazookaLeft3.png"));
				images[2] = ii.getImage();
			}
			getSound(2);

		} else {
			ii = new ImageIcon(this.getClass().getResource(imagePath + "grenade1.png"));
			images[0] = ii.getImage();
			ii = new ImageIcon(this.getClass().getResource(imagePath + "grenade2.png"));
			images[1] = ii.getImage();
			ii = new ImageIcon(this.getClass().getResource(imagePath + "grenade3.png"));
			images[2] = ii.getImage();
		}

		ii = new ImageIcon(this.getClass().getResource(imagePath + "explosion1.jpeg"));
		explosions[0] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(imagePath + "explosion2.jpeg"));
		explosions[1] = ii.getImage();
		ii = new ImageIcon(this.getClass().getResource(imagePath + "explosion3.jpeg"));
		explosions[2] = ii.getImage();

		return images[0];		
	}

	/**
	 * Method return Rectangle to determine the size of the Weapon image.
	 * 
	 * @return	Rectangle with Weapon Object bounds,
	 * to check collision with worms in Worm class
	 */
	public Rectangle getHitRestangle() {
		return hitRestangle;
	}

	/**
	 * Method to make sound on some events.
	 * 
	 * The sound depend of event type and theme chosen:
	 * type 0 - explosion;
	 * type 1 - shot bullet;
	 * type 2 - shot bazooka;
	 * type 3 - worm has hit.
	 * 
	 * @param reason		reason to make sound.
	 */
	private void getSound(int reason) {
		if (reason == 0) {
			if (theme == 0) {
				SoundEffect.EXPLODE1.play();
			} else {
				SoundEffect.EXPLODE2.play();
			}
		} else if (reason == 1) {
			if (theme == 0) {
				SoundEffect.BULLET1.play();
			} else {
				SoundEffect.BULLET2.play();
			}
		} else if (reason == 2) {
			if (theme == 0) {
				SoundEffect.BAZOOKA1.play();
			} else {
				SoundEffect.BAZOOKA2.play();
			}
		} else {
			if (theme == 0) {
				SoundEffect.HIT1.play();
			} else {
				SoundEffect.HIT1.play();
			}
		}
	}

	/**
	 * Method to calculate velocity of weapon.
	 * 
	 * @param type
	 * @return
	 */
	private int getXVelocity(int type) {
		if (type == 0) {
			return 15;	// bullet
		} else if (type == 1) {
			return 10;	// bazooka
		} else {
			return 5;	// grenade
		}
	}

	/**
	 * Getter method to return the visibility of the weapon.
	 * 
	 * The visibility of weapon object, is used to decide whether the object should be deleted.
	 * 
	 * @return	value to set, only false could be used
	 */
	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * Setter method to set the visibility of the the weapon.
	 * 
	 * Used to set the visibility of weapon to false.
	 * 
	 * @param isVisible
	 */
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	/**
	 * Getter method to return the current image of the Weapon Object.
	 * 
	 * @return	The image of Weapon to be displayed on game board.
	 */
	public Image getImage() {
		return current;
	}

	/**
	 * Getter method to return the X position of the weapon.
	 * 
	 * @return	X position of the weapon.
	 */
	public int getX() {
		return (int) xCurrent;
	}

	/**
	 * Getter method to return the Y position of the weapon.
	 * 
	 * @return	Y position of the weapon.
	 */
	public int getY() {
		return (int) yCurrent;
	}

	/**
	 * Getter method to return the width of the Weapon Object's image.
	 * 
	 * @return	width of the Weapon Object's image.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Getter method to return the height of the Weapon Object's image.
	 * 
	 * @return	height of the Weapon Object's image.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Method to update Weapon Object's related data on regular basis.
	 * 
	 * Called each frame from game bard class and control the movement of the weapon.
	 */
	public void update() {
		if (!checkCollision(others, ours, terrain)) {
			xCurrent = (int) (xCurrent + xVelocity);
			yCurrent = (int) (yCurrent + yVelocity);
			yVelocity = yVelocity + gravitySpeed;
			//System.out.println("update: " + xCurrent + " " + yCurrent);
		} else {
			images = explosions;
			xVelocity = 0;
			xVelocity = 0;
			if (explosionDuration <= 0) {
				isVisible = false;
				makeDamage(xCurrent, yCurrent, type, terrain);

			}
			explosionDuration--;			
		}

		if (xCurrent <=4 || xCurrent >= 800 - width - 4) {
			isVisible = false;
		}

		if (yCurrent <=4 || yCurrent >= 600 - height - 4) {
			isVisible = false;
		}

		imageIndex = (imageIndex + 1) % images.length;
		current = images[imageIndex];
		//System.out.println("current: " + imageIndex + " " + current.toString());
	}

	/**
	 * Method to check collision between weapon object, terrain objects and worms.
	 * 
	 * Method detect the moment when the weapon, during it's movement,  collide with
	 * terrain objects or worms' object.
	 * 
	 * @param o		Array of worms objects
	 * @param t		Array of terrain object
	 * @return		boolean, true if collision is exist.
	 */
	private boolean checkCollision(ArrayList<Worm> o, ArrayList<Worm> ours,ArrayList<TerrainObject> t) {

		for (int i = 0; i < t.size(); i++) {		
//			System.out.println("Check collision 1: " + xCurrent + " : " + yCurrent + "  " + width + " - " + height);
//			System.out.println("Check collision 2: " + t.get(i).getBounds().x + " : " + t.get(i).getBounds().y + "  " + t.get(i).getBounds().width + " - " + t.get(i).getBounds().height);
			if (new Rectangle((int) xCurrent, (int) yCurrent, width, height).intersects(t.get(i).getBounds())) {
				//System.out.println("intersects:        " + xCurrent + " : " + yCurrent);
//				TerrainObject tt = t.get(i);
//				tt.setRemove(true);
//				isVisible = false;
				getSound(2);
				return true;
			}
		}	

		for (int i = 0; i < o.size(); i++) {	
			if (new Rectangle((int) xCurrent, (int) yCurrent, width, height).intersects(o.get(i).getBounds())) {
				//System.out.println("Worm intersects:        " + xCurrent + " : " + yCurrent);
				if (o.get(i).getWormHealt() > 0) {
					getSound(3);
					o.get(i).setHitByWeapon(type);
					//System.out.println("Hit enemies: " + type + "  x: " + xCurrent + "  y: " + yCurrent );
					return true;
				}
			}		
		}

		for (int i = 0; i < ours.size(); i++) {	
			if (new Rectangle((int) xCurrent, (int) yCurrent, width, height).intersects(ours.get(i).getBounds())) {
				//System.out.println("Worm intersects:        " + xCurrent + " : " + yCurrent);
				if (ours.get(i).getWormHealt() > 0) {
					getSound(3);
					ours.get(i).setHitByWeapon(type);
					//System.out.println("Hit ours: " + type + "  x: " + xCurrent + "  y: " + yCurrent );
					return true;
				}
			}		
		}


		return false;
	}

	/**
	 * Method make damage for terrain.
	 * 
	 * Method calculate damage radius depend weapon's type,
	 * and set visibility to false to all terrain objects in this area.
	 * 
	 * @param xC		X position of the centre of damage.
	 * @param yC		Y position of the centre of damage.
	 * @param type	type of weapon.
	 * @param t		List of terrain objects to make damage.
	 */
	private void makeDamage(double xC, double yC, int type, ArrayList<TerrainObject> t) {

		double damageRadius = 0;
		int damageX = (int) (xC + width / 2);
		int damageY = (int) (yC + height / 2);

		if (type == 0) {
			damageRadius = 15;
		} else if (type == 0) {
			damageRadius = 45;
		} else {
			damageRadius = 30;
		}
		//System.out.println("damage:            " + damageX + " : " + damageY + "    " + damageRadius);

		for (int i = 0; i < t.size(); i++) {
			if ((Math.pow((damageX - (t.get(i).getX() + t.get(i).getW()/2)), 2) + Math.pow((damageY - (t.get(i).getY() + t.get(i).getH()/2)), 2)) <= (Math.pow(damageRadius, 2))) {
				//System.out.println("damage 2:           " + t.get(i).getBounds().x + " : " + t.get(i).getBounds().y + "  " + t.get(i).getBounds().width + " - " + t.get(i).getBounds().height);
				TerrainObject tt = t.get(i);
				tt.setRemove(true);
			}
		}
		
		for (int i = 0; i < others.size(); i++) {
			if (others.get(i).getHitByWeapon() > -1 ) {
				others.get(i).decreaseHelth();
			}
		}

		for (int i = 0; i < ours.size(); i++) {
			if (ours.get(i).getHitByWeapon() > -1 ) {
				ours.get(i).decreaseHelth();
			}
		}

	}

	/**
	 * Method to update Game Data after turn.
	 * 
	 * Method update data about terrain and worms after game turn.
	 * 
	 * @param t		List of terrain objects.
	 * @param t1		List of worms belonging to team 1.
	 * @param t2		List of worms belonging to team 2.
	 */
	public void updateGameState(ArrayList<TerrainObject> t,
			ArrayList<Worm> t1, ArrayList<Worm> t2) {

		terrain = t;
		if(team == 0) {       
            this.others = t2; 
		} else {
			this.others = t1;
		}

	}

}