package com.zskx.net.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.zskx.net.request.AbstractRequest.GetResponseListener;
import com.zskx.net.request.LoginRequest;
import com.zskx.net.response.ResponseEntity;
import com.zskx.net.response.user.User;

public class TestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		setContentView(layout);
		Button b = new Button(this);
		//final StringBuffer sb = new StringBuffer();
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
			//	sb.append("aaaa0");

				final Dialog dialog = new AlertDialog.Builder(TestActivity.this)
						.setMessage("加载吧少年！").create();
				dialog.setTitle("访问网络");
				LoginRequest request = new LoginRequest(
						new GetResponseListener<User>() {

							@Override
							public void onSuccess(ResponseEntity<User> result) {
								System.out.println("访问网络返回数据！");
								dialog.dismiss();
							}

							@Override
							public void onError(ResponseEntity<User> result) {
								System.out.println("访问网络失败！");
								dialog.dismiss();
							

							}
						}, "", "");
				request.sendByThread();
				dialog.show();
			}
		});

		layout.addView(b);
	}

}
