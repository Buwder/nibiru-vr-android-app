package com.zskx.pemsystem;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ListView;
import android.widget.TextView;

import com.zskx.net.request.AbstractRequest.GetResponseListener;
import com.zskx.net.request.GetAllTestTypeRequest;
import com.zskx.net.response.ResponseEntity;
import com.zskx.net.response.TestTypeEntity;
import com.zskx.pemsystem.adpater.TestTypeAdapter;
import com.zskx.pemsystem.util.AppConfiguration;
import com.zskx.pemsystem.util.AsyncImageLoader;
import com.zskx.pemsystem.util.Common_Title;
import com.zskx.pemsystem.util.ListViewListener;
import com.zskx.pemsystem.util.MyPost;
import com.zskx.pemsystem.util.ScreenManager;
import com.zskx.pemsystem.util.ShowNotification;
import com.zskx.pemsystem.util.UtilService;
import com.zskx.pemsystem.util.ListViewListener.GetServerData;

public class PsychoTestActivity extends MenuActivity {
	private static int DEFAULT_COUNT = 30;
	public static String LIST = "list";
	public static int RESULT = 47894561;
	private static String TAG = "PsychoTestActivity";
	public static String TYPE_POS = "type_pos";
	private GetAllTestTypeRequest allTestTypeRequest;
	private int currentPage = 1;
	private Handler handler_status;
	private boolean isResponsed = false;
	private ListView listView;
	private View server_progressBar;
	private TextView textViewNoData;
	private Common_Title title;
	private int totalCount = 0;
	private int totalPageCount = 1;
	private ArrayList<TestTypeEntity> typeList;
	private ArrayList<TestTypeEntity> typeList_all = new ArrayList<TestTypeEntity>();
	private static AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
	@Override
	protected void onCreate(Bundle paramBundle) {
		Log.i(TAG, "onCreate");
		super.onCreate(paramBundle);
		ScreenManager.getScreenManager().pushActivity(this);
		setContentView(R.layout.test_activity);
		init();
	
	}

	@Override
	protected void onDestroy() {
		if (this.allTestTypeRequest != null)
			this.allTestTypeRequest.cancel();
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		ShowNotification.showNotification(this, "PEM", getResources().getString(R.string.title_test_report), PsychoTestActivity.class, null);
		getServerResponse();
		super.onStart();
	}

	private void afterResponse() {
		Log.i(TAG, "typeList::" + this.typeList_all.size());
		if (this.typeList_all.size() == 0)
			this.textViewNoData.setVisibility(View.VISIBLE);
		else {
			this.listView.setAdapter(new TestTypeAdapter(typeList_all, this, handler_status , asyncImageLoader));
			this.textViewNoData.setVisibility(View.GONE);
		}
	}

	private void getServerResponse() {
		if ((AppConfiguration.getUser(this) == null)|| (AppConfiguration.getUser(this).getSessionId() == null)) {
			UtilService.show(this,getResources().getString(R.string.server_overtime));
			return;
		}else {
			textViewNoData.setVisibility(View.GONE);
			allTestTypeRequest = new GetAllTestTypeRequest(new GetResponseListener<TestTypeEntity>() {

				@Override
				public void onSuccess(ResponseEntity<TestTypeEntity> result) {
					totalCount = result.getTotalCount();
					totalPageCount = (totalCount + DEFAULT_COUNT - 1) / DEFAULT_COUNT;
					typeList_all = ((ArrayList<TestTypeEntity>) result.getContent());
					typeList = typeList_all;
					typeList = get_tested_enable(typeList,"INIT");
					if(currentPage <= totalPageCount) currentPage++;
					isResponsed = true;
					MyPost.post(new Runnable() {
						public void run() {
							server_progressBar.setVisibility(View.GONE);
							afterResponse();
						}
					});
				}

				@Override
				public void onError(final ResponseEntity<TestTypeEntity> result) {
					MyPost.post(new Runnable() {
						public void run() {
							server_progressBar.setVisibility(View.GONE);
							UtilService.show(PsychoTestActivity.this, result.getMsg(), 3000);
//							show_fail(result.getMsg());
						}
					});
				}
			}, AppConfiguration.getUser(this).getSessionId(), this.currentPage, DEFAULT_COUNT);
			
			this.allTestTypeRequest.sendByThread();
			if (!isResponsed) server_progressBar.setVisibility(View.VISIBLE);
		}
	}

	private void init() {
		this.listView =   (ListView) findViewById(R.id.test_list);
		this.typeList = new ArrayList<TestTypeEntity>();
		this.typeList_all = new ArrayList<TestTypeEntity>();
		this.server_progressBar = findViewById(R.id.loading_progress);
		this.textViewNoData = ((TextView) findViewById(R.id.test_text_failure));
		this.title = ((Common_Title) findViewById(R.id.test_title));
		this.title.setTitleTxt(getResources().getString(R.string.test_title_list));
		listView.setClickable(false);
		handler_status = new Handler() {
			public void handleMessage(Message message) {
				TestTypeEntity testTypeEntity = (TestTypeEntity) typeList_all
						.get(message.arg1);
				switch (message.what) {
				case TestTypeAdapter.TestTypeAdapter_test:
					int pos = typeList.indexOf(testTypeEntity);
					Intent intent_test = new Intent(PsychoTestActivity.this,
							PsychoTestDoActivity.class);
					intent_test.putExtra(PsychoTestActivity.LIST,
							PsychoTestActivity.this.typeList);
					intent_test.putExtra(PsychoTestActivity.TYPE_POS, pos);
					PsychoTestActivity.this.startActivity(intent_test);
					break;
				case TestTypeAdapter.TestTypeAdapter_report:
					Intent intent_report = new Intent(PsychoTestActivity.this,
							ReportDetailsActivity.class);
					intent_report.putExtra("report_id",
							String.valueOf(testTypeEntity.getTestItemId()));
					PsychoTestActivity.this.startActivity(intent_report);
					break;
				}
			}
		};
		
		ListViewListener listViewListener = new ListViewListener(listView, new GetServerData(){

			@Override
			public void getData() {
				System.out.println("getData");
				if(currentPage <= totalPageCount) 
					getServerResponse(); 
			}});
		listViewListener.setListViewListeners();
	}
	/**
	 * 从list中挑选出特定的元素
	 * @param list
	 * @param string 量表的状态
	 * @return
	 */
	protected ArrayList<TestTypeEntity> get_tested_enable(
			ArrayList<? extends TestTypeEntity> list,String string) {
		ArrayList<TestTypeEntity> arrayList = new ArrayList<TestTypeEntity>();
		
		if (list != null && string != null) {
			for (TestTypeEntity testTypeEntity : list) {
				if (string.equals(testTypeEntity.getTestStatus())) {
					arrayList.add(testTypeEntity);
				}
			}
		}
		return arrayList;
	}
	
	 /**
	   * 显示失败界面
	   * @param id
	   */
	  private void show_fail(String str) {
	    String str1 = getResources().getString(R.string.test_failure);
	    textViewNoData.setText(String.format(str1, str));
	    textViewNoData.setVisibility(View.VISIBLE);
	  }

}

/*
 * Location:
 * /home/guokai/share/apktool-install-linux-r04-brut1/com.zskx.pemsystem
 * -1/classes_dex2jar.jar Qualified Name: com.zskx.pemsystem.PsychoTestActivity
 * JD-Core Version: 0.5.4
 */