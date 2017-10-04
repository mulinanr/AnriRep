
package de.hhu.propra14.team102;

import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;


/**
 * Utility class to make sound effects during game.
 * 
 * All sound track from http://www.shockwave-sound.com/sound-effects/explosion_sounds.html,
 * code main idea from http://www3.ntu.edu.sg/home/ehchua/programming/java/J8c_PlayingSound.html
 * 
 * @author anrio
 *
 */
public enum SoundEffect {
	EXPLODE1("theme1/explode.wav"), 			// Theme 1 sound files
	BULLET1("theme1/shot_bullet.wav"), 
	BAZOOKA1("theme1/shot_bazooka.wav"), 
	JUMP1("theme1/jump.wav"), 
	HIT1("theme1/damage.wav"),

	EXPLODE2("theme1/explode.wav"), 			// Theme 2 sound files
	BULLET2("theme1/shot_bullet.wav"), 
	BAZOOKA2("theme1/shot_bazooka.wav"), 
	JUMP2("theme1/jump.wav"), 
	HIT2("theme1/damage.wav");

//	public static enum Volume {
//		MUTE, LOW, MEDIUM, HIGH
//	}
//
//	public static Volume volume = Volume.HIGH;

	private Clip clip;

	/**
	 * Constructor, load the sound clip file.
	 * 
	 * @param soundFileName	The name of sound clip to load
	 */
	private SoundEffect(String soundFileName) {

		try {

			AudioInputStream audioInputStream = AudioSystem
					.getAudioInputStream(new File(soundFileName));
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to play the sound clip file
	 */
	public void play() {
		if (clip.isRunning()) {
			clip.stop();
		}
		clip.setFramePosition(0);
		clip.start();
	}

//	static void init() {
//		values();
//	}

}
