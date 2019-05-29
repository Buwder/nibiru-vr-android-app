package com.zskx.pemsystem;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zskx.net.response.VideoDetailEntity;
import com.zskx.pemsystem.customview.VerticalSeekBar;
import com.zskx.pemsystem.util.ScreenManager;

public class VideoPalyerActivity extends BaseActivity {

	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private LinearLayout topLay;
	private LinearLayout bottomLay;
	private SeekBar videoSeekBar;
	private TextView textCurrTime;
	private TextView textDuration;
	private TextView textMovieTitle;
	private Button btnPrevMovie;
	private Button btnPrevNext;
	private Button btnVoiceControl;
	private ImageButton imgBtnPauseOrPlay;
	private ProgressBar movieLoadingBar;
	private MediaPlayer videoPlayer;

	// 音量部分
	private VerticalSeekBar volumeSeekBar;
	private TextView textVolume;
	private LinearLayout popContentView;
	private PopupWindow popupWindow;

	private AudioManager mAudioManager;

	private final static int VIDEO_STATUS_FINISHED = 0x200;
	private final static int VIDEO_STATUS_PLAYING = 0x201;
	private final static int VIDEO_STATUS_PAUSED = 0x202;
	private final static int VIDEO_STATUS_PREPARING = 0x203;
	private final static int VIDEO_STATUS_PREPARED = 0x204;
	private final static int VIDEO_STATUS_RELEASED = 0x205;

	private final static int VIDEO_STATUS_DEFAULT = -0x201;

	private final static int SURFACE_STATUS_DESTORY = -0x109;
	private final static int SURFACE_STATUS_CREATED = -0x108;
	private final static int SURFACE_STATUS_DEFAULT = -0x107;

	private int videoStatus = VIDEO_STATUS_DEFAULT;
	private int surfaceStatus = SURFACE_STATUS_DEFAULT;
	
	private VideoDetailEntity videoDetailEntity;
	
	private String video_url="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ScreenManager.getScreenManager().pushActivity(this);
		setContentView(R.layout.movie_player);
		initComponent();
		initDataOrEvent();

	}

	@Override
	protected void onResume() {
		super.onResume();
		/** * 设置为横屏 */
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		
		
		initSurfaceView();
		videoPlayer = getVideoPlayer();
	}

	@Override
	public void finish() {
		// 解决 View.WindowLeaked
		if(popupWindow.isShowing())
		{
			popupWindow.dismiss();
		}
		super.finish();
	}
	
	@Override
	protected void onDestroy() {
		if (videoPlayer != null) {
			videoPlayer.stop();
			videoPlayer.release();
			videoPlayer = null;
			videoStatus = VIDEO_STATUS_RELEASED;
		}
		super.onDestroy();
	}

	private void initComponent() {

		mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		topLay = (LinearLayout) findViewById(R.id.top);
		bottomLay = (LinearLayout) findViewById(R.id.bottom);
		videoSeekBar = (SeekBar) findViewById(R.id.movieProgress_seekbar);
		textCurrTime = (TextView) findViewById(R.id.courrentPosition);
		textDuration = (TextView) findViewById(R.id.duration);
		textMovieTitle = (TextView) findViewById(R.id.text_movie_title);
		btnPrevMovie = (Button) findViewById(R.id.prevMovie);
		btnPrevNext = (Button) findViewById(R.id.nextMovie);
		btnVoiceControl = (Button) findViewById(R.id.voiceControl);
		imgBtnPauseOrPlay = (ImageButton) findViewById(R.id.pauseOrPlay);
		movieLoadingBar = (ProgressBar) findViewById(R.id.movie_loading_bar);

		// 音量部分
		popContentView = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.pop_content, null);
		textVolume = (TextView) popContentView.findViewById(R.id.text_volume);
		volumeSeekBar = (VerticalSeekBar) popContentView
				.findViewById(R.id.volumeSeekBar);
		popupWindow = new PopupWindow(this);

	}

	/**
	 * 
	 * @return videoPlayer
	 */
	private MediaPlayer getVideoPlayer() {

		if (videoPlayer == null) {
			videoPlayer = new MediaPlayer();
		}
		return videoPlayer;
	}

	/**
	 * 初始化surfaceView
	 */
	private void initSurfaceView() {
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(callback);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mSurfaceView.setOnTouchListener(touchListener);
	}

	private void initDataOrEvent() {

		Intent intent=getIntent();
		
		videoDetailEntity=(VideoDetailEntity) intent.getSerializableExtra(MovieItemsActivity.VIDEOENTITYDATAIL_KEY);
		
		video_url=videoDetailEntity.getVideoUrl();
		
		videoSeekBar.setOnSeekBarChangeListener(videoSeekBaristener);

		volumeSeekBar.setOnSeekBarChangeListener(volumSeekBarListener);

		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		int volumeMax = mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		int volumeCurr = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);

		textMovieTitle.setText(videoDetailEntity.getVideoTitle());
		textMovieTitle.setEllipsize(TruncateAt.END);
		textVolume.setText(volumeCurr + "");

		volumeSeekBar.setMax(volumeMax);
		volumeSeekBar.setProgress(volumeCurr);

		popupWindow.setWidth(50);
		popupWindow.setHeight(292);
		
		Drawable popBackDrawable=getResources().getDrawable(R.drawable.movie_playing_control_voice_panel_bg);
		popupWindow.setBackgroundDrawable(popBackDrawable);
		popupWindow.setContentView(popContentView);
		popupWindow.setAnimationStyle(0);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);

		btnPrevMovie.setOnClickListener(btnOncClickListener);
		btnPrevNext.setOnClickListener(btnOncClickListener);
		btnVoiceControl.setOnClickListener(btnOncClickListener);
		imgBtnPauseOrPlay.setOnClickListener(btnOncClickListener);

	}

	/**
	 * button 单击事件
	 */
	private View.OnClickListener btnOncClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.prevMovie:
				if (videoStatus == VIDEO_STATUS_PAUSED
						|| videoStatus == VIDEO_STATUS_FINISHED
						|| videoStatus == VIDEO_STATUS_PLAYING) {
					int barCurrent = videoSeekBar.getProgress();
					barCurrent -= 5;
					if (barCurrent >= 0) {
						videoSeekBar.setProgress(barCurrent);
						videoPlayer.seekTo((barCurrent) * durationTime / 100);

					} else {
						videoSeekBar.setProgress(0);
						videoPlayer.seekTo(0);
					}
				}

				break;
			case R.id.nextMovie:
				if (videoStatus == VIDEO_STATUS_PAUSED
						|| videoStatus == VIDEO_STATUS_FINISHED
						|| videoStatus == VIDEO_STATUS_PLAYING) {
					int barCurrent = videoSeekBar.getProgress();
					int barMax = videoSeekBar.getMax();
					barCurrent += 5;
					if (barCurrent <= barMax) {
						videoSeekBar.setProgress(barCurrent);
						videoPlayer.seekTo((barCurrent) * durationTime / 100);

					} else {
						videoSeekBar.setProgress(barMax);
						videoPlayer.seekTo((barMax) * durationTime / 100);
					}
				}
				break;
        case R.id.voiceControl:
	
        	  if(popupWindow.isShowing())
        	  {
        		  popupWindow.dismiss();
        	  }else
        	  {
        		  popupWindow.update();
        		  //popupWindow.showAtLocation(btnVoiceControl, Gravity.RIGHT, 0, 15);
        		  popupWindow.showAsDropDown(btnVoiceControl, (popupWindow.getWidth()-btnVoiceControl.getWidth())/2, 15);
        	
        	  }
            	break;
        case R.id.pauseOrPlay:
        	if(videoStatus==VIDEO_STATUS_PAUSED ||videoStatus==VIDEO_STATUS_FINISHED||videoStatus==VIDEO_STATUS_PREPARED)
        	{
        		startVideoPlayer();
        	}else if(videoStatus==VIDEO_STATUS_PLAYING)
        	{
        		pauseVideoPlayer();
        	}
	
	    break;

			default:
				break;
			}

		}
	};

	/**
	 * 暂停播放器
	 */
	private void pauseVideoPlayer() {

		Drawable drawable = getResources().getDrawable(
				R.drawable.select_movie_playing_control_btn_play);
		imgBtnPauseOrPlay.setImageDrawable(drawable);
		videoPlayer.pause();
		videoStatus = VIDEO_STATUS_PAUSED;
	}

	/**
	 * 打开播放器，进行播放
	 */
	private void startVideoPlayer() {
		Drawable drawable = getResources().getDrawable(
				R.drawable.select_movie_playing_control_btn_pause);
		imgBtnPauseOrPlay.setImageDrawable(drawable);
		videoPlayer.start();
		videoStatus = VIDEO_STATUS_PLAYING;
		movieLoadingBar.setVisibility(View.GONE);
	}

	
	/**
	 * 播放视频准备
	 */
	private void setVideoPlayer() {
		try {

			videoPlayer.reset();

			videoPlayer.setOnPreparedListener(preparedListener);
			videoPlayer.setOnBufferingUpdateListener(bufferingUpdateListener);
			videoPlayer.setOnCompletionListener(completionListener);
			videoPlayer.setLooping(false);
			videoPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			videoPlayer.setScreenOnWhilePlaying(true);
//			videoPlayer
//					.setDataSource(
//							this,
//							Uri.parse("http://daily3gp.com/vids/family_guy_penis_car.3gp"));
			// videoPlayer.setDataSource("http://192.168.1.144:5080/interpose/anime/file/20/00/01/06/200001061/intervention_address_200001061.flv");
			
			System.out.println("video_url:--------------->"+video_url);
			videoPlayer.setDataSource(video_url);
			// videoPlayer.prepareAsync();

			// System.out.println("setVideoPlayer!end");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {

			if (videoStatus == VIDEO_STATUS_FINISHED) {
				videoPlayer.stop();
				videoPlayer.release();
				videoPlayer = null;
				videoStatus = VIDEO_STATUS_RELEASED;
				videoSeekBar.setProgress(0);
				videoSeekBar.setSecondaryProgress(0);
				System.out.println("videostatus:---->VIDEO_STATUS_FINISHED");
			} else if (videoStatus == VIDEO_STATUS_PLAYING) {
				pauseVideoPlayer();
				System.out.println("videostatus:---->VIDEO_STATUS_PLAYING");

			} else if (videoStatus == VIDEO_STATUS_PREPARING) {

				videoStatus = VIDEO_STATUS_PREPARING;

			}
			surfaceStatus = SURFACE_STATUS_DESTORY;
			System.out.println("surface:--->surfaceDestroyed");

		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {

			videoPlayer.setDisplay(holder);

			Message msg = new Message();
			msg.obj = holder;
			handlerPlaying.sendMessage(msg);

			surfaceStatus = SURFACE_STATUS_CREATED;
			System.out.println("surface_status:---------->surface_created");

		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {

		}
	};

	private SurfaceView.OnTouchListener touchListener = new SurfaceView.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			if (event.getAction() == MotionEvent.ACTION_UP) {

				if (topLay.getVisibility() == View.VISIBLE) {
					topLay.setVisibility(View.GONE);
					bottomLay.setVisibility(View.GONE);

				} else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
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

			if (videoStatus == VIDEO_STATUS_PREPARING) {
				seekBar.setProgress(currentTime);

			} else if (videoStatus == VIDEO_STATUS_PLAYING
					|| (videoStatus == VIDEO_STATUS_PAUSED)) {
				int precent = seekBar.getProgress();
				videoPlayer.seekTo(precent * durationTime / 100);

			}

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {

			seekBar.setProgress(progress);
		}
	};

	private VerticalSeekBar.OnSeekBarChangeListener volumSeekBarListener = new VerticalSeekBar.OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(VerticalSeekBar verticalSeekBar,
				int progress, boolean fromUser) {

			verticalSeekBar.setProgress(progress);
			textVolume.setText(verticalSeekBar.getProgress() + "");
		}

		@Override
		public void onStartTrackingTouch(VerticalSeekBar verticalSeekBar) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStopTrackingTouch(VerticalSeekBar verticalSeekBar) {
			int volume = verticalSeekBar.getProgress();
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume,
					AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

		}
	};

	private int durationTime = 0;
	private int currentTime = 0;

	// private boolean isPlaying;
	// private boolean isPlayFinish=false;

	/**
	 * 转化时间为字符串
	 * 
	 * @param time
	 * @return String
	 */
	private String convertTime(int time) {
		int minutes = time / (60 * 1000);
		String minutesStr = "";
		if (minutes < 10) {
			minutesStr = "" + "0" + minutes + ":";
		} else {
			minutesStr = minutes + ":";
		}

		int seconds = (time % (60 * 1000)) / 1000;
		String secondsStr = "";
		if (seconds < 10) {
			secondsStr = "" + "0" + seconds;
		} else {
			secondsStr = "" + seconds;
		}

		return minutesStr + secondsStr;
	}

	private MediaPlayer.OnPreparedListener preparedListener = new MediaPlayer.OnPreparedListener() {

		@Override
		public void onPrepared(MediaPlayer mp) {

			movieLoadingBar.setVisibility(View.VISIBLE);

			final Message msg = new Message();
			videoStatus = VIDEO_STATUS_PREPARED;

			msg.arg1 = videoStatus;
			msg.arg2 = surfaceStatus;
			handlerPlaying.sendMessage(msg);

			durationTime = mp.getDuration();
			videoSeekBar.setMax(100);
			videoSeekBar.setProgress(currentTime);
			textCurrTime.setText(convertTime(currentTime));
			textDuration.setText(convertTime(durationTime));
			movieLoadingBar.setVisibility(View.GONE);

			CurrentPositionThread thread = new CurrentPositionThread();
			thread.start();
		}
	};

	private MediaPlayer.OnBufferingUpdateListener bufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {

		@Override
		public void onBufferingUpdate(MediaPlayer mp, int percent) {

			videoSeekBar.setSecondaryProgress(percent);

			System.out.println("bufferingUpdateListener:--->percent:--->"
					+ percent);

		}
	};

	private MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {

		@Override
		public void onCompletion(MediaPlayer mp) {

			videoPlayer.seekTo(0);
			videoSeekBar.setProgress(0);
			videoStatus = VIDEO_STATUS_FINISHED;
			Drawable drawable = getResources().getDrawable(
					R.drawable.select_movie_playing_control_btn_play);
			imgBtnPauseOrPlay.setImageDrawable(drawable);

		}
	};

	private final Handler handlerPlaying = new Handler() {
		public void handleMessage(Message msg) {

			mSurfaceHolder = (SurfaceHolder) msg.obj;
			if (videoStatus == VIDEO_STATUS_PAUSED) {
				System.out.println("111111111111111111111111111");

				videoPlayer.setDisplay(mSurfaceHolder);
				startVideoPlayer();
				movieLoadingBar.setVisibility(View.GONE);

			} else if (videoStatus == VIDEO_STATUS_RELEASED
					|| videoStatus == VIDEO_STATUS_DEFAULT) {
				System.out.println("12222222222222222222222222222222");

				setVideoPlayer();

				videoPlayer.prepareAsync();
				movieLoadingBar.setVisibility(View.VISIBLE);

				videoStatus = VIDEO_STATUS_PREPARING;

				surfaceStatus = SURFACE_STATUS_CREATED;
			}
			if (videoStatus == VIDEO_STATUS_PREPARING) {

			} else if (msg.arg1 == VIDEO_STATUS_PREPARED
					&& surfaceStatus == SURFACE_STATUS_CREATED) {

				videoPlayer.setDisplay(mSurfaceHolder);
				startVideoPlayer();

				videoStatus = VIDEO_STATUS_PLAYING;
				movieLoadingBar.setVisibility(View.GONE);
			}

		};
	};

	private final Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg.arg1 == 1) {
				if (videoPlayer != null) {
					float rataf = ((float) videoPlayer.getCurrentPosition())
							/ videoPlayer.getDuration();
					int rata = (int) (rataf * 100);
					videoSeekBar.setProgress(rata);

					int time = videoPlayer.getCurrentPosition();
					textCurrTime.setText(convertTime(time));
				}

			} else if (msg.arg1 == 2) {
				topLay.setVisibility(View.GONE);
				bottomLay.setVisibility(View.GONE);
			}
		};
	};

	// private boolean isShowing = true;
	// private TimerTask timerTask = new TimerTask() {
	//
	// @Override
	// public void run() {
	//
	// Message msg = new Message();
	// msg.arg1 = 2;
	// // handler.sendMessage(msg);
	// }
	// };
	// private Timer timer = new Timer();
	//
	// private void cancelTopAndBottom() {
	// Timer timer = new Timer();
	// timer.schedule(timerTask, 5 * 1000);
	//
	// }

	class CurrentPositionThread extends Thread {
		@Override
		public void run() {

			while (videoStatus != VIDEO_STATUS_RELEASED) {

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
