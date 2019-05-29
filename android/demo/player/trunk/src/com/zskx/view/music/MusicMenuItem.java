package com.zskx.view.music;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zskx.activity.R;
/**
 * 音乐菜单列表的单个选项
 * @author demo
 *
 */
public class MusicMenuItem extends LinearLayout {

	/**音乐到图片背景*/
	private ImageView musicImage;
	/**音乐标题*/
	private TextView musicTitle;
	/**音乐描述*/
	private TextView musicDescription;
	/**父控件传下来到监听器*/
	private MusicSubItemListener listener;
	/**标识音乐的类型*/
	private int musicTag;
	
	public MusicMenuItem(Context context) {
		super(context);
		init();
	}

	public MusicMenuItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * 初始化
	 */
	private void init(){
		View.inflate(getContext(), R.layout.music_menu_item, this);
		
		musicTitle=(TextView)findViewById(R.id.music_name);
		musicDescription=(TextView)findViewById(R.id.music_description);
		musicImage=(ImageView)findViewById(R.id.music_image);
		
		this.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				beClicked();
			}
		});
		
	}
	
	/**
	 * 被点击后做的操作
	 */
	public void beClicked(){
		if(listener!=null){
			listener.subItemBeClicked(musicTag);
		}
		
		musicTitle.setTextAppearance(getContext(), R.style.light_blue_25sp_bold);
	}
	
	/**
	 * 设置音乐图片
	 * @param ResId
	 */
	public void setMusicImage(int ResId){
		musicImage.setImageResource(ResId);
	}
	/**
	 * 设置音乐描述
	 * @param resId
	 */
	public void setMusicDescription(int resId){
		musicDescription.setText(resId);
	}
	
	/**
	 * 设置音乐标题
	 * @param s
	 */
	public void setMusicTitle(int resId){
		musicTitle.setText(resId);
	}
	
	/**
	 * 设置音乐标题
	 * @param s
	 */
	public void setMusicTitle(String s){
		musicTitle.setText(s);
	}
	/**
	 * 设置监听器
	 * @param listener
	 */
	public void setSubItemListener(MusicSubItemListener listener){
		this.listener = listener;
	}
	/**
	 * 设置音乐标签
	 * @param tag
	 */
	public void setMusicTag(int tag){
		this.musicTag = tag;
	}
	/**
	 * 设置字体为正常状态
	 */
	public void becomeNormal(){
		musicTitle.setTextAppearance(getContext(), R.style.black_25sp_bold);
	}
	
	/**
	 * 监听音乐菜单里的选项的情况
	 * @author demo
	 *
	 */
	public interface MusicSubItemListener{
		/**
		 * 音乐项被单击
		 * @param tag 被点击的音乐的标签
		 */
		public void subItemBeClicked(int tag);
	}
	
}
