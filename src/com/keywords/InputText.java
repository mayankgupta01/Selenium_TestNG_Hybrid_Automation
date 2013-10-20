package com.keywords;



import java.util.Hashtable;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.config.CreatePropertiesObjects;
import com.highlightelement.HighlightElement;
import com.listeners.ErrorUtil;
import com.objectanalyser.ObjectAnalyser;

public class InputText {

	public String doInputText(String object, String input, Hashtable<String, String> data){
		String result = "";
		InputText it = new InputText();
				
		if(data.get(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_DATA_COLUMN_BROWSER")).equals(CreatePropertiesObjects.CONFIG.getProperty("FIREFOX"))){
			result = it.actOnObject(SetBaseState.driverff, object, input);

		}
		
		else if(data.get(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_DATA_COLUMN_BROWSER")).equals(CreatePropertiesObjects.CONFIG.getProperty("CHROME"))){
			result = it.actOnObject(SetBaseState.driverch, object, input);
		}
		
		else if(data.get(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_DATA_COLUMN_BROWSER")).equals(CreatePropertiesObjects.CONFIG.getProperty("IE"))){
			result = it.actOnObject(SetBaseState.driverie, object, input);
		}
				
		return result;
	}
	
	
	public String actOnObject(WebDriver driver, String object, String input){
		ObjectAnalyser oa = new ObjectAnalyser();
		String objectType = oa.doObjectAnalysis(object);
		HighlightElement h = new HighlightElement();
		String result = "";
		if(objectType.equalsIgnoreCase("xpath")){
			try{
				WebElement element = driver.findElement(By.xpath(CreatePropertiesObjects.OR.getProperty(object)));
				element.sendKeys(input);
				result="PASS";
				h.highlightElement(driver, element, result);
			}catch(Exception e){
				result="FAIL "+ CreatePropertiesObjects.CONFIG.getProperty("DISCONTINUE_ON_FAIL");
			}
		}
		else if(objectType.equalsIgnoreCase("css")){
			try{
				WebElement element = driver.findElement(By.cssSelector(CreatePropertiesObjects.OR.getProperty(object)));
				element.sendKeys(input);
				result="PASS";
				h.highlightElement(driver, element, result);
			}catch(Exception e){
				result="FAIL "+ CreatePropertiesObjects.CONFIG.getProperty("DISCONTINUE_ON_FAIL");
			}
		}
		return result;
	}
}
