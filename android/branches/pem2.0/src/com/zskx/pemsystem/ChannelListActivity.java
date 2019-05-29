package com.zskx.pemsystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKGeocoderAddressComponent;
import com.baidu.mapapi.MKPoiInfo;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKSuggestionResult;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.zskx.net.response.HospitalVO;
import com.zskx.pemsystem.adpater.ChannelListAdapter;
import com.zskx.pemsystem.util.Common_Title;

public class ChannelListActivity extends MapActivity {
	private static String TAG = "ChannelListActivity";
	public final static String DETAILS = "details";
	View server_progress;
	TextView textView;
	Common_Title common_Title;
	// MapView mMapView;
	ListView listView;
	TextView tx_info;
	ChannelListAdapter channelListAdapter;
	private String file_name = "ChannelListActivity";

	// private BMapManager mMapManager = null;
	// private LocationListener locationListener;
	private MKSearch searchModel;
	GeoPoint pt;
	String city;

	LocationListener mLocationListener = null;// create时注册此listener，Destroy时需要Remove

	private ArrayList<HospitalVO> hospitalVOs = new ArrayList<HospitalVO>();
	private ArrayList<HospitalVO> city_hospitalVOs = new ArrayList<HospitalVO>();

	@Override
	protected void onCreate(Bundle icicle) {
		// TODO Auto-generated method stub
		super.onCreate(icicle);
		setContentView(R.layout.channel_list);

		getHospitalList();
		initViews();

		initListView(hospitalVOs);

		initMAPviews();

	}

	private void searchForDistance(ArrayList<HospitalVO> hospital_list) {
		
			for (HospitalVO vo : hospital_list) {
				double dst = getDistatce(vo.getLatitude(), vo.getLongitude(),
						pt.getLatitudeE6() / 1e6, pt.getLongitudeE6() / 1e6);
				vo.setDistance(dst);
				Log.i(TAG, "distance--" + vo.getLatitude()+ ":" + pt.getLatitudeE6() / 1e6);
			}
			
			sortList(hospital_list);
			// searchModel.poiSearchInCity(city, "");
		
	}

	private void sortList(ArrayList<HospitalVO> hospital_list) {
		Comparator<HospitalVO> comparator = new Comparator<HospitalVO>() {

			@Override
			public int compare(HospitalVO lhs, HospitalVO rhs) {
				return	String.valueOf(lhs.getDistance()).compareTo(String.valueOf(rhs.getDistance()));
//				return lhs.getDistance() - rhs.getDistance());
			}
		};
		Collections.sort(hospitalVOs, comparator);
		
		initListView(hospitalVOs);

		channelListAdapter.notifyDataSetChanged();
	}

	private void initMAPviews() {
		final BMapApiDemoApp app = (BMapApiDemoApp) this.getApplication();
		if (app.mBMapMan == null) {
			app.mBMapMan = new BMapManager(getApplication());
			app.mBMapMan.init(app.mStrKey,
					new BMapApiDemoApp.MyGeneralListener());
		}
		app.mBMapMan.start();

		// 注册定位事件
		mLocationListener = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				if (location != null) {
					String strLog = String.format("您当前的位置:\r\n" + "经度:%f\r\n"
							+ "纬度:%f", location.getLongitude(),
							location.getLatitude());
					Log.i(TAG, strLog);
					pt = new GeoPoint((int) (location.getLatitude() * 1e6),
							(int) (location.getLongitude() * 1e6));

					if (searchModel == null) {
						searchModel = new MKSearch();
						MKSearch.setPoiPageCapacity(20);
						searchModel.init(app.mBMapMan, search_listener);
					}
					
					searchForDistance(hospitalVOs);

					searchModel.reverseGeocode(pt);
				
					
					System.out.println("searchForDistance--!!!! " + pt.getLatitudeE6() * 1e6 + pt.getLongitudeE6() * 1e6);
				}
			}
		};
	}

	private void initViews() {
		listView = (ListView) findViewById(R.id.channel_list);
		common_Title = (Common_Title) findViewById(R.channel.title_bar);
		common_Title.setTitleTxt(getResources().getString(R.string.channel_title_list));
		tx_info = (TextView) findViewById(R.id.channel_info);
		
		String info1 = "提示：您所在地附近有心理科门诊的医院共有 ";
		String info2 = " 家，你可以选择以下医院信息进行查询";
		String inf0 = info1 + hospitalVOs.size() + info2;
		tx_info.setText(inf0);
	}

	private void getHospitalList() {
		hospitalVOs = new ArrayList<HospitalVO>();
		hospitalVOs.add(new HospitalVO("西安西京医院", "西安市", "心理科",
				"陕西省西安市长乐西路127号", "0298888888", "郭凯，王启鹏", 34.269942, 108.988312));
		hospitalVOs.add(new HospitalVO("西安高新医院", "西安市", "心理科",
				"陕西省西安市长乐西路127号", "0298888888", "郭凯，王启鹏", 34.231997, 108.876695));
		hospitalVOs.add(new HospitalVO("西安hhh医院", "西安市", "心理科",
				"陕西省西安市长乐西路127号", "0298888888", "郭凯，王启鹏", 34.281997, 108.456695));

		// hospitalVOs = AppConfiguration.getHospitalList(this);
	}

	private void initListView(final ArrayList<HospitalVO> hospital_list) {
		if(channelListAdapter == null) channelListAdapter = new ChannelListAdapter(this, hospital_list);
		else channelListAdapter.setHospitalVOs(hospital_list);
		listView.setAdapter(channelListAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(ChannelListActivity.this,
						ChannelHospitalDetails.class);
				intent.putExtra(DETAILS, hospital_list.get((int) position));
				startActivity(intent);
			}
		});
		System.out.println("initListView ---  hospital_list:"
				+ hospital_list.size());
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onPause() {
		BMapApiDemoApp app = (BMapApiDemoApp) this.getApplication();
		// 移除listener
		app.mBMapMan.getLocationManager().removeUpdates(mLocationListener);
		app.mBMapMan.stop();
		super.onPause();
	}

	@Override
	protected void onResume() {
		BMapApiDemoApp app = (BMapApiDemoApp) this.getApplication();
		// 注册Listener
		app.mBMapMan.getLocationManager().requestLocationUpdates(
				mLocationListener);
		app.mBMapMan.start();
		super.onResume();
	}

	MKSearchListener search_listener = new MKSearchListener() {

		@Override
		public void onGetAddrResult(MKAddrInfo res, int error) {
			if (error != 0) {
				String str = String.format("错误号：%d", error);
				Toast.makeText(ChannelListActivity.this, str, Toast.LENGTH_LONG)
						.show();
				return;
			}

			MKGeocoderAddressComponent kk = res.addressComponents;
			city = kk.city;
			Log.i(TAG, "onGetAddrResult------city:" + city);

			
			
			if (city != null && !city.equals("")) {
				getCityHospital(hospitalVOs);
				if (city_hospitalVOs.size() > 0)
					initListView(city_hospitalVOs);
			}

		}

		/**
		 * 得到同城市的医院列表
		 * 
		 * @param hospitalVOs2
		 */
		private void getCityHospital(ArrayList<HospitalVO> hospitalVOs2) {
			// city_hospitalVOs = new ArrayList<HospitalVO>();
			for (HospitalVO hospitalVO : hospitalVOs2) {
				String hospital = hospitalVO.getCity();
				if (city_hospitalVOs.size() == 0) {
					if (city.equals(hospital) || hospital.contains(city)) {
						city_hospitalVOs.add(hospitalVO);
						Log.i(TAG, "city_hospitalVOs:" + hospital);
					}
				} else {
					for (HospitalVO city_hospitalVO2 : city_hospitalVOs) {
						if (!city_hospitalVO2.getName().trim().equals(hospitalVO.getName().trim())) {
							if (city.equals(hospital) || hospital.contains(city)) {
								city_hospitalVOs.add(hospitalVO);
								Log.i(TAG, "city_hospitalVOs:" + hospital);
							}
						}
					}
				}
			}
		}

		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {

		}

		@Override
		public void onGetPoiResult(MKPoiResult result, int arg1, int arg2) {

			if (result == null) {

				// 没有找到相关的POI记录，给以用户提示

				return;

			}

			// 取得所有相关的POI搜索记录

			ArrayList<MKPoiInfo> poiList = result.getAllPoi();

			for (MKPoiInfo info : poiList) {

				// 处理找到的POI信息并显示到地图上

			}

		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {

		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {

		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetRGCShareUrlResult(String arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
			// TODO Auto-generated method stub

		}

	};

	private static final double EARTH_RADIUS = 6378.137 ; 
    private static double rad(double d) 
    { 
       return d * Math.PI / 180.0; 
    } 
    public static double getDistatce(double lat1, double lng1, double lat2, double lng2) 
    { 
       double radLat1 = rad(lat1); 
       double radLat2 = rad(lat2); 
       double a = radLat1 - radLat2; 
       double b = rad(lng1) - rad(lng2); 
       double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +  
        Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2))); 
       s = s * EARTH_RADIUS ; 
       s = (double)Math.round(s * 100) / 100; 
       return s; 
    } 
    
    
    
}
