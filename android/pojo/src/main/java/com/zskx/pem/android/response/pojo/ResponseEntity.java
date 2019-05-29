package com.zskx.pem.android.response.pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 返回数据的模型
 * 
 * @author demo
 * 
 * @param <T>
 */
public class ResponseEntity<T> implements Serializable {

	/** 200表示成功,999表示其他错误 */
	private String code;

	/** 提供一段描述信息以供客户端提示使用（如“成功”，“密码错误”等） */
	private String msg;
	/** 返回的记录条数 */
	private int count;
	/** 数据库所有的条数 */
	private int totalCount;
	/** 返回的数据，为对象列表 */
	private ArrayList<? extends T> content = new ArrayList<T>();

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public ArrayList<? extends T> getContent() {
		return content;
	}

	public void setContent(ArrayList<? extends T> content) {
		this.content = content;
	}

}
