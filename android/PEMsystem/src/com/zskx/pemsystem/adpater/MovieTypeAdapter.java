package com.zskx.pemsystem.adpater;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zskx.net.response.VideoTypeEntity;
import com.zskx.pemsystem.R;

/**
 * 视频系列适配器
 *
 */
public class MovieTypeAdapter extends BaseAdapter{

	private Context mContext;
	private LayoutInflater mInflater;
	private List<VideoTypeEntity> movieItems;
	
	
	public MovieTypeAdapter(Context context,List<VideoTypeEntity> movieItemsArray)
	{
		mContext=context;
		mInflater=LayoutInflater.from(mContext);
		this.movieItems=movieItemsArray;
	}
	
	@Override
	public int getCount() {
	
       if(movieItems != null)
       {
    	   return movieItems.size();
       }
		
		return 0;
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
	
		LinearLayout view=null;
		MovieHolder holder=null;
		if(convertView !=null)
		{
			view=(LinearLayout) convertView;
			holder=(MovieHolder) view.getTag();
		}else
		{
			view=(LinearLayout) mInflater.inflate(R.layout.movie_type_list_item, null);
			holder=new MovieHolder();
			holder.subTypeName=(TextView) view.findViewById(R.id.textView_sub_type_video);
		}
		
		holder.subTypeName.setText(movieItems.get(position).getVideoTypeTitle());
		
		view.setTag(holder);
		return view;
	}
	
	
	class MovieHolder
	{
		public TextView subTypeName;
	}

}
