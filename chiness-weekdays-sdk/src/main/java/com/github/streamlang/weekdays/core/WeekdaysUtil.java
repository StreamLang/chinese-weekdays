package com.github.streamlang.weekdays.core;

import com.github.streamlang.weekdays.pattern.MonthPattern;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

public class WeekdaysUtil {
	
	public static int countWeekdaysOfMonth(String date, String datePattern) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DateUtils.parseDate(date, datePattern));
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
	
	public static List<String> listWeekdaysOfMonth(String date,String datePattern) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DateUtils.parseDate(date, datePattern));
		int month = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);
		return listWeekdaysOfMonth(year, month,datePattern);
	}
	public static List<String> listWeekdaysOfMonth(int year, int month, String datePattern) {
		final FastDateFormat formatter = FastDateFormat.getInstance(datePattern);
		List<String> weekdaysList = new ArrayList<>();
		listWeekdaysOfMonth(year, month).stream().forEach(date -> {
			weekdaysList.add(formatter.format(date));
		});
		return weekdaysList;
	}
	
	public static List<Date> listWeekdaysOfMonth(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int month = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);
		return listWeekdaysOfMonth(year,month);
	}
	
	public static List<Date> listWeekdaysOfMonth(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DATE, 1);
		List<Date> weekdays = new ArrayList<>();
		MonthPattern monthPattern = WeekdaysDataBuilder.getStorage().getPattern(year, month);
		List<Integer> holidays = monthPattern.getHolidays();
		boolean nonHoliday = isEmptyCollection(holidays);
		while (calendar.get(Calendar.YEAR) == year
				&& calendar.get(Calendar.MONTH) < month) {
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
			if (!(dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY)) {
				if (!nonHoliday) {
					int dayOfMonth = calendar.get(Calendar.DATE);
					if (holidays.stream().noneMatch(holiday -> holiday == dayOfMonth))
						weekdays.add(calendar.getTime());
				} else {
					weekdays.add(calendar.getTime());
				}
			}
			calendar.add(Calendar.DATE, 1);
		}
		if (monthPattern != null) {
			List<Integer> shiftDays = monthPattern.getShiftDays();
			if (!isEmptyCollection(shiftDays)){
				calendar.set(Calendar.MONTH,month);
				shiftDays.stream().forEach(shiftDay->{
					calendar.set(Calendar.DATE,shiftDay);
					weekdays.add(calendar.getTime());
				});
			}
		}
		return weekdays.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
	}
	
	public static int countWeekdaysOfMonth(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DATE, 1);
		int weekdays = 0;
		while (calendar.get(Calendar.YEAR) == year
				&& calendar.get(Calendar.MONTH) < month) {
			int day = calendar.get(Calendar.DAY_OF_WEEK);
			if (!(day == Calendar.SUNDAY || day == Calendar.SATURDAY)) {
				weekdays++;
			}
			calendar.add(Calendar.DATE, 1);
		}
		MonthPattern monthPattern = WeekdaysDataBuilder.getStorage().getPattern(year, month);
		if (monthPattern != null) {
			if (!isEmptyCollection(monthPattern.getShiftDays()))
				weekdays += monthPattern.getShiftDays().size();
			if (!isEmptyCollection(monthPattern.getHolidays()))
				weekdays -= monthPattern.getHolidays().size();
		}
		return weekdays;
	}
	
	private static boolean isEmptyCollection(Collection collection) {
		return collection == null || collection.size() == 0;
	}
}
