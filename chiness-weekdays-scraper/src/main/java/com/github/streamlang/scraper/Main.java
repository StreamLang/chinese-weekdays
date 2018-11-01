package com.github.streamlang.scraper;

import com.alibaba.fastjson.JSON;
import com.github.streamlang.scraper.juhe.JuheGenerator;
import com.github.streamlang.weekdays.pattern.WeekdaysPatterns;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		String appKey = null, filePath = null;
		Integer startYear = null, endYear = null;
		try {
			appKey = args[0];
			startYear = Integer.valueOf(args[1]);
			endYear = Integer.valueOf(args[2]);
			if(args.length>3) {
				filePath = args[3];
			}else{
				filePath = new Object().getClass().getResource("/").getPath() + "weekdays_source-"+System.currentTimeMillis()+".json";
			}
		} catch (Exception e) {
			System.err.println("illegal args,{appKey startYear endYear filePath} are required");
			System.exit(1);
		}
		WeekdaysPatterns patterns = JuheGenerator.scrapHolidayPattern(appKey,startYear, endYear);
		try {
			FileUtils.writeStringToFile(new File(filePath), JSON.toJSONString(patterns.getYearPatterns()));
			System.exit(0);
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
			System.exit(1);
		}
	}
}
