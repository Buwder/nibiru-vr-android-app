package com.zskx.pemsystem;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zskx.net.NetConfiguration;
import com.zskx.net.request.AbstractRequest.GetResponseListener;
import com.zskx.net.request.LoginRequest;
import com.zskx.net.response.Host;
import com.zskx.net.response.ResponseEntity;
import com.zskx.net.response.user.User;
import com.zskx.pemsystem.util.AppConfiguration;
import com.zskx.pemsystem.util.NetUtil;

/**
 * 404 用户不存在
 * 405 密码错误
 * 500 过期
 * 501 新建用户未开始
 * 200 成功
 * @author wqp
 *
 */
public class LoginActivity extends Activity {
	
	private AlertDialog IPdialog;
	private EditText editText_ip_config;
	private EditText editText_download_ip;
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case 1:
			init_dialog();
			IPdialog.show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	void init_dialog(){
		IPdialog = new AlertDialog.Builder(this).create();
		LinearLayout ll = (LinearLayout) View.inflate(this, R.layout.login_ip, null);
		editText_ip_config = (EditText) ll.findViewById(R.id.login_ip_edit);
		editText_download_ip = (EditText) ll.findViewById(R.id.login_download_edit);
		String _ip = "mobile.pems.cn";
		String _download = "http://img.pems.cn/";
		
		editText_ip_config.setText(_ip);
		editText_download_ip.setText(_download);
		
		IPdialog.setView(ll);
		IPdialog.setTitle("ip设置");
		IPdialog.setButton("确定", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				String ip_address = editText_ip_config.getText().toString();
				String download_ip = editText_download_ip.getText().toString();
				
				if (ip_address !=null && !"".equals(ip_address)){
					Host host=new Host(ip_address, "");
					hostList.add(0,host);
					System.out.println("ip_address--" + ip_address);
				} 
				
				if (download_ip !=null && !"".equals(download_ip)){
					NetConfiguration.setDOWNLOAD_ADD(download_ip);
					NetConfiguration.IMAGE_DOWNLOAD_ADD = NetConfiguration.DOWNLOAD_ADD + "mobile/";
					System.out.println("download_ip--" + NetConfiguration.IMAGE_DOWNLOAD_ADD);
					System.out.println("download_ip--" + NetConfiguration.DOWNLOAD_ADD);
				}
				
			}});
		IPdialog.setButton2("cancle", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				IPdialog.dismiss();
			}
		});
	}
	
	@Override
	public boolean onCreatePanelMenu(int featureId, Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreatePanelMenu(featureId, menu);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(1, 1, 1, "ip设置");
		return super.onCreateOptionsMenu(menu);
	}



	private Button loginButton;// 登录按钮
	private EditText accountEdite;// 账户
	private EditText passEdit;// 密码
	private CheckBox savePassCheck;// 是否保存密码
	private LinearLayout loadingView;// 登录进度条

	private final static String ACCCOUNT = "account";
	private final static String PASSWORD = "password";
	private final static String IS_CHECKED = "checked";
	private final static String SAVA_ACCOUNT_INFO = "isSave";
	
	
	private final static String USER_HOST="user_host";
	private final static String USER_CITY="city";
	private final static String USER_IP="ip";
	

	private final static int SHOW_PROGRESS = 1;// 显示进度条
	private final static int REMOVE_PROGRESS = 2;// 去掉进度条
	private final static int LOGIN_SUCCESS=0x003;
	private final static int LOGIN_FAILED=0x004;
	private final static int LOGIN_END=0x008;
	private final static int LOGINING=0x007;
	
	private List<Host> hostList; //服务器地址列表
	private int index_IP=0;
	private int totalHostCount=0;
	
	private boolean isReception=true;  //是否登录中退出
	


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
		
		//********************************
//		SharedPreferences hostSpf = getSharedPreferences(USER_HOST,
//				Activity.MODE_PRIVATE);
//		
//		String city=hostSpf.getString(USER_CITY, "");
//		String ip=hostSpf.getString(USER_IP, "");
		
		hostList=NetUtil.getHosts(this);
		
		System.out.println("hostList.size:--->"+hostList.size());
		//************************ 
//		if(!"".equals(city)&& !"".equals(ip))
//		{
//			Host host=new Host(ip, city); 
//			hostList.add(0,host);
//			System.out.println("hostList.size:---  add  --->"+hostList.size());
//		}
		
		totalHostCount=hostList.size();
		
		
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
	
	/**
	 * 保存用户有效IP
	 */
	private void savaUserHost(Host host)
	{
		SharedPreferences spf = getSharedPreferences(USER_HOST,
				Activity.MODE_PRIVATE);
		
		Editor editor=spf.edit();
		editor.putString(USER_CITY,host.getCity());
		editor.putString(USER_IP,host.getIp());
		editor.commit();
		
		
	}


	
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

					index_IP=0;
					isLoading = true;
					loadingView.setVisibility(View.VISIBLE);

					loginPME();

				}
			}else
			{
				 Toast.makeText(LoginActivity.this,
				 R.string.acc_or_pass_is_emp_warning,
				 Toast.LENGTH_SHORT).show();
			}

		}
	};
	
	
	
	
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
				 savaUserHost(hostList.get(index_IP-1));
				 startActivity(new Intent().setClass(LoginActivity.this,
				 HomeActivity.class));
				 LoginActivity.this.finish();
			}else if(msg.arg1==LOGINING&&isReception)
			{
				if(index_IP>=totalHostCount)
				{
					Toast.makeText(LoginActivity.this, (String)msg.obj, Toast.LENGTH_LONG).show();
				}else
				{
					loginPME();
				}
			
				
				
			}
			else if(msg.arg1==LOGIN_END&&isReception)
			{
				System.out.println("index_IP-1:---->"+(index_IP-1));
				savaUserHost(hostList.get(index_IP-1));
				Toast.makeText(LoginActivity.this, (String)msg.obj, Toast.LENGTH_LONG).show();
				
			}else if(msg.arg1==LOGIN_FAILED&&isReception)
			{
				Toast.makeText(LoginActivity.this, (String)msg.obj, Toast.LENGTH_LONG).show();
			}

		};
	};

	
	private GetResponseListener<User> loginListener =new GetResponseListener<User>() {
		
							@Override
							public void onSuccess(ResponseEntity<User> result) {
								System.out.println("wqp:----->访问网络返回数据！");
								isLoading=false;
								//AppConfiguration.user=result.getContent().get(0);
								
								AppConfiguration.saveUser(LoginActivity.this, result.getContent().get(0));
								User user=AppConfiguration.getUser(LoginActivity.this);
//AppConfiguration.saveHospitalList(this, result.getList());							
								System.out.println(user);
								
								
								System.out.println("wqp:--->"+AppConfiguration.getUser(LoginActivity.this));
								Message msgRemove = new Message();
								msgRemove.what = REMOVE_PROGRESS;
								msgRemove.arg1=LOGIN_SUCCESS;
								handler.sendMessage(msgRemove);
								
							}
		
							@Override
							public void onError(ResponseEntity<User> result) {
								
							
							
								String statusCode=result.getCode();
								System.out.println("code: = "+statusCode+" ,msg = "+result.getMsg());
																
							    if("404".equals(statusCode)&&index_IP>=totalHostCount)
								{
									
							    //index_IP=0;
								isLoading=false;
								Message msgRemove = new Message();
								msgRemove.what = REMOVE_PROGRESS;
								msgRemove.arg1=LOGIN_FAILED;
								msgRemove.obj=result.getMsg();
								handler.sendMessage(msgRemove);
								
								
							    }else if("404".equals(statusCode)&&index_IP<totalHostCount&&isReception)
							    {
							    	Message msgRemove = new Message();
									msgRemove.what = SHOW_PROGRESS;
									msgRemove.arg1=LOGINING;
									msgRemove.obj=result.getMsg();
									handler.sendMessage(msgRemove);
							    	
							    }
							    else if("001".equals(statusCode)&&index_IP<totalHostCount&&isReception)
							    {
							    	
							    	Message msgRemove = new Message();
									msgRemove.what = SHOW_PROGRESS;
									msgRemove.arg1=LOGINING;
									msgRemove.obj=result.getMsg();
									handler.sendMessage(msgRemove);
									
							    	
							    }else if("001".equals(statusCode)&&index_IP>=totalHostCount&&isReception)
							    {
							    	isLoading=false;
							    	Message msgRemove = new Message();
									msgRemove.what = REMOVE_PROGRESS;
									msgRemove.arg1=LOGIN_FAILED;
									msgRemove.obj=result.getMsg();
									handler.sendMessage(msgRemove);
									//index_IP=0;
							    	
							    }
							    
							    else
							    {
							    	isLoading=false;
									Message msgRemove = new Message();
									msgRemove.what = REMOVE_PROGRESS;
									msgRemove.arg1=LOGIN_END;
									msgRemove.obj=result.getMsg();
									handler.sendMessage(msgRemove);
								
							    }
							
							}

						
						};
	
	
	
	
	
	
	
	
 private LoginRequest request;
	

 /**
  * 登录轮循
  */
 private void loginPME()
 {
	 
	 if(index_IP<totalHostCount&&isReception)
	 {
		 Host host=hostList.get(index_IP);
		 request = new LoginRequest(loginListener, account,password);
		 
		 System.out.println("account:="+account+",password=:"+password+" ,ip = "+host.getIp()+", city = "+host.getCity());
		 request.setServerIp(host.getIp());
		 System.out.println("index_IP:--->"+index_IP);
		 index_IP+=1;
		 
		 request.sendByThread();
	 }
 }
 
 
 
 
 
 
 
 
	
	
	
	
	


  
	@Override
	protected void onStop() {
		
		isReception=false;
		if(request!=null)
		{
			request.cancel();
		}
		super.onStop();
	}

}
