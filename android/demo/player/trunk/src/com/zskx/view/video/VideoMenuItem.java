package com.zskx.view.video;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zskx.activity.R;
/**
 * 视频菜单项
 * @author demo
 *
 */
public class VideoMenuItem extends LinearLayout {

	/**图片*/
	private ImageView image;
	/**文字*/
	private TextView text;
	/**视频文件所在的路径*/
	private String path;
	/**监听器*/
	private VideoMenuItemListener listener;
	public VideoMenuItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.videoMenuItem);
		int image_src_id = ta.getResourceId(R.styleable.videoMenuItem_imageSrc, 0);
		int text_id = ta.getResourceId(R.styleable.videoMenuItem_textId, 0);
		init(image_src_id,text_id);
	}

	/**
	 * 初始化
	 * @param imageId
	 * @param textId
	 */
	private void init(int imageId,int textId){
		View.inflate(getContext(), R.layout.video_menu_item, this);
		
		image = (ImageView)findViewById(R.id.image);
		text = (TextView)findViewById(R.id.text);
		
		//从布局文件里得到图片和文字
		if(imageId!=0){
			image.setImageResource(imageId);
		}
		if(textId!=0){
			text.setText(textId);
		}
		
		this.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				beClicked();
			}
		});
	}
	public String getPath() {
		return path;
	}

	/**
	 * 设置监听器
	 * @param listener
	 */
	public void setListener(VideoMenuItemListener listener) {
		this.listener = listener;
	}

	/**
	 * 设置相应视频文件存储的路径
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * 设置图片
	 * @param id
	 */
	public void setImage(int id){
		image.setImageResource(id);
	}
	/**
	 * 设置文字
	 * @param id
	 */
	public void setText(int id){
		text.setText(id);
	}
	/**
	 * 设置文字
	 * @param id
	 */
	public void setText(String text){
		this.text.setText(text);
	}
	/**
	 * 程序员手动选择，用于设置默认选中项
	 */
	public void selectedByHand(){
		this.setBackgroundResource(R.drawable.movie_list_bg_current);
	}
	/**
	 * 被点击后改变背景图片
	 */
	public void beClicked(){
		if(listener!=null){
			this.listener.itemBeClicked(path);
		}
		this.setBackgroundResource(R.drawable.movie_list_bg_current);
	}
	/**
	 * 变回没有被选中的状态
	 */
	public void becomeNormal(){
		this.setBackgroundResource(R.drawable.movie_list_bg);
	}
	/**
	 * 点击监听器
	 * @author demo
	 *
	 */
	public interface VideoMenuItemListener{
		/**
		 * 被点击调用返回该菜单项对应的路径
		 * @param path
		 */
		public void itemBeClicked(String path);
	}
}
