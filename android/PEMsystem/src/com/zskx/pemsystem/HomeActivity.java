package com.zskx.pemsystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.zskx.pemsystem.util.ScreenManager;

/**
 * 
 * @author guokai
 *
 */
public class HomeActivity extends MenuActivity {
	private Button button_test_report;
	private Button button_musical_therapy;
	private Button button_decompression;
	private Button button_psycho_train;
	private Button button_psycho_games;
	private Button button_positive_mental;
	
	public final static int PSYCHOLOGICAL_SERIES_ID=0;   
	public final static int DECOMPRESSION_SERIES_ID=1;
	
	public final static String SERIES_KEY="series_key";
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ScreenManager.getScreenManager().pushActivity(this);
		setContentView(R.layout.home_activity);
		
		initButtons();
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
		super.onResume();
	}

	private void initButtons() {
		button_test_report = (Button) this.findViewById(R.id.kaishi_test_report);
		button_test_report.setOnClickListener(new ButtonClick());
		button_musical_therapy = (Button) this.findViewById(R.id.kaishi_musical_therapy);
		button_musical_therapy.setOnClickListener(new ButtonClick());
		button_decompression = (Button) this.findViewById(R.id.kaishi_category1_decompression);
		button_decompression.setOnClickListener(new ButtonClick());
		button_psycho_train = (Button) this.findViewById(R.id.kaishi_category2_psycho_train);
		button_psycho_train.setOnClickListener(new ButtonClick());
		button_psycho_games = (Button) this.findViewById(R.id.kaishi_category3_psycho_games);
		button_psycho_games.setOnClickListener(new ButtonClick());
		button_positive_mental = (Button) this.findViewById(R.id.kaishi_category4_positive_mental);
		button_positive_mental.setOnClickListener(new ButtonClick());
	}

	private final class ButtonClick implements android.view.View.OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent;
			switch (v.getId()) {
			case R.id.kaishi_test_report:
				intent = new Intent();
				intent.setClass(HomeActivity.this, ReportListActivity.class);
				startActivity(intent);
				break;
				
			case R.id.kaishi_musical_therapy:
				intent = new Intent();
				intent.setClass(HomeActivity.this, Music_Activity.class);
				startActivity(intent);
				
				break;
				
			case R.id.kaishi_category1_decompression:
				intent = new Intent();
				intent.setClass(HomeActivity.this, MovieTypeActivity.class);
				intent.putExtra(SERIES_KEY, DECOMPRESSION_SERIES_ID);
				startActivity(intent);
				
				break;
				
			case R.id.kaishi_category2_psycho_train:
				intent = new Intent();
				intent.setClass(HomeActivity.this, MovieTypeActivity.class);
				intent.putExtra(SERIES_KEY, PSYCHOLOGICAL_SERIES_ID);
				startActivity(intent);
				
				break;
				
			case R.id.kaishi_category3_psycho_games:
				
				break;
				
			case R.id.kaishi_category4_positive_mental:
				intent = new Intent();
				intent.setClass(HomeActivity.this, Magazine_Activity.class);
				startActivity(intent);
				
				break;
			}
		}
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
