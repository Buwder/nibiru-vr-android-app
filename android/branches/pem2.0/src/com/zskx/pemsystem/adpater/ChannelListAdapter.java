package com.zskx.pemsystem.adpater;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zskx.net.response.HospitalVO;
import com.zskx.pemsystem.R;

public class ChannelListAdapter extends BaseAdapter {
	private static String TAG = "ChannelListAdapter";
	
	private Context context1;
	private Handler handler;
	private ArrayList<HospitalVO> hospitalVOs;
	private LayoutInflater mInflater;
	int i = 0;

	public ChannelListAdapter(Context context1,
			ArrayList<HospitalVO> hospitalVOs) {
		super();
		this.context1 = context1;
		this.hospitalVOs = hospitalVOs;
		Log.i(TAG, "ChannelListAdapter:" + hospitalVOs.size());
		mInflater = LayoutInflater.from(context1);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return hospitalVOs.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return hospitalVOs.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
//		HospitalVO vo = hospitalVOs.get(position);
//		String name = vo.getName();
//		String distance = String.valueOf(vo.getDistance());
//		Holder holder ;
//		
//		if (convertView == null) {  
//            
//                convertView = View.inflate(context1, R.layout.channel_list_item, null);
//  
//            holder = new Holder();  
//            holder.name = (TextView) convertView.findViewById(R.id.channel_list_name);  
//            holder.distance = (TextView) convertView.findViewById(R.id.channel_list_distance);
//        } else {  
//        	holder = (Holder) convertView.getTag();  
//        }  
//        holder.name.setText(name);  
//        holder.distance.setText(distance);  
////  Log.i(TAG, "size:" + name);
//        convertView.setTag(holder);
//        return convertView;  
        
        
        
		Holder holder;
		
		if(convertView != null) holder = (Holder) convertView.getTag();
		else{ 
			convertView =  mInflater.inflate( R.layout.channel_list_item, null);
			holder = new Holder();
			 holder.name = (TextView) convertView.findViewById(R.id.channel_list_name);  
			 holder.distance = (TextView) convertView.findViewById(R.id.channel_list_distance);
			 
			 System.out.println("holder::" + ++i);
		}
		
		holder.name.setText(hospitalVOs.get(position).getName());  
		holder.distance.setText(hospitalVOs.get(position).getDistance() + " km");  
		
		Log.i(TAG, "size:" + holder.name.getText().toString() +"  " + holder.distance.getText());
		
		convertView.setTag(holder);
		return convertView;
        
	}

	public static  class Holder {
		public TextView name;
		public TextView distance;
	}
	
	public ArrayList<HospitalVO> getHospitalVOs() {
		return hospitalVOs;
	}

	public void setHospitalVOs(ArrayList<HospitalVO> hospitalVOs) {
		this.hospitalVOs = hospitalVOs;
	}
}
