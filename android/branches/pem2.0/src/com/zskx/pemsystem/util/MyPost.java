package com.zskx.pemsystem.util;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

public class MyPost {

	private static String TAG = "MyPost";
	
	/**
	 * push thread into MainThread for UI update;
	 * @param run
	 */
	public static void post(Runnable run){
		
		Looper mLooper = Looper.myLooper();
		
		Looper looper = Looper.getMainLooper();
		
		if(mLooper == looper){
			Log.i(TAG, "this looper is in the MainLooper");
			run.run();
		}else {
			Handler handler = new Handler(Looper.getMainLooper());
			handler.post(run);
		}
	}
	
	/**
	 * push thread into MainThread for UI update;
	 * @param run
	 */
	public static void post(Runnable run,long delay){
		
		Looper mLooper = Looper.myLooper();
		
		Looper looper = Looper.getMainLooper();
		
		if(mLooper == looper){
			Log.i(TAG, "this looper is in the MainLooper");
			try {
				run.wait(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			run.run();
		}else {
			Handler handler = new Handler(Looper.getMainLooper());
			handler.postDelayed(run, delay);
		}
		
	}
	
}
