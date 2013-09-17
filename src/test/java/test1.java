import org.github.dx88968.monitor.logger.DirectOutputTracker;
import org.github.dx88968.monitor.logger.TraceEvent;
import org.github.dx88968.monitor.logger.TraceLevel;


public class test1 {

	public static void main(String[] args) throws Exception {
		DirectOutputTracker.instance.waitfor(TraceEvent.SessionCreated);
		DirectOutputTracker.instance.print(null, TraceLevel.ALL, "hello");
		DirectOutputTracker.instance.join();
		System.exit(0);
		//DirectOutputTracker.join();
	}
}
