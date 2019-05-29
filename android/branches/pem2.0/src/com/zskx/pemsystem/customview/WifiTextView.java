package com.zskx.pemsystem.customview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;


import com.zskx.pemsystem.R;
import com.zskx.pemsystem.receiver.NetWorkObserver;
import com.zskx.pemsystem.receiver.NetWorkObserver.WifiState;

public class WifiTextView extends TextView {

	private static String TAG = "WIFI STATE";
	private Animation wanringAnimation; // 警告动画
	private NetWorkObserver netWorkObserver;
	private WifiState state; // wifi 状态
	private Context context;
	private WIFIReceiver receiver;
	private WifiTextView wifiTextView;
	
	public WifiTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		receiver = new WIFIReceiver();
		wifiTextView = this;
		setAnimation(context);
		
		initTest(context);
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
		
			openWifiSet();
		}
		return super.onTouchEvent(event);
	}

	
	/**
	 * 设置显示动画
	 * @param context
	 */
	private void setAnimation(Context context) {
		wanringAnimation = AnimationUtils.loadAnimation(context, R.anim.wifi_warning);
		wifiTextView.startAnimation(wanringAnimation);
	}
	
	/**
	 * 打开wifi系统界面
	 */
	private void openWifiSet() {
		Intent intentWifi = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
		this.context.startActivity(intentWifi);
	}
	
	
	public WIFIReceiver getReceiver() {
		return receiver;
	}
	
	/**
	 * 接受网络变化广播
	 * @author guokai
	 *
	 */
	public class WIFIReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// 监听WIFI状态变化
//			if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
//				WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//				
//				int wifi_flag = wifiManager.getWifiState();
//				Log.i(TAG, "Setting wifistate:" + wifi_flag);
//				if(wifi_flag == WifiManager.WIFI_STATE_ENABLED) {
//					wifiTextView.setVisibility(View.GONE);
//				}else{
//					wifiTextView.setVisibility(View.VISIBLE);
//				}
//			
//			} else 
				
				if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
//				// //监听连接状态的变化莫测
//				NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
//				State s = networkInfo.getState();
//				Log.i(TAG, "Setting wifistate:" + networkInfo.isConnected());
//				changeView(s);
				
				changeView(initTest(context));
			}
		}
	}
	
	/**
	 * 根据wifi状态是否显示控件
	 * @param s wifi状态
	 */
	private void changeView(State s) {
		if (s == State.CONNECTED) {
			wifiTextView.setVisibility(View.GONE);
		}else{
			wifiTextView.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 获得wifi状态
	 */
	public State initTest(Context context)
	{
		ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo networkInfo = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		
		State state = networkInfo.getState();
		
		return state;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
