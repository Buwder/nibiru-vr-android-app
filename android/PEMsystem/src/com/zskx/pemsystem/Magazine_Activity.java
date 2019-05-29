package com.zskx.pemsystem;

import java.util.ArrayList;

import org.kobjects.util.Util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zskx.net.NetConfiguration;
import com.zskx.net.request.AbstractRequest.GetResponseListener;
import com.zskx.net.request.GetAllMagazineByTypeRequest;
import com.zskx.net.request.GetAllMagazineTypeRequest;
import com.zskx.net.response.MagazineDetailEntity;
import com.zskx.net.response.MagazineTypeEntity;
import com.zskx.net.response.ResponseEntity;
import com.zskx.pemsystem.adpater.GalleryAdapter;
import com.zskx.pemsystem.util.AsyncImageLoader;
import com.zskx.pemsystem.util.AsyncImageLoader.ImageCallback;
import com.zskx.pemsystem.util.AppConfiguration;
import com.zskx.pemsystem.util.BackButtonSet;
import com.zskx.pemsystem.util.Constant;
import com.zskx.pemsystem.util.MyPost;
import com.zskx.pemsystem.util.ScreenManager;
import com.zskx.pemsystem.util.UtilService;

/**
 * 
 * @author guokai
 *
 */
public class Magazine_Activity extends MenuActivity {
	private final String TAG = "magazine";
	ImageView imageView;
	private View server_progressBar;	//连接服务器进度条
	private boolean isResponsed = false; //显示连接服务器进度条的标志
	
	private final static int DEFAULT_COUNT = 30;//每页默认每页数据条数
	private int totalPageCount = 1;//  第一次访问，必须设为 1
	private int currentPage = 0;
	private int totalCount = 0;
	
	ArrayList<Button> buttons = new ArrayList<Button>();
	int[] btns = new int[]{R.id.magazine_button_1, R.id.magazine_button_2, R.id.magazine_button_3, R.id.magazine_button_4, R.id.magazine_button_5};
	ArrayList<View> lyts = new ArrayList<View>(); //用来改变是否被点中
	
	
	Gallery gallery;

	
	private ArrayList<MagazineDetailEntity> magazineList; ///杂志列表
	private MagazineDetailEntity entity;
	

	private ArrayList<MagazineTypeEntity> typeList; //杂志类型列表
	private LinearLayout typiesView; //HorizontalScrollView中的LinearLayout
	
	private String[] buttonImages = new String[]{Constant.MAGAZINE_type_1, Constant.MAGAZINE_type_2, Constant.MAGAZINE_type_3, Constant.MAGAZINE_type_4};
	
	private static String sessionID ;
	
	
/*	ArrayList<Integer> lyts_r = new ArrayList<Integer>();
 * 	ArrayList<Drawable> imageViews_1 = new ArrayList<Drawable>();
	ArrayList<Drawable> imageViews_2 = new ArrayList<Drawable>();
	ArrayList<Drawable> imageViews_3 = new ArrayList<Drawable>();
	ArrayList<Drawable> imageViews_4 = new ArrayList<Drawable>();
	ArrayList<Drawable> imageViews_5 = new ArrayList<Drawable>();
 * 	View lyt_1;
	View lyt_2;
	View lyt_3;
	View lyt_4;
	View lyt_5;
	
	Button btn_1;
	Button btn_2;
	Button btn_3;
	Button btn_4;
	Button btn_5;*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		ScreenManager.getScreenManager().pushActivity(this);
		setContentView(R.layout.megazine_activity);
		
		if(AppConfiguration.user != null) {
			sessionID = AppConfiguration.user.getSessionId();
		}else{
			UtilService.show(Magazine_Activity.this, "用户过期，请重新登录");
			finish();
		}
		
		server_progressBar = this.findViewById(R.id.magazine_progressbar);
		typiesView = (LinearLayout) this.findViewById(R.id.magazine_mainmenu);
		typiesView.removeAllViews();
		gallery = (Gallery) findViewById(R.id.magazine_gallery);
				
		getMagazineTypeList();//从服务器得到杂志类别列表
		
		if(!isResponsed) server_progressBar.setVisibility(View.VISIBLE);
		
		BackButtonSet.setButton(this);
		Log.i(TAG, "onCreate over!");
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Log.i(TAG, "onSTART over!");
	}
	
	
	/**
	 * 从服务器得到杂志类别列表
	 */
	private boolean Debug_Type = true;
	private void getMagazineTypeList() {
		GetAllMagazineTypeRequest allMagazineTypeRequest = new GetAllMagazineTypeRequest(new GetResponseListener<MagazineTypeEntity>(){

			@Override
			public void onSuccess(ResponseEntity<MagazineTypeEntity> result) {
				typeList =  (ArrayList<MagazineTypeEntity>) result.getContent();
				if(typeList != null) Log.i(TAG, "typeList:::" + typeList.size());
				
				MyPost.post(new Runnable(){
					@Override
					public void run() {
						initButtons(); //初始化控件
					}});
			}

			@Override
			public void onError(final ResponseEntity<MagazineTypeEntity> result) {
				
				MyPost.post(new Runnable(){
					@Override
					public void run() {
						UtilService.show(Magazine_Activity.this, result.getMsg());
						Log.i(TAG, "GetAllMagazineTypeRequest::" + result.getMsg());
						server_progressBar.setVisibility(View.GONE);
					}});
				
				isResponsed = true;
			}}, sessionID, currentPage, DEFAULT_COUNT);
		allMagazineTypeRequest.sendByThread();
		Debug_Type = allMagazineTypeRequest.getDebug();
	}
	
	/**
	 * 初始化控件
	 */
	private void initButtons() {
		Log.i(TAG, "initbUTTON start!");
	
		addArtificialType(); //添加人造杂志类型
		
		setHorizontalScroll(); //如果按钮图像都是空，设置HorizontalScroll高度
		
		newViews(); //动态加载控件
		
		changeView(lyts, 0);	
		getMagazineDetails(typeList.get(0).getMagazineTypeId()); //获取各种类型的杂志
		
		Log.i(TAG, "initbUTTON finish!");
	}
	
	/**
	 * 添加人造杂志类型
	 */
	private void addArtificialType() {
		if(typeList == null || typeList.size() == 0 && Debug_Type) {
			if(typeList == null) typeList = new ArrayList<MagazineTypeEntity>();
			for (int i = 1; i <= 4; i++) {
				MagazineTypeEntity typeEntity = new MagazineTypeEntity();
				typeEntity.setMagazineTypeId(""+ i);
				typeEntity.setMagazineTypeTitle(getResources().getStringArray(R.array.button_name)[i-1]);
				typeEntity.setMagazineTypeIconUrl(buttonImages[i-1]);
				typeList.add(typeEntity);
			}
		}
	}

	private boolean iconFlag = true; //
	/**
	 * 如果按钮图像都是空，设置HorizontalScroll高度
	 */
	private void setHorizontalScroll() {
		for (int i = 0  ; i < typeList.size(); i++) {
			MagazineTypeEntity typeEntity = typeList.get(i);
			if(typeEntity.getMagazineTypeIconUrl() != null && typeEntity.getMagazineTypeIconUrl().equals(""))	
				iconFlag = false;
		}
		
		if(!iconFlag){
			LayoutParams lp = typiesView.getLayoutParams();
			lp.height = LayoutParams.WRAP_CONTENT;
			typiesView.setLayoutParams(lp);
		}
	}

	int button_id = 123;
	int typeTextView_id = 456;
	int typeView_id = 789;
	/**'
	 * 动态加载控件
	 */
	private void newViews() {
		LinearLayout typeView; //包含标题和按钮
		TextView typeTextView; //标题
		int width = Magazine_Activity.this.getWindowManager().getDefaultDisplay().getWidth();
		AsyncImageLoader asyncImageLoader =  new AsyncImageLoader();
			
		for (int i = 0  ; i < typeList.size(); i++) {
			
			MagazineTypeEntity typeEntity = typeList.get(i);
			typeView = new LinearLayout(Magazine_Activity.this);
			typeTextView = new TextView(Magazine_Activity.this);
			final Button typeButton = new Button(this); //按钮
			Log.i(TAG, "imageUrl_title>>>>" + typeEntity.getMagazineTypeTitle());
			String imageUrl = typeEntity.getMagazineTypeIconUrl();
			Log.i(TAG, "imageUrl_url>>>>" + imageUrl);
			typeTextView.setTextSize(12);
			
			
			
			typeTextView.setId(typeTextView_id + i);
			typeView.setId(typeView_id + i);
			typeButton.setId(button_id + i);
			typeButton.setOnClickListener(new ButtonClick());
			typeTextView.setText(typeEntity.getMagazineTypeTitle());
			typeTextView.setTextColor(getResources().getColor(R.color.white));
			
			if(!imageUrl.equals("")) {
				asyncImageLoader.loadDrawable(imageUrl, new ImageCallback(){
					@Override
					public void imageLoaded(Drawable imageDrawable,String imageUrl) {
						typeButton.setBackgroundDrawable(imageDrawable);
					}});
				typeView.addView(typeButton, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			}else{
				typeView.addView(typeButton, new LayoutParams(LayoutParams.WRAP_CONTENT, 0));
				typeTextView.setTextSize(14);
				typeTextView.setPadding(0, 10, 0, 10);
			}
			
			typeView.addView(typeTextView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

			typeView.setFocusable(true);
			typeView.setClickable(true);
			typeView.setOrientation(LinearLayout.VERTICAL);
			typeView.setGravity(Gravity.CENTER);
			typeView.setOnClickListener(new LytClick());
			setLytWidth(width, typeView); //设置图片按钮宽度
		
			lyts.add(typeView);
			typiesView.addView(typeView); //添加到ScrollView
			Log.i(TAG, "lyts:: " + lyts.size() + "    typiesView:: " + typiesView);
		}
	}

	
	/********************获取各种类型的杂志*****************************************************/
	private boolean Debug_Details = true;
	/**
	 * 获取各种类型的杂志
	 * @param magazineTypeId
	 */
	private void getMagazineDetails(String magazineTypeId) {
		Log.i(TAG, "getMagazineDetails start!");
		GetAllMagazineByTypeRequest magazineRequest = new GetAllMagazineByTypeRequest(new GetResponseListener<MagazineDetailEntity>(){

	
			public void onSuccess(ResponseEntity<MagazineDetailEntity> result) {
				magazineList = (ArrayList<MagazineDetailEntity>) result.getContent();
				totalCount = result.getTotalCount();
				totalPageCount = (totalCount + DEFAULT_COUNT - 1) / DEFAULT_COUNT;
				
				isResponsed = true;
				MyPost.post(new Runnable() {
					
					public void run() {
						server_progressBar.setVisibility(View.GONE);
						getMagazineDetailEntity();
					}
				});
				
			}
		
			public void onError(final ResponseEntity<MagazineDetailEntity> result) {
				
				MyPost.post(new Runnable() {
					@Override
					public void run() {
						server_progressBar.setVisibility(View.GONE);
						UtilService.show(Magazine_Activity.this, result.getMsg());
					}
				});
				isResponsed = true;
			}},	sessionID, magazineTypeId, currentPage, DEFAULT_COUNT);
		magazineRequest.sendByThread();
		Debug_Details = magazineRequest.getDebug();
		Log.i(TAG, "getMagazineDetails over!");
	}

	
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "onResume over!");
	}



	private void getMagazineDetailEntity() {
		Log.i(TAG, "getMagazineDetailEntity start!");
		if(magazineList == null || magazineList.size() == 0 && Debug_Details) {
			set_magazine_list_debug(); //测试人工数据
			//initImages_debug();
		}
		
		Log.i(TAG, "getMagazineDetailEntity ---done" + gallery);
		if(magazineList.size()==0 && !Debug_Details) {
			UtilService.show(this, "暂时木有杂志");
			return;
		}
		gallery.setAdapter(new GalleryAdapter(Magazine_Activity.this, magazineList));
		gallery.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.i(TAG, "arg2==" + arg2 + "  magazineList " + magazineList );
				Intent _intent = new Intent();
				_intent.putExtra("magazineUrl", magazineList.get(arg2).getmagazineUrl());
				_intent.setClass(Magazine_Activity.this, Magazine_Play.class);
				startActivity(_intent);
			}});
		
		server_progressBar.setVisibility(View.GONE);
		Log.i(TAG, "getMagazineDetailEntity over!");
	}
	

	/**
	 * 测试人工数据
	 */
	private void set_magazine_list_debug() {
		magazineList = new ArrayList<MagazineDetailEntity>();
		entity = new MagazineDetailEntity();
		entity.setmagazineUrl("magazineUrl---1");
		entity.setmagazineImage(Constant.MAGAZINE_IMAGE_1);
		magazineList.add(entity);
		entity = new MagazineDetailEntity();
		entity.setmagazineUrl("magazineUrl---2");
		entity.setmagazineImage(Constant.MAGAZINE_IMAGE_2);
		magazineList.add(entity);
		entity = new MagazineDetailEntity();
		entity.setmagazineUrl("magazineUrl---3");
		entity.setmagazineImage(Constant.MAGAZINE_IMAGE_3);
		magazineList.add(entity);
		Log.i(TAG, "set_magazine_list::" + magazineList.size());
	}
/*
	 //* 异步加载图片并添加到列表
	private void getImagesFromServer() {
		AsyncImageLoader asyncImageLoader =  new AsyncImageLoader();
		for(int i = 0; i < magazineList.size(); i++){
			String imageUrl = magazineList.get(i).getmagazineImage();
			asyncImageLoader.loadDrawable(imageUrl, new ImageCallback(){

				@Override
				public void imageLoaded(Drawable imageDrawable, String imageUrl) {
					imageViews_1.add(imageDrawable);
					Log.i(TAG, "getImagesFromServer >> " + imageViews_1.size());
				}});
		}
	}
	
	private void initImages_debug() {
		imageViews_1.add(getResources().getDrawable(R.drawable.magazine_1));
		imageViews_1.add(getResources().getDrawable(R.drawable.magazine_2));
		imageViews_1.add(getResources().getDrawable(R.drawable.magazine_3));
		imageViews_2.add(getResources().getDrawable(R.drawable.magazine_2));
		imageViews_2.add(getResources().getDrawable(R.drawable.magazine_3));
		imageViews_2.add(getResources().getDrawable(R.drawable.magazine_1));
		imageViews_3.add(getResources().getDrawable(R.drawable.magazine_3));
		imageViews_3.add(getResources().getDrawable(R.drawable.magazine_2));
		imageViews_3.add(getResources().getDrawable(R.drawable.magazine_1));
		imageViews_4.add(getResources().getDrawable(R.drawable.magazine_2));
		imageViews_4.add(getResources().getDrawable(R.drawable.magazine_1));
		imageViews_4.add(getResources().getDrawable(R.drawable.magazine_3));
		imageViews_5.add(getResources().getDrawable(R.drawable.magazine_1));
		imageViews_5.add(getResources().getDrawable(R.drawable.magazine_3));
		imageViews_5.add(getResources().getDrawable(R.drawable.magazine_2));
	}
	
	
	private void initLayout_debug() {
		int width = this.getWindowManager().getDefaultDisplay().getWidth();
		
		lyts.add(lyt_1);
		lyts.add(lyt_2);
		lyts.add(lyt_3);
		lyts.add(lyt_4);
		lyts.add(lyt_5);
		lyts_r.add(R.id.magazine_layout_1);
		lyts_r.add(R.id.magazine_layout_2);
		lyts_r.add(R.id.magazine_layout_3);
		lyts_r.add(R.id.magazine_layout_4);
		lyts_r.add(R.id.magazine_layout_5);
		
		for (int i = 0; i < lyts.size(); i++) {
			lyts.set(i, findViewById(lyts_r.get(i)));
			setLytWidth(width, lyts.get(i));
			lyts.get(i).setOnClickListener(new ButtonClick_debug());
		}
	}
	
	
	private void initButtons_debug() {
		buttons.add(btn_1);
		buttons.add(btn_2);
		buttons.add(btn_3);
		buttons.add(btn_4);
		buttons.add(btn_5);
		for(int i = 0; i< btns.length; i++){
			buttons.set(i, (Button) this.findViewById(btns[i]));
			buttons.get(i).setOnClickListener(new ButtonClick_debug());
		}
	}
*/	
	
	/**
	 * 设置按钮的宽度
	 * @param width 屏幕的宽度
	 * @param v 按钮的view
	 */
	private void setLytWidth(int width, View v) {
		LayoutParams ayoutParams_1 = new LayoutParams(-2, -2);
		
		if(typeList.size() <= 2 ) ayoutParams_1.width = width/2;
		else if(typeList.size() == 3 ) ayoutParams_1.width = width/3;
		else ayoutParams_1.width = width/4;
		ayoutParams_1.height = -2;
		v.setLayoutParams(ayoutParams_1);
		Log.d(TAG, String.valueOf(ayoutParams_1.width));
	}
	
	/**
	 * 改变选择到的按钮颜色
	 * @param list 所有按钮的列表
	 * @param num 要选择的按钮
	 */
	private void changeView(ArrayList<View> list, int num){
		if(Debug_Type){
			changeView_debug( list,  num);
		}else{
			changeView_server( list,  num);
		}
	}
	
	private void changeView_debug(ArrayList<View> list, int num){
		for(int i = 0; i<list.size();i++){
			if(i == num ) {
				list.get(i).setBackgroundColor(getResources().getColor(R.color.darkblue));
			}
			else{
				list.get(i).setBackgroundColor(Color.TRANSPARENT);
			}
		}
	}
	
	private void changeView_server(ArrayList<View> list, int num){
		TextView typeTextView;
		for(int i = 0; i<list.size();i++){
			typeTextView = (TextView) findViewById(typeTextView_id + i);
			
			if(i == num){
				typeTextView.setTextColor(getResources().getColor(R.color.blue));
				list.get(i).setBackgroundColor(getResources().getColor(R.color.white));
			}else{
				typeTextView.setTextColor(getResources().getColor(R.color.black));
				list.get(i).setBackgroundColor(getResources().getColor(R.color.gray1));
			}
		}
	}
	
	/*
	private final class ButtonClick_debug implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.magazine_button_1 :
				changeView(lyts, lyts.get(0));
				gallery.setAdapter(new GalleryAdapter(Magazine_Activity.this, imageViews_1));
				break;
			case R.id.magazine_button_2 :
				changeView(lyts, lyts.get(1));	
				gallery.setAdapter(new GalleryAdapter(Magazine_Activity.this, imageViews_2));
				break;
			case R.id.magazine_button_3 :
				changeView(lyts, lyts.get(2));
				gallery.setAdapter(new GalleryAdapter(Magazine_Activity.this, imageViews_3));
				break;
			case R.id.magazine_button_4 :
				changeView(lyts, lyts.get(3));
				gallery.setAdapter(new GalleryAdapter(Magazine_Activity.this, imageViews_4));
				break;
			case R.id.magazine_button_5 :
				changeView(lyts, lyts.get(4));
				gallery.setAdapter(new GalleryAdapter(Magazine_Activity.this, imageViews_5));
				break;
		
			case R.id.magazine_layout_1:
				changeView(lyts, lyts.get(0));
				gallery.setAdapter(new GalleryAdapter(Magazine_Activity.this, imageViews_1));
				break;
			case R.id.magazine_layout_2:
				changeView(lyts, lyts.get(1));	
				gallery.setAdapter(new GalleryAdapter(Magazine_Activity.this, imageViews_2));
				break;
			case R.id.magazine_layout_3:
				changeView(lyts, lyts.get(2));
				gallery.setAdapter(new GalleryAdapter(Magazine_Activity.this, imageViews_3));
				break;
			case R.id.magazine_layout_4:
				changeView(lyts, lyts.get(3));
				gallery.setAdapter(new GalleryAdapter(Magazine_Activity.this, imageViews_4));
				break;
			case R.id.magazine_layout_5:
				changeView(lyts, lyts.get(4));
				gallery.setAdapter(new GalleryAdapter(Magazine_Activity.this, imageViews_5));
				break;
			default:
				break;
			}
		}
		
	}
	
	*/
	private final class ButtonClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			for (int i = 0; i < typeList.size(); i++) {
				if(v.getId() == i){
					changeView(lyts, i);
					if(!isResponsed) server_progressBar.setVisibility(View.VISIBLE);
					getMagazineDetails(typeList.get(i).getMagazineTypeId());
				}
			}
		}
	}
	
	private final class LytClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			for (int i = 0; i < typeList.size(); i++) {
				if(v.getId() == typeView_id + i){
					changeView(lyts, i);
					if(!isResponsed) server_progressBar.setVisibility(View.VISIBLE);
					getMagazineDetails(typeList.get(i).getMagazineTypeId());
				}
			}
							
		}
	}
	
/*	public class GalleryAdapter extends BaseAdapter{
		Context context;
		//ArrayList<Drawable> imgViews = new ArrayList<Drawable>();
		ArrayList<MagazineDetailEntity> magazineList = new ArrayList<MagazineDetailEntity>();
		AsyncImageLoader asyncImageLoader =  new AsyncImageLoader();
		public GalleryAdapter(Context c , ArrayList<MagazineDetailEntity> imagelist){
			context = c;
			magazineList = imagelist;
		}
		@Override
		public int getCount() {
			return magazineList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			
			
			
			final ImageView imageView = new ImageView(context);
			String imageUrl = magazineList.get(position).getmagazineImage();
			Log.i("MagazineAdapter", "imageUrl:" + position + ">>>>>>>" + imageUrl);
			asyncImageLoader.loadDrawable(imageUrl, new ImageCallback(){

				@Override
				public void imageLoaded(Drawable imageDrawable, String imageUrl) {
					imageView.setImageDrawable(imageDrawable);
				}});
			
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			//imageView.setLayoutParams(new Gallery.LayoutParams(136, 88));
			imageView.setLayoutParams(new Gallery.LayoutParams(getWindowManager().getDefaultDisplay().getWidth()-110, LayoutParams.FILL_PARENT));
			
			return imageView;
		}
	}*/
}
