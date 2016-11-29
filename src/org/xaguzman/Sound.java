package org.xaguzman;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public enum Sound {
   MOVE("/move.wav"),
   SCORE("/score.wav"),
   WRONG("/wrong.wav"),
   GAMEOVER("/gameover.wav"); 
   
   public static enum Volume {
      OFF, ON;
   }
   
   public static Volume volume = Volume.ON;
   
   private Clip clip;
   
   Sound(String soundFileName) {
      
         URL url = Sound.class.getResource(soundFileName);
         AudioInputStream audioInputStream;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(url);
			clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
		
		
                
   }
   
   // Play or Re-play the sound effect from the beginning, by rewinding.
   public void play() {
      if (volume == Volume.ON) {
         if (clip.isRunning())
            clip.stop();   // Stop the player if it is still running
         clip.setFramePosition(0); // rewind to the beginning
         clip.start();     // Start playing
      }
   }
   
   static void init() {
      values(); // calls the constructor for all the elements
   }
}