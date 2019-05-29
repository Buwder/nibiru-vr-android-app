package com.zskx.sleep;


/**
 * 单个的音频
 * @author demo
 *
 */
/**
 * @author demo
 *
 */
public class SingleMusicBean {

	/**
	 * 音频图片
	 */
	private String image;
	/**
	 * 音频名字
	 */
	private String name;
	/**
	 * 音频音量
	 */
	private int volumn;
	/**
	 * 音频文件
	 */
	private String music;
	
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getMusic() {
		return music;
	}
	public void setMusic(String music) {
		this.music = music;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getVolumn() {
		return volumn;
	}
	public void setVolumn(int volumn) {
		this.volumn = volumn;
	}
	
	@Override
	public String toString() {
		return this.name+"_"+this.image+"_"+this.music+"_"+this.volumn;
	}
	
}
