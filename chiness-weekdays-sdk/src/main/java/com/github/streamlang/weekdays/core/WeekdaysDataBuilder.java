package com.github.streamlang.weekdays.core;

import com.github.streamlang.weekdays.spi.PatternSource;
import com.github.streamlang.weekdays.spi.PatternStorage;

import java.util.Iterator;
import java.util.ServiceLoader;

public class WeekdaysDataBuilder {
	private static WeekdaysDataBuilder I;
	private PatternSource patternSource;
	private PatternStorage patternStorage;
	
	public static PatternStorage getStorage() {
		return I.patternStorage;
	}
	
	public void build() {
		I = new WeekdaysDataBuilder();
		ServiceLoader<PatternSource> sourceLoader = ServiceLoader.load(PatternSource.class);
		ServiceLoader<PatternStorage> storageLoader = ServiceLoader.load(PatternStorage.class);
		patternSource = load(PatternSource.class, sourceLoader);
		patternStorage = load(PatternStorage.class, storageLoader);
		patternStorage.init(patternSource.getPatterns());
		I.patternSource = patternSource;
		I.patternStorage = patternStorage;
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
}
