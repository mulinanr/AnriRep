package de.hhu.propra14.team102;
 
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
 




import javax.swing.ImageIcon;
 
/**
 * Class to control Worm object on the Game Boards.
 * 
 * Worm could belong to team 1 or team 2.
 * Worm can move to left or to right, and make a jump.
 * Worm is a subject for gravitation and an aim to fire.
 * Worm can be hit, as result, worm's health decrease.
 * 
 * @author anrio
 *
 */
/**
 * @author anrio
 *
 */
public class Worm {
     
    // Konfiguration
    private final int B_WIDTH = 800;
    private final int B_HEIGHT = 600;
    private int wormHealt = 100;
    private int hitByWeapon = -1;
 
    // Animation
    private String worm = "worms.png";
    //private transient Image image;
     
    private double x;
    private double y;
    private int w;
    private int h;
    
    private int team;
    private String info = null;
     
    private int waffen1Left = 30;
    private int waffen2Left = 10;
     
	private Image[] imagesLeft;
	private Image[] imagesRight;
	private Image[] current;
	private Image image;
	private Image imageStaticLeft;
	private Image imageStaticRight;
	private Image imageCemetery;
	private int iI = 0;	//imageIndex
	private String imagePath;
    
    private double gravitySpeed = 1;
    private double windVelocity = 0;
    
    private boolean moveState = false;
    private boolean jumpState = false;
    private boolean directionRight = true;
    
    private double velocityX;
    private double velocityY;
    private double moveVelocity = 5;
    private double jumpVelosity = 15;
    private int dopingTime;
    private int infoTime;
    private double factor = 1;
    
    
    private ArrayList<TerrainObject> terrain;
    private ArrayList<Items> items;
    private ArrayList<Worm> others;
     
    /**
     * Constructor, create a new worm object.
     * 
     * Worm belong to certain team,
     * Worm can interact with terrain and items,
     * worm located at defined position,
     * worm can move and is subject to gravity.
     * 
     * @param team			The team whom the worm belongs to.
     * @param xPosition		The X position of worm.
     * @param yPosition		The Y position of worm.
     * @param t				Array of Terrain object to check collision.
     * @param i				Array of Items object to check collision.
     * @param o				Array of worms belonging to other team.
     */
    public Worm(int team, int xPosition, int yPosition, ArrayList<TerrainObject> t, ArrayList<Items> i,
            ArrayList<Worm> o) {
 
        this.team = team;
    		this.x = xPosition;
        this.y = yPosition;

        this.terrain = t;
        this.items = i;
        this.others = o;
        
        if (team == 0) {
        		imagePath = GameConstants.team1Theme;
        } else {
        		imagePath = GameConstants.team2Theme;
		}
        
        
        loadImages();
        if (directionRight){
        		current = imagesRight;
        		image = imageStaticRight;
        } else {
			current = imagesLeft;
			image = imageStaticLeft;
		}
        current = imagesRight;
        
        windVelocity = (int) (Math.pow(Math.random() - 0.5, 1) * 5.0);
 
        w = current[0].getWidth(null);
        h = current[0].getHeight(null);
    }
    
	/**
	 * Method to load images needed to animate worm.
	 */
	private void loadImages() {
		imagesLeft = new Image[3];
		imagesRight = new Image[3];
		ImageIcon ii;

				ii = new ImageIcon(this.getClass().getResource(imagePath + "wormRight1.png"));
				imagesRight[0] = ii.getImage();
				ii = new ImageIcon(this.getClass().getResource(imagePath + "wormRight2.png"));
				imagesRight[1] = ii.getImage();
				ii = new ImageIcon(this.getClass().getResource(imagePath + "wormRight3.png"));
				imagesRight[2] = ii.getImage();
				ii = new ImageIcon(this.getClass().getResource(imagePath + "wormLeft1.png"));
				imagesLeft[0] = ii.getImage();
				ii = new ImageIcon(this.getClass().getResource(imagePath + "wormLeft2.png"));
				imagesLeft[1] = ii.getImage();
				ii = new ImageIcon(this.getClass().getResource(imagePath + "wormLeft3.png"));
				imagesLeft[2] = ii.getImage();

				ii = new ImageIcon(this.getClass().getResource(imagePath + "wormLeft.png"));
				imageStaticLeft = ii.getImage();

				ii = new ImageIcon(this.getClass().getResource(imagePath + "wormRight.png"));
				imageStaticRight = ii.getImage();

				ii = new ImageIcon(this.getClass().getResource(imagePath + "wormCemetery.png"));
				imageCemetery = ii.getImage();

		}
   
    /**
     * Getter method to return the X position of the worm.
     * 
     * @return	X position of the worm.
     */
    public int getX() {
        return (int) x;
    }
 
    /**
     * Getter method to return the Y position of the worm.
     * 
     * @return	Y position of the worm.
     */
    public int getY() {
        return (int) y;
    }
    
    /**
     * Setter method to set the X position of the worm.
     * 
     * @param x		X position of the worm.
     */
    public void setX(double x) {
		this.x = x;
	}

	/**
	 * Setter method to set the Y position of the worm.
	 * 
	 * @param y		Y position of the worm.
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
     * Getter method to return the width of the Worm Object's image.
     * 
     * @return	width of the Worm Object's image.
     */
    public int getW() {
        return w;
    }
 
    /**
     * Getter method to return the height of the Worm Object's image.
     * 
     * @return	height of the Worm Object's image.
     */
    public int getH() {
        return h;
    }
    
    /**
     * Getter method to return the current image of the Worm Object.
     * 
     * @return	current image of the Worm Object.
     */
    public Image getImage() {
    		if (moveState) {
    			return current[iI];
    		} else {
    			return image;
    		}
        
    }
  
    /**
     * Getter method to return the type of weapon of team 1.
     * 
     * @return	type of weapon of team 1.
     */
    public int getWaffen1Left() {
		return waffen1Left;
	}

	/**
	 * Setter method to set the type of weapon for team 1.
	 * 
	 * @param waffen1Left	new type of weapon for team 1.
	 */
	public void setWaffen1Left(int waffen1Left) {
		this.waffen1Left = waffen1Left;
	}

	/**
	 * Getter method to return the type of weapon of team 2.
	 * 
	 * @return	type of weapon of team 2.
	 */
	public int getWaffen2Left() {
		return waffen2Left;
	}

	/**
	 * Setter method to set the type of weapon for team 2.
	 * 
	 * @param waffen2Left	new type of weapon for team 2.
	 */
	public void setWaffen2Left(int waffen2Left) {
		this.waffen2Left = waffen2Left;
	}

	/**
	 * Getter method to return the value of worm health.
	 * 
	 * @returnGetter method to return the value of worm health.
	 */
	public int getWormHealt() {
        return wormHealt;
    }
    
    /**
     * Setter method to set the value of worm health.
     * 
     * @param wormHealt	the value of worm health.
     */
    public void setWormHealt(int wormHealt) {
		this.wormHealt = wormHealt;
	}

	/**
     * Getter method to return the Item related info.
     * 
     * @return	the Item related info to display on game info line on the Board.
     */
    public String getInfo() {
		return info;
	}
	
	/**
	 * Getter method to return true if worm has a hit.
	 * 
	 * @return		true if worm has a hit.
	 */
	public int getHitByWeapon() {
		return hitByWeapon;
	}

	/**
	 * Setter method to set true if worm has a hit.
	 * 
	 * @param hitByWeapon	true to set a hit.
	 */
	public void setHitByWeapon(int hitByWeapon) {
		this.hitByWeapon = hitByWeapon;
	}

	/**
	 * Method to decrease worm health.
	 */
	public void decreaseHelth() {
    		wormHealt = wormHealt - ( hitByWeapon + 1 ) * 20;
    		System.out.print("Decrease health: " + ( hitByWeapon + 1 ) * 20 + " : ");
    		
    		hitByWeapon = -1;
    		
    		if (wormHealt <= 0) {
    			wormHealt = 0;
    			moveState = false;
    			//image = imageCemetery;
    		}
    		
    		System.out.println(wormHealt );
    }
    
    /**
     * Method return Rectangle to determine the size of the worm.
     * 
     * @return	Rectangle with worm bounds,
     * to check collision with weapons and terrain objects.
     */
    public Rectangle getBounds() {
    		return new Rectangle((int) x, (int) y, w, h);
    }
    
	/**
	 * Method to update Worm Object's related data on regular basis.
	 * 
	 * Called each frame from game bard class and control the movement of the worm.
	 */
	public void update() {

		if (jumpState ) {
			double collision = checkCollisionJump(velocityY);
			if (collision == 0) {
				y = y + velocityY;
				velocityY = velocityY + gravitySpeed * factor;
				x = x + windVelocity;
			} else {
				y = collision - h - 2;
				jumpState = false;
				velocityY = 0;
			}
			if (y < 0) {
				y = 1;
			}
			//System.out.println("jumpState: " + jumpState + " " + velocityY + " " + y);
		} else {
			double collision = checkCollisionDown(terrain);
			if (collision == 601) {
				y = y + velocityY;
				velocityY = velocityY + gravitySpeed * factor;
				//y = y + gravitySpeed * factor;
			} else {
				y = collision - h - 1;
				velocityY = 0;
			}

		}

		if (moveState) {
			if (!checkCollisionMove(terrain)) {
				x = x + velocityX * factor;
				if (checkCollisionDown(terrain) == 0) {
					y = y + gravitySpeed;
				} 
			} else {
				moveState = false;
			}
			//System.out.println("moveState: " + moveState + " " + velocityX + " " + x);
		}

		if (jumpState || moveState) {
			int type = checkCollisionItem();

			if (type >= 0) {
				processItem(type);
			}			
		}

		if (x < 0) {
			x = 3;
			moveState = false;
		} else if (x > B_WIDTH - w) {
			x = B_WIDTH - w - 3;
			moveState = false;
		}			

		dopingTime--;
		if (dopingTime == 0) {
			moveVelocity = 5;
			jumpVelosity = 15;
		}

		infoTime--;
		if (infoTime == 0) {
			info = null;
		}
		
		if (wormHealt < 0) {
			image = imageCemetery;
		}

		iI = (iI + 1) % current.length;
	}


	/**
	 * Method to calculate effect of collision between Item and worm.
	 * 
	 * @param type	type of Item to calculate effect.
	 */
	private void processItem(int type) {
		infoTime = 30;
		if (type == 0) {
			waffen1Left = waffen1Left + 20;
			info = "Weapon added";
		} else if (type == 1) {
			waffen2Left = waffen2Left + 10;
			info = "Grenades added";
		} else if (type == 2) {
			wormHealt = wormHealt + 70;
			info = "Health impoved";
		} else if (type == 3) {
			moveVelocity = 15;
			jumpVelosity = 45;
			dopingTime = 100;
			info = "Doping added";

		}
	}

	/**
	 * Method to check collision between worm and Item objects.
	 * 
	 * @return	boolean, true if collision is exist.
	 */
	private int checkCollisionItem() {
		Rectangle wormBounds = new Rectangle((int) x, (int) y, w, h);

		for (int i = 0; i < items.size(); i++) {
			if (wormBounds.intersects(items.get(i).getBounds())) {
				//System.out.println("item found: " + items.get(i).getType());
				items.get(i).setVisible(false);
				return items.get(i).getType();
			}
		}
		return -1;
	}

	/**
	 * Method to check collision between worm and terrain objects in case of horisontal move.
	 * 
	 * @param t		Array of terrain object.
	 * @return		boolean, true if collision is exist.
	 */
	private boolean checkCollisionMove(ArrayList<TerrainObject> t) {

		if (directionRight) {
			Rectangle wormBounds = new Rectangle((int) (x + w - 1), (int) y, 2, h);

			for (int i = 0; i < t.size(); i++) {
				if (wormBounds.intersects(t.get(i).getBounds())) {
					return true;
				}
			}
		} else {
			Rectangle wormBounds = new Rectangle((int) (x - 1), (int) y, 2, h);

			for (int i = 0; i < t.size(); i++) {
				if (wormBounds.intersects(t.get(i).getBounds())) {
					return true;
				}
			}
		}

		return false;

	}

    /**
     * Method to check collision between worm and terrain objects in case of moving down.
     * 
     * @param t		Array of terrain object.
     * @return		boolean, true if collision is exist.
     */
    private double checkCollisionDown(ArrayList<TerrainObject> t) {
        Rectangle wormBounds = new Rectangle((int) x, (int) (y - velocityY + 1) , w, (int) (h + velocityY + 1));
        double collision = 601;
        
        if (velocityY > 30 * factor) {		// Too big falling velocity cause dead
        		wormHealt = 0;
        }
        
        for (int i = 0; i < t.size(); i++) {
            if ( wormBounds.intersects(t.get(i).getBounds())){
            		if (collision > t.get(i).getY()) {
            			collision = t.get(i).getY();
            		}
             	//System.out.println("Collision Down: " + collision);
            }
        }
 
        return collision;
    }
    
	/**
	 * Method to check collision during worm's jump.
	 * 
	 * @param yVelocity		vertical velocity of worm
	 * @return				Y position of collision place.
	 */
	private double checkCollisionJump(double yVelocity) {
		Rectangle wormBounds;
		double collision;
		int objectHeight = 0;

		if (yVelocity < 0) {		// movement up
			collision = -1;
			wormBounds = new Rectangle((int) x, (int) (y + yVelocity), w, (int) (- yVelocity + 1));
		} else {
			collision = 601;
			wormBounds = new Rectangle((int) x, (int) (y + h + yVelocity), w, (int) (yVelocity - 1));
		}

		for (int i = 0; i < terrain.size(); i++) {
			if (wormBounds.intersects(terrain.get(i).getBounds())) {
				if (yVelocity < 0) {
					if (collision < terrain.get(i).getY()) {
						collision = terrain.get(i).getY();
						objectHeight = terrain.get(i).getH();
					}
					//System.out.println("Collision Up: " + collision + " - " + objectHeight);
				} else {
					if (collision > terrain.get(i).getY()) {
						collision = terrain.get(i).getY();
					}
					//System.out.println("Collision Down: " + collision);
				}
			}
		}

		if ((collision == 601) || (collision == -1) ) {
			return 0;				// no collision
		} else if (yVelocity < 0) {
			return collision + objectHeight + h + 4;
		} else {
			return collision;
		}		
	}


	/**
	 * Method to process worm move left.
	 * 
	 * @param fps	FPS to adjust game speed
	 */
	public void moveLeft(long fps) {
		directionRight = false;
		current = imagesLeft;
		image = imageStaticLeft;
		iI = 0;
		moveState = true;
		factor = (double) 30 / fps;
		velocityX = - moveVelocity ;
	}
	
	/**
	 * Method to process worm move right.
	 * 
	 * @param fps	FPS to adjust game speed
	 */
	public void moveRight(long fps) {
		directionRight = true;
		current = imagesRight;
		image = imageStaticRight;
		iI = 0;
		moveState = true;
		factor = (double) 30 / fps;
		velocityX = moveVelocity;
	}
	
	/**
	 * Method to process worm jump.
	 * 
	 * @param fps	FPS to adjust game speed
	 */
	public void jump(long fps) {
		if (!jumpState && (velocityY == 0)) {
			jumpState = true;
			factor = (double) 30 / fps;
			velocityY = - jumpVelosity;
			//System.out.println("Jump velosity: " + velocityY);
			SoundEffect.JUMP1.play();
		}
	}
	
	/**
	 * Method to process stop event of worm move.
	 */
	public void stopMove() {
		moveState = false;
		velocityX = 0;
	}
  
      
    /**
     * Method to update Game Data after turn.
     * 
     * @param t		List of terrain objects.
     * @param i		List of item objects.
     * @param o		List of worms belonging to team 1.
     */
    public void updateGameState(ArrayList<TerrainObject> t, ArrayList<Items> i, ArrayList<Worm> o, int wVelocity) {
            this.terrain = t;     
            this.items = i;
            this.others = o; 
            
		if (wormHealt > 0) {
			if (directionRight) {
				image = imageStaticRight;
			} else {
				image = imageStaticLeft;
			}
		} else {
			image = imageCemetery;
		}
            
            windVelocity = wVelocity;
    }
}