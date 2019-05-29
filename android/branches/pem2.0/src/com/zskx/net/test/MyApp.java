package com.zskx.net.test;

import com.zskx.pemsystem.util.SDCardUtil;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;

public class MyApp extends Application {

	private static  MyApp ma;
	
	public static String currentPage = "currentPage";
	
	public static String otherPage = "otherPage";
	
	public void createBitmapForBook(){
		DisplayMetrics dm = getResources().getDisplayMetrics();
		Bitmap currentPage = Bitmap.createBitmap(dm.widthPixels, dm.heightPixels,
				Bitmap.Config.ARGB_8888);
		Bitmap otherPage = Bitmap.createBitmap(dm.widthPixels, dm.heightPixels,
				Bitmap.Config.ARGB_8888);
		SDCardUtil.cacheBitmap(currentPage, MyApp.currentPage);
		SDCardUtil.cacheBitmap(otherPage, MyApp.otherPage);
		if(currentPage != null && currentPage.isRecycled()){
			currentPage.recycle();
		}
		if(otherPage != null && otherPage.isRecycled()){
			otherPage.recycle();
		}
	}
	
	@Override
	public void onCreate() {
		ma = this;
//		createBitmapForBook();
		super.onCreate();
	}

	public static MyApp getInstance(){
		return ma;
	}
	
	public  Context getApplicationContext(){
		return this.getApplicationContext();
	}
}
