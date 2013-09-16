package org.github.dx88968.monitor.utils;

public class Constants {
	
	public static void loadConstants(){
		Integer port=toInteger(System.getProperty("org.github.dx88968.serverport"));
		if (port!=null) {
			DEFAULTSERVERPORT=port;
		}
		Integer sessiontimeout=toInteger(System.getProperty("org.github.dx88968.sessiontimeout"));
		if (sessiontimeout!=null) {
			SESSIONTIMEOUT=sessiontimeout;
		}
		Integer max_num_pipeline=toInteger(System.getProperty("org.github.dx88968.max_num_pipeline"));
		if (max_num_pipeline!=null) {
			MAX_NUM_PIPELINE=max_num_pipeline;
		}
		Integer max_num_session_each_pipeline=toInteger(System.getProperty("org.github.dx88968.max_num_session_each_pipeline"));
		if (max_num_session_each_pipeline!=null) {
			MAX_NUM_SESSION_EACH_PIPELINE=max_num_session_each_pipeline;
		}
		Integer max_thourghout=toInteger(System.getProperty("org.github.dx88968.max_thourghout"));
		if (max_thourghout!=null) {
			MAX_THOURGHOUT=max_thourghout;
		}
	}
	
	private static Integer toInteger(String tar){
		try {
			return Integer.parseInt(tar);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	public static final String htmlBeforeBody="<html xmlns='http://www.w3.org/1999/xhtml' xml:lang='en'>" +
			"<html>" +
			"<head>"+
		        "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>"+
		        "<title>Welcome to Spring Web MVC project</title>"+
		    "</head><body>";
	
	public static final String htmlTail="</body></html>";
	
	public static final String IDHEAD="/";
	public static final String IDInterval="_";
	public static final String TIMEInterval=" "; 
	public static final String ParamsInterval=";";
	public static final String BEWTEEN_CALSS_METHOD="#";
	public static final String END_SUMMERY=":";
	public static int MAX_NUM_PIPELINE=100;
	public static int MAX_NUM_SESSION_EACH_PIPELINE=10;
	public static int MAX_THOURGHOUT=10000;
	public static int SESSIONTIMEOUT=3;
	public static int DEFAULTSERVERPORT=9002;
}
