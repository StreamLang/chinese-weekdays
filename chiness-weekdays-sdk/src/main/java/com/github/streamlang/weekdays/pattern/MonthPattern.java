package com.github.streamlang.weekdays.pattern;

import java.util.List;

public class MonthPattern {
	private int month;
	private List<Integer> holidays;
	private List<Integer> shiftDays;
	
	public int getMonth() {
		return month;
	}
	
	public void setMonth(int month) {
		this.month = month;
	}
	
	public List<Integer> getHolidays() {
		return holidays;
	}
	
	public void setHolidays(List<Integer> holidays) {
		this.holidays = holidays;
	}
	
	public List<Integer> getShiftDays() {
		return shiftDays;
	}
	
	public void setShiftDays(List<Integer> shiftDays) {
		this.shiftDays = shiftDays;
	}
}
