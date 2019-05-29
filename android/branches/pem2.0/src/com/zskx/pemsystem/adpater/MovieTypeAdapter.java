package com.zskx.pemsystem.adpater;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zskx.net.response.VideoTypeEntity;
import com.zskx.pemsystem.R;
import com.zskx.pemsystem.util.ImageLoader;
import com.zskx.pemsystem.util.ImageLoader.ImageCallback;
import com.zskx.pemsystem.util.StringsUtil;

/**
 * 视频系列适配器
 * 
 */
public class MovieTypeAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<VideoTypeEntity> movieItems;
	private Resources res;
	private ImageLoader il;
	private boolean isReception = true;// 是否 界面 处于前台

	private final static int DESCRIPTION_LENGTH = 35;

	public MovieTypeAdapter(Context context,
			List<VideoTypeEntity> movieItemsArray) {
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		this.movieItems = movieItemsArray;
		res = context.getResources();
		il = new ImageLoader(context.getClass().getSimpleName());
	}

	public boolean isReception() {
		return isReception;
	}

	public void setReception(boolean isReception) {
		this.isReception = isReception;
	}

	@Override
	public int getCount() {

		if (movieItems != null) {
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
Log.w(getClass().getSimpleName(), "into getView method.");
		LinearLayout view = null;
		final MovieHolder holder;
		if (convertView != null) {
			view = (LinearLayout) convertView;
			holder = (MovieHolder) view.getTag();
		} else {
			view = (LinearLayout) mInflater.inflate(
					R.layout.movie_type_list_item, null);
			holder = new MovieHolder();
			holder.subTypeName = (TextView) view
					.findViewById(R.id.textView_sub_type_video);
			holder.sunTypeDescription = (TextView) view
					.findViewById(R.id.textView_sub_type_description);
			holder.subTypeImage = (ImageView) view
					.findViewById(R.id.movie_type_image);
		}

		final VideoTypeEntity vTypeEntity = movieItems.get(position);

		holder.subTypeImage.setTag(vTypeEntity.getVideoTypeImageUrl());
		holder.subTypeName.setText(vTypeEntity.getVideoTypeTitle());

		String description = vTypeEntity.getVideoTypeSummary();

		description = StringsUtil.subString(description, DESCRIPTION_LENGTH);

		holder.sunTypeDescription.setText(description);

		holder.subTypeImage.setImageDrawable(null);

		il.loadDrawable(mContext.getClass().getSimpleName(),vTypeEntity.getVideoTypeImageUrl(),
				new ImageCallback() {

				

					@Override
					public void imageLoaded(Drawable imageDrawable,
							String imageUrl) {

						if (imageDrawable != null) {

							if (((String) holder.subTypeImage.getTag())
									.equals(imageUrl)) {
								holder.subTypeImage
										.setImageDrawable(imageDrawable);
								System.out.println("position:--->" + "--->"
										+ imageUrl);
							}

						} else {
							// if(((String)holder.subTypeImage.getTag()).equals(imageUrl))
							// {
							// holder.subTypeImage.setImageDrawable(res.getDrawable(R.drawable.movie_class_img));
							// System.out.println("position:--->"+"--->"+imageUrl);
							// }
							//
//							if (isReception)
//								asyncImageLoader.loadDrawable(
//										vTypeEntity.getVideoTypeImageUrl(),
//										this);
							
							System.out.println("MovieTypeAdapter:---drawable is null!");
						}

						
					}
				});

		view.setTag(holder);
		return view;
	}

	/**
	 * 类型项目索引
	 * 
	 * @author wqp
	 * 
	 */
	class MovieHolder {

		public TextView subTypeName;
		public ImageView subTypeImage;
		public TextView sunTypeDescription;

	}

}
