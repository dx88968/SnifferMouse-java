package org.github.dx88968.monitor.j2eesupport;

import javax.servlet.ServletContext;

import org.github.dx88968.monitor.utils.Constants;

public class Configuration {

	public static void configure(ServletContext context){
		Integer port=Constants.toInteger(context.getInitParameter("org.github.dx88968.serverport"));
		if (port!=null) {
			Constants.DEFAULTSERVERPORT=port;
		}
		Integer sessiontimeout=Constants.toInteger(context.getInitParameter("org.github.dx88968.sessiontimeout"));
		if (sessiontimeout!=null) {
			Constants.SESSIONTIMEOUT=sessiontimeout;
		}
		Integer max_num_pipeline=Constants.toInteger(context.getInitParameter("org.github.dx88968.max_num_pipeline"));
		if (max_num_pipeline!=null) {
			Constants.MAX_NUM_PIPELINE=max_num_pipeline;
		}
		Integer max_num_session_each_pipeline=Constants.toInteger(context.getInitParameter("org.github.dx88968.max_num_session_each_pipeline"));
		if (max_num_session_each_pipeline!=null) {
			Constants.MAX_NUM_SESSION_EACH_PIPELINE=max_num_session_each_pipeline;
		}
		Integer max_thourghout=Constants.toInteger(context.getInitParameter("org.github.dx88968.max_thourghout"));
		if (max_thourghout!=null) {
			Constants.MAX_THOURGHOUT=max_thourghout;
		}
		Constants.isLocked=true;
	}
}
