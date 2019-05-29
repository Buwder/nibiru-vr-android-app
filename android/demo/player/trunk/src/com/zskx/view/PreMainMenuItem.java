package com.zskx.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zskx.activity.R;

public class PreMainMenuItem extends LinearLayout {

	/**
	 * 图片资源数组，顺序有效
	 */
	private int[] imageResId={R.drawable.index_ico_music,R.drawable.index_ico_movie,R.drawable.index_ico_relax,R.drawable.index_ico_crisis,R.drawable.index_ico_game,R.drawable.index_ico_magazine,R.drawable.index_ico_add};
	
	public PreMainMenuItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context,attrs);
	}

	public PreMainMenuItem(Context context) {
		super(context);
		init(context);
	}

	/**
	 * 初始化
	 * @param context
	 */
	private void init(Context context){
		View.inflate(context, R.layout.preview_main_menu_item, this);
	}
	
	/**
	 * 初始化
	 * @param context
	 */
	private void init(Context context,AttributeSet attrs){
		init(context);
		//从xml中得到配置到
		TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.preMainMenuItem);
		((ImageView)this.findViewById(R.pre_main_menu_item.iamge)).setImageResource(ta.getInt(R.styleable.preMainMenuItem_image_src, R.drawable.index_ico_add));
		((TextView)this.findViewById(R.pre_main_menu_item.text)).setText(ta.getInt(R.styleable.preMainMenuItem_text_id, R.string.null_string));
	}
	/**
	 * 设置图片
	 * @param id
	 */
	public void setImage(int id){
		((ImageView)this.findViewById(R.pre_main_menu_item.iamge)).setImageResource(id);
	}
	/**
	 * 设置描述
	 * @param id
	 */
	public void setText(int id){
		((TextView)this.findViewById(R.pre_main_menu_item.text)).setText(id);
	}
	/**
	 * 设置描述
	 * @param id
	 */
	public void setText(String id){
		((TextView)this.findViewById(R.pre_main_menu_item.text)).setText(id);
	}
	/**
	 * 使用默认顺序设置图片
	 * @param position
	 */
	public void setImageByDefaultSort(int position){
		if(position>=imageResId.length){
			position = imageResId.length-1;
		}
		((ImageView)this.findViewById(R.pre_main_menu_item.iamge)).setImageResource(imageResId[position]);
	}
}
