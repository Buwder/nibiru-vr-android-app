package com.zskx.view.music;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.zskx.activity.R;
import com.zskx.view.music.MusicMenu.MusicMenuListener;
import com.zskx.view.music.MusicPlayer.MusicPlayerListener;

public class MusicLayout extends LinearLayout {

	/**音乐菜单*/
	private MusicMenu menu;
	/**音乐播放器*/
	private MusicPlayer player;
	
	public MusicLayout(Context context) {
		super(context);
		init();
	}

	public MusicLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init(){
		View.inflate(getContext(), R.layout.music_layout, this);
		
		menu = (MusicMenu)findViewById(R.music.menu);
		player = (MusicPlayer)findViewById(R.music.player);
		
		
		menu.setMusicMenuListener(new MusicMenuListener() {
			@Override
			public void musicBeClicked(int oldTag, int newTag) {
				player.loadMusic(newTag);
				player.play(0);
			}
		});
		
		player.setMusicPlayerListener(new MusicPlayerListener() {

			

			@Override
			public void pauseMusic(int tag) {
				
			}

			@Override
			public void playMusic(int tag) {
				
			}

			@Override
			public void resetMusic(int tag) {
				
			}

			@Override
			public void preMusic(int oldTag, int tag) {
				menu.selectMusicMenuByHand(oldTag, tag);
			}

			@Override
			public void nextMusic(int oldTag, int tag) {
				menu.selectMusicMenuByHand(oldTag, tag);
				
			}
			
			
		});
	}

	/**
	 * 释放播放器资源
	 */
	public void releasePlayer(){
		player.releasePlayer();
	}
	
}
