package com.zskx.pemsystem;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zskx.net.request.AbstractRequest.GetResponseListener;
import com.zskx.net.request.GetAllVideoByTypeRequest;
import com.zskx.net.response.ResponseEntity;
import com.zskx.net.response.VideoDetailEntity;
import com.zskx.net.response.VideoTypeEntity;
import com.zskx.net.response.user.User;
import com.zskx.pemsystem.adpater.MovieItemAdapter;
import com.zskx.pemsystem.receiver.NetWorkObserver;
import com.zskx.pemsystem.receiver.NetWorkObserver.WifiState;
import com.zskx.pemsystem.receiver.NetWorkObserver.WifiStateListener;
import com.zskx.pemsystem.util.AppConfiguration;
import com.zskx.pemsystem.util.ScreenManager;
import com.zskx.pemsystem.util.ShowNotification;
import com.zskx.pemsystem.util.StringsUtil;

public class MovieItemsActivity extends MenuActivity{
	
	
	//private GridView gridView;
	private ListView listView;
	private Button backButton;  //返回按钮
	private Button homeButton;
	private TextView textTitle;//  类型标题
   // private ProgressBar progressBar;// 加载进度
	private TextView textInFo;//  信息提示
	private TextView textNetWorkState;
	private Button refreshButton;//刷新按钮
	private LinearLayout emptyLinearLayout;//提示界面
	private LinearLayout refreshLayout;  //刷新区域
	private LinearLayout loadingLayout;  //加载提示区域
	
	private boolean isFirstRequest=true;//是不是第一次访问
	private VideoTypeEntity videoTypeEntity;//视频类型
	private String videoTypeID="";//类型 ID 
	private String series="";//父类型
	
	private MovieItemAdapter adapter; //适配器
	
	private Animation wanringAnimation; //警告动画
	
	private NetWorkObserver netWorkObserver;
	private WifiState state; //wifi 状态
	
	public final static String VIDEOENTITYDATAIL_KEY="videoEntityDatail_key";
	
	private List<VideoDetailEntity> videoEntityList=new ArrayList<VideoDetailEntity>();//视频集合
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ScreenManager.getScreenManager().pushActivity(this);
		setContentView(R.layout.movie_item);
		
		initComponent();
		initDataOrEvent();
		getMovieDetails();
		isFirstRequest=false;
	}
	
	
	@Override
	protected void onResume() {
		//注册Wifi监听
		IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(netWorkObserver, filter);
		
		Intent intent=new Intent(this, MovieItemsActivity.class);
		intent.putExtra(MovieTypeActivity.MOVIE_SUB_SERIES_KEY, videoTypeEntity);
		intent.putExtra(MovieTypeActivity.MOVIE_TYPE_SERIES_KEY, series);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		ShowNotification.showNotification(this,series, videoTypeEntity.getVideoTypeTitle(), MovieTypeActivity.class,intent);
		super.onResume();
	}
	
	
	/**
	 * 初始化组件
	 */
	private void initComponent()
	{
		
		Intent intent=getIntent();
		
		netWorkObserver=new NetWorkObserver();
		
		
		videoTypeEntity=(VideoTypeEntity) intent.getSerializableExtra(MovieTypeActivity.MOVIE_SUB_SERIES_KEY);
		series=intent.getStringExtra(MovieTypeActivity.MOVIE_TYPE_SERIES_KEY);
		
		backButton=(Button)findViewById(R.id.btn_back);
		homeButton=(Button)findViewById(R.id.home_btn);
		textTitle=(TextView)findViewById(R.id.text_title);
		textNetWorkState=(TextView)findViewById(R.id.textNetWorkState);
		//progressBar=(ProgressBar)findViewById(R.id.load_progressBar);
		
	//	gridView=(GridView) findViewById(R.id.gridView);
		listView=(ListView) findViewById(R.id.listView);
		
		emptyLinearLayout=(LinearLayout)findViewById(R.id.linear_empty);
		loadingLayout=(LinearLayout)findViewById(R.id.linearLayout_loading);
		refreshLayout=(LinearLayout)findViewById(R.id.linearLayout_refresh);
		textInFo=(TextView)findViewById(R.id.textView_access_tip_info);
		refreshButton=(Button)findViewById(R.id.refresh_button);
	}

	
	private WifiStateListener wifiStateListener=new WifiStateListener() {
		
		@Override
		public void observeWifiState(WifiState state) {
			
			getWifiState(state);
			
		}
	};
	
	
	private void getWifiState(WifiState state)
	{
		this.state=state;
		if(state==WifiState.WIFI_CONNECTED)
		{
			textNetWorkState.setVisibility(View.GONE);
			
		}else if (state==WifiState.WIFI_FAILOVER)
		{
			textNetWorkState.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 初始化数据和事件
	 */
	private void initDataOrEvent()
	{
		if(videoTypeEntity == null)
		{
			finish();
			return;
		}
		
		netWorkObserver.setWifiStateListener(wifiStateListener);
		
		state=NetWorkObserver.getNetWorkState(this);
		
		getWifiState(state);
		
		videoTypeID=videoTypeEntity.getVideoTypeId();
		
		String title=videoTypeEntity.getVideoTypeTitle();
		
		if(title !=null)
		{
			textTitle.setText(StringsUtil.subString(title, 7));
		}
		
		textNetWorkState.setOnClickListener(clickListener);
		wanringAnimation=AnimationUtils.loadAnimation(this, R.anim.wifi_warning);
		textNetWorkState.startAnimation(wanringAnimation);
		
		backButton.setOnClickListener(clickListener);
		homeButton.setOnClickListener(clickListener);
		refreshButton.setOnClickListener(clickListener);
		
		addFooter();
		adapter=new MovieItemAdapter(getApplicationContext(),videoEntityList);
		//gridView.setAdapter(adapter);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			if(position<videoEntityList.size())//&&state == WifiState.WIFI_CONNECTED
			{
			Intent intent=new Intent(MovieItemsActivity.this, VideoPlayerMP4.class);
			intent.putExtra(VIDEOENTITYDATAIL_KEY, videoEntityList.get(position));
			startActivity(intent);
				
			}
		  }
		});
		
		
		listView.setOnScrollListener(scrollListener);
		listView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return  gestureDetector.onTouchEvent(event);
			}
		});
	}

	// Button 点击事件
	private View.OnClickListener clickListener=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
	
			switch (v.getId()) {
			case R.id.btn_back:
				
				MovieItemsActivity.this.finish();
				
				break;
				
            case R.id.refresh_button:
//				progressBar.setVisibility(View.VISIBLE);
//				backButton.setVisibility(View.GONE);
//				emptyLinearLayout.setVisibility(View.GONE);
				getMovieDetails();
				
				
				break;
            case R.id.home_btn:
            	
            	Intent intent=new Intent(MovieItemsActivity.this,HomeActivity.class);
            	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            	startActivity(intent);
            	
            case R.id.textNetWorkState:
            	Intent intentWifi=new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
            	startActivity(intentWifi);
            	break;

			default:
				break;
			}
			
		}
	};
	
	
	
	private int totalCount = 0;
	private final static int DEFAULT_COUNT = 30;// 每页默认每页数据条数
	private int totalPageCount = 1;//  第一次访问，必须设为 1
	private int currentPage = 0;
	
	
	//  接受消息 处理 UI
	private Handler handler = new Handler()
	{
	
		public void handleMessage(Message msg)
		{
		
//			progressBar.setVisibility(View.GONE);
//			backButton.setVisibility(View.VISIBLE);
			loadingLayout.setVisibility(View.GONE);
			refreshLayout.setVisibility(View.VISIBLE);
			if(msg.arg1 == ACCESS_NET_OK)
			{
				if(videoEntityList.size() == 0)
				{
					listView.setVisibility(View.GONE);
					emptyLinearLayout.setVisibility(View.VISIBLE);
					refreshButton.setVisibility(View.GONE);
					textInFo.setText(R.string.video_is_empty);
				}else
				{
					listView.setVisibility(View.VISIBLE);
					emptyLinearLayout.setVisibility(View.GONE);
					refreshButton.setVisibility(View.VISIBLE);
					adapter.notifyDataSetChanged();
				}
				
			}else if (msg.arg1 == ACCESS_NET_FAILED)
			{
				currentPage-=1;
				if(videoEntityList.size() == 0)
				{
					listView.setVisibility(View.GONE);
					emptyLinearLayout.setVisibility(View.VISIBLE);
					refreshButton.setVisibility(View.VISIBLE);
					textInFo.setText(R.string.movie_access_network_error);
				
				}else
				{
					listView.setVisibility(View.VISIBLE);
					emptyLinearLayout.setVisibility(View.GONE);
					listViewFooter.setVisibility(View.GONE);
					Toast.makeText(MovieItemsActivity.this, R.string.movie_access_network_error, Toast.LENGTH_LONG).show();
				}
			}
			
		};
	};
	
	//网络访问监听器
	private GetResponseListener<VideoDetailEntity> listener = new GetResponseListener<VideoDetailEntity>() {

		@Override
		public void onSuccess(ResponseEntity<VideoDetailEntity> result) {
			
			List<VideoDetailEntity> tempList=(List<VideoDetailEntity>) result.getContent();
			videoEntityList.addAll(tempList);
			
			Message msg=new Message();
			msg.arg1=ACCESS_NET_OK;
			totalCount=result.getTotalCount();
			totalPageCount = (totalCount + DEFAULT_COUNT - 1) / DEFAULT_COUNT;
			handler.sendMessage(msg);
			
            
		}

		@Override
		public void onError(ResponseEntity<VideoDetailEntity> result) {
			Message msg=new Message();
			msg.arg1=ACCESS_NET_FAILED;
			handler.sendMessage(msg);
			
		}
	};
	
	private GetAllVideoByTypeRequest request;//网络访问器
	
	private static final int ACCESS_NET_OK = 1;// 访问网络成功
	private static final int ACCESS_NET_FAILED = -1;// 访问网络失败
	
	//访问网络
	private void getMovieDetails()
	{
		User user=AppConfiguration.getUser(this);
		
		if(user!=null)
		{
		currentPage+=1;
		if(currentPage<=totalPageCount)
		{
//		progressBar.setVisibility(View.VISIBLE);
//		backButton.setVisibility(View.GONE);
		if(videoEntityList.size()==0)
		{
		    loadingLayout.setVisibility(View.VISIBLE);
		    refreshLayout.setVisibility(View.GONE);
			listView.setVisibility(View.GONE);
			emptyLinearLayout.setVisibility(View.VISIBLE);
		}else
		{
			listView.setVisibility(View.VISIBLE);
			emptyLinearLayout.setVisibility(View.GONE);
			listViewFooter.setVisibility(View.VISIBLE);
		}
		
		 
		request=new GetAllVideoByTypeRequest(listener,  user.getSessionId(),videoTypeID, currentPage, DEFAULT_COUNT);
		//request=new GetAllVideoByTypeRequest(listener, videoTypeId, pageIndex, pageSize)
		request.sendByThread();
		}else
		{
			listViewFooter.setVisibility(View.VISIBLE);
			footerProgressBar.setVisibility(View.GONE);
			footerTextTip.setText(R.string.data_complete_load);
			//Toast.makeText(this, R.string.load_accomplished, Toast.LENGTH_LONG).show();
		}
		
		}
	}
	
	@Override
	protected void onStop() {

		if(request !=null )
		{
			request.cancel();
		}
		
		adapter.setReception(false);
		unregisterReceiver(netWorkObserver);
		super.onStop();
	}
	
	
	
	private final static int MOTION_UP = 0x200;// 向上滑动
	private final static int MOTION_DOWN = 0x201;// 向下滑动
	private final static int MOTION_NONE = 0x202; //无运动
	private final static float MIN_VELOCITY = 150f; //最小速度
	private final static float MIN_DISTANSE = 150f;  //最小距离
	private int motionDiretion = 0;
	
	
	// 滑动监测器
	private GestureDetector gestureDetector = new GestureDetector(
			new ScrollGestureDectorListener());

	/**
	 * 滑动动方向和速度监测
	 * 
	 * 
	 */
	class ScrollGestureDectorListener extends SimpleOnGestureListener {

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			float distanse = e1.getY() - e2.getY();

			if (distanse > MIN_DISTANSE || (velocityY<0&&Math.abs(velocityY) > MIN_VELOCITY)) {
				motionDiretion = MOTION_DOWN;
			} else {
				motionDiretion = MOTION_NONE;
			}

			return super.onFling(e1, e2, velocityX, velocityY);
		}

	}
	
	
	// 滚动监听器
	private int containerCount;
	private AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
				int lastPosition = view.getLastVisiblePosition();
				if(containerCount == lastPosition+1 && motionDiretion == MOTION_DOWN)
				{
					getMovieDetails();
				}
			}

		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			containerCount=totalItemCount;

		}
	};
	
	
	/**
	 * lsitView 添加脚
	 */
	private LinearLayout listViewFooter; 
	private ProgressBar  footerProgressBar;
	private TextView footerTextTip;
	
	private void addFooter()
	{
		LayoutInflater inflater=getLayoutInflater();
		listViewFooter=(LinearLayout) inflater.inflate(R.layout.listview_footer, null);
		footerProgressBar=(ProgressBar)listViewFooter.findViewById(R.id.footer_progressbar);
		footerTextTip=(TextView)listViewFooter.findViewById(R.id.footer_text_tip);
		listView.addFooterView(listViewFooter);
		listViewFooter.setVisibility(View.GONE);
	}
}
