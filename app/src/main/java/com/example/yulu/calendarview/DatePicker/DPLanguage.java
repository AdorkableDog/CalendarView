package com.example.yulu.calendarview.DatePicker;

/**
 * Created by lenovo on 2017/5/9.
 */

public class DPLanguage {
    /**
     * 月份
     * @return 月份数组，从一月开始，数组长度为12
     */
    public String[] titleMonth() {
        return new String[]{"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};
    }

    /**
     * 星期
     * @return 星期数组，从星期天开始，数组长度为7
     */
    public String[] titleWeek() {
        return new String[]{"日", "一", "二", "三", "四", "五", "六"};
    }
}
