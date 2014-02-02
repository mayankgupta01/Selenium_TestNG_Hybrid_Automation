package com.keywords;

import java.util.Hashtable;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.config.CreatePropertiesObjects;
import com.customexception.CustomException;
import com.highlightelement.HighlightElement;
import com.listeners.ErrorUtil;
import com.logs.Logging;
import com.objectanalyser.ObjectAnalyser;
import com.readxls.ReadingTestStepsWithRunmode;

public class ClickButton {
	public String doClickButton(String object, String input, Hashtable<String, String> data){
		String result = "";
		ClickButton cb = new ClickButton();
		if(data.get(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_DATA_COLUMN_BROWSER")).equals(CreatePropertiesObjects.CONFIG.getProperty("FIREFOX"))){
			result = cb.doClickAction(SetBaseState.driverff, object, input);
		}
		if(data.get(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_DATA_COLUMN_BROWSER")).equals(CreatePropertiesObjects.CONFIG.getProperty("CHROME"))){
			result = cb.doClickAction(SetBaseState.driverch, object, input);
		}
		if(data.get(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_DATA_COLUMN_BROWSER")).equals(CreatePropertiesObjects.CONFIG.getProperty("IE"))){
			result = cb.doClickAction(SetBaseState.driverie, object, input);
		}
		
		
		return result;
	}
	
	public String doClickAction(WebDriver driver, String object, String input){
		ObjectAnalyser oa = new ObjectAnalyser();
		String objectType = oa.doObjectAnalysis(object);
		HighlightElement h = new HighlightElement();
		String result = "";
		
		if(objectType.equalsIgnoreCase("xpath")){
			try{
				WebElement element = driver.findElement(By.xpath(CreatePropertiesObjects.OR.getProperty(object)));
				result = "PASS";
				h.highlightElement(driver, element, result);
				element.click();
			}catch(Exception e){
				Logging.log(String.format("Unable to find %s element to click on row number - %s in excel sheet", object, ReadingTestStepsWithRunmode.lastTestStepRowExecuted++));
				ErrorUtil.addVerificationFailure(new CustomException(String.format("Unable to find %s element to click on row number - %s in excel sheet", object, ReadingTestStepsWithRunmode.lastTestStepRowExecuted++)));
				result = "FAIL " + CreatePropertiesObjects.CONFIG.getProperty("DISCONTINUE_ON_FAIL");
			}
			 return result;
		}
		else if(objectType.equalsIgnoreCase("css")){
			try{
				WebElement element = driver.findElement(By.cssSelector(CreatePropertiesObjects.OR.getProperty(object)));
				result = "PASS";
				h.highlightElement(driver, element, result);
				element.click();
			}catch(Exception e){
				Logging.log(String.format("Unable to find %s element to click on row number - %s in excel sheet", object, ReadingTestStepsWithRunmode.lastTestStepRowExecuted++));
				ErrorUtil.addVerificationFailure(new CustomException(String.format("Unable to find %s element to click on row number - %s in excel sheet", object, ReadingTestStepsWithRunmode.lastTestStepRowExecuted++)));
				result = "FAIL " + CreatePropertiesObjects.CONFIG.getProperty("DISCONTINUE_ON_FAIL");
			}
			 return result;
		}
		return result;
	}
}
