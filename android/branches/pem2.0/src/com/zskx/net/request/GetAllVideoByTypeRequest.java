package com.zskx.net.request;

import com.zskx.net.response.VideoDetailEntity;

public class GetAllVideoByTypeRequest extends
		AbstractRequest<VideoDetailEntity> {

	public GetAllVideoByTypeRequest(
			GetResponseListener<VideoDetailEntity> listener, String sessionId,
			String videoTypeId, int pageIndex, int pageSize) {
		super("getAllVideoByType", listener);
		addProperty("sessionId", sessionId);
		addProperty("videoTypeId", videoTypeId);
		addProperty("pageIndex", pageIndex);
		addProperty("pageSize", pageSize);
		setDebug(false);
	}

}
