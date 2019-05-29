package com.zskx.pemsystem;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.zskx.net.response.HospitalVO;
import com.zskx.pemsystem.util.Common_Title;

public class ChannelHospitalDetails extends Activity {
	private TextView name;
	private TextView department;
	private LinearLayout phone;
	private LinearLayout doctor;
	private TextView address;
	private Common_Title title;
	
	private HospitalVO hospital ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.channel_hospital_details);
		
		hospital =  (HospitalVO) getIntent().getSerializableExtra(ChannelListActivity.DETAILS);
		
		initViews();
		
		setListners();
	}

	private void initViews() {
		name = (TextView) findViewById(R.id.channal_details_name);
		department =  (TextView) findViewById(R.id.channel_details_department);
		phone =  (LinearLayout) findViewById(R.id.channel_details_phone);
		doctor =  (LinearLayout) findViewById(R.id.channel_details_doctor);
		address = (TextView) findViewById(R.id.channel_details_address);
		title = (Common_Title) findViewById(R.channel.title_bar);
		title.setTitleTxt(getResources().getString(R.string.channel_title_detail));
		
		name.setText(hospital.getName());
		department.setText(department.getText() + hospital.getDepartment());
		address.setText(address.getText() + hospital.getAddress());
		
		addPhones();
		
		addDoctors();
	}
	
	int phone_id = 16516;
	private void addPhones() {
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.setMargins(15, 0, 0, 0);
		String s = hospital.getPhoneNumber();
		String[] numbers = null;
		if(s.contains("/")){
			numbers = s.split("/");
		}
		if(s.contains(",")){
			numbers = s.split(",");
		}
		
		if(numbers != null ) {
			for (int i = 0; i < numbers.length; i++) {
				TextView v = new TextView(this);
				final String ss = numbers[i].trim();
				final String num = numbers[i].trim();
				if(ss.contains("-")){
					ss.replace("-", "");
				}
				if(ss.contains("－")){
					ss.replace("－", "");
				}
					
				v.setText(num + ",");
//				v.setId(phone_id + i);
				v.setTextColor(getResources().getColor(R.color.skyblue));
				v.setTextSize(17);
				
				v.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						 Intent intent=new Intent("android.intent.action.CALL", Uri.parse("tel:"+ ss));
						 startActivity(intent);
					}
				});
				phone.addView(v, lp);
			}
		}else{
			TextView v = new TextView(this);
			v.setText(s);
			v.setTextColor(getResources().getColor(R.color.skyblue));
			v.setTextSize(17);
			
			final String num = s.trim();
			v.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					 Intent intent=new Intent("android.intent.action.CALL", Uri.parse("tel:"+ num));
					 startActivity(intent);
				}
			});
			
			phone.addView(v, lp);
		}
	}

	int doctor_id = 181532;
	private void addDoctors() {
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.setMargins(15, 0, 0, 0);
		String s = hospital.getDoctor().trim(); //取消空格
		String[] numbers = null;
		
		if(s.contains(" ")){
			numbers = s.split(" ");
		}
		if(s.contains("，")){
			numbers = s.split("，");
		}
		if(s.contains("、")){
			numbers = s.split("、");
		}
		if(s.contains(",")){
			numbers = s.split(",");
		}
		
		if(numbers != null ) {
			for (int i = 0; i < numbers.length; i++) {
				TextView v = new TextView(this);
				v.setText(numbers[i].trim() + ",");
//				v.setId(doctor_id + i);
				v.setTextColor(getResources().getColor(R.color.skyblue));
				v.setTextSize(17);
				final String phone = numbers[i].trim();
				v.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
//						 Intent intent=new Intent("android.intent.action.CALL", Uri.parse("tel:"+phone));
//						 startActivity(intent);
					}
				});
				doctor.addView(v, lp);
			}
		}else{
			TextView v = new TextView(this);
			v.setText(s);
			v.setTextColor(getResources().getColor(R.color.skyblue));
			v.setTextSize(17);
			doctor.addView(v, lp);
		}
	}

	private void setListners() {
		address.setOnClickListener(clickListener);
	}
	
	View.OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.channel_details_address:
				Intent intent = new Intent(ChannelHospitalDetails.this, ChannelMAPActivity.class);
				intent.putExtra(ChannelListActivity.DETAILS, hospital);
				startActivity(intent);
				break;
			}
		}
	};
	
}
