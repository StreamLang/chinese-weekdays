package com.github.streamlang.weekdays.spi;

import com.github.streamlang.weekdays.pattern.MonthPattern;
import com.github.streamlang.weekdays.pattern.WeekdaysPatterns;

public interface PatternStorage {
	void init(WeekdaysPatterns patterns);
	void reload(WeekdaysPatterns patterns);
	void destroy();
	MonthPattern getPattern(int year, int month);
}
