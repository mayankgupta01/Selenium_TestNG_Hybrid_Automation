package com.keywords;

import java.util.Hashtable;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.config.CreatePropertiesObjects;
import com.customexception.CustomException;
import com.highlightelement.HighlightElement;
import com.listeners.ErrorUtil;
import com.logs.Logging;
import com.objectanalyser.ObjectAnalyser;

public class SearchWebTableColumn {
	

	public String doSearchWebTableColumn(String object, String input, Hashtable<String, String> data){
		String result = "";
		SearchWebTableColumn swt = new SearchWebTableColumn();
				
		if(data.get(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_DATA_COLUMN_BROWSER")).equals(CreatePropertiesObjects.CONFIG.getProperty("FIREFOX"))){
			result = swt.actOnObject(SetBaseState.driverff, object, input);

		}
		
		else if(data.get(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_DATA_COLUMN_BROWSER")).equals(CreatePropertiesObjects.CONFIG.getProperty("CHROME"))){
			result = swt.actOnObject(SetBaseState.driverch, object, input);
		}
		
		else if(data.get(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_DATA_COLUMN_BROWSER")).equals(CreatePropertiesObjects.CONFIG.getProperty("IE"))){
			result = swt.actOnObject(SetBaseState.driverie, object, input);
		}
				
		return result;
	}
	
	
	public String actOnObject(WebDriver driver, String object, String input){
		String result = "";
		String objects[] = object.split(",");
		ObjectAnalyser oa = new ObjectAnalyser();
		String firstObjectType = oa.doObjectAnalysis(objects[0]);
		int rowCount;
		HighlightElement h = new HighlightElement();
		
		if(firstObjectType.equalsIgnoreCase("xpath")){
			
				List<WebElement> elementTable = driver.findElements(By.xpath(CreatePropertiesObjects.OR.getProperty(objects[0])));
				rowCount = elementTable.size();
				if(rowCount > 0){
					for(int i = 1; i <= rowCount; i++){
						WebElement cellData = driver.findElement(By.xpath(CreatePropertiesObjects.OR.getProperty(objects[0]) + "[" +i + "]" + CreatePropertiesObjects.OR.getProperty(objects[1])));
						try{
							boolean flag = cellData.getText().toLowerCase().contains(input.toLowerCase().trim());
							if(!flag){
								Logging.log(String.format("Input string %s not found in row number - %s" ,input, i));
								result="FAIL";
								throw new CustomException(String.format("Input string %s not found in Web table row number - %s" ,input, i));
							}
							else{
								Logging.log(String.format("Input string %s found in row number - %s" ,input, i));
								result = "PASS";
							}
						}catch(Throwable e){
							ErrorUtil.addVerificationFailure(e);
						}
						h.highlightElement(driver, cellData, result);
						if(!result.equalsIgnoreCase("FAIL")){
						result="PASS";
						}
					}
				}
				else{
					Logging.log("There are no search results for the search string - " + input);
					result = "FAIL";
				}
		}
		else if(firstObjectType.equalsIgnoreCase("css")){
			
			List<WebElement> elementTable = driver.findElements(By.cssSelector(CreatePropertiesObjects.OR.getProperty(objects[0])));
			rowCount = elementTable.size();
			if(rowCount > 0){
				for(int i = 1; i <= rowCount; i++){
					WebElement cellData = driver.findElement(By.cssSelector(CreatePropertiesObjects.OR.getProperty(objects[0]) + "[" +i + "]" + CreatePropertiesObjects.OR.getProperty(objects[1])));
					try{
						boolean flag = cellData.getText().contains(input.trim());
						if(!flag){
							Logging.log(String.format("Input string %s not found in row number - %s" ,input, i));
							result="FAIL";
							throw new CustomException(String.format("Input string %s not found in Web table row number - %s" ,input, i));
						}
						else{
							Logging.log(String.format("Input string %s found in row number - %s" ,input, i));
							result = "PASS";
						}
					}catch(Throwable e){
						ErrorUtil.addVerificationFailure(e);
					}
					h.highlightElement(driver, cellData, result);
					if(!result.equalsIgnoreCase("FAIL")){
					result="PASS";
					}
				}
			}
			else{
				Logging.log("There are no search results for the searh string - " + input);
				result = "FAIL";
			}
		}
		return result;
	}
}

