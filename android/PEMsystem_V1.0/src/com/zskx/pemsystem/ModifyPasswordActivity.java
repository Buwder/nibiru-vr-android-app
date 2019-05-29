package com.zskx.pemsystem;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zskx.net.NetConfiguration;
import com.zskx.net.request.AbstractRequest.GetResponseListener;
import com.zskx.net.request.ChangePasswordRequest;

import com.zskx.net.response.ResponseEntity;
import com.zskx.net.response.user.User;
import com.zskx.pemsystem.util.AppConfiguration;
import com.zskx.pemsystem.util.ScreenManager;
import com.zskx.pemsystem.util.ShowNotification;

public class ModifyPasswordActivity extends MenuActivity{
	
	private Button backButton; //返回按钮
	private Button savePassButton;//保存新密码按钮
	private Button homeButton;
	private Button cancelButton;
	
//	private ProgressBar progressBar; //提交进度条
	
	//private EditText editOldPassword; //原始密码
	private EditText editNewPassword; //新密码
	private EditText editRenewPassword;// 确认新密码
	
	//private String oldPassword="";
	private String newPassword="";
	private String renewPassword="";
	
	private AlertDialog dialog;
	
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
		
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		 
		 View view=LayoutInflater.from(this).inflate(R.layout.progress_dialog, null);
		 
		 builder.setView(view);
		 builder.setCancelable(false);
		 dialog= builder.create();
		 
		 Window window=dialog.getWindow();
		 WindowManager.LayoutParams p=window.getAttributes();
	     int width= p.width*3/4;
	     p.width=width;
	     window.setLayout(width, p.height);
	     
		backButton=(Button)findViewById(R.id.btn_back);
		savePassButton=(Button)findViewById(R.id.btn_save_password);
		homeButton=(Button)findViewById(R.id.home_btn);
		cancelButton=(Button)findViewById(R.id.btn_save_password_cancel);
		
		//progressBar=(ProgressBar)findViewById(R.id.load_progressBar);
		
		//editOldPassword=(EditText)findViewById(R.id.editText_original_password);
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
		homeButton.setOnClickListener(clickListener);
		cancelButton.setOnClickListener(clickListener);
	}
	
	// Button 点击事件
	private Button.OnClickListener clickListener=new Button.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_back:
			case R.id.btn_save_password_cancel:
				ModifyPasswordActivity.this.finish();
				break;
				
			case R.id.btn_save_password:
				
			//	oldPassword=editOldPassword.getText().toString().trim();
				newPassword=editNewPassword.getText().toString().trim();
				renewPassword=editRenewPassword.getText().toString().trim();
				
//				if("".equals(oldPassword)||null==oldPassword)
//				{
//					tipDialog(getResources().getString(R.string.old_password_empty));
//					return;
//				}
				
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
             case R.id.home_btn:
				
				Intent intent=new Intent(ModifyPasswordActivity.this,HomeActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
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
			
			
			dialog.dismiss();
			
			if(msg.arg1==ACCESS_NET_OK)
			{
				
				Toast.makeText(ModifyPasswordActivity.this, (String)msg.obj, Toast.LENGTH_LONG).show();
				ModifyPasswordActivity.this.finish();
				
			}else if(msg.arg1==ACCESS_NET_FAILED)
			{
				Toast.makeText(ModifyPasswordActivity.this, (String)msg.obj, Toast.LENGTH_LONG).show();
			}
			
			
		};
	};
	
	private static final int ACCESS_NET_OK = 1;// 访问网络成功
	private static final int ACCESS_NET_FAILED = -1;// 访问网络失败
	//网络访问监听器
	private GetResponseListener<User> responseListener=new GetResponseListener<User>() {

		@Override
		public void onSuccess(ResponseEntity<User> result) {
	
			User user=result.getContent().get(0);
			AppConfiguration.saveUser(ModifyPasswordActivity.this, user);
			Message msg=new Message();
		    msg.obj= result.getMsg();
		    msg.arg1=ACCESS_NET_OK;
		    hander.sendMessage(msg);
			
		}

		@Override
		public void onError(ResponseEntity<User> result) {
			
			Message msg=new Message();
		    msg.obj= result.getMsg();
		    msg.arg1=ACCESS_NET_FAILED;
		    hander.sendMessage(msg);
			
		}
		
		
	};
	
	private ChangePasswordRequest request; //网络访问器
	
	/**
	 * 保存新密码
	 */
	private void savaNewPassword()
	{
//		backButton.setVisibility(View.GONE);
//		progressBar.setVisibility(View.VISIBLE);
	  User user=AppConfiguration.getUser(this);
	  if(user !=null)
	  {
		dialog.show();
		request=new ChangePasswordRequest(responseListener, user.getSessionId(), newPassword, renewPassword, NetConfiguration.CLIENT_OS, "android_phone");
		request.sendByThread();
	  }
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		String contentStr=getResources().getString(R.string.modify_password);
		ShowNotification.showNotification(this,"PEM",contentStr, ModifyPasswordActivity.class,null);
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
