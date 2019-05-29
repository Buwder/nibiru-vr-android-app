package com.zskx.pemsystem;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zskx.net.NetConfiguration;
import com.zskx.net.request.AbstractRequest.GetResponseListener;
import com.zskx.net.request.ChangeUserInformationRequest;
import com.zskx.net.response.ResponseEntity;
import com.zskx.net.response.user.User;
import com.zskx.pemsystem.util.AppConfiguration;
import com.zskx.pemsystem.util.EnumIncome;
import com.zskx.pemsystem.util.EnumSex;
import com.zskx.pemsystem.util.ScreenManager;
import com.zskx.pemsystem.util.ShowNotification;

public class ModifyInfoActivity extends MenuActivity {

	private String loginName;

	private TextView textUserID;
	private EditText editUserName;
	private EditText editAge;
	private Button spannerSalary;
	
	private RadioGroup genderGroup;
	private RadioButton rdBtnMale;
	private RadioButton rdBtnFemale;
//	private RadioGroup marriageGroup;
//	private RadioButton rdBtnMarriaged;
//	private RadioButton rdBtnUnMarriaged;
//	private RadioButton rdBtnOtherMarriaged;

	private Button buttonConfirm;
	private Button backButton;
	private Button homeButton;
	private Button cancelButton;
	//private ProgressBar progressBar;

	private String userName;
	private String age;
	private String gender;
	//private String marriage;
	
	
	//薪资水平
	private String salaryLevel[]={EnumIncome.BELOW_TEN_HUN.getTitle(),EnumIncome.TEN_TO_TWENTY_HUN.getTitle(),EnumIncome.TWENTY_TO_THIRTY_HUN.getTitle(),EnumIncome.THIRTY_TO_FIFTY_HUN.getTitle(),EnumIncome.ABOVE_FIFTY_HUN.getTitle()};
    private EnumIncome salary=EnumIncome.BELOW_TEN_HUN;
	private int index=0;
	
	
	private AlertDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ScreenManager.getScreenManager().pushActivity(this);
		setContentView(R.layout.modify_info);
		initComponent();
		initDataOrEvent(); // 暂时注销
	}

	/**
	 * 初始化组件
	 */
	private void initComponent() {

	//	progressBar = (ProgressBar) findViewById(R.id.load_progressBar);
		 
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		 
		 View view=LayoutInflater.from(this).inflate(R.layout.progress_dialog, null);
		 
		 builder.setView(view);
		 builder.setCancelable(false);
		 dialog= builder.create();
		 
		 Window window=dialog.getWindow();
		 WindowManager.LayoutParams p=window.getAttributes();
	     int width= p.width*3/4;
	     p.width=width;
	     window.setLayout(width, p.height);
		 
	//	 dialog.show();
		 
		 

		textUserID = (TextView) findViewById(R.id.textView_user_id);

		editUserName = (EditText) findViewById(R.id.editText_name);

		editAge = (EditText) findViewById(R.id.editText_age);
		
		spannerSalary=(Button) findViewById(R.id.editText_salary_level);
		

		genderGroup = (RadioGroup) findViewById(R.id.radioGroup_gender);

		rdBtnMale = (RadioButton) findViewById(R.id.man);

		rdBtnFemale = (RadioButton) findViewById(R.id.female);

//		marriageGroup = (RadioGroup) findViewById(R.id.radioGroup_marriage);
//
//		rdBtnMarriaged = (RadioButton) findViewById(R.id.radioButton_marriaged);
//
//		rdBtnUnMarriaged = (RadioButton) findViewById(R.id.radioButton_not_marriaged);
//
//		rdBtnOtherMarriaged = (RadioButton) findViewById(R.id.radioButton_other_marriaged);

		buttonConfirm = (Button) findViewById(R.id.btn_confirm);
		cancelButton = (Button) findViewById(R.id.btn_cancel);

		homeButton=(Button)findViewById(R.id.home_btn);
		backButton = (Button) findViewById(R.id.btn_back);

	}

	/**
	 * 初始化组件数据和事件
	 */

	private void initDataOrEvent() {

		User user=AppConfiguration.getUser(this);
		
		String incomeLevel=user.getMoneyLevel();
		
		
		if(EnumIncome.BELOW_TEN_HUN.getValue().equals(incomeLevel))
		{
			salary=EnumIncome.BELOW_TEN_HUN;
			index=0;
			
		}else if(EnumIncome.TEN_TO_TWENTY_HUN.getValue().equals(incomeLevel))
		{
			salary=EnumIncome.TEN_TO_TWENTY_HUN;
			index=1;
			
		}else if(EnumIncome.TWENTY_TO_THIRTY_HUN.getValue().equals(incomeLevel))
		{
			
			salary=EnumIncome.TWENTY_TO_THIRTY_HUN;
			index=2;
			
		}else if(EnumIncome.THIRTY_TO_FIFTY_HUN.getValue().equals(incomeLevel))
		{
			salary=EnumIncome.THIRTY_TO_FIFTY_HUN;
			index=3;
			
			
		}else if(EnumIncome.ABOVE_FIFTY_HUN.getValue().equals(incomeLevel))
		{
			salary=EnumIncome.ABOVE_FIFTY_HUN;
			index=4;
			
		}
		
		
		loginName = user.getLoginName();

		genderGroup.setOnCheckedChangeListener(changeListener);
		//marriageGroup.setOnCheckedChangeListener(changeListener);
		buttonConfirm.setOnClickListener(onClickListener);
		cancelButton.setOnClickListener(onClickListener);
		homeButton.setOnClickListener(onClickListener);
		backButton.setOnClickListener(onClickListener);

		textUserID.setText(user.getLoginName());

		editUserName.setText(user.getUserName());
		editAge.setText(user.getUserAge());
		spannerSalary.setText(salary.getTitle());
		spannerSalary.setOnClickListener(onClickListener);

	
		if (EnumSex.MALE.getValue().equals(user.getUserSex())) {

			rdBtnMale.setChecked(true);
			rdBtnFemale.setChecked(false);
			gender=EnumSex.MALE.getValue();
			

		} else if (EnumSex.FEMALE.getValue().equals(
				user.getUserSex())) {

			rdBtnMale.setChecked(false);
			rdBtnFemale.setChecked(true);
			gender=EnumSex.FEMALE.getValue();
		}

//		if (EnumMarriage.MARRIED.getValue().equals(
//				AppConfiguration.user.getIsMarried())) {
//
//			rdBtnMarriaged.setChecked(true);
//			rdBtnUnMarriaged.setChecked(false);
//			rdBtnOtherMarriaged.setChecked(false);
//			marriage=EnumMarriage.MARRIED.getValue();
//
//		} else if (EnumMarriage.UNMARRIED.getValue().equals(
//				AppConfiguration.user.getIsMarried())) {
//
//			rdBtnMarriaged.setChecked(false);
//			rdBtnUnMarriaged.setChecked(true);
//			rdBtnOtherMarriaged.setChecked(false);
//			marriage=EnumMarriage.UNMARRIED.getValue();
//
//		} else if (EnumMarriage.OTHER.getValue().equals(
//				AppConfiguration.user.getIsMarried())) {
//
//			rdBtnMarriaged.setChecked(false);
//			rdBtnUnMarriaged.setChecked(false);
//			rdBtnOtherMarriaged.setChecked(true);
//			marriage=EnumMarriage.OTHER.getValue();
//		}

	}

	/**
	 * button 点击事件
	 */
	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.btn_back:
			case R.id.btn_cancel:
				ModifyInfoActivity.this.finish();
				break;

			case R.id.btn_confirm:

				userName = editUserName.getText().toString().trim();
				age = editAge.getText().toString().trim();
                dialog.show();
				sendInfo();

				break;
			case R.id.home_btn:
				
				Intent intent=new Intent(ModifyInfoActivity.this,HomeActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			case R.id.editText_salary_level:
				showSalary();
				break;
				
			default:
				break;
			}

		}
	};
	
	
	
	
	private void showSalary()
	{
		final AlertDialog.Builder builder=new AlertDialog.Builder(this);
		
		builder.setTitle(R.string.salary_title);
		
		
		builder.setSingleChoiceItems(salaryLevel, index, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(salaryLevel[which].equals(EnumIncome.BELOW_TEN_HUN.getTitle()))
					{
						salary=EnumIncome.BELOW_TEN_HUN;
						
					}else if(salaryLevel[which].equals(EnumIncome.TEN_TO_TWENTY_HUN.getTitle()))
					{
						salary=EnumIncome.TEN_TO_TWENTY_HUN;
					}
					else if(salaryLevel[which].equals(EnumIncome.TWENTY_TO_THIRTY_HUN.getTitle()))
					{
						salary=EnumIncome.TWENTY_TO_THIRTY_HUN;
					}else if(salaryLevel[which].equals(EnumIncome.THIRTY_TO_FIFTY_HUN.getTitle()))
					{
						salary=EnumIncome.THIRTY_TO_FIFTY_HUN;
					}else if(salaryLevel[which].equals(EnumIncome.ABOVE_FIFTY_HUN.getTitle()))
					{
						salary=EnumIncome.ABOVE_FIFTY_HUN;
					}
					
				    index=which;
					spannerSalary.setText(salary.getTitle());
					dialog.dismiss();
				
			}
		});

		builder.create().show();
	}
	
	
	
	

	// RadioGroup 切换项目事件
	private RadioGroup.OnCheckedChangeListener changeListener = new RadioGroup.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {

			switch (group.getId()) {
			case R.id.radioGroup_gender:

				if (checkedId == R.id.man) {
					gender = EnumSex.MALE.getValue();

				} else if (checkedId == R.id.female) {
					gender = EnumSex.FEMALE.getValue();
				}

				// Toast.makeText(ModifyInfoActivity.this, gender,
				// Toast.LENGTH_LONG).show();

				break;
//			case R.id.radioGroup_marriage:
//
//				if (checkedId == R.id.radioButton_marriaged) {
//					marriage = EnumMarriage.MARRIED.getValue();
//
//				} else if (checkedId == R.id.radioButton_not_marriaged) {
//					marriage = EnumMarriage.UNMARRIED.getValue();;
//
//				} else if (checkedId == R.id.radioButton_other_marriaged) {
//					marriage = EnumMarriage.OTHER.getValue();;
//
//				}
//
//				// Toast.makeText(ModifyInfoActivity.this, marriage+"",
//				// Toast.LENGTH_LONG).show();
//
//				break;

			default:
				break;
			}

		}
	};

	// 提接结果处理
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			dialog.dismiss();
	
			if (msg.arg1 == ACCESS_NET_OK) {

				
				Toast.makeText(ModifyInfoActivity.this, (String) msg.obj,
						Toast.LENGTH_LONG).show();
				
				ModifyInfoActivity.this.finish();

			} else if (msg.arg1 == ACCESS_NET_FAILED) {

				Toast.makeText(ModifyInfoActivity.this,
						(String) msg.obj, Toast.LENGTH_LONG)
						.show();
				

			}

		};
	};

	private static final int ACCESS_NET_OK = 1;// 访问网络成功
	private static final int ACCESS_NET_FAILED = -1;// 访问网络失败
	// 访问监听器
	private GetResponseListener<User> listener = new GetResponseListener<User>() {

		@Override
		public void onSuccess(ResponseEntity<User> result) {

			Message msg = new Message();
			User user=result.getContent().get(0);
			AppConfiguration.saveUser(ModifyInfoActivity.this, user);
			msg.arg1 = ACCESS_NET_OK;
			msg.obj = result.getMsg();
			handler.sendMessage(msg);
		}

		@Override
		public void onError(ResponseEntity<User> result) {

			Message msg = new Message();
			msg.arg1 = ACCESS_NET_FAILED;
			msg.obj = result.getMsg();
			handler.sendMessage(msg);
		}
	};

	private ChangeUserInformationRequest request;

	
	/**
	 * 发送修改后的资料
	 */
	private void sendInfo() {

		User user=AppConfiguration.getUser(this);
	
		if(user !=null)
		{
			
		
		request = new ChangeUserInformationRequest(listener, user.getSessionId(), userName, gender, age, salary.getValue(), NetConfiguration.CLIENT_OS, "android_phone");
		
		request.sendByThread();
		
		}

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		String contentStr=getResources().getString(R.string.edit_info);
		ShowNotification.showNotification(this,"PEM",contentStr, ModifyInfoActivity.class,null);
	}
	

	@Override
	protected void onStop() {

		if (request != null) {
			request.cancel();
		}
		super.onStop();
	}
}
