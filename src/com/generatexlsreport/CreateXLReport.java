package com.generatexlsreport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.config.CreatePropertiesObjects;
import com.logs.Logging;
import com.readxls.ExcelWriter;
import com.readxls.XLReader;

public class CreateXLReport {
	
	public static int testCaseDataSetNumber=0;
	public static int newResultSheetCreated = 0;
	public static Date date = null;
	public static  SimpleDateFormat sdf = null;;
	public static  String formattedDate =null;;
	public static int resultSheetLastPopulatedRow = 0;
	
	public static void insertResultSetInTestSteps(String testName,String currentTestSuite, ArrayList<String> resultSet,XLReader xl, int lastRowExecuted){
		try{
			int resultSetIndex = resultSet.size();
			ExcelWriter ex = new ExcelWriter(System.getProperty("user.dir")+"\\src\\com\\xlfiles\\"+currentTestSuite+".xlsx");
			
			if(date==null){
				date = new Date();
				sdf = new SimpleDateFormat("MM-dd-yyyy h-mm-ss a");
				formattedDate = sdf.format(date);
			}

			if(resultSetIndex > 0){
				if(newResultSheetCreated==0){
					//System.out.println(formattedDate); // 12/01/2011 4:48:16 PM
					ex.addSheet("RESULT-" + formattedDate);
					Logging.log(String.format("New Result Sheet created in the %s excel with the name RESULT-%s",currentTestSuite,formattedDate));
					newResultSheetCreated = newResultSheetCreated+1;
				}
				
				// 5 columns are supposed to be present in teststeps sheet after this result columns will follow
				//System.out.println(xl.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), 5+testCaseDataSetNumber, 1));
				for(int rowNum=resultSheetLastPopulatedRow+1;rowNum <=lastRowExecuted; rowNum++){
					String TCID = xl.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), 0, rowNum);
					String KEYWORD = xl.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), 2, rowNum);
					String OBJECT = xl.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), 3, rowNum);
					String DATA = xl.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), 4, rowNum);
					Logging.log("The result sheet name is RESULT-" +formattedDate);
					if(ex.isSheetExist("RESULT-" + formattedDate)){
						Logging.log("The Result sheet exists - going inside the if condition Line 39 CreateXLReport");
						ex.setCellDataColNo("RESULT-" + formattedDate, 0, rowNum, TCID);
						Logging.log(String.format("Inserting TCID value in resultsheet value = %s", TCID));
						
						ex.setCellDataColNo("RESULT-" + formattedDate, 1, rowNum, KEYWORD);
						Logging.log(String.format("Inserting KEYWORD value in resultsheet value = %s", KEYWORD));
						
						ex.setCellDataColNo("RESULT-" + formattedDate, 2, rowNum, OBJECT);
						Logging.log(String.format("Inserting OBJECT value in resultsheet value = %s", OBJECT));
						
						ex.setCellDataColNo("RESULT-" + formattedDate, 3, rowNum, DATA);
						Logging.log(String.format("Inserting DATA value in resultsheet value = %s", DATA));
						resultSheetLastPopulatedRow = rowNum;
					}
					
				}
				Logging.log(String.format("Going to check the existence of Result column with testCaseDataSetNumber suffix , The testCaseDataSetNumber value is %s", testCaseDataSetNumber));
				Logging.log(String.format("Get cell data for RESULT column returns value %s", xl.getCellData("RESULT-" + formattedDate, 3+testCaseDataSetNumber, 1)));
				Logging.log(String.format("IS THE COLUMN RESULT"+testCaseDataSetNumber+"NOT PRESENT?  %s", ex.getCellData("RESULT-" + formattedDate, 3+testCaseDataSetNumber, 1).equals("")));
				if(ex.getCellData("RESULT-" + formattedDate, 3+testCaseDataSetNumber, 1).equals("")){
					ex.addColumn("RESULT-" + formattedDate, "RESULT"+testCaseDataSetNumber);
					Logging.log("Adding new result column  in result sheet - " + "RESULT-" +formattedDate+ "Column name = RESULT" + testCaseDataSetNumber);
				 
			    }
					
				Logging.log(String.format("The last row in the TestSteps sheet which has been executed is %s", lastRowExecuted));
				if(ex.isSheetExist("RESULT-" + formattedDate)){
					for(int rowNum=lastRowExecuted;rowNum >1; rowNum--){
						if(xl.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), "TCID", rowNum).equals(testName) && resultSetIndex >= 0 ){
							Logging.log("Result set has " + resultSetIndex + "elements");
							ex.setCellDataColNo("RESULT-" + formattedDate, 3+testCaseDataSetNumber, rowNum, resultSet.get(resultSetIndex - 1));
							//ex.setCellData("RESULT-" + formattedDate, "RESULT"+testCaseDataSetNumber, rowNum, resultSet.get(resultSetIndex - 1));
							Logging.log("Setting cell data in RESULT" + testCaseDataSetNumber + ",row num = " + rowNum + " value being inserted is" + resultSet.get(resultSetIndex - 1) );
							resultSetIndex--;
						}
							
					}
				}

				
			}
		}catch(Exception e){
			Logging.log("Error occured while inserting result in TestSteps sheet for TestCase = " + testName);
			e.printStackTrace();
		}
		
		
	}

	public static void insertResultSetInTestStepsAsSkipped(String testName,String currentTestSuite,  XLReader xl){
		try{
			ExcelWriter ex = new ExcelWriter(System.getProperty("user.dir")+"\\src\\com\\xlfiles\\"+currentTestSuite+".xlsx");
			
			if(date==null){
				date = new Date();
				sdf = new SimpleDateFormat("MM-dd-yyyy h-mm-ss a");
				formattedDate = sdf.format(date);
			}
			
			for(int rowNum=resultSheetLastPopulatedRow+1;rowNum <=xl.getRowCount(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME")); rowNum++){
				if(xl.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), 0, rowNum).equals(testName)){
					String TCID = xl.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), 0, rowNum);
					String KEYWORD = xl.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), 2, rowNum);
					String OBJECT = xl.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), 3, rowNum);
					String DATA = xl.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), 4, rowNum);
					Logging.log("The result sheet name is RESULT-" +formattedDate);
					if(ex.isSheetExist("RESULT-" + formattedDate)){
						Logging.log("The Result sheet exists - going inside the if condition Line 39 CreateXLReport");
						ex.setCellDataColNo("RESULT-" + formattedDate, 0, rowNum, TCID);
						Logging.log(String.format("Inserting TCID value in resultsheet value = %s", TCID));
						
						ex.setCellDataColNo("RESULT-" + formattedDate, 1, rowNum, KEYWORD);
						Logging.log(String.format("Inserting KEYWORD value in resultsheet value = %s", KEYWORD));
						
						ex.setCellDataColNo("RESULT-" + formattedDate, 2, rowNum, OBJECT);
						Logging.log(String.format("Inserting OBJECT value in resultsheet value = %s", OBJECT));
						
						ex.setCellDataColNo("RESULT-" + formattedDate, 3, rowNum, DATA);
						Logging.log(String.format("Inserting DATA value in resultsheet value = %s", DATA));
						resultSheetLastPopulatedRow = rowNum;
					}
				}
			}
			
			
			
			Logging.log(String.format("Going to check the existence of Result column with testCaseDataSetNumber suffix , The testCaseDataSetNumber value is %s", testCaseDataSetNumber));
			Logging.log(String.format("Get cell data for RESULT column returns value %s", xl.getCellData("RESULT-" + formattedDate, 3+testCaseDataSetNumber, 1)));
			Logging.log(String.format("IS THE COLUMN RESULT"+testCaseDataSetNumber+"NOT PRESENT?  %s", xl.getCellData("RESULT-" + formattedDate, 3+testCaseDataSetNumber, 1).equals("")));
			if(ex.getCellData("RESULT-" + formattedDate, 3+testCaseDataSetNumber, 1).equals("")){
				ex.addColumn("RESULT-" + formattedDate, "RESULT"+testCaseDataSetNumber);
				Logging.log("Adding new result column  in result sheet - " + "RESULT-" +formattedDate+ "Column name = RESULT" + testCaseDataSetNumber);
			 
		    }
			
			for(int rowNum=2; rowNum <= xl.getRowCount(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"));rowNum++){			
				if(ex.getCellData("RESULT-" + formattedDate, "TCID", rowNum).equals(testName))
					ex.setCellDataColNo("RESULT-" + formattedDate, 3+testCaseDataSetNumber, rowNum, "SKIPPED");
			}
		}catch(Exception e){
			Logging.log("Error occured while inserting result in TestSteps sheet for TestCase = " + testName);
			e.printStackTrace();
		}
				
	}
	
	
	public static void deletePastResultSet(int lastRowExecuted,XLReader xl){
		if (lastRowExecuted==0){
			for(int i=1; i <= xl.getColumnCount(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"));i++){
				if(xl.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), i, 1).startsWith("RESU"))
					xl.removeColumn(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), i);
			}
			
		}
	}
}

