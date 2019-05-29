package com.zskx.pemsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.zskx.pemsystem.util.ScreenManager;

public class MenuActivity extends Activity {

	private final static int PSYCHOTEST = Menu.FIRST;
	private final static int EDIT_PASSWORD = Menu.FIRST + 1; // 主页
	private final static int EDIT_INFO = Menu.FIRST + 2; // 设置
	private final static int HELP = Menu.FIRST + 3;// 信息反馈
	private final static int ABOUT_US = Menu.FIRST + 4;// 注销
	private final static int EXIT_SYSTEM = Menu.FIRST + 5;// 退出

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.activity_main, menu);

		if (!(this instanceof PsychoTestActivity)) {

			menu.add(1, PSYCHOTEST, PSYCHOTEST, R.string.menu_psychotest).setIcon(android.R.drawable.ic_menu_search);
		}

		if (!(this instanceof ModifyPasswordActivity)) {
			menu.add(1, EDIT_PASSWORD, EDIT_PASSWORD,
					R.string.menu_edit_password).setIcon(
					R.drawable.setting_ico_pwd);
		}

		if (!(this instanceof ModifyInfoActivity)) {
			menu.add(1, EDIT_INFO, EDIT_INFO, R.string.menu_edit_info).setIcon(
					R.drawable.setting_ico_edit);
		}

		menu.add(1, HELP, HELP, R.string.menu_help).setIcon(
				R.drawable.setting_ico_help);
		menu.add(1, ABOUT_US, ABOUT_US, R.string.menu_about_us).setIcon(
				R.drawable.setting_ico_about);
		menu.add(1, EXIT_SYSTEM, EXIT_SYSTEM, R.string.menu_exit).setIcon(
				R.drawable.setting_ico_exit);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Intent intent;

		switch (item.getItemId()) {

		case PSYCHOTEST:

			intent = new Intent(this, PsychoTestActivity.class);
			startActivity(intent);

			break;
		case EDIT_PASSWORD:
			intent = new Intent(this, ModifyPasswordActivity.class);
			startActivity(intent);

			break;
		case EDIT_INFO:
			intent = new Intent(this, ModifyInfoActivity.class);
			startActivity(intent);

			break;
		case HELP:
			intent = new Intent(this, HelpActivity.class);
			startActivity(intent);

			break;
		case ABOUT_US:
			intent = new Intent(this, AboutActivity.class);
			startActivity(intent);

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
