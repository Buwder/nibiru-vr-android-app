package com.zskx.net.request;

import com.zskx.net.response.ReportEntity;

public class GetAllReportByUserIdRequest extends AbstractRequest<ReportEntity> {

	public GetAllReportByUserIdRequest(
			GetResponseListener<ReportEntity> listener, String sessionId,
			int pageIndex, int pageSize) {
		super("GetAllReportByUserId", listener);
		addProperty("sessionId", sessionId);
		addProperty("pageIndex", pageIndex);
		addProperty("pageSize", pageSize);
		setDebug(false);
	}

}
