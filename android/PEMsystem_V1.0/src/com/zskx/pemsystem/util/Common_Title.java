package com.zskx.pemsystem.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zskx.pemsystem.HomeActivity;
import com.zskx.pemsystem.R;

public class Common_Title extends RelativeLayout {

	private Context context;

	private TextView title_txt_view;

	private ImageView home_btn;

	private ImageView back_btn;

	/*** 加载进度圈 */
	private ProgressBar progress;

	public Common_Title(Context context) {
		super(context);
		init(context, null);
	}

	public Common_Title(Context context, AttributeSet attrs) {
		super(context, attrs);

		init(context, attrs);
	}

	public Common_Title(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 * @param attrs
	 */
	private void init(Context context, AttributeSet attrs) {

		View.inflate(context, R.layout.title_layout, this);

		this.context = context;

		String title_txt = "";
		if (attrs != null) {
			TypedArray ta = context.obtainStyledAttributes(attrs,
					R.styleable.comm_title);
			title_txt = ta.getString(R.styleable.comm_title_text);
		}

		title_txt_view = (TextView) findViewById(R.title.title_txt);
		title_txt_view.setText(title_txt);
		title_txt_view.setTextSize(20);
		
		home_btn = (ImageView) findViewById(R.title.home_btn);
		back_btn = (ImageView) findViewById(R.title.back_btn);
		progress = (ProgressBar) findViewById(R.title.progressBar);

		setClickAction();

	}

	/**
	 * 设置标题
	 * 
	 * @param txt
	 */
	public void setTitleTxt(final String txt) {
		if (txt == null) {
			Log.e(this.getClass().getSimpleName(), "给出的标题名为null，请检查！");
			return;
		}
		MyPost.post(new Runnable() {

			@Override
			public void run() {
				title_txt_view.setText(txt);
			}
		});
	}

	/**
	 * 开始显示加载进度圈
	 */
	public void startShowProgress() {
		MyPost.post(new Runnable() {

			@Override
			public void run() {
				back_btn.setVisibility(View.GONE);
				progress.setVisibility(View.VISIBLE);
			}
		});

	}

	/**
	 * 停止显示加载进度圈
	 */
	public void stopShowProgress() {

		MyPost.post(new Runnable() {

			@Override
			public void run() {
				progress.setVisibility(View.GONE);
				back_btn.setVisibility(View.VISIBLE);
			}
		});

	}

	/**
	 * 得到标题内容
	 * 
	 * @return
	 */
	public String getTitleTxt() {
		return title_txt_view.getText().toString();
	}

	/**
	 * 设置home和back点击事件的动作
	 */
	private void setClickAction() {

		/*********************** for test ***********************/
		// final boolean test = true;
		// this.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// if (test)
		// stopProgress();
		// else {
		// startProgress();
		// }
		// test = !test;
		// }
		// });
		/*********************** for test ***********************/

		back_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Common_Title.this.context instanceof Activity) {
					((Activity) Common_Title.this.context).finish();
				}
			}
		});

		home_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Common_Title.this.context,
						HomeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				((Activity) Common_Title.this.context).startActivity(intent);
			}
		});

	}

}
