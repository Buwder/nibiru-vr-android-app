package com.zskx.pemsystem;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.zskx.net.request.AbstractRequest.GetResponseListener;
import com.zskx.net.request.GetAllTestTypeRequest;
import com.zskx.net.request.GetConsultantListRequest;
import com.zskx.net.response.ConsultantEntity;
import com.zskx.net.response.ResponseEntity;
import com.zskx.net.response.TestTypeEntity;
import com.zskx.pemsystem.adpater.ConsultantAdapter;
import com.zskx.pemsystem.util.AppConfiguration;
import com.zskx.pemsystem.util.MyPost;
import com.zskx.pemsystem.util.ScreenManager;
import com.zskx.pemsystem.util.ShowNotification;
import com.zskx.pemsystem.util.UtilService;

public class ConsultActivity extends MenuActivity {

	private static String TAG = "ConsultActivity";
	static String ENTITY = "CONSULTANT";
	private ListView listView;
	private View server_progressBar;
	private TextView textViewNoData;
	private TextView textFail;
	private Button btn_flash;
	
	private ArrayList<ConsultantEntity> arrayList = new ArrayList<ConsultantEntity>();
	
	private boolean isResponsed = false;
	private GetConsultantListRequest consultantListRequest;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ScreenManager.getScreenManager().pushActivity(this);
		setContentView(R.layout.consultant_list);
		
		initViews();
	}
	
	@Override
	protected void onStart() {
		ShowNotification.showNotification(this, "PEM", getResources().getString(R.string.index_psycho_consult), PsychoTestActivity.class, null);
		getServerResponse();
		super.onStart();
	}
	
	@Override
	protected void onDestroy() {
		if (this.consultantListRequest != null)
			this.consultantListRequest.cancel();
		super.onDestroy();
	}

	private void initViews() {
		listView = (ListView) findViewById(R.id.consult_list);
		server_progressBar = findViewById(R.id.consultant_progressbar);
		server_progressBar.setVisibility(View.INVISIBLE);
		textViewNoData = (TextView) findViewById(R.id.consultant_nodata);
		textViewNoData.setVisibility(View.INVISIBLE);
		textFail = (TextView) findViewById(R.id.consultant_fail);
		textFail.setVisibility(View.INVISIBLE);
		btn_flash = (Button) findViewById(R.id.consult_btn_flash);
		btn_flash.setOnClickListener(btn_clickListener);
	}
	
	private void getServerResponse() {

		if ((AppConfiguration.getUser(this) == null)|| (AppConfiguration.getUser(this).getSessionId() == null)) {
			UtilService.show(this,getResources().getString(R.string.server_overtime));
			return;
		}else {
			textViewNoData.setVisibility(View.GONE);
			consultantListRequest = new GetConsultantListRequest(new GetResponseListener<ConsultantEntity>() {

				@Override
				public void onSuccess(ResponseEntity<ConsultantEntity> result) {
					arrayList = (ArrayList<ConsultantEntity>) result.getContent();
//通过user的帐号返回资讯者信息					
					
					isResponsed = true;
					MyPost.post(new Runnable() {
						public void run() {
							server_progressBar.setVisibility(View.GONE);
							afterResponse();
						}
					});
				}

				@Override
				public void onError(final ResponseEntity<ConsultantEntity> result) {
					MyPost.post(new Runnable() {
						public void run() {
							server_progressBar.setVisibility(View.GONE);
							UtilService.show(ConsultActivity.this, result.getMsg(), 3000);
							textFail.setVisibility(View.VISIBLE);
						}
					});
				}
			}, AppConfiguration.getUser(this).getSessionId(), 0, 1);
					
					
			
			consultantListRequest.sendByThread();
			if (!isResponsed) server_progressBar.setVisibility(View.VISIBLE);
					
					
		}
	
	}
	
	private void afterResponse() {
		Log.i(TAG, "typeList::" + arrayList.size());
		if (arrayList.size() == 0)
			this.textViewNoData.setVisibility(View.VISIBLE);
		else {
			this.listView.setAdapter(new ConsultantAdapter(arrayList, this));
			this.textViewNoData.setVisibility(View.GONE);
			listView.setOnItemClickListener(new OnItemClickListener() {
	
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if(position < arrayList.size()){
						Intent intent = new Intent(ConsultActivity.this, ConsultantChatActivity.class);
						intent.putExtra(ENTITY, arrayList.get(position));
						startActivity(intent);
					}
				}
			});
		}
	}
	
	private View.OnClickListener btn_clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.consult_btn_flash:
				getServerResponse();
				break;
			}
		}
	};
}
