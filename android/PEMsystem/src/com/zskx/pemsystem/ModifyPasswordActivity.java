package com.zskx.pemsystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.zskx.net.request.AbstractRequest.GetResponseListener;
import com.zskx.net.request.ModifyPasswordRequest;
import com.zskx.net.response.ResponseEntity;
import com.zskx.pemsystem.util.AppConfiguration;
import com.zskx.pemsystem.util.ScreenManager;

public class ModifyPasswordActivity extends MenuActivity{
	
	private Button backButton; //返回按钮
	private Button savePassButton;//保存新密码按钮
	
	private ProgressBar progressBar; //提交进度条
	
	private EditText editOldPassword; //原始密码
	private EditText editNewPassword; //新密码
	private EditText editRenewPassword;// 确认新密码
	
	private String oldPassword="";
	private String newPassword="";
	private String renewPassword="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		ScreenManager.getScreenManager().pushActivity(this);
		setContentView(R.layout.modify_password);
		initComponent();
		initDataOrEvent();
	}

	/**
	 * 出始化组件
	 */
	private void initComponent()
	{
		backButton=(Button)findViewById(R.id.btn_back);
		savePassButton=(Button)findViewById(R.id.btn_sava_password);
		
		progressBar=(ProgressBar)findViewById(R.id.load_progressBar);
		
		editOldPassword=(EditText)findViewById(R.id.editText_original_password);
		editNewPassword=(EditText)findViewById(R.id.editText_new_password);
		editRenewPassword=(EditText)findViewById(R.id.editText_new_password_affirm);
		
	}
	
	/**
	 * 初始化数据和事件
	 */
	private void initDataOrEvent()
	{
		backButton.setOnClickListener(clickListener);
		savePassButton.setOnClickListener(clickListener);
	}
	
	// Button 点击事件
	private Button.OnClickListener clickListener=new Button.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_back:
				ModifyPasswordActivity.this.finish();
				break;
				
			case R.id.btn_sava_password:
				
				oldPassword=editOldPassword.getText().toString().trim();
				newPassword=editNewPassword.getText().toString().trim();
				renewPassword=editRenewPassword.getText().toString().trim();
				
				if("".equals(oldPassword)||null==oldPassword)
				{
					tipDialog(getResources().getString(R.string.old_password_empty));
					return;
				}
				
//				if(oldPassword.equals(AppConfiguration.user.getUserPwd()))
//				{
//					tipDialog(getResources().getString(R.string.old_password_not_right));
//					return;
//				}
				
				
				if("".equals(newPassword)||null==newPassword)
				{
					tipDialog(getResources().getString(R.string.new_password_empty));
					return;
				}
				
				if("".equals(renewPassword)||null==renewPassword)
				{
					tipDialog(getResources().getString(R.string.renew_password_empty));
					return;
				}
				
				if(!newPassword.equals(renewPassword))
				{
					tipDialog(getResources().getString(R.string.new_renew_not_equal));
					return;
				}
				
				savaNewPassword();
				break;

			default:
				break;
			}
			
		}
	};
	
	
	
	// 处理网络返回结果
	private Handler hander= new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			
			progressBar.setVisibility(View.GONE);
			savePassButton.setVisibility(View.VISIBLE);
			if(msg.arg1==ACCESS_NET_OK)
			{
				Toast.makeText(ModifyPasswordActivity.this, R.string.save_pass_network_ok, Toast.LENGTH_LONG).show();
				
			}else if(msg.arg1==ACCESS_NET_FAILED)
			{
				Toast.makeText(ModifyPasswordActivity.this, R.string.modify_pass_network_failed, Toast.LENGTH_LONG).show();
			}
			
			
		};
	};
	
	private static final int ACCESS_NET_OK = 1;// 访问网络成功
	private static final int ACCESS_NET_FAILED = -1;// 访问网络失败
	//网络访问监听器
	private GetResponseListener<String> responseListener=new GetResponseListener<String>() {

		@Override
		public void onSuccess(ResponseEntity<String> result) {
	
			Message msg=new Message();
		    msg.obj= result.getMsg();
		    msg.arg1=ACCESS_NET_OK;
		    hander.sendMessage(msg);
			
		}

		@Override
		public void onError(ResponseEntity<String> result) {
			
			Message msg=new Message();
		    msg.obj= result.getMsg();
		    msg.arg1=ACCESS_NET_FAILED;
		    hander.sendMessage(msg);
			
		}
		
		
	};
	
	private ModifyPasswordRequest request; //网络访问器
	
	/**
	 * 保存新密码
	 */
	private void savaNewPassword()
	{
		progressBar.setVisibility(View.VISIBLE);
		savePassButton.setVisibility(View.GONE);
		request=new ModifyPasswordRequest(responseListener, null, oldPassword, newPassword);
		request.sendByThread();
	}
	
	/**
	 * 提示窗
	 * @param msg
	 */
	private void tipDialog(String msg)
	{
		
		AlertDialog.Builder builder=new AlertDialog.Builder(ModifyPasswordActivity.this);
		builder.setMessage(msg);
		builder.setPositiveButton(R.string.affirmed, null);
		builder.create().show();
	}
	
	@Override
	protected void onStop() {
		if(request!=null)
		{
			request.cancel();
		}
		super.onStop();
	}
}
