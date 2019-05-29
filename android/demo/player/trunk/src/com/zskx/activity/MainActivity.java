package com.zskx.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.zskx.controller.ActivityHolder;
import com.zskx.dialog.ExitDialog;
import com.zskx.util.PreMenuImageRes;
import com.zskx.view.MainMenuLayout;
import com.zskx.view.MainMenuLayout.MainMenuListener;
import com.zskx.view.music.MusicLayout;
import com.zskx.view.video.CrisisLayout;
import com.zskx.view.video.MovieLayout;
import com.zskx.view.video.RelaxLayout;

public class MainActivity extends Activity {

	/**被选中到功能名字*/
	private TextView title;
	/**退出程序按钮*/
	private ImageView exit;
	/**主菜单*/
	private MainMenuLayout menu;
	/**音乐播放界面*/
	private MusicLayout music;
	/**心理减压*/
	private MovieLayout movie;
	/**放松*/
	private RelaxLayout relax;
	/**干预*/
	private CrisisLayout crisis;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		
		init();
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	private void init(){
		
		int functionTag = getIntent().getIntExtra("functionTag", 0);
		
		music = (MusicLayout)findViewById(R.id.musicLayout);
		movie = (MovieLayout)findViewById(R.id.movieLayout);
		relax = (RelaxLayout)findViewById(R.id.relaxLayout);
		crisis = (CrisisLayout)findViewById(R.id.crisisLayout);
		
		exit = (ImageView)findViewById(R.id.exit);
		exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ExitDialog.show(MainActivity.this);
			}
		});
		
		
		title = (TextView)findViewById(R.id.function_title);
		menu = (MainMenuLayout)findViewById(R.id.menu);
		
		//设置上个页面点击的功能为显示页面
		Log.i(this.getClass().getSimpleName(), "上一个界面传过来的tag:"+functionTag);
		menu.selectMenuByHand(functionTag);
		setTitleAndShowView(functionTag);
		
		
		
		
		menu.setMainMenuListener(new MainMenuListener() {
			
			@Override
			public void menuBeClicked(int oldTag, int newTag) {
				setViewInvisible(oldTag);
				setTitleAndShowView(newTag);
				
			}
		});
		
		
	}
	/**
	 * 设置前一个功能界面不可见
	 * @param oldTag
	 */
	public void setViewInvisible(int oldTag){
		switch (oldTag) {
		case PreMenuImageRes.MUSIC:
			music.setVisibility(View.GONE);
			break;
		case PreMenuImageRes.MOVIE:
			movie.setVisibility(View.GONE);
			break;
		case PreMenuImageRes.RELAX:
			relax.setVisibility(View.GONE);
			break;
		case PreMenuImageRes.CRISIS:
			crisis.setVisibility(View.GONE);
			break;
		case PreMenuImageRes.GAME:
			break;
		case PreMenuImageRes.MAGAZINE:
			break;
		default:
			break;
		}
	}
	/**
	 * 设置标题和相应界面可见
	 */
	public void setTitleAndShowView(int newTag){
		switch (newTag) {
		case PreMenuImageRes.MUSIC:
			title.setText(R.string.music);
			music.setVisibility(View.VISIBLE);
			break;
		case PreMenuImageRes.MOVIE:
			title.setText(R.string.hrv);
			movie.setVisibility(View.VISIBLE);
			break;
		case PreMenuImageRes.RELAX:
			title.setText(R.string.think);
			relax.setVisibility(View.VISIBLE);
			break;
		case PreMenuImageRes.CRISIS:
			title.setText(R.string.dangerStop);
			crisis.setVisibility(View.VISIBLE);
			break;
		case PreMenuImageRes.GAME:
			title.setText(R.string.game);
			break;
		case PreMenuImageRes.MAGAZINE:
			title.setText(R.string.positiveStop);
			break;
		default:
			break;
		}
	}
	
	 @Override
	    protected void onResume() {
	    	super.onResume();
	    	//移除控制器中
	    	ActivityHolder.getIntance().push(this);
	    }
	    
	    @Override
	    protected void onStop() {
	    	super.onStop();
	    	music.releasePlayer();
	    	//压入控制器中
	    	ActivityHolder.getIntance().pop(this);
	    }
	    
	    @Override
	    protected void onDestroy() {
	    	super.onDestroy();
	    	
	    	
	    	//移除控制器中
	    	ActivityHolder.getIntance().push(this);
	    }
}
