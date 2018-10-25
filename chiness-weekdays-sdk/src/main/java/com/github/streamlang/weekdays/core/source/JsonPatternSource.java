package com.github.streamlang.weekdays.core.source;

import com.alibaba.fastjson.JSONArray;
import com.github.streamlang.weekdays.pattern.WeekdaysPatterns;
import com.github.streamlang.weekdays.pattern.YearPattern;
import com.github.streamlang.weekdays.spi.PatternSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class JsonPatternSource implements PatternSource {
	private static final String FILE_NAME = "weekdays_source.json";
	private static Lock lock = new ReentrantLock();
	
	public WeekdaysPatterns getPatterns() {
		if (lock.tryLock()) {
			try {
				String content;
				File configFile = new File(this.getClass().getResource("/").getPath()+FILE_NAME);
				if(configFile.exists()){
					content = FileUtils.readFileToString(configFile);
				} else {
					content = IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(FILE_NAME));
				}
				WeekdaysPatterns patterns = new WeekdaysPatterns();
				patterns.setYearPatterns(JSONArray.parseArray(content, YearPattern.class));
				return patterns;
			} catch (IOException e) {
				throw new IllegalStateException("error occurred when read <" + FILE_NAME + ">,init weekdaysUtil failed:" + e.getMessage());
			}
		}
		return new WeekdaysPatterns();
	}
}
