package com.zskx.pemsystem.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class AsyncImageLoader {

	public static String TAG = "AsyncImageLoader";

	private HashMap<String, SoftReference<Drawable>> imageCache;

	private List<String>  loadingUrls=new ArrayList<String>();
	
	Drawable drawable;
	int i = 0;
	public AsyncImageLoader() {
		imageCache = new HashMap<String, SoftReference<Drawable>>();
	}

	/**
	 * 加载图片并处理
	 * 
	 * @param imageUrl
	 * @param imageCallback
	 */
	public void loadDrawable(final String imageUrl,
			final ImageCallback imageCallback) {

//		Log.i(TAG, "loadDrawable:" + imageUrl);

		// 异步处理图片
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				if (message.obj != null){
					imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
				}
			}
		};
		// 如果缓存内含有图片,直接返回缓存内图片
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			Drawable drawable = softReference.get();
			if (drawable != null) {
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
				return;
			}
			Log.i(TAG, "hashmap中有這個鑰匙值，但是還沒有獲取到圖片，這兒就到下面去！會出現再次訪問相同數據！");
		}
		// 缓存内没有图片，开启线程下载图片
		
		 newThread(imageUrl, handler);
		 System.out.println("i::::" + i);
	
		
		return;
	}

	private void newThread(final String imageUrl, final Handler handler) {
		if(imageUrl != null && !imageUrl.equals("") && !loadingUrls.contains(imageUrl))
		new Thread() {
			@Override
			public void run() {
				Drawable drawable = null;
				if (drawable == null) {
					drawable = loadImageFromUrl(imageUrl);
				}
//				Log.i(TAG, "drawable:" + drawable);
				if(drawable != null) {
					loadingUrls.add(imageUrl);
					imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
					
					loadingUrls.remove(imageUrl);
					Message message = handler.obtainMessage(0, drawable);
					handler.sendMessage(message);
					drawable = null;// 重置引用。
				}else{
					i++;
					if(i <= 3){
						 newThread(imageUrl, handler);
					}
				}
				
				drawable = null;// 重置引用。
				
			}
		}.start();
	}

	/**
	 * 从网络下载图片
	 * 
	 * @param url
	 * @return
	 */
	public static Drawable loadImageFromUrl(String url) {
		Log.i(TAG, "loadImageFromUrl:" + url);
		URL m;
		Drawable d = null;
		try {
			m = new URL(url);
			HttpURLConnection hcc = (HttpURLConnection) m.openConnection();
			hcc.connect();
			if (hcc.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream i = hcc.getInputStream();
				Bitmap dd = BitmapFactory.decodeStream(i);
				BitmapDrawable bd= new BitmapDrawable(dd);
				d = bd;
//				d = Drawable.createFromStream(i, "src");
				i.close();
			}
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e2) {
			e2.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		
		return d;
	}

	/**
	 * 异步处理图片的接口方法
	 * 
	 * @author guokai
	 * 
	 */
	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, String imageUrl);
	}

}
