package com.zskx.pemsystem.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class MusicDialogButton extends Button {
	private Longtouch longtouch;
	private Runnable runnable;
	private int delay = 500;
	private int period = 100;
	private MusicDialogButton button;
	
	public MusicDialogButton(Context context) {
		super(context);
		newRunnable();
		
	}

	public MusicDialogButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		newRunnable();
	}

	public MusicDialogButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		newRunnable();
	}
	/**
	 * 开启线程执行长按接口
	 */
	private void newRunnable() {
		button= this;
		runnable = new Runnable() {
			
			@Override
			public void run() {
				if(longtouch != null) longtouch.do_in_long(button);
				postDelayed(runnable, period); //每0.1秒执行一次
			}
		};
	}
	/**
	 * 设置长按接口方法
	 * @param longtouch1
	 */
	public void set_long_click(Longtouch longtouch1){
		longtouch = longtouch1;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			postDelayed(runnable, delay);//按下之后0.5秒之后启动接口方法
			break;
		case MotionEvent.ACTION_UP:
			removeCallbacks(runnable); //松开后移出线程
			break;
		}
		
		return super.dispatchTouchEvent(event);
	}
	/**
	 * 长按接口
	 * @author guokai
	 *
	 */
	public interface Longtouch{
		public void do_in_long(View v);

	}
}
