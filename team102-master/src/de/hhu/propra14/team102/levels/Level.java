package de.hhu.propra14.team102.levels;

import java.util.ArrayList;

/**
 * Class define game level data. It include position of ground objects, posotion
 * of grass objects, position of wolke, haus nad andere objects and position of
 * worms.
 * 
 * @author anrio
 * 
 */
public class Level {

	private int levelNumber;

	private ArrayList<Integer> groundXIndex;
	private ArrayList<Integer> groundYIndex;

	private ArrayList<Integer> grassXIndex;
	private ArrayList<Integer> grassYIndex;

	private ArrayList<Integer> xObjectIndex;
	private ArrayList<Integer> yObjectIndex;
	private ArrayList<Integer> tObjectIndex;

	private ArrayList<Integer> xTeam1Index;
	private ArrayList<Integer> yTeam1Index;

	private ArrayList<Integer> xTeam2Index;
	private ArrayList<Integer> yTeam2Index;

	private int[][][] levelPositions = {
			// Level 0
			{ { 10, 50, 0 }, { 150, 200, 0 }, // Wolke
					{ 100, 300, 1 }, // Haus
					{ 500, 30, 2 }, { 650, 150, 2 }, { 300, 70, 2 } }, // Anderer

			// Level 1
			{ { 10, 50, 0 }, { 150, 200, 0 }, { 150, 300, 0 }, { 150, 400, 0 }, // Wolke
					{ 100, 300, 1 }, { 200, 300, 1 }, // Haus
					{ 700, 250, 2 }, { 500, 250, 2 } }, // Anderer

			// Level 2
			{ { 10, 50, 0 }, { 10, 50, 0 }, { 10, 50, 0 }, { 10, 50, 0 },
					{ 10, 50, 0 }, { 10, 50, 0 }, // Wolke
					{ 100, 300, 1 }, { 100, 300, 1 }, // Haus
					{ 500, 250, 2 }, { 500, 250, 2 }, { 500, 250, 2 } }, // Anderer
	};

	/**
	 * Constructor to create a Level object template for Level random genetator.
	 */
	public Level() {

		groundXIndex = new ArrayList<Integer>();
		groundYIndex = new ArrayList<Integer>();

		grassXIndex = new ArrayList<Integer>();
		grassYIndex = new ArrayList<Integer>();

		xObjectIndex = new ArrayList<Integer>();
		yObjectIndex = new ArrayList<Integer>();
		tObjectIndex = new ArrayList<Integer>();

		xTeam1Index = new ArrayList<Integer>();
		yTeam1Index = new ArrayList<Integer>();

		xTeam2Index = new ArrayList<Integer>();
		yTeam2Index = new ArrayList<Integer>();

	}

	/**
	 * Constructor to create a Level object from predefined Level data.
	 * 
	 * Create X position arrays and Y position arrays. Then fill them with data
	 * about actual position of objects according level number.
	 * 
	 * @param levelNumber
	 */
	public Level(int levelNumber) {
		this.levelNumber = levelNumber;

		groundXIndex = new ArrayList<Integer>();
		groundYIndex = new ArrayList<Integer>();

		grassXIndex = new ArrayList<Integer>();
		grassYIndex = new ArrayList<Integer>();

		xObjectIndex = new ArrayList<Integer>();
		yObjectIndex = new ArrayList<Integer>();
		tObjectIndex = new ArrayList<Integer>();

		xTeam1Index = new ArrayList<Integer>();
		yTeam1Index = new ArrayList<Integer>();

		xTeam2Index = new ArrayList<Integer>();
		yTeam2Index = new ArrayList<Integer>();

		makeGroundObject(levelNumber);
		makeGrassObject(levelNumber);
		makeObjects(levelNumber);
		makeWorms();

	}

	/**
	 * Getter method to return size of Terrain Object's array.
	 * 
	 * @return size of Terrain Object's array.
	 */
	public int getSize() {
		// return levelPositions[levelNumber].length;
		return xObjectIndex.size();
	}

	/**
	 * Getter method to return X position of Terrain Object with index i.
	 * 
	 * @param i
	 *            index of Terrain Object.
	 * @return X position of Terrain Object.
	 */
	public int getX(int i) {
		// return levelPositions[levelNumber][i][0];
		return xObjectIndex.get(i);
	}

	/**
	 * Setter method to set X position of Terrain Object with index i.
	 * 
	 * @param i
	 *            index of Terrain Object.
	 * @param positionX
	 *            value to set as X position of Terrain Object.
	 */
	public void setX(int i, int positionX) {
		xObjectIndex.add(positionX);
	}

	/**
	 * Getter method to return Y position of Terrain Object with index i.
	 * 
	 * @param i
	 *            index of Terrain Object.
	 * @return Y position of Terrain Object.
	 */
	public int getY(int i) {
		// return levelPositions[levelNumber][i][1];
		return yObjectIndex.get(i);
	}

	/**
	 * Setter method to set Y position of Terrain Object with index i.
	 * 
	 * @param i
	 *            index of Terrain Object.
	 * @param positionY
	 *            value to set as Y position of Terrain Object.
	 */
	public void setY(int i, int positionY) {
		yObjectIndex.add(positionY);
		;
	}

	/**
	 * Getter method to return X position of Teams 1 with index i.
	 * 
	 * @param i
	 *            index of worm in Team 1 array.
	 * @return X position of Worm Object.
	 */
	public int getTeam1X(int i) {
		return xTeam1Index.get(i);
	}

	/**
	 * Setter method to set X position of worms in Teams 1 array.
	 * 
	 * @param xPosition
	 *            value to set as X position of Worm Object.
	 */
	public void setTeam1X(int xPosition) {
		xTeam1Index.add(xPosition);
	}

	/**
	 * Getter method to return Y position of Teams 1 with index i.
	 * 
	 * @param i
	 *            index of worm in Team 1 array.
	 * @return Y position of Worm Object.
	 */
	public int getTeam1Y(int i) {
		return yTeam1Index.get(i);
	}

	/**
	 * Setter method to set Y position of worms in Teams 1 array.
	 * 
	 * @param yPosition
	 *            value to set as X position of Worm Object.
	 */
	public void setTeam1Y(int yPosition) {
		yTeam1Index.add(yPosition);
	}

	/**
	 * Getter method to return X position of Teams 2 with index i.
	 * 
	 * @param i
	 *            index of worm in Team 2 array.
	 * @return X position of Worm Object.
	 */
	public int getTeam2X(int i) {
		return xTeam2Index.get(i);
	}

	/**
	 * Setter method to set X position of worms in Teams 2 array.
	 * 
	 * @param xPosition
	 *            value to set as X position of Worm Object.
	 */
	public void setTeam2X(int xPosition) {
		xTeam2Index.add(xPosition);
	}

	/**
	 * Getter method to return Y position of Teams 2 with index i.
	 * 
	 * @param i
	 *            index of worm in Team 2 array.
	 * @return Y position of Worm Object.
	 */
	public int getTeam2Y(int i) {
		return yTeam2Index.get(i);
	}

	/**
	 * Setter method to set Y position of worms in Teams 2 array.
	 * 
	 * @param yPosition
	 *            value to set as X position of Worm Object.
	 */
	public void setTeam2Y(int yPosition) {
		yTeam2Index.add(yPosition);
	}

	/**
	 * Method return the type of Terrain object with index i.
	 * 
	 * @param i
	 *            index of element in Terrain object array.
	 * @return type of the element.
	 */
	public String getType(int i) {

		if (tObjectIndex.get(i) == 0) {
			return "wolke.png";
		} else if (tObjectIndex.get(i) == 1) {
			return "haus.png";
		} else if (tObjectIndex.get(i) == 2) {
			return "andere.png";
		}
		return "nicht.png";
	}

	/**
	 * Method set the type of Terrain object with index i.
	 * 
	 * @param i
	 *            index of element in Terrain object array.
	 * @param type
	 *            value to set as type of element.
	 */
	public void setType(int i, int type) {
		tObjectIndex.add(type);
	}

	/**
	 * Method makes the Terrain of Level object from arrays data.
	 * 
	 * @param levelNumber
	 *            number of level map to make.
	 */
	private void makeObjects(int levelNumber) {

		for (int i = 0; i < levelPositions[levelNumber].length; i++) {
			xObjectIndex.add(levelPositions[levelNumber][i][0]);
			yObjectIndex.add(levelPositions[levelNumber][i][1]);
			tObjectIndex.add(levelPositions[levelNumber][i][2]);
		}
	}

	/**
	 * Method makes position of worms for defined Level.
	 */
	private void makeWorms() {
		xTeam1Index.add(100);
		yTeam1Index.add(510);

		xTeam1Index.add(150);
		yTeam1Index.add(400);

		xTeam1Index.add(200);
		yTeam1Index.add(300);

		xTeam1Index.add(300);
		yTeam1Index.add(200);

		xTeam2Index.add(500);
		yTeam2Index.add(270);

		xTeam2Index.add(550);
		yTeam2Index.add(230);

		xTeam2Index.add(600);
		yTeam2Index.add(170);

		xTeam2Index.add(650);
		yTeam2Index.add(120);
	}

	/**
	 * Method makes position of Ground Objects for defined Level.
	 * 
	 * @param levelNumber
	 *            number of level map to make.
	 */
	private void makeGroundObject(int levelNumber) {
		// Ground restangle
		for (int i = 0; i < 300; i = i + 3) {
			int startY = 560;
			for (int j = startY; j <= 600; j = j + 3) {
				getGroundXIndex().add(i);
				getGroundYIndex().add(j);
			}
		}

		for (int i = 300; i < 550; i = i + 3) {
			for (int j = (int) (560 - (i - 300) * 0.7); j <= 600; j = j + 3) {
				getGroundXIndex().add(i);
				getGroundYIndex().add(j);
			}
		}

		for (int i = 550; i < 800; i = i + 3) {
			for (int j = (int) (375 + Math.sqrt(i - 550) * 10); j <= 600; j = j + 3) {
				getGroundXIndex().add(i);
				getGroundYIndex().add(j);
			}
		}
	}

	/**
	 * Method makes position of Grass Objects for defined Level.
	 * 
	 * @param levelNumber
	 *            number of level map to make.
	 */
	private void makeGrassObject(int levelNumber) {
		// Ground restangle
		for (int i = 0; i < 300; i = i + 3) {
			int startY = 560;
			for (int j = startY - 12; j <= startY; j = j + 3) {
				getGrassXIndex().add(i);
				getGrassYIndex().add(j);
			}
		}

		for (int i = 300; i < 550; i = i + 3) {
			int startY = (int) (560 - (i - 300) * 0.7);
			for (int j = startY - 20; j <= startY; j = j + 3) {
				getGrassXIndex().add(i);
				getGrassYIndex().add(j);
			}
		}

		for (int i = 550; i < 800; i = i + 3) {
			int startY = (int) (375 + Math.sqrt(i - 550) * 10);
			for (int j = startY - 20; j <= startY; j = j + 3) {
				getGrassXIndex().add(i);
				getGrassYIndex().add(j);
			}
		}
	}

	/**
	 * Getter method to return Ground Object X position.
	 * 
	 * @return X position of Ground Object.
	 */
	public ArrayList<Integer> getGroundXIndex() {
		return groundXIndex;
	}

	/**
	 * Setter method to set value for Ground Object's X position.
	 * 
	 * @param groundXIndex
	 *            value to set as X position.
	 */
	public void setGroundXIndex(ArrayList<Integer> groundXIndex) {
		this.groundXIndex = groundXIndex;
	}

	/**
	 * Getter method to return Ground Object Y position.
	 * 
	 * @return Y position of Ground Object.
	 */
	public ArrayList<Integer> getGroundYIndex() {
		return groundYIndex;
	}

	/**
	 * Setter method to set value for Ground Object's Y position.
	 * 
	 * @param groundYIndex
	 *            value to set as Y position.
	 */
	public void setGroundYIndex(ArrayList<Integer> groundYIndex) {
		this.groundYIndex = groundYIndex;
	}

	/**
	 * Getter method to return Grass Object X position.
	 * 
	 * @return X position of Grass Object.
	 */
	public ArrayList<Integer> getGrassXIndex() {
		return grassXIndex;
	}

	/**
	 * Setter method to set value for Grass Object's X position.
	 * 
	 * @param grassXIndex
	 *            value to set as X position.
	 */
	public void setGrassXIndex(ArrayList<Integer> grassXIndex) {
		this.grassXIndex = grassXIndex;
	}

	/**
	 * Getter method to return Grass Object Y position.
	 * 
	 * @return Y position of Grass Object.
	 */
	public ArrayList<Integer> getGrassYIndex() {
		return grassYIndex;
	}

	/**
	 * Setter method to set value for Grass Object's Y position.
	 * 
	 * @param grassYIndex
	 *            value to set as Y position.
	 */
	public void setGrassYIndex(ArrayList<Integer> grassYIndex) {
		this.grassYIndex = grassYIndex;
	}

}