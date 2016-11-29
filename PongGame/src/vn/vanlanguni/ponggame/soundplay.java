package vn.vanlanguni.ponggame;


import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class soundplay {
	private Clip clip;
public soundplay(File path){
	try{
AudioInputStream as;
as = AudioSystem.getAudioInputStream(path);
AudioFormat bs = as.getFormat();
AudioFormat de = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, bs.getSampleRate(), 16,bs.getChannels(), bs.getChannels()*2, bs.getSampleRate(), false);
AudioInputStream ass  = AudioSystem.getAudioInputStream(de,as);
clip = AudioSystem.getClip();
clip.open(ass);
	}catch(Exception e)
	{
		
	}
}
	
public void play(){
	clip.setFramePosition(0);
	clip.start();
}
public void playMusic(){
	clip.setFramePosition(0);
	clip.loop(1000000000);
}
public void stop(){
	if (clip.isRunning()) clip.stop();
	
}
public void close(){
	clip.close();
}
	
		
}