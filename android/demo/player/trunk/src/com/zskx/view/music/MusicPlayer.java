package com.zskx.view.music;

import java.io.IOException;
import java.util.Formatter;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.zskx.activity.R;
import com.zskx.util.MusicRes;
import com.zskx.util.MyPost;
/**
 * 播放器(标识svn)
 * @author demo
 *
 */
public class MusicPlayer extends RelativeLayout {

	/**多媒体播放器*/
	private MediaPlayer player = new MediaPlayer();
	/**上一首按钮*/
	private ImageView preBtn;
	/**播放或暂停按钮*/
	private ImageView playOrPause;
	/**下一首按钮*/
	private ImageView nextBtn;
	/**重置按钮*/
	private ImageView resetBtn;
	/**播放进度条*/
	private SeekBar progress;
	/**当前正在播放的音乐id*/
	private int currentMusicId = MusicRes.HELP_SLEEP_MUSIC;
	/**歌曲是否为暂停状态*/
	private boolean isPause = true;
	/**暂停时音乐播放的位置*/
	private int pausePostion = 0;
	/**检测歌曲进度线程*/
	private CheckProgressThread progressThread;
	/**播放器操作监听器*/
	private MusicPlayerListener listener;
	/**是否释放资源*/
	private boolean isRelease = false;
	/**音乐全长时间*/
	private TextView totalTime;
	/**音乐当前时间*/
	private TextView currentTime;
	public MusicPlayer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * 摧毁播放器
	 */
	public void releasePlayer(){
		if(progressThread!=null){
			progressThread.destroyThread();
		}
		isRelease = true;
		player.release();
	}
	
	/**
	 * 初始化
	 */
	private void init(){
		View.inflate(getContext(), R.layout.music_player, this);
		
		preBtn = (ImageView)findViewById(R.id.pre_music);
		playOrPause = (ImageView)findViewById(R.id.playOrPause);
		nextBtn = (ImageView)findViewById(R.id.next_music);
		resetBtn = (ImageView)findViewById(R.id.reset_music);
		progress = (SeekBar)findViewById(R.id.musicbar);
		totalTime = (TextView)findViewById(R.id.allTime);
		currentTime = (TextView)findViewById(R.id.currentTime);
		
		
		loadMusic(currentMusicId);
		
		initListener();
	}
	/**
	 * 初始化监听器
	 */
	private void initListener(){
		preBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				preMusic();
			}
		});
		
		playOrPause.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isPause){
					play(pausePostion);
				}else {
					pause();
				}
			}
		});
		
		nextBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				nextMusic();
			}
		});
		
		resetBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				resetMusic();
			}
		});
		
		progress.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				if(!isPause)
					player.start();
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				if(!isPause)
					player.pause();
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, final int progress,
					boolean fromUser) {
				if(fromUser){
					if(isPause){
						pausePostion = progress;
						
						Log.i(this.getClass().getSimpleName(), "播放器暂停时拖动进度条到:"+pausePostion);
					}else {
						player.seekTo(progress);
					}
					
				}
				currentTime.setText(changeMillinTimeToString(progress));
			}
		});
		
		
		player.setOnErrorListener(new OnErrorListener() {
			
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				Log.i(this.getClass().getSimpleName(), "播放器出错了");
				return false;
			}
		});
		player.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				play(0);
				Log.i(this.getClass().getSimpleName(), "播放完成");
			}
		});
		player.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer mp) {
				if(progressThread==null){
					progressThread = new CheckProgressThread();
					progressThread.start();
				}
			}
		});
	}
	/**
	 * 将毫秒数转换成时间字符串
	 * @param millinTime
	 * @return
	 */
	public String changeMillinTimeToString(int millinTime){
		
		int temp = millinTime/1000;
		int hour = temp/60/60;
		int minute = temp/60%60;
		int second = temp%60;
		if(hour>0){
			return new Formatter().format("%d:%02d:%02d", hour,minute,second).toString();
		}else {
			return new Formatter().format("%02d:%02d", minute,second).toString();
		}
		
	}
	/**
	 * 装载传入的音乐id对应的文件
	 * @param musicId
	 */
	public void loadMusic(int musicId){
		String path = getMusicResPath(musicId);
		
		try {
			player.reset();
			player.setDataSource(path);
			player.prepare();
			progress.setMax(player.getDuration());
			totalTime.setText(changeMillinTimeToString(player.getDuration()));
			currentMusicId = musicId;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		
		if(visibility==View.GONE){
			if(!isRelease)
				pause();
		}
		
		super.onVisibilityChanged(changedView, visibility);
	}
	
	/**
	 * 播放音乐在指定位置
	 * @param musicId
	 * @param position
	 */
	public void play(int position){
		
			
			player.seekTo(position);
			progress.setProgress(position);
			player.start();
			//设置播放器属性
			
			isPause = false;
			pausePostion = position;
			
			playOrPause.setImageResource(R.drawable.pause_btn_selector);
	}

	/**
	 * 暂停播放
	 */
	public void pause(){
			if(player.isPlaying()){
				player.pause();
				isPause = true;
				pausePostion = player.getCurrentPosition();
				//改变背景图片
				playOrPause.setImageResource(R.drawable.play_btn_selector);
			}
	}
	/**
	 * 上一曲
	 */
	public void preMusic(){
		loadMusic(getPreMusicId(currentMusicId));
		play(0);
		listener.preMusic(getPreMusicId(currentMusicId),currentMusicId);
	}
	/**
	 * 下一曲
	 */
	public void nextMusic(){
		loadMusic(getNextMusicId(currentMusicId));
		play(0);
		listener.nextMusic(getNextMusicId(currentMusicId),currentMusicId);
	}
	/**
	 * 重置歌曲
	 */
	public void resetMusic(){
		loadMusic(currentMusicId);
		play(0);
		pause();
		listener.resetMusic(currentMusicId);
//		player.reset();
	}
	/**
	 * 得到上一曲音乐id
	 * @param currentId
	 * @return
	 */
	public int getPreMusicId(int currentId){
		int musicId = currentId-1;
		if(musicId<MusicRes.HELP_SLEEP_MUSIC){
			musicId = MusicRes.RELAX_MUSIC;
		}
		return musicId;
	}
	/**
	 * 得到下一曲音乐id
	 * @param currentId
	 * @return
	 */
	public int getNextMusicId(int currentId){
		int musicId = currentId+1;
		if(musicId>MusicRes.RELAX_MUSIC){
			musicId = MusicRes.HELP_SLEEP_MUSIC;
		}
		return musicId;
	}
	
	/**
	 * 通过传入的音乐id，得到在sd卡上的存储路径
	 * @param musicId 音乐的规定id
	 * @return 
	 */
	public String getMusicResPath(int musicId){
		String path = null;
		
		path = getSDPath(getContext())+"/web/media/music/music00"+musicId+".ogg";
		Log.i(this.getClass().getSimpleName(), "歌曲的路径为："+path);
		return path;
	}
	
	public static Toast toast;
	
	/**
	 * 得到设备SD卡的路径
	 * @return 没有sd卡则返回null
	 */
	public static String getSDPath(Context context){
		String path = null;
		//判断SD卡是否存在
		if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
			path = Environment.getExternalStorageDirectory().toString();
		}else {
			if(toast!=null)
				toast.cancel();
			toast = Toast.makeText(context, "没有检测到SD卡", 1000);
			toast.show();
		}
		Log.i("sdcard", "sd卡的路径为："+path);
		return path;
	}
	/**
	 * 检测歌曲播放进度线程
	 * @author demo
	 *
	 */
	public class CheckProgressThread extends Thread{
		private boolean isRun = false;
		/**
		 * 銷毀綫程
		 */
		public void destroyThread(){
			isRun = false;
		}
		@Override
		public void run() {
			isRun = true;
			int currentPosition = 0;
			if(player!=null){
				int total = player.getDuration();
				while (currentPosition<total&&isRun) {
					try {
						Thread.sleep(1000);
						if(!isRelease&&!isPause){
							currentPosition = player.getCurrentPosition();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(!isPause){
						progress.setProgress(currentPosition);
						
					}
				}
			}
		}
	}
	/**
	 * 设置监听器
	 * @param listener
	 */
	public void setMusicPlayerListener(MusicPlayerListener listener){
		this.listener = listener;
	}
	
	/**
	 * 播放器操作监听器
	 * @author demo
	 *
	 */
	public interface MusicPlayerListener {
		/**
		 * 播放上一首歌曲
		 */
		public void preMusic(int oldTag,int tag);
		/**
		 * 播放下一首歌曲
		 */
		public void nextMusic(int oldTag,int tag);
		/**
		 * 暂停歌曲
		 */
		public void pauseMusic(int tag);
		/**
		 * 播放歌曲
		 */
		public void playMusic(int tag);
		/**
		 * 重置歌曲
		 */
		public void resetMusic(int tag);
	}
	
}
