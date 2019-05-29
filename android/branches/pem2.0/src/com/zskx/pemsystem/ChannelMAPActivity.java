package com.zskx.pemsystem;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.ItemizedOverlay;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKEvent;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.MKGeocoderAddressComponent;
import com.baidu.mapapi.MKPlanNode;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKRoute;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKStep;
import com.baidu.mapapi.MKSuggestionResult;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.MyLocationOverlay;
import com.baidu.mapapi.OverlayItem;
import com.baidu.mapapi.Projection;
import com.baidu.mapapi.RouteOverlay;
import com.baidu.mapapi.TransitOverlay;
import com.zskx.net.NetConfiguration;
import com.zskx.net.response.HospitalVO;
import com.zskx.pemsystem.util.Common_Title;

public class ChannelMAPActivity extends MapActivity {

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(flag_dialog) {
			if(keyCode == KeyEvent.KEYCODE_BACK){
				
//				setMapViewLayout(R.channel.title_bar);
				map_dialog.setVisibility(View.GONE);
				search_btn.setVisibility(View.VISIBLE);
				flag_dialog = false;
				return true;
			}
		}
		
		return super.onKeyDown(keyCode, event);
	}

	private static String TAG = "ChannelMAPActivity";
	private EditText mylocation1 = null;
	private EditText location2 = null;
	private Button search_bus = null;
	private Button search_drive = null;
	private Button search_btn = null;
	private Button search_back = null;
	private LinearLayout map_dialog;
	private RelativeLayout relativeLayout;
	private Common_Title common_Title;
	private boolean flag_dialog = false;
	private boolean falg_search = true;

	private MapView mapView = null;
	OverItemT overitem = null;
	private BMapManager mMapManager = null;
	private MyLocationOverlay myLocationOverlay = null; // 定位图层
	// onResume时注册此listener，onPause时需要Remove,注意此listener不是Android自带的，是百度API中的
	private LocationListener locationListener;
	private MKSearch searchModel;
	GeoPoint pt;
	String city;

	private static HospitalVO hospital;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.channel_map);

		hospital = (HospitalVO) getIntent().getSerializableExtra(
				ChannelListActivity.DETAILS);

		initViews();

		mMapManager = new BMapManager(getApplication());
		mMapManager.init(NetConfiguration.mMapKey, new MyGeneralListener());
		super.initMapActivity(mMapManager);

		// 设置启用内置的缩放控件
		mapView.setBuiltInZoomControls(true);
		// 设置在缩放动画过程中也显示overlay,默认为不绘制
		// mapView.setDrawOverlayWhenZooming(true);

		searchModel(); // 初始化搜索模块

		// 获取当前位置层
		myLocationOverlay = new MyLocationOverlay(this, mapView);
		// 将当前位置的层添加到地图底层中
		
		// 注册定位事件
		locationListener = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				if (location != null) {
					// 生成GEO类型坐标并在地图上定位到该坐标标示的地点
					pt = new GeoPoint((int) (location.getLatitude() * 1e6),
							(int) (location.getLongitude() * 1e6));
					 System.out.println("location---"+location.getLatitude()
					 +":"+location.getLongitude());
//					mapView.getController().animateTo(pt);

//					if(falg_search) setCenterPoint(pt);

					if (searchModel == null)
						searchModel = new MKSearch();

					searchModel.reverseGeocode(pt);
				}
			}
		};
		
		setClickListener();
		
mapView.getOverlays().add(myLocationOverlay);
		
	
		
		setPointOverlay();
		
	}
	/**
	 * 画出地里坐标
	 */
	private void setPointOverlay() {
		GeoPoint point = new GeoPoint((int) (hospital.getLatitude() * 1e6),
				(int) (hospital.getLongitude() * 1e6));
		mapView.getController().animateTo(point);
		mapView.getController().setCenter(point);
		mapView.getController().setZoom(12);
		
		Drawable marker = getResources().getDrawable(R.drawable.channel_iconmarka);  //得到需要标在地图上的资源
//		marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker
//				.getIntrinsicHeight());   //为maker定义位置和边界
		
		overitem = new OverItemT(marker, this, hospital.getLatitude(),hospital.getLongitude());
		mapView.getOverlays().add(overitem); //添加ItemizedOverlay实例到mMapView
		
	}

	private void setClickListener() {
		search_drive.setOnClickListener(clickListener);
		search_bus.setOnClickListener(clickListener);
		search_btn.setOnClickListener(clickListener);
		search_back.setOnClickListener(clickListener);		
	}

	private void initViews() {
		mylocation1 = (EditText) this.findViewById(R.id.map_location1);
		location2 = (EditText) this.findViewById(R.id.map_location2);
		location2.setText(hospital.getName());
		search_bus = (Button) this.findViewById(R.id.map_search_bus);
		search_drive = (Button) this.findViewById(R.id.map_search_drive);
		search_btn = (Button) findViewById(R.id.channel_btn_search);
		search_back = (Button) findViewById(R.id.channel_btn_back);
		map_dialog = (LinearLayout) findViewById(R.id.channel_map_dialog);
		map_dialog.setVisibility(View.GONE);
		
		common_Title = (Common_Title) findViewById(R.channel.title_bar);
		common_Title.setTitleTxt(getResources().getString(R.string.channel_title_map));
//		relativeLayout = (RelativeLayout) findViewById(R.channel.map_views);

		mapView = (MapView) this.findViewById(R.id.bmapView);
		
//		setMapViewLayout(R.channel.title_bar);
	}
	
	

	OnClickListener clickListener = new OnClickListener() {
		public void onClick(View v) {
			if(search_back.equals(v)){
				if(flag_dialog) {
					
					common_Title.setVisibility(View.VISIBLE);
//						setMapViewLayout(R.channel.title_bar);
						map_dialog.setVisibility(View.GONE);
//						search_btn.setVisibility(View.VISIBLE);
						flag_dialog = false;
				}else
				
				ChannelMAPActivity.this.finish();
			}else
			if (search_btn.equals(v)) {
				common_Title.setVisibility(View.GONE);
				map_dialog.setVisibility(View.VISIBLE);
//				search_btn.setVisibility(View.INVISIBLE);
//				setMapViewLayout(R.id.channel_map_dialog);
				flag_dialog = true;
			} else {
				falg_search = true;
				SearchButtonProcess(v);
			}
		}
	};

	private void setMapViewLayout(int titleBar) {
		relativeLayout.removeView(mapView);
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		lp.addRule(RelativeLayout.BELOW, titleBar);
		relativeLayout.addView(mapView, lp);
	}

	void SearchButtonProcess(View v) {

		// 对起点终点的name进行赋值，也可以直接对坐标赋值，赋值坐标则将根据坐标进行搜索
		String destination = location2.getText().toString();

		// 设置起始地（当前位置）
		MKPlanNode startNode = new MKPlanNode();
		if (mylocation1.getText().toString().equals("本地位置")) {
			startNode.pt = pt;
		} else {
			startNode.name = mylocation1.getText().toString();
		}

		// 设置目的地
		MKPlanNode endNode = new MKPlanNode();
		endNode.name = destination;

		// 实际使用中请对起点终点城市进行正确的设定
		if (search_drive.equals(v)) {
			searchModel.drivingSearch(city, startNode, city, endNode);
		}
		 else if (search_bus.equals(v)) {
			 searchModel.transitSearch(city, startNode, endNode);}
		// } else if (mBtnWalk.equals(v)) {
		// mSearch.walkingSearch("北京", stNode, "北京", enNode);
		// }
	}

	// 初始化搜索模块
	private void searchModel() {
		searchModel = new MKSearch();
		// 设置路线策略为最短距离
		searchModel.setDrivingPolicy(MKSearch.ECAR_DIS_FIRST);
		searchModel.init(mMapManager, new MKSearchListener() {
			// 获取驾车路线回调方法
			@Override
			public void onGetDrivingRouteResult(MKDrivingRouteResult res,
					int error) {
				// 错误号可参考MKEvent中的定义
				if (error != 0 || res == null) {
					Toast.makeText(ChannelMAPActivity.this, "抱歉，未找到结果",
							Toast.LENGTH_SHORT).show();
					return;
				}
				RouteOverlay routeOverlay = new RouteOverlay(
						ChannelMAPActivity.this, mapView);

				// 此处仅展示一个方案作为示例
				MKRoute route = res.getPlan(0).getRoute(0);
				int distanceM = route.getDistance();
				String distanceKm = String.valueOf(distanceM / 1000) + "."
						+ String.valueOf(distanceM % 1000);
				System.out.println("距离:" + distanceKm + "公里---节点数量:"
						+ route.getNumSteps());
				for (int i = 0; i < route.getNumSteps(); i++) {
					MKStep step = route.getStep(i);
					System.out.println("节点信息：" + step.getContent());
				}
				routeOverlay.setData(route);
				mapView.getOverlays().clear();
				mapView.getOverlays().add(routeOverlay);
				mapView.invalidate();
				mapView.getController().animateTo(res.getStart().pt);
			}

			// 以下两种方式和上面的驾车方案实现方法一样
			@Override
			public void onGetWalkingRouteResult(MKWalkingRouteResult res,
					int error) {
				// 获取步行路线
			}

			@Override
			public void onGetTransitRouteResult(MKTransitRouteResult res,
					int arg1) {
				// 获取公交线路
				
				TransitOverlay  routeOverlay = new TransitOverlay (ChannelMAPActivity.this, mapView);
			    // 此处仅展示一个方案作为示例
			    routeOverlay.setData(res.getPlan(0));
			    mapView.getOverlays().clear();
			    mapView.getOverlays().add(routeOverlay);
			    mapView.invalidate();
			    
			    mapView.getController().animateTo(res.getStart().pt);
			}

			/**
			 * 在地图上画出bus搜索的线路
			 */
			@Override
			public void onGetBusDetailResult(MKBusLineResult result, int iError) {

			}

			/**
			 * 根据当前地址得到城市，需要在获得位置函数onLocationChanged中加上searchModel.
			 * reverseGeocode(pt);
			 */
			@Override
			public void onGetAddrResult(MKAddrInfo res, int error) {
				if (error != 0) {
					String str = String.format("错误号：%d", error);
					Toast.makeText(ChannelMAPActivity.this, str,
							Toast.LENGTH_LONG).show();
					return;
				}

				MKGeocoderAddressComponent kk = res.addressComponents;
				city = kk.city;
				Log.d(TAG, "city:" + city);
			}

			@Override
			public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
			}

			@Override
			public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
			}

			@Override
			public void onGetPoiDetailSearchResult(int arg0, int arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onGetRGCShareUrlResult(String arg0, int arg1) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void setCenterPoint(GeoPoint pt) {
		MapController mMapController = mapView.getController(); // 得到mMapView的控制权,可以用它控制和驱动平移和缩放
		mMapController.setCenter(pt); // 设置地图中心点
		mMapController.setZoom(12);
		falg_search = false;
		System.out.println("setCenterPoint!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	}

	@Override
	protected void onResume() {
		if(hospital == null) hospital = (HospitalVO) getIntent().getSerializableExtra(
				ChannelListActivity.DETAILS);
		 System.out.println("hospital---"+hospital.getLatitude() +":"+hospital.getLongitude());
		
		
		mMapManager.getLocationManager().requestLocationUpdates(
				locationListener);
		myLocationOverlay.enableMyLocation();
		myLocationOverlay.enableCompass(); // 打开指南针
		mMapManager.start();
		super.onResume();
	}

	@Override
	protected void onPause() {
		mMapManager.getLocationManager().removeUpdates(locationListener);
		myLocationOverlay.disableMyLocation();// 显示当前位置
		myLocationOverlay.disableCompass(); // 关闭指南针
		mMapManager.stop();
		super.onPause();
	}

	@Override
	// 建议在APP整体退出之前调用MapApi的destroy()函数，不要在每个activity的OnDestroy中调用，
	// 避免MapApi重复创建初始化，提高效率
	protected void onDestroy() {
		if (mMapManager != null) {
			mMapManager.destroy();
			mMapManager = null;
		}
		super.onDestroy();
		System.exit(0);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	class MyGeneralListener implements MKGeneralListener {
		@Override
		public void onGetNetworkState(int iError) {
			Log.d("MyGeneralListener", "onGetNetworkState error is " + iError);
			Toast.makeText(ChannelMAPActivity.this, "您的网络出错啦！",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onGetPermissionState(int iError) {
			Log.d("MyGeneralListener", "onGetPermissionState error is "
					+ iError);
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
				// 授权Key错误：
				Toast.makeText(ChannelMAPActivity.this,
						"请在BMapApiDemoApp.java文件输入正确的授权Key！", Toast.LENGTH_LONG)
						.show();
			}
		}
	}
	
	
	class OverItemT extends ItemizedOverlay<OverlayItem> {

		public List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
		private Drawable marker;
		private Context mContext;

		private double mLat1 = 39.90923; // point1纬度
		private double mLon1 = 116.357428; // point1经度

		private double mLat2 = 39.90923;
		private double mLon2 = 116.397428;


		public OverItemT(Drawable marker, Context context, double lat1, double lon1) {
			super(boundCenterBottom(marker));

			this.marker = marker;
			this.mContext = context;

			// 用给定的经纬度构造GeoPoint，单位是微度 (度 * 1E6)
			GeoPoint p1 = new GeoPoint((int) (lat1 * 1E6), (int) (lon1 * 1E6));
//			GeoPoint p2 = new GeoPoint((int) (mLat2 * 1E6), (int) (mLon2 * 1E6));
			
			// 构造OverlayItem的三个参数依次为：item的位置，标题文本，文字片段
			mGeoList.add(new OverlayItem(p1, "P1", "point1"));
//			mGeoList.add(new OverlayItem(p2, "P2", "point2"));
		
			populate();  //createItem(int)方法构造item。一旦有了数据，在调用其它方法前，首先调用这个方法
		}

		public void updateOverlay()
		{
			populate();
		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {

			// Projection接口用于屏幕像素坐标和经纬度坐标之间的变换
			Projection projection = mapView.getProjection(); 
			for (int index = size() - 1; index >= 0; index--) { // 遍历mGeoList
				OverlayItem overLayItem = getItem(index); // 得到给定索引的item

				String title = overLayItem.getTitle();
				// 把经纬度变换到相对于MapView左上角的屏幕像素坐标
				Point point = projection.toPixels(overLayItem.getPoint(), null); 

				// 可在此处添加您的绘制代码
				Paint paintText = new Paint();
				paintText.setColor(Color.BLUE);
				paintText.setTextSize(15);
				canvas.drawText(title, point.x-30, point.y, paintText); // 绘制文本
			}

			super.draw(canvas, mapView, shadow);
			//调整一个drawable边界，使得（0，0）是这个drawable底部最后一行中心的一个像素
			boundCenterBottom(marker);
		}

		@Override
		protected OverlayItem createItem(int i) {
			// TODO Auto-generated method stub
			return mGeoList.get(i);
		}

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return mGeoList.size();
		}
	}
	
}