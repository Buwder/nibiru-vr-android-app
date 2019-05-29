package com.zskx.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView {

	private static String TAG = "CustomScrollView";

	public CustomScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {

		case MotionEvent.ACTION_DOWN:

			Log.d(TAG, "onInterceptTouchEvent action:ACTION_DOWN");

			break;

		case MotionEvent.ACTION_MOVE:

			Log.d(TAG, "onInterceptTouchEvent action:ACTION_MOVE");

			break;

		case MotionEvent.ACTION_UP:

			Log.d(TAG, "onInterceptTouchEvent action:ACTION_UP");

			break;

		case MotionEvent.ACTION_CANCEL:

			Log.d(TAG, "onInterceptTouchEvent action:ACTION_CANCEL");

			break;

		}

		boolean superResult = super.onInterceptTouchEvent(ev);
		Log.d(TAG, "super.onInterceptTouchEvent(ev):" + superResult);
		return superResult;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {

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

		boolean superResult = super.onTouchEvent(ev);
		Log.d(TAG, "super.onTouchEvent(ev):" + superResult);
		return superResult;

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {

		case MotionEvent.ACTION_DOWN:

			Log.d(TAG, "dispatchTouchEvent action:ACTION_DOWN");

			break;

		case MotionEvent.ACTION_MOVE:

			Log.d(TAG, "dispatchTouchEvent action:ACTION_MOVE");

			break;

		case MotionEvent.ACTION_UP:

			Log.d(TAG, "dispatchTouchEvent action:ACTION_UP");

			break;

		case MotionEvent.ACTION_CANCEL:

			Log.d(TAG, "dispatchTouchEvent action:ACTION_CANCEL");

			break;

		}
		boolean superResult = super.dispatchTouchEvent(ev);
		Log.d(TAG, "super.dispatchTouchEvent(ev):" + superResult);
		return superResult;
	}

}
