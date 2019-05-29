package com.zskx.pemsystem.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.zskx.pemsystem.threadpool.Task;
import com.zskx.pemsystem.threadpool.ThreadPoolManager;

public class ImageLoader {

	public static String TAG = "ImageLoader";
	private static ConcurrentHashMap<String, ConcurrentHashMap<String, SoftReference<Drawable>>> allImageCache = new ConcurrentHashMap<String, ConcurrentHashMap<String,SoftReference<Drawable>>>();
	private  ConcurrentHashMap<String, SoftReference<Drawable>> imageCache;
	private List<String> loadingUrls = new ArrayList<String>();

	public ImageLoader(String activityName) {
		if(allImageCache.containsKey(activityName)){
			imageCache =  allImageCache.get(activityName);
		}else {
			imageCache = new ConcurrentHashMap<String, SoftReference<Drawable>>();
			allImageCache.put(activityName, imageCache);
		}
	}

	

	
	/**
	 * 加载图片并处理
	 * 
	 * @param imageUrl
	 * @param imageCallback
	 */
	@SuppressLint("HandlerLeak")
	public void loadDrawable(String activityName,final String imageUrl,
			final ImageCallback imageCallback) {

		// 异步处理图片
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
			}
		};
		// 如果缓存内含有图片,直接返回缓存内图片
		if (imageCache.containsKey(imageUrl)) {
			Drawable drawable = imageCache.get(imageUrl).get();
			if (drawable != null) {
				Log.i(TAG, "the cache has the image:" + imageUrl + ":drawable="
						+ drawable);
				Message m = handler.obtainMessage();
				m.obj = drawable;
				handler.sendMessage(m);
				return;
			} else {
				Log.i(TAG, "the cache has the image,but it is null;" + imageUrl);
			}
		}
		// 再硬盘中找是否有该文件
		Drawable d = SDCardUtil.getImage(changeUrlToMd5(imageUrl));
		if (d != null) {
			Log.d(TAG, "the sdcard has the image:" + imageUrl + d);
			imageCache.put(imageUrl, new SoftReference<Drawable>(d));
			Message m = handler.obtainMessage();
			m.obj = d;
			handler.sendMessage(m);
			return;
		} else {
			Log.i(TAG, "the sdcard hasn't the image:" + imageUrl);
		}

		// 缓存内没有图片，开启线程下载图片

		if (!loadingUrls.contains(imageUrl)) {
			loadingUrls.add(imageUrl);
			getDrawableByThread(imageUrl, imageCallback, handler);
		} else {
			Log.i(TAG, "the url is downloading ,please wait;" + imageUrl);
		}
		return;
	}

	private void getDrawableByThread(final String imageUrl,
			final ImageCallback imageCallback, final Handler handler) {
		System.out.println("getDrawableByThread:" + imageUrl);

		ThreadPoolManager.getInstance().addTask(new Task(imageUrl) {

			@Override
			public void run() {

				Drawable newDrwable = null;
				int downloadCount = 0;
				while (newDrwable == null && downloadCount < 3) {
					newDrwable = loadImageFromUrl(imageUrl);
					downloadCount++;
					Log.d(TAG, "imageUrl:" + imageUrl
							+ "download result is null,go on " + downloadCount
							+ "time download!");
				}
				Log.d(TAG, "imageUrl:" + imageUrl + "download result is "
						+ newDrwable);
				synchronized (TAG) {
					if(newDrwable != null){
					imageCache.put(imageUrl, new SoftReference<Drawable>(
							newDrwable));
					pushImageToSDCard(newDrwable, imageUrl);
					}
					loadingUrls.remove(imageUrl);
					Message m = handler.obtainMessage();
					m.obj = newDrwable;
					handler.sendMessage(m);
				}

			}
		});

	}

	/**
	 * 从网络下载图片
	 * 
	 * @param url
	 * @return
	 */
	public  Drawable loadImageFromUrl(String url) {

		if (url == null || "".equals(url)) {
			Log.i(TAG, "url is null or empty!,return null");
			return null;
		}
		Log.i(TAG, "loadImageFromUrl:" + url);
		URL m;
		Drawable d = null;
		try {
			m = new URL(url);
			HttpURLConnection hcc = (HttpURLConnection) m.openConnection();
			hcc.setConnectTimeout(30000);
			hcc.connect();
			if (hcc.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream i = hcc.getInputStream();
				d = Drawable.createFromStream(i, url);
				i.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {

			e.printStackTrace();
//			clearCache();
		}

		return d;
	}

//	public static synchronized void clearAllCache(){
//		Iterator<String> allImageCache.keySet().iterator();
//	}
	
	public static synchronized void clearCache(String activityName) {
		if(allImageCache.containsKey(activityName)){
			ConcurrentHashMap<String, SoftReference<Drawable>> cache = allImageCache.get(activityName);
			Iterator<String> iterator = cache.keySet().iterator();
			while (iterator.hasNext()) {
				String s = iterator.next();
				Drawable d = cache.get(s).get();
				if (d != null) {
					d.setCallback(null);
					Bitmap b = ((BitmapDrawable)d).getBitmap();
					if(!b.isRecycled()){
						b.recycle();
					}
					b = null;
				}
				cache.get(s).clear();
				d = null;
			}
			cache.clear();
			allImageCache.remove(activityName);
			Log.d(TAG, "this is cache called:"+activityName+",cleared!");
			System.gc();
			Runtime.getRuntime().gc();
		}else {
			Log.d(TAG, "this is no cache called:"+activityName);
		}
		
	}

	public static  void pushImageToSDCard(final Drawable drawable,final String url) {
		ThreadPoolManager.getInstance().addTask(new Task("imageToSDcard:"+url) {
			
			@Override
			public void run() {
						if (drawable!= null) {
							SDCardUtil.cacheImage(changeUrlToMd5(url), drawable);
						}
			}
		});
		
	}

	public static String changeUrlToMd5(String url) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] md5 = md.digest(url.getBytes("UTF-8"));
			StringBuffer md5StrBuff = new StringBuffer();

			for (int i = 0; i < md5.length; i++) {
				if (Integer.toHexString(0xFF & md5[i]).length() == 1)
					md5StrBuff.append("0").append(
							Integer.toHexString(0xFF & md5[i]));
				else
					md5StrBuff.append(Integer.toHexString(0xFF & md5[i]));
			}
			return md5StrBuff.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
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
