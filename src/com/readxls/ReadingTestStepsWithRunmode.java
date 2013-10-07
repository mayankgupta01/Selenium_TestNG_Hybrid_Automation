package com.readxls;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;

import com.config.CreatePropertiesObjects;
import com.generatexlsreport.CreateXLReport;
import com.logs.Logging;

public class ReadingTestStepsWithRunmode {
	
	public static int lastTestStepRowExecuted=0;
	public ArrayList<String> testResultSet;
	public static Method method[];
	public static ArrayList<String> currentTestName = new ArrayList<String>();
	
	
	public static boolean getRunModeOfTestCase(String testName, XLReader xls){
		
		for(int rowNum=2;rowNum<=xls.getRowCount(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTCASE_SHEET_NAME"));rowNum++){
			if(xls.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTCASE_SHEET_NAME"), 0, rowNum).equals(testName)){
				if(xls.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTCASE_SHEET_NAME"), 1, rowNum).equalsIgnoreCase(CreatePropertiesObjects.XL.getProperty("RUNMODE_YESVALUE")))
					return true;
				else
					return false;
			}
						
		}
		return false;		
	}

	public static void executeTestStepsSerially(String testName, XLReader xls, Hashtable<String, String> data) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException{
		
		
		int sizeOfcurrentTestName;
		int startTestCaseExecutionFromRow = 2;
		int rowNum;
		
		ReadingTestStepsWithRunmode obj = new ReadingTestStepsWithRunmode();
		int testStepsSheetRowCount = xls.getRowCount(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"));
		
		
			// currentTestName.get(0).equals(testName) - this condition checks for iterations, after 1st iteration this condition will pass(first test case to be executed), for next test case both will fail
			if(lastTestStepRowExecuted==0 || currentTestName.get(0).equals(testName)){
				Logging.log(String.format("Execution starting from %s and ending at %s row number" , startTestCaseExecutionFromRow, testStepsSheetRowCount));
				//deleting past result set if any before starting execution
				//CreateXLReport.deletePastResultSet(lastTestStepRowExecuted, xls);
				obj.executeKeywordsInTestCase(testName, xls, startTestCaseExecutionFromRow, testStepsSheetRowCount);
				if(lastTestStepRowExecuted > 0){
					currentTestName.add(testName);
				}
				
			}
		
			else{
				currentTestName.add(testName);
				sizeOfcurrentTestName = currentTestName.size();
					if(!currentTestName.get(sizeOfcurrentTestName - 1).equals(currentTestName.get(sizeOfcurrentTestName - 2))){
						startTestCaseExecutionFromRow = lastTestStepRowExecuted+1;
						Logging.log(String.format("Execution starting from %s and ending at %s row number" , startTestCaseExecutionFromRow, testStepsSheetRowCount));
						obj.executeKeywordsInTestCase(testName, xls, startTestCaseExecutionFromRow, testStepsSheetRowCount);
						/*for(rowNum=lastTestStepRowExecuted+1; rowNum <= xls.getRowCount(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME")); rowNum++){
							if(xls.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), "TCID", rowNum).equals(testName)){
								String KEYWORD = xls.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), "KEYWORD", rowNum);
								String OBJECT = xls.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), "OBJECT", rowNum);
								String DATA = xls.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), "DATA", rowNum);
								
								ReadingTestStepsWithRunmode.method = KEYWORD.getClass().getMethods();
								for(int i=0;i < method.length; i++){
									try{
										if(method[i].getName().equals("do"+KEYWORD))
											rowResult=(String)method[i].invoke(KEYWORD, OBJECT,DATA);
											testResultSet.add(rowResult);
									}catch(Exception e){
										Logging.log("KEYWORD given in XL does not match with code");
										e.printStackTrace();
									}
										
								}
								lastTestStepRowExecuted = rowNum;
			         		}
							
						}*/
						
					}
					else{
						Logging.log(String.format("Execution starting from %s and ending at %s row number" , startTestCaseExecutionFromRow, lastTestStepRowExecuted));
						obj.executeKeywordsInTestCase(testName, xls, startTestCaseExecutionFromRow, lastTestStepRowExecuted);
						
						/*for(rowNum= startTestCaseExecutionFromRow; rowNum <= lastTestStepRowExecuted; rowNum++){
							if(xls.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), "TCID", rowNum).equals(testName)){
								String KEYWORD = xls.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), "KEYWORD", rowNum);
								String OBJECT = xls.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), "OBJECT", rowNum);
								String DATA = xls.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), "DATA", rowNum);
								
								ReadingTestStepsWithRunmode.method = KEYWORD.getClass().getMethods();
								for(int i=0;i < method.length; i++){
									try{
										if(method[i].getName().equals("do"+KEYWORD))
											rowResult=(String)method[i].invoke(KEYWORD, OBJECT,DATA);
											testResultSet.add(rowResult);
									}catch(Exception e){
										Logging.log(String.format("%s in XL does not match with code for object %s and data %s", KEYWORD, OBJECT, DATA));
										Logging.log(e.getMessage());
									}
										
								}
								lastTestStepRowExecuted = rowNum;
			         		}
							CreateXLReport.insertResultSetInTestSteps(testName,testResultSet,xls,lastTestStepRowExecuted);
						}*/
						
					}

			}


			}
	
	public void executeKeywordsInTestCase(String testName, XLReader xls, int startFromRow, int endAtRow ) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		int rowNum;
		String rowResult=null;
		testResultSet = new ArrayList<String>();
		
		for(rowNum=startFromRow; rowNum <= endAtRow; rowNum++){
			if(xls.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), "TCID", rowNum).equals(testName)){
				String KEYWORD = xls.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), "KEYWORD", rowNum);
				String OBJECT = xls.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), "OBJECT", rowNum);
				String DATA = xls.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), "DATA", rowNum);
				
				Object keyword = Class.forName("com.keywords." + KEYWORD).newInstance();
				ReadingTestStepsWithRunmode.method = keyword.getClass().getDeclaredMethods();
				
				// TO CHECK THAT RIGHT CLASS METHODS ARE COMING
				for( int j = 0; j< method.length; j++)
					System.out.println(method[j]);
				/*System.out.println(KEYWORD.getClass().getMethods());
				System.out.println(method.length);*/
				for(int i=0;i < method.length; i++){
					try{
						if(method[i].getName().equals("do"+KEYWORD))
							rowResult=(String)method[i].invoke(keyword,OBJECT,DATA);
							testResultSet.add(rowResult);
					}catch(Exception e){
						Logging.log(String.format("%s in XL does not match with code for object %s and data %s", KEYWORD, OBJECT, DATA));
						e.printStackTrace();
					}
						
				}
				lastTestStepRowExecuted = rowNum;
     		}
		}
		CreateXLReport.insertResultSetInTestSteps(testName,testResultSet,xls,lastTestStepRowExecuted);
		
	}
}
