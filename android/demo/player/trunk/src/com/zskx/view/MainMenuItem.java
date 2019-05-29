package com.zskx.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zskx.activity.R;
/**
 * 主页面功能选项
 * @author demo
 *
 */
public class MainMenuItem extends LinearLayout {

	/**功能名称*/
	private TextView menuText;
	/**功能图片*/
	private ImageView menuImage;
	/**主菜单监听器*/
	private MainMenuOnClickListener listener;
	/**菜单的标签*/
	private int tag;
	
	public MainMenuItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init(){
		View.inflate(getContext(), R.layout.main_menu_item, this);
		
		menuText=(TextView)findViewById(R.id.main_menu_text);
		menuImage=(ImageView)findViewById(R.id.main_menu_image);
		
		this.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 beClicked();
			}
		});
	}
	/**
	 * 被点击后做的事
	 */
	public void beClicked(){
		if (listener!=null) {
			listener.menuBeClicked(tag);
		}
		 MainMenuItem.this.setBackgroundResource(R.drawable.menu_current);
	}
	/**
	 * 设置菜单的tag
	 * @param tag
	 */
	public void setMenuTag(int tag){
		this.tag = tag;
	}
	/**
	 * 设置菜单文字，通过资源id
	 * @param ResId
	 */
	public void setMenuText(int ResId){
		menuText.setText(ResId);
	}
	/**
	 * 设置菜单文字
	 * @param text
	 */
	public void setMenuText(String text){
		menuText.setText(text);
	}
	/**
	 * 设置菜单图片
	 * @param ResId
	 */
	public void setMenuImage(int ResId){
		menuImage.setImageResource(ResId);
	}
	/**
	 * 设置监听器
	 * @param listener
	 */
	public void setMainMenuListener(MainMenuOnClickListener listener){
		this.listener = listener;
	}
	/**
	 * 菜单背景变为没被选中效果
	 */
	public void becomeNormal(){
		MainMenuItem.this.setBackgroundResource(R.drawable.menu_normal);
	}
	/**
	 * 菜单监听器
	 * @author demo
	 *
	 */
	public interface MainMenuOnClickListener{
		/**
		 * 菜单被点击
		 * @param tag 菜单标记
		 */
		public void menuBeClicked(int tag);
	}
}
