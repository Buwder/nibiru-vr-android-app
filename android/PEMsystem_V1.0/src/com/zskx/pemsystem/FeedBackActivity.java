package com.zskx.pemsystem;




import com.zskx.net.request.AbstractRequest.GetResponseListener;
import com.zskx.net.request.UserFeedBackRequest;
import com.zskx.net.response.ResponseEntity;
import com.zskx.pemsystem.util.ScreenManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class FeedBackActivity extends MenuActivity{
	
	
	private Button backButton;   //返回按钮
	private Button sendInfoButton;  //发送按钮
	private ProgressBar progressBar;   //提交进度
	
	private EditText editTitle;  //标题 输入框
	private EditText editContent; //内容输入框
	
	private String title=""; //标题
	private String content=""; //内容
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		ScreenManager.getScreenManager().pushActivity(this);
		
		setContentView(R.layout.info_feedback);
		initComponent();
		initDataOrEvent();
	}

	/**
	 * 初始化组件
	 */
	private void initComponent()
	{
		
		backButton=(Button)findViewById(R.id.btn_back);
		sendInfoButton=(Button)findViewById(R.id.btn_send_info);
		
		progressBar=(ProgressBar)findViewById(R.id.load_progressBar);
		
		editTitle=(EditText)findViewById(R.id.editText_title);
		editContent=(EditText)findViewById(R.id.editText_content);
		
		
	}
	
	/**
	 * 初始化数据和事件
	 */
	
	private void initDataOrEvent()
	{
		backButton.setOnClickListener(clickListener);
		sendInfoButton.setOnClickListener(clickListener);
	}
	
	// Button  点击事件
	private Button.OnClickListener clickListener=new Button.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_back:
				
				FeedBackActivity.this.finish();
				
				break;
				
			case R.id.btn_send_info:
				
				String titleStr=editTitle.getText().toString().trim();
				String contentStr=editContent.getText().toString().trim();
				
				if(titleStr!=null)
				{
					title=titleStr;
				}
				
				if(contentStr!=null)
				{
					content=contentStr;
				}
				
				sendInfo();
				break;

			default:
				break;
			}
			
		}
	};
	
	
	
	// 访问结果处理
	private Handler handler=new Handler()
	{
		public void handleMessage(android.os.Message msg) {
		
			
			sendInfoButton.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			
			if(msg.arg1==ACCESS_NET_OK)
			{
				Toast.makeText(FeedBackActivity.this, R.string.feed_access_network_ok, Toast.LENGTH_LONG).show();
							
			}else if(msg.arg1==ACCESS_NET_FAILED)
			{
				Toast.makeText(FeedBackActivity.this, (String)msg.obj, Toast.LENGTH_LONG).show();
			}
		};
	};
	
	
	private UserFeedBackRequest request; //网络访问器
	
	private static final int ACCESS_NET_OK = 1;// 访问网络成功
	private static final int ACCESS_NET_FAILED = -1;// 访问网络失败
	
	// 网络访问监听器
	private GetResponseListener<String> responseListener =new GetResponseListener<String>() {

		@Override
		public void onSuccess(ResponseEntity<String> result) {
			
			Message msg=new Message();
			msg.arg1=ACCESS_NET_OK;
			msg.obj=result.getMsg();
			handler.sendMessage(msg);
			
			
		}

		@Override
		public void onError(ResponseEntity<String> result) {
			
			Message msg=new Message();
			msg.arg1=ACCESS_NET_FAILED;
			msg.obj=result.getMsg();
			handler.sendMessage(msg);
			
		}
	};
	
	
	
	/**
	 * 提交信息
	 */
	private void sendInfo()
	{
		sendInfoButton.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		request= new UserFeedBackRequest(responseListener,  title, content);
		request.sendByThread();
		
	}
	
	@Override
	protected void onStop() {


		if(request != null)
		{
			request.cancel();
		}
		super.onStop();
	}
	
}
