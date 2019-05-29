package com.zskx.sleep;

import android.content.Context;
import android.widget.Toast;

public class MyToast {

	public static Toast toast;

	public static void show(Context context, String tip) {
		if (toast != null) {
			toast.cancel();
			toast.setText(tip);
		} else {
			toast = Toast.makeText(context, tip, 1000);
		}
		toast.show();
	}

}
