package com.zskx.pemsystem;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import com.zskx.pemsystem.util.BackButtonSet;
import com.zskx.pemsystem.util.ImageLoader;
/**
 * 因为activity的生命周期在启动下一个activity时(this表示当前activity,next表示下一个需要启动的activity)：this.onPause->next.oncreate->next.onstart->next.onresume->this.onStop->this.onDestory
 * 关闭当前activity回到上一个activity(this表示当前activity,pre表示上一个需要启动的activity)：this.onPause->pre.onrestart->pre.onstart->pre.onresume->this.onstop->this.ondestroy
 * 所以在onDestroy里recycle Bitmap，上一个activity先引用了没有回收的bitmap，但是onDestroy才被调用了，故在对当前的activity操作就会出现使用被回收的bitmap的错误
 * 又由于返回上一个activity只是单纯得把压入栈里的activity实例拿出来(这个表达的是：一个listView经过onCreat方法画好，进入onstop状态，等待它回来时不会重新去adapter里getView方法重绘，直接再内存块里拿出之前的数据画。)
 * 
 * @author demo
 *
 */
public class BaseActivity extends Activity {

	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		ImageLoader.clearCache(this.getClass().getSimpleName()); //clear all the image cache
		Log.d("BaseActivity", this.getClass().getSimpleName()+" onDestroy，clear the imageCache");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("BaseActivity", this.getClass().getSimpleName()+" onCreate");
		BackButtonSet.setButton(this);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Log.d("BaseActivity", this.getClass().getSimpleName()+" onStart");
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d("BaseActivity", this.getClass().getSimpleName()+" onRestart");
	}
	@Override
	protected void onResume() {
		super.onResume();
		Log.d("BaseActivity", this.getClass().getSimpleName()+" onResume");
		
		/** * 设置为横屏 */
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		Log.d("BaseActivity", this.getClass().getSimpleName()+" onPause");
	}
	@Override
	protected void onStop() {
		super.onStop();
		Log.d("BaseActivity", this.getClass().getSimpleName()+" onStop");
	}
}
