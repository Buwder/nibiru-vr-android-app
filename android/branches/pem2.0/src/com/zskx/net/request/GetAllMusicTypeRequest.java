package com.zskx.net.request;

import com.zskx.net.response.MusicTypeEntity;

public class GetAllMusicTypeRequest extends AbstractRequest<MusicTypeEntity> {

	public GetAllMusicTypeRequest(
			GetResponseListener<MusicTypeEntity> listener, String sessionId,
			int pageIndex, int pageSize) {
		super("getAllMusicType", listener);
		addProperty("sessionId", sessionId);
		addProperty("pageIndex", pageIndex);
		addProperty("pageSize", pageSize);
		setDebug(false);
	}

}
