package com.objectanalyser;

public class ObjectAnalyser {
	public String doObjectAnalysis(String objectKey){
		StringBuilder sb = new StringBuilder();
		String locatorType= "";
		
		if(objectKey.startsWith("//")){
			locatorType="xpath";
		}
		else if(objectKey.contains("[") && !objectKey.contains("@")){
			locatorType="css";
		}
		return locatorType;
	}
}
