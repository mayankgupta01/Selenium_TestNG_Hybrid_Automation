package com.generatexlsreport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import com.config.CreatePropertiesObjects;
import com.customexception.CustomException;
import com.listeners.ErrorUtil;
import com.logs.Logging;
import com.readxls.ExcelWriter;
import com.readxls.XLReader;

public class CreateXLReport {
	
	public static int testCaseDataSetNumber=0;
	public static Hashtable<String, Integer> newResultSheetCreatedforTestSuite = new Hashtable<String, Integer>();
	public static Date date = null;
	public static  SimpleDateFormat sdf = null;;
	public static  String formattedDate =null;;
	public static int resultSheetLastPopulatedRow = 0;
	
	public static void insertResultSetInTestSteps(String testName,String currentTestSuite, ArrayList<String> resultSet,XLReader xl, int lastRowExecuted){
		try{
			int resultSetIndex = resultSet.size();
			ExcelWriter ex = new ExcelWriter(CreatePropertiesObjects.XL.getProperty("EXCEL_FILE_PATH")+currentTestSuite+".xlsx");
			
			if(date==null){
				date = new Date();
				sdf = new SimpleDateFormat("MM-dd-yyyy h-mm-ss a");
				formattedDate = sdf.format(date);
			}

			if(resultSetIndex > 0){
				if(newResultSheetCreatedforTestSuite.get(currentTestSuite) == null){
					newResultSheetCreatedforTestSuite.put(currentTestSuite, 0);
				}
		try{
			if(newResultSheetCreatedforTestSuite.get(currentTestSuite)==0){
				//System.out.println(formattedDate); // 12/01/2011 4:48:16 PM
				ex.addSheet("RESULT-" + formattedDate);
				Logging.log(String.format("New Result Sheet created in the %s excel with the name RESULT-%s",currentTestSuite,formattedDate));
				newResultSheetCreatedforTestSuite.put(currentTestSuite, 1);
			}
		}catch(Exception e){
			ErrorUtil.addVerificationFailure(e);
			Logging.log("The excel file " +currentTestSuite+".xlsx "+ "maybe open, please close the excel file and re-run the test");
			throw new CustomException("The excel file " +currentTestSuite+".xlsx "+ "maybe open, please close the excel file and re-run the test");
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
						try{
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
						}catch(Exception e){
							ErrorUtil.addVerificationFailure(e);
							Logging.log("The excel file " +currentTestSuite+".xlsx "+ "maybe open, please close the excel file and re-run the test");
							throw new CustomException("The excel file " +currentTestSuite+".xlsx "+ "maybe open, please close the excel file and re-run the test");
						}
						
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
							try{
								ex.setCellDataColNo("RESULT-" + formattedDate, 3+testCaseDataSetNumber, rowNum, resultSet.get(resultSetIndex - 1));
								Logging.log("Setting cell data in RESULT" + testCaseDataSetNumber + ",row num = " + rowNum + " value being inserted is" + resultSet.get(resultSetIndex - 1) );
							}catch(Exception e){
								ErrorUtil.addVerificationFailure(e);
								Logging.log("The excel file " +currentTestSuite+".xlsx "+ "maybe open, please close the excel file and re-run the test");
								throw new CustomException("The excel file " +currentTestSuite+".xlsx "+ "maybe open, please close the excel file and re-run the test");
								//throw new CustomException("Result set size is 0, there is an uncaught exception in the keyword executed");
							}
							
							//ex.setCellData("RESULT-" + formattedDate, "RESULT"+testCaseDataSetNumber, rowNum, resultSet.get(resultSetIndex - 1));
							
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

	public static void insertResultSetInTestStepsAsSkipped(String testName,String currentTestSuite,  XLReader xl) throws CustomException{
		
			ExcelWriter ex = new ExcelWriter(CreatePropertiesObjects.XL.getProperty("EXCEL_FILE_PATH")+currentTestSuite+".xlsx");
			
			if(date==null){
				date = new Date();
				sdf = new SimpleDateFormat("MM-dd-yyyy h-mm-ss a");
				formattedDate = sdf.format(date);
			}
			if(newResultSheetCreatedforTestSuite.get(currentTestSuite) == null){
				newResultSheetCreatedforTestSuite.put(currentTestSuite, 0);
			}
	try{
		if(newResultSheetCreatedforTestSuite.get(currentTestSuite)==0){
			//System.out.println(formattedDate); // 12/01/2011 4:48:16 PM
			ex.addSheet("RESULT-" + formattedDate);
			Logging.log(String.format("New Result Sheet created in the %s excel with the name RESULT-%s",currentTestSuite,formattedDate));
			newResultSheetCreatedforTestSuite.put(currentTestSuite, 1);
		}
	}catch(Exception e){
		ErrorUtil.addVerificationFailure(e);
		Logging.log("The excel file " +currentTestSuite+".xlsx "+ "maybe open, please close the excel file and re-run the test");
		throw new CustomException("The excel file " +currentTestSuite+".xlsx "+ "maybe open, please close the excel file and re-run the test");
	}
			if(ex.getCellData("RESULT-" + formattedDate, 0, 1).equals("")){
				String TCIDCol = xl.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), 0, 1);
				String KEYWORDCol = xl.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), 2, 1);
				String OBJECTCol = xl.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), 3, 1);
				String DATACol = xl.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), 4, 1);
				Logging.log("The result sheet name is RESULT-" +formattedDate);
				if(ex.isSheetExist("RESULT-" + formattedDate)){
					
						Logging.log("The Result sheet exists - going inside the if condition Line 39 CreateXLReport");
						ex.setCellDataColNo("RESULT-" + formattedDate, 0, 1, TCIDCol);
						Logging.log(String.format("Inserting TCID value in resultsheet value = %s", TCIDCol));
						
						ex.setCellDataColNo("RESULT-" + formattedDate, 1, 1, KEYWORDCol);
						Logging.log(String.format("Inserting KEYWORD value in resultsheet value = %s", KEYWORDCol));
						
						ex.setCellDataColNo("RESULT-" + formattedDate, 2, 1, OBJECTCol);
						Logging.log(String.format("Inserting OBJECT value in resultsheet value = %s", OBJECTCol));
						
						ex.setCellDataColNo("RESULT-" + formattedDate, 3, 1, DATACol);
						Logging.log(String.format("Inserting DATA value in resultsheet value = %s", DATACol));
				}
			}
			
			for(int rowNum=resultSheetLastPopulatedRow+1;rowNum <=xl.getRowCount(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME")); rowNum++){
				if(xl.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), 0, rowNum).equals(testName)){
					String TCID = xl.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), 0, rowNum);
					String KEYWORD = xl.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), 2, rowNum);
					String OBJECT = xl.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), 3, rowNum);
					String DATA = xl.getCellData(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"), 4, rowNum);
					Logging.log("The result sheet name is RESULT-" +formattedDate);
					if(ex.isSheetExist("RESULT-" + formattedDate)){
						try{
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
					
						}catch(Exception e){
							ErrorUtil.addVerificationFailure(e);
							Logging.log("The excel file " +currentTestSuite+".xlsx "+ "maybe open, please close the excel file and re-run the test");
							throw new CustomException("The excel file " +currentTestSuite+".xlsx "+ "maybe open, please close the excel file and re-run the test");
						}
					}
				}
			}
			
			
			
			Logging.log(String.format("Going to check the existence of Result column with testCaseDataSetNumber suffix , The testCaseDataSetNumber value is %s", testCaseDataSetNumber));
			Logging.log(String.format("Get cell data for RESULT column returns value %s", xl.getCellData("RESULT-" + formattedDate, 3+testCaseDataSetNumber, 1)));
			Logging.log(String.format("IS THE COLUMN RESULT"+testCaseDataSetNumber+"NOT PRESENT?  %s", ex.getCellData("RESULT-" + formattedDate, 3+testCaseDataSetNumber, 1).equals("")));
			if(ex.getCellData("RESULT-" + formattedDate, 3+testCaseDataSetNumber, 1).equals("")){
				ex.addColumn("RESULT-" + formattedDate, "RESULT"+testCaseDataSetNumber);
				Logging.log("Adding new result column  in result sheet - " + "RESULT-" +formattedDate+ "Column name = RESULT" + testCaseDataSetNumber);
			 
		    }
			
			for(int rowNum=2; rowNum <= xl.getRowCount(CreatePropertiesObjects.XL.getProperty("TEST_SUITE_TESTSTEPS_SHEET_NAME"));rowNum++){			
				if(ex.getCellData("RESULT-" + formattedDate, "TCID", rowNum).equals(testName))
					ex.setCellDataColNo("RESULT-" + formattedDate, 3+testCaseDataSetNumber, rowNum, "SKIPPED");
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

