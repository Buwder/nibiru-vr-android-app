package com.zskx.pemsystem;

import com.zskx.pemsystem.util.ScreenManager;
import com.zskx.pemsystem.util.ShowNotification;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends Activity{
	
	private TextView text_versionName;
	private Button btn_confirm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ScreenManager.getScreenManager().pushActivity(this);
		setContentView(R.layout.about);
		
		initComponent();
		initDataOrEvent();
		
		String versionNamePackage=getAppVersionName();
		
		Resources res=getResources();
		String strVersionName=String.format(res.getString(R.string.about_versionName), versionNamePackage);
		
		text_versionName.setText(strVersionName);
		
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		String contentStr=getResources().getString(R.string.about_title);
		ShowNotification.showNotification(this,"PEM",contentStr, AboutActivity.class,null);
	}
	
	
	/**
	 * 实例化组件
	 */
	private void initComponent()
	{
		text_versionName=(TextView)findViewById(R.id.text_versionName);
		btn_confirm=(Button)findViewById(R.id.btn_confirm);
		
	}
	
	/**
	 * button 点击事件
	 */
	private View.OnClickListener clickListener=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			AboutActivity.this.finish();
			
		}
	};
	
	/**
	 * 初始化数据或事件
	 */
	private void initDataOrEvent()
	{
		btn_confirm.setOnClickListener(clickListener);
	}
	
	
	
	/**
	 *  获得版本名
	 * @return
	 */
	private String getAppVersionName()
	{
		String versionName="1.0";
		PackageManager pm=getPackageManager();
		String packageName=getPackageName();
		
		try {
			PackageInfo info=pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
			versionName=info.versionName;
			
		} catch (NameNotFoundException e) {
			
		}
		
		return versionName;
	}

}
