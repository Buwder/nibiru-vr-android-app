package com.zskx.pemsystem;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

public class VideoPalyerActivity extends Activity {

	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private LinearLayout topLay;
	private LinearLayout bottomLay;
	private SeekBar videoSeekBar;
	private SeekBar volumeSeekBar;
	private TextView textCurrTime;
	private TextView textDuration;
    private ProgressBar movieLoadingBar;
	private MediaPlayer videoPlayer;

	private AudioManager mAudioManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.movie_player);
		initComponent();
		initDataOrEvent();
		setVideoPlayer();
	}

	private void initComponent() {

		mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		topLay = (LinearLayout) findViewById(R.id.top);
		bottomLay = (LinearLayout) findViewById(R.id.bottom);
		videoSeekBar = (SeekBar) findViewById(R.id.movieProgress_seekbar);
		volumeSeekBar = (SeekBar) findViewById(R.id.vioce_seekbar);
		textCurrTime = (TextView) findViewById(R.id.courrentPosition);
		textDuration = (TextView) findViewById(R.id.duration);
		movieLoadingBar=(ProgressBar)findViewById(R.id.movie_loading_bar);
	}

	private void initDataOrEvent() {

		videoPlayer = new MediaPlayer();

		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(callback);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mSurfaceView.setOnTouchListener(touchListener);
		videoSeekBar.setOnSeekBarChangeListener(videoSeekBaristener);
		volumeSeekBar.setOnSeekBarChangeListener(volumSeekBarListener);

		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		int volumeMax = mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		int volumeCurr = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		volumeSeekBar.setMax(volumeMax);
		volumeSeekBar.setProgress(volumeCurr);

		videoPlayer.setOnPreparedListener(preparedListener);
		videoPlayer.setOnBufferingUpdateListener(bufferingUpdateListener);
		videoPlayer.setOnCompletionListener(completionListener);

	}

	private void setVideoPlayer() {
		try {
			System.out.println("setVideoPlayer!start");
			// videoPlayer.reset();
			videoPlayer.setLooping(false);
			videoPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			videoPlayer.setScreenOnWhilePlaying(true);
			videoPlayer
					.setDataSource(
							this,
							Uri.parse("http://daily3gp.com/vids/family_guy_penis_car.3gp"));
			// videoPlayer.setDataSource("http://192.168.1.144:5080/interpose/anime/file/20/00/01/06/200001061/intervention_address_200001061.flv");
			// videoPlayer.setDataSource("/sdcard/a.flv");
			videoPlayer.prepareAsync();

			System.out.println("setVideoPlayer!end");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {

			isPlaying = false;
			if (!isPlaying) {
				videoPlayer.release();
				videoPlayer = null;
			}

			System.out.println("holder:---->surfaceDestroyed");
			System.out.println("surfaceDestroyed:--->isPlaying:--->"
					+ isPlaying);
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			System.out.println("holder:---->surfaceCreated");
			videoPlayer.setDisplay(holder);

			// setVideoPlayer();

		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			System.out.println("holder:---->surfaceChanged");

		}
	};

	private SurfaceView.OnTouchListener touchListener = new SurfaceView.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			if (event.getAction() == MotionEvent.ACTION_UP) {

				if (topLay.getVisibility() == View.VISIBLE) {
					topLay.setVisibility(View.GONE);
					bottomLay.setVisibility(View.GONE);

				} else {
					topLay.setVisibility(View.VISIBLE);
					bottomLay.setVisibility(View.VISIBLE);
					// cancelTopAndBottom();
				}
			}

			return true;
		}
	};

	private SeekBar.OnSeekBarChangeListener videoSeekBaristener = new SeekBar.OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {

			videoPlayer.seekTo(seekBar.getProgress());
			System.out
					.println("videoSeekBaristener:------>seekBar.getProgress()------->"
							+ seekBar.getProgress());

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {

		}
	};

	private SeekBar.OnSeekBarChangeListener volumSeekBarListener = new SeekBar.OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {

			int volume = seekBar.getProgress();
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume,
					AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {

		}
	};

	private int durationTime = 0;
	private int currentTime = 0;
	private boolean isPlaying;
	
	/**
	 *  转化时间为字符串
	 * @param time
	 * @return String
	 */
	private String convertTime(int time)
	{
		int minutes=time/(60*1000);
		String minutesStr="";
		if(minutes<10)
		{
			minutesStr=""+"0"+minutes+":";
		}else
		{
			minutesStr=minutes+":";
		}
		
		int seconds=(time%(60*1000))/1000;
		String secondsStr="";
		if(seconds<10)
		{
			secondsStr=""+"0"+seconds;
		}else
		{
			secondsStr=""+seconds;
		}
		
		return minutesStr+secondsStr;
	}
	

	private MediaPlayer.OnPreparedListener preparedListener = new MediaPlayer.OnPreparedListener() {

		@Override
		public void onPrepared(MediaPlayer mp) {
			
			movieLoadingBar.setVisibility(View.VISIBLE);
			mp.start();
			movieLoadingBar.setVisibility(View.GONE);
			isPlaying = true;
			durationTime = mp.getDuration();
			videoSeekBar.setMax(100);
			videoSeekBar.setProgress(currentTime);
			textCurrTime.setText(convertTime(currentTime));
			textDuration.setText(convertTime(durationTime));

			CurrentPositionThread thread = new CurrentPositionThread();
			thread.start();
		}
	};

	private MediaPlayer.OnBufferingUpdateListener bufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {

		@Override
		public void onBufferingUpdate(MediaPlayer mp, int percent) {

			videoSeekBar.setSecondaryProgress(percent);
			System.out.println("mp.getDuration:---->" + mp.getDuration());
			// videoSeekBar.setSecondaryProgress(percent * mp.getDuration() /
			// 100);
			// System.out.println("bufferingUpdateListener:--->percent:--->"+percent);
			// System.err.println("surplus:---->"+(100-percent)*durationTime/60/100);

		}
	};

	private MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {

		@Override
		public void onCompletion(MediaPlayer mp) {

			 isPlaying = false;
			System.out.println("onCompletion:--->isPlaying:--->" + isPlaying);

		}
	};

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg.arg1 == 1) {
				if (videoPlayer != null) {
					float rataf = ((float) videoPlayer.getCurrentPosition())
							/ videoPlayer.getDuration();
					int rata = (int) (rataf * 100);
					videoSeekBar.setProgress(rata);
					
					int time=videoPlayer.getCurrentPosition();
					textCurrTime.setText(convertTime(time));
				}

			} else if (msg.arg1 == 2) {
				topLay.setVisibility(View.GONE);
				bottomLay.setVisibility(View.GONE);
			}
		};
	};

	private boolean isShowing = true;
	private TimerTask timerTask = new TimerTask() {

		@Override
		public void run() {

			Message msg = new Message();
			msg.arg1 = 2;
			// handler.sendMessage(msg);
		}
	};
	private Timer timer = new Timer();

	private void cancelTopAndBottom() {
		Timer timer = new Timer();
		timer.schedule(timerTask, 5 * 1000);

	}

	class CurrentPositionThread extends Thread {
		@Override
		public void run() {

			while (isPlaying) {
				System.out.println("thread:--->isPlaying:--->" + isPlaying);

				try {
					sleep(50);

					Message msg = new Message();
					msg.arg1 = 1;
					handler.sendMessage(msg);

				} catch (InterruptedException e) {

					e.printStackTrace();
				}

			}
		}
	}

}
