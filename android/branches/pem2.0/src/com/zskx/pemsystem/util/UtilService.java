package com.zskx.pemsystem.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class UtilService {
public static Toast toast;
	/**
	 * 弹出一个toast
	 * @param context
	 * @param tip 显示的信息
	 */
	public static void show(Context context,String tip){
		if(toast!=null)
			toast.cancel();
		toast = Toast.makeText(context, tip, Toast.LENGTH_SHORT);
		toast.show();
	}
	
	/**
	 * 弹出一个toast
	 * @param context
	 * @param tip 显示的信息
	 * @param time 显示时间
	 */
	public static void show(Context context,String tip , int time){
		if(toast!=null)
			toast.cancel();
		toast = Toast.makeText(context, tip, time);
		toast.show();
	}
	
	/**
	 * 得到SD路径
	 * @param context
	 * @return
	 */
	private static String path=null;
	public static String getSdcardPath(Context context){
		if(path==null){
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				path = Environment.getExternalStorageDirectory().getPath();
			}else {
				UtilService.show(context,  "没有检测到SD卡");
			}
		}
		Log.i("sdcard", "sd卡的路径为："+path);
		return path;
	}
}
