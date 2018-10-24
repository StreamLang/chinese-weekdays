package com.github.streamlang.weekdays.spi;

import com.github.streamlang.weekdays.pattern.WeekdaysPatterns;

public interface PatternStorage {
	void init(WeekdaysPatterns patterns);
	void destroy();
	
}
