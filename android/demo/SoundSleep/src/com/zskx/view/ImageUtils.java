package com.zskx.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ImageUtils {
	
	public static Drawable scaleImage(Context c,String path)
	{
		Bitmap bitmap=BitmapFactory.decodeFile(path);
		
		Bitmap scaleBitmap=Bitmap.createScaledBitmap(bitmap, 30, 30, false);
		
		Drawable drawable=new BitmapDrawable(c.getResources(),scaleBitmap);
		return drawable;
	}
	

}
