package com.zskx.net.response;

import java.io.Serializable;

public class TestQuestionEntity
  implements Serializable
{
  private static final long serialVersionUID = -5006882912528980783L;
  private String code;
  private TestQuestionVo content;
  private String msg;

  public String getCode()
  {
    return this.code;
  }

  public TestQuestionVo getContent()
  {
    return this.content;
  }

  public String getMsg()
  {
    return this.msg;
  }

  public void setCode(String paramString)
  {
    this.code = paramString;
  }

  public void setContent(TestQuestionVo paramTestQuestionVo)
  {
    this.content = paramTestQuestionVo;
  }

  public void setMsg(String paramString)
  {
    this.msg = paramString;
  }

  public String toString()
  {
    return "TestQuestionEntity [questionVo=" + this.content.toString() + ", code=" + this.code + ", msg=" + this.msg + "]";
  }
}

/* Location:           /home/guokai/share/apktool-install-linux-r04-brut1/com.zskx.pemsystem-1/classes_dex2jar.jar
 * Qualified Name:     com.zskx.net.response.TestQuestionEntity
 * JD-Core Version:    0.5.4
 */