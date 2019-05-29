package com.zskx.pemsystem.util;


import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
/**
 * 列表下拉动作监听类
 * 需要传入一个动作接口
 * @author guokai
 */
public class ListViewListener {
	private final static String TAG = "ListViewListener";
	
	private final static int MOTION_UP = 0x200;// 向上滑动
	private final static int MOTION_DOWN = 0x201;// 向下滑动
	private final static int MOTION_NONE = 0x202;
	private final static float MIN_VELOCITY = -2000f;
	private final static float MIN_DISTANSE = 500f;
	private int motionDiretion = 0;
	
	private ListView listView;
	private GestureDetector gestureDetector;
	GetServerData getServerData;

	public ListViewListener(ListView listView ,GetServerData getServerDatas) {
		super();
		this.listView = listView;
		gestureDetector = new GestureDetector(new ScrollGestureDectorListener());
		getServerData = getServerDatas;
	}
	
	public void setListViewListeners(){
		if(listView != null) {
			
			listView.setLongClickable(true);//必须的，因为 只有这样，view才能够处理不同于Tap（轻触）的hold（即ACTION_MOVE，或者多个ACTION_DOWN)
			listView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				return gestureDetector.onTouchEvent(event);
			}
		});
			
			listView.setOnScrollListener(scrollListener);
		}
	}
	
		
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

			if (distanse > MIN_DISTANSE || velocityY < MIN_VELOCITY) {
				motionDiretion = MOTION_DOWN;
				Log.i(TAG, "onFling:::" + distanse + "     " + "MOTION_DOWN::" + motionDiretion);
			} else {
				motionDiretion = MOTION_NONE;
			}
//Log.i(TAG, "velocityY:::" + velocityY) ;
			return super.onFling(e1, e2, velocityX, velocityY);
		}

	}
	
	private int containerCount;
	private AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
		
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
				int lastPosition = view.getLastVisiblePosition();
				if(containerCount == lastPosition+1 && motionDiretion == MOTION_DOWN)
				{
					getServerData.getData();
				}
			}

		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			containerCount=totalItemCount;
		//	Log.i(TAG, "totalItemCount:::"  + totalItemCount);
		}
	};
	
	public interface GetServerData{
		public void getData();
	}
}
