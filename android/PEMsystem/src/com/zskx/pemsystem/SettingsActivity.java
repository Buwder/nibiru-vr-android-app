package com.zskx.pemsystem;

import com.zskx.pemsystem.util.ScreenManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends MenuActivity {

	private Button backButton; // 返回
	private Button modifyPassButton;// 修改密码
	private Button modifyInfoButton;// 修改资料

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ScreenManager.getScreenManager().pushActivity(this);
		setContentView(R.layout.settings);
		initComponent();
	}

	/**
	 * 初始化组件
	 */
	private void initComponent() {
		
		backButton = (Button) findViewById(R.id.btn_back);
		backButton.setOnClickListener(clickListener);
		
		modifyPassButton = (Button) findViewById(R.id.btn_modify_password);
		modifyPassButton.setOnClickListener(clickListener);
		
		modifyInfoButton = (Button) findViewById(R.id.btn_modify_info);
		modifyInfoButton.setOnClickListener(clickListener);
	}

	/*
	 * 点击事件
	 */
	private View.OnClickListener clickListener = new View.OnClickListener() {

		Intent intent;
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_back:
				SettingsActivity.this.finish();

				break;
			case R.id.btn_modify_password:
				
				 intent=new Intent(SettingsActivity.this,ModifyPasswordActivity.class);
				startActivity(intent);

				break;
			case R.id.btn_modify_info:
				intent=new Intent(SettingsActivity.this,ModifyInfoActivity.class);
				startActivity(intent);

				break;

			default:
				break;
			}

		}
	};

}
