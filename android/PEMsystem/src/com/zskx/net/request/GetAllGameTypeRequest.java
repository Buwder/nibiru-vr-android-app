package com.zskx.net.request;

import com.zskx.net.response.GameTypeEntity;

public class GetAllGameTypeRequest extends AbstractRequest<GameTypeEntity> {

	public GetAllGameTypeRequest(GetResponseListener<GameTypeEntity> listener,
			String sessionId, int pageIndex, int pageSize) {
		super("getAllGameType", listener);

		addProperty("pageIndex", pageIndex);
		addProperty("pageSize", pageSize);
		addProperty("sessionId", sessionId);
		setDebug(false);
	}

}
