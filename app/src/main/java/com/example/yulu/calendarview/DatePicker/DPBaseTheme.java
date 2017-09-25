package com.example.yulu.calendarview.DatePicker;

/**
 * 日历主题管理接口
 * Created by lenovo on 2017/5/9.
 */

public interface DPBaseTheme {
    /**
     * 内容区背景色
     */
    int getContentBG();

    /**
     * 今天的背景色
     */
    int getTodayBG();

    /**
     * 双休日的文字颜色
     */
    int getHoliTxtColor();

    /**
     * 常规日期文字颜色
     */
    int getCommonTxtColor();

    /**
     * 星期标题文字颜色
     */
    int getTitleTxtColor();

    /**
     * 当天有直播的背景色
     */
    int getLiveBG();

    /**
     * 星期标题背景色
     */
    int getTitleBG();

    /**
     * 选中时文字颜色
     */
    int getSelectedTxtColor();

    /**
     * 选中时的背景色
     */
    int getSelectedBG();
    /**
     * 当天字颜色
     */
    int getTodayTxtColor();

    /**
     *
     */

    int getTickTextColor();
}
