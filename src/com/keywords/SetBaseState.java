package com.keywords;

import java.io.IOException;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import com.config.CreatePropertiesObjects;
import com.customexception.CustomException;
import com.listeners.ErrorUtil;
import com.listeners.WebDriverListener;
import com.logs.Logging;
public class SetBaseState {
	public static WebDriver wbdvff = null;
	public static  EventFiringWebDriver driverff = null;
	
	public static WebDriver wbdvch = null;
	public static  EventFiringWebDriver driverch = null;
	
	public static WebDriver wbdvie = null;
	public static  EventFiringWebDriver driverie = null;
	WebDriverListener myListener = new WebDriverListener();
	
	public String doSetBaseState(String Object, String DATA, Hashtable<String, String> data) throws ClientProtocolException, IOException{
		String result = "";
		int resp_code;
		// initializing the driver object
		if(wbdvff==null && (DATA.equalsIgnoreCase(CreatePropertiesObjects.CONFIG.getProperty("FIREFOX")) || DATA.equals(""))){
			try{	
					DesiredCapabilities capabilities = new DesiredCapabilities();
					capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
					if(CreatePropertiesObjects.CONFIG.getProperty("JAVASCRIPT_ENABLED").equalsIgnoreCase("true"))
						capabilities.setJavascriptEnabled(true);
					else
						capabilities.setJavascriptEnabled(false);
					if(!CreatePropertiesObjects.CONFIG.getProperty("PROXY").equalsIgnoreCase(""))
						capabilities.setCapability(CapabilityType.PROXY, CreatePropertiesObjects.CONFIG.getProperty("PROXY"));
					wbdvff = new FirefoxDriver(capabilities);
					driverff = new EventFiringWebDriver(wbdvff);
					driverff.register(myListener);
					driverff.manage().window().maximize();
					driverff.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
					driverff.manage().timeouts().pageLoadTimeout(Integer.parseInt(CreatePropertiesObjects.CONFIG.getProperty("PAGELOAD_PERFORMANCE_BENCHMARK")), TimeUnit.SECONDS);
					result = "PASS";
					
				}catch(Throwable e){
					ErrorUtil.addVerificationFailure(e);
					result = "FAIL - " + CreatePropertiesObjects.CONFIG.getProperty("DISCONTINUE_ON_FAIL");
				}
		}
			
			else if(wbdvch==null && DATA.equals(CreatePropertiesObjects.CONFIG.getProperty("CHROME"))){
				try{	
					DesiredCapabilities capabilities = new DesiredCapabilities();
						capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
						//capabilities.setCapability(CapabilityType.ENABLE_PERSISTENT_HOVERING, true);
						if(CreatePropertiesObjects.CONFIG.getProperty("JAVASCRIPT_ENABLED").equalsIgnoreCase("true"))
							capabilities.setJavascriptEnabled(true);
						else
							capabilities.setJavascriptEnabled(false);
						if(!CreatePropertiesObjects.CONFIG.getProperty("PROXY").equalsIgnoreCase(""))
							capabilities.setCapability(CapabilityType.PROXY, CreatePropertiesObjects.CONFIG.getProperty("PROXY"));
						System.setProperty("webdriver.chrome.driver", CreatePropertiesObjects.CONFIG.getProperty("CHROMEPATH")); 
						wbdvch = new ChromeDriver(capabilities);
						driverch = new EventFiringWebDriver(wbdvch);
						driverch.register(myListener);
						
						driverch.manage().window().maximize();
						driverch.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
						driverch.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
						/*driverch.manage().window().maximize();
						driverch.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);*/
						result = "PASS";
				}catch(Throwable e){
					ErrorUtil.addVerificationFailure(e);
					result = "FAIL - " + CreatePropertiesObjects.CONFIG.getProperty("DISCONTINUE_ON_FAIL");
				}
			}
				else if(DATA.equals(CreatePropertiesObjects.CONFIG.getProperty("IE"))){
					DesiredCapabilities capabilities = new DesiredCapabilities();
					capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
					if(CreatePropertiesObjects.CONFIG.getProperty("JAVASCRIPT_ENABLED").equalsIgnoreCase("true"))
						capabilities.setJavascriptEnabled(true);
					else
						capabilities.setJavascriptEnabled(false);
					if(!CreatePropertiesObjects.CONFIG.getProperty("PROXY").equalsIgnoreCase(""))
						capabilities.setCapability(CapabilityType.PROXY, CreatePropertiesObjects.CONFIG.getProperty("PROXY"));
					System.setProperty("webdriver.ie.driver", CreatePropertiesObjects.CONFIG.getProperty("IEPATH")); 
					wbdvie = new InternetExplorerDriver(capabilities);
					driverie = new EventFiringWebDriver(wbdvie);
					driverie.register(myListener);
					driverie.manage().window().maximize();
					driverie.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
					driverie.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
					result = "PASS";
				}
				
		if(DATA.equalsIgnoreCase(CreatePropertiesObjects.CONFIG.getProperty("FIREFOX"))){
			
			try{
				resp_code = Request.Get(CreatePropertiesObjects.CONFIG.getProperty("TESTURL")).execute().returnResponse().getStatusLine().getStatusCode();
				if(resp_code==200){
					Logging.log("The TESTURL in OR.properties is a valid URL");
					driverff.get(CreatePropertiesObjects.CONFIG.getProperty("TESTURL"));
				}
				else{
					Logging.log("The TESTURL in OR.properties is an INVALID URL, please check and update");
					ErrorUtil.addVerificationFailure(new CustomException("The TESTURL in OR.properties is an INVALID URL, please check and update"));
					result = "FAIL " + CreatePropertiesObjects.CONFIG.getProperty("DISCONTINUE_ON_FAIL");
				}
			}catch(Exception e){
				ErrorUtil.addVerificationFailure(new CustomException("The Server for the TestURL mentioned in OR.properties is down, please check "));
				result = "FAIL";
			}
			
			System.out.println(driverff.getTitle());
			if(driverff.getTitle().equals(CreatePropertiesObjects.CONFIG.getProperty("HOMETITLE"))){
				result="PASS - navigated successfully to testURL - " + CreatePropertiesObjects.CONFIG.getProperty("TESTURL");
			}
			else{
				result = "FAIL " + CreatePropertiesObjects.CONFIG.getProperty("DISCONTINUE_ON_FAIL");
				}
		}
		
		if(DATA.equalsIgnoreCase(CreatePropertiesObjects.CONFIG.getProperty("CHROME"))){
			
			try{
				resp_code = Request.Get(CreatePropertiesObjects.CONFIG.getProperty("TESTURL")).execute().returnResponse().getStatusLine().getStatusCode();
				if(resp_code==200){
					Logging.log("The TESTURL in OR.properties is a valid URL");
					driverch.get(CreatePropertiesObjects.CONFIG.getProperty("TESTURL"));
				}
				else{
					Logging.log("The TESTURL in OR.properties is an INVALID URL, please check and update");
					ErrorUtil.addVerificationFailure(new CustomException("The TESTURL in OR.properties is an INVALID URL, please check and update"));
					result = "FAIL " + CreatePropertiesObjects.CONFIG.getProperty("DISCONTINUE_ON_FAIL");
				}
			}catch(Exception e){
				ErrorUtil.addVerificationFailure(new CustomException("The Server for the TestURL mentioned in OR.properties is down, please check "));
				result = "FAIL";
			}
			if(driverch.getTitle().equals(CreatePropertiesObjects.CONFIG.getProperty("HOMETITLE"))){
				result="PASS - navigated successfully to testURL - " + CreatePropertiesObjects.CONFIG.getProperty("TESTURL");
			}
			else{
				result = "FAIL " + CreatePropertiesObjects.CONFIG.getProperty("DISCONTINUE_ON_FAIL");
			}
		}
		if(DATA.equalsIgnoreCase(CreatePropertiesObjects.CONFIG.getProperty("IE"))){
			
			try{
				resp_code = Request.Get(CreatePropertiesObjects.CONFIG.getProperty("TESTURL")).execute().returnResponse().getStatusLine().getStatusCode();
				if(resp_code==200){
					Logging.log("The TESTURL in OR.properties is a valid URL");
					driverie.get(CreatePropertiesObjects.CONFIG.getProperty("TESTURL"));
				}
				else{
					Logging.log("The TESTURL in OR.properties is an INVALID URL, please check and update");
					ErrorUtil.addVerificationFailure(new CustomException("The TESTURL in OR.properties is an INVALID URL, please check and update"));
					result = "FAIL " + CreatePropertiesObjects.CONFIG.getProperty("DISCONTINUE_ON_FAIL");
				}
			}catch(Exception e){
				ErrorUtil.addVerificationFailure(new CustomException("The Server for the TestURL mentioned in OR.properties is down, please check "));
				result = "FAIL";
			}
			if(driverie.getTitle().equals(CreatePropertiesObjects.CONFIG.getProperty("HOMETITLE"))){
				result="PASS - navigated successfully to testURL - " + CreatePropertiesObjects.CONFIG.getProperty("TESTURL");
			}
			else{
				result = "FAIL - not able to navigate to testURL - " + CreatePropertiesObjects.CONFIG.getProperty("TESTURL");
			}
		}
		return result;
	}
}
