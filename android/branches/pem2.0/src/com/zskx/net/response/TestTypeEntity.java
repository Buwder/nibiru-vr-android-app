package com.zskx.net.response;

import java.io.Serializable;

import com.zskx.net.NetConfiguration;

public class TestTypeEntity
  implements Serializable
{
  private static final long serialVersionUID = 6065117343450358617L;
  private String testCoverImage;
  private String testItemId;
  private String testQuestionDescription;
  private String testQuestionId;
  private String testQuestionTitle;
  private String testSignatureImage;
  private String testStatus;
  private String visible;

  public String getTestCoverImage()
  {
	  if (testCoverImage.startsWith(NetConfiguration.DOWNLOAD_ADD)
			|| "".equals(testCoverImage)) {
		return testCoverImage;
	} else {
		return NetConfiguration.DOWNLOAD_ADD + testCoverImage;
	}
  }

  public String getTestItemId()
  {
    return this.testItemId;
  }

  public String getTestQuestionDescription()
  {
    return this.testQuestionDescription;
  }

  public String getTestQuestionId()
  {
    return this.testQuestionId;
  }

  public String getTestQuestionTitle()
  {
    return this.testQuestionTitle;
  }

  public String getTestSignatureImage()
  {
    return this.testSignatureImage;
  }

  public String getTestStatus()
  {
    return this.testStatus;
  }

  public String getVisible()
  {
    return this.visible;
  }

  public void setTestCoverImage(String paramString)
  {
    this.testCoverImage = paramString;
  }

  public void setTestItemId(String paramString)
  {
    this.testItemId = paramString;
  }

  public void setTestQuestionDescription(String paramString)
  {
    this.testQuestionDescription = paramString;
  }

  public void setTestQuestionId(String paramString)
  {
    this.testQuestionId = paramString;
  }

  public void setTestQuestionTitle(String paramString)
  {
    this.testQuestionTitle = paramString;
  }

  public void setTestSignatureImage(String paramString)
  {
    this.testSignatureImage = paramString;
  }

  public void setTestStatus(String paramString)
  {
    this.testStatus = paramString;
  }

  public void setVisible(String paramString)
  {
    this.visible = paramString;
  }
}

/* Location:           /home/guokai/share/apktool-install-linux-r04-brut1/com.zskx.pemsystem-1/classes_dex2jar.jar
 * Qualified Name:     com.zskx.net.response.TestTypeEntity
 * JD-Core Version:    0.5.4
 */