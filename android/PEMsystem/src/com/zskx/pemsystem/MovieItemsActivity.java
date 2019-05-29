package com.zskx.pemsystem;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zskx.net.request.GetAllVideoByTypeRequest;
import com.zskx.net.request.AbstractRequest.GetResponseListener;
import com.zskx.net.response.ResponseEntity;
import com.zskx.net.response.VideoDetailEntity;
import com.zskx.net.response.VideoTypeEntity;
import com.zskx.pemsystem.MovieTypeActivity.ScrollGestureDectorListener;
import com.zskx.pemsystem.adpater.MovieItemAdapter;
import com.zskx.pemsystem.util.AppConfiguration;
import com.zskx.pemsystem.util.ScreenManager;
import com.zskx.pemsystem.util.StringsUtil;

public class MovieItemsActivity extends MenuActivity{
	
	
	private GridView gridView;
	private Button backButton;  //返回按钮
	private TextView textTitle;//  类型标题
	private ProgressBar progressBar;// 加载进度
	private TextView textInFo;//  信息提示
	private Button refreshButton;//刷新按钮
	private LinearLayout emptyLinearLayout;//提示界面
	
	private boolean isFirstRequest=true;//是不是第一次访问
	private VideoTypeEntity videoTypeEntity;//视频类型
	private String videoTypeID="";//类型 ID 
	
	private MovieItemAdapter adapter; //适配器
	
	public final static String VIDEOENTITYDATAIL_URL="videoEntityDatail_url";
	
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
	
	/**
	 * 初始化组件
	 */
	private void initComponent()
	{
		
		Intent intent=getIntent();
		
		videoTypeEntity=(VideoTypeEntity) intent.getSerializableExtra(MovieTypeActivity.MOVIE_SUB_SERIES_KEY);
		
		backButton=(Button)findViewById(R.id.btn_back);
		textTitle=(TextView)findViewById(R.id.textView_type_title);
		progressBar=(ProgressBar)findViewById(R.id.load_progressBar);
		
		gridView=(GridView) findViewById(R.id.gridView);
	
		
		emptyLinearLayout=(LinearLayout)findViewById(R.id.linear_empty);
		textInFo=(TextView)findViewById(R.id.textView_access_tip_info);
		refreshButton=(Button)findViewById(R.id.refresh_button);
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
		
		videoTypeID=videoTypeEntity.getVideoTypeId();
		
		String title=videoTypeEntity.getVideoTypeTitle();
		
		if(title !=null)
		{
			textTitle.setText(StringsUtil.subString(title, 7));
		}
		
		backButton.setOnClickListener(clickListener);
		refreshButton.setOnClickListener(clickListener);
		
		adapter=new MovieItemAdapter(getApplicationContext(),videoEntityList);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			Intent intent=new Intent(MovieItemsActivity.this, VideoPalyerActivity.class);
			intent.putExtra(VIDEOENTITYDATAIL_URL, videoEntityList.get(position).getVideoUrl());
			startActivity(intent);
				
			}
		});
		
		
		gridView.setOnScrollListener(scrollListener);
		gridView.setOnTouchListener(new OnTouchListener() {
			
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
				
				emptyLinearLayout.setVisibility(View.GONE);
				getMovieDetails();
				
				
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
		
			progressBar.setVisibility(View.GONE);
			adapter.notifyDataSetChanged();
			if(msg.arg1 == ACCESS_NET_OK)
			{
				if(videoEntityList.size() == 0)
				{
					gridView.setVisibility(View.GONE);
					emptyLinearLayout.setVisibility(View.VISIBLE);
					refreshButton.setVisibility(View.GONE);
					textInFo.setText(R.string.video_is_empty);
				}else
				{
					gridView.setVisibility(View.VISIBLE);
					emptyLinearLayout.setVisibility(View.GONE);
					refreshButton.setVisibility(View.VISIBLE);
				}
				
			}else if (msg.arg1 == ACCESS_NET_FAILED)
			{
				if(videoEntityList.size() == 0)
				{
					gridView.setVisibility(View.GONE);
					emptyLinearLayout.setVisibility(View.VISIBLE);
					refreshButton.setVisibility(View.GONE);
					textInFo.setText(R.string.movie_access_network_error);
				
				}else
				{
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
			currentPage-=1;
		}
	};
	
	private GetAllVideoByTypeRequest request;//网络访问器
	
	private static final int ACCESS_NET_OK = 1;// 访问网络成功
	private static final int ACCESS_NET_FAILED = -1;// 访问网络失败
	
	//访问网络
	private void getMovieDetails()
	{
		
		currentPage+=1;
		if(currentPage<=totalPageCount)
		{
		progressBar.setVisibility(View.VISIBLE);
		if(isFirstRequest)
		{
			gridView.setVisibility(View.GONE);
			emptyLinearLayout.setVisibility(View.GONE);
		}
		
		System.out.println("user.sessionId:------>"+AppConfiguration.user.getSessionId());
		request=new GetAllVideoByTypeRequest(listener,  AppConfiguration.user.getSessionId(),videoTypeID, currentPage, totalPageCount);
		//request=new GetAllVideoByTypeRequest(listener, videoTypeId, pageIndex, pageSize)
		request.sendByThread();
		}
		
		
	}
	
	@Override
	protected void onStop() {

		if(request !=null )
		{
			request.cancel();
		}
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
			float distanse = e1.getX() - e2.getX();

			if (distanse > MIN_DISTANSE || Math.abs(velocityY) > MIN_VELOCITY) {
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
	
}
