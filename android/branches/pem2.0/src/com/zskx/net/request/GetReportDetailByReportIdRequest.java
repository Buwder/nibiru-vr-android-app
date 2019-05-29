package com.zskx.net.request;

import com.zskx.net.response.ReportDetailEntity;

public class GetReportDetailByReportIdRequest extends
		AbstractRequest<ReportDetailEntity> {

	public GetReportDetailByReportIdRequest(
			GetResponseListener<ReportDetailEntity> listener, String sessionId,
			String reportId) {
		super("getReportDetailByReportId", listener);
		addProperty("sessionId", sessionId);
		addProperty("reportId", reportId);
		setDebug(false);
	}

}
