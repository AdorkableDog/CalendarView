package com.example.yulu.calendarview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yulu.calendarview.DatePicker.DPManager;
import com.example.yulu.calendarview.DatePicker.DPMonthView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements DPMonthView.OnDatePickerClickListener {

	private static final String TAG = "MainActivity";
	private DPManager instance;
	private TextView Text_calendar_year;
	private TextView Text_calendar_month;
	private DPMonthView dpMonthView;
	private int month_random;
	private int day_random;
	private Map<String, String> map = new HashMap<String, String>();
	private String[] years = new String[]{


	};
	private List<String> a = new ArrayList<>();
	private String random_calender_date;
	private int day_random2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Text_calendar_year = (TextView) findViewById(R.id.calendar_year);
		Text_calendar_month = (TextView) findViewById(R.id.calendar_month);
		dpMonthView = (DPMonthView) findViewById(R.id.dp_calendar_content);

		dpMonthView.setOnDatePickClickListener(this);
		initData();
		initDPMonth();
	}

	Random random = new Random();

	private void initData() {
		instance = DPManager.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM—dd");
		String format = simpleDateFormat.format(new Date());
		int max = 1000;
		int min = 100;
		dpMonthView.openCalendar();
		for (int i = 0; i < 100; i++) {
			month_random = (int) (12 * Math.random() + 1);
			day_random = (int) (30 * Math.random() + 1);
			day_random2 = (int) (28 * Math.random() + 1);
//			pic_random = (int) (200 * Math.random() + 1);
			int pic_random = random.nextInt(max) % (max - min + 1) + min;
			if (month_random != 2) {
				random_calender_date = "2017-" + month_random + "-" + day_random;
			}else {
				random_calender_date ="2017-" + month_random + "-" + day_random2;
			}
			map.put(random_calender_date, "￥" + pic_random);
		}


		// 第三种：推荐，尤其是容量大时
		System.out.println("第三种：通过Map.entrySet遍历key和value");
		for (Map.Entry<String, String> entry : map.entrySet()) {
			//Map.entry<Integer,String> 映射项（键-值对）  有几个方法：用上面的名字entry
			//entry.getKey() ;entry.getValue(); entry.setValue();
			//map.entrySet()  返回此映射中包含的映射关系的 Set视图。
			Log.i(TAG, "map: " + "key= " + entry.getKey() + " and value= " + entry.getValue());
		}

	}

	private void initDPMonth() {
		new Thread(new Runnable() {


			@Override
			public void run() {

				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				for (Map.Entry<String, String> entry : map.entrySet()) {
					//Map.entry<Integer,String> 映射项（键-值对）  有几个方法：用上面的名字entry
					//entry.getKey() ;entry.getValue(); entry.setValue();
					//map.entrySet()  返回此映射中包含的映射关系的 Set视图。
					Log.i(TAG, "map: " + "key= " + entry.getKey() + " and value= " + entry.getValue());
					String[] date = entry.getKey().split("-");
					instance.obtainDPInfo(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2])).isPic = entry.getValue();
				}


				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						dpMonthView.invalidate();
					}
				});
			}
		}).start();
	}

	@Override
	public void onDatePick(String date) {
//		tvDate.setText(getDateStr(date));
		Toast.makeText(this, date, Toast.LENGTH_SHORT).show();
	}

	public void onMonthChanged(int year, int month) {

		Text_calendar_year.setText(year + "年");
		Text_calendar_month.setText(month + "月");
		Log.i(TAG, "onMonthChanged...year:" + year + ",month:" + month);
	}

	public String getDateStr(String dateStr) {
		String[] split = dateStr.split("-");
		return split[1] + "月" + split[2] + "日";
	}

}
