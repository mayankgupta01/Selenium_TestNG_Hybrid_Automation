package com.listeners;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;

import com.logs.Logging;

public class WebDriverListener extends AbstractWebDriverEventListener {
	
	public void onException(Throwable e, WebDriver driver){
		ErrorUtil.addVerificationFailure(e);
		Logging.log("Exception encountered while action was being done by WebDriver object");
		
	}
}
