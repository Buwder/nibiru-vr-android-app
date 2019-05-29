package com.zskx.net.request;

import com.zskx.net.response.GameDetailEntity;

public class GetAllGameByTypeRequest extends AbstractRequest<GameDetailEntity> {

	public GetAllGameByTypeRequest(
			GetResponseListener<GameDetailEntity> listener, String sessionId,
			String gameTypeId, int pageIndex, int pageSize) {
		super("getAllGameByType", listener);

		addProperty("gameTypeId", gameTypeId);
		addProperty("pageIndex", pageIndex);
		addProperty("pageSize", pageSize);
		addProperty("sessionId", sessionId);
		setDebug(false);
	}

}
