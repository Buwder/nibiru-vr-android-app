package com.zskx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.zskx.controller.ActivityHolder;
import com.zskx.dialog.ExitDialog;
import com.zskx.util.PreMenuImageRes;
import com.zskx.view.PreMainMenuItem;

/**
 * 主选择页面
 * 
 * @author demo
 * 
 */
public class PreMainActivity extends Activity {

	/** 选择到主菜单 */
	private GridView mainMenu;
	/** 退出按钮 */
	private ImageView exitBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.preview_main);

		initView();
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		mainMenu = (GridView) findViewById(R.preMain.menu);
		exitBtn = (ImageView) findViewById(R.id.exit);
		// 给每个功能添加点击事件
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				int tag =  (Integer)v.getTag();
				// Toast.makeText(PreMainActivity.this, tag, 1000).show();
				Intent intent = new Intent(PreMainActivity.this,
						MainActivity.class);
				intent.putExtra("functionTag", tag);
				PreMainActivity.this.startActivity(intent);
			}
		};
		// 从资源文件中得到功能名和传入
		int[] imageName = {PreMenuImageRes.MUSIC, PreMenuImageRes.MOVIE,
				PreMenuImageRes.RELAX, PreMenuImageRes.CRISIS,
				PreMenuImageRes.GAME, PreMenuImageRes.MAGAZINE,0,0};
		String[] textName = this.getResources().getStringArray(
				R.array.menu_text_name);
		final PreMainMenuItem[] menu = new PreMainMenuItem[imageName.length];

		for (int i = 0; i < textName.length; i++) {
			menu[i] = new PreMainMenuItem(this);
			menu[i].setText(textName[i]);
			menu[i].setImageByDefaultSort(i);
			menu[i].setTag(imageName[i]);
			if(i<textName.length-2){
				menu[i].setOnClickListener(listener);
			}else {
				menu[i].setOnClickListener(null);
			}
		}

		mainMenu.setAdapter(new BaseAdapter() {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				return menu[position];
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public Object getItem(int position) {
				return menu[position];
			}

			@Override
			public int getCount() {
				return menu.length;
			}
		});

		initViewListener();
	}

	/**
	 * 初始化监听器
	 */
	private void initViewListener() {
		exitBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ExitDialog.show(PreMainActivity.this);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 移除控制器中
		ActivityHolder.getIntance().push(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		// 压入控制器中
		ActivityHolder.getIntance().pop(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 移除控制器中
		ActivityHolder.getIntance().push(this);
	}
}
