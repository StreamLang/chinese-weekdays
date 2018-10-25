package com.github.streamlang.weekdays.core;

import com.github.streamlang.weekdays.spi.PatternSource;
import com.github.streamlang.weekdays.spi.PatternStorage;

import java.util.Iterator;
import java.util.ServiceLoader;

public class WeekdaysDataBuilder {
	public static final WeekdaysDataBuilder I = new WeekdaysDataBuilder();
	private PatternSource patternSource;
	private PatternStorage patternStorage;
	
	public static PatternStorage getStorage() {
		return I.patternStorage;
	}
	
	public static PatternSource getSource() {
		return I.patternSource;
	}
	
	public WeekdaysDataBuilder() {
		try {
			ServiceLoader<PatternSource> sourceLoader = ServiceLoader.load(PatternSource.class);
			ServiceLoader<PatternStorage> storageLoader = ServiceLoader.load(PatternStorage.class);
			patternSource = load(PatternSource.class, sourceLoader);
			patternStorage = load(PatternStorage.class, storageLoader);
			patternStorage.init(patternSource.getPatterns());
		}catch (Exception e){
			System.err.println(e.getMessage());
			//本工具初始化失败，不应影响主程序正常运行
		}
	}
	
	private <T> T load(Class<T> clazz, ServiceLoader<T> loader) {
		Iterator<T> it = loader.iterator();
		if (it.hasNext()) {
			return it.next();
		} else {
			throw new IllegalStateException("missing SPI file:" + clazz.getName());
		}
	}
	
	public void destroy() {
		patternStorage.destroy();
	}
	
	private void setPatternSource(PatternSource patternSource) {
		this.patternSource = patternSource;
	}
	
	private void setPatternStorage(PatternStorage patternStorage) {
		this.patternStorage = patternStorage;
	}
}
