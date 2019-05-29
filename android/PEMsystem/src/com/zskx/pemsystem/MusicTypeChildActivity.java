package com.zskx.pemsystem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import com.zskx.net.NetConfiguration;
import com.zskx.net.request.AbstractRequest.GetResponseListener;
import com.zskx.net.request.GetAllMusicByTypeRequest;
import com.zskx.net.response.MusicDetailEntity;
import com.zskx.net.response.ResponseEntity;
import com.zskx.pemsystem.adpater.MusicTypeChildAdapter;
import com.zskx.pemsystem.adpater.MusicTypeChildAdapter.MusicDetailHolder;
import com.zskx.pemsystem.util.AppConfiguration;
import com.zskx.pemsystem.util.BackButtonSet;
import com.zskx.pemsystem.util.Constant;
import com.zskx.pemsystem.util.ListViewListener;
import com.zskx.pemsystem.util.ListViewListener.GetServerData;
import com.zskx.pemsystem.util.MyPost;
import com.zskx.pemsystem.util.ScreenManager;
import com.zskx.pemsystem.util.UtilService;

/**
 * 
 * @author guokai
 *
 */
public class MusicTypeChildActivity extends Activity{
	
	public final static int RESULTCODE = 321;
	private static String TAG = "MusicTypeChildActivity";
	private String type;
	private String imageURL;
	private ListView mMusicChildListView;

	private View server_progressBar;	//连接服务器进度条
	private boolean isResponsed = false; //显示连接服务器进度条的标志
	
	private ArrayList<MusicDetailEntity> typeList = new ArrayList<MusicDetailEntity>();
	private final static int DEFAULT_COUNT = 10;// 每页默认每页数据条数
	private int totalPageCount = 1;//  第一次访问，必须设为 1
	private int currentPage = 0;
	private int totalCount = 0;
	
	private ArrayList<MusicDetailEntity> selectedList  = new ArrayList<MusicDetailEntity>();
	
	Button buttonSelect ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ScreenManager.getScreenManager().pushActivity(this);
		setContentView(R.layout.music_type_child_list);
		server_progressBar = this.findViewById(R.id.music_type_child_progressbar);
		mMusicChildListView=(ListView)findViewById(R.id.listView_item);
		
		Intent intent = getIntent();
		type = intent.getStringExtra(Music_Activity.TYPE);
		imageURL = intent.getStringExtra(Music_Activity.IMG_URL);
		
		getResponse();
		if(!isResponsed) server_progressBar.setVisibility(View.VISIBLE);
		
		BackButtonSet.setButton(this);
		
		buttonSelect = (Button) findViewById(R.id.music_button_accomplished);
		buttonSelect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				forResult();
				finish();
			}
		});
	}
	

	private void forResult() {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();   
		Set<Integer> keySet = itemAdapter.isSelected.keySet();
		for (Integer key : keySet) {
			selectedList.add(typeList.get(key));
		}
		addMusicURL(); //添加图像URL
		//bundle.putParcelableArrayList("result", (ArrayList<? extends Parcelable>) selectedList);
		intent.putExtra("result",  (Serializable)selectedList);
		this.setResult(RESULTCODE, intent);
Log.i(TAG, "selectedList:::" +  selectedList.size());
	}


	private boolean Debug = true;
	private void getResponse() {
		GetAllMusicByTypeRequest allMusicByTypeRequest = new GetAllMusicByTypeRequest(new GetResponseListener<MusicDetailEntity>(){

			@Override
			public void onSuccess(ResponseEntity<MusicDetailEntity> result) {
				totalCount = result.getTotalCount();
				totalPageCount = (totalCount + DEFAULT_COUNT - 1) / DEFAULT_COUNT;
				typeList.addAll(result.getContent());
				isResponsed = true;
				MyPost.post(new Runnable(){

					@Override
					public void run() {
						initComponent();
						server_progressBar.setVisibility(View.GONE);
					}});
				
			}

			@Override
			public void onError(final ResponseEntity<MusicDetailEntity> result) {
				isResponsed = true;
				MyPost.post(new Runnable(){

					@Override
					public void run() {
						server_progressBar.setVisibility(View.GONE);
						UtilService.show(MusicTypeChildActivity.this, result.getMsg());
					}});

			}}, AppConfiguration.user.getSessionId(), type, currentPage, DEFAULT_COUNT);
		allMusicByTypeRequest.sendByThread();
		Debug = allMusicByTypeRequest.getDebug();
	}

	/**
	 * 添加图像URL
	 */
	protected void addMusicURL() {
		if(selectedList != null && selectedList.size()>0){
			for (MusicDetailEntity music : selectedList) {
				music.setMusicImage(imageURL);
			}
		}
	}


	/**
	 * 初始化组件
	 * @param list 
	 */
	MusicTypeChildAdapter itemAdapter;
	
	private void initComponent()
	{
		if(Debug && typeList == null || typeList.size() == 0) addTypeList();
		
		itemAdapter = new MusicTypeChildAdapter(MusicTypeChildActivity.this , typeList);
		mMusicChildListView.setAdapter(itemAdapter);
		mMusicChildListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				System.out.println("onItemClick --" + arg2);
				
				MusicDetailHolder holder = (MusicDetailHolder) arg1.getTag();
				CheckBox mCheckBox = holder.checkBox;
				mCheckBox.toggle();
				//itemAdapter.isSelected.put(arg2, checkBox.isChecked());
			}
		});
		
		ListViewListener listViewListener = new ListViewListener(mMusicChildListView, new GetServerData(){

			@Override
			public void getData() {
				System.out.println("getData");
				getResponse();
			}});
		listViewListener.setListViewListeners();
		
		itemAdapter.notifyDataSetChanged();
	}

	private void addTypeList() {
		Log.i(TAG, "addTypeList");
		MusicDetailEntity MusicDetailEntity ;
		for (int i = 0; i < 15; i++) {
			MusicDetailEntity = new MusicDetailEntity();
			MusicDetailEntity.setMusicId("guokai"+ i);
			MusicDetailEntity.setMusicTitle("guokai-" + i);
			MusicDetailEntity.setMusicUrl(Constant.MUSIC_URL);
			typeList.add(MusicDetailEntity);
		}
	}

}
