package com.zskx.pemsystem.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

/**
 * 
 * WiFi 连接 监听广播
 *
 */
public class NetWorkObserver extends BroadcastReceiver{
	
	/**
	 * 
	 * WIFI 连接状态<br>
	 * WIFI_CONNECTED：已连接<br>
	 * WIFI_FAILOVER：未连接<br>
	 */
	public enum WifiState
	{
		WIFI_CONNECTED,WIFI_FAILOVER
	}
	
	//监听回调接器
	private WifiStateListener wifiStateListener;

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("onReceive");
		WifiState state=getNetWorkState(context);
		notifyWifiState(state);
	}

	
	




	public void setWifiStateListener(WifiStateListener wifiStateListener) {
		this.wifiStateListener = wifiStateListener;
	}




	/**
	 * 判断  WiFi 状态
	 * @param context
	 * @return
	 */
	public static WifiState  getNetWorkState(Context context)
	{
		ConnectivityManager cManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetworkInfo=cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		
		State state=wifiNetworkInfo.getState();
		if(state==State.CONNECTED)
		{
			return WifiState.WIFI_CONNECTED;
		}else
		{
			return WifiState.WIFI_FAILOVER;
		}
	}
	
	//监听回调接口
	public interface WifiStateListener
	{
		
		public void observeWifiState(WifiState state);
	}
	
	//通知WiFi状态变化
	private void notifyWifiState(WifiState state)
	{
		if(wifiStateListener !=null)
		{
			wifiStateListener.observeWifiState(state);
		}
	}
	
	
}
