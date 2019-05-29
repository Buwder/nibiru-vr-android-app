package com.zskx.view;

import java.io.File;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class SoundsModen {
	
	private Map<String,Drawable> bitmapMap;//存放播放按钮背景图片
	private Map<String,String> soundMap;//存放播放音乐文件
	private Map<String,String> nameMap;//存放播放音乐名字
	private Map<String,Integer> volumeMap;//存放播放初始音量
	
	
	
	public SoundsModen(Map<String, Drawable> bitmapMap,
			Map<String, String> soundMap, Map<String, String> nameMap,
			Map<String, Integer> volumeMap) {
		super();
		this.bitmapMap = bitmapMap;
		this.soundMap = soundMap;
		this.nameMap = nameMap;
		this.volumeMap = volumeMap;
	}



	public Map<String, Drawable> getBitmapMap() {
		return bitmapMap;
	}



	public void setBitmapMap(Map<String, Drawable> bitmapMap) {
		this.bitmapMap = bitmapMap;
	}



	public Map<String, String> getSoundMap() {
		return soundMap;
	}



	public void setSoundMap(Map<String, String> soundMap) {
		this.soundMap = soundMap;
	}



	public Map<String, String> getNameMap() {
		return nameMap;
	}



	public void setNameMap(Map<String, String> nameMap) {
		this.nameMap = nameMap;
	}



	public Map<String, Integer> getVolumeMap() {
		return volumeMap;
	}



	public void setVolumeMap(Map<String, Integer> volumeMap) {
		this.volumeMap = volumeMap;
	}



	@Override
	public String toString() {
		return "MusicModen [bitmapMap=" + bitmapMap + ", soundMap=" + soundMap
				+ ", nameMap=" + nameMap + ", volumeMap=" + volumeMap + "]";
	}
	
	
	


}
