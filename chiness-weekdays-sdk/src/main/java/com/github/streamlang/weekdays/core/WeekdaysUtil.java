package com.github.streamlang.weekdays.core;

import com.github.streamlang.weekdays.pattern.MonthPattern;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

public class WeekdaysUtil {
	/**
	 * 计算指定月份的工作日天数
	 *
	 * @param month        日期
	 * @param monthPattern 日期格式
	 * @return 天数
	 * @throws ParseException
	 */
	public static int countWeekdaysOfMonth(String month, String monthPattern) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DateUtils.parseDate(month, monthPattern));
		int monthOfYear = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);
		return countWeekdaysOfMonth(year, monthOfYear);
	}
	
	/**
	 * 计算指定月份的工作日天数
	 *
	 * @param date 至少包含年份和月份信息
	 * @return 天数
	 */
	public static int countWeekdaysOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int month = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);
		return countWeekdaysOfMonth(year, month);
	}
	
	/**
	 * 指定月份的工作日清单
	 *
	 * @param month        月份
	 * @param monthPattern 月份格式
	 * @param dayPattern   日期格式
	 * @return
	 * @throws ParseException
	 */
	public static List<String> listWeekdaysOfMonth(String month, String monthPattern, String dayPattern) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DateUtils.parseDate(month, monthPattern));
		int monthOfYear = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);
		return listWeekdaysOfMonth(year, monthOfYear, dayPattern);
	}
	
	/**
	 * 指定月份的所有工作日列表
	 *
	 * @param year       年
	 * @param month      月
	 * @param dayPattern 日-格式
	 * @return
	 */
	public static List<String> listWeekdaysOfMonth(int year, int month, String dayPattern) {
		final FastDateFormat formatter = FastDateFormat.getInstance(dayPattern);
		List<String> weekdaysList = new ArrayList<>();
		listWeekdaysOfMonth(year, month).stream().forEach(date -> {
			weekdaysList.add(formatter.format(date));
		});
		return weekdaysList;
	}
	
	public static List<Date> listWeekdaysOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int month = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);
		return listWeekdaysOfMonth(year, month);
	}
	
	public static boolean isWeekday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int month = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		int dayOfMonth = calendar.get(Calendar.DATE);
		MonthPattern monthPattern = WeekdaysDataBuilder.getStorage().getPattern(year, month);
		boolean nonHoliday = monthPattern == null || isEmptyCollection(monthPattern.getHolidays());
		if (!(dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY)) {
			List<Integer> holidays = nonHoliday ? new ArrayList<>() : monthPattern.getHolidays();
			if (!nonHoliday) {
				return holidays.stream().noneMatch(holiday -> holiday == dayOfMonth);
			} else {
				return true;
			}
		} else {
			//周六周日需要判断是否调班
			List<Integer> shiftDays = monthPattern.getShiftDays();
			if (!isEmptyCollection(shiftDays)) {
				return shiftDays.stream().anyMatch(shiftDay -> shiftDay==dayOfMonth);
			} else {
				return false;
			}
		}
	}
	
	public static List<Date> listWeekdaysOfMonth(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DATE, 1);
		List<Date> weekdays = new ArrayList<>();
		MonthPattern monthPattern = WeekdaysDataBuilder.getStorage().getPattern(year, month);
		boolean nonHoliday = monthPattern == null || isEmptyCollection(monthPattern.getHolidays());
		List<Integer> holidays = nonHoliday ? new ArrayList<>() : monthPattern.getHolidays();
		//如果有假期则需要额外判断 Mon - Fri是否为假日
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
			if (!isEmptyCollection(shiftDays)) {
				calendar.set(Calendar.MONTH, month);
				shiftDays.stream().forEach(shiftDay -> {
					calendar.set(Calendar.DATE, shiftDay);
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
