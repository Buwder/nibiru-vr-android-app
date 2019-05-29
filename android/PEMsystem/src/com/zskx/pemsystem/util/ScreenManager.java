package com.zskx.pemsystem.util;

import java.util.Stack;

import android.app.Activity;

/**
 * 界面 Activity 管理
 * 
 * @author wqp
 * 
 */
public class ScreenManager {

	private static Stack<Activity> activityStack;

	private static ScreenManager instance;

	private ScreenManager() {
	}

	/**
	 * 得到单例对象
	 * 
	 * @return ScreenManager
	 */
	public static ScreenManager getScreenManager() {

		if (instance == null) {

			instance = new ScreenManager();

		}
		return instance;
	}

	/**
	 * 从栈中弹出指定的 Activity 并销毁
	 */
	public void popActivity(Activity activity) {

		if (activity != null) {
			activity.finish();
			activityStack.removeElement(activity);
			activity = null;
		}
	}

	/**
	 * 从栈中弹出顶端的 Activity 并销毁
	 */
	public void popActivity() {
		Activity activity = activityStack.lastElement();
		if (activity != null) {
			activity.finish();
			activityStack.removeElement(activity);
			activity = null;
		}
	}

	/**
	 * 得到当前位于栈顶的 Activity
	 * 
	 * @return Activity
	 */
	public Activity getCurrentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 将指定的 Activity 实例推入栈
	 */
	public void pushActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 弹出所有 Activity
	 */
	public void popAllActivity() {
		if (activityStack != null) {
			while (activityStack.size() > 0) {
				popActivity();
			}
		}
	}

	/**
	 * 弹出除了指定的 Class 外所有 Activity
	 * 
	 * @param cls
	 */
	public void popAllActivityExceptOne(Class<?> cls) {

		while (true) {

			Activity activity = getCurrentActivity();

			if (activity == null) {

				break;

			}

			if (activity.getClass().equals(cls)) {

				break;

			}

			popActivity(activity);

		}

	}

}
