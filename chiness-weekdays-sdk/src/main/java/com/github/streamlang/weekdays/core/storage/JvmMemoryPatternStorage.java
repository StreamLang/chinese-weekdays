package com.github.streamlang.weekdays.core.storage;

import com.github.streamlang.weekdays.pattern.MonthPattern;
import com.github.streamlang.weekdays.pattern.WeekdaysPatterns;
import com.github.streamlang.weekdays.spi.PatternStorage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JvmMemoryPatternStorage implements PatternStorage {
	private static Map<String, MonthPattern> cache = new ConcurrentHashMap<>();
	
	public MonthPattern getPattern(int year, int month) {
		return cache.get(getKey(year, month));
	}
	
	public void init(WeekdaysPatterns patterns) {
		patterns.getYearPatterns().parallelStream().forEach(yearPattern -> {
			Integer year = yearPattern.getYear();
			yearPattern.getMonths().stream().forEach(monthPattern -> {
				cache.put(getKey(year, monthPattern.getMonth()), monthPattern);
			});
		});
	}
	
	@Override
	public void reload(WeekdaysPatterns patterns) {
		init(patterns);
		//cachePool using concurrentHashMap , invoke init() is enough;
	}
	
	private static String getKey(Integer year, Integer month) {
		return String.format("%d-%d", year, month);
	}
	
	public void destroy() {
		cache.clear();
	}
}
