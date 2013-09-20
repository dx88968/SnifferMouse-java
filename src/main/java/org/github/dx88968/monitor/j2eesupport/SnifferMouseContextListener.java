package org.github.dx88968.monitor.j2eesupport;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.github.dx88968.monitor.logger.DirectOutputTracker;

public class SnifferMouseContextListener implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			Configuration.configure(sce.getServletContext());
			DirectOutputTracker.init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		DirectOutputTracker.instance.stop();
	}
	
	

}


