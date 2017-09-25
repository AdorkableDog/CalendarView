package com.example.yulu.calendarview.DatePicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.support.v4.view.NestedScrollingChild;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;


import com.example.yulu.calendarview.Utils.DensityUtils;

import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by lenovo on 2017/5/9.
 */

public class DPMonthView extends View implements NestedScrollingChild {

	private static final String TAG = "DPMonthView";
	private final Region[][] MONTH_REGIONS_4 = new Region[4][7];
	private final Region[][] MONTH_REGIONS_5 = new Region[5][7];
	private final Region[][] MONTH_REGIONS_6 = new Region[6][7];
	private final Region[][] MONTH_REGIONS_1 = new Region[1][7];
	private final DPCellInfo[][] INFO_4 = new DPCellInfo[4][7];
	private final DPCellInfo[][] INFO_5 = new DPCellInfo[5][7];
	private final DPCellInfo[][] INFO_6 = new DPCellInfo[6][7];
	private final DPCellInfo[][] INFO_1 = new DPCellInfo[1][7];
	protected Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG |
			Paint.LINEAR_TEXT_FLAG);

	private Scroller mScroller;
	private DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();
	private AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();
	private DPDimen mDPDimen;
	private DPTheme mDPTheme;
	private int mCellWidth;
	private int mCellHeight;
	private DPLanguage mDPLanguage;
	private int measureWidth;
	private int measuredHeight;
	private int mLiveRadius;
	private int mPaddingBottom;
	private float mTxtRadius;
	private DPManager mDPManager;
	private DPCellInfo mSelectedInfo;
	private int mCurrentYear;
	private int mCurrentMonth;
	private boolean isNewEvent;
	private int downPointX;
	private int downPointY;
	private boolean isHorizontalMove;
	private float mTouchSlop;
	private int mMinMoveWidth;
	private int mLeftMonth;
	private int mLeftYear;
	private int mRightMonth;
	private int mRightYear;
	private boolean mChangePage;
	private Dicretion mCurrentDirection;
	private int mPageIndex;
	private int mLastMoveX;

	private CalendarType mCalendarType = CalendarType.TYPE_CLOSE;
	private OnDatePickerClickListener onDatePickerClickListener;
	private int mSelectedLine = -1;
	private boolean isFirstDraw = true;

	public enum CalendarType {
		TYPE_OPEN,
		TYPE_CLOSE
	}

	public DPMonthView(Context context) {
		super(context);
		init();
	}

	public DPMonthView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DPMonthView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		mScroller = new Scroller(getContext());
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
		mPaint.setTextAlign(Paint.Align.CENTER);
		mDPDimen = new DPDimen(getContext());
		mDPTheme = new DPTheme(getContext());
		mDPLanguage = new DPLanguage();
		mDPManager = DPManager.getInstance();
		mLiveRadius = DensityUtils.dp2px(getContext(), 3);
		mPaddingBottom = DensityUtils.dp2px(getContext(), 12);
		mTxtRadius = DensityUtils.dp2px(getContext(), 18);
		mCurrentYear = Calendar.getInstance().get(Calendar.YEAR);
		mCurrentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
		mPaint.setTextSize(mDPDimen.getTitleTxtSize());
		if (mCalendarType == CalendarType.TYPE_CLOSE) {
			DPCellInfo[][] info = mDPManager.obtainDPInfo(mCurrentYear, mCurrentMonth);
			for (int i = 0; i < info.length; i++) {
				for (int j = 0; j < info[i].length; j++) {
					if (info[i][j].isToday) {
						mSelectedLine = i;
						break;
					}

				}
			}
		}
		Log.i(TAG, "mSelectedLine->" + mSelectedLine);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (mCalendarType == CalendarType.TYPE_OPEN) {
			/**
			 * 当前宽度
			 */
			measureWidth = MeasureSpec.getSize(widthMeasureSpec);
			/**高度瓜分为title和content区域*/
			mCellWidth = (int) (measureWidth * 1f / 7);
			mCellHeight = mCellWidth;
			measuredHeight = (int) (measureWidth * 6F / 7F);

			Log.i(TAG, "measureWidth:" + measureWidth + ",measuredHeight:" + measuredHeight);
			setMeasuredDimension(measureWidth, measuredHeight);
		} else {
			/**
			 * 当前宽度
			 */
			measureWidth = MeasureSpec.getSize(widthMeasureSpec);
			/**高度瓜分为title和content区域*/
			mCellWidth = (int) (measureWidth * 1f / 7);
			mCellHeight = mCellWidth;
			measuredHeight = (int) (measureWidth * 1F / 7F);
			Log.i(TAG, "measureWidth:" + measureWidth + ",measuredHeight:" + measuredHeight);
			setMeasuredDimension(measureWidth, measuredHeight);
		}

	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mCalendarType == CalendarType.TYPE_OPEN) {
			drawTitle(canvas);
			drawContent(canvas);
		} else if (mCalendarType == CalendarType.TYPE_CLOSE) {
			drawTitle(canvas);
			drawCloseContent(canvas);
		}

	}

	private void drawCloseContent(Canvas canvas) {
		if ((mCurrentMonth - 1) % 12 == 0) {
			mLeftMonth = 12;
			mLeftYear = mCurrentYear - 1;
		} else if ((mCurrentMonth + 1) % 13 == 0) {
			mRightMonth = 1;
			mRightYear = mCurrentYear + 1;
		} else {
			mLeftMonth = mCurrentMonth - 1;
			mRightMonth = mCurrentMonth + 1;
			mLeftYear = mRightYear = mCurrentYear;
		}
		Log.i(TAG, "pageIndex:" + mPageIndex);
		//drawCloseContent(canvas, measureWidth * (mPageIndex - 1), 0, mLeftYear, mLeftMonth);
		drawCloseContent(canvas, measureWidth * mPageIndex, 0, mCurrentYear, mCurrentMonth);
		//drawCloseContent(canvas, measureWidth * (mPageIndex + 1), 0, mRightYear, mRightMonth);
	}

	private void drawTitle(Canvas canvas) {

	}

	private void drawContent(Canvas canvas) {
		if ((mCurrentMonth - 1) % 12 == 0) {
			mLeftMonth = 12;
			mLeftYear = mCurrentYear - 1;
		} else if ((mCurrentMonth + 1) % 13 == 0) {
			mRightMonth = 1;
			mRightYear = mCurrentYear + 1;
		} else {
			mLeftMonth = mCurrentMonth - 1;
			mRightMonth = mCurrentMonth + 1;
			mLeftYear = mRightYear = mCurrentYear;
		}
		Log.i(TAG, "pageIndex:" + mPageIndex);
		drawContent(canvas, measureWidth * (mPageIndex - 1), 0, mLeftYear, mLeftMonth);
		drawContent(canvas, measureWidth * mPageIndex, 0, mCurrentYear, mCurrentMonth);
		drawContent(canvas, measureWidth * (mPageIndex + 1), 0, mRightYear, mRightMonth);

	}

	private void drawContentBG(Canvas canvas, int x, int y) {
		mPaint.setColor(mDPTheme.getContentBG());
		canvas.drawRect(x, y, x + measureWidth, y + measuredHeight, mPaint);
	}

	Rect bounds = new Rect();

	private void arrayClear(DPCellInfo[][] info) {
		for (DPCellInfo[] anInfo : info) {
			Arrays.fill(anInfo, null);
		}
	}

	private DPCellInfo[][] arrayCopy(DPCellInfo[][] src, DPCellInfo[][] dst) {
		for (int i = 0; i < dst.length; i++) {
			System.arraycopy(src[i], 0, dst[i], 0, dst[i].length);
		}
		return dst;
	}

	private void drawCloseContent(Canvas canvas, int x, int y, int year, int month) {
		Log.i(TAG, "x:" + x + ",y:" + y + ",year:" + year + ",month:" + month);
		canvas.save();
		canvas.translate(x, y);
		drawContentBG(canvas, 0, 0);
		DPCellInfo[][] info = mDPManager.obtainDPInfo(year, month);
		DPCellInfo[][] result;
		Region[][] tmp = MONTH_REGIONS_1;
		arrayClear(INFO_1);
		Log.i(TAG, "mSelectedLine:" + mSelectedLine);
		System.arraycopy(info[mSelectedLine], 0, INFO_1[0], 0, INFO_1[0].length);

		for (int i = 0; i < INFO_1.length; i++) {
			for (int j = 0; j < INFO_1[i].length; j++) {
				draw(canvas, tmp[i][j].getBounds(), info[mSelectedLine][j], mSelectedLine);
			}
		}

		canvas.restore();

	}

	private void drawContent(Canvas canvas, int x, int y, int year, int month) {
		Log.i(TAG, "x:" + x + ",y:" + y + ",year:" + year + ",month:" + month);
		canvas.save();
		canvas.translate(x, y);
		drawContentBG(canvas, 0, 0);
		DPCellInfo[][] info = mDPManager.obtainDPInfo(year, month);
		DPCellInfo[][] result;
		Region[][] tmp;
		if (TextUtils.isEmpty(info[4][0].strG)) {
			tmp = MONTH_REGIONS_4;
			arrayClear(INFO_4);
			result = arrayCopy(info, INFO_4);
		} else if (TextUtils.isEmpty(info[5][0].strG)) {
			tmp = MONTH_REGIONS_5;
			arrayClear(INFO_5);
			result = arrayCopy(info, INFO_5);
		} else {
			tmp = MONTH_REGIONS_6;
			arrayClear(INFO_6);
			result = arrayCopy(info, INFO_6);
		}
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[i].length; j++) {
				draw(canvas, tmp[i][j].getBounds(), info[i][j], i);
			}
		}

		canvas.restore();

	}

	private void draw(Canvas canvas, Rect rect, DPCellInfo info, int line) {
		drawDecor(canvas, rect, info, line);
		drawDay(canvas, rect, info);
	}

	public void openCalendar() {
		mCalendarType = CalendarType.TYPE_OPEN;
		requestLayout();

	}

	public void closeCalendar() {
		mCalendarType = CalendarType.TYPE_CLOSE;
		if (mSelectedLine == -1) {
			DPCellInfo[][] info = mDPManager.obtainDPInfo(mCurrentYear, mCurrentMonth);

			for (int i = 0; i < info.length; i++) {
				for (int j = 0; j < info[i].length; j++) {
					if (info[i][j].isSelected) {
						mSelectedLine = i;
						break;
					}

				}
			}
		}
		requestLayout();

    }

	private void drawDecor(Canvas canvas, Rect rect, DPCellInfo info, int line) {
		if (info.isSelected) {
			mPaint.setColor(mDPTheme.getSelectedBG());
			canvas.drawCircle(rect.centerX(), rect.centerY(), mTxtRadius, mPaint);
		} else if (info.isToday) {
			if (isFirstDraw) {
				isFirstDraw = false;
				mSelectedLine = line;
			}
			mPaint.setColor(mDPTheme.getTodayBG());
			canvas.drawCircle(rect.centerX(), rect.centerY(), mTxtRadius, mPaint);
		}
//		else if (info.isLive) {
//			mPaint.setColor(mDPTheme.getLiveBG());
//			canvas.drawCircle(rect.centerX(), rect.centerY() + (mPaint.descent() - mPaint.ascent()) / 2 + mPaddingBottom, mLiveRadius, mPaint);
//		}

		 if (!TextUtils.isEmpty(info.isPic)) {
			mPaint.setColor(mDPTheme.getTickTextColor());
			float height = (mPaint.descent() - mPaint.ascent()) / 2;
			mPaint.setTextSize(mDPDimen.getPicTextSize());
//			canvas.drawText(info.isPic, rect.centerX(), rect.centerY() + (mPaint.descent() - mPaint.ascent()) / 2 + mPaddingBottom, mPaint);
//			canvas.drawText(info.isPic, rect.centerX(), rect.centerY() + Math.abs(mPaint.ascent()) + mPaddingBottom, mPaint);
			canvas.drawText(info.isPic,
					rect.centerX(),
					rect.centerY() + height +
							mPaddingBottom +
							Math.abs(mPaint.ascent()) - (mPaint.descent() - mPaint.ascent()) / 2F,
					mPaint);
		}
	}

	private void drawDay(Canvas canvas, Rect rect, DPCellInfo info) {
		bounds.setEmpty();
		mPaint.setTextSize(mDPDimen.getTitleTxtSize());
		if (info.isSelected) {
			mPaint.setColor(mDPTheme.getSelectedTxtColor());
		} else if (info.isToday) {
			mPaint.setColor(mDPTheme.getTodayTxtColor());
		} else {
			mPaint.setColor(mDPTheme.getCommonTxtColor());

		}
		float y = rect.centerY() + Math.abs(mPaint.ascent()) - (mPaint.descent() - mPaint.ascent()) / 2F;
		canvas.drawText(info.strG, rect.centerX(), y, mPaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mScroller.forceFinished(true);
				isNewEvent = true;
				mChangePage = false;
				isHorizontalMove = false;
				downPointX = (int) event.getX();
				downPointY = (int) event.getY();

				break;
			case MotionEvent.ACTION_MOVE:
				if (mCalendarType == CalendarType.TYPE_OPEN) {
					if (isNewEvent) {
						if (Math.abs(downPointX - event.getX()) > mTouchSlop) {
							isHorizontalMove = true;
							isNewEvent = false;
						}
					}
					if (isHorizontalMove) {
						int totalMoveX = (int) (downPointX - event.getX() + mLastMoveX);
						smoothScrollTo(totalMoveX, 0);
					}
				}

				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				if (isHorizontalMove) {
					if (downPointX > event.getX() &&
							Math.abs(downPointX - event.getX()) >= mMinMoveWidth) {
						Log.i(TAG, "右滑");
						mCurrentMonth++;
						if (mCurrentMonth % 13 == 0) {
							mCurrentMonth = 1;
							mCurrentYear++;
						}
						smoothScrollTo(Dicretion.RIGHT);
					} else if (downPointX < event.getX() &&
							Math.abs(downPointX - event.getX()) >= mMinMoveWidth) {
						Log.i(TAG, "左滑");
						mCurrentMonth--;
						if (mCurrentMonth % 12 == 0) {
							mCurrentMonth = 12;
							mCurrentYear--;
						}
						smoothScrollTo(Dicretion.LEFT);
					} else {
						smoothScrollTo(measureWidth * mPageIndex, 0);
					}
					//smoothScrollTo(measureWidth, 0);

					mLastMoveX = measureWidth * mPageIndex;
				} else {

					if (Math.abs(downPointX - event.getX()) < 20 && Math.abs(downPointY - event.getY()) < 20)
						defineRegion((int) event.getX(), (int) event.getY());
				}

				break;

		}
		return true;
	}

	enum Dicretion {
		LEFT,
		RIGHT
	}

	private void smoothScrollTo(int fx, int fy) {
		int dx = fx - mScroller.getFinalX();
		int dy = fy - mScroller.getFinalY();
		smoothScrollBy(dx, dy);
		Log.i(TAG, "PageIndex:" + mPageIndex);
	}

	private void smoothScrollTo(Dicretion dicretion) {

		if (dicretion == Dicretion.LEFT) {
			mPageIndex--;

			smoothScrollTo(measureWidth * mPageIndex, 0);
			mChangePage = true;
		} else if (dicretion == Dicretion.RIGHT) {
			mPageIndex++;
			smoothScrollTo(measureWidth * mPageIndex, 0);
			mChangePage = true;
		}

		mCurrentDirection = dicretion;

	}

	private void smoothScrollBy(int dx, int dy) {
		mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy, 500);
		invalidate();
	}

	private void defineRegion(int x, int y) {
		Log.i(TAG, "defineRegion...");
		if (mCalendarType == CalendarType.TYPE_OPEN) {
			DPCellInfo[][] info = mDPManager.obtainDPInfo(mCurrentYear, mCurrentMonth);
			Region[][] tmp;
			if (TextUtils.isEmpty(info[4][0].strG)) {
				tmp = MONTH_REGIONS_4;
			} else if (TextUtils.isEmpty(info[5][0].strG)) {
				tmp = MONTH_REGIONS_5;
			} else {
				tmp = MONTH_REGIONS_6;
			}

			for (int i = 0; i < tmp.length; i++) {
				for (int j = 0; j < tmp[i].length; j++) {
					Region region = tmp[i][j];
					if (TextUtils.isEmpty(mDPManager.obtainDPInfo(mCurrentYear, mCurrentMonth)[i][j].strG)) {
						continue;
					}

					if (region.contains(x, y)) {
						if (mSelectedInfo != null)
							mSelectedInfo.isSelected = false;
						info[i][j].isSelected = true;
						mSelectedLine = i;
						mSelectedInfo = info[i][j];
						if (onDatePickerClickListener != null) {
							onDatePickerClickListener.onDatePick(String.format("%04d", mCurrentYear)
									+ "-" + String.format("%02d", mCurrentMonth)
									+ "-" + String.format("%02d", Integer.valueOf(info[i][j].strG)));
						}
						Log.i(TAG, "当前日期:" + String.format("%04d", mCurrentYear) + "-"
								+ String.format("%02d", mCurrentMonth)
								+ "-" + String.format("%02d", Integer.valueOf(info[i][j].strG)));
					}
				}
			}
		} else {
			DPCellInfo[][] info = mDPManager.obtainDPInfo(mCurrentYear, mCurrentMonth);
			Region[][] tmp = MONTH_REGIONS_1;
			for (int i = 0; i < tmp[0].length; i++) {

				Region region = tmp[0][i];
				if (TextUtils.isEmpty(info[mSelectedLine][i].strG)) {
					continue;
				}

				if (region.contains(x, y)) {
					if (mSelectedInfo != null)
						mSelectedInfo.isSelected = false;
					info[mSelectedLine][i].isSelected = true;
					mSelectedInfo = info[mSelectedLine][i];
					if (onDatePickerClickListener != null) {
						onDatePickerClickListener.onDatePick(String.format("%04d", mCurrentYear)
								+ "-" + String.format("%02d", mCurrentMonth)
								+ "-" + String.format("%02d", Integer.valueOf(info[mSelectedLine][i].strG)));
					}
					Log.i(TAG, "当前日期:" + String.format("%04d", mCurrentYear) + "-"
							+ String.format("%02d", mCurrentMonth)
							+ "-" + String.format("%02d", Integer.valueOf(info[mSelectedLine][i].strG)));
				}

			}

		}

		invalidate();
	}

	public interface OnDatePickerClickListener {
		/**
		 * @param date 参数日期形式：yyyy-MM-dd
		 */
		void onDatePick(String date);

		/**
		 * @param year
		 * @param month
		 */
		void onMonthChanged(int year, int month);
	}

	public void setOnDatePickClickListener(OnDatePickerClickListener onDatePickerClickListener) {
		this.onDatePickerClickListener = onDatePickerClickListener;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (mCalendarType == CalendarType.TYPE_OPEN) {
			int cellW = (int) (w / 7F);
			int cellH4 = (int) (h / 4F);
			int cellH5 = (int) (h / 5F);
			int cellH6 = (int) (h / 6F);
			mMinMoveWidth = (int) (1F / 5F * w);
			for (int i = 0; i < MONTH_REGIONS_4.length; i++) {
				for (int j = 0; j < MONTH_REGIONS_4[i].length; j++) {
					Region region = new Region();
					region.set((j * cellW), (i * cellH4), cellW + (j * cellW),
							cellW + (i * cellH4));
					MONTH_REGIONS_4[i][j] = region;
				}
			}
			for (int i = 0; i < MONTH_REGIONS_5.length; i++) {
				for (int j = 0; j < MONTH_REGIONS_5[i].length; j++) {
					Region region = new Region();
					region.set((j * cellW), (i * cellH5), cellW + (j * cellW),
							cellW + (i * cellH5));
					MONTH_REGIONS_5[i][j] = region;
				}
			}
			for (int i = 0; i < MONTH_REGIONS_6.length; i++) {
				for (int j = 0; j < MONTH_REGIONS_6[i].length; j++) {
					Region region = new Region();
					region.set((j * cellW), (i * cellH6), cellW + (j * cellW),
							cellW + (i * cellH6));
					MONTH_REGIONS_6[i][j] = region;
				}
			}
		} else if (mCalendarType == CalendarType.TYPE_CLOSE) {
			int cellW = (int) (w / 7F);
			int cellH = h;
			mMinMoveWidth = (int) (1F / 5F * w);

			for (int i = 0; i < MONTH_REGIONS_1.length; i++) {
				for (int j = 0; j < MONTH_REGIONS_1[i].length; j++) {
					Region region = new Region();
					region.set((j * cellW), (i * cellH), cellW + (j * cellW),
							cellW + (i * cellH));
					MONTH_REGIONS_1[i][j] = region;
				}
			}
		}

	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			invalidate();
		} else {
			Log.i(TAG, "mScroller.getCurrX():" + mScroller.getCurrX() + ",mScroller.getCurrY()" + mScroller.getCurrY());
			/*if (mChangePage) {
			    if (mCurrentDirection == Dicretion.LEFT) {
                    mCurrentMonth++;
                    if ((mCurrentMonth) % 13 == 0) {
                        mCurrentMonth = 1;
                        mCurrentYear++;
                    }
                } else if (mCurrentDirection == Dicretion.RIGHT) {
                    mCurrentMonth--;
                    if ((mCurrentMonth) % 12 == 0) {
                        mCurrentMonth = 12;
                        mCurrentYear--;
                    }
                }
                mChangePage = false;
                //scrollTo(0, 0);
                invalidate();
            }*/

			if (onDatePickerClickListener != null) {
				onDatePickerClickListener.onMonthChanged(mCurrentYear, mCurrentMonth);

			}
		}
	}
}
