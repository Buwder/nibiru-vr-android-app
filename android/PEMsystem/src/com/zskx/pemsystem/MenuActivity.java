package com.zskx.pemsystem;

import com.zskx.pemsystem.util.ScreenManager;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class MenuActivity extends Activity {

	private final static int HOME_PAGE = Menu.FIRST; // 主页
	private final static int SETTING = Menu.FIRST + 1; //设置
	private final static int INFO_FEEDBACK = Menu.FIRST + 2;//信息反馈
	private final static int LOGINOUT = Menu.FIRST + 3;//注销
	private final static int EXIT_SYSTEM = Menu.FIRST + 4;//退出

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.activity_main, menu);

		if (!(this instanceof HomeActivity)) {
			menu.add(Menu.NONE, HOME_PAGE, Menu.NONE,
					getResources().getString(R.string.home_page));
		}

		if (!(this instanceof SettingsActivity))
		{
				menu.add(Menu.NONE, SETTING, Menu.NONE,
				getResources().getString(R.string.settings));
		}
	

		if (!(this instanceof FeedBackActivity)) {
			menu.add(Menu.NONE, INFO_FEEDBACK, Menu.NONE, getResources()
					.getString(R.string.feed_back));
		}

		menu.add(Menu.NONE, LOGINOUT, Menu.NONE,
				getResources().getString(R.string.loginout));
		menu.add(Menu.NONE, EXIT_SYSTEM, Menu.NONE,
				getResources().getString(R.string.exit));

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case HOME_PAGE:
			intent = new Intent(this, HomeActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case INFO_FEEDBACK:
			intent = new Intent(this, FeedBackActivity.class);
			startActivity(intent);
			break;
		case SETTING:
			intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			break;
		case LOGINOUT:
			intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			ScreenManager.getScreenManager().popAllActivity();
			break;
		case EXIT_SYSTEM:
			
			ScreenManager.getScreenManager().popAllActivity();
			break;
			
			default:
				break;
			

		}

		return true;
	}

}
