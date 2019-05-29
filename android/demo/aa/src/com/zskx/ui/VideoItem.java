package com.zskx.ui;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.zskx.activity.R;

public class VideoItem extends LinearLayout implements View.OnClickListener {

	private static String TAG = "VideoItem";

	public VideoItem(Context context, AttributeSet attrs) {
		super(context, attrs);

		View.inflate(getContext(), R.layout.video_item, this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:

			Log.d(TAG, "onTouchEvent action:ACTION_DOWN");

			break;

		case MotionEvent.ACTION_MOVE:

			Log.d(TAG, "onTouchEvent action:ACTION_MOVE");

			break;

		case MotionEvent.ACTION_UP:

			Log.d(TAG, "onTouchEvent action:ACTION_UP");

			break;

		case MotionEvent.ACTION_CANCEL:

			Log.d(TAG, "onTouchEvent action:ACTION_CANCEL");

			break;

		}

		if (event.getAction() == MotionEvent.ACTION_UP) {
			Log.i(TAG, "被分配了UP事件，认为该控件被点击了");
		}

		return super.onTouchEvent(event);
	}

	@Override
	public void onClick(View v) {
		((Activity) getContext()).moveTaskToBack(true);
	}
}
