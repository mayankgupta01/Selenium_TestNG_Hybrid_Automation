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
				Logging.log("The testcase name is " + xl.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTCASE_SHEET_NAME"), 0, rowNum));
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
						createJavaTestCaseFiles(testCase, currentTestSuite,xl, testCaseNames.get(rowNum - 2));
						
				}
					
			}
		}catch(Exception e){
			Logging.log("Unable to create Folder or java file");
		}
		return testCaseNames;
	}

	public static void createJavaTestCaseFiles(File newTestCase, String currentTSname, XLReader xls, String testCaseName){
		StringBuilder sbimport = new StringBuilder();
		StringBuilder sbclass = new StringBuilder();
		StringBuilder sbclass2 = new StringBuilder();
		
			try{
			PrintWriter print = new PrintWriter(newTestCase);
			print.println("package com.testcases" + "."+ currentTSname +";");
			BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir")+"\\resources\\templates\\import.template"));
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
	
		        
		       br = new BufferedReader(new FileReader(System.getProperty("user.dir")+"\\resources\\templates\\testclass1.template"));
			   line = br.readLine();
	
	            while (line != null) {
		            sbclass.append(line);
		            sbclass.append('\n');
		            line = br.readLine();
		        }
	        everything = sbclass.toString();
	    	print.println(sbclass);
	    	print.println("public void do" +  testCaseName +"(Hashtable<String,String> data) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException, CustomException{");
			for(int row = 2; row <= xls.getRowCount(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTCASE_SHEET_NAME")); row++){
				if(xls.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTCASE_SHEET_NAME"), 0, row).equals(testCaseName)){
					if(xls.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTCASE_SHEET_NAME"), 2, row).equalsIgnoreCase("Y")){
						br = new BufferedReader(new FileReader(System.getProperty("user.dir")+"\\resources\\templates\\testclass2parallel.template"));
						   line = br.readLine();
				
				            while (line != null) {
				            	sbclass2.append(line);
				            	sbclass2.append('\n');
					            line = br.readLine();
					        }
				        everything = sbclass2.toString();
				    	print.println(sbclass2);
				    	
						print.flush();
					}else{
						br = new BufferedReader(new FileReader(System.getProperty("user.dir")+"\\resources\\templates\\testclass2.template"));
						   line = br.readLine();
				
				            while (line != null) {
				            	sbclass2.append(line);
				            	sbclass2.append('\n');
					            line = br.readLine();
					        }
				        everything = sbclass2.toString();
				    	print.println(sbclass2);
				    	
						print.flush();
					}
				}
			}
	    	 
			
			
			}catch(Exception e){
		Logging.log("Unable to locate text file import.template or testclass.template OR unable to locate the targeted .java file");
			}
	}
}
