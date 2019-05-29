package com.zskx.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class ReportDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.report_detail);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.my_titlebar);

		((TextView) findViewById(R.id.big)).setText("体检报告");
		((TextView) findViewById(R.id.small)).setText(" ");
	}

}
