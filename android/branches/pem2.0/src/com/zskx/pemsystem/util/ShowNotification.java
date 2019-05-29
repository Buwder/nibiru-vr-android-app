package com.zskx.pemsystem.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.zskx.pemsystem.LoginActivity;
import com.zskx.pemsystem.MagazineActivity;
import com.zskx.pemsystem.MusicTypeChildActivity;
import com.zskx.pemsystem.R;

public class ShowNotification {

	private static Notification notification ;
	private static NotificationManager mNotificationManager ;
	private static int ID =  41965;
	private static Context context1 ;
	
	
	public static Notification getInstence(){
		if(notification == null)  notification = new Notification();
		return notification;
	}
	
	/**
	 * 显示通知栏
	 * @param context
	 * @param title 标题,一般是PEM 
	 * @param content 内容，一般是activity名称
	 * @param clazz 启动的activity
	 * @param intent 有的activity启动是需要的intent ,必须设置setClass方法
	 */
	public static void showNotification(Context context, String title, String content , Class<?> clazz, Intent intent) {
//		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
//							.setSmallIcon(R.drawable.ic_launcher)
//							.setContentText("PEM")
//							.setContentTitle("Home");
//		Intent resultIntent = new Intent(context, HomeActivity.class);
//		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//		// Adds the back stack for the Intent (but not the Intent itself)
//		stackBuilder.addParentStack(HomeActivity.class);
//		// Adds the Intent that starts the Activity to the top of the stack
//		stackBuilder.addNextIntent(resultIntent);
//		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent( 0, PendingIntent.FLAG_UPDATE_CURRENT );
//		mBuilder.setContentIntent(resultPendingIntent);
//		NotificationManager mNotificationManager =
//		    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//		// mId allows you to update the notification later on.
//		mNotificationManager.notify(123455,  mBuilder);
		if(context == null) {
			if(mNotificationManager != null) mNotificationManager.cancel(ID);
			return;
		}
		notification = getInstence();
		if(mNotificationManager == null) mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		if(context1 == null) context1 =  context.getApplicationContext();
		notification.icon = R.drawable.notification;
		notification.tickerText = title;
		notification.when = System.currentTimeMillis();
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		
		PendingIntent pt;
		if(AppConfiguration.getUser(context) != null && !AppConfiguration.getUser(context).getSessionId().equals("")) {
			if(intent != null)  {
				pt = PendingIntent.getActivity(context, 12321, intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT), 0);
			}
			else if(clazz != null ){
				pt = PendingIntent.getActivity(context, 12321, new Intent(context, clazz).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT), 0);
			}else{
				 pt = PendingIntent.getActivity(context1, 12321, new Intent(context1, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT), 0);
			}
		}else{
			 pt = PendingIntent.getActivity(context1, 12321, new Intent(context1, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT), 0);
		}
		notification.setLatestEventInfo(context, title, content, pt);
		
		mNotificationManager.notify(ID, notification);
	}
	
	public static NotificationManager getmNotificationManager() {
		return mNotificationManager;
	}

	public static void setmNotificationManager(
			NotificationManager mNotificationManager) {
		ShowNotification.mNotificationManager = mNotificationManager;
	}
	
	public static void cancleNotification(){
		if(mNotificationManager!=null) mNotificationManager.cancel(ID);
	}

}
