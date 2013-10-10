package com.readxls;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Hashtable;

import org.testng.xml.XmlSuite;

import com.config.CreatePropertiesObjects;
import com.customexception.CustomException;
import com.logs.Logging;
import com.testcases.CreateTestCasesFromTestSuite;


public class ReadingTestSuiteXLWithRunmode {
	
	public static XLReader masterTestSuiteXL = null;
	public static Hashtable<String, XLReader> currentTestSuiteXL = new Hashtable<String, XLReader>();
	
	
	public static void main(String[] args) throws EmptyStackException, CustomException{
		String currentTestSuite;
		ArrayList<XmlSuite> suitePassedTorunTestNGXMLForAllSuites = new ArrayList<XmlSuite>();
		int rowNum = 2; //Initializing to 2 because data will start from row 2
		ArrayList<String> testCasesWithinTestSuite;
		XLReader testSuiteWithRunModeY;
		CreatePropertiesObjects.getPropertiesObjectInstance();
						
		Logging.log(CreatePropertiesObjects.XL.getProperty("EXCEL_FILE_PATH")+ CreatePropertiesObjects.XL.getProperty("MASTER_TEST_SUITE"));
			try{
					masterTestSuiteXL = new XLReader(CreatePropertiesObjects.XL.getProperty("EXCEL_FILE_PATH")+ CreatePropertiesObjects.XL.getProperty("MASTER_TEST_SUITE"));
					
				}catch(Exception e){
					throw new CustomException("The file " + CreatePropertiesObjects.XL.getProperty("MASTER_TEST_SUITE") + "cannot be found int the mentioned path -" +CreatePropertiesObjects.XL.getProperty("EXCEL_FILE_PATH"));
				}
				int rowCount = masterTestSuiteXL.getRowCount(CreatePropertiesObjects.XL.getProperty("MASTER_TEST_SUITE_SHEET_NAME"));
				
				if(rowCount > 0){
					for(;rowNum <= rowCount;rowNum++){
						
						currentTestSuite = (String)masterTestSuiteXL.getCellData(CreatePropertiesObjects.XL.getProperty("MASTER_TEST_SUITE_SHEET_NAME"), CreatePropertiesObjects.XL.getProperty("MASTER_TEST_SUITE_1ST_COLUMN"), rowNum);
						if(masterTestSuiteXL.getCellData(CreatePropertiesObjects.XL.getProperty("MASTER_TEST_SUITE_SHEET_NAME"), CreatePropertiesObjects.XL.getProperty("MASTER_TEST_SUITE_RUNMODE_COLUMN"), rowNum).equalsIgnoreCase(CreatePropertiesObjects.XL.getProperty("RUNMODE_YESVALUE"))){
							testSuiteWithRunModeY = new XLReader(CreatePropertiesObjects.XL.getProperty("EXCEL_FILE_PATH")+currentTestSuite+".xlsx");
							Logging.log("The current TestSuite with Runmode Y is " + currentTestSuite);
							Logging.log("The path to the current testsuite xl file is "+ CreatePropertiesObjects.XL.getProperty("EXCEL_FILE_PATH")+currentTestSuite+".xlsx");
							currentTestSuiteXL.put(currentTestSuite, testSuiteWithRunModeY);
							testCasesWithinTestSuite = CreateTestCasesFromTestSuite.CreateTestCases(currentTestSuite);
							if(testCasesWithinTestSuite.size() == 0){
								Logging.log("There are no testcases in the TestSuite " + currentTestSuite);
								throw  new CustomException("There are no testcases in the TestSuite " + currentTestSuite);
							}
							
							//Logging.log(currentTestSuite);
							//suitePassedTorunTestNGXMLForAllSuites.add(BootStrapCreateTestNGXML.createTestNGXMLForCurrentSuite(currentTestSuite, testCasesWithinTestSuite)); 
				        }
					}
				}
				else{
					Logging.log("There are no Test Suites in Master test suite excel");
					throw  new CustomException("There are no testsuites in the MasterTestSuite " + CreatePropertiesObjects.XL.getProperty("MASTER_TEST_SUITE"));
				}
						
			}
	

}
