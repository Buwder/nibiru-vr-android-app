package com.zskx.sleep;

import java.util.ArrayList;
import java.util.List;
/**
 * 复合音频模型
 * @author demo
 *
 */
public class MultiMusicBean {

	/**
	 * 名称
	 */
	private String name;
	/**
	 * 图片
	 */
	private String image;
	/**
	 * 对应音乐的背景图片
	 */
	private String imageBackGround;
	/**
	 * 包含的单个音频列表
	 */
	private List<SingleMusicBean> singleMusic = new ArrayList<SingleMusicBean>();
	/**
	 * 背景音乐
	 */
	private String backGroundMusic;
	
	
	public String getBackGroundMusic() {
		return backGroundMusic;
	}
	public void setBackGroundMusic(String backGroundMusic) {
		this.backGroundMusic = backGroundMusic;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getImageBackGround() {
		return imageBackGround;
	}
	public void setImageBackGround(String imageBackGround) {
		this.imageBackGround = imageBackGround;
	}
	public List<SingleMusicBean> getSingleMusic() {
		return singleMusic;
	}
	public void setSingleMusic(List<SingleMusicBean> singleMusic) {
		this.singleMusic = singleMusic;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("_sub:\n");
		for (int i = 0; i < singleMusic.size(); i++) {
			sb.append(singleMusic.get(i).toString()+"\n");
		}
		return this.name+"_"+this.image+"_"+this.imageBackGround+"_"+this.backGroundMusic+sb.toString();
	}
	
}
