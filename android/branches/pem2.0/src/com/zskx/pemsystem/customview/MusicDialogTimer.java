package com.zskx.pemsystem.customview;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.zskx.pemsystem.Music_Activity;
import com.zskx.pemsystem.R;
import com.zskx.pemsystem.customview.MusicDialogButton.Longtouch;
import com.zskx.pemsystem.util.UtilService;

public class MusicDialogTimer extends Dialog {
	private static String TAG = "Music_dialog_timer";
    private static int default_width = 160; //默认宽度
    private static int default_height = 120;//默认高度
    Context context_this;
    MusicDialogTimer dialog;
    
    private static ObserverShowBtn observerShowBtn;
    
	private Button music_btn_timer_1; //快捷设置分钟按钮
	private Button music_btn_timer_2;
	private Button music_btn_timer_3;
	private Button music_btn_timer_4;
	private MusicDialogButton music_btn_timer_plus_h; //小时，分钟递增递减按钮
	private MusicDialogButton music_btn_timer_plus_m;
	private MusicDialogButton music_btn_timer_minus_h;
	private MusicDialogButton music_btn_timer_minus_m;
	private Button music_btn_timer_confirm; //开启定时器
	private Button music_btn_timer_close; // 关闭定时器
	private Button music_btn_timer_cancle; //关闭对话框
	private EditText music_edit_h;
	private EditText music_edit_m;
	
	private int time_of_close_minite = 0;
	private int time_of_close_hour = 0;
	
    private static Timer timer;
    private TimerTask task;
    private Handler handler;
    public static final int TIMER = 1566;
    
    
	public MusicDialogTimer(Context context , int layout ,ObserverShowBtn observer1) {
		this(context, default_width, default_height, layout ,observer1);
	}

	public MusicDialogTimer(Context context, int width,
			int height, int layout ,ObserverShowBtn observer1) {
		super(context, R.style.music_dialog_timer);
		context_this = context;
		dialog = this ;
		observerShowBtn = observer1;
		
		setContentView(layout);
        //set window params
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        //set width,height by density and gravity
        float density = getDensity(context);
        
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        
        System.out.println("density>>>>" + density);
        params.width = (int) (dm.widthPixels * 0.74);
        params.height = (int) (dm.heightPixels * 0.64);
//        params.width = (int) (width*density);
//        params.height = (int) (height*density);
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        Log.i(TAG, "width::" + params.width + "    height::" + params.height);
		time_of_close_minite = 0;
		time_of_close_hour = 0;
		 
		init_views_dialog(this);
    }
    private float getDensity(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
       return dm.density;
    }
    
    
    public void dialog_confirm(long delay){
    	Log.i(TAG, "dialog_confirm!!");
    	timer = new Timer();
    	handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case TIMER:
					if(context_this != null) ((Activity) context_this).finish();
					break;
				}
			}
    	};
    	task = new TimerTask() {
			@Override
			public void run() {
				Message message = new Message();
				message.what = TIMER;
				handler.sendMessage(message);
			}
		};
		
		timer.schedule(task, delay);
		if(observerShowBtn!=null) observerShowBtn.show_button();
		UtilService.show(context_this, "定时器开启!!");
    }
    
    public static void dialog_cancle_timer(){
    	if(timer != null) {
    		timer.cancel();
    		if(observerShowBtn!=null) observerShowBtn.hide_button();
    	}
    }
    
    /**
     * 初始化dialog所有控件
     */
	private void init_views_dialog(MusicDialogTimer dialog) {
		
		Log.i(TAG, "init_views_dialog!!");
		music_btn_timer_1 = (Button) dialog.findViewById(R.id.music_dialog_timer_btn_1);
		music_btn_timer_2 = (Button) dialog.findViewById(R.id.music_dialog_timer_btn_2);
		music_btn_timer_3 = (Button) dialog.findViewById(R.id.music_dialog_timer_btn_3);
		music_btn_timer_4 = (Button) dialog.findViewById(R.id.music_dialog_timer_btn_4);
		music_btn_timer_plus_h = (MusicDialogButton) dialog.findViewById(R.id.music_dialog_timer_h_plus);
		music_btn_timer_plus_m = (MusicDialogButton) dialog.findViewById(R.id.music_dialog_timer_m_plus);
		music_btn_timer_minus_h = (MusicDialogButton) dialog.findViewById(R.id.music_dialog_timer_h_minus);
		music_btn_timer_minus_m = (MusicDialogButton) dialog.findViewById(R.id.music_dialog_timer_m_minus);
		music_btn_timer_confirm = (Button) dialog.findViewById(R.id.music_dialog_time_btn_confirm);
		music_btn_timer_cancle = (Button) dialog.findViewById(R.id.music_timer_btn_cancle_dialog);
		music_btn_timer_close = (Button) dialog.findViewById(R.id.music_dialog_time_btn_close);
		music_edit_h = (EditText) dialog.findViewById(R.id.music_dialog_timer_edit_h);
		music_edit_m = (EditText) dialog.findViewById(R.id.music_dialog_timer_edit_m);
		
		music_btn_timer_1.setOnClickListener(dialog_byn_click);
		music_btn_timer_2.setOnClickListener(dialog_byn_click);
		music_btn_timer_3.setOnClickListener(dialog_byn_click);
		music_btn_timer_4.setOnClickListener(dialog_byn_click);
		music_btn_timer_plus_h.setOnClickListener(dialog_byn_click);
		music_btn_timer_plus_m.setOnClickListener(dialog_byn_click);
		music_btn_timer_minus_h.setOnClickListener(dialog_byn_click);
		music_btn_timer_minus_m.setOnClickListener(dialog_byn_click);
		music_btn_timer_confirm.setOnClickListener(dialog_byn_click);
		music_btn_timer_cancle.setOnClickListener(dialog_byn_click);
		music_btn_timer_close.setOnClickListener(dialog_byn_click);
		
		/*设置长按接口方法*/
		music_btn_timer_plus_h.set_long_click(longtouch);
		music_btn_timer_plus_m.set_long_click(longtouch);
		music_btn_timer_minus_h.set_long_click(longtouch);
		music_btn_timer_minus_m.set_long_click(longtouch);
	}
	
	@Override
	public void show() {
		show_editText(); //显示dialog时显示当前设置的时间
		super.show();
	}

	private void show_editText() {
		music_edit_m.setText(addzero(time_of_close_minite));
		music_edit_h.setText(addzero(time_of_close_hour));
	}

	
	private View.OnClickListener dialog_byn_click = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.music_dialog_timer_btn_1:
				change_btn_set(music_btn_timer_1 , 5);
				break;
			case R.id.music_dialog_timer_btn_2:
				change_btn_set(music_btn_timer_2 , 10);
				break;
			case R.id.music_dialog_timer_btn_3:
				change_btn_set(music_btn_timer_3, 30);
				break;
			case R.id.music_dialog_timer_btn_4:
				change_btn_set(music_btn_timer_4, 60);
				break;
			
			
			case R.id.music_dialog_timer_h_plus:
				btn_h_plus();
				break;
			case R.id.music_dialog_timer_m_plus:
				btn_m_plus();
				break;
			case R.id.music_dialog_timer_h_minus:
				btn_h_minus();
				break;
			case R.id.music_dialog_timer_m_minus:
				btn_m_minus();
				break;
			
			
			case R.id.music_dialog_time_btn_confirm:
				time_of_close_hour = Integer.parseInt(music_edit_h.getText().toString());
				time_of_close_minite = Integer.parseInt(music_edit_m.getText().toString());
				long delay = (time_of_close_minite*60 + time_of_close_hour*60*60)*1000;
				if(delay > 0)  dialog.dialog_confirm(delay); //所设时间大于0，开启定时器
				else if(delay <0 ) UtilService.show(context_this, "请输入正数!!"); //
				else if(delay == 0) dialog_cancle_timer(); //所设时间＝0，关闭定时器
				dialog.dismiss();
				Log.i(TAG, time_of_close_hour + ":" + time_of_close_minite);
				break;
			case R.id.music_timer_btn_cancle_dialog:
				dialog.dismiss();
				break;
			case R.id.music_dialog_time_btn_close:
				dialog_cancle_timer();
				dialog.dismiss();
				break;
			}
		}
	};

	/**
	 * 快捷设定分钟
	 * @param btn 所选择的按钮
	 * @param min 所设定的时间(分钟)
	 */
	private void change_btn_set(Button btn ,int min) {
		time_of_close_hour = 0;
		time_of_close_minite = min;
		show_editText();
		change_btn_normal();
		btn.setBackgroundResource(R.drawable.music_settime_shortcut_bg_current);
	}
	/**
	 * 把所有快捷按钮未激活
	 */
	private void change_btn_normal() {
		music_btn_timer_1.setBackgroundResource(R.drawable.music_settime_shortcut_bg);
		music_btn_timer_2.setBackgroundResource(R.drawable.music_settime_shortcut_bg);
		music_btn_timer_3.setBackgroundResource(R.drawable.music_settime_shortcut_bg);
		music_btn_timer_4.setBackgroundResource(R.drawable.music_settime_shortcut_bg);
	}
	/**
	 * 点击小时增加按钮，不能大于99
	 */
	private void btn_h_plus() {
		if(time_of_close_hour < 99) time_of_close_hour ++;
		music_edit_h.setText(addzero(time_of_close_hour));
	}
	/**
	 * 点击小时减少按钮，不能小于0
	 */
	private void btn_h_minus() {
		if(time_of_close_hour < 1) time_of_close_hour = 0;
		else time_of_close_hour--;
		music_edit_h.setText(addzero(time_of_close_hour));
	}
	/**
	 * 点击分钟减少按钮，小于0后，变为60分钟
	 */
	private void btn_m_minus() {
		if(time_of_close_minite < 1) {
			time_of_close_minite = 60;
		}
		time_of_close_minite --;
		music_edit_m.setText(addzero(time_of_close_minite));
	}

	private void btn_m_plus() {
		if(time_of_close_minite > 59) {
			time_of_close_minite = 0;
		}
		time_of_close_minite ++;
		music_edit_m.setText(addzero(time_of_close_minite));
	}
	/**
	 * 为单个数字前面加0，变为双位数
	 * @param time
	 * @return
	 */
	private String addzero(int time) {
		String num;
		if(time < 10 && time >-1) num = "0"+ time;
		else num = "" + time;
		return num;
	}

	/**
	 * 实现长按接口方法
	 */
	Longtouch longtouch = new Longtouch() {
		
		@Override
		public void do_in_long(View v) {
			
			switch (v.getId()) {
			case R.id.music_dialog_timer_h_plus:
				btn_h_plus();
				break;
			case R.id.music_dialog_timer_m_plus:
				btn_m_plus();
				break;
			case R.id.music_dialog_timer_h_minus:
				btn_h_minus();
				break;
			case R.id.music_dialog_timer_m_minus:
				btn_m_minus();
				break;
			}
		}
	};
	
	/**
	 * 显示定时器接口
	 * @author guokai
	 *
	 */
	public interface ObserverShowBtn{
		public void show_button();
		public void hide_button();
	}
}

