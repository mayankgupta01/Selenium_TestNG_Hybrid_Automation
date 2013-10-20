package com.objectanalyser;

import com.config.CreatePropertiesObjects;

public class ObjectAnalyser {
	public String doObjectAnalysis(String objectKey){
		StringBuilder sb = new StringBuilder();
		String locatorType= "";
		
		if(CreatePropertiesObjects.OR.getProperty(objectKey).startsWith("//")){
			locatorType="xpath";
		}
		else if(CreatePropertiesObjects.OR.getProperty(objectKey).contains("[") && !objectKey.contains("@")){
			locatorType="css";
		}
		return locatorType;
	}
}
