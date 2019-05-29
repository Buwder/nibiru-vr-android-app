package com.zskx.sleep;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zskx.sleep.MyTimePicker.TimeSetListener;
import com.zskx.view.Contanst;
import com.zskx.view.ImageUtils;
import com.zskx.view.PaneleView;
import com.zskx.view.SoundsModen;

public class SoundSleepActivity extends Activity {
	/**播放器*/
	private PaneleView player;
	/**背景音乐的播放器*/
	private MediaPlayer backgroundPlayer = new MediaPlayer();
	
	/**SD卡上音乐的信息*/
	private List<MultiMusicBean> list;
	/**音乐菜单列表*/
	private ListView musicList;
	/**播放时间选择*/
	private ImageView timePicker;
	/**退出按钮*/
	private ImageView exitBtn;
	/**向上按钮*/
	private ImageView upBtn;
	/**向下按钮*/
	private ImageView downBtn;
	/**展示播放时间和播放进度的文字*/
	private TextView showTime;
	/**展示播放的进度条*/
	private ProgressBar bar;
	/**播放暂停按钮*/
	private ImageView playOrPause;
	/**整个布局*/
	private RelativeLayout mainLayout;
	
	/**前一个被选中的位置*/
	private int oldSelectedPosition = -1;
	
	
	/**要求播放时间：分*/
	private int all_minute = 1;
	/**要求播放时间：秒*/
	private int all_second = 0;
	/**当前播放了的时间：分*/
	private int current_minute = 0;
	/**当前播放了的时间：秒*/
	private int current_second = 0;
	/**是否为暂停状态*/
	private boolean isPause = true;
	/**用来计时的timer*/
	private Timer timer;
	
	 /**更新播放状态*/
    private Handler progressHander  = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			case 1://1表示时间过了一秒
				current_second++;
				if(current_second>59){
					current_minute++;
					current_second=0;
				}
				break;
			case 2://2表示播放总时间被更新
				if((all_minute*60+all_second)<(current_minute*60+current_second)){
					current_minute = 0;
					current_second = 0;
				}
				bar.setMax(all_minute*60+all_second);
				break;
			case 3://3表示播放时间被重置为0
				current_minute = 0;
				current_second = 0;
				break;
			default:
				break;
			}
			updateProgressBar();
			updateShowTime();
			checkIsComplete();
		}
    };
    /**
     * 检测是否播放时间完成
     * @return
     */
    public void checkIsComplete(){
    	if(current_minute==all_minute&&current_second==all_second){
    		isPause = true;
    		playOrPause.setImageResource(R.drawable.play_start);
    		current_minute = 0;
			current_second = 0;
			updateProgressBar();
			updateShowTime();
//			if(timer!=null){
//				timer.cancel();
//			}
			pausePlay();

    	}
    	
    }
    /**
     * 更新进度条
     */
    public void updateProgressBar(){
    	bar.setProgress(current_minute*60+current_second);
    }
    
	/**
	 * 更新时间显示
	 */
    public void updateShowTime(){
    	showTime.setText(MyTimePicker.changeIntToTime(current_minute)+":"+MyTimePicker.changeIntToTime(current_second)+"/"+MyTimePicker.changeIntToTime(all_minute)+":"+MyTimePicker.changeIntToTime(all_second));
    }
    
    /**
     * 音乐列表适配器
     * @author demo
     *
     */
	public class MyListAdapter extends BaseAdapter{

		private List<ImageView> views = new ArrayList<ImageView>(); 
		
		public MyListAdapter(List<MultiMusicBean> list) {
			processData(list);
		}
		/**
		 * 提取数据转换为需要显示的东西。
		 * @param list 
		 * 
		 */
		private void processData(List<MultiMusicBean> list){
			views.clear();
			int position = 0;
			for (MultiMusicBean multiMusicBean : list) {
				ImageView iv = new ImageView(SoundSleepActivity.this);
				iv.setImageDrawable(XmlConfigLoader.getDrawableByPath(multiMusicBean.getImage(), SoundSleepActivity.this));
				iv.setTag(position);//设置一下在数据集合里的位置，以便后面直接取出
				views.add(iv);
			}
		}
		
		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public Object getItem(int position) {
			return views.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return views.get(position);
		}
		
	}
	
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.main_layout);
        //从XML配置文件中得到音乐信息
        try {
		    list = XmlConfigLoader.getMultiMusicList(getResources().openRawResource(R.raw.sound));
			for (MultiMusicBean multiMusicBean : list) {
				System.out.println(multiMusicBean.toString());
			}
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		init();
    }
    /**
     * 初始化
     */
    private void init(){
    	
    	player = (PaneleView)findViewById(R.id.player);
    	player.setIsAllPause(true);
    	
    	musicList = (ListView)findViewById(R.id.list);
    	timePicker = (ImageView)findViewById(R.id.time_picker);
    	exitBtn = (ImageView)findViewById(R.id.exit_btn);
    	upBtn = (ImageView)findViewById(R.id.up);
    	downBtn = (ImageView)findViewById(R.id.down);
    	playOrPause = (ImageView)findViewById(R.id.playOrpause);
    	showTime = (TextView)findViewById(R.id.show_time);
    	bar = (ProgressBar)findViewById(R.id.progress);
    	mainLayout = (RelativeLayout)findViewById(R.id.mainLayout);
    	
    	bar.setMax(all_minute*60+all_second);
    	updateShowTime();
    	
    	initFunction();
    }
    /**
     * 初始化功能
     */
    private void initFunction(){
    	/*时间设置事件*/
    	timePicker.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new Builder(SoundSleepActivity.this);
				
				Dialog dialog = builder.create();
				WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
				lp.x = 20;
				lp.y = 0;
				
				dialog.show();
				dialog.onWindowAttributesChanged(lp);
				dialog.getWindow().setContentView(new MyTimePicker(SoundSleepActivity.this, all_minute, all_second, dialog, new TimeSetListener() {
					
					@Override
					public void setTime(int minute, int second) {
						all_minute = minute;
						all_second = second;
						Message m = progressHander.obtainMessage();
						m.arg1 = 2;
						progressHander.sendMessage(m);
					}
				}));
			}
		});
    	/*退出事件设置*/
    	exitBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ExitDialog.show(SoundSleepActivity.this);
			}
		});
    	
    	/*音乐播放列表初始化*/
    	musicList.setAdapter(new MyListAdapter(list));
    	musicList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.i(SoundSleepActivity.this.getClass().getSimpleName(), list.get(position).getName()+"被点击");
				/*如果没有点击其他的列表，不会产生任何效果*/
				if(position==oldSelectedPosition){
					return ;
				}
				if(oldSelectedPosition!=-1){
					((View)musicList.getAdapter().getItem(oldSelectedPosition)).setBackgroundColor(Color.TRANSPARENT);
				}
				
				view.setBackgroundResource(R.drawable.pic001);
				oldSelectedPosition = position;
				Message m = progressHander.obtainMessage();
				m.arg1=3;
				progressHander.sendMessage(m);
				
				mainLayout.setBackgroundDrawable(XmlConfigLoader.getDrawableByPath(list.get(position).getImageBackGround(), SoundSleepActivity.this));
				/*加入其他声音的播放器初始化*/
				player.setSoundsModen(processDataToModen(list, position));
				player.initMultiMedias();
				/*设置背景音乐*/
				try {
					FileInputStream fis = new FileInputStream(XmlConfigLoader.getSdcardPath(SoundSleepActivity.this)+list.get(position).getBackGroundMusic());
					backgroundPlayer.reset();
					backgroundPlayer.setDataSource(fis.getFD());
					backgroundPlayer.setLooping(true);
					backgroundPlayer.prepare();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				startPlay();
			}
		});
    	/*向上移动*/
    	upBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				musicList.smoothScrollBy(-80, 1000);
			}
		});
    	/*向下移动*/
    	downBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				musicList.smoothScrollBy(80, 1000);
			}
		});
    	/*暂停播放按钮*/
    	playOrPause.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(oldSelectedPosition!=-1){
					if(isPause){
						startPlay();
					}else {
						pausePlay();
					}
				}
			}
		});
    	
    }  
    /**
     * 暂停播放
     */
    public void pausePlay(){
    	playOrPause.setImageResource(R.drawable.play_start);
		isPause = true;
		timer.cancel();
		player.setIsAllPause(true);
		player.pausePlayers();
		if(backgroundPlayer.isPlaying()){
			backgroundPlayer.pause();
		}
		
    }
    
    /**
     * 开始播放
     */
    public void startPlay(){
    	playOrPause.setImageResource(R.drawable.play_pause);
		isPause = false;
		/*设置每秒向进度更新控制器里发送一个更新请求*/
		if(timer!=null){
			timer.cancel();
		}
		timer = new Timer();
    	timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				Message m = progressHander.obtainMessage();
				m.arg1=1;
				progressHander.sendMessage(m);
			}
		}, 1000,1000);
    	player.setIsAllPause(false);
    	player.startPlay();
    	backgroundPlayer.start();
    }
    
   /**
    * 将从配置文件里读出的数据信息转换为播放器可用的数据模型
    * @param list
    * @return
    */
    public SoundsModen processDataToModen(List<MultiMusicBean> list,int position){
    	SoundsModen sm = null;
    	
    	 Map<String,Drawable> bitmapMap = new HashMap<String, Drawable>();//存放播放按钮背景图片
    	 Map<String,String> soundMap = new HashMap<String, String>();//存放播放音乐文件
    	 Map<String,String> nameMap = new HashMap<String, String>();//存放播放音乐名字
    	 Map<String,Integer> volumeMap = new HashMap<String, Integer>();//存放播放初始音量
    	
    	
			List<SingleMusicBean> sList = list.get(position).getSingleMusic();
			int i = 0;
			for (SingleMusicBean singleMusicBean : sList) {
				Drawable drawable = ImageUtils.scaleImage(SoundSleepActivity.this, XmlConfigLoader.getSdcardPath(SoundSleepActivity.this)+singleMusicBean.getImage());
//				Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity()!=PixelFormat.OPAQUE?Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565);
//				Canvas canvas = new Canvas(bitmap);
//				drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//				drawable.draw(canvas);
				switch (i) {
				case 0:
					bitmapMap.put(Contanst.bitmap_1, drawable);
					soundMap.put(Contanst.sound_1, XmlConfigLoader.getSdcardPath(this)+singleMusicBean.getMusic());
					nameMap.put(Contanst.name_1, singleMusicBean.getName());
					volumeMap.put(Contanst.volume_1, singleMusicBean.getVolumn());
					break;
				case 1:
					bitmapMap.put(Contanst.bitmap_2, drawable);
					soundMap.put(Contanst.sound_2, XmlConfigLoader.getSdcardPath(this)+singleMusicBean.getMusic());
					nameMap.put(Contanst.name_2, singleMusicBean.getName());
					volumeMap.put(Contanst.volume_2, singleMusicBean.getVolumn());
					break;
				case 2:
					bitmapMap.put(Contanst.bitmap_3, drawable);
					soundMap.put(Contanst.sound_3, XmlConfigLoader.getSdcardPath(this)+singleMusicBean.getMusic());
					nameMap.put(Contanst.name_3, singleMusicBean.getName());
					volumeMap.put(Contanst.volume_3, singleMusicBean.getVolumn());
					break;
				case 3:
					bitmapMap.put(Contanst.bitmap_4, drawable);
					soundMap.put(Contanst.sound_4, XmlConfigLoader.getSdcardPath(this)+singleMusicBean.getMusic());
					nameMap.put(Contanst.name_4, singleMusicBean.getName());
					volumeMap.put(Contanst.volume_4, singleMusicBean.getVolumn());
					break;
				case 4:
					bitmapMap.put(Contanst.bitmap_5, drawable);
					soundMap.put(Contanst.sound_5, XmlConfigLoader.getSdcardPath(this)+singleMusicBean.getMusic());
					nameMap.put(Contanst.name_5, singleMusicBean.getName());
					volumeMap.put(Contanst.volume_5, singleMusicBean.getVolumn());
					break;
				default:
					break;
				}
				i++;
			}
		
    	 sm = new SoundsModen(bitmapMap, soundMap, nameMap, volumeMap);
    	return sm;
    }
    
    private boolean isStopByActivity = false;
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
    	if(isStopByActivity){
    		startPlay();
    	}
    	isStopByActivity =false;
    }
    
    @Override
    protected void onStop() {
    	player.pausePlayers();
    	backgroundPlayer.pause();
    	
    	isStopByActivity = true;
    	super.onStop();
    }
    
    @Override
    protected void onDestroy() {
    	player.stopPlay();
    	backgroundPlayer.stop();
    	backgroundPlayer.release();
    	super.onDestroy();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	// TODO Auto-generated method stub
    	super.onSaveInstanceState(outState);
    }
}