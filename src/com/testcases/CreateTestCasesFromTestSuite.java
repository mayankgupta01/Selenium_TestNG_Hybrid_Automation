package com.testcases;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.config.CreatePropertiesObjects;
import com.logs.Logging;
import com.readxls.ReadingTestSuiteXLWithRunmode;
import com.readxls.XLReader;

public class CreateTestCasesFromTestSuite {


	public static ArrayList<String> CreateTestCases(String currentTestSuite) {
		XLReader xl = ReadingTestSuiteXLWithRunmode.currentTestSuiteXL.get(currentTestSuite);
		ArrayList<String> testCaseNames = new ArrayList<String>();
		File testSuiteFolder;
		File testCase;
		boolean flag;
		int rowCount = xl.getRowCount(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTCASE_SHEET_NAME"));
		try{
			if(rowCount == 0){
				Logging.log("Cannot proceed as there are no testcases in the test suite");
				return null;
			}
					
			for (int rowNum = 2; rowNum <= xl.getRowCount(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTCASE_SHEET_NAME")); rowNum++){
				Logging.log(xl.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTCASE_SHEET_NAME"), 0, rowNum));
				testCaseNames.add(xl.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTCASE_SHEET_NAME"), 0, rowNum));
				testSuiteFolder = new File(System.getProperty("user.dir")+"\\src\\com\\testcases\\"+currentTestSuite);
				    if(!testSuiteFolder.exists()){
						flag = testSuiteFolder.mkdir();
						Logging.log("Test Suite created true/false? " + flag);
					}
					
				testCase = testSuiteFolder;
				testCase = new File(System.getProperty("user.dir")+"\\src\\com\\testcases\\" + currentTestSuite +"\\" + testCaseNames.get(rowNum - 2) +".java");
					if(!testCase.exists()){
						testCase.createNewFile();
						Logging.log("File has been created " + testCaseNames.get(rowNum - 2) );
						createJavaTestCaseFiles(testCase, currentTestSuite, testCaseNames.get(rowNum - 2));
						
					}
					
			}
		}catch(Exception e){
			Logging.log("Unable to create Folder or java file");
		}
		return testCaseNames;
	}

	public static void createJavaTestCaseFiles(File newTestCase, String currentTSname, String testCaseName){
		StringBuilder sbimport = new StringBuilder();
		StringBuilder sbclass = new StringBuilder();
			try{
			PrintWriter print = new PrintWriter(newTestCase);
			print.println("package com.testcases" + "."+ currentTSname +";");
			BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir")+"\\src\\com\\config\\import.txt"));
			 String line = br.readLine();
	
		        while (line != null) {
		            sbimport.append(line);
		            sbimport.append('\n');
		            line = br.readLine();
		        }
		        String everything = sbimport.toString();
		        print.println(sbimport);
		        print.println("public class " +  testCaseName +"{");
		        print.println("public String testName = \"" + testCaseName +"\";");
		        print.println("public String currentTestSuite = \"" + currentTSname +"\";");
	
		        
		       br = new BufferedReader(new FileReader(System.getProperty("user.dir")+"\\src\\com\\config\\testclass.txt"));
			   line = br.readLine();
	
	            while (line != null) {
		            sbclass.append(line);
		            sbclass.append('\n');
		            line = br.readLine();
		        }
	        everything = sbclass.toString();
	    	print.println(sbclass);
			
			print.flush();
			
			
			}catch(Exception e){
		Logging.log("Unable to locate text file import.txt or testclass.txt OR unable to locate the targeted .java file");
			}
	}
}
