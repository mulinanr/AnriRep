
package de.hhu.propra14.team102;

import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import com.sun.org.apache.bcel.internal.generic.LUSHR;

/**
 * The class to controll items that worms could collect.
 * 
 * Each item appear in random basis. It has limited life time. May be four different kinds of items:
 * weapons for bazooka - add some bazooka weapon;
 * weapon for grnade - add some grenades;
 * medicine - give some additionals amound of healts to the worm;
 * Doping - temporary increase move and jump velocity of the worm.
 * 
 * @author anrio
 *
 */
public class Items {
	
	private ArrayList<TerrainObject> terrain;
	
	private int type;
	private int lifeTime = 1000;
	
	private int x;
	private int y;
	private int w;
	private int h;
	
	private Image image;
	private Image [] images;
	private int imageIndex;
	private String imagePath;
	
	private boolean isVisible;
	
	/**
	 * Constructor, create a new item of random type.
	 * 
	 * @param t		List of terrain objects to check for collision.
	 */
	public Items(ArrayList<TerrainObject> t) {
		
		terrain = t;
		
		imagePath = GameConstants.imagePath;
		
		setItemTypeAndPosition();
		
		image = loadImages(type);
		w = image.getWidth(null);
		h = image.getHeight(null);
		
		isVisible = true;
		
		System.out.println("Item added: " + x + "  " + y + "   " + type);
	}

	/**
	 * Constructor, create a new item.
	 * 
	 * @param t		List of terrain objects to check for collision.
	 * @param type	Type of the Item
	 * @param x		X Position of Item
	 * @param y		Y Position of Item
	 */
	public Items(ArrayList<TerrainObject> t, int type, int x, int y) {
		this.terrain = t;
		this.type = type;
		this.x = x;
		this.y = y;
		
		imagePath = GameConstants.imagePath;
		
		image = loadImages(type);
		w = image.getWidth(null);
		h = image.getHeight(null);
		
		isVisible = true;
		
		//System.out.println("Item added: " + x + "  " + y + "   " + type);
	}

	/**
	 * Method to generate item type and position on random basis.
	 */
	private void setItemTypeAndPosition() {
		double random = Math.random();
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
		y = 25;
	}

	/**
	 * Method to load image of item, based on item type.
	 * 
	 * @param type	The type of the item generated
	 * @return		The image to be displayed on game board.
	 */
	private Image loadImages(int type) {
		ImageIcon ii = null;
		
		if (type == 0) {
			ii = new ImageIcon(this.getClass().getResource(imagePath + "bullets.png"));
		} else if (type == 1) {
			ii = new ImageIcon(this.getClass().getResource(imagePath + "grenades.png"));
		} else if (type == 2) {
			ii = new ImageIcon(this.getClass().getResource(imagePath + "medicine.png"));
		} else {
			ii = new ImageIcon(this.getClass().getResource(imagePath + "doping.png"));
		}
		return ii.getImage();
	}

	/**
	 * Getter method to return the type of the item.
	 * 
	 * @return	The type of item as integer, may be 0, 1, 2, or 3.
	 */
	public int getType() {
		return type;
	}

	/**
	 * Getter method to return the X position of the item.
	 * 
	 * @return	X position of the item.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Getter method to return the Y position of the item.
	 * 
	 * @return	Y position of the item.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Getter method to return the image of the item.
	 * 
	 * @return	The image of item to be displayed on game board.
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * Getter method to return the visibility of the item.
	 * 
	 * @return	The visibility of item, is used to decide whether item should be deleted.
	 */
	public boolean isVisible() {
		return isVisible;
	}
	
	/**
	 * Setter method to set the visibility of the item.
	 * 
	 * Used to set the visibility to false.
	 * 
	 * @param isVisible	value to set, only false could be used
	 */
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	/**
	 * Method to update item's related data on regular basis.
	 * 
	 * Called each frame from game bard class and control the falling of the item.
	 * 
	 */
	public void update() {
		
		if (!checkCollision()) {
			y = y + 1;
		}

		lifeTime--;
		if (lifeTime < 0) {
			isVisible = false;
		}
	}

	/**
	 * Method to check collision between terrain objects and the item.
	 * 
	 * Method detect the moment when the item, during it's falling,  reach the ground or 
	 * some other terrain object.
	 * 
	 * @return	true if collision occur.
	 */
	private boolean checkCollision() {
		for (int i = 0; i < terrain.size(); i++) {
			if (new Rectangle(x, y, w, h).intersects(terrain.get(i).getBounds())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Method return Rectangle to determine the size of the item.
	 * 
	 * @return	Rectangle with item bounds, 
	 * to check collision with worms in worm class.
	 */
	public Rectangle getBounds() {
		return new Rectangle(x, y, w, h);
	}
	
}
