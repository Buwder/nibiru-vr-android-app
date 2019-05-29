package com.zskx.pemsystem;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.LayoutParams;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.zskx.net.request.AbstractRequest.GetResponseListener;
import com.zskx.net.request.GetTestQuestionsRequest;
import com.zskx.net.request.SendTestQuestionRequest;
import com.zskx.net.response.ResponseEntity;
import com.zskx.net.response.TestQuestionAnswer;
import com.zskx.net.response.TestQuestionVo;
import com.zskx.net.response.TestTypeEntity;
import com.zskx.pemsystem.util.AppConfiguration;
import com.zskx.pemsystem.util.Common_Title;
import com.zskx.pemsystem.util.MyPost;
import com.zskx.pemsystem.util.ScreenManager;
import com.zskx.pemsystem.util.ShowNotification;
import com.zskx.pemsystem.util.UtilService;
import com.zskx.testreport.ChoiceVO;
import com.zskx.testreport.SelectQuestionParsedVO;
import com.zskx.testreport.SingleQuestionVO;

public class PsychoTestDoActivity extends MenuActivity
{

private static int RADIO_BUTTON_ID = 789654123;
  private static int RESULTCODE = 489416532;
  private static String TAG = "PsychoTestDoActivity";
  private ArrayList<String> answers;
  
  private Button btn_end_next; //答下一个量表
  private Button btn_next; //下一个问题
  private Button btn_prev; //上一个问题
  private Button btn_report; //查看报告
  private Button btn_resend; //重新发送
  private Button btn_start; //开始答题
  
  private Common_Title common_Title; //自定义标题
  private View start_lyt; //开始答题界面
  private View test_lyt; //答题界面
  private View end_lyt; //结束答题界面
  private View fail_lyt; //连接失败
  
  private GetTestQuestionsRequest getTestQuestionsRequest;
  private Handler handler = new Handler();
  
  private int type_pos; //量表的位置
  private long typeId; //itemId
  private long next_typeId; //下一个问题的itemId
  private int pos = 0; //问题在量表中的位置
  private ProgressBar progressBar;
  private String question; //答案的字符串
  private TestQuestionAnswer questionAnswer;
  private String questionTitle; //具体问题
  private List<SingleQuestionVO> questions; //量表
  private int questions_nums; //量表问题总数
  private RadioGroup radioGroup; //单选题的radioGroup
  private SendTestQuestionRequest sendTestQuestionRequest;
  private View server_progressBar; //连接服务器
  private boolean isResponsed = false;
  
  private TestQuestionVo testQuestionVo;
  
  private TextView textView_end_text; // 下一个问题
  private TextView textView_fail; //连接失败的text
  private TextView textView_guide; //开始答题界面的导语
  private TextView textView_intro; //开始答题界面的介绍
  private TextView textView_subtitle; //子表题
  private TextView textView_subtitle_guide;
  private TextView textView_subtitle_intro;
  private TextView textView_test_question; //显示问题的textView
  
  private ArrayList<TestTypeEntity> typeList;
 
  private TextView view_progress;
  private SelectQuestionParsedVO vo;
  private Intent intent;

  @Override
  protected void onCreate(Bundle bundle)
  {
    super.onCreate(bundle);
    ScreenManager.getScreenManager().pushActivity(this);
    setContentView(R.layout.test_detail);
   
    init();
    
    intent = getIntent();
    typeList = ((ArrayList)intent.getSerializableExtra(PsychoTestActivity.LIST));
    type_pos = intent.getIntExtra(PsychoTestActivity.TYPE_POS, 0);
    
    if (typeList == null || (this.type_pos >= this.typeList.size()) || (this.type_pos < 0)){
      Log.i(TAG, "typeList::" + this.typeList.size() + "   type_pos::" + this.type_pos);
      show_fail(getResources().getString(R.string.test_no_data));
    } else{
      typeId = Long.parseLong(((TestTypeEntity)typeList.get(type_pos)).getTestItemId());
      getServerResponse(typeId);
    }
    
  }
  
  @Override
	protected void onResume() {
	  ShowNotification.showNotification(this, "PEM", getResources().getString(R.string.title_test_report), PsychoTestDoActivity.class, intent.setClass(this, PsychoTestDoActivity.class));
		super.onResume();
	}

  
  
  @Override
  protected void onDestroy()
  {
    if (this.questions != null)
      this.questions = null;
    if (this.testQuestionVo != null)
      this.testQuestionVo = null;
    if (this.vo != null)
      this.vo = null;
    if (this.getTestQuestionsRequest != null)
      this.getTestQuestionsRequest.cancel();
    if (this.sendTestQuestionRequest != null)
      this.sendTestQuestionRequest.cancel();
    super.onDestroy();
  }
  
  
  
  
  private void afterResponse()
  {
    this.vo = this.testQuestionVo.getVo();
    this.textView_subtitle.setText(this.vo.getTitle());
    this.textView_subtitle_intro.setText(R.string.test_start_intro);
    this.textView_intro.setText(this.vo.getDescription());
    this.textView_subtitle_guide.setText(R.string.test_start_guide);
    this.textView_guide.setText(this.vo.getGuide());
    this.start_lyt.setVisibility(View.VISIBLE);
  }

  private void do_test()
  {
    this.start_lyt.setVisibility(View.GONE);
    this.test_lyt.setVisibility(View.VISIBLE);
    common_Title.setTitleTxt(getResources().getString(R.string.test_title_test));
    single_test();
  }

  private void single_test()
  {
    this.pos = 0;
    this.questions = this.vo.getListSelectQuestion();
    this.questions_nums = this.questions.size();
    this.answers = new ArrayList<String>();
    show_question();
  }
  /**
   * 刷新问题
   * @param pos
   */
  private void show_question()
  {
    if (pos <= 0) pos = 0;
    if (pos >= questions_nums) pos = questions_nums - 1;
    int pos2 = pos + 1;
    Log.i(TAG, "show_question:: pos:" + pos + "  pos2:" + pos2);
    this.question = ((SingleQuestionVO)this.questions.get(pos)).getTitle();
    this.textView_test_question.setText(this.question);
    this.progressBar.setMax(this.questions_nums);
    this.progressBar.setProgress(pos2);
    this.view_progress.setText(pos2 + "/" + this.questions_nums);
    show_choices();
  }
  /**
   * 刷新答案
   * @param pos
   */
  private void show_choices( )
  {
    radioGroup.removeAllViews();
  
    final List<ChoiceVO> choices = questions.get(pos).getChoices();
    
    for (int i = 0; i < choices.size(); i++) {
    	String value = choices.get(i).getValue();
        RadioButton localRadioButton = create_radio_buttons(this, value, i ,choices);
        if ((pos < this.answers.size()) && (((String)this.answers.get(pos)).equals(value)))
          localRadioButton.setChecked(true);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 5, 10, 5);
        radioGroup.addView(localRadioButton, lp);
	}
    
//    radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//		
//		@Override
//		public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//            System.out.println("pos::" + pos);
//            if (pos < answers.size()){
//            	//如果是前面已经选过得答案，重新选择
//            	answers.set(pos, choices.get(checkedId - RADIO_BUTTON_ID).getValue());
//            }else {
//            	//如果是新答案加进字符串
//            	answers.add(pos, choices.get(checkedId - RADIO_BUTTON_ID).getValue());
//            }
//             
//            Log.i(PsychoTestDoActivity.TAG, "answers::" + answers.get(pos) + "/" + answers.size());
//            next_question();
//		}
//	});
 
  }
  /**
   * 显示结束界面
   */
  private void end_test()
  {
    test_lyt.setVisibility(View.GONE);
    end_lyt.setVisibility(View.VISIBLE);
    common_Title.setTitleTxt(getResources().getString(R.string.test_title_end));
    if ((questionTitle != null) && (!questionTitle.equals("")))
      textView_end_text.setText(questionTitle);
    else{
      textView_end_text.setText(R.string.test_end_no_next);
    }
  }

  private void forResult()
  {
  
    if (this.type_pos > 0){
    	  Intent localIntent = new Intent();
	      localIntent.putExtra("itemId", ((TestTypeEntity)this.typeList.get(type_pos -1)).getTestItemId());
	      setResult(RESULTCODE, localIntent);
    }
  }

  private void getServerResponse(long itemId)
  {
	  Log.i(TAG, "getServerResponse!!");
	  
	isResponsed = false;
    questionAnswer = new TestQuestionAnswer();
    btn_resend.setVisibility(View.GONE);
    getTestQuestionsRequest = new GetTestQuestionsRequest(new GetResponseListener<TestQuestionVo>() {

		@Override
		public void onSuccess(ResponseEntity<TestQuestionVo> result) {

	        testQuestionVo = ((TestQuestionVo) result.getContent().get(0));
	        isResponsed = true;
	        MyPost.post(new Runnable()
	        {
	          public void run()
	          {
	            server_progressBar.setVisibility(View.GONE);
	            if (!getQestionEntity(testQuestionVo))
	              afterResponse();
	          }
	        });
	      
		}

		@Override
		public void onError(final ResponseEntity<TestQuestionVo> result) {
			MyPost.post(new Runnable()
	        {
	          public void run()
	          {
	            server_progressBar.setVisibility(View.GONE);
//	            UtilService.show(PsychoTestDoActivity.this, result.getMsg());
	            show_fail(result.getMsg());
	          }
	        });
	        isResponsed = true;
		}
	}, AppConfiguration.getUser(this).getSessionId(), itemId);
    
   
    getTestQuestionsRequest.sendByThread();
    if (!isResponsed)  server_progressBar.setVisibility(View.VISIBLE);
  
    initViews();
  }

  private void get_next_scale()
  {
    int pos =  type_pos + 1;
    Log.i(TAG, "get_next_scale  type_pos:" + type_pos + "   typeList.size():" + typeList.size());
    if (pos < typeList.size())
    {
      questionTitle = ((TestTypeEntity)typeList.get(pos)).getTestQuestionTitle();
      next_typeId = Long.parseLong(((TestTypeEntity)typeList.get(pos)).getTestItemId());
      type_pos =  type_pos++;
    }else{
      questionTitle = "";
      next_typeId = 0L;
//    btn_end_next.setClickable(false);
    }
  }

  private void init()
  {
    this.common_Title = ((Common_Title)findViewById(R.id.test_detail_title));
    this.start_lyt = findViewById(R.id.test_start_lyt);
    this.start_lyt.setVisibility(View.GONE);
    this.test_lyt = findViewById(R.id.test_test_lyt);
    this.test_lyt.setVisibility(View.GONE);
    this.end_lyt = findViewById(R.id.test_end_lyt);
    this.end_lyt.setVisibility(View.GONE);
    this.fail_lyt = findViewById(R.id.test_failure);
    this.fail_lyt.setVisibility(View.GONE);
    this.server_progressBar = findViewById(R.id.loading_progress);
    this.common_Title.setTitleTxt(getResources().getString(R.string.test_start_guide));
    this.textView_fail = ((TextView)findViewById(R.id.text_failure));
    this.btn_resend = ((Button)findViewById(R.id.test_resend));
    this.btn_resend.setOnClickListener(this.btn_click_listener);

  }
  /**
   * 连接网络的同时初始化其他控件
   */
  private void initViews()
  {
    this.textView_subtitle = ((TextView)findViewById(R.id.test_subtital));
    this.textView_subtitle_intro = ((TextView)findViewById(R.id.test_subtitle_intro));
    this.textView_intro = ((TextView)findViewById(R.id.test_intro));
    this.textView_subtitle_guide = ((TextView)findViewById(R.id.test_subtitle_guide));
    this.textView_guide = ((TextView)findViewById(R.id.test_guide));
    this.textView_end_text = ((TextView)findViewById(R.id.test_end_next));
    this.textView_test_question = ((TextView)findViewById(R.id.test_single_question));
    this.progressBar = ((ProgressBar)findViewById(R.id.test_progress));
    this.view_progress = ((TextView)findViewById(R.id.test_progress_text));
    this.radioGroup = ((RadioGroup)findViewById(R.id.test_radioGroup));
    this.radioGroup.removeAllViews();
    this.btn_start = ((Button)findViewById(R.id.test_start_btn));
    this.btn_prev = ((Button)findViewById(R.id.test_single_previous));
    this.btn_next = ((Button)findViewById(R.id.test_single_next));
    this.btn_end_next = ((Button)findViewById(R.id.test_end_btn_next));
    this.btn_report = ((Button)findViewById(R.id.test_end_btn_stop));
    this.btn_start.setOnClickListener(this.btn_click_listener);
    this.btn_prev.setOnClickListener(this.btn_click_listener);
    this.btn_next.setOnClickListener(this.btn_click_listener);
    this.btn_end_next.setOnClickListener(this.btn_click_listener);
    this.btn_report.setOnClickListener(this.btn_click_listener);
  }
  
  /**
   * 发送答案
   */
  private void send_answer_request()
  {
//	end_test();  
	Log.i(TAG, "send_answer_request!!");
    this.isResponsed = false;
    sendTestQuestionRequest = new SendTestQuestionRequest(new GetResponseListener<String>() {

		@Override
		public void onSuccess(ResponseEntity<String> result) {
			isResponsed = true;
			MyPost.post(new Runnable()
	        {
	          public void run()
	          {
	            server_progressBar.setVisibility(View.GONE);
	            end_test();
	          }
	        });
	      
		}

		@Override
		public void onError(ResponseEntity<String> result) {
			isResponsed = true;
			MyPost.post(new Runnable()
	        {
	          public void run()
	          {
	            server_progressBar.setVisibility(View.GONE);
	            show_fail(getResources().getString(R.string.test_send_fail));
	            btn_resend.setVisibility(View.VISIBLE);
	          }
	        });
		}
	}, AppConfiguration.getUser(this).getSessionId(), questionAnswer.getTestAnswer(), questionAnswer.getTestItemId(), questionAnswer.getTestQuestionId(), null);
    
    sendTestQuestionRequest.sendByThread();
    test_lyt.setVisibility(View.GONE);
    fail_lyt.setVisibility(View.GONE);
    if (!isResponsed) server_progressBar.setVisibility(View.VISIBLE);
  }

  private void send_answers()
  {
    String answer_string = "1%%" + (String)this.answers.get(0);
    for (int i = 1; i < answers.size(); i++) {
    	int j = i + 1;
        answer_string = answer_string + "@@" + j + "%%" + (String)this.answers.get(i);
	}
    Log.i(TAG, "send_answers::" + answer_string);
    questionAnswer.setTestAnswer(answer_string);
    send_answer_request();
    get_next_scale();

  }
  /**
   * 显示失败界面
   * @param id
   */
  private void show_fail(String str) {
    String str1 = getResources().getString(R.string.test_failure);
    textView_fail.setText(String.format(str1, str));
    fail_lyt.setVisibility(View.VISIBLE);
  }

  public RadioButton create_radio_buttons(Context context, String choice, int id , final List<ChoiceVO> choices)
  {
    RadioButton radioButton = new RadioButton(context);
    radioButton.setId(RADIO_BUTTON_ID + id);
    radioButton.setText(choice +  "   " + choices.get(id).getTitle());
    radioButton.setTextColor(context.getResources().getColor(R.color.DimGray));
    radioButton.setButtonDrawable(R.drawable.radio_button_selector);
    radioButton.setBackgroundResource(R.drawable.radio_button_selector_back);
    radioButton.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			System.out.println("pos::" + pos);
            if (pos < answers.size()){
            	//如果是前面已经选过得答案，重新选择
            	answers.set(pos, choices.get(v.getId() - RADIO_BUTTON_ID).getValue());
            }else {
            	//如果是新答案加进字符串
            	answers.add(pos, choices.get(v.getId()- RADIO_BUTTON_ID).getValue());
            }
             
            Log.i(PsychoTestDoActivity.TAG, "answers::" + answers.get(pos) + "/" + answers.size());
            next_question();
		}
	});
    return radioButton;
  }
  /**
   * 检查是否存在量表，如果有创建答案对象
   * @param testQuestionVo
   * @return
   */
  protected boolean getQestionEntity(TestQuestionVo testQuestionVo)
  {
    if ((testQuestionVo == null) || (testQuestionVo.equals("")) || (testQuestionVo.getVo() == null))
    {
      Log.i(TAG, "量表为空");
      show_fail(getResources().getString(R.string.test_no_data));
      return true;
    }
    
      questionAnswer.setTestQuestionId((int)testQuestionVo.getVo().getQuestionId());
      questionAnswer.setTestItemId(testQuestionVo.getTestItemId());
    return false;
  }
  /**
   * 显示下一个问题，延迟150毫秒
   */
  protected void next_question()
  {
	  Log.i(TAG, "next_question");
     pos++;
    if (pos >= questions_nums)
      send_answers();
    else{
      handler.postDelayed(new Runnable() {
		
		@Override
		public void run() {
			 show_question();
		}
	}, 150);
    }
  }
  
  private View.OnClickListener btn_click_listener = new View.OnClickListener()
  {	
    public void onClick(View v)
    {
      switch (v.getId())
      {
      case R.id.test_start_btn:
    	  do_test();
    	  break;
      case R.id.test_single_previous:
          pos--;
          show_question();
          
    	  break;
      case R.id.test_single_next:
    	  if (pos < answers.size()) next_question();
    	  else UtilService.show(PsychoTestDoActivity.this, getResources().getString(R.string.test_test_checkone));
    	  break;
      case R.id.test_end_btn_next:
    	  if( next_typeId == 0)  {
    		  UtilService.show(PsychoTestDoActivity.this, "已经是最后一题了!");
    	  }else{
    		  init();
              getServerResponse(next_typeId);
    	  }
    	 
    	  break;
      case R.id.test_end_btn_stop:
    	  finish();
    	  break;
    	  
      case R.id.test_resend:
    	  send_answer_request();
          btn_resend.setVisibility(View.GONE);
    	  break;
      };
     }
  };
 
}
