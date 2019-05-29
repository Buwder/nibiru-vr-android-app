package com.zskx.net.request;

import com.zskx.net.response.HospitalVO;

public class GetHospitalRequest extends AbstractRequest<HospitalVO> {

	public GetHospitalRequest(AbstractRequest.GetResponseListener<HospitalVO> listener,String paramString, int paramInt1, int paramInt2) {
		super("getHospitalList", listener);
		addProperty("sessionId", paramString);
	    addProperty("pageIndex", Integer.valueOf(paramInt1));
	    addProperty("pageSize", Integer.valueOf(paramInt2));
	    setDebug(false);
		
	}

		

}
