package com.zskx.pemsystem.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

public class SDCardUtil {

	public static String PEMSys = "/Pem/image/cache/";
	
	static {
		if(hasCanWriteSDCard()){
			PEMSys = Environment.getExternalStorageDirectory().getAbsolutePath()+PEMSys;
			File dir = new File(PEMSys);
			if(!dir.exists()){
				 dir.mkdirs();
			}
		}
	}
	
	public static boolean cacheBitmap(Bitmap bitmap,String fileName){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		File file = new File(PEMSys+fileName);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(file !=null){
			try {
				FileOutputStream fos = new FileOutputStream(file);
				bitmap.compress(CompressFormat.PNG, 100, baos);
				fos.write(baos.toByteArray());
				fos.flush();
				fos.close();
				return true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		Log.d("SDCardUtil", PEMSys+fileName);
		return false;
	}
	
	public static boolean clearSDCardImageCache(){
		if(hasCanWriteSDCard()){
			File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Pem");
			if(dir.exists()){
				return deleteFolder(dir);
			}
			
		}
		return false;
	}
	
	public static boolean deleteFile(File file){
		if(file!=null && file.isFile()){
			return file.delete();
		}
		return false;
	}
	
	public static boolean deleteFolder(File file){
		if(file!=null && file.isDirectory()){
			for (File subfile : file.listFiles()) {
				if(!deleteFile(subfile)){
					deleteFolder(subfile);
				}
			}
			return true;
		}
		return false;
	}
	
	public static boolean hasCanWriteSDCard(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	private static boolean getPemSys(){
		if(hasCanWriteSDCard()){
			File dir = new File(PEMSys);
			if(!dir.exists()){
				 dir.mkdirs();
			}
			dir = null;
			return true;
		}
		return false;
	}
	
	/**
	 * 缓存 图片到sd卡上。
	 * @param fileName
	 * @param image
	 * @return
	 */
	public static boolean cacheImage(String fileName,Drawable image){
		if(!getPemSys()||fileName==null||"".equals(fileName)){
			return false;
		}
		File imageF = new File(PEMSys+fileName);
		
		try {
			if (!imageF.exists()) {
				imageF.createNewFile();
			}else {
				Log.d("SDCardUtil", "this fileName:"+fileName +"is exit,no need to write to sdcard.");
				return true;
			}
			FileOutputStream fos = new FileOutputStream(imageF);
			fos.write(DrawableToByte(image));
			fos.flush();
			fos.close();
			return true;
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.d("SDCardUtil", PEMSys+fileName);
		return false;
	}
	
	public static byte[] DrawableToByte(Drawable drawable){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		((BitmapDrawable)drawable).getBitmap().compress(CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}
	
	/**
	 * 如果有SD卡上到缓存，从SD卡上得到图片
	 * @param fileName
	 * @return
	 */
	public static Drawable getImage(String fileName){
		if(!getPemSys()||fileName==null||"".equals(fileName)){
			return null;
		}
		Drawable image = null;
		File imageF = new File(PEMSys+fileName);
		try {
			FileInputStream fis = new FileInputStream(imageF);
			Bitmap b = BitmapFactory.decodeStream(fis);
			image = new BitmapDrawable(b);
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.e("SDCardUtil", "no this file in sdcard:"+fileName);
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return image;
	}
}
