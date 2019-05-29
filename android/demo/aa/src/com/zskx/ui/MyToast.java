package com.zskx.ui;

import android.content.Context;
import android.widget.Toast;

public class MyToast {

	public static Toast toast;

	public static int TOAST_DURATION = 1000;

	public static void show(Context context, String tip) {

		if (toast != null) {
			toast.setText(tip);
		} else {

			toast = Toast.makeText(context, tip, TOAST_DURATION);
		}
		toast.show();
	}

}
