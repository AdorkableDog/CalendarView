package com.example.yulu.calendarview.DatePicker;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yulu.calendarview.R;
import com.example.yulu.calendarview.Utils.DensityUtils;


/**
 * Created by lenovo on 2017/5/11.
 */

public class DPTitleView extends LinearLayout {

    private static final String TAG = "DPTitleView";

    private DPLanguage mDPLanguage;

    public DPTitleView(Context context) {
        super(context);
        init();
    }

    public DPTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DPTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        mDPLanguage = new DPLanguage();
        setOrientation(HORIZONTAL);
        String[] titleWeek = mDPLanguage.titleWeek();
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.weight = 1;
        int textColor = getResources().getColor(R.color.sub_txt_color);
        int textSize = DensityUtils.sp2px(getContext(),15);
        Log.i(TAG,"textSize："+textSize);
        for (int i = 0; i < titleWeek.length; i++) {
            TextView textView = new TextView(getContext());
            textView.setText(titleWeek[i]);
            textView.setTextColor(textColor);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
            textView.setGravity(Gravity.CENTER);
            addView(textView, params);
        }
    }

}
