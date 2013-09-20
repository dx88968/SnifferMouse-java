import org.github.dx88968.monitor.logger.DirectOutputTracker;
import org.github.dx88968.monitor.logger.TraceEvent;
import org.github.dx88968.monitor.logger.TraceLevel;


public class test1 {

	public static void main(String[] args) throws Exception {
		System.setProperty("org.github.dx88968.serverport", "10081");
		DirectOutputTracker.instance.waitfor(TraceEvent.SessionCreated);
		DirectOutputTracker.instance.print("200", TraceLevel.ALL, "hello");
		DirectOutputTracker.instance.join();
		System.exit(0);
	}
}
