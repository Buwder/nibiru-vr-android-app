package com.zskx.pemsystem.meida;

import android.media.MediaPlayer;
import android.os.Handler;

public class PEM_player {
	
	private MediaPlayer inter_mediaPlayer;
	private boolean isPlayingForward = false;
	
	public PEM_player() {
		super();
		inter_mediaPlayer = new MediaPlayer();
	}
	
	private Handler handler = new Handler();
	private Runnable stateListener = new Runnable() {
		
		@Override
		public void run() {
		}
	};
}
