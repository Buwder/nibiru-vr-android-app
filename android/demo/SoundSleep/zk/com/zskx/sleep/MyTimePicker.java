package com.zskx.sleep;

import android.app.Dialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyTimePicker extends LinearLayout {

	/**加分钟*/
	private Button minute_add;
	/**减分钟*/
	private Button minute_reduce;
	/**加秒数*/
	private Button second_add;
	/**减秒数*/
	private Button second_reduce;
	/**确定按钮*/
	private Button sure;
	/**取消按钮*/
	private Button cancel;
	/**分钟数*/
	private TextView minute;
	/**秒钟数*/
	private TextView second;
	
	private int set_minute = 0;
	private int set_second = 0;
	
	/**外层的对话框*/
	private Dialog dialog;
	/**设置时间监听器*/
	private TimeSetListener listener;
	
	public MyTimePicker(Context context,int all_minute,int all_second,Dialog dialog,TimeSetListener listener) {
		super(context);
		
		set_minute = all_minute;
		set_second = all_second;
		
		this.dialog = dialog;
		this.listener = listener;
		
		init();
	}

	public MyTimePicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init(){
		View.inflate(getContext(), R.layout.time_picker, this);
		
		minute_add = (Button) findViewById(R.id.minute_add);
		minute_reduce = (Button) findViewById(R.id.minute_reduce);
		second_add = (Button) findViewById(R.id.second_add);
		second_reduce = (Button) findViewById(R.id.second_reduce);
		sure = (Button) findViewById(R.id.sure);
		cancel = (Button) findViewById(R.id.cancel);
		minute = (TextView)findViewById(R.id.minute_text);
		second = (TextView)findViewById(R.id.second_text);
		
		updateMinute();
		updateSecond();
		
		initFunction();
	}
	
	public void updateMinute(){
		minute.setText(changeIntToTime(set_minute));
	}
	
	public void updateSecond(){
		second.setText(changeIntToTime(set_second));
	}
	
	private void initFunction(){
		minute_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				set_minute++;
				updateMinute();
			}
		});
		minute_reduce.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if((set_minute-1<0)){
					set_minute = 0;
				}else {
					set_minute--;
				}
				updateMinute();
			}
		});
		second_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if((set_second+1)>59){
					set_minute++;
					updateMinute();
					set_second = 0;
				}else {
					set_second++;
				}
				updateSecond();
				
			}
		});
		second_reduce.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if((set_second-1<0)){
						set_second = 0;
				}else {
					set_second--;
				}
				updateSecond();
			}
		});
		
		sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(listener!=null){
					if(set_minute==0&&set_second==0){
						MyToast.show(getContext(), "设置的时间必须大于0！");
					}else {
						listener.setTime(set_minute, set_second);
						dialog.dismiss();
					}
					
				}
			}
		});
		
		OnClickListener exitListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(dialog!=null){
					dialog.dismiss();
				}
				
			}
		};
		
		cancel.setOnClickListener(exitListener);
	}
	
	/**
     * 将整型转换为时间字符串
     * @param times
     * @return
     */
    public static String changeIntToTime(int times){
    	String time ;
    	if(times<10){
    		time = "0"+times;
    	}else {
    		time = ""+times;
		}
    	return time;
    }
    /**
     * 时间设置监听器
     * @author demo
     *
     */
    public interface TimeSetListener{
    	/**
    	 * 时间设置
    	 * @param minute 分钟
    	 * @param second 秒钟
    	 */
    	public void setTime(int minute,int second);
    }
}
