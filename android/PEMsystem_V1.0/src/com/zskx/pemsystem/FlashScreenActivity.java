package com.zskx.pemsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class FlashScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.flash_screen);
		
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				startActivity(new Intent(FlashScreenActivity.this,
						LoginActivity.class));

				overridePendingTransition(R.anim.fade, R.anim.hold);

				FlashScreenActivity.this.finish();

			}
		}, 3000);
	}

	
}
