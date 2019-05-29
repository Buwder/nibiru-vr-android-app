package com.zskx.pemsystem;

import com.zskx.net.request.LoginRequest;
import com.zskx.net.request.AbstractRequest.GetResponseListener;
import com.zskx.net.response.ResponseEntity;
import com.zskx.net.response.user.User;
import com.zskx.pemsystem.util.AppConfiguration;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private Button loginButton;// 登录按钮
	private EditText accountEdite;// 账户
	private EditText passEdit;// 密码
	private CheckBox savePassCheck;// 是否保存密码
	private LinearLayout loadingView;// 登录进度条

	private final static String ACCCOUNT = "account";
	private final static String PASSWORD = "password";
	private final static String IS_CHECKED = "checked";
	private final static String SAVA_ACCOUNT_INFO = "isSave";

	private final static int SHOW_PROGRESS = 1;// 显示进度条
	private final static int REMOVE_PROGRESS = 2;// 去掉进度条
	private final static int LOGIN_SUCCESS=3;
	private final static int LOGIN_FAILED=4;
	
	private LoginRequest request=null;

	private boolean isLoading = false;// 判断是否正在登录，若为 true , 则表示正在登录 , 登录建此时点击无效.

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		initComponent();
		initDataAndEvent();

	}

	/**
	 * 初始化组件
	 */
	private void initComponent() {
		accountEdite = (EditText) findViewById(R.id.editText_account);
		passEdit = (EditText) findViewById(R.id.editText_password);
		savePassCheck = (CheckBox) findViewById(R.id.checkBox_remenber_password);
		loginButton = (Button) findViewById(R.id.btn_login);

		loadingView = (LinearLayout) findViewById(R.id.loading_view);

	}

	/**
	 * 初始化组件数据和事件
	 */
	private void initDataAndEvent() {
		SharedPreferences spf = getSharedPreferences(ACCCOUNT,
				Activity.MODE_PRIVATE);
		boolean isSaved = spf.getBoolean(SAVA_ACCOUNT_INFO, false);
		if (isSaved) {
			accountEdite.setText(spf.getString(ACCCOUNT, ""));
			passEdit.setText(spf.getString(PASSWORD, ""));
		}

		savePassCheck.setChecked(spf.getBoolean(IS_CHECKED, true));// 第一次登录默认选中
		loginButton.setOnClickListener(listener);
	}

	/**
	 * 保存密码和帐号
	 * 
	 * @param account
	 * @param password
	 */
	private void saveAccountAndPassword(String account, String password,
			boolean isSave) {
		SharedPreferences spf = getSharedPreferences(ACCCOUNT,
				Activity.MODE_PRIVATE);
		Editor editor = spf.edit();
		editor.putString(ACCCOUNT, account);
		editor.putString(PASSWORD, password);
		editor.putBoolean(SAVA_ACCOUNT_INFO, isSave);
		editor.putBoolean(IS_CHECKED, isSave);
		editor.commit();
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == SHOW_PROGRESS) {
				loadingView.setVisibility(View.VISIBLE);

			} else if (msg.what == REMOVE_PROGRESS) {
				loadingView.setVisibility(View.GONE);
			}
			
			if(msg.arg1==LOGIN_SUCCESS)
			{
				 boolean isSave = savePassCheck.isChecked();
				 if (isSave) {
				 saveAccountAndPassword(account, password, isSave);
				 } else {
				 saveAccountAndPassword("", "", isSave);
				 }
				 startActivity(new Intent().setClass(LoginActivity.this,
				 HomeActivity.class));
				 LoginActivity.this.finish();
			}else if(msg.arg1==LOGIN_FAILED)
			{
				Toast.makeText(LoginActivity.this, (String)msg.obj, Toast.LENGTH_LONG).show();
			}

		};
	};

	
	String account="";
	String password="";
	// 登录事件
	private Button.OnClickListener listener = new Button.OnClickListener() {

		@SuppressLint("ShowToast")
		@Override
		public void onClick(View v) {

			 account = accountEdite.getText().toString().trim();
			 password = passEdit.getText().toString().trim();
			if (!"".equals(account) && !"".equals(password)) {

				if (!isLoading) {

					isLoading = true;
					loadingView.setVisibility(View.VISIBLE);

//					Thread thread = new LoginThread();
//
//					thread.start();
					
				 request = new LoginRequest(
							new GetResponseListener<User>() {

								@Override
								public void onSuccess(ResponseEntity<User> result) {
									//System.out.println("访问网络返回数据！");
									isLoading=false;
									AppConfiguration.user=result.getContent().get(0);
									System.out.println("wqp:--->"+AppConfiguration.user);
									Message msgRemove = new Message();
									msgRemove.what = REMOVE_PROGRESS;
									msgRemove.arg1=LOGIN_SUCCESS;
									handler.sendMessage(msgRemove);
									
								}

								@Override
								public void onError(ResponseEntity<User> result) {
								//	System.out.println("访问网络失败！");
									isLoading=false;
									Message msgRemove = new Message();
									msgRemove.what = REMOVE_PROGRESS;
									msgRemove.arg1=LOGIN_FAILED;
									msgRemove.obj=result.getMsg();
									handler.sendMessage(msgRemove);
								
								}
							}, account,password);
					request.sendByThread();

				}
			}else
			{
				 Toast.makeText(LoginActivity.this,
				 R.string.acc_or_pass_is_emp_warning,
				 Toast.LENGTH_SHORT).show();
			}

		}
	};
//
//	class LoginThread extends Thread {
//
//		@Override
//		public void run() {
//
//			Message msgShow = new Message();
//			msgShow.what = SHOW_PROGRESS;
//			handler.sendMessage(msgShow);
//
//			try {
//				sleep(5 * 1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			Message msgRemove = new Message();
//			msgRemove.what = REMOVE_PROGRESS;
//			msgRemove.arg1=LOGIN_SUCCESS;
//			handler.sendMessage(msgRemove);
//			isLoading = false;
//			System.out.println("Thread  end:--->"+handler);
//
//		}
//
//	}

  
	@Override
	protected void onStop() {
		
		if(request!=null)
		{
			request.cancel();
		}
		super.onStop();
	}

}
