package com.highlightelement;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HighlightElement {
	
	public void highlightElement(WebDriver driver, WebElement element,String result){
		if(result.equalsIgnoreCase("PASS")){
			for (int i = 0; i < 2; i++) { JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "color: yellow; border: 2px solid yellow;");
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
			}
		}
		else{
			for (int i = 0; i < 2; i++) { JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "color: red; border: 2px solid red;");
			js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
			}
		}
		
	} 
	
	


}
