package com.keywords;



import org.testng.Assert;

import com.listeners.ErrorUtil;

public class InputText {

	public String doInputText(String Object, String Input){
		String result = "PASS";
		if(result.equals("FAIL")){
			try{
				Assert.fail();
			}catch(Throwable t){
				ErrorUtil.addVerificationFailure(t);
			}
			
		}
		return result;
	}
}
