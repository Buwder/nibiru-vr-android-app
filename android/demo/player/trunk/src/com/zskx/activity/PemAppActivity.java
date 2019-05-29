package com.zskx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.zskx.controller.ActivityHolder;
import com.zskx.dialog.ExitDialog;
/**
 * 进入界面
 * @author demo
 *
 */
public class PemAppActivity extends Activity {
	
	/**进入预览主界面到按钮*/
	private ImageView enterPerMainBtn;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview);
        
        initView();
    }
    /**
     * 初始化View
     */
    private void initView(){
    	enterPerMainBtn = (ImageView)findViewById(R.id.enter);
    	
    	initViewListener();
    }
    /**
     * 初始化点击事件
     */
    private void initViewListener(){
    	enterPerMainBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(PemAppActivity.this, PreMainActivity.class);
				PemAppActivity.this.startActivity(intent);
			}
		});
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	//移除控制器中
    	ActivityHolder.getIntance().push(this);
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	//压入控制器中
    	ActivityHolder.getIntance().pop(this);
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	//移除控制器中
    	ActivityHolder.getIntance().push(this);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	
    	if(keyCode==KeyEvent.KEYCODE_BACK){
    	
    		ExitDialog.show(this);
    		
    	}
    	
    	return super.onKeyDown(keyCode, event);
    }
}