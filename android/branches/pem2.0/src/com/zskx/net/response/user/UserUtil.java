package com.zskx.net.response.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * 用户控制类
 * 
 * @author demo
 * 
 */
public class UserUtil {

	private static User user;

	/**
	 * 得到当前用户，如若没有有效用户则为一个空用户
	 * 
	 * @return
	 */
	public static User getUser() {
		if (user == null) {
			Log.d("User", "目前不存在有效的用户，故新建一个空用户");
			user = new User();
		}
		return user;
	}

	/**
	 * 设置一个用户
	 * 
	 * @param user
	 */
	public static void setUser(User user) {
		UserUtil.user = user;
	}

	/**
	 * 保存用户到手机
	 * 
	 * @param context
	 */
	public static void saveToPhone(Context context) {
		SharedPreferences preference = context.getSharedPreferences("user",
				Context.MODE_PRIVATE);
		Editor editor = preference.edit();
		editor.putString("userId", user.getLoginName());
		editor.putString("userPwd", user.getUserPwd());

	}
}
