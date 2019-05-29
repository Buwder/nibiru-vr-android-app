package com.zskx.pemsystem;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Debug;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.zskx.pemsystem.ebook.Directory;
import com.zskx.pemsystem.ebook.EBookUtil;
import com.zskx.pemsystem.ebook.MagazineBean;
import com.zskx.pemsystem.ebook.PageFactory;
import com.zskx.pemsystem.ebook.PageWidget;
import com.zskx.pemsystem.util.ImageLoader;
import com.zskx.pemsystem.util.SDCardUtil;
import com.zskx.pemsystem.util.ShowNotification;

/**
 * 杂志显示界面
 * 
 * @author demo
 * 
 */
public class MagazineShowActivity extends BaseActivity {
	private static String TAG = "MagazineShowActivity";

	public Bitmap currentPage;

	public Bitmap otherPage;

	public Bitmap backGround;

	private Canvas factoryPageCanvas;

	private PageWidget pageWidget;

	private PageFactory pageFactory;

	private DisplayMetrics dm;

	private AlertDialog DirectoryDia;

	private AlertDialog GoToDia; 

	private AlertDialog FontSizeDia;

	private String title = "";

	private String summary = "";

	@Override
	public void onLowMemory() {
		Debug.MemoryInfo mi = new Debug.MemoryInfo();
		Debug.getMemoryInfo(mi);
		Log.w("Memeroy", mi.toString());
		super.onLowMemory();
	}

	private boolean requestBitmapAlloction(DisplayMetrics dm) {
		try {
			BitmapFactory.Options op = new BitmapFactory.Options();
			op.inPreferredConfig = Config.ALPHA_8;
//			op.inSampleSize = 2;
			op.inPurgeable = true;
			op.inInputShareable  = true;
			if (currentPage == null){
//				currentPage = BitmapFactory.decodeFile(SDCardUtil.PEMSys
//						+ MyApp.currentPage, op);
				currentPage = Bitmap.createBitmap(dm.widthPixels, dm.heightPixels, Config.RGB_565);
			}
			if (otherPage == null)
			{
//				otherPage = BitmapFactory.decodeFile(SDCardUtil.PEMSys
//						+ MyApp.otherPage, op);
				otherPage = Bitmap.createBitmap(dm.widthPixels, dm.heightPixels, Config.RGB_565);
			}
			if (backGround == null) {
 
				backGround = BitmapFactory.decodeStream(this.getResources()
						.openRawResource(R.drawable.read_bg_1), null, op);
				
			}
			return true;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
//			ImageLoader.clearCache();
			return false;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		pageWidget = new PageWidget(this);
		setContentView(pageWidget);

		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		int requestCount = 0;
		while (requestCount < 3 && !requestBitmapAlloction(dm)) {
			requestCount++;
		}
		if (requestCount >= 3) {
			Toast.makeText(this, "没有足够的内存用来查看杂志，无法打开。", Toast.LENGTH_SHORT).show();
			this.setResult(-1);
			this.finish();
			return;
		}

		pageWidget.setScreenSize(dm.widthPixels, dm.heightPixels);

		factoryPageCanvas = new Canvas();
		Intent intent = getIntent();
		String content = intent.getStringExtra("content");
		title = intent.getStringExtra("title");
		summary = intent.getStringExtra("summary");
		Log.i(TAG, "content:" + content);
		if (content != null) {
			MagazineBean mb = new EBookUtil().parserString(content);

			pageFactory = new PageFactory(mb.getContent().toString(),
					mb.getBold_list(), dm.widthPixels, dm.heightPixels);

			float heightScale = (dm.heightPixels*1.0f/backGround.getHeight());
			float widthScale = dm.widthPixels*1.0f/backGround.getWidth();
			pageFactory.setBackGroundScale(heightScale, widthScale);
			pageFactory.setBackGroundImg(backGround);
			initDia(mb); //dialogs
			factoryPageCanvas.setBitmap(currentPage);
			pageFactory.nextPage(factoryPageCanvas);

			factoryPageCanvas.setBitmap(otherPage);
			pageFactory.nextPage(factoryPageCanvas);
			pageFactory.updatePosition();
			// pageFactory.nextPage(otherPageCanvas);
			// pageFactory.nextPage(factoryPageCanvas);
			pageWidget.setBitMap(currentPage, otherPage);

			pageWidget.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (v == pageWidget) {
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							Log.i(TAG, "touch down!");
							pageWidget.abortAnimation();
							pageWidget.calcutionOtherPage(event.getX(),
									event.getY());
							if (pageWidget.isPrePage()) {
								if (pageFactory.isFirstPage()) {
									Toast.makeText(MagazineShowActivity.this,
											"这已经是第一页了！", 1000).show();
									return false;
								}
								pageFactory.prePage(factoryPageCanvas);
							} else {
								if (pageFactory.isLastPage()) {
									Toast.makeText(MagazineShowActivity.this,
											"这已经是最后一页了！", 1000).show();
									return false;
								}
								pageFactory.nextPage(factoryPageCanvas);
							}
						}
						boolean result = pageWidget.doTouchEvent(event);
						if (event.getAction() == MotionEvent.ACTION_UP) {
							Log.i(TAG, "touch up!");

							if (pageWidget.isSuccessFlip()) {
								pageFactory.updatePosition();//update page positon
								changeFactoryBitmapToDraw(); //

							}
						}
						return result;
					}
					return false;
				}
			});

		} else {
			Toast.makeText(this, "没有找到杂志内容，请退出重载", 1000).show();
		}

	}

	@Override
	protected void onResume() {

		ShowNotification.showNotification(this, title, summary,
				this.getClass(), getIntent());

		super.onResume();
	}

	public void changeFactoryBitmapToDraw() {
		Bitmap temp = pageWidget.getCurrentPageBitmap();
		factoryPageCanvas.setBitmap(temp);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(1, 1, 1, "目录");
		menu.add(1, 2, 2, "字体");
		menu.add(1, 3, 3, "跳转");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			DirectoryDia.show();
			break;
		case 2:
			FontSizeDia.show();
			break;
		case 3:
			gotoBar.setProgress((int) (pageFactory.getCurrentPosition() * 1.0
					/ pageFactory.getmResouceLen() * 100));
			gotoTxt.setText(gotoBar.getProgress() + "%");
			GoToDia.show();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	private SeekBar gotoBar;

	private TextView gotoTxt;

	private void initDia(final MagazineBean mb) {
		List<Directory> dir = mb.getDirectory();
		List<String> dirS = new ArrayList<String>();
		for (Directory directory : dir) {
			dirS.add(directory.getName());
		}
		if (dirS.size() == 0) {
			dirS.add("这篇文章，没有目录。");
		}
		String[] dirs = new String[dirS.size()];
		DirectoryDia = new AlertDialog.Builder(this).setItems(
				dirS.toArray(dirs), new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						if (mb.getDirectory().size() == 0) {
							DirectoryDia.dismiss();
							return;
						}

						int position = mb.getDirectory().get(which)
								.getPosition();
						if (position != -1) {
							if (pageWidget.isReverseBitmap()) {
								Log.i(TAG, "目录跳转时 当前页和另一页是反向的！");
								pageWidget.changeBitmapEachOther();
							}
							factoryPageCanvas.setBitmap(pageWidget
									.getOtherPageBitmap());
							pageFactory.gotoPage(mb.getDirectory().get(which)
									.getPosition(), factoryPageCanvas);
							pageWidget.autoFlipToPage();
							pageFactory.updatePosition();
							changeFactoryBitmapToDraw();
						}

					}
				}).create();
		DirectoryDia.setTitle("目录");

		GoToDia = new AlertDialog.Builder(this).create();
		LinearLayout ll = (LinearLayout) View.inflate(this,
				R.layout.magazine_goto, null);
		gotoBar = (SeekBar) ll.findViewById(R.magazineGoto.progress);
		gotoTxt = (TextView) ll.findViewById(R.magazineGoto.progressTxt);

		gotoBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				gotoTxt.setText(progress + "%");
			}
		});
		GoToDia.setView(ll);
		GoToDia.setTitle("跳转");
		GoToDia.setButton("跳转", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				int position = gotoBar.getProgress();
				if (pageWidget.isReverseBitmap()) {
					Log.i(TAG, "目录跳转时 当前页和另一页是反向的！");
					pageWidget.changeBitmapEachOther();
				}
				factoryPageCanvas.setBitmap(pageWidget.getOtherPageBitmap());
				pageFactory.gotoPage(mb.getContent().length() * position / 100
						- 1, factoryPageCanvas);
				pageWidget.autoFlipToPage();
				pageFactory.updatePosition();
				changeFactoryBitmapToDraw();
			}
		});

		final String[] fontList = new String[15];
		for (int i = 0; i < 15; i++) {
			fontList[i] = i + 25 + "";
		}

		FontSizeDia = new AlertDialog.Builder(this).setSingleChoiceItems(
				fontList, 0, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (pageWidget.isReverseBitmap()) {
							Log.i(TAG, "字体改变时 当前页和另一页是反向的！");
							pageWidget.changeBitmapEachOther();
						}
						factoryPageCanvas.setBitmap(pageWidget
								.getCurrentPageBitmap());
						pageFactory.setTextSize(
								Integer.valueOf(fontList[which]),
								factoryPageCanvas);
						pageWidget.showCurrentPage();
						pageFactory.updatePosition();
						factoryPageCanvas.setBitmap(pageWidget
								.getOtherPageBitmap());
						FontSizeDia.dismiss();
					}
				}).create();
		FontSizeDia.setTitle("字体");
	}

	@Override
	protected void onDestroy() {
		// 释放内存
		if (currentPage != null && !currentPage.isRecycled()) {
			currentPage.recycle();
			currentPage = null;
		}
		if (otherPage != null && !otherPage.isRecycled()) {
			otherPage.recycle();
			otherPage = null;
		}
		if (backGround != null && !backGround.isRecycled()) {
			backGround.recycle();
			backGround = null;
		}
		factoryPageCanvas = null;
		pageWidget = null;
		pageFactory = null;
		dm = null;
		DirectoryDia = null;
		GoToDia = null;
		FontSizeDia = null;
		System.gc();
		Runtime.getRuntime().gc();
		super.onDestroy();
		
	}

	// /***
	// * 将 一个bitmap的内容放入另一个bitmap中，不使用引用。
	// *
	// * @param target
	// * @param source
	// */
	// public void copyBitMapFromBitMap(Bitmap target, Bitmap source) {
	//
	// for (int i = 0; i < dm.heightPixels; i++) {
	// for (int j = 0; j < dm.widthPixels; j++) {
	// int color = source.getPixel(j, i);
	// target.setPixel(j, i, color);
	// }
	// }
	// }
}
