package com.zskx.net.request;

import com.zskx.net.response.TestTypeEntity;

public class GetAllTestTypeRequest extends AbstractRequest<TestTypeEntity>
{
  public GetAllTestTypeRequest(AbstractRequest.GetResponseListener<TestTypeEntity> paramGetResponseListener, String paramString, int paramInt1, int paramInt2)
  {
    super("getAllTestReportList", paramGetResponseListener);
    addProperty("sessionId", paramString);
    addProperty("pageIndex", Integer.valueOf(paramInt1));
    addProperty("pageSize", Integer.valueOf(paramInt2));
    setDebug(false);
  }
}

/* Location:           /home/guokai/share/apktool-install-linux-r04-brut1/com.zskx.pemsystem-1/classes_dex2jar.jar
 * Qualified Name:     com.zskx.net.request.GetAllTestTypeRequest
 * JD-Core Version:    0.5.4
 */