import com.github.streamlang.weekdays.core.WeekdaysDataBuilder;
import com.github.streamlang.weekdays.core.WeekdaysUtil;
import com.github.streamlang.weekdays.pattern.WeekdaysPatterns;
import org.junit.After;
import org.junit.Before;

import java.util.Date;

public class Test {

	@org.junit.Test
	public void doTest(){
//		WeekdaysDataBuilder.getStorage().reload(WeekdaysDataBuilder.getSource().getPatterns());
		int count = WeekdaysUtil.countWeekdaysOfMonth(new Date());
		WeekdaysUtil.listWeekdaysOfMonth(new Date()).stream().forEach(str ->{
			System.out.println(str);
		});
		System.out.println(count + "天工作日");
	}
	
	@After
	public void destroy(){
		WeekdaysDataBuilder.I.destroy();
	}
}
