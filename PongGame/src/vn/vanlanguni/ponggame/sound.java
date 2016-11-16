package vn.vanlanguni.ponggame;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class sound {
	public synchronized void play(final String FileName){
	
	new	 Thread (new  Runnable() {
		public void run() {
			try {
				Clip clip = AudioSystem.getClip();
				AudioInputStream inputStream = AudioSystem.getAudioInputStream((new File (FileName)));
				clip.open();
				clip.start();
				
			} catch (Exception e) {
				System.out.println("play sound error: " + e.getMessage()+"for" +FileName);
			}
		}
		}).start();
	}
	
	
		
}