package com.createTestNG;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import com.config.CreatePropertiesObjects;
import com.customexception.CustomException;
import com.listeners.TestsListenerAdapter;
import com.logs.Logging;
import com.readxls.XLReader;

public class BootStrapCreateTestNGXML {
	public static ArrayList<XmlSuite> suitePassedTorunTestNGXMLForAllSuites = new ArrayList<XmlSuite>();
	public static XLReader masterTestSuiteXL = null;
	public static Hashtable<String, XLReader> currentTestSuiteXL = new Hashtable<String, XLReader>();
	
	public static void main(String[] args) throws CustomException{
		String currentTestSuite;
		String currentTestName;
		ArrayList<XmlSuite> suitePassedTorunTestNGXMLForAllSuites = new ArrayList<XmlSuite>();
		int rowNum = 2; //Initializing to 2 because data will start from row 2
		ArrayList<String> testCasesWithinTestSuite = null;
		XLReader testSuiteWithRunModeY;
		CreatePropertiesObjects.getPropertiesObjectInstance();
		BootStrapCreateTestNGXML cr = new BootStrapCreateTestNGXML();
		
		Logging.log("The path to MasterTestSuite" + CreatePropertiesObjects.XL.getProperty("EXCEL_FILE_PATH")+ CreatePropertiesObjects.XL.getProperty("MASTER_TEST_SUITE"));
			try{
				masterTestSuiteXL = new XLReader(CreatePropertiesObjects.XL.getProperty("EXCEL_FILE_PATH")+ CreatePropertiesObjects.XL.getProperty("MASTER_TEST_SUITE"));
				
			}catch(Exception e){
				throw new CustomException("The file " + CreatePropertiesObjects.XL.getProperty("MASTER_TEST_SUITE") + "cannot be found int the mentioned path -" +System.getProperty("user.dir")+"\\src\\com\\xlfiles\\");
			}
		int rowCount = masterTestSuiteXL.getRowCount(CreatePropertiesObjects.XL.getProperty("MASTER_TEST_SUITE_SHEET_NAME"));
		if(rowCount > 0){
			for(;rowNum <= rowCount;rowNum++){
				currentTestSuite = (String)masterTestSuiteXL.getCellData(CreatePropertiesObjects.XL.getProperty("MASTER_TEST_SUITE_SHEET_NAME"), CreatePropertiesObjects.XL.getProperty("MASTER_TEST_SUITE_1ST_COLUMN"), rowNum);
				testCasesWithinTestSuite = new ArrayList<String>();
					if(masterTestSuiteXL.getCellData(CreatePropertiesObjects.XL.getProperty("MASTER_TEST_SUITE_SHEET_NAME"), CreatePropertiesObjects.XL.getProperty("MASTER_TEST_SUITE_RUNMODE_COLUMN"), rowNum).equalsIgnoreCase(CreatePropertiesObjects.XL.getProperty("RUNMODE_YESVALUE"))){
						try{
							testSuiteWithRunModeY = new XLReader(CreatePropertiesObjects.XL.getProperty("EXCEL_FILE_PATH")+currentTestSuite+".xlsx");
						}catch(Exception e){
							throw new CustomException("Expected excel file" + currentTestSuite + "at location " + System.getProperty("user.dir")+"\\src\\com\\xlfiles -- was not found");
						}
						
						Logging.log("The current testsuite with Runmode = Y is " +currentTestSuite);
						Logging.log("The path of the excel of current Test Suite" + CreatePropertiesObjects.XL.getProperty("EXCEL_FILE_PATH")+currentTestSuite+".xlsx");
						currentTestSuiteXL.put(currentTestSuite, testSuiteWithRunModeY);
						for(int i= 2; i <= testSuiteWithRunModeY.getRowCount(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTCASE_SHEET_NAME")); i++ ){
							currentTestName = testSuiteWithRunModeY.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTCASE_SHEET_NAME"), 0, i);
							testCasesWithinTestSuite.add(currentTestName);
					}
						if(testCasesWithinTestSuite.size() == 0){
							Logging.log("There are no testcases in the TestSuite " + currentTestSuite);
							throw  new CustomException("There are no testcases in the TestSuite " + currentTestSuite);
						}
						
						XmlSuite currentSuite = new XmlSuite();
						currentSuite.setName(currentTestSuite);
						XmlTest addTests = new XmlTest(currentSuite);
						XmlClass xcTest = null;
						addTests.setName(currentTestSuite + "Tests");
						
						List<XmlClass> addClasses = new ArrayList<XmlClass>();
						for( int i = 0; i < testCasesWithinTestSuite.size(); i++){
							xcTest = new XmlClass("com.testcases." + currentTestSuite + "." + testCasesWithinTestSuite.get(i));
							//addClasses.add(new XmlClass(System.getProperty("user.dir")+"\\src\\com\\testcases\\" + currentTestSuite + "\\" + testCases.get(i) + ".java"));
							addClasses.add(xcTest);
							
						}
						addTests.setXmlClasses(addClasses);
						suitePassedTorunTestNGXMLForAllSuites.add(currentSuite); 
					//Logging.log(currentTestSuite);
					//suitePassedTorunTestNGXMLForAllSuites.add(BootStrapCreateTestNGXML.createTestNGXMLForCurrentSuite(currentTestSuite, testCasesWithinTestSuite)); 
		        }
			}
		}
		else{
			Logging.log("There are no Test Suites in Master test suite excel");
			throw  new CustomException("There are no testsuites in the MasterTestSuite " + CreatePropertiesObjects.XL.getProperty("MASTER_TEST_SUITE"));
		}
		
		cr.runTestNGXMLForAllSuites(suitePassedTorunTestNGXMLForAllSuites);
	}
	
	public static void runTestNGXMLForAllSuites(ArrayList<XmlSuite> currentSuite) throws CustomException{
		//List<XmlSuite> suites = new ArrayList<XmlSuite>();
		//suites.add(currentSuite);
		if(currentSuite == null){
			Logging.log("There are no test cases in testNG suite");
			throw new CustomException("There are no test cases in testNG suite");
		}
		else{
		TestNG tng = new TestNG();
		tng.addListener(new TestsListenerAdapter());
		tng.setThreadCount(5);
		tng.setXmlSuites(currentSuite);
		tng.run();
		}
	}
}
