package com.zskx.pemsystem;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import com.zskx.net.request.AbstractRequest.GetResponseListener;
import com.zskx.net.request.GetAllReportByUserIdRequest;
import com.zskx.net.response.ReportEntity;
import com.zskx.net.response.ResponseEntity;
import com.zskx.pemsystem.util.AppConfiguration;
import com.zskx.pemsystem.util.ScreenManager;
import com.zskx.pemsystem.util.StringsUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;

import android.widget.Toast;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ReportListActivity extends MenuActivity {
	private ListView listView_Items;// 请求列表
	private LayoutInflater mInflater;// 解析XML文件
	private Button backButton;

	private Button refreshButton;// 数据显示为空时的刷新按钮
	private LinearLayout linear_empty;// 数据为空的界面
	private TextView empty_info; // 无数据或者网络访问失败

	private TextView textTotalCount;
	private ProgressBar progressBar;
	

	private boolean isLoading = false;// 是否正在加载数据

	public static final String REPORTITEM_ID = "report_id";// 传递值时的 key

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ScreenManager.getScreenManager().pushActivity(this);
		setContentView(R.layout.report_list);
		intiComponent();
		getReportList();
	}

	private ItemsAdapter adapter = new ItemsAdapter();

	/**
	 * 初始化组件
	 */
	private void intiComponent() {

		mInflater = getLayoutInflater();
		progressBar=(ProgressBar)findViewById(R.id.load_progressBar);

		linear_empty = (LinearLayout) findViewById(R.id.linear_empty);
		refreshButton = (Button) findViewById(R.id.refresh_button);
		refreshButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				getReportList();
				listView_Items.setVisibility(View.VISIBLE);

			}
		});
		empty_info = (TextView) findViewById(R.id.textView_access_tip_info);

		textTotalCount = (TextView) findViewById(R.id.text_total_account);

		backButton = (Button) findViewById(R.id.btn_back);

		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ReportListActivity.this.finish();
				
			}
		});

		listView_Items = (ListView) findViewById(R.id.listView_item);
		listView_Items.setAdapter(adapter);
		listView_Items.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Intent intent = new Intent();
				intent.setClass(getApplicationContext(),
						ReportDetailsActivity.class);
				intent.putExtra(REPORTITEM_ID, reportEntityList.get(arg2).getReportId());

				startActivity(intent);

			}
		});

		listView_Items.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				return gestureDetector.onTouchEvent(event);
			}
		});
		listView_Items.setLongClickable(true);

		listView_Items.setOnScrollListener(scrollListener);

	}

	/*************************************************************************************************************/

	private List<ReportEntity> reportEntityList = new ArrayList<ReportEntity>();// 报告集合

	private int totalCount = 0;
	private final static int DEFAULT_COUNT = 30;// 每页默认每页数据条数
	private int totalPageCount = 1;//  第一次访问，必须设为 1
	private int currentPage = 0;

	// 接受消息，处理 UI
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg.arg1 == ACCESS_NET_OK) {
				if (reportEntityList.size() == 0) {
					listView_Items.setVisibility(View.GONE);
					empty_info.setText(getResources().getString(
							R.string.zearo_count));
					refreshButton.setVisibility(View.GONE);
				}

				String total_count = String.format(
						getResources().getString(R.string.report_account),
						msg.arg2);
				textTotalCount.setText(total_count);
				
				progressBar.setVisibility(View.GONE);
				textTotalCount.setVisibility(View.VISIBLE);

			} else if (msg.arg1 == ACCESS_NET_FAILED) {
				if (reportEntityList.size() == 0) {
					listView_Items.setVisibility(View.GONE);
					empty_info.setText(getResources().getString(
							R.string.access_network_failed));
				}
				progressBar.setVisibility(View.GONE);
				textTotalCount.setVisibility(View.VISIBLE);
//				String str = getResources().getString(
//						R.string.access_network_failed);
				currentPage=currentPage-1;
				Toast.makeText(ReportListActivity.this, (String)msg.obj, Toast.LENGTH_LONG)
						.show();
			}
			
			adapter.notifyDataSetChanged();
			isLoading = false;

		};
	};

	private static final int ACCESS_NET_OK = 1;// 访问网络成功
	private static final int ACCESS_NET_FAILED = -1;// 访问网络失败

	// 访问监听器
	private GetResponseListener<ReportEntity> listener = new GetResponseListener<ReportEntity>() {

		@Override
		public void onSuccess(ResponseEntity<ReportEntity> result) {

			reportEntityList.addAll(result.getContent());
			totalCount = result.getTotalCount();
			totalPageCount = (totalCount + DEFAULT_COUNT - 1) / DEFAULT_COUNT;

			Message msg = new Message();
			msg.arg1 = ACCESS_NET_OK;
			msg.arg2 = totalCount;
			handler.sendMessage(msg);

		}

		@Override
		public void onError(ResponseEntity<ReportEntity> result) {

			//reportEntityList.addAll(result.getContent());
			Message msg = new Message();
			msg.arg1 = ACCESS_NET_FAILED;
			msg.obj=result.getMsg();
			handler.sendMessage(msg);
		}
	};

	GetAllReportByUserIdRequest request = null;

	// 得到报告列表
	private void getReportList() {
		if (!isLoading) {
		//	System.out.println("accessing network!.......");
			currentPage=currentPage+1;
			if (currentPage <= totalPageCount) {
				
				isLoading = true;
				request = new GetAllReportByUserIdRequest(listener, AppConfiguration.user.getSessionId(),
						currentPage, DEFAULT_COUNT);
				progressBar.setVisibility(View.VISIBLE);
				textTotalCount.setVisibility(View.GONE);
				request.sendByThread();
			}else
			{
				Toast.makeText(this, R.string.loaded_finish, Toast.LENGTH_LONG).show();
			}

		}

	}

	/**
	 * 控件标记类
	 * 
	 * 
	 */
	class Holder {
		public static final int TITLE_MAX_LENGTH = 10;// 报告标题最大长度
		public static final int CONTENT_MAX_LENGTH = 35;// 报告内容最大长度
		public ImageView report_image;// 图标
		public TextView title;// 标题
		public TextView subContent;// 内容简介
		public TextView date;// 报告日期
	}

	// 列表适配器
	class ItemsAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			return reportEntityList.size();

		}

		@Override
		public Object getItem(int position) {

			return reportEntityList.get(position);

		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			final Holder holder;

			if (convertView != null) {
				holder = (Holder) convertView.getTag();
			} else {
				final LinearLayout lay = (LinearLayout) mInflater.inflate(
						R.layout.report_list_item, null);
				holder = new Holder();
				holder.report_image = (ImageView) lay
						.findViewById(R.id.imageView_report);
				holder.title = (TextView) lay
						.findViewById(R.id.textView_item_title);
				holder.subContent = (TextView)lay.findViewById(R.id.textView_item_content);
				holder.date = (TextView) lay.findViewById(R.id.textView_date);
				convertView = lay;
			}

			holder.report_image
					.setImageResource(R.drawable.report_list_item_pic);

			String title = reportEntityList.get(position).getReportTitle();
			title = StringsUtil.subString(title, Holder.TITLE_MAX_LENGTH);
			holder.title.setText(title);

			String comment = reportEntityList.get(position).getReportInstroduction();
			comment = StringsUtil.subString(comment, Holder.CONTENT_MAX_LENGTH);
			holder.subContent.setText(comment);

			Date date = reportEntityList.get(position).getReportTime();
			String time = StringsUtil.formatDate(date);
			holder.date.setText(time);

			convertView.setTag(holder);

			return convertView;
		}

	}

	@Override
	protected void onStop() {

		super.onStop();
		if (request != null) {
			request.cancel();
		}
	}

	private final static int MOTION_UP = 0x200;// 向上滑动
	private final static int MOTION_DOWN = 0x201;// 向下滑动
	private final static int MOTION_NONE = 0x202;
	private final static float MIN_VELOCITY = 100f;
	private final static float MIN_DISTANSE = 80f;
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

	private int containerCount;
	private AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
				int lastPosition = view.getLastVisiblePosition();
				if(containerCount == lastPosition+1 && motionDiretion == MOTION_DOWN)
				{
					getReportList();
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
