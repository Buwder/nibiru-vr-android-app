package com.zskx.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.zskx.activity.R;
import com.zskx.util.PreMenuImageRes;
import com.zskx.view.MainMenuItem.MainMenuOnClickListener;

public class MainMenuLayout extends LinearLayout {

	/**前一个被选择的菜单tag*/
	private int oldMenuTag;
	/**音乐治疗*/
	private MainMenuItem music;
	/**hrv干预*/
	private MainMenuItem hrv;
	/**冥想*/
	private MainMenuItem think;
	/**危机干预*/
	private MainMenuItem stopDanger;
	/**游戏*/
	private MainMenuItem game;
	/**积极干预*/
	private MainMenuItem positiveStop;
	/**主菜单监听器*/
	private MainMenuListener listener;
	
	public MainMenuLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init(){
		View.inflate(getContext(), R.layout.main_menu, this);
		
		music = (MainMenuItem)findViewById(R.id.music);
		hrv = (MainMenuItem)findViewById(R.id.hrv);
		think = (MainMenuItem)findViewById(R.id.think);
		stopDanger = (MainMenuItem)findViewById(R.id.dangerStop);
		game = (MainMenuItem)findViewById(R.id.game);
		positiveStop = (MainMenuItem)findViewById(R.id.positiveStop);
		
		music.setMenuText(R.string.music);
		music.setMenuImage(R.drawable.menu_ico_music);
		music.setMenuTag(PreMenuImageRes.MUSIC);
		
		hrv.setMenuText(R.string.hrv);
		hrv.setMenuImage(R.drawable.menu_ico_movie);
		hrv.setMenuTag(PreMenuImageRes.MOVIE);
		
		think.setMenuText(R.string.think);
		think.setMenuImage(R.drawable.menu_ico_relax);
		think.setMenuTag(PreMenuImageRes.RELAX);
		
		stopDanger.setMenuText(R.string.dangerStop);
		stopDanger.setMenuImage(R.drawable.menu_ico_crisis);
		stopDanger.setMenuTag(PreMenuImageRes.CRISIS);
		
		game.setMenuText(R.string.game);
		game.setMenuImage(R.drawable.menu_ico_game);
		game.setMenuTag(PreMenuImageRes.GAME);
		
		positiveStop.setMenuText(R.string.positiveStop);
		positiveStop.setMenuImage(R.drawable.menu_ico_magazine);
		positiveStop.setMenuTag(PreMenuImageRes.MAGAZINE);
		
		MainMenuOnClickListener listener = new MainMenuOnClickListener() {
			
			@Override
			public void menuBeClicked(int tag) {
				
				beClicked(tag);
				
			}
		};
		
		music.setMainMenuListener(listener);
		hrv.setMainMenuListener(listener);
		think.setMainMenuListener(listener);
		stopDanger.setMainMenuListener(listener);
		game.setMainMenuListener(listener);
		positiveStop.setMainMenuListener(listener);
	}
	/**
	 * 手动选择菜单
	 * @param tag
	 */
	public void selectMenuByHand(int tag){
		switch (tag) {
		case PreMenuImageRes.MUSIC:
			music.beClicked();
			break;
		case PreMenuImageRes.MOVIE:
			hrv.beClicked();		
			break;
		case PreMenuImageRes.RELAX:
			think.beClicked();
			break;
		case PreMenuImageRes.CRISIS:
			stopDanger.beClicked();
			break;
		case PreMenuImageRes.GAME:
			game.beClicked();
			break;
		case PreMenuImageRes.MAGAZINE:
			positiveStop.beClicked();
			break;
		default:
			break;
		}
		oldMenuTag = tag;
	}
	/**
	 * 被点击后的操作
	 * @param tag
	 */
	public void beClicked(int tag){
		switch (oldMenuTag) {
		case PreMenuImageRes.MUSIC:
			music.becomeNormal();
			break;
		case PreMenuImageRes.MOVIE:
			hrv.becomeNormal();		
			break;
		case PreMenuImageRes.RELAX:
			think.becomeNormal();
			break;
		case PreMenuImageRes.CRISIS:
			stopDanger.becomeNormal();
			break;
		case PreMenuImageRes.GAME:
			game.becomeNormal();
			break;
		case PreMenuImageRes.MAGAZINE:
			positiveStop.becomeNormal();
			break;
		default:
			break;
		}
		if(MainMenuLayout.this.listener!=null){
			//如果上层要监听主菜单的情况，则在此调用该回调
			MainMenuLayout.this.listener.menuBeClicked(oldMenuTag, tag);
		}
		
		oldMenuTag = tag;
	}
	/**
	 * 设置主菜单监听器
	 * @param listener
	 */
	public void setMainMenuListener(MainMenuListener listener){
		this.listener = listener;
	}
	/**
	 * 主菜单到监听器
	 * @author demo
	 *
	 */
	public interface MainMenuListener{
		/**
		 * 主菜单被点击的监听器
		 * @param oldTag
		 * @param newTag
		 */
		public void menuBeClicked(int oldTag,int newTag);
	}

}
