package com.zskx.pemsystem;

import java.util.List;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zskx.net.request.AbstractRequest.GetResponseListener;
import com.zskx.net.request.GetReportDetailByReportIdRequest;
import com.zskx.net.response.ReportDetailEntity;
import com.zskx.pemsystem.customview.ImageAsynLoaddown;
import com.zskx.pemsystem.util.AppConfiguration;
import com.zskx.pemsystem.util.ScreenManager;

public class ReportDetailsActivity extends MenuActivity {

	private Button backButton; // 返回按钮
	private Button refreshButton; // 刷新按钮

	private String reportId; // 报表ID
	private ReportDetailEntity reportDetailEntity; // 根据报表得到的实体

	private LinearLayout reportContent;// 报告内容容器
	private LinearLayout reportEmpty;// 数据为空，或访问错误提示
	private TextView textInfoTip;// 信息提示

	private ProgressBar loadProgressBar;// 进度加载条

	private TextView reportTitle;// 报告标题

	private TextView name; // 姓名
	private TextView gender; // 性别
	private TextView age; // 年龄
	private TextView company; // 单位

	private TextView reultLeft;
	private TextView resultRight;

	private LinearLayout imageContent;// 测评结果图

	private TextView textComment;// 评语

	private TextView textSuggestion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ScreenManager.getScreenManager().pushActivity(this);
		setContentView(R.layout.report_detail);

		Intent intent = getIntent();
		reportId = intent.getStringExtra(ReportListActivity.REPORTITEM_ID);

		initComponent();
		// textSuggestion.setText(null);
		getReportDetail();

	}

	/**
	 * 初始化组件
	 */
	private void initComponent() {
		backButton = (Button) findViewById(R.id.btn_back);
		backButton.setOnClickListener(clickListener);

		reportContent = (LinearLayout) findViewById(R.id.linearLayout_content);
		reportEmpty = (LinearLayout) findViewById(R.id.linear_empty);

		textInfoTip = (TextView) findViewById(R.id.textView_access_tip_info);

		refreshButton = (Button) findViewById(R.id.refresh_button);
		refreshButton.setOnClickListener(clickListener);

		loadProgressBar = (ProgressBar) findViewById(R.id.load_progressBar);

		reportTitle = (TextView) findViewById(R.id.report_title);

		name = (TextView) findViewById(R.id.textView_name);
		gender = (TextView) findViewById(R.id.textView_gender);
		age = (TextView) findViewById(R.id.textView_age);
		company = (TextView) findViewById(R.id.textView_company);

		reultLeft = (TextView) findViewById(R.id.textView_result_left);
		resultRight = (TextView) findViewById(R.id.textView_result_right);

		imageContent = (LinearLayout) findViewById(R.id.linearLayout_imageContent);
		textComment = (TextView) findViewById(R.id.textComment);
		textSuggestion = (TextView) findViewById(R.id.text_Suggestion);

	}

	// button 点击事件
	private View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_back:
				ReportDetailsActivity.this.finish();
				break;

			case R.id.refresh_button:

				getReportDetail();
				break;

			default:
				break;
			}

		}
	};

	/**
	 * 访问结束后对 UI 进行操作
	 */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			if (msg.arg1 == ACCESS_NET_OK) {
				if (msg.arg2 > 0) {
					reportContent.setVisibility(View.VISIBLE);
					reportEmpty.setVisibility(View.GONE);
					setReportDetails();
				} else {
					reportContent.setVisibility(View.GONE);
					reportEmpty.setVisibility(View.VISIBLE);
					textInfoTip.setText(R.string.zearo_count);
					refreshButton.setVisibility(View.GONE);

				}
			} else if (msg.arg1 == ACCESS_NET_FAILED) {
				reportContent.setVisibility(View.GONE);
				reportEmpty.setVisibility(View.VISIBLE);
				textInfoTip.setText(R.string.access_network_failed);
				Toast.makeText(ReportDetailsActivity.this,
						(String)msg.obj, Toast.LENGTH_LONG)
						.show();
			}

			loadProgressBar.setVisibility(View.GONE);
		};

	};

	private static final int ACCESS_NET_OK = 1;// 访问网络成功
	private static final int ACCESS_NET_FAILED = -1;// 访问网络失败
	// 访问监听器
	private GetResponseListener<ReportDetailEntity> listener = new GetResponseListener<ReportDetailEntity>() {
		public void onSuccess(
				com.zskx.net.response.ResponseEntity<ReportDetailEntity> result) {

			Message msg = new Message();

			if (result.getContent().size() > 0) {
				reportDetailEntity = result.getContent().get(0);
				msg.arg1 = ACCESS_NET_OK;
				msg.arg2 = result.getContent().size();

			} else {
				msg.arg1 = ACCESS_NET_OK;
				msg.arg2 = result.getContent().size();
			}

			handler.sendMessage(msg);

		};

		public void onError(
				com.zskx.net.response.ResponseEntity<ReportDetailEntity> result) {
			Message msg = new Message();
			msg.arg1 = ACCESS_NET_FAILED;
			msg.obj=result.getMsg();
			handler.sendMessage(msg);

		};
	};

	private GetReportDetailByReportIdRequest request;

	/**
	 * 访问数据
	 */
	private void getReportDetail() {
		reportEmpty.setVisibility(View.GONE);
		reportContent.setVisibility(View.GONE);

		loadProgressBar.setVisibility(View.VISIBLE);

		request = new GetReportDetailByReportIdRequest(listener,
				AppConfiguration.user.getSessionId(), reportId);

		request = new GetReportDetailByReportIdRequest(listener,
				AppConfiguration.user.getSessionId(), reportId);

		request.sendByThread();
	}

	@Override
	protected void onStop() {

		if (request != null) {
			request.cancel();
		}
		super.onStop();
	}

	/**
	 * 设置数据
	 */
	private void setReportDetails() {

		reportTitle.setText(reportDetailEntity.getReportTitle()); // 设置标题

		// 设置基本资料
		Resources res = getResources();

		name.setText(String.format(res.getString(R.string.report_detail_name),
				AppConfiguration.user.getUserName()));
		gender.setText(String.format(
				res.getString(R.string.report_detail_gender),
				AppConfiguration.user.getUserSex()));
		age.setText(String.format(res.getString(R.string.report_detail_age),
				AppConfiguration.user.getUserAge()));
		company.setText(String.format(
				res.getString(R.string.report_detail_company),
				AppConfiguration.user.getUserCompany()));

		// 设置测评结果
		List<String> resultList = reportDetailEntity.getReportResultText();
//		StringBuffer sbLeft = new StringBuffer();
//		StringBuffer sbRight = new StringBuffer();
		String leftStr="";
		String rightStr="";
		int flag=0;
		for (int i = 0; i < resultList.size(); i++) {
			
			
			
			if (flag == 0) {
				flag=1;
				leftStr=leftStr+resultList.get(i) + "\n";
				System.out.println("flag=0:--->"+leftStr);
			} else if(flag== 1){
				flag=0;
				rightStr=rightStr+resultList.get(i) + "\n";
				System.out.println("flag=1:--->"+rightStr);
			}
		}

		
		reultLeft.setText(leftStr.toString());
		resultRight.setText(rightStr.toString());

		// 设置图片
		List<String> imageList = reportDetailEntity.getReportResultImage();

		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER_HORIZONTAL;
		params.topMargin = 20;

		for (String url : imageList) {
			ImageView imageView = new ImageView(this);
			ImageAsynLoaddown isdd = new ImageAsynLoaddown(imageView);
			isdd.execute(url);
			imageContent.addView(imageView, params);

		}

		// 设置评语
		textComment.setText(reportDetailEntity.getReportComment());

		// 设置建议
		textSuggestion.setText(reportDetailEntity.getReportSuggestion());

	}

}
