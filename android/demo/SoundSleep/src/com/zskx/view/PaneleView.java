package com.zskx.view;


import java.io.IOException;

import com.zskx.sleep.R;

import android.R.bool;
import android.content.Context;
import android.graphics.drawable.Drawable;

import android.media.MediaPlayer;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class PaneleView extends LinearLayout{
	
	private static final String LOCAT="PanelView";
	private static final int MAX_VOLUME=100;//最大音量值
	
	private boolean first=true;
	private boolean isAllPause=false;
	private SoundsModen soundsModen=null;//播放文件信息
	private ImageButton sound_play_1, sound_play_2, sound_play_3, sound_play_4, sound_play_5;//播放按钮
	private TextView sound_name_1,sound_name_2,sound_name_3,sound_name_4,sound_name_5;//sound文件名
	
	private TextView sound_voume_1,sound_voume_2,sound_voume_3,sound_voume_4,sound_voume_5;//显示音量大小
	private SeekBar seekBar_1,seekBar_2,seekBar_3,seekBar_4,seekBar_5;//改变音量大小
	
	
	
	private MediaPlayer mediaPlayer_1,mediaPlayer_2,mediaPlayer_3,mediaPlayer_4,mediaPlayer_5;//播放器
	
	private LinearLayout linearLayout=null;
	private LinearLayout sound_player_1_back,sound_player_2_back,sound_player_3_back,sound_player_4_back,sound_player_5_back;//小按钮背景
	
	private Drawable pause_bg,play_bg;
	
	private static final LayoutParams MATCH_MACTH=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
   	
	public PaneleView(Context context) {
		super(context);

	}

	public PaneleView(Context context, AttributeSet attrs) {
		super(context, attrs);
	    
		linearLayout=(LinearLayout) View.inflate(context, R.layout.panel, null);
		
		pause_bg=context.getResources().getDrawable(R.drawable.pause_btn);
		play_bg=context.getResources().getDrawable(R.drawable.play_btn);
		
    	initMediaPlayer();
		initComponent();
		
		initControllerListener();
		linearLayout.setLayoutParams(MATCH_MACTH);
		this.addView(linearLayout, MATCH_MACTH);
		
	}
	
	
	
	/**
	 * 从view中初始化组件
	 */
	private void initComponent()
	{
		sound_play_1=(ImageButton) linearLayout.findViewById(R.id.sound_player_1);
		sound_name_1=(TextView)linearLayout.findViewById(R.id.sound_name_1);
		sound_voume_1=(TextView)linearLayout.findViewById(R.id.sound_volume_1);
		seekBar_1=(SeekBar)linearLayout.findViewById(R.id.seekBar_1);
		seekBar_1.setMax(MAX_VOLUME);
		sound_player_1_back=(LinearLayout)linearLayout.findViewById(R.id.sound_player_1_back);
		System.out.println("sound_player_1_back:"+sound_player_1_back);
		
		
		sound_play_2=(ImageButton) linearLayout.findViewById(R.id.sound_player_2);
		sound_name_2=(TextView)linearLayout.findViewById(R.id.sound_name_2);
		sound_voume_2=(TextView)linearLayout.findViewById(R.id.sound_volume_2);
		seekBar_2=(SeekBar)linearLayout.findViewById(R.id.seekBar_2);
		seekBar_2.setMax(MAX_VOLUME);
		sound_player_2_back=(LinearLayout)linearLayout.findViewById(R.id.sound_player_2_back);
		
		sound_play_3=(ImageButton) linearLayout.findViewById(R.id.sound_player_3);
		sound_name_3=(TextView)linearLayout.findViewById(R.id.sound_name_3);
		sound_voume_3=(TextView)linearLayout.findViewById(R.id.sound_volume_3);
		seekBar_3=(SeekBar)linearLayout.findViewById(R.id.seekBar_3);
		seekBar_3.setMax(MAX_VOLUME);
		sound_player_3_back=(LinearLayout)linearLayout.findViewById(R.id.sound_player_3_back);
		
		sound_play_4=(ImageButton) linearLayout.findViewById(R.id.sound_player_4);
		sound_name_4=(TextView)linearLayout.findViewById(R.id.sound_name_4);
		sound_voume_4=(TextView)linearLayout.findViewById(R.id.sound_volume_4);
		seekBar_4=(SeekBar)linearLayout.findViewById(R.id.seekBar_4);
		seekBar_4.setMax(MAX_VOLUME);
		sound_player_4_back=(LinearLayout)linearLayout.findViewById(R.id.sound_player_4_back);
		
		sound_play_5=(ImageButton) linearLayout.findViewById(R.id.sound_player_5);
		sound_name_5=(TextView)linearLayout.findViewById(R.id.sound_name_5);
		sound_voume_5=(TextView)linearLayout.findViewById(R.id.sound_volume_5);
		seekBar_5=(SeekBar)linearLayout.findViewById(R.id.seekBar_5);
		seekBar_5.setMax(MAX_VOLUME);
		sound_player_5_back=(LinearLayout)linearLayout.findViewById(R.id.sound_player_5_back);
	}
    
	/*
	 * seekbar 拖动事件
	 */
	private SeekBar.OnSeekBarChangeListener seekBarListener=new SeekBar.OnSeekBarChangeListener() {
		
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			switch (seekBar.getId()) {
			case R.id.seekBar_1:
				
				mediaPlayer_1.setVolume(((float) progress)/MAX_VOLUME, ((float) progress)/MAX_VOLUME);
				sound_voume_1.setText("vol."+progress+"%");
				break;
				
			case R.id.seekBar_2:
				
				mediaPlayer_2.setVolume(((float) progress)/MAX_VOLUME, ((float) progress)/MAX_VOLUME);
				sound_voume_2.setText("vol."+progress+"%");
				break;
				
			case R.id.seekBar_3:
				
				mediaPlayer_3.setVolume(((float) progress)/MAX_VOLUME, ((float) progress)/MAX_VOLUME);
				sound_voume_3.setText("vol."+progress+"%");
				break;
			case R.id.seekBar_4:
				
				mediaPlayer_4.setVolume(((float) progress)/MAX_VOLUME, ((float) progress)/MAX_VOLUME);
				sound_voume_4.setText("vol."+progress+"%");
				break;
				
			case R.id.seekBar_5:
				
				mediaPlayer_5.setVolume(((float) progress)/MAX_VOLUME, ((float) progress)/MAX_VOLUME);
				sound_voume_5.setText("vol."+progress+"%");
				break;

			default:
				break;
			}
			
		}
	};

	/*
	 * 播放按钮点击事件
	 */
	private ImageButton.OnClickListener imageButtonListener=new ImageButton.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(!isAllPause)
			{
				
			
			switch (v.getId()) {
			case R.id.sound_player_1:
				
				if(mediaPlayer_1.isPlaying())
				{
					mediaPlayer_1.pause();
					sound_play_1.setImageDrawable(play_bg);
					Log.i(LOCAT, "sound1 暂停播放");
				}else
				{
					mediaPlayer_1.start();
					sound_play_1.setImageDrawable(pause_bg);
					Log.i(LOCAT, "sound1 开始播放");
				}
				
				break;
            case R.id.sound_player_2:
            	if(mediaPlayer_2.isPlaying())
				{
					mediaPlayer_2.pause();
					sound_play_2.setImageDrawable(play_bg);
					Log.i(LOCAT, "sound2 暂停播放");
				}else
				{
					mediaPlayer_2.start();
					sound_play_2.setImageDrawable(pause_bg);
					Log.i(LOCAT, "sound2 开始播放");
				}
				break;
            case R.id.sound_player_3:
            	if(mediaPlayer_3.isPlaying())
				{
					mediaPlayer_3.pause();
					sound_play_3.setImageDrawable(play_bg);
					Log.i(LOCAT, "sound3 暂停播放");
				}else
				{
					mediaPlayer_3.start();
					sound_play_3.setImageDrawable(pause_bg);
					Log.i(LOCAT, "sound3 开始播放");
				}
	            break;
            case R.id.sound_player_4:
            	if(mediaPlayer_4.isPlaying())
				{
					mediaPlayer_4.pause();
					sound_play_4.setImageDrawable(play_bg);
					Log.i(LOCAT, "sound4 暂停播放");
				}else
				{
					mediaPlayer_4.start();
					sound_play_4.setImageDrawable(pause_bg);
					Log.i(LOCAT, "sound4 开始播放");
				}
	
	            break;
            case R.id.sound_player_5:
            	if(mediaPlayer_5.isPlaying())
				{
					mediaPlayer_5.pause();
					sound_play_5.setImageDrawable(play_bg);
					Log.i(LOCAT, "sound5 暂停播放");
				}else
				{
					mediaPlayer_5.start();
					sound_play_5.setImageDrawable(pause_bg);
					Log.i(LOCAT, "sound5 开始播放");
				}
	
	            break;

			default:
				break;
			}
		  }
		}
	};
	
	/**
	 * 控件注册事件
	 */
	private void initControllerListener()
	{
		seekBar_1.setOnSeekBarChangeListener(seekBarListener);
		seekBar_2.setOnSeekBarChangeListener(seekBarListener);
		seekBar_3.setOnSeekBarChangeListener(seekBarListener);
		seekBar_4.setOnSeekBarChangeListener(seekBarListener);
		seekBar_5.setOnSeekBarChangeListener(seekBarListener);
		
		sound_play_1.setOnClickListener(imageButtonListener);
		sound_play_2.setOnClickListener(imageButtonListener);
		sound_play_3.setOnClickListener(imageButtonListener);
		sound_play_4.setOnClickListener(imageButtonListener);
		sound_play_5.setOnClickListener(imageButtonListener);
	}
	
	/**
	 * 初始化ImageButton图片
	 */
	private void initPalyButtonBackgroud()

	{
	 
		sound_play_1.setImageDrawable(pause_bg);
		sound_play_2.setImageDrawable(pause_bg);
		sound_play_3.setImageDrawable(pause_bg);
		sound_play_4.setImageDrawable(pause_bg);
		sound_play_5.setImageDrawable(pause_bg);
		
		sound_player_1_back.setBackgroundDrawable(soundsModen.getBitmapMap().get(Contanst.bitmap_1));

		sound_player_2_back.setBackgroundDrawable(soundsModen.getBitmapMap().get(Contanst.bitmap_2));
		sound_player_3_back.setBackgroundDrawable(soundsModen.getBitmapMap().get(Contanst.bitmap_3));
		sound_player_4_back.setBackgroundDrawable(soundsModen.getBitmapMap().get(Contanst.bitmap_4));
		sound_player_5_back.setBackgroundDrawable(soundsModen.getBitmapMap().get(Contanst.bitmap_5));

	}
	
	
    /**
     * 初始化音乐文件名
     */
    private void initSoundName()
    {
    	sound_name_1.setText(soundsModen.getNameMap().get(Contanst.name_1));
    	sound_name_2.setText(soundsModen.getNameMap().get(Contanst.name_2));
    	sound_name_3.setText(soundsModen.getNameMap().get(Contanst.name_3));
    	sound_name_4.setText(soundsModen.getNameMap().get(Contanst.name_4));
    	sound_name_5.setText(soundsModen.getNameMap().get(Contanst.name_5));
    }
    
    

    /**
     * 初始化音乐音量
     */
    private void initSoundVolume()
    {
    	
    	int volume_1=soundsModen.getVolumeMap().get(Contanst.volume_1);
    	sound_voume_1.setText("vol."+volume_1+"%");
    	mediaPlayer_1.setVolume(((float)volume_1)/MAX_VOLUME, ((float)volume_1)/MAX_VOLUME);
    	seekBar_1.setProgress(volume_1);
    	
    	int volume_2=soundsModen.getVolumeMap().get(Contanst.volume_2);
    	sound_voume_2.setText("vol."+volume_2+"%");
    	mediaPlayer_2.setVolume(((float)volume_2)/MAX_VOLUME, ((float)volume_2)/MAX_VOLUME);
    	seekBar_2.setProgress(volume_2);
    	
    	int volume_3=soundsModen.getVolumeMap().get(Contanst.volume_3);
    	sound_voume_3.setText(""+volume_3+"%");
    	mediaPlayer_3.setVolume(((float)volume_3)/MAX_VOLUME, ((float)volume_3)/MAX_VOLUME);
    	seekBar_3.setProgress(volume_3);
    	
    	int volume_4=soundsModen.getVolumeMap().get(Contanst.volume_4);
    	sound_voume_4.setText("vol."+volume_4+"%");
    	mediaPlayer_4.setVolume(((float)volume_4)/MAX_VOLUME, ((float)volume_4)/MAX_VOLUME);
    	seekBar_4.setProgress(volume_4);
    	
    	int volume_5=soundsModen.getVolumeMap().get(Contanst.volume_5);
    	sound_voume_5.setText("vol."+volume_5+"%");
    	mediaPlayer_5.setVolume(((float)volume_1)/MAX_VOLUME, ((float)volume_5)/MAX_VOLUME);
    	seekBar_5.setProgress(volume_5);
    	
    	
    	
    	
    }
	
    /**
     * 得到封装的音乐文件信息
     * @return
     */
    public SoundsModen getSoundsModen() {
		return soundsModen;
	}

    /**
     * 实例 MediaPlayers
     */
    private void initMediaPlayer()
    {
    	mediaPlayer_1=new MediaPlayer();
    	mediaPlayer_2=new MediaPlayer();
    	mediaPlayer_3=new MediaPlayer();
    	mediaPlayer_4=new MediaPlayer();
    	mediaPlayer_5=new MediaPlayer();
    }
    
    /**
     * 设置音乐文件信息
     * @param soundsModen
     */
	public void setSoundsModen(SoundsModen soundsModen) {
		this.soundsModen = soundsModen;
	}
	
	/**
	 * 得到是否全部暂停的状态
	 * @return
	 */
	public boolean getISAllPause()
	{
		return isAllPause;
	}
	
	public void setIsAllPause(boolean flag)
	{
		isAllPause=flag;
	}
	/**
	 * 设置媒体资源与预准备
	 */
	 private void setMediaPlayer()
	    {
//	    	mediaPlayer_1=MediaPlayer.create(getContext(), Uri.fromFile(soundsModen.getSoundMap().get(Contanst.sound_1)));
//	    	mediaPlayer_2=MediaPlayer.create(getContext(), Uri.fromFile(soundsModen.getSoundMap().get(Contanst.sound_2)));
//	    	mediaPlayer_3=MediaPlayer.create(getContext(), Uri.fromFile(soundsModen.getSoundMap().get(Contanst.sound_3)));
//	    	mediaPlayer_4=MediaPlayer.create(getContext(), Uri.fromFile(soundsModen.getSoundMap().get(Contanst.sound_4)));
//	    	mediaPlayer_5=MediaPlayer.create(getContext(), Uri.fromFile(soundsModen.getSoundMap().get(Contanst.sound_5)));
	    	
		   // initMediaPlayer();
		    if(first)
		    {
		    	first=false;
		    	Log.i(LOCAT, "第一次初始化媒体播放器");
		    }
		    else
		    {
		    mediaPlayer_1.reset();
	    	mediaPlayer_2.reset();
	    	mediaPlayer_3.reset();
	    	mediaPlayer_4.reset();
	    	mediaPlayer_5.reset();
		    }
	    	
	    	
	    	try {
				mediaPlayer_1.setDataSource(soundsModen.getSoundMap().get(Contanst.sound_1));
				mediaPlayer_2.setDataSource(soundsModen.getSoundMap().get(Contanst.sound_2));
				mediaPlayer_3.setDataSource(soundsModen.getSoundMap().get(Contanst.sound_3));
				mediaPlayer_4.setDataSource(soundsModen.getSoundMap().get(Contanst.sound_4));
				mediaPlayer_5.setDataSource(soundsModen.getSoundMap().get(Contanst.sound_5));
			} catch (Exception e) {
				Log.i(LOCAT, "初始化 mediaplayer 失败");
				e.printStackTrace();
			} 
	    	
	    	mediaPlayer_1.setLooping(true);
	    	mediaPlayer_2.setLooping(true);
	    	mediaPlayer_3.setLooping(true);
	    	mediaPlayer_4.setLooping(true);
	    	mediaPlayer_5.setLooping(true);
	    	
	    	try {
				mediaPlayer_1.prepare();
				mediaPlayer_2.prepare();
				mediaPlayer_3.prepare();
				mediaPlayer_4.prepare();
				mediaPlayer_5.prepare();
			} catch (IllegalStateException e) {
				Log.i(LOCAT, e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				Log.i(LOCAT, e.getMessage());
				e.printStackTrace();
			}
	    }
	
	/**
	 * 初始化多媒体文件。
	 * 注：此方法必须在 setSoundsModen调用之后才可操作，否则产生空指针异常
	 */
	public void initMultiMedias()
	{
//		initMediaPlayer();
		setMediaPlayer();
		initSoundName();
		initSoundVolume();
		initPalyButtonBackgroud();
	}
	
	
	/**
//	 * 时间结束停止播放并释放 MediaPlayer
//	 */
//	private Handler handler=new Handler()
//	{
//		public void handleMessage(android.os.Message msg) {
//			if(msg.what==-1)
//			{
//				stopPlay();
//			}
//			
//		};
//	};
    
	/**
	 * 播放结束
	 */
    public  void stopPlay()
    {
    	if(mediaPlayer_1.isPlaying())
    	{
    		mediaPlayer_1.stop();
    		mediaPlayer_1.release();
    		mediaPlayer_1=null;
    	}
    	
    	if(mediaPlayer_2.isPlaying())
    	{
    		mediaPlayer_2.stop();
    		mediaPlayer_2.release();
    		mediaPlayer_2=null;
    	}
    	if(mediaPlayer_3.isPlaying())
    	{
    		mediaPlayer_3.stop();
    		mediaPlayer_3.release();
    		mediaPlayer_3=null;
    	}
    	if(mediaPlayer_4.isPlaying())
    	{
    		mediaPlayer_4.stop();
    		mediaPlayer_4.release();
    		mediaPlayer_4=null;
    	}
    	if(mediaPlayer_5.isPlaying())
    	{
    		mediaPlayer_5.stop();
    		mediaPlayer_5.release();
    		mediaPlayer_5=null;
    	}
    		
    	    
    }

    /**
     * 开始播放
     */

    public void startPlay()
    {
    	if(!mediaPlayer_1.isPlaying())
    	{
    		mediaPlayer_1.start();
    	}
    	
    	if(!mediaPlayer_2.isPlaying())
    	{
    		mediaPlayer_2.start();
    	}
    	
    	if(!mediaPlayer_3.isPlaying())
    	{
    		mediaPlayer_3.start();
    	}
    	if(!mediaPlayer_4.isPlaying())
    	{
    		mediaPlayer_4.start();
    	}
    	if(!mediaPlayer_5.isPlaying())
    	{
    		mediaPlayer_5.start();
    	}
    }
    
    /**
     * 暂停播放
     * 
     */
   public void pausePlayers()
   {
	   mediaPlayer_1.pause();
	   mediaPlayer_2.pause();
	   mediaPlayer_3.pause();
	   mediaPlayer_4.pause();
	   mediaPlayer_5.pause();
   }
    
//    /**
//     * 释放所有Mediaplayer
//     */
//     public void releaseMediaPlayer()
//    {
//    	mediaPlayer_1.release();
//		mediaPlayer_1=null;
//		
//		mediaPlayer_2.release();
//		mediaPlayer_2=null;
//		
//		mediaPlayer_3.release();
//		mediaPlayer_3=null;
//		
//		
//		mediaPlayer_4.release();
//		mediaPlayer_4=null;
//		
//		mediaPlayer_5.release();
//		mediaPlayer_5=null;
//    }
}
