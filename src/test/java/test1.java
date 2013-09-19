import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.github.dx88968.monitor.logger.DirectOutputTracker;
import org.github.dx88968.monitor.logger.TraceEvent;
import org.github.dx88968.monitor.logger.TraceLevel;


public class test1 {

	public static void main(String[] args) throws Exception {
		/*
		 int num = 10;
	        String str = "inqury_dfghj";
	         
	        //匹配大于2的数字
	        Pattern p = Pattern.compile("inqury_.*");
	        Matcher m = p.matcher(str);
	        System.out.println(m.matches());
	         
	         
	        //匹配大于20的数字
	        Pattern p1 = Pattern.compile("2[1-9]|[3-9][0-9]|[1-9]\\d{2}");
	        Matcher m1 = p1.matcher(str);
	        System.out.println(m1.matches());*/
		DirectOutputTracker.instance.waitfor(TraceEvent.SessionCreated);
		DirectOutputTracker.instance.print("200", TraceLevel.ALL, "hello");
		DirectOutputTracker.instance.join();
		System.exit(0);
		//DirectOutputTracker.join();
	}
}
