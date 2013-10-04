package com.readxls;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Hashtable;

import org.testng.xml.XmlSuite;

import com.config.CreatePropertiesObjects;
import com.createTestNG.CreateTestNGXML;
import com.logs.Logging;
import com.testcases.CreateTestCasesFromTestSuite;


public class ReadingTestSuiteXLWithRunmode {
	
	public static XLReader masterTestSuiteXL = null;
	public static Hashtable<String, XLReader> currentTestSuiteXL = new Hashtable<String, XLReader>();
	
	
	
	public static void main(String[] args) throws EmptyStackException{
		
		String currentTestSuite;
		ArrayList<XmlSuite> suitePassedTorunTestNGXMLForAllSuites = new ArrayList<XmlSuite>();
		int rowNum = 2; //Initializing to 2 because data will start from row 2
		ArrayList<String> testCasesWithinTestSuite;
		XLReader testSuiteWithRunModeY;
		CreatePropertiesObjects.getPropertiesObjectInstance();
		
			try{
				
				Logging.log(System.getProperty("user.dir")+"\\src\\com\\xlfiles\\"+ CreatePropertiesObjects.XL.getProperty("MASTER_TEST_SUITE"));
				masterTestSuiteXL = new XLReader(System.getProperty("user.dir")+"\\src\\com\\xlfiles\\"+ CreatePropertiesObjects.XL.getProperty("MASTER_TEST_SUITE"));
				int rowCount = masterTestSuiteXL.getRowCount(CreatePropertiesObjects.XL.getProperty("MASTER_TEST_SUITE_SHEET_NAME"));
				System.out.println(rowCount);
				if(rowCount > 0){
					for(;rowNum <= rowCount;rowNum++){
						
						currentTestSuite = (String)masterTestSuiteXL.getCellData(CreatePropertiesObjects.XL.getProperty("MASTER_TEST_SUITE_SHEET_NAME"), CreatePropertiesObjects.XL.getProperty("MASTER_TEST_SUITE_1ST_COLUMN"), rowNum);
						if(masterTestSuiteXL.getCellData(CreatePropertiesObjects.XL.getProperty("MASTER_TEST_SUITE_SHEET_NAME"), CreatePropertiesObjects.XL.getProperty("MASTER_TEST_SUITE_RUNMODE_COLUMN"), rowNum).equalsIgnoreCase(CreatePropertiesObjects.XL.getProperty("RUNMODE_YESVALUE"))){
							testSuiteWithRunModeY = new XLReader(System.getProperty("user.dir")+"\\src\\com\\xlfiles\\"+currentTestSuite+".xlsx");
							Logging.log(currentTestSuite);
							Logging.log(System.getProperty("user.dir")+"\\src\\com\\xlfiles\\"+currentTestSuite+".xlsx");
							currentTestSuiteXL.put(currentTestSuite, testSuiteWithRunModeY);
							testCasesWithinTestSuite = CreateTestCasesFromTestSuite.CreateTestCases(currentTestSuite);
							if(testCasesWithinTestSuite.size() == 0){
								throw  new EmptyStackException();
							}
							Logging.log(currentTestSuite);
							Logging.log(testCasesWithinTestSuite.get(0));
							suitePassedTorunTestNGXMLForAllSuites.add(CreateTestNGXML.createTestNGXMLForCurrentSuite(currentTestSuite, testCasesWithinTestSuite)); 
				        }
					}
				}
				else{
					Logging.log("There are no Test Suites in Master test suite excel");
				}
			}catch(Exception e){
				Logging.log("Unable to find xl files or there are no entries in the Master test suite/individual test suite files");
				e.printStackTrace();
			}
			
			try{
				int rowCount = masterTestSuiteXL.getRowCount(CreatePropertiesObjects.XL.getProperty("MASTER_TEST_SUITE_SHEET_NAME"));
				if(rowCount > 0)
				CreateTestNGXML.runTestNGXMLForAllSuites(suitePassedTorunTestNGXMLForAllSuites);
				else
					throw  new EmptyStackException();
				
			}catch(Throwable t){
			   Logging.log("The TestNG execution encountered a problem OR there are no entries in the Master test suite file");	
			}
	}
	
	

}
