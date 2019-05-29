package com.zskx.pemsystem.ebook;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Region.Op;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

public class PageWidget extends View {

	/** 屏幕宽度和高度 **/
	private int screenWidth, screenHeight;

	/*** 当前页面的文字图片像素数据 ***/
	private Bitmap currentPage;
	/** 另外一页的文字图片像素数据 **/
	private Bitmap otherPage;
	/** 触摸点 **/
	private PointF touchPoint;
	// /** 页面的拖拽点 **/
	// private PointF pageDragPoint;
	// /*** 起始点在 X轴上的贝赛尔曲线 起始点 控制点 最高点（即中点因为使用的是二次贝赛尔曲线） 结束点 */
	// private PointF xBezierStartPoint, xBezierControlPoint,
	// xBezierVertexPoint,
	// xBezierEndPoint;
	//
	// /*** 起始点在 Y轴上的贝赛尔曲线 起始点 控制点 最高点（即中点因为使用的是二次贝赛尔曲线） 结束点 */
	// private PointF yBezierStartPoint, yBezierControlPoint,
	// yBezierVertexPoint,
	// yBezierEndPoint;

	private Camera camera;

	/** 画笔 **/
	private Paint mPaint;
	/** 当前页面翻起的区域 的路径 **/
	private Path currentBackAreaPath;
	/** 当前无法显示的区域 的路径 **/
	private Path otherPath;
	/** 是否是前一页 **/
	private boolean isPrePage = false;
	/** 是否使用 贝赛尔曲线实现翻页 **/
	private boolean isBezierFlip = false;
	/** 是否翻页成功 **/
	private boolean isSuccess = false;

	private GradientDrawable currentPageShadowRL;

	private GradientDrawable currentPageShadowLR;

	private GradientDrawable backAreaShadow;

	private GradientDrawable otherPageShadowLR;

	private GradientDrawable otherPageShadowRL;

	private ColorMatrixColorFilter mColorMatrixFilter;

	private Scroller mScroller;

	private boolean isReverse = false;

	public PageWidget(Context context) {
		super(context);

		init();
	}

	/**
	 * 设置屏幕大小
	 * 
	 * @param screenWidth
	 * @param screenHeight
	 */
	public void setScreenSize(int screenWidth, int screenHeight) {
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		touchPoint.x = screenWidth;
		touchPoint.y = screenHeight;
	}

	/**
	 * 初始化
	 */
	private void init() {
		touchPoint = new PointF();

		camera = new Camera();

		// pageDragPoint = new PointF();
		// xBezierStartPoint = new PointF();
		// xBezierControlPoint = new PointF();
		// xBezierVertexPoint = new PointF();
		// xBezierEndPoint = new PointF();
		// yBezierStartPoint = new PointF();
		// yBezierControlPoint = new PointF();
		// yBezierVertexPoint = new PointF();
		// yBezierEndPoint = new PointF();

		ColorMatrix cm = new ColorMatrix();
		float array[] = { 0.55f, 0, 0, 0, 80.0f, 0, 0.55f, 0, 0, 80.0f, 0, 0,
				0.55f, 0, 80.0f, 0, 0, 0, 1.0f, 0 };

		// cm.setSaturation((float) 0.2);
		cm.set(array);
		mColorMatrixFilter = new ColorMatrixColorFilter(cm);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.FILL);

		currentBackAreaPath = new Path();
		otherPath = new Path();

		mScroller = new Scroller(getContext());

		createDrawable();

	}

	private void createDrawable() {

		int currentPagecolors[] = { 0x80111111, 0x111111 };
		currentPageShadowRL = new GradientDrawable(Orientation.RIGHT_LEFT,
				currentPagecolors);
		currentPageShadowRL.setGradientType(GradientDrawable.LINEAR_GRADIENT);
		currentPageShadowLR = new GradientDrawable(Orientation.LEFT_RIGHT,
				currentPagecolors);
		currentPageShadowLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		int backAreacolors[] = { 0x333333, 0xb0333333 };
		backAreaShadow = new GradientDrawable(Orientation.LEFT_RIGHT,
				backAreacolors);
		backAreaShadow.setGradientType(GradientDrawable.LINEAR_GRADIENT);

		int otherPagecolors[] = { 0xff111111, 0x111111 };
		otherPageShadowLR = new GradientDrawable(Orientation.LEFT_RIGHT,
				otherPagecolors);
		otherPageShadowLR.setGradientType(GradientDrawable.LINEAR_GRADIENT);
		otherPageShadowRL = new GradientDrawable(Orientation.RIGHT_LEFT,
				otherPagecolors);
		otherPageShadowRL.setGradientType(GradientDrawable.LINEAR_GRADIENT);
	}

	/**
	 * 设置 两张图片的内存空间
	 * 
	 * @param c
	 * @param o
	 */
	public void setBitMap(Bitmap c, Bitmap o) {
		currentPage = c;
		otherPage = o;
	}

	/****
	 * 翻页动画是否完成
	 * 
	 * @return
	 */
	public void abortAnimation() {
		if (!mScroller.isFinished()) {
			mScroller.abortAnimation();
			touchPoint.x = mScroller.getFinalX();
			touchPoint.y = mScroller.getFinalY();
			invalidate();

		}
	}

	/**
	 * 计算 需要展示的另一个页面是前一页还是后一页
	 * 
	 * @param x
	 * @param y
	 */
	public void calcutionOtherPage(float x, float y) {

		if (x < screenWidth / 2) {
			isPrePage = true;
		} else { // TODO 这儿先只实现 平滑翻页。
			isPrePage = false;
		}

	}

	/***
	 * 是否翻页成功
	 * 
	 * @return
	 */
	public boolean isSuccessFlip() {

		return isSuccess;
	}

	/**
	 * 得到当前页的bitmap引用
	 * 
	 * @return
	 */
	public Bitmap getCurrentPageBitmap() {
		return currentPage;
	}

	/**
	 * 得到另一页的bitmap引用
	 * 
	 * @return
	 */
	public Bitmap getOtherPageBitmap() {
		return otherPage;
	}

	/***
	 * 是否拖拽到前一页
	 * 
	 * @return
	 */
	public boolean isPrePage() {
		return isPrePage;
	}

	public void showCurrentPage() {
		isPrePage = false;
		touchPoint.x = screenWidth;
		invalidate();
	}

	/**
	 * 处理 手势
	 * 
	 * @param e
	 * @return
	 */
	public boolean doTouchEvent(MotionEvent e) {
		touchPoint.x = e.getX();
		touchPoint.y = e.getY();
		switch (e.getAction()) {

		case MotionEvent.ACTION_DOWN:
			if (isReverse) {
				Log.i("PageWidget", "手势点击下时 当前页和另一页是相反的！");
				changeBitmapEachOther();
			}
			break;
		case MotionEvent.ACTION_UP:
			isFlipToOtherPage();
			break;
		}
		this.invalidate();
		return true;

	}

	/**
	 * 翻页成功后更改 当前页和另一页的位置
	 */
	public void changeBitmapEachOther() {
		Bitmap temp = currentPage;
		currentPage = otherPage;
		otherPage = temp;
		// 置换当前页和另一页后，显示内容和逻辑位置相同。
		isReverse = false;
	}

	public void autoFlipToPage() {
		touchPoint.x = screenWidth - 1;
		startNextPageAnimation(1500);
		isSuccess = true;
		isReverse = true;
		isPrePage = false;
		this.invalidate();
	}

	public boolean isReverseBitmap() {
		return isReverse;
	}

	/**
	 * 判断是否翻到下一页
	 * 
	 * @return
	 */
	public boolean isFlipToOtherPage() {
		if (isPrePage) {
			if (touchPoint.x > screenWidth / 2) {
				startPrePageAnimation(1200);
				isSuccess = true;
				isReverse = true;
				return true;
			} else {
				startLeftCurrentPageAnimation(1200);
				isSuccess = false;
				isReverse = false;
				return false;
			}

		} else {
			if (touchPoint.x < screenWidth / 2) {
				startNextPageAnimation(1200);
				isSuccess = true;
				isReverse = true;
				return true;
			} else {
				startRightCurrentPageAnimation(1200);
				isSuccess = false;
				isReverse = false;
				return false;
			}

		}

	}

	@Override
	public void computeScroll() {
		if (!mScroller.isFinished()) {
			if (mScroller.computeScrollOffset()) {
				Log.i("PageWidget", "scroller is scrolling!");
				touchPoint.x = mScroller.getCurrX();
				touchPoint.y = mScroller.getCurrY();
				invalidate();
				return;

			}
		}

	}

	public void startPrePageAnimation(int durationTime) {
		scrollDx = screenWidth;
		startscroll(scrollDx, durationTime);

	}

	public void startLeftCurrentPageAnimation(int durationTime) {
		scrollDx = -(int) (screenWidth + touchPoint.x);
		startscroll(scrollDx, durationTime);

	}

	private void startscroll(int dx, int durationTime) {
		mScroller.startScroll((int) touchPoint.x, (int) touchPoint.y, dx,
				(int) touchPoint.y, durationTime);
	}

	public void startNextPageAnimation(int durationTime) {
		scrollDx = -(int) (screenWidth + touchPoint.x);
		startscroll(scrollDx, durationTime);
	}

	private int scrollDx;

	public void startRightCurrentPageAnimation(int durationTime) {
		scrollDx = (int) (screenWidth);
		startscroll(scrollDx, durationTime);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.clipRect(0, 0, screenWidth, screenHeight);

		Log.i("PageWidget", "touch-x:" + touchPoint.x);

		drawCurrentArea(canvas);
		drawCurrentBackArea(canvas);
		drawOtherArea(canvas);

	}

	public void drawCurrentArea(Canvas canvas) {
		otherPath.reset();

		if (isPrePage) {
			otherPath.moveTo(touchPoint.x, 0);
			otherPath.lineTo(0, 0);
			otherPath.lineTo(0, screenHeight);
			otherPath.lineTo(touchPoint.x, screenHeight);
			otherPath.close();

		} else {
			otherPath.moveTo(touchPoint.x, 0);
			otherPath.lineTo(screenWidth, 0);
			otherPath.lineTo(screenWidth, screenHeight);
			otherPath.lineTo(touchPoint.x, screenHeight);
			otherPath.close();

		}
		canvas.save();
		canvas.clipPath(otherPath, Op.DIFFERENCE);
		canvas.drawBitmap(currentPage, 0, 0, null);
		drawCurrentAreaShadow(canvas);

		canvas.restore();
	}

	public void drawCurrentBackArea(Canvas canvas) {
		currentBackAreaPath.reset();
		float tempX = 0;
		if (isPrePage) {
			tempX = 2 * touchPoint.x - screenWidth;
			currentBackAreaPath.moveTo(tempX, 0);
			currentBackAreaPath.lineTo(touchPoint.x, 0);
			currentBackAreaPath.lineTo(touchPoint.x, screenHeight);
			currentBackAreaPath.lineTo(tempX, screenHeight);
			currentBackAreaPath.close();

		} else {
			tempX = touchPoint.x;
			currentBackAreaPath.moveTo(touchPoint.x, 0);
			currentBackAreaPath.lineTo(touchPoint.x
					+ (screenWidth - touchPoint.x) / 2 - 15, 0);
			currentBackAreaPath.lineTo(touchPoint.x
					+ (screenWidth - touchPoint.x) / 2 - 15, screenHeight);
			currentBackAreaPath.lineTo(touchPoint.x, screenHeight);

			currentBackAreaPath.close();

		}
		canvas.save();
		canvas.clipPath(otherPath);
		canvas.clipPath(currentBackAreaPath, Op.INTERSECT);

		Matrix matrix = new Matrix();

		camera.save();
		camera.rotateY(180);
		camera.getMatrix(matrix);
		camera.restore();

		matrix.preTranslate(-screenWidth / 2, -screenHeight / 2);

		matrix.postTranslate(screenWidth / 2 + tempX, screenHeight / 2);
		mPaint.setColorFilter(null);
		mPaint.setColorFilter(mColorMatrixFilter);

		if (isPrePage) {
			canvas.drawBitmap(otherPage, matrix, mPaint);
		} else {
			canvas.drawBitmap(currentPage, matrix, mPaint);
		}

		mPaint.setColorFilter(null);
		drawCurrentBackAreaShadow(canvas);
		canvas.restore();

	}

	public void drawOtherArea(Canvas canvas) {

		canvas.save();
		canvas.clipPath(otherPath);
		canvas.clipPath(currentBackAreaPath, Op.DIFFERENCE);
		canvas.drawBitmap(otherPage, 0, 0, null);
		drawOtherAreaShadow(canvas);
		canvas.restore();

	}

	public void drawCurrentAreaShadow(Canvas canvas) {
		if (isPrePage) {
			currentPageShadowLR.setBounds(Math.round(touchPoint.x), 0,
					Math.round(touchPoint.x) + 15, screenHeight);
			currentPageShadowLR.draw(canvas);
		} else {
			float temp = screenWidth - touchPoint.x;
			float shadowLeft;
			if (temp < 15) {
				shadowLeft = touchPoint.x - temp;
			} else {
				shadowLeft = touchPoint.x - 15;
			}
			currentPageShadowRL.setBounds((int) shadowLeft, 0,
					Math.round(touchPoint.x), screenHeight);
			currentPageShadowRL.draw(canvas);
		}
	}

	public void drawCurrentBackAreaShadow(Canvas canvas) {
		int tempX = 0;
		if (isPrePage) {
			tempX = Math.round(touchPoint.x);
		} else {
			tempX = Math
					.round((touchPoint.x + (screenWidth - touchPoint.x) / 2 - 15));
		}
		backAreaShadow.setBounds(tempX - 20, 0, tempX, screenHeight);
		backAreaShadow.draw(canvas);

	}

	public void drawOtherAreaShadow(Canvas canvas) {
		if (isPrePage) {
			float tempX = 2 * touchPoint.x - screenWidth;
			float shadowLeft;
			if (tempX > 0) {
				float tempX1 = screenWidth - tempX;
				if (tempX1 < 15) {
					shadowLeft = tempX - tempX1;
				} else {
					shadowLeft = tempX - 15;
				}
				otherPageShadowRL.setBounds((int) shadowLeft, 0,
						Math.round(tempX), screenHeight);
				otherPageShadowRL.draw(canvas);
			} else {
				return;
			}

		} else {
			otherPageShadowLR
					.setBounds((int) (touchPoint.x
							+ (screenWidth - touchPoint.x) / 2 - 15), 0,
							(int) (touchPoint.x + (screenWidth - touchPoint.x)
									/ 2 + 5), screenHeight);
			otherPageShadowLR.draw(canvas);
		}

	}
}
