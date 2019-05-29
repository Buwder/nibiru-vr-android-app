package com.zskx.pemsystem.meida;

import java.io.IOException;
import java.util.ArrayList;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

import com.zskx.net.response.MusicDetailEntity;

public class Myplayer extends MediaPlayer {
	private static final String TAG = "Myplayer";
	private boolean isFirst;
	private ArrayList<MusicDetailEntity> mPlaylist;
	private MusicDetailEntity music_track;
	private MediaPlayer mediaPlayer;
	private int list_pos = 0;
	
	/**
	 * Handler to the context thread
	 */
	private Handler mHandler;
	
	/**
	 * Listener to the engine events
	 */
	private PlayerEngineListener mPlayerEngineListener;
	
	public Myplayer() {
		super();
		isFirst = true;
		mediaPlayer = new MediaPlayer();
		mHandler = new Handler();
	}

	public Myplayer(ArrayList<MusicDetailEntity> mPlaylist) {
		super();
		this.mPlaylist = mPlaylist;
	}

	public int getList_pos() {
		return list_pos;
	}

	public void setList_pos(int list_pos) {
		this.list_pos = list_pos;
	}

	public void openPlaylist(ArrayList<MusicDetailEntity> playlist) {
		if(!playlist.isEmpty()){
			mPlaylist = playlist;
			music_track = mPlaylist.get(list_pos);
		}
		else
			mPlaylist = null;
	}
	public boolean isPlaying(){
		if(mediaPlayer != null) return mediaPlayer.isPlaying();
		else return false;
	}
	
	public void start()  {
		//��һ�β����ļ�
		if(isFirst){
			
			String path = music_track.getMusicUrl();
			mediaPlayer.reset();	//����Ҫ����player
			try {
				mediaPlayer.setDataSource(path);
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mediaPlayer.prepare();
			} catch (IOException e) {
				Log.e(TAG, e.toString());
			}
			
			// starting timer
            mHandler.removeCallbacks(mUpdateTimeTask);
            mHandler.postDelayed(mUpdateTimeTask, 1000);
			
			mediaPlayer.start();
			mPlayerEngineListener.onTrackChanged(music_track);
			if(mPlayerEngineListener != null)
				mPlayerEngineListener.onTrackStart();
		//	btn_play.setBackgroundResource(R.drawable.music_pause);
			isFirst = false;
		//	handler.post(AnotherThread);//����ʼ���ž�������ȸ����߳�
		}
		//������ڲ��ţ�����ͣ
		else if(mediaPlayer.isPlaying()) {
			pause();
			//btn_play.setBackgroundResource(R.drawable.music_button_play);
			
		}
		//�������ͣ���ͼ���
		else if(!isFirst){
			mediaPlayer.start();
			mPlayerEngineListener.onTrackChanged(music_track);
			if(mPlayerEngineListener != null)
				mPlayerEngineListener.onTrackStart();
		//	btn_play.setBackgroundResource(R.drawable.music_pause);
		}
	}
	
	private boolean checkList() {
		if (mPlaylist.size() == 0 ) return false;
		else return true;
	}

	public void pause(){
		if(mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			if(mPlayerEngineListener != null)
				mPlayerEngineListener.onTrackPause();
			return;
		}
	}
	
	public void stop() {
		if(!isFirst){
			close();
			isFirst = true;
			if(mPlayerEngineListener != null){
				mPlayerEngineListener.onTrackStop();
			}
			
		//	handler.removeCallbacks(AnotherThread);//ֹͣʱ��ȥ����½��
			
		//	seekBar.setProgress(0); //���ý�ȹ���
		//	btn_play.setBackgroundResource(R.drawable.music_button_play);
		//	textView_current.setText("00:00");//������ʾ����
		}
	}
	
	/**
	 * mp3��next��
	 */
	public void next() {
		if(list_pos < mPlaylist.size()-1){
			stop();
			list_pos = list_pos + 1;
			music_track = mPlaylist.get(list_pos);
			//textMp3name.setText(mp3model.getMp3_name());
			start();
		}
	}
	/**
	 * mp3��previous��
	 */
	public void previous() {
		if(list_pos > 0){
			stop();
			list_pos = list_pos -1;
			music_track = mPlaylist.get(list_pos);
			//textMp3name.setText(mp3model.getMp3_name());
			start();
		}
	}
	
		
	public void playerSeekTo(int pos){
		if (mediaPlayer != null) {
			if (isFirst) {
				start();
			}
			mediaPlayer.seekTo(pos);
		}
	}
	
	private void close(){
		// nice clean-up job
		if(mediaPlayer != null)
			try{
				mediaPlayer.stop();
			} catch (IllegalStateException e){
				// this may happen sometimes
			} finally {
				mediaPlayer.release();
				mediaPlayer = null;
			}
	}
	
	public void setListener(PlayerEngineListener playerEngineListener) {
		mPlayerEngineListener = playerEngineListener;
	}
	
	/**
     * Runnable periodically querying Media Player
     * about the current position of the track and
     * notifying the listener
     */
    private Runnable mUpdateTimeTask = new Runnable() {
            public void run() {

            	if(mediaPlayer != null)
            		mPlayerEngineListener.onTrackProgress(mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration());
                mHandler.postDelayed(this, 1000);
                    
            }
    };
	
}
