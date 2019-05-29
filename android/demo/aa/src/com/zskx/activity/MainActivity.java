package com.zskx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.zskx.ui.GalleryFlow;
import com.zskx.ui.ImageAdapter;
import com.zskx.ui.MyToast;

public class MainActivity extends Activity {

	/** 用来展示功能的长廊 */
	private GalleryFlow gallery;

	private Integer imageId[] = { R.drawable.report_icon,
			R.drawable.music_icon, R.drawable.decompression_icon,
			R.drawable.psychology_practise_icon,
			R.drawable.psychology_game_icon,
			R.drawable.positive_psychology_icon };

	private ImageAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_function);

		gallery = (GalleryFlow) findViewById(R.id.main_function);

		adapter = new ImageAdapter(this, imageId);

		adapter.createReflectedImages();

		gallery.setAdapter(adapter);

		gallery.setSpacing(-50);

		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (gallery.getSelectedItemPosition() == position) {
					Intent intent = new Intent();
					System.out.println(position);
					switch (position) {
					case 0:
						intent.setClass(MainActivity.this, ReportActivity.class);
						MainActivity.this.startActivity(intent);
						break;
					case 1:
						intent.setClass(MainActivity.this, MusicActivity.class);
						MainActivity.this.startActivity(intent);
						break;
					case 2:
						intent.setClass(MainActivity.this,
								DecompressionActivity.class);
						MainActivity.this.startActivity(intent);
						break;
					case 3:
						MyToast.show(MainActivity.this.getApplicationContext(),
								"click + function " + 3);
						break;
					case 4:
						MyToast.show(MainActivity.this.getApplicationContext(),
								"click + function " + 4);
						break;
					case 5:
						MyToast.show(MainActivity.this.getApplicationContext(),
								"click + function " + 5);
						break;

					default:
						break;
					}

				}
			}
		});

	}
}
