package com.zskx.sleep;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

public class XmlConfigLoader {

	/**
	 * 从XML文件流中得到配置信息
	 * @param is xml流
	 * @return 复合音频列表
	 * @throws Exception
	 */
	public static List<MultiMusicBean> getMultiMusicList(InputStream is){
		
		List<MultiMusicBean> list  = null;
		
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(is, "utf-8");
		
			
		MultiMusicBean mmb = null;
		SingleMusicBean smb = null;
		int type = parser.getEventType();
		while (type != XmlPullParser.END_DOCUMENT) {
			switch (type) {
			case XmlPullParser.START_DOCUMENT:
				list  = new ArrayList<MultiMusicBean>();
				break;
			case XmlPullParser.START_TAG:
				if("catolog".equals(parser.getName())){
					mmb = new MultiMusicBean();
					mmb.setName(parser.getAttributeValue(0));
					mmb.setImage(parser.getAttributeValue(1));
					mmb.setImageBackGround(parser.getAttributeValue(2));
					mmb.setBackGroundMusic(parser.getAttributeValue(3));
				}
				if("file".equals(parser.getName())){
					smb = new SingleMusicBean();
					smb.setImage(mmb.getImage());
					smb.setName(parser.getAttributeValue(1));
					smb.setMusic(parser.getAttributeValue(2));
					smb.setVolumn(Integer.valueOf(parser.getAttributeValue(3)));
				}
				break;
				
			case XmlPullParser.END_TAG:
				if("file".equals(parser.getName())){
					mmb.getSingleMusic().add(smb);
				}
				if("catolog".equals(parser.getName())){
					list.add(mmb);
				}
				break;
			default:
				break;
			}
			type = parser.next();
		}
		
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	private static String path=null;
	/**
	 * 得到SD路径
	 * @param context
	 * @return
	 */
	public static String getSdcardPath(Context context){
		if(path==null){
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				path = Environment.getExternalStorageDirectory().getPath();
			}else {
				MyToast.show(context,  "没有检测到SD卡");
			}
		}
		Log.i("sdcard", "sd卡的路径为："+path);
		return path;
	}
	/**
	 * 通过文件路径得到图片，如果不是图片文件或没有该文件返回null
	 * @param path 文件路径
	 * @param context 
	 * @return
	 */
	public static Drawable getDrawableByPath(String path,Context context){
    	Drawable drawable = null;
    	Bitmap bd = BitmapFactory.decodeFile(getSdcardPath(context)+path);
    	if(bd!=null){
    		drawable = new BitmapDrawable(context.getResources(), bd);
    	}else {
    		Log.i("XmlConfigLoader", "么有找到该图片！"+path);
		}
    	return drawable;
    }
	/**
	 * 得到音频文件，如果没有该文件则返回null
	 * @param path
	 * @param context
	 * @return
	 */
	public static File getMusicByPath(String path,Context context){
		File file = null;
		file = new File(getSdcardPath(context)+path);
		if(!file.exists()){
			Log.i("XmlConfigLoader", "么有找到该文件！"+path);
			file = null;
		}
		return file;
	}
}
