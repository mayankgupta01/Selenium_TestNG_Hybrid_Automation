package com.logs;

import org.apache.log4j.Logger;





public class Logging {

	public static Logger APPLICATION_LOGS = Logger.getLogger("devpinoyLogger");
	
	public static void log(String debug){
		APPLICATION_LOGS.debug(debug);
	}
}
