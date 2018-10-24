package com.github.streamlang.weekdays.pattern;

import java.util.List;

public class YearPattern {
	private int year;
	private List<MonthPattern> months;
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public List<MonthPattern> getMonths() {
		return months;
	}
	
	public void setMonths(List<MonthPattern> months) {
		this.months = months;
	}
}
