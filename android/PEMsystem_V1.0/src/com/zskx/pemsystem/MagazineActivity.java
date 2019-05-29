package com.zskx.pemsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zskx.net.request.AbstractRequest.GetResponseListener;
import com.zskx.net.request.GetAllMagazineByTypeRequest;
import com.zskx.net.request.GetAllMagazineTypeRequest;
import com.zskx.net.response.MagazineDetailEntity;
import com.zskx.net.response.MagazineTypeEntity;
import com.zskx.net.response.ResponseEntity;
import com.zskx.pemsystem.adpater.MagazineAdapter;
import com.zskx.pemsystem.util.AppConfiguration;
import com.zskx.pemsystem.util.Common_Title;
import com.zskx.pemsystem.util.ScreenManager;
import com.zskx.pemsystem.util.ShowNotification;

/**
 * 
 * @author demo
 * 
 */
public class MagazineActivity extends MenuActivity {

	private final String TAG = "magazine";

	private int default_max_type_num = 4;

	private Common_Title title;

	private LinearLayout type_tab;

	private HorizontalScrollView type_tab_container;

	// private FrameLayout container;

	private int type_tab_width;

	private TextView wrong_tip;

	private ProgressBar progressBar;

	// private ListView magazine_list;

	private FrameLayout shelf_container;

	private static int PAGE_SIZE = 4;

	private HashMap<String, Integer> type_page = new HashMap<String, Integer>();

	private HashMap<String, Integer> type_page_total = new HashMap<String, Integer>();


	private ArrayList<MagazineTypeEntity> type_data;

	/** 储存 类型对应的杂志数据 */
	private HashMap<String, ArrayList<MagazineDetailEntity>> data_map = new HashMap<String, ArrayList<MagazineDetailEntity>>();

	/** 请求是否 在发送过程中或是已经得到结果 */
	private HashMap<String, Boolean> request_map = new HashMap<String, Boolean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		ScreenManager.getScreenManager().pushActivity(this);
		setContentView(R.layout.magazine_activity);

		getViewById();

		getTypeDataByNet();
	}

	@Override
	protected void onResume() {
		ShowNotification.showNotification(this, "积极心理", "可以查看不同类型杂志",
				this.getClass(), null);
		super.onResume();
	}

	private void setClickListener() {
		OnItemClickListener itemListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO 点击跳转
				Intent intent = new Intent(MagazineActivity.this,
						MagazineShowActivity.class);
				HeaderViewListAdapter hvla = (HeaderViewListAdapter) currentListView
						.getAdapter();
				MagazineAdapter ma = (MagazineAdapter) hvla.getWrappedAdapter();
				intent.putExtra("content", ma.getBindData().get(position)
						.getMobileMagazineInformations());
				intent.putExtra("title", ma.getBindData().get(position)
						.getMagazineTitle());
				intent.putExtra("summary", ma.getBindData().get(position)
						.getMagazineSummary());
				MagazineActivity.this.startActivity(intent);

			}
		};

		OnScrollListener scrollListener = new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (visibleItemCount > 0) {
					if (view.getLastVisiblePosition()+1 >= totalItemCount) {

						String id = currentTab.getTag().toString();
						int pageNum = type_page.get(id);
						Integer pageTotalNum = type_page_total.get(id);
						if (pageTotalNum != null) {
							if (pageNum * PAGE_SIZE < pageTotalNum) {
								if(!(pageNum * PAGE_SIZE > data_map.get(id).size())){
									Log.i(TAG, "翻頁加載！");
									type_page.put(id, pageNum + 1);
									getMagazineDataByTypeFromNet(id,false);
									Toast.makeText(MagazineActivity.this, "加载下一页！", 800).show();
								}
								
							}
						}
					}
				}
			}
		};

		for (int i = 0; i < shelf_container.getChildCount(); i++) {
			View v = shelf_container.getChildAt(i);
			((ListView) v).setOnItemClickListener(itemListener);
			((ListView) v).setOnScrollListener(scrollListener);
		}
	}

	/**
	 * 得到view对象
	 */
	public void getViewById() {
		title = (Common_Title) findViewById(R.magazine.title_bar);
		type_tab = (LinearLayout) findViewById(R.magazine.type_tab);
		type_tab_container = (HorizontalScrollView) findViewById(R.magazine.type_scroll);
		shelf_container = (FrameLayout) findViewById(R.magazine.shelf_container);
		wrong_tip = (TextView) findViewById(R.magazine.data_wrong_tip);
		progressBar = (ProgressBar) findViewById(R.magazine.progressBar);
		// magazine_list = (ListView) findViewById(R.magazine.magazine_shelf);
	}

	/**
	 * 从网络得到杂志类型
	 */
	private void getTypeDataByNet() {
		GetAllMagazineTypeRequest request = new GetAllMagazineTypeRequest(
				new GetResponseListener<MagazineTypeEntity>() {

					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(
							ResponseEntity<MagazineTypeEntity> result) {
						title.stopShowProgress();
						setTypeListDataRight();
						type_data = (ArrayList<MagazineTypeEntity>) result.getContent();
						if (type_data.size() != 0) {
							initTypeTabUI(type_data.size());
						} else {
							setAllDataWrong();
						}
					}

					@Override
					public void onError(
							ResponseEntity<MagazineTypeEntity> result) {
						title.stopShowProgress();
						setAllDataWrong();
					}
				}, AppConfiguration.getUser(this).getSessionId(), 0, PAGE_SIZE);
		request.sendByThread();
		title.startShowProgress();
	}

	/**
	 * 得到类型 对应杂志
	 * 
	 * @param magazineTypeId
	 */
	private void getMagazineDataByTypeFromNet(final String magazineTypeId,Boolean isLoadUi) {
		if(isLoadUi){
			setShelfProgressBackground();
		}
		
		GetAllMagazineByTypeRequest request = new GetAllMagazineByTypeRequest(
				new GetResponseListener<MagazineDetailEntity>() {

					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(
							ResponseEntity<MagazineDetailEntity> result) {
						Integer totalNum = type_page_total.get(magazineTypeId);
						if (totalNum == null) {
							type_page_total.put(magazineTypeId,
									result.getTotalCount());
						}

						ArrayList<MagazineDetailEntity> temp = (ArrayList<MagazineDetailEntity>) result
								.getContent();
						ArrayList<MagazineDetailEntity> old_data = data_map
								.get(magazineTypeId);
						if (old_data != null) {
							old_data.addAll(temp);
						} else {
							data_map.put(magazineTypeId, temp);
						}

						// data_map.put(magazineTypeId,
						// (ArrayList<MagazineDetailEntity>) result
						// .getContent());
						// 请求完毕

						request_map.put(magazineTypeId, true);

						updateShelfUI(magazineTypeId);
					}

					@Override
					public void onError(
							ResponseEntity<MagazineDetailEntity> result) {
						ArrayList<MagazineDetailEntity> old_data = data_map
								.get(magazineTypeId);
						if (old_data == null) {
							data_map.put(magazineTypeId, null);
						}

						// 请求完毕

						request_map.put(magazineTypeId, true);

						int pageNum = type_page.get(magazineTypeId);
						if (pageNum > 1)
							type_page.put(magazineTypeId, pageNum - 1);
						updateShelfUI(magazineTypeId);
					}
				}, AppConfiguration.getUser(this).getSessionId(),
				magazineTypeId, type_page.get(magazineTypeId), PAGE_SIZE);

		request.sendByThread();
		// title.startShowProgress();
		/** 把 typeId 当作 key值 放入 图表中 表示这个网络请求正在执行 */
		Boolean b = request_map.get(magazineTypeId);
		if (b == null) {
			request_map.put(magazineTypeId, false);
		}

	}

	/**
	 * 更新 书架界面
	 * 
	 * @param typeId
	 */
	public void updateShelfUI(String typeId) {

		Boolean isCurrentTagData = (currentTab.getTag().equals(typeId));
		if (isCurrentTagData) {
			Boolean isOverNetJob = request_map.get(typeId);
			// 这个类型的 杂志发送的网络请求已经完毕
			if (isOverNetJob) {
				ArrayList<MagazineDetailEntity> list_current_data = data_map
						.get(typeId);
				if (list_current_data != null && list_current_data.size() != 0) {
					// TODO 更新 list的界面
					setShelfDataRight(typeId);
					// 判断是否有了适配器
					if (currentListView.getAdapter() == null) {
						View view = new View(this);
						view.setBackgroundResource(R.drawable.magazine_bookshelf_item_backgroud);
						view.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Log.i(TAG, "這是用來使 最後一個item不能選中！");
							}
						});
						currentListView.addFooterView(view);

						currentListView.setAdapter(new MagazineAdapter(
								list_current_data, this));

					} else {
						// 如果有header或footer，得到的是HeaderViewListAdapter。
						HeaderViewListAdapter listAdapter = (HeaderViewListAdapter) currentListView
								.getAdapter();
						MagazineAdapter adapter = (MagazineAdapter) listAdapter
								.getWrappedAdapter();
						adapter.setBindData(list_current_data);
						adapter.notifyDataSetChanged();
					}

				} else {
					setShelfDataWrong(typeId);
				}
			} else {
				setShelfProgressBackground();
			}
		}
	}

	/**
	 * 没有获取到 杂志类别 列表 ，设置提示信息
	 */
	public void setAllDataWrong() {
		progressBar.setVisibility(View.GONE);
		type_tab_container.setVisibility(View.INVISIBLE);
		shelf_container.setVisibility(View.INVISIBLE);
		wrong_tip.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置 书架 内容为错误
	 */
	public void setShelfDataWrong(String typeId) {
		progressBar.setVisibility(View.GONE);
		currentListView.setVisibility(View.INVISIBLE);
		wrong_tip.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置 类型数据为正确
	 */
	public void setTypeListDataRight() {
		progressBar.setVisibility(View.GONE);
		wrong_tip.setVisibility(View.INVISIBLE);
		type_tab_container.setVisibility(View.VISIBLE);

	}

	/**
	 * 设置 杂志数据为正确
	 */
	public void setShelfDataRight(String typeId) {
		progressBar.setVisibility(View.GONE);
		wrong_tip.setVisibility(View.INVISIBLE);
		currentListView.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置 显示内容为空
	 */
	public void setShelfProgressBackground() {
		wrong_tip.setVisibility(View.INVISIBLE);
		currentListView.setVisibility(View.INVISIBLE);
		progressBar.setVisibility(View.VISIBLE);
	}

	/**
	 * 初始化杂志类型界面
	 * 
	 * @param type_num
	 */
	private void initTypeTabUI(int type_num) {
		// 得到最杂志类型标签宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		if (type_num > default_max_type_num) {
			type_tab_width = dm.widthPixels / default_max_type_num;
		} else {
			type_tab_width = dm.widthPixels / type_num;
		}

		for (int i = 0; i < type_num; i++) {
			addTabAndShelf(type_data.get(i).getMagazineTypeTitle(), type_data
					.get(i).getMagazineTypeId());
			// 将每个类型到第一页设置。
			type_page.put(type_data.get(i).getMagazineTypeId(), 1);
		}
		setClickListener();
		setCurrentTab((TextView) type_tab.getChildAt(0));
	}

	public void addShelf(String typeId) {
		ListView lv = new ListView(this);
		lv.setTag(typeId);
		lv.setCacheColorHint(Color.TRANSPARENT);
		lv.setDivider(getResources().getDrawable(R.drawable.magazine_bookshelf));
		lv.setVisibility(View.INVISIBLE);
		shelf_container.addView(lv, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
	}

	/**
	 * 添加 杂志类型标签
	 * 
	 * @param tab_name
	 */

	public void addTabAndShelf(String tab_name, String typeId) {
		TextView tab = new TextView(this);
		tab.setTag(typeId);
		tab.setPadding(5, 5, 5, 5);
		tab.setBackgroundResource(R.drawable.magazine_tab_item_bg);
		tab.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
		tab.setTextColor(Color.GRAY);
		tab.getPaint().setFakeBoldText(true);
		tab.setGravity(Gravity.CENTER);
		tab.setText(tab_name);
		tab.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		/** 把 标签 添加到 杂志类别目录中 */
		type_tab.addView(tab, new LayoutParams(type_tab_width,
				LayoutParams.WRAP_CONTENT));

		addShelf(typeId);// 添加书架
		tab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v != currentTab) {
					resumeTabBackGound(v);
					setCurrentTab((TextView) v);
				}
			}
		});

	}

	private View currentTab = null;

	private ListView currentListView = null;

	public ListView getListViewByTypeId(String typeId) {
		for (int i = 0; i < shelf_container.getChildCount(); i++) {
			View v = shelf_container.getChildAt(i);
			if (v.getTag().equals(typeId)) {
				return (ListView) v;
			}
		}
		return null;
	}

	/**
	 * 设置被选中的 杂志类型
	 * 
	 * @param tab
	 */
	public void setCurrentTab(TextView tab) {
		tab.setBackgroundResource(R.drawable.magazine_title_current);
		tab.setTextColor(Color.BLACK);
		title.setTitleTxt(tab.getText().toString());
		currentTab = tab;
		String typeId = (String) tab.getTag();
		currentListView = getListViewByTypeId(typeId);
		currentListView.setVisibility(View.VISIBLE);
		Boolean isRequested = request_map.get(typeId);
		if (isRequested == null) {
			getMagazineDataByTypeFromNet(typeId,true);
		} else {
			updateShelfUI(typeId);
		}

	}

	/**
	 * 检测 是否在进行网络访问
	 */
	@Deprecated
	private boolean checkIsInNetJob() {
		Iterator<String> keys = request_map.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			Boolean isOverNet = request_map.get(key);
			if (!isOverNet) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 将所有的界面设置为没有选中的状态
	 */
	public void resumeTabBackGound(View tab) {
		int length = type_tab.getChildCount();
		for (int i = 0; i < length; i++) {
			ListView lv = (ListView) shelf_container.getChildAt(i);

			TextView tv = (TextView) type_tab.getChildAt(i);
			if (!tv.getTag().equals(tab.getTag())) {
				tv.setBackgroundResource(R.drawable.magazine_tab_item_bg);
				tv.setTextColor(Color.GRAY);
				Log.i(TAG, "lv:" + lv.getTag().toString());
				lv.setVisibility(View.INVISIBLE);
			}
		}
	}

}
