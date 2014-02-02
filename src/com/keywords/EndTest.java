package com.keywords;

import java.util.Hashtable;

import com.listeners.ErrorUtil;
import com.logs.Logging;

public class EndTest {

	public String doEndTest(String object, String input, Hashtable<String, String> data){
		String result ="";
		try{
			if(SetBaseState.wbdvff!=null){
				SetBaseState.driverff.quit();
		    }
			if(SetBaseState.wbdvch!=null){
				SetBaseState.driverch.quit();
		    }
		    if(SetBaseState.wbdvie!=null){
				SetBaseState.driverie.quit();
		    }
		    result = "PASS";
		}catch(Exception e){
			Logging.log("Exception occured while closing the browsers - inside the doEndTest");
			ErrorUtil.addVerificationFailure(e);
			result = "FAIL";
		}
		
		
		
		return result;
		
	}
}
