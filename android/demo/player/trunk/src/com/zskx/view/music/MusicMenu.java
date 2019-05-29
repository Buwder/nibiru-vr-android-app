package com.zskx.view.music;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.zskx.activity.R;
import com.zskx.util.MusicRes;
import com.zskx.view.music.MusicMenuItem.MusicSubItemListener;
/**
 * 音乐菜单
 * @author demo
 *
 */
public class MusicMenu extends LinearLayout {

	/**前一首被选中的音乐菜单tag,默认是第一首*/
	private int oldMenuTag = MusicRes.HELP_SLEEP_MUSIC;
	/**帮助睡眠*/
	private MusicMenuItem helpSleep;
	/**净化心灵*/
	private MusicMenuItem clearHeart;
	/**放松减压*/
	private MusicMenuItem relax;
	/**缓解焦虑*/
	private MusicMenuItem crisis;
	/**音乐菜单监听器*/
	private MusicMenuListener listener;
	
	public MusicMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init(){
		View.inflate(getContext(), R.layout.music_menu, this);
		
		helpSleep = (MusicMenuItem) findViewById(R.id.help_sleep);
		clearHeart = (MusicMenuItem) findViewById(R.id.clear_heart);
		crisis = (MusicMenuItem) findViewById(R.id.crisis);
		relax = (MusicMenuItem) findViewById(R.id.relax);
		
		helpSleep.setMusicImage(R.drawable.music_01);
		helpSleep.setMusicTitle(R.string.help_sleep);
		helpSleep.setMusicDescription(R.string.help_sleep_description);
		helpSleep.setMusicTag(MusicRes.HELP_SLEEP_MUSIC);
		
		clearHeart.setMusicImage(R.drawable.music_02);
		clearHeart.setMusicTitle(R.string.clear_heart);
		clearHeart.setMusicDescription(R.string.clear_heart_description);
		clearHeart.setMusicTag(MusicRes.CLEAR_HEART_MUSIC);
		
		crisis.setMusicImage(R.drawable.music_03);
		crisis.setMusicTitle(R.string.crisis);
		crisis.setMusicDescription(R.string.crisis_description);
		crisis.setMusicTag(MusicRes.CRISIS_MUSIC);
		
		relax.setMusicImage(R.drawable.music_04);
		relax.setMusicTitle(R.string.relax);
		relax.setMusicDescription(R.string.relax_description);
		relax.setMusicTag(MusicRes.RELAX_MUSIC);
		
		MusicSubItemListener listener = new MusicSubItemListener() {
			
			@Override
			public void subItemBeClicked(int tag) {
				beClicked(tag);
			}
		};
		
		helpSleep.setSubItemListener(listener);
		clearHeart.setSubItemListener(listener);
		crisis.setSubItemListener(listener);
		relax.setSubItemListener(listener);
		//默认选中第一首歌
		selectMusicMenuByHand(oldMenuTag,oldMenuTag);
	}
	
	public void selectMusicMenuByHand(int oldTag,int tag){
		//将上一次变为未被选中的
		switch (oldTag) {
		case MusicRes.HELP_SLEEP_MUSIC:
			helpSleep.becomeNormal();
			break;
		case MusicRes.CLEAR_HEART_MUSIC:
			clearHeart.becomeNormal();		
			break;
		case MusicRes.CRISIS_MUSIC:
			crisis.becomeNormal();
			break;
		case MusicRes.RELAX_MUSIC:
			relax.becomeNormal();
			break;
		default:
			break;
		}
		
		switch (tag) {
		case MusicRes.HELP_SLEEP_MUSIC:
			helpSleep.beClicked();
			break;
		case MusicRes.CLEAR_HEART_MUSIC:
			clearHeart.beClicked();		
			break;
		case MusicRes.CRISIS_MUSIC:
			crisis.beClicked();
			break;
		case MusicRes.RELAX_MUSIC:
			relax.beClicked();
			break;
		default:
			break;
		}
		oldMenuTag = tag;
	}
	/**
	 * 被点击后的操作 
	 */
	public void beClicked(int tag){
		//将前一首歌到菜单变会正常状态
		switch (oldMenuTag) {
		case MusicRes.HELP_SLEEP_MUSIC:
			helpSleep.becomeNormal();
			break;
		case MusicRes.CLEAR_HEART_MUSIC:
			clearHeart.becomeNormal();		
			break;
		case MusicRes.CRISIS_MUSIC:
			crisis.becomeNormal();
			break;
		case MusicRes.RELAX_MUSIC:
			relax.becomeNormal();
			break;
		default:
			break;
		}
		
		if (MusicMenu.this.listener!=null) {
			//如果上层要监听音乐菜单的情况，则在此调用该回调
			MusicMenu.this.listener.musicBeClicked(oldMenuTag, tag);
		}
		
		oldMenuTag = tag;
		
	}
	
	/**
	 * 设置音乐菜单监听器
	 * @param listener
	 */
	public void setMusicMenuListener(MusicMenuListener listener){
		this.listener = listener;
	}
	
	/**
	 * 音乐菜单到监听器
	 * @author demo
	 *
	 */
	public interface MusicMenuListener{
		/**
		 * 音乐菜单被点击的监听器
		 * @param oldTag
		 * @param newTag
		 */
		public void musicBeClicked(int oldTag,int newTag);
	}
	
	
}
