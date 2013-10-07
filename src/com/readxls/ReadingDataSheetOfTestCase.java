package com.readxls;

import java.util.Hashtable;

import com.config.CreatePropertiesObjects;
import com.logs.Logging;



public class ReadingDataSheetOfTestCase {

	public static Object[][] getData(String testName, XLReader xls){
		
		int counter = 0;
		
		// finding the row corresponding to test case
		int testStartRowNum = 1;
		while(!xls.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_DATA_SHEET_NAME"), 0, testStartRowNum).equals(testName)){
			testStartRowNum++;
			if(xls.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_DATA_SHEET_NAME"), 0, testStartRowNum).equals(testName)){
				counter++;
			}
				
		}
		
		if(counter==0 && testStartRowNum!=1){
			Logging.log("There is no data for this test case in Data sheet --> Skipping the test");
			return null;
		}
		
		int colStartRowNum = testStartRowNum+1;
		Logging.log("Test starts from row number - "+testStartRowNum+", for test case -"+testName);
		
		//finding number of rows of data, stored in rows variable
		int dataStartRowNum = testStartRowNum+2;
		int rows = 0;
		while(!xls.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_DATA_SHEET_NAME"), 0,dataStartRowNum+rows).equals(""))
			rows++;
		Logging.log("Total number of Data rows for test case - "+testName+"= "+rows);
		
		//finding no. of columns - stored indataStartRowNum testColumnCount variable
		int testColumnCount = 0;
		while(!xls.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_DATA_SHEET_NAME"), testColumnCount, testStartRowNum+1).equals(""))
			testColumnCount++;
		Logging.log("Total number of Columns for test case - "+testName+"= "+testColumnCount);
		
		Object data[][] = new Object[rows][1];
		Hashtable<String,String> testData = null ;
		int index = 0;
		//extract data
		for (int RowNum=dataStartRowNum; RowNum < dataStartRowNum+rows; RowNum++){
			testData = new Hashtable<String,String>();
			for(int cNum=0;cNum<testColumnCount;cNum++){
				String key = xls.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_DATA_SHEET_NAME"), cNum, colStartRowNum);
				String value = xls.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_DATA_SHEET_NAME"), cNum, RowNum);
				testData.put(key, value);
			}
			
			data[index][0] = testData;
			index++;
			Logging.log("*************");
			}
	
		return data;
	}
}
