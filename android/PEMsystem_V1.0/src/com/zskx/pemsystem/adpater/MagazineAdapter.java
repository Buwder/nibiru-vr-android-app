package com.zskx.pemsystem.adpater;

import java.util.ArrayList;

import android.R.color;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zskx.net.response.MagazineDetailEntity;
import com.zskx.pemsystem.R;
import com.zskx.pemsystem.util.AsyncImageLoader;
import com.zskx.pemsystem.util.AsyncImageLoader.ImageCallback;

/**
 * 杂志 列表适配器
 * 
 * @author demo
 * 
 */
public class MagazineAdapter extends BaseAdapter {

	private AsyncImageLoader ail;

	private ArrayList<MagazineDetailEntity> bindData;

	private Context context;

	public MagazineAdapter(ArrayList<MagazineDetailEntity> data, Context context) {
		ail = new AsyncImageLoader();
		this.context = context;
		setBindData(data);
	}

	/**
	 * 设置绑定的数据
	 */
	public void setBindData(ArrayList<MagazineDetailEntity> data) {
		this.bindData = data;
	}

	public ArrayList<MagazineDetailEntity> getBindData() {
		return this.bindData;
	}

	@Override
	public int getCount() {
		return bindData.size();
	}

	@Override
	public Object getItem(int position) {

		return bindData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public class HolderTag {
		public TextView title;

		public TextView summary;

		public ImageView iv;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {

			convertView = View.inflate(context, R.layout.magazine_shelf_item,
					null);
			HolderTag ht = new HolderTag();
			ht.title = (TextView) convertView
					.findViewById(R.magazine_item.title);
			ht.summary = (TextView) convertView
					.findViewById(R.magazine_item.summary);
			ht.iv = (ImageView) convertView.findViewById(R.magazine_item.img);
			convertView.setTag(ht);
		}
		final HolderTag ht = (HolderTag) convertView.getTag();

		if (!(bindData.get(position)
				.getMagazineImageUrl()).equals(ht.iv.getTag())) {
			ht.iv.setImageDrawable(null);
		}

		ht.iv.setTag(
				bindData.get(position).getMagazineImageUrl());
		final String test = new String();
		ail.loadDrawable(
				bindData.get(position).getMagazineImageUrl(),
				new ImageCallback() {

					@Override
					public void imageLoaded(Drawable imageDrawable,
							String imageUrl) {
						String a = test;
						if (ht.iv.getTag().equals(
								(bindData.get(
										position).getMagazineImageUrl()))) {
							ht.iv.setImageDrawable(imageDrawable);
						}
					}
				});

		ht.title.setText(bindData.get(position).getMagazineTitle());

		ht.summary.setText(bindData.get(position).getMagazineSummary());

		if (position == 0) {
			convertView.setBackgroundResource(color.transparent);
		} else {
			convertView
					.setBackgroundResource(R.drawable.magazine_bookshelf_item_backgroud);
		}
		return convertView;
	}
}
