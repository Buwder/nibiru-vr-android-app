package com.zskx.pemsystem.meida;

import android.media.MediaPlayer;

public class MyMediaPlayer extends MediaPlayer {
	public boolean isPause = false;
	
	@Override
	public void pause() throws IllegalStateException {
		isPause = true;
		super.pause();
	}
 
	@Override
	public void start() throws IllegalStateException {
		isPause = false;
		super.start();
	}

	@Override
	public void stop() throws IllegalStateException {
		isPause = false;
		super.stop();
	}
	
}
