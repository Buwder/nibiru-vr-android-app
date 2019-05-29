package com.zskx.pemsystem.adpater;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zskx.net.response.VideoDetailEntity;

import com.zskx.pemsystem.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MovieItemAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<VideoDetailEntity> mVideoEntityList;
	private MovieItemAdapter adapter;
	private Map<Integer, PositionBitmap> map = new HashMap<Integer, PositionBitmap>();

	public MovieItemAdapter(Context context,
			List<VideoDetailEntity> videoEntityList) {
		this.mContext = context;
		this.mVideoEntityList = videoEntityList;
		mInflater = LayoutInflater.from(mContext);
		adapter=this;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LinearLayout view = null;

		VideoHolder videoHolder = null;

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

		}

		VideoDetailEntity vde = mVideoEntityList.get(position);
		PositionBitmap pb = map.get(position);
		if (pb == null || pb.bitmap == null) {
			pb = new PositionBitmap();

			pb.position = position;
			map.put(position, pb);
			loadImage(vde.getVideoImage(), position);
		} else {
			videoHolder.imageView.setImageBitmap(pb.bitmap);
		}

		videoHolder.textVideoName.setText(vde.getVideoTitle());

		view.setTag(videoHolder);

		return view;
	}

	// 下载图片
	private void loadImage(String url, int position) {
		LoadImageThread load = new LoadImageThread(url, position);
		load.start();
	}

	class VideoHolder {
		public ImageView imageView;
		public TextView textVideoName;
		public int position;

	}

	class PositionBitmap {
		public Bitmap bitmap = null;
		public int position;
	}

	/**
	 * 
	 * 下载 图片
	 * 
	 */
	class LoadImageThread extends Thread {
		String urlStr = "";
		int position = -1;

		public LoadImageThread(String urlStrParm, int positionParm) {
			urlStr = urlStrParm;
			position = positionParm;

		}

		@Override
		public void run() {
			try {
				URL url = new URL(urlStr);
				System.out.println("loadImage:---->"+urlStr);
				HttpURLConnection httpURLConnection = (HttpURLConnection) url
						.openConnection();
				httpURLConnection.connect();
				if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
					InputStream is = httpURLConnection.getInputStream();

					Bitmap bitmap = BitmapFactory.decodeStream(is);
					is.close();
					
					FileOutputStream fi=new FileOutputStream("/sdcard/bitmap.png");
					bitmap.compress(CompressFormat.PNG, 0,fi );

					fi.close();
					System.out.println("hahahahaaaahahahhhhah");
					Message msg = new Message();
					
					msg.obj = bitmap;
					msg.arg1 = position;
					handler.sendMessage(msg);

				}else
				{
					System.out.println("LoadImage:_______----->failed");
				}

			} catch (Exception e) {

				e.printStackTrace();
				System.out.println("下载图片：-----》occure a  exception");
			}

		}
	}
	
	
	//处理下载完成后的 界面 更新

	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			PositionBitmap pb = map.get(msg.arg1);
			pb.bitmap = (Bitmap) msg.obj;
			pb.position = msg.arg1;
		
			adapter.notifyDataSetChanged();

		};

	};

}
