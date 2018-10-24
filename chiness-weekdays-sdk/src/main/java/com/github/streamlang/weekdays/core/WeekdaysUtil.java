package com.github.streamlang.weekdays.core;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class WeekdaysUtil {
	
	public static int countWeekdaysOfMonth(String date, String dateFormat) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DateUtils.parseDate(date, dateFormat));
		int month = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);
		return countWeekdaysOfMonth(year, month);
	}
	
	public static int countWeekdaysOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int month = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);
		return countWeekdaysOfMonth(year, month);
	}
	
	public static int countWeekdaysOfMonth(int year, int month) {
		//todo
		return 0;
	}
}
