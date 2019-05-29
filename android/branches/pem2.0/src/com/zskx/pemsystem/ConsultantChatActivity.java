package com.zskx.pemsystem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.zskx.net.response.ConsultantEntity;
import com.zskx.pemsystem.adpater.ConsultantChatAdapter;
import com.zskx.pemsystem.util.ScreenManager;
import com.zskx.testreport.ChatMsgEntity;

public class ConsultantChatActivity extends MenuActivity {
	private static String TAG = "ConsultantChatActivity";
	private static boolean MSG_CLIENT = false;
	private static boolean MSG_CONSULTANT = true;
	
	private Button btn_desription;
	private TextView txt_name;
	private ListView listView_msg;
	private EditText editText_send;
	private Button btn_send;
	
	private Intent intent;
	private ConsultantEntity consultant;
	private ConsultantEntity client; //资讯者实例没有创建
	private ArrayList<ChatMsgEntity> msg_list = new ArrayList<ChatMsgEntity>();
	private ConsultantChatAdapter chatAdapter ;
	
	private Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ScreenManager.getScreenManager().pushActivity(this);
		setContentView(R.layout.consult_chat_activity);
		
		intent = getIntent();
		consultant = (ConsultantEntity) intent.getSerializableExtra(ConsultActivity.ENTITY);
		
		initViews();

	}
	private void initViews() {
		btn_desription = (Button) findViewById(R.id.chat_despription);
		btn_send = (Button) findViewById(R.id.chat_btn_send);
		txt_name = (TextView) findViewById(R.id.chat_name);
		listView_msg = (ListView) findViewById(R.id.chat_list_msg);
		editText_send = (EditText) findViewById(R.id.chat_txt_msg);
		
		handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case ConsultantChatAdapter.ConsultantChatAdapter_IMG:
//显示名片
					break;
				}
			}};
		
		btn_send.setOnClickListener(btn_click);
		btn_desription.setOnClickListener(btn_click);
		txt_name.setText(consultant.getConsultantName());
		chatAdapter = new ConsultantChatAdapter(this, msg_list, null, handler);
		listView_msg.setAdapter(chatAdapter);
	}
	
	View.OnClickListener btn_click = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.chat_btn_send:
				sendMsg();
				break;
			case R.id.chat_despription:
				
				break;
			}
		}
	};
	private void sendMsg() {
		String msg = editText_send.getText().toString();
		ChatMsgEntity msg_entity = new ChatMsgEntity();
		msg_entity.setDate(getDate());
		msg_entity.setMsgType(MSG_CLIENT);
		msg_entity.setMessage(msg);
		msg_entity.setName(client.getConsultantName());
		msg_list.add(msg_entity);
		chatAdapter.notifyDataSetChanged();
		//send msg
	}
	/** 
     * 发送消息时，获取当前事件 
     *  
     * @return 当前时间 
     */  
    private String getDate() {  
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
        return format.format(new Date());  
    }  
    
    class ChatClient{
    	public Socket s;
            public boolean clientConnect(Object obj){
            	try{s=new Socket();  
                try{  
                    s.connect(new InetSocketAddress("10.0.2.2",5469),2000);  
                }catch(SocketTimeoutException e){  
                    //连接服务器超时  
                	return false;
                }  
                ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());  
                oos.writeObject(obj);  
                ObjectInputStream ois=new ObjectInputStream(s.getInputStream());  
                ChatMsgEntity ms=(ChatMsgEntity)ois.readObject();  
            	}catch (IOException e) {  
                    e.printStackTrace();  
                } catch (ClassNotFoundException e) {  
                    e.printStackTrace();  
                } 
            	return true;
            }
    }
}
	
