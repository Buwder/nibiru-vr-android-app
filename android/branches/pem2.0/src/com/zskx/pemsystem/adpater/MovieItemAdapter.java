package com.zskx.pemsystem.adpater;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zskx.net.response.VideoDetailEntity;
import com.zskx.pemsystem.R;
import com.zskx.pemsystem.util.ImageLoader;
import com.zskx.pemsystem.util.ImageLoader.ImageCallback;
import com.zskx.pemsystem.util.StringsUtil;

public class MovieItemAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<VideoDetailEntity> mVideoEntityList;
	private final static int DESCRIPTION_LENGTH = 35;
	private ImageLoader il;
	private boolean isReception = true;
	
	public boolean isReception() {
		return isReception;
	}

	public void setReception(boolean isReception) {
		this.isReception = isReception;
	}

	public MovieItemAdapter(Context context,
			List<VideoDetailEntity> videoEntityList) {
		this.mContext = context;
		this.mVideoEntityList = videoEntityList;
		mInflater = LayoutInflater.from(mContext);
		il = new ImageLoader(context.getClass().getSimpleName());
	}

	@Override
	public int getCount() {

		if (mVideoEntityList != null) {
			return mVideoEntityList.size();
		}

		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	final @Override
	public View getView(int position, final View convertView, ViewGroup parent) {

		LinearLayout view = null;

		final VideoHolder videoHolder;

		String s;

		if (convertView != null) {
			view = (LinearLayout) convertView;
			videoHolder = (VideoHolder) view.getTag();

		} else {
			view = (LinearLayout) mInflater.inflate(R.layout.video_item_detail,
					null);
			videoHolder = new VideoHolder();
			videoHolder.imageView = (ImageView) view
					.findViewById(R.id.video_image);
			videoHolder.textVideoName = (TextView) view
					.findViewById(R.id.video_name);
			videoHolder.textDescription = (TextView) view
					.findViewById(R.id.video_description);

		}

		final VideoDetailEntity vde = mVideoEntityList.get(position);

		videoHolder.textVideoName.setText(vde.getVideoTitle());

		String description = vde.getVideoSummary();
		description = StringsUtil.subString(description, DESCRIPTION_LENGTH);

		videoHolder.textDescription.setText(description);
		s = vde.getVideoImage();
		videoHolder.imageView.setTag(s);
		videoHolder.imageView.setImageDrawable(null);

		il.loadDrawable(mContext.getClass().getSimpleName(),vde.getVideoImage(), new ImageCallback() {


			@Override
			public void imageLoaded(Drawable imageDrawable, String imageUrl) {
				// TODO Auto-generated method stub
				if (imageDrawable != null) {

					if (((String) videoHolder.imageView.getTag())
							.equals(imageUrl)) {
						videoHolder.imageView.setImageDrawable(imageDrawable);
					}

				} else {
					// if(((String)videoHolder.imageView.getTag()).equals(imageUrl))
					// {
					// //videoHolder.imageView.setImageDrawable(imageDrawable);
					// videoHolder.imageView.setImageResource(R.drawable.movie_list_img);
					// }
					// videoHolder.imageView.setImageDrawable(R.drawable.movie_list_img);
//					if (isReception)
//						asyncImageLoader.loadDrawable(vde.getVideoImage(), this);
					
					System.out.println("MovieItemAdapter:---drawable is null!");
				}
			}
		});

		view.setTag(videoHolder);

		return view;
	}

	class VideoHolder {
		public ImageView imageView;
		public TextView textVideoName;
		public TextView textDescription;

	}

}
