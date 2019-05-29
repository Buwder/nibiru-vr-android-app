package com.zskx.pemsystem.customview;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

/**
 * 异步下载图片
 * 

 * 
 */
public final class ImageAsynLoaddown extends AsyncTask<String, Void, Bitmap> {


	private WeakReference<ImageView> imageViewWeakReference;

	public ImageAsynLoaddown(ImageView imageView) {
		imageViewWeakReference = new WeakReference<ImageView>(imageView);
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		Bitmap bitmap = null;

		try {
			URL url = new URL(params[0]);

		
			HttpURLConnection httpConn = (HttpURLConnection) url
					.openConnection();

			httpConn.connect();
			if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream is = httpConn.getInputStream();
				bitmap = BitmapFactory.decodeStream(is);
				is.close();
			}

		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		return bitmap;
	}

	@Override
	protected void onPostExecute(Bitmap result) {

		if (isCancelled()) {
			result = null;
		}

		if (imageViewWeakReference != null && result != null) {
			final ImageView imageView = imageViewWeakReference.get();
			if (imageView != null) {
				imageView.setImageBitmap(result);
			}
		}
	}
}
