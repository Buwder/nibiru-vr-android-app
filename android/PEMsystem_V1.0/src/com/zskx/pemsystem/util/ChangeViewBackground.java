package com.zskx.pemsystem.util;

import java.util.List;

import android.content.Context;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.zskx.pemsystem.R;

public class ChangeViewBackground {
	
	public  OnTouchListener get_change_back(final Context context ,final SparseArray< View> map, final List<Integer> list_btn, final int btnBack){
		 View.OnTouchListener buttons_touch1 = new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					for (int btnID : list_btn) {
						touch_event(v, btnID, map.get(btnID), event);
					}
					
					return false;
				}
				/**
				 * 点击一个控件，改变另一控件背景点击一个控件，改变另一控件背景
				 * @param v 点击的控件
				 * @param btn 控件的id
				 * @param lyt 要改变背景的控件
				 * @param event
				 */
				void touch_event(View v, int btn , View lyt , MotionEvent event ){
					if(v.getId() == btn ) change_ontouch(lyt, event );
				}
				/**
				 * 改变背景图片
				 * @param music_lyt_next2 需要改变的背景
				 * @param event 触摸事件
				 */
				protected void change_ontouch(View music_lyt, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
//						music_lyt.setBackgroundResource(R.drawable.movie_playing_control_voice_bar);
						music_lyt.setBackgroundResource(btnBack);
						break;
					
					case MotionEvent.ACTION_UP:
						music_lyt.setBackgroundColor(context.getResources().getColor(R.color.transparent));			
						break;
					}
				}
			};
			return  buttons_touch1;
	}
	
}
