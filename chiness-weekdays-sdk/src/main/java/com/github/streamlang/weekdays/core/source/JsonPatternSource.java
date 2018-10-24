package com.github.streamlang.weekdays.core.source;

import com.alibaba.fastjson.JSON;
import com.github.streamlang.weekdays.pattern.WeekdaysPatterns;
import com.github.streamlang.weekdays.spi.PatternSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ClassPathUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonPatternSource implements PatternSource {
	private static final String FILE_NAME = "weekdays_source.json";
	
	public WeekdaysPatterns getPatterns() {
		try {
			String content = FileUtils.readFileToString(
					new File(this.getClass().getClassLoader().getResource(FILE_NAME).getPath()));
			return JSON.parseObject(content, WeekdaysPatterns.class);
		} catch (IOException e) {
			throw new IllegalStateException("error occurred when read <" + FILE_NAME + ">,init weekdaysUtil failed");
		}
	}
}
