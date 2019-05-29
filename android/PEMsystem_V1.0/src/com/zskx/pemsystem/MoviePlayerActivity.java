package com.zskx.pemsystem;


import com.zskx.net.response.VideoDetailEntity;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * 视频播放
 *
 *
 */
public class MoviePlayerActivity extends Activity implements SurfaceHolder.Callback{
	
	
	private static final int SECOND=1000;
	
	private SurfaceView surfaceView;//显示视频图像
	private SurfaceHolder mSurfaceHolder;
	
	private LinearLayout topLay,bottomLay;
	private boolean isShowController=false;//控制界面是否正在显示
	
	private String durationString="00:00";
	private String currentPositionString="00:00";
	
	private TextView currentPositionText, durationText;//当前播放时间，影片时长
	private int mDuration;
	private int mCurrentPosition;
	
	private SeekBar movieProgreesBar;//影片播放进度条
	//private SeekBar vioceControllBar;//音量大小调节器

	
	private MediaPlayer mediaPlayer;
	private String movieURL="/sdcard/movie003.mp4";
	
	private AudioManager mAudioManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.movie_player);
		
		initComponent();
		
		init();
		//获得视频网络地址
		Intent  intent=getIntent();
		VideoDetailEntity videoDetailEntity=(VideoDetailEntity) intent.getSerializableExtra(MovieItemsActivity.VIDEOENTITYDATAIL_KEY);
		String url=videoDetailEntity.getVideoUrl();
		if((url!=null)&&(!"".equals(url)))
		{
			movieURL=url;
			
		}
		
		setMediaPlayer();
		
	}
	
	
	
	/**
	 * 初始化组件
	 */
	private void initComponent()
	{
		surfaceView=(SurfaceView)findViewById(R.id.surfaceView);
		mSurfaceHolder=surfaceView.getHolder();
	
		topLay=(LinearLayout)findViewById(R.id.top);
		bottomLay=(LinearLayout)findViewById(R.id.bottom);
		
		currentPositionText=(TextView)findViewById(R.id.courrentPosition);
		durationText=(TextView)findViewById(R.id.duration);
		
		movieProgreesBar=(SeekBar)findViewById(R.id.movieProgress_seekbar);
	//	vioceControllBar=(SeekBar)findViewById(R.id.vioce_seekbar);
		
		mediaPlayer=new MediaPlayer();
		
		mAudioManager=(AudioManager) getSystemService(AUDIO_SERVICE);//调节系统音量
		
		
	
	}
	
	/**
	 * 设置媒体播放器
	 */
	private void setMediaPlayer()
	{
		System.out.println("wqp:-------->setMediaPlayer()");
		mSurfaceHolder.addCallback(this);
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setLooping(false);
		try {
			//mediaPlayer.setDataSource("/sdcard/a.flv");
			//mediaPlayer.setDataSource("http://192.168.1.144:5080/interpose/anime/file/20/00/01/06/200001061/intervention_address_200001061.flv");
			//System.out.println("movieURL:----->"+movieURL);
			mediaPlayer.setDataSource("http://daily3gp.com/vids/family_guy_penis_car.3gp");
					//"http://pl.pems.cn/interpose/anime/file/20/00/00/33/200000333/intervention_address_200000333.flv");
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		} 
	}

	
	private int maxVolume=0;
	
	//初始化数据或事件
	private void init()
	{
		//mSurfaceHolder.addCallback(this);
		//mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mediaPlayer.setScreenOnWhilePlaying(true);
 
		movieProgreesBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				mediaPlayer.seekTo(seekBar.getProgress());
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				
			}
		});
		
		maxVolume=mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		//System.out.println("maxVolume:----->"+maxVolume);
//		vioceControllBar.setMax(maxVolume);
//		vioceControllBar.setProgress(7);
//		vioceControllBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
//			
//			@Override
//			public void onStopTrackingTouch(SeekBar seekBar) {
//				int volume=seekBar.getProgress();
//				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_PLAY_SOUND);
//				
//			}
//			
//			@Override
//			public void onStartTrackingTouch(SeekBar seekBar) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onProgressChanged(SeekBar seekBar, int progress,
//					boolean fromUser) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
	
	    surfaceView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_UP)
				{
					if(topLay.getVisibility()==View.VISIBLE)
					{
						topLay.setVisibility(View.GONE);
						bottomLay.setVisibility(View.GONE);
					}else if(topLay.getVisibility()==View.GONE)
					{
						topLay.setVisibility(View.VISIBLE);
						bottomLay.setVisibility(View.VISIBLE);
					}
				}
				
				
				return true;
			}
		});
	
	
	}


	/**
	 * 更新进度条
	 */
	private Handler progressHandler=new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			int temp=msg.arg1;
			currentPositionString=temp/(60*SECOND)+":"+(temp%(60*SECOND))/SECOND;
		    currentPositionText.setText(currentPositionString);
		    movieProgreesBar.setProgress(temp);
			
		};
	};
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
		System.out.println("wqp:----->"+"surfaceCreated");
		//mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mediaPlayer.setDisplay(mSurfaceHolder);
		try {
			mediaPlayer.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mediaPlayer.start();
		
		//设置进度内容
		mCurrentPosition=mediaPlayer.getCurrentPosition();
		mDuration=mediaPlayer.getDuration();
		
	//	SimpleDateFormat sdf=new SimpleDateFormat("hh:mm");
		//System.out.println("duration:--->"+sdf.format(new Date(mDuration)));
		
		movieProgreesBar.setMax(mDuration);
	    movieProgreesBar.setProgress(mCurrentPosition);
	    
	    durationString=mDuration/(60*SECOND)+":"+(mDuration%(60*SECOND))/SECOND;
	    durationText.setText(durationString);
	 
	    ProgeressThread ptd=new ProgeressThread();
	    ptd.setDaemon(true);
	    ptd.start();
	
		
		
		
	}



	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}



	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		System.out.println("wqp:----->"+"surfaceDestroyed");
		mediaPlayer.release();
		mediaPlayer=null;
		
	}
	
	class ProgeressThread extends Thread
	{

		@Override
		public void run() {
			
			int currPosition;
			while((mediaPlayer!=null)&&((currPosition=mediaPlayer.getCurrentPosition())<=mDuration))
			{
				
			    Message message=Message.obtain(progressHandler);
				message.arg1=currPosition;
				message.sendToTarget();
			
				try {
				 sleep(SECOND);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
		
	}

	
	
}
