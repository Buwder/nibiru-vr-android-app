package com.zskx.pemsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

import com.zskx.pemsystem.util.ChangeViewBackground;
import com.zskx.pemsystem.util.ScreenManager;
import com.zskx.pemsystem.util.ShowNotification;
import com.zskx.pemsystem.util.UtilService;

/**
 * 
 * @author guokai
 *
 */
public class HomeActivity extends MenuActivity {
	
	private Button button_psycho_test;
	private Button button_check_report;
	private Button button_musical_therapy;
	private Button button_decompression;
	private Button button_psycho_train;
	private Button button_psycho_hospital;
	private Button button_positive_mental;
	private Button button_psycho_consult;
	
	private View lyt_psycho_test;
	private View lyt_check_report;
	private View lyt_musical_therapy;
	private View lyt_decompression;
	private View lyt_psycho_train;
	private View lyt_psycho_hospital;
	private View lyt_positive_mental;
	private View lyt_psycho_consult;
	
	private List<Integer> list_btns ; //点击的按钮
	private SparseArray< View> map_lyts; //点击的按钮的id，需要改变的view
	
	public final static int PSYCHOLOGICAL_SERIES_ID=0;   
	public final static int DECOMPRESSION_SERIES_ID=1;
	
	public final static String SERIES_KEY="series_key";
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ScreenManager.getScreenManager().pushActivity(this);
		setContentView(R.layout.index_center_line);
		
		
		
		initButtons();
		
		initLyts();
		
		change_btn_back();
		
	}


	private void showNotification() {
//		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
//							.setSmallIcon(R.drawable.ic_launcher)
//							.setContentText("PEM")
//							.setContentTitle("Home");
//		Intent resultIntent = new Intent(this, HomeActivity.class);
//		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
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
		
//		Notification notification=new Notification();
//		notification.icon = R.drawable.ic_launcher;
//		notification.tickerText = "PEM";
//		notification.when = System.currentTimeMillis();
//		PendingIntent pt=PendingIntent.getActivity(this, 12321, new Intent(this, HomeActivity.class), 0);
//		notification.setLatestEventInfo(this, "PEM", "Home", pt);
//		mNotificationManager.notify(41965, notification);
		ShowNotification.showNotification(this, "PEM", "Home", HomeActivity.class ,null);
	}


	private void initLyts() {
		lyt_psycho_test = findViewById(R.id.index_psycho_test_lyt);
		lyt_check_report = findViewById(R.id.index_check_report_lyt);
		lyt_musical_therapy = findViewById(R.id.index_musical_therapy_lyt);
		lyt_decompression = findViewById(R.id.index_decompression_lyt);
		lyt_psycho_train = findViewById(R.id.index_psycho_train_lyt);
		lyt_psycho_hospital = findViewById(R.id.index_hospital_lyt);
		lyt_positive_mental = findViewById(R.id.index_positive_mental_lyt);
		lyt_psycho_consult = findViewById(R.id.index_psycho_consult_lyt);
		
		map_lyts = new SparseArray< View>(); 
		map_lyts.put(R.id.index_psycho_test, lyt_psycho_test);
		map_lyts.put(R.id.index_check_report, lyt_check_report);
		map_lyts.put(R.id.index_musical_therapy, lyt_musical_therapy);
		map_lyts.put(R.id.index_decompression, lyt_decompression);
		map_lyts.put(R.id.index_psycho_train, lyt_psycho_train);
		map_lyts.put(R.id.index_positive_mental, lyt_positive_mental);
		map_lyts.put(R.id.index_hospital, lyt_psycho_hospital);
		map_lyts.put(R.id.index_psycho_consult, lyt_psycho_consult);
	}



	@Override
	protected void onSaveInstanceState(Bundle outState) {
		//为解决 IllegalStateException: Can not perform this action after onSaveInstanceState问题, 重写该方法
	}


	@Override
	protected void onResume() {
		/** * 设置为横屏 */
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		showNotification();
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		ShowNotification.cancleNotification();
		super.onDestroy();
	}

	

	private void initButtons() {
		button_psycho_test = (Button) this.findViewById(R.id.index_psycho_test);
		button_psycho_test.setOnClickListener(buttonClickListener);
		button_check_report = (Button) this.findViewById(R.id.index_check_report);
		button_check_report.setOnClickListener(buttonClickListener);
		button_musical_therapy = (Button) this.findViewById(R.id.index_musical_therapy);
		button_musical_therapy.setOnClickListener(buttonClickListener);
		button_decompression = (Button) this.findViewById(R.id.index_decompression);
		button_decompression.setOnClickListener(buttonClickListener);
		button_psycho_train = (Button) this.findViewById(R.id.index_psycho_train);
		button_psycho_train.setOnClickListener(buttonClickListener);
		button_positive_mental = (Button) this.findViewById(R.id.index_positive_mental);
		button_positive_mental.setOnClickListener(buttonClickListener);
		
		button_psycho_hospital = (Button) this.findViewById(R.id.index_hospital);
		button_psycho_hospital.setOnClickListener(buttonClickListener);
		button_psycho_consult = (Button) this.findViewById(R.id.index_psycho_consult);
		button_psycho_consult.setOnClickListener(buttonClickListener);
		
		list_btns = new ArrayList<Integer>();
		list_btns.add(R.id.index_psycho_test);
		list_btns.add(R.id.index_check_report);
		list_btns.add(R.id.index_musical_therapy);
		list_btns.add(R.id.index_decompression);
		list_btns.add(R.id.index_psycho_train);
		list_btns.add(R.id.index_positive_mental);
		list_btns.add(R.id.index_hospital);
		list_btns.add(R.id.index_psycho_consult);
	}

	View.OnClickListener buttonClickListener  = new View.OnClickListener(){

		@Override
		public void onClick(View v) {
			Intent intent;
			switch (v.getId()) {
			
			case R.id.index_psycho_test:
				intent = new Intent(HomeActivity.this, PsychoTestActivity.class);
				startActivity(intent);
				break;
			
			case R.id.index_check_report:
				intent = new Intent();
				intent.setClass(HomeActivity.this, ReportListActivity.class);
				startActivity(intent);
				break;
				
			case R.id.index_musical_therapy:
				intent = new Intent();
				intent.setClass(HomeActivity.this, Music_Activity.class);
				startActivity(intent);
				
				break;
				
			case R.id.index_decompression:
				intent = new Intent();
				intent.setClass(HomeActivity.this, MovieTypeActivity.class);
				intent.putExtra(SERIES_KEY, DECOMPRESSION_SERIES_ID);
				startActivity(intent);
				
				break;
				
			case R.id.index_psycho_train:
				intent = new Intent();
				intent.setClass(HomeActivity.this, MovieTypeActivity.class);
				intent.putExtra(SERIES_KEY, PSYCHOLOGICAL_SERIES_ID);
				startActivity(intent);
				
				break;
				
			case R.id.index_positive_mental:
				intent = new Intent();
				intent.setClass(HomeActivity.this, MagazineActivity.class);
				startActivity(intent);
				
				break;
				
			case R.id.index_hospital:
				String str1 = HomeActivity.this.getResources().getString(R.string.index_update);
				UtilService.show(HomeActivity.this, str1, 2000);
				break;
				
			case R.id.index_psycho_consult:
				String str2 = HomeActivity.this.getResources().getString(R.string.index_update);
				UtilService.show(HomeActivity.this, str2, 2000);
				break;
			}
		}
	};
	
	
	private void change_btn_back() {
		int back = R.drawable.index_ico_active;
		OnTouchListener change_back_listener = new ChangeViewBackground().get_change_back(this,  map_lyts, list_btns, back);
		
		button_psycho_test.setOnTouchListener(change_back_listener);
		button_check_report.setOnTouchListener(change_back_listener);
		button_decompression.setOnTouchListener(change_back_listener);
		button_musical_therapy.setOnTouchListener(change_back_listener);
		button_positive_mental.setOnTouchListener(change_back_listener);
		button_psycho_train.setOnTouchListener(change_back_listener);
//		button_psycho_hospital.setOnTouchListener(change_back_listener);
//		button_psycho_consult.setOnTouchListener(change_back_listener);

 	}
	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			builder.setTitle(R.string.dialog_title);
			builder.setMessage(R.string.dialog_message);
			builder.setPositiveButton(R.string.dialog_ok, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ScreenManager.getScreenManager().popAllActivity();
					
				}
			}).setNegativeButton(R.string.dialog_back, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
				
					
				}
			}).create().show();
			
			return true;
			
		}
		
		return super.onKeyDown(keyCode, event);
	}

}
