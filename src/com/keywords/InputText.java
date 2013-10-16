package com.keywords;



import java.util.Hashtable;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.config.CreatePropertiesObjects;
import com.highlightelement.HighlightElement;
import com.listeners.ErrorUtil;
import com.takescreenshot.TakeScreenshot;

public class InputText {

	public String doInputText(String object, String input, Hashtable<String, String> data){
		String result = "";
		HighlightElement h = new HighlightElement();
		
		
		if(data.get(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_DATA_COLUMN_BROWSER")).equals(CreatePropertiesObjects.CONFIG.getProperty("FIREFOX"))){
			try{
				WebElement element = SetBaseState.driverff.findElement(By.xpath(CreatePropertiesObjects.OR.getProperty(object)));
				element.sendKeys(input);
				result="PASS";
				h.highlightElement(SetBaseState.wbdvff, element, result);
			}catch(Exception e){
				result="FAIL "+ CreatePropertiesObjects.CONFIG.getProperty("DISCONTINUE_ON_FAIL");
			}
		}
		
		else if(data.get(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_DATA_COLUMN_BROWSER")).equals(CreatePropertiesObjects.CONFIG.getProperty("CHROME"))){
			try{
				WebElement element = SetBaseState.driverch.findElement(By.xpath(CreatePropertiesObjects.OR.getProperty(object)));
				result="PASS";
				h.highlightElement(SetBaseState.wbdvch, element, result);
				element.sendKeys(input);
			}catch(Exception e){
				ErrorUtil.addVerificationFailure(e);
				result="FAIL "+ CreatePropertiesObjects.CONFIG.getProperty("DISCONTINUE_ON_FAIL") + "Error details - " + e.getMessage();
			}
		}
				
		
		return result;
	}
}
