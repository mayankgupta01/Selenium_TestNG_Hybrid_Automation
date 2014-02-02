package com.keywords;

import java.util.Hashtable;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.config.CreatePropertiesObjects;
import com.highlightelement.HighlightElement;
import com.objectanalyser.ObjectAnalyser;

public class BusinessSignOut {
	public String doBusinessSignOut(String object, String input, Hashtable<String, String> data){
		String result = "";
		BusinessSignOut so = new BusinessSignOut();
		if(data.get(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_DATA_COLUMN_BROWSER")).equals(CreatePropertiesObjects.CONFIG.getProperty("FIREFOX"))){
			result = so.actOnObject(SetBaseState.driverff, object);

		}
		
		else if(data.get(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_DATA_COLUMN_BROWSER")).equals(CreatePropertiesObjects.CONFIG.getProperty("CHROME"))){
			result = so.actOnObject(SetBaseState.driverch, object);
		}
		
		else if(data.get(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_DATA_COLUMN_BROWSER")).equals(CreatePropertiesObjects.CONFIG.getProperty("IE"))){
			result = so.actOnObject(SetBaseState.driverie, object);
		}
		return result;
	}
	
	public String actOnObject(WebDriver driver, String object){
		String result = "";
		String objects[] = object.split(",");
		ObjectAnalyser oa = new ObjectAnalyser();
		String firstObjectType = oa.doObjectAnalysis(objects[0]);
		HighlightElement h = new HighlightElement();
		Actions act = new Actions(driver);
		WebDriverWait wait =  new WebDriverWait(driver, 20);
		
		if(firstObjectType.equalsIgnoreCase("xpath")){
			try{
				WebElement elementRoot = driver.findElement(By.xpath(CreatePropertiesObjects.OR.getProperty(objects[0])));
				WebElement elementSub = driver.findElement(By.xpath(CreatePropertiesObjects.OR.getProperty(objects[1])));
				act.moveToElement(elementRoot).build().perform();
				wait.until(ExpectedConditions.visibilityOf(elementSub));
			//	h.highlightElement(driver, elementSub, result);
				elementSub.click();
				result="PASS";
				
			}catch(Exception e){
				driver.get(CreatePropertiesObjects.CONFIG.getProperty("TESTURL")+"/j_spirng_security_logout");
				result = "PASS";
				//result="FAIL "+ CreatePropertiesObjects.CONFIG.getProperty("DISCONTINUE_ON_FAIL");
			}
		}
		else if(firstObjectType.equalsIgnoreCase("css")){
			try{
				WebElement elementRoot = driver.findElement(By.cssSelector(CreatePropertiesObjects.OR.getProperty(objects[0])));
				WebElement elementSub = driver.findElement(By.cssSelector(CreatePropertiesObjects.OR.getProperty(objects[1])));
				act.moveToElement(elementRoot).build().perform();
				h.highlightElement(driver, elementSub, result);
				elementSub.click();
				result="PASS";
			}catch(Exception e){
				result="FAIL "+ CreatePropertiesObjects.CONFIG.getProperty("DISCONTINUE_ON_FAIL");
			}
		}
		
		return result;
	}
}
