package com.zskx.net.request;

import com.zskx.net.response.VideoTypeEntity;

public class GetAllVideoTypeRequest extends AbstractRequest<VideoTypeEntity> {

	/**
	 * 得到指定视频系列的所有类型
	 * 
	 * @param listener
	 * @param videoSeries
	 *            目前有两个视频系列“心理”和“减压”两个系列。规定：0为心理，1为减压；
	 * @param pageIndex
	 * @param pageSize
	 */
	public GetAllVideoTypeRequest(
			GetResponseListener<VideoTypeEntity> listener, String sessionId,
			String videoSeries, int pageIndex, int pageSize) {
		super("getAllVideoType", listener);
		addProperty("sessionId", sessionId);
		addProperty("videoSeries", videoSeries);
		addProperty("pageIndex", pageIndex);
		addProperty("pageSize", pageSize);
		setDebug(false);
	}

}
