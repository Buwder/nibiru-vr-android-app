package com.zskx.pemsystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.LayoutParams;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.zskx.net.request.AbstractRequest.GetResponseListener;
import com.zskx.net.request.GetAllMusicTypeRequest;
import com.zskx.net.response.MusicDetailEntity;
import com.zskx.net.response.MusicTypeEntity;
import com.zskx.net.response.ResponseEntity;
import com.zskx.pemsystem.adpater.MusicAdapter;
import com.zskx.pemsystem.adpater.PlayingListAdapter;
import com.zskx.pemsystem.customview.MusicDialogTimer;
import com.zskx.pemsystem.customview.WifiTextView;
import com.zskx.pemsystem.customview.MusicDialogTimer.ObserverShowBtn;
import com.zskx.pemsystem.meida.MyMediaPlayer;
import com.zskx.pemsystem.sql.DBService;
import com.zskx.pemsystem.util.AppConfiguration;
import com.zskx.pemsystem.util.AsyncImageLoader;
import com.zskx.pemsystem.util.BackButtonSet;
import com.zskx.pemsystem.util.ChangeViewBackground;
import com.zskx.pemsystem.util.Constant;
import com.zskx.pemsystem.util.ListViewListener;
import com.zskx.pemsystem.util.ShowNotification;
import com.zskx.pemsystem.util.ListViewListener.GetServerData;
import com.zskx.pemsystem.util.MyPost;
import com.zskx.pemsystem.util.ScreenManager;
import com.zskx.pemsystem.util.StringsUtil;
import com.zskx.pemsystem.util.UtilService;

/**
 * 
 * @author guokai
 *
 */
public class Music_Activity extends MenuActivity {
	public final static String TYPE = "type";
	public final static String IMG_URL = "iamgeURL";
	public final static int REQUESTCode = 123;
	public final static int PLAYINGLIST = 76542;
	private final String TAG = "Music_Activity";
	private final static int BAR_MAX = 100;
	private ListView mMusicListView; //服务器音乐列表
	private ListView playingList; 	//正在播放列表
	
	private Button btn_play ; //播放按钮
	private Button btn_prev ;	//后退按钮
	private Button btn_next ;	//前进按钮
	private Button btn_list_menu ; //音乐列表按钮
	private Button btn_playing_menu ; //正在播放列表按钮
	private Button btn_playmode ; //播放模式按钮
	private Button btn_timer;   //定时器
	private View music_lyt_playmode;
	private View music_lyt_previous;
	private View music_lyt_play;
	private View music_lyt_next;
	private View music_lyt_timer;
	
	private Button btn_timer_show;

	
	private int flag_playmode = StringsUtil.Circle; //播放模式
	private Button btn_volume ; //音量控制按钮
	
	private View lyt_list = null;
	private View lyt_playing = null;
	LayoutInflater layoutInflater = null;
	private View lyt_include_playing; //正在播放列表layout
	private View lyt_include_list ; //音乐列表layout
	
	private CheckBox ckb_swp; //启动时播放
	private CheckBox ckb_mkp; //记录播放位置
	private CheckBox ckb_fifo; //淡入淡出效果
	
	private MyMediaPlayer mediaPlayer;
	private boolean isFirst = true;
	private SeekBar seekBar; //播放进度条
	private TextView textView_current; //播放音乐的当前进度
	private TextView textView_timeAll; //播放音乐的总长度
	private TextView textView_tilte; //播放音乐的名称
	private static AsyncImageLoader asyncImageLoader = new AsyncImageLoader();;   //异步加载图像辅助类
	
	
	private PlayingListAdapter playingListAdapter ; //播放音乐列表Adapter
	private ArrayList<MusicDetailEntity> mPlaylist = new ArrayList<MusicDetailEntity>(); //播放音乐列表
	private MusicDetailEntity music_track; //
	private int posList = 0; //播放音乐列表的位置
	private int posListFromDB = 1000; //从服务器读取的列表位置
	
	private GetAllMusicTypeRequest allMusicTypeRequest;
	
	private AudioManager audioManager;
	DBService dbService = null;
	private static String userId = "1111";
	private static String sessionID ;
	int curr_position = 0;
	
	private View music_popwindow; //音乐配置弹出菜单
	
	private ArrayList<MusicTypeEntity> typeList = new ArrayList<MusicTypeEntity>();
	private View server_progressBar; //连接服务器进度条
	private boolean isResponsed = false; //显示连接服务器进度条的标志
	
	MyHandler myHandler = new MyHandler() ; //
	
	private int bar_pos; //拖动进度条的位置
	private boolean isBufferReady = true; //缓冲区是否准备好
	
	private ProgressBar title_progressBar; //歌曲名缓冲
	
	private WifiTextView wifiTextView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ScreenManager.getScreenManager().pushActivity(this);
		setContentView(R.layout.music_activity);
		
		check_sessionID();
		
		if(mediaPlayer == null) mediaPlayer = new MyMediaPlayer();
		dbService = new DBService(this, userId);
		
		initComponent();
		
		BackButtonSet.setButton(this); //初始化后退键
		
		//change_playing_init();
		change_playlist();
		
		setTelMgr(); // 设置来电监听
		
		map.put(R.id.music_button_next, music_lyt_next);
		map.put(R.id.music_button_play, music_lyt_play);
		map.put(R.id.music_button_previous, music_lyt_previous);
		map.put(R.id.music_button_playmode, music_lyt_playmode);
		map.put(R.id.music_button_timer, music_lyt_timer);
		list_btn.add(R.id.music_button_next);
		list_btn.add(R.id.music_button_play);
		list_btn.add(R.id.music_button_previous);
		list_btn.add(R.id.music_button_playmode);
		list_btn.add(R.id.music_button_timer);
	}

	private void check_sessionID() {
		if(AppConfiguration.getUser(this) != null) {
			userId = AppConfiguration.getUser(this).getLoginName();
			sessionID = AppConfiguration.getUser(this).getSessionId();
		}else{
			UtilService.show(Music_Activity.this, "用户过期，请重新登录");
			finish();
		}
	}
	
	private void setTelMgr() {
		TelephonyManager telephonyManager = 
			(TelephonyManager) Music_Activity.this.getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(new PhoneStateChange(), PhoneStateListener.LISTEN_CALL_STATE);
	}
/*****************************************************************************************/
	/**
	 * 启动时是否播放
	 */
	private void startWithPlaying() {
//		Log.i(TAG, "startWithPlaying, start!!  " + ckb_swp.isChecked());
		if(ckb_swp != null && ckb_swp.isChecked()){
			if(posListFromDB < mPlaylist.size() && posListFromDB >= 0){
				if (isFirst) {
					mp3Start(posListFromDB);
					System.out.println("startWithPlaying!!!!" + mediaPlayer.isPlaying());
				}
				if(ckb_mkp.isChecked()){
					startThreadSeekto();//异步跳转
				}
			}
		}
	}
	/**
	 * 异步跳转
	 */
	private void startThreadSeekto() {
		handler.post(startThread);
		/*Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while(mediaPlayer != null && !mediaPlayer.isPlaying() && 
							!Thread.currentThread().isInterrupted()){
						try {
							Thread.sleep(200);
							sendMsg(BLANK_TITLE);
						} catch (InterruptedException e) {
							e.printStackTrace();
							System.out.println("interrupt");
						}
					}
					playerSeekTo(curr_position);
					sendMsg(ORIGINAL_TITLE);
				} catch (Exception e) {
					System.out.println("startThreadSeekto");
					e.printStackTrace();
				}
			}
		});
		thread.start();*/
	}
	private Runnable startThread = new Runnable() {
		
		@Override
		public void run() {
			System.out.println("startThread is running!");
			if (mediaPlayer != null && !mediaPlayer.isPlaying() ){
					sendMsg(BLANK_TITLE);
					handler.postDelayed(startThread, 200);
			}else{
				if (posList == posListFromDB ) playerSeekTo(curr_position); //当前播放歌曲是记录歌曲时跳转上次播放位置
				sendMsg(ORIGINAL_TITLE);
			}
			
		}
	};
	
	/******************消息队列处理*************************/
	private final static int BLANK_TITLE = 0x0058;
	private final static int ORIGINAL_TITLE = 0x0043;
	public final class MyHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BLANK_TITLE:
				if(music_track != null) {
					textView_tilte.setText("正在下载..." + music_track.getMusicTitle());
					title_progressBar.setVisibility(View.VISIBLE);
				}
				break;
			case ORIGINAL_TITLE:
				if(music_track != null) {
					textView_tilte.setText(music_track.getMusicTitle());
					title_progressBar.setVisibility(View.GONE);
				}
				break;
			}
			super.handleMessage(msg);
		}
		
	}
	/**
	 * 发送消息，改变UI
	 * @param name 消息名
	 * @return
	 */
	private Message sendMsg(int name) {
		Message message = myHandler.obtainMessage();
		message.what = name;
		message.sendToTarget();
		return message;
	}

	/********************访问服务器****************************/
	private final static int DEFAULT_COUNT = 30;// 每页默认每页数据条数
	private int totalPageCount = 1;//  第一次访问，必须设为 1
	private int currentPage = 0;
	private int totalCount = 0;
	
	private boolean Debug ; //
	private boolean Debug_local = false;
	private boolean isGetResponse = false;
	private void getServerResponse() {
		 allMusicTypeRequest = new GetAllMusicTypeRequest(new GetResponseListener<MusicTypeEntity>(){

			@Override
			public void onSuccess(ResponseEntity<MusicTypeEntity> result) {
				totalCount = result.getTotalCount();
				totalPageCount = (totalCount + DEFAULT_COUNT - 1) / DEFAULT_COUNT;
				typeList.addAll( result.getContent());
				isResponsed = true;
				if(currentPage <= totalPageCount) currentPage++;
				MyPost.post(new Runnable(){
					@Override
					public void run() {
						server_progressBar.setVisibility(View.GONE);
						afterResponse();
					}});
				isGetResponse = true;
			}

			@Override
			public void onError(final ResponseEntity<MusicTypeEntity> result) {
				
				MyPost.post(new Runnable(){
					@Override
					public void run() {
						server_progressBar.setVisibility(View.GONE);
						UtilService.show(Music_Activity.this, result.getMsg());
					}});
			
				isResponsed = true;
			}}, sessionID, currentPage, DEFAULT_COUNT);
		allMusicTypeRequest.sendByThread();
		Debug = allMusicTypeRequest.getDebug();
	}
	/**
	 * 成功获得数据后
	 */
	private void afterResponse() {
		Log.i(TAG, "typeList::" + typeList);
		if(Debug && typeList == null || typeList.size() == 0) {
			addTypeList(); //自造音乐类型数据
		}
		if(typeList.size() == 0) {
			TextView textView = (TextView) findViewById(R.id.music_list_text);
			textView.setVisibility(View.VISIBLE);
		}
		mMusicListView.setAdapter(new MusicAdapter(Music_Activity.this, typeList , myHandler, asyncImageLoader));
	    mMusicListView.setOnItemClickListener(new OnItemClickListener() {
	
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
				Intent intent=new Intent(Music_Activity.this, MusicTypeChildActivity.class);
				intent.putExtra(TYPE, typeList.get((int) arg2).getMusicTypeId());
				intent.putExtra(IMG_URL, typeList.get((int) arg2).getMusicTypeImage());
				startActivityForResult(intent, REQUESTCode);
				}
		});
	    /**
	     * 列表下拉动作监听类
	     */
	    new ListViewListener(mMusicListView, new GetServerData(){

			@Override
			public void getData() {
				System.out.println("ListViewListener");
			}}).setListViewListeners();
	}
	
	/**
	 * 自造音乐类型数据
	 */
	private void addTypeList() {
		Log.i(TAG, "addTypeList");
		
		MusicTypeEntity musicTypeEntity ;
		for (int i = 0; i < 6; i++) {
			musicTypeEntity = new MusicTypeEntity();
			musicTypeEntity.setMusicTypeTitle(getResources().getString(R.string.music_item_title));
			musicTypeEntity.setMusicTypeDescription(getResources().getString(R.string.music_item_introduction));
			musicTypeEntity.setMusicTypeImage("http://i.ftimg.net/picture/7/000030247_piclink_0_0.jpg");
			typeList.add(musicTypeEntity);
			musicTypeEntity = new MusicTypeEntity();
			musicTypeEntity.setMusicTypeTitle(getResources().getString(R.string.music_item_title));
			musicTypeEntity.setMusicTypeDescription(getResources().getString(R.string.music_item_introduction));
			musicTypeEntity.setMusicTypeImage("http://i0.sinaimg.cn/2012/idx/2012/0810/U687P1280T45D1F1164DT20120810050540.jpg");
			typeList.add(musicTypeEntity);
		}
	}
	
	private void initComponent() {
		LayoutInflater layoutInflater = LayoutInflater.from(Music_Activity.this);
		music_popwindow = layoutInflater.inflate(R.layout.music_popupwindow_menu, null);
		ckb_swp = (CheckBox) music_popwindow.findViewById(R.id.music_menu_checkbox_startwithplaying);
		ckb_mkp = (CheckBox) music_popwindow.findViewById(R.id.music_menu_checkbox_markposition);
		ckb_fifo = (CheckBox) music_popwindow.findViewById(R.id.music_menu_checkbox_fifo);
		
	//	mPlaylist = new ArrayList<MusicDetailEntity>();
		
		mMusicListView = (ListView)findViewById(R.id.listView_item);
		playingList = (ListView) findViewById(R.id.music_playing_listView);
		
		lyt_include_list = this.findViewById(R.id.music_include_list);
		lyt_include_playing = this.findViewById(R.id.music_include_playing);
		
		btn_list_menu = (Button) this.findViewById(R.id.music_button_list);
		btn_playing_menu = (Button) this.findViewById(R.id.music_button_playing);
		
		lyt_list = this.findViewById(R.id.music_layout_playlist);
		lyt_playing = this.findViewById(R.id.music_layout_playing);
		
		btn_playmode = (Button) this.findViewById(R.id.music_button_playmode);
		setMenuButtonListenser();
		
		startThread2DB(); // 异步读取数据库，启动时播放，自动跳转
		
		btn_volume = (Button) this.findViewById(R.id.music_button_volume);
		btn_volume.setOnClickListener(BtnClick);
		
		btn_play = (Button) this.findViewById(R.id.music_button_play);
		btn_prev = (Button) this.findViewById(R.id.music_button_previous);
		btn_next = (Button) this.findViewById(R.id.music_button_next);
		btn_play.setOnClickListener(BtnClick);
		btn_prev.setOnClickListener(BtnClick);
		btn_next.setOnClickListener(BtnClick);
		btn_timer_show = (Button) this.findViewById(R.id.music_button_timershow);
		btn_timer_show.setOnClickListener(BtnClick);
		
		btn_timer = (Button) findViewById(R.id.music_button_timer);
		btn_timer.setOnClickListener(BtnClick);
		music_lyt_playmode = findViewById(R.id.music_lyt_playmode);
		music_lyt_next = findViewById(R.id.music_lyt_next);
		music_lyt_play = findViewById(R.id.music_lyt_play);
		music_lyt_previous = findViewById(R.id.music_lyt_previous);
		music_lyt_timer = findViewById(R.id.music_lyt_timer);
		
		seekBar = (SeekBar) this.findViewById(R.id.music_seekbar);
		seekBar.setMax(BAR_MAX);
		seekBar.setOnSeekBarChangeListener(new SeekBarChange());
		textView_current = (TextView) this.findViewById(R.id.music_time_current);
		textView_timeAll = (TextView) this.findViewById(R.id.music_time_all);
		textView_tilte = (TextView) this.findViewById(R.id.music_text_title);
		audioManager = (AudioManager) Music_Activity.this.getSystemService(AUDIO_SERVICE);
	
		server_progressBar =  findViewById(R.id.loading_progress);
		title_progressBar = (ProgressBar) this.findViewById(R.id.music_song_progressBar);
		if(music_track == null) textView_tilte.setText(""); 
		
		add_button_touch();//添加按钮点击效果, 改变按钮背景
		
		wifiTextView =  (WifiTextView) findViewById(R.id.wifiNetWorkState);
	}
	/**
	 * 添加按钮点击效果, 改变按钮背景
	 */
	private void add_button_touch() {
		int back = R.drawable.music_ico_bg_active;//背景图片
		
		OnTouchListener listener = new ChangeViewBackground().get_change_back(this, map, list_btn, back);
		btn_play.setOnTouchListener(listener);
		btn_next.setOnTouchListener(listener);
		btn_prev.setOnTouchListener(listener);
		btn_playmode.setOnTouchListener(listener);
		btn_timer.setOnTouchListener(listener);
//		music_lyt_playmode.setOnTouchListener(buttons_touch);
//		music_lyt_next.setOnTouchListener(buttons_touch);
//		music_lyt_play.setOnTouchListener(buttons_touch);
//		music_lyt_previous.setOnTouchListener(buttons_touch);
//		music_lyt_timer.setOnTouchListener(buttons_touch);
	}
	
	
	
	
	/**
	 * 异步读取数据库，启动时播放，自动跳转
	 */
	private void startThread2DB() {
		new Thread( new Runnable() {
			
			@Override
			public void run() {
				MyPost.post(new Runnable() {
					
					@Override
					public void run() {
//						addMusicTrackFromDB(); //从数据库取得播放列表和播放配置
						if(mPlaylist.size() < 1 && Debug_local) addMusicTrack();
						if(music_track == null) {
							if(mPlaylist != null && mPlaylist.size() > 0 ) music_track = mPlaylist.get(posList);
						}
						setPlaymode(flag_playmode);
						btn_playmode.setOnClickListener(BtnClick);
						
						 playingListAdapter = new  PlayingListAdapter(Music_Activity.this , mPlaylist);
						 playingList.setAdapter(playingListAdapter);
						 playingList.setClickable(false);
						 playingList.setOnItemClickListener(new OnItemClickListener(){
							@Override
							public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
								//调取播放列表条目
//								PlayingListHolder listHolder = (PlayingListHolder) arg1.getTag();
								if(position != posList) {
									isFirst = true;
									mp3Start(position);
								}else{
									if(isBufferReady){
										mp3Start(position);
									}else{
										UtilService.show(Music_Activity.this, "正在缓冲...请稍等");
									}
								}
						}});
//						 playingList.setOnTouchListener(new OnTouchListener() {
//							@Override
//							public boolean onTouch(View v, MotionEvent event) {
//								return true;
//							}
//						});
						 
						 startWithPlaying(); //启动时是否播放
					}
				});
			}
		}).start();
	}

	private void setMenuButtonListenser() {
		btn_list_menu.setOnClickListener(new LytClick());
		btn_playing_menu.setOnClickListener(new LytClick());
		lyt_list.setOnClickListener(new LytClick());
		lyt_playing.setOnClickListener(new LytClick());
	}
	
	/**
	 * 从数据库取得播放列表和播放配置
	 */
	private void addMusicTrackFromDB() {
		ArrayList<MusicDetailEntity> _list = dbService.getAll_playing_list();
		if(_list != null &&  _list.size() != 0) {
			mPlaylist = _list;
			Log.i(TAG, "addMusicTrackFromDB::" + _list.size());
		}
		posListFromDB = dbService.qurey_track_position();
		curr_position = dbService.qurey_pos();
		flag_playmode = dbService.qurey_model();
		ckb_swp.setChecked(dbService.qurey_isStart() == 1 ? true: false);
		ckb_mkp.setChecked(dbService.qurey_isPos() == 1 ? true: false);
		ckb_fifo.setChecked(dbService.qurey_isFIFO() == 1 ? true: false);
		Log.i(TAG, "posListFromDB:" + posListFromDB + "  curr_position:" + curr_position + "  flag_playmode:" + flag_playmode);
	}
	@Override
	protected void onResume() {
		if(music_track == null) ShowNotification.showNotification(this, "PEM", getResources().getString(R.string.title_musical_therapy), Music_Activity.class, null);
		super.onResume();
		if(mediaPlayer == null) mediaPlayer = new MyMediaPlayer();
		registerReceiver(nextReceiver, getIntentFilter("NEXT"));
		registerReceiver(wifiTextView.getReceiver(), getIntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(nextReceiver);
		unregisterReceiver(wifiTextView.getReceiver());
	}
	 
	@Override
	public void onDestroy() {
		MusicDialogTimer.dialog_cancle_timer(); //取消定时关闭功能
		handler.removeCallbacks(startThread);
//		dbService.delete_music_list();
//		dbService.delete_music_pos();
//		Log.i(TAG, "delete_music_list--" + dbService.getCount(StringsUtil.Music_Playing_list));
		
		int pos = 0;
		if(mediaPlayer != null) {
//			addlist2DB();
			if(mediaPlayer.isPlaying()) pos = mediaPlayer.getCurrentPosition();
//			save_music_pos( pos);
			try{
				mp3Stop();
				mediaPlayer.release();
			}catch(IllegalStateException e1){
				UtilService.show(Music_Activity.this, "网络连接出错!");
				Log.e(TAG, e1.toString());
			}catch (Exception e) {
				Log.e(TAG, e.toString());
			}
			
		}
		allMusicTypeRequest.cancel(); //取消接受服务器请求
		super.onDestroy();
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "resultCode::" + resultCode);
		Log.i(TAG, "requestCode::" + requestCode);
		switch (resultCode) {
		case MusicTypeChildActivity.RESULTCODE:
			if(data != null) {
				ArrayList<MusicDetailEntity> musicList = (ArrayList<MusicDetailEntity>) data.getSerializableExtra("result");
				//cancleRepeat(musicList);//去除重复歌曲
				if(musicList != null) mPlaylist.addAll(cancleRepeat(musicList)); //从服务器音乐列表添加音乐到正在播放列表
				change_playing();
				if(mPlaylist != null && mPlaylist.size() != 0 && mPlaylist.get(0) != null) mp3Start(0);
			}
			break;
		} 
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	/**
	 * 去除重复歌曲
	 * @param musicList
	 */
	private  ArrayList<MusicDetailEntity> cancleRepeat(ArrayList<MusicDetailEntity> musicList) {
		if(musicList != null) {
		for (int j = 0; j < musicList.size(); j++) {
			MusicDetailEntity entity = musicList.get(j);
			for (int i = 0; i < mPlaylist.size(); i++) {
				MusicDetailEntity music = mPlaylist.get(i);

				if (entity.getMusicTitle().equals(music.getMusicTitle())) {
					musicList.remove(entity);
					UtilService.show(Music_Activity.this, entity.getMusicTitle() + " 已经添加");
				}
			}
		}
		}
		return musicList;
	}

	private void save_music_pos(int pos) {
		
		int is_start = ckb_swp.isChecked()? 1 : 0;
		int is_pos = ckb_mkp.isChecked()? 1 : 0;
		int is_fifo = ckb_fifo.isChecked()? 1 : 0;
		Log.i(TAG, "is_start==" + is_start + "   is_pos==" + is_pos +  "   is_fifo== " + is_fifo);
		dbService.save_music_pos(posList, pos, flag_playmode , is_start , is_pos , is_fifo);
		
	}
	
	/**
	 * 向sqlite添加歌曲列表
	 */
	private void addlist2DB() {
		if (mPlaylist !=null && mPlaylist.size()>0) {
			for (MusicDetailEntity music_track : mPlaylist) {
				dbService.save_music_list(music_track);
				Log.i(TAG, "addlist2DB----music_track:::" + music_track.getMusicUrl());
			}
		}
	}
	
	/**********************广播接受*******************************/
	private IntentFilter getIntentFilter(String broadAction){
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(broadAction);
		return intentFilter;
	}
	
	BroadcastReceiver nextReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if ( mPlaylist != null && intent.getStringExtra("getMusicId") != null && music_track != null){
			String getMusicId = intent.getStringExtra("getMusicId");
			String music_track_id = music_track.getMusicId();
			Log.i(TAG, "onReceive::" + getMusicId + "   " + music_track_id);
			
				
			
				if (getMusicId.trim().equals( music_track_id.trim())) {
					if(mPlaylist.size() == 0 ) music_track = null;
					mp3Stop();
					mp3Start(posList); 
				} 
			}
		}
	};
	
	/**************************************************************/
	/**
	 * 手动添加数据
	 */
	private void addMusicTrack() {
		music_track = new MusicDetailEntity();
		music_track.setMusicId("1");
		music_track.setMusicTitle("11111");
		music_track.setMusicUrl(Constant.MUSIC_URL);
		mPlaylist.add(music_track);
		music_track = new MusicDetailEntity();
		music_track.setMusicId("2");
		music_track.setMusicTitle("2222");
		music_track.setMusicUrl(Constant.MUSIC_URL2);
		mPlaylist.add(music_track);
		music_track = new MusicDetailEntity();
		music_track.setMusicId("3");
		music_track.setMusicTitle("333");
		music_track.setMusicUrl(Constant.MUSIC_URL3);
		mPlaylist.add(music_track);
		music_track = mPlaylist.get(0);
	}
	
	/**
	 * 得到播放器的播放状态
	 * @return 1 表示播放, 2 表示暂停, 0 表示停止
	 */
	public int getGlayingStatus(){
		if(mediaPlayer != null) {
			if (mediaPlayer.isPlaying()) {
				return 1;
			}else if(mediaPlayer.isPause){
				return 2;
			}
		}
		return 0;
	}
	

	public void mp3Start(int pos)  {
		Log.i(TAG, "mp3Start!!!!");
		if (mPlaylist != null && mPlaylist.size() > 0 ) {
			if(pos < mPlaylist.size() && pos >= 0) {
				music_track = mPlaylist.get(pos);
				if(!checkSong(music_track)) next_music();
				if (isFirst) {
					String path = music_track.getMusicUrl();
					Log.i(TAG, "music_URL::" + path);
					if(path==null || "".equals(path)) {
						UtilService.show(Music_Activity.this, "要下载的文件不存在");
						return;
					}
					setPlayerPath(path); //设置player资源地址
					setMediaPlayerListener(); //设置mediaPlayer监听器
					posList = mPlaylist.indexOf(music_track);
				}
				//暂停
				else if (mediaPlayer.isPlaying()) {
					mediaPlayer.pause();
					btn_play.setBackgroundResource(R.drawable.music_button_play);
				}
				//不是初次播放，继续播放
				else if (!isFirst) {
					mediaPlayer.start();
					btn_play.setBackgroundResource(R.drawable.music_ico_pause);
				}
				Log.i(TAG, "getGlayingStatus::" + getGlayingStatus());
				sendPlayingSong(posList , getGlayingStatus()); //发送正在播放歌曲到playinglist
			}else{ 
				Log.i(TAG, "pos超出列表范围");
				music_track = null;
			}
		}else {
			if(mediaPlayer != null ) {
				if (mediaPlayer.isPlaying()) mediaPlayer.stop();
				mediaPlayer.release();
				mediaPlayer = null;
				setNoTrack(); 
				Log.i(TAG, "-------------" + textView_tilte.getText()+ "/////" + textView_timeAll.getText());
			}
//			if(isBufferReady) 
				UtilService.show(Music_Activity.this, "请添加音乐"); //
//			else{
//				UtilService.show(Music_Activity.this, "正在缓冲...请稍等");
//			}
		}
	}
	/**
	 * 如果当前没有歌曲，设置显示和notification
	 */
	private void setNoTrack() {
		title_progressBar.setVisibility(View.GONE);
		textView_tilte.setText("");
		textView_timeAll.setText("00:00");
		ShowNotification.showNotification(this, "PEM", getResources().getString(R.string.title_musical_therapy), Music_Activity.class ,null);
	}
	/**
	 * 设置player资源地址
	 * @param path
	 */
	private void setPlayerPath(String path) {
		if(mediaPlayer == null) mediaPlayer = new MyMediaPlayer();
		mediaPlayer.reset(); 
		try {
			mediaPlayer.setDataSource(path);
//			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.prepareAsync();
			isBufferReady = false;
			sendMsg(BLANK_TITLE); //添加正在下载title
		}catch(IllegalStateException e1){
			UtilService.show(Music_Activity.this, "网络连接出错!");
			Log.e(TAG, e1.toString());
		}catch (IOException e) {
			Log.e(TAG, e.toString());
		}
	}
	
	/**
	 * 设置mediaPlayer监听器
	 */
	private void setMediaPlayerListener() {
		//添加准备监听器
		mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.start();
				sendMsg(ORIGINAL_TITLE); //添加正在下载title
				//loadMusicImage(music_track.getMusicImage()); //异步加载当前音乐图像
				btn_play.setBackgroundResource(R.drawable.music_ico_pause);
				isFirst = false;
				handler.post(FlashUI_Thread);
				isBufferReady = true;
				System.out.println("onPrepared!!!!!");
				if(music_track != null) ShowNotification.showNotification(Music_Activity.this, getResources().getString(R.string.title_musical_therapy), music_track.getMusicTitle(),Music_Activity.class ,null);
			}});
		//添加播放文件结束监听器
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				Log.i(TAG, "meida onCompletion >>>" + flag_playmode);
				next_music();
			}
		});
		//添加缓冲监听器
		mediaPlayer.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
			@Override
			public void onBufferingUpdate(MediaPlayer mp, int percent) {
				seekBar.setSecondaryProgress(percent * seekBar.getMax() / 100);
				if (percent < 100 && percent%10==0) System.out.println("onBufferingUpdate::::" + percent * seekBar.getMax() / 100);
					
			}
		});
		//进度条拖拽完成 设置播放器buffer为false
		mediaPlayer.setOnSeekCompleteListener(new OnSeekCompleteListener() {
			
			@Override
			public void onSeekComplete(MediaPlayer mp) {
				isBufferReady = false;
				System.out.println("SeekComplete!!!!!" + bar_pos);				
			}
		});
	}
	

	public void mp3Stop() {
		
		if(!isFirst){
			mediaPlayer.stop();
			btn_play.setBackgroundResource(R.drawable.music_button_play);
			isFirst = true;
			handler.removeCallbacks(FlashUI_Thread);//ֹͣʱ��ȥ����½��
			
			seekBar.setProgress(0); //���ý�ȹ���
			seekBar.setSecondaryProgress(0);
			textView_current.setText("00:00");//������ʾ����
			if ( music_track == null) {
				setNoTrack();
			}
			Log.i(TAG, "mp3Stop!!");
		}
	}
	
	
	/**
	 * mp3��previous��
	 */
	public void mp3Previous() {
		System.out.println("mp3Previous::" + posList);
		if (mPlaylist != null && mPlaylist.size() > 0) {
			if (posList > 0) {
				mp3Stop();
				posList = posList - 1;
				//music_track = mPlaylist.get(posList);
				//	textView_tilte.setText(music_track.getMusicTitle());
				mp3Start(posList);
			}
		}
	}
	
	private void next_music() {
		Log.i(TAG, "next_music!!" + posList);
		if (mPlaylist != null && mPlaylist.size() > 0) {
			switch (flag_playmode) {
			case StringsUtil.Circle:
				if (posList >= mPlaylist.size() - 1) {
					mp3Stop();
					posList = 0;
					//music_track = mPlaylist.get(posList);
					mp3Start(posList);
				} else {
					mp3Stop();
					posList = posList + 1;
					//music_track = mPlaylist.get(posList);
					mp3Start(posList);
				}
				break;

			case StringsUtil.Single:
				mp3Stop();
				if (posList >= mPlaylist.size() - 1) {
					posList = 0;
					//music_track = mPlaylist.get(posList);
				}
				mp3Start(posList);
				break;

			case StringsUtil.Sheffle:
				mp3Stop();
				posList = (int) (Math.random() * mPlaylist.size());
				//music_track = mPlaylist.get(posList);
				mp3Start(posList);
				break;

			case StringsUtil.Order:
				if (posList >= mPlaylist.size() - 1) {
					mp3Stop();
				} else {
					mp3Stop();
					posList = posList + 1;
					//music_track = mPlaylist.get(posList);
					mp3Start(posList);
				}
				break;
			}
		}
	}
	
	private boolean checkSong(MusicDetailEntity song) {
		for (MusicDetailEntity iterable_element : mPlaylist) {
			if (song == iterable_element) return true;
		}
		return false;
	}
	
	public void playerSeekTo(int pos){
		if (mediaPlayer != null) {
			mediaPlayer.seekTo(pos);
			Log.i(TAG, "mediaPlayer.seekTo:::" + pos);
		}
	}
	
	public void seekBarTouched(int des){
		if (mediaPlayer != null ) {
			System.out.println("isFirst===" + isFirst);
			if (isFirst) {
				mp3Start(posList);
			}
			int musicMax = mediaPlayer.getDuration();
			int barMax = seekBar.getMax();
			this.playerSeekTo(musicMax * des / barMax);
			isBufferReady = false;
		}
	}
	
	public void setTimeView() {
		if(mediaPlayer.isPlaying()){
			String curTimeShow = this.milliseconds2time(mediaPlayer.getCurrentPosition());
			String allTimeShow = this.milliseconds2time(mediaPlayer.getDuration());
			textView_current.setText(curTimeShow);
			textView_timeAll.setText(allTimeShow);
//			System.out.println("allTimeShow:::" + mediaPlayer.getDuration());
		}
	}
	
	
	public String milliseconds2time(int milliseconds){
		String zeroSecond = "0";
		String zeroMinute = "0";
		int minuts  = (int)(milliseconds/1000)/60;
		int seconds = (int)(milliseconds/1000 - minuts*60);
		if(seconds >= 10) zeroSecond = "";
		if(minuts >= 10) zeroMinute = "";
		return zeroMinute + minuts + ":" + zeroSecond + seconds;
	 }
	
	
	private void setPlaymode(int flag_playmode) {
		switch (flag_playmode) {
		case StringsUtil.Circle:
			btn_playmode.setBackgroundResource(R.drawable.music_ico_cycle);
			break;
		case StringsUtil.Single:
			btn_playmode.setBackgroundResource(R.drawable.music_ico_single);
			break;
		case StringsUtil.Sheffle:
			btn_playmode.setBackgroundResource(R.drawable.music_ico_random);
			break;
		case StringsUtil.Order:
			btn_playmode.setBackgroundResource(R.drawable.music_ico_order);
			break;
		default:
			btn_playmode.setBackgroundResource(R.drawable.music_ico_order);
			break;
		}
	}
	
	


	private View.OnClickListener BtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			
			switch (v.getId()) {
			case R.id.music_button_play:
				if(isBufferReady) mp3Start(posList);
				else UtilService.show(Music_Activity.this, "正在缓冲...请稍等");
				break;
			case R.id.music_button_previous:
				mp3Previous();
				break;
			case R.id.music_button_next:
				if (flag_playmode == StringsUtil.Single ) posList++; //单曲播放时，按下next键将会播放下一曲
				next_music();
				break;
			/*
			 * 按下播放模式按钮
			 */
			case R.id.music_button_playmode:
				if(flag_playmode == 4) flag_playmode = 0;
				setPlaymode(++flag_playmode);
				toastMode(flag_playmode);
				//save_music_pos( 0);
				break;
				
			case R.id.music_button_timer:
				creat_dialog_timer(); //创建计时器对话框
				break;
			case R.id.music_button_timershow:
				creat_dialog_timer(); //显示定时器
				break;
				
			
			case R.id.music_menu_button_startwithplaying :
				ckb_swp.toggle();
				break;
			case R.id.music_menu_button_markposition :
				ckb_mkp.toggle();
				break;
			case R.id.music_menu_button_fifo :
				ckb_fifo.toggle();
				break;
			
			case R.id.music_button_volume:
				popwindow_volume(v);
				break;
			}
		}
	};
	//Toast显示播放模式
	private void toastMode(int flag_playmode) {
		switch (flag_playmode) {
		case StringsUtil.Circle:
			UtilService.show(Music_Activity.this, "列表循环");
			break;
		case StringsUtil.Single:
			UtilService.show(Music_Activity.this, "单曲循环");
			break;
		case StringsUtil.Sheffle:
			UtilService.show(Music_Activity.this, "随机播放");
			break;
		case StringsUtil.Order:
			UtilService.show(Music_Activity.this, "顺序播放");
			break;
		}
	}
/**********************************************************************************************************************/

	MusicDialogTimer dialog;
	/**
	 * 创建计时器对话框
	 */
	protected void creat_dialog_timer() {
		if(dialog == null){
			dialog = new MusicDialogTimer(Music_Activity.this, 237, 342, R.layout.music_dialog_timer ,observerShowBtn);
		}
		dialog.show();
	}
	/**
	 * 观察者模式，显示／取消定时器图标
	 */
	private ObserverShowBtn observerShowBtn = new ObserverShowBtn() {
		
		@Override
		public void show_button() {
			if(btn_timer_show!=null) btn_timer_show.setVisibility(View.VISIBLE);
		}
		
		@Override
		public void hide_button() {
			if(btn_timer_show!=null) btn_timer_show.setVisibility(View.INVISIBLE);
		}
	};


	
/********************************************************************************************************/

	

	private final class LytClick implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
		
			case R.id.music_layout_playlist:
				change_playlist();
				break;
			case R.id.music_layout_playing:
				change_playing();
				break;
				
		
			case R.id.music_button_list:
				change_playlist();
				break;
			case R.id.music_button_playing:
				change_playing();
				break;
			
			}
		}
	}
	
	/**
	 * 显示当前列表和播放版面
	 */
	private void change_playing_init() {
		lyt_include_list.setVisibility(View.GONE);
		lyt_include_playing.setVisibility(View.VISIBLE);
		
		lyt_playing.setBackgroundResource(R.drawable.music_playing_currentbg);
		lyt_list.setBackgroundResource(R.drawable.content_title_bg);
	}
	/**
	 * 显示当前列表和播放版面
	 */
	private void change_playing() {
		if (playingListAdapter != null ) playingListAdapter.notifyDataSetChanged();
		lyt_include_list.setVisibility(View.GONE);
		lyt_include_playing.setVisibility(View.VISIBLE);
		
		lyt_playing.setBackgroundResource(R.drawable.music_playing_currentbg);
		lyt_list.setBackgroundResource(R.drawable.content_title_bg);
	}
	/**
	 * 显示音乐库列表
	 */
	private void change_playlist() {
		if(!isGetResponse) getServerResponse();
		if(!isResponsed) server_progressBar.setVisibility(View.VISIBLE);
		
		lyt_include_list.setVisibility(View.VISIBLE);
		lyt_include_playing.setVisibility(View.GONE);
		
		lyt_list.setBackgroundResource(R.drawable.music_playing_currentbg);
		lyt_playing.setBackgroundResource(R.drawable.content_title_bg);
	}
		
/*	private void showMenuDialog()
	{
		AlertDialog.Builder b=new AlertDialog.Builder(Music_Activity.this);
		b.setTitle("功能选项");
		b.setSingleChoiceItems(R.array.music_menus, 1, null);
		b.setPositiveButton("确定", null);
		b.setCancelable(false);
		b.create().show();
	}*/
	
	
/**********************************点击一个控件，改变另一控件背景点击一个控件，改变另一控件背景******************************************/	
	
	SparseArray< View> map = new SparseArray<View>(); //点击的按钮的id，需要改变的view
	List<Integer> list_btn = new ArrayList<Integer>(); //点击的按钮
	


/**********************************************************************************************************/	
	
	/**
	 * 弹出音量控制
	 */
	private void popwindow_volume(View parent){
		LayoutInflater layoutInflater = LayoutInflater.from(Music_Activity.this);
		View music_popwindow_volume = layoutInflater.inflate(R.layout.music_popupwindow_volume, null);
		
		SeekBar seekBar = (SeekBar) music_popwindow_volume.findViewById(R.id.music_seekbar_volume);
		seekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		seekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
		Log.i(TAG, "Volume>>" + audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
		//音量进度条控制监听器
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {
				if(audioManager != null) audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, seekBar.getProgress(), AudioManager.FLAG_PLAY_SOUND);
			}
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				Log.i(TAG, "curvolume>>" + progress);
			}
		});
		
		PopupWindow popupWindow = new PopupWindow(music_popwindow_volume, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		popupWindow.setAnimationStyle(R.style.AnimationMusicVolume);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());//must be set 点击别处，就会自动消失
		popupWindow.showAsDropDown((View) parent.getParent()); //在控件下方弹出
		
	}
	
	/**
	 * 弹出配置菜单
	 * @param parent 
	 */
	private void popwindow_menu(View parent){
		
		Button btn_swp = (Button) music_popwindow.findViewById(R.id.music_menu_button_startwithplaying);
		Button btn_mkp = (Button) music_popwindow.findViewById(R.id.music_menu_button_markposition);
		Button btn_fifo = (Button) music_popwindow.findViewById(R.id.music_menu_button_fifo);
		btn_swp.setOnClickListener(BtnClick);
		btn_mkp.setOnClickListener(BtnClick);
		btn_fifo.setOnClickListener(BtnClick);
		
		if (music_popwindow.getParent() == null) {
			int ht = getWindowManager().getDefaultDisplay().getHeight();
			System.out.println("getHeight::" + ht);
			PopupWindow popupWindow = new PopupWindow(music_popwindow,
												LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			popupWindow.setAnimationStyle(R.style.AnimationMusicMenu);
			popupWindow.setFocusable(true);
			popupWindow.setOutsideTouchable(true);
			popupWindow.setBackgroundDrawable(new BitmapDrawable());//must be set 点击别处，就会自动消失
			popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			//popupWindow.showAsDropDown(parent,0,38);
			
		}
		
	}
	
/************************************************************/
	/**
	 * 发送正在播放歌曲到playinglist
	 * @param position
	 */
	private void sendPlayingSong(int position , int status) {
		Message message = playingListHandler.obtainMessage();
		message.what = PLAYINGLIST;
		message.arg1 = position;
		message.arg2 = status;
		message.sendToTarget();
		Log.i(TAG, "getGlayingStatus:::" + status);
	}

	Handler playingListHandler = new Handler(){
		private int posList = 1000;
		private int status = 0;//0 - stop  1 - playing  2 - pause
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Music_Activity.PLAYINGLIST:
				posList = msg.arg1;
				status = msg.arg2;
				playingListAdapter.changeSong(posList, status);
				break;
			}
			super.handleMessage(msg);
		}
	};


	/**
	 * 进度条控制类
	 * @author guokai
	 */
	class SeekBarChange implements OnSeekBarChangeListener{
		
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			setTimeView();
		}

		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		public void onStopTrackingTouch(SeekBar seekBar) {
			int des = seekBar.getProgress();
			bar_pos = des;
			if (isBufferReady) seekBarTouched(des);
			else UtilService.show(Music_Activity.this, "正在缓冲...请稍等");
		}
	}
	
	/**************************************************************************/
	
	/*
	 * 更新进度条，时间显示
	 */
	int pos_last; //上一次播放点
	boolean is_last = true; //第一次刷新
	Handler handler = new Handler();
	Runnable FlashUI_Thread = new Runnable(){
		public void run() {
			if (!isFirst && mediaPlayer.isPlaying()) {
				flashView(); // 更新进度条，时间显示
				
				isPlayerForward(); //是否在播放
			}
			if(music_track != null){
			
				if(!isBufferReady ){
					textView_tilte.setText("正在下载..." + music_track.getMusicTitle());
					title_progressBar.setVisibility(View.VISIBLE);
				}else{
					textView_tilte.setText(music_track.getMusicTitle());
					title_progressBar.setVisibility(View.GONE);
				}
			}else{
				setNoTrack();
			}
			handler.postDelayed(FlashUI_Thread, 1000);
		}


		private void isPlayerForward() {
			if(pos_last != mediaPlayer.getCurrentPosition() && !is_last) {
				if(!isBufferReady) isBufferReady = true;
			}
			if(pos_last == mediaPlayer.getCurrentPosition() && !is_last && mediaPlayer.isPlaying()) {
				if(isBufferReady) isBufferReady = false;
			}
			pos_last = mediaPlayer.getCurrentPosition();
			
			if(is_last) is_last =false;
		}
		

		private void flashView() {
			int pos = mediaPlayer.getCurrentPosition();
			int musicMax = mediaPlayer.getDuration();
			int barMax = seekBar.getMax();
			if(musicMax != 0) seekBar.setProgress(barMax * pos / musicMax);
			setTimeView();
		}
	};
	
	
	/**************************************************************/
	/**
	 * 来电监听类
	 * @author guokai
	 */
	private final class PhoneStateChange extends PhoneStateListener{
		private boolean isPlaying = false;
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				if(isPlaying && mediaPlayer !=null) {
					handler.removeCallbacks(stateListener);
					mediaPlayer.start(); 
					isPlaying = false;
					is_last_pos = true;
				}
				
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
							
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				handler.post(stateListener);
				break;
			default:
				break;
			}
		}
		/****************player播放监听器，有来电如果正在播放就暂停。正下下载，就等待，下载完成立刻暂停************************/
		private int last_position;
		private boolean is_last_pos = true;
		Runnable stateListener = new Runnable() {
			
			@Override
			public void run() {
				try {
					if(mediaPlayer != null && mediaPlayer.isPlaying()){
						if(last_position != mediaPlayer.getCurrentPosition() && !is_last_pos) {
							mediaPlayer.pause();
							isPlaying = true;
						}
						last_position = mediaPlayer.getCurrentPosition();
						if(is_last_pos) is_last_pos =false;
					}
					handler.postDelayed(stateListener, 100);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
			}
		};
	}
}
