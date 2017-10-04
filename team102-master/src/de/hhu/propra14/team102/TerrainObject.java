package de.hhu.propra14.team102;

import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

/**
 * Class to control Terrain Objects located on the Game Boards.
 * 
 * There would be five type of Terrain objects:
 * Ground - can be destroyed by weapon.
 * Grass - can be destroyed by weapon
 * Wolke - can't be destroyed by weapon
 * Haus - can't be destroyed by weapon
 * Andere - can't be destroyed by weapon.
 * 
 * Terrain Items can collide with worms, with items and with weapons.
 * Collision with worm objects and item objects stops them. 
 * Collision with weapon objects cause explosions.
 * 
 * @author anrio
 */
public class TerrainObject {
	
//	Image image;
	private int x, y;
	private int w, h;
	private int iT;
	boolean r;
	private String imagePath;
	
	/*
	 *  Image Type
	 *  0 - Wolke
	 *  1 - Hous
	 *  2 - Andere
	 *  3 - Ground
	 *  4 - Grass
	 *  */
	
	/**
	 * Contructor for Terrain Object. The X and Y positions and Object's type are required.
	 * 
	 * Constructor instantiate the Objects and load image of Terrain Object according the type.
	 * 
	 * @param x		X Position of Object
	 * @param y		Y Position of Object
	 * @param type	type of Object
	 */
	public TerrainObject(int x, int y, String type) {
		
		this.x = x;
		this.y = y;
		r = false;
		imagePath = GameConstants.imagePath;
		
		ImageIcon ii = new ImageIcon(this.getClass().getResource(imagePath + type));
		w = ii.getIconWidth();
		h = ii.getIconHeight();
		
//		image = ii.getImage();
//		width = image.getWidth(null);
//		height = image.getHeight(null);
		
		switch (type) {
		case "grass.png":
			iT = 4;
			break;
			
		case "ground.png":
			iT = 3;
			break;

		case "andere.png":
			iT = 2;
			break;

		case "haus.png":
			iT = 1;
			break;

		case "wolke.png":
			iT = 0;
			break;
			
		case "explosion.jpeg":
			iT = 6;
			break;

		default:
			iT = 5;
		}

		
	}
	
	/**
	 * Method return Rectangle to determine the size of the Terrain Object.
	 * 
	 * @return	Rectangle with Terrain Object bounds, 
	 * to check collision with worms in Worm class,
	 * with weapons in Weapon class and with item in Itemclass.
	 */
	public Rectangle getBounds() {
		return new Rectangle(x, y, w, h);
	}

	/**
	 * Getter method to return the Y position of the Terrain Object.
	 * 
	 * @return	X position of the Terrain Object.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Getter method to return the Y position of the Terrain Object.
	 * 
	 * @return	Y position of the Terrain Object.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Getter method to return the height of the Terrain Object's image.
	 * 
	 * @return	height of the Terrain Object's image.
	 */
	public int getH() {
		return h;
	}

	/**
	 * Getter method to return the width of the Terrain Object's image.
	 * 
	 * @return	width of the Terrain Object's image.
	 */
	public int getW() {
		return w;
	}

	/**
	 * Getter method to return the index of the Terrain Object's image.
	 * 
	 * @return	index of the Terrain Object's image.
	 */
	public int getiT() {
		return iT;
	}

	/**
	 * Getter method to return the need to remove the Terrain Object's.
	 * 
	 * @return	need to remove the Terrain Object's.
	 */
	public boolean isRemove() {
		return r;
	}

	/**
	 * Setter method to set the need to remove the Terrain Object's.
	 * 
	 * Used to set the need to remove of the Terrain Object's to true.
	 * 
	 * @param remove		value to set, only true could be used
	 */
	public void setRemove(boolean remove) {
		this.r = remove;
	}

}
