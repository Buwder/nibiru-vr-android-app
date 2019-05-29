package com.zskx.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import com.zskx.controller.ActivityHolder;
/**
 * 退出对话框
 * @author demo
 *
 */
public class ExitDialog{

	
	
	private ExitDialog() {
	}

	/**
	 * 初始化对话框
	 */
	private static AlertDialog.Builder initDialog(final Context context){
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("提示信息");
		builder.setMessage("确定要退出？");
		builder.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ActivityHolder.getIntance().closeActivitys();
				if(context instanceof Activity){
					((Activity)context).finish();
				}
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		return builder;
	}
	/**
	 * 显示退出对话框
	 * @param context
	 */
	public static void show(Context context){
		initDialog(context).create().show();
	}
	
}
