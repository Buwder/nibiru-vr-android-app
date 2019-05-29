package com.zskx.pemsystem;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zskx.net.request.AbstractRequest.GetResponseListener;
import com.zskx.net.request.ModifyUserDataRequest;
import com.zskx.net.response.ResponseEntity;
import com.zskx.pemsystem.util.AppConfiguration;
import com.zskx.pemsystem.util.EnumMarriage;
import com.zskx.pemsystem.util.EnumSex;
import com.zskx.pemsystem.util.ScreenManager;

public class ModifyInfoActivity extends MenuActivity {

	private String loginName;

	private TextView textUserID;
	private EditText editUserName;
	private EditText editAge;
	private RadioGroup genderGroup;
	private RadioButton rdBtnMale;
	private RadioButton rdBtnFemale;
	private RadioGroup marriageGroup;
	private RadioButton rdBtnMarriaged;
	private RadioButton rdBtnUnMarriaged;
	private RadioButton rdBtnOtherMarriaged;

	private Button buttonSendInfo;
	private Button backButton;
	private ProgressBar progressBar;

	private String userName;
	private String age;
	private String gender;
	private EnumMarriage marriage;

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

		progressBar = (ProgressBar) findViewById(R.id.load_progressBar);

		textUserID = (TextView) findViewById(R.id.textView_user_id);

		editUserName = (EditText) findViewById(R.id.editText_name);

		editAge = (EditText) findViewById(R.id.editText_age);

		genderGroup = (RadioGroup) findViewById(R.id.radioGroup_gender);

		rdBtnMale = (RadioButton) findViewById(R.id.man);

		rdBtnFemale = (RadioButton) findViewById(R.id.female);

		marriageGroup = (RadioGroup) findViewById(R.id.radioGroup_marriage);

		rdBtnMarriaged = (RadioButton) findViewById(R.id.radioButton_marriaged);

		rdBtnUnMarriaged = (RadioButton) findViewById(R.id.radioButton_not_marriaged);

		rdBtnOtherMarriaged = (RadioButton) findViewById(R.id.radioButton_other_marriaged);

		buttonSendInfo = (Button) findViewById(R.id.btn_send);

		backButton = (Button) findViewById(R.id.btn_back);

	}

	/**
	 * 初始化组件数据和事件
	 */

	private void initDataOrEvent() {

		loginName = AppConfiguration.user.getLoginName();

		genderGroup.setOnCheckedChangeListener(changeListener);
		marriageGroup.setOnCheckedChangeListener(changeListener);
		buttonSendInfo.setOnClickListener(onClickListener);
		backButton.setOnClickListener(onClickListener);

		textUserID.setText(AppConfiguration.user.getLoginName());

		editUserName.setText(AppConfiguration.user.getUserName());
		editAge.setText(AppConfiguration.user.getUserAge());

		if (EnumSex.MALE.getValue().equals(AppConfiguration.user.getUserSex())) {

			rdBtnMale.setChecked(true);
			rdBtnFemale.setChecked(false);

		} else if (EnumSex.FEMALE.getValue().equals(
				AppConfiguration.user.getUserSex())) {

			rdBtnMale.setChecked(false);
			rdBtnFemale.setChecked(true);
		}

		if (EnumMarriage.MARRIED.getValue().equals(
				AppConfiguration.user.getIsMarried())) {

			rdBtnMarriaged.setChecked(true);
			rdBtnUnMarriaged.setChecked(false);
			rdBtnOtherMarriaged.setChecked(false);

		} else if (EnumMarriage.UNMARRIED.getValue().equals(
				AppConfiguration.user.getIsMarried())) {

			rdBtnMarriaged.setChecked(false);
			rdBtnUnMarriaged.setChecked(true);
			rdBtnOtherMarriaged.setChecked(false);

		} else if (EnumMarriage.OTHER.getValue().equals(
				AppConfiguration.user.getIsMarried())) {

			rdBtnMarriaged.setChecked(false);
			rdBtnUnMarriaged.setChecked(false);
			rdBtnOtherMarriaged.setChecked(true);
		}

	}

	/**
	 * button 点击事件
	 */
	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.btn_back:
				ModifyInfoActivity.this.finish();
				break;

			case R.id.btn_send:

				userName = editUserName.getText().toString().trim();
				age = editAge.getText().toString().trim();

				sendInfo();

				break;

			default:
				break;
			}

		}
	};

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
			case R.id.radioGroup_marriage:

				if (checkedId == R.id.radioButton_marriaged) {
					marriage = EnumMarriage.MARRIED;

				} else if (checkedId == R.id.radioButton_not_marriaged) {
					marriage = EnumMarriage.UNMARRIED;

				} else if (checkedId == R.id.radioButton_other_marriaged) {
					marriage = EnumMarriage.OTHER;

				}

				// Toast.makeText(ModifyInfoActivity.this, marriage+"",
				// Toast.LENGTH_LONG).show();

				break;

			default:
				break;
			}

		}
	};

	// 提接结果处理
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			buttonSendInfo.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			if (msg.arg1 == ACCESS_NET_OK) {

				AppConfiguration.user.setUserAge(age);
				AppConfiguration.user.setUserName(userName);
				AppConfiguration.user.setUserSex(gender);
				AppConfiguration.user.setIsMarried(marriage.getValue());
				Toast.makeText(ModifyInfoActivity.this, (String) msg.obj,
						Toast.LENGTH_LONG).show();
				// Toast.makeText(ModifyInfoActivity.this,
				// R.string.access_network_failed, Toast.LENGTH_LONG).show();

			} else if (msg.arg1 == ACCESS_NET_FAILED) {

				Toast.makeText(ModifyInfoActivity.this,
						R.string.access_network_failed, Toast.LENGTH_LONG)
						.show();

			}

		};
	};

	private static final int ACCESS_NET_OK = 1;// 访问网络成功
	private static final int ACCESS_NET_FAILED = -1;// 访问网络失败
	// 访问监听器
	private GetResponseListener<String> listener = new GetResponseListener<String>() {

		@Override
		public void onSuccess(ResponseEntity<String> result) {

			Message msg = new Message();
			msg.arg1 = ACCESS_NET_OK;
			msg.obj = result.getMsg();
			handler.sendMessage(msg);
		}

		@Override
		public void onError(ResponseEntity<String> result) {

			Message msg = new Message();
			msg.arg1 = ACCESS_NET_FAILED;
			handler.sendMessage(msg);
		}
	};

	private ModifyUserDataRequest request;

	/**
	 * 发送修改后的资料
	 */

	private void sendInfo() {

		buttonSendInfo.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		request = new ModifyUserDataRequest(listener, loginName, userName,
				gender, age, marriage + "");
		request.sendByThread();

	}

	@Override
	protected void onStop() {

		if (request != null) {
			request.cancel();
		}
		super.onStop();
	}
}
