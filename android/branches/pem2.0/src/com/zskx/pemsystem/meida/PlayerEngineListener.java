package com.zskx.pemsystem.meida;

import com.zskx.net.response.MusicDetailEntity;


public interface PlayerEngineListener {

	/**
	 * Callback invoked before starting a new track, return false to prevent
	 * playback from happening
	 * 
	 * @param playlistEntry
	 */
	public boolean onTrackStart(); 
	
	/**
	 * Callback invoked when a new track is played
	 * 
	 * @param playlistEntry
	 */
	public void onTrackChanged(MusicDetailEntity music_track); 
	
	/**
	 * Callback invoked periodically, contains info on track 
	 * playing progress
	 * 
	 * @param seconds int value
	 */
	public void onTrackProgress(int seconds , int duration);
	
	/**
	 * Callback invoked when buffering a track
	 * 
	 * @param percent
	 */
	public void onTrackBuffering(int percent);
	
	/**
	 * Callback invoked when track is stoped
	 */
	public void onTrackStop();
	
	/**
	 * Callback invoked when track is paused
	 */
	public void onTrackPause();
	
	/**
	 * Callback invoked when there was an error with
	 * streaming
	 */
	public void onTrackStreamError();
}
