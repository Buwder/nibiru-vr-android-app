package com.zskx.pemsystem.adpater;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zskx.net.response.ConsultantEntity;
import com.zskx.pemsystem.R;
import com.zskx.pemsystem.util.ImageLoader;
import com.zskx.pemsystem.util.ImageLoader.ImageCallback;

public class ConsultantAdapter extends BaseAdapter {
	private static String TAG = "ConsultantAdapter";
	private static String STATUS1 = "online";
	private static String STATUS2 = "busy";
	private ArrayList<ConsultantEntity> consultantList;
	private ImageLoader il;
	int i = 0;

	private Context context1;
	private Handler handler;

	public ConsultantAdapter(ArrayList<ConsultantEntity> arrayList,
			Context context) {
		super();
		this.consultantList = arrayList;
		this.il = new ImageLoader(context.getClass().getSimpleName());
		this.context1 = context;
		this.handler = handler;
	}

	@Override
	public int getCount() {
		return consultantList.size();
	}

	@Override
	public Object getItem(int position) {
		return consultantList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (convertView != null) {
			holder = (Holder) convertView.getTag();
		} else {

			System.out.println("holder::" + ++i);
			convertView =  View.inflate(context1, R.layout.consultant_list_item, null);
			holder = new Holder();
			holder.imageView = ((ImageView) convertView
					.findViewById(R.id.consultant_img));
			holder.txtName = ((TextView) convertView
					.findViewById(R.id.consultant_name));
			holder.txtDescription = ((TextView) convertView
					.findViewById(R.id.consultant_description));
			holder.txtStatus = ((TextView) convertView
					.findViewById(R.id.consultant_status));
			
			holder.imageView.setImageDrawable(this.context1.getResources()
					.getDrawable(R.drawable.test_item_img));
		}
		holder.imageView.setImageDrawable(null);
		String img_url = ( (ConsultantEntity)this.consultantList.get(position))
				.getConsultantImage();
		holder.imageView.setTag(img_url);
		if ((img_url != null) && (!img_url.equals(""))) {
			il.loadDrawable(context1.getClass().getSimpleName(),img_url, new ImageCallback() {

				@Override
				public void imageLoaded(Drawable imageDrawable, String imageUrl) {
					if (((String) holder.imageView.getTag()).equals(imageUrl)){
						holder.imageView.setImageDrawable(imageDrawable);
					}
					
				}
			});
		}
		holder.txtName.setText(((ConsultantEntity) this.consultantList.get(position))
				.getConsultantName());
		holder.txtDescription.setText(((ConsultantEntity) this.consultantList.get(position))
				.getConsultantDescription());
		
		if(((ConsultantEntity) this.consultantList.get(position))
				.getConsultantStatus().equals(STATUS1)){
			holder.txtStatus.setText(STATUS1);
		}else{
			holder.txtStatus.setText(STATUS2);
		}
		convertView.setTag(holder);

		return convertView;
	}



	public static final class Holder {
		public ImageView unsend;
		public ImageView imageView;
		public TextView txtName;
		public TextView txtDescription;
		public TextView txtStatus;
	}
}
