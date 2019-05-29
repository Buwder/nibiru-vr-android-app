package com.zskx.net.request;

import com.zskx.net.response.MusicDetailEntity;

public class GetAllMusicByTypeRequest extends
		AbstractRequest<MusicDetailEntity> {

	public GetAllMusicByTypeRequest(
			GetResponseListener<MusicDetailEntity> listener, String sessionId,
			String musicTypeId, int pageIndex, int pageSize) {
		super("getAllMusicByType", listener);
		addProperty("sessionId", sessionId);
		addProperty("musicTypeId", musicTypeId);
		addProperty("pageIndex", pageIndex);
		addProperty("pageSize", pageSize);
		setDebug(false);
	}

}
