package com.zskx.net.request;

public class SendTestQuestionRequest extends AbstractRequest<String>
{
  public SendTestQuestionRequest(AbstractRequest.GetResponseListener<String> paramGetResponseListener, String paramString1, String paramString2, long paramLong1, long paramLong2, String paramString3)
  {
    super("saveAnswer", paramGetResponseListener);
    addProperty("sessionId", paramString1);
    addProperty("testAnswer", paramString2);
    addProperty("testItemId", Long.valueOf(paramLong1));
    addProperty("testQuestionId", Long.valueOf(paramLong2));
    addProperty("educationalLevel", paramString3);
  }
}

/* Location:           /home/guokai/share/apktool-install-linux-r04-brut1/com.zskx.pemsystem-1/classes_dex2jar.jar
 * Qualified Name:     com.zskx.net.request.SendTestQuestionRequest
 * JD-Core Version:    0.5.4
 */