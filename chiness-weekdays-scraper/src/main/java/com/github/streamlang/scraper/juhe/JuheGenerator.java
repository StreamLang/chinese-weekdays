package com.github.streamlang.scraper.juhe;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.github.streamlang.weekdays.pattern.MonthPattern;
import com.github.streamlang.weekdays.pattern.WeekdaysPatterns;
import com.github.streamlang.weekdays.pattern.YearPattern;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

//聚合数据的假期信息虽然是按月查询的，但是会查到指定时间附近的假期，
// 例如指定1月，可能返回的数据中包含元旦（1月），春节（2月）的信息，
// 所以有些月份是不需要重复调用接口的
public class JuheGenerator {
	static final String url = "http://v.juhe.cn/calendar/month?year-month=%s&key=%s";
	private static CloseableHttpClient httpClient = null;
	
	public static WeekdaysPatterns scrapHolidayPattern(String appKey, Integer startYear, Integer endYear) {
		preInvoke();
		WeekdaysPatterns patterns = new WeekdaysPatterns();
		List<YearPattern> yearPatternList = new ArrayList<>();
		patterns.setYearPatterns(yearPatternList);
		for (int i = startYear; i <= endYear; i++) {
			yearPatternList.add(getYearPatterns(appKey, i));
		}
		afterInvoke();
		return patterns;
	}
	
	private static YearPattern getYearPatterns(String appKey, Integer year) {
		YearPattern yearPattern = new YearPattern();
		yearPattern.setYear(year);
		List<MonthPattern> monthPatterns = new ArrayList<>();
		yearPattern.setMonths(monthPatterns);
		List<DayOfYear> dayOfYearList = listDayOfYear(appKey, year);
		Map<Integer, List<DayOfYear>> monthGroupList = dayOfYearList.stream().collect(Collectors.groupingBy(day -> day.getMonth()));
		monthGroupList.forEach((month, dayList) -> {
			MonthPattern monthPattern = new MonthPattern();
			monthPatterns.add(monthPattern);
			monthPattern.setMonth(month);
			List<Integer> holidays = new ArrayList<>();
			List<Integer> shiftDays = new ArrayList<>();
			monthPattern.setHolidays(holidays);
			monthPattern.setShiftDays(shiftDays);
			dayList.stream().forEach(day -> {
				if (JuheApiRef.rest.equals(day.getStatus())) {
					holidays.add(day.getDay());
				} else if (JuheApiRef.shift.equals(day.getStatus())) {
					shiftDays.add(day.getDay());
				}
			});
		});
		return yearPattern;
	}
	
	//接口按节日分组，假日可能跨年，也可能跨月，
	// 所以需要先把日期全部列出来，再转换为期望的数据格式
	private static List<DayOfYear> listDayOfYear(String appKey, Integer currentYear) {
		List<DayOfYear> dayOfYearList = new ArrayList<>();
		//这里原本是想做month Set，减少接口调用次数，但春节可以在3、4、5月的查询中拿到，
		//如果在遍历节日时校验月份，根本无法确认，因为节日的标记日期是当天，而假期是可能跨月跨年的
		// 如果在遍历假日时再校验一次月份----若存在重复月份则跳过----会跳过同一节日下的多天假期
		//没想好把二次校验放哪
		// 所以暂时按节日名做重复性校验
		Set<String> holidaySet = new HashSet<>();//避免重复写入节日信息
		for (int month = 1; month <= 12; month++) {
			String yearMonth = String.format("%d-%d", currentYear, month);
			HttpGet httpGet = new HttpGet(String.format(url, yearMonth, appKey));
			try {
				String res = EntityUtils.toString(httpClient.execute(httpGet).getEntity());
				JuheHolidayResponse juheHolidayResponse = JSON.parseObject(res, JuheHolidayResponse.class);
				if (juheHolidayResponse == null || !JuheApiRef.success.equals(juheHolidayResponse.getReason())) {
					System.out.println(String.format("[%s]-{%s}-error_code{%s}", yearMonth, juheHolidayResponse.getReason(), juheHolidayResponse.getErrorCode()));
					continue;
				}
				List<JuheHolidayResponse.Holiday> holidayList = juheHolidayResponse.getResult().getData().getHolidayArray();
				holidayList.stream().forEach(holiday -> {
					//聚合数据返回的假期安排中是按节日分组的，MonthPattern需要的是按年份、月份分组
					//有些节日的假期安排是跨月份的，例如五一劳动节
					//有些节日的假期安排有可能会跨年，例如元旦
					//先把所有假日安排列出来
					List<JuheHolidayResponse.Day> dayList = holiday.getList();
					//无需重复解析同一个节日
					if (holidaySet.contains(holiday.getName())) {
						return;
					}
					holidaySet.add(holiday.getName());
					dayList.stream().forEach(day -> {
						//聚合返回的日期格式不是yyyy-MM-dd ,而是短日期fM-fd，如果月份或日期为一位，不会出现0
						DayOfYear dayOfYear = formatDate(day, currentYear);
						if (dayOfYear != null) {
							System.out.println(String.format("[%d-%d-%d]-{%s}",
									currentYear, dayOfYear.getMonth(), dayOfYear.getDay(), holiday.getName()));
							dayOfYearList.add(dayOfYear);
						}
					});
				});
			} catch (IOException ioe) {
				ioe.printStackTrace();
				continue;
			} catch (JSONException je) {
				je.printStackTrace();
			}
			
		}
		return dayOfYearList;
	}
	
	private static DayOfYear formatDate(JuheHolidayResponse.Day day, Integer expectYear) {
		String date = day.getDate();
		
		int first = date.indexOf("-");
		int second = date.indexOf("-", first + 1);
		int year = Integer.valueOf(date.substring(0, first));
		if (year != expectYear) {
			return null;
		}
		DayOfYear dayOfYear = new DayOfYear();
		dayOfYear.setMonth(Integer.valueOf(date.substring(first + 1, second)));
		dayOfYear.setDay(Integer.valueOf(date.substring(second + 1)));
		dayOfYear.setYear(year);
		dayOfYear.setStatus(day.getStatus());
		return dayOfYear;
	}
	
	private static void preInvoke() {
		httpClient = HttpClients.createDefault();
	}
	
	private static void afterInvoke() {
		try {
			if (httpClient != null)
				httpClient.close();
		} catch (IOException e) {
		}
	}
}
