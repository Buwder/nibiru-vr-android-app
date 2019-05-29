package com.zskx.controller;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.util.Log;

/**
 * activity窗口的控制器
 * @author demo
 *
 */
public class ActivityHolder {

	/**窗口控制器*/
	private static ActivityHolder holder;
	/**窗口列表*/
	private List<Activity> list;
	
	/**
	 * 禁止外部实例化
	 */
	private ActivityHolder(){
		list = new LinkedList<Activity>();
	}
	/**
	 * 得到唯一实例
	 * @return
	 */
	public static ActivityHolder getIntance(){
		if(holder==null){
			holder = new ActivityHolder();
		}
		return holder;
	}
	
	
	/**
	 * 将窗口加入到列表。就是当前的窗口状态转为stop时就压入到控制器到“栈”里
	 * @param activity
	 */
	public boolean pop(Activity activity){
		if(activity!=null){
			list.add(activity);
			Log.i(this.getClass().getSimpleName(), "压入栈内到是："+activity.getClass().getSimpleName());
			return true;
		}
		
		Log.i(this.getClass().getSimpleName(), "压入的窗口为空!不放入栈中");
		return false;
	}
	/**
	 * 
	 * @param activity
	 * @return
	 */
	public boolean push(Activity activity){
		int flag = -1;
		int position = 0;
		for (Activity act : list) {
			//用==，因为理论上为同一个引用
			if(act==activity){
				flag = position;
				// list.remove(position);在对链表进行使用iterator遍历时，对链表进行add/remove不会同步到iterator里，所以就会报ConcurrentModificationException
				Log.i(this.getClass().getSimpleName(), "出栈的是："+activity.getClass().getSimpleName());
				return true;
			}
			position++;
		}
		
		if(flag!=-1){
			list.remove(flag);
		}
		
		Log.i(this.getClass().getSimpleName(), "列表中没有找到该窗口，其实可以视为出栈。");
		return false;
	}
	/**
	 * 关闭程序拥有的所有窗口
	 */
	public void closeActivitys(){
//		int position = 0;
		for (Activity act : list) {
			
			//list.remove(position);在对链表进行使用iterator遍历时，对链表进行add/remove不会同步到iterator里，所以就会报ConcurrentModificationException
			//结束窗口
			act.finish();
			Log.i(this.getClass().getSimpleName(), act.getClass().getSimpleName()+"关闭！");
		}
		list.clear();
	}
}
