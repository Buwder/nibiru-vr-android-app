package com.zskx.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zskx.activity.R;

public class MusicController extends LinearLayout {

	private ImageView music_image;

	private ImageView pre_song;

	private ImageView playAndPause;

	private ImageView next_song;

	private ImageView audio_volumn_button;

	private SeekBar audio_volumn_bar;

	private TextView current_time;

	private SeekBar progress_bar;

	private TextView whole_time;

	private boolean isPlay = false;

	private boolean isSilent = false;

	public MusicController(Context context, AttributeSet attrs) {
		super(context, attrs);

		initView();
	}

	private void initView() {
		View.inflate(getContext(), R.layout.music_controller_layout, this);

		music_image = (ImageView) findViewById(R.id.music_image);
		pre_song = (ImageView) findViewById(R.id.pre_song);
		playAndPause = (ImageView) findViewById(R.id.playAndpause);
		next_song = (ImageView) findViewById(R.id.next_song);
		audio_volumn_button = (ImageView) findViewById(R.id.audio_volumn_icon);

		audio_volumn_bar = (SeekBar) findViewById(R.id.audio_volumn_bar);
		progress_bar = (SeekBar) findViewById(R.id.play_progress_bar);

		current_time = (TextView) findViewById(R.id.current_play_time);
		whole_time = (TextView) findViewById(R.id.song_time);

		initListener();

		scaleImageForView(BitmapFactory.decodeResource(getResources(),
				R.drawable.music_type_icon));
	}

	/**
	 * 将音乐的图片转换为50*50的图片显示在图框里
	 * 
	 * @param bitmap
	 */
	public void scaleImageForView(Bitmap bitmap) {
		music_image.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 50, 50,
				true));
	}

	private void initListener() {

		playAndPause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 暂时放在这儿，以后转移到主UI更新中去。
				isPlay = !isPlay;
				if (isPlay) {
					playAndPause
							.setImageResource(R.drawable.play_pause_button_pause_style);
				} else {
					playAndPause
							.setImageResource(R.drawable.play_pause_button_play_style);
				}
			}
		});

		audio_volumn_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 暂时放在这儿，以后转移到主UI更新中去。
				isSilent = !isSilent;
				if (isSilent) {
					audio_volumn_button
							.setImageResource(R.drawable.audio_volumn_silent_style);
				} else {
					audio_volumn_button
							.setImageResource(R.drawable.audio_volumn_button_style);
				}

			}
		});

	}
}
