package com.github.streamlang.extra.utils;

import com.github.streamlang.extra.common.DefaultConfig;
import com.github.streamlang.extra.struct.Pair;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * 做统计时常用到的日期格式化
 *
 * @author StreamLang  created at 2018/4/8.
 */
public class StatsDateUtil {
	
	public static Date getYesterday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -1);
		return calendar.getTime();
	}
	
	/**
	 * 给出日期、指定偏移年份后获得 起始 终止月份
	 *
	 * @param date        截止日期
	 * @param offsetYears
	 * @param monthFormat 月份格式化规则
	 * @return
	 */
	public static Pair recentlyMonth(Date date, int offsetYears, String monthFormat) {
		Pair<String> pair = new Pair();
		pair.setLeft(getMonth(date, offsetYears, monthFormat));
		pair.setRight(getMonth(date, 0, monthFormat));
		return pair;
	}
	
	/**
	 * 给出日期、指定偏移年份后获得 起始 终止月份
	 * @param date          截止日期
	 * @param offsetYears
	 * @param quarterFormat 季度格式化规则
	 * @return
	 */
	public static Pair recentlyQuarter(Date date, int offsetYears, String quarterFormat) {
		Pair<String> pair = new Pair();
		pair.setLeft(getQuarter(date, offsetYears, quarterFormat));
		pair.setRight(getQuarter(date, 0, quarterFormat));
		return pair;
	}
	
	/**
	 * 给出日期、指定偏移年份后获得月份数据
	 * e.g:
	 *
	 * @param date        2018-10-24
	 * @param offsetYears 1
	 * @param monthFormat null
	 * @return 201904
	 */
	public static String getMonth(Date date, int offsetYears, String monthFormat) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, offsetYears);
		int month = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);
		return String.format(StringUtils.isEmpty(monthFormat) ? DefaultConfig.DEFAULT_MONTH_FORMAT : monthFormat, year, month);
	}
	
	/**
	 * 给出日期、指定偏移年份后获得季度数据
	 * e.g:
	 *
	 * @param date          2018-10-24
	 * @param offsetYears   1
	 * @param quarterFormat null
	 * @return 201904
	 */
	public static String getQuarter(Date date, int offsetYears, String quarterFormat) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, offsetYears);
		double month = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);
		int quarter = new BigDecimal(month / DefaultConfig.MONTH_PER_QUARTER).setScale(0, BigDecimal.ROUND_UP).intValue();
		return String.format(StringUtils.isEmpty(quarterFormat) ? DefaultConfig.DEFAULT_QUARTER_FORMAT : quarterFormat, year, quarter);
	}
	
	
}