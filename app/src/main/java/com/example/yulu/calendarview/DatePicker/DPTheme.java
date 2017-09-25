package com.example.yulu.calendarview.DatePicker;

import android.content.Context;

import com.example.yulu.calendarview.R;


/**
 * Created by lenovo on 2017/5/9.
 */

public class DPTheme implements DPBaseTheme {
    private Context context;

    public DPTheme(Context context) {
        this.context = context;
    }

    @Override
    public int getContentBG() {
        return context.getResources().getColor(R.color.white);
    }

    @Override
    public int getTodayBG() {
        return context.getResources().getColor(R.color.light_orange_color);
    }

    @Override
    public int getHoliTxtColor() {
        return context.getResources().getColor(R.color.normal_txt_color);
    }

    @Override
    public int getCommonTxtColor() {
        return context.getResources().getColor(R.color.normal_txt_color);
    }

    @Override
    public int getTitleTxtColor() {
        return context.getResources().getColor(R.color.tips_txt_color);
    }

    @Override
    public int getLiveBG() {
        return context.getResources().getColor(R.color.orange_color);
    }

    @Override
    public int getTitleBG() {
        return context.getResources().getColor(R.color.white);
    }

    @Override
    public int getSelectedTxtColor() {
        return context.getResources().getColor(R.color.white);
    }

    @Override
    public int getSelectedBG() {
        return context.getResources().getColor(R.color.orange_color);
    }

    @Override
    public int getTodayTxtColor() {
        return context.getResources().getColor(R.color.orange_color);
    }

    @Override
    public int getTickTextColor() {
        return context.getResources().getColor(R.color.sub_blue_color);
    }
}
