package com.rain.zhihu_example.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期的工具类
 * Created by yangchunyu
 * 2016/1/26 15:33
 */
public class DateUtil {
    /**
     * 转换日期
     * @param originDate 日期参数 例如：20160126
     * @return 转换后的日期
     */
    @SuppressWarnings("deprecation")
    public static String formatDateByString(String originDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date compareDate = null;
        try {
            compareDate = sdf.parse(originDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date nowDate = new Date();
        if(compareDate != null){
            int dayDiff = daysOfTwo(nowDate, compareDate);
            if (dayDiff <= 0)
                return "今日新闻";
            else if (dayDiff == 1)
                return "昨日新闻";
            else if (dayDiff == 2)
                return "前日新闻";
            else
                return new SimpleDateFormat("M月d日 EEEE").format(compareDate);
        }else
            return new SimpleDateFormat("M月d日 EEEE").format(compareDate);
    }

    /**
     * 看两个日期相差几天
     * @param nowDate 当前时间
     * @param compareDateDate 需要格式化的时间
     * @return 返回相差的天数
     */
    private static int daysOfTwo(Date nowDate, Date compareDateDate) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(nowDate);
        int originalDay = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(compareDateDate);
        int compareDay = aCalendar.get(Calendar.DAY_OF_YEAR);
        return originalDay - compareDay;
    }

    /**
     * 获取昨天的日期 格式为 20141210
     * @return 日期
     */
    public static String getYesterday() {
        return getLastDate(null);
    }

    /**
     * 获取今天的日期
     * @return 格式 20131201
     */
    public static String getNowDay(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date nowDate = new Date();
        return sdf.format(nowDate);
    }
    /**
     * 获取前一日的日期 格式为 20141210
     * @param nowData 格式为20141210
     * @return 日期
     */
    public static String getLastDate(String nowData){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date nowDate = new Date();
        if(!TextUtils.isEmpty(nowData)){
            try {
                nowDate = sdf.parse(nowData);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(nowDate);
        calendar.add(Calendar.DATE,-1);//把日期往后增加一天.整数往后推,负数往前移动
        Date yesterDay = calendar.getTime(); //这个时间就是日期往后推一天的结果
        return sdf.format(yesterDay);
    }
}
