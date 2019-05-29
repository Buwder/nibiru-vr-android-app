package com.zskx.net.request;

import com.alibaba.fastjson.JSON;
import com.zskx.net.response.ResponseEntity;
import com.zskx.net.response.TestQuestionEntity;
import com.zskx.net.response.TestQuestionVo;
import com.zskx.pemsystem.util.MyPost;
import com.zskx.testreport.SelectQuestionParsedVO;
import com.zskx.testreport.SingleQuestionVO;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetTestQuestionsRequest extends AbstractRequest<TestQuestionVo>
{
  public GetTestQuestionsRequest(AbstractRequest.GetResponseListener<TestQuestionVo> listener, String paramString, long paramLong)
  {
    super("getTestQuestionDetails", listener);
    addProperty("sessionId", paramString);
    addProperty("testItemId", Long.valueOf(paramLong));
  }

  
  
//  
//  private void fillTestQuestionEntity(TestQuestionEntity paramTestQuestionEntity, JSONObject paramJSONObject)
//    throws JSONException
//  {
//    TestQuestionVo localTestQuestionVo = new TestQuestionVo();
//    localTestQuestionVo.setTestQuestionId(paramJSONObject.getJSONObject("content").getInt("testQuestionId"));
//    localTestQuestionVo.setQuestionAmount(paramJSONObject.getJSONObject("content").getInt("questionAmount"));
//    localTestQuestionVo.setTestItemId(paramJSONObject.getJSONObject("content").getInt("testItemId"));
//    SelectQuestionParsedVO localSelectQuestionParsedVO = new SelectQuestionParsedVO();
//    localSelectQuestionParsedVO.setBackgroundMusic(paramJSONObject.getJSONObject("content").getJSONObject("vo").getString("backgroundMusic"));
//    localSelectQuestionParsedVO.setDescription(paramJSONObject.getJSONObject("content").getJSONObject("vo").getString("description"));
//    localSelectQuestionParsedVO.setGuide(paramJSONObject.getJSONObject("content").getJSONObject("vo").getString("guide"));
//    localSelectQuestionParsedVO.setQuestionForFemaleAmount(paramJSONObject.getJSONObject("content").getJSONObject("vo").getInt("questionForFemaleAmount"));
//    localSelectQuestionParsedVO.setQuestionForMaleAmount(paramJSONObject.getJSONObject("content").getJSONObject("vo").getInt("questionForMaleAmount"));
//    localSelectQuestionParsedVO.setQuestionId(paramJSONObject.getJSONObject("content").getJSONObject("vo").getInt("questionId"));
//    localSelectQuestionParsedVO.setTitle(paramJSONObject.getJSONObject("content").getJSONObject("vo").getString("title"));
//    JSONArray localJSONArray = paramJSONObject.getJSONObject("content").getJSONObject("vo").getJSONArray("listSelectQuestion");
//    ArrayList localArrayList = new ArrayList();
//    for (int i = 0; ; ++i)
//    {
//      if (i >= localJSONArray.length())
//        return;
//      JSONObject localJSONObject = (JSONObject)localJSONArray.opt(i);
//      SingleQuestionVO localSingleQuestionVO = new SingleQuestionVO();
//      localSingleQuestionVO.setNumber(localJSONObject.getInt("number"));
//      localSingleQuestionVO.setQuestionImage(localJSONObject.getString("questionImage"));
//      localSingleQuestionVO.setTitle(localJSONObject.getString("title"));
//      new ArrayList();
//      localSingleQuestionVO.setNumber(localJSONObject.getInt("choices"));
//      localArrayList.add(localSingleQuestionVO);
//    }
//  }

  @Override
  public ResponseEntity<TestQuestionVo> filterJsonDataToBean(String paramString)
    throws Exception
  {
    final ResponseEntity<TestQuestionVo> response = new ResponseEntity<TestQuestionVo>();
    if (!super.getDebug())
    {
      TestQuestionEntity localTestQuestionEntity = (TestQuestionEntity)JSON.parseObject(paramString, TestQuestionEntity.class);
      JSONObject json = new JSONObject(paramString);
      response.setCode(json.getString("code"));
      response.setMsg(json.getString("msg"));
      ArrayList<TestQuestionVo> localArrayList = new ArrayList<TestQuestionVo>();
      localArrayList.add(localTestQuestionEntity.getContent());
      response.setContent(localArrayList);
    }
    if ((super.isAccessData()) && (this.listener != null))
    {
		if ("200".equals(response.getCode()) || super.getDebug()) {
			MyPost.post(new Runnable() {

				@Override
				public void run() {
					listener.onSuccess(response);

				}
			});

		} else {
			MyPost.post(new Runnable() {

				@Override
				public void run() {
					listener.onError(response);

				}
			});

		}
	}
	return response;
  }
}

