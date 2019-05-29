package com.zskx.pemsystem.adpater;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.zskx.net.response.MagazineDetailEntity;
import com.zskx.pemsystem.R;
import com.zskx.pemsystem.util.AsyncImageLoader;
import com.zskx.pemsystem.util.AsyncImageLoader.ImageCallback;

public class GalleryAdapter extends BaseAdapter{
	Context context;
	int width;
	//ArrayList<Drawable> imgViews = new ArrayList<Drawable>();
	ArrayList<MagazineDetailEntity> magazineList = new ArrayList<MagazineDetailEntity>();
	AsyncImageLoader asyncImageLoader =  new AsyncImageLoader();
	public GalleryAdapter(Context c , ArrayList<MagazineDetailEntity> imagelist){
		context = c;
		magazineList = imagelist;
		width = ((Activity) c).getWindowManager().getDefaultDisplay().getWidth();
	}
	@Override
	public int getCount() {
		return magazineList.size();
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
		final Holder holder;
		
		if(convertView != null )  holder = (Holder) convertView.getTag();
		else{
			holder = new Holder();
			holder.imageView = new ImageView(context);
			convertView = holder.imageView;
		}
		holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.music_record));
		String imageUrl = magazineList.get(position).getmagazineImage();
		Log.i("MagazineAdapter", "imageUrl:" + position + ">>>>>>>" + imageUrl);
		asyncImageLoader.loadDrawable(imageUrl, new ImageCallback(){

			@Override
			public void imageLoaded(Drawable imageDrawable, String imageUrl) {
				holder.imageView.setImageDrawable(imageDrawable);
			}});
		
		holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		holder.imageView.setLayoutParams(new Gallery.LayoutParams(width - 110, LayoutParams.FILL_PARENT));
		convertView.setTag(holder);
		
		/*final ImageView imageView = new ImageView(context);
		String imageUrl = magazineList.get(position).getmagazineImage();
		Log.i("MagazineAdapter", "imageUrl:" + position + ">>>>>>>" + imageUrl);
		asyncImageLoader.loadDrawable(imageUrl, new ImageCallback(){

			@Override
			public void imageLoaded(Drawable imageDrawable, String imageUrl) {
				imageView.setImageDrawable(imageDrawable);
			}});
		
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		//imageView.setLayoutParams(new Gallery.LayoutParams(136, 88));
		imageView.setLayoutParams(new Gallery.LayoutParams(width - 110, LayoutParams.FILL_PARENT));
		*/
		return convertView;
	}
	
	private class Holder{
		public ImageView imageView;
	}
}
