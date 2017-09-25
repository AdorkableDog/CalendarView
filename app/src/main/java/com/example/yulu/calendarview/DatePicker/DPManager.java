package com.example.yulu.calendarview.DatePicker;

import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by lenovo on 2017/5/10.
 */

public class DPManager {

	private static DPManager mDPManager;

	private DPManager() {

	}

	/**
	 * 单例对象
	 *
	 * @return
	 */
	public static DPManager getInstance() {
		if (mDPManager == null)
			synchronized (DPManager.class) {
				if (mDPManager == null) {
					mDPManager = new DPManager();
				}
			}
		return mDPManager;
	}

	private static final HashMap<Integer, HashMap<Integer, DPCellInfo[][]>> DATE_CACHE = new HashMap<>();
	private DPCalendar c = new DPCalendar();

	/**
	 * 获取指定年月的日历对象数组
	 *
	 * @param year  阳历年
	 * @param month 阳历月
	 * @return 日历对象数组 该数组长度恒为6x7 如果某个下标对应无数据则填充为null
	 */
	public DPCellInfo[][] obtainDPInfo(int year, int month) {
		HashMap<Integer, DPCellInfo[][]> dataOfYear = DATE_CACHE.get(year);
		if (null != dataOfYear && dataOfYear.size() != 0) {
			DPCellInfo[][] dataOfMonth = dataOfYear.get(month);
			if (dataOfMonth != null) {
				return dataOfMonth;
			}
			dataOfMonth = buildDPInfo(year, month);
			dataOfYear.put(month, dataOfMonth);
			return dataOfMonth;
		}
		if (null == dataOfYear) dataOfYear = new HashMap<>();
		DPCellInfo[][] dataOfMonth = buildDPInfo(year, month);
		dataOfYear.put((month), dataOfMonth);
		DATE_CACHE.put(year, dataOfYear);
		return dataOfMonth;
	}

	/**
	 *
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public DPCellInfo obtainDPInfo(int year, int month,int day) {

		DPCellInfo[][] dpCellInfos = obtainDPInfo(year, month);

		DPCellInfo info = null;
		OUTTER: for(int i = 0; i < dpCellInfos.length; i++) {
			INNER: for (int j = 0; j < dpCellInfos[i].length; j++) {
				if ((day+"").equals(dpCellInfos[i][j].strG)){

					info = dpCellInfos[i][j];
					break OUTTER;
				}
			}
		}

		return info;
	}

	private static final HashMap<String, Set<String>> DECOR_CACHE = new HashMap<>();

	/**
	 * 构建有无直播的day - info
	 *
	 * @param year
	 * @param month
	 * @return
	 */
	private DPCellInfo[][] buildDPInfo(int year, int month) {
		DPCellInfo[][] info = new DPCellInfo[6][7];

		String[][] strG = c.buildMonthG(year, month);
		Log.i("DPManager", strG.length + "<--length");
		Set<String> decorBG = DECOR_CACHE.get(year + ":" + month);

		for (int i = 0; i < info.length; i++) {
			for (int j = 0; j < info[i].length; j++) {
				DPCellInfo tmp = new DPCellInfo();
				Log.i("DPManager", "decorBG:" + decorBG + ",strG[i][j]:" + strG[i][j]);
				if (decorBG != null && decorBG.contains(strG[i][j])) {
					tmp.isLive = true;
				}

				if (!TextUtils.isEmpty(strG[i][j])) {
					tmp.isToday = c.isToday(year, month, Integer.valueOf(strG[i][j]));
					tmp.isLive = random.nextInt(3) % 3 == 0;
				}
				//if (tmp.isLive)
//					tmp.isPic = "$ 200";
				tmp.strG = strG[i][j];
				info[i][j] = tmp;
			}
		}
		return info;
	}

	private Random random = new Random();

	/**
	 * 有直播记录的时间
	 * <p>
	 * 传入的list的形式2017-05-10
	 *
	 * @param date
	 */
	public void setDecor(List<String> date) {
		for (String str : date) {
			int index = str.lastIndexOf("-");
			String key = str.substring(0, index).replace("-", ":");
			Set<String> days = DECOR_CACHE.get(key);
			if (null == days) {
				days = new HashSet<>();
			}
			days.add(str.substring(index + 1, str.length()));
			DECOR_CACHE.put(key, days);
		}
	}
}
