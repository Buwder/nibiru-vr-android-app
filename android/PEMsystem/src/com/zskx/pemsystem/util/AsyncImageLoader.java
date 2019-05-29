package com.zskx.pemsystem.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class AsyncImageLoader {
	
	private String TAG = "AsyncImageLoader";
	
	private HashMap<String, SoftReference<Drawable>> imageCache;

	Drawable drawable ;
	
	public AsyncImageLoader() {
		imageCache = new HashMap<String, SoftReference<Drawable>>();
	}
	
	/**
	 * 加载图片并处理
	 * @param imageUrl
	 * @param imageCallback
	 */
	public void loadDrawable(final String imageUrl, final ImageCallback imageCallback) {
		
		// 异步处理图片
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				if(message.obj != null) imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
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
		}
		// 缓存内没有图片，开启线程下载图片
		new Thread() {
			@Override
			public void run() {
				if (drawable == null)  drawable = loadImageFromUrl(imageUrl);//图像文件需要释放资源，特别是bitmap
				Log.i(TAG, imageUrl);
				imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
				drawable = null;
			}
		}.start();
		return;
	}

	/**
	 * 从网络下载图片
	 * 
	 * @param url
	 * @return
	 */
	public static Drawable loadImageFromUrl(String url) {
		URL m;
		Drawable d = null;
		try {
			m = new URL(url);
			HttpURLConnection hcc = (HttpURLConnection) m.openConnection();
			hcc.connect();
			if (hcc.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream i = hcc.getInputStream();
				d = Drawable.createFromStream(i, "src");
				i.close();
			}
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e2) {
			e2.printStackTrace();
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
