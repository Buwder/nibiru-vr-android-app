package com.zskx.pemsystem;


import java.util.ArrayList;
import java.util.List;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.zskx.net.request.GetAllVideoTypeRequest;
import com.zskx.net.request.AbstractRequest.GetResponseListener;
import com.zskx.net.response.ResponseEntity;
import com.zskx.net.response.VideoTypeEntity;
import com.zskx.net.response.user.User;
import com.zskx.pemsystem.adpater.MovieTypeAdapter;
import com.zskx.pemsystem.util.AppConfiguration;
import com.zskx.pemsystem.util.EnumInterventionResourceType;
import com.zskx.pemsystem.util.ScreenManager;
import com.zskx.pemsystem.util.ShowNotification;

/**
 * 
 * 视频类型目录
 *
 *
 */
public class MovieTypeActivity extends MenuActivity{

	private ListView movieTypeListView; //视频列表组
	
	private LinearLayout linearDataEmpty;
	private LinearLayout refreshLayout;  //刷新区域
	private LinearLayout loadingLayout;  //加载提示区域
	
	private List<VideoTypeEntity> movieTypeList=new ArrayList<VideoTypeEntity>();// 视频类型集合
	private MovieTypeAdapter movieTypeAdapter;
	
	private Button backButton;// 返回按钮
	private Button homeButton;
	//private ProgressBar progressBar;//进度加载条
	private TextView textTitle; //标题
	private TextView textErrorInfo; //错误或数据为空时的信息
	private Button refreshButton;    //刷新按钮
	private int series_id=0;
	
	private String series;
	
	private String videoSeries;
	private boolean isFirstRequest=true;
	
	public static final String MOVIE_SUB_SERIES_KEY="movie_sub_series_key";
	public static final String MOVIE_TYPE_SERIES_KEY="movie_type_series_key";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ScreenManager.getScreenManager().pushActivity(this);
		setContentView(R.layout.movie_type_list);
		initComponent();
		initDataOrEvent();
		getMovieSeries();
		isFirstRequest=false;
		
		
	}
	
	
	
	
	
	/*
	 * 实例化组件
	 */
	private void initComponent()
	{
		movieTypeListView=(ListView)findViewById(R.id.listView_item);
		backButton=(Button)findViewById(R.id.btn_back);
		homeButton=(Button)findViewById(R.id.home_btn);
	//	progressBar=(ProgressBar)findViewById(R.id.load_progressBar);
		textTitle=(TextView)findViewById(R.id.text_title);
		
		linearDataEmpty=(LinearLayout)findViewById(R.id.linear_empty);
		loadingLayout=(LinearLayout)findViewById(R.id.linearLayout_loading);
		refreshLayout=(LinearLayout)findViewById(R.id.linearLayout_refresh);
		textErrorInfo=(TextView)findViewById(R.id.textView_access_tip_info);
		refreshButton=(Button)findViewById(R.id.refresh_button);
	}
	
	//初始化组件数据和事件
	private void initDataOrEvent()
	{
		
		
		backButton.setOnClickListener(clickListener);
		homeButton.setOnClickListener(clickListener);
		refreshButton.setOnClickListener(clickListener);
		
		series_id=getIntent().getIntExtra(HomeActivity.SERIES_KEY, 0);
		
		
		if(series_id == HomeActivity.PSYCHOLOGICAL_SERIES_ID)
		{
			series=getResources().getString(R.string.movie_type_mental_training);
			textTitle.setText(getResources().getString(R.string.movie_type_mental_training));
			videoSeries=EnumInterventionResourceType.TRAINANIME.getValue();
			
		}else if(series_id == HomeActivity.DECOMPRESSION_SERIES_ID)
		{
			series=getResources().getString(R.string.movie_type_decompression);
			textTitle.setText(getResources().getString(R.string.movie_type_decompression));
			videoSeries=EnumInterventionResourceType.DECOMPRESSANIME.getValue();
			
		}
		
		addFooter();
		movieTypeAdapter=new MovieTypeAdapter(MovieTypeActivity.this, movieTypeList);
		
		movieTypeListView.setAdapter(movieTypeAdapter);
		
		movieTypeListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				

				if(position<movieTypeList.size())
				{
				Intent intent=new Intent(MovieTypeActivity.this,MovieItemsActivity.class);
				intent.putExtra(MOVIE_SUB_SERIES_KEY,movieTypeList.get(position));
				intent.putExtra(MOVIE_TYPE_SERIES_KEY, series);
				startActivity(intent);
				
				}
			}
		});
	
		movieTypeListView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
		
				return gestureDetector.onTouchEvent(event);
			}
		});
		
		movieTypeListView.setOnScrollListener(scrollListener);
		
	}
	
	
	// 初始化 Button 点击事件
	
	private View.OnClickListener clickListener=new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.btn_back)
			{
				
				MovieTypeActivity.this.finish();
				
			}else if(v.getId() == R.id.refresh_button)
			{
//				progressBar.setVisibility(View.VISIBLE);
//				backButton.setVisibility(View.GONE);
				getMovieSeries();
			}else if(v.getId()==R.id.home_btn)
			{
				Intent intent=new Intent(MovieTypeActivity.this,HomeActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
			
		}
	};
	
	
	private final static int MOTION_UP = 0x200;// 向上滑动
	private final static int MOTION_DOWN = 0x201;// 向下滑动
	private final static int MOTION_NONE = 0x202; //无运动
	private final static float MIN_VELOCITY = 100f; //最小速度
	private final static float MIN_DISTANSE = 80f;  //最小距离
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
					//view.get
					getMovieSeries();
				}
			}

		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			containerCount=totalItemCount;

		}
	};
	
	
	
	
	private int totalCount = 0;
	private final static int DEFAULT_COUNT = 30;// 每页默认每页数据条数
	private int totalPageCount = 1;//  第一次访问，必须设为 1
	private int currentPage = 0;
	
	// 接受消息，处理 UI
	private Handler handler=new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			
			loadingLayout.setVisibility(View.GONE);
			refreshLayout.setVisibility(View.VISIBLE);
			if(msg.arg1 == ACCESS_NET_OK)
			{
				
				if(movieTypeList.size()==0)
				{
					movieTypeListView.setVisibility(View.GONE);
					
					refreshButton.setVisibility(View.GONE);
					textErrorInfo.setText(R.string.movie_series_no_data);
					
					linearDataEmpty.setVisibility(View.VISIBLE);
					movieTypeListView.setVisibility(View.GONE);
				}else
				{
					movieTypeListView.setVisibility(View.VISIBLE);
					linearDataEmpty.setVisibility(View.GONE);

				}
				
				
			}else if(msg.arg1 == ACCESS_NET_FAILED)
			{
			
				currentPage-=1;
				System.out.println("currentPage----------->"+currentPage);
				if(movieTypeList.size()==0)
				{
				movieTypeListView.setVisibility(View.GONE);
				
//				progressBar.setVisibility(View.GONE);
//				backButton.setVisibility(View.VISIBLE);
				refreshButton.setVisibility(View.VISIBLE);
				textErrorInfo.setText(R.string.movie_series_access_network_failed);
				linearDataEmpty.setVisibility(View.VISIBLE);
				
				}
				else
				{
					movieTypeListView.setVisibility(View.VISIBLE);
					linearDataEmpty.setVisibility(View.GONE);
					listViewFooter.setVisibility(View.GONE);
					Toast.makeText(MovieTypeActivity.this, R.string.movie_series_access_network_failed,Toast.LENGTH_LONG).show();
				}
				
			}
			movieTypeAdapter.notifyDataSetChanged();
		};
	};
	/**
	 * 网络访问监听器
	 */
	private GetResponseListener<VideoTypeEntity> listener =new GetResponseListener<VideoTypeEntity>() {

		@Override
		public void onSuccess(ResponseEntity<VideoTypeEntity> result) {
			
			ArrayList<VideoTypeEntity> list= (ArrayList<VideoTypeEntity>)result.getContent();
			
			movieTypeList.addAll(list);
			Message msg=new Message();
			msg.arg1=ACCESS_NET_OK;
			totalCount=result.getTotalCount();
			totalPageCount = (totalCount + DEFAULT_COUNT - 1) / DEFAULT_COUNT;
			handler.sendMessage(msg);
			
			
		}

		@Override
		public void onError(ResponseEntity<VideoTypeEntity> result) {
			
			System.out.println("onError----------->call");
			Message msg = new Message();
			msg.arg1 = ACCESS_NET_FAILED;
			handler.sendMessage(msg);
			
		}
	};
	
	
	
	
	//网络访问器
	private GetAllVideoTypeRequest request;
	
	private static final int ACCESS_NET_OK = 1;// 访问网络成功
	private static final int ACCESS_NET_FAILED = -1;// 访问网络失败
	/**
	 * 得到视频类型列表
	 */
	private void getMovieSeries()
	{
		User user=AppConfiguration.getUser(this);
		
		if(user !=null)
		{
		currentPage=currentPage+1;
		if (currentPage <= totalPageCount) {
//		progressBar.setVisibility(View.VISIBLE);
//		backButton.setVisibility(View.GONE);
		if(movieTypeList.size()==0)
		{
			movieTypeListView.setVisibility(View.GONE);
			linearDataEmpty.setVisibility(View.VISIBLE);
			loadingLayout.setVisibility(View.VISIBLE);
			refreshLayout.setVisibility(View.GONE);
		}else
		{
			movieTypeListView.setVisibility(View.VISIBLE);
			linearDataEmpty.setVisibility(View.GONE);
			listViewFooter.setVisibility(View.VISIBLE);
		}
		
		request=new GetAllVideoTypeRequest(listener, user.getSessionId(), videoSeries, currentPage, DEFAULT_COUNT);
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ShowNotification.showNotification(this,"PEM", series, MovieTypeActivity.class,null);
		
	}
	
	@Override
	protected void onStop() {
	
		request.cancel();
		movieTypeAdapter.setReception(false);
		super.onStop();
	}

	
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
		movieTypeListView.addFooterView(listViewFooter);
		listViewFooter.setVisibility(View.GONE);
	}
	
}
