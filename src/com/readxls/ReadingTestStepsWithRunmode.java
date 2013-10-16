package com.readxls;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import com.config.CreatePropertiesObjects;
import com.customexception.CustomException;
import com.generatexlsreport.CreateXLReport;
import com.keywords.SetBaseState;
import com.listeners.ErrorUtil;
import com.logs.Logging;
import com.takescreenshot.TakeScreenshot;

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

	public static void executeTestStepsSerially(String testName,String currentTestSuite, XLReader xls, Hashtable<String, String> data) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException, CustomException{
		
		
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
				obj.executeKeywordsInTestCase(testName,currentTestSuite, xls, startTestCaseExecutionFromRow, testStepsSheetRowCount, data);
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
						obj.executeKeywordsInTestCase(testName,currentTestSuite, xls, startTestCaseExecutionFromRow, testStepsSheetRowCount, data);
						
						
					}
					else{
						Logging.log(String.format("Execution starting from %s and ending at %s row number" , startTestCaseExecutionFromRow, lastTestStepRowExecuted));
						obj.executeKeywordsInTestCase(testName,currentTestSuite, xls, startTestCaseExecutionFromRow, lastTestStepRowExecuted, data);
     				}

			   }


			}
	
	public void executeKeywordsInTestCase(String testName, String currentTestSuite, XLReader xls, int startFromRow, int endAtRow, Hashtable<String, String> data ) throws InstantiationException, IllegalAccessException, ClassNotFoundException, CustomException{
		int rowNum;
		String rowResult=null;
		testResultSet = new ArrayList<String>();
		TakeScreenshot ts = new TakeScreenshot();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy h-mm-ss a");
		String formatteddate = sdf.format(date);
		
		for(rowNum=startFromRow; rowNum <= endAtRow; rowNum++){

			if(xls.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), "TCID", rowNum).equals(testName)){
				String KEYWORD = xls.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), "KEYWORD", rowNum);
				String OBJECT = xls.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), "OBJECT", rowNum);
				String DATA = xls.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), "DATA", rowNum);
				//System.out.println("THe value in data sheet ----->" + data.get(DATA));
					if(data.get(DATA) != null){
						DATA = data.get(DATA);
					}
				
				Object keyword = Class.forName("com.keywords." + KEYWORD).newInstance();
				ReadingTestStepsWithRunmode.method = keyword.getClass().getDeclaredMethods();
				
				// TO CHECK THAT RIGHT CLASS METHODS ARE COMING
				/*for( int j = 0; j< method.length; j++)
					System.out.println(method[j]);*/
				/*System.out.println(KEYWORD.getClass().getMethods());
				System.out.println(method.length);*/
				
					for(int i=0;i < method.length; i++){
						try{
							if(method[i].getName().equals("do"+KEYWORD))
								rowResult=(String)method[i].invoke(keyword,OBJECT,DATA, data);
								testResultSet.add(rowResult);
						}catch(Exception e){
							Logging.log(String.format("%s in XL does not match with code for object %s and data %s", KEYWORD, OBJECT, DATA));
							e.printStackTrace();
						    }
						if(rowResult.contains(CreatePropertiesObjects.CONFIG.getProperty("DISCONTINUE_ON_FAIL"))){
							lastTestStepRowExecuted = rowNum;
							currentTestName.add(testName);
							if(data.get("BROWSER").equals("1")){
								ts.takeScreenShot(currentTestSuite+"_"+ testName +"_rownum_" +rowNum +"_"+ KEYWORD + "_"+formatteddate, SetBaseState.driverff);
							}
							else if(data.get("BROWSER").equals("2")){
								ts.takeScreenShot(currentTestSuite+"_"+testName +"_rownum_" +rowNum +"_"+ KEYWORD + "_"+formatteddate, SetBaseState.wbdvch);
							}
							else if(data.get("BROWSER").equals("3")){
								ts.takeScreenShot(currentTestSuite+"_"+testName +"_rownum_" +rowNum +"_"+ KEYWORD + "_"+formatteddate, SetBaseState.wbdvie);
							}
							CreateXLReport.insertResultSetInTestSteps(testName,currentTestSuite,testResultSet,xls,lastTestStepRowExecuted);
							//ErrorUtil.addVerificationFailure(new CustomException((String.format("Skipping the rest of teststeps as execution FAILED on %s for keyword %s" , lastTestStepRowExecuted, KEYWORD))));
							throw new CustomException(String.format("Skipping the rest of teststeps as execution FAILED on row number -  %s for keyword - %s" , lastTestStepRowExecuted, KEYWORD));
						}
						else if(rowResult.contains(CreatePropertiesObjects.CONFIG.getProperty("FAIL_VALUE"))){
							if(data.get("BROWSER").equals(CreatePropertiesObjects.CONFIG.getProperty("FIREFOX"))){
								ts.takeScreenShot(currentTestSuite+"_"+testName +"_rownum_" +rowNum +"_"+ KEYWORD + "_"+formatteddate, SetBaseState.driverff);
							}
							else if(data.get("BROWSER").equals(CreatePropertiesObjects.CONFIG.getProperty("CHROME"))){
								ts.takeScreenShot(currentTestSuite+"_"+testName +"_rownum_" +rowNum +"_"+ KEYWORD + "_"+formatteddate, SetBaseState.wbdvch);
							}
							else if(data.get("BROWSER").equals(CreatePropertiesObjects.CONFIG.getProperty("IE"))){
								ts.takeScreenShot(currentTestSuite+"_"+testName +"_rownum_" +rowNum +"_"+ KEYWORD + "_"+formatteddate, SetBaseState.wbdvie);
							}
						}
					}
					lastTestStepRowExecuted = rowNum;
				}
				
			}
		
		if(lastTestStepRowExecuted < startFromRow){
			Logging.log("There are no test steps for the test case " + testName);
			ErrorUtil.addVerificationFailure(new CustomException("There are no test steps for the test case " + testName));
			throw new CustomException("There are no test steps for the test case " + testName);
		}
		CreateXLReport.insertResultSetInTestSteps(testName,currentTestSuite,testResultSet,xls,lastTestStepRowExecuted);
		
	}
	

}
