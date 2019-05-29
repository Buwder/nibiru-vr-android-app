package com.zskx.pemsystem.util;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.zskx.pemsystem.R;
/**
 * 返回键控制
 * @author guokai
 *
 */
public class BackButtonSet {
	private static BackButtonSet backButtonSet;
	
	private  BackButtonSet() {
	}
	
	public static BackButtonSet getInstence(){
		if(backButtonSet == null)  backButtonSet = new BackButtonSet();
		return backButtonSet;
	}
	
	public static void setButton(final Activity activity){
		Button backButton;
		backButton  = (Button) activity.findViewById(R.id.btn_back);
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				activity.finish();
			}
		});
	}
}
