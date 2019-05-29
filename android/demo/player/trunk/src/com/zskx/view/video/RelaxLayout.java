package com.zskx.view.video;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.zskx.activity.R;
import com.zskx.view.music.MusicPlayer;
import com.zskx.view.video.VideoMenuItem.VideoMenuItemListener;

public class RelaxLayout extends LinearLayout {

	/** 视频播放器 */
	private MyVideoView videoPlayer;
	/** 焦虑缓解训练 */
	private VideoMenuItem relax01;
	/** 心理助眠训练 */
	private VideoMenuItem relax02;
	/** 减压训练 */
	private VideoMenuItem relax03;
	/** 肌肉放松训练 */
	private VideoMenuItem relax04;
	/**视频控制器*/
	private MyMediaController controller;
	
	public RelaxLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	private void init() {
		View.inflate(getContext(), R.layout.relax_layout, this);

		videoPlayer = (MyVideoView) findViewById(R.id.videoPlayer);
		//TODO 由于系统提供的xml方法不能初始化界面，所以目前使用动态的添加。缺点不能自定义布局
		controller = new MyMediaController(getContext());
//		try {
//			Field field = controller.getClass().getDeclaredField("mDecorLayoutParams");
//			field.setAccessible(true);
//				WindowManager.LayoutParams layout = (WindowManager.LayoutParams) field.get(controller);
//				layout.x = videoPlayer.getLeft();
//				System.out.println("layout.x:"+layout.x);
//				field.set(controller, layout);
//		} catch (NoSuchFieldException e) {
//			e.printStackTrace();
//		}catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		}
//		controller = (MediaController)findViewById(R.id.mediaController);
//		controller.setAnchorView(findViewById(R.id.videoLayout));
		relax01 = (VideoMenuItem) findViewById(R.id.relax_01);
		relax02 = (VideoMenuItem) findViewById(R.id.relax_02);
		relax03 = (VideoMenuItem) findViewById(R.id.relax_03);
		relax04 = (VideoMenuItem) findViewById(R.id.relax_04);

		relax01.setPath(getVideoPath(1));
		relax02.setPath(getVideoPath(2));
		relax03.setPath(getVideoPath(3));
		relax04.setPath(getVideoPath(4));

		VideoMenuItemListener listener = new VideoMenuItemListener() {
			
			@Override
			public void itemBeClicked(String path) {
				setAllMenuNormal();
				loadVideo(path);
				play(0);
			}
		};
		
		relax01.setListener(listener);
		relax02.setListener(listener);
		relax03.setListener(listener);
		relax04.setListener(listener);
		
		relax01.selectedByHand();
		loadVideo(relax01.getPath());
		
	}

	public void setAllMenuNormal(){
		relax01.becomeNormal();
		relax02.becomeNormal();
		relax03.becomeNormal();
		relax04.becomeNormal();
	}
	/**
	 * 得到视频文件的路径
	 * 
	 * @param videoId
	 * @return
	 */
	public String getVideoPath(int videoId) {
		String path = null;
		path = MusicPlayer.getSDPath(getContext()) + "/web/media/relax/relax00"
				+ videoId + ".mp4";
		Log.i(this.getClass().getSimpleName(), "视频的路径为：" + path);
		return path;
	}

	/**
	 * 加载视频
	 * 
	 * @param path
	 */
	public void loadVideo(String path) {
		videoPlayer.setVideoURI(Uri.parse(path));
		videoPlayer.setMediaController(controller);
		videoPlayer.requestFocus();
	}

	/**
	 * 播放从指定位置
	 * 
	 * @param position
	 */
	public void play(int position) {
		videoPlayer.seekTo(position);

		videoPlayer.start();
	}
	
	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		
		if(visibility==View.GONE){
			if(videoPlayer!=null){
				if(videoPlayer.isPlaying()){
					videoPlayer.pause();
				}
			}
		}
		
		super.onVisibilityChanged(changedView, visibility);
	}
}
