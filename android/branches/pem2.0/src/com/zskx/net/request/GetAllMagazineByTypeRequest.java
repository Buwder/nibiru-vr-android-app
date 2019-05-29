package com.zskx.net.request;

import com.zskx.net.response.MagazineDetailEntity;

public class GetAllMagazineByTypeRequest extends
		AbstractRequest<MagazineDetailEntity> {

	public GetAllMagazineByTypeRequest(
			GetResponseListener<MagazineDetailEntity> listener,
			String sessionId, String magazineTypeId, int pageIndex, int pageSize) {
		super("getAllMagazineByType", listener);
		addProperty("magazineTypeId", magazineTypeId);
		addProperty("pageIndex", pageIndex);
		addProperty("pageSize", pageSize);
		addProperty("sessionId", sessionId);
		setDebug(false);
	}

}
