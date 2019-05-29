package com.zskx.pemsystem;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zskx.pemsystem.util.ScreenManager;
import com.zskx.pemsystem.util.ShowNotification;

public class HelpActivity extends BaseActivity {

	private Button backButton;
	private Button homeButton;
	private Button helpBackButton;
	private TextView textIntroduction;
	private TextView textDocument;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ScreenManager.getScreenManager().pushActivity(this);
		setContentView(R.layout.help);
		initComponent();
		initDataOrEvent();
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		String contentStr=getResources().getString(R.string.HELP);
		ShowNotification.showNotification(this,"PEM",contentStr, HelpActivity.class,null);
	}
	
	
	
	private void initComponent() {
		backButton = (Button) findViewById(R.id.btn_back);
		homeButton = (Button) findViewById(R.id.btn_home);
		helpBackButton = (Button) findViewById(R.id.btn_help_back);
		textIntroduction=(TextView)findViewById(R.id.text_introduction);
		textDocument=(TextView)findViewById(R.id.text_document);
	}

	
	private void initDataOrEvent()
	{
		backButton.setOnClickListener(clickListener);
		homeButton.setOnClickListener(clickListener);
		helpBackButton.setOnClickListener(clickListener);
		
		String introduction=getResources().getString(R.string.help_system_introduce);
		
		String document=getResources().getString(R.string.help_document_content);
		
		 Spanned text_intro=Html.fromHtml(introduction);
		 //Spanned text_Document=Html.fromHtml(document);
		 
		 textIntroduction.setText(text_intro);
		 textDocument.setText(document);
		
		
		
		
	}
	
	private View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			
			
			case R.id.btn_home:
				Intent intent=new Intent(HelpActivity.this,HomeActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
				
			case R.id.btn_back:
			case R.id.btn_help_back:

				HelpActivity.this.finish();
				break;

			default:
				break;
			}

		}
	};
	

}
