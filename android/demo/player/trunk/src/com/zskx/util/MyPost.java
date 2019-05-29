package com.zskx.util;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
/**
 * 将需要进行UI修改到线程post去主线程
 * @author demo
 *
 */
public class MyPost {

	/**
	 * 将线程提交到主线程栈中
	 * @param run
	 */
	public static void post(Runnable run){
		if(Looper.getMainLooper()==Looper.myLooper()){
			Log.i("MyPost", "这个线程已经在主UI线程，不需要post进UI线程！");
		}else {
			Handler handler = new Handler(Looper.getMainLooper());
			handler.post(run);
		}
	}
	/**
	 * 经过delayMillis的时间再将线程提交到主UI线程
	 * @param run
	 * @param delayMillis
	 */
	public static void postDelay(Runnable run,long delayMillis){
		if(Looper.getMainLooper()==Looper.myLooper()){
			Log.i("MyPost", "这个线程已经在主UI线程，不需要post进UI线程！");
		}else {
			Handler handler = new Handler(Looper.getMainLooper());
			handler.postDelayed(run, delayMillis);
		}
	}
}
