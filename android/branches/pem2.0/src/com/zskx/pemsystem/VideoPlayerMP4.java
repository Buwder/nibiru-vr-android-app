package com.zskx.pemsystem;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.zskx.net.response.VideoDetailEntity;

public class VideoPlayerMP4 extends Activity {

	private static String TAG = "media player original test";
	
	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	private MyMediaPlayer mediaPlayer;
	private MediaController mMediaController;
	private AudioManager audioManager;
	
	private TextView text_title;
	private Button button_prev;
	private Button button_play;
	private Button button_ff;
	private Button button_vol;
	private SeekBar seekBar;
	private SeekBar seekBar_vol;
	private TextView text_cur;
	private TextView text_all;
	private View lyt_controller;
	private View lyt_bottom;
	private View lyt_buffer;
	private int vol_progress;
	private int volume;
	
	private VideoDetailEntity videoDetailEntity;
	private String video_url="";
	
	private Timer timer;
    private TimerTask timerTask;
    private Handler handler  ;
	private Runnable showController ;
    public static final int TIMER = 1898966;
	
    private int cur_time;
    private int all_time;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.movie_videoplayer);
		
		initDataOrEvent();
		initComponent();
		initHandlerCtl();
	}

	private void initHandlerCtl() {
		handler  = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case TIMER:
					if(lyt_controller.getVisibility() == View.VISIBLE){
						lyt_controller.setVisibility(View.INVISIBLE);
						System.out.println("Controllor dismiss!");
					}
					break;
				}
			}
		};
		
		showController = new Runnable() {
			
			@Override
			public void run() {
				handler.sendEmptyMessage(TIMER);
			}
		};
	}

	private void initDataOrEvent() {
		Intent intent = getIntent();
		videoDetailEntity = (VideoDetailEntity) intent.getSerializableExtra(MovieItemsActivity.VIDEOENTITYDATAIL_KEY);
		video_url = videoDetailEntity.getVideoUrl();
	}

	private void initComponent() {
		init_buttons();
		
		init_mediaplayer();
		
		surfaceView = (SurfaceView) findViewById(R.movie.surface);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceHolder.addCallback(callback);
		
	}
	
	private void init_buttons() {
		text_title = (TextView) findViewById(R.movie.title);
		text_title.setText(videoDetailEntity.getVideoTitle());
		button_prev = (Button) findViewById(R.movie.prev);
		button_play = (Button) findViewById(R.movie.play_pause);
		button_ff = (Button) findViewById(R.movie.ff);
		button_vol = (Button) findViewById(R.movie.volume);
		seekBar = (SeekBar) findViewById(R.movie.seelbar);
		seekBar_vol = (SeekBar) findViewById(R.movie.seekbar_vol);
		text_cur = (TextView) findViewById(R.movie.text_current);
		text_all = (TextView) findViewById(R.movie.text_all);
		lyt_controller = findViewById(R.movie.contrller);
		lyt_controller.setVisibility(View.INVISIBLE);
		lyt_bottom = findViewById(R.movie.lyt_bottom);
		lyt_buffer = findViewById(R.movie.progress_buffer);
//		lyt_buffer.setVisibility(View.INVISIBLE);
		audioManager = (AudioManager) VideoPlayerMP4.this.getSystemService(AUDIO_SERVICE);
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		newPlayer();
		super.onResume();
	}

	private void newPlayer() {
		if(mediaPlayer == null ){
			initDataOrEvent();
			init_mediaplayer();
		}
	}
	/**
	 * 初始化播放器
	 */
	private void init_mediaplayer() {
		mediaPlayer = new MyMediaPlayer();
//		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
			
			@Override
			public void onBufferingUpdate(MediaPlayer mp, int percent) {
//				System.out.println("percent:" + percent);
				if (percent < 100 && percent%10==0) System.out.println("onBufferingUpdate::::" + percent);
				seekBar.setSecondaryProgress(percent);
			}
		});
		mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.start();
			}
		});
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.start();
			}
		});
		  
		
		System.out.println("mediaPlayer::" + mediaPlayer);
		setPlayerPath(video_url);

	}

	private Callback callback = new Callback() {
		
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			finishPLayer();
		}
		
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			mediaPlayer.setDisplay(surfaceHolder);
//			attachMediaController();
			bindListener();
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub
			
		}
	};
	
	/**
	 * 绑定监听器，所有按钮和进度条。自定义界面使用
	 */
	private void bindListener() {
		button_prev.setOnClickListener(buttonClickListener);
		button_play.setOnClickListener(buttonClickListener);
		button_ff.setOnClickListener(buttonClickListener);
		button_vol.setOnClickListener(buttonClickListener);
		lyt_bottom.setOnClickListener(buttonClickListener);
		
		seekBar.setMax(100);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				setTimeView();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				handler.removeCallbacks(showController);
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				startTimer();
				int pos = seekBar.getProgress();
				newPlayer();
				all_time = mediaPlayer.getDuration();
				mediaPlayer.seekTo( all_time * pos / 100);
			}});
		seekBar_vol.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		vol_progress = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		seekBar_vol.setProgress(vol_progress);
		seekBar_vol.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
//				seekBarChange(seekBar);
				startTimer();
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				handler.removeCallbacks(showController);
				startTimer();
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
//				seekBarChange(seekBar);
				startTimer();
				
				volume = seekBar.getProgress();
				System.out.println("seekBarChange!!" + volume);
				if(audioManager != null) {
					muteVolume(false);
					audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_PLAY_SOUND);
//					vol_progress = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
					
				}
			}
			/**
			 * 触摸音量进度条
			 * @param seekBar
			 */
			private void seekBarChange(SeekBar volumebar) {
				startTimer();
				volume = volumebar.getProgress();
				System.out.println("seekBarChange!!" + volume);
				if(audioManager != null) {
					muteVolume(false);
//					audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol_progress, AudioManager.FLAG_PLAY_SOUND);
//					vol_progress = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
					
				}
			}
		});
		
		text_cur = (TextView) findViewById(R.movie.text_current);
		text_all = (TextView) findViewById(R.movie.text_all);
	}
	 
	OnClickListener buttonClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (mediaPlayer!=null) {
				if(audioManager == null) audioManager = (AudioManager) VideoPlayerMP4.this.getSystemService(AUDIO_SERVICE);
				cur_time = mediaPlayer.getCurrentPosition();
				all_time = mediaPlayer.getDuration();
				switch (v.getId()) {
				case R.movie.prev:
					startTimer();
					if (cur_time > 3000)
						mediaPlayer.seekTo(cur_time - 3000);
					else
						mediaPlayer.seekTo(0);
					break;
				case R.movie.play_pause:
					startTimer();
					if(mediaPlayer.isPlaying()) mediaPlayer.pause();
					else mediaPlayer.start();
					break;
				case R.movie.ff:
					startTimer();
					if (cur_time + 5000 < all_time) mediaPlayer.seekTo(cur_time + 5000);
					else mediaPlayer.seekTo(all_time - 100);
					break;
				case R.movie.volume:
					startTimer();
					if(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) > 0) muteVolume(true);
					else if (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) == 0) muteVolume(false);
					break;
				case R.movie.lyt_bottom:
					startTimer();
					System.out.println("lyt_bottom");
					break;
				}
			}
		}

		
	};
	
	/**
	 * 静音
	 */
	private void muteVolume(boolean f) {
		if(f){ 
			volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			vol_progress = volume;
//			seekBar_vol.setProgress(0);
			button_vol.setBackgroundResource(R.drawable.phone_480_sound_mute);
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_PLAY_SOUND);
		}else{
//			seekBar_vol.setProgress(vol_progress);
			button_vol.setBackgroundResource(R.drawable.phone_480_sound_on);
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol_progress, AudioManager.FLAG_PLAY_SOUND);
		}
		System.out.println("vol_progress::" + volume);
//		audioManager.setStreamMute(AudioManager.STREAM_MUSIC, f);
	}
	
	/**
	 * 显示播放时间
	 */
	public void setTimeView() {
		if(mediaPlayer.isPlaying()){
			String curTimeShow = this.milliseconds2time(mediaPlayer.getCurrentPosition());
			String allTimeShow = this.milliseconds2time(mediaPlayer.getDuration());
			text_cur.setText(curTimeShow);
			text_all.setText(allTimeShow);
		}
	}
	
	public String milliseconds2time(int milliseconds){
		String zeroSecond = "0";
		String zeroMinute = "0";
		int minuts  = (int)(milliseconds/1000)/60;
		int seconds = (int)(milliseconds/1000 - minuts*60);
		if(seconds >= 10) zeroSecond = "";
		if(minuts >= 10) zeroMinute = "";
		return zeroMinute + minuts + ":" + zeroSecond + seconds;
	 }
	 
	/**
	 * 设置player资源地址
	 * @param path
	 */
	private void setPlayerPath(String path) {
		if(mediaPlayer == null) mediaPlayer = new MyMediaPlayer();
		mediaPlayer.reset(); 
		try {
			
//			mediaPlayer.setDataSource(getBaseContext(), Uri.parse(path));
			mediaPlayer.setDataSource(path);
//			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.prepareAsync();
		}catch(IllegalStateException e1){
			Log.e(TAG, e1.toString());
		}catch (IOException e) {
			Log.e(TAG, e.toString());
		}
	}  
	
	@Override
	protected void onDestroy() {
		finishPLayer();
		super.onDestroy();
	}
	
	private void finishPLayer(){
		handler.removeCallbacks(showController);
		if(mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
		}
		mediaPlayer = null;
	}
	
    private void attachMediaController() {
    	mMediaController = new MediaController(this);
   
//        mMediaController.setMediaPlayer(mMediaPlayerControl);
        mMediaController.setAnchorView(surfaceView);
//        surfaceView.setMediaController(mMediaController);
        mMediaController.setMediaPlayer(new MediaPlayerControl() {
			
			@Override
			public void start() {
				// TODO Auto-generated method stub
				mediaPlayer.start();
			}
			
			@Override
			public void seekTo(int pos) {
				// TODO Auto-generated method stub
				mediaPlayer.seekTo(pos);
			}
			
			@Override
			public void pause() {
				// TODO Auto-generated method stub
				mediaPlayer.pause();
			}
			
			@Override
			public boolean isPlaying() {
				// TODO Auto-generated method stub
				return mediaPlayer.isPlaying();
			}
			
			@Override
			public int getDuration() {
				// TODO Auto-generated method stub
				return mediaPlayer.getDuration();
			}
			
			@Override
			public int getCurrentPosition() {
				// TODO Auto-generated method stub
				return mediaPlayer.getCurrentPosition();
			}
			
			@Override
			public int getBufferPercentage() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public boolean canSeekForward() {
				// TODO Auto-generated method stub
				return true;
			}
			
			@Override
			public boolean canSeekBackward() {
				// TODO Auto-generated method stub
				return true;
			}
			
			@Override
			public boolean canPause() {
				// TODO Auto-generated method stub
				return true;
			}
		});
        mMediaController.setEnabled(true);
        System.out.println("mMediaController::" + mMediaController);
        
    }
	
	
    
    
    
	@Override
	public boolean onTouchEvent(MotionEvent event) {


		if (event.getAction() == MotionEvent.ACTION_UP) {

			if (lyt_controller.getVisibility() == View.VISIBLE) {
				lyt_controller.setVisibility(View.GONE);

			} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
				lyt_controller.setVisibility(View.GONE);
			} else {
				lyt_controller.setVisibility(View.VISIBLE);
				
				startTimer();
				System.out.println("VISIBLE");
				// cancelTopAndBottom();
			}
		}

		return true;
	
		
		//
//		if(!mMediaController.isShowing()) {
//			mMediaController.show(3000);
//		}
		//costume
		
//		if(flg_show ){
//			lyt_controller.setVisibility(View.INVISIBLE);
//			flg_show = false;
//			System.out.println("flg_show");
//		}else
//		
//		
//		if(lyt_controller.getVisibility() != View.VISIBLE){
//			lyt_controller.setVisibility(View.VISIBLE);
//			handler.removeCallbacks(showController);
//			startTimer();
//			flg_show = true;
//		}
//		else if(flg_show && lyt_controller.getVisibility() == View.VISIBLE) {
//			lyt_controller.setVisibility(View.INVISIBLE);
//			flg_show = false;
//		}
//		return true;
	}

	private void startTimer() {
		handler.removeCallbacks(showController);
		handler.postDelayed(showController, 4000);
//		timer = new Timer();
//		timerTask = new TimerTask() {
//			
//			@Override
//			public void run() {
//				handler.sendEmptyMessage(TIMER);
//			}
//		}; 
//		timer.schedule(timerTask, 4000);
	}
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_UP:
			flashVolSeek(1);
			break;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			flashVolSeek(-1);
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void flashVolSeek(int vol) {
		if(lyt_controller.getVisibility() != View.VISIBLE) {
			lyt_controller.setVisibility(View.VISIBLE);
		}
		vol_progress = vol_progress + vol;
		muteVolume(false);
		volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		seekBar_vol.setProgress(volume);
		System.out.println("flashVolSeek::" + volume);
		startTimer();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	class MyMediaPlayer extends MediaPlayer {
		
		int pos_last; //上一次播放点
		boolean is_last = true; //第一次刷新
		private boolean isBufferReady = true; //缓冲区是否准备好
		
		
		@Override
		public void release() {
			fresh_handler.removeCallbacks(FlashUI_Thread);
			super.release();
		}

		@Override
		public void start() throws IllegalStateException {
			button_play.setBackgroundResource(R.drawable.select_movie_playing_control_btn_pause);
			fresh_handler.post(FlashUI_Thread);
			System.out.println("MyMediaPlayer start");
			super.start();
		}

		@Override
		public void pause() throws IllegalStateException {
			button_play.setBackgroundResource(R.drawable.select_movie_playing_control_btn_play);
			System.out.println("MyMediaPlayer pause");
			super.pause();
		}
		
		
		/*
		 * 更新进度条，时间显示
		 */
		Handler fresh_handler = new Handler();
		Runnable FlashUI_Thread = new Runnable(){

			@Override
			public void run() {
				newPlayer();
				flashView(); // 更新进度条，时间显示
				
				isPlayerForward(); //是否在播放
				if(!isBufferReady ){
					lyt_buffer.setVisibility(View.VISIBLE);
				}else{
					lyt_buffer.setVisibility(View.GONE);
				}
				
				
				fresh_handler.postDelayed(FlashUI_Thread, 1000);
			}
			private void flashView() {
				int pos = mediaPlayer.getCurrentPosition();
				int musicMax = mediaPlayer.getDuration();
				int barMax = seekBar.getMax();
				if(musicMax != 0) seekBar.setProgress(barMax * pos / musicMax);
				setTimeView();
			}
		};
		

		private void isPlayerForward() {
			if(pos_last != mediaPlayer.getCurrentPosition() && !is_last) {
				if(!isBufferReady) isBufferReady = true;
			}
			if(pos_last == mediaPlayer.getCurrentPosition() && !is_last && mediaPlayer.isPlaying()) {
				if(isBufferReady) isBufferReady = false;
			}
			pos_last = mediaPlayer.getCurrentPosition();
			
			if(is_last) is_last =false;
		}
	}
	
	
}