package com.github.streamlang.scraper.juhe;

import java.util.List;

/**
 * 聚合Api 响应报文描述
 * https://www.juhe.cn/box/index/id/177
 */
public class JuheHolidayResponse {
	private String reason;
	private String errorCode;
	private Result result;
	static class Result{
		private Holidays data;
		
		public Holidays getData() {
			return data;
		}
		
		public void setData(Holidays data) {
			this.data = data;
		}
	}
	static class Holidays{
		private List<Holiday> holidayArray;
		
		public List<Holiday> getHolidayArray() {
			return holidayArray;
		}
		
		public void setHolidayArray(List<Holiday> holidayArray) {
			this.holidayArray = holidayArray;
		}
	}
	static class Holiday{
		private String name;//节日名称
		private String desc;//
		private List<Day> list;
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public String getDesc() {
			return desc;
		}
		
		public void setDesc(String desc) {
			this.desc = desc;
		}
		
		public List<Day> getList() {
			return list;
		}
		
		public void setList(List<Day> list) {
			this.list = list;
		}
	}
	static class Day{
		private String date; //日期
		private String status; //属性 1 休息日 2 调班
		
		public String getDate() {
			return date;
		}
		
		public void setDate(String date) {
			this.date = date;
		}
		
		public String getStatus() {
			return status;
		}
		
		public void setStatus(String status) {
			this.status = status;
		}
	}
	
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	public Result getResult() {
		return result;
	}
	
	public void setResult(Result result) {
		this.result = result;
	}
}
