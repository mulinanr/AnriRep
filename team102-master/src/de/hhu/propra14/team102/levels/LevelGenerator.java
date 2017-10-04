/**
 * Main idea from  http://www.javacooperation.gmxhome.de/LandscapeEng.html
 *
 */
package de.hhu.propra14.team102.levels;

import java.util.Random;

import de.hhu.propra14.team102.GameConstants;
import de.hhu.propra14.team102.WormsGame;

/**
 * Class generate random game level.
 * 
 * @author anrio
 */
public class LevelGenerator {

	private Random rand = new Random ();
	private int[] groundMap;
	private int[] grassMap;

	private int[] xObject1;
	private int[] yObject1;

	private int[] xObject2;
	private int[] yObject2;

	private int[] xObject3;
	private int[] yObject3;

	private int[] xTotalObjects;
	private int[] yTotalObjects;

	private int[] xTotalWorms;
	private int[] yTotalWorms;

	private int[] xTeam1;
	private int[] yTeam1;

	private int[] xTeam2;
	private int[] yTeam2;

	private int terrainWidth;

	Level level;

	/**
	 * Constructor of class, no parameters needed.
	 */
	public LevelGenerator() {

		terrainWidth = GameConstants.boardWidth;

		groundMap = new int[terrainWidth];
		grassMap = new int[terrainWidth];

		generateGround();
		generateGrass();
		generateObjects();
		generateWorms();

		//makeLevel();
		//printData();

		//new WormsGame(level);
	}

	/**
	 * Method to generate Ground object data.
	 */
	private void generateGround() {
		int direction = 1;	
		int step = 0;
		int stability = 50;

		groundMap[0] = Math.abs(50 + (rand.nextInt() % 50));

		for (int i = 1; i < terrainWidth; i = i + 1) {
			int last = groundMap[i-1];

			if ( ((last <= 10) && (direction < 0)) || ((last >= 200)  && (direction > 0)) ) {
				direction = direction * (-1);
				step = 2;
			} else if ( (Math.abs(rand.nextInt() % 10) > 5) && (stability <= 0) ) {
				direction = direction * (-1);
				step = Math.abs(rand.nextInt() % 5);
				stability = 40 + Math.abs(rand.nextInt() % 20);
				if (step == 0) {
					stability = 100;
				}
			} else {
				stability--;
			}

			groundMap[i] = last + (direction * step);	
			//System.out.println(i + "\t: " + direction + "\t  "+ step + "\t  " + groundMap[i]);
		}

	}

	/**
	 * Method to generate Grass object data.
	 */
	private void generateGrass() {
		int direction = 1;	
		int step = 0;
		int stability = 10;

		grassMap[0] = Math.abs(5 + (rand.nextInt() % 5));

		for (int i = 1; i < terrainWidth; i = i + 1) {
			int last = grassMap[i-1];

			if ( ((last <= 0) && (direction < 0)) || ((last >= 15)  && (direction > 0)) ) {
				direction = direction * (-1);
				step = 2;
			} else if (Math.abs(rand.nextInt() % 10) > 7) {
				direction = direction * (-1);
			}

			if ( (Math.abs(rand.nextInt() % 10) > 7) && (stability <= 0) ) {
				step = 1;
				stability = Math.abs(rand.nextInt() % 20);
			}

			grassMap[i] = last + (direction * step);
			//System.out.println(i + "\t: " + direction + "\t  "+ step + "\t  " + grassMap[i]);

		}

	}

	/**
	 * Method to generate Terrain objects.
	 */
	private void generateObjects() {

		int amountObject1 = 1 + Math.abs(rand.nextInt() % 3);
		int amountObject2 = 1 + Math.abs(rand.nextInt() % 3);
		int amountObject3 = 1 + Math.abs(rand.nextInt() % 3);

		xTotalObjects = new int[amountObject1 + amountObject2 + amountObject3];
		yTotalObjects = new int[amountObject1 + amountObject2 + amountObject3];

		xObject1 = new int[amountObject1];
		yObject1 = new int[amountObject1];

		xObject2 = new int[amountObject2];
		yObject2 = new int[amountObject2];

		xObject3 = new int[amountObject3];
		yObject3 = new int[amountObject3];

		for (int i = 0; i < amountObject1; i++) {

			xObject1[i] = Math.abs(rand.nextInt() % terrainWidth);
			yObject1[i] = Math.abs(rand.nextInt() % 300);

			xTotalObjects[i] = xObject1[i];
			yTotalObjects[i] = yObject1[i];

			//System.out.println("Object 1: " + i + "  " + xObject1[i] + "  " + yObject1[i] + "      "  + "  " + xTotals[i] + "  " + yTotals[i]);

			int trials = 100;
			while (isCollision(i) && (trials > 0)) {
				xObject1[i] = Math.abs(rand.nextInt() % terrainWidth);
				yObject1[i] = Math.abs(rand.nextInt() % 300);

				xTotalObjects[i] = xObject1[i];
				yTotalObjects[i] = yObject1[i];

				trials--;
				//System.out.println("Collision 1: " + i + "  " + xObject1[i] + "  " + yObject1[i] + "  " + trials);
			}
			//System.out.println("Object 1: " + i + "  " + xObject1[i] + "  " + yObject1[i] + "      "  + "  " + xTotals[i] + "  " + yTotals[i]);
		}

		for (int i = 0; i < amountObject2; i++) {

			xObject2[i] = Math.abs(rand.nextInt() % terrainWidth);
			yObject2[i] = Math.abs(rand.nextInt() % 300);

			xTotalObjects[i + amountObject1] = xObject2[i];
			yTotalObjects[i + amountObject1] = yObject2[i];
			//System.out.println("Object 2: " + i + "  " + xObject2[i] + "  " + yObject2[i] + "      "  + "  " + xTotals[i + amountObject1] + "  " + yTotals[i + amountObject1]);

			int trials = 100;
			while (isCollision(i + amountObject1) && (trials > 0)) {
				xObject2[i] = Math.abs(rand.nextInt() % terrainWidth);
				yObject2[i] = Math.abs(rand.nextInt() % 300);

				xTotalObjects[i + amountObject1] = xObject2[i];
				yTotalObjects[i + amountObject1] = yObject2[i];

				trials--;

				//System.out.println("Collision 2: " + i + "  " + xObject2[i] + "  " + yObject2[i] + "  " + trials);
			}
			//System.out.println("Object 2: " + i + "  " + xObject2[i] + "  " + yObject2[i] + "      "  + "  " + xTotals[i + amountObject1] + "  " + yTotals[i + amountObject1]);
		}

		for (int i = 0; i < amountObject3; i++) {
			xObject3[i] = Math.abs(rand.nextInt() % terrainWidth);
			yObject3[i] = Math.abs(rand.nextInt() % 300);

			xTotalObjects[i + amountObject1 + amountObject2] = xObject3[i];
			yTotalObjects[i + amountObject1 + amountObject2] = yObject3[i];

			int trials = 100;
			while (isCollision(i + amountObject1 + amountObject2) && (trials > 0)) {
				xObject3[i] = Math.abs(rand.nextInt() % terrainWidth);
				yObject3[i] = Math.abs(rand.nextInt() % 300);

				xTotalObjects[i + amountObject1 + amountObject2] = xObject3[i];
				yTotalObjects[i + amountObject1 + amountObject2] = yObject3[i];

				trials--;

				//System.out.println("Collision 2: " + i + "  " + xObject2[i] + "  " + yObject2[i] + "  " + trials);
			}
			//System.out.println("Object 3: " + i + "  " + xObject3[i] + "  " + yObject3[i] + "      "  + "  " + xTotals[i + amountObject1 + amountObject2] + "  " + yTotals[i + amountObject1 + amountObject2]);

		}		
	}

	/**
	 * Method to generate positions for worms.
	 */
	private void generateWorms() {
		xTeam1 = new int[4];
		yTeam1 = new int[4];

		xTeam2 = new int[4];
		yTeam2 = new int[4];

		xTotalWorms  = new int[8];
		yTotalWorms  = new int[8];

		for (int i = 0; i < 4; i++) {
			xTeam1[i] = Math.abs(rand.nextInt() % terrainWidth);
			yTeam1[i] = Math.abs(rand.nextInt() % 320);

			xTotalWorms[i] = xTeam1[i];
			yTotalWorms[i] = yTeam1[i];

			int trials = 10;
			while (isCollisionWorms(i) && (trials > 0)) {
				xTeam1[i] = Math.abs(rand.nextInt() % terrainWidth);
				yTeam1[i] = Math.abs(rand.nextInt() % 320);

				xTotalWorms[i] = xTeam1[i];
				yTotalWorms[i] = yTeam1[i];

				trials--;

				System.out.println("Collision 2: " + i + "  " + xTeam1[i] + "  " + yTeam1[i] + "  " + trials);
			}

		}

		for (int i = 0; i < 4; i++) {
			xTeam2[i] = Math.abs(rand.nextInt() % terrainWidth);
			yTeam2[i] = Math.abs(rand.nextInt() % 320);

			xTotalWorms[i + 4] = xTeam2[i];
			yTotalWorms[i + 4] = yTeam2[i];

			int trials = 10;
			while (isCollisionWorms(i + 4) && (trials > 0)) {
				xTeam2[i] = Math.abs(rand.nextInt() % terrainWidth);
				yTeam2[i] = Math.abs(rand.nextInt() % 320);

				xTotalWorms[i + 4] = xTeam2[i];
				yTotalWorms[i + 4] = yTeam2[i];

				trials--;

				System.out.println("Collision 2: " + i + "  " + xTeam2[i] + "  " + yTeam2[i] + "  " + trials);
			}

		}


	}


	/**
	 * Method to print terrain object's data for test purposes.
	 */
	private void printData() {
		for (int i = 0; i < terrainWidth; i++) {
			System.out.println(i + "\t: " + groundMap[i] + "\t+ " + grassMap[i] + "\t= " + (600 - groundMap[i] - grassMap[i]) );
		}

		for (int i = 0; i < 4; i++) {
			System.out.println(i + "\t: " + xTeam1[i] + "\t+ " + yTeam1[i]);
		}

		for (int i = 0; i < 4; i++) {
			System.out.println(i + "\t: " + xTeam2[i] + "\t+ " + yTeam2[i]);
		}

	}

	/**
	 * Method to check collision between terrain objects.
	 * 
	 * No less than 60 point between terrain objects is allowed.
	 * 
	 * @param k		number of terrain objects to check
	 * @return		true, if collision exist.
	 */
	private boolean isCollision(int k) {
		for (int i = 0; i < k; i++) {
			for (int j = i + 1; j < k+1; j++) {
				if ( Math.abs(xTotalObjects[i] - xTotalObjects[j]) < 60 ) {
					if ( Math.abs(yTotalObjects[i] - yTotalObjects[j]) < 60 ) {
						return true;
					}

				}
			}
		}
		return false;
	}

	/**
	 * Method to check collision between worms.
	 * 
	 * No less than 100 point between worms is allowed.
	 * 
	 * @param k		number of worms to check
	 * @return		true, if collision exist.
	 */
	private boolean isCollisionWorms(int k) {
		for (int i = 0; i < k; i++) {
			for (int j = i + 1; j < k+1; j++) {
				if ( Math.abs(xTotalWorms[i] - xTotalWorms[j]) < 100 ) {
					if ( Math.abs(yTotalWorms[i] - yTotalWorms[j]) < 100 ) {
						return true;
					}

				}
			}
		}
		return false;
	}

	/**
	 * Method to make Level object from randomly generated data.
	 */
	public Level makeLevel() {
		level = new Level();

		for (int i = 0; i < terrainWidth; i = i + 3) {

			for (int j = 580; j > 580 - groundMap[i]; j--) {
				level.getGroundXIndex().add(i);
				level.getGroundYIndex().add(j);
			}

			for (int j = 580 - groundMap[i]; j > 580 - groundMap[i] - grassMap[i]; j--) {
				level.getGrassXIndex().add(i);
				level.getGrassYIndex().add(j);
			}		
		}

		for (int i = 0; i < xObject1.length; i++) {
			level.setX(i, xObject1[i]);
			level.setY(i, yObject1[i]);
			level.setType(i, 0);
		}

		for (int i = 0; i < xObject2.length; i++) {
			level.setX(i, xObject2[i]);
			level.setY(i, yObject2[i]);
			level.setType(i, 1);
		}

		for (int i = 0; i < xObject3.length; i++) {
			level.setX(i, xObject3[i]);
			level.setY(i, yObject3[i]);
			level.setType(i, 2);
		}
		
		for (int i = 0; i < 4; i++) {
			level.setTeam1X(xTeam1[i]);
			level.setTeam1Y(yTeam1[i]);
			
			level.setTeam2X(xTeam2[i]);
			level.setTeam2Y(yTeam2[i]);			
		}
		
		return level;
		
	}


	/**
	 * main method to directly run the generator class.
	 * 
	 * @param args	no any parameters needed.
	 */
	public static void main(String[] args) {
		new LevelGenerator();

	}

}