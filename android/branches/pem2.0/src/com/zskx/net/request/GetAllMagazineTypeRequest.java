package com.zskx.net.request;

import com.zskx.net.response.MagazineTypeEntity;

public class GetAllMagazineTypeRequest extends
		AbstractRequest<MagazineTypeEntity> {

	public GetAllMagazineTypeRequest(
			GetResponseListener<MagazineTypeEntity> listener, String sessionId,
			int pageIndex, int pageSize) {
		super("getAllMagazineType", listener);

		addProperty("pageIndex", pageIndex);
		addProperty("pageSize", pageSize);
		addProperty("sessionId", sessionId); 
		setDebug(false);
	}

}
