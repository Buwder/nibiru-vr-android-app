package com.zskx.pemsystem.adpater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zskx.net.response.MusicDetailEntity;
import com.zskx.pemsystem.R;

/**
 * 音乐项目子列表
 *
 */
public class MusicTypeChildAdapter extends BaseAdapter{

	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<MusicDetailEntity> typeList;
	public  Map<Integer, Boolean> isSelected;  
	
	public MusicTypeChildAdapter(Context context , ArrayList<MusicDetailEntity> list)
	{
		mContext=context;
		mInflater=LayoutInflater.from(mContext);
		typeList = list;
		
		initSelected();    
	} 

	private void initSelected() {
		isSelected = new HashMap<Integer, Boolean>();    
		   
		for (int i = 0; i < typeList.size(); i++) {    
		isSelected.put(i, true);    
		}
	}
	
	@Override
	public int getCount() {
	//	System.out.println("typeList::" + typeList.size());
		return typeList.size();
	}

	@Override
	public Object getItem(int position) {
		return typeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		MusicDetailHolder holder;
		if(convertView != null)  holder = (MusicDetailHolder) convertView.getTag();
		else{
			LinearLayout linearLayout=(LinearLayout) mInflater.inflate(R.layout.music_type_child_item, null);
			holder = new MusicDetailHolder();
			holder.textView = (TextView) linearLayout.findViewById(R.id.music_type_item);
			holder.checkBox = (CheckBox) linearLayout.findViewById(R.id.music_type_checkbox);
			convertView = linearLayout;
		}
		holder.textView.setText(typeList.get(position).getMusicTitle());
	//	holder.checkBox.setChecked(isSelected.get(position));
		holder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	
				if (isChecked) {
					isSelected.put(position, isChecked);
				} else {
					isSelected.remove(position);
				}
			}
	
		});

		holder.checkBox.setChecked((isSelected.get(position) == null ? false : true));
		convertView.setTag(holder);
		return convertView;
	}

	public static class MusicDetailHolder{
		public TextView textView;
		public CheckBox checkBox;
		public String imageUrl;
	}
}
