package com.zskx.pemsystem.ebook;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/**
 * @author demo
 * 
 */
public class PageFactory {

	private static String TAG = "PageFactory";

	/** 加粗字体列表 */
	private LinkedList<Bold> bold_list;
	/** 文字资源 */
	private String resource;
	/** 文字长度 */
	private int mResouceLen = 0;
	/** 当前页面的开始位置 */
	private int mCurrentPageBegin = 0;
	/** 当前页面的结束位置 */
	private int mCurrentPageEnd = 0;
	/** 缓存页面的开始位置 */
	private int mOtherPageBegin = 0;
	/** 缓冲页面的结束位置 */
	private int mOtherPageEnd = 0;

	/** 字符编码 */
	private String mCharSetName = "UTF-8";
	/** 字体颜色 */
	private int textColor = Color.BLACK;
	/** 字体大小 */
	private int textSize = 25;
	/** 每页显示的行数 */
	private int lineCount;
	/** 背景颜色（如果没有背景图片则用这个） */
	private int bgColor = 0xffff9e85;
	/** 背景图片 */
	private Bitmap bgImage;
	/** 文字显示区域与四边的距离 */
	private int paddingTop = 30, paddingLeft = 10, paddingBottom = 25,
			paddingRight = 10;
	/** 屏幕宽度 */
	private int screenWidth;
	/** 屏幕高度 */
	private int screenHeight;
	/** 可用于显示 字体的宽度 */
	private int visableWidth;
	/** 可用于显示 字体的高度 */
	private int visableHeight;
	/** 是否为第一页和最后一页 */
	private boolean isFirstPage = true, isLastPage = false;

	/** 画笔 **/
	private Paint mPaint;

	private Vector<String> mPageTxtLineSet = new Vector<String>();

	public PageFactory(String resource, LinkedList<Bold> bold_list, int w, int h) {

		mCurrentPageBegin = 0;
		mCurrentPageEnd = 0;

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(textSize);
		mPaint.setColor(textColor);

		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(15);
		paint.setColor(textColor);

		this.resource = resource;
		this.mResouceLen = this.resource.length();
		this.bold_list = bold_list;
		this.screenWidth = w;
		this.screenHeight = h;

		Log.i(TAG, "resource:" + resource);

		visableWidth = screenWidth - paddingLeft - paddingRight;
		visableHeight = screenHeight - paddingTop - paddingBottom;

		lineCount = visableHeight / textSize;

		Log.i(TAG, "screenWith:" + w + ",screenHeight:" + h + ",visableWidth:"
				+ visableWidth + ",visableHeight:" + visableHeight
				+ ",lineCount:" + lineCount + "。");
	}

	public void setPageBeginPosition(int position) {
		mCurrentPageBegin = position;
	}

	public void setTextSize(int size, Canvas canvas) {
		this.textSize = size;
		mPaint.setTextSize(size);
		lineCount = visableHeight / textSize;
		updatePageForTxtSizeChange(canvas);
	}

	public void drawLineWithBold(Canvas canvas, int y, String line,
			int startPosition, int endPosition) {

		List<Bold> list = checkBoldInLine(startPosition, endPosition);
		if (list.size() == 0) {
			// 没有加粗字体
			canvas.drawText(line, paddingLeft, y, mPaint);
			return;
		}
		int paintPositon = 0;
		for (Bold bold : list) {
			Log.i(TAG, "line:" + line);
			Log.i(TAG, "bold.getStart_position():" + bold.getStart_position());
			Log.i(TAG, "startPosition:" + startPosition);
			String normalTxt = line.substring(paintPositon,
					Math.max(bold.getStart_position() - startPosition, 0));
			Log.i("PageFactory", "paintPosition:" + paintPositon
					+ ",normalTxt:" + normalTxt);
			canvas.drawText(normalTxt, paddingLeft, y, mPaint);
			paintPositon += Math.max(bold.getStart_position() - startPosition,
					0);
			float txtLength = mPaint.measureText(normalTxt);
			// if (bold.getEnd_position() <= endPosition) {// 只做了一点，以后完善
			mPaint.setFakeBoldText(true);
			int end;
			if (bold.getEnd_position() > endPosition) {
				end = endPosition;
			} else {
				end = bold.getEnd_position();
			}
			canvas.drawText(line.substring(paintPositon, end - startPosition),
					paddingLeft + txtLength, y, mPaint);
			paintPositon = bold.getEnd_position() + 1;
			mPaint.setFakeBoldText(false);
		}
		// }
	}

	public List<Bold> checkBoldInLine(int startPosition, int endPosition) {
		ArrayList<Bold> boldL = new ArrayList<Bold>();
		for (Bold bold : bold_list) {
			if (bold.getStart_position() >= startPosition
					&& bold.getStart_position() <= endPosition) {

				boldL.add(bold);
			}
		}
		return boldL;
	}

	public int getCurrentPosition() {
		return mCurrentPageBegin;
	}

	public int getmResouceLen() {
		return mResouceLen;
	}

	public int getmOtherPageBegin() {
		return mOtherPageBegin;
	}

	public int getLineCount() {
		return lineCount;
	}

	/**
	 * 画出当前页面到画布上
	 * 
	 * @param canvas
	 */
	private void drawPage(Canvas canvas) {

		// /**************** 第一次打开，没有页面 ******************/
		// if (mPageTxtLineSet == null) {
		// mPageTxtLineSet = pageDown(0, false);
		// }

		if (mPageTxtLineSet.size() > 0) {

			if (bgImage != null) {
				canvas.drawBitmap(bgImage, 0, 0, null);
			} else {
				canvas.drawColor(bgColor);
			}
			int y = paddingTop;
			int lineStartPosition = mOtherPageBegin;

			for (String line : mPageTxtLineSet) {
				int length = line.length();
				Log.i(TAG, "lineStartPosition:" + lineStartPosition);
				drawLineWithBold(canvas, y, line, lineStartPosition,
						lineStartPosition + length);
				if (length != 0) {

					lineStartPosition += length;
				} else {
					lineStartPosition += 1;
				}

				y += textSize;

			}
		}
		float percent = 0;

		percent = (float) ((mOtherPageBegin * 1.0) / mResouceLen);
		if (mOtherPageEnd == mResouceLen) {
			percent = 1;
		}
		DecimalFormat df = new DecimalFormat("#0.0");
		String percentString = df.format(percent * 100) + "%";
		int percentWidth = (int) paint.measureText("999.9%") + 1;
		canvas.drawText(percentString, screenWidth - percentWidth - 10,
				screenHeight - 15 - 2, paint);
	}

	private Paint paint;

	/**
	 * 更新 当前页面文字的位置
	 */
	public void updatePosition() {

		mCurrentPageBegin = mOtherPageBegin;
		mCurrentPageEnd = mOtherPageEnd;
	}

	/**
	 * 前一页 的 文字内容
	 */
	private Vector<String> pageUp() {
		Vector<String> lines = new Vector<String>();
		mOtherPageEnd = mOtherPageBegin = mCurrentPageBegin - 1;

		while (lines.size() < lineCount && mOtherPageBegin >= 0) {
			String paragraph = readBackForwardParagraph(mOtherPageBegin);
			Log.i(TAG, "up_paragraph:" + paragraph);
			if (paragraph.length() == 0) {

				lines.add(0, paragraph);
				mOtherPageBegin -= 1;
			} else {
				int i = 0;

				while (paragraph.length() > 0) {
					int size = mPaint.breakText(paragraph, true, visableWidth,
							null);
					lines.add(i, paragraph.substring(0, size));
					Log.i(TAG,
							"up_paragraph substring:"
									+ paragraph.substring(0, size));
					paragraph = paragraph.substring(size);
					Log.i(TAG, "up_paragraph leaved:" + paragraph);
					mOtherPageBegin -= size;
					i++;

				}
				while (lines.size() > lineCount) {
					mOtherPageBegin += lines.get(0).length();
					Log.i(TAG, "remove line:" + lines.get(0));
					lines.remove(0);
				}
				// if (i > 0) {
				// mOtherPageBegin -= 1;
				// }
			}
		}
		if (mOtherPageBegin != 0) {
			mOtherPageBegin += 1;
		}
		return lines;
	}

	private void calculatePageDown(int position, boolean isExternal) {
		if (!isExternal) {
			if (mCurrentPageEnd == 0) {
				mOtherPageEnd = mOtherPageBegin = 0;
			} else {
				mOtherPageEnd = mOtherPageBegin = mCurrentPageEnd + 1;
			}
		} else {
			mOtherPageEnd = mOtherPageBegin = position;
		}

	}

	private Vector<String> pageUpdate() {
		return pageDown(mCurrentPageBegin, true);
	}

	/**
	 * 后一页 的 文字内容
	 */
	private Vector<String> pageDown(int position, boolean isExternal) {

		calculatePageDown(position, isExternal);

		Vector<String> lines = new Vector<String>();
		while (lines.size() < lineCount && mOtherPageEnd < mResouceLen) {
			String paragraph = readForwardParagraph(mOtherPageEnd);
			Log.i(TAG, "down_paragraph:" + paragraph);
			if (paragraph.length() == 0) {
				lines.add(paragraph);
				mOtherPageEnd += 1;
			} else {

				while (paragraph.length() > 0) {
					int size = mPaint.breakText(paragraph, true, visableWidth,
							null);
					lines.add(paragraph.substring(0, size));
					Log.i(TAG,
							"down_paragraph substring:"
									+ paragraph.substring(0, size));
					paragraph = paragraph.substring(size);
					Log.i(TAG, "down_paragraph leaved:" + paragraph);
					mOtherPageEnd += size;
					if ((lines.size() + 1) > lineCount) {
						break;
					}

				}
			}
		}
		if (mOtherPageEnd != mResouceLen)
			mOtherPageEnd -= 1;

		return lines;
	}

	/**
	 * 读取给出位置的前面一段 的内容
	 * 
	 * @param startPosition
	 * @return
	 */
	public String readBackForwardParagraph(int startPosition) {

		StringBuilder line = new StringBuilder();

		int end = startPosition;
		char txt;
		while (!(end < 0) && ((txt = resource.charAt(end)) != '\n')) {
			line.append(txt);
			end--;
		}
		return line.reverse().toString();

	}

	/**
	 * 读取给出位置的后面一段 内容
	 * 
	 * @param startPosition
	 * @return
	 */
	public String readForwardParagraph(int startPosition) {

		StringBuilder line = new StringBuilder();

		int start = startPosition;
		char txt;
		while (!(start >= mResouceLen)
				&& ((txt = resource.charAt(start)) != '\n')) {
			line.append(txt);
			start++;
		}

		return line.toString();

	}

	/**
	 * 前一页
	 */
	public void prePage(Canvas canvas) {

		mPageTxtLineSet.clear();
		mPageTxtLineSet = pageUp();
		drawPage(canvas);
	}

	/**
	 * 后一页
	 */
	public void nextPage(Canvas canvas) {

		mPageTxtLineSet.clear();
		mPageTxtLineSet = pageDown(0, false);
		drawPage(canvas);
	}

	/**
	 * 跳转到指定位置
	 * 
	 * @param position
	 */
	public void gotoPage(int position, Canvas canvas) {
		mPageTxtLineSet.clear();
		mPageTxtLineSet = pageDown(position, true);
		drawPage(canvas);
	}

	public void updatePageForTxtSizeChange(Canvas canvas) {
		mPageTxtLineSet.clear();
		mPageTxtLineSet = pageUpdate();
		drawPage(canvas);
	}

	public void setBackGroundImg(Bitmap b) {
		this.bgImage = b;
	}

	public boolean isFirstPage() {
		if (mCurrentPageBegin == 0) {
			isFirstPage = true;
		} else {
			isFirstPage = false;
		}
		return isFirstPage;
	}

	public boolean isLastPage() {
		if (mCurrentPageEnd == mResouceLen) {
			isLastPage = true;
		} else {
			isLastPage = false;
		}
		return isLastPage;
	}
}
