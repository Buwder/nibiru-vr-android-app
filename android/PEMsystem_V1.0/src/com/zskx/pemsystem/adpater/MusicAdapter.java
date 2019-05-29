package com.zskx.pemsystem.adpater;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zskx.net.response.MusicTypeEntity;
import com.zskx.pemsystem.Music_Activity.MyHandler;
import com.zskx.pemsystem.R;
import com.zskx.pemsystem.util.AsyncImageLoader;
import com.zskx.pemsystem.util.AsyncImageLoader.ImageCallback;


/**
 * 音乐列表适配器
 * @author 
 *
 */
public class MusicAdapter extends BaseAdapter{
	private final String TAG = "MusicAdapter";
	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<MusicTypeEntity> typeList;
	private MyHandler myHandler;
	private AsyncImageLoader asyncImageLoader;   
	private final static int IMAGE = 0x0778;
	int i = 0;
	public MusicAdapter(Context context, ArrayList<MusicTypeEntity> types , MyHandler myHandle ,AsyncImageLoader asyncImageLoader)
	{
		Log.i(TAG, "types::" + types.size());
		mContext=context;
		mInflater=LayoutInflater.from(mContext);
		typeList = types;
		this.asyncImageLoader = asyncImageLoader;
		myHandler = myHandle;
	}
	@Override
	public int getCount() {
		return typeList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final MusicTypeHolder holder;
		
		if(convertView != null) holder = (MusicTypeHolder) convertView.getTag();
		else{ 
			System.out.println("holder::" + ++i);
			convertView = (LinearLayout) mInflater.inflate(R.layout.music_list_item, null);
			holder = new MusicTypeHolder();
			holder.imageView = (ImageView) convertView.findViewById(R.id.music_type_imageview);
			holder.txtTitle = (TextView) convertView.findViewById(R.id.music_type_title);
			holder.txtIntro = (TextView) convertView.findViewById(R.id.music_type_intro);
		}
		
		holder.imageView.setImageDrawable(null);
		String url = typeList.get(position).getMusicTypeImage();
		holder.imageView.setTag(url);
		if(url != null && !url.equals(""))	{
			asyncImageLoader.loadDrawable(url, new ImageCallback(){

				@Override
				public void imageLoaded(Drawable imageDrawable, String imageUrl) {
					if(((String)holder.imageView.getTag()).equals(imageUrl)) 
						holder.imageView.setImageDrawable(imageDrawable);
					
//					Message message = myHandler.obtainMessage();
//					message.what = IMAGE;
				}});
		}
		
		holder.txtTitle.setText(typeList.get(position).getMusicTypeTitle());
		holder.txtIntro.setText(typeList.get(position).getMusicTypeDescription());
		convertView.setTag(holder);
		return convertView;
	}

	
	public static class MusicTypeHolder{
		public ImageView imageView;
		public TextView txtTitle;
		public TextView txtIntro;
	}
}
