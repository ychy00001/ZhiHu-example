package com.rain.zhihu_example.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

/**
 * 自定义日期控件
 *
 * @author yangchunyu
 *         2016/3/21
 *         10:29
 */
@SuppressWarnings("unused")
public class CalendarView extends View {
    //!start 顶部控制条
    private Paint mTopControllerPaint;
    private int mTopMonthControllerClickBg = Color.parseColor("#5DC8FA"); //左右控制块点击背景
    private int mTopMonthLefReiBtnColor = Color.parseColor("#F39121"); //左右控制按钮颜色

    private int mTopMonthControllerMargin = 30;//顶部切换月份的按钮距左右边界的距离
    private int mTopMonthControllerWight = 35;//顶部切换月份的按钮宽度
    private int mTopControllStrokeWidth = 6;
    //!end 顶部控制条

    //!start 顶部星期条
    private int mTopLineColor = Color.parseColor("#CCE4F2"); //上横线颜色
    private int mBottomLineColor = Color.parseColor("#CCE4F2");//下横线颜色
    private int mWeedayColor = Color.parseColor("#1FC2F3");//周一到周五的颜色
    private int mWeekendColor = Color.parseColor("#fa4451");//周六、周日的颜色
    //星期线的宽度
    private int mWeekStrokeWidth = 4;
    //日期间隔
    private int mWeekSize = 14;

    private int mTitleWeekTextHeight;//头部星期字符的高度
    private int mMonthDataHeight;//日期高度

    private Paint mTitleLinePaint;
    private DisplayMetrics mDisplayMetrics;
    private String[] weekString = new String[]{"日", "一", "二", "三", "四", "五", "六"};
    //!start 顶部星期条

    //!--start日历部分
    private static final int NUM_COLUMNS = 7;
    private static final int NUM_ROWS = 6;
    private Paint mCalendarPaint;
    private int mDayColor = Color.parseColor("#000000");//日期显示颜色
    private int mSelectDayColor = Color.parseColor("#ffffff");//选择的日期显示颜色
    private int mSelectBGColor = Color.parseColor("#1FC2F3");//选择的背景显示颜色
    private int mCurrentColor = Color.parseColor("#ff0000");//当前日期显示的颜色
    private int mCurrYear, mCurrMonth, mCurrDay;
    private int mSelYear, mSelMonth, mSelDay;
    private int mColumnSize, mRowSize; //日期的列宽 行高
    private int mDaySize = 18;
    private TextView tv_date, tv_week;
    private int weekRow;
    private int[][] daysString;//二维数组存放显示月日期
    private int mCircleRadius = 6;
    private int mCircleColor = Color.parseColor("#ff0000");
    private DateClick dateClick;
    private List<Integer> daysHasThingList;
    private int mTopControllerHeight;//顶部控制条高度
    //!--end日历部分

    public CalendarView(Context context) {
        super(context);
        init();
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public boolean isInEditMode() {
        return true;
    }

    private void init() {
        //!start--顶部控制条
        mTopControllerPaint = new Paint();
        //!end--顶部控制条

        //!--顶部星期条
        mTitleLinePaint = new Paint();
        mDisplayMetrics = getResources().getDisplayMetrics();
        //定义顶部星期显示的高度 顶部控制条高度 以及 年月日内容显示的高度
        mTitleWeekTextHeight = (int) (mDisplayMetrics.density * 30.0f + 0.5f);
        mTopControllerHeight = (int) (mDisplayMetrics.density * 30.0f + 0.5f);
        mMonthDataHeight = (int) (mDisplayMetrics.density * 200.0f + 0.5f);
        //!--顶部星期条

        //!--日历部分 start
        mCalendarPaint = new Paint();

        mDisplayMetrics = getResources().getDisplayMetrics();
        Calendar calendar = Calendar.getInstance();
        mCurrYear = calendar.get(Calendar.YEAR);
        mCurrMonth = calendar.get(Calendar.MONTH) + 1;
        mCurrDay = calendar.get(Calendar.DATE);
        setSelectYearMonth(mCurrYear, mCurrMonth, mCurrDay);
        //!--日历部分 end
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (heightMode == MeasureSpec.AT_MOST) {//wrap_content
            //头部日期高度
            heightSize = mTitleWeekTextHeight + mMonthDataHeight + mTopControllerHeight;
        }
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = mDisplayMetrics.densityDpi * 300;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        drawTopControllerBar(canvas,width,height);
        //画顶部星期显示
        drawTopWeekStr(canvas, width);
        //接下来画日历显示
        drawCalendar(canvas, width, height);
    }

    /**
     * 画顶部控制条
     */
    private void drawTopControllerBar(Canvas canvas,int width,int height) {
        //画左右两个点击小方块 注意margin值
        float margin = mTopMonthControllerMargin*mDisplayMetrics.density;
        float monthControllerWidth = mTopMonthControllerWight *mDisplayMetrics.density;

        mTopControllerPaint.setColor(mTopMonthControllerClickBg);
        canvas.drawRect(mTopMonthControllerWight+mTopMonthControllerMargin,0,
                monthControllerWidth + margin,
                mTopControllerHeight,mTopControllerPaint);//左方块

        float rightStartX = width - margin -monthControllerWidth;
        canvas.drawRect(rightStartX,0, rightStartX + monthControllerWidth
                ,mTopControllerHeight,mTopControllerPaint);//右方块

        //画两个控制按钮
        mTopControllerPaint.setColor(mTopMonthLefReiBtnColor);
        mTopControllerPaint.setStrokeWidth(mTopControllStrokeWidth);
        //x坐标为margin+方块宽度的 2/3   y坐标为控制条高度的1/6  左边按钮
        PointF leftTopP =  new PointF(margin+(monthControllerWidth * 2.0f/3.0f)
                ,mTopControllerHeight * 1.0f/6.0f);
        PointF leftCenterP = new PointF(margin+(monthControllerWidth * 2.0f/6.0f)
                ,mTopControllerHeight * 1.0f/2.0f);
        PointF leftBottomP = new PointF(margin+(monthControllerWidth * 2.0f/3.0f),
                mTopControllerHeight * 5.0f/6.0f);
        canvas.drawLine(leftTopP.x,leftTopP.y,leftCenterP.x,leftCenterP.y,mTopControllerPaint);
        canvas.drawLine(leftCenterP.x,leftCenterP.y,leftBottomP.x,leftBottomP.y,mTopControllerPaint);

        //右边按钮
        PointF rightTopP =  new PointF(width-margin-(monthControllerWidth * 2.0f/3.0f)
                ,mTopControllerHeight * 1.0f/6.0f);
        PointF rightCenterP = new PointF(width-margin-(monthControllerWidth * 2.0f/6.0f)
                ,mTopControllerHeight * 1.0f/2.0f);
        PointF rightBottomP = new PointF(width-margin-(monthControllerWidth * 2.0f/3.0f),
                mTopControllerHeight * 5.0f/6.0f);
        canvas.drawLine(rightTopP.x,rightTopP.y,rightCenterP.x,rightCenterP.y,mTopControllerPaint);
        canvas.drawLine(rightCenterP.x,rightCenterP.y,rightBottomP.x,rightBottomP.y,mTopControllerPaint);

    }

    /**
     * 绘制顶部日历
     */
    private void drawTopDateMsg(Canvas canvas,int width,int height,String text){
        //TODO 等待后续添加
    }

    /**
     * 画日历
     */
    private void drawCalendar(Canvas canvas, int width, int height) {
        initMothDataSize(width);//初始化每列宽度 和每行高度
        daysString = new int[6][7];
        mCalendarPaint.setTextSize(mDaySize * mDisplayMetrics.scaledDensity);
        String dayString;
        int mMonthDays = getMonthDays(mSelYear, mSelMonth);//选择的月有几天
        int weekNumber = getFirstDayWeek(mSelYear, mSelMonth);//第一天是星期几
        Log.d("DateView", "DateView:" + mSelMonth + "月1号周" + weekNumber);
        for (int day = 0; day < mMonthDays; day++) {//从第0天开始
            dayString = (day + 1) + "";
            int column = (day + weekNumber - 1) % 7;//第几列
            int row = (day + weekNumber - 1) / 7; //第几行
            daysString[row][column] = day + 1;
            int startX = (int) (mColumnSize * column + (mColumnSize - mCalendarPaint.measureText(dayString)) / 2);
            int startY = (int) (mRowSize * row + mRowSize / 2 - (mCalendarPaint.ascent() + mCalendarPaint.descent()) / 2) + mTitleWeekTextHeight + mTopControllerHeight;
            if (dayString.equals(mSelDay + "")) {
                //绘制背景色矩形
                int startRecX = mColumnSize * column;
                int startRecY = mRowSize * row + mTitleWeekTextHeight + mTopControllerHeight;
                int endRecX = startRecX + mColumnSize;
                int endRecY = startRecY + mRowSize;
                mCalendarPaint.setColor(mSelectBGColor);
                canvas.drawRect(startRecX, startRecY, endRecX, endRecY, mCalendarPaint);
                //记录第几行，即第几周
                weekRow = row + 1;
            }
            //绘制事务圆形标志
            drawCircle(row, column, day + 1, canvas);
            if (dayString.equals(mSelDay + "")) {
                mCalendarPaint.setColor(mSelectDayColor);
            } else if (dayString.equals(mCurrDay + "") && mCurrDay != mSelDay && mCurrMonth == mSelMonth) {
                //正常月，选中其他日期，则今日为红色
                mCalendarPaint.setColor(mCurrentColor);
            } else {
                mCalendarPaint.setColor(mDayColor);
            }
            canvas.drawText(dayString, startX, startY, mCalendarPaint);
            if (tv_date != null) {
                tv_date.setText(mSelYear + "年" + (mSelMonth + 1) + "月");
            }
            if (tv_week != null) {
                tv_week.setText("第" + weekRow + "周");
            }
        }
    }

    /**
     * 画圆形事物 标记
     */
    private void drawCircle(int row, int column, int day, Canvas canvas) {
        if (daysHasThingList != null && daysHasThingList.size() > 0) {
            if (!daysHasThingList.contains(day)) return;
            mCalendarPaint.setColor(mCircleColor);
            float circleX = (float) (mColumnSize * column + mColumnSize * 0.8);
            float circley = (float) (mRowSize * row + mRowSize * 0.2) + mTitleWeekTextHeight + mTopControllerHeight;
            canvas.drawCircle(circleX, circley, mCircleRadius, mCalendarPaint);
        }
    }

    /**
     * 初始化日期的宽高
     *
     * @param width 整体控件宽度
     */
    private void initMothDataSize(int width) {
        mColumnSize = width / NUM_COLUMNS;
        mRowSize = mMonthDataHeight / NUM_ROWS;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private int downX = 0, downY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventCode = event.getAction();
        switch (eventCode) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                if (Math.abs(upX - downX) < 10 && Math.abs(upY - downY) < 10) {//点击事件
                    performClick();
                    doClickAction((upX + downX) / 2, (upY + downY) / 2);
                }
                break;
        }
        return true;
    }

    /**
     * 画顶部 星期的显示部分
     */
    @SuppressWarnings("SuspiciousNameCombination")
    private void drawTopWeekStr(Canvas canvas, int width) {//进行画上下线
        mTitleLinePaint.setStyle(Paint.Style.STROKE);
        mTitleLinePaint.setColor(mTopLineColor);
        mTitleLinePaint.setStrokeWidth(mWeekStrokeWidth);
        canvas.drawLine(0, mWeekStrokeWidth + mTopControllerHeight, width, mWeekStrokeWidth + mTopControllerHeight, mTitleLinePaint);

        //画下横线
        mTitleLinePaint.setColor(mBottomLineColor);
        canvas.drawLine(0, mTitleWeekTextHeight + mTopControllerHeight, width, mTitleWeekTextHeight + mTopControllerHeight, mTitleLinePaint);

        mTitleLinePaint.setStyle(Paint.Style.FILL);
        mTitleLinePaint.setTextSize(mWeekSize * mDisplayMetrics.scaledDensity);
        int columnWidth = width / 7;
        for (int i = 0; i < weekString.length; i++) {
            String text = weekString[i];
            //获取字体的实例宽度
            int fontWidth = (int) mTitleLinePaint.measureText(text);
            //计算左上点坐标
            int startX = columnWidth * i + (columnWidth - fontWidth) / 2;
            int startY = (int) (mTitleWeekTextHeight / 2 - (mTitleLinePaint.ascent() + mTitleLinePaint.descent()) / 2) + mTopControllerHeight;
            if (text.contains("日") || text.contains("六")) {
                mTitleLinePaint.setColor(mWeekendColor);
            } else {
                mTitleLinePaint.setColor(mWeedayColor);
            }
            canvas.drawText(text, startX, startY, mTitleLinePaint);
        }
    }


    /**
     * 执行点击事件
     */
    private void doClickAction(int x, int y) {
        int row = y / mRowSize;
        int column = x / mColumnSize;
        setSelectYearMonth(mSelYear, mSelMonth, daysString[row][column]);
        invalidate();
        //执行activity发送过来的点击处理事件
        if (dateClick != null) {
            dateClick.onClickOnDate();
        }
    }

    /**
     * 左点击，日历向后翻页
     */
    public void onLeftClick() {
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDay;
        if (month == 0) {//若果是1月份，则变成12月份
            year = mSelYear - 1;
            month = 11;
        } else if (getMonthDays(year, month) == day) {
            //如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            month = month - 1;
            day = getMonthDays(year, month);
        } else {
            month = month - 1;
        }
        setSelectYearMonth(year, month, day);
        invalidate();
    }

    /**
     * 右点击，日历向前翻页
     */
    public void onRightClick() {
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDay;
        if (month == 11) {//若果是12月份，则变成1月份
            year = mSelYear + 1;
            month = 0;
        } else if (getMonthDays(year, month) == day) {
            //如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            month = month + 1;
            day = getMonthDays(year, month);
        } else {
            month = month + 1;
        }
        setSelectYearMonth(year, month, day);
        invalidate();
    }

    /**
     * 获取选择的年份
     */
    public int getmSelYear() {
        return mSelYear;
    }

    /**
     * 获取选择的月份
     */
    public int getmSelMonth() {
        return mSelMonth;
    }

    /**
     * 获取选择的日期
     */
    public int getmSelDay() {
        return this.mSelDay;
    }

    /**
     * 普通日期的字体颜色，默认黑色
     */
    public void setmDayColor(int mDayColor) {
        this.mDayColor = mDayColor;
    }

    /**
     * 选择日期的颜色，默认为白色
     */
    public void setmSelectDayColor(int mSelectDayColor) {
        this.mSelectDayColor = mSelectDayColor;
    }

    /**
     * 选中日期的背景颜色，默认蓝色
     */
    public void setmSelectBGColor(int mSelectBGColor) {
        this.mSelectBGColor = mSelectBGColor;
    }

    /**
     * 当前日期不是选中的颜色，默认红色
     */
    public void setmCurrentColor(int mCurrentColor) {
        this.mCurrentColor = mCurrentColor;
    }

    /**
     * 日期的大小，默认18sp
     */
    public void setmDaySize(int mDaySize) {
        this.mDaySize = mDaySize;
    }

    /**
     * 设置显示当前日期的控件
     *
     * @param tv_date 显示日期
     * @param tv_week 显示周
     */
    public void setTextView(TextView tv_date, TextView tv_week) {
        this.tv_date = tv_date;
        this.tv_week = tv_week;
        invalidate();
    }

    /**
     * 设置事务天数
     */
    public void setDaysHasThingList(List<Integer> daysHasThingList) {
        this.daysHasThingList = daysHasThingList;
    }

    /***
     * 设置圆圈的半径，默认为6
     */
    public void setmCircleRadius(int mCircleRadius) {
        this.mCircleRadius = mCircleRadius;
    }

    /**
     * 设置圆圈的半径
     */
    public void setmCircleColor(int mCircleColor) {
        this.mCircleColor = mCircleColor;
    }

    /**
     * 跳转至今天
     */
    public void setTodayToView() {
        setSelectYearMonth(mCurrYear, mCurrMonth, mCurrDay);
        invalidate();
    }


    /**
     * 设置顶线的颜色
     */
    public void setmTopLineColor(int mTopLineColor) {
        this.mTopLineColor = mTopLineColor;
    }

    /**
     * 设置底线的颜色
     */
    public void setmBottomLineColor(int mBottomLineColor) {
        this.mBottomLineColor = mBottomLineColor;
    }

    /**
     * 设置周一-五的颜色
     */
    public void setmWeedayColor(int mWeedayColor) {
        this.mWeedayColor = mWeedayColor;
    }

    /**
     * 设置周六、周日的颜色
     */
    public void setmWeekendColor(int mWeekendColor) {
        this.mWeekendColor = mWeekendColor;
    }

    /**
     * 设置边线的宽度
     */
    public void setmWeekStrokeWidth(int mStrokeWidth) {
        this.mWeekStrokeWidth = mStrokeWidth;
    }

    /**
     * 设置字体的大小
     */
    public void setmWeekSize(int mWeekSize) {
        this.mWeekSize = mWeekSize;
    }

    /**
     * 设置星期的形式
     * 默认值  "日","一","二","三","四","五","六"
     */
    public void setWeekString(String[] weekString) {
        this.weekString = weekString;
    }

    /**********************日历部分设置*****************************/
    /**
     * 设置年月
     */
    private void setSelectYearMonth(int year, int month, int day) {
        mSelYear = year;
        mSelMonth = month;
        mSelDay = day;
    }

    /**
     * 设置日期的点击回调事件
     */
    public interface DateClick {
        void onClickOnDate();
    }

    /**
     * 设置日期点击事件
     */
    public void setDateClick(DateClick dateClick) {
        this.dateClick = dateClick;
    }

    /***************** 计算日期 ********************/
    //返回当前日期
    private int getMonthDays(int year, int month) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MONTH, month);
        instance.add(Calendar.YEAR, year);
        return instance.getMaximum(Calendar.DATE);
    }

    //返回当月第一天的星期
    private int getFirstDayWeek(int year, int month) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MONTH, month);
        instance.add(Calendar.YEAR, year);
        return instance.get(Calendar.DAY_OF_WEEK);
    }
}
