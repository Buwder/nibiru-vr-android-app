package com.zskx.net.request;

import com.zskx.net.response.ConsultantEntity;

public class GetConsultantListRequest extends AbstractRequest<ConsultantEntity>{

	  public GetConsultantListRequest(AbstractRequest.GetResponseListener<ConsultantEntity> paramGetResponseListener, String paramString, int paramInt1, int paramInt2)
	  {
	    super("getConsultantList", paramGetResponseListener);
	    addProperty("sessionId", paramString);
	    addProperty("pageIndex", Integer.valueOf(paramInt1));
	    addProperty("pageSize", Integer.valueOf(paramInt2));
	    setDebug(false);
	  }

}
